/*
 * B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
 * for authentication/authorization, pricing and usage reporting.
 *
 * Copyright 2006, 2007, 2008 B3Partners BV
 * 
 * This file is part of B3P Kaartenbalie.
 * 
 * B3P Kaartenbalie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * B3P Kaartenbalie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
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
