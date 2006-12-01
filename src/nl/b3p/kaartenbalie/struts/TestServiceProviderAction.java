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
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.ContactInformation;
import nl.b3p.kaartenbalie.core.server.ServiceProvider;

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

public class TestServiceProviderAction extends KaartenbalieCrudAction {
    
    /* forward name="success" path="" */
    private final static String SUCCESS = "success";
    /*
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer id = FormUtils.StringToInteger(dynaForm.getString("id"));
        User user = this.getUser(dynaForm,request,false, id);
        
        if (null != user) {
            this.populateUserForm(user, dynaForm, request);
        }
        return super.unspecified(mapping, dynaForm, request, response);
    }
    */
    /*
     * Returns the user with a specified id.
     */
    
    private ServiceProvider getServiceProvider(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew, Integer id) {
        Session session = getHibernateSession();
        ServiceProvider serviceProvider = null;
        
        if(null == id && createNew) {
            serviceProvider = new ServiceProvider();
        } else if (null != id) {
            serviceProvider = (ServiceProvider)session.load(ServiceProvider.class, new Long(id));
        }
        return serviceProvider;
    }
    
    /*
     * If a user with a specified id is chosen this method will fill the JSP form with the dat of this user.
     */
    /*
    private void populateUserForm(User user, DynaValidatorForm dynaForm, HttpServletRequest request) {
        dynaForm.set("firstname", user.getFirstName());
        dynaForm.set("lastname", user.getLastName());
        dynaForm.set("email", user.getEmail());
        dynaForm.set("username", user.getUsername());
        dynaForm.set("password", user.getPassword());
        dynaForm.set("selectedRole", user.getRole());
    }
    
    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        super.createLists(form, request);
        
        List userList = getHibernateSession().createQuery("from User").list();
        request.setAttribute("userlist", userList);        
    }
     */
    
    private void populateServiceProviderObject(DynaValidatorForm dynaForm, ServiceProvider sp) {
        
        ContactInformation ci = new ContactInformation();
        ci.setContactPerson(FormUtils.nullIfEmpty(dynaForm.getString("contactPerson")));
        ci.setContactPosition(FormUtils.nullIfEmpty(dynaForm.getString("contactPosition")));
        ci.setAddress(FormUtils.nullIfEmpty(dynaForm.getString("address")));
        ci.setAddressType(FormUtils.nullIfEmpty(dynaForm.getString("addressType")));
        ci.setPostcode(FormUtils.nullIfEmpty(dynaForm.getString("postcode")));
        ci.setCity(FormUtils.nullIfEmpty(dynaForm.getString("city")));
        ci.setStateOrProvince(FormUtils.nullIfEmpty(dynaForm.getString("stateOrProvince")));    
        ci.setCountry(FormUtils.nullIfEmpty(dynaForm.getString("country")));
        ci.setVoiceTelephone(FormUtils.nullIfEmpty(dynaForm.getString("voiceTelephone")));
        ci.setFascimileTelephone(FormUtils.nullIfEmpty(dynaForm.getString("fascimileTelephone")));
        ci.setEmailAddress(FormUtils.nullIfEmpty(dynaForm.getString("emailAddress")));            
            
        sp.setName(FormUtils.nullIfEmpty(dynaForm.getString("name")));
        sp.setTitle(FormUtils.nullIfEmpty(dynaForm.getString("title")));
        sp.setAbstracts(FormUtils.nullIfEmpty(dynaForm.getString("abstracts")));    
        sp.setFees(FormUtils.nullIfEmpty(dynaForm.getString("fees")));        
        sp.setAccessConstraints(FormUtils.nullIfEmpty(dynaForm.getString("accessConstraints")));
        sp.setContactInformation(ci);
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
        /*
        Integer id = FormUtils.StringToInteger(dynaForm.getString("id"));
        ServiceProvider sp = getServiceProvider(dynaForm,request,true, null);
        if (sp == null) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        */
        
        ServiceProvider sp = new ServiceProvider();
        
        populateServiceProviderObject(dynaForm, sp);
        //store in db
        sess.saveOrUpdate(sp);
        sess.flush();        
        return super.save(mapping,dynaForm,request,response);
    }
    /*
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

            populateUserObject(dynaForm,user);
            //store in db
            sess.delete(user);
            sess.flush();
        }
        return super.delete(mapping, dynaForm, request, response);
    }
     **/
}