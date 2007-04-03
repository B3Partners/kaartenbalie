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
import nl.b3p.kaartenbalie.core.server.ServiceDomainResource;
import nl.b3p.kaartenbalie.core.server.ServiceProvider;
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
        
        List tempSP = getServiceProviders(false);
        if (tempSP==null)
            return;
        
        ArrayList urls = new ArrayList();
        
        Iterator it = tempSP.iterator();
        while (it.hasNext()) {
            ServiceProvider s = (ServiceProvider)it.next();
            StringBuffer spUrl = null;
            
            //Lets first check if this ServiceProvider can provide us the asked layers
            //otherwise it is not necessary at all to look further and ask for a lot of resources
            String[] layer = (String[])parameters.get(WMS_PARAM_LAYERS);
            String spls = calcFormattedLayers(s, layer);
            if (spls==null)
                continue;
            
            //Since some or all layers were found, we need to build up a string to become an url
            //Find the beginning of the url which thise layers belong to
            spUrl = calcRequestUrl(s, WMS_REQUEST_GetLegendGraphic);
            if (spUrl==null)
                continue;
            
            // toevoegen van andere parameters
            spUrl.append(WMS_VERSION);
            spUrl.append("=");
            spUrl.append((String)parameters.get(WMS_VERSION));
            spUrl.append("&");
            spUrl.append(WMS_REQUEST);
            spUrl.append("=");
            spUrl.append(WMS_REQUEST_GetLegendGraphic);
            spUrl.append("&");
            spUrl.append(WMS_PARAM_LAYERS);
            spUrl.append("=");
            spUrl.append(spls);
            
            spUrl.append("&");
            spUrl.append(WMS_PARAM_STYLE);
            spUrl.append("=");
            spUrl.append((String)parameters.get(WMS_PARAM_STYLE));
            
            String value = (String)parameters.get(WMS_PARAM_FEATURETYPE);
            if(value != null) {
                spUrl.append("&");
                spUrl.append(WMS_PARAM_FEATURETYPE);
                spUrl.append("=");
                spUrl.append(value);
            }
            value = (String)parameters.get(WMS_PARAM_RULE);
            if(value != null) {
                spUrl.append("&");
                spUrl.append(WMS_PARAM_RULE);
                spUrl.append("=");
                spUrl.append(value);
            }
            value = (String)parameters.get(WMS_PARAM_SCALE);
            if(value != null) {
                spUrl.append("&");
                spUrl.append(WMS_PARAM_SCALE);
                spUrl.append("=");
                spUrl.append(value);
            }
            value = (String)parameters.get(WMS_PARAM_SLD);
            if(value != null) {
                spUrl.append("&");
                spUrl.append(WMS_PARAM_SLD);
                spUrl.append("=");
                spUrl.append(value);
            }
            value = (String)parameters.get(WMS_PARAM_SLD_BODY);
            if(value != null) {
                spUrl.append("&");
                spUrl.append(WMS_PARAM_SLD_BODY);
                spUrl.append("=");
                spUrl.append(value);
            }
            value = (String)parameters.get(WMS_PARAM_FORMAT);
            if(value != null) {
                spUrl.append("&");
                spUrl.append(WMS_PARAM_FORMAT);
                spUrl.append("=");
                spUrl.append(value);
            }
            value = (String)parameters.get(WMS_PARAM_WIDTH);
            if(value != null) {
                spUrl.append("&");
                spUrl.append(WMS_PARAM_WIDTH);
                spUrl.append("=");
                spUrl.append(value);
            }
            value = (String)parameters.get(WMS_PARAM_HEIGHT);
            if(value != null) {
                spUrl.append("&");
                spUrl.append(WMS_PARAM_HEIGHT);
                spUrl.append("=");
                spUrl.append(value);
            }
        }
        
        StringBuffer [] url = null;
        ArrayList tempList = new ArrayList();
        getOnlineData(dw, tempList, false, WMS_REQUEST_GetLegendGraphic);
    }
    // </editor-fold>
}