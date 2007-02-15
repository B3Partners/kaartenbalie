/*
 * @(#)FlamingoConfigServlet.java
 * @author R. Braam
 * @version 1.00, 6 februari 2007
 *
 * @copyright 2007 B3Partners. All rights reserved.
 * B3Partners PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package nl.b3p.kaartenbalie.service.servlet;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.io.*;

import javax.servlet.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import nl.b3p.kaartenbalie.core.server.FlamingoMap;
import nl.b3p.kaartenbalie.service.MyDatabase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.parsers.DOMParser;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * FlamingoConfigServlet definition:
 *
 */

public class FlamingoConfigServlet extends HttpServlet {
    
    public static final long serialVersionUID = 24362462L; 
    
    /** Creates a new instance of FlamingoConfigServlet */
    public FlamingoConfigServlet() {
    }
    



    private static Log log = null;
    private static Document defaultConfig;
    public static final String CHARSET_UTF8 = "UTF-8";
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*if (defaultConfig == null){
            throw new ServletException("Default config bestand is niet geladen");
        }
        
        String layersSelected = request.getPathInfo();
        layersSelected = layersSelected.substring(1, layersSelected.length());
        
        if (layersSelected != null){
            Session sess= MyDatabase.currentSession();
            Transaction tx = sess.beginTransaction();
            FlamingoMap flamingoMap = (FlamingoMap)sess.get(FlamingoMap.class, new Integer(layersSelected));
            
            Document doc = (Document) defaultConfig.cloneNode(true);
            //vervang de url en layers attributen in de map node
            Node node = getElementBy(doc,"id","map");
            
            for (int i = 0; i < node.getAttributes().getLength(); i++){
                if (node.getAttributes().item(i).getNodeName().equalsIgnoreCase("url")){
                    node.getAttributes().item(i).setNodeValue(flamingoMap.getFMCWMSLink());
                }
                if (node.getAttributes().item(i).getNodeName().equalsIgnoreCase("layers")){
                    node.getAttributes().item(i).setNodeValue(flamingoMap.getLayers());
                }
            }
            
            try{
                tx.commit();
            } catch(Exception e) {
                tx.rollback();
                log.error("Exception occured, rollback", e);
                
            }
            write(response,doc);
         *
        }*/
        
        
        String layers= request.getPathInfo();
        //don't alter config if no parameter is given
        Document returnConfig = (Document)defaultConfig.cloneNode(true);
        if (layers!=null && layers.length()>1)
        {
            layers=layers.replaceAll("/","");
            Node node = getElementBy(returnConfig,"id","map");
            for (int i = 0; i < node.getAttributes().getLength(); i++){
                if (node.getAttributes().item(i).getNodeName().equalsIgnoreCase("layers")){
                    node.getAttributes().item(i).setNodeValue(layers);
                }
            }
            
            
        }        
        
        write(response,returnConfig);
    }
    
    public void write(HttpServletResponse response, Document doc) throws IOException{
        response.setContentType("text/xml;charset=UTF-8");
        OutputFormat format = new OutputFormat(doc);
        format.setIndenting(true);
        OutputStream output = new ByteArrayOutputStream();
        XMLSerializer serializer = new XMLSerializer(output, format);
        serializer.serialize(doc);
        OutputStream os = response.getOutputStream();
        os.write(output.toString().getBytes(CHARSET_UTF8));
    }
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        // Zet de logger
        log = LogFactory.getLog(this.getClass());
        log.info("Initializing Flamingo Config Servlet");
        String defaultConfigLocation=null;
        try{
            defaultConfigLocation=config.getServletContext().getRealPath( config.getInitParameter("defaultConfigLocation") );
        } catch (Exception e) {
            log("defaultConfigLocation load exception", e);
            throw new ServletException("Cannot load var defaultConfigLocation");
        }
        DOMParser dp = new DOMParser();
        try {
            dp.parse(defaultConfigLocation);
            defaultConfig = dp.getDocument();
        } catch(Exception e){
            log.error(e);
        }
        
    }
    private Node getElementBy(Node node,String attributeName,String attributeValue){
        NodeList nl=node.getChildNodes();
        Node returnValue=null;
        if (node.hasAttributes()){
            for (int a=0; a < node.getAttributes().getLength(); a++){
                if (node.getAttributes().item(a).getNodeName().equalsIgnoreCase(attributeName)) {
                    if (node.getAttributes().item(a).getNodeValue().equalsIgnoreCase(attributeValue)) {
                        returnValue=node;
                    }
                }
            }
        }
        if (node.hasChildNodes()){
            for (int c=0; c < node.getChildNodes().getLength() &&returnValue==null; c++){
                Node testNode=getElementBy(node.getChildNodes().item(c),attributeName,attributeValue);
                if(testNode!=null){
                    returnValue=testNode;
                }
            }
        }
        return returnValue;
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    // </editor-fold>
}

