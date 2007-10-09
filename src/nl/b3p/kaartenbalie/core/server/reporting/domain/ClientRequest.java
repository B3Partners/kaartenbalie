/*
 * ClientRequest.java
 *
 * Created on September 27, 2007, 4:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ClientRequest {
    
    private Integer id;
    private Date timeStamp;
    private Integer bytesReceivedFromUser;
    private Integer bytesSendToUser;
    private Long totalResponseTime;
    private String clientRequestURI;
    private Set serviceProviderRequests;
    
    
    
    public ClientRequest() {
        setTimeStamp(new Date());
        serviceProviderRequests = new HashSet();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getBytesReceivedFromUser() {
        return bytesReceivedFromUser;
    }

    public void setBytesReceivedFromUser(Integer bytesReceivedFromUser) {
        this.bytesReceivedFromUser = bytesReceivedFromUser;
    }

    public Integer getBytesSendToUser() {
        return bytesSendToUser;
    }

    public void setBytesSendToUser(Integer bytesSendToUser) {
        this.bytesSendToUser = bytesSendToUser;
    }

    public Long getTotalResponseTime() {
        return totalResponseTime;
    }

    public void setTotalResponseTime(Long totalResponseTime) {
        this.totalResponseTime = totalResponseTime;
    }

    public Set getServiceProviderRequests() {
        return serviceProviderRequests;
    }

    public void setServiceProviderRequests(Set serviceProviderRequests) {
        this.serviceProviderRequests = serviceProviderRequests;
    }

    
    public String getClientRequestURI() {
        return clientRequestURI;
    }

    public void setClientRequestURI(String clientRequestURI) {
        this.clientRequestURI = clientRequestURI;
    }



    
    
}
