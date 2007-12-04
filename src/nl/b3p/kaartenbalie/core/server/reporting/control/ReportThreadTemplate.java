/*
 * ReportThreadTemplate.java
 *
 * Created on November 2, 2007, 9:42 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.control;

import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.core.server.reporting.domain.ReportTemplate;
import nl.b3p.kaartenbalie.core.server.reporting.domain.ThreadReportStatus;

/**
 *
 * @author Chris Kramer
 */
public abstract class ReportThreadTemplate extends Thread{
    
    
    private ReportGenerator reportGenerator;
    private Map parameters;
    private User user;
    private Organization organization;
    private EntityManager em;
    private ReportTemplate reportTemplate;
    private Integer trsId;
    
    public ReportThreadTemplate() {
        em = MyEMFDatabase.createEntityManager();
    }
    public void init() {
        EntityTransaction et = em.getTransaction();
        et.begin();
        ThreadReportStatus trs = new ThreadReportStatus();
        trs.setOrganization(organization);
        trs.setState(ThreadReportStatus.CREATED);
        em.persist(trs);
        et.commit();
        em.clear();
        trsId = trs.getId();
    }
    public void notifyOnQueue() {
        notifyStateChanged(ThreadReportStatus.ONQUEUE, "The Report Generator is currently busy. Your report is on queue.",null);
    }
    protected void notifyStateChanged(int newState, String message, Integer reportId) {
        EntityTransaction et = em.getTransaction();
        et.begin();
        ThreadReportStatus trs = (ThreadReportStatus) em.find(ThreadReportStatus.class, trsId);
        trs.setState(newState);
        trs.setStatusMessage(message);
        
        trs.setReportId(reportId);
        et.commit();
        
        if (newState == ThreadReportStatus.COMPLETED) {
            trs.setReportId(reportId);
            getReportGenerator().notifyClosed(this);
        } else if (newState == ThreadReportStatus.FAILED) {
            getReportGenerator().notifyClosed(this);
        } else {
            
        }
        em.clear();
    }
    
    protected void notifyBreak(Throwable e) {
        
        e.printStackTrace();
        notifyStateChanged(ThreadReportStatus.FAILED, e.getMessage(),null);
    }
    
    public abstract void setParameters(Map parameters) throws Exception;
    public void setUser(User user) {
        this.user = user;
    }
    public void setReportGenerator(ReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
    }
    
    public ReportGenerator getReportGenerator() {
        return reportGenerator;
    }
    
    public Map getParameters() {
        return parameters;
    }
    
    public User getUser() {
        return user;
    }
    
    public Organization getOrganization() {
        return organization;
    }
    
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
    
    
    
    
    
    
}
