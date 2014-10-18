/*
 * B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
 * for authentication/authorization, pricing and usage reporting.
 *
 * Copyright 2006, 2007, 2008 B3Partners BV
 * 
 * This file is part of B3P Kaartenbalie.
 * 
 * B3P Kaartenbalie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * B3P Kaartenbalie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.b3p.kaartenbalie.service.requesthandler;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.monitoring.ServiceProviderRequest;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.service.servlet.ProxySLDServlet;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.KBCrypter;
import nl.b3p.ogc.utils.LayerSummary;
import nl.b3p.ogc.utils.OGCCommunication;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.wms.capabilities.Style;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ProxyRequestHandler extends WMSRequestHandler {

    private static final Log log = LogFactory.getLog(ProxyRequestHandler.class);

    public ProxyRequestHandler() {
    }

    /**
     * Processes the parameters and creates the specified urls from the given parameters.
     * Each url will be used to recieve the data from the ServiceProvider this url is refering to.
     *
     * @param dw DataWrapper which contains all information that has to be sent to the client
     * @param user User the user which invoked the request
     *
     * @throws Exception
     * @throws IOException
     */
    public void getRequest(DataWrapper dw, User user) throws IOException, Exception {
        OGCRequest ogcrequest = dw.getOgcrequest();
        String spInUrl = ogcrequest.getServiceProviderName();
        
        String decodedUrl = ogcrequest.getParameter(OGCConstants.PROXY_URL);
        if (decodedUrl == null || decodedUrl.length() == 0) {
            log.error(KBConfiguration.KB_PROXY_EXECPTION);
            throw new Exception(KBConfiguration.KB_PROXY_EXECPTION);
        }
        String encodedUrl = URLEncoder.encode(decodedUrl);
        String purl = KBCrypter.decryptText(encodedUrl);

        OGCRequest proxyrequest = new OGCRequest(purl);
        String sld = ogcrequest.getParameter(OGCConstants.WMS_PARAM_SLD);
        if (sld != null) {
            proxyrequest.addOrReplaceParameter(OGCConstants.WMS_PARAM_SLD, sld);
        }
        String style = ogcrequest.getParameter(OGCConstants.WMS_PARAM_STYLES);
        if (style != null) {
            //style naam omzetten naar SldProxy url zodat sld meegenomen wordt
            String spLayerName = ogcrequest.getParameter(OGCConstants.WMS_PARAM_LAYER);
            LayerSummary m = OGCCommunication
                    .splitLayerWithoutNsFix(spLayerName, (spInUrl==null), spInUrl, null);
            String spAbbr = m.getSpAbbr();
            String layerName = m.getLayerName();
            List<Style> stylesList = findStyles(spAbbr, layerName);
            Integer styleId = null;
            for (Style s : stylesList) {
                if (style.equalsIgnoreCase(s.getName())) {
                    styleId = s.getId();
                    break;
                }
            }
            // maak sld proxy url
            if (styleId!=null) {
                String serviceUrl = dw.getRequest().getRequestURL().toString();
                //make a new SLD url to the proxySldServlet.
                StringBuilder sldUrl= new StringBuilder();
                sldUrl.append(serviceUrl.replace("/services/", "/proxysld/"));
                sldUrl.append(sldUrl.indexOf("?") > 0 ? "&" : "?");
                sldUrl.append(ProxySLDServlet.PARAM_STYLES);
                sldUrl.append("=");
                sldUrl.append(styleId);
                //hier wordt evt bestaande sld param overschreven! 
                proxyrequest.addOrReplaceParameter(OGCConstants.WMS_PARAM_SLD, 
                        sldUrl.toString());
            }
        }
        
        String proxyUrl = proxyrequest.getUrl();
        
        ServiceProviderRequest proxyWrapper = new ServiceProviderRequest();
        proxyWrapper.setProviderRequestURI(proxyUrl);
        proxyWrapper.setServiceName(dw.getOgcrequest().getServiceProviderName());
        
        ArrayList urlWrapper = new ArrayList();
        urlWrapper.add(proxyWrapper);
        getOnlineData(dw, urlWrapper, false, KBConfiguration.KB_PROXY);

    }
    
    private List findStyles(String spAbbr, String layerName) throws Exception {
        Object identity = null;
        EntityManager em = null;
        try {
            identity = MyEMFDatabase.createEntityManager(MyEMFDatabase.MAIN_EM);
            log.debug("Getting entity manager ......");
            em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);

            return em.createQuery("select s from Style s join s.layer l "
                    + "where s.sld_part is not null and "
                    + "l.serviceProvider.abbr = :spAbbr and "
                    + "l.name = :layerName ")
                    .setParameter("spAbbr", spAbbr)
                    .setParameter("layerName", layerName)
                    .getResultList();
            
        } finally {
            log.debug("Closing entity manager .....");
            MyEMFDatabase.closeEntityManager(identity, MyEMFDatabase.MAIN_EM);
        }
    }
}
