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
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.service.AccessDeniedException;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import nl.b3p.kaartenbalie.util.LDAPUtil;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.KBCrypter;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.ogc.utils.OgcWfsClient;
import nl.b3p.wms.capabilities.Roles;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

abstract public class GeneralServlet extends HttpServlet {

    protected static Log log = LogFactory.getLog(GeneralServlet.class);

    private static Boolean ldapUseLdap;
    private static String ldapDefaultGroup;
    private static String ldapHost;
    private static Integer ldapPort;
    private static String ldapUserSuffix;

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
    abstract protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    public static StringBuffer createBaseUrl(HttpServletRequest request) {
        return createBaseUrl(request, false);
    }

    public static StringBuffer createBaseUrl(HttpServletRequest request, boolean useInternal) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath;
        if (useInternal) {
            contextPath = KBConfiguration.KB_SERVICES_INTERNAL_CONTEXT_PATH;
        } else {
            contextPath = KBConfiguration.KB_SERVICES_CONTEXT_PATH;
        }
        if (contextPath == null || contextPath.length() == 0) {
            contextPath = request.getContextPath();
        }

        StringBuffer theUrl = new StringBuffer();
        String serverURI;
        if (useInternal) {
            serverURI = KBConfiguration.KB_SERVICES_INTERNAL_SERVER_URI;
        } else {
            serverURI = KBConfiguration.KB_SERVICES_SERVER_URI;
        }

        if (serverURI != null && serverURI.length() > 5) {
            theUrl.append(serverURI);
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

    /**
     * Checks if an user is allowed to make any requests. Therefore there is
     * checked if a user is logged in or if a user is using a private unique IP
     * address.
     *
     * @param request servlet request
     * @param pcode personal code
     *
     * @return user object
     *
     * @throws AccessDeniedException
     */
    protected User checkLogin(HttpServletRequest request, String pcode) 
            throws AccessDeniedException  {

        return checkLogin4All(request, pcode);
    }
    protected User checkLogin(HttpServletRequest request, EntityManager em, String pcode) 
            throws AccessDeniedException {
        return checkLogin4All(request, em, pcode);
    }

    public static User checkLogin4All(HttpServletRequest request, String pcode)
            throws AccessDeniedException {
        User user = null;
        Object identity = null;
        EntityTransaction tx = null;

        try {
            identity = MyEMFDatabase.createEntityManager(MyEMFDatabase.MAIN_EM);
            EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);
            tx = em.getTransaction();

            tx.begin();

            user = checkLogin4All(request, em, pcode);

            tx.commit();
        } catch (AccessDeniedException ade) {
            throw ade;
            
        } catch (Exception ex) {
            if (tx != null) {
                tx.rollback();
            }

            return null;
        } finally {
            MyEMFDatabase.closeEntityManager(identity, MyEMFDatabase.MAIN_EM);
        }

        return user;
    }

    public static User checkLogin4All(HttpServletRequest request, EntityManager em,
            String pcode) throws AccessDeniedException  {
        User user = checkLoginAlreadyLoggedIn(request, em, pcode);
        
        if (user == null) {
            user = checkLoginPersonalCode(request, em, pcode);
        }
        if (user == null) {
            user = checkLoginPreemptiveAuthentication(request, em);
        }
        if (user == null) {
            user = checkLoginLDAP(request, em);
        }

        /* Nog steeds geen user ? */
        if (user == null) {
            throw new AccessDeniedException("Inlog vereist voor deze service. Geen geldige inlog gevonden in url, preemptive header, cookie of ldap. Try Basic Authentication challenge");
        }

        /* Controleer ip adressen */
        boolean isValidIp = checkValidIpAddress(request, user);
        if (!isValidIp) {
            String remoteAddress = request.getRemoteAddr();
            log.debug("Ip adres " + remoteAddress + " ongeldig"
                    + " voor gebruiker " + user.getName());
            setDetachedUserLastLoginStatus(user, User.LOGIN_STATE_INVALID_IP, em);
            return null;
        }
        /* Controleer time out */
        boolean expired = checkUserTimeExpired(em, user);
        if (expired) {
            setDetachedUserLastLoginStatus(user, User.LOGIN_STATE_EXPIRED, em);
            log.debug("Account van " + user.getUsername() + " is verlopen.");
            return null;
        }

        /* Er is een user. loginstatus aanpassen */
        setDetachedUserLastLoginStatus(user, null, em);
        log.debug("Gebruiker " + user.getName() + " mag inloggen.");

        return user;        
    }

