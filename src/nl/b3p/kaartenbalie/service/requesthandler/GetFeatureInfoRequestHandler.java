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
    
    // <editor-fold defaultstate="collapsed" desc="default GetFeatureInfoRequestHandler() constructor.">
    public GetFeatureInfoRequestHandler() {}
    // </editor-fold>
    
    //TODO: De methode werkt nog niet juist omdat ze nog geen volledige URL's maakt.
    //Dat wil zeggen dat de URL's nog niet aangevuld zijn met de parameters van de GetMap
    //functie.
    
    /** Processes the parameters and creates the specified urls from the given parameters.
     * Each url will be used to recieve the data from the ServiceProvider this url is refering to.
     * @param parameters Map parameters
     * @return byte[]
     *
     * @throws IOException
     */
    // <editor-fold defaultstate="collapsed" desc="getRequest(Map parameters) method.">
    public byte[] getRequest(Map <String, Object> parameters) throws IOException {
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
        
        //TODO: deze code moet verwijderd worden voor de oplevering
        //Een klein stukje test code
        /**/
        int length = layers.length;
        String [] reverseLayers = new String [length];
        for (int i = length - 1; i >= 0; i--) {
            reverseLayers[i] = layers[length - i - 1];
        }        
        layers = reverseLayers;
        System.out.println("There are " + length + " layers selected.");
        System.out.println("There are " + tempSP.size() + " service providers available.");
        /**/
        //Einde test
        
        
        
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
                System.out.println("The service provider in progress is " + serviceProvider.getGivenName());
                System.out.println("This layer has been found " + layer + ". And belongs to service provider " + spls);
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
                        spUrl.append((String)parameters.get(WMS_VERSION));
                        spUrl.append("&");
                        spUrl.append(WMS_REQUEST);
                        spUrl.append("=");
                        spUrl.append(WMS_REQUEST_GetFeatureInfo);
                        spUrl.append("&");
                        spUrl.append(WMS_PARAM_QUERY_LAYERS);
                        spUrl.append("=");
                        spUrl.append(spls);

                        String infoFormat = (String)parameters.get(WMS_PARAM_INFO_FORMAT);
                        if(null != infoFormat) {
                            spUrl.append("&");
                            spUrl.append(WMS_PARAM_INFO_FORMAT);
                            spUrl.append("=");
                            spUrl.append(infoFormat);
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
                        spUrl.append((String)parameters.get(WMS_PARAM_X));
                        spUrl.append("&");
                        spUrl.append(WMS_PARAM_Y);
                        spUrl.append("=");
                        spUrl.append((String)parameters.get(WMS_PARAM_Y));
                        
                        urls.add(spUrl);
                    }
                }
            }
        }
        return getOnlineData(urls);

    }
    // </editor-fold>
}