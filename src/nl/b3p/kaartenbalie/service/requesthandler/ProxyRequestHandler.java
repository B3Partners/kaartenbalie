/**
 * @(#)GetLegendGraphicRequestHandler.java
 * @author N. de Goeij
 * @version 1.00 2006/12/13
 *
 * Purpose: *
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.service.requesthandler;

import java.io.IOException;
import java.util.ArrayList;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.reporting.domain.requests.ProxyRequest;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.KBCrypter;
import nl.b3p.ogc.utils.OGCRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ProxyRequestHandler extends WMSRequestHandler {
    
    private static final Log log = LogFactory.getLog(ProxyRequestHandler.class);
    
    public ProxyRequestHandler() {}
    
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
        String encodedUrl = ogcrequest.getParameter(KBConfiguration.KB_PROXY_URL);
        if (encodedUrl==null || encodedUrl.length()==0) {
            log.error(KBConfiguration.KB_PROXY_EXECPTION);
            throw new Exception(KBConfiguration.KB_PROXY_EXECPTION);
        }
        
        String purl = KBCrypter.decryptText(encodedUrl);
        ProxyRequest proxyWrapper = new ProxyRequest();
        proxyWrapper.setProviderRequestURI(purl);
        
        ArrayList urlWrapper = new ArrayList();
        urlWrapper.add(proxyWrapper);
        getOnlineData(dw, urlWrapper, false, KBConfiguration.KB_PROXY);
    }
    
}