    protected static User checkLoginAlreadyLoggedIn(HttpServletRequest request, EntityManager em,
            String pcode) {

        /* Gebruiker al ingelogd en persoonlijke code hetzelfde ? */
        User user = (User) request.getUserPrincipal();

        if (user != null) {
            String userCode = user.getPersonalURL();
            if (pcode != null && userCode != null && !pcode.equals(userCode)) {
                user = null;
            }
        }

        if (user != null) {
            log.debug("Gebruiker " + user.getName() + " al ingelogd via cookie.");
        }

        return user;
    }

    protected static User checkLoginPersonalCode(HttpServletRequest request, EntityManager em,
            String pcode) {

        /* Zoek gebruiker via persoonlijke code */
        User user = null;
        if (pcode != null) {
            try {
                user = (User) em.createQuery(
                        "from User u where u.personalURL = :personalURL")
                        .setParameter("personalURL", pcode)
                        .getSingleResult();
            } catch (NonUniqueResultException nue) {
                log.debug("Meerdere gebruikers gevonden bij persoonlijke code: " + pcode);
                user = null;
            } catch (NoResultException nre) {
                log.debug("Persoonlijke code niet gevonden.");
                user = null;
            }

            if (user != null) {
                log.debug("Persoonlijke code gevonden bij gebruiker: " + user.getName());
            }
        }
        return user;
    }

    protected static User checkLoginPreemptiveAuthentication(HttpServletRequest request, EntityManager em) {

        /* Zoek gebruiker via Preemptive authentication */
        User user = null;
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            String decoded = decodeBasicAuthorizationString(authorizationHeader);
            String username = parseUsername(decoded);
            String password = parsePassword(decoded);

            String encpw = null;
            try {
                encpw = KBCrypter.encryptText(password);
            } catch (Exception ex) {
                log.error("Fout tijdens encrypten wachtwoord: ", ex);
            }
            try {
                user = (User) em.createQuery(
                        "from User u where "
                        + "lower(u.username) = lower(:username) "
                        + "and u.password = :password")
                        .setParameter("username", username)
                        .setParameter("password", encpw)
                        .getSingleResult();
            } catch (NonUniqueResultException nue) {
                log.error("Meerdere gebruikers gevonden via encrypted wachtwoord.");
                user = null;
            } catch (NoResultException nre) {
                log.debug("Geen gebruiker gevonden via encrypted wachtwoord.");
                user = null;
            }

            // extra check voor oude non-encrypted passwords
            if (user == null) {
                try {
                    user = (User) em.createQuery(
                            "from User u where "
                            + "lower(u.username) = lower(:username) "
                            + "and lower(u.password) = lower(:password)")
                            .setParameter("username", username)
                            .setParameter("password", password)
                            .getSingleResult();

                    // Volgende keer dus wel encrypted
                    user.setPassword(encpw);
                    em.merge(user);
                    em.flush();
                } catch (NonUniqueResultException nue) {
                    log.error("Meerdere gebruikers gevonden via plain wachtwoord.");
                    user = null;
                } catch (NoResultException nre) {
                    log.debug("Geen gebruiker gevonden via plain wachtwoord.");
                    user = null;
                }
            }

            /* Controleer ingevuld wachtwoord met db gebruiker wachtwoord */
            if (user == null) {
                try {
                    user = (User) em.createQuery(
                            "from User u where "
                            + "lower(u.username) = lower(:username) ")
                            .setParameter("username", username)
                            .getSingleResult();
                } catch (NoResultException nre) {
                    log.debug("Gebruiker " + username + " niet gevonden in db.");
                    user = null;
                }

                if (user != null && !user.getPassword().equals(encpw)) {
                    user.setLastLoginStatus(User.LOGIN_STATE_WRONG_PASSW);

                    em.merge(user);
                    em.flush();

                    log.debug("Wachtwoord voor gebruiker " + username + " verkeerd.");
                    user = null;
                }
            }
        }

