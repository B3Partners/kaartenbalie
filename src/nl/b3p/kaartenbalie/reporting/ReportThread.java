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
package nl.b3p.kaartenbalie.reporting;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.monitoring.Operation;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.reporting.castor.HourlyLoad;
import nl.b3p.kaartenbalie.reporting.castor.MonitorReport;
import nl.b3p.kaartenbalie.reporting.castor.Parameters;
import nl.b3p.kaartenbalie.reporting.castor.RequestLoad;
import nl.b3p.kaartenbalie.reporting.castor.RequestSummary;
import nl.b3p.kaartenbalie.reporting.castor.ResponseFrequency;
import nl.b3p.kaartenbalie.reporting.castor.ResponseTime;
import nl.b3p.kaartenbalie.reporting.castor.ServiceProvider;
import nl.b3p.kaartenbalie.reporting.castor.ServiceProviders;
import nl.b3p.kaartenbalie.reporting.castor.TypeSummary;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Chris Kramer
 */
public class ReportThread extends Thread {

    private static final Log log = LogFactory.getLog(ReportThread.class);

    //Owning user & organization..
    protected User user;
    protected Organization organization;
    protected ReportGenerator reportGenerator;
    /* entity manager alleen voor deze class onafhankelijk van thread */
    protected EntityManager em = null;
    Report report;
    private Date startDate;
    private Date endDate;

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

