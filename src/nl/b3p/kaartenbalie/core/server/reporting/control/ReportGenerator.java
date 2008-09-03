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

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.core.server.reporting.datausagereport.DataUsageReport;
import nl.b3p.kaartenbalie.core.server.reporting.domain.BaseReport;
import nl.b3p.kaartenbalie.core.server.reporting.domain.ThreadReportStatus;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Chris Kramer
 */
public class ReportGenerator {

    private static final Log log = LogFactory.getLog(ReportGenerator.class);
    private final static String XSL_PATH = "/xslt/";
    public static SimpleDateFormat trsDate = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
    private static List reportStatusMap;
    private static Stack reportStack;
    private static int maxSimultaneousReports = 15;
    private static Map reportTypeInfo;
    public final static int RESPONSE_XML = 0;
    public final static int RESPONSE_XML_XSLT = 1;
    public final static int RESPONSE_HTML = 2;
    private User user;
    private Organization organization;
    

    static {
        reportStatusMap = new ArrayList();
        reportStack = new Stack();
        reportTypeInfo = new HashMap();
        reportTypeInfo.put(new Integer(RESPONSE_XML), new String[]{"text/xml", "xml"});
        reportTypeInfo.put(new Integer(RESPONSE_XML_XSLT), new String[]{"text/xml", "xml"});
        reportTypeInfo.put(new Integer(RESPONSE_HTML), new String[]{"text/html", "html"});
    }

    public static String[] getReportTypeInfo(int reportType) {
        return (String[]) reportTypeInfo.get(new Integer(reportType));
    }

    private ReportGenerator() {
    }

    public ReportGenerator(User user, Organization organization) {
        this.user = user;
        this.organization = organization;
    }

    public void createReport(Class reportThreadType, Map parameters) throws Exception {
        try {
            if (ReportThreadTemplate.class.isAssignableFrom(reportThreadType)) {
                ReportThreadTemplate rtt = (ReportThreadTemplate) reportThreadType.newInstance();
                rtt.init(this, user, organization, parameters);
//                if (reportStatusMap.size() >= maxSimultaneousReports) {
//                    rtt.notifyOnQueue();
//                    reportStack.push(rtt);
//                } else {
                    reportStatusMap.add(rtt);
                    rtt.start();
//                }

            }
        } catch (Exception e) {
            throw e;
        }
    }

    public Object[] getWorkload() {

        Double workloadPercentage = new Double(((double) reportStatusMap.size() / (double) maxSimultaneousReports) * 100.0);
        Integer itemsOnStack = new Integer(reportStack.size());
        return new Object[]{workloadPercentage, itemsOnStack};

    }

    public void notifyClosed(ReportThreadTemplate rtt) {

        if (!reportStack.empty()) {
            try {
                ReportThreadTemplate newRtt = (ReportThreadTemplate) reportStack.pop();
                if (!newRtt.isAlive()) {
                    reportStatusMap.add(newRtt);
                    newRtt.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        reportStatusMap.remove(rtt);
    }

    public List requestReportStatus() throws Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);
        List trsList = em.createQuery("" +
                "FROM ThreadReportStatus AS trs " +
                "WHERE trs.organization.id = :organizationId " +
                "ORDER BY trs.state DESC, trs.creationDate DESC").setParameter("organizationId", organization.getId()).getResultList();
        return trsList;
    }

    public static void startupClear() throws Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.INIT_EM);
        List trsList = em.createQuery(
                "FROM ThreadReportStatus AS trs " +
                "WHERE (trs.state != :stateComplete AND trs.state != :stateFailed) ").setParameter("stateComplete", new Integer(ThreadReportStatus.COMPLETED)).setParameter("stateFailed", new Integer(ThreadReportStatus.FAILED)).getResultList();
        Iterator listIter = trsList.iterator();
        while (listIter.hasNext()) {
            ThreadReportStatus trs = (ThreadReportStatus) listIter.next();
            trs.setState(ThreadReportStatus.FAILED);
            trs.setStatusMessage("Application halted.");
        }
        em.flush();
    }

    public void removeReport(Integer trsId) throws Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);
        if (trsId != null) {
            ThreadReportStatus trs = (ThreadReportStatus) em.find(ThreadReportStatus.class, trsId);
            if (trs.getReportId() != null) {
                BaseReport reportTemplate = (BaseReport) em.find(BaseReport.class, trs.getReportId());
                if (reportTemplate != null) {
                    em.remove(reportTemplate);
                }
            }
            em.remove(trs);
            em.flush();
        }
    }

    public String reportName(Integer trsId) throws Exception {
        String result = null;
        log.debug("Getting entity manager ......");
        EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);

        if (trsId != null) {
            ThreadReportStatus trs = (ThreadReportStatus) em.find(ThreadReportStatus.class, trsId);
            if (trs.getReportId() == null) {
                log.error("Report not found!");
                throw new Exception("Report not found!");
            }
            Object report = em.find(DataUsageReport.class, trs.getReportId());

            if (report != null) {
                result = trsDate.format(trs.getCreationDate()) + "_" + report.getClass().getSimpleName() + "_" + trs.getId();
            }
        }

        return result;
    }

    public void fetchReport(Integer trsId, OutputStream outStream, int responseMode, ServletContext servletContext) throws Exception {

        log.debug("Getting entity manager ......");
        EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);

        if (trsId != null) {
            ThreadReportStatus trs = (ThreadReportStatus) em.find(ThreadReportStatus.class, trsId);
            if (trs.getReportId() != null) {
                BaseReport report = (BaseReport) em.find(BaseReport.class, trs.getReportId());
                //Create instance of DocumentBuilderFactory
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

                //Get the DocumentBuilder
                DocumentBuilder docBuilder = factory.newDocumentBuilder();

                //Create blank DOM Document
                Document doc = docBuilder.newDocument();

                //get the root element
                Element reportElement = report.buildElement(doc);
                doc.appendChild(reportElement);
                Source reportSource = new DOMSource(doc);


                String reportType = report.getClass().getSimpleName();
                TransformerFactory transFactory = TransformerFactory.newInstance();
                Transformer xmlTransformer = transFactory.newTransformer();
                xmlTransformer.setOutputProperty(OutputKeys.METHOD, "xml");
                xmlTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
                xmlTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");

                switch (responseMode) {
                    case RESPONSE_XML:
                        xmlTransformer.transform(reportSource, new StreamResult(outStream));
                        break;
                    case RESPONSE_XML_XSLT:
                        Node reportXsl = doc.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"http://localhost:8084/kaartenbalie/xslt/" + reportType + "/mnp/report.xsl\"");
                        doc.insertBefore(reportXsl, reportElement);
                        xmlTransformer.transform(reportSource, new StreamResult(outStream));

                        break;
                    case RESPONSE_HTML:

                        File xslFile = new File(servletContext.getRealPath(XSL_PATH + reportType + "/mnp/report.xsl"));
                        File xmlPath = new File(xslFile.getParent());
                        Source xsltSource = new StreamSource(new FileInputStream(xslFile));
                        xsltSource.setSystemId(xmlPath.toURI().toString());
                        Transformer xsltTransformer = transFactory.newTransformer(xsltSource);
                        xsltTransformer.transform(reportSource, new StreamResult(outStream));
                        break;
                }
            }
        }

    }
}
