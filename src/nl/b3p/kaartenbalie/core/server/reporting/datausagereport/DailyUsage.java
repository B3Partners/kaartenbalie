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
package nl.b3p.kaartenbalie.core.server.reporting.datausagereport;

import java.util.Date;
import nl.b3p.wms.capabilities.XMLElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Chris Kramer
 */
public class DailyUsage implements XMLElement {

    private Integer id;
    private Date date;
    private Integer hour;
    private Long dataUsage;
    private Long hits;
    private UsageDetails usageDetails;

    private DailyUsage() {
    }

    public DailyUsage(UsageDetails usageDetails) {
        this();
        this.usageDetails = usageDetails;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Long getDataUsage() {
        return dataUsage;
    }

    public void setDataUsage(Long dataUsage) {
        this.dataUsage = dataUsage;
    }

    public Long getHits() {
        return hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Element toElement(Document doc, Element rootElement) {
        Element hourlyUsage = doc.createElement("hourlyUsage");
        hourlyUsage.setAttribute("hour", getHour().toString());
        hourlyUsage.setAttribute("dataUsage", getDataUsage().toString());
        hourlyUsage.setAttribute("requests", getHits().toString());
        return hourlyUsage;
    }

    public UsageDetails getUsageDetails() {
        return usageDetails;
    }

    public void setUsageDetails(UsageDetails usageDetails) {
        this.usageDetails = usageDetails;
    }
}
