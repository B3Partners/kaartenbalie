/*
 * DetailsSet.java
 *
 * Created on October 26, 2007, 11:56 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.domain.generation;

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
    public RepDataDetails(Report report) {
        super(report);
    }
    
    public Element toElement(Document doc, Element rootElement) {
        Element details = doc.createElement("details");
        Element hours = doc.createElement("hours");
        for (int i = getMinHour().intValue(); i <= getMaxHour().intValue(); i++) {
            hours.appendChild(DataMonitoring.createElement(doc,"hour", "" + i));
        }
        details.appendChild(hours);
        
        Element days = doc.createElement("days");
        
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(report.getStartDate());
        startCalendar.set(Calendar.HOUR,0);
        startCalendar.set(Calendar.MINUTE,0);
        startCalendar.set(Calendar.SECOND,0);
        Calendar endCalendar = Calendar.getInstance();
        
        endCalendar.setTime(report.getEndDate());
        endCalendar.set(Calendar.HOUR,0);
        endCalendar.set(Calendar.MINUTE,0);
        endCalendar.set(Calendar.SECOND,0);
        endCalendar.add(Calendar.DAY_OF_YEAR,1);
        
        while(startCalendar.before(endCalendar)) {
            days.appendChild(DataMonitoring.createElement(doc,"date", Report.periodFormat.format(startCalendar.getTime())));
            startCalendar.add(Calendar.DAY_OF_YEAR,1);
        }
        details.appendChild(days);
        
        if (usageDetails != null && usageDetails.size() > 0) {
            Iterator i = usageDetails.iterator();
            while (i.hasNext()) {
                UsageDetails usageDetails = (UsageDetails) i.next();
                details.appendChild(usageDetails.toElement(doc,rootElement));
            }
            
        }
        
        /*
        if (report.getUsers() != null && report.getUsers().size() != 0) {
            //TODO
        } else {
            Element usagedetails = doc.createElement("usagedetails");
            usagedetails.setAttribute("userid", "-1");
         
            details.appendChild(usagedetails);
        }
         */
        
        
        
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
