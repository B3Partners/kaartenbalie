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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.service.AccessDeniedException;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import nl.b3p.ogc.utils.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

abstract public class GeneralServlet extends HttpServlet {
    protected static Log log = null;
    
    /** Processes the incoming request and calls the various methods to create the right output stream.
     *
     * @param request servlet request
     * @param response servlet response
     *
     * @throws ServletException
     * @throws IOException
     */
    abstract protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
    
    public static StringBuffer createBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = KBConfiguration.KB_SERVICES_CONTEXT_PATH;
        if (contextPath == null || contextPath.length() == 0) {
            contextPath = request.getContextPath();
        }

        StringBuffer theUrl = new StringBuffer();
        if (KBConfiguration.KB_SERVICES_SERVER_URI != null && KBConfiguration.KB_SERVICES_SERVER_URI.length() > 5) {
            theUrl.append(KBConfiguration.KB_SERVICES_SERVER_URI);
        } else {
            theUrl.append(scheme);
            theUrl.append("://");
            theUrl.append(serverName);
            if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
                theUrl.append(":");
                theUrl.append(serverPort);
            }
        }
        theUrl.append(contextPath);
        
        return theUrl;
    }    
    
    protected String extractCode(HttpServletRequest request) {
        String code = request.getPathInfo();
        if(code == null){
            return null;
        }else{
            int pos = code.indexOf("/");
            if (pos < 0) {
                return code;
            }
            code = code.substring(pos + 1);
            pos = code.indexOf("/");
            if (pos < 0) {
                return code;
            }
            code = code.substring(0, pos);
            return code;
        }
    }
    
    /** Checks if an user is allowed to make any requests.
     * Therefore there is checked if a user is logged in or if a user is using a private unique IP address.
     *
     * @param request servlet request
     *
     * @return user object
     *
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @throws AccessDeniedException
     * @throws Exception
     */
    protected User checkLogin(HttpServletRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException, AccessDeniedException, Exception {
        EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);
        
        return checkLogin(request,em);
    }
    
    protected User checkLogin(HttpServletRequest request, EntityManager em ) throws NoSuchAlgorithmException, UnsupportedEncodingException, AccessDeniedException, Exception {      
        User user = null;
        String code = extractCode(request);

        // checken of user gewoon ingelogd is
        if (user == null) {
            user = (User) request.getUserPrincipal();
            if (user != null) {
                String userCode = user.getPersonalURL();
                if (code != null && userCode != null && !code.equals(userCode)) {
                    // verkeerde user
                    user = null;
                }
            }
            if (user != null) {
                log.info("Cookie accepted for login, username: " + user.getName());
            }
        }


        if (user == null) {

            // probeer eerst personal url, checken op token in url
            try {
                //log.debug("Check code for login: " + code);
                
                log.debug("BEGIN query checkLogin");
        
                long start = System.currentTimeMillis();
                
                user = (User) em.createQuery(
                        "from User u where "
                        + "u.personalURL = :personalURL").setParameter("personalURL", code).getSingleResult();
                                
                long end = System.currentTimeMillis();
                long durUser = end - start;
        
                log.debug(durUser + "ms: END query checkLogin");
        
                em.flush();

            } catch (NonUniqueResultException nue) {
                log.error("More than one person found for this url (to be fixed in database), trying next method.");
                user = null;
            } catch (NoResultException nre) {
                log.debug("Personal url not found, trying next method.");
                user = null;
            }
 
            if (user != null) {
                java.util.Date date = user.getTimeout();
                if (date.getTime() <= (new java.util.Date().getTime())) {
                    log.debug("Personal URL key has expired, trying next method.");
                    user = null;
                }
            }

            if (user != null) {
                String remoteaddress = request.getRemoteAddr();
                String forwardedFor = request.getHeader("X-Forwarded-For");
                if(forwardedFor != null) {
                    remoteaddress = forwardedFor;
                }
                String remoteAddressDesc = remoteaddress + 
                        (forwardedFor == null ? "" : " (proxy: " + request.getRemoteAddr() + ")");
                
                boolean validip = false;
                
                /* remoteaddress controleren tegen ip adressen van user.
                 * Ip ranges mogen ook via een asterisk */
                Set ipaddresses = user.getIps();
                Iterator it = ipaddresses.iterator();
                while (it.hasNext()) {
                    String ipaddress = (String) it.next();

                    log.debug("Checking ip: " + ipaddress + " against: " + remoteAddressDesc);
                            
                    if (ipaddress.indexOf("*") != -1) {
                        if (isRemoteAddressWithinIpRange(ipaddress, remoteaddress) ) {
                            validip = true;

                            log.debug("Request within ip range for remote ip " + remoteAddressDesc +
                                    " for user " + user.getUsername() + " with code " + code);

                            break;
                        }
                    }

                    if (ipaddress.equalsIgnoreCase(remoteaddress)
                            || ipaddress.equalsIgnoreCase("0.0.0.0")
                            || ipaddress.equalsIgnoreCase("::")) {
                        validip = true;
                        break;
                    }
                }

                /* lokale verzoeken mogen ook */
                String localAddress = request.getLocalAddr();
                
                log.debug("Checking local: " + localAddress + " against: " + remoteAddressDesc);
                
                if (remoteaddress.equalsIgnoreCase(localAddress)) {
                    validip = true;

                    log.debug("Local request from ip: " + localAddress + " for user " +
                            user.getUsername() + " with code " + code);
                }

                if (!validip) {
                    log.debug("Personal URL not usuable for this IP address, trying next method");
                    user = null;
                }
            }
            if (user != null) {
                log.debug("Personal URL accepted for login, username: " + user.getName());
            }
        }

        // probeer preemptive basic login
        if (user == null) {
            // attempt to dig out authentication info only if the user has not yet been authenticated
            String authorizationHeader = request.getHeader("Authorization");
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
                try {
                    user = (User) em.createQuery(
                            "from User u where "
                            + "lower(u.username) = lower(:username) "
                            + "and u.password = :password").setParameter("username", username).setParameter("password", encpw).getSingleResult();
                    em.flush();
                } catch (NonUniqueResultException nue) {
                    log.error("More than one person found for these credentials (to be fixed in database), trying next method.");
                    user = null;
                } catch (NoResultException nre) {
                    user = null;
                    log.debug("No results using encrypted password, trying next method");
                }

                // extra check voor oude non-encrypted passwords
                if (user == null) {
                    try {
                        user = (User) em.createQuery(
                                "from User u where "
                                + "lower(u.username) = lower(:username) "
                                + "and lower(u.password) = lower(:password)").setParameter("username", username).setParameter("password", password).getSingleResult();

                        // Volgende keer dus wel encrypted
                        user.setPassword(encpw);
                        em.merge(user);
                        em.flush();
                        log.debug("Cleartext password now encrypted!");
                    } catch (NonUniqueResultException nue) {
                        log.error("More than one person found for these (cleartext) credentials (to be fixed in database), trying next method.");
                        user = null;
                    } catch (NoResultException nre) {
                        log.debug("No results using cleartext password, trying next method.");
                    }
                }
            }
            if (user != null) {
                log.info("Basic authentication accepted for login, username: " + user.getName());
            }
        }

        // hebben we nu een user?
        if (user == null) {
            throw new AccessDeniedException("Authorisation required for this service! No credentials found in Personal url, Authentication header or Cookie, Giving up! ");
        }

        return user;
    }

    /* This function should only be called when ip contains an asterisk. This
     is the case when someone has given an ip to a user with an asterisk
     eq. 10.0.0.*  */
    protected boolean isRemoteAddressWithinIpRange(String ip, String remote) {
        if (ip == null || remote == null) {
            return false;
        }

        String[] arrIp = ip.split("\\.");
        String[] arrRemote = remote.split("\\.");

        if (arrIp == null || arrIp.length < 1 || arrRemote == null || arrRemote.length < 1) {
            return false;
        }

        /* kijken of het niet asteriks gedeelte overeenkomt met
         hetzelfde gedeelte uit remote address */
        for (int i=0; i < arrIp.length; i++) {
            if (!arrIp[i].equalsIgnoreCase("*")) {
                if (!arrIp[i].equalsIgnoreCase(arrRemote[i])) {
                    return false;
                }
            }
        }

        return true;
    }
    
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
    abstract public void parseRequestAndData(DataWrapper data, User user) throws IllegalArgumentException, UnsupportedOperationException, IOException, Exception;

    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {        
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {        
        processRequest(request, response);
    }
    
        /**
     *
     * @param request
     * @return
     * @throws java.io.UnsupportedEncodingException
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     * @throws ValidationException 
     * @throws java.lang.Exception
     */
    protected OGCRequest calcOGCRequest(HttpServletRequest request) throws UnsupportedEncodingException, ParserConfigurationException, SAXException, IOException, ValidationException, Exception {
        OGCRequest ogcrequest = null;

        StringBuffer baseUrl = createBaseUrl(request);
        String iUrl = completeUrl(baseUrl, request).toString();

        if (request.getMethod().equalsIgnoreCase("GET")) {
            ogcrequest = new OGCRequest(iUrl);
            log.debug("Incoming Get URL: " + iUrl);
        } else if (request.getMethod().equalsIgnoreCase("POST")
                && request.getParameter(OGCConstants.SERVICE) != null
                && request.getParameter(OGCConstants.SERVICE).equalsIgnoreCase(OGCConstants.WMS_SERVICE_WMS)) {
            ogcrequest = new OGCRequest(iUrl);
            Enumeration params = request.getParameterNames();
            while (params.hasMoreElements()) {
                String paramName = (String) params.nextElement();
                String paramValue = request.getParameter(paramName);
                //Parameters zijn niet UTF8.
                if (paramName.equalsIgnoreCase("onload") || paramName.equalsIgnoreCase("ondata") || paramName.equalsIgnoreCase("loadmovie") || paramName.equalsIgnoreCase("oldloadmovie")) {
                    //do nothing
                } else {
                    ogcrequest.addOrReplaceParameter(paramName, paramValue);
                }
            }
            if (ogcrequest.getParameter(OGCRequest.WMS_PARAM_SLD_BODY) != null) {
                //<Name>demo_gemeenten_2006</Name>
                String sld_body = ogcrequest.getParameter(OGCRequest.WMS_PARAM_SLD_BODY);
                if (ogcrequest.getParameter(OGCRequest.WMS_PARAM_LAYERS) != null) {
                    String[] layersArray = ogcrequest.getParameter(OGCRequest.WMS_PARAM_LAYERS).split(",");
                    for (int i = 0; i < layersArray.length; i++) {
                        if (layersArray[i].indexOf("_") > -1 && layersArray[i].indexOf("_") < layersArray[i].length() - 1) {
                            String newLayer = layersArray[i].substring(layersArray[i].indexOf("_") + 1);
                            sld_body = sld_body.replaceAll("(?i)name>" + layersArray[i] + "<", "Name>" + newLayer + "<");
                        }
                    }
                }
                ogcrequest.addOrReplaceParameter(OGCRequest.WMS_PARAM_SLD_BODY, URLEncoder.encode(sld_body, "UTF-8"));
            }
            log.debug("Incoming POST converted to GET URL: " + ogcrequest.getUrlWithNonOGCparams());
        } else {
            log.debug("Incoming POST URL (content follows): " + iUrl);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setValidating(false);
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document doc = builder.parse(request.getInputStream());
            ogcrequest = new OGCRequest(doc.getDocumentElement(), baseUrl.toString());
            log.debug("Incoming POST content: \n" + OgcWfsClient.elementToString(doc.getDocumentElement()));
        }
        ogcrequest.setHttpMethod(request.getMethod());
        return ogcrequest;
    }
    
    protected StringBuffer requestUrl(StringBuffer baseUrl, HttpServletRequest request) {
        String servletPath = request.getServletPath();
        String pathInfo = request.getPathInfo();

        if (servletPath != null && servletPath.length() != 0) {
            baseUrl.append(servletPath);
        }
        if (pathInfo != null && pathInfo.length() != 0) {
            baseUrl.append(pathInfo);
        }
        return baseUrl;
    }
    
    protected StringBuffer completeUrl(StringBuffer baseUrl, HttpServletRequest request) {
        baseUrl = requestUrl(baseUrl, request);
        String queryString = request.getQueryString();

        if (queryString != null && queryString.length() != 0) {
            baseUrl.append("?");
            baseUrl.append(queryString);
        }
        return baseUrl;
    }
    
    /**
     * Parse the username out of the BASIC authorization header string.
     * @param decoded
     * @return username parsed out of decoded string
     */
    protected String parseUsername(String decoded) {
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
    protected String parsePassword(String decoded) {
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
    protected String decodeBasicAuthorizationString(String authorization) {
        if (authorization == null || !authorization.toLowerCase().startsWith("basic ")) {
            return null;
        } else {
            authorization = authorization.substring(6).trim();
            // Decode and parse the authorization credentials
            return new String(Base64.decodeBase64(authorization.getBytes()));
        }
    }
    
    /** Returns a short description of the servlet.
     */
    @Override
    abstract public String getServletInfo();
}