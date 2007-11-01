/*
 * Report.java
 *
 * Created on October 23, 2007, 11:30 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.domain.generation;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.datawarehousing.Warehouse;
import nl.b3p.kaartenbalie.core.server.reporting.control.DataMonitoring;
import nl.b3p.wms.capabilities.XMLElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Chris Kramer
 */
public class Report implements XMLElement {
    
    private Integer id;
    private Date reportDate;
    private Date startDate;
    private Date endDate;
    private Long processingTime;
    private Set reportData;
    private Set userIds;
    public static SimpleDateFormat periodFormat = new SimpleDateFormat("dd-MM-yyyy");
    private Report() {
        setReportDate(new Date());
        setReportData(new HashSet());
        userIds = new HashSet();
    }
    
    public Report(Date startDate, Date endDate) {
        this();
        this.setStartDate(startDate);
        this.setEndDate(endDate);
    }
    
    
    public Element toElement(Document doc, Element rootElement) {
        Element report = doc.createElement("report");
        
        report.setAttribute("id", getId().toString());
        report.setAttribute("processingTime", getProcessingTime().toString());
        report.setAttribute("date", getReportDate().toString());
        
        Element vars = doc.createElement("vars");
        Element period = doc.createElement("period");
        period.appendChild(DataMonitoring.createElement(doc,"start", getStartDate().toString()));
        period.appendChild(DataMonitoring.createElement(doc,"end", getEndDate().toString()));
        vars.appendChild(period);
        Element users = doc.createElement("users");
        //TODO For Each Loop door users in het rapport, for now just add a global user...
        
        Iterator userIter = getUsers().iterator();
        while (userIter.hasNext())
        {
            User tmpUser = (User) userIter.next();
            Element user = doc.createElement("user");
            user.setAttribute("id",tmpUser.getId().toString());
            user.appendChild(DataMonitoring.createElement(doc,"name", tmpUser.getFirstName() + " " + tmpUser.getSurname()));
            users.appendChild(user);
            vars.appendChild(users);
        }

        Element company = doc.createElement("company");
        //TODO do something with company info here!
        company.setAttribute("id", null);
        company.appendChild(DataMonitoring.createElement(doc,"name",null));
        vars.appendChild(company);
        report.appendChild(vars);
        
        Element data = doc.createElement("data");
        if (getReportData() != null) {
            
            Iterator i = getReportData().iterator();
            while (i.hasNext()) {
                RepData repData = (RepData) i.next();
                data.appendChild(repData.toElement(doc,rootElement));
            }
        }
        
        
        report.appendChild(data);
        return report;
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
    
    public Date getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public Long getProcessingTime() {
        return processingTime;
    }
    
    public void setProcessingTime(Long processingTime) {
        this.processingTime = processingTime;
    }
    
    public Set getReportData() {
        return reportData;
    }
    
    public void setReportData(Set reportData) {
        this.reportData = reportData;
    }
    
    private Set getUserIds() {
        return userIds;
    }
    
    public void setUserIds(Set userIds) {
        this.userIds = userIds;
    }
    

    public Set getUsers() {
        Set users = new HashSet();
        Iterator i = userIds.iterator();
        while (i.hasNext()) {
            Integer userId = (Integer)i.next();
            try {
                User user = (User) Warehouse.find(User.class, userId);
                if (user != null) {
                    users.add(user);
                }
            }catch(Exception e) {
                
            }
        }
        return users;
    }
    public void setUsers(Collection users) {
        setUserIds(new HashSet());
        if (users != null) {
            Iterator i = users.iterator();
            while (i.hasNext()) {
                User user = (User)i.next();
                getUserIds().add(user.getId());
            }
        }
    }
    
}




