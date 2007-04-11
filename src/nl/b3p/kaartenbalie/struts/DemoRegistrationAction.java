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
    // <editor-fold defaultstate="" desc="unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //We select here on user id's...
        Integer id = FormUtils.StringToInteger(dynaForm.getString("id"));
        
        //First check if this user already has been added to the system.
        User user = this.getUser(dynaForm, request, false, id);
        
        //If a user is a known user we can get the organization by extracting it from the user
        if (null != user) {
            Organization organization = user.getOrganization();
            this.populateRegistrationForm(user, organization, dynaForm, request);
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
            user = (User)session.load(User.class, new Integer(id.intValue()));
        }
        return user;
    }
    // </editor-fold>
        
    /** Method which will fill the JSP form with the data of a given service provider.
     *
     * @param serviceProvider ServiceProvider object from which the information has to be printed.
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     */
    // <editor-fold defaultstate="" desc="populateOrganizationForm(ServiceProvider serviceProvider, DynaValidatorForm dynaForm, HttpServletRequest request) method.">
    private void populateRegistrationForm(User user, Organization organization, DynaValidatorForm dynaForm, HttpServletRequest request) {
        dynaForm.set("id", user.getId().toString());
        dynaForm.set("firstname", user.getFirstName());
        dynaForm.set("lastname", user.getLastName());
        dynaForm.set("emailAddress", user.getEmailAddress());
        dynaForm.set("username", user.getUsername());
        dynaForm.set("password", user.getPassword());
        
        dynaForm.set("personalURL", user.getPersonalURL());
        
        dynaForm.set("name", organization.getName());
        dynaForm.set("organizationStreet", organization.getStreet());
        dynaForm.set("organizationNumber", organization.getNumber());
        dynaForm.set("organizationAddition", organization.getAddition());
        dynaForm.set("organizationPostalcode", organization.getPostalcode());
        dynaForm.set("organizationProvince", organization.getProvince());
        dynaForm.set("organizationCountry", organization.getCountry());
        dynaForm.set("organizationPostbox", organization.getPostbox());
        dynaForm.set("organizationTelephone", organization.getTelephone());
        dynaForm.set("organizationFax", organization.getFax());
    }
    // </editor-fold>
    
    /** Method that fills a serive provider object with the user input from the forms.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param serviceProvider ServiceProvider object that to be filled
     */
    // <editor-fold defaultstate="" desc="populateServerObject(DynaValidatorForm dynaForm, ServiceProvider serviceProvider) method.">
    private void populateRegistrationObject(HttpServletRequest request, DynaValidatorForm dynaForm, User user, Organization organization) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        user.setFirstName(FormUtils.nullIfEmpty(dynaForm.getString("firstname")));
        user.setLastName(FormUtils.nullIfEmpty(dynaForm.getString("lastname")));
        user.setEmailAddress(FormUtils.nullIfEmpty(dynaForm.getString("emailAddress")));
        String username = FormUtils.nullIfEmpty(dynaForm.getString("username"));
        user.setUsername(username);
        String password = FormUtils.nullIfEmpty(dynaForm.getString("password"));        
        user.setPassword(password);
                
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        // Parse with a custom format
        String personalDate = df.format(new Date());
        String registeredIP = request.getRemoteAddr();
        
        String toBeHashedString = registeredIP + username + password + personalDate;
        
        MessageDigest md = MessageDigest.getInstance(MD_ALGORITHM);
        md.update(toBeHashedString.getBytes(CHARSET));
        BigInteger hash = new BigInteger(1, md.digest());
        
        StringBuffer sb = request.getRequestURL();
        sb.delete(sb.indexOf("demo/registration.do"), sb.capacity());
        sb.append("servlet/CallWMSServlet/");
        
        user.setPersonalURL(sb.toString() + hash.toString());
        user.setRole("demogebruiker");
        
        organization.setName(FormUtils.nullIfEmpty(dynaForm.getString("name")));
        organization.setStreet(FormUtils.nullIfEmpty(dynaForm.getString("organizationStreet")));
        organization.setNumber(FormUtils.nullIfEmpty(dynaForm.getString("organizationNumber")));
        organization.setAddition(FormUtils.nullIfEmpty(dynaForm.getString("organizationAddition")));
        organization.setPostalcode(FormUtils.nullIfEmpty(dynaForm.getString("organizationPostalcode")));
        organization.setProvince(FormUtils.nullIfEmpty(dynaForm.getString("organizationProvince")));
        organization.setCountry(FormUtils.nullIfEmpty(dynaForm.getString("organizationCountry")));
        organization.setPostbox(FormUtils.nullIfEmpty(dynaForm.getString("organizationPostbox")));
        organization.setTelephone(FormUtils.nullIfEmpty(dynaForm.getString("organizationTelephone")));
        organization.setFax(FormUtils.nullIfEmpty(dynaForm.getString("organizationFax")));
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
        
        if (null == user) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        Organization organization = user.getOrganization();
        
        if (organization == null) {
            organization = new Organization();
        }        
        
        populateRegistrationObject(request, dynaForm, user, organization);
        
        User dbUser = (User)getHibernateSession().createQuery(
                "from User u where " +
                "lower(u.username) = lower(:username) ")
                .setParameter("username", user.getUsername())
                .uniqueResult();
        
        if(dbUser != null) {
            request.setAttribute("message", "De opgegeven gebruikersnaam bestaat al. Probeert u een andere naam.");
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
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
        request.setAttribute("organizationStreet", organization.getStreet());
        request.setAttribute("organizationNumber", organization.getNumber());
        request.setAttribute("organizationAddition", organization.getAddition());
        request.setAttribute("organizationPostalcode", organization.getPostalcode());
        request.setAttribute("organizationProvince", organization.getProvince());
        request.setAttribute("organizationCountry", organization.getCountry());
        request.setAttribute("organizationPostbox", organization.getPostbox());
        request.setAttribute("organizationTelephone", organization.getTelephone());
        request.setAttribute("organizationFax", organization.getFax());
        
        return action;
    }
    // </editor-fold>
}
