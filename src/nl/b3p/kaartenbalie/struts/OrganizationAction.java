/*
 * OrganizationAction.java
 *
 * Created on 2 oktober 2006, 13:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.validator.DynaValidatorForm;
import org.hibernate.Session;

/**
 *
 * @author Nando De Goeij
 */
public class OrganizationAction extends KaartenbalieCrudAction {
    
    /* forward name="success" path="" */
    private final static String SUCCESS = "success";
    
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer id = FormUtils.StringToInteger(dynaForm.getString("id"));
        Organization organization = this.getOrganization(dynaForm, request, false, id);
        
        if (null != organization) {
            this.populateOrganizationForm(organization, dynaForm, request);
        }
        return super.unspecified(mapping, dynaForm, request, response);
    }
    
    /*
     * Returns the organization with a specified id.
     */
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
    
    /*
     * If a organization with a specified id is chosen this method will fill the JSP form with the dat of this organization.
     */
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
        Set layers = new HashSet();
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
        
        return super.save(mapping,dynaForm,request,response);
    }
    
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
}