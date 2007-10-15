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
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.wms.capabilities.KBConstants;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.wms.capabilities.ServiceProvider;
import nl.b3p.wms.capabilities.WMSCapabilitiesReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.validator.DynaValidatorForm;
import org.xml.sax.SAXException;

public class ServerAction extends KaartenbalieCrudAction implements KBConstants {
    
    /* forward name="success" path="" */
    private final static String SUCCESS = "success";
    private static final Log log = LogFactory.getLog(ServerAction.class);
    
    protected static final String MISSING_SEPARATOR_ERRORKEY = "error.missingseparator";
    protected static final String MISSING_QUESTIONMARK_ERRORKEY = "error.missingquestionmark";
    protected static final String SERVER_CONNECTION_ERRORKEY = "error.serverconnection";
    protected static final String MALFORMED_URL_ERRORKEY = "error.malformedurl";
    protected static final String MALFORMED_CAPABILITY_ERRORKEY = "error.malformedcapability";
    protected static final String SERVICE_LINKED_ERROR_KEY = "error.servicestilllinked";
    protected static final String UNSUPPORTED_WMSVERSION_ERRORKEY = "error.wmsversion";
    protected static final String SP_NOTFOUND_ERROR_KEY = "error.spnotfound";
    
    //-------------------------------------------------------------------------------------------------------
    // PUBLIC METHODS
    //-------------------------------------------------------------------------------------------------------
    
    /* Execute method which handles all unspecified requests.
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
        this.createLists(dynaForm, request);
        prepareMethod(dynaForm, request, LIST, LIST);
        addDefaultMessage(mapping, request);
        return mapping.findForward(SUCCESS);
    }
    // </editor-fold>
    
    /* Edit method which handles all editable requests.
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
    // <editor-fold defaultstate="" desc="edit(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    public ActionForward edit(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServiceProvider serviceprovider = getServiceProvider(dynaForm, request, false);
        if (serviceprovider == null) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, SP_NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        populateServiceProviderForm(serviceprovider, dynaForm, request);
        return super.edit(mapping, dynaForm, request, response);
    }
    // </editor-fold>
    
    /* Method for saving a new service provider from input of a user.
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
            super.addMessages(request, errors);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, VALIDATION_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        /*
         * If previous check were completed succesfully, we can start performing the real request which is
         * saving the user input. This means that we can start checking if we are dealing with a new
         * serviceprovider or with an existing one which has to be updated. In both cases we need to load a
         * new Serviceprovider into the memory. Before we can take any action we need the users input to read
         * the variables.
         */
        //Session sess = getHibernateSession();
        
        String url = dynaForm.getString("url");
        
        /*
         * First we need to check if the given url is realy an url.
         */
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
        ServiceProvider oldServiceProvider = getServiceProvider(dynaForm, request, false);
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
        
        populateServerObject(dynaForm, newServiceProvider);
        newServiceProvider.setReviewed(true);
        em.persist(newServiceProvider);
        em.flush();
        //sess.saveOrUpdate(newServiceProvider);
        //sess.flush();
        
        /*
         * All tests have been completed succesfully.
         * We have now a newServiceProvider object and all we need to check
         * is if this serviceprovider has been changed or newly added. The
         * easiest way of doing so, is by checking the id.
         */
        if(oldServiceProvider != null) {
            /* Before we can start, we need to save the new serviceprovider.
             *
             * Then we need to call for a list with organizations.
             * We walk through this list and for each organization in the
             * list we need to check if this organization has connections
             * with the old serviceprovider.
             *
             * The following steps have to be made:
             * With each layer belonging to a certain organization check if this
             * layer belongs to the old serviceprovider.
             *      if not -> save the layer in a new set and go to the next layer
             *      if yes -> check if the old layer also appears in the list of layers
             *                of the new serviceprovider.
             *          if not -> don't do anything. Just skip this layer, it will not be saved into the new set.
             *          if yes -> add the new layer in the new set of layers for this organization.
             * When all is done save the organization and go on with the next one.
             *
             * After completing all organizations, we can delete the old serviceprovider.
             */
            List orgList = em.createQuery("from Organization").getResultList();
            Iterator orgit = orgList.iterator();
            while (orgit.hasNext()) {
                Set newOrganizationLayer = new HashSet();
                Organization org = (Organization)orgit.next();
                Set orgLayers = org.getOrganizationLayer();
                Iterator layerit = orgLayers.iterator();
                while (layerit.hasNext()) {
                    /*
                     * We are now iterating over a set of layers which belong to one organization.
                     * Each of these layers have specified which serviceprovider they belong to.
                     * So we can check if the id of the layer serviceprovider is the same as the
                     * id of the old serviceprovider from above.
                     */
                    Layer organizationLayer = (Layer)layerit.next();
                    ServiceProvider orgLayerServiceProvider = organizationLayer.getServiceProvider();
                    if (orgLayerServiceProvider.getId() == oldServiceProvider.getId()) {
                        /* It is for sure that the layer belongs to the old servideprovider.
                         * So now we need to check if this same layer is still available in
                         * the new serviceprovider. If this is true then we need to add this
                         * layer again to the new list with layer rights for  this organization.
                         * Otherwise we don't have to do anything.
                         * Since this layer belongs to the old serviceprovider we don't have to
                         * be affraid that we are checking against the wrong items. Therefore
                         * we can perform this check just by checking if the layer titles are
                         * the same.
                         */
                        Set topLayerSet = new HashSet();
                        topLayerSet.add(newServiceProvider.getTopLayer());
                        Layer newLayer = checkLayer(organizationLayer, topLayerSet);
                        if (newLayer != null)
                            newOrganizationLayer.add(newLayer);
                    } else {
                        /* The layer doesn't belong to the old serviceprovider.
                         * Therefore not much has to be done. We only need to
                         * add this layer again back to the list with layer-
                         * rights.
                         */
                        newOrganizationLayer.add(organizationLayer);
                    }
                }
                //vervang de oude set met layers in de organisatie voor de nieuwe set
                org.setOrganizationLayer(newOrganizationLayer);
                //sess.saveOrUpdate(org);
                em.flush();
            }
            
            try {
                em.remove(oldServiceProvider);
                em.flush();
            } catch (Exception e) {
                log.error("Error deleting the old serviceprovider", e);
                prepareMethod(dynaForm, request, EDIT, LIST);
                addAlternateMessage(mapping, request, null, e.getMessage());
                return getAlternateForward(mapping, request);
            }
        }
        
        dynaForm.set("id", null);
        return super.save(mapping, dynaForm, request, response);
    }
    // </editor-fold>
    
    /* Method for deleting a serviceprovider selected by a user.
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
        
        EntityManager em = getEntityManager();
        /*
         * Before we can start deleting a serviceprovider, we first need to check if the given token
         * is valid. First there will be checked if the request is valid. This means that every JSP
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
         * Get the serviceprovider which is given in the form. If for some reason this
         * ServiceProvider doesn't exist anymore in the database then we need to catch
         * this error and show it to the user.
         */
        ServiceProvider serviceProvider = getServiceProvider(dynaForm, request, false);
        if (null == serviceProvider) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        /*
         * Instead of letting the Database decide if it is allowed to delete a serviceprovider
         * we can decide this ourselves. All there has to be done is checking if there are
         * still rights connected to this serviceprovider. Since the rights to a certain layer
         * are going from bottom to top (this means if a sub layer is set with right that all
         * the superlayers of this sublayer also are set with the same rights) all we have to
         * do is getting the most upper layer and compare this one with the toplayer in the
         * servicerpovider.
         * Therefore we only need to create a loop with all the organizationlayers, ask each
         * one of them for it's toplayer and compare it. If we find one single equality it is
         * enough to stop the search and give an error.
         */
        Layer serviceProviderTopLayer = serviceProvider.getTopLayer();
        if (serviceProviderTopLayer!=null) {
            List orgList = em.createQuery("from Organization").getResultList();
            Iterator orgit = orgList.iterator();
            while (orgit.hasNext()) {
                Organization org = (Organization)orgit.next();
                Set orgLayers = org.getOrganizationLayer();
                Iterator orgLayerIterator = orgLayers.iterator();
                while (orgLayerIterator.hasNext()) {
                    Layer organizationLayer = (Layer)orgLayerIterator.next();
                    Layer organizationLayerTopLayer = organizationLayer.getTopLayer();
                    if (organizationLayerTopLayer!=null && 
                            organizationLayerTopLayer.getId() == serviceProviderTopLayer.getId()) {
                        prepareMethod(dynaForm, request, LIST, EDIT);
                        addAlternateMessage(mapping, request, SERVICE_LINKED_ERROR_KEY);
                        return getAlternateForward(mapping, request);
                    }
                }
            }
        }
        
        /*
         * If no errors occured and no right were set anymore to this serviceprovider
         * we can assume that all is good and we can safely delete this serviceproiver.
         * Any other exception that might occur is in the form of an unknown or unsuspected
         * form and will be thrown in the super class.
         *
         */
        em.remove(serviceProvider);
        em.flush();
        return super.delete(mapping, dynaForm, request, response);
    }
    // </editor-fold>
    
    /* Creates a list of all the service providers in the database.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="" desc="createLists(DynaValidatorForm form, HttpServletRequest request) method.">
    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        super.createLists(form, request);
        
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        List serviceproviderlist = em.createQuery("from ServiceProvider").getResultList();
        request.setAttribute("serviceproviderlist", serviceproviderlist);
        
    }
    // </editor-fold>
    
    //-------------------------------------------------------------------------------------------------------
    // PROTECTED METHODS -- Will be used in the demo by ServerActioDemo
    //-------------------------------------------------------------------------------------------------------
    
    /* Method which returns the service provider with a specified id or a new object if no id is given.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param createNew A boolean which indicates if a new object has to be created.
     * @param id An Integer indicating which organization id has to be searched for.
     *
     * @return a service provider object.
     */
    // <editor-fold defaultstate="" desc="getServiceProvider(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew, Integer id) method.">
    protected ServiceProvider getServiceProvider(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew) {
        
        
        EntityManager em = getEntityManager();
        ServiceProvider serviceProvider = null;
        Integer id = getID(dynaForm);
        
        if(null == id && createNew) {
            serviceProvider = new ServiceProvider();
        } else if (null != id) {
            serviceProvider = (ServiceProvider)em.find(ServiceProvider.class, new Integer(id.intValue()));
        }
        return serviceProvider;
    }
    // </editor-fold>
    
    /* Method that fills a serive provider object with the user input from the forms.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param serviceProvider ServiceProvider object that to be filled
     */
    // <editor-fold defaultstate="" desc="populateServerObject(DynaValidatorForm dynaForm, ServiceProvider serviceProvider) method.">
    protected void populateServerObject(DynaValidatorForm dynaForm, ServiceProvider serviceProvider) {
        serviceProvider.setGivenName(FormUtils.nullIfEmpty(dynaForm.getString("givenName")));
        serviceProvider.setUrl(getUrlWithoutParams(dynaForm.getString("url")));
        if (serviceProvider.getId() == null) {
            serviceProvider.setUpdatedDate(new Date());
        }
    }
    // </editor-fold>
    
    //-------------------------------------------------------------------------------------------------------
    // PRIVATE METHODS
    //-------------------------------------------------------------------------------------------------------
    
    /* Method which gets the hidden id in a form.
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
    protected Integer getID(DynaValidatorForm dynaForm) {
        return FormUtils.StringToInteger(dynaForm.getString("id"));
    }
    // </editor-fold>
    
    /* Method which will fill the JSP form with the data of a given service provider.
     *
     * @param serviceProvider ServiceProvider object from which the information has to be printed.
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     */
    // <editor-fold defaultstate="" desc="populateOrganizationForm(ServiceProvider serviceProvider, DynaValidatorForm dynaForm, HttpServletRequest request) method.">
    private void populateServiceProviderForm(ServiceProvider serviceProvider, DynaValidatorForm dynaForm, HttpServletRequest request) {
        dynaForm.set("id", serviceProvider.getId().toString());
        dynaForm.set("givenName", serviceProvider.getGivenName());
        dynaForm.set("url", serviceProvider.getUrl());
        dynaForm.set("updatedDate", serviceProvider.getUpdatedDate().toString());
    }
    // </editor-fold>
    
    /* Tries to find a specified layer given for a certain ServiceProvider.
     *
     * @param layers the set with layers which the method has to surch through
     * @param orgLayer the layer to be found
     *
     * @return layer if found.
     */
    // <editor-fold defaultstate="" desc="checkLayer(Layer orgLayer, Set layers) method.">
    private Layer checkLayer(Layer orgLayer, Set layers) {
        if (layers==null || layers.isEmpty())
            return null;
        
        Iterator it = layers.iterator();
        while (it.hasNext()) {
            Layer layer = (Layer) it.next();
            
            if (orgLayer.getName()==null && layer.getName()==null &&
                    orgLayer.getTitle().equalsIgnoreCase(layer.getTitle()))
                return layer;
            
            if (orgLayer.getName()!=null && layer.getName()!=null &&
                    orgLayer.getName().equalsIgnoreCase(layer.getName()))
                return layer;
            
            Layer foundLayer = checkLayer(orgLayer, layer.getLayers());
            if (foundLayer != null)
                return foundLayer;
        }
        return null;
    }
    // </editor-fold>
    
    private String getUrlWithoutParams(String url) {
        int qpos = url.lastIndexOf("?");
        if (url.length()==qpos+1)
            return url;
        
        StringBuffer trimmedUrl = new StringBuffer(url.substring(0, qpos));
        trimmedUrl.append("?");
        String theParams = url.substring(qpos + 1);
        String [] paramPairs = theParams.split("&");
        for (int i = 0; i < paramPairs.length; i ++) {
            String [] params = paramPairs[i].split("=");
            if(params.length>1 &&
                    !PARAMS_GetCapabilities.contains(params[0]) &&
                    !PARAMS_GetMap.contains(params[0]) &&
                    !PARAMS_GetFeatureInfo.contains(params[0]) &&
                    !PARAMS_GetLegendGraphic.contains(params[0]) &&
                    !PARAMS_GetStyles.contains(params[0]) &&
                    !PARAMS_PutStyles.contains(params[0]) &&
                    !PARAMS_DescribeLayer.contains(params[0])) {
                trimmedUrl.append(params[0]);
                trimmedUrl.append("=");
                trimmedUrl.append(params[1]);
                trimmedUrl.append("&");
            }
        }
        return trimmedUrl.toString();
    }
    
    protected String checkWmsUrl(String url) throws Exception {
        
        /*
         * If the URL is valid we need to check if it complies with the WMS standard
         * This means that it should have at least an '?' or a '&' at the end of the
         * URL.
         * Furthermore if nothing else has been added to the URL, KB needs to add the
         * specific parameters REUQEST, VERSION and SERVICE to the URL in order for
         * KB to be able to perform the request.
         */
        
        int length = url.length();
        
        boolean hasLastQuest = false;
        int lastQuest = url.lastIndexOf("?");
        if (lastQuest >= 0 || length == lastQuest + 1) {
            hasLastQuest = true;
        }
        
        boolean hasLastAmper = false;
        int lastAmper = url.lastIndexOf("&");
        if ((lastAmper >= 0 && length == lastAmper + 1) || length == lastQuest + 1) {
            hasLastAmper = true;
        }
        
        if (!hasLastQuest)
            throw new Exception(MISSING_QUESTIONMARK_ERRORKEY);
        
        if (!hasLastAmper)
            throw new Exception(MISSING_SEPARATOR_ERRORKEY);
        
        //Maybe some parameters have been given. We need to check which params still
        //need to be added.
        boolean req = false;
        boolean version = false;
        boolean service = false;
        
        String paramURL = url.substring(lastQuest + 1);
        String [] params = paramURL.split("&");
        for (int i = 0; i < params.length; i++) {
            String [] paramValue = params[i].split("=");
            if (paramValue.length == 2) {
                if (paramValue[0].equalsIgnoreCase(WMS_REQUEST) && paramValue[1].equalsIgnoreCase(WMS_REQUEST_GetCapabilities)) {
                    req = true;
                } else if (paramValue[0].equalsIgnoreCase(WMS_VERSION) && paramValue[1].equalsIgnoreCase(WMS_VERSION_111)) {
                    version = true;
                } else if (paramValue[0].equalsIgnoreCase(WMS_SERVICE) && paramValue[1].equalsIgnoreCase(WMS_SERVICE_WMS)) {
                    service = true;
                }
            }
        }
        
        if (!req) {
            url += WMS_REQUEST + "=" + WMS_REQUEST_GetCapabilities + "&";
        }
        if (!version) {
            url += WMS_VERSION + "=" + WMS_VERSION_111 + "&";
        }
        if (!service) {
            url += WMS_SERVICE + "=" + WMS_SERVICE_WMS + "&";
        }
        
        return url;
    }
}