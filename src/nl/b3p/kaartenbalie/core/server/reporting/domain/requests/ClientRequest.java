/*
 * ClientRequest.java
 *
 * Created on September 27, 2007, 4:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.domain.requests;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.datawarehousing.Warehouse;

public class ClientRequest {
    
    private Integer id;
    private Date timeStamp;
    private String clientRequestURI;
    private Set serviceProviderRequests;
    private Set requestOperations;
    private Integer userId;
    
    public ClientRequest() {
        setTimeStamp(new Date());
        setServiceProviderRequests(new HashSet());
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
    
    public Set getRequestOperations() {
        return requestOperations;
    }
    
    public void setRequestOperations(Set requestOperations) {
        this.requestOperations = requestOperations;
    }
    
    private Integer getUserId() {
        return userId;
    }
    
    private void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    
    public void setUser(User user) {
        if (user != null) {
            setUserId(user.getId());
        } else {
            setUserId(null);
        }
    }
    
    public User getUser(User user) {
        try {
            return (User) Warehouse.find(User.class, userId);
        } catch (Exception e) {
            return null;
        }
    }
    
    
}
