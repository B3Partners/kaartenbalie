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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SelectCSS extends HttpServlet {
    
    private static Log log = null;
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
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        log = LogFactory.getLog(this.getClass());
        
        csspath_default = config.getInitParameter("csspath_default");
        server1 = config.getInitParameter("server1");
        csspath1 = config.getInitParameter("csspath1");
        server2 = config.getInitParameter("server2");
        csspath2 = config.getInitParameter("csspath2");
    }
    
    /** Handles the HTTP <code>GET</code> method.
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     *
     * @throws ServletException
     * @throws MalformedURLException
     * @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String scheme = request.getScheme();
        String server = request.getServerName();
        String contextPath = request.getContextPath();
        int port = request.getServerPort();
        
        String cssPath = csspath_default;
        //Create an url where we can find the specific css.
        if (server.equalsIgnoreCase(server1)) {
            cssPath = csspath1;
        } else if (server.equalsIgnoreCase(server2)) {
            cssPath = csspath2;
        }
        
        URL cssur = null;
        try {
            cssur = new URL(createUrl(scheme, server, port, contextPath, cssPath));
        } catch (MalformedURLException ex) {
            try {
                cssur = new URL(createUrl(scheme, server, port, contextPath, csspath_default));
            } catch (MalformedURLException ex2) {
                errorMessage(response, "No suitable css url found: " + 
                        createUrl(scheme, server, port, contextPath, csspath_default));
                return;
            }
        }
        InputStream s = null;
        try {
            s = cssur.openStream();
        } catch (IOException ex) {
            try {
                cssur = new URL(createUrl(scheme, server, port, contextPath, csspath_default));
                s = cssur.openStream();
            } catch (IOException ex2) {
                errorMessage(response, "No css url could be opened: " + 
                        createUrl(scheme, server, port, contextPath, csspath_default));
                return;
            }
        }
        
        if (s == null) {
            errorMessage(response, "No css url could be opened (2)" + 
                    createUrl(scheme, server, port, contextPath, cssPath));
            return;
        }
        
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(s));
            response.setContentType("text/css");
            PrintWriter out;
            out = response.getWriter();
            String line = null;
            while ((line = reader.readLine()) != null) {
                out.write(line);
            }
            out.close();
            reader.close();
        } catch (IOException ex) {
            errorMessage(response, "Error reading css file");
            return;
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        doGet(request, response);
    }
    
    protected void errorMessage(HttpServletResponse response, String message) {
        try {
            PrintWriter out = response.getWriter();
            out.println(message);
            out.close();
            log.error("error: " + message);
        } catch (IOException ex) {
            log.error("error2: " + message);
        }
    }
    
    protected String createUrl(String scheme, String server, int port, String contextPath, String path) {
        StringBuffer url = new StringBuffer();
        url.append(scheme);
        url.append("://");
        url.append(server);
        url.append(":");
        url.append(port);
        url.append(contextPath);
        url.append("/");
        url.append(path);
        return url.toString();
    }
}