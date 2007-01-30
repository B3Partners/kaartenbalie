/**
 * @(#)OrganizationAction.java
 * @author N. de Goeij
 * @version 1.00 2006/10/02
 *
 * Purpose: a Struts action class defining all the Action for the Organization view.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.struts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.kaartenbalie.core.server.Layer;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.service.MyDatabase;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.validator.DynaValidatorForm;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class OrganizationAction extends KaartenbalieCrudAction {
    
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
        Organization organization = this.getOrganization(dynaForm, request, false, id);
        
        if (null != organization) {
            Set l = organization.getOrganizationLayer();
            
            Object [] organizationLayer = l.toArray();            
            String [] layerid = new String[l.size()];
            
            for (int i = 0; i < organizationLayer.length; i++) {
                layerid [i] = ((Layer)organizationLayer[i]).getId().toString();
            }
            
            dynaForm.set("layerSelected", layerid);
            this.populateOrganizationForm(organization, dynaForm, request);
        }
        return super.unspecified(mapping, dynaForm, request, response);
    }
    // </editor-fold>
    
    /** Method which returns the organization with a specified id.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param createNew A boolean which indicates if a new object has to be created.
     * @param id An Integer indicating which organization id has to be searched for.
     *
     * @return an Organization object.
     */
    // <editor-fold defaultstate="collapsed" desc="getOrganization(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew, Integer id) method.">
    private Organization getOrganization(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew, Integer id) {
        Session session = getHibernateSession();
        Organization organization = null;
        
        if(null == id && createNew) {
            organization = new Organization();
        } else if (null != id) {
            try {
                organization = (Organization)session.createQuery(
                        "from Organization o where " +
                        "lower(o.id) = lower(:id) ").setParameter("id", id).uniqueResult();
            } catch(Exception e){}
            //organization = (Organization)session.load(Organization.class, new Integer(id));
        }
        return organization;
    }
    // </editor-fold>
    
    /** Method which will fill the JSP form with the data of  a given organization.
     *
     * @param organization Organization object from which the information has to be printed.
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     */
    // <editor-fold defaultstate="collapsed" desc="populateOrganizationForm(Organization organization, DynaValidatorForm dynaForm, HttpServletRequest request) method.">
    private void populateOrganizationForm(Organization organization, DynaValidatorForm dynaForm, HttpServletRequest request) {
        dynaForm.set("name", organization.getName());
        dynaForm.set("organizationStreet", organization.getStreet());
        dynaForm.set("organizationNumber", organization.getNumber());
        dynaForm.set("organizationAddition", organization.getAddition());
        dynaForm.set("organizationProvince", organization.getProvince());
        dynaForm.set("organizationCountry", organization.getCountry());
        dynaForm.set("organizationPostbox", organization.getPostbox());
        dynaForm.set("organizationBillingAddress", organization.getBillingAddress());
        dynaForm.set("organizationVisitorsAddress", organization.getVisitorsAddress());
        dynaForm.set("organizationTelephone", organization.getTelephone());
        dynaForm.set("organizationFax", organization.getFax()); 
    }
    // </editor-fold>
    
    /** Creates a list of all the organizations in the database.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="collapsed" desc="createLists(DynaValidatorForm form, HttpServletRequest request) method.">
    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        super.createLists(form, request);
        
        List organizationlist = getHibernateSession().createQuery("from Organization").list();
        request.setAttribute("organizationlist", organizationlist);
        
        List layerlist = getHibernateSession().createQuery(
                "from Layer l left join fetch l.latLonBoundingBox left join fetch l.attribution").list();
        HttpSession session = request.getSession();
        session.setAttribute("layerlist", layerlist);
        //request.setAttribute();
    }
    // </editor-fold>
    
    /** Method that fills an organization object with the user input from the forms.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param organization Organization object that to be filled
     * @param layerList List with all the layers
     * @param layerSelected String array with the selected layers for this organization
     */
    // <editor-fold defaultstate="collapsed" desc="populateOrganizationObject(DynaValidatorForm dynaForm, Organization organization, List layerList, String [] layerSelected) method.">
    private void populateOrganizationObject(DynaValidatorForm dynaForm, Organization organization, List layerList, String [] layerSelected) {
        organization.setName(FormUtils.nullIfEmpty(dynaForm.getString("name")));
        organization.setStreet(FormUtils.nullIfEmpty(dynaForm.getString("organizationStreet")));
        organization.setNumber(FormUtils.nullIfEmpty(dynaForm.getString("organizationNumber")));
        organization.setAddition(FormUtils.nullIfEmpty(dynaForm.getString("organizationAddition")));
        organization.setProvince(FormUtils.nullIfEmpty(dynaForm.getString("organizationProvince")));
        organization.setCountry(FormUtils.nullIfEmpty(dynaForm.getString("organizationCountry")));
        organization.setPostbox(FormUtils.nullIfEmpty(dynaForm.getString("organizationPostbox")));
        organization.setBillingAddress(FormUtils.nullIfEmpty(dynaForm.getString("organizationBillingAddress")));
        organization.setVisitorsAddress(FormUtils.nullIfEmpty(dynaForm.getString("organizationVisitorsAddress")));
        organization.setTelephone(FormUtils.nullIfEmpty(dynaForm.getString("organizationTelephone")));
        organization.setFax(FormUtils.nullIfEmpty(dynaForm.getString("organizationFax")));
        
        int size = layerSelected.length;
        Set <Layer> layers = new HashSet <Layer>();
        for(int i = 0; i < size; i++) {
            int select = Integer.parseInt(layerSelected[i]);
            Iterator it = layerList.iterator();
            while (it.hasNext()) {
                Layer layer = (Layer)it.next();
                if (layer.getId() == select) {
                    layers.add(layer);///layerList.get(select));
                    break;
                }
                    
            }
        }
        organization.setOrganizationLayer(layers);
    }
    // </editor-fold>
    
    /** Method for saving a new organization from input of a user.
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
        String [] layerSelected = dynaForm.getStrings("layerSelected");
        HttpSession session = request.getSession();
        //sess.setAttribute("layerlist", layerlist);
        List layerList = (List)session.getAttribute("layerlist");        
        
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
        Organization organization = getOrganization(dynaForm, request, true, id);
        if (null == organization) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        populateOrganizationObject(dynaForm, organization, layerList, layerSelected);
        //store in db
        sess.saveOrUpdate(organization);
        sess.flush();
        
        dynaForm.set("id", "");
        dynaForm.set("name", "");
        dynaForm.set("organizationStreet", "");
        dynaForm.set("organizationNumber", "");
        dynaForm.set("organizationAddition", "");
        dynaForm.set("organizationProvince", "");
        dynaForm.set("organizationCountry", "");
        dynaForm.set("organizationPostbox", "");
        dynaForm.set("organizationBillingAddress", "");
        dynaForm.set("organizationVisitorsAddress", "");
        dynaForm.set("organizationTelephone", "");
        dynaForm.set("organizationFax", ""); 
        dynaForm.set("layerSelected", null);
        
        return super.save(mapping,dynaForm,request,response);
    }
    // </editor-fold>
    
    /** Method for deleting an organization selected by a user.
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
        String [] organizationSelected = dynaForm.getStrings("organizationSelected");
        int size = organizationSelected.length;
        
        String [] layerSelected = dynaForm.getStrings("layerSelected");
        HttpSession session = request.getSession();
        List layerList = (List)session.getAttribute("layerlist"); 
        
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
            
            Integer id = Integer.parseInt(organizationSelected[i]);
            Organization organization = getOrganization(dynaForm, request, false, id);
            
            if (null == organization) {
                prepareMethod(dynaForm, request, LIST, EDIT);
                addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
                return getAlternateForward(mapping, request);
            }

            populateOrganizationObject(dynaForm, organization, layerList, layerSelected);
            //store in db
            sess.delete(organization);
            sess.flush();
        }
        return super.delete(mapping, dynaForm, request, response);
    }
    // </editor-fold>
}