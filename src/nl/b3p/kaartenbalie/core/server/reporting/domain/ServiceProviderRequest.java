/*
 * ServiceProviderRequest.java
 *
 * Created on September 28, 2007, 5:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.domain;

/**
 *
 * @author Chris Kramer
 */
public class ServiceProviderRequest {
    
    
    private Integer id;
    
    //Data fields
    private Integer bytesSend;
    private Integer bytesReceived;
    private Integer responseStatus;
    private String providerRequestURI;
    
    //Relational Mappings
    private ClientRequest clientRequest;
    
    public ServiceProviderRequest() {
    }
    public ServiceProviderRequest(ClientRequest clientRequest) {
        this.clientRequest = clientRequest;
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
    
    public Integer getBytesSend() {
        return bytesSend;
    }
    
    public void setBytesSend(Integer bytesSend) {
        this.bytesSend = bytesSend;
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
    
    public Integer getBytesReceived() {
        return bytesReceived;
    }
    
    public void setBytesReceived(Integer bytesReceived) {
        this.bytesReceived = bytesReceived;
    }
    
    
    
    
    
}
