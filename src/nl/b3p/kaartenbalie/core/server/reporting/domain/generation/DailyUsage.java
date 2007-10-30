/*
 * DailyUsage.java
 *
 * Created on October 26, 2007, 3:51 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.domain.generation;

import java.util.Date;
import nl.b3p.wms.capabilities.XMLElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Chris Kramer
 */
public class DailyUsage implements XMLElement  {
    
    private Integer id;
    private Date date;
    private Integer hour;
    private Long dataUsage;
    private Long hits;
    private UsageDetails usageDetails;
    private  DailyUsage() {
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
