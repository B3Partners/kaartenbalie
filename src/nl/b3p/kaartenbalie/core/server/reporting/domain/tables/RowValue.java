/*
 * RowValue.java
 *
 * Created on February 18, 2008, 1:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.domain.tables;

import nl.b3p.wms.capabilities.XMLElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Chris Kramer
 */
public class RowValue implements XMLElement  {
    
    private Integer id;
    private TableRow tableRow;
    private String rowValue;
    private Integer valueOrder;
    
    public RowValue() {
        
    }
    public RowValue(String rowValue) {
        this();
        this.setRowValue(rowValue);
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public TableRow getTableRow() {
        return tableRow;
    }
    
    public void setTableRow(TableRow tableRow) {
        this.tableRow = tableRow;
    }
    
    public String getRowValue() {
        return rowValue;
    }
    
    public void setRowValue(String rowValue) {
        this.rowValue = rowValue;
    }
    
    
    
    public Integer getValueOrder() {
        return valueOrder;
    }
    
    public void setValueOrder(Integer valueOrder) {
        this.valueOrder = valueOrder;
    }
    
    public Element toElement(Document doc, Element rootElement) {
        Element rowValueElement = doc.createElement("rowValue");
        rowValueElement.setTextContent(getRowValue());
        return rowValueElement;
    }
    
    
    
    
}
