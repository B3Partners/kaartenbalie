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
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.kaartenbalie.core.server.IPAddresses;
import nl.b3p.wms.capabilities.KBConstants;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.wms.capabilities.Roles;
import nl.b3p.wms.capabilities.ServiceProvider;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.service.LayerValidator;
import nl.b3p.kaartenbalie.service.ServiceProviderValidator;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;
import org.hibernate.Session;
import org.securityfilter.filter.SecurityRequestWrapper;

public class DemoRegistrationAction extends UserAction implements KBConstants {
    
    /* forward name="success" path="" */
    private final static String SUCCESS = "success";
    protected static final String NON_UNIQUE_USERNAME_ERROR_KEY = "error.nonuniqueusername";
    protected static final String PREDEFINED_SERVER = "demo.serverurl";
    protected static final String PREDEFINED_SERVER_NAME = "demo.servername";
    protected static final String SAVESUCCES = "savesucceeded";
    protected static final String NONMATCHING_PASSWORDS_ERROR_KEY = "error.passwordmatch";
    
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
        
        /* CHECKING FOR UNIQUE USERNAME!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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
        
        /*
         * First get all the user input which need to be saved.
         */
        String password = FormUtils.nullIfEmpty(dynaForm.getString("password"));
        String repeatpassword = FormUtils.nullIfEmpty(dynaForm.getString("repeatpassword"));
        
        if(password != null && repeatpassword != null) {
            if(!password.equals(repeatpassword)) {
                prepareMethod(dynaForm, request, EDIT, LIST);
                addAlternateMessage(mapping, request, NONMATCHING_PASSWORDS_ERROR_KEY);
                return getAlternateForward(mapping, request);
            } else {
                user.setPassword(password);
            }
        }
        
        /*
         * All checks have been performed. Now we can save the user and his organization.
         */
        Organization organization = user.getOrganization();
        if (organization == null) {
            organization = new Organization();
        }
        organization.setOrganizationLayer(getLayerSet(request, sess));
        populateRegistrationObject(request, dynaForm, user, organization);
        sess.saveOrUpdate(organization);
        user.setOrganization(organization);
        sess.saveOrUpdate(user);
        sess.flush();
        
        /*
         * Make sure that the system will accept the user already as logged in.
         * In order to do this we need to get the SecurityRequestWrapper and set
         * this user as Principal in the requets.
         *
         * ATTENTION: Be sure that the user set as Principal has an ID, otherwise 
         * the program will crash.
         */
        Principal principal = (Principal) user;
        if (request instanceof SecurityRequestWrapper) {
            SecurityRequestWrapper srw = (SecurityRequestWrapper)request;
            srw.setUserPrincipal(principal);
        }
        