    public void run() {

        try {
            EntityTransaction tx = em.getTransaction();

            long processStart = System.currentTimeMillis();

            Report report = new Report();
            tx = em.getTransaction();
            tx.begin();
            try {
                /*
                 * Store all the parameters in the report...
                 */
                report.setOwningOrganization(organization);
                report.setStartDate(startDate);
                report.setEndDate(endDate);
                em.persist(report);
                em.flush();
                tx.commit();
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }

            Parameters parameters = new Parameters();
            parameters.setDateEnd(new org.exolab.castor.types.Date(endDate));
            parameters.setDateStart(new org.exolab.castor.types.Date(startDate));
            parameters.setId(Integer.toString(report.getId().intValue()));
            parameters.setOrganization(organization.getName());
            parameters.setTimeStamp(new Date());

            MonitorReport mr = createMonitorReport();

            Long procTime = new Long(System.currentTimeMillis() - processStart);
            parameters.setProcessingTime(procTime.longValue());
            mr.setParameters(parameters);

            CastorXmlTransformer cxt = new CastorXmlTransformer();
            report.setReportXML(cxt.createXml(mr));
            report.setProcessingTime(procTime);

            tx = em.getTransaction();
            tx.begin();
            try {
                em.merge(report);
                em.flush();
                tx.commit();
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            log.debug("Closing local entity manager ......");
            if (em != null && em.isOpen()) {
                em.close();
            }
            em = null;
        }
    }

    protected MonitorReport createMonitorReport() {
        MonitorReport mr = new MonitorReport();

        RequestLoad requestLoad = createRequestLoad();
        mr.setRequestLoad(requestLoad);
        RequestSummary requestSummary = createRequestSummary();
        mr.setRequestSummary(requestSummary);
        ResponseFrequency responseFrequency = createResponseFrequency();
        mr.setResponseFrequency(responseFrequency);
        ServiceProviders serviceProviders = createServiceProviders();
        mr.setServiceProviders(serviceProviders);

        return mr;
    }

    protected RequestLoad createRequestLoad() {
        RequestLoad requestLoad = new RequestLoad();

        List resultList = null;
        try {
            resultList = (List) em.createQuery(
                    "SELECT count(cr.timeStamp), " +
                    "to_char(cr.timeStamp, 'DD-MM-YYYY'), " +
                    "to_char(cr.timeStamp, 'HH24'), " +
                    "sum(ro.bytesReceivedFromUser), " +
                    "sum(ro.bytesSendToUser), " +
                    "avg(ro.duration), " +
                    "max(ro.duration) " +
                    "FROM ClientRequest AS cr " +
                    "LEFT JOIN cr.requestOperations AS ro " +
                    "WHERE cr.organizationId = :organizationId " +
                    "AND cr.timeStamp BETWEEN :startDate AND :endDate " +
                    "AND ro.type = :type " +
                    "GROUP BY to_char(cr.timeStamp, 'DD-MM-YYYY'), to_char(cr.timeStamp, 'HH24') " +
                    "ORDER BY to_char(cr.timeStamp, 'DD-MM-YYYY'), to_char(cr.timeStamp, 'HH24') ASC").
                    setParameter("type", new Integer(Operation.REQUEST)).
                    setParameter("startDate", startDate).
                    setParameter("endDate", endDate).
                    setParameter("organizationId", organization.getId()).
                    getResultList();
        } catch (NoResultException nre) {
            // nothing to do
        }
        if (resultList != null && resultList.size() > 0) {
            Iterator it = resultList.iterator();
            while (it.hasNext()) {
                Object[] result = (Object[]) it.next();
                if (result != null && result.length == 7) {


                    HourlyLoad hourlyLoad = new HourlyLoad();

                    Long count = (Long) result[0];
                    hourlyLoad.setCount(count == null ? new Integer(0) : new Integer(count.intValue()));

                    Date datum = FormUtils.StringToDate((String) result[1], null);
                    hourlyLoad.setDate(new org.exolab.castor.types.Date(datum == null ? new Date() : datum));
                    String hour = (String) result[2];
                    hourlyLoad.setHour(new Integer(hour));

                    Long bytesReceived = (Long) result[3];
                    hourlyLoad.setBytesReceivedSum(bytesReceived == null ? new Integer(0) : new Integer(bytesReceived.intValue()));
                    Long bytesSent = (Long) result[4];
                    hourlyLoad.setBytesSentSum(bytesSent == null ? new Integer(0) : new Integer(bytesSent.intValue()));
                    Double durationAvg = (Double) result[5];
                    hourlyLoad.setDurationAvg(durationAvg == null ? new Integer(0) : new Integer(durationAvg.intValue()));
                    Long durationMax = (Long) result[6];
                    hourlyLoad.setDurationMax(durationMax == null ? new Integer(0) : new Integer(durationMax.intValue()));

                    requestLoad.addHourlyLoad(hourlyLoad);
                }
            }
        }
        return requestLoad;
    }

    protected RequestSummary createRequestSummary() {
        RequestSummary requestSummary = new RequestSummary();

        for (int operation = 2; operation <= 5; operation++) {

            Object[] result = null;
            try {
                result = (Object[]) em.createQuery(
                        "SELECT count(ro.type), " +
                        "sum(ro.bytesReceivedFromUser), " +
                        "sum(ro.bytesSendToUser), " +
                        "sum(ro.dataSize), " +
                        "avg(ro.duration), " +
                        "max(ro.duration) " +
                        "FROM Operation AS ro " +
                        "WHERE ro.clientRequest.organizationId = :organizationId " +
                        "AND ro.type = :type " +
                        "AND ro.clientRequest.timeStamp BETWEEN :startDate AND :endDate " +
                        "GROUP BY ro.type ").
                        setParameter("type", new Integer(operation)).
                        setParameter("startDate", startDate).
                        setParameter("endDate", endDate).
                        setParameter("organizationId", organization.getId()).
                        getSingleResult();
            } catch (NoResultException nre) {
                // nothing to do
            }

            if (result != null && result.length == 6) {
                TypeSummary typeSummary = new TypeSummary();

                Long count = (Long) result[0];
                typeSummary.setCount(count == null ? new Integer(0) : new Integer(count.intValue()));
                Long bytesReceived = (Long) result[1];
                typeSummary.setBytesReceivedSum(bytesReceived == null ? new Integer(0) : new Integer(bytesReceived.intValue()));
                Long bytesSent = (Long) result[2];
                typeSummary.setBytesSentSum(bytesSent == null ? new Integer(0) : new Integer(bytesSent.intValue()));
                Long dataSize = (Long) result[3];
                typeSummary.setDataSizeSum(dataSize == null ? new Integer(0) : new Integer(dataSize.intValue()));
                Double durationAvg = (Double) result[4];
                typeSummary.setDurationAvg(durationAvg == null ? new Integer(0) : new Integer(durationAvg.intValue()));
                Long durationMax = (Long) result[5];
                typeSummary.setDurationMax(durationMax == null ? new Integer(0) : new Integer(durationMax.intValue()));
                typeSummary.setType(Operation.NAME[operation]);

                requestSummary.addTypeSummary(typeSummary);
            }
        }
        return requestSummary;
    }

    protected ResponseFrequency createResponseFrequency() {
        ResponseFrequency responseFrequency = new ResponseFrequency();

        for (int operation = 2; operation <= 6; operation++) {
            int stepSize = 1000;
            int start = 0;
            for (int i = 1; i <= 23; i++) {

                Long msLow = new Long((i - 1) * stepSize + start);
                Long msHigh = new Long(i * stepSize + start);
                Long frequencyHits = null;
                try {
                    if (i <= 22) {
                        frequencyHits = (Long) em.createQuery(
                                "SELECT count(*) FROM Operation AS ro " +
                                "WHERE ro.clientRequest.organizationId = :organizationId " +
                                "AND ro.type = :type " +
                                "AND ro.clientRequest.timeStamp BETWEEN :startDate AND :endDate " +
                                "AND ro.duration BETWEEN :msLow AND :msHigh ").
                                setParameter("type", new Integer(operation)).
                                setParameter("startDate", startDate).
                                setParameter("endDate", endDate).
                                setParameter("organizationId", organization.getId()).
                                setParameter("msLow", msLow).
                                setParameter("msHigh", msHigh).
                                getSingleResult();
                    } else {
                        frequencyHits = (Long) em.createQuery(
                                "SELECT count(*) FROM Operation AS ro " +
                                "WHERE ro.clientRequest.organizationId = :organizationId " +
                                "AND ro.type = :type " +
                                "AND ro.clientRequest.timeStamp BETWEEN :startDate AND :endDate " +
                                "AND ro.duration > :msLow ").
                                setParameter("type", new Integer(operation)).
                                setParameter("startDate", startDate).
                                setParameter("endDate", endDate).
                                setParameter("organizationId", organization.getId()).
                                setParameter("msLow", msLow).
                                getSingleResult();
                        msHigh = new Long(-1);
                    }
                } catch (NoResultException nre) {
                    // nothing to do
                }
                if (frequencyHits != null && frequencyHits.longValue() > 0) {
                    ResponseTime responseTime = new ResponseTime();
                    responseTime.setCount(frequencyHits == null ? 0 : frequencyHits.intValue());
                    responseTime.setDurationHigh(msHigh == null ? new Integer(0) : new Integer(msHigh.intValue()));
                    responseTime.setDurationLow(msLow == null ? new Integer(0) : new Integer(msLow.intValue()));
                    responseTime.setType(Operation.NAME[operation]);

                    responseFrequency.addResponseTime(responseTime);
                }

            }
        }
        return responseFrequency;
    }

    protected ServiceProviders createServiceProviders() {
        ServiceProviders serviceProviders = new ServiceProviders();

        List resultList = null;
        try {
            resultList = (List) em.createQuery(
                    "SELECT ro.serviceProviderId, " +
                    "count(ro.serviceProviderId), " +
                    "sum(ro.bytesReceived), " +
                    "sum(ro.bytesSend), " +
                    "avg(ro.requestResponseTime), " +
                    "max(ro.requestResponseTime) " +
                    "FROM ServiceProviderRequest AS ro " +
                    "WHERE ro.clientRequest.organizationId = :organizationId " +
                    "AND ro.clientRequest.timeStamp BETWEEN :startDate AND :endDate " +
                    "GROUP BY ro.serviceProviderId ").
                    setParameter("startDate", startDate).
                    setParameter("endDate", endDate).
                    setParameter("organizationId", organization.getId()).
                    getResultList();
        } catch (NoResultException nre) {
            // nothing to do
        }
        if (resultList != null && resultList.size() > 0) {
            Iterator it = resultList.iterator();
            while (it.hasNext()) {
                Object[] result = (Object[]) it.next();
                if (result != null && result.length == 6) {
                    ServiceProvider serviceProvider = new ServiceProvider();

                    Object name = result[0];
                    serviceProvider.setName(name == null ? "onbekend" : name.toString());
                    Long count = (Long) result[1];
                    serviceProvider.setCount(count == null ? new Integer(0) : new Integer(count.intValue()));
                    Long bytesReceived = (Long) result[2];
                    serviceProvider.setBytesReceivedSum(bytesReceived == null ? new Integer(0) : new Integer(bytesReceived.intValue()));
                    Long bytesSent = (Long) result[3];
                    serviceProvider.setBytesSentSum(bytesSent == null ? new Integer(0) : new Integer(bytesSent.intValue()));
                    Double durationAvg = (Double) result[4];
                    serviceProvider.setDurationAvg(durationAvg == null ? new Integer(0) : new Integer(durationAvg.intValue()));
                    Long durationMax = (Long) result[5];
                    serviceProvider.setDurationMax(durationMax == null ? new Integer(0) : new Integer(durationMax.intValue()));

                    serviceProviders.addServiceProvider(serviceProvider);
                }
            }
        }
        return serviceProviders;
    }


    /*


    protected void reportSummaryData(RepDataSummary rds, DataUsageReport dur) throws Exception {
    if (dur == null || rds == null) {
    return;
    }
    Integer organizationId = dur.getOrganizationId();
    EntityTransaction tx = em.getTransaction();
    try {
    tx.begin();
    //CombineImagesOperation
    Long cioHits = (Long) em.createQuery(
    "SELECT COUNT(*) " +
    "FROM CombineImagesOperation AS cio " +
    "WHERE cio.clientRequest.organizationId = :organizationId " +
    "AND cio.clientRequest.timeStamp BETWEEN :startDate AND :endDate").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("organizationId", organizationId).getSingleResult();
    rds.setCioHits(cioHits);
    if (cioHits != null) {
    rds.setCioAverageResponse((Double) em.createQuery(
    "SELECT AVG(duration) " +
    "FROM CombineImagesOperation AS cio " +
    "WHERE cio.clientRequest.organizationId = :organizationId " +
    "AND cio.clientRequest.timeStamp BETWEEN :startDate AND :endDate").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("organizationId", organizationId).getSingleResult());
    }
    //ServerTransferOperation
    Long ctoHits = (Long) em.createQuery("" +
    "SELECT COUNT(*) " +
    "FROM ServerTransferOperation AS sto " +
    "WHERE sto.clientRequest.organizationId = :organizationId " +
    "AND sto.clientRequest.timeStamp BETWEEN :startDate AND :endDate").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("organizationId", organizationId).getSingleResult();
    rds.setCtoHits(ctoHits);
    if (ctoHits != null) {
    rds.setCtoAverageResponse((Double) em.createQuery(
    "SELECT AVG(duration) " +
    "FROM ServerTransferOperation AS sto " +
    "WHERE sto.clientRequest.organizationId = :organizationId " +
    "AND sto.clientRequest.timeStamp BETWEEN :startDate AND :endDate").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("organizationId", organizationId).getSingleResult());
    }

    //ClientXFerOperation
    Long cxoHits = (Long) em.createQuery(
    "SELECT COUNT(*) " +
    "FROM ClientXFerOperation AS cxo " +
    "WHERE cxo.clientRequest.organizationId = :organizationId " +
    "AND cxo.clientRequest.timeStamp BETWEEN :startDate AND :endDate").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("organizationId", organizationId).getSingleResult();
    rds.setCxoHits(cxoHits);
    if (cxoHits != null) {
    rds.setCxoAverageResponse((Double) em.createQuery(
    "SELECT AVG(duration) " +
    "FROM ClientXFerOperation AS cxo " +
    "WHERE cxo.clientRequest.organizationId = :organizationId " +
    "AND cxo.clientRequest.timeStamp BETWEEN :startDate AND :endDate").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("organizationId", organizationId).getSingleResult());
    rds.setCxoData((Long) em.createQuery(
    "SELECT SUM(dataSize) " +
    "FROM ClientXFerOperation AS cxo " +
    "WHERE cxo.clientRequest.organizationId = :organizationId " +
    "AND cxo.clientRequest.timeStamp BETWEEN :startDate AND :endDate").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("organizationId", organizationId).getSingleResult());
    }

    //RequestOperation
    Long roHits = (Long) em.createQuery(
    "SELECT COUNT(*) " +
    "FROM RequestOperation AS ro " +
    "WHERE ro.clientRequest.organizationId = :organizationId " +
    "AND ro.clientRequest.timeStamp BETWEEN :startDate AND :endDate").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("organizationId", organizationId).getSingleResult();
    rds.setRoHits(roHits);
    if (roHits != null) {
    rds.setRoAverageResponse((Double) em.createQuery(
    "SELECT AVG(duration) " +
    "FROM RequestOperation AS ro " +
    "WHERE ro.clientRequest.organizationId = :organizationId " +
    "AND ro.clientRequest.timeStamp BETWEEN :startDate AND :endDate").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("organizationId", organizationId).getSingleResult());
    rds.setRoUpload((Long) em.createQuery(
    "SELECT SUM(bytesReceivedFromUser) " +
    "FROM RequestOperation AS ro " +
    "WHERE ro.clientRequest.organizationId = :organizationId " +
    "AND ro.clientRequest.timeStamp BETWEEN :startDate AND :endDate").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("organizationId", organizationId).getSingleResult());
    rds.setRoDownload((Long) em.createQuery(
    "SELECT SUM(bytesSendToUser) " +
    "FROM RequestOperation AS ro " +
    "WHERE ro.clientRequest.organizationId = :organizationId " +
    "AND ro.clientRequest.timeStamp BETWEEN :startDate AND :endDate").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("organizationId", organizationId).getSingleResult());
    }
    em.persist(rds);
    em.flush();
    tx.commit();
    } catch (Exception e) {
    tx.rollback();
    throw e;
    }
    }

    protected void reportDetailData(RepDataDetails rdd, DataUsageReport dur) throws Exception {
    if (dur == null) {
    return;
    }
    EntityTransaction tx = em.getTransaction();
    Integer organizationId = dur.getOrganizationId();
    try {
    tx.begin();
    rdd.setMaxHour((Integer) em.createQuery(
    "SELECT MAX(HOUR(timeStamp)) " +
    "FROM ClientRequest AS cr " +
    "WHERE cr.organizationId = :organizationId " +
    "AND cr.timeStamp BETWEEN :startDate AND :endDate").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("organizationId", organizationId).getSingleResult());
    rdd.setMinHour((Integer) em.createQuery(
    "SELECT MIN(HOUR(timeStamp)) " +
    "FROM ClientRequest AS cr " +
    "WHERE cr.organizationId = :organizationId " +
    "AND cr.timeStamp BETWEEN :startDate AND :endDate").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("organizationId", organizationId).getSingleResult());
    em.persist(rdd);
    em.flush();
    tx.commit();
    } catch (Exception e) {
    tx.rollback();
    throw e;
    }
    }

    protected void reportHits(RepDataDetails rdd, DataUsageReport dur) throws Exception {
    if (dur == null) {
    return;
    }
    EntityTransaction tx = em.getTransaction();
    Integer organizationId = dur.getOrganizationId();
    try {
    tx.begin();
    Iterator iterUsers = dur.getUsers(em).iterator();
    while (iterUsers.hasNext()) {
    User user = (User) iterUsers.next();
    UsageDetails usageDetails = new UsageDetails(rdd, user);
    em.persist(usageDetails);

    //Complex query to get the hits, date in string format, hour, bytes uploaded and bytes downloaded.
    //                List list = em.createQuery( //MYSQL versie
    //                        "SELECT COUNT(DISTINCT cr) AS hits, DATE_FORMAT(cr.timeStamp,'%d-%m-%Y') AS tsDate, HOUR(cr.timeStamp) AS tsHour, SUM(ro.bytesReceivedFromUser), SUM(ro.bytesSendToUser)" +
    //                        "FROM ClientRequest AS cr " +
    //                        "LEFT JOIN cr.requestOperations AS ro " +
    //                        "WHERE cr.userId = :userId " +
    //                        "AND cr.timeStamp BETWEEN :startDate AND :endDate " +
    //                        "AND cr.organizationId = :organizationId " +
    //                        "GROUP BY DATE_FORMAT(cr.timeStamp,'%d-%m-%Y'), HOUR(cr.timeStamp) " +
    //                        "ORDER BY cr.timeStamp ASC").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("userId", user.getId()).setParameter("organizationId", organizationId).getResultList();
    List list = em.createQuery( // postgres versie
    "SELECT COUNT(cr.timeStamp) AS hits, " +
    "to_char(cr.timeStamp, 'DD-MM-YYYY') AS tsDate, " +
    "to_char(cr.timeStamp, 'HH24') AS tsHour, " +
    "SUM(ro.bytesReceivedFromUser), " +
    "SUM(ro.bytesSendToUser) " +
    "FROM ClientRequest AS cr " +
    "LEFT JOIN cr.requestOperations AS ro " +
    "WHERE cr.userId = :userId " +
    "AND cr.timeStamp BETWEEN :startDate AND :endDate " +
    "AND cr.organizationId = :organizationId " +
    "GROUP BY to_char(cr.timeStamp, 'DD-MM-YYYY'), to_char(cr.timeStamp, 'HH24') " +
    "ORDER BY to_char(cr.timeStamp, 'DD-MM-YYYY'), to_char(cr.timeStamp, 'HH24') ASC").setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("userId", user.getId()).setParameter("organizationId", organizationId).getResultList();
    Iterator i = list.iterator();
    while (i.hasNext()) {
    Object[] object = (Object[]) i.next();
    DailyUsage dailyUsage = new DailyUsage(usageDetails);
    dailyUsage.setHits((Long) object[0]);
    try {
    dailyUsage.setDate(DataUsageReport.periodFormat.parse((String) object[1]));
    } catch (ParseException ex) {
    log.debug("", ex);
    }
    try {
    dailyUsage.setHour(Integer.valueOf((String) object[2]));
    } catch (NumberFormatException nfe) {
    log.debug("", nfe);
    }
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
    em.flush();
    tx.commit();
    } catch (Exception e) {
    tx.rollback();
    throw e;
    }
    }
     */
    public void setParameters(Map parameters) throws Exception {
        if (parameters != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime((Date) parameters.get("startDate"));
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            startDate =
                    cal.getTime();
            cal.setTime((Date) parameters.get("endDate"));
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 99);
            endDate =
                    cal.getTime();
            organization =
                    (Organization) parameters.get("organization");
        }
    }
}
