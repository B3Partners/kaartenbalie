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
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
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
        //if invalid
        if (!isTokenValid(request)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        // nieuwe default actie op delete zetten
        //
        //validate and check for errors
        ActionErrors errors = dynaForm.validate(mapping, request);
        if(!errors.isEmpty()) {
            addMessages(request, errors);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, VALIDATION_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        Integer id = FormUtils.StringToInteger(dynaForm.getString("serverid"));
        
        ServiceProvider serviceProvider = getServiceProvider(dynaForm,request,true, id);
        
        if(id == null) {                
            if (null == serviceProvider) {
                prepareMethod(dynaForm, request, LIST, EDIT);
                addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
                return getAlternateForward(mapping, request);
            }

            WMSCapabilitiesReader wms = new WMSCapabilitiesReader(serviceProvider);
            String url = dynaForm.getString("serviceProviderUrl");

            //check this URL, if no parameters are given, fill them in yourself with the standard options
            //Split eerst in twee delen, namelijk daar waar het vraagteken zich bevindt
            String [] urls = url.split("\\?");
            url = urls[0] + "?";
            
            try {
                String params = urls[1];
            } catch (Exception e) {
                request.setAttribute("message", "Missing parameters REQUEST, VERSION and/or SERVICE in URL");
                prepareMethod(dynaForm, request, LIST, EDIT);
                addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
                return getAlternateForward(mapping, request);
            }

            boolean req = false, version = false, service = false;        
            String [] params = urls[1].split("&");
            for (int i = 0; i < params.length; i++) {
                String [] paramValue = params[i].split("=");
                if (!paramValue[0].equalsIgnoreCase("REQUEST") &&
                    !paramValue[0].equalsIgnoreCase("VERSION") &&
                    !paramValue[0].equalsIgnoreCase("SERVICE")) {
                    url += paramValue[0] + "=" + paramValue[1] + "&";
                }
            }

            url += "REQUEST=GetCapabilities&VERSION=1.1.1&SERVICE=WMS";

            try {
                serviceProvider = wms.getProvider(url);
            } catch (IOException e) {
                request.setAttribute("message", "Kan geen verbinding maken met de server. Controleer de URL en/of controleer of de server actief is.");
                //super.msg = "Kan geen verbinding maken met de server. Controleer de URL en/of controleer of de server actief is.";
                log.error("Error saving server", e);
                prepareMethod(dynaForm, request, EDIT, LIST);
                addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
                return getAlternateForward(mapping, request);
            } catch (SAXException e) {
                request.setAttribute("message", "Kan verbinding met server maken maar deze heeft een ongeldige Capability en niet worden opgeslagen.");
                //super.msg = "Kan verbinding met server maken maar deze heeft een ongeldige Capability en niet worden opgeslagen.";
                log.error("Error saving server", e);
                prepareMethod(dynaForm, request, EDIT, LIST);
                addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
                return getAlternateForward(mapping, request);
            } catch (Exception e) {
                request.setAttribute("message", "Er is een fout opgetreden: " + e);
                //super.msg = "Er is een fout opgetreden: " + e;
                log.error("Error saving server", e);
                prepareMethod(dynaForm, request, EDIT, LIST);
                addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
                return getAlternateForward(mapping, request);
            }
        }
        
        populateServerObject(dynaForm, serviceProvider);
        Session sess = getHibernateSession();
        sess.saveOrUpdate(serviceProvider);
        sess.flush();
        
        dynaForm.set("serverid", "");
        dynaForm.set("serviceProviderGivenName", "");
        dynaForm.set("serviceProviderUrl", "");
        dynaForm.set("serviceProviderUpdatedDate", "");
        dynaForm.set("serviceProviderReviewed", "");
        
        return super.save(mapping,dynaForm,request,response);
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