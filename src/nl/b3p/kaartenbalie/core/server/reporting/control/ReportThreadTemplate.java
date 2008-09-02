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
        EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);
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
        em.flush();
        /*
         * Set the trs to this threadTemplate.
         */
        trsId = trs.getId();
    }

    public void notifyOnQueue() throws Exception {
        notifyStateChanged(ThreadReportStatus.ONQUEUE, "The Report Generator is currently busy. Your report is on queue.", null);
    }
    /*
    
    2008-08-28 22:08:56,108 [Thread-11] WARN  org.hibernate.util.JDBCExceptionReporter - SQL Error: 1452, SQLState: 23000
    2008-08-28 22:08:56,108 [Thread-11] ERROR org.hibernate.util.JDBCExceptionReporter - Cannot add or update a child row: a foreign key constraint fails (`kaartenbalie_test/rep_tablerow`, CONSTRAINT `FKDB7E1CAEEDED1520` FOREIGN KEY (`tro_tab_id`) REFERENCES `rep_table` (`tab_id`))
    2008-08-28 22:08:56,111 [Thread-11] ERROR org.hibernate.event.def.AbstractFlushingEventListener - Could not synchronize database state with session
    org.hibernate.exception.ConstraintViolationException: could not insert: [nl.b3p.kaartenbalie.core.server.reporting.domain.tables.TableRow]
    at org.hibernate.exception.SQLStateConverter.convert(SQLStateConverter.java:71)
    at org.hibernate.exception.JDBCExceptionHelper.convert(JDBCExceptionHelper.java:43)
    at org.hibernate.id.insert.AbstractReturningDelegate.performInsert(AbstractReturningDelegate.java:40)
    at org.hibernate.persister.entity.AbstractEntityPersister.insert(AbstractEntityPersister.java:2158)
    at org.hibernate.persister.entity.AbstractEntityPersister.insert(AbstractEntityPersister.java:2638)
    at org.hibernate.action.EntityIdentityInsertAction.execute(EntityIdentityInsertAction.java:48)
    at org.hibernate.engine.ActionQueue.execute(ActionQueue.java:250)
    at org.hibernate.engine.ActionQueue.executeActions(ActionQueue.java:234)
    at org.hibernate.engine.ActionQueue.executeActions(ActionQueue.java:141)
    at org.hibernate.event.def.AbstractFlushingEventListener.performExecutions(AbstractFlushingEventListener.java:298)
    at org.hibernate.event.def.DefaultFlushEventListener.onFlush(DefaultFlushEventListener.java:27)
    at org.hibernate.impl.SessionImpl.flush(SessionImpl.java:1000)
    at org.hibernate.impl.SessionImpl.managedFlush(SessionImpl.java:338)
    at org.hibernate.transaction.JDBCTransaction.commit(JDBCTransaction.java:106)
    at org.hibernate.ejb.TransactionImpl.commit(TransactionImpl.java:54)
    at nl.b3p.kaartenbalie.core.server.reporting.control.ReportThreadTemplate.notifyStateChanged(ReportThreadTemplate.java:110)
    at nl.b3p.kaartenbalie.core.server.reporting.datausagereport.DataUsageReportThread.run(DataUsageReportThread.java:105)
    Caused by: java.sql.SQLException: Cannot add or update a child row: a foreign key constraint fails (`kaartenbalie_test/rep_tablerow`, CONSTRAINT `FKDB7E1CAEEDED1520` FOREIGN KEY (`tro_tab_id`) REFERENCES `rep_table` (`tab_id`))
    at com.mysql.jdbc.MysqlIO.checkErrorPacket(MysqlIO.java:2851)
    at com.mysql.jdbc.MysqlIO.sendCommand(MysqlIO.java:1531)
    at com.mysql.jdbc.ServerPreparedStatement.serverExecute(ServerPreparedStatement.java:1366)
    at com.mysql.jdbc.ServerPreparedStatement.executeInternal(ServerPreparedStatement.java:952)
    at com.mysql.jdbc.PreparedStatement.executeUpdate(PreparedStatement.java:1974)
    at com.mysql.jdbc.PreparedStatement.executeUpdate(PreparedStatement.java:1897)
    at com.mysql.jdbc.PreparedStatement.executeUpdate(PreparedStatement.java:1758)
    at org.apache.tomcat.dbcp.dbcp.DelegatingPreparedStatement.executeUpdate(DelegatingPreparedStatement.java:102)
    at org.hibernate.id.IdentityGenerator$GetGeneratedKeysDelegate.executeAndExtract(IdentityGenerator.java:73)
    at org.hibernate.id.insert.AbstractReturningDelegate.performInsert(AbstractReturningDelegate.java:33)
    ... 14 more
    
     */

    protected void notifyStateChanged(int newState, String message, Integer reportId) throws Exception {
        EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);
        ThreadReportStatus trs = (ThreadReportStatus) em.find(ThreadReportStatus.class, trsId);
        trs.setState(newState);
        trs.setStatusMessage(message);
        trs.setReportId(reportId);
        em.flush();
        if (newState == ThreadReportStatus.COMPLETED || newState == ThreadReportStatus.FAILED) {
            reportGenerator.notifyClosed(this);
        }
    }

    protected void notifyBreak(Throwable e) throws Exception {
        notifyStateChanged(ThreadReportStatus.FAILED, e.getMessage(), null);
    }

    public abstract void setParameters(Map parameters) throws Exception;

    protected abstract Class getReportClass();
}
