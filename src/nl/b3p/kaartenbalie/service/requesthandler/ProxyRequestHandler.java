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
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.monitoring.ServiceProviderRequest;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.KBCrypter;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
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
     * @return byte[]
     *
     * @throws Exception
     * @throws IOException
     */
    public void getRequest(DataWrapper dw, User user) throws IOException, Exception {
        OGCRequest ogcrequest = dw.getOgcrequest();
        String decodedUrl = ogcrequest.getParameter(OGCConstants.PROXY_URL);
        if (decodedUrl == null || decodedUrl.length() == 0) {
            log.error(KBConfiguration.KB_PROXY_EXECPTION);
            throw new Exception(KBConfiguration.KB_PROXY_EXECPTION);
        }
        String encodedUrl = URLEncoder.encode(decodedUrl);
        StringBuilder purl = new StringBuilder(KBCrypter.decryptText(encodedUrl));

        String sld = ogcrequest.getParameter(OGCConstants.WMS_PARAM_SLD);
        if (sld != null && sld.length() > 0) {
            if (purl.indexOf("?") != purl.length() - 1 && purl.indexOf("&") != purl.length() - 1) {
                if (purl.indexOf("?") >= 0) {
                    purl.append("&");
                } else {
                    purl.append("?");
                }
            }
            purl.append(OGCConstants.WMS_PARAM_SLD);
            purl.append("=");
            purl.append(sld);
        }
        ServiceProviderRequest proxyWrapper = new ServiceProviderRequest();
        proxyWrapper.setProviderRequestURI(purl.toString());
        proxyWrapper.setServiceName(dw.getOgcrequest().getServiceProviderName());

        ArrayList urlWrapper = new ArrayList();
        urlWrapper.add(proxyWrapper);
        getOnlineData(dw, urlWrapper, false, KBConfiguration.KB_PROXY);

    }
}
