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
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.kaartenbalie.core.KBConstants;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.validator.DynaValidatorForm;
import org.hibernate.Session;

public class DemoRegistrationAction extends KaartenbalieCrudAction implements KBConstants {
    
    /* forward name="success" path="" */
    private final static String SUCCESS = "success";
    protected static final String NON_UNIQUE_USERNAME_ERROR_KEY = "error.nonuniqueusername";
    
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
        
        /*
         * All the given input can be of any kind, but the username has to be unique.
         * Therefore we need to check with the database if there is already a user with
         * the given username. If such a user exists we need to inform the user that
         * this is not allowed.
         */
        Session sess = getHibernateSession();
        User dbUser = (User)sess.createQuery(
                "from User u where " +
                "lower(u.username) = lower(:username) ")
                .setParameter("username", FormUtils.nullIfEmpty(dynaForm.getString("username")))
                .uniqueResult();
        
        if(dbUser != null && (dbUser.getId() != user.getId())) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, NON_UNIQUE_USERNAME_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        //Nu kunnen we verder...
        Organization organization = user.getOrganization();
        if (organization == null) {
            organization = new Organization();
        }
        
        populateRegistrationObject(request, dynaForm, user, organization);
                
        sess.saveOrUpdate(organization);
        user.setRole("demogebruiker");
        user.setOrganization(organization);
        sess.saveOrUpdate(user);
        sess.flush();
        
        dynaForm.set("id", user.getId().toString());
        
        ActionForward action = super.save(mapping,dynaForm,request,response);
        
        request.setAttribute("id", user.getId().toString());
        request.setAttribute("firstname", user.getFirstName());
        request.setAttribute("lastname", user.getLastName());
        request.setAttribute("emailAddress", user.getEmailAddress());
        request.setAttribute("username", user.getUsername());
        request.setAttribute("password", user.getPassword());
        request.setAttribute("personalURL", user.getPersonalURL());
        request.setAttribute("name", organization.getName());
        request.setAttribute("organizationTelephone", organization.getTelephone());
        
        return action;
    }
    // </editor-fold>
    
    /** Method that fills a serive provider object with the user input from the forms.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param serviceProvider ServiceProvider object that to be filled
     */
    // <editor-fold defaultstate="" desc="populateServerObject(DynaValidatorForm dynaForm, ServiceProvider serviceProvider) method.">
    private void populateRegistrationObject(HttpServletRequest request, DynaValidatorForm dynaForm, User user, Organization organization) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String firstname        = FormUtils.nullIfEmpty(dynaForm.getString("firstname"));
        String surname          = FormUtils.nullIfEmpty(dynaForm.getString("lastname"));
        String email            = FormUtils.nullIfEmpty(dynaForm.getString("emailAddress"));
        String username         = FormUtils.nullIfEmpty(dynaForm.getString("username"));
        String password         = FormUtils.nullIfEmpty(dynaForm.getString("password"));
        SimpleDateFormat df     = new SimpleDateFormat("yyyy/MM/dd");
        String registeredIP     = request.getRemoteAddr();
        
        /*
         * Everything seems to be ok, so it's alright to save the information
         * First we need to create a personal URL based on the information from the user
         */
        String requestServerName    = request.getServerName();
        String contextPath          = request.getContextPath();
        int port                    = request.getServerPort();
        
        String toBeHashedString = registeredIP + username + password + df.format(new Date());
        MessageDigest md = MessageDigest.getInstance(MD_ALGORITHM);
        md.update(toBeHashedString.getBytes(CHARSET));
        BigInteger hash = new BigInteger(1, md.digest());
        String personalURL = "http://" + requestServerName + ":" + port + 
                contextPath + "/" + WMS_SERVICE_WMS.toLowerCase() + "/" + hash.toString( 16 );
        
        user.setFirstName(firstname);
        user.setLastName(surname);
        user.setEmailAddress(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setPersonalURL(personalURL);
        user.setRole("demogebruiker");
        
        organization.setName(FormUtils.nullIfEmpty(dynaForm.getString("name")));
        organization.setTelephone(FormUtils.nullIfEmpty(dynaForm.getString("organizationTelephone")));
    }
    // </editor-fold>
    
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
    private Integer getID(DynaValidatorForm dynaForm) {
        return FormUtils.StringToInteger(dynaForm.getString("id"));
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
    private User getUser(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew) {
        Session session = getHibernateSession();
        User user = null;
        
        Integer id = getID(dynaForm);
        
        if(null == id && createNew) {
            user = new User();
        } else if (null != id) {
            user = (User)session.load(User.class, new Integer(id.intValue()));
        }
        return user;
    }
    // </editor-fold>
}
