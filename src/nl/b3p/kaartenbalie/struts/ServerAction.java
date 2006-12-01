/*
 * ServerAction.java
 *
 * Created on 2 oktober 2006, 13:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
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

/**
 *
 * @author Nando De Goeij
 */
public class ServerAction extends KaartenbalieCrudAction {
    
    /* forward name="success" path="" */
    private final static String SUCCESS = "success";
    
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer id = FormUtils.StringToInteger(dynaForm.getString("id"));
        ServiceProvider serviceProvider = this.getServiceProvider(dynaForm, request, false, id);
        
        if (null != serviceProvider) {
            this.populateServiceProviderForm(serviceProvider, dynaForm, request);
        }
        return super.unspecified(mapping, dynaForm, request, response);
    }
    
    /*
     * Returns the server with a specified id.
     */
    private ServiceProvider getServiceProvider(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew, Integer id) {
        Session session = getHibernateSession();
        ServiceProvider serviceProvider = null;
        
        if(null == id && createNew) {
            serviceProvider = new ServiceProvider();
        } else if (null != id) {
            serviceProvider = (ServiceProvider)session.load(ServiceProvider.class, new Integer(id));
        }
        return serviceProvider;
    }
    
    /*
     * If a server with a specified id is chosen this method will fill the JSP form with the dat of this server.
     */
    private void populateServiceProviderForm(ServiceProvider serviceProvider, DynaValidatorForm dynaForm, HttpServletRequest request) {
        dynaForm.set("id", serviceProvider.getId().toString());
        dynaForm.set("serviceProviderGivenName", serviceProvider.getGivenName());
        dynaForm.set("serviceProviderUrl", serviceProvider.getUrl());
        dynaForm.set("serviceProviderUpdatedDate", serviceProvider.getUpdatedDate().toString());
        dynaForm.set("serviceProviderReviewed", "false");
    }
    
    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        super.createLists(form, request);
        
        List serviceproviderlist = getHibernateSession().createQuery("from ServiceProvider").list();
        request.setAttribute("serviceproviderlist", serviceproviderlist);        
    }
    
    private void populateServerObject(DynaValidatorForm dynaForm, ServiceProvider serviceProvider) {
        serviceProvider.setGivenName(FormUtils.nullIfEmpty(dynaForm.getString("serviceProviderGivenName")));
        //serviceProvider.setUrl(dynaForm.getString("serviceProviderUrl"));
        serviceProvider.setUpdatedDate(new Date());
        serviceProvider.setReviewed(false);
    }
    
    //This method has not been implemented yet into the system.
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
}