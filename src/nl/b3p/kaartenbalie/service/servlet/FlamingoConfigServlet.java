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
import nl.b3p.wms.capabilities.KBConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * FlamingoConfigServlet definition:
 *
 */

public class FlamingoConfigServlet extends HttpServlet implements KBConstants {
    
    public static final long serialVersionUID = 24362462L; 
    
    /** Creates a new instance of FlamingoConfigServlet */
    public FlamingoConfigServlet() {
    }
    



    private static Log log = null;
    private static Document defaultConfig;
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
        String path= request.getPathInfo();
        path=path.replaceAll("/","");
        String[] tokens=path.split("extent=");
        String layers=null;
        String extent=null;
        if (tokens.length>0){
            layers=tokens[0];
        }
        if (tokens.length==2){
            extent=tokens[1];
        }        
        //don't alter config if no parameter is given
        Document returnConfig = (Document)defaultConfig.cloneNode(true);
        if (layers!=null && layers.length()>1)
        {
            Node node = getElementBy(returnConfig,"id","map");
            for (int i = 0; i < node.getAttributes().getLength(); i++){
                if (node.getAttributes().item(i).getNodeName().equalsIgnoreCase("layers")){
                    node.getAttributes().item(i).setNodeValue(layers.replace("*",","));
                }else if(node.getAttributes().item(i).getNodeName().equalsIgnoreCase("query_layers")){
                    node.getAttributes().item(i).setNodeValue(layers.replace("*",","));
                }else if(node.getAttributes().item(i).getNodeName().equalsIgnoreCase("url")){
                    String appRoot="";
                    appRoot = request.getProtocol().substring(0, request.getProtocol().indexOf("/")).toLowerCase();
                    appRoot += "://" + request.getServerName();
                    if (request.getServerPort() != 80) {
                        appRoot += ":" + request.getServerPort();
                    }
                    appRoot += request.getContextPath();
                    
                    String s= node.getAttributes().item(i).getNodeValue();
                    s=s.replace("[app_root]",appRoot);
                    node.getAttributes().item(i).setNodeValue(s);
                }                
            }
        }
        if (extent!=null && extent.length()>1){
            Node node = getElementBy(returnConfig,"id","mainMap");
            for (int i = 0; i < node.getAttributes().getLength(); i++){
                if (node.getAttributes().item(i).getNodeName().equalsIgnoreCase("extent")){
                    node.getAttributes().item(i).setNodeValue(extent.replace("*",","));
                }
            }
        }
        write(response,returnConfig);
    }
    
    public void write(HttpServletResponse response, Document doc) throws IOException{
        response.setContentType(CHARSET);
        OutputFormat format = new OutputFormat(doc);
        format.setIndenting(true);
        OutputStream output = new ByteArrayOutputStream();
        XMLSerializer serializer = new XMLSerializer(output, format);
        serializer.serialize(doc);
        OutputStream os = response.getOutputStream();
        String s= output.toString();
        os.write(output.toString().getBytes(CHARSET));
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
    
    // <editor-fold defaultstate="" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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

