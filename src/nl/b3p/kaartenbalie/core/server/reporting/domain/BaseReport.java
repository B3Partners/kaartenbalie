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
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.wms.capabilities.XMLElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Chris Kramer
 */
public abstract class BaseReport  implements XMLElement {
    
    private Integer id;
    private Date reportDate;
    private Long processingTime;
    private Organization owningOrganization;
    
    
    
    public BaseReport() {
        setReportDate(new Date());
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
    
    public Organization getOwningOrganization() {
        return owningOrganization;
    }
    
    public void setOwningOrganization(Organization owningOrganization) {
        this.owningOrganization = owningOrganization;
    }
    
    
    
    
    
    
    
    
    
}
