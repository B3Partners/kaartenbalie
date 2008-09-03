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
package nl.b3p.kaartenbalie.core.server.reporting.datausagereport;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.core.server.reporting.control.ReportThreadTemplate;
import nl.b3p.kaartenbalie.core.server.reporting.domain.BaseReport;
import nl.b3p.kaartenbalie.core.server.reporting.domain.ThreadReportStatus;
import nl.b3p.kaartenbalie.core.server.reporting.domain.tables.DataTable;
import nl.b3p.kaartenbalie.core.server.reporting.domain.tables.RowValue;
import nl.b3p.kaartenbalie.core.server.reporting.domain.tables.TableRow;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Chris Kramer
 */
public class DataUsageReportThread extends ReportThreadTemplate {

    private static final Log log = LogFactory.getLog(DataUsageReportThread.class);
    private Date startDate;
    private Date endDate;
    private List users;

    public void run() {
        Object identity = null;
        try {
            identity = MyEMFDatabase.createEntityManager(MyEMFDatabase.INIT_EM);
            log.debug("Getting entity manager ......");
            em = MyEMFDatabase.getEntityManager(MyEMFDatabase.INIT_EM);
            EntityTransaction tx = em.getTransaction();
            tx.begin();

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

            try {
                long processStart = System.currentTimeMillis();
                super.notifyStateChanged(ThreadReportStatus.GENERATING, null, null);

                DataUsageReport dur = (DataUsageReport) report;

                em.persist(report);
                em.flush();

                /*
                 * Store all the parameters in the report...
                 */
                dur.setUsers(users);
                dur.setOrganizationId(organization.getId());
                dur.setStartDate(startDate);
                dur.setEndDate(endDate);


                Integer oranizationId = organization.getId();
                /*
                 * Create some table data about average time split by ms!
                 */

                DataTable dt = new DataTable(report, "FrequencyResponseTimeMatrix");
                TableRow header = new TableRow();
                header.setHeader(new Boolean(true));

                RowValue range = new RowValue("Range (ms)");
                header.addValue(range);
                em.persist(range);

                RowValue frequency = new RowValue("Frequency");
                header.addValue(frequency);
                em.persist(frequency);

                dt.addRow(header);
                em.persist(header);
                em.persist(dt);
                int stepSize = 1000;
                int maxRange = stepSize * 22;
                int start = 1000;
                for (int i = start; i <= maxRange; i += stepSize) {
                    double percentageDone = ((double) i / (double) maxRange) * 100.0;
                    super.notifyStateChanged(ThreadReportStatus.GENERATING, "FrequencyResponseTimeMatrix " + Math.round(percentageDone) + "% done.", null);
                    TableRow tr = new TableRow();
                    dt.addRow(tr);
                    em.persist(tr);
                    //Add Range Value
                    RowValue tdRange = new RowValue("" + i);
                    tr.addValue(tdRange);
                    em.persist(tdRange);

                    Long msLow = null;
                    if (i - stepSize < 0) {
                        msLow = new Long(0);
                    } else {
                        msLow = new Long(i - stepSize);
                    }

                    Long frequencyHits = (Long) em.createQuery(
                            "SELECT COUNT(*) FROM RequestOperation AS ro " +
                            "WHERE ro.clientRequest.organizationId = :organizationId " +
                            "AND ro.clientRequest.timeStamp BETWEEN :startDate AND :endDate " +
                            "AND ro.duration BETWEEN :msLow AND :msHigh ").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("organizationId", oranizationId).setParameter("msLow", msLow).setParameter("msHigh", new Long(i)).getSingleResult();

                    RowValue tdFrequency = new RowValue(frequencyHits.toString());
                    tr.addValue(tdFrequency);
                    em.persist(tdFrequency);

                }

                //Now everything larger then maxRange

                TableRow tr = new TableRow();
                dt.addRow(tr);
                em.persist(tr);
                //Add Range Value
                RowValue tdRange = new RowValue("> " + maxRange);
                tr.addValue(tdRange);
                em.persist(tdRange);

                Long frequencyHits = (Long) em.createQuery(
                        "SELECT COUNT(*) FROM RequestOperation AS ro " +
                        "WHERE ro.clientRequest.organizationId = :organizationId " +
                        "AND ro.clientRequest.timeStamp BETWEEN :startDate AND :endDate " +
                        "AND ro.duration > :msLow ").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("organizationId", oranizationId).setParameter("msLow", new Long(maxRange)).getSingleResult();

                RowValue tdFrequency = new RowValue(frequencyHits.toString());
                tr.addValue(tdFrequency);
                em.persist(tdFrequency);

                /*
                 * Fill Summary Data
                 */
                super.notifyStateChanged(ThreadReportStatus.GENERATING, "Generating Summary Data", null);
                RepDataSummary rds = new RepDataSummary(dur);


                //CombineImagesOperation
                Long cioHits = (Long) em.createQuery(
                        "SELECT COUNT(*) " +
                        "FROM CombineImagesOperation AS cio " +
                        "WHERE cio.clientRequest.organizationId = :organizationId " +
                        "AND cio.clientRequest.timeStamp BETWEEN :startDate AND :endDate").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("organizationId", oranizationId).getSingleResult();
                rds.setCioHits(cioHits);
                if (cioHits != null) {
                    rds.setCioAverageResponse((Double) em.createQuery(
                            "SELECT AVG(duration) " +
                            "FROM CombineImagesOperation AS cio " +
                            "WHERE cio.clientRequest.organizationId = :organizationId " +
                            "AND cio.clientRequest.timeStamp BETWEEN :startDate AND :endDate").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("organizationId", oranizationId).getSingleResult());
                }
                //ServerTransferOperation
                Long ctoHits = (Long) em.createQuery("" +
                        "SELECT COUNT(*) " +
                        "FROM ServerTransferOperation AS sto " +
                        "WHERE sto.clientRequest.organizationId = :organizationId " +
                        "AND sto.clientRequest.timeStamp BETWEEN :startDate AND :endDate").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("organizationId", oranizationId).getSingleResult();
                rds.setCtoHits(ctoHits);
                if (ctoHits != null) {
                    rds.setCtoAverageResponse((Double) em.createQuery(
                            "SELECT AVG(duration) " +
                            "FROM ServerTransferOperation AS sto " +
                            "WHERE sto.clientRequest.organizationId = :organizationId " +
                            "AND sto.clientRequest.timeStamp BETWEEN :startDate AND :endDate").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("organizationId", oranizationId).getSingleResult());
                }

                //ClientXFerOperation
                Long cxoHits = (Long) em.createQuery(
                        "SELECT COUNT(*) " +
                        "FROM ClientXFerOperation AS cxo " +
                        "WHERE cxo.clientRequest.organizationId = :organizationId " +
                        "AND cxo.clientRequest.timeStamp BETWEEN :startDate AND :endDate").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("organizationId", oranizationId).getSingleResult();
                rds.setCxoHits(cxoHits);
                if (cxoHits != null) {
                    rds.setCxoAverageResponse((Double) em.createQuery(
                            "SELECT AVG(duration) " +
                            "FROM ClientXFerOperation AS cxo " +
                            "WHERE cxo.clientRequest.organizationId = :organizationId " +
                            "AND cxo.clientRequest.timeStamp BETWEEN :startDate AND :endDate").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("organizationId", oranizationId).getSingleResult());
                    rds.setCxoData((Long) em.createQuery(
                            "SELECT SUM(dataSize) " +
                            "FROM ClientXFerOperation AS cxo " +
                            "WHERE cxo.clientRequest.organizationId = :organizationId " +
                            "AND cxo.clientRequest.timeStamp BETWEEN :startDate AND :endDate").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("organizationId", oranizationId).getSingleResult());
                }
                //RequestOperation
                Long roHits = (Long) em.createQuery(
                        "SELECT COUNT(*) " +
                        "FROM RequestOperation AS ro " +
                        "WHERE ro.clientRequest.organizationId = :organizationId " +
                        "AND ro.clientRequest.timeStamp BETWEEN :startDate AND :endDate").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("organizationId", oranizationId).getSingleResult();
                rds.setRoHits(roHits);
                if (roHits != null) {
                    rds.setRoAverageResponse((Double) em.createQuery(
                            "SELECT AVG(duration) " +
                            "FROM RequestOperation AS ro " +
                            "WHERE ro.clientRequest.organizationId = :organizationId " +
                            "AND ro.clientRequest.timeStamp BETWEEN :startDate AND :endDate").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("organizationId", oranizationId).getSingleResult());
                    rds.setRoUpload((Long) em.createQuery(
                            "SELECT SUM(bytesReceivedFromUser) " +
                            "FROM RequestOperation AS ro " +
                            "WHERE ro.clientRequest.organizationId = :organizationId " +
                            "AND ro.clientRequest.timeStamp BETWEEN :startDate AND :endDate").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("organizationId", oranizationId).getSingleResult());
                    rds.setRoDownload((Long) em.createQuery(
                            "SELECT SUM(bytesSendToUser) " +
                            "FROM RequestOperation AS ro " +
                            "WHERE ro.clientRequest.organizationId = :organizationId " +
                            "AND ro.clientRequest.timeStamp BETWEEN :startDate AND :endDate").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("organizationId", oranizationId).getSingleResult());
                }
                em.persist(rds);
                em.flush();
                /*
                 * Fill Detail Data
                 */
                super.notifyStateChanged(ThreadReportStatus.GENERATING, "Generating Detailed Data", null);
                RepDataDetails rdd = new RepDataDetails(dur);
                rdd.setMaxHour((Integer) em.createQuery(
                        "SELECT MAX(HOUR(timeStamp)) " +
                        "FROM ClientRequest AS cr " +
                        "WHERE cr.organizationId = :organizationId " +
                        "AND cr.timeStamp BETWEEN :startDate AND :endDate").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("organizationId", oranizationId).getSingleResult());
                rdd.setMinHour((Integer) em.createQuery(
                        "SELECT MIN(HOUR(timeStamp)) " +
                        "FROM ClientRequest AS cr " +
                        "WHERE cr.organizationId = :organizationId " +
                        "AND cr.timeStamp BETWEEN :startDate AND :endDate").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("organizationId", oranizationId).getSingleResult());
                em.persist(rdd);
                /*
                 * Get the Daily Usages...
                 */
                em.flush();
                Iterator iterUsers = dur.getUsers().iterator();

                while (iterUsers.hasNext()) {
                    User user = (User) iterUsers.next();
                    UsageDetails usageDetails = new UsageDetails(rdd, user);
                    em.persist(usageDetails);


                    //Complex query to get the hits, date in string format, hour, bytes uploaded and bytes downloaded.
                    List list = em.createQuery(
                            "SELECT COUNT(DISTINCT cr) AS hits, DATE_FORMAT(cr.timeStamp,'%d-%m-%Y') AS tsDate, HOUR(cr.timeStamp) AS tsHour, SUM(ro.bytesReceivedFromUser), SUM(ro.bytesSendToUser)" +
                            "FROM ClientRequest AS cr " +
                            "LEFT JOIN cr.requestOperations AS ro " +
                            "WHERE cr.userId = :userId " +
                            "AND cr.timeStamp BETWEEN :startDate AND :endDate " +
                            "AND cr.organizationId = :organizationId " +
                            "GROUP BY DATE_FORMAT(cr.timeStamp,'%d-%m-%Y'), HOUR(cr.timeStamp) " +
                            "ORDER BY cr.timeStamp ASC").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("userId", user.getId()).setParameter("organizationId", oranizationId).getResultList();
                    Iterator i = list.iterator();
                    while (i.hasNext()) {
                        Object[] object = (Object[]) i.next();
                        DailyUsage dailyUsage = new DailyUsage(usageDetails);
                        dailyUsage.setHits((Long) object[0]);
                        try {
                            dailyUsage.setDate(DataUsageReport.periodFormat.parse((String) object[1]));
                        } catch (ParseException ex) {
                        }
                        dailyUsage.setHour((Integer) object[2]);
                        Long upload = (Long) object[3];
                        Long download = (Long) object[4];
                        if (upload == null) {
                            upload = new Long(0);
                        }
                        if (download == null) {
                            download = new Long(0);
                        }
                        Long data = new Long(upload.intValue() + download.intValue());
                        dailyUsage.setDataUsage(data);
                        em.persist(dailyUsage);
                    }
                }
                report.setProcessingTime(new Long(System.currentTimeMillis() - processStart));
                em.flush();

                super.notifyStateChanged(ThreadReportStatus.COMPLETED, null, report.getId());
            } catch (Exception e) {
                try {
                    super.notifyBreak(e);
                    log.error("", e);
                } catch (Exception ex) {
                    log.error("", ex);
                }
                log.error("", e);
            }
        } catch (Throwable e) {
            log.warn("Error creating EntityManager: ", e);
        } finally {
            log.debug("Closing entity manager .....");
            MyEMFDatabase.closeEntityManager(identity, MyEMFDatabase.INIT_EM);
        }
    }

    public void setParameters(Map parameters) throws Exception {
        if (parameters != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime((Date) parameters.get("startDate"));
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            startDate = cal.getTime();
            cal.setTime((Date) parameters.get("endDate"));
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 99);
            endDate = cal.getTime();
            organization = (Organization) parameters.get("organization");
            users = (List) parameters.get("users");
        }
    }

    protected Class getReportClass() {
        return DataUsageReport.class;
    }
}
