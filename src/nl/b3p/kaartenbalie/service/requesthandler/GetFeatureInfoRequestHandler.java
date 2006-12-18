package nl.b3p.kaartenbalie.service.requesthandler;

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

/**
 * @(#)GetFeatureInfoRequestHandler.java
 *
 *
 * @author N. de Goeij
 * @version 1.00 2006/12/13
 */


public class GetFeatureInfoRequestHandler extends WMSRequestHandler {
    //Since this class is used for the GetFeatureInfo request we don't need to give this parameter anymore to the class itself
    private static final String request = "GetFeatureInfo";
    
    //private member variables
    private String mapRequestCopy;
    private String layers;
    private String infoFormat;
    private String featureCount;
    private String x;
    private String y;
    private String exceptionFormat;
    
    // <editor-fold defaultstate="collapsed" desc="GetFeatureInfoRequestHandler Constructors">
    public GetFeatureInfoRequestHandler() {}
    
    public GetFeatureInfoRequestHandler(String mapRequestCopy, String layers, String infoFormat, String featureCount, 
            String x, String y, String exceptionFormat) {
        this.setMapRequestCopy(mapRequestCopy);
        this.setInfoFormat(infoFormat);
        this.setFeatureCount(featureCount);
        this.setX(x);
        this.setY(y);
        this.setExceptionFormat(exceptionFormat);
    }
    
    public GetFeatureInfoRequestHandler(String mapRequestCopy, String layers, String x, String y) {
        this(mapRequestCopy, layers, "", "", x, y, "");
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="getRequest() method">
    public byte[] getRequest(Map params) throws IOException, Exception {
        if (!getVersion().equalsIgnoreCase("1.1.1")) {
            log.error("GetFeatureInfoRequestHandler: Unsupported WMS VERSION: " + getVersion());
            throw new Exception("Not (yet) supported WMS VERSION: " + getVersion());
        }
        if(!getService().equalsIgnoreCase("WMS")) {
            log.error("GetFeatureInfoRequestHandler: Unsupported FeatureInfo SERVICE: " + getService());
            throw new Exception("Not supported FeatureInfo SERVICE: " + getService());
        }
        
        if(null == layers) {
            throw new Exception("Parameter required, wms LAYERS");
        }
        if(null == x) {
            throw new Exception("Parameter required, wms x");
        }
        if(null == y) {
            throw new Exception("Parameter required, wms y");
        }
        
        
        String layer[]= layers.split(",");
        List tempSP = super.getServiceProviders(false);
        StringBuffer [] url = new StringBuffer[tempSP.size()];
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
                                    url[counter] = new StringBuffer(sdr.getPostUrl());
                                } else {
                                    url[counter] = new StringBuffer(sdr.getGetUrl());
                                }
                            }
                        }
                        url[counter].append("VERSION=" + getVersion() + "&REQUEST=" + request + "&QUERY_LAYERS=" + requestedLayers);
                        
                        
                        if(null != infoFormat) {
                            url[counter].append("&INFO_FORMAT=" + infoFormat);
                        }
                        if (null != featureCount) {
                            url[counter].append("&FEATURECOUNT=" + featureCount);
                        }
                        url[counter].append("&X=" + x + "&Y=" + y);
                        counter++;
                    }
                }
            }
        }
        return super.getOnlineData(url);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Get and Set methods">
    public String getMapRequestCopy() {
        return mapRequestCopy;
    }

    public void setMapRequestCopy(String mapRequestCopy) {
        this.mapRequestCopy = mapRequestCopy;
    }
    
    public String getLayers() {
        return layers;
    }
    
    public void setLayers(String layers) {
        this.layers = layers;
    }
    
    public String getInfoFormat() {
        return infoFormat;
    }
    
    public void setInfoFormat(String infoFormat) {
        this.infoFormat = infoFormat;
    }
    
    public String getFeatureCount() {
        return featureCount;
    }
    
    public void setFeatureCount(String featureCount) {
        this.featureCount = featureCount;
    }
    
    public String getX() {
        return x;
    }
    
    public void setX(String x) {
        this.x = x;
    }
    
    public String getY() {
        return y;
    }
    
    public void setY(String y) {
        this.y = y;
    }
    
    public String getExceptionFormat() {
        return exceptionFormat;
    }

    public void setExceptionFormat(String exceptionFormat) {
        this.exceptionFormat = exceptionFormat;
    }
    // </editor-fold>
}