/*
 * ThreadReportStatus.java
 *
 * Created on November 2, 2007, 2:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.domain;

/**
 *
 * @author Chris Kramer
 */
public class ThreadReportStatus {
    
    private Integer id;
    private int state;
    private String statusMessage;
    
    public final static int CREATED = 0;
    public final static int GENERATING = 1;
    public final static int COMPLETED = 2;
    public final static int ONQUEUE = 3;
    public final static int FAILED = -1;
    public ThreadReportStatus() {
        state = CREATED;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public int getState() {
        return state;
    }
    
    public void setState(int state) {
        this.state = state;
    }
    
    public String getStatusMessage() {
        return statusMessage;
    }
    
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
    
    
    
    
    
}
