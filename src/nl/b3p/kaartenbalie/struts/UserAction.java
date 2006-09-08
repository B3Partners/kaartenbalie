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
import nl.b3p.kaartenbalie.core.User;

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

public class UserAction extends KaartenbalieCrudAction{
    
    /* forward name="success" path="" */
    private final static String SUCCESS = "success";
    
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
        User user= getUser(dynaForm,request,true);
        if (user==null) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        populateUserObject(dynaForm,user);
        /*store in db*/
        sess.saveOrUpdate(user);
        sess.flush();
        
        return super.save(mapping,dynaForm,request,response);
    }
    
    private User getUser(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew) {
        Session sess = getHibernateSession();
        Integer id = FormUtils.StringToInteger(dynaForm.getString("id"));
        User user = null;
        if(id == null && createNew)
            user = new User();
        else if (id!=null)
            user = (User)sess.get(User.class, id);
        return user;
    }
    
    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        super.createLists(form,request);
        
        List userList = getHibernateSession().createQuery("from User").list();
        request.setAttribute("userlist",userList);
        
    }
    
    private void populateUserObject(DynaValidatorForm dynaForm, User user) {
        user.setUsername(FormUtils.nullIfEmpty(dynaForm.getString("firstname")));
        user.setLastName(FormUtils.nullIfEmpty(dynaForm.getString("lastname")));
        user.setEmail(FormUtils.nullIfEmpty(dynaForm.getString("email")));
        user.setUsername(FormUtils.nullIfEmpty(dynaForm.getString("username")));
        user.setPassword(FormUtils.nullIfEmpty(dynaForm.getString("password")));
        String[] roles=dynaForm.getStrings("roles");
        if (roles!=null){
            HashSet hs= new HashSet();
            for (int i=0; i < roles.length; i++){
                hs.add(roles[i]);
            }
            user.setRoles(hs);
        }else{
            user.setRoles(new HashSet());
        }
    }


}
