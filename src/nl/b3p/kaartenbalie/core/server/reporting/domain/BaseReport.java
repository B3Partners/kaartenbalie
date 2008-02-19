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
import java.util.Iterator;
import java.util.Set;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.reporting.domain.tables.DataTable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Chris Kramer
 */
public abstract class BaseReport  {
    
    private Integer id;
    private Date reportDate;
    private Long processingTime;
    private Organization owningOrganization;
    private Set dataTables;
    
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
    
    public Set getDataTables() {
        return dataTables;
    }
    
    public void setDataTables(Set dataTables) {
        this.dataTables = dataTables;
    }
    
    protected abstract Element toElement(Document doc, Element rootElement);
    
    public Element buildElement(Document doc) {
        Element report = doc.createElement("report");
        report.setAttribute("id", getId().toString());
        report.setAttribute("processingTime", getProcessingTime().toString());
        report.setAttribute("date", getReportDate().toString());
        if (dataTables != null) {
            Iterator iterTable = dataTables.iterator();
            while (iterTable.hasNext()){
                DataTable dt = (DataTable) iterTable.next();
                report.appendChild(dt.toElement(doc, report));
            }
        }
        return toElement(doc, report);
    }
    
    
}
