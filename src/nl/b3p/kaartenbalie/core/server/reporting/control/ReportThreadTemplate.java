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
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.core.server.reporting.domain.ReportTemplate;
import nl.b3p.kaartenbalie.core.server.reporting.domain.ThreadReportStatus;
import org.hibernate.connection.UserSuppliedConnectionProvider;

/**
 *
 * @author Chris Kramer
 */
public abstract class ReportThreadTemplate extends Thread{
    
    
    protected ReportGenerator reportGenerator;
    protected Map parameters;
    protected User user;
    protected EntityManager em;
    protected ReportTemplate reportTemplate;
    private Integer trsId;
    
    public ReportThreadTemplate() {
        em = MyEMFDatabase.createEntityManager();
    }
    public void init() {
        EntityTransaction et = em.getTransaction();
        et.begin();
        ThreadReportStatus trs = new ThreadReportStatus();
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
            reportGenerator.notifyClosed(this);
        } else if (newState == ThreadReportStatus.FAILED) {
            reportGenerator.notifyClosed(this);
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
    
    
    
    
    
}
