/*
 * B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
 * for authentication/authorization, pricing and usage reporting.
 *
 * Copyright 2006, 2007, 2008 B3Partners BV
 * 
 * This file is part of B3P Kaartenbalie.
 * 
 * B3P Kaartenbalie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * B3P Kaartenbalie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.b3p.kaartenbalie.struts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.accounting.entity.Account;
import nl.b3p.kaartenbalie.service.LayerValidator;
import nl.b3p.kaartenbalie.service.ServiceProviderValidator;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.wfs.v110.WfsLayer;
import nl.b3p.ogc.wfs.v110.WfsServiceProvider;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.ServiceProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;
import org.hibernate.HibernateException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Jytte
 */
public class OrganizationAction extends KaartenbalieCrudAction {

    private final static String SUCCESS = "success";
    private static final Log log = LogFactory.getLog(OrganizationAction.class);
    protected static final String ORGANIZATION_LINKED_ERROR_KEY = "error.organizationstilllinked";
    protected static final String CAPABILITY_WARNING_KEY = "warning.saveorganization";
    protected static final String ORG_NOTFOUND_ERROR_KEY = "error.organizationnotfound";
    protected static final String DELETE_ADMIN_ERROR_KEY = "error.deleteadmin";
    protected static final String USER_JOINED_KEY = "beheer.org.user.joined";
    protected static final String CREDITS_JOINED_KEY = "beheer.org.credits.joined";

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
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.createLists(dynaForm, request);
        prepareMethod(dynaForm, request, LIST, LIST);
        addDefaultMessage(mapping, request);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward create(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward af = super.create(mapping, dynaForm, request, response);
        if (isWFS(dynaForm)) {
            dynaForm.set("serverType", OGCConstants.WFS_SERVICE_WFS);
            request.setAttribute("layerList", createWfsTree());
        } else {
            dynaForm.set("serverType", "");
            request.setAttribute("layerList", createTree());
        }
        createLists(dynaForm, request);
        return af;
    }

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
    public ActionForward edit(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Organization organization = getOrganization(dynaForm, request, false);
        if (organization == null) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, ORG_NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        populateOrganizationForm(organization, dynaForm, request);
        return super.edit(mapping, dynaForm, request, response);
    }

