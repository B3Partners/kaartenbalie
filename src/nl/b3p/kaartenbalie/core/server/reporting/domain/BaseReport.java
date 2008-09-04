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
package nl.b3p.kaartenbalie.core.server.reporting.domain;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.EntityManager;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.reporting.domain.tables.DataTable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Chris Kramer
 */
public abstract class BaseReport {

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

    protected abstract Element toElement(Document doc, Element rootElement, EntityManager em);

    public Element buildElement(Document doc, EntityManager em) {
        Element report = doc.createElement("report");
        report.setAttribute("id", getId().toString());
        report.setAttribute("processingTime", getProcessingTime().toString());
        report.setAttribute("date", getReportDate().toString());
        if (dataTables != null) {
            Iterator iterTable = dataTables.iterator();
            while (iterTable.hasNext()) {
                DataTable dt = (DataTable) iterTable.next();
                report.appendChild(dt.toElement(doc, report));
            }
        }
        return toElement(doc, report, em);
    }
}
