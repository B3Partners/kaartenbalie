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

import javax.transaction.NotSupportedException;
import nl.b3p.kaartenbalie.core.server.reporting.domain.requests.WMSGetCapabilitiesRequest;
import nl.b3p.kaartenbalie.core.server.reporting.domain.requests.WMSGetFeatureInfoRequest;
import nl.b3p.kaartenbalie.core.server.reporting.domain.requests.WMSGetLegendGraphicRequest;
import nl.b3p.kaartenbalie.core.server.reporting.domain.requests.WMSGetMapRequest;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import java.util.HashMap;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import nl.b3p.kaartenbalie.service.AccessDeniedException;
import nl.b3p.kaartenbalie.service.requesthandler.*;
import java.io.*;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.http.*;
import nl.b3p.kaartenbalie.core.server.User;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.codec.binary.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.xml.parsers.ParserConfigurationException;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.core.server.reporting.control.DataMonitoring;
import nl.b3p.ogc.utils.KBConstants;
import nl.b3p.ogc.utils.OGCRequest;
import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.xml.sax.SAXParseException;

public class CallWMSServlet extends HttpServlet implements KBConstants {
    private static Log log = null;
    public static final long serialVersionUID = 24362462L;
    private String format;
    private String inimageType;
    public static String CAPABILITIES_DTD = null;
    
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
        //TODO; make this smarter!!;
        //This is the dataset required for reporting...
        
        long startTime = System.currentTimeMillis();
        int totalDatasize = 0;
        
        
        
        String scheme       = request.getScheme();
        String serverName   = request.getServerName();
        int serverPort      = request.getServerPort();
        String contextPath  = request.getContextPath();
        String servletPath  = request.getServletPath();
        String pathInfo     = request.getPathInfo();
        String queryString  = request.getQueryString();
        
        StringBuffer theUrl = new StringBuffer(scheme);
        theUrl.append("://");
        theUrl.append(serverName);
        if ((scheme.equals("http") && serverPort!=80) || (scheme.equals("https") && serverPort!=443)) {
            theUrl.append(":");
            theUrl.append(serverPort);
        }
        theUrl.append(contextPath);
        
        if (CAPABILITIES_DTD == null) {
            StringBuffer dtdUrl = theUrl;
            dtdUrl.append(MyEMFDatabase.getDtd());
            CAPABILITIES_DTD = dtdUrl.toString();
        }
        
        theUrl.append(servletPath);
        theUrl.append(pathInfo);
        theUrl.append("?");
        theUrl.append(queryString);
        log.info("Request: " + theUrl.toString());
        
