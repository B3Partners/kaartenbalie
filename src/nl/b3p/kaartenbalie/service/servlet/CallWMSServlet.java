/*
 * @(#)CallWMSServlet.java
 * @author Nando De Goeij
 * @version 1.00
 * Created on 25 oktober 2006, 10:21
 *
 * Purpose: portal to which a client sends his requests. This servlet takes all incoming requests and handles
 * each request differently depending on the input given in the request. The Servlets takes the KBConfiguration
 * interface to check for all the different parameters which can be send together with the request.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */
package nl.b3p.kaartenbalie.service.servlet;

import javax.persistence.EntityManagerFactory;
import nl.b3p.kaartenbalie.core.server.reporting.domain.requests.ProxyRequest;
import nl.b3p.kaartenbalie.core.server.reporting.domain.requests.WMSGetCapabilitiesRequest;
import nl.b3p.kaartenbalie.core.server.reporting.domain.requests.WMSGetFeatureInfoRequest;
import nl.b3p.kaartenbalie.core.server.reporting.domain.requests.WMSGetLegendGraphicRequest;
import nl.b3p.kaartenbalie.core.server.reporting.domain.requests.WMSGetMapRequest;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.KBCrypter;
import nl.b3p.ogc.utils.OGCRequest;
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
import nl.b3p.kaartenbalie.core.server.b3pLayering.ExceptionLayer;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.core.server.reporting.control.DataMonitoring;
import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

public class CallWMSServlet extends HttpServlet {
    
    private EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("defaultKaartenbaliePU");
    private EntityManager em = emf.createEntityManager();
    private static Log log = null;
    public static final long serialVersionUID = 24362462L;
    private String format;
    private String inimageType;
    public static String CAPABILITIES_DTD = null;
    public static String EXCEPTION_DTD = null;
    
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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        int totalDatasize = 0;
        
        
        StringBuffer baseUrl = createBaseUrl(request);
        if (CAPABILITIES_DTD == null) {
            CAPABILITIES_DTD = baseUrl.toString() + MyEMFDatabase.getCapabilitiesdtd();
        }
        if (EXCEPTION_DTD == null) {
            EXCEPTION_DTD = baseUrl.toString() + MyEMFDatabase.getExceptiondtd();
        }
        
        String iUrl = completeUrl(baseUrl, request).toString();
        log.debug("Incoming URL: " + iUrl);
        
        MyEMFDatabase.initEntityManager();
        EntityManager em = MyEMFDatabase.getEntityManager();
        User user = null;
        
        DataWrapper data = new DataWrapper(response);
        
        DataMonitoring rr = new DataMonitoring();
        data.setRequestReporting(rr);
        OGCRequest ogcrequest = null;
        
