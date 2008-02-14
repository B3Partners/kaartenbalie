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
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.datawarehousing.DataWarehousing;

public class ClientRequest {
    
    private Integer id;
    private Date timeStamp;
    private String clientRequestURI;
    private Set serviceProviderRequests;
    private Set requestOperations;
    private String clientIp;
    private String method;
    private Integer userId;
    private Integer organizationId;
    private String service;
    private String operation;

    private Class exceptionClass;
    private String exceptionMessage;    
    
    
    public ClientRequest() {
        setTimeStamp(new Date());
        setServiceProviderRequests(new HashSet());
        setRequestOperations(new HashSet());
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
    
    public User getUser() {
        try {
            return (User) DataWarehousing.find(User.class, getUserId());
        } catch (Exception e) {
            return null;
        }
    }
    
    private Integer getOrganizationId() {
        return organizationId;
    }
    
    private void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }
    
    public void setOrganization(Organization organization) {
        if (organization != null) {
            setOrganizationId(organization.getId());
        } else {
            setOrganizationId(null);
        }
    }
    
    public Organization getOrganization() {
        try {
            return (Organization) DataWarehousing.find(Organization.class, getOrganizationId());
        } catch (Exception e) {
            return null;
        }
    }
    
    public String getClientIp() {
        return clientIp;
    }
    
    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
    
    public String getMethod() {
        return method;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }
    
    public String getService() {
        return service;
    }
    
    public void setService(String service) {
        this.service = service;
    }
    
    public String getOperation() {
        return operation;
    }
    
    public void setOperation(String operation) {
        this.operation = operation;
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

}