        if (user != null) {
            log.debug("Basic authentication gelukt voor gebruiker: " + user.getName());
        }
        return user;
    }

    protected static User checkLoginLDAP(HttpServletRequest request, EntityManager em) {
        /* Probeer LDAP bind, ldapUseLdap is param in web.xml */
        User user = null;
        String authorizationHeader = request.getHeader("Authorization");
        if (ldapUseLdap != null && ldapUseLdap
                && authorizationHeader != null) {

            LDAPUtil ldapUtil = new LDAPUtil();

            String decoded = decodeBasicAuthorizationString(authorizationHeader);
            String username = parseUsername(decoded);
            String password = parsePassword(decoded);

            // check if user is in ldap
            boolean inLdap = ldapUtil.userInLdap(ldapHost, ldapPort, username,
                    password, ldapUserSuffix);

            // check if username already in kaartenbalie database
            user = ldapUtil.getUserByName(em, username);

            /* case 1: wel in ldap, niet in db, return nieuw gebruiker */
            if (inLdap && user == null) {
                user = new User();
                user.setUsername(username);
                user.setPassword(User.createCode());

                List<Roles> gebruikerRol = ldapUtil.getGebruikerRol(em);
                user.getRoles().retainAll(gebruikerRol);
                user.getRoles().addAll(gebruikerRol);

                Set ips = new HashSet(1);
                ips.add("0.0.0.0");
                user.setIps(ips);

                String personalUrl = User.createCode();
                user.setPersonalURL(personalUrl);

                if (ldapDefaultGroup != null && !ldapDefaultGroup.isEmpty()) {
                    Organization org = ldapUtil.getLDAPOrg(em, ldapDefaultGroup);
                    user.setMainOrganization(org);
                }

                Date userTimeOut = ldapUtil.getDefaultTimeOut(36);
                user.setTimeout(userTimeOut);

                user.setLastLoginStatus(null);

                em.persist(user);
                em.flush();

                log.debug("Gebruiker " + username + " in Ldap maar nog niet in db.");

                return user;
            }

            /* case 2: wel in ldap, wel in db, return user */
            if (inLdap && user != null) {
                user.setLastLoginStatus(null);
                em.flush();

                log.debug("Gebruiker " + username + " in Ldap en al in db.");

                return user;
            }

            /* case 3: niet in ldap, wel in db, uitroepteken zetten */
            if (!inLdap && user != null) {
                user.setLastLoginStatus(User.LOGIN_STATE_WRONG_PASSW);
                em.flush();

                log.debug("Gebruiker " + username + " niet in Ldap maar wel in db.");

                return null;
            }

            /* case 4: niet in ldap, niet in db, niets doen */
            log.debug("Gebruiker " + username + " niet in Ldap en niet in db.");
        }
        return user;
    }

    private static void setDetachedUserLastLoginStatus(User user, String status,
            EntityManager em) {

        user.setLastLoginStatus(status);

        em.createQuery("update User set lastLoginStatus = :status where id = :id")
                .setParameter("status", status)
                .setParameter("id", user.getId())
                .executeUpdate();
    }

    private static boolean checkValidIpAddress(HttpServletRequest request, User user) {

        /* ip adressen van user die bij pcode hoort worden gechecked
         dit hoeven dus niet perse de ip adressen te zijn van de user
         waarmee nu wordt ingelogd, bijvoorbeeld via ldap of andere username
         dan die aan gebruikerscode is gekoppeld */
        boolean validip = false;

        if (user != null) {
            String remoteaddress = request.getRemoteAddr();
            String forwardedFor = request.getHeader("X-Forwarded-For");
            if (forwardedFor != null) {
                remoteaddress = forwardedFor;
            }
            String remoteAddressDesc = remoteaddress
                    + (forwardedFor == null ? "" : " (proxy: " + request.getRemoteAddr() + ")");

            validip = false;

            /* remoteaddress controleren tegen ip adressen van user.
             * Ip ranges mogen ook via een asterisk */
            Set ipaddresses = user.getIps();
            Iterator it = ipaddresses.iterator();
            while (it.hasNext()) {
                String ipaddress = (String) it.next();

                log.debug("Controleren ip: " + ipaddress + " tegen: " + remoteAddressDesc);

                if (ipaddress.indexOf("*") != -1) {
                    if (isRemoteAddressWithinIpRange(ipaddress, remoteaddress)) {
                        validip = true;

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

            log.debug("Controleren lokaal ip: " + localAddress + " tegen: " + remoteAddressDesc);

            if (remoteaddress.equalsIgnoreCase(localAddress)) {
                validip = true;
            }
        }

        return validip;
    }

    private static boolean checkUserTimeExpired(EntityManager em, User user) {
        if (user != null) {
            java.util.Date date = user.getTimeout();

            if (date.getTime() <= (new java.util.Date().getTime())) {
                return true;
            }
        }

        return false;
    }

    /* This function should only be called when ip contains an asterisk. This
     is the case when someone has given an ip to a user with an asterisk
     eq. 10.0.0.*  */
    protected static boolean isRemoteAddressWithinIpRange(String ip, String remote) {
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
        for (int i = 0; i < arrIp.length; i++) {
            if (!arrIp[i].equalsIgnoreCase("*")) {
                if (!arrIp[i].equalsIgnoreCase(arrRemote[i])) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Parses any incoming request and redirects this request to the right
     * handler.
     *
     * @param data map with the given parameters
     * @param user database user
     *
     * @throws IllegalArgumentException
     * @throws UnsupportedOperationException
     * @throws IOException
     */
    abstract public void parseRequestAndData(DataWrapper data, User user) throws IllegalArgumentException, UnsupportedOperationException, IOException, Exception;

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
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
        OGCRequest ogcrequest;

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
                if (paramName.equalsIgnoreCase("onload") 
                        || paramName.equalsIgnoreCase("ondata") 
                        || paramName.equalsIgnoreCase("loadmovie") 
                        || paramName.equalsIgnoreCase("oldloadmovie")) {
                    //do nothing
                } else {
                    ogcrequest.addOrReplaceParameter(paramName, paramValue);
                }
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
     *
     * @param decoded
     * @return username parsed out of decoded string
     */
    protected static String parseUsername(String decoded) {
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
     *
     * @param decoded
     * @return password parsed out of decoded string
     */
    protected static String parsePassword(String decoded) {
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
    protected static String decodeBasicAuthorizationString(String authorization) {
        if (authorization == null || !authorization.toLowerCase().startsWith("basic ")) {
            return null;
        } else {
            authorization = authorization.substring(6).trim();
            // Decode and parse the authorization credentials
            return new String(Base64.decodeBase64(authorization.getBytes()));
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        log = LogFactory.getLog(this.getClass());
        log.debug("Initializing GeneralServlet");

        ServletContext context = config.getServletContext();

        if (context.getInitParameter("ldapUseLdap") != null) {
            ldapUseLdap = new Boolean(context.getInitParameter("ldapUseLdap"));
        }
        if (context.getInitParameter("ldapDefaultGroup") != null) {
            ldapDefaultGroup = context.getInitParameter("ldapDefaultGroup");
        }
        if (context.getInitParameter("ldapHost") != null) {
            ldapHost = context.getInitParameter("ldapHost");
        }
        if (context.getInitParameter("ldapPort") != null) {
            ldapPort = new Integer(context.getInitParameter("ldapPort"));
        }
        if (context.getInitParameter("ldapUserSuffix") != null) {
            ldapUserSuffix = context.getInitParameter("ldapUserSuffix");
        }
    }

    /**
     * Returns a short description of the
     *
     * @return String servlet info.
     */
    @Override
    abstract public String getServletInfo();
}
