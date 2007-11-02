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
        super.notifyStateChanged(ThreadReportStatus.GENERATING,null);
        et.begin();
        try {
            DataUsageReport report = new DataUsageReport(startDate, endDate);
            
            //Used to make sure the progress takes some while...
            /*
            long sleep = (long)(8000*Math.random()) + 2000;
            super.notifyStateChanged(ThreadReportStatus.GENERATING,"Taking a nap for " + sleep + "ms.....");
            sleep(sleep);
            sleep = (long)(4000*Math.random()) + 1000;
            super.notifyStateChanged(ThreadReportStatus.GENERATING,"*yawning for " + sleep + "ms*");
            sleep(sleep);
            */
            sleep(1000);
            em.persist(report);
            /*
             * Add the users... 
             */
            report.setUsers(users);
            /*
             * Fill Summary Data
             */
            super.notifyStateChanged(ThreadReportStatus.GENERATING,"Generating Summary Data");
            RepDataSummary rds = new RepDataSummary(report);
            //CombineImagesOperation
            Long cioHits = (Long) em.createQuery("SELECT COUNT(*) FROM CombineImagesOperation").getSingleResult();
            rds.setCioHits(cioHits);
            if (cioHits != null) {
                rds.setCioAverageResponse((Double) em.createQuery("SELECT AVG(duration) FROM CombineImagesOperation ").getSingleResult());
            }
            //ServerTransferOperation
            Long ctoHits = (Long) em.createQuery("SELECT COUNT(*) FROM ServerTransferOperation").getSingleResult();
            rds.setCtoHits(ctoHits);
            if (ctoHits != null) {
                rds.setCtoAverageResponse((Double) em.createQuery("SELECT AVG(duration) FROM ServerTransferOperation ").getSingleResult());
            }
            
            //ClientXFerOperation
            Long cxoHits = (Long) em.createQuery("SELECT COUNT(*) FROM ClientXFerOperation").getSingleResult();
            rds.setCxoHits(cxoHits);
            if (cxoHits != null) {
                rds.setCxoAverageResponse((Double) em.createQuery("SELECT AVG(duration) FROM ClientXFerOperation ").getSingleResult());
                rds.setCxoData((Long) em.createQuery("SELECT SUM(dataSize) FROM ClientXFerOperation").getSingleResult());
            }
            //RequestOperation
            Long roHits = (Long) em.createQuery("SELECT COUNT(*) FROM RequestOperation").getSingleResult();
            rds.setRoHits(roHits);
            if (roHits != null) {
                rds.setRoAverageResponse((Double) em.createQuery("SELECT AVG(duration) FROM RequestOperation ").getSingleResult());
                rds.setRoUpload((Long) em.createQuery("SELECT SUM(bytesReceivedFromUser) FROM RequestOperation").getSingleResult());
                rds.setRoDownload((Long) em.createQuery("SELECT SUM(bytesSendToUser) FROM RequestOperation").getSingleResult());
            }
            em.persist(rds);
            em.flush();
            /*
             * Fill Detail Data
             */
            super.notifyStateChanged(ThreadReportStatus.GENERATING,"Generating Detailed Data");
            RepDataDetails rdd = new RepDataDetails(report);
            rdd.setMaxHour((Integer) em.createQuery("SELECT MAX(HOUR(timeStamp)) FROM ClientRequest").getSingleResult());
            rdd.setMinHour((Integer) em.createQuery("SELECT MIN(HOUR(timeStamp)) FROM ClientRequest").getSingleResult());
            em.persist(rdd);
            /*
             * Get the Daily Usages...
             */
            em.flush();
            Iterator iterUsers = report.getUsers().iterator();
            
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
                        "GROUP BY DATE_FORMAT(cr.timeStamp,'%d-%m-%Y'), HOUR(cr.timeStamp) " +
                        "ORDER BY cr.timeStamp ASC"
                        )
                        .setParameter("userId",user.getId())
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
        } catch (Exception e) {
            et.rollback();
            super.notifyBreak(e);
            em.close();
            return;
        }
        
        super.notifyStateChanged(ThreadReportStatus.COMPLETED,null);
        em.close();
    }
    
    
    public void setParameters(Map parameters) throws Exception {
        if (parameters != null) {
            startDate = (Date) parameters.get("startDate");
            endDate = (Date) parameters.get("endDate");
            organization = (Organization) parameters.get("organization");
            users = (List) parameters.get("users");
        }
    }
    
}
