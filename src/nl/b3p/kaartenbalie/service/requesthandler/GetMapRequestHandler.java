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
import nl.b3p.kaartenbalie.service.LayerValidator;

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
    public void getRequest(DataWrapper dw, Map parameters) throws IOException, Exception {
        /* Initialize some variables */
        user = (User) parameters.get(KB_USER);
        url = (String) parameters.get(KB_PERSONAL_URL);
        
        String previousUrl = "";
        ArrayList urls = new ArrayList();
        StringBuffer spUrl = null;
        
        /* Get a list with ServiceProviders from the database */
        List tempSP = getServiceProviders(false);
        if (tempSP == null) 
            return;
        
        /*
         * Voordat we aan de lijst beginnen iis het eerst noodzakelijk dat een aantal variabelen
         * gecontroleerd worden op hun input. Als hier namelijk fouten in blijken te zitten dan
         * moeten deze aan de gebruiker gerapporteerd worden.
         */
        //Eerst moeten de WIDTH en HEIGHT gecontroleerd worden
        //Vervolgens moeten de BBOX waarden gecontroleerd worden
        //Dan moet de SRS bepaald worden. Stemt deze voor alle SP's overeen?
        String givenSRS = (String)parameters.get(WMS_PARAM_SRS);
        boolean srsFound = false;
        
        Set spLayers = ((ServiceProvider)tempSP.get(0)).getLayers();
        LayerValidator lv = new LayerValidator(spLayers);
        String [] srsses = lv.validateSRS();
        for (int i = 0; i < srsses.length; i++) {
            String srs = srsses[i];
            if(srs.equals(givenSRS)) {
                srsFound = true;
            }
        }
        
        if (!srsFound) {
            throw new Exception("msWMSLoadGetMapParams(): WMS server error. Invalid SRS given : SRS must be valid for all requested layers.");
        }        
        
        
        int width  = Integer.parseInt((String)parameters.get(WMS_PARAM_WIDTH));
        int height = Integer.parseInt((String)parameters.get(WMS_PARAM_HEIGHT));
        if(width < 1 || height < 1 || width > 2048 || height > 2048) {
            throw new Exception("msWMSLoadGetMapParams(): WMS server error. Image size out of range, WIDTH and HEIGHT must be between 1 and 2048 pixels.");
        }
        
        String [] boxx = ((String)parameters.get(WMS_PARAM_BBOX)).split(",");
        if(boxx.length < 4) {
            throw new Exception("msWMSLoadGetMapParams(): WMS server error. Invalid values for BBOX.");
        }
        
        double minx = Double.parseDouble(boxx[0]);
        double miny = Double.parseDouble(boxx[1]);
        double maxx = Double.parseDouble(boxx[2]);
        double maxy = Double.parseDouble(boxx[3]);
        
        if (minx > maxx || miny > maxy) {
            throw new Exception("msWMSLoadGetMapParams(): WMS server error. Invalid values for BBOX.");
        }
        
        
        /* Split the string with layers into a String array */
        String layerString = (String) parameters.get(WMS_PARAM_LAYERS);
        String [] layers = (String[]) layerString.split(",");
        
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
                        spUrl.append((String)parameters.get(WMS_VERSION));
                        spUrl.append("&");
                        spUrl.append(WMS_REQUEST);
                        spUrl.append("=");
                        spUrl.append(WMS_REQUEST_GetMap);

                        spUrl.append("&");
                        spUrl.append(WMS_PARAM_BBOX);
                        spUrl.append("=");
                        spUrl.append((String)parameters.get(WMS_PARAM_BBOX));

                        spUrl.append("&");
                        spUrl.append(WMS_PARAM_SRS);
                        spUrl.append("=");
                        spUrl.append((String)parameters.get(WMS_PARAM_SRS));

                        spUrl.append("&");
                        spUrl.append(WMS_PARAM_TRANSPARENT);
                        spUrl.append("=");
                        spUrl.append(WMS_PARAM_TRANSPARENT_TRUE);

                        spUrl.append("&");
                        spUrl.append(WMS_PARAM_FORMAT);
                        spUrl.append("=");
                        spUrl.append((String)parameters.get(WMS_PARAM_FORMAT));

                        spUrl.append("&");
                        spUrl.append(WMS_PARAM_WIDTH);
                        spUrl.append("=");
                        spUrl.append((String)parameters.get(WMS_PARAM_WIDTH));

                        spUrl.append("&");
                        spUrl.append(WMS_PARAM_HEIGHT);
                        spUrl.append("=");
                        spUrl.append((String)parameters.get(WMS_PARAM_HEIGHT));
                        
                        spUrl.append("&");
                        spUrl.append(WMS_PARAM_LAYERS);
                        spUrl.append("=");
                        spUrl.append(spls);
                        urls.add(spUrl);
                    }
                } 
            }
            if(spUrl == null) {
                throw new Exception("msWMSLoadGetMapParams(): WMS server error. Invalid layer(s) given in the LAYERS parameter.");
            }
        }
        
        String format = (String) parameters.get(WMS_PARAM_FORMAT);
        dw.setContentType(format);
        
        String inimageType = null;
        
        if (parameters.containsKey(WMS_PARAM_EXCEPTION_FORMAT)) {
            inimageType = format;
        }
        dw.setErrorContentType(inimageType);
        
        getOnlineData(dw, urls, true, WMS_REQUEST_GetMap);
    }
    // </editor-fold>
}