package nl.b3p.kaartenbalie.service.requesthandler;

/**
 * @(#)GetMapRequestHandler.java
 *
 *
 * @author N. de Goeij
 * @version 1.00 2006/12/13
 *
 * The function of this class is to create a list of url's which direct to the right servers that have the desired layers
 * for WMS requests
 */

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.ServiceDomainResource;
import nl.b3p.kaartenbalie.core.server.ServiceProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetMapRequestHandler extends WMSRequestHandler {
    
    //Since this class is used for the GetMap request we don't need to give this parameter anymore to the class itself
    private static final String request = "GetMap";
    
    //private member variables
    private String layers;
    private String styles;
    private String srs;
    private String bbox;
    private String width;
    private String height;
    private String format;
    private String transparent;
    private String bgColor;
    private String exceptions;
    private String time;
    private String elevation;

    public GetMapRequestHandler() {}
    
    // <editor-fold defaultstate="collapsed" desc="getRequest() method">
    public byte[] getRequest(Map params) throws IOException, Exception {
        if (!getVersion().equalsIgnoreCase("1.1.1")) {
            super.log.error("GetMapRequestHandler: Unsupported WMS VERSION: " + getVersion());
            throw new Exception("Not (yet) supported WMS VERSION: " + getVersion());
        }
        if(null == layers) {
            log.error("GetMapRequestHandler: Parameter required, WMS LAYERS");
            throw new Exception("Parameter required, WMS LAYERS");
        }
        /*
        if(null == styles) {
                log.error("GetMapRequestHandler: Parameter required, wms STYLES");
            throw new Exception("Parameter required, wms STYLES");
        }
         */
        if(null == srs) {
            log.error("GetMapRequestHandler: Parameter required, WMS SRS");
            throw new Exception("Parameter required, WMS SRS");
        }
        if(null == bbox) {
            log.error("GetMapRequestHandler: Parameter required, WMS BBOX");
            throw new Exception("Parameter required, WMS BBOX");
        }
        if(null == width) {
            log.error("GetMapRequestHandler: Parameter required, WMS WIDTH");
            throw new Exception("Parameter required, WMS WIDTH");
        }
        if(null == height) {
            log.error("GetMapRequestHandler: Parameter required, WMS HEIGHT");
            throw new Exception("Parameter required, WMS HEIGHT");
        }
        if(null == format) {
            log.error("GetMapRequestHandler: Parameter required, WMS FORMAT");
            throw new Exception("Parameter required, WMS FORMAT");
        }
        
        String layer[]= layers.split(",");
        List tempSP = super.getServiceProviders(false);
        StringBuffer [] urls = new StringBuffer[tempSP.size()];
        if(null != tempSP) {
            int counter = 0;
            Iterator it = tempSP.iterator();
            while (it.hasNext()) {
                ServiceProvider s = (ServiceProvider)it.next();
                
                //Lets first check if this ServiceProvider can provide us the asked layers
                //otherwise it is not necessary at all to look further and ask for a lot of resources
                Set tempLayers = s.getLayers();
                StringBuffer requestedLayers = new StringBuffer();
                if(null != tempLayers) {
                    for (int i = 0; i < layer.length; i++) {
                        String foundLayer = super.findLayer(tempLayers, layer[i], s);
                        if(null != foundLayer) {
                            requestedLayers.append(foundLayer);
                            requestedLayers.append(",");
                        }
                    }
                    
                    //Check if the layers were found and if so, change the string into the wellformated size
                    if(null != requestedLayers) {
                        requestedLayers.deleteCharAt(requestedLayers.lastIndexOf(","));
                        
                        //Since some or all layers were found, we need to build up a string to become an url
                        //Find the beginning of the url which thise layers belong to
                        Set domain = s.getDomainResource();
                        Iterator domainIter = domain.iterator();
                        while (domainIter.hasNext()) {
                            ServiceDomainResource sdr = (ServiceDomainResource)domainIter.next();
                            if(sdr.getDomain().equalsIgnoreCase(request)) {
                                if(null != sdr.getPostUrl()) {
                                    urls[counter] = new StringBuffer(sdr.getPostUrl());
                                } else {
                                    urls[counter] = new StringBuffer(sdr.getGetUrl());
                                }
                            }
                        }
                        urls[counter].append("VERSION=" + getVersion() + "&REQUEST=" + request + "&LAYERS=" + requestedLayers);
                        urls[counter].append("&BBOX=" + bbox + "&SRS=" + srs + "&WIDTH=" + width + "&HEIGHT=" + height + "&FORMAT=" + format + "&TRANSPARENT=TRUE");
                        counter++;
                    }
                }
            }
        }
        return super.getOnlineData(urls);
    }
    // </editor-fold>
}