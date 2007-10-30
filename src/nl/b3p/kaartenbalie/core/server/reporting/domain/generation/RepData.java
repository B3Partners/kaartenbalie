/*
 * ReportDataSet.java
 *
 * Created on October 26, 2007, 11:55 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.domain.generation;

import nl.b3p.wms.capabilities.XMLElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Chris Kramer
 */
public abstract class RepData {
    
    private Integer id;
    protected Report report;
    protected RepData() {
    }
    
    public RepData(Report report) {
        this();
        this.setReport(report);
    }
    
    public Report getReport() {
        return report;
    }
    
    public void setReport(Report report) {
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
