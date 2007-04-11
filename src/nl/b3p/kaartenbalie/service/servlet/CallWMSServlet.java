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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import nl.b3p.kaartenbalie.core.AccessDeniedException;
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
import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

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
    
    //TODO:
    // De foutafhandeling in processRequest moet nog verbeterd worden.
    // De wms client kan vragen om een image of een xml bestand als return voor een foutmelding.
    // Daartoe dient ook de response.setContentType(CHARSET); aangepast te worden,
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
        DataWrapper data = new DataWrapper(response);
        User user = null;
        
        try {
            //Get the information about the user performing the request
            //if the user doesn't exist the method will throw an exception
            user = checkLogin(request);
            
            //Setting the header for this specific user so that any action
            //of the user can be logged.
            data.setHeader("X-Kaartenbalie-User", user.getUsername());
            
            //Create a map with parameters of of reques parameters given
            //with the request of this user.
            Map parameters = new HashMap();
            parameters.putAll(request.getParameterMap());
            parameters.put(KB_USER, user);
            parameters.put(KB_PERSONAL_URL, request.getRequestURL().toString());
            
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
                
                //Creeer nu een plaatje van deze gegegevens en stuur dit plaatje uit.
                // configure all of the parameters
                try {
                TextToImage tti = new TextToImage();
                data.setContentType(errorContentType);
                tti.createImage(message, data.getErrorContentType().substring(data.getErrorContentType().indexOf("/") + 1), data);
                
                //data.write(baos);
                //baos.close();
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
                serviceExceptionElement.setAttribute("code", ex.getClass().getName());
                
                CDATASection cdata = dom.createCDATASection(ex.getMessage() + " - " + ex.getCause());
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
                
                data.setContentType("application/vnd.ogc.se_xml");
                data.write(output);
            }
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
    public User checkLogin(HttpServletRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException, AccessDeniedException {
        
        // eerst checken of user gewoon ingelogd is
        User user = (User) request.getUserPrincipal();
        if (user==null) {
            // niet ingelogd dus, dan checken op token in url
            Session sess = MyDatabase.currentSession();
            Transaction tx = sess.beginTransaction();
            String url = request.getRequestURL().toString();
            String remote = request.getRemoteAddr();
            
            System.out.println("remote address: " + remote);
            
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
                    return null;
                }
                
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                // Parse with a custom format
                String personalDate = df.format(date);
                
                // bereken token voor deze user
                String token = calcToken(
                        request.getRemoteAddr(),
                        user.getUsername(),
                        user.getPassword(),
                        personalDate);
                
                // bereken token in url
                String pathInfo = request.getPathInfo();
                String urlToken = pathInfo.substring(1);
                
                System.out.println("token: " + token);
                
                if (!urlToken.equals(token)) {
                    // ongeldig token!
                    return null;
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
        BigInteger hash = new BigInteger(1, md.digest());
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
    public void parseRequestAndData(DataWrapper data, Map parameters) throws IllegalArgumentException, UnsupportedOperationException, IOException, Exception {
        
        String givenRequest=null;
        boolean supported_request = false;
        
        String requestType = checkCaseInsensitiveParameter(parameters, WMS_REQUEST);
        if (parameters.get(requestType)!=null){
            givenRequest = ((String[]) parameters.get(requestType))[0];
            
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
            data.setHeader("Content-Disposition", "inline; filename=\"GetCapabilities.xml\";");
            data.setContentType("application/vnd.ogc.wms_xml");
            
        } else if (givenRequest.equalsIgnoreCase(WMS_REQUEST_GetMap)) {
                        
            requestHandler = new GetMapRequestHandler();
            reqParams = PARAMS_GetMap;
            try {
                format = (String)((String[])parameters.get(WMS_PARAM_FORMAT))[0];
            } catch (Exception e) {
                format = "image/png";
            }
            
            try {
                inimageType = (String)((String[]) parameters.get(WMS_PARAM_EXCEPTION_FORMAT))[0];
            } catch (Exception e) {
                inimageType = null;
            }
            if (inimageType != null) {
                data.setErrorContentType(format);
            }
                
            data.setContentType(format);
            
        } else if (givenRequest.equalsIgnoreCase(WMS_REQUEST_GetFeatureInfo)) {
            
            requestHandler = new GetFeatureInfoRequestHandler();
            reqParams = PARAMS_GetFeatureInfo;
            data.setHeader("Content-Disposition", "inline; filename=\"GetCapabilities.xml\";");
            try {
                format = (String)((String[])parameters.get(WMS_PARAM_INFO_FORMAT))[0];
            } catch (Exception e) {
                format = "application/vnd.ogc.gml";
            }
            data.setContentType(format);
            
        } else if (givenRequest.equalsIgnoreCase(WMS_REQUEST_GetLegendGraphic)) {
            
            requestHandler = new GetLegendGraphicRequestHandler();
            reqParams = PARAMS_GetLegendGraphic;
            data.setHeader("Content-Disposition", "inline; filename=\"GetCapabilities.xml\";");
            try {
                format = (String)((String[])parameters.get(WMS_PARAM_INFO_FORMAT))[0];
            } catch (Exception e) {
                format = "image/png";
            }
            data.setContentType(format);
            
        }
        
        if (!requestComplete(parameters, reqParams))
            throw new IllegalArgumentException("Not all parameters for request '" +
                    givenRequest + "' are available, required: [" + reqParams.toString() +
                    "], available: [" + parameters.toString() + "].");
        
        //This can throw also a ParserConfigurationException.
        requestHandler.getRequest(data, parameters);       
    }
    // </editor-fold>
    
    private String checkCaseInsensitiveParameter(Map parameters, String param) {
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
    
    private Object createAndValidateErrorXML() {
        //Kan een Boolean object, Image object of XML String object retouneren
        Object object = new Object();
        return object;
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