        try {
            if (request.getMethod().equalsIgnoreCase("GET")) {
                ogcrequest = new OGCRequest(iUrl);
            } else if (request.getMethod().equalsIgnoreCase("POST")) {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = dbf.newDocumentBuilder();
                Document doc = builder.parse(request.getInputStream());
                ogcrequest = new OGCRequest(doc.getDocumentElement(), baseUrl.toString());
            }
            data.setOgcrequest(ogcrequest);
            
            rr.startClientRequest(iUrl, iUrl.getBytes().length, startTime, request.getRemoteAddr(), request.getMethod());
            user = checkLogin(request);
            data.getOgcrequest().checkRequestURL();
            rr.setUserAndOrganization(user, user.getOrganization());
            data.setHeader("X-Kaartenbalie-User", user.getUsername());
            parseRequestAndData(data, user);
        } catch (Exception ex) {
            log.error("Error while handling request: ", ex);
            rr.setClientRequestException(ex);
            handleRequestException(ex, data);
        } finally {
            rr.endClientRequest("WMS", data.getOperation(), data.getContentLength(), System.currentTimeMillis() - startTime);
        }
        MyEMFDatabase.closeEntityManager();
    }
    
    private StringBuffer createBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();
        
        StringBuffer theUrl = new StringBuffer(scheme);
        theUrl.append("://");
        theUrl.append(serverName);
        if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
            theUrl.append(":");
            theUrl.append(serverPort);
        }
        theUrl.append(contextPath);
        return theUrl;
    }
    
    private StringBuffer completeUrl(StringBuffer baseUrl, HttpServletRequest request) {
        String servletPath = request.getServletPath();
        String pathInfo = request.getPathInfo();
        String queryString = request.getQueryString();
        
        if (servletPath != null && servletPath.length() != 0) {
            baseUrl.append(servletPath);
        }
        if (pathInfo != null && pathInfo.length() != 0) {
            baseUrl.append(pathInfo);
        }
        if (queryString != null && queryString.length() != 0) {
            baseUrl.append("?");
            baseUrl.append(queryString);
        }
        return baseUrl;
    }
    
    private void handleRequestException(Exception ex, DataWrapper data) throws IOException {
        OGCRequest ogcrequest = data.getOgcrequest();
        
        if (ogcrequest == null) {
            data.setContentType(OGCConstants.WMS_PARAM_EXCEPTION_XML);
            handleRequestExceptionAsXML(ex, data);
        }else if(ogcrequest.getParameter(OGCConstants.SERVICE).equals(OGCConstants.WFS_SERVICE_WFS)){
            data.setContentType(OGCConstants.WMS_PARAM_EXCEPTION_XML);
            handleRequestExceptionAsXML(ex, data);
        } else {
            String exType = "";
            if (ogcrequest.containsParameter(OGCConstants.WMS_PARAM_EXCEPTIONS)) {
                exType = ogcrequest.getParameter(OGCConstants.WMS_PARAM_EXCEPTIONS);
            }
            String requestparam = "";
            if (ogcrequest.containsParameter(OGCConstants.REQUEST)) {
                requestparam = ogcrequest.getParameter(OGCConstants.REQUEST);
            }
            if ((requestparam.equalsIgnoreCase(OGCConstants.WMS_REQUEST_GetMap) ||
                    requestparam.equalsIgnoreCase(OGCConstants.WMS_REQUEST_GetLegendGraphic)) &&
                    (exType.equalsIgnoreCase(OGCConstants.WMS_PARAM_EXCEPTION_INIMAGE) ||
                    exType.equalsIgnoreCase(OGCConstants.WMS_PARAM_SHORT_EXCEPTION_INIMAGE)) &&
                    ogcrequest.containsParameter(OGCConstants.WMS_PARAM_FORMAT) &&
                    ogcrequest.containsParameter(OGCConstants.WMS_PARAM_WIDTH) &&
                    ogcrequest.containsParameter(OGCConstants.WMS_PARAM_HEIGHT)) {
                data.setContentType(ogcrequest.getParameter(OGCConstants.WMS_PARAM_FORMAT));
                handleRequestExceptionAsImage(ex, data);
            } else {
                data.setContentType(OGCConstants.WMS_PARAM_EXCEPTION_XML);
                handleRequestExceptionAsXML(ex, data);
            }
        }
    }
    
    private void handleRequestExceptionAsImage(Exception ex, DataWrapper data) throws IOException {
        String message = ex.getMessage();
        try {
            ExceptionLayer el = new ExceptionLayer();
            Map parameterMap = new HashMap();
            parameterMap.put("type", ex.getClass());
            parameterMap.put("message", message);
            parameterMap.put("stacktrace", ex.getStackTrace());
            el.sendImage(data, el.drawImage(data.getOgcrequest(), parameterMap));
        } catch (Exception e) {
            TextToImage tti = new TextToImage();
            tti.createImage(message, data);
            log.error("error: ", e);
        }
    }
    
    private void handleRequestExceptionAsXML(Exception ex, DataWrapper data) throws IOException {
        ByteArrayOutputStream output = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
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
        DocumentType dt = di.createDocumentType("ServiceExceptionReport", null, CallWMSServlet.EXCEPTION_DTD);
        Document dom = di.createDocument(null, "ServiceExceptionReport", dt);
        Element rootElement = dom.getDocumentElement();
        rootElement.setAttribute("version", "1.1.1");
        
        Element serviceExceptionElement = dom.createElement("ServiceException");
        
        String exceptionName = ex.getClass().getName();
        String message = ex.getMessage();
        Throwable cause = ex.getCause();
        
        serviceExceptionElement.setAttribute("code", exceptionName);
        CDATASection cdata = null;
        if (cause != null) {
            cdata = dom.createCDATASection(message + " - " + cause);
        } else {
            cdata = dom.createCDATASection(message);
        }
        
        serviceExceptionElement.appendChild(cdata);
        rootElement.appendChild(serviceExceptionElement);
        
        OutputFormat format = new OutputFormat(dom);
        format.setIndenting(true);
        output = new ByteArrayOutputStream();
        XMLSerializer serializer = new XMLSerializer(output, format);
        serializer.serialize(dom);
        
        DOMValidator dv = new DOMValidator();
        try {
            dv.parseAndValidate(new ByteArrayInputStream(output.toString().getBytes(KBConfiguration.CHARSET)));
        } catch (Exception e) {
            log.error("error: ", e);
            throw new IOException("Exception occured during validation of error message: " + e);
        }
        
        data.setHeader("Content-Disposition", "inline; filename=\"ServiceException.xml\";");
        data.write(output);
    }
    
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
            String key = (String) keySetIterator.next();
            String value = ((String[]) parameters.get(key))[0];
            
            String caseInsensitiveKey = key.toUpperCase();
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
                
                String encpw = null;
                try {
                    encpw = KBCrypter.encryptText(password);
                } catch (Exception ex) {
                    log.error("error encrypting password: ", ex);
                }
                EntityManager em = MyEMFDatabase.createEntityManager();
                try {
                    EntityTransaction tx = em.getTransaction();
                    tx.begin();
                    try {
                        user = (User) em.createQuery(
                                "from User u where " +
                                "lower(u.username) = lower(:username) " +
                                "and u.password = :password").setParameter("username", username).setParameter("password", encpw).getSingleResult();
                    } catch (NoResultException nre) {
                        log.debug("No results using encrypted password");
                    } finally {
                        tx.commit();
                    }
                    
                    if (user == null) {
                        tx = em.getTransaction();
                        tx.begin();
                        try {
                            user = (User) em.createQuery(
                                    "from User u where " +
                                    "lower(u.username) = lower(:username) " +
                                    "and lower(u.password) = lower(:password)").setParameter("username", username).setParameter("password", password).getSingleResult();
                            
                            // Volgende keer dus wel encrypted
                            user.setPassword(encpw);
                            em.merge(user);
                            em.flush();
                            log.debug("Cleartext password encrypted!");
                        } catch (NoResultException nre) {
                            log.debug("No results using cleartext password");
                        } finally {
                            tx.commit();
                        }
                    }
                } finally {
                    em.close();
                }
            }
        }
        
        // probeer personal url
        if (user == null) {
            // niet ingelogd dus, dan checken op token in url
            EntityManager em = MyEMFDatabase.getEntityManager();
            try {
                String url = request.getRequestURL().toString();
                user = (User) em.createQuery(
                        "from User u where " +
                        "u.personalURL = :personalURL").setParameter("personalURL", url).getSingleResult();
            } catch (NoResultException nre) {
                throw new AccessDeniedException("Personal URL not found! Authorisation required for this service!");
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
                if (ipaddress.equalsIgnoreCase(remoteaddress) || ipaddress.equalsIgnoreCase("0.0.0.0")) {
                    validip = true;
                    break;
                }
            }
            
            if (!validip) {
                throw new AccessDeniedException("Personal URL not usuable for this IP address!");
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
        String toBeHashedString = username + password + personalDate;
        MessageDigest md = MessageDigest.getInstance(KBConfiguration.MD_ALGORITHM);
        md.update(toBeHashedString.getBytes(KBConfiguration.CHARSET));
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
        String request = data.getOgcrequest().getParameter(OGCConstants.REQUEST);
        String service = data.getOgcrequest().getParameter(OGCConstants.SERVICE);
        
        if (request == null || request.length() == 0) {
            // niet bekend, dus moet proxy zijn
            data.setRequestClassType(ProxyRequest.class);
            requestHandler = new ProxyRequestHandler();
            requestHandler.getRequest(data, user);
        }
        data.setOperation(request);
        
        if (service.equalsIgnoreCase(OGCConstants.WMS_SERVICE_WMS)) {
            if (request.equalsIgnoreCase(OGCConstants.WMS_REQUEST_GetCapabilities)) {
                data.setRequestClassType(WMSGetCapabilitiesRequest.class);
                requestHandler = new GetCapabilitiesRequestHandler();
            } else if (request.equalsIgnoreCase(OGCConstants.WMS_REQUEST_GetMap)) {
                data.setRequestClassType(WMSGetMapRequest.class);
                requestHandler = new GetMapRequestHandler();
            } else if (request.equalsIgnoreCase(OGCConstants.WMS_REQUEST_GetFeatureInfo)) {
                data.setRequestClassType(WMSGetFeatureInfoRequest.class);
                requestHandler = new GetFeatureInfoRequestHandler();
            } else if (request.equalsIgnoreCase(OGCConstants.WMS_REQUEST_GetLegendGraphic)) {
                data.setRequestClassType(WMSGetLegendGraphicRequest.class);
                requestHandler = new GetLegendGraphicRequestHandler();
            } else {
                throw new UnsupportedOperationException("Request " + request + " is not suported!");
            }
            requestHandler.getRequest(data, user);
        } else if (service.equalsIgnoreCase(OGCConstants.WFS_SERVICE_WFS)) {
            if (request.equalsIgnoreCase(OGCConstants.WFS_REQUEST_GetCapabilities)) {
                data.setRequestClassType(WFSGetCapabilitiesRequestHandler.class);
                requestHandler = new WFSGetCapabilitiesRequestHandler();
            } else if (request.equalsIgnoreCase(OGCConstants.WFS_REQUEST_DescribeFeatureType)) {
                data.setRequestClassType(WFSDescribeFeatureTypeRequestHandler.class);
                requestHandler = new WFSDescribeFeatureTypeRequestHandler();
            } else if (request.equalsIgnoreCase(OGCConstants.WFS_REQUEST_GetFeature)) {
                data.setRequestClassType(WFSGetFeatureRequestHandler.class);
                requestHandler = new WFSGetFeatureRequestHandler();
            } else if (request.equalsIgnoreCase(OGCConstants.WFS_REQUEST_Transaction)) {
                throw new UnsupportedOperationException("Request " + request + " is not suported yet!");
            } else if (request.equalsIgnoreCase(OGCConstants.WFS_REQUEST_GetFeatureWithLock)) {
                throw new UnsupportedOperationException("Request " + request + " is not suported yet!");
            } else if (request.equalsIgnoreCase(OGCConstants.WFS_REQUEST_LockFeature)) {
                throw new UnsupportedOperationException("Request " + request + " is not suported yet!");
            } else {
                throw new UnsupportedOperationException("Request " + request + " is not suported!");
            }
            requestHandler.getRequest(data, user);
        } else {
            throw new UnsupportedOperationException("Service " + service + " is not suported!");
        }
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
    
    /*public void persist(Object object) {
        try {
            em.getTransaction().begin();
            // TODO:
            // em.persist(object);    em.getTransaction().commit();
        } catch (Exception e) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", e);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }*/
}
