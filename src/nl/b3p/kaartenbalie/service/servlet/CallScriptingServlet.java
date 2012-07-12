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
 * 
 * @author Rachelle Scheijen
 */
package nl.b3p.kaartenbalie.service.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;
import javax.xml.parsers.ParserConfigurationException;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.monitoring.DataMonitoring;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.service.AccessDeniedException;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import nl.b3p.kaartenbalie.service.scriptinghandler.*;
import nl.b3p.ogc.utils.KBCrypter;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCScriptingRequest;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

public class CallScriptingServlet extends GeneralServlet {
    private HttpServletRequest httpRequest;
    

    /**
     * Initializes the servlet. Turns the logging of the servlet on.
     *
     * @param config ServletConfig config
     *
     * @throws ServletException
     */
    // <editor-fold defaultstate="" desc="init(ServletConfig config) method.">
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // Set the logger
        log = LogFactory.getLog(this.getClass());
        log.debug("Initializing Call Scripting Servlet");
    }

    /**
     * Processes the incoming request and calls the various methods to create
     * the right output stream.
     *
     * @param request servlet request
     * @param response servlet response
     *
     * @throws ServletException
     * @throws IOException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();

        DataWrapper data = new DataWrapper(request, response);

        Object identity = null;
        EntityManager em;
        EntityTransaction tx = null;

        try {
            /*
             * Check IP lock
             */
            checkRemoteIP(request);

            identity = MyEMFDatabase.createEntityManager(MyEMFDatabase.MAIN_EM);
            log.debug("Getting entity manager ......");
            em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);
            tx = em.getTransaction();
            tx.begin();

            DataMonitoring rr = new DataMonitoring();
            data.setRequestReporting(rr);

            String serviceName = OGCConstants.WMS_SERVICE_WMS;

            try {
                OGCScriptingRequest ogcrequest = calcOGCScriptingRequest(request);

                if (!ogcrequest.containsParameter(OGCScriptingRequest.COMMAND)) {
                    throw new Exception("Bad request");
                }

                data.setOgcrequest(ogcrequest);

                String serviceParam = ogcrequest.getParameter(OGCConstants.SERVICE);
                if (serviceParam != null || !"".equals(serviceParam)) {
                    serviceName = serviceParam;
                }

                String iUrl = ogcrequest.getUrl();
                rr.startClientRequest(iUrl, iUrl.getBytes().length, startTime, request.getRemoteAddr(), request.getMethod());

                User user = checkLogin(request);

                if (ogcrequest != null) {
                    ogcrequest.checkRequestURL();
                }

                rr.setUserAndOrganization(user, user.getMainOrganization());
                data.setHeader("X-Kaartenbalie-User", user.getUsername());

                this.httpRequest    = request;
                parseRequestAndData(data, user);

            } catch (AccessDeniedException adex) {
                log.error("Error while logging in: " + adex.getLocalizedMessage());
                rr.setClientRequestException(adex);
                response.sendError(401, "Unknown credentials");
            } catch (Exception ex) {
                log.error("Error while handling request: ", ex);
                rr.setClientRequestException(ex);
                response.sendError(400, "Bad Request. See API documentation");
            } finally {
                rr.endClientRequest(serviceName, data.getOperation(), data.getContentLength(), System.currentTimeMillis() - startTime);
            }
            tx.commit();
        } catch (AccessDeniedException adex) {
            log.error("Error while logging in: " + adex.getLocalizedMessage());
            response.sendError(403, adex.getLocalizedMessage());
        } catch (Exception ex) {
            log.error("Error creating EntityManager: ", ex);
            try {
                tx.rollback();
            } catch (Exception ex2) {
                log.error("Error trying to rollback: ", ex2);
            }
        } finally {
            //log.debug("Closing entity manager .....");
            MyEMFDatabase.closeEntityManager(identity, MyEMFDatabase.MAIN_EM);
        }
    }

    /**
     * Parses the incoming request
     *
     * @param data The DataWrapper
     * @param user The authenticated user
     * @throws UnsupportedOperationException if operation is not supported
     * @throws IOException
     * @throws Exception
     */
    @Override
    public void parseRequestAndData(DataWrapper data, User user) throws UnsupportedOperationException, IOException, Exception {
        OGCScriptingRequest ogcrequest = (OGCScriptingRequest) data.getOgcrequest();

        String request = ogcrequest.getParameter(OGCConstants.REQUEST);
        String command = ogcrequest.getParameter(OGCScriptingRequest.COMMAND);

        ScriptingHandler requestHandler = null;

        if (command.equalsIgnoreCase(OGCScriptingRequest.UPDATE_SERVICES) && ogcrequest.containsParameter(OGCScriptingRequest.SERVICE_TYPE) && ogcrequest.containsParameter(OGCScriptingRequest.SERVICE)) {
            UpdateHandler handler = new UpdateHandler();
            if (ogcrequest.getParameter(OGCScriptingRequest.SERVICE_TYPE).equalsIgnoreCase("WMS") ) {
                handler.setWMS();
            } else if (ogcrequest.getParameter(OGCScriptingRequest.SERVICE_TYPE).equalsIgnoreCase("WFS") ) {
                handler.setWFS();
            }
            
            requestHandler  = handler;
        }
        else if( command.equalsIgnoreCase(OGCScriptingRequest.ADD_SERVICE) && ogcrequest.containsParameter(OGCScriptingRequest.SERVICE_TYPE) && ogcrequest.containsParameter(OGCScriptingRequest.NAME) && ogcrequest.containsParameter(OGCScriptingRequest.ABBR) && ogcrequest.containsParameter(OGCScriptingRequest.URL) && ogcrequest.containsParameter(OGCScriptingRequest.SLD) && ogcrequest.containsParameter(OGCScriptingRequest.UPDATE) && ogcrequest.containsParameter(OGCScriptingRequest.GROUPS)){
            AddHandler handler = new AddHandler();
            if (ogcrequest.getParameter(OGCScriptingRequest.SERVICE_TYPE).equalsIgnoreCase("WMS") ) {
                handler.setWMS();
            } else if (ogcrequest.getParameter(OGCScriptingRequest.SERVICE_TYPE).equalsIgnoreCase("WFS") ) {
                handler.setWFS();
            }
            
            requestHandler  = handler;
        }
        else if( command.equalsIgnoreCase(OGCScriptingRequest.ADD_ALLOWED_SERVICES) && ogcrequest.containsParameter(OGCScriptingRequest.SERVICE_TYPE) && ogcrequest.containsParameter(OGCScriptingRequest.ABBR) ){
            AddAllowedHandler handler = new AddAllowedHandler();
            if (ogcrequest.getParameter(OGCScriptingRequest.SERVICE_TYPE).equalsIgnoreCase("WMS") ) {
                handler.setWMS();
            } else if (ogcrequest.getParameter(OGCScriptingRequest.SERVICE_TYPE).equalsIgnoreCase("WFS") ) {
                handler.setWFS();
            }
            
            requestHandler  = handler;
        }
        else if( command.equalsIgnoreCase(OGCScriptingRequest.DELETE_ALLOWED_SERVICES) && ogcrequest.containsParameter(OGCScriptingRequest.SERVICE_TYPE) && ogcrequest.containsParameter(OGCScriptingRequest.ABBR) ){
            DeleteAllowedHandler handler = new DeleteAllowedHandler();
            handler.setType(DeleteAllowedHandler.DELETE_SINGLE);
            if (ogcrequest.getParameter(OGCScriptingRequest.SERVICE_TYPE).equalsIgnoreCase("WMS") ) {
                handler.setWMS();
            } else if (ogcrequest.getParameter(OGCScriptingRequest.SERVICE_TYPE).equalsIgnoreCase("WFS") ) {
                handler.setWFS();
            }
            
            requestHandler  = handler;
        }
        else if( command.equalsIgnoreCase(OGCScriptingRequest.DELETE_ALL_ALLOWED_SERVICES) ){
            DeleteAllowedHandler handler = new DeleteAllowedHandler();
            handler.setType(DeleteAllowedHandler.DELETE_ALL);
            if (ogcrequest.getParameter(OGCScriptingRequest.SERVICE_TYPE).equalsIgnoreCase("WMS") ) {
                handler.setWMS();
            } else if (ogcrequest.getParameter(OGCScriptingRequest.SERVICE_TYPE).equalsIgnoreCase("WFS") ) {
                handler.setWFS();
            }
            
            requestHandler  = handler;
        }

        if (requestHandler == null) {
            throw new UnsupportedOperationException("Request " + request + " is not suported!");
        }

        data.setOperation(request);
        data.setService(command);
        requestHandler.getRequest(this.httpRequest,data, user);
    }

    /**
     * Creates a OGCScriptingRequest from the incoming request
     *
     * @param request The incoming request
     * @return OGCScriptingRequest
     * @throws java.io.UnsupportedEncodingException
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     * @throws ValidationException
     * @throws java.lang.Exception
     */
    protected OGCScriptingRequest calcOGCScriptingRequest(HttpServletRequest request) throws UnsupportedEncodingException, ParserConfigurationException, SAXException, IOException, ValidationException, Exception {
        OGCScriptingRequest ogcrequest = null;

        StringBuffer baseUrl = createBaseUrl(request);
        String iUrl = completeUrl(baseUrl, request).toString();

        if (request.getMethod().equalsIgnoreCase("GET")) {
            ogcrequest = new OGCScriptingRequest(iUrl);
            log.debug("Incoming Get URL: " + iUrl);
        }

        ogcrequest.setHttpMethod(request.getMethod());
        return ogcrequest;
    }

    /**
     * Checks the login session or credentials
     *
     * @param request The incoming request
     * @param em The entityManager
     * @return The user Principal
     * @throws AccessDeniedException if the user can not be authenticated
     */
    @Override
    protected User checkLogin(HttpServletRequest request, EntityManager em) throws AccessDeniedException {
        User user = (User) request.getUserPrincipal();

        if (user != null) {
            log.info("Cookie accepted for login, username: " + user.getName());

            return user;
        }

        // Try preemptive basic login
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
                        "from User u INNER JOIN users_roles ul ON u.id = ul.users"
                        + "where lower(u.username) = lower(:username) "
                        + "and u.password = :password"
                        + "and u.role = 1").setParameter("username", username).setParameter("password", encpw).getSingleResult();
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
                            "from User u INNER JOIN users_roles ul ON u.id = ul.users"
                            + "where lower(u.username) = lower(:username) "
                            + "and u.password = :password"
                            + "and u.role = 1").setParameter("username", username).setParameter("password", encpw).getSingleResult();

                    // Volgende keer dus wel encrypted
                    user.setPassword(encpw);
                    em.merge(user);
                    em.flush();

                    if (!user.checkRole("beheerder")) {
                        throw new NoResultException("Not a admin");
                    }
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
            return user;
        } else {
            throw new AccessDeniedException("Authorisation required for this service! No credentials found in Personal url, Authentication header or Cookie, Giving up! ");
        }
    }

    @Override
    public String getServletInfo() {
        return "CallScriptingServlet info";
    }

    /**
     * Checks the IP from the incoming request Only connections from localhost
     * are allowed
     *
     * @param request The incoming request
     * @throws AccessDeniedException if the remote IP is not localhost
     */
    private void checkRemoteIP(HttpServletRequest request) throws AccessDeniedException {
        String ip = request.getRemoteAddr();
System.err.println("IP : "+ip);
        /**
         * IPv4 localhost => 127.0.0.1 IPv6 localhost => ::1
         */
        if (!ip.equals("127.0.0.1") && !ip.equals("::1")) {
            /*
             * Only connections from localhost are allowed
             */
            throw new AccessDeniedException("Only connections from localhost are allowed");
        }
    }
}
