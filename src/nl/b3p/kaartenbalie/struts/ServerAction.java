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

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.kaartenbalie.core.server.SRS;
import nl.b3p.kaartenbalie.core.server.ServiceProvider;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.validator.DynaValidatorForm;
import org.hibernate.Session;

public class ServerAction extends KaartenbalieCrudAction {
    
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
    // <editor-fold defaultstate="collapsed" desc="execute(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer id = FormUtils.StringToInteger(dynaForm.getString("id"));
        ServiceProvider serviceProvider = this.getServiceProvider(dynaForm, request, false, id);
        
        if (null != serviceProvider) {
            this.populateServiceProviderForm(serviceProvider, dynaForm, request);
        }
        return super.unspecified(mapping, dynaForm, request, response);
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
    // <editor-fold defaultstate="collapsed" desc="getServiceProvider(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew, Integer id) method.">
    protected ServiceProvider getServiceProvider(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew, Integer id) {
        Session session = getHibernateSession();
        ServiceProvider serviceProvider = null;
        
        if(null == id && createNew) {
            serviceProvider = new ServiceProvider();
        } else if (null != id) {
            serviceProvider = (ServiceProvider)session.load(ServiceProvider.class, new Integer(id));
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
    // <editor-fold defaultstate="collapsed" desc="populateOrganizationForm(ServiceProvider serviceProvider, DynaValidatorForm dynaForm, HttpServletRequest request) method.">
    private void populateServiceProviderForm(ServiceProvider serviceProvider, DynaValidatorForm dynaForm, HttpServletRequest request) {
        dynaForm.set("id", serviceProvider.getId().toString());
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
    // <editor-fold defaultstate="collapsed" desc="createLists(DynaValidatorForm form, HttpServletRequest request) method.">
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
    // <editor-fold defaultstate="collapsed" desc="populateServerObject(DynaValidatorForm dynaForm, ServiceProvider serviceProvider) method.">
    protected void populateServerObject(DynaValidatorForm dynaForm, ServiceProvider serviceProvider) {
        serviceProvider.setGivenName(FormUtils.nullIfEmpty(dynaForm.getString("serviceProviderGivenName")));
        //serviceProvider.setUrl(dynaForm.getString("serviceProviderUrl"));
        serviceProvider.setUpdatedDate(new Date());
        serviceProvider.setReviewed(false);
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
    // <editor-fold defaultstate="collapsed" desc="save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
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
        ServiceProvider serviceProvider = getServiceProvider(dynaForm,request,true, id);
                
        if (null == serviceProvider) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        WMSCapabilitiesReader wms = new WMSCapabilitiesReader(serviceProvider);
        String url = dynaForm.getString("serviceProviderUrl");
        serviceProvider = wms.getProvider(url);
        //serviceProvider.setGivenName("TestName");
        //serviceProvider.setUrl("http://columbo.nrlssc.navy.mil/ogcwms/servlet/WMSServlet/Newport_Beach_CA_Maps.wms?SERVICE=WMS&REQUEST=GetCapabilities");
        //serviceProvider.setUpdatedDate(new Date());
        //serviceProvider.setReviewed(false); 
        //String givenName = dynaForm.getString("serviceProviderGivenName");
        //
        
        populateServerObject(dynaForm, serviceProvider);
//        SRS slechteSRS = null;
//        for (Iterator it = SRS.srsen.iterator(); it.hasNext();) {
//            SRS elem = (SRS) it.next();
//            if(elem.getLayer() == null) {
//                slechteSRS = elem;
//                break;
//            }
//        }
//        LogFactory.getLog(ServerAction.class).info(slechteSRS);
        //store in db
        sess.saveOrUpdate(serviceProvider);
        sess.flush();
        
        dynaForm.set("id", "");
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
    // <editor-fold defaultstate="collapsed" desc="delete(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
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
            //validate and check for errors
            ActionErrors errors = dynaForm.validate(mapping, request);
            if(!errors.isEmpty()) {
                addMessages(request, errors);
                prepareMethod(dynaForm, request, EDIT, LIST);
                addAlternateMessage(mapping, request, VALIDATION_ERROR_KEY);
                return getAlternateForward(mapping, request);
            }
            
            Integer id = Integer.parseInt(serviceProviderSelected[i]);
            ServiceProvider serviceProvider = getServiceProvider(dynaForm,request,true, id);
            
            if (null == serviceProvider) {
                prepareMethod(dynaForm, request, LIST, EDIT);
                addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
                return getAlternateForward(mapping, request);
            }

            populateServerObject(dynaForm, serviceProvider);
            //store in db
            sess.delete(serviceProvider);
            sess.flush();
        }
        return super.delete(mapping, dynaForm, request, response);
    }
    // </editor-fold>
}