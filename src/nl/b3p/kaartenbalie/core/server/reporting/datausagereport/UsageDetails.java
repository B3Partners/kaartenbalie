/*
 * UsageDetails.java
 *
 * Created on October 26, 2007, 4:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.datausagereport;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.datawarehousing.DataWarehousing;
import nl.b3p.wms.capabilities.XMLElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Chris Kramer
 */
public class UsageDetails implements XMLElement{
    
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
            if (!referenceDate.equals(dailyUsage.getDate()))
            {
                usageDetails.appendChild(dailyUsageElement);
                dailyUsageElement = doc.createElement("dailyUsage");
                referenceDate = dailyUsage.getDate();
                dailyUsageElement.setAttribute("date", DataUsageReport.periodFormat.format(dailyUsage.getDate()));
                usageDetails.appendChild(dailyUsageElement);
            }
            dailyUsageElement.appendChild(dailyUsage.toElement(doc,rootElement));
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
    public User getUser(User user) {
        try {
            return (User) DataWarehousing.find(User.class, userId);
        } catch (Exception e) {
            return null;
        }
    }
}
