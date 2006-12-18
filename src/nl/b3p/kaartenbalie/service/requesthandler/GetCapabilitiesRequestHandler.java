package nl.b3p.kaartenbalie.service.requesthandler;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.ServiceProvider;

/**
 * @(#)GetCapabilitiesRequestHandler.java
 *
 * @author N. de Goeij
 * @version 1.00 2006/12/13
 * 
 * The function of this class is to create a well formed XML document of the capabilities that the server has
 * for WMS requests
 */

public class GetCapabilitiesRequestHandler extends WMSRequestHandler {
    
    //Since this class is used for the GetCapabilities request we don't need to pass this parameter anymore to the class itself
    private static final String request = "GetCapabilities";
    
    //private member variables
    private String updateSequence;
    private Object [] params;
    
    //Constructor
    public GetCapabilitiesRequestHandler() {}
    
    // <editor-fold defaultstate="collapsed" desc="getRequest() method">
    public byte[] getRequest(Map parameters) throws IOException, Exception {
        if (!((String)parameters.get("version")).equalsIgnoreCase("1.1.1")) {
            log.error("GetCapabilitiesRequestHandler: Unsupported WMS VERSION: " + getVersion());
            throw new Exception("Not (yet) supported WMS VERSION: " + getVersion());
        }
        if(!((String)parameters.get("service")).equalsIgnoreCase("WMS")) {
            log.error("GetCapabilitiesRequestHandler: Unsupported GetCapabilities SERVICE: " + getService());
            throw new Exception("Not supported GetCapabilities SERVICE: " + getService());
        }
        
        this.organization = (Organization) parameters.get("organization");
        
        ServiceProvider s = null;
        List sps = super.getServiceProviders(true);
        Iterator it = sps.iterator();
        
        while (it.hasNext()) {
            s = (ServiceProvider)it.next();
            try{
                Method m = s.getClass().getDeclaredMethod("overwriteURL", String.class);
                m.setAccessible(true);
                m.invoke(s, new String(url));
            } catch (Exception e) { log.error("Error rewriting the URL's : " + e); }
        }
        
        return super.getOnlineData(s.toString());
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Get and Set methods">
    public String getUpdateSequence() {
        return updateSequence;
    }

    public void setUpdateSequence(String updateSequence) {
        this.updateSequence = updateSequence;
    }
    // </editor-fold>
}