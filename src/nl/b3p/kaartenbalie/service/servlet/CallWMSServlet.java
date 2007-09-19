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

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import nl.b3p.kaartenbalie.service.AccessDeniedException;
import nl.b3p.kaartenbalie.service.requesthandler.*;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.http.*;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.service.MyDatabase;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import nl.b3p.wms.capabilities.KBConstants;
import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

public class CallWMSServlet extends HttpServlet implements KBConstants {
    private static Log log = null;
    public static final long serialVersionUID = 24362462L;
    private String format;
    private String inimageType;

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
        DataWrapper data = new DataWrapper(response);        
        User user = null;
        log.info("Request: " + request.getServletPath() + request.getPathInfo() + request.getQueryString());
        try {
            //Create a map with parameters of of request parameters given
            //with the request of this user and transforms each of these
            //parameters and their keys into uppercase values.
            Map parameters = getUpperCaseParameterMap(request.getParameterMap());
            
            //Get the information about the user performing the request
            //if the user doesn't exist the method will throw an exception
            user = checkLogin(request, parameters, data);

            //Setting the header for this specific user so that any action
            //of the user can be logged.
            if(user != null) {
            	data.setHeader("X-Kaartenbalie-User", user.getUsername());
            }

            //TODO: setHeader met de tijd die verstreken is in de periode van het ophalen van de kaart.

            

            //put two extra parameters into the map, since these two vars
            //are needed at several places in the application.
            parameters.put(KB_USER, user);
            parameters.put(KB_PERSONAL_URL, request.getRequestURL().toString());

            //Finally call the parse and request method.
            parseRequestAndData(data, parameters);
        } catch (Exception ex) {
            log.error("error: ", ex);
            String errorContentType = data.getErrorContentType();

            if(errorContentType != null) {
                String exceptionName, message, cause;

                try {
                    exceptionName = ex.getClass().getName();
                } catch (Exception e) {
                    exceptionName = "";
                }

                try {
                    message = ex.getMessage();
                } catch (Exception e) {
                    message = "";
                }

                try {
                    TextToImage tti = new TextToImage();
                    data.setContentType(errorContentType);

                    /*
                     * Inside TextToImage, when the image with the exception has been created, the image will be stored
                     * into the Datawrapper and sent directly. This means we don't have to given another sent command
                     * anymore after calling method below.
                     */
                    int width  = Integer.parseInt((String)data.getParameters().get(WMS_PARAM_WIDTH));
                    int height = Integer.parseInt((String)data.getParameters().get(WMS_PARAM_HEIGHT));
                    
                    tti.createImage(message, data.getErrorContentType().substring(data.getErrorContentType().indexOf("/") + 1), data);
                } catch (Exception e) {
                    log.error("error: ", e);
                }
            } else {
                ByteArrayOutputStream output = null;
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setValidating(true);
                DocumentBuilder db = null;
                try {
                    db = dbf.newDocumentBuilder();
                } catch (Exception e) {
                    log.error("error: ", e);
                    throw new IOException("Exception occured during creation of error message: " + e);
                }
                DOMImplementation di = db.getDOMImplementation();

                // <!DOCTYPE ServiceExceptionReport SYSTEM "http://schemas.opengeospatial.net/wms/1.1.1/exception_1_1_1.dtd"
                // <!-- end of DOCTYPE declaration -->
                DocumentType dt = di.createDocumentType("ServiceExceptionReport",null,"http://schemas.opengeospatial.net/wms/1.1.1/exception_1_1_1.dtd");
                Document dom = di.createDocument(null, "ServiceExceptionReport", dt);
                Element rootElement = dom.getDocumentElement();
                rootElement.setAttribute("version", "1.1.1");

                Element serviceExceptionElement = dom.createElement("ServiceException");
                
                String exceptionName;
                try {
                    exceptionName = ex.getClass().getName();
                } catch (Exception e) {
                    exceptionName = "";
                }
                
                String message;
                
                try {
                    message = ex.getMessage();
                } catch (Exception e) {
                    message = "";
                }
                
                Throwable cause;
                
                try {
                    cause = ex.getCause();
                } catch (Exception e) {
                    cause = null;
                }
                
                serviceExceptionElement.setAttribute("code", exceptionName);
                CDATASection cdata = null;
                if(cause != null) {
                    cdata = dom.createCDATASection(message + " - " + cause);
                } else {
                    cdata = dom.createCDATASection(message);
                }
                
                serviceExceptionElement.appendChild(cdata);
                rootElement.appendChild(serviceExceptionElement);

                /*
                 * Create a new output format to which this document should be translated and
                 * serialize the tree to an XML document type
                 */
                OutputFormat format = new OutputFormat(dom);

                format.setIndenting(true);
                output = new ByteArrayOutputStream();
                XMLSerializer serializer = new XMLSerializer(output, format);
                serializer.serialize(dom);

                DOMValidator dv = new DOMValidator();
                try {
                    dv.parseAndValidate(new ByteArrayInputStream(output.toString().getBytes(CHARSET)));
                } catch (Exception e) {
                    log.error("error: ", e);
                    throw new IOException("Exception occured during validation of error message: " + e);
                }
                data.setHeader("Content-Disposition", "inline; filename=\"ServiceException.xml\";");
                data.setContentType("application/vnd.ogc.se_xml");
                data.write(output);
            }
        }
    }
    // </editor-fold>


    /** Checks if an user is allowed to make any requests.
     * Therefore there is checked if a user is logged in or if a user is using a private unique IP address.
     *
     * @param parameters The parameters of a given request
     *
     * @return Map with the same parameters and same key all in uppercase
     */
    // <editor-fold defaultstate="" desc="getUpperCaseParameterMap(Map parameters) method.">
    private Map getUpperCaseParameterMap(Map parameters) {
        Map newParameterMap = new HashMap();
        Set paramKeySet = parameters.keySet();
        Iterator keySetIterator = paramKeySet.iterator();
        while (keySetIterator.hasNext()) {
            String key   = (String)keySetIterator.next();
            String value = ((String[]) parameters.get(key))[0];

            String caseInsensitiveKey   = key.toUpperCase();
            String caseInsensitiveValue = value.toUpperCase();

            newParameterMap.put(caseInsensitiveKey, caseInsensitiveValue);
        }
        return newParameterMap;
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
    public User checkLogin(HttpServletRequest request, Map parameters, DataWrapper dw) throws NoSuchAlgorithmException, UnsupportedEncodingException, AccessDeniedException {

        // eerst checken of user gewoon ingelogd is
        User user = (User) request.getUserPrincipal();
        if (user == null) {
            if (parameters.containsKey(WMS_PARAM_EXCEPTION_FORMAT)) {
                if (parameters.containsKey(WMS_PARAM_WIDTH) && parameters.containsKey(WMS_PARAM_HEIGHT)) {
                    int width  = Integer.parseInt((String)parameters.get(WMS_PARAM_WIDTH));
                    int height = Integer.parseInt((String)parameters.get(WMS_PARAM_HEIGHT));
                    if(width >= 1 || height >= 1 || width <= 2048 || height <= 2048) {
                        if(((String) parameters.get(WMS_PARAM_EXCEPTION_FORMAT)).equalsIgnoreCase("inimage")) {
                            dw.setErrorContentType((String) parameters.get(WMS_PARAM_FORMAT));
                            dw.setParameters(parameters);
                        }
                    }
                }
            }
            
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
                java.util.Date date = (java.util.Date)user.getTimeout();

                if (date.compareTo(new java.util.Date()) <= 0) {
                    throw new AccessDeniedException("Personal URL key has expired!");
                }

                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                String token = calcToken(
                        request.getRemoteAddr(),
                        user.getUsername(),
                        user.getPassword(),
                        df.format(date));

                // vraag het token uit ingegeven url op
                String urlToken = request.getPathInfo().substring(1);

                if (!urlToken.equals(token)) {
                    throw new AccessDeniedException("Personal URL not found!");
                }
            } else {
                throw new AccessDeniedException("Authorisation required for this service!");
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
        MessageDigest md = MessageDigest.getInstance(MD_ALGORITHM);
        md.update(toBeHashedString.getBytes(CHARSET));
        byte[] md5hash = md.digest();
        return new String(Hex.encodeHex(md5hash));
    }
    // </editor-fold>

    /** Parses any incoming request and redirects this request to the right handler.
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
    public void parseRequestAndData(DataWrapper data, Map parameters) throws IllegalArgumentException, UnsupportedOperationException, IOException, Exception {
        /*
         * First we need to find out if a request given by the user is supported by the system
         * If a request is not supported then we need to inform the user about this and throw
         * an error. Otherwise we need to execute the request and provide the information the
         * user asked for.
         */
        String givenRequest = null;
        boolean supported = false;

        if (parameters.get(WMS_REQUEST)!=null){
            givenRequest = (String) parameters.get(WMS_REQUEST);

            Iterator it = SUPPORTED_REQUESTS.iterator();
            while (it.hasNext()) {
                String supported_requests = (String) it.next();
                if(supported_requests.equals(givenRequest)) {
                    supported = true;
                }
            }
        }

        if (!supported) {
            throw new UnsupportedOperationException("Request '" + givenRequest + "' not supported! Use GetCapabilities request to " +
                    "get the list of supported functions. Usage: i.e. http://urltoserver/personalurl?REQUEST=GetCapabilities&" +
                    "VERSION=1.1.1&SERVICE=WMS");
        }

        /*
         * The request is supported so now we can go ahed and find the right information which
         * the user asked for.
         */
        RequestHandler requestHandler = null;
        List reqParams = null;
        if(givenRequest.equals(WMS_REQUEST_GetCapabilities)) {
            requestHandler = new GetCapabilitiesRequestHandler();
            reqParams = PARAMS_GetCapabilities;
        } else if (givenRequest.equals(WMS_REQUEST_GetMap)) {

            //Att all time set the error contenttype at first....
            String format = (String) parameters.get(WMS_PARAM_FORMAT);
            String inimageType = null;
            if (parameters.containsKey(WMS_PARAM_EXCEPTION_FORMAT)) {
                inimageType = format;
            }
            data.setErrorContentType(inimageType);

            requestHandler = new GetMapRequestHandler();
            reqParams = PARAMS_GetMap;
        } else if (givenRequest.equalsIgnoreCase(WMS_REQUEST_GetFeatureInfo)) {
            requestHandler = new GetFeatureInfoRequestHandler();
            reqParams = PARAMS_GetFeatureInfo;
        } else if (givenRequest.equalsIgnoreCase(WMS_REQUEST_GetLegendGraphic)) {
            requestHandler = new GetLegendGraphicRequestHandler();
            reqParams = PARAMS_GetLegendGraphic;
        }

        /*
         * If the request is supported and we also know which variables are given in the request, then we
         * also first need to find out if all the mandatory variables are given in the request. If not we
         * cannot proceed with the request.
         */
        if (!requestComplete(parameters, reqParams)) {
            StringBuffer availableParams = new StringBuffer();
            Set paramKeySet = parameters.keySet();
            Iterator keySetIterator = paramKeySet.iterator();
            while (keySetIterator.hasNext()) {
                availableParams.append((String)keySetIterator.next());
                availableParams.append(", ");
            }
            throw new IllegalArgumentException("Not all parameters for request '" +
                    givenRequest + "' are available, required: [" + reqParams.toString() +
                    "], available: [" + availableParams.toString() + "].");
        }

        /*
         * All clear, we can continue!
         */
        requestHandler.getRequest(data, parameters);
    }
    // </editor-fold>

    /** Checks if the parameters of a given request comply to the required parameters.
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
