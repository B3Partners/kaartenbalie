/**
 * @(#)ServerAction.java
 * @author N. de Goeij
 * @version 1.00 2006/10/02
 *
 * Purpose: a Struts action class defining all the Action for the ServiceProvider view.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.struts;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.wms.capabilities.ServiceProvider;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.wms.capabilities.WMSCapabilitiesReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;

import org.apache.struts.validator.DynaValidatorForm;
import org.securityfilter.filter.SecurityRequestWrapper;
import org.xml.sax.SAXException;

public class ServerActionDemo extends ServerAction {
    
    private static final Log log = LogFactory.getLog(ServerActionDemo.class);
    protected static final String NOTREGISTERED_ERROR_KEY = "demo.errornotregistered";
    protected static final String MAP_ALREADY_ADDED = "demo.mapalreadyadded";
    
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
        String userid = request.getParameter("userid");
        ActionForward action = super.unspecified(mapping, dynaForm, request, response);
        dynaForm.set("id", userid);
        return action;
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
        EntityManager em = getEntityManager();
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
         * First lets check if the user still exists.
         */
        User user = (User)request.getUserPrincipal();
        if (user == null) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, NOTREGISTERED_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        User dbUser = (User) em.createQuery("from User u where u.id = :uid").setParameter("uid", user.getId()).getSingleResult();        
        Organization org = dbUser.getOrganization();        
                
        /*
         * Now check if the given abbreviation is unique.
         */
        if (!isAbbrUnique(null, dynaForm, em)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, NON_UNIQUE_ABBREVIATION_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        String abbreviation = FormUtils.nullIfEmpty(dynaForm.getString("abbr"));
        if(!isAlphaNumeric(abbreviation)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, NON_ALPHANUMERIC_ABBREVIATION_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        /*
         * If previous check were completed succesfully, we can start performing the real request which is
         * saving the user input. This means that we can start checking if we are dealing with a new
         * serviceprovider or with an existing one which has to be updated. In both cases we need to load a
         * new Serviceprovider into the memory. Before we can take any action we need the users input to read
         * the variables.
         */
        
        /*
         * First we need to check if the given url is conform the url standard.
         */
        String url = dynaForm.getString("url");
        try {
            URL tempurl = new URL(url);
        } catch (MalformedURLException mue) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, MALFORMED_URL_ERRORKEY);
            return getAlternateForward(mapping, request);
        }
        
        try {
            url = checkWmsUrl(url);
        } catch (Exception e) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, null, e.getMessage());
            return getAlternateForward(mapping, request);
        }
        
        /*
         * We have now a fully checked URL which can be used to add a new ServiceProvider
         * or to change an already existing ServiceProvider. Therefore we are first going
         * to create some objects which we need to change the data if necessary.
         */
        ServiceProvider newServiceProvider = null;
        WMSCapabilitiesReader wms = new WMSCapabilitiesReader();
        
        /*
         * This request can lead to several problems.
         * The server can be down or the url given isn't right. This means that the url
         * is correct according to the specification but is leading to the wrong address.
         * Or the address is OK, but the Capabilities of the provider do not comply the
         * specifications. Or there can be an other exception during the process.
         * Either way we need to inform the user about the error which occured.
         */
        try {
            newServiceProvider = wms.getProvider(url);
        } catch (IOException e) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, SERVER_CONNECTION_ERRORKEY);
            return getAlternateForward(mapping, request);
        } catch (SAXException e) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, MALFORMED_CAPABILITY_ERRORKEY);
            return getAlternateForward(mapping, request);
        } catch (Exception e) {
            log.error("Error saving server", e);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, null, e.getMessage());
            return getAlternateForward(mapping, request);
        }
        
        if(!newServiceProvider.getWmsVersion().equalsIgnoreCase(WMS_VERSION_111)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, UNSUPPORTED_WMSVERSION_ERRORKEY);
            return getAlternateForward(mapping, request);
        }        
        
        /*
         * Now we first need to save this serviceprovider.
         */
        populateServerObject(dynaForm, newServiceProvider);
        if (newServiceProvider.getId() == null) {
            // haal set op om vulling van set af te dwingen
            Set testSet = newServiceProvider.getAllLayers();
            em.persist(newServiceProvider);
        }
        
        Set organizationLayers = new HashSet();
        Iterator it = org.getOrganizationLayer().iterator();
        while (it.hasNext()) {
            organizationLayers.add(((Layer)it.next()).clone());
        }
        
        Iterator newLayers = newServiceProvider.getAllLayers().iterator();
        while (newLayers.hasNext()) {
            organizationLayers.add((Layer)newLayers.next());
        }
        
        org.setOrganizationLayer(organizationLayers);
        user.setOrganization(org);
        
        if (org.getId() == null) {
            em.persist(org);
        } else {
            em.merge(org);
        }
        
        if (user.getId() == null) {
            em.persist(user);
        } else {
            em.merge(user);
        }
        em.flush();
        
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
        
        return mapping.findForward("nextPage");
    }
    // </editor-fold>
    
    
}