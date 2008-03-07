/*
 * TableRow.java
 *
 * Created on February 18, 2008, 1:30 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.domain.tables;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import nl.b3p.wms.capabilities.XMLElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Chris Kramer
 */
public class TableRow implements XMLElement {
    
    private Integer id;
    private Boolean header;
    private Integer rowOrder;
    private int valueCounter;
    
    private DataTable dataTable;
    private Set rowValues;
    
    public TableRow() {
        setRowValues(new HashSet());
        this.setHeader(new Boolean(false));
        valueCounter = 0;
    }
    
    
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public DataTable getDataTable() {
        return dataTable;
    }
    
    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }
    
    public Set getRowValues() {
        return rowValues;
    }
    
    private void setRowValues(Set rowValues) {
        this.rowValues = rowValues;
    }
    
    public Integer getRowOrder() {
        return rowOrder;
    }
    
    public void setRowOrder(Integer rowOrder) {
        this.rowOrder = rowOrder;
    }
    
    public Element toElement(Document doc, Element rootElement) {
        Element tableRow = doc.createElement("tableRow");
        if (getHeader() != null) {
            tableRow.setAttribute("header", getHeader().toString());
        } else {
            tableRow.setAttribute("header", "false");
        }
        if (rowValues != null) {
            Iterator rowValueIter = rowValues.iterator();
            while (rowValueIter.hasNext()) {
                RowValue rowValue = (RowValue) rowValueIter.next();
                tableRow.appendChild(rowValue.toElement(doc, rootElement));
            }
        }
        return tableRow;
    }
    
    public void addValue(RowValue rowValue) {
        rowValue.setValueOrder(new Integer(valueCounter));
        rowValues.add(rowValue);
        rowValue.setTableRow(this);
        valueCounter++;
    }

    public Boolean getHeader() {
        return header;
    }

    public void setHeader(Boolean header) {
        this.header = header;
    }
    
    
}
