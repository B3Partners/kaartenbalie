/*
 * Report.java
 *
 * Created on October 23, 2007, 11:30 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.datausagereport;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.datawarehousing.DataWarehousing;
import nl.b3p.kaartenbalie.core.server.reporting.control.DataMonitoring;
import nl.b3p.kaartenbalie.core.server.reporting.domain.BaseReport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Chris Kramer
 */
public class DataUsageReport extends BaseReport {
    
    
    private Date startDate;
    private Date endDate;
    private Set reportData;
    private Set userIds;
    private Integer organizationId;
    public static SimpleDateFormat periodFormat = new SimpleDateFormat("dd-MM-yyyy");
    public DataUsageReport() {
        super();
        setReportData(new HashSet());
        userIds = new HashSet();
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
        Iterator i = getUserIds().iterator();
        while (i.hasNext()) {
            Integer userId = (Integer)i.next();
            try {
                User user = (User) DataWarehousing.find(User.class, userId);
                if (user != null) {
                    users.add(user);
                }
            }catch(Exception e) {
                e.printStackTrace();
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
    
    public Integer getOrganizationId() {
        return organizationId;
    }
    
    public void setOrganization(Organization organization) {
        if (organization != null) {
            setOrganizationId(organization.getId());
        } else {
            setOrganizationId(null);
        }
    }
    
    public Organization getOrganization() {
        try {
            return (Organization) DataWarehousing.find(Organization.class, getOrganizationId());
        } catch (Exception e) {
            return null;
        }
    }
    
    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }
    
    
    public Element toElement(Document doc, Element rootElement){
        
        Element vars = doc.createElement("vars");
        Element period = doc.createElement("period");
        period.appendChild(DataMonitoring.createElement(doc,"start", getStartDate().toString()));
        period.appendChild(DataMonitoring.createElement(doc,"end", getEndDate().toString()));
        vars.appendChild(period);
        Element users = doc.createElement("users");
        
        
        Iterator userIter = getUsers().iterator();
        while (userIter.hasNext()) {
            User tmpUser = (User) userIter.next();
            Element user = doc.createElement("user");
            user.setAttribute("id",tmpUser.getId().toString());
            user.appendChild(DataMonitoring.createElement(doc,"name", tmpUser.getFirstName() + " " + tmpUser.getSurname()));
            users.appendChild(user);
            vars.appendChild(users);
        }
        
        Element company = doc.createElement("company");
        
        Organization org = getOrganization();
        
        if (org!=null) {
            company.setAttribute("id", org.getId().toString());
            company.appendChild(DataMonitoring.createElement(doc,"name",org.getName()));
        } else {
            company.setAttribute("id", "");
            company.appendChild(DataMonitoring.createElement(doc,"name",""));
        }
        vars.appendChild(company);
        rootElement.appendChild(vars);
        
        Element data = doc.createElement("data");
        if (getReportData() != null) {
            Iterator i = getReportData().iterator();
            while (i.hasNext()) {
                RepData repData = (RepData) i.next();
                data.appendChild(repData.toElement(doc,rootElement));
            }
        }
        rootElement.appendChild(data);
        return rootElement;
    }
    
    
}




