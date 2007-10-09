/*
 * WMSRequest.java
 *
 * Created on October 1, 2007, 12:00 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.domain;

/**
 *
 * @author Chris Kramer
 */
public class WMSRequest extends ServiceProviderRequest{
    
    /** Creates a new instance of WMSRequest */
    private String wmsVersion;

    public WMSRequest()  {
        super();
    }
    
    
    public String getWmsVersion() {
        return wmsVersion;
    }
    
    public void setWmsVersion(String wmsVersion) {
        this.wmsVersion = wmsVersion;
    }
    
    
    
}
