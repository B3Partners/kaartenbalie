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

import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.wms.capabilities.ServiceProvider;
import nl.b3p.kaartenbalie.service.LayerValidator;
import nl.b3p.kaartenbalie.service.ServiceProviderValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.validator.DynaValidatorForm;
import org.hibernate.HibernateException;
import org.json.JSONException;
import org.json.JSONObject;

public class WmsOrganizationAction extends OrganizationAction {

    private static final Log log = LogFactory.getLog(WmsOrganizationAction.class);

    public ActionForward create(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward af = super.create(mapping, dynaForm, request, response);
        dynaForm.set("serverType", "wms");
        createLists(dynaForm, request);
        request.setAttribute("layerList", createTree());
        return af;
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

        /*
         * A warning has to be given if the organization has an invalid capability with the
         * selected layers.
         */
        if (!organization.getHasValidGetCapabilities()) {
            addAlternateMessage(mapping, request, null, CAPABILITY_WARNING_KEY);
        }
        if (organization.getId() == null) {
            em.persist(organization);
        } else {
            em.merge(organization);
        }
        em.flush();
//TODO        getDataWarehousing().enlist(Organization.class, organization.getId(), DwObjectAction.PERSIST_OR_MERGE);
        return super.save(mapping, dynaForm, request, response);
    }

    /* Method which will fill the JSP form with the data of  a given organization.
     *
     * @param organization Organization object from which the information has to be printed.
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     */
// <editor-fold defaultstate="" desc="populateOrganizationForm(Organization organization, DynaValidatorForm dynaForm, HttpServletRequest request) method.">
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
        Set l = organization.getOrganizationLayer();
        Object[] organizationLayer = l.toArray();
        String checkedLayers = "";
        for (int i = 0; i < organizationLayer.length; i++) {
            if (i < organizationLayer.length - 1) {
                checkedLayers += ((Layer) organizationLayer[i]).getUniqueName() + ",";
            } else {
                checkedLayers += ((Layer) organizationLayer[i]).getUniqueName();
            }
        }
        JSONObject root = this.createTree();
        request.setAttribute("layerList", root);
        request.setAttribute("checkedLayers", checkedLayers);
    }
// </editor-fold>

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
