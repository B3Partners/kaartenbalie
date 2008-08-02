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
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        String protocolAndVersion = request.getProtocol();
        String requestServerName = request.getServerName();
        String contextPath = request.getContextPath();
        int port = request.getServerPort();
        String protocol = protocolAndVersion.substring(0, protocolAndVersion.indexOf("/")).toLowerCase();

        String url = "";
        String cssUrl = "";

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

        PrintWriter out = response.getWriter();
        BufferedReader reader = new BufferedReader(new InputStreamReader(cssur.openStream()));
        String line = null;

        while ((line = reader.readLine()) != null) {
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