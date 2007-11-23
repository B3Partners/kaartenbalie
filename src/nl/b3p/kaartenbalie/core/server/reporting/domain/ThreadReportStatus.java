/*
 * ThreadReportStatus.java
 *
 * Created on November 2, 2007, 2:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.domain;

import java.util.Date;

/**
 *
 * @author Chris Kramer
 */
public class ThreadReportStatus {
    
    private Integer id;
    private int state;
    private String statusMessage;
    private Date creationDate;
    private Integer reportId;
    
    
    
    public final static int ONQUEUE = 4;
    public final static int CREATED = 3;
    public final static int GENERATING = 2;
    public final static int FAILED = 1;
    public final static int COMPLETED =0;
    
    
    
    
    
    public ThreadReportStatus() {
        setState(CREATED);
        setCreationDate(new Date());
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
    
    public Date getCreationDate() {
        return creationDate;
    }
    
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    public Integer getReportId() {
        return reportId;
    }
    
    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }
    
    
    
    
    
    
    
    
    
    
    
    
}
