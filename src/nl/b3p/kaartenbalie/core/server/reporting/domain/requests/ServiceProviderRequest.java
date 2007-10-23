/*
 * ServiceProviderRequest.java
 *
 * Created on September 28, 2007, 5:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.domain.requests;

import nl.b3p.wms.capabilities.XMLElement;

/**
 *
 * @author Chris Kramer
 */
public abstract class ServiceProviderRequest implements XMLElement {
    
    
    private Integer id;
    
    //Data fields
    private Long bytesSend;
    private Long bytesReceived;
    private Integer responseStatus;
    private Long requestResponseTime;
    private String providerRequestURI;
    private Long msSinceRequestStart;
    //Relational Mappings
    private ClientRequest clientRequest;
    
    public ServiceProviderRequest() {
    }
    public ServiceProviderRequest(ClientRequest clientRequest) {
        this();
        this.setClientRequest(clientRequest);
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
    
    public void setProviderRequestURI(String providerRequestURI) {
        this.providerRequestURI = providerRequestURI;
    }
    
    
    public Long getRequestResponseTime() {
        return requestResponseTime;
    }
    
    public void setRequestResponseTime(Long requestResponseTime) {
        this.requestResponseTime = requestResponseTime;
    }
    
    public Long getBytesSend() {
        return bytesSend;
    }
    
    public void setBytesSend(Long bytesSend) {
        this.bytesSend = bytesSend;
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
    
    
    
    
    
    
}
