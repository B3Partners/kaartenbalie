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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.service.SecurityRealm;
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

public class CreatePersonalURLAction extends KaartenbalieCrudAction  {
    
    /* forward name="success" path="" */
    private static Log log = LogFactory.getLog(CreatePersonalURLAction.class);
    private final static String SUCCESS = "success";
    private String registeredIP;
    private String personalURL;
    
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
    // <editor-fold defaultstate="collapsed" desc="unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = (User) request.getUserPrincipal();
        
        ActionForward af = super.unspecified(mapping, dynaForm, request, response);
        dynaForm.set("username", user.getUsername());
        dynaForm.set("password", user.getPassword());
        
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String date = df.format(user.getTimeout());        
        
        dynaForm.set("timeout", date);
        dynaForm.set("personalURL", user.getPersonalURL());
        return af;
    }
    // </editor-fold>
    
    /**
     * Ability to enter a new password
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
    // <editor-fold defaultstate="collapsed" desc="create(ActionMapping mapping, DynaValidatorForm form, HttpServletRequest request, HttpServletResponse response) method.">
    public ActionForward create(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("changePassword", "changepwd");
        User user = (User) request.getUserPrincipal();
        ActionForward afcreate = super.create(mapping, dynaForm, request, response);
        dynaForm.set("username", user.getUsername());
        
        
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String date = df.format(user.getTimeout());        
        
        dynaForm.set("timeout", date);
        dynaForm.set("personalURL", user.getPersonalURL());
        return afcreate;
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
     * @throws Exception
     */
    // <editor-fold defaultstate="collapsed" desc="save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
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
        
        String newpassword = FormUtils.nullIfEmpty(dynaForm.getString("newpassword"));
        String newpasswordretyped = FormUtils.nullIfEmpty(dynaForm.getString("newpasswordretyped"));
        
        User user = (User)getHibernateSession().createQuery(
                "from User u where " +
                "lower(u.username) = lower(:username) " +
                "and lower(u.password) = lower(:password)")
                .setParameter("username", username)
                .setParameter("password", password)
                .uniqueResult();
        
        if(newpassword != null && newpasswordretyped != null) {
            if(!newpassword.equals(newpasswordretyped)) {
                request.setAttribute("message", "De opgegeven nieuwe wachtwoorden komen niet overeen");
                return getAlternateForward(mapping, request);
            } else {
                password = newpassword;
            }
        }
        
        if (null != user) {
            try {
                Date date = null;
                DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                try {
                    // Parse with a custom format
                    date = df.parse(timeout);
                    System.out.println(date.toString());
                } catch(ParseException e) {
                    log.error("Unable to parse : " + e);
                }
                
                String toBeHashedString = registeredIP + username + password + df.format(date);
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(toBeHashedString.getBytes("8859_1"));
                BigInteger hash = new BigInteger(1, md.digest());
                personalURL = sb.toString() + hash.toString( 16 );
                user.setPassword(password);
                user.SetRegisteredIP(registeredIP);
                user.setPersonalURL(personalURL);
                user.setTimeout(date);
                // Format with a custom format
                
            } catch (NoSuchAlgorithmException ns) {
                ns.printStackTrace();
            }
        } else {
            request.setAttribute("message", "Het huidige wachtwoord is onjuist");
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
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
        
        dynaForm.set("username", user.getUsername());
        dynaForm.set("password", user.getPassword());
        dynaForm.set("registeredIP", registeredIP);
        dynaForm.set("personalURL", personalURL);
        
        //store in db
        sess.saveOrUpdate(user);
        sess.flush(); 
        
        return super.save(mapping,dynaForm,request,response);
    }
    // </editor-fold>
}