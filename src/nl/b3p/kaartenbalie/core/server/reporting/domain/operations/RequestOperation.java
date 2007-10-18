/*
 * RequestOperation.java
 *
 * Created on October 16, 2007, 8:43 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.domain.operations;

import java.util.Date;
import nl.b3p.kaartenbalie.core.server.reporting.domain.requests.ClientRequest;

/**
 *
 * @author Chris Kramer
 */
public abstract class RequestOperation {
    
    private Integer id;
    private Date timeStamp;
    private Long duration;
    private ClientRequest clientRequest;
    public RequestOperation() {
        setTimeStamp(new Date());
    }
    public RequestOperation(ClientRequest clientRequest){
        this();
        this.setClientRequest(clientRequest);
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
    
    public Long getDuration() {
        return duration;
    }
    
    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public ClientRequest getClientRequest() {
        return clientRequest;
    }

    public void setClientRequest(ClientRequest clientRequest) {
        this.clientRequest = clientRequest;
    }
    
    
    
}
