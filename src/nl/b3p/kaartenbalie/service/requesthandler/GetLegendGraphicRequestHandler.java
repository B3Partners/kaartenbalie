package nl.b3p.kaartenbalie.service.requesthandler;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.ServiceDomainResource;
import nl.b3p.kaartenbalie.core.server.ServiceProvider;

/**
 * @(#)GetLegendGraphicRequestHandler.java
 *
 *
 * @author N. de Goeij
 * @version 1.00 2006/12/13
 *
 * This class has no function yet, needs to be implemented
 */


public class GetLegendGraphicRequestHandler extends WMSRequestHandler {
    private String version;
    private String style;
    private String layers;
    private String featureType;
    private String rule;
    private String scale;
    private String sld;
    private String sldBody;
    private String format;
    private String width;
    private String height;
    

    // <editor-fold defaultstate="collapsed" desc="GetLegendGraphicRequestHandler Constructors">
    public GetLegendGraphicRequestHandler() {
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="getRequest() method">
    public byte[] getRequest(Map parameters) throws IOException, Exception {
        version      = (String) parameters.get("VERSION");
        style        = (String) parameters.get("STYLE");
        layers        = (String) parameters.get("LAYER");
        featureType  = (String) parameters.get("FEATURETYPE");
        rule         = (String) parameters.get("RULE");
        scale        = (String) parameters.get("SCALE");
        sld          = (String) parameters.get("SLD");
        sldBody      = (String) parameters.get("SLD_BODY");
        format       = (String) parameters.get("FORMAT");
        width        = (String) parameters.get("WIDTH");
        height       = (String) parameters.get("HEIGHT");

        //Check first if all the required field are given
        if(null == style) {
            throw new Exception("Parameter required, wms LAYERS");
        }
        
        this.organization = (Organization) parameters.get("organization");
        this.url = (String) parameters.get("peronalURL"); 
        
        
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
                        urls[counter].append("REQUEST=" + request + "STYLE=" + style + "&LAYER=" + requestedLayers);
                        if(null != featureType) {
                            urls[counter].append("&FEATURETYPE=" + featureType);
                        }
                        if(null != rule) {
                            urls[counter].append("&RULE=" + rule);
                        }
                        if(null != scale) {
                            urls[counter].append("&SCALE=" + scale);
                        }
                        if(null != sld) {
                            urls[counter].append("&SLD=" + sld);
                        }
                        if(null != sldBody) {
                            urls[counter].append("&SLD_BODY=" + sldBody);
                        }
                        if(null != format) {
                            urls[counter].append("&FORMAT=" + format);
                        }
                        if(null != width) {
                            urls[counter].append("&WIDTH=" + width);
                        }
                        if(null != height) {
                            urls[counter].append("&HEIGHT=" + height);
                        }
                        counter++;
                    }
                }
            }
        }
        return super.getOnlineData(urls);
    }
    // </editor-fold>   
}