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
import org.hibernate.Session;

public class UserAction extends KaartenbalieCrudAction {
    
    /* forward name="success" path="" */
    private final static String SUCCESS = "success";
    
    /** Execute method which handles all executable requests.
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
    // <editor-fold defaultstate="" desc="execute(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer id = FormUtils.StringToInteger(dynaForm.getString("id"));
        User user = this.getUser(dynaForm,request,false, id);
        
        if (null != user) {
            this.populateUserForm(user, dynaForm, request);
        }
        return super.unspecified(mapping, dynaForm, request, response);
    }
    // </editor-fold>
    
    /** Method which returns the user with a specified id.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param createNew A boolean which indicates if a new object has to be created.
     * @param id An Integer indicating which organization id has to be searched for.
     *
     * @return a User object.
     */
    // <editor-fold defaultstate="" desc="getUser(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew, Integer id) method.">
    private User getUser(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew, Integer id) {
        Session session = getHibernateSession();
        User user = null;
        
        if(null == id && createNew) {
            user = new User();
        } else if (null != id) {
            user = (User)session.load(User.class, new Integer(id));
        }
        return user;
    }
    // </editor-fold>
    
    /** Method which returns the organization with a specified id.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param id An Integer indicating which organization id has to be searched for.
     *
     * @return a Organization object.
     */
    // <editor-fold defaultstate="" desc="getOrganization(DynaValidatorForm dynaForm, HttpServletRequest request, Integer id) method.">
    private Organization getOrganization(DynaValidatorForm dynaForm, HttpServletRequest request, Integer id) {
        Session session = getHibernateSession();
        Organization org = null;
        
        
        Session sess = getHibernateSession();
        try {
            org = (Organization)sess.createQuery(
                    "from Organization o where " +
                    "lower(o.id) = lower(:id) ").setParameter("id", id).uniqueResult();
            return org;
        } catch(Exception e){
            return org;
        }
        
        
        /*
        if (null != id) {
            org = (Organization)session.load(Organization.class, new Integer(id));
         }
         */
        //return org;
    }
    // </editor-fold>
    
    /** Method which will fill the JSP form with the data of a given user.
     *
     * @param user User object from which the information has to be printed.
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     */
    // <editor-fold defaultstate="" desc="populateUserForm(User user, DynaValidatorForm dynaForm, HttpServletRequest request) method.">
    private void populateUserForm(User user, DynaValidatorForm dynaForm, HttpServletRequest request) {
        dynaForm.set("firstname", user.getFirstName());
        dynaForm.set("lastname", user.getLastName());
        dynaForm.set("emailAddress", user.getEmailAddress());
        dynaForm.set("username", user.getUsername());
        dynaForm.set("password", user.getPassword());
        dynaForm.set("selectedOrganization", user.getOrganization().getName());
        dynaForm.set("selectedRole", user.getRole());
    }
    // </editor-fold>
    
    /** Creates a list of all the users in the database.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="" desc="createLists(DynaValidatorForm form, HttpServletRequest request) method.">
    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        super.createLists(form, request);
        
        List userList = getHibernateSession().createQuery("from User").list();
        request.setAttribute("userlist", userList); 
        
        List organizationlist = getHibernateSession().createQuery("from Organization").list();
        request.setAttribute("organizationlist", organizationlist); 
    }
    // </editor-fold>
    
    /** Method that fills a user object with the user input from the forms.
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
        user.setOrganization(this.getOrganization(dynaForm, request, FormUtils.StringToInteger(dynaForm.getString("selectedOrganization"))));
        user.setRole(dynaForm.getString("selectedRole"));
    }
    // </editor-fold>
        
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
        //if invalid
        if (!isTokenValid(request)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        // nieuwe default actie op delete zetten
        Session sess = getHibernateSession();
        //validate and check for errors
        ActionErrors errors = dynaForm.validate(mapping, request);
        if(!errors.isEmpty()) {
            addMessages(request, errors);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, VALIDATION_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        Integer id = FormUtils.StringToInteger(dynaForm.getString("id"));
        User user = getUser(dynaForm,request,true, id);
        if (user==null) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        populateUserObject(dynaForm, user, request);
        
        
        User dbUser = (User)getHibernateSession().createQuery(
                "from User u where " +
                "lower(u.username) = lower(:username) ")
                .setParameter("username", user.getUsername())
                .uniqueResult();
        
        if(dbUser != null && user.getId() == null) {
            request.setAttribute("message", "De opgegeven gebruikersnaam bestaat al. Probeert u een andere naam.");
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        //store in db
        sess.saveOrUpdate(user);
        sess.flush();
        
        dynaForm.set("id", "");
        dynaForm.set("firstname", "");
        dynaForm.set("lastname", "");
        dynaForm.set("emailAddress", "");
        dynaForm.set("username", "");
        dynaForm.set("password", "");
        dynaForm.set("selectedRole", user.getRole());
        
        return super.save(mapping,dynaForm,request,response);
    }
    // </editor-fold>
    
    /** Method for deleting a user selected by a user.
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
        
        String [] userSelected = dynaForm.getStrings("userSelected");
        int size = userSelected.length;
        
        for(int i = 0; i < size; i++) {
            //if invalid
            if (!isTokenValid(request)) {
                prepareMethod(dynaForm, request, EDIT, LIST);
                addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
                return getAlternateForward(mapping, request);
            }

            // nieuwe default actie op delete zetten
            Session sess = getHibernateSession();
            //validate and check for errors
            ActionErrors errors = dynaForm.validate(mapping, request);
            if(!errors.isEmpty()) {
                addMessages(request, errors);
                prepareMethod(dynaForm, request, EDIT, LIST);
                addAlternateMessage(mapping, request, VALIDATION_ERROR_KEY);
                return getAlternateForward(mapping, request);
            }
            
            Integer id = Integer.parseInt(userSelected[i]);
            User user = getUser(dynaForm,request,true, id);
            
            if (null == user) {
                prepareMethod(dynaForm, request, LIST, EDIT);
                addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
                return getAlternateForward(mapping, request);
            }

            populateUserObject(dynaForm, user, request);
            //store in db
            sess.delete(user);
            sess.flush();
        }
        return super.delete(mapping, dynaForm, request, response);
    }
    // </editor-fold>
}