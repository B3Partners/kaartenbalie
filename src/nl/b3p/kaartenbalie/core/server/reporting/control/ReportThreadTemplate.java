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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Chris Kramer
 */
public abstract class ReportThreadTemplate extends Thread {

    private static final Log log = LogFactory.getLog(ReportThreadTemplate.class);    //Owning user & organization..
    protected User user;
    protected Organization organization;
    protected ReportGenerator reportGenerator;
    protected Integer trsId;
    protected BaseReport report;
    /* entity manager alleen voor deze class onafhankelijk van thread */
    protected EntityManager em = null;

    public void init(ReportGenerator reportGenerator, User user, Map parameters) throws Exception {
        /*
         * Set all the values...
         */
        this.reportGenerator = reportGenerator;
        this.user = user;
        if (user != null) {
            this.organization = user.getOrganization();
        }
        setParameters(parameters);

        /* entity manager moet gesloten worden aan einde van de run methode */
        try {
            log.debug("Creating local entity manager ......");
            em = MyEMFDatabase.getEntityManagerFactory().createEntityManager();
        } catch (Throwable e) {
            log.warn("Error creating local entity manager: ", e);
            if (em != null && em.isOpen()) {
                em.close();
            }
            em = null;
        }
    }

    public void notifyOnQueue() throws Exception {
        notifyStateChanged(ThreadReportStatus.ONQUEUE, "The Report Generator is currently busy. Your report is on queue.", null);
    }

    protected void notifyStateChanged(int newState, String message, Integer reportId) throws Exception {
        if (em == null || !em.isOpen()) {
            log.error("No entity manager found.");
            return;
        }

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            ThreadReportStatus trs = (ThreadReportStatus) em.find(ThreadReportStatus.class, trsId);
            if (trs == null) {
                log.error("No current report found.");
                return;
            }
            trs.setState(newState);
            trs.setStatusMessage(message);
            trs.setReportId(reportId);
            tx.commit();
            if (newState == ThreadReportStatus.COMPLETED || newState == ThreadReportStatus.FAILED) {
                reportGenerator.notifyClosed(this);
            }
        } catch (Exception e) {
            log.error("", e);
            try {
                tx.rollback();
            } catch (Exception e2) {
                log.error("", e2);
            }
        }
    }

    protected void notifyBreak(Throwable e) throws Exception {
        notifyStateChanged(ThreadReportStatus.FAILED, e.getMessage(), null);
    }

    public abstract void setParameters(Map parameters) throws Exception;
}
