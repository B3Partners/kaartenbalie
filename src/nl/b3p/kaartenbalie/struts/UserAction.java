/**
 * @(#)UserAction.java
 * @author R. Braam
 * @version 1.00 2006/10/02
 *
 * Purpose: a Struts action class defining all the Action for the User view.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.struts;

import java.util.HashSet;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.validator.DynaValidatorForm;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class UserAction extends KaartenbalieCrudAction {
    
    private final static String SUCCESS = "success";
    protected static final String NON_UNIQUE_USERNAME_ERROR_KEY = "error.nonuniqueusername";
    
    //-------------------------------------------------------------------------------------------------------
    // PUBLIC METHODS
    //-------------------------------------------------------------------------------------------------------
    
    /* Execute method which handles all executable requests.
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
    // <editor-fold defaultstate="" desc="unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.createLists(dynaForm, request);
        prepareMethod(dynaForm, request, LIST, LIST);
        addDefaultMessage(mapping, request);
        return mapping.findForward(SUCCESS);
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
        populateUserForm(getUser(dynaForm, request, false), dynaForm, request);
        return super.edit(mapping, dynaForm, request, response);
    }
    // </editor-fold>
    
    /* Method for saving a new or updating an existing user.
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
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        /*
         * Once we have a (new or existing) user object we can fill this object with
         * the user input.
         */
        populateUserObject(dynaForm, user, request);
        
        /*
         * All the given input can be of any kind, but the username has to be unique.
         * Therefore we need to check with the database if there is already a user with
         * the given username. If such a user exists we need to inform the user that
         * this is not allowed.
         */
        Session sess = getHibernateSession();
        User dbUser = (User)getHibernateSession().createQuery(
                "from User u where " +
                "lower(u.username) = lower(:username) ")
                .setParameter("username", user.getUsername())
                .uniqueResult();
        
        if(dbUser != null && user.getId() == null) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NON_UNIQUE_USERNAME_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        /*
         * No errors occured so we can assume that all is good and we can safely
         * save this user. Any other exception that might occur is in the form of 
         * an unknown or unsuspected form and will be thrown in the super class.
         * 
         */
        sess.saveOrUpdate(user);
        sess.flush();        
        return super.save(mapping,dynaForm,request,response);
    }
    // </editor-fold>
    
    /* Method for deleting a user.
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
    // <editor-fold defaultstate="" desc="delete(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    public ActionForward delete(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
         * No errors occured during validation and token check. Therefore we can get
         * the selected user from the database. If this user is unknown in the database
         * something has gone wrong and we need to inform the user about it.
         */                
        User user = getUser(dynaForm, request, false);
        if (user == null) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        /*
         * Otherwise we can assume that all is good and we can safely delete this user.
         * Any other exception that might occur is in the form of an unknown or unsuspected
         * form and will be thrown in the super class.
         */
        populateUserObject(dynaForm, user, request);
        Session sess = getHibernateSession();
        sess.delete(user);
        sess.flush();
        return super.delete(mapping, dynaForm, request, response);
    }
    // </editor-fold>
    
    /* Creates a list of all the users in the database.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="" desc="createLists(DynaValidatorForm form, HttpServletRequest request) method.">
    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        super.createLists(form, request);
        Session sess = getHibernateSession();
        
        List userList = sess.createQuery("from User").list();
        request.setAttribute("userlist", userList); 
        
        List organizationlist = sess.createQuery("from Organization").list();
        request.setAttribute("organizationlist", organizationlist); 
    }
    // </editor-fold>
    
    //-------------------------------------------------------------------------------------------------------
    // PRIVATE METHODS
    //-------------------------------------------------------------------------------------------------------
    
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
    
    /* Method which returns the organization with a specified id.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param id An Integer indicating which organization id has to be searched for.
     *
     * @return a Organization object.
     */
    // <editor-fold defaultstate="" desc="getOrganization(DynaValidatorForm dynaForm, HttpServletRequest request, Integer id) method.">
    private Organization getOrganization(Integer id) throws HibernateException {
        Session sess = getHibernateSession();
        return (Organization)sess.createQuery(
                    "from Organization o where " +
                    "lower(o.id) = lower(:id) ").setParameter("id", id).uniqueResult();
    }
    // </editor-fold>
    
    /* Method which will fill-in the JSP form with the data of a given user.
     *
     * @param user User object from which the information has to be printed.
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     */
    // <editor-fold defaultstate="" desc="populateUserForm(User user, DynaValidatorForm dynaForm, HttpServletRequest request) method.">
    private void populateUserForm(User user, DynaValidatorForm dynaForm, HttpServletRequest request) {
        dynaForm.set("id", user.getId().toString());
        dynaForm.set("firstname", user.getFirstName());
        dynaForm.set("lastname", user.getLastName());
        dynaForm.set("emailAddress", user.getEmailAddress());
        dynaForm.set("username", user.getUsername());
        dynaForm.set("password", user.getPassword());
        dynaForm.set("selectedOrganization", user.getOrganization().getId().toString());
        dynaForm.set("selectedRole", user.getRole());
    }
    // </editor-fold>
    
    /* Method that fills a user object with the user input from the forms.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param user User object that to be filled.
     * @param request The HTTP Request we are processing.
     */
    // <editor-fold defaultstate="" desc="populateUserObject(DynaValidatorForm dynaForm, User user, HttpServletRequest request) method.">
    private void populateUserObject(DynaValidatorForm dynaForm, User user, HttpServletRequest request) {
        user.setFirstName(FormUtils.nullIfEmpty(dynaForm.getString("firstname")));
        user.setLastName(FormUtils.nullIfEmpty(dynaForm.getString("lastname")));
        user.setEmailAddress(FormUtils.nullIfEmpty(dynaForm.getString("emailAddress")));
        user.setUsername(FormUtils.nullIfEmpty(dynaForm.getString("username")));
        user.setPassword(FormUtils.nullIfEmpty(dynaForm.getString("password")));
        user.setOrganization(this.getOrganization(FormUtils.StringToInteger(dynaForm.getString("selectedOrganization"))));
        user.setRole(dynaForm.getString("selectedRole"));
    }
    // </editor-fold>
}