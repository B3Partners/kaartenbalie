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
    // <editor-fold defaultstate="collapsed" desc="init(ServletConfig config) method.">
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
    // <editor-fold defaultstate="collapsed" desc="processRequest(HttpServletRequest request, HttpServletResponse response) method.">
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long firstMeasurement = System.currentTimeMillis();
        User user = null;
        byte[] data = null;
        OutputStream sos = null;
                
        try {
            long secondMeasurement = System.currentTimeMillis();
            user = checkLogin(request);
            long thirdMeasurement = System.currentTimeMillis();
            
            if (user != null) {
                Map <String, Object> parameters = new HashMap <String, Object>();
                parameters.putAll(request.getParameterMap());
                parameters.put(KB_USER, user);
                parameters.put(KB_PERSONAL_URL, request.getRequestURL().toString());
                long fourthMeasurement = System.currentTimeMillis();
                data = parseRequestAndData(parameters);
                
                String dataRepresent = new String(data);
                System.out.println("The data is : " + dataRepresent);
                
                long fifthMeasurement = System.currentTimeMillis();
                
                long sixthMeasurement = System.currentTimeMillis();
                if (parameters.get(WMS_PARAM_EXCEPTION_FORMAT) != null) {
                    String exception = ((String[]) parameters.get(WMS_PARAM_EXCEPTION_FORMAT))[0];
                    response.setContentType(exception + ";charset=" + CHARSET_UTF8);
                }

//                if (!SUPPORTED_EXCEPTIONS.contains(exception)) {
//                    response.setContentType(exception + ";charset=" + CHARSET_UTF8);
//                } else {
//                    response.setContentType("text/xml;charset=" + CHARSET_UTF8);
//                }
                long seventhMeasurement = System.currentTimeMillis();
                sos = response.getOutputStream();
                sos.write(data);
                long eighthMeasurement = System.currentTimeMillis();
                
                /*-----------------------------------------------------------------------------------------------------------*/
//                System.out.println("Resultaten van verschillende metingen : ");
                long elapsedTimeMillis = secondMeasurement - firstMeasurement;
                long elapsedTimeSecond = elapsedTimeMillis / 1000; // total # of seconds
                elapsedTimeMillis = elapsedTimeMillis % 1000; // total number of millis left 
//                System.out.println("Kleine initialisatie : Seconds = " + elapsedTimeSecond + " millis = " + elapsedTimeMillis);
                
                elapsedTimeMillis = thirdMeasurement - secondMeasurement;
                elapsedTimeSecond = elapsedTimeMillis / 1000; // total # of seconds
                elapsedTimeMillis = elapsedTimeMillis % 1000; // total number of millis left 
//                System.out.println("Gebruikersrechten test : Seconds = " + elapsedTimeSecond + " millis = " + elapsedTimeMillis);
                
                elapsedTimeMillis = fourthMeasurement - thirdMeasurement;
                elapsedTimeSecond = elapsedTimeMillis / 1000; // total # of seconds
                elapsedTimeMillis = elapsedTimeMillis % 1000; // total number of millis left 
//                System.out.println("Kopie van alle params : Seconds = " + elapsedTimeSecond + " millis = " + elapsedTimeMillis);
                
                elapsedTimeMillis = fifthMeasurement - fourthMeasurement;
                elapsedTimeSecond = elapsedTimeMillis / 1000; // total # of seconds
                elapsedTimeMillis = elapsedTimeMillis % 1000; // total number of millis left 
//                System.out.println("Het parsen van het request : Seconds = " + elapsedTimeSecond + " millis = " + elapsedTimeMillis);
                
                elapsedTimeMillis = sixthMeasurement - fifthMeasurement;
                elapsedTimeSecond = elapsedTimeMillis / 1000; // total # of seconds
                elapsedTimeMillis = elapsedTimeMillis % 1000; // total number of millis left 
//                System.out.println("Printout op scherm van data : Seconds = " + elapsedTimeSecond + " millis = " + elapsedTimeMillis);
                
                elapsedTimeMillis = eighthMeasurement - seventhMeasurement;
                elapsedTimeSecond = elapsedTimeMillis / 1000; // total # of seconds
                elapsedTimeMillis = elapsedTimeMillis % 1000; // total number of millis left 
//                System.out.println("Doorvoer van gegevens naar client : Seconds = " + elapsedTimeSecond + " millis = " + elapsedTimeMillis);
                
                
            } else {
                response.sendError(response.SC_UNAUTHORIZED);
            }
        } catch (NoSuchAlgorithmException ex) {
            log.error("NoSuchAlgorithmException error: ", ex);
            throw new ServletException("NoSuchAlgorithmException", ex);
        } catch (UnsupportedEncodingException ex) {
            log.error("UnsupportedEncodingException error: ", ex);
            throw new ServletException("UnsupportedEncodingException", ex);
        } catch (Exception e) {
            log.error("Undefined error: ", e);
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
    // <editor-fold defaultstate="collapsed" desc="checkLogin(HttpServletRequest request) method.">
    public User checkLogin(HttpServletRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        
        // eerst checken of user gewoon ingelogd is
        User user = (User) request.getUserPrincipal();
        if (user==null) {
            // niet ingelogd dus, dan checken op token in url
            Session sess = MyDatabase.currentSession();
            Transaction tx = sess.beginTransaction();
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
                Date date = user.getTimeout();
                
                if (date.compareTo(new Date()) <= 0) {
                    System.out.println("The date for your personal URL has expired");
                    return null;
                } else {
                    System.out.println("The date for your personal URL is still ok");
                }
                
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                // Parse with a custom format
                String personalDate = df.format(date);
                System.out.println("De opgegeven datum is : " + date.toString());
                                
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
    // <editor-fold defaultstate="collapsed" desc="calcToken(String registeredIP, String username, String password) method.">
    private String calcToken(String registeredIP, String username, String password, String personalDate) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String toBeHashedString = registeredIP + username + password + personalDate;
        System.out.println("String to be hashed in CallWMS is  : " + toBeHashedString);
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(toBeHashedString.getBytes("8859_1")); // UTF-8 ???
        BigInteger hash = new BigInteger(1, md.digest());
        
        
        System.out.println("Personal URL in CallWMSServlet: " + hash.toString( 16 ));
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
    // <editor-fold defaultstate="collapsed" desc="parseRequestAndData(Map parameters) method.">
    public byte[] parseRequestAndData(Map <String, Object> parameters) throws IllegalArgumentException, UnsupportedOperationException, IOException {
        String givenRequest = ((String[]) parameters.get(WMS_REQUEST))[0];
        
        boolean supported_request = false;
        Iterator it = SUPPORTED_REQUESTS.iterator();
        while (it.hasNext()) {
            String elem = (String) it.next();
            if(elem.equalsIgnoreCase(givenRequest)) {
                supported_request = true;
            }
        }
        if (!supported_request)
            throw new UnsupportedOperationException("Request '" + givenRequest + "' not supported!");
        
        /*
        String givenService = ((String[]) parameters.get(WMS_SERVICE))[0];
        if (!SUPPORTED_SERVICES.contains(givenService))
            throw new UnsupportedOperationException("Service '" + givenService + "' not supported!");
        
        String givenVersion = ((String[]) parameters.get(WMS_VERSION))[0];
        if (!SUPPORTED_VERSIONS.contains(givenVersion))
            throw new UnsupportedOperationException("Version '" + givenVersion + "' not supported!");
        */
        
        RequestHandler requestHandler = null;
        List reqParams = null;
        if(givenRequest.equalsIgnoreCase(WMS_REQUEST_GetCapabilities)) {
            requestHandler = new GetCapabilitiesRequestHandler();
            reqParams = PARAMS_GetCapabilities;
        } else if (givenRequest.equalsIgnoreCase(WMS_REQUEST_GetMap)) {
            requestHandler = new GetMapRequestHandler();
            reqParams = PARAMS_GetMap;
        } else if (givenRequest.equalsIgnoreCase(WMS_REQUEST_GetFeatureInfo)) {
            requestHandler = new GetFeatureInfoRequestHandler();
            reqParams = PARAMS_GetFeatureInfo;
        } else if (givenRequest.equalsIgnoreCase(WMS_REQUEST_GetLegendGraphic)) {
            requestHandler = new GetLegendGraphicRequestHandler();
            reqParams = PARAMS_GetLegendGraphic;
        }
        
        if (!requestComplete(parameters, reqParams))
            throw new IllegalArgumentException("Not all parameters for request '" +
                    givenRequest + "' are available, required: [" + reqParams.toString() +
                    "], available: [" + parameters.toString() + "].");
        
        return requestHandler.getRequest(parameters);
    }
    // </editor-fold>
    
    /** Handles the requestComplete method.
     *
     * @param parameters map with the given parameters
     * @param requiredParameters list with the required parameters
     *
     * @return boolean which indicates if the request was incomplete or not
     */
    // <editor-fold defaultstate="collapsed" desc="requestComplete(Map parameters, List requiredParameters) method">
    protected boolean requestComplete(Map parameters, List requiredParameters) {
        if (parameters == null || requiredParameters == null || (parameters.isEmpty() && !requiredParameters.isEmpty()))
            return false;
        Iterator it = requiredParameters.iterator();
        while (it.hasNext()) {
            String reqParam = (String)it.next();
            if (!parameters.containsKey(reqParam))
                return false;
        }
        return true;
    }
    // </editor-fold>
    
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
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "CallWMSServlet info";
    }
    // </editor-fold>
}
