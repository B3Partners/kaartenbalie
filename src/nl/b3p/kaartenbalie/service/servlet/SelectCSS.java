/**
 * @(#)CreatePersonalURLAction.java
 * @author G. Plaisier
 * @version 1.00 2007/05/07
 *
 * Purpose: A Servlet for changing the StyleSheet of the website depending on the URL request.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.service.servlet;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.http.*;
import javax.servlet.*;

public class SelectCSS extends HttpServlet {
    public static final long serialVersionUID = 24362462L;
    
    private String server_default;
    private String csspath_default;
    private String server1;
    private String csspath1;
    private String server2;
    private String csspath2;  
    
    /** Initializes the servlet.
     *
     * @param config ServletConfig configuration file in which is described how to configure the servlet.
     *
     * @throws ServletException
     */
    // <editor-fold defaultstate="" desc="init(ServletConfig config) method.">
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        server_default = config.getInitParameter("server_default");
        csspath_default = config.getInitParameter("csspath_default");
        server1 = config.getInitParameter("server1");
        csspath1 = config.getInitParameter("csspath1");
        server2 = config.getInitParameter("server2");
        csspath2 = config.getInitParameter("csspath2");
    }
    // </editor-fold>
    
    /** Handles the HTTP <code>GET</code> method.
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     *
     * @throws ServletException
     * @throws MalformedURLException
     * @throws IOException
     */
    // <editor-fold defaultstate="" desc="doGet(HttpServletRequest request, HttpServletResponse response) method.">
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, MalformedURLException, IOException {
        String protocolAndVersion   = request.getProtocol();
        String requestServerName    = request.getServerName();
        String contextPath          = request.getContextPath();
        int port                    = request.getServerPort();
        String protocol             = protocolAndVersion.substring(0, protocolAndVersion.indexOf("/")).toLowerCase();
        
        String url      = "";
        String cssUrl   = "";
        
        //Create an url where we can find the specific css.
        if (requestServerName.equalsIgnoreCase(server1)) {
            url = protocol + "://" + server1;
            if (port != 80) {
                url += ":" + port;
            }
            url += contextPath + "/";
            cssUrl = url + csspath1;
        } else if (requestServerName.equalsIgnoreCase(server2)) {
            url = protocol + "://" + server2;
            if (port != 80) {
                url += ":" + port;
            }
            url += contextPath + "/";
            cssUrl = url + csspath2;
        } else {
            url = protocol + "://" + server_default;
            if (port != 80) {
                url += ":" + port;
            }
            url += contextPath + "/";
            cssUrl = url + csspath_default;
        }
        
        //Connect to this URL and read the specified CSS file
        URL cssur = new URL(cssUrl);
        response.setContentType("text/css");
        
        PrintWriter out         = response.getWriter();
        BufferedReader reader   = new BufferedReader( new InputStreamReader(cssur.openStream()));
        String line             = null;
        
        while ( (line = reader.readLine()) != null) {
            out.write(line);
        }
        
        out.close();
        reader.close();
    }
    // </editor-fold>
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        doGet(request, response);
    }
}