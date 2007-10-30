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
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.core.server.reporting.domain.generation.Report;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
/**
 *
 * @author Chris Kramer
 */
public class ReportGenerator {
    
    /** Creates a new instance of ReportGenerator */
    private EntityManager em;
    
    public ReportGenerator() {
        em = MyEMFDatabase.createEntityManager();
    }
    
    public void createReport(Date start, Date end) {
        ReportCreationThread rct = new ReportCreationThread(start, end, this);
        rct.start();
    }
    
    public void notifyDone(ReportCreationThread rct) {
        System.out.println("RCT:" + rct + " is done..");
    }
    
    public void clean() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.createQuery("DELETE FROM DailyUsage").executeUpdate();
        em.createQuery("DELETE FROM UsageDetails").executeUpdate();
        em.createQuery("DELETE FROM RepData").executeUpdate();
        em.createNativeQuery("DELETE FROM rep_users;").executeUpdate();
        em.createQuery("DELETE FROM Report").executeUpdate();
        tx.commit();
        
    }
    public void doXMLTrick(Integer reportId) {
        Report report = (Report) em.find(Report.class, reportId);
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
            
            File destFile = new File("C:\\xsltTutorial\\test.xml");
            Result dest = new StreamResult(destFile);
            aTransformer.transform(src, dest);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    
    
    
    
}
