/*
 * WMSRequest.java
 *
 * Created on October 1, 2007, 12:00 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.domain.requests;

import nl.b3p.kaartenbalie.core.server.datawarehousing.DataWarehousing;
import nl.b3p.wms.capabilities.ServiceProvider;

/**
 *
 * @author Chris Kramer
 */
public abstract class WMSRequest extends ServiceProviderRequest{
    
    /** Creates a new instance of WMSRequest */
    private String wmsVersion;
    private Integer serviceProviderId;
    public WMSRequest()  {
        super();
    }
    
    
    public String getWmsVersion() {
        return wmsVersion;
    }
    
    public void setWmsVersion(String wmsVersion) {
        this.wmsVersion = wmsVersion;
    }
    
    public Integer getServiceProviderId() {
        return serviceProviderId;
    }
    
    public void setServiceProviderId(Integer serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }
    public void setServiceProvider(ServiceProvider serviceProvider) {
        if (serviceProvider == null) {
            serviceProviderId = null;
        } else {
            this.serviceProviderId = serviceProvider.getId();
        }
    }
    
    public ServiceProvider getServiceProvider() {
        try {
            return (ServiceProvider) DataWarehousing.find(ServiceProvider.class, serviceProviderId);
        } catch (Exception e) {
            return null;
        }
    }
    
    
    
}
