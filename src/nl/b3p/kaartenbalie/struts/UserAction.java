/*
 * UserAction.java
 *
 * Created on 8 september 2006, 9:08
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
/**
 *
 * @author Roy
 * @version
 */

public class UserAction extends KaartenbalieCrudAction {
    
    /* forward name="success" path="" */
    private final static String SUCCESS = "success";
    
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer id = FormUtils.StringToInteger(dynaForm.getString("id"));
        User user = this.getUser(dynaForm,request,false, id);
        
        if (null != user) {
            this.populateUserForm(user, dynaForm, request);
        }
        return super.unspecified(mapping, dynaForm, request, response);
    }
    
    /*
     * Returns the user with a specified id.
     */
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
            System.out.println("Exception is : " + e);
            return org;
        }
        
        
        /*
        if (null != id) {
            org = (Organization)session.load(Organization.class, new Integer(id));
         }
         */
        //return org;
    }
    
    /*
     * If a user with a specified id is chosen this method will fill the JSP form with the dat of this user.
     */
    private void populateUserForm(User user, DynaValidatorForm dynaForm, HttpServletRequest request) {
        dynaForm.set("firstname", user.getFirstName());
        dynaForm.set("lastname", user.getLastName());
        dynaForm.set("emailAddress", user.getEmailAddress());
        dynaForm.set("username", user.getUsername());
        dynaForm.set("password", user.getPassword());
        dynaForm.set("selectedOrganization", user.getOrganization().getName());
        dynaForm.set("selectedRole", user.getRole());
    }
    
    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        super.createLists(form, request);
        
        List userList = getHibernateSession().createQuery("from User").list();
        request.setAttribute("userlist", userList); 
        
        List organizationlist = getHibernateSession().createQuery("from Organization").list();
        request.setAttribute("organizationlist", organizationlist); 
    }   
    
    private void populateUserObject(DynaValidatorForm dynaForm, User user, HttpServletRequest request) {
        user.setFirstName(FormUtils.nullIfEmpty(dynaForm.getString("firstname")));
        user.setLastName(FormUtils.nullIfEmpty(dynaForm.getString("lastname")));
        user.setEmailAddress(FormUtils.nullIfEmpty(dynaForm.getString("emailAddress")));
        user.setUsername(FormUtils.nullIfEmpty(dynaForm.getString("username")));
        user.setPassword(FormUtils.nullIfEmpty(dynaForm.getString("password")));
        user.setOrganization(this.getOrganization(dynaForm, request, FormUtils.StringToInteger(dynaForm.getString("selectedOrganization"))));
        user.setRole(dynaForm.getString("selectedRole"));
    }
    
    
    //This method has not been implemented yet into the system.
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
}