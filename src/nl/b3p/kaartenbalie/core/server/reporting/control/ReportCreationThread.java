/*
 * ReportCreationThread.java
 *
 * Created on October 26, 2007, 1:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.control;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.sound.midi.SysexMessage;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.core.server.reporting.domain.generation.DailyUsage;
import nl.b3p.kaartenbalie.core.server.reporting.domain.generation.RepDataDetails;
import nl.b3p.kaartenbalie.core.server.reporting.domain.generation.RepDataSummary;
import nl.b3p.kaartenbalie.core.server.reporting.domain.generation.Report;
import nl.b3p.kaartenbalie.core.server.reporting.domain.generation.UsageDetails;
import nl.b3p.kaartenbalie.core.server.reporting.domain.requests.ClientRequest;

/**
 *
 * @author Chris Kramer
 */
public class ReportCreationThread extends Thread{
    
    /** Creates a new instance of ReportCreationThread */
    private Date startDate;
    private Date endDate;
    //public Company company
    //public List users
    private EntityManager em;
    private ReportGenerator reportGenerator;
    
    private ReportCreationThread() {
        em = MyEMFDatabase.createEntityManager();
    }
    public ReportCreationThread(Date startDate, Date endDate, ReportGenerator reportGenerator) {
        this();
        this.startDate = startDate;
        this.endDate = endDate;
        this.reportGenerator = reportGenerator;
    }
    public void run() {
        long processStart = System.currentTimeMillis();
        EntityTransaction et = em.getTransaction();
        et.begin();
        
        
        Report report = new Report(startDate, endDate);
        em.persist(report);
        /*
         * Add the users... Just add all for now..
         */
        
        report.setUsers(em.createQuery("From User").getResultList());
        
        
        /*
         * Fill Summary Data
         */
        
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
                    dailyUsage.setDate(Report.periodFormat.parse((String) object[1]));
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
        em.close();
        reportGenerator.notifyDone(this);
    }
    
    public static void main(String [] args) throws Exception {
        
        MyEMFDatabase.openEntityManagerFactory(MyEMFDatabase.nonServletKaartenbaliePU);
        
        
        ReportGenerator rg = new ReportGenerator();
        
        
        rg.clean();
        Calendar start = Calendar.getInstance();
        start.set(2007,10,14);
        
        Calendar end = Calendar.getInstance();
        end.set(2007,11,01);
        
        rg.createReport(start.getTime(), end.getTime());
        sleep(2000);
        EntityManager em = MyEMFDatabase.createEntityManager();
        Report rerport = (Report) em.createQuery("FROM Report AS r").getSingleResult();
        rg.doXMLTrick(rerport.getId());
        
        
        
        
    }
    
    
}
