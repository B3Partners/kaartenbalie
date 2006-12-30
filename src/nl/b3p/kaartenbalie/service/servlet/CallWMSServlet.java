/*
 * CallWMSServlet.java
 *
 * Created on 25 oktober 2006, 10:21
 */

package nl.b3p.kaartenbalie.service.servlet;

import java.util.HashMap;
import nl.b3p.kaartenbalie.service.requesthandler.*;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.http.*;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.service.MyDatabase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import nl.b3p.kaartenbalie.core.KBConstants;

/**
 *
 * @author Nando De Goeij
 * @version
 * http://localhost:8084/kaartenbalie/servlet/CallWMSServlet/98100ae84e47961542bc82c14f19aa4f?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetCapabilities
 * http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/ijmond.map&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetCapabilities
 * http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gbkn.map&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetCapabilities
 * http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gstreet-demo.map&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetCapabilities
 *
 */
public class CallWMSServlet extends HttpServlet implements KBConstants {
    private static Log log = null;
    
    /** Initializes the servlet. */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        // Zet de logger
        log = LogFactory.getLog(this.getClass());
        log.info("Initializing Call WMS Servlet");
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // foutafhandeling moet nog verbeterd worden:
        // wms kan plaatje of xml vragen om fout in te wrappen
        // TODO
        
        User user = null;
        try {
            user= checkLogin(request);
        } catch (NoSuchAlgorithmException ex) {
            log.error("NoSuchAlgorithmException error: ", ex);
            throw new ServletException("NoSuchAlgorithmException", ex);
        } catch (UnsupportedEncodingException ex) {
            log.error("UnsupportedEncodingException error: ", ex);
            throw new ServletException("UnsupportedEncodingException", ex);
        }
        if (user==null)
            response.sendError(response.SC_UNAUTHORIZED);
        
        Map parameters = new HashMap();
        parameters.putAll(request.getParameterMap());
        // beter user meegeven in map dan organisatie
        parameters.put(KB_USER, user);
        parameters.put(KB_PERSONAL_URL, request.getRequestURL().toString());
        
        byte[] data = null;
        try {
            data = parseRequestAndData(parameters);
            log.debug("response data: " + new String(data));
        } catch (Exception ex) {
            log.error("parseRequestAndData error: ", ex);
            throw new ServletException("parseRequestAndData Exception", ex);
        }
        
        // dit klopt nog niet, kan xml of image zijn
        // TODO
        response.setContentType("text/xml;charset=UTF-8");
        
        OutputStream sos = null;
        try {
            sos = response.getOutputStream();
            sos.write(data);
        } finally {
            if (sos!=null)
                sos.close();
        }
    }
    
    public User checkLogin(HttpServletRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        
        // eerst checken of user gewoon ingelogd is
        User user = (User) request.getUserPrincipal();
        if (user==null) {
            // niet ingelogd dus, dan checken op token in url
            Session sess = MyDatabase.currentSession();
            user = (User)sess.createQuery(
                    "from User u where " +
                    "lower(u.personalURL) = lower(:personalURL) ")
                    .setParameter("personalURL", request.getRequestURL().toString())
                    .uniqueResult();
            
            if (user!=null) {
                // bereken token voor deze user
                 String token = calcToken(
                         request.getRemoteAddr(), 
                         user.getUsername(), 
                         user.getPassword());
                
                // bereken token in url
                String pathInfo = request.getPathInfo();
                String urlToken = pathInfo.substring(1);
                
                if (!urlToken.equals(token)) {
                    // ongeldig token!
                    return null;
                }
            }
        }
        return user;
    }
    
    private String calcToken(String registeredIP, String username, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String toBeHashedString = registeredIP + username + password;
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(toBeHashedString.getBytes("8859_1")); // UTF-8 ???
        BigInteger hash = new BigInteger(1, md.digest());
        return hash.toString( 16 );
    }
    
    public byte[] parseRequestAndData(Map parameters) throws Exception {
        
        String givenRequest = (String) parameters.get(WMS_REQUEST);
        if (SUPPORTED_REQUESTS.contains(givenRequest))
            throw new UnsupportedOperationException("Request '" + givenRequest + "' not supported!");
        
        String givenService = (String) parameters.get(WMS_SERVICE);
        if (SUPPORTED_SERVICES.contains(givenService))
            throw new UnsupportedOperationException("Service '" + givenService + "' not supported!");
        
        String givenVersion = (String) parameters.get(WMS_VERSION);
        if (SUPPORTED_VERSIONS.contains(givenVersion))
            throw new UnsupportedOperationException("Version '" + givenVersion + "' not supported!");
        
        RequestHandler requestHandler = null;
        List reqParams = null;
        if(givenRequest.equalsIgnoreCase(WMS_REQUEST_GetCapabilities)) {
            requestHandler = new GetCapabilitiesRequestHandler();
            reqParams = PARAMS_GetCapabilities;
        } else if (givenRequest.equalsIgnoreCase(WMS_REQUEST_GetMap)) {
            requestHandler = new GetMapRequestHandler();
            reqParams = PARAMS_GetCapabilities;
        } else if (givenRequest.equalsIgnoreCase(WMS_REQUEST_GetFeatureInfo)) {
            requestHandler = new GetFeatureInfoRequestHandler();
            reqParams = PARAMS_GetCapabilities;
        } else if (givenRequest.equalsIgnoreCase(WMS_REQUEST_GetLegendGraphic)) {
            requestHandler = new GetLegendGraphicRequestHandler();
            reqParams = PARAMS_GetCapabilities;
        }
        
        if (!requestComplete(parameters, reqParams))
            throw new IllegalArgumentException("Not all parameters for request '" +
                    givenRequest + "' are available, required: [" + reqParams.toString() +
                    "], available: [" + parameters.toString() + "].");
        
        return requestHandler.getRequest(parameters);
    }
    
    protected boolean requestComplete(Map parameters, List requiredParameters) {
        if (parameters==null || requiredParameters==null)
            return false;
        if (requiredParameters.isEmpty())
            return true;
        if (parameters.isEmpty())
            return false;
        Iterator it = requiredParameters.iterator();
        while (it.hasNext()) {
            String reqParam = (String)it.next();
            if (!parameters.containsKey(reqParam))
                return false;
        }
        return true;
    }
    
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
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "CallWMSServlet info";
    }
}
