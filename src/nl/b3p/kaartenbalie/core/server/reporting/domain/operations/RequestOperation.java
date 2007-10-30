/*
 * TotalRequestOperation.java
 *
 * Created on October 23, 2007, 2:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.domain.operations;

/**
 *
 * @author Chris Kramer
 */
public class RequestOperation extends Operation{
    private Integer bytesReceivedFromUser;
    private Integer bytesSendToUser;
    
    /** Creates a new instance of TotalRequestOperation */
    public RequestOperation() {
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

}
