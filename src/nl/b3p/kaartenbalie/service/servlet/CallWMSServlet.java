/*
 * @(#)CallWMSServlet.java
 * @author Nando De Goeij
 * @version 1.00
 * Created on 25 oktober 2006, 10:21
 *
 * Purpose: portal to which a client sends his requests. This servlet takes all incoming requests and handles
 * each request differently depending on the input given in the request. The Servlets takes the KBConstants
 * interface to check for all the different parameters which can be send together with the request.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.service.servlet;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;
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

public class CallWMSServlet extends HttpServlet implements KBConstants {
    private static Log log = null;
    public static final long serialVersionUID = 24362462L;
    
    /** Initializes the servlet.
     * Turns the logging of the servlet on.
     *
     * @param config ServletConfig config
     *
     * @throws ServletException
     */
    // <editor-fold defaultstate="" desc="init(ServletConfig config) method.">
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        // Zet de logger
        log = LogFactory.getLog(this.getClass());
        log.info("Initializing Call WMS Servlet");
    }
    // </editor-fold>
    
    //TODO:
    // De foutafhandeling in processRequest moet nog verbeterd worden.
    // De wms client kan vragen om een image of een xml bestand als return voor een foutmelding.
    // Daartoe dient ook de response.setContentType("text/xml;charset=UTF-8"); aangepast te worden,
    // zodat deze de response op de juiste wijze terug kan sturen naar de client.
    
    /** Processes the incoming request and calls the various methods to create the right output stream.
     *
     * @param request servlet request
     * @param response servlet response
     *
     * @throws ServletException
     * @throws IOException
     */
    // <editor-fold defaultstate="" desc="processRequest(HttpServletRequest request, HttpServletResponse response) method.">
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long firstMeasurement = System.currentTimeMillis();
        User user = null;
        DataWrapper data = new DataWrapper();
        OutputStream sos = null;
        
        try {
            user = checkLogin(request);
            
            if (user != null) {
                
                Map <String, Object> parameters = new HashMap <String, Object>();
                parameters.putAll(request.getParameterMap());
                parameters.put(KB_USER, user);
                parameters.put(KB_PERSONAL_URL, request.getRequestURL().toString());
                
                try {
                    data = parseRequestAndData(data, parameters);
                } catch (Exception ex) {
                    log.error("error: ", ex);
                    // TODO: moet nog echt xml error bericht worden
                    StringBuffer es = new StringBuffer();
                    es.append("<?xml version='1.0' encoding='UTF-8' standalone='no' ?>");
                    es.append("<ServiceExceptionReport version='1.1.1'>");
                    es.append("<ServiceException>");
                    es.append("<![CDATA[");
                    es.append(ex.getMessage());
                    es.append(" - ");
                    es.append(ex.getCause());
                    es.append("]]>");
                    es.append("</ServiceException>");
                    es.append("</ServiceExceptionReport>");
                    
                    byte[] ba = es.toString().getBytes(CHARSET_UTF8);
                    data.setData(ba);
                    data.setContentType("application/vnd.ogc.se_xml");
                    data.setContentLength(ba.length);
                    data.setContentDisposition("inline; filename=\"error.xml\";");
                }
                
                response.setContentType(data.getContentType());
                response.setContentLength(data.getContentLength());
                response.setHeader("Content-Disposition", data.getContentDisposition());
                
                sos = response.getOutputStream();
                sos.write(data.getData());
                sos.flush();
                
                
                
                
            } else {
                response.sendError(response.SC_UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error("error: ", e);
        } finally {
            if (sos!=null)
                sos.close();
        }
        
    }
    // </editor-fold>
    
    /** Checks if an user is allowed to make any requests.
     * Therefore there is checked if a user is logged in or if a user is using a private unique IP address.
     *
     * @param request servlet request
     *
     * @return user object
     *
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    // <editor-fold defaultstate="" desc="checkLogin(HttpServletRequest request) method.">
    public User checkLogin(HttpServletRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        
        // eerst checken of user gewoon ingelogd is
        User user = (User) request.getUserPrincipal();
        if (user==null) {
            // niet ingelogd dus, dan checken op token in url
            Session sess = MyDatabase.currentSession();
            Transaction tx = sess.beginTransaction();
            String url = request.getRequestURL().toString();
            String remote = request.getRemoteAddr();
            try {
                user = (User)sess.createQuery(
                        "from User u where " +
                        "lower(u.personalURL) = lower(:personalURL) ")
                        .setParameter("personalURL", request.getRequestURL().toString())
                        .uniqueResult();
            } finally {
                tx.commit();
            }
            
            if (user!=null) {
                java.util.Date date = (java.util.Date)user.getTimeout();
                
                if (date.compareTo(new java.util.Date()) <= 0) {
                    //System.out.println("The date for your personal URL has expired");
                    return null;
                } //else {
                //System.out.println("The date for your personal URL is still ok");
                //}
                
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                // Parse with a custom format
                String personalDate = df.format(date);
                //System.out.println("De opgegeven datum is : " + date.toString());
                
                // bereken token voor deze user
                String token = calcToken(
                        request.getRemoteAddr(),
                        user.getUsername(),
                        user.getPassword(),
                        personalDate);
                
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
    // </editor-fold>
    
    /** Creates a hash of the IP address, username and the password.
     *
     * @param registeredIP string representing the ip address of this user
     * @param username string representing the username of this user
     * @param password string representing the password of this user
     *
     * @return hashed string which is unique for the user
     *
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    // <editor-fold defaultstate="" desc="calcToken(String registeredIP, String username, String password) method.">
    private String calcToken(String registeredIP, String username, String password, String personalDate) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String toBeHashedString = registeredIP + username + password + personalDate;
        //System.out.println("String to be hashed in CallWMS is  : " + toBeHashedString);
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(toBeHashedString.getBytes("8859_1")); // UTF-8 ???
        BigInteger hash = new BigInteger(1, md.digest());
        
        
        //System.out.println("Personal URL in CallWMSServlet: " + hash.toString( 16 ));
        return hash.toString( 16 );
    }
    // </editor-fold>
    
    /** Parses any incoming request.
     *
     * @param parameters map with the given parameters
     *
     * @return byte array with the requested data
     *
     * @throws IllegalArgumentException
     * @throws UnsupportedOperationException
     * @throws IOException
     */
    // <editor-fold defaultstate="" desc="parseRequestAndData(Map parameters) method.">
    public DataWrapper parseRequestAndData(DataWrapper data, Map <String, Object> parameters) throws IllegalArgumentException, UnsupportedOperationException, IOException {
        
        String givenRequest=null;
        boolean supported_request = false;
        
        String requestType = checkCaseInsensitiveParameter(parameters, WMS_REQUEST);
        if (parameters.get(requestType)!=null){
            givenRequest = ((String[]) parameters.get(requestType))[0];
            
                /*if (givenRequest==null){
                givenRequest=WMS_REQUEST_GetCapabilities;
                parameters.put(WMS_VERSION,"1.1.1");
                parameters.put(WMS_SERVICE,"WMS");
                }*/
            
            Iterator it = SUPPORTED_REQUESTS.iterator();
            while (it.hasNext()) {
                String elem = (String) it.next();
                if(elem.equalsIgnoreCase(givenRequest)) {
                    supported_request = true;
                }
            }
        }
        if (!supported_request)
            throw new UnsupportedOperationException("Request '" + givenRequest + "' not supported!");
        
        RequestHandler requestHandler = null;
        List reqParams = null;
        
        if(givenRequest.equalsIgnoreCase(WMS_REQUEST_GetCapabilities)) {
            requestHandler = new GetCapabilitiesRequestHandler();
            reqParams = PARAMS_GetCapabilities;
            data.setContentDisposition("inline; filename=\"GetCapabilities.xml\";");
        } else if (givenRequest.equalsIgnoreCase(WMS_REQUEST_GetMap)) {
            requestHandler = new GetMapRequestHandler();
            reqParams = PARAMS_GetMap;
            String ct = (String)((String[])parameters.get(WMS_PARAM_FORMAT))[0];
            data.setContentType(ct);
        } else if (givenRequest.equalsIgnoreCase(WMS_REQUEST_GetFeatureInfo)) {
            requestHandler = new GetFeatureInfoRequestHandler();
            reqParams = PARAMS_GetFeatureInfo;
            data.setContentDisposition("inline; filename=\"GetCapabilities.xml\";");
            String infoFormat = ((String)((String[])parameters.get(WMS_PARAM_INFO_FORMAT))[0]);
            if (infoFormat==null)
                infoFormat = "application/vnd.ogc.gml";
            data.setContentType(infoFormat);
        } else if (givenRequest.equalsIgnoreCase(WMS_REQUEST_GetLegendGraphic)) {
            requestHandler = new GetLegendGraphicRequestHandler();
            reqParams = PARAMS_GetLegendGraphic;
            data.setContentDisposition("inline; filename=\"GetCapabilities.xml\";");
            String infoFormat = ((String)((String[])parameters.get(WMS_PARAM_FORMAT))[0]);
            if (infoFormat==null)
                infoFormat = "image/png";
            data.setContentType(infoFormat);
        }
        
        if (!requestComplete(parameters, reqParams))
            throw new IllegalArgumentException("Not all parameters for request '" +
                    givenRequest + "' are available, required: [" + reqParams.toString() +
                    "], available: [" + parameters.toString() + "].");
        
        data = requestHandler.getRequest(data, parameters);
        
        return data;
        
    }
    // </editor-fold>
    
    private String checkCaseInsensitiveParameter(Map <String, Object> parameters, String param) {
        //The list with parameters is now checked with predefined parameters
        //This goes all good if we use the same writing in the request as defined
        //in the predefined list. This should offcourse be case insensitive
        //Therefore we need to go over the list of parameters and check the keys
        //which hold the variables
        Set paramKeySet = parameters.keySet();
        Iterator keySetIterator = paramKeySet.iterator();
        String requestType = null;
        while (keySetIterator.hasNext()) {
            String key = (String)keySetIterator.next();
            if (key.equalsIgnoreCase(param)) {
                return key;
            }
        }
        return null;
    }
    
    /** Handles the requestComplete method.
     *
     * @param parameters map with the given parameters
     * @param requiredParameters list with the required parameters
     *
     * @return boolean which indicates if the request was incomplete or not
     */
    // <editor-fold defaultstate="" desc="requestComplete(Map parameters, List requiredParameters) method">
    protected boolean requestComplete(Map parameters, List requiredParameters) {
        if (parameters == null || requiredParameters == null || (parameters.isEmpty() && !requiredParameters.isEmpty()))
            return false;
        
        // lijst met default waarden voor parameters die eigenlijk verplicht zijn, goed idee?
        HashMap defVals = new HashMap();
        defVals.put(WMS_PARAM_STYLES, "");
        
        Iterator it = requiredParameters.iterator();
        while (it.hasNext()) {
            String reqParam = (String)it.next();
            if (!parameters.containsKey(reqParam)) {
                if (!defVals.containsKey(reqParam))
                    return false;
                parameters.put(reqParam, defVals.get(reqParam));
            }
        }
        return true;
    }
    
    /*
    protected boolean requestComplete(Map parameters, List requiredParameters) {
        if (parameters == null || requiredParameters == null || (parameters.isEmpty() && !requiredParameters.isEmpty()))
            return false;
        
        // lijst met default waarden voor parameters die eigenlijk verplicht zijn, goed idee?
        HashMap defVals = new HashMap();
        defVals.put(WMS_PARAM_STYLES, "");
        
        Iterator it = requiredParameters.iterator();
        while (it.hasNext()) {
            String reqParam = (String)it.next();
            String requestType = checkCaseInsensitiveParameter(parameters, reqParam);
            
            if (requestType != null) {
                String requestType_Style = checkCaseInsensitiveParameter(defVals, reqParam);
                if (requestType_Style != null)
                    return false;
                parameters.put(reqParam, defVals.get(reqParam));
            }
        }
        return true;
    }
     */
    // </editor-fold>
    
    // <editor-fold defaultstate="" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String remote = request.getRemoteAddr();
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String remote = request.getRemoteAddr();
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "CallWMSServlet info";
    }
    // </editor-fold>
}
