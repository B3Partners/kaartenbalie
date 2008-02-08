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
import nl.b3p.kaartenbalie.core.server.reporting.datausagereport.DataUsageReport;
import nl.b3p.kaartenbalie.core.server.reporting.datausagereport.DataUsageReportThread;
import nl.b3p.kaartenbalie.core.server.reporting.domain.BaseReport;
import nl.b3p.kaartenbalie.core.server.reporting.domain.ThreadReportStatus;

/**
 *
 * @author Chris Kramer
 */
public abstract class ReportThreadTemplate extends Thread{
    
    
    //Owning user & organization..
    private User user;
    private Organization organization;
    private ReportGenerator reportGenerator;
    private BaseReport reportTemplate;
    private Integer trsId;
    
    protected BaseReport report;
    
    public void init(ReportGenerator reportGenerator, User user, Organization organization, Map parameters) throws Exception {
        /*
         * Set all the values...
         */
        this.reportGenerator = reportGenerator;
        this.user = user;
        this.organization = organization;
        setParameters(parameters);
        /*
         * Start a new EntityManager and transaction.
         */
        EntityManager em = MyEMFDatabase.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        try {
            /*
             * Initialize a new report.
             */
            report = (BaseReport) getReportClass().newInstance();
            report.setOwningOrganization(organization);
            /*
             * Create a new ThreadReportStatus object and persist it in the DB.
             */
            ThreadReportStatus trs = new ThreadReportStatus();
            trs.setOrganization(organization);
            trs.setState(ThreadReportStatus.CREATED);
            em.persist(trs);
            et.commit();
            /*
             * Set the trs to this threadTemplate.
             */
            trsId = trs.getId();
        } catch (Exception e) {
            e.printStackTrace();
            et.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    
    public void notifyOnQueue() {
        notifyStateChanged(ThreadReportStatus.ONQUEUE, "The Report Generator is currently busy. Your report is on queue.",null);
    }
    
    protected void notifyStateChanged(int newState, String message, Integer reportId) {
        EntityManager em = MyEMFDatabase.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        try {
            ThreadReportStatus trs = (ThreadReportStatus) em.find(ThreadReportStatus.class, trsId);
            trs.setState(newState);
            trs.setStatusMessage(message);
            trs.setReportId(reportId);
            et.commit();
        } catch (Exception e) {
            et.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        if (newState == ThreadReportStatus.COMPLETED || newState == ThreadReportStatus.FAILED) {
            reportGenerator.notifyClosed(this);
        }
    }
    
    protected void notifyBreak(Throwable e) {
        e.printStackTrace();
        notifyStateChanged(ThreadReportStatus.FAILED, e.getMessage(),null);
    }
    
    public abstract void setParameters(Map parameters) throws Exception;
    protected abstract Class getReportClass();
}
