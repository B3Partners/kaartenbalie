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
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.core.server.reporting.datausagereport.DataUsageReport;
import nl.b3p.kaartenbalie.core.server.reporting.datausagereport.DataUsageReportThread;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
/**
 *
 * @author Chris Kramer
 */
public class ReportGenerator {
    
    private static Map reportStatusMap;
    static {
        reportStatusMap = new LinkedHashMap();
    }
    
    
    private EntityManager em;
    
    public ReportGenerator() {
        em = MyEMFDatabase.createEntityManager();
    }
    
    public static void main(String [] args) throws Exception {
        MyEMFDatabase.openEntityManagerFactory(MyEMFDatabase.nonServletKaartenbaliePU);
        EntityManager em = MyEMFDatabase.createEntityManager();
        ReportGenerator rg = new ReportGenerator();
        Map parameterMap = new HashMap();
        Calendar cal = Calendar.getInstance();
        parameterMap.put("endDate", cal.getTime());
        cal.set(2007,10,20);
        parameterMap.put("startDate", cal.getTime());
        rg.createReport(DataUsageReportThread.class, parameterMap, null);
        
    }
    public void createReport(Class reportThreadType, Map parameters, User user) throws Exception {
        try {
            if (ReportThreadTemplate.class.isAssignableFrom(reportThreadType)) {
                ReportThreadTemplate rtt = (ReportThreadTemplate) reportThreadType.newInstance();
                reportStatusMap.put(rtt, rtt);
                rtt.setParameters(parameters);
                rtt.setUser(user);
                rtt.setReportGenerator(this);
                rtt.start();
            }
        } catch (Exception e) {
            throw e;
        }
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
    
    
    
    public void notifyDone(ReportThreadTemplate reportThreadTemplate) {
        //System.out.println(reportThreadTemplate + " completed succesfully in " + reportThreadTemplate.getReportTemplate().getProcessingTime() + " ms...");
        //doXMLTrick(reportThreadTemplate.getReportTemplate().getId());
    }
    
    public void notifyBreak(ReportThreadTemplate reportThreadTemplate, Exception exception) {
        System.out.println(reportThreadTemplate + " halted on " + exception.getMessage());
    }
    
    
    
    
}
