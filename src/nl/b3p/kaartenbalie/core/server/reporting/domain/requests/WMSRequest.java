/*
 * B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
 * for authentication/authorization, pricing and usage reporting.
 *
 * Copyright 2006, 2007, 2008 B3Partners BV
 * 
 * This file is part of B3P Kaartenbalie.
 * 
 * B3P Kaartenbalie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * B3P Kaartenbalie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.b3p.kaartenbalie.core.server.reporting.domain.requests;

import javax.persistence.EntityManager;
import nl.b3p.kaartenbalie.core.server.datawarehousing.DataWarehousing;
import nl.b3p.wms.capabilities.ServiceProvider;

/**
 *
 * @author Chris Kramer
 *
 */
public abstract class WMSRequest extends ServiceProviderRequest {

    /** Creates a new instance of WMSRequest */
    private String wmsVersion;
    private Integer serviceProviderId;

    public WMSRequest() {
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

    public ServiceProvider getServiceProvider(EntityManager em) {
        try {
            return (ServiceProvider) DataWarehousing.find(ServiceProvider.class, serviceProviderId, em);
        } catch (Exception e) {
            return null;
        }
    }
}
