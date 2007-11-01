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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetLegendGraphicRequestHandler extends WMSRequestHandler {
    
    private static final Log log = LogFactory.getLog(GetLegendGraphicRequestHandler.class);
    
    // <editor-fold defaultstate="" desc="default GetLegendGraphicRequestHandler() constructor.">
    public GetLegendGraphicRequestHandler() {}
    // </editor-fold>
    
    // TODO: onderstaande getRequest methode werkt nog niet naar behoren. De implementatie kan misschien
    // volledig verwijderd worden omdat deze niet de functie uitvoert die ze uit zou moeten voeren.
    
    /** Processes the parameters and creates the specified urls from the given parameters.
     * Each url will be used to recieve the data from the ServiceProvider this url is refering to.
     * @param parameters Map parameters
     * @return byte[]
     *
     * @throws IOException
     */
    // <editor-fold defaultstate="" desc="getRequest(Map parameters) method.">
    public void getRequest(DataWrapper dw, User user) throws IOException, Exception {
        /* 
         * Initialize some variables
         * And immediatly set the right output format (also for errors) because if an error occurs
         * with the GetMap functionality before the outputformat is set then the standard output
         * format would be used.
         */
        this.user = user;
        this.url = user.getPersonalURL();
        Integer orgId = user.getOrganization().getId();
        
        String format = dw.getOgcrequest().getParameter(WMS_PARAM_FORMAT);
        dw.setContentType(format);
        
        String inimageType = null;
        
        if (dw.getOgcrequest().containsParameter(WMS_PARAM_EXCEPTION_FORMAT)) {
            inimageType = format;
        }
        dw.setErrorContentType(inimageType);
        
        String [] layers = dw.getOgcrequest().getParameter(WMS_PARAM_LAYERS).split(",");
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
        
        StringBuffer serviceProviderUrl = new StringBuffer();
        serviceProviderUrl.append((String)spInfo.get("spUrl"));        
        serviceProviderUrl.append(WMS_VERSION);
        serviceProviderUrl.append("=");
        serviceProviderUrl.append(dw.getOgcrequest().getParameter(WMS_VERSION));
        serviceProviderUrl.append("&");
        serviceProviderUrl.append(WMS_REQUEST);
        serviceProviderUrl.append("=");
        serviceProviderUrl.append(WMS_REQUEST_GetLegendGraphic);
        serviceProviderUrl.append("&");
        serviceProviderUrl.append(WMS_PARAM_LAYER);
        serviceProviderUrl.append("=");
        serviceProviderUrl.append((StringBuffer)spInfo.get("layersList"));

        serviceProviderUrl.append("&");
        serviceProviderUrl.append(WMS_PARAM_STYLE);
        serviceProviderUrl.append("=");
        serviceProviderUrl.append(dw.getOgcrequest().getParameter(WMS_PARAM_STYLE));

        String value = dw.getOgcrequest().getParameter(WMS_PARAM_FEATURETYPE);
        if(value != null) {
            serviceProviderUrl.append("&");
            serviceProviderUrl.append(WMS_PARAM_FEATURETYPE);
            serviceProviderUrl.append("=");
            serviceProviderUrl.append(value);
        }
        value = dw.getOgcrequest().getParameter(WMS_PARAM_RULE);
        if(value != null) {
            serviceProviderUrl.append("&");
            serviceProviderUrl.append(WMS_PARAM_RULE);
            serviceProviderUrl.append("=");
            serviceProviderUrl.append(value);
        }
        value = dw.getOgcrequest().getParameter(WMS_PARAM_SCALE);
        if(value != null) {
            serviceProviderUrl.append("&");
            serviceProviderUrl.append(WMS_PARAM_SCALE);
            serviceProviderUrl.append("=");
            serviceProviderUrl.append(value);
        }
        value = dw.getOgcrequest().getParameter(WMS_PARAM_SLD);
        if(value != null) {
            serviceProviderUrl.append("&");
            serviceProviderUrl.append(WMS_PARAM_SLD);
            serviceProviderUrl.append("=");
            serviceProviderUrl.append(value);
        }
        value = dw.getOgcrequest().getParameter(WMS_PARAM_SLD_BODY);
        if(value != null) {
            serviceProviderUrl.append("&");
            serviceProviderUrl.append(WMS_PARAM_SLD_BODY);
            serviceProviderUrl.append("=");
            serviceProviderUrl.append(value);
        }
        value = dw.getOgcrequest().getParameter(WMS_PARAM_FORMAT);
        if(value != null) {
            serviceProviderUrl.append("&");
            serviceProviderUrl.append(WMS_PARAM_FORMAT);
            serviceProviderUrl.append("=");
            serviceProviderUrl.append(value);
        }
        value = dw.getOgcrequest().getParameter(WMS_PARAM_WIDTH);
        if(value != null) {
            serviceProviderUrl.append("&");
            serviceProviderUrl.append(WMS_PARAM_WIDTH);
            serviceProviderUrl.append("=");
            serviceProviderUrl.append(value);
        }
        value = dw.getOgcrequest().getParameter(WMS_PARAM_HEIGHT);
        if(value != null) {
            serviceProviderUrl.append("&");
            serviceProviderUrl.append(WMS_PARAM_HEIGHT);
            serviceProviderUrl.append("=");
            serviceProviderUrl.append(value);
        }
        
        ArrayList urls = new ArrayList();
        urls.add(serviceProviderUrl.toString());
        getOnlineData(dw, urls, false, WMS_REQUEST_GetLegendGraphic);
    }
    // </editor-fold>

}