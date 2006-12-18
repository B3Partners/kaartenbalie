package nl.b3p.kaartenbalie.service.requesthandler;

import java.io.IOException;

/**
 * @(#)GetLegendGraphicRequestHandler.java
 *
 *
 * @author N. de Goeij
 * @version 1.00 2006/12/13
 *
 * This class has no function yet, needs to be implemented
 */


public class GetLegendGraphicRequestHandler {

    // <editor-fold defaultstate="collapsed" desc="GetLegendGraphicRequestHandler Constructors">
    public GetLegendGraphicRequestHandler() {
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="getRequest() method">
    public byte[] getRequest() throws IOException, Exception {
        return null;
    }
    // </editor-fold>
    
    /*
    String version      = request.getParameter("VERSION");
    String style        = request.getParameter("STYLE");
    String layer        = request.getParameter("LAYER");
    String featureType  = request.getParameter("FEATURETYPE");
    String rule         = request.getParameter("RULE");
    String scale        = request.getParameter("SCALE");
    String sld          = request.getParameter("SLD");
    String sldBody      = request.getParameter("SLD_BODY");
    String format       = request.getParameter("FORMAT");
    String width        = request.getParameter("WIDTH");
    String height       = request.getParameter("HEIGHT");
    
    //Check first if all the required field are given
    if(null == style) {
        throw new Exception("Parameter required, wms LAYERS");
    }

    String layers[]= layer.split(",");
    List tempSP = this.getServiceProviders(false);
    StringBuffer [] url = new StringBuffer[tempSP.size()];
    boolean [] urlLogger = new boolean[tempSP.size()];
    if(null != tempSP) {
        int counter = 0;
        Iterator it = tempSP.iterator();
        while (it.hasNext()) {
            ServiceProvider s = (ServiceProvider)it.next();
            Set tempLayers = s.getLayers();
            
            if(null != tempLayers) {
                Set domain = s.getDomainResource();
                Iterator domainIter = domain.iterator();
                while (domainIter.hasNext()) {
                    ServiceDomainResource sdr = (ServiceDomainResource)domainIter.next();
                    if(sdr.getDomain().equalsIgnoreCase(givenRequest)) {
                        if(null != sdr.getPostUrl()) {
                            url[counter] = new StringBuffer (sdr.getPostUrl());
                        } else {
                            url[counter] = new StringBuffer (sdr.getGetUrl());
                        }
                    }
                }
                
                try {
                    int length = url[counter].length();
                    int pos = url[counter].lastIndexOf("?");
                    
                    if(length != pos) {
                        url[counter].append("?");
                    }
                } catch (Exception e){
                    url[counter].append("?");
                }
                
                url[counter].append("REQUEST=" + givenRequest + "STYLE=" + style + "&LAYER=");
                
                for (int i = 0; i < layers.length; i++) {
                    String foundLayer = this.findLayer(tempLayers, layers[i], s);
                    if(null != foundLayer) {
                        url[counter].append(foundLayer);
                        url[counter].append(",");
                        urlLogger[counter] = true;
                    }                            
                }
                
                try {
                    url[counter].deleteCharAt(url[counter].lastIndexOf(","));
                } catch (Exception e){}
                
                if(null != featureType) {
                    url[counter].append("&FEATURETYPE=" + featureType);
                }
                if(null != rule) {
                    url[counter].append("&RULE=" + rule);
                }
                if(null != scale) {
                    url[counter].append("&SCALE=" + scale);
                }
                if(null != sld) {
                    url[counter].append("&SLD=" + sld);
                }
                if(null != sldBody) {
                    url[counter].append("&SLD_BODY=" + sldBody);
                }
                if(null != format) {
                    url[counter].append("&FORMAT=" + format);
                }
                if(null != width) {
                    url[counter].append("&WIDTH=" + width);
                }
                if(null != height) {
                    url[counter].append("&HEIGHT=" + height);
                }
                counter++;
            }
        }
    }
    data = this.getOnlineData(url, urlLogger);
    */
    
}