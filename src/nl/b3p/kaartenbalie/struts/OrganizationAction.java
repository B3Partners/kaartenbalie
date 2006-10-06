/*
 * OrganizationAction.java
 *
 * Created on 2 oktober 2006, 13:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.struts;

import java.util.HashSet;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.kaartenbalie.core.Organization;

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
            organization = (Organization)session.load(Organization.class, new Long(id));
        }
        return organization;
    }
    
    /*
     * If a organization with a specified id is chosen this method will fill the JSP form with the dat of this organization.
     */
    private void populateOrganizationForm(Organization organization, DynaValidatorForm dynaForm, HttpServletRequest request) {
        dynaForm.set("name", organization.getName());
        dynaForm.set("organizationStreet", organization.getOrganizationStreet());
        dynaForm.set("organizationNumber", organization.getOrganizationNumber());
        dynaForm.set("organizationAddition", organization.getOrganizationAddition());
        dynaForm.set("organizationProvince", organization.getOrganizationProvince());
        dynaForm.set("organizationPostbox", organization.getOrganizationPostbox());
        dynaForm.set("organizationBillingAddress", organization.getOrganizationBillingAddress());
        dynaForm.set("organizationVisitorsAddress", organization.getOrganizationVisitorsAddress());
        dynaForm.set("organizationTelephone", organization.getOrganizationTelephone());
        dynaForm.set("organizationFax", organization.getOrganizationFax()); 
    }
    
    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        super.createLists(form, request);
        
        List organizationlist = getHibernateSession().createQuery("from Organization").list();
        request.setAttribute("organizationlist", organizationlist);        
    }
    
    private void populateOrganizationObject(DynaValidatorForm dynaForm, Organization organization) {
        organization.setName(FormUtils.nullIfEmpty(dynaForm.getString("name")));
        organization.setOrganizationStreet(FormUtils.nullIfEmpty(dynaForm.getString("organizationStreet")));
        organization.setOrganizationNumber(FormUtils.nullIfEmpty(dynaForm.getString("organizationNumber")));
        organization.setOrganizationAddition(FormUtils.nullIfEmpty(dynaForm.getString("organizationAddition")));
        organization.setOrganizationProvince(FormUtils.nullIfEmpty(dynaForm.getString("organizationProvince")));
        organization.setOrganizationPostbox(FormUtils.nullIfEmpty(dynaForm.getString("organizationPostbox")));
        organization.setOrganizationBillingAddress(FormUtils.nullIfEmpty(dynaForm.getString("organizationBillingAddress")));
        organization.setOrganizationVisitorsAddress(FormUtils.nullIfEmpty(dynaForm.getString("organizationVisitorsAddress")));
        organization.setOrganizationTelephone(FormUtils.nullIfEmpty(dynaForm.getString("organizationTelephone")));
        organization.setOrganizationFax(FormUtils.nullIfEmpty(dynaForm.getString("organizationFax")));
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
        Organization organization = getOrganization(dynaForm,request,true, id);
        if (null == organization) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        populateOrganizationObject(dynaForm, organization);
        //store in db
        sess.saveOrUpdate(organization);
        sess.flush();
        
        dynaForm.set("id", "");
        dynaForm.set("name", "");
        dynaForm.set("organizationStreet", "");
        dynaForm.set("organizationNumber", "");
        dynaForm.set("organizationAddition", "");
        dynaForm.set("organizationProvince", "");
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
            Organization organization = getOrganization(dynaForm,request,true, id);
            
            if (null == organization) {
                prepareMethod(dynaForm, request, LIST, EDIT);
                addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
                return getAlternateForward(mapping, request);
            }

            populateOrganizationObject(dynaForm, organization);
            //store in db
            sess.delete(organization);
            sess.flush();
        }
        return super.delete(mapping, dynaForm, request, response);
    }
}