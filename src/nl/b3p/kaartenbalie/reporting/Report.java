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
package nl.b3p.kaartenbalie.reporting;

import java.util.Date;
import nl.b3p.kaartenbalie.core.server.Organization;

/**
 *
 * @author Chris van Lith
 */
public class Report {

    private Integer id;
    private Date reportDate;
    private Long processingTime;
    private Organization owningOrganization;
    private String reportXML;
    private Date startDate;
    private Date endDate;
    private int organizationId;

    public Report() {
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

    /**
     * @return the reportXML
     */
    public String getReportXML() {
        return reportXML;
    }

    /**
     * @param reportXML the reportXML to set
     */
    public void setReportXML(String reportXML) {
        this.reportXML = reportXML;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the organizationId
     */
    public int getOrganizationId() {
        return organizationId;
    }

    /**
     * @param organizationId the organizationId to set
     */
    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

}
