/**
 * @(#)GetCapabilitiesRequestHandler.java
 * @author N. de Goeij
 * @version 1.00 2006/12/13
 *
 * Purpose: the function of this class is to create a list of url's which direct to the right servers that have the desired layers
 * for the WMS GetFeatureInfo request.
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

public class GetFeatureInfoRequestHandler extends WMSRequestHandler {
    
    // <editor-fold defaultstate="" desc="default GetFeatureInfoRequestHandler() constructor.">
    public GetFeatureInfoRequestHandler() {}
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
        if (layers.length == 0)
            return null;
        
        /* Go through each layer and find the ServiceProvider this layer belongs to.
         * If a ServiceProvider has been found there will be checked if the previous
         * layer belonged to the same ServiceProvider. If yes then this layer is added
         * up in the URL of the previous layer, otherwise a new URL if created and will
         * be stored in the previous URL variable to let further layer do the same 
         * check.
         */
        for (int j = 0; j < layers.length; j++) {
            String layer = layers[j];
            
            Iterator it = tempSP.iterator();
            while (it.hasNext()) {
                ServiceProvider serviceProvider = (ServiceProvider)it.next();
                Set serviceProviderLayers = serviceProvider.getLayers();
                String spls = findQueryableLayer(layer, serviceProviderLayers);
                if (spls != null) {
                    spUrl = calcRequestUrl(serviceProvider, WMS_REQUEST_GetFeatureInfo);

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
                        spUrl.append(WMS_REQUEST_GetFeatureInfo);
                        spUrl.append("&");
                        spUrl.append(WMS_PARAM_QUERY_LAYERS);
                        spUrl.append("=");
                        spUrl.append(spls);

                        String infoFormat = ((String)((String[])parameters.get(WMS_PARAM_INFO_FORMAT))[0]);
                        if(null != infoFormat) {
                            spUrl.append("&");
                            spUrl.append(WMS_PARAM_INFO_FORMAT);
                            spUrl.append("=");
                            spUrl.append(FEATURE_INFO_FORMAT);
                        }

                        String featureCount = (String)parameters.get(WMS_PARAM_FEATURECOUNT);
                        if (null != featureCount) {
                            spUrl.append("&");
                            spUrl.append(WMS_PARAM_FEATURECOUNT);
                            spUrl.append("=");
                            spUrl.append(featureCount);
                        }

                        spUrl.append("&");
                        spUrl.append(WMS_PARAM_X);
                        spUrl.append("=");
                        spUrl.append((String)((String[])parameters.get(WMS_PARAM_X))[0]);
                        spUrl.append("&");
                        spUrl.append(WMS_PARAM_Y);
                        spUrl.append("=");
                        spUrl.append((String)((String[])parameters.get(WMS_PARAM_Y))[0]);

                        spUrl.append("&");
                        spUrl.append(WMS_PARAM_LAYERS);
                        spUrl.append("=");

                        String [] dbLayers = (((String[])parameters.get(WMS_PARAM_LAYERS))[0]).split(",");
                        StringBuffer cutOffdbLayers = new StringBuffer();
                        for (int i = 0; i < dbLayers.length; i++) {
                            cutOffdbLayers.append(dbLayers[i].substring(dbLayers[i].indexOf("_") + 1));
                            if (i != (dbLayers.length - 1)) {
                                cutOffdbLayers.append(",");
                            }
                        }

                        //spUrl.append(cutOffdbLayers.toString());
                        spUrl.append(spls);
                        //spUrl.append((String)((String[])parameters.get(WMS_PARAM_LAYERS))[0]);
                        spUrl.append("&");
                        spUrl.append(WMS_PARAM_SRS);
                        spUrl.append("=");
                        spUrl.append((String)((String[])parameters.get(WMS_PARAM_SRS))[0]);
                        spUrl.append("&");
                        spUrl.append(WMS_PARAM_BBOX);
                        spUrl.append("=");
                        spUrl.append((String)((String[])parameters.get(WMS_PARAM_BBOX))[0]);
                        spUrl.append("&");
                        spUrl.append(WMS_PARAM_WIDTH);
                        spUrl.append("=");
                        spUrl.append((String)((String[])parameters.get(WMS_PARAM_WIDTH))[0]);
                        spUrl.append("&");
                        spUrl.append(WMS_PARAM_HEIGHT);
                        spUrl.append("=");
                        spUrl.append((String)((String[])parameters.get(WMS_PARAM_HEIGHT))[0]);  
                        
                        urls.add(spUrl);
                    }
                }
            }
        }
        
        /* This return might be a little confusing, because this specific 
         * super method getOnlineData is mostly used to transfer image data 
         * from one or more serviceproviders to the client. But since the 
         * boolean is set to false, and the urls exists of only ONE url the 
         * method automatically returns all the incoming data without any 
         * control or adaptation of this information. Therefore we don't have 
         * to be affraid anything will go wrong when using this method for 
         * an xml document as well.
         */
        return getOnlineData(dw, urls, false, WMS_REQUEST_GetFeatureInfo);
    }
    // </editor-fold>
}