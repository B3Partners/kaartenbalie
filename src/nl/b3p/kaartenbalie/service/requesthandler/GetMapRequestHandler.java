/**
 * @(#)GetMapRequestHandler.java
 * @author N. de Goeij
 * @version 1.00 2006/12/13
 *
 * Purpose: the function of this class is to create a list of url's which direct to the right servers that have the desired layers
 * for the WMS GetMap request.
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
import nl.b3p.kaartenbalie.core.KBConstants;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.ServiceDomainResource;
import nl.b3p.kaartenbalie.core.server.ServiceProvider;
import nl.b3p.kaartenbalie.core.server.User;

public class GetMapRequestHandler extends WMSRequestHandler implements KBConstants {
    
    // <editor-fold defaultstate="" desc="default GetMapRequestHandler() constructor.">
    public GetMapRequestHandler() {}
    // </editor-fold>
    
    /** Processes the parameters and creates the specified urls from the given parameters.
     * Each url will be used to recieve the data from the ServiceProvider this url is refering to.
     * @param parameters Map parameters
     * @return byte[]
     *
     * @throws IOException
     */
    // <editor-fold defaultstate="" desc="getRequest(Map parameters) method.">
    public DataWrapper getRequest(DataWrapper dw, Map parameters) throws IOException, Exception {
        /* Initialize some variables */
        user = (User) parameters.get(KB_USER);
        url = (String) parameters.get(KB_PERSONAL_URL);
        String previousUrl = "";
        ArrayList urls = new ArrayList();
        StringBuffer spUrl = null;
        
        /* Get a list with ServiceProviders from the database */
        List tempSP = getServiceProviders(false);
        if (tempSP == null) 
            return null;
        
        /* Split the string with layers into a String array */
        String [] layers = (((String[])parameters.get(WMS_PARAM_LAYERS))[0]).split(",");
        
        /* Go through each layer and find the ServiceProvider this layer belongs to.
         * If a ServiceProvider has been found there will be checked if the previous
         * layer belonged to the same ServiceProvider. If yes then this layer is added
         * up in the URL of the previous layer, otherwise a new URL if created and will
         * be stored in the previous URL variable to let further layer do the same 
         * check.
         */
        for (int i = 0; i < layers.length; i++) {
            String layer = layers[i];
            Iterator it = tempSP.iterator();
            while (it.hasNext()) {
                ServiceProvider serviceProvider = (ServiceProvider)it.next();
                Set serviceProviderLayers = serviceProvider.getLayers();
                String spls = findLayer(layer, serviceProviderLayers);
                if (spls != null) {
                    spUrl = calcRequestUrl(serviceProvider, WMS_REQUEST_GetMap);
                    
                    if (spUrl == null) {
                        continue;
                    }
                    
                    if(previousUrl.equals(spUrl.toString())) {
                        StringBuffer url = (StringBuffer)urls.get(urls.size() - 1);
                        url.append("," + spls);
                        urls.remove(urls.size() - 1);
                        urls.add(url);                        
                    } else {
                        previousUrl = spUrl.toString();                     
                        spUrl.append(WMS_VERSION);
                        spUrl.append("=");
                        spUrl.append((String)((String[])parameters.get(WMS_VERSION))[0]);
                        spUrl.append("&");
                        spUrl.append(WMS_REQUEST);
                        spUrl.append("=");
                        spUrl.append(WMS_REQUEST_GetMap);

                        spUrl.append("&");
                        spUrl.append(WMS_PARAM_BBOX);
                        spUrl.append("=");
                        spUrl.append((String)((String[])parameters.get(WMS_PARAM_BBOX))[0]);

                        spUrl.append("&");
                        spUrl.append(WMS_PARAM_SRS);
                        spUrl.append("=");
                        spUrl.append((String)((String[])parameters.get(WMS_PARAM_SRS))[0]);

                        spUrl.append("&");
                        spUrl.append(WMS_PARAM_TRANSPARENT);
                        spUrl.append("=");
                        spUrl.append(WMS_PARAM_TRANSPARENT_TRUE);

                        spUrl.append("&");
                        spUrl.append(WMS_PARAM_FORMAT);
                        spUrl.append("=");
                        spUrl.append((String)((String[])parameters.get(WMS_PARAM_FORMAT))[0]);

                        spUrl.append("&");
                        spUrl.append(WMS_PARAM_WIDTH);
                        spUrl.append("=");
                        spUrl.append((String)((String[])parameters.get(WMS_PARAM_WIDTH))[0]);

                        spUrl.append("&");
                        spUrl.append(WMS_PARAM_HEIGHT);
                        spUrl.append("=");
                        spUrl.append((String)((String[])parameters.get(WMS_PARAM_HEIGHT))[0]);

                        if (parameters.get(WMS_PARAM_EXCEPTION_FORMAT) != null) {
                            spUrl.append("&");
                            spUrl.append(WMS_PARAM_EXCEPTION_FORMAT);
                            spUrl.append("=");
                            spUrl.append((String)((String[]) parameters.get(WMS_PARAM_EXCEPTION_FORMAT))[0]);
                        }

                        spUrl.append("&");
                        spUrl.append(WMS_PARAM_LAYERS);
                        spUrl.append("=");
                        spUrl.append(spls);
                        
                        urls.add(spUrl);
                    }
                } 
            }
        }
        return getOnlineData(dw, urls, true, WMS_REQUEST_GetMap);
    }
    // </editor-fold>
}