    /* Method for saving a new organization from input of a user.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     *
     * @return an Actionforward object.
     *
     * @throws Exception, HibernateException
     */
    public ActionForward save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws HibernateException, Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        if (!isTokenValid(request)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        ActionErrors errors = dynaForm.validate(mapping, request);
        if (!errors.isEmpty()) {
            addMessages(request, errors);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, VALIDATION_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        Organization organization = getOrganization(dynaForm, request, true);
        if (null == organization) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        populateOrganizationObject(dynaForm, organization);

        /**
         * A warning has to be given if the organization has an invalid capability with the
         * selected layers.
         */
        if (isWMS(dynaForm) && !organization.getHasValidGetCapabilities()) {
            addAlternateMessage(mapping, request, null, CAPABILITY_WARNING_KEY);
        }

        if (organization.getId() == null) {
            em.persist(organization);
        } else {
            em.merge(organization);
        }
        em.flush();
        return super.save(mapping, dynaForm, request, response);
    }

    public ActionForward deleteConfirm(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        Organization organization = getOrganization(dynaForm, request, false);
        if (null == organization) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        MessageResources messages = getResources(request);
        Locale locale = getLocale(request);
        String userJoinedMessage = messages.getMessage(locale, USER_JOINED_KEY);
        String creditsJoinedMessage = messages.getMessage(locale, CREDITS_JOINED_KEY);
        StringBuffer strMessage = null;

        Set userList = organization.getUser();
        if (userList != null && !userList.isEmpty()) {
            strMessage = new StringBuffer();
            Iterator it = userList.iterator();
            boolean notFirstUser = false;
            while (it.hasNext()) {
                User u = (User) it.next();
                if (notFirstUser) {
                    strMessage.append(", ");
                } else {
                    strMessage.append(userJoinedMessage);
                    strMessage.append(": ");
                    notFirstUser = true;
                }
                strMessage.append(u.getSurname());
            }
            addAlternateMessage(mapping, request, null, strMessage.toString());
        }
        Account acc = organization.getAccount();
        if (acc != null) {
            BigDecimal cb = acc.getCreditBalance();
            if (cb != null && cb.doubleValue() > 0) {
                cb.setScale(2, RoundingMode.HALF_UP);
                strMessage = new StringBuffer();
                strMessage.append(creditsJoinedMessage);
                strMessage.append(": ");
                strMessage.append(cb.toString());
                addAlternateMessage(mapping, request, null, strMessage.toString());
            }
        }
        User sessionUser = (User) request.getUserPrincipal();
        Organization sessionOrg = null;
        if (sessionUser != null) {
            sessionOrg = sessionUser.getOrganization();
        }
        if (sessionOrg != null && sessionOrg.getId().equals(organization.getId())) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, DELETE_ADMIN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        prepareMethod(dynaForm, request, DELETE, EDIT);
        addDefaultMessage(mapping, request);
        return getDefaultForward(mapping, request);
    }

    /* Method for deleting an organization selected by a user.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     *
     * @return an Actionforward object.
     *
     * @throws Exception, HibernateException
     */
    public ActionForward delete(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws HibernateException, Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        if (!isTokenValid(request)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        Organization organization = getOrganization(dynaForm, request, false);
        if (null == organization) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        User sessionUser = (User) request.getUserPrincipal();
        Organization sessionOrg = null;
        if (sessionUser != null) {
            sessionOrg = sessionUser.getOrganization();
        }
        if (sessionOrg != null && sessionOrg.getId().equals(organization.getId())) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, DELETE_ADMIN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        em.remove(organization);
        em.flush();
        return super.delete(mapping, dynaForm, request, response);
    }

    /* Method which will fill the JSP form with the data of  a given organization.
     *
     * @param organization Organization object from which the information has to be printed.
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     */
    protected void populateOrganizationForm(Organization organization, DynaValidatorForm dynaForm, HttpServletRequest request) throws JSONException {
        dynaForm.set("id", organization.getId().toString());
        dynaForm.set("name", organization.getName());
        dynaForm.set("street", organization.getStreet());
        dynaForm.set("number", organization.getNumber());
        dynaForm.set("addition", organization.getAddition());
        dynaForm.set("postalcode", organization.getPostalcode());
        dynaForm.set("province", organization.getProvince());
        dynaForm.set("country", organization.getCountry());
        dynaForm.set("postbox", organization.getPostbox());
        dynaForm.set("billingAddress", organization.getBillingAddress());
        dynaForm.set("visitorsAddress", organization.getVisitorsAddress());
        dynaForm.set("telephone", organization.getTelephone());
        dynaForm.set("fax", organization.getFax());
        dynaForm.set("bbox", organization.getBbox());
        dynaForm.set("code", organization.getCode());
        if (organization.getAllowAccountingLayers() == true) {
            dynaForm.set("allow", "on");
        } else {
            dynaForm.set("allow", "");
        }

        JSONObject root = null;
        String checkedLayers = "";
        if (isWFS(dynaForm)) {
            Set l = organization.getWfsOrganizationLayer();
            Object[] organizationLayer = l.toArray();

            for (int i = 0; i < organizationLayer.length; i++) {
                if (i < organizationLayer.length - 1) {
                    checkedLayers += ((WfsLayer) organizationLayer[i]).getUniqueName() + ",";
                } else {
                    checkedLayers += ((WfsLayer) organizationLayer[i]).getUniqueName();
                }
            }
            root = this.createWfsTree();
        } else {
            Set l = organization.getOrganizationLayer();
            Object[] organizationLayer = l.toArray();
            for (int i = 0; i < organizationLayer.length; i++) {
                if (i < organizationLayer.length - 1) {
                    checkedLayers += ((Layer) organizationLayer[i]).getUniqueName() + ",";
                } else {
                    checkedLayers += ((Layer) organizationLayer[i]).getUniqueName();
                }
            }
            root = this.createTree();
        }

        request.setAttribute("layerList", root);
        request.setAttribute("checkedLayers", checkedLayers);
    }

    /* Method that fills an organization object with the user input from the forms.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param organization Organization object that to be filled
     * @param layerList List with all the layers
     * @param selectedLayers String array with the selected layers for this organization
     */
    private void populateOrganizationObject(DynaValidatorForm dynaForm, Organization organization) throws Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        organization.setName(FormUtils.nullIfEmpty(dynaForm.getString("name")));
        organization.setStreet(FormUtils.nullIfEmpty(dynaForm.getString("street")));
        organization.setNumber(FormUtils.nullIfEmpty(dynaForm.getString("number")));
        organization.setAddition(FormUtils.nullIfEmpty(dynaForm.getString("addition")));
        organization.setPostalcode(FormUtils.nullIfEmpty(dynaForm.getString("postalcode")));
        organization.setProvince(FormUtils.nullIfEmpty(dynaForm.getString("province")));
        organization.setCountry(FormUtils.nullIfEmpty(dynaForm.getString("country")));
        organization.setPostbox(FormUtils.nullIfEmpty(dynaForm.getString("postbox")));
        organization.setBillingAddress(FormUtils.nullIfEmpty(dynaForm.getString("billingAddress")));
        organization.setVisitorsAddress(FormUtils.nullIfEmpty(dynaForm.getString("visitorsAddress")));
        organization.setTelephone(FormUtils.nullIfEmpty(dynaForm.getString("telephone")));
        organization.setFax(FormUtils.nullIfEmpty(dynaForm.getString("fax")));
        String bbox = FormUtils.nullIfEmpty(dynaForm.getString("bbox"));
        if (bbox != null) {
            String[] boxxvalues = bbox.split(",");
            if (boxxvalues.length != 4) {
                log.error("BBOX wrong size: " + boxxvalues.length);
                throw new Exception(KBConfiguration.BBOX_EXCEPTION + " Usage: minx,miny,maxx,maxy");
            }
            double minx = 0.0, miny = 0.0, maxx = -1.0, maxy = -1.0;
            try {
                minx = Double.parseDouble(boxxvalues[0]);
                miny = Double.parseDouble(boxxvalues[1]);
                maxx = Double.parseDouble(boxxvalues[2]);
                maxy = Double.parseDouble(boxxvalues[3]);
                if (minx > maxx || miny > maxy) {
                    log.error("BBOX values out of range (minx, miny, maxx, maxy): " + minx + ", " + miny + ", " + maxx + ", " + maxy);
                    throw new Exception("BBOX values out of range (minx, miny, maxx, maxy): " + minx + ", " + miny + ", " + maxx + ", " + maxy);
                }
            } catch (Exception e) {
                log.error("BBOX error minx, miny, maxx, maxy: " + minx + ", " + miny + ", " + maxx + ", " + maxy);
                throw new Exception(KBConfiguration.BBOX_EXCEPTION + " Usage: minx,miny,maxx,maxy");
            }
        }
        organization.setBbox(bbox);
        organization.setCode(FormUtils.nullIfEmpty(dynaForm.getString("code")));
        if ("on".equalsIgnoreCase(FormUtils.nullIfEmpty(dynaForm.getString("allow")))) {
            organization.setAllowAccountingLayers(true);
        } else {
            organization.setAllowAccountingLayers(false);
        }
        Set layers = new HashSet();
        Set serviceProviders = new HashSet();
        String[] selectedLayers = (String[]) dynaForm.get("selectedLayers");
        int size = selectedLayers.length;

        if (isWFS(dynaForm)) {
            for (int i = 0; i < size; i++) {
                // nieuwe methode voor maken
                WfsLayer l = getWfsLayerByUniqueName(selectedLayers[i]);
                if (l == null) {
                    continue;
                }
                layers.add(l);
                WfsServiceProvider sp = l.getWfsServiceProvider();
                if (!serviceProviders.contains(sp)) {
                    serviceProviders.add(sp);
                }
            }
            organization.setWfsOrganizationLayer(layers);
        } else {

            for (int i = 0; i < size; i++) {
                Layer l = getLayerByUniqueName(selectedLayers[i]);
                if (l == null) {
                    continue;
                }
                layers.add(l);
                ServiceProvider sp = l.getServiceProvider();
                if (!serviceProviders.contains(sp)) {
                    serviceProviders.add(sp);
                }
            }

            /* There is a possibility that some serviceproviders do not support the same SRS's or image formats.
             * Some might have compatibility some others not. To make sure this wont give any problems, we need to
             * check which formats and srs's are the same. If and only if this complies we can say for sure that
             * the GetCapabilities request which is going to be sent to the client is valid. In all other cases
             * we need to give a warning that the GetCapabilities can have problems when used with certain viewers.
             *
             * In order to give the user the same warning as the supervisor and in order to keep the administration
             * up to date a boolean hasValidGetCapabilities will be set to false if a GetCapabilities is not stictly
             * according to the WMS rules. This will prevent the user from being kept in the dark if something doesn't
             * work properly.
             */
            LayerValidator lv = new LayerValidator(layers);
            ServiceProviderValidator spv = new ServiceProviderValidator(serviceProviders);
            organization.setHasValidGetCapabilities(lv.validate() && spv.validate());
            organization.setOrganizationLayer(layers);
        }
    }

    /* Creates a list with the available layers.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     *
     * @throws HibernateException, JSONException, Exception
     */
    protected void createLists(DynaValidatorForm form, HttpServletRequest request) throws HibernateException, JSONException, Exception {
        super.createLists(form, request);
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        List organizationlist = em.createQuery("from Organization").getResultList();
        request.setAttribute("organizationlist", organizationlist);
    }

    /* Method which returns the organization with a specified id.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param createNew A boolean which indicates if a new object has to be created.
     * @param id An Integer indicating which organization id has to be searched for.
     *
     * @return an Organization object.
     */
    protected Organization getOrganization(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew) throws Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        Organization organization = null;
        Integer id = getID(dynaForm);

        if (null == id && createNew) {
            organization = new Organization();
        } else if (null != id) {
            organization = (Organization) em.find(Organization.class, id);
        }
        return organization;
    }

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
    private Integer getID(DynaValidatorForm dynaForm) {
        return FormUtils.StringToInteger(dynaForm.getString("id"));
    }

    private String getServerType(DynaValidatorForm dynaForm) {
        return FormUtils.nullIfEmpty(dynaForm.getString("serverType"));
    }

    private boolean isWMS(DynaValidatorForm dynaForm) {
        String wms = getServerType(dynaForm);
        if (wms == null || OGCConstants.WMS_SERVICE_WMS.equalsIgnoreCase(wms)) {
            return true;
        }
        return false;
    }

    private boolean isWFS(DynaValidatorForm dynaForm) {
        String wfs = getServerType(dynaForm);
        if (wfs != null && OGCConstants.WFS_SERVICE_WFS.equalsIgnoreCase(wfs)) {
            return true;
        }
        return false;
    }

}
