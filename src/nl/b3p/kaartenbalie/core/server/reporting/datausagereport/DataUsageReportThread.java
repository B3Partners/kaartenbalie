/*
 * DataUsageReportThread.java
 *
 * Created on November 2, 2007, 9:45 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
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
import nl.b3p.kaartenbalie.core.server.reporting.domain.ThreadReportStatus;

/**
 *
 * @author Chris Kramer
 */
public class DataUsageReportThread extends ReportThreadTemplate{
    
    private Date startDate;
    private Date endDate;
    private Organization organization;
    private List users;
    
    public void run() {
        
        long processStart = System.currentTimeMillis();
        EntityManager em = MyEMFDatabase.createEntityManager();
        EntityTransaction et = em.getTransaction();
        super.notifyStateChanged(ThreadReportStatus.GENERATING,null,null);
        et.begin();
        
        DataUsageReport dur = (DataUsageReport) report;
        
        try {
            em.persist(report);
            
            /*
             * Store all the parameters in the report...
             */
            dur.setUsers(users);
            dur.setOrganizationId(organization.getId());
            dur.setStartDate(startDate);
            dur.setEndDate(endDate);
            
            /*
             * Fill Summary Data
             */
            super.notifyStateChanged(ThreadReportStatus.GENERATING,"Generating Summary Data",null);
            RepDataSummary rds = new RepDataSummary(dur);
            
            Integer oranizationId = organization.getId();
            //CombineImagesOperation
            Long cioHits = (Long) em.createQuery(
                    "SELECT COUNT(*) " +
                    "FROM CombineImagesOperation AS cio " +
                    "WHERE cio.clientRequest.organizationId = :organizationId " +
                    "AND cio.clientRequest.timeStamp BETWEEN :startDate AND :endDate")
                    .setParameter("startDate", startDate).setParameter("endDate", endDate)
                    .setParameter("organizationId", oranizationId)
                    .getSingleResult();
            rds.setCioHits(cioHits);
            if (cioHits != null) {
                rds.setCioAverageResponse((Double) em.createQuery(
                        "SELECT AVG(duration) " +
                        "FROM CombineImagesOperation AS cio " +
                        "WHERE cio.clientRequest.organizationId = :organizationId " +
                        "AND cio.clientRequest.timeStamp BETWEEN :startDate AND :endDate")
                        .setParameter("startDate", startDate).setParameter("endDate", endDate)
                        .setParameter("organizationId", oranizationId)
                        .getSingleResult());
            }
            //ServerTransferOperation
            Long ctoHits = (Long) em.createQuery("" +
                    "SELECT COUNT(*) " +
                    "FROM ServerTransferOperation AS sto " +
                    "WHERE sto.clientRequest.organizationId = :organizationId " +
                    "AND sto.clientRequest.timeStamp BETWEEN :startDate AND :endDate")
                    .setParameter("startDate", startDate).setParameter("endDate", endDate)
                    .setParameter("organizationId", oranizationId)
                    .getSingleResult();
            rds.setCtoHits(ctoHits);
            if (ctoHits != null) {
                rds.setCtoAverageResponse((Double) em.createQuery(
                        "SELECT AVG(duration) " +
                        "FROM ServerTransferOperation AS sto " +
                        "WHERE sto.clientRequest.organizationId = :organizationId " +
                        "AND sto.clientRequest.timeStamp BETWEEN :startDate AND :endDate")
                        .setParameter("startDate", startDate).setParameter("endDate", endDate)
                        .setParameter("organizationId", oranizationId)
                        .getSingleResult());
            }
            
            //ClientXFerOperation
            Long cxoHits = (Long) em.createQuery(
                    "SELECT COUNT(*) " +
                    "FROM ClientXFerOperation AS cxo " +
                    "WHERE cxo.clientRequest.organizationId = :organizationId " +
                    "AND cxo.clientRequest.timeStamp BETWEEN :startDate AND :endDate")
                    .setParameter("startDate", startDate).setParameter("endDate", endDate)
                    .setParameter("organizationId",oranizationId)
                    .getSingleResult();
            rds.setCxoHits(cxoHits);
            if (cxoHits != null) {
                rds.setCxoAverageResponse((Double) em.createQuery(
                        "SELECT AVG(duration) " +
                        "FROM ClientXFerOperation AS cxo " +
                        "WHERE cxo.clientRequest.organizationId = :organizationId " +
                        "AND cxo.clientRequest.timeStamp BETWEEN :startDate AND :endDate")
                        .setParameter("startDate", startDate).setParameter("endDate", endDate)
                        .setParameter("organizationId",oranizationId)
                        .getSingleResult());
                rds.setCxoData((Long) em.createQuery(
                        "SELECT SUM(dataSize) " +
                        "FROM ClientXFerOperation AS cxo " +
                        "WHERE cxo.clientRequest.organizationId = :organizationId " +
                        "AND cxo.clientRequest.timeStamp BETWEEN :startDate AND :endDate")
                        .setParameter("startDate", startDate).setParameter("endDate", endDate)
                        .setParameter("organizationId",oranizationId)
                        .getSingleResult());
            }
            //RequestOperation
            Long roHits = (Long) em.createQuery(
                    "SELECT COUNT(*) " +
                    "FROM RequestOperation AS ro " +
                    "WHERE ro.clientRequest.organizationId = :organizationId " +
                    "AND ro.clientRequest.timeStamp BETWEEN :startDate AND :endDate")
                    .setParameter("startDate", startDate).setParameter("endDate", endDate)
                    .setParameter("organizationId",oranizationId)
                    .getSingleResult();
            rds.setRoHits(roHits);
            if (roHits != null) {
                rds.setRoAverageResponse((Double) em.createQuery(
                        "SELECT AVG(duration) " +
                        "FROM RequestOperation AS ro " +
                        "WHERE ro.clientRequest.organizationId = :organizationId " +
                        "AND ro.clientRequest.timeStamp BETWEEN :startDate AND :endDate")
                        .setParameter("startDate", startDate).setParameter("endDate", endDate)
                        .setParameter("organizationId",oranizationId)
                        .getSingleResult());
                rds.setRoUpload((Long) em.createQuery(
                        "SELECT SUM(bytesReceivedFromUser) " +
                        "FROM RequestOperation AS ro " +
                        "WHERE ro.clientRequest.organizationId = :organizationId " +
                        "AND ro.clientRequest.timeStamp BETWEEN :startDate AND :endDate")
                        .setParameter("startDate", startDate).setParameter("endDate", endDate)
                        .setParameter("organizationId",oranizationId)
                        .getSingleResult());
                rds.setRoDownload((Long) em.createQuery(
                        "SELECT SUM(bytesSendToUser) " +
                        "FROM RequestOperation AS ro " +
                        "WHERE ro.clientRequest.organizationId = :organizationId " +
                        "AND ro.clientRequest.timeStamp BETWEEN :startDate AND :endDate")
                        .setParameter("startDate", startDate).setParameter("endDate", endDate)
                        .setParameter("organizationId",oranizationId)
                        .getSingleResult());
            }
            em.persist(rds);
            em.flush();
            /*
             * Fill Detail Data
             */
            super.notifyStateChanged(ThreadReportStatus.GENERATING,"Generating Detailed Data",null);
            RepDataDetails rdd = new RepDataDetails(dur);
            rdd.setMaxHour((Integer) em.createQuery(
                    "SELECT MAX(HOUR(timeStamp)) " +
                    "FROM ClientRequest AS cr " +
                    "WHERE cr.organizationId = :organizationId " +
                    "AND cr.timeStamp BETWEEN :startDate AND :endDate")
                    .setParameter("startDate", startDate).setParameter("endDate", endDate)
                    .setParameter("organizationId",oranizationId)
                    .getSingleResult());
            rdd.setMinHour((Integer) em.createQuery(
                    "SELECT MIN(HOUR(timeStamp)) " +
                    "FROM ClientRequest AS cr " +
                    "WHERE cr.organizationId = :organizationId " +
                    "AND cr.timeStamp BETWEEN :startDate AND :endDate")
                    .setParameter("startDate", startDate).setParameter("endDate", endDate)
                    .setParameter("organizationId",oranizationId)
                    .getSingleResult());
            em.persist(rdd);
            /*
             * Get the Daily Usages...
             */
            em.flush();
            Iterator iterUsers = dur.getUsers().iterator();
            
            while(iterUsers.hasNext()) {
                User user  = (User) iterUsers.next();
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
                        "ORDER BY cr.timeStamp ASC"
                        )
                        .setParameter("startDate", startDate).setParameter("endDate", endDate)
                        .setParameter("userId",user.getId())
                        .setParameter("organizationId",oranizationId)
                        .getResultList();
                Iterator i = list.iterator();
                while (i.hasNext()) {
                    Object[] object = (Object[])i.next();
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
            et.commit();
        } catch (Throwable e) {
            e.printStackTrace();
            et.rollback();
            super.notifyBreak(e);
            em.close();
            return;
        }
        
        
        super.notifyStateChanged(ThreadReportStatus.COMPLETED,null, report.getId());
        em.close();
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
            cal.set(Calendar.HOUR_OF_DAY,23);
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