        this.populateForm(user, dynaForm, request);        
        ActionForward action = super.save(mapping,dynaForm,request,response);        
        return mapping.findForward(SAVESUCCES);
    }
    // </editor-fold>
    
    /* Method which will fill-in the JSP form with the data of a given user.
     *
     * @param request The HTTP Request we are processing.
     * @param session Session object for the database.
     */
    // <editor-fold defaultstate="" desc="getLayerSet(HttpServletRequest request, Session session) method.">
    private Set getLayerSet(HttpServletRequest request, Session session) {
        /*
         * Because every new demo user has access to the predefined server which will
         * be supported by B3Partners we first need to get this WMS server from the database
         * in order to set the rights to this new user.
         */
        MessageResources messages = getResources(request);
        Locale locale = getLocale(request);
        String serverName = messages.getMessage(locale, PREDEFINED_SERVER_NAME);
        String serverUrl  = messages.getMessage(locale, PREDEFINED_SERVER);
        
        ServiceProvider stdServiceProvider = (ServiceProvider)session.createQuery(
                "from ServiceProvider sp where " +
                "lower(sp.givenName) = lower(:givenName) " +
                "and lower(sp.url) = lower(:url)")
                .setParameter("givenName", serverName)
                .setParameter("url", serverUrl)
                .uniqueResult();
        
        /*
         * Get all layers supported by this WMS server.
         */
         return stdServiceProvider.getAllLayers();
    }
    // </editor-fold>
    
    /* Method which will fill-in the JSP form with the data of a given user.
     *
     * @param user User object from which the information has to be printed.
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     */
    // <editor-fold defaultstate="" desc="populateUserForm(User user, DynaValidatorForm dynaForm, HttpServletRequest request) method.">
    private void populateForm(User user, DynaValidatorForm dynaForm, HttpServletRequest request) {
        super.populateUserForm(user, dynaForm, request);
        dynaForm.set("personalURL", user.getPersonalURL());
        dynaForm.set("organizationName", user.getOrganization().getName());
//        dynaForm.set("selectedRole", user.getRole());
    }
    // </editor-fold>
    
    
    /** Method that fills a user and organization object with the user input from the forms.
     *
     * @param request The HTTP Request we are processing.
     * @param form The DynaValidatorForm bean for this request.
     * @param user User object that to be filled
     * @param organization Organization object that to be filled
     */
    // <editor-fold defaultstate="" desc="populateRegistrationObject(HttpServletRequest request, DynaValidatorForm dynaForm, User user, Organization organization) method.">
    private void populateRegistrationObject(HttpServletRequest request, DynaValidatorForm dynaForm, User user, Organization organization) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        populateUserObject(dynaForm, user, request);
        
        SimpleDateFormat df         = new SimpleDateFormat("yyyy/MM/dd");
        String registeredIP         = request.getRemoteAddr();
        String protocolAndVersion   = request.getProtocol();
        String requestServerName    = request.getServerName();
        String contextPath          = request.getContextPath();
        int port                    = request.getServerPort();
        String protocol             = protocolAndVersion.substring(0, protocolAndVersion.indexOf("/")).toLowerCase();
        
        String toBeHashedString = registeredIP + user.getUsername() + user.getPassword() + df.format(new Date());
        MessageDigest md = MessageDigest.getInstance(MD_ALGORITHM);
        md.update(toBeHashedString.getBytes(CHARSET));
        BigInteger hash = new BigInteger(1, md.digest());
        
        String personalURL = protocol + "://" + requestServerName;
        if(port != 80) {
            personalURL += ":" + port;
        }
        personalURL += contextPath + "/" + WMS_SERVICE_WMS.toLowerCase() + "/" + hash.toString( 16 );
        
        IPAddresses ipa = new IPAddresses();
        ipa.setIpaddress(registeredIP);
        user.addIpaddresses(ipa);
        user.setPersonalURL(personalURL);
        
        List roles = getHibernateSession().createQuery("from Roles").list();
        Iterator roleIt = roles.iterator();
        while (roleIt.hasNext()) {
            Roles role = (Roles) roleIt.next();
            if (role.getRole().equalsIgnoreCase("demogebruiker"))
                user.addUserRole(role);
        }
        
        organization.setName(FormUtils.nullIfEmpty(dynaForm.getString("organizationName")));
        organization.setTelephone(FormUtils.nullIfEmpty(dynaForm.getString("organizationTelephone")));
        
        
        List layerList = getHibernateSession().createQuery(
                "from Layer l left join fetch l.attribution").list();
        
        /* If a user selects layers from the treeview. He/she selects only sublayers. Because the parent
         * layers are not automaticaly selected too, we need to do this ourselfs. Therefore there must be
         * checked if a layer has any parents and if so this has to be checked recursively until there
         * aren't any parents anymore. Each of the parents found have to be added to the list of layers 
         * which are allowed to be requested.
         */
        //String [] selectedLayer;
        Set selectedLayers = organization.getOrganizationLayer();
        Iterator itselected = selectedLayers.iterator();
        //int size = selectedLayers.length;
        Set layers = new HashSet();
        Set serviceProviders = new HashSet();
        while(itselected.hasNext()) {
        //for(int i = 0; i < size; i++) {
            int select = ((Layer)itselected.next()).getId().intValue();
            //int select = Integer.parseInt(selectedLayers[i].substring(0, selectedLayers[i].indexOf("_")));
            Iterator it = layerList.iterator();
            while (it.hasNext()) {
                Layer layer = (Layer)it.next();
                if (layer.getId().intValue() == select) {
                    //layers.add(layer);
                    layers = getAllParentLayers(layer,  layers );
                    serviceProviders.add(layer.getTopLayer().getServiceProvider());
                    break;
                }
            }
        }
        
        /* There is a possibility that some serviceproviders do not support the same SRS's or image formats.
         * Some might have compatibility some others not. To make sure this wont give any problems, we need to
         * check which formats and srs's are the same. If and only if this complies we can say for sure that
         * the GetCapabilities request which is going to be sent to the client is valid. In all other cases
         * we need to give a warning that the GetCapabilities can have problems when used with certain viewers.
         * 
         * In order to give the user the same warning as the supervisor and in order to keep the administration
         * up to date a boolean hasValidGetCapabilities will be set to false if a GetCapabilities is not stictly
         * according to the WMS rules. This will prevent the user from being kept in the dark if something doesn't
         * work properly.
         */
        LayerValidator lv = new LayerValidator(layers);
        ServiceProviderValidator spv = new ServiceProviderValidator(serviceProviders);
        
        organization.setHasValidGetCapabilities(lv.validate() && spv.validate());
    }
    // </editor-fold>
    
    /* Creates a list with the available layers.
     *
     * @param layer The layer of which we have to find the parent layers.
     * @param layers Set <Layer> with all direct and indirect parental layers..
     *
     * @return the same set Set <Layer> as given.
     */
    // <editor-fold defaultstate="" desc="getAllParentLayers(Layer layer, Set <Layer> layers) method.">
    private Set getAllParentLayers(Layer layer, Set layers) {
        if(layer.getParent() != null) {
            layers.add(layer);
            this.getAllParentLayers(layer.getParent(), layers);
        } else {
            layers.add(layer);
        }
        return layers;
    }
    // </editor-fold>
}