        DataWrapper data = new DataWrapper(response);
        User user = null;
        
        
        //TODO:
        //Complete request mag voor kaartenbalie weg.... is deze nog van belang voor reporting of
        //mag deze in zijn geheel vervangen worden voor theUrl.toString()???
        //Is de URL string wel de hoeveelheid bytes die van de User ontvangen wordt?
        //Er wordt een reporting gestart op user, terwijl user nog null is???
        String completeRequest = request.getServletPath() + request.getPathInfo() + "?" + request.getQueryString();
        
        
        try {
            OGCRequest ogcrequest = new OGCRequest(theUrl.toString());
            data.setOgcrequest(ogcrequest);
            
            StringBuffer reason = new StringBuffer();
            if (ogcrequest.containsParameter(WMS_PARAM_FORMAT)) {
                String format = ogcrequest.getParameter(WMS_PARAM_FORMAT);
                data.setContentType(format);
                
                if (ogcrequest.containsParameter(WMS_PARAM_EXCEPTION_FORMAT)) {
                    if (ogcrequest.containsParameter(WMS_PARAM_WIDTH) && ogcrequest.containsParameter(WMS_PARAM_HEIGHT)) {
                        int width  = Integer.parseInt(ogcrequest.getParameter(WMS_PARAM_WIDTH));
                        int height = Integer.parseInt(ogcrequest.getParameter(WMS_PARAM_HEIGHT));
                        if(width >= 1 || height >= 1 || width <= 2048 || height <= 2048) {
                            String exceptionFormat = ogcrequest.getParameter(WMS_PARAM_EXCEPTION_FORMAT);
                            if(exceptionFormat.equalsIgnoreCase(WMS_PARAM_EXCEPTION_INIMAGE) || 
                                    exceptionFormat.equalsIgnoreCase(WMS_PARAM_SHORT_EXCEPTION_INIMAGE)) {
                                data.setErrorContentType(format);
                            }
                        }
                    }
                }
            } else {
                data.setContentType(WMS_PARAM_EXCEPTION_XML);
            }
            
            boolean isvalid = ogcrequest.isValidRequestURL(reason);
            if(!isvalid){ 
                log.error(reason);
                throw new Exception(reason.toString());
            }
            
            user = checkLogin(request);
            
            DataMonitoring rr = new DataMonitoring(user);
            data.setRequestReporting(rr);
            rr.startClientRequest(completeRequest, theUrl.toString().getBytes().length, startTime);
                        
            data.setHeader("X-Kaartenbalie-User", user.getUsername());            
            parseRequestAndData(data, user);
            rr.endClientRequest(data.getContentLength(),System.currentTimeMillis() - startTime);
        }catch (Exception ex) {
            log.error("",ex);
            
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
                    int width  = Integer.parseInt(data.getOgcrequest().getParameter(WMS_PARAM_WIDTH));
                    int height = Integer.parseInt(data.getOgcrequest().getParameter(WMS_PARAM_HEIGHT));
                    
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
    public User checkLogin(HttpServletRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException, AccessDeniedException {
        EntityManager em = getEntityManager();
        // eerst checken of user gewoon ingelogd is
        User user = (User) request.getUserPrincipal();
        
        // probeer preemptive basic login
        if (user == null) {
            // attempt to dig out authentication info only if the user has not yet been authenticated
            String authorizationHeader = request.getHeader("Authorization");
            HttpSession session = request.getSession();
            if (authorizationHeader != null) {
                String decoded = decodeBasicAuthorizationString(authorizationHeader);
                String username = parseUsername(decoded);
                String password = parsePassword(decoded);
                // niet ingelogd dus, dan checken op token in url
                EntityTransaction tx = em.getTransaction();
                tx.begin();
                try {
                    user = (User)em.createQuery(
                            "from User u where " +
                            "lower(u.username) = lower(:username) " +
                            "and lower(u.password) = lower(:password)")
                            .setParameter("username", username)
                            .setParameter("password", password)
                            .getSingleResult();
                } catch (NoResultException nre) {
                    //Here nothing to do, because user gets second chance if this login fails.
                } finally {
                    tx.commit();
                }
            }
        }
        
        // probeer personal url
        if (user == null) {
            // niet ingelogd dus, dan checken op token in url
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            try {
                String url = request.getRequestURL().toString();
                String testurl = "http://localhost:8084/kaartenbalie/wms/d6cec623c87928f6b3404364d718b332";
                if(testurl.equalsIgnoreCase(url)) {
                    log.info("url is gelijk");
                }
                user = (User)em.createQuery(
                        "from User u where " +
                        "u.personalURL = :personalURL")
                        .setParameter("personalURL", url)
                        .getSingleResult();
            } catch (NoResultException nre) {
                throw new AccessDeniedException("Personal URL not found! Authorisation required for this service!");
            } finally {
                tx.commit();
            }
            
            java.util.Date date = user.getTimeout();
            
            if (date.compareTo(new java.util.Date()) <= 0) {
                throw new AccessDeniedException("Personal URL key has expired!");
            }
            
            String remoteaddress = request.getRemoteAddr();
            boolean validip = false;
            
            Set ipaddresses = user.getUserips();
            Iterator it = ipaddresses.iterator();
            while (it.hasNext()) {
                String ipaddress = (String) it.next();
                if(ipaddress.equalsIgnoreCase(remoteaddress)) {
                    validip = true;
                    break;
                }
            }
            
            if(!validip) {
                throw new AccessDeniedException("Personal URL not usuable for this IP address!");
            }
        }
        MyEMFDatabase.closeEntityManager();
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
        String toBeHashedString = username + password + personalDate;
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
    public void parseRequestAndData(DataWrapper data, User user) throws IllegalArgumentException, UnsupportedOperationException, IOException, Exception {
        RequestHandler requestHandler = null;
        String request = data.getOgcrequest().getParameter(REQUEST);
        if(request.equalsIgnoreCase(WMS_REQUEST_GetCapabilities)) {
            data.setRequestClassType(WMSGetCapabilitiesRequest.class);
            requestHandler = new GetCapabilitiesRequestHandler();
        } else if (request.equalsIgnoreCase(WMS_REQUEST_GetMap)) {
            data.setRequestClassType(WMSGetMapRequest.class);
            requestHandler = new GetMapRequestHandler();
        } else if (request.equalsIgnoreCase(WMS_REQUEST_GetFeatureInfo)) {
            data.setRequestClassType(WMSGetFeatureInfoRequest.class);
            requestHandler = new GetFeatureInfoRequestHandler();
        } else if (request.equalsIgnoreCase(WMS_REQUEST_GetLegendGraphic)) {
            data.setRequestClassType(WMSGetLegendGraphicRequest.class);
            requestHandler = new GetLegendGraphicRequestHandler();
        }
        requestHandler.getRequest(data, user);
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
    
    /**
     * Parse the username out of the BASIC authorization header string.
     * @param decoded
     * @return username parsed out of decoded string
     */
    private String parseUsername(String decoded) {
        if (decoded == null) {
            return null;
        } else {
            int colon = decoded.indexOf(':');
            if (colon < 0) {
                return null;
            } else {
                return decoded.substring(0, colon).trim();
            }
        }
    }
    
    /**
     * Parse the password out of the decoded BASIC authorization header string.
     * @param decoded
     * @return password parsed out of decoded string
     */
    private String parsePassword(String decoded) {
        if (decoded == null) {
            return null;
        } else {
            int colon = decoded.indexOf(':');
            if (colon < 0) {
                return (null);
            } else {
                return decoded.substring(colon + 1).trim();
            }
        }
    }
    
    /**
     * Decode the BASIC authorization string.
     *
     * @param authorization
     * @return decoded string
     */
    private String decodeBasicAuthorizationString(String authorization) {
        if (authorization == null || !authorization.toLowerCase().startsWith("basic ")) {
            return null;
        } else {
            authorization = authorization.substring(6).trim();
            // Decode and parse the authorization credentials
            return new String(Base64.decodeBase64(authorization.getBytes()));
        }
    }
    
    public static EntityManager getEntityManager() {
        return MyEMFDatabase.getEntityManager();
    }
    
}
