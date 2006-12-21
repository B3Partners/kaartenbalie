package nl.b3p.kaartenbalie.service.requesthandler;

import java.io.IOException;
import java.util.Map;


/**
 * @(#)RequestHandler.java
 *
 *
 * @author 
 * @version 1.00 2006/12/13
 */


public interface RequestHandler {
    public byte[] getRequest(Map parameters) throws IOException, Exception;
    
    public String getVersion();
    public void setVersion(String version);
    public String getService();
    public void setService(String service);
    public String getRequestType();
}