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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nl.b3p.wms.capabilities.ServiceProvider;
import nl.b3p.kaartenbalie.core.server.User;

public class GetLegendGraphicRequestHandler extends WMSRequestHandler {
    
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
    public void getRequest(DataWrapper dw, Map parameters) throws IOException, Exception {
        
        user = (User) parameters.get(KB_USER);
        url = (String) parameters.get(KB_PERSONAL_URL);
        
        String format = null;
        dw.setHeader("Content-Disposition", "inline; filename=\"GetLegendGraphic.xml\";");
        
        try {
            format = (String)((String[])parameters.get(WMS_PARAM_INFO_FORMAT))[0];
        } catch (Exception e) {
            format = "image/png";
        }
        dw.setContentType(format);
        
        //First check if the given URL contains really only one layer
        //Otherwise we need to throw an error.
        String layerParams = (String)parameters.get(WMS_PARAM_LAYER);
        String [] layers = layerParams.split(",");
        if(layers.length != 1) {
            throw new Exception(LEGENDGRAPHIC_EXCEPTION);
        }
        
        //Now get a list with service providers in which we can search for the specified layer        
        List serviceProviders = getServiceProviders(false);
        if (serviceProviders==null)
            return;
        
        StringBuffer serviceProviderUrl = null;
        String serviceProviderLayer = null;
        Iterator serviceProviderIterator = serviceProviders.iterator();
        while (serviceProviderIterator.hasNext()) {
            ServiceProvider serviceProvider = (ServiceProvider)serviceProviderIterator.next();
            Set serviceProviderLayers = serviceProvider.getLayers();
            if(serviceProviderLayers == null)
                continue;
            serviceProviderLayer = findLayer(serviceProviderLayers, layers[0], serviceProvider, false);
            if(serviceProviderLayer != null) {
                serviceProviderUrl = calcRequestUrl(serviceProvider, WMS_REQUEST_GetLegendGraphic);
                break;
            }
        }
        
        if(serviceProviderLayer == null && serviceProviderUrl == null) {
            throw new Exception(LEGENDGRAPHIC_EXCEPTION);
        }
        
        serviceProviderUrl.append(WMS_VERSION);
        serviceProviderUrl.append("=");
        serviceProviderUrl.append((String)parameters.get(WMS_VERSION));
        serviceProviderUrl.append("&");
        serviceProviderUrl.append(WMS_REQUEST);
        serviceProviderUrl.append("=");
        serviceProviderUrl.append(WMS_REQUEST_GetLegendGraphic);
        serviceProviderUrl.append("&");
        serviceProviderUrl.append(WMS_PARAM_LAYER);
        serviceProviderUrl.append("=");
        serviceProviderUrl.append(serviceProviderLayer);

        serviceProviderUrl.append("&");
        serviceProviderUrl.append(WMS_PARAM_STYLE);
        serviceProviderUrl.append("=");
        serviceProviderUrl.append((String)parameters.get(WMS_PARAM_STYLE));

        String value = (String)parameters.get(WMS_PARAM_FEATURETYPE);
        if(value != null) {
            serviceProviderUrl.append("&");
            serviceProviderUrl.append(WMS_PARAM_FEATURETYPE);
            serviceProviderUrl.append("=");
            serviceProviderUrl.append(value);
        }
        value = (String)parameters.get(WMS_PARAM_RULE);
        if(value != null) {
            serviceProviderUrl.append("&");
            serviceProviderUrl.append(WMS_PARAM_RULE);
            serviceProviderUrl.append("=");
            serviceProviderUrl.append(value);
        }
        value = (String)parameters.get(WMS_PARAM_SCALE);
        if(value != null) {
            serviceProviderUrl.append("&");
            serviceProviderUrl.append(WMS_PARAM_SCALE);
            serviceProviderUrl.append("=");
            serviceProviderUrl.append(value);
        }
        value = (String)parameters.get(WMS_PARAM_SLD);
        if(value != null) {
            serviceProviderUrl.append("&");
            serviceProviderUrl.append(WMS_PARAM_SLD);
            serviceProviderUrl.append("=");
            serviceProviderUrl.append(value);
        }
        value = (String)parameters.get(WMS_PARAM_SLD_BODY);
        if(value != null) {
            serviceProviderUrl.append("&");
            serviceProviderUrl.append(WMS_PARAM_SLD_BODY);
            serviceProviderUrl.append("=");
            serviceProviderUrl.append(value);
        }
        value = (String)parameters.get(WMS_PARAM_FORMAT);
        if(value != null) {
            serviceProviderUrl.append("&");
            serviceProviderUrl.append(WMS_PARAM_FORMAT);
            serviceProviderUrl.append("=");
            serviceProviderUrl.append(value);
        }
        value = (String)parameters.get(WMS_PARAM_WIDTH);
        if(value != null) {
            serviceProviderUrl.append("&");
            serviceProviderUrl.append(WMS_PARAM_WIDTH);
            serviceProviderUrl.append("=");
            serviceProviderUrl.append(value);
        }
        value = (String)parameters.get(WMS_PARAM_HEIGHT);
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