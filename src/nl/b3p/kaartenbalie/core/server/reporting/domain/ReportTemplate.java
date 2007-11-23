/*
 * ReportTemplate.java
 *
 * Created on November 2, 2007, 9:56 AM
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
public class ReportTemplate {
    
    private Integer id;
    private Date reportDate;
    private Long processingTime;

    
    public ReportTemplate() {
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Date getReportDate() {
        return reportDate;
    }
    
    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }
    
    public Long getProcessingTime() {
        return processingTime;
    }
    
    public void setProcessingTime(Long processingTime) {
        this.processingTime = processingTime;
    }



    
    
}
