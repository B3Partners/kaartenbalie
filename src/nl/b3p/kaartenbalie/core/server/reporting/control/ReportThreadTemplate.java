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
package nl.b3p.kaartenbalie.core.server.reporting.control;

import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.core.server.reporting.domain.BaseReport;
import nl.b3p.kaartenbalie.core.server.reporting.domain.ThreadReportStatus;

/**
 *
 * @author Chris Kramer
 */
public abstract class ReportThreadTemplate extends Thread {
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
            et.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void notifyOnQueue() {
        notifyStateChanged(ThreadReportStatus.ONQUEUE, "The Report Generator is currently busy. Your report is on queue.", null);
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
        } finally {
            em.close();
        }
        if (newState == ThreadReportStatus.COMPLETED || newState == ThreadReportStatus.FAILED) {
            reportGenerator.notifyClosed(this);
        }
    }

    protected void notifyBreak(Throwable e) {
        notifyStateChanged(ThreadReportStatus.FAILED, e.getMessage(), null);
    }

    public abstract void setParameters(Map parameters) throws Exception;

    protected abstract Class getReportClass();
}
