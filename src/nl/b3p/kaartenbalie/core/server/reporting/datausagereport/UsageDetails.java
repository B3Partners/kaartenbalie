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
import java.util.Iterator;
import java.util.Set;
import javax.persistence.EntityManager;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.wms.capabilities.XMLElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Chris Kramer
 */
public class UsageDetails implements XMLElement {

    private Integer id;
    private RepDataDetails reportDataDetails;
    private Set dailyUsages;
    private Integer userId;

    private UsageDetails() {
    }

    public UsageDetails(RepDataDetails reportDataDetails, User user) {
        this.setReportDataDetails(reportDataDetails);
        this.setUserId(user.getId());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RepDataDetails getReportDataDetails() {
        return reportDataDetails;
    }

    public void setReportDataDetails(RepDataDetails reportDataDetails) {
        this.reportDataDetails = reportDataDetails;
    }

    public Set getDailyUsages() {
        return dailyUsages;
    }

    public void setDailyUsages(Set dailyUsages) {
        this.dailyUsages = dailyUsages;
    }

    public Element toElement(Document doc, Element rootElement) {
        Element usageDetails = doc.createElement("usagedetails");

        usageDetails.setAttribute("userid", getUserId().toString());


        Iterator i = dailyUsages.iterator();
        Date referenceDate = null;
        Element dailyUsageElement = null;
        while (i.hasNext()) {

            DailyUsage dailyUsage = (DailyUsage) i.next();
            if (referenceDate == null) {
                referenceDate = dailyUsage.getDate();
                dailyUsageElement = doc.createElement("dailyUsage");
                dailyUsageElement.setAttribute("date", DataUsageReport.periodFormat.format(dailyUsage.getDate()));
                usageDetails.appendChild(dailyUsageElement);
            }
            if (!referenceDate.equals(dailyUsage.getDate())) {
                usageDetails.appendChild(dailyUsageElement);
                dailyUsageElement = doc.createElement("dailyUsage");
                referenceDate = dailyUsage.getDate();
                dailyUsageElement.setAttribute("date", DataUsageReport.periodFormat.format(dailyUsage.getDate()));
                usageDetails.appendChild(dailyUsageElement);
            }
            dailyUsageElement.appendChild(dailyUsage.toElement(doc, rootElement));
        }
        return usageDetails;

    }

    private Integer getUserId() {
        return userId;
    }

    private void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUser(User user) {
        if (user != null) {
            setUserId(user.getId());
        } else {
            setUserId(null);
        }
    }

    public User getUser(User user, EntityManager em) {
        try {
            return (User) em.find(User.class, userId);
        } catch (Exception e) {
            return null;
        }
    }
}
