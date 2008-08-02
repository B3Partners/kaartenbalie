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
import nl.b3p.kaartenbalie.core.server.reporting.domain.BaseReport;
import nl.b3p.wms.capabilities.XMLElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DataTable implements XMLElement {

    private Integer id;
    private String tableName;
    private BaseReport baseReport;
    private Set tableRows;
    private int rowCounter;

    public DataTable() {
        setTableRows(new HashSet());
        rowCounter = 0;
    }

    public DataTable(BaseReport baseReport, String tableName) {
        this();
        this.setTableName(tableName);
        this.setBaseReport(baseReport);
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public BaseReport getBaseReport() {
        return baseReport;
    }

    public void setBaseReport(BaseReport baseReport) {
        this.baseReport = baseReport;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set getTableRows() {
        return tableRows;
    }

    private void setTableRows(Set tableRows) {
        this.tableRows = tableRows;
    }

    public Element toElement(Document doc, Element rootElement) {
        Element dataTable = doc.createElement("dataTable");
        dataTable.setAttribute("tableName", getTableName());
        if (getTableRows() != null) {
            Iterator iterTableRows = getTableRows().iterator();
            while (iterTableRows.hasNext()) {
                TableRow tr = (TableRow) iterTableRows.next();
                dataTable.appendChild(tr.toElement(doc, rootElement));
            }
        }
        return dataTable;
    }

    public void addRow(TableRow tableRow) {
        tableRow.setRowOrder(new Integer(rowCounter));
        getTableRows().add(tableRow);
        tableRow.setDataTable(this);
        rowCounter++;
    }
}
