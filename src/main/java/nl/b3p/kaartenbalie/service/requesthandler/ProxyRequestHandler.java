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
import java.util.*;

import nl.b3p.commons.services.B3PCredentials;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.monitoring.ServiceProviderRequest;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.KBCrypter;
import nl.b3p.ogc.utils.LayerSummary;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.ogc.utils.SpLayerSummary;
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
        Integer[] orgIds = user.getOrganizationIds();
        OGCRequest ogcrequest = dw.getOgcrequest();
        String spInUrl = ogcrequest.getServiceProviderName();
        
        String[] la = ogcrequest.getParameter(OGCConstants.WMS_PARAM_LAYER).split(",");
        List<LayerSummary> lsl = LayerSummary.createLayerSummaryList(Arrays.asList(la), spInUrl, (spInUrl==null)); 

        if (lsl==null || lsl.size() != 1) {
            log.error("Only one layer for proxy.");
            throw new Exception(KBConfiguration.KB_PROXY_EXECPTION);
        }

        List spUrls = getServiceProviderURLS(lsl, orgIds, false, dw, false);
        if (spUrls == null || spUrls.size() != 1) {
            log.error("Only one layer for proxy.");
            throw new Exception(KBConfiguration.KB_PROXY_EXECPTION);
        }

        SpLayerSummary spInfo = (SpLayerSummary) spUrls.get(0);
        if (spInfo == null) {
            log.error("No urls found!");
            throw new Exception(KBConfiguration.KB_PROXY_EXECPTION);
        }
           
        B3PCredentials credentials = new B3PCredentials();
        credentials.setUserName(spInfo.getUsername());
        credentials.setPassword(spInfo.getPassword());
        
        
        String decodedUrl = ogcrequest.getParameter(OGCConstants.PROXY_URL);
        HashMap<String, String> extraParameter = ogcrequest.getNonOGCParameters();
        extraParameter.remove(OGCConstants.PROXY_URL);
        if (decodedUrl == null || decodedUrl.length() == 0) {
            log.error(KBConfiguration.KB_PROXY_EXECPTION);
            throw new Exception(KBConfiguration.KB_PROXY_EXECPTION);
        }
        String encodedUrl = URLEncoder.encode(decodedUrl);
        String purl = KBCrypter.decryptText(encodedUrl);

        OGCRequest proxyrequest = new OGCRequest(purl);
        for(Map.Entry<String, String> m: extraParameter.entrySet()) {
            proxyrequest.addOrReplaceParameter(m.getKey(), m.getValue());
        }
        String proxyUrl = proxyrequest.getUrl();
        
        ServiceProviderRequest proxyWrapper = new ServiceProviderRequest();
        proxyWrapper.setProviderRequestURI(proxyUrl);
        proxyWrapper.setServiceName(dw.getOgcrequest().getServiceProviderName());
        proxyWrapper.setCredentials(credentials);
        
        ArrayList urlWrapper = new ArrayList();
        urlWrapper.add(proxyWrapper);
        getOnlineData(dw, urlWrapper, false, KBConfiguration.KB_PROXY);

    }
}
