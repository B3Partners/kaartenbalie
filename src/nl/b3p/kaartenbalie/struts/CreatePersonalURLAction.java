/*
 * CreatePersonalURLAction.java
 *
 * Created on 8 november 2006, 14:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.struts;

import java.util.HashSet;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.service.SecurityRealm;

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
/**
 *
 * @author Nando De Goeij
 */
public class CreatePersonalURLAction extends KaartenbalieCrudAction  {
    
    /* forward name="success" path="" */
    private final static String SUCCESS = "success";
    private String registeredIP;
    private String personalURL;
    
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return super.unspecified(mapping, dynaForm, request, response);
    }
    
    //This method has not been implemented yet into the system.
    public ActionForward save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //Bij deze save actie moet er een unieke 'sleutel' gemaakt worden om mee in te kunnen loggen
        //Deze sleutel moet gebaseerd worden op drie gegevens, namelijk:
        //- de username
        //- het password
        //- het ip adres van de gebruiker
        //maak gebruik van request.getRemoteAddr();
        registeredIP = request.getRemoteAddr();
        StringBuffer sb = request.getRequestURL();
        //rip off the last two slashes of the URL and fill it up with the right URL
        sb.delete(sb.indexOf("viewer/createPersonalURL.do"), sb.capacity());
        sb.append("servlet/CallWMSServlet/");
        
        String username = FormUtils.nullIfEmpty(dynaForm.getString("username"));
        String password = FormUtils.nullIfEmpty(dynaForm.getString("password"));
        String timeout  = FormUtils.nullIfEmpty(dynaForm.getString("timeout"));
        
        User user = (User)getHibernateSession().createQuery(
                "from User u where " +
                "lower(u.username) = lower(:username) " +
                "and lower(u.password) = lower(:password)")
                .setParameter("username", username)
                .setParameter("password", password)
                .uniqueResult();
        
        if (null != user) {
            try {
                String toBeHashedString = registeredIP + username + password;
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(toBeHashedString.getBytes("8859_1"));
                BigInteger hash = new BigInteger(1, md.digest());
                personalURL = sb.toString() + hash.toString( 16 );
                user.SetRegisteredIP(registeredIP);
                user.setPersonalURL(personalURL);
                user.setTimeout(timeout);
                System.out.println(personalURL);
            } catch (NoSuchAlgorithmException ns) {
                ns.printStackTrace();
            }
        }
        
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
        
        if (null == user) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        dynaForm.set("username", user.getUsername());
        dynaForm.set("password", user.getPassword());
        dynaForm.set("registeredIP", registeredIP);
        dynaForm.set("personalURL", personalURL);
        
        //store in db
        sess.saveOrUpdate(user);
        sess.flush(); 
        
        return super.save(mapping,dynaForm,request,response);
    }
}