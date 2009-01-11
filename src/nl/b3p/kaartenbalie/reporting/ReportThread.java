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
    protected Organization organization;
    /* entity manager alleen voor deze class onafhankelijk van thread */
    protected EntityManager em = null;
    private Date startDate;
    private Date endDate;
    private String xsl;
    private String name;
    private String type;

    public void init(Map parameters) throws Exception {
        setParameters(parameters);
        if (type == null || type.length() == 0) {
            type = CastorXmlTransformer.XML;
        }
        if (name == null || name.length() == 0) {
            name = type;
        }
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
                report.setOrganization(organization);
                report.setStartDate(startDate);
                report.setEndDate(endDate);
                report.setName(name);
                String mime = (String) CastorXmlTransformer.getContentTypes().get(type);
                report.setReportMime(mime);

                em.persist(report);
                em.flush();
                tx.commit();
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }

            try {
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
                report.setProcessingTime(procTime);

                if (type.equalsIgnoreCase(CastorXmlTransformer.HTML)) {
                    CastorXmlTransformer cxt = new CastorXmlTransformer(xsl, MyEMFDatabase.localPath());
                    report.setReportXML(cxt.createHtml(mr));
                } else {
                    CastorXmlTransformer cxt = new CastorXmlTransformer();
                    report.setReportXML(cxt.createXml(mr));
                }
            } catch (Exception e) {
                StringBuffer rerror = new StringBuffer();
                rerror.append("<error>");
                rerror.append(e.getLocalizedMessage());

                StackTraceElement[] ste = e.getStackTrace();
                if (ste.length > 0) {
                    rerror.append(" at ");
                    rerror.append(ste[0].toString());
                }
                rerror.append("</error>");
                report.setReportXML(rerror.toString());
            }

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
        if (requestLoad != null) {
            mr.setRequestLoad(requestLoad);
        }
        RequestSummary requestSummary = createRequestSummary();
        if (requestSummary != null) {
            mr.setRequestSummary(requestSummary);
        }
        ResponseFrequency responseFrequency = createResponseFrequency();
        if (responseFrequency != null) {
            mr.setResponseFrequency(responseFrequency);
        }
        ServiceProviders serviceProviders = createServiceProviders();
        if (serviceProviders != null) {
            mr.setServiceProviders(serviceProviders);
        }

        return mr;
    }

    protected RequestLoad createRequestLoad() {
        RequestLoad requestLoad = null;

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
            requestLoad = new RequestLoad();
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
        RequestSummary requestSummary = null;

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
                requestSummary = new RequestSummary();
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
                typeSummary.setType(Operation.NAME[operation - 1]);

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
            for (int i = 1; i <= 21; i++) {

                Long msLow = new Long((i - 1) * stepSize + start);
                Long msHigh = new Long(i * stepSize + start);
                Long frequencyHits = null;
                try {
                    if (i <= 20) {
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
                    responseTime.setType(Operation.NAME[operation - 1]);

                    responseFrequency.addResponseTime(responseTime);
                }

            }
        }
        if (responseFrequency.getResponseTimeCount() == 0) {
            return null;
        }
        return responseFrequency;
    }

    protected ServiceProviders createServiceProviders() {
        ServiceProviders serviceProviders = null;

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
            serviceProviders = new ServiceProviders();
            Iterator it = resultList.iterator();
            while (it.hasNext()) {
                Object[] result = (Object[]) it.next();
                if (result != null && result.length == 6) {
                    ServiceProvider serviceProvider = new ServiceProvider();

                    serviceProvider.setName(getSpName((Integer) result[0]));
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

    private String getSpName(Integer id) {
        nl.b3p.wms.capabilities.ServiceProvider sp =
                (nl.b3p.wms.capabilities.ServiceProvider) em.find(nl.b3p.wms.capabilities.ServiceProvider.class, id);
        String spName = "onbekend";
        if (sp != null) {
            spName = sp.getAbbr();
        }
        return spName;
    }

    public void setParameters(Map parameters) throws Exception {
        if (parameters == null) {
            return;
        }
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
        xsl =
                (String) parameters.get("xsl");
        name =
                (String) parameters.get("name");
        type =
                (String) parameters.get("type");
    }
}
