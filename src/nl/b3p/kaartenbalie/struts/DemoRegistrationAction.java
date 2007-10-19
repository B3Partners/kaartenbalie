/*
 * @(#)DemoRegistrationAction.java
 * @author N. de Goeij
 * @version 1.00, 8 februari 2007
 *
 * @copyright 2007 B3Partners. All rights reserved.
 * B3Partners PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package nl.b3p.kaartenbalie.struts;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.wms.capabilities.KBConstants;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.wms.capabilities.Roles;
import nl.b3p.wms.capabilities.ServiceProvider;
import nl.b3p.kaartenbalie.core.server.User;
import org.apache.commons.codec.binary.Hex;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;
import org.securityfilter.filter.SecurityRequestWrapper;

public class DemoRegistrationAction extends UserAction implements KBConstants {
    
    /* forward name="success" path="" */
    private final static String SUCCESS = "success";
    protected static final String NON_UNIQUE_USERNAME_ERROR_KEY = "error.nonuniqueusername";
    protected static final String PREDEFINED_SP_ABBR = "demo.spabbr";
    protected static final String SAVESUCCES = "savesucceeded";
    protected static final String NONMATCHING_PASSWORDS_ERROR_KEY = "error.passwordmatch";
    
    /** Method for saving a new service provider from input of a user.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     *
     * @return an Actionforward object.
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="" desc="save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    public ActionForward save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        EntityManager em = getEntityManager();
        
        /*
         * Before we can start checking for changes or adding a new serviceprovider, we first need to check if
         * everything is valid. First there will be checked if the request is valid. This means that every JSP
         * page, when it is requested, gets a unique hash token. This token is internally saved by Struts and
         * checked when an action will be performed.
         * Each time when a JSP page is opened (requested) a new hash token is made and added to the page. Now
         * when an action is performed and Struts reads this token we can perform a check if this token is an
         * old token (the page has been requested again with a new token) or the token has already been used for
         * an action).
         * This type of check performs therefore two safety's. First of all if a user clicks more then once on a
         * button this action will perform only the first click. Second, if a user has the same page opened twice
         * only on one page a action can be performed (this is the page which is opened last). The previous page
         * isn't valid anymore.
         */
        if (!isTokenValid(request)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        /*
         * If a token is valid the second validation is necessary. This validation performs a check on the
         * given parameters supported by the user. Off course this check should already have been performed
         * by a Javascript which does exactly the same, but some browsers might not support JavaScript or
         * JavaScript can be disabled by the browser/user.
         */
        ActionErrors errors = dynaForm.validate(mapping, request);
        if(!errors.isEmpty()) {
            addMessages(request, errors);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, VALIDATION_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        /*
         * No errors occured during validation and token check. Therefore we can get a new
         * user object if a we are dealing with new input of the user, otherwise we can change
         * the user object which is already know, because of it's id.
         */
        User user = getUser(dynaForm, request, true);
        if (user == null) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        /* CHECKING FOR UNIQUE USERNAME!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         */
        try {
            User dbUser = (User)em.createQuery(
                    "from User u where " +
                    "lower(u.username) = lower(:username) ")
                    .setParameter("username", FormUtils.nullIfEmpty(dynaForm.getString("username")))
                    .getSingleResult();
            if(dbUser != null && (dbUser.getId() != user.getId())) {
                prepareMethod(dynaForm, request, EDIT, LIST);
                addAlternateMessage(mapping, request, NON_UNIQUE_USERNAME_ERROR_KEY);
                return getAlternateForward(mapping, request);
            }
        } catch (NoResultException nre) {
            //this is good!; This means that there are no other users in the DB with this username..
            //therefore nothing has to be done here.
        }
        
        /*
         * First get all the user input which need to be saved.
         */
        String password = FormUtils.nullIfEmpty(dynaForm.getString("password"));
        String repeatpassword = FormUtils.nullIfEmpty(dynaForm.getString("repeatpassword"));
        
        if(password != null && repeatpassword != null) {
            if(!password.equals(repeatpassword)) {
                prepareMethod(dynaForm, request, EDIT, LIST);
                addAlternateMessage(mapping, request, NONMATCHING_PASSWORDS_ERROR_KEY);
                return getAlternateForward(mapping, request);
            } else {
                user.setPassword(password);
            }
        }
        
        /*
         * All checks have been performed. Now we can save the user and his organization.
         */
        Organization organization = user.getOrganization();
        if (organization == null) {
            organization = new Organization();
        }
        
        Set layers = getStandardDemoLayerSet(request);
        if (layers!=null) {
            organization.setOrganizationLayer(new HashSet(layers));
            // We nemen aan dat de standard set ok is
            organization.setHasValidGetCapabilities(true);
        }
        populateRegistrationObject(request, dynaForm, user, organization);
        
        if (organization.getId() == null) {
            em.persist(organization);
        }
        if (user.getId() == null) {
            em.persist(user);
        }
        
        em.flush();
        
        /*
         * Make sure that the system will accept the user already as logged in.
         * In order to do this we need to get the SecurityRequestWrapper and set
         * this user as Principal in the requets.
         *
         * ATTENTION: Be sure that the user set as Principal has an ID, otherwise
         * the program will crash.
         */
        
        Principal principal = (Principal) user;
        if (request instanceof SecurityRequestWrapper) {
            SecurityRequestWrapper srw = (SecurityRequestWrapper)request;
            srw.setUserPrincipal(principal);
        }
        
        this.populateForm(user, dynaForm, request);
        
        //ActionForward action = super.save(mapping,dynaForm,request,response);
        return mapping.findForward(SAVESUCCES);
    }
    // </editor-fold>
    
    /* Method which will fill-in the JSP form with the data of a given user.
     *
     * @param request The HTTP Request we are processing.
     * @param session Session object for the database.
     */
    // <editor-fold defaultstate="" desc="getStandardDemoLayerSet(HttpServletRequest request, Session session) method.">
    private Set getStandardDemoLayerSet(HttpServletRequest request) {
        EntityManager em = getEntityManager();
        
        /*
         * Because every new demo user has access to the predefined server which will
         * be supported by B3Partners we first need to get this WMS server from the database
         * in order to set the rights to this new user.
         */
        MessageResources messages = getResources(request);
        Locale locale = getLocale(request);
        String spAbbr = messages.getMessage(locale, PREDEFINED_SP_ABBR);
        
        ServiceProvider stdServiceProvider = null;
        try {
            stdServiceProvider = (ServiceProvider)em.createQuery(
                    "from ServiceProvider sp where " +
                    "lower(sp.abbr) = lower(:abbr) ")
                    .setParameter("abbr", spAbbr)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
        return stdServiceProvider.getAllLayers();
    }
    // </editor-fold>
    
    /* Method which will fill-in the JSP form with the data of a given user.
     *
     * @param user User object from which the information has to be printed.
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     */
    // <editor-fold defaultstate="" desc="populateUserForm(User user, DynaValidatorForm dynaForm, HttpServletRequest request) method.">
    private void populateForm(User user, DynaValidatorForm dynaForm, HttpServletRequest request) {
        super.populateUserForm(user, dynaForm, request);
        dynaForm.set("personalURL", user.getPersonalURL());
        dynaForm.set("organizationName", user.getOrganization().getName());
//        dynaForm.set("selectedRole", user.getRole());
    }
    // </editor-fold>
    
    
    /** Method that fills a user and organization object with the user input from the forms.
     *
     * @param request The HTTP Request we are processing.
     * @param form The DynaValidatorForm bean for this request.
     * @param user User object that to be filled
     * @param organization Organization object that to be filled
     */
    // <editor-fold defaultstate="" desc="populateRegistrationObject(HttpServletRequest request, DynaValidatorForm dynaForm, User user, Organization organization) method.">
    private void populateRegistrationObject(HttpServletRequest request, DynaValidatorForm dynaForm, User user, Organization organization) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        populateUserObject(dynaForm, user, request);
        
        EntityManager em = getEntityManager();
        
        SimpleDateFormat df         = new SimpleDateFormat("yyyy/MM/dd");
        String registeredIP         = request.getRemoteAddr();
        String protocolAndVersion   = request.getProtocol();
        String requestServerName    = request.getServerName();
        String contextPath          = request.getContextPath();
        int port                    = request.getServerPort();
        String protocol             = protocolAndVersion.substring(0, protocolAndVersion.indexOf("/")).toLowerCase();
        
        
        Random rd = new Random();
        String toBeHashedString = user.getUsername() + user.getPassword() + df.format(new Date()) + rd.nextLong();
        MessageDigest md = MessageDigest.getInstance(MD_ALGORITHM);
        md.update(toBeHashedString.getBytes(CHARSET));
        byte[] md5hash = md.digest();
        
        String personalURL = protocol + "://" + requestServerName;
        if(port != 80) {
            personalURL += ":" + port;
        }
        personalURL += contextPath + "/" + WMS_SERVICE_WMS.toLowerCase() + "/" + new String(Hex.encodeHex(md5hash));
        
        user.addUserips(registeredIP);
        user.setPersonalURL(personalURL);
        
        user.setUserroles(null);
        List roles = em.createQuery("from Roles").getResultList();
        Iterator roleIt = roles.iterator();
        while (roleIt.hasNext()) {
            Roles role = (Roles) roleIt.next();
            if (role.getRole().equalsIgnoreCase("demogebruiker")) {
                user.addUserRole(role);
            }
        }
        
        organization.setName(FormUtils.nullIfEmpty(dynaForm.getString("organizationName")));
        organization.setTelephone(FormUtils.nullIfEmpty(dynaForm.getString("organizationTelephone")));
        
        user.setOrganization(organization);
        
    }
    // </editor-fold>
    
}
