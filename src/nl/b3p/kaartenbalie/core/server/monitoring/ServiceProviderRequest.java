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
package nl.b3p.kaartenbalie.core.server.monitoring;

import javax.persistence.EntityManager;
import nl.b3p.gis.B3PCredentials;
import nl.b3p.wms.capabilities.ServiceProvider;

/**
 *
 * @author Chris van Lith
 */
public class ServiceProviderRequest {

    private Integer id;
    private Long bytesSent;
    private Long bytesReceived;
    private String messageSent;
    private String messageReceived;
    private Integer responseStatus;
    private Long requestResponseTime;
    private String providerRequestURI;
    private Long msSinceRequestStart;
    private Class exceptionClass;
    private String exceptionMessage;
    private ClientRequest clientRequest;
    private String wmsVersion;
    private Integer serviceProviderId;
    private String serviceProviderAbbreviation;
    private String srs;
    private Integer width;
    private Integer height;
    private String format;
    private String boundingBox;
    private B3PCredentials credentials;
    private String serviceName;

    public ServiceProviderRequest() {
    }

    public ServiceProviderRequest(ClientRequest clientRequest) {
        this();
        this.setClientRequest(clientRequest);
    }
    
    public void setCredentials(B3PCredentials credentials){
        this.credentials    = credentials;
    }
    
    public B3PCredentials getCredentials(){
        return this.credentials;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ClientRequest getClientRequest() {
        return clientRequest;
    }

    public void setClientRequest(ClientRequest clientRequest) {
        this.clientRequest = clientRequest;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getProviderRequestURI() {
        return providerRequestURI;
    }
    public static final int MAX_LEN = 4000;

    public void setProviderRequestURI(String providerRequestURI) {
        if (providerRequestURI != null && providerRequestURI.length() > MAX_LEN) {
            providerRequestURI = providerRequestURI.substring(0, MAX_LEN);
        }
        this.providerRequestURI = providerRequestURI;
    }

    public Long getRequestResponseTime() {
        return requestResponseTime;
    }

    public void setRequestResponseTime(Long requestResponseTime) {
        this.requestResponseTime = requestResponseTime;
    }

    public Long getBytesSent() {
        return bytesSent;
    }

    public void setBytesSent(Long bytesSent) {
        this.bytesSent = bytesSent;
    }

    public Long getBytesReceived() {
        return bytesReceived;
    }

    public void setBytesReceived(Long bytesReceived) {
        this.bytesReceived = bytesReceived;
    }

    public Long getMsSinceRequestStart() {
        return msSinceRequestStart;
    }

    public void setMsSinceRequestStart(Long msSinceRequestStart) {
        this.msSinceRequestStart = msSinceRequestStart;
    }

    public Class getExceptionClass() {
        return exceptionClass;
    }

    public void setExceptionClass(Class exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
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
            this.credentials    = serviceProvider.getCredentials();
        }
    }

    public ServiceProvider getServiceProvider(EntityManager em) {
        try {
            return (ServiceProvider) em.find(ServiceProvider.class, serviceProviderId);
        } catch (Exception e) {
            return null;
        }
    }

    public String getSrs() {
        return srs;
    }

    public void setSrs(String srs) {
        this.srs = srs;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(String boundingBox) {
        this.boundingBox = boundingBox;
    }

    public String getMessageSent() {
        return messageSent;
    }

    public void setMessageSent(String messageSent) {
        if (messageSent != null && messageSent.length() > MAX_LEN) {
            messageSent = messageSent.substring(0, MAX_LEN);
        }
        this.messageSent = messageSent;
    }

    public String getMessageReceived() {
        return messageReceived;
    }

    public void setMessageReceived(String messageReceived) {
        if (messageReceived != null && messageReceived.length() > MAX_LEN) {
            messageReceived = messageReceived.substring(0, MAX_LEN);
        }
        this.messageReceived = messageReceived;
    }

    public String getServiceProviderAbbreviation() {
        return this.serviceProviderAbbreviation;
    }

    public void setServiceProviderAbbreviation(String abbreviation) {
        this.serviceProviderAbbreviation = abbreviation;
    }
    
    public String getServiceName(){
        return this.serviceName;
    }
    
    public void setServiceName(String serviceName){
        this.serviceName    = serviceName;
    }
}
