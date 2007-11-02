/*
 * ReportDataSet.java
 *
 * Created on October 26, 2007, 11:55 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.datausagereport;

import nl.b3p.wms.capabilities.XMLElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Chris Kramer
 */
public abstract class RepData {
    
    private Integer id;
    protected DataUsageReport report;
    protected RepData() {
    }
    
    public RepData(DataUsageReport report) {
        this();
        this.setReport(report);
    }
    
    public DataUsageReport getReport() {
        return report;
    }
    
    public void setReport(DataUsageReport report) {
        this.report = report;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public abstract Element toElement(Document doc, Element rootElement);
    
    
}
