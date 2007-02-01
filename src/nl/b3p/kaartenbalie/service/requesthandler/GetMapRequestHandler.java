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
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.ServiceDomainResource;
import nl.b3p.kaartenbalie.core.server.ServiceProvider;
import nl.b3p.kaartenbalie.core.server.User;

public class GetMapRequestHandler extends WMSRequestHandler {
    
    // <editor-fold defaultstate="collapsed" desc="default GetMapRequestHandler() constructor.">
    public GetMapRequestHandler() {}
    // </editor-fold>
    
    /** Processes the parameters and creates the specified urls from the given parameters.
     * Each url will be used to recieve the data from the ServiceProvider this url is refering to.
     * @param parameters Map parameters
     * @return byte[]
     *
     * @throws IOException
     */
    // <editor-fold defaultstate="collapsed" desc="getRequest(Map parameters) method.">
    public byte[] getRequest(Map <String, Object> parameters) throws IOException {
        user = (User) parameters.get(KB_USER);
        url = (String) parameters.get(KB_PERSONAL_URL);
        
        
        
        String previousUrl = "";
        
        ArrayList urls = new ArrayList();
        
        StringBuffer spUrl = null;
        
        List tempSP = getServiceProviders(false);
        if (tempSP == null) 
            return null;
        
        String [] layers = (((String[])parameters.get(WMS_PARAM_LAYERS))[0]).split(",");
//        int length = layers.length;
//        String [] testLayers = new String [length];
//        int y = 0;
//        for(int i = (length - 1); i >= 0; i--) {
//            testLayers[y] = layers[i];
//            y++;
//        }
        
        
        for (int i = 0; i < layers.length; i++) {
            //Find those URLS
            String layer = layers[i];
            //String layer = testLayers[i];
            //String previousURL = "";
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
        
        Iterator it = urls.iterator();
        while (it.hasNext()) {
            StringBuffer elem = (StringBuffer) it.next();
            System.out.println("We found an url : " + elem.toString());            
        }
        
        byte [] bytes = getOnlineData(urls);
        String b = new String(bytes);
        
        System.out.println("The size of this string is : " + b.length());
        return bytes;//getOnlineData(urls);
    }
        
        
        
/*        
//        ArrayList urls = new ArrayList();
        //int counter = 0;
        //StringBuffer [] urls = new StringBuffer[tempSP.size()];
        //StringBuffer [] urls = null;
        
        ArrayList urls = new ArrayList();
        
        Iterator it = tempSP.iterator();
        while (it.hasNext()) {
            
            ServiceProvider serviceProvider = (ServiceProvider)it.next();
            System.out.println("Een serviceprovider die opgehaald is uit de database: " + serviceProvider.getGivenName());
            StringBuffer spUrl = null;
            
            //Lets first check if this ServiceProvider can provide us the asked layers
            //otherwise it is not necessary at all to look further and ask for a lot of resources
            String [] layers = (((String[])parameters.get(WMS_PARAM_LAYERS))[0]).split(",");
            
            //Part of test case
            //String paramValue = "";
            //for (int i = 0; i < l.length; i++) {
//                paramValue = paramValue + "/" + l[i];
//            }
            //System.out.println("-------------------------------------------");
            //System.out.println("In GetMapRequestHandler.");
            //System.out.println("Layers defined in handler are : " + paramValue);
            //System.out.println("-------------------------------------------");
            //end Part of test case
            
            //String lString = l[0];
            //String [] layer = lString.split(",");
            String spls = calcFormattedLayers(serviceProvider, layers);
            
            //Part of test case
            //System.out.println("-------------------------------------------");
            //System.out.println("In GetMapRequestHandler.");
            //System.out.println("Found serviceProvider : " + spls);
            //System.out.println("-------------------------------------------");
            //end Part of test case
            
            if (spls==null)
                continue;
            
            //Since some or all layers were found, we need to build up a string to become an url
            //Find the beginning of the url which thise layers belong to
            spUrl = calcRequestUrl(serviceProvider, WMS_REQUEST_GetMap);
            if (spUrl==null)
                continue;
            
            
            // toevoegen van andere parameters
            spUrl.append(WMS_VERSION);
            spUrl.append("=");
            spUrl.append((String)((String[])parameters.get(WMS_VERSION))[0]);
            spUrl.append("&");
            spUrl.append(WMS_REQUEST);
            spUrl.append("=");
            spUrl.append(WMS_REQUEST_GetMap);
            spUrl.append("&");
            spUrl.append(WMS_PARAM_LAYERS);
            spUrl.append("=");
            spUrl.append(spls);
            
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
            
            //Part of test case
            System.out.println("-------------------------------------------");
            System.out.println("In GetMapRequestHandler.");
            System.out.println("Found url : " + spUrl);
            System.out.println("-------------------------------------------");
            //end Part of test case
            
            urls.add(spUrl);
        }
//        urls = new StringBuffer[tempList.size()];
        
//        for (int counter = 0; counter < tempList.size(); counter++) {
//            urls[counter] = (StringBuffer)tempList.get(counter);
//        }
            //counter++;
        //return getOnlineData(urls);
        byte [] bytes = getOnlineData(urls);
        String b = new String(bytes);
        
        System.out.println("The size of this string is : " + b.length());
        return bytes;//getOnlineData(urls);
        //return getOnlineData((StringBuffer[])urls.toArray(url));
    }
    // </editor-fold>
    */
}