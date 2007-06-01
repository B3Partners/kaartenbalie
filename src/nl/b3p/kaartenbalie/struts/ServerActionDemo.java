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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.kaartenbalie.core.server.Layer;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.ServiceProvider;
import nl.b3p.kaartenbalie.core.server.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.xml.sax.SAXException;

public class ServerActionDemo extends ServerAction {

    /* forward name="success" path="" */
    private final static String SUCCESS = "success";
    private static final Log log = LogFactory.getLog(ServerActionDemo.class);
    protected static final String PREDEFINED_SERVER = "demo.serverurl";
    protected static final String PREDEFINED_SERVER_NAME = "demo.servername";
    protected static final String NOTREGISTERED_ERROR_KEY = "demo.errornotregistered";
    
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
        String userid = (String) request.getParameter("userid");
        ActionForward action = super.unspecified(mapping, dynaForm, request, response);
        dynaForm.set("userid", userid);
        
        MessageResources messages = getResources(request);
        Locale locale = getLocale(request);
        String serverName = messages.getMessage(locale, PREDEFINED_SERVER_NAME);
        String serverUrl  = messages.getMessage(locale, PREDEFINED_SERVER);
        dynaForm.set("givenName", serverName);
        dynaForm.set("url", serverUrl);
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
        User user = getUser(dynaForm, request, false);
        if (user == null) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, NOTREGISTERED_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        /*
         * No errors occured during usercheck. Therefore we can get a new serviceprovider object 
         * which we can use to connect this provider to the user.
         */
        ServiceProvider hibernateSP = getServiceProvider(dynaForm,request,true);
        ServiceProvider serviceProvider = (ServiceProvider) hibernateSP.clone();
        if (null == serviceProvider) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        /*
         * Because every new demo user has access to the predefined server which will
         * be supported by B3Partners we first need to get this WMS server from the database
         * in order to set the rights to this new user.
         */
        Session sess = getHibernateSession();
        MessageResources messages = getResources(request);
        Locale locale = getLocale(request);
        String serverName = messages.getMessage(locale, PREDEFINED_SERVER_NAME);
        String serverUrl  = messages.getMessage(locale, PREDEFINED_SERVER);
        
        ServiceProvider stdServiceProvider = (ServiceProvider)sess.createQuery(
                "from ServiceProvider sp where " +
                "lower(sp.givenName) = lower(:givenName) " +
                "and lower(sp.url) = lower(:url)")
            .setParameter("givenName", serverName)
            .setParameter("url", serverUrl)
            .uniqueResult();
        
        /*
         * Get all layers supported by this WMS server.
         */
        Set standardLayerSet = new HashSet();
        standardLayerSet = getAllLayers(stdServiceProvider.getLayers(), standardLayerSet);
        
        /*
         * Now we need to check if the given wms provider is the same as the provider
         * in the database. If this provider is the same all we need to do is setting
         * the rights of the standardLayerSet to this new userand save everything.
         * Otherwise we need to read the capabilities of the new ServiceProvider, add
         * this to the database and add these layers also to the rights of the new user.
         */
        
        /*
         * First we need to check if the given url is realy an url.
         */
        String url = dynaForm.getString("url");
        try {
            URL tempurl = new URL(url);
        } catch (MalformedURLException mue) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, MALFORMED_URL_ERRORKEY);
            return getAlternateForward(mapping, request);
        }
        
        /*
         * The given URL is checked, even though it might be the same URL as
         * predefined in the Kaartenbalie. Now we can check if the URL is the 
         * same as predefined.
         */
        Set newLayerSet = new HashSet();        
        if(!url.equals(serverUrl)) {
            /*
             * If we get inside this statement it means that we have a new URL
             */      
        
            /*
             * If the URL is valid we need to check if it complies with the WMS standard
             * This means that it should have at least an '?' or a '&' at the end of the
             * URL.
             * Furthermore if nothing else has been added to the URL, KB needs to add the
             * specific parameters REUQEST, VERSION and SERVICE to the URL in order for
             * KB to be able to perform the request.
             */
            int lastAmper = url.lastIndexOf("&");
            int lastQuest = url.lastIndexOf("?");
            int length = url.length();

            boolean hasLastAmper = (length == (lastAmper + 1));
            boolean hasLastQuest = (length == (lastQuest + 1));

            if (!hasLastAmper && !hasLastQuest) {
                prepareMethod(dynaForm, request, EDIT, LIST);
                addAlternateMessage(mapping, request, MISSING_SEPARATOR_ERRORKEY);
                return getAlternateForward(mapping, request);
            }

            String eventualURL = new String();
            String [] urls = url.split("\\?");
            eventualURL += urls[0] + "?";

            if (hasLastAmper) {
                //Maybe some parameters have been given. We need to check which params still
                //need to be added.
                boolean req = false, version = false, service = false;
                String [] params = urls[1].split("&");
                for (int i = 0; i < params.length; i++) {
                    String [] paramValue = params[i].split("=");
                    if (paramValue[0].equalsIgnoreCase(WMS_REQUEST)) {
                        try {
                            if (paramValue[1].equalsIgnoreCase(WMS_REQUEST_GetCapabilities)) {
                                eventualURL = eventualURL + paramValue[0] + "=" + paramValue[1] + "&";
                                req = true;
                            }
                        } catch (Exception e){log.debug("Parameter " + WMS_REQUEST + " gegeven, maar value niet. App voegt waarde zelf toe."); }
                    }
                    else if (paramValue[0].equalsIgnoreCase(WMS_VERSION)) {
                        try {
                            if (paramValue[1].equalsIgnoreCase(WMS_VERSION_111)) {
                                eventualURL = eventualURL + paramValue[0] + "=" + paramValue[1] + "&";
                                version = true;
                            }
                        } catch (Exception e){log.debug("Parameter " + WMS_VERSION + " gegeven, maar value niet. App voegt waarde zelf toe."); }
                    }
                    else if (paramValue[0].equalsIgnoreCase(WMS_SERVICE)) {
                        try {
                            if (paramValue[1].equalsIgnoreCase(WMS_SERVICE_WMS)) {
                                eventualURL = eventualURL + paramValue[0] + "=" + paramValue[1] + "&";
                                service = true;
                            }
                        } catch (Exception e){log.debug("Parameter " + WMS_SERVICE + " gegeven, maar value niet. App voegt waarde zelf toe."); }
                    }
                    else {
                        //An extra parameter which has to be given.
                        eventualURL = eventualURL + paramValue[0] + "=" + paramValue[1] + "&";
                    }
                }
                if (!req) {
                    eventualURL = eventualURL + WMS_REQUEST + "=" + WMS_REQUEST_GetCapabilities + "&";
                }
                if (!version) {
                    eventualURL = eventualURL + WMS_VERSION + "=" + WMS_VERSION_111 + "&";
                }
                if (!service) {
                    eventualURL = eventualURL + WMS_SERVICE + "=" + WMS_SERVICE_WMS + "&";
                }
            } else {
                //No parameters have been given at all. We need to add everything
                eventualURL = eventualURL + WMS_REQUEST + "=" + WMS_REQUEST_GetCapabilities + "&";
                eventualURL = eventualURL + WMS_VERSION + "=" + WMS_VERSION_111 + "&";
                eventualURL = eventualURL + WMS_SERVICE + "=" + WMS_SERVICE_WMS + "&";
            }

            /*
             * We have now a fully checked URL which can be used to add a new ServiceProvider
             * or to change an already existing ServiceProvider. Therefore we are first going
             * to create some objects which we need to change the data if necessary.
             */
            WMSCapabilitiesReader wms = new WMSCapabilitiesReader(serviceProvider);

            /*
             * This request can lead to several problems.
             * The server can be down or the url given isn't right. This means that the url
             * is correct according to the specification but is leading to the wrong address.
             * Or the address is OK, but the Capabilities of the provider do not comply the
             * specifications. Or there can be an other exception during the process.
             * Either way we need to inform the user about the error which occured.
             */
            try {
                serviceProvider = wms.getProvider(eventualURL);
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
                addAlternateMessage(mapping, request, e.toString());
                return getAlternateForward(mapping, request);
            }

            if(!serviceProvider.getWmsVersion().equalsIgnoreCase(WMS_VERSION_111)) {
                prepareMethod(dynaForm, request, EDIT, LIST);
                addAlternateMessage(mapping, request, UNSUPPORTED_WMSVERSION_ERRORKEY);
                return getAlternateForward(mapping, request);
            }
            
            /*
             * Save this new WMS provider in the database.
             */
            populateServerObject(dynaForm, serviceProvider);
            sess.saveOrUpdate(serviceProvider);
            
            /*
             * Get the layers of this new WMS provider.
             */
            newLayerSet = getAllLayers(serviceProvider.getLayers(), newLayerSet);
        }
        
        /*
         * If the newLayerSet is filled with new layers then we need to add these layers
         * to the list with all layers on which the user should have access.
         */
        if(!newLayerSet.isEmpty()) {
            Iterator it = newLayerSet.iterator();
            while (it.hasNext()) {
                Layer newLayer = (Layer) it.next();
                standardLayerSet.add(newLayer);
            }
        }
        
        /*
         * Get the users organization and store the layers into this Organization.
         */
        Organization org = user.getOrganization();
        org.setOrganizationLayer(standardLayerSet);
        sess.saveOrUpdate(org);
        sess.flush();
        
        return mapping.findForward("nextPage");
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
    public ActionForward delete(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return null;
    }
    // </editor-fold>
    
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
        return FormUtils.StringToInteger(dynaForm.getString("userid"));
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
    
    /** Defines a Set with layers in which only leafs are added. These have no childs.
     *
     * @param originalLayers
     * 
     * @return Set with only leaf layers
     */
    // <editor-fold defaultstate="" desc="getLeafLayers(Set orgLayers) method.">
    private Set getAllLayers(Set layers, Set newLayerSet) {
        if (layers != null) {
            Iterator it = layers.iterator();
            while (it.hasNext()) {
                Layer layer = (Layer) it.next();
                newLayerSet.add(layer);
                getAllLayers(layer.getLayers(), newLayerSet);            
            }
        }
        return newLayerSet;
    }
    // </editor-fold>
}