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

import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;
import nl.b3p.kaartenbalie.core.server.reporting.control.DataMonitoring;
import nl.b3p.wms.capabilities.XMLElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Chris Kramer
 */
public class RepDataDetails extends RepData implements XMLElement {

    private Integer minHour;
    private Integer maxHour;
    private Set usageDetails;

    /** Creates a new instance of DetailsSet */
    private RepDataDetails() {
    }

    public RepDataDetails(DataUsageReport report) {
        super(report);
    }

    public Element toElement(Document doc, Element rootElement) {
        Element details = doc.createElement("details");
        Element hours = doc.createElement("hours");
        if (getMinHour() != null && getMaxHour() != null) {
            for (int i = getMinHour().intValue(); i <= getMaxHour().intValue(); i++) {
                hours.appendChild(DataMonitoring.createElement(doc, "hour", "" + i));
            }
            details.appendChild(hours);

            Element days = doc.createElement("days");

            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(report.getStartDate());
            startCalendar.set(Calendar.HOUR, 0);
            startCalendar.set(Calendar.MINUTE, 0);
            startCalendar.set(Calendar.SECOND, 0);
            Calendar endCalendar = Calendar.getInstance();

            endCalendar.setTime(report.getEndDate());
            endCalendar.set(Calendar.HOUR, 0);
            endCalendar.set(Calendar.MINUTE, 0);
            endCalendar.set(Calendar.SECOND, 0);
            endCalendar.add(Calendar.DAY_OF_YEAR, 1);

            while (startCalendar.before(endCalendar)) {
                days.appendChild(DataMonitoring.createElement(doc, "date", DataUsageReport.periodFormat.format(startCalendar.getTime())));
                startCalendar.add(Calendar.DAY_OF_YEAR, 1);
            }
            details.appendChild(days);

            if (usageDetails != null && usageDetails.size() > 0) {
                Iterator i = usageDetails.iterator();
                while (i.hasNext()) {
                    UsageDetails usageDetails = (UsageDetails) i.next();
                    details.appendChild(usageDetails.toElement(doc, rootElement));
                }
            }
        }
        return details;
    }

    public Integer getMinHour() {
        return minHour;
    }

    public void setMinHour(Integer minHour) {
        this.minHour = minHour;
    }

    public Integer getMaxHour() {
        return maxHour;
    }

    public void setMaxHour(Integer maxHour) {
        this.maxHour = maxHour;
    }

    public Set getUsageDetails() {
        return usageDetails;
    }

    public void setUsageDetails(Set usageDetails) {
        this.usageDetails = usageDetails;
    }
}
