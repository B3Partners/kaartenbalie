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
package nl.b3p.kaartenbalie.service.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletConfig;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.ogc.utils.KBConfiguration;
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
public class FlamingoConfigServlet extends HttpServlet {

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

        String path = request.getPathInfo();
        path = path.replaceAll("/", "");
        String[] tokens = path.split("extent=");
        String layers = null;
        String extent = null;
        if (tokens.length > 0) {
            layers = tokens[0];
        }
        if (tokens.length == 2) {
            extent = tokens[1];
        }
        //don't alter config if no parameter is given
        Document returnConfig = (Document) defaultConfig.cloneNode(true);
        if (layers != null && layers.length() > 1) {
            Node node = getElementBy(returnConfig, "id", "map");
            for (int i = 0; i < node.getAttributes().getLength(); i++) {
                if (node.getAttributes().item(i).getNodeName().equalsIgnoreCase("layers")) {
                    node.getAttributes().item(i).setNodeValue(layers.replace("*", ","));
                } else if (node.getAttributes().item(i).getNodeName().equalsIgnoreCase("query_layers")) {
                    node.getAttributes().item(i).setNodeValue(layers.replace("*", ","));
                } else if (node.getAttributes().item(i).getNodeName().equalsIgnoreCase("url")) {
                    String appRoot = "";
                    appRoot = request.getProtocol().substring(0, request.getProtocol().indexOf("/")).toLowerCase();
                    appRoot += "://" + request.getServerName();
                    if (request.getServerPort() != 80) {
                        appRoot += ":" + request.getServerPort();
                    }
                    appRoot += request.getContextPath();

                    String s = node.getAttributes().item(i).getNodeValue();
                    s = s.replace("[app_root]", appRoot);
                    node.getAttributes().item(i).setNodeValue(s);
                }
            }
        }
        if (extent != null && extent.length() > 1) {
            Node node = getElementBy(returnConfig, "id", "mainMap");
            for (int i = 0; i < node.getAttributes().getLength(); i++) {
                if (node.getAttributes().item(i).getNodeName().equalsIgnoreCase("extent")) {
                    node.getAttributes().item(i).setNodeValue(extent.replace("*", ","));
                }
            }
        }
        write(response, returnConfig);
    }

    public void write(HttpServletResponse response, Document doc) throws IOException {
        response.setContentType(KBConfiguration.CHARSET);
        OutputFormat format = new OutputFormat(doc);
        format.setIndenting(true);
        OutputStream output = new ByteArrayOutputStream();
        XMLSerializer serializer = new XMLSerializer(output, format);
        serializer.serialize(doc);
        OutputStream os = response.getOutputStream();
        String s = output.toString();
        os.write(output.toString().getBytes(KBConfiguration.CHARSET));
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        // Zet de logger
        log = LogFactory.getLog(this.getClass());
        log.info("Initializing Flamingo Config Servlet");
        String defaultConfigLocation = null;
        try {
            defaultConfigLocation = config.getServletContext().getRealPath(config.getInitParameter("defaultConfigLocation"));
        } catch (Exception e) {
            log("defaultConfigLocation load exception", e);
            throw new ServletException("Cannot load var defaultConfigLocation");
        }
        DOMParser dp = new DOMParser();
        try {
            dp.parse(defaultConfigLocation);
            defaultConfig = dp.getDocument();
        } catch (Exception e) {
            log.error(e);
        }

    }

    private Node getElementBy(Node node, String attributeName, String attributeValue) {
        NodeList nl = node.getChildNodes();
        Node returnValue = null;
        if (node.hasAttributes()) {
            for (int a = 0; a < node.getAttributes().getLength(); a++) {
                if (node.getAttributes().item(a).getNodeName().equalsIgnoreCase(attributeName)) {
                    if (node.getAttributes().item(a).getNodeValue().equalsIgnoreCase(attributeValue)) {
                        returnValue = node;
                    }
                }
            }
        }
        if (node.hasChildNodes()) {
            for (int c = 0; c < node.getChildNodes().getLength() && returnValue == null; c++) {
                Node testNode = getElementBy(node.getChildNodes().item(c), attributeName, attributeValue);
                if (testNode != null) {
                    returnValue = testNode;
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

