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

import nl.b3p.wms.capabilities.XMLElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Chris Kramer
 */
public class RowValue implements XMLElement {

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
