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
import java.util.List;
import java.util.Map;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.reporting.domain.requests.WMSGetLegendGraphicRequest;
import nl.b3p.ogc.utils.OGCRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetLegendGraphicRequestHandler extends WMSRequestHandler {
    
    private static final Log log = LogFactory.getLog(GetLegendGraphicRequestHandler.class);
    
    // <editor-fold defaultstate="" desc="default GetLegendGraphicRequestHandler() constructor.">
    public GetLegendGraphicRequestHandler() {}
    // </editor-fold>
    
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
    // <editor-fold defaultstate="" desc="getRequest(DataWrapper dw, User user) method.">
    public void getRequest(DataWrapper dw, User user) throws IOException, Exception {
        this.user       = user;
        this.url        = user.getPersonalURL();
        Integer orgId   = user.getOrganization().getId();
        OGCRequest ogc  = dw.getOgcrequest();
        
        String [] layers = ogc.getParameter(WMS_PARAM_LAYER).split(",");
        if(layers.length != 1) {
            log.error("Only one layer for legend graphic.");
            throw new Exception(LEGENDGRAPHIC_EXCEPTION);
        }
        
        List spUrls = getSeviceProviderURLS(layers, orgId, false);
        if(spUrls == null || spUrls.size()!=1) {
            throw new Exception(LEGENDGRAPHIC_EXCEPTION);
        }
        
        Map spInfo = (Map)spUrls.get(0);
        if(spInfo == null) {
            throw new Exception(LEGENDGRAPHIC_EXCEPTION);
        }
        
        ArrayList urlWrapper = new ArrayList();
        WMSGetLegendGraphicRequest lgrWrapper = new WMSGetLegendGraphicRequest();
        Integer serviceProviderId = (Integer)spInfo.get("spId");
        lgrWrapper.setServiceProviderId(serviceProviderId);
        StringBuffer url = new StringBuffer();
        url.append((String)spInfo.get("spUrl"));
        String [] params = ogc.getParametersArray();
        for (int i = 0; i < params.length; i++) {
            String [] keyValuePair = params[i].split("=");
            if (keyValuePair[0].equalsIgnoreCase(WMS_PARAM_LAYER)) {
                url.append(WMS_PARAM_LAYER);
                url.append("=");
                url.append(spInfo.get("layersList"));
                url.append("&");
            } else {
                url.append(params[i]);
                url.append("&");
            }
        }
        lgrWrapper.setProviderRequestURI(url.toString());
        urlWrapper.add(lgrWrapper);
        getOnlineData(dw, urlWrapper, false, WMS_REQUEST_GetLegendGraphic);
    }
    // </editor-fold>
}