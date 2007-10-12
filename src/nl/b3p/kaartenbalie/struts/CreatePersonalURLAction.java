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

import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.commons.struts.ExtendedMethodProperties;
import nl.b3p.wms.capabilities.KBConstants;
import nl.b3p.kaartenbalie.core.server.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.validator.DynaValidatorForm;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Set;
import nl.b3p.kaartenbalie.core.server.Userip;
import org.apache.commons.codec.binary.Hex;
import org.securityfilter.filter.SecurityRequestWrapper;

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


    //private String registeredIP;
    //private String personalURL;


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
        return populateUserForm(mapping, dynaForm, request, response, LIST, LIST);
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
        return populateUserForm(mapping, dynaForm, request, response, EDIT, LIST);
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
        return populateUserForm(mapping, dynaForm, request, response, LIST, EDIT);
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
            populateUserForm(mapping, dynaForm, request, response, EDIT, EDIT);
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
            populateUserForm(mapping, dynaForm, request, response, EDIT, EDIT);
            addMessages(request, errors);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, VALIDATION_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }

        User user = getUser(dynaForm, request, false);
        if(user == null) {
            populateUserForm(mapping, dynaForm, request, response, EDIT, EDIT);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, UNKNOWN_SES_USER_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }

        /*
         * First get all the user input which need to be saved.
         */
        String timeout  = FormUtils.nullIfEmpty(dynaForm.getString("timeout"));
        Date date = null;
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        try {
            date = df.parse(timeout);
        } catch (ParseException ex) {
            populateUserForm(mapping, dynaForm, request, response, EDIT, EDIT);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, DATE_PARSE_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }

        if (date.compareTo(new java.util.Date()) < 0) {
            populateUserForm(mapping, dynaForm, request, response, EDIT, EDIT);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, DATE_INPUT_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }



        String [] registeredIP = dynaForm.getStrings("registeredIP");
        Set newset = new HashSet();
        int size = registeredIP.length;
        for (int i = 0; i < size; i ++) {
            Userip ipa = new Userip();
            ipa.setIpaddress(registeredIP[i]);
            ipa.setUser(user);
            newset.add(ipa);
        }
        user.setUserips(compareSets(user.getUserips(), newset));

        /*
         * Everything seems to be ok, so it's alright to save the information
         * First we need to create a personal URL based on the information from the user
         */
        String protocolAndVersion   = request.getProtocol();
        String requestServerName    = request.getServerName();
        String contextPath          = request.getContextPath();
        int port                    = request.getServerPort();
        String protocol             = protocolAndVersion.substring(0, protocolAndVersion.indexOf("/")).toLowerCase();


        String hashString = user.getPersonalURL().substring(user.getPersonalURL().lastIndexOf("/") + 1);


        Random rd = new Random();
        String toBeHashedString = user.getUsername() + user.getPassword() + df.format(date) + rd.nextLong();
        MessageDigest md = MessageDigest.getInstance(MD_ALGORITHM);
        md.update(toBeHashedString.getBytes(CHARSET));
        byte[] md5hash = md.digest();

        if (user.getTimeout() == null || date.compareTo(user.getTimeout()) != 0) {
            hashString = new String(Hex.encodeHex(md5hash));
        }

        String personalURL = protocol + "://" + requestServerName;
        if(port != 80) {
            personalURL += ":" + port;
        }
        personalURL += contextPath + "/" + WMS_SERVICE_WMS.toLowerCase() + "/" + hashString;

        /*
         * Set the new information in the userobject
         */
        user.setPersonalURL(personalURL);
        user.setTimeout(date);

        /*
         * Check if this user gets a valid capability when using this personalURL
         */
        if(!user.getOrganization().getHasValidGetCapabilities()) {
            addAlternateMessage(mapping, request, CAPABILITY_WARNING_KEY);
        }

        /*
         * Save the information
         */
        if (user.getId() == null) {
            em.persist(user);
        }
        em.flush();

        Principal principal = request.getUserPrincipal();
        if (request instanceof SecurityRequestWrapper) {
            SecurityRequestWrapper srw = (SecurityRequestWrapper)request;
            srw.setUserPrincipal(user);
        }

        populateUserForm(mapping, dynaForm, request, response, LIST, LIST);
        return super.save(mapping, dynaForm, request, response);
    }
    // </editor-fold>

    public Set compareSets(Set oldset, Set newset) {
        EntityManager em = getEntityManager();
        
        Set tempRemoveSet = new HashSet();
        Iterator it = oldset.iterator();
        while (it.hasNext()) {
            Userip userip = (Userip) it.next();
            if(!userip.inList(newset)) {
                tempRemoveSet.add(userip);
            }
        }

        Iterator removeIt = tempRemoveSet.iterator();
        while (removeIt.hasNext()) {
            Userip removableIP = (Userip) removeIt.next();
            oldset.remove(removableIP);
            em.remove(removableIP);
        }

        Iterator newit = newset.iterator();
        while (newit.hasNext()) {
            Userip userip = (Userip) newit.next();
            if(!userip.inList(oldset)) {
                if (userip.getId() == null) {
                    em.persist(userip);
                }
                oldset.add(userip);
            }
        }
        return oldset;
    }


    private Userip ipInList(Set ipaddresses, String address) {
        if (!address.equals("")) {
            Iterator it = ipaddresses.iterator();
            while (it.hasNext()) {
                Userip ipaddress = (Userip) it.next();
                if (similarAddress(ipaddress, address)) {
                    return ipaddress;
                }
            }
        }
        return null;
    }

    private boolean similarAddress(Userip ipaddress, String address) {
        return ipaddress.getIpaddress().equalsIgnoreCase(address);
    }

    /* Private method which gets the hidden id in a form.
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
    // <editor-fold defaultstate="" desc="getID(DynaValidatorForm dynaForm) method.">
    protected Integer getID(DynaValidatorForm dynaForm, HttpServletRequest request) {
        return ((User) request.getUserPrincipal()).getId();
    }
    // </editor-fold>

    /* Method which returns the user with a specified id or a new user if no id is given.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param createNew A boolean which indicates if a new object has to be created.
     * @param id An Integer indicating which organization id has to be searched for.
     *
     * @return a User object.
     */
    // <editor-fold defaultstate="" desc="getUser(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew, Integer id) method.">
    protected User getUser(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew) {

        EntityManager em = getEntityManager();

        User user = null;

        Integer id = getID(dynaForm, request);

        if(null == id && createNew) {
            user = new User();
        } else if (null != id) {
            user = (User)em.find(User.class, new Integer(id.intValue()));
        }
        return user;
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
    private ActionForward populateUserForm(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response, String def, String alt) throws Exception {
        User user = (User) request.getUserPrincipal();
        if(user == null) {
            prepareMethod(dynaForm, request, def, alt);
            addAlternateMessage(mapping, request, UNKNOWN_SES_USER_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }

        dynaForm.set("username", user.getUsername());
        dynaForm.set("firstname", user.getFirstName());
        dynaForm.set("surname", user.getSurname());
        dynaForm.set("emailaddress", user.getEmailAddress());
        dynaForm.set("role", user.getRolesAsString());
        dynaForm.set("personalURL", user.getPersonalURL());

        dynaForm.set("currentaddress", request.getRemoteAddr());

        List iplist = new ArrayList(user.getUserips());
        request.setAttribute("iplist", iplist);

        String [] registeredIP = new String[iplist.size()];
        for (int i = 0; i < iplist.size(); i++) {
            Userip ipaddresses = (Userip)iplist.get(i);
            registeredIP[i] = ipaddresses.getId().toString();
        }
        dynaForm.set("registeredIP", registeredIP);

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

        prepareMethod(dynaForm, request, def, alt);
        return mapping.findForward(SUCCESS);
    }
    // </editor-fold>
}
