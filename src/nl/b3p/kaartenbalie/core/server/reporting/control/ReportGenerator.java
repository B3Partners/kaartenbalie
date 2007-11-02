/*
 * ReportGenerator.java
 *
 * Created on October 23, 2007, 11:28 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.control;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.persistence.EntityManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.core.server.reporting.datausagereport.DataUsageReport;
import nl.b3p.kaartenbalie.core.server.reporting.datausagereport.DataUsageReportThread;
import nl.b3p.kaartenbalie.core.server.reporting.domain.ThreadReportStatus;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
/**
 *
 * @author Chris Kramer
 */
public class ReportGenerator {
    
    private static List reportStatusMap;
    private static Stack reportStack;
    private static int maxSimultaneousReports = 15;
    
    static {
        reportStatusMap = new ArrayList();
        reportStack = new Stack();
    }
    
    
    private EntityManager em;
    
    public ReportGenerator() {
        em = MyEMFDatabase.createEntityManager();
    }
    
    
    public void createReport(Class reportThreadType, Map parameters, User user) throws Exception {
        try {
            if (ReportThreadTemplate.class.isAssignableFrom(reportThreadType)) {
                ReportThreadTemplate rtt = (ReportThreadTemplate) reportThreadType.newInstance();
                rtt.init();
                
                rtt.setParameters(parameters);
                rtt.setUser(user);
                rtt.setReportGenerator(this);
                if (reportStatusMap.size() >= maxSimultaneousReports) {
                    rtt.notifyOnQueue();
                    reportStack.push(rtt);
                } else {
                    reportStatusMap.add(rtt);
                    rtt.start();
                }
                
            }
        } catch (Exception e) {
            throw e;
        }
    }
    public Object[] getWorkload() {
        
        Double workloadPercentage = new Double(((double)reportStatusMap.size() / (double)maxSimultaneousReports) * 100.0);
        Integer itemsOnStack = new Integer(reportStack.size());
        return new Object[] {workloadPercentage, itemsOnStack};
        
    }
    public void notifyClosed(ReportThreadTemplate rtt) {
        System.out.println(rtt + " done!");
        
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
        Object[] workLoad = getWorkload();
        System.out.println("Workload: " + workLoad[0] + "%, stack remaining: " + workLoad[1]);
    }
    
    
    public List requestReportStatus() {
        return null;
    }
    
    public void doXMLTrick(Integer reportId) {
        DataUsageReport report = (DataUsageReport) em.find(DataUsageReport.class, reportId);
        try{
            //Create instance of DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            
            //Get the DocumentBuilder
            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            
            //Create blank DOM Document
            Document doc = docBuilder.newDocument();
            
            //get the root element
            Element reportElement = report.toElement(doc,null);
            doc.appendChild(reportElement);
            Node reportXsl = doc.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"report.xsl\"");
            doc.insertBefore(reportXsl, reportElement);
            
            TransformerFactory tranFactory = TransformerFactory.newInstance();
            Transformer aTransformer = tranFactory.newTransformer();
            aTransformer.setOutputProperty(OutputKeys.METHOD, "xml");
            aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
            aTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
            
            Source src = new DOMSource(doc);
            
            File destFile = new File("C:\\dev_chris\\projects\\kaartenbalie\\ChrisXSLT\\test.xml");
            Result dest = new StreamResult(destFile);
            aTransformer.transform(src, dest);
        }catch(Exception e){
            e.printStackTrace();
            
        }
    }
    
    public static void main(String [] args) throws Exception {
        MyEMFDatabase.openEntityManagerFactory(MyEMFDatabase.nonServletKaartenbaliePU);
        EntityManager em = MyEMFDatabase.createEntityManager();
        
        Map parameterMap = new HashMap();
        Calendar cal = Calendar.getInstance();
        parameterMap.put("endDate", cal.getTime());
        cal.set(2007,10,20);
        parameterMap.put("startDate", cal.getTime());
        Organization organization = (Organization) em.find(Organization.class, new Integer(1));
        parameterMap.put("organization", organization);
        parameterMap.put("users", em.createQuery(
                "FROM User AS u " +
                "WHERE u.organization.id = :organizationId")
                .setParameter("organizationId", organization.getId())
                .getResultList());
        
        for (int j = 0; j< 50; j++) {
            ReportGenerator rg = new ReportGenerator();
            for(int i = 0; i< 10; i++) {
                rg.createReport(DataUsageReportThread.class, parameterMap, null);
            }
            
        }
        
    }
}
