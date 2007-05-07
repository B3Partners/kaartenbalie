/**
 * @(#)CreatePersonalURLAction.java
 * @author N. de Goeij
 * @version 1.00 2006/11/08
 *
 * Purpose: a class for creating a personal URL for a user when he uses a program that doesn't 
 * understand basis HTTP authentication.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.struts;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.commons.struts.ExtendedMethodProperties;
import nl.b3p.kaartenbalie.core.KBConstants;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.service.SecurityRealm;
import nl.b3p.kaartenbalie.service.WMSParamUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.validator.DynaValidatorForm;
import org.hibernate.Session;
        
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

public class CreatePersonalURLAction extends KaartenbalieCrudAction implements KBConstants  {
    
    /* forward name="success" path="" */
    private static Log log = LogFactory.getLog(CreatePersonalURLAction.class);
    private final static String SUCCESS = "success";
    private final static String GETIPADDRESS = "getIpAddress";
    
    protected static final String DATE_PARSE_ERROR_KEY = "error.dateparse";
    protected static final String DATE_INPUT_ERROR_KEY = "error.dateinput";
    protected static final String AUTHORISATION_ERROR_KEY = "error.authorization";
    protected static final String NONMATCHING_PASSWORDS_ERROR_KEY = "error.passwordmatch";
    protected static final String CAPABILITY_WARNING_KEY = "warning.saveorganization";
    
     
    private String registeredIP;
    private String personalURL;
    
    
    protected static final String UNKNOWN_SES_USER_ERROR_KEY = "error.sesuser";
    protected static final String UNKNOWN_DB_USER_ERROR_KEY = "error.dbuser";
    
    //-------------------------------------------------------------------------------------------------------
    // PUBLIC METHODS
    //-------------------------------------------------------------------------------------------------------
         
    /** Unspecified method which handles all incoming request that have no actions defined.
     *
     * @param mapping action mapping
     * @param dynaForm dyna validator form
     * @param request servlet request
     * @param response servlet response
     *
     * @return ActionForward defined by Apache foundation
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="" desc="unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return populateUserFormAndAttribute(mapping, dynaForm, request, response, LIST, LIST);
    }
    // </editor-fold>
    
    /* Edit method which handles all editable requests.
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
    // <editor-fold defaultstate="" desc="edit(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    public ActionForward edit(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return populateUserFormAndAttribute(mapping, dynaForm, request, response, EDIT, LIST);
    }
    // </editor-fold>
    
    /**
     * Ability to cancel an action
     *
     * @param mapping The mapping of the action
     * @param form The form the action is linking to
     * @param request The request of this action
     * @param response response of this action
     *
     * @return the action forward
     *
     * @throws java.lang.Exception when an error occurs
     */
    // <editor-fold defaultstate="" desc="cancelled(ActionMapping mapping, DynaValidatorForm form, HttpServletRequest request, HttpServletResponse response) method.">
    public ActionForward cancelled(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return populateUserFormAndAttribute(mapping, dynaForm, request, response, LIST, EDIT);
    }
    // </editor-fold>
    
    /** Save method which creates a unique IP address for the user and saves this address into the database.
     *
     * @param mapping action mapping
     * @param dynaForm dyna validator form
     * @param request servlet request
     * @param response servlet response
     *
     * @return ActionForward defined by Apache foundation
     *
     * @throws NoSuchAlgorithmException, ParseException, Exception
     */
    // <editor-fold defaultstate="" desc="save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    public ActionForward save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws NoSuchAlgorithmException, Exception {
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
            populateUserFormAndAttribute(mapping, dynaForm, request, response, EDIT, EDIT);
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
            populateUserFormAndAttribute(mapping, dynaForm, request, response, EDIT, EDIT);
            addMessages(request, errors);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, VALIDATION_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        User sessionUser = (User) request.getUserPrincipal();
        if(sessionUser == null) {
            populateUserFormAndAttribute(mapping, dynaForm, request, response, EDIT, EDIT);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, UNKNOWN_SES_USER_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        /*
         * First get all the user input which need to be saved.
         */
        String timeout  = FormUtils.nullIfEmpty(dynaForm.getString("timeout"));
        String registeredIP = FormUtils.nullIfEmpty(dynaForm.getString("registeredIP"));
        
        /*
         * Now lets do some checks to make sure that all the input is in the right format.
         * Check if the date is according some specified rules
         */
        Date date = null;
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        try {
            date = df.parse(timeout);
        } catch (ParseException ex) {
            populateUserFormAndAttribute(mapping, dynaForm, request, response, EDIT, EDIT);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, DATE_PARSE_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        if (date.compareTo(new java.util.Date()) < 0) {
            populateUserFormAndAttribute(mapping, dynaForm, request, response, EDIT, EDIT);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, DATE_INPUT_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        /*
         * Everything seems to be ok, so it's alright to save the information
         * First we need to create a personal URL based on the information from the user
         */
        String requestServerName    = request.getServerName();
        String contextPath          = request.getContextPath();
        int port                    = request.getServerPort();
        
        String toBeHashedString = registeredIP + sessionUser.getUsername() + sessionUser.getPassword() + df.format(date);
        MessageDigest md = MessageDigest.getInstance(MD_ALGORITHM);
        md.update(toBeHashedString.getBytes(CHARSET));
        BigInteger hash = new BigInteger(1, md.digest());
        personalURL = "http://" + requestServerName + ":" + port + 
                contextPath + "/" + WMS_SERVICE_WMS.toLowerCase() + "/" + hash.toString();
        
        /*
         * Set the new information in the userobject
         */
        sessionUser.setRegisteredIP(registeredIP);
        sessionUser.setPersonalURL(personalURL);
        sessionUser.setTimeout(date);
        
        /*
         * Check if this user gets a valid capability when using this personalURL
         */
        if(!sessionUser.getOrganization().getHasValidGetCapabilities()) {
            addAlternateMessage(mapping, request, CAPABILITY_WARNING_KEY);
        }
        
        /*
         * Save the information
         */
        Session sess = getHibernateSession();
        sess.saveOrUpdate(sessionUser);
        sess.flush();
        populateUserFormAndAttribute(mapping, dynaForm, request, response, LIST, LIST);
        return super.save(mapping, dynaForm, request, response);
    }
    // </editor-fold>
    
    /** Method which sets a the IP address of the users from his current location to the screen.
     *
     * @param mapping action mapping
     * @param dynaForm dyna validator form
     * @param request servlet request
     * @param response servlet response
     *
     * @return ActionForward defined by Apache foundation
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="" desc="getIpAdrs(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    public ActionForward getIpAddress(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String timeout  = FormUtils.nullIfEmpty(dynaForm.getString("timeout"));
        ActionForward af = populateUserFormAndAttribute(mapping, dynaForm, request, response, EDIT, EDIT);
        dynaForm.set("registeredIP", request.getRemoteAddr());
        dynaForm.set("timeout", timeout);
        return af;
    }
    // </editor-fold>
    
    //-------------------------------------------------------------------------------------------------------
    // PROTECTED METHODS
    //-------------------------------------------------------------------------------------------------------    
    
    /*
     * Which in which self specified ActionMethods can be added to a Map of ActionMethods which will be returned.
     *
     * @return Map with ActionMethods
     *
     */
    // <editor-fold defaultstate="" desc="getActionMethodPropertiesMap() method.">
    protected Map getActionMethodPropertiesMap() {
        Map map = super.getActionMethodPropertiesMap();
        ExtendedMethodProperties crudProp = new ExtendedMethodProperties(GETIPADDRESS);
        crudProp.setDefaultForwardName(SUCCESS);
        crudProp.setDefaultMessageKey("viewer.persoonlijkeurl.success");
        crudProp.setAlternateForwardName(FAILURE);
        crudProp.setAlternateMessageKey("viewer.persoonlijkeurl.failure");
        map.put(GETIPADDRESS, crudProp);               
        return map;
    }
    // </editor-fold>
    
    //-------------------------------------------------------------------------------------------------------
    // PRIVATE METHODS
    //-------------------------------------------------------------------------------------------------------    
    
    /* Method which will fill the JSP form with the data of a given service provider.
     *
     * @param serviceProvider ServiceProvider object from which the information has to be printed.
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     */
    // <editor-fold defaultstate="" desc="checkUser(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response, String def, String alt) method.">
    private ActionForward populateUserFormAndAttribute(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response, String def, String alt) throws Exception {
        User sessionUser = (User) request.getUserPrincipal();
        if(sessionUser == null) {
            prepareMethod(dynaForm, request, def, alt);
            addAlternateMessage(mapping, request, UNKNOWN_SES_USER_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        Session session = this.getHibernateSession();
        User dbUser = (User) session.get(User.class, sessionUser.getId());
        if(dbUser == null) {
            prepareMethod(dynaForm, request, def, alt);
            addAlternateMessage(mapping, request, UNKNOWN_DB_USER_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
                
        //Als er geen fout opgetreden is, moeten de gebruikers gegevens op het request en de dynaform gezet worden.
        request.setAttribute("user", sessionUser);
        populateCreatePersonalURLForm(sessionUser, dynaForm, request);
        
        prepareMethod(dynaForm, request, def, alt);
        return mapping.findForward(SUCCESS);
    }
    // </editor-fold>    
    
    /* Method which will fill the JSP form with the data of a given service provider.
     *
     * @param serviceProvider ServiceProvider object from which the information has to be printed.
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     */
    // <editor-fold defaultstate="" desc="populateOrganizationForm(ServiceProvider serviceProvider, DynaValidatorForm dynaForm, HttpServletRequest request) method.">
    private void populateCreatePersonalURLForm(User user, DynaValidatorForm dynaForm, HttpServletRequest request) {
        dynaForm.set("username", user.getUsername());
        String ipaddress = user.getRegisteredIP();
        if (ipaddress == null || ipaddress.equals("")) {
            dynaForm.set("registeredIP", request.getRemoteAddr());
        } else {
            dynaForm.set("registeredIP", user.getRegisteredIP());
        }
        
        
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        Date timeout = user.getTimeout();
        if (timeout != null) {
            String date = df.format(timeout); 
            dynaForm.set("timeout", date);
        } else {
            timeout = new Date();
            String date = df.format(timeout); 
            dynaForm.set("timeout", date);
        }
        dynaForm.set("personalURL", user.getPersonalURL());
    }
    // </editor-fold>   
}