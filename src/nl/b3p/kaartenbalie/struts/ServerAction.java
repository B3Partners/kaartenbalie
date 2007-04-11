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
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.kaartenbalie.core.server.Layer;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.ServiceProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.validator.DynaValidatorForm;
import org.hibernate.Session;
import org.xml.sax.SAXException;

public class ServerAction extends KaartenbalieCrudAction {
    
    /* forward name="success" path="" */
    private final static String SUCCESS = "success";
     private static final Log log = LogFactory.getLog(ServerAction.class);
    
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
        String selectedId = request.getParameter("id");
        
        if (selectedId != null) {
            Integer id = FormUtils.StringToInteger(selectedId);
            ServiceProvider serviceProvider = this.getServiceProvider(dynaForm, request, false, id);
            
            if (null != serviceProvider) {
                this.populateServiceProviderForm(serviceProvider, dynaForm, request);
            }
            
            request.setAttribute("selectedId", selectedId);
        } else {
            request.setAttribute("selectedId", null);
        }
        return super.unspecified(mapping, dynaForm, request, response);
    }
    // </editor-fold>
    
    /**
     * Creates a new login code for the specified email address
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
    public ActionForward create(ActionMapping mapping, DynaValidatorForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("showFields", "show");
        return super.create(mapping, form, request, response);
    }
    // </editor-fold>
        
    /**
     * Creates a new login code for the specified email address
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
    // <editor-fold defaultstate="collapsed" desc="cancelled(ActionMapping mapping, DynaValidatorForm form, HttpServletRequest request, HttpServletResponse response) method.">
    public ActionForward cancelled(ActionMapping mapping, DynaValidatorForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("showFields", null);
        return super.cancelled(mapping, form, request, response);
    }
    // </editor-fold>
        
    /** Method which returns the service provider with a specified id.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param createNew A boolean which indicates if a new object has to be created.
     * @param id An Integer indicating which organization id has to be searched for.
     *
     * @return a service provider object.
     */
    // <editor-fold defaultstate="" desc="getServiceProvider(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew, Integer id) method.">
    protected ServiceProvider getServiceProvider(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew, Integer id) {
        Session session = getHibernateSession();
        ServiceProvider serviceProvider = null;
        
        if(null == id && createNew) {
            serviceProvider = new ServiceProvider();
        } else if (null != id) {
            serviceProvider = (ServiceProvider)session.load(ServiceProvider.class, new Integer(id.intValue()));
        }
        return serviceProvider;
    }
    // </editor-fold>
    
    /** Method which will fill the JSP form with the data of a given service provider.
     *
     * @param serviceProvider ServiceProvider object from which the information has to be printed.
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     */
    // <editor-fold defaultstate="" desc="populateOrganizationForm(ServiceProvider serviceProvider, DynaValidatorForm dynaForm, HttpServletRequest request) method.">
    private void populateServiceProviderForm(ServiceProvider serviceProvider, DynaValidatorForm dynaForm, HttpServletRequest request) {
        dynaForm.set("serverid", serviceProvider.getId().toString());
        dynaForm.set("serviceProviderGivenName", serviceProvider.getGivenName());
        dynaForm.set("serviceProviderUrl", serviceProvider.getUrl());
        dynaForm.set("serviceProviderUpdatedDate", serviceProvider.getUpdatedDate().toString());
        dynaForm.set("serviceProviderReviewed", "false");
    }
    // </editor-fold>
    
    /** Creates a list of all the service providers in the database.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="" desc="createLists(DynaValidatorForm form, HttpServletRequest request) method.">
    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        super.createLists(form, request);
        
        List serviceproviderlist = getHibernateSession().createQuery("from ServiceProvider").list();
        request.setAttribute("serviceproviderlist", serviceproviderlist);        
    }
    // </editor-fold>z
    
    /** Method that fills a serive provider object with the user input from the forms.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param serviceProvider ServiceProvider object that to be filled
     */
    // <editor-fold defaultstate="" desc="populateServerObject(DynaValidatorForm dynaForm, ServiceProvider serviceProvider) method.">
    protected void populateServerObject(DynaValidatorForm dynaForm, ServiceProvider serviceProvider) {
        serviceProvider.setGivenName(FormUtils.nullIfEmpty(dynaForm.getString("serviceProviderGivenName")));
        serviceProvider.setUrl(dynaForm.getString("serviceProviderUrl"));
        if (serviceProvider.getId() == null) {
            serviceProvider.setUpdatedDate(new Date());
            serviceProvider.setReviewed(false);
        }
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
        Session sess = getHibernateSession();
        
        /*
         * Before we can start checking for changes or adding a new serviceprovider, we first need to check if
         * everything is valid. If not it is of no use to go on with the action.
         */
        if (!isTokenValid(request)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        ActionErrors errors = dynaForm.validate(mapping, request);
        
        if(!errors.isEmpty()) {
            addMessages(request, errors);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, VALIDATION_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        /*
         * Everything is OK, so now we can start checking if we are dealing with 
         * a new serviceprovider or with an existing one which has to be updated.
         * In both cases we need to load a new Serviceprovider into the memory.
         * Before we can take any action we need the users input to read the va-
         * riables.
         */
        Integer id = FormUtils.StringToInteger(dynaForm.getString("serverid"));
        //Deze kan straks verwijderd worden.
        //ServiceProvider serviceProvider = getServiceProvider(dynaForm, request, true, id);
        
        ServiceProvider newServiceProvider = null;
        ServiceProvider oldServiceProvider = null;
        if (id == null) {
            newServiceProvider = getServiceProvider(dynaForm, request, true, null);
        } else {
            oldServiceProvider = getServiceProvider(dynaForm, request, true, id);
            newServiceProvider = getServiceProvider(dynaForm, request, true, null);
        }
        
        String url = dynaForm.getString("serviceProviderUrl");
        
        /*
         * The given url needs to be checked against some rules:
         * - the url needs to have at least the three parameters REUQEST, VERSION and SERVICE
         * If the ? seperator is missing it means that there are no paramaters given.
         * No extra tests have to be done.
         * Otherwise we need to test if all three parameters are given.
         *
         * - if either one of them is missing we should give a error message.
         * Also as a special extra rule, each url always has to be closed with an &.
         */
        String [] urls = url.split("\\?");
        try {
            String params = urls[1];
        } catch (Exception e) {
            request.setAttribute("message", "Missing seperator \'?\'");
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        boolean req = false, version = false, service = false;        
        String [] params = urls[1].split("&");
        for (int i = 0; i < params.length; i++) {
            String [] paramValue = params[i].split("=");
            System.out.println("param value: " + paramValue[0]);
            if (paramValue[0].equalsIgnoreCase("REQUEST"))
                req = true;
            if (paramValue[0].equalsIgnoreCase("VERSION"))
                version = true;
            if (paramValue[0].equalsIgnoreCase("SERVICE"))
                service = true;
        }
        
        if (!(req && version && service)) {
            request.setAttribute("message", "Missing parameters REQUEST, VERSION and/or SERVICE in URL");
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }

        int lastchar = url.lastIndexOf("&");
        int length = url.length();
        System.out.println("Lastchar: " + lastchar + " Length: " + length);
        if (url.length() != (url.lastIndexOf("&") + 1)) {
            request.setAttribute("message", "Missing closing seperator \'&\'");
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        /*
         * Because we are dealing in both cases with a new serviceprovider
         * first thing we can do is load all the data of the new serviceproiver 
         * into the memory. After doing that we can do one final step in which
         * we will check if there is an old serviceproivder and if so we need
         * to copy all the layer rights from one to the other.
         *
         * if the newServiceProvider is null it means that something 
         * went wrong with the creation of this object.
         */
        if (newServiceProvider == null) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }

        /* 
         * Otherwise we can fill this object with information from the internet.
         */
        WMSCapabilitiesReader wms = new WMSCapabilitiesReader(newServiceProvider);

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
            request.setAttribute("message", "Kan geen verbinding maken met de server. Controleer de URL en/of controleer of de server actief is.");
            log.error("Error saving server", e);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        } catch (SAXException e) {
            request.setAttribute("message", "Kan verbinding met server maken maar deze heeft een ongeldige Capability en niet worden opgeslagen.");
            log.error("Error saving server", e);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        } catch (Exception e) {
            request.setAttribute("message", "Er is een fout opgetreden: " + e);
            log.error("Error saving server", e);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        /*
         * All tests have been completed succesfully.
         * We have now a newServiceProvider object and all we need to check
         * is if this serviceprovider has been changed or newly added. The
         * easiest way of doing so, is by checking the id.
         */
        if(id != null) {
            /* Before we can start, we need to save the new serviceprovider.
             * 
             * Then we need to call for a list with organizations. 
             * We walk through this list and for each organization in the
             * list we need to check if this organization has connections
             * with the old serviceprovider.
             * 
             * The following steps have to be made:
             * Which each layer belonging to a certain organization check if this
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
            
            populateServerObject(dynaForm, newServiceProvider);
            newServiceProvider.setReviewed(true);
            sess.saveOrUpdate(newServiceProvider);
            sess.flush();
            
            List orgList = sess.createQuery("from Organization").list();
            Iterator orgit = orgList.iterator();
            while (orgit.hasNext()) {
                Set newOrganizationLayer = new HashSet();
                Organization org = (Organization)orgit.next();
                Set orgLayers = org.getOrganizationLayer();
                Iterator layerit = orgLayers.iterator();
                while (layerit.hasNext()) {
                    Layer organizationLayer = (Layer)layerit.next();
                    ServiceProvider orgLayerServiceProvider = organizationLayer.getServiceProvider();
                    if (orgLayerServiceProvider.getGivenName().equals(oldServiceProvider.getGivenName())) {
                        /* The layer belongs to the old servideprovider.
                         * So now we need to check if this same layer is
                         * still available in the new serviceprovider.
                         * If this is true then we need to add this layer
                         * again to the new list with layer rights for
                         * this organization. Otherwise we don't have to
                         * do anything.
                         */
                        Layer newLayer = checkLayer(organizationLayer, newServiceProvider.getLayers());
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
                sess.saveOrUpdate(org);
                sess.flush();
            }       
            //EIND TEST
        } else {
            populateServerObject(dynaForm, newServiceProvider);
            sess.saveOrUpdate(newServiceProvider);
            sess.flush();
        }
        
        try {
            sess.delete(oldServiceProvider);
            sess.flush();
        } catch (Exception e) {
            super.msg = "De service is niet juist geupdate/verwijderd; Er zijn nog organisaties gekoppeld aan deze service.";
            log.error("Error deleting server", e);
        }
                
        dynaForm.set("serverid", "");
        dynaForm.set("serviceProviderGivenName", "");
        dynaForm.set("serviceProviderUrl", "");
        dynaForm.set("serviceProviderUpdatedDate", "");
        dynaForm.set("serviceProviderReviewed", "");
        
        return super.save(mapping,dynaForm,request,response);
    }
    // </editor-fold>
    
    /** Tries to find a specified layer given for a certain ServiceProvider.
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
            
            if(orgLayer.getName().equalsIgnoreCase(layer.getName()) || orgLayer.getTitle().equalsIgnoreCase(layer.getTitle()))
                return layer;
            
            Layer foundLayer = checkLayer(orgLayer, layer.getLayers());
            if (foundLayer != null)
                return foundLayer;
        }
        
        return null;
    }
    // </editor-fold>    
    
    /** Method for deleting a serviceprovider selected by a user.
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
        
        String [] serviceProviderSelected = dynaForm.getStrings("serviceProviderSelected");
        int size = serviceProviderSelected.length;
        
        for(int i = 0; i < size; i++) {
            //if invalid
            if (!isTokenValid(request)) {
                prepareMethod(dynaForm, request, EDIT, LIST);
                addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
                return getAlternateForward(mapping, request);
            }

            // nieuwe default actie op delete zetten
            Session sess = getHibernateSession();
                        
            Integer id = new Integer(Integer.parseInt(serviceProviderSelected[i]));
            ServiceProvider serviceProvider = getServiceProvider(dynaForm,request,true, id);
            
            if (null == serviceProvider) {
                prepareMethod(dynaForm, request, LIST, EDIT);
                addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
                return getAlternateForward(mapping, request);
            }

            populateServerObject(dynaForm, serviceProvider);
            //store in db
            try {
                sess.delete(serviceProvider);
                sess.flush();
            } catch (Exception e) {
                super.msg = "De service is niet verwijderd: Er zijn nog organisaties gekoppeld aan deze service.";
                log.error("Error deleting server", e);
            }
        }
        return super.delete(mapping, dynaForm, request, response);
    }
    // </editor-fold>
}