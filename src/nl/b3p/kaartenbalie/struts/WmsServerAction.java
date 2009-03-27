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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.kaartenbalie.core.server.accounting.LayerCalculator;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPricing;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.wms.capabilities.ServiceProvider;
import nl.b3p.wms.capabilities.WMSCapabilitiesReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;
import org.xml.sax.SAXException;

public class WmsServerAction extends ServerAction {

    private static final Log log = LogFactory.getLog(WmsServerAction.class);

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
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        /*
         * Change DataWarehousing mode to performance as this is a very complicated process which will
         * otherwise consume a lot of time.
         */
        if (!isTokenValid(request)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        ActionErrors errors = dynaForm.validate(mapping, request);
        if (!errors.isEmpty()) {
            super.addMessages(request, errors);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, VALIDATION_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }

        String url = FormUtils.nullIfEmpty(dynaForm.getString("url"));
        if (url==null) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, MALFORMED_URL_ERRORKEY);
            return getAlternateForward(mapping, request);
        }
        /*
         * First we need to check if the given url is realy an url.
         */
        try {
            URL tempurl = new URL(url.trim());
        } catch (MalformedURLException mue) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, MALFORMED_URL_ERRORKEY);
            return getAlternateForward(mapping, request);
        }
        try {
            url = checkWmsUrl(url.trim());
        } catch (Exception e) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, null, e.getMessage());
            return getAlternateForward(mapping, request);
        }
        ServiceProvider newServiceProvider = null;
        ServiceProvider oldServiceProvider = getServiceProvider(dynaForm, request, false);
        WMSCapabilitiesReader wms = new WMSCapabilitiesReader();

        if (!isAbbrUnique(oldServiceProvider, dynaForm, em)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, NON_UNIQUE_ABBREVIATION_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        String abbreviation = FormUtils.nullIfEmpty(dynaForm.getString("abbr"));
        if (!isAlphaNumeric(abbreviation)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, NON_ALPHANUMERIC_ABBREVIATION_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        if (abbreviation.equalsIgnoreCase(KBConfiguration.SERVICEPROVIDER_BASE_ABBR)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, ABBR_RESERVED_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
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
            log.error("Error saving server", e);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, SERVER_CONNECTION_ERRORKEY);
            return getAlternateForward(mapping, request);
        } catch (SAXException e) {
            log.error("Error saving server", e);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, MALFORMED_CAPABILITY_ERRORKEY);
            return getAlternateForward(mapping, request);
        } catch (Exception e) {
            log.error("Error saving server", e);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, null, e.getMessage());
            return getAlternateForward(mapping, request);
        }
        if (!newServiceProvider.getWmsVersion().equalsIgnoreCase(OGCConstants.WMS_VERSION_111)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, UNSUPPORTED_WMSVERSION_ERRORKEY);
            return getAlternateForward(mapping, request);
        }
        populateServerObject(dynaForm, newServiceProvider);
        // haal set op om vulling van set af te dwingen
        Set testSet = newServiceProvider.getAllLayers();
        em.persist(newServiceProvider);
        em.flush();
        Iterator dwIter = testSet.iterator();
        while (dwIter.hasNext()) {
            Layer layer = (Layer) dwIter.next();
        }
        /*
         * All tests have been completed succesfully.
         * We have now a newServiceProvider object and all we need to check
         * is if this serviceprovider has been changed or newly added. The
         * easiest way of doing so, is by checking the id.
         */
        if (oldServiceProvider != null) {
            /* Then we need to call for a list with organizations.
             * We walk through this list and for each organization in the
             * list we need to check if this organization has connections
             * with the old serviceprovider.
             */
            List orgList = em.createQuery("from Organization").getResultList();
            Iterator orgit = orgList.iterator();
            while (orgit.hasNext()) {
                Set newOrganizationLayer = new HashSet();
                Organization org = (Organization) orgit.next();
                Set orgLayers = org.getLayers();
                Iterator layerit = orgLayers.iterator();
                while (layerit.hasNext()) {
                    Layer organizationLayer = (Layer) layerit.next();
                    ServiceProvider orgLayerServiceProvider = organizationLayer.getServiceProvider();
                    if (orgLayerServiceProvider.getId() == oldServiceProvider.getId()) {
                        Set topLayerSet = new HashSet();
                        topLayerSet.add(newServiceProvider.getTopLayer());
                        Layer newLayer = checkLayer(organizationLayer, topLayerSet);
                        if (newLayer != null) {
                            newOrganizationLayer.add(newLayer);
                        }
                    } else {
                        newOrganizationLayer.add(organizationLayer);
                    }
                }
                //vervang de oude set met layers in de organisatie voor de nieuwe set
                org.setLayers(newOrganizationLayer);
                em.flush();
            }
            try {
                Set oldLayers = oldServiceProvider.getAllLayers();
                Iterator oldLayersIter = oldLayers.iterator();
                while (oldLayersIter.hasNext()) {
                    Layer oldLayer = (Layer) oldLayersIter.next();
                }
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
    public ActionForward deleteConfirm(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        ServiceProvider serviceProvider = getServiceProvider(dynaForm, request, false);
        if (null == serviceProvider) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        prepareMethod(dynaForm, request, DELETE, EDIT);
        Layer serviceProviderTopLayer = serviceProvider.getTopLayer();
        if (serviceProviderTopLayer != null) {
            //Check if layers are bound to organizations
            MessageResources messages = getResources(request);
            Locale locale = getLocale(request);
            String orgJoinedMessage = messages.getMessage(locale, ORG_JOINED_KEY);
            String layerJoinedMessage = messages.getMessage(locale, LAYER_JOINED_KEY);
            StringBuffer strMessage = new StringBuffer();
            List orgList = em.createQuery("from Organization").getResultList();
            Iterator orgit = orgList.iterator();
            boolean notFirstOrg = false;
            while (orgit.hasNext()) {
                Organization org = (Organization) orgit.next();
                Set orgLayers = org.getLayers();
                Iterator orgLayerIterator = orgLayers.iterator();
                boolean notFirstLayer = false;
                while (orgLayerIterator.hasNext()) {
                    Layer organizationLayer = (Layer) orgLayerIterator.next();
                    Layer organizationLayerTopLayer = organizationLayer.getTopLayer();
                    if (organizationLayerTopLayer != null &&
                            organizationLayerTopLayer.getId() == serviceProviderTopLayer.getId()) {
                        if (notFirstLayer) {
                            strMessage.append(", ");
                        } else {
                            if (notFirstOrg) {
                                strMessage.append(", ");
                            } else {
                                strMessage.append(orgJoinedMessage);
                                strMessage.append(": ");
                                notFirstOrg = true;
                            }
                            strMessage.append(org.getName());
                            strMessage.append(" [");
                            strMessage.append(layerJoinedMessage);
                            strMessage.append(": ");
                            notFirstLayer = true;
                        }
                        strMessage.append(organizationLayer.getName());
                    }
                }
                if (notFirstLayer) {
                    strMessage.append("]");
                }
            }
            if (strMessage.length()>0)
                addAlternateMessage(mapping, request, null, strMessage.toString());
            //Check if current pricing is bound to this provider
            List lpList = null;
            LayerCalculator lc = new LayerCalculator();
            try {
                lpList = lc.getSpLayerPricingList(serviceProvider.getAbbr(), new Date(), OGCConstants.WMS_SERVICE_WMS);
            } finally {
                lc.closeEntityManager();
            }
            if (lpList != null) {
                Iterator lpit = lpList.iterator();
                strMessage = new StringBuffer();
                String pricingJoinedMessage = messages.getMessage(locale, PRICING_JOINED_KEY);
                boolean notFirstPrice = false;
                while (lpit.hasNext()) {
                    LayerPricing lp = (LayerPricing) lpit.next();
                    String ln = lp.getLayerName(); // unieke naam
                    if (strMessage.indexOf(ln) == -1) {
                        if (notFirstPrice) {
                            strMessage.append(", ");
                        } else {
                            strMessage.append(pricingJoinedMessage);
                            strMessage.append(": ");
                            notFirstPrice = true;
                        }
                        strMessage.append(ln);
                    }
                }
                if (strMessage.length()>0)
                    addAlternateMessage(mapping, request, null, strMessage.toString());
            }
        }
        addDefaultMessage(mapping, request);
        return getDefaultForward(mapping, request);
    }

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
    public ActionForward delete(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        /*
         * Change DataWarehousing mode to performance as this is a very complicated process which will
         * otherwise consume a lot of time.
         */
        if (!isTokenValid(request)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        ServiceProvider serviceProvider = getServiceProvider(dynaForm, request, false);
        if (null == serviceProvider) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        Layer serviceProviderTopLayer = serviceProvider.getTopLayer();
        if (serviceProviderTopLayer != null) {
            List orgList = em.createQuery("from Organization").getResultList();
            Iterator orgit = orgList.iterator();
            while (orgit.hasNext()) {
                Organization org = (Organization) orgit.next();
                Set orgLayers = org.getLayers();
                HashSet clonedOrgLayers = new HashSet();
                clonedOrgLayers.addAll(orgLayers);
                Iterator orgLayerIterator = orgLayers.iterator();
                while (orgLayerIterator.hasNext()) {
                    Layer organizationLayer = (Layer) orgLayerIterator.next();
                    Layer organizationLayerTopLayer = organizationLayer.getTopLayer();
                    if (organizationLayerTopLayer != null &&
                            organizationLayerTopLayer.getId() == serviceProviderTopLayer.getId()) {
                        clonedOrgLayers.remove(organizationLayer);
                    }
                }
                if (orgLayers.size() != clonedOrgLayers.size()) {
                    org.setLayers(clonedOrgLayers);
                    em.merge(org);
                }
            }
        }
        em.remove(serviceProvider);
        em.flush();
        return super.delete(mapping, dynaForm, request, response);
    }

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
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        // Only shows WMS servers for now
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
    protected ServiceProvider getServiceProvider(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew) throws Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        ServiceProvider serviceProvider = null;
        Integer id = getID(dynaForm);
        if (null == id && createNew) {
            serviceProvider = new ServiceProvider();
        } else if (null != id) {
            serviceProvider = (ServiceProvider) em.find(ServiceProvider.class, new Integer(id.intValue()));
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
        serviceProvider.setUpdatedDate(new Date());
        serviceProvider.setAbbr(dynaForm.getString("abbr"));
    }
    // </editor-fold>
    //-------------------------------------------------------------------------------------------------------
    // PRIVATE METHODS
    //-------------------------------------------------------------------------------------------------------
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
        dynaForm.set("abbr", serviceProvider.getAbbr());
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
        if (layers == null || layers.isEmpty()) {
            return null;
        }
        Iterator it = layers.iterator();
        while (it.hasNext()) {
            Layer layer = (Layer) it.next();
            if (orgLayer.getName() == null && layer.getName() == null &&
                    orgLayer.getTitle().equalsIgnoreCase(layer.getTitle())) {
                return layer;
            }
            if (orgLayer.getName() != null && layer.getName() != null &&
                    orgLayer.getName().equalsIgnoreCase(layer.getName())) {
                return layer;
            }
            Layer foundLayer = checkLayer(orgLayer, layer.getLayers());
            if (foundLayer != null) {
                return foundLayer;
            }
        }
        return null;
    }
    // </editor-fold>
    protected String checkWmsUrl(String url) throws Exception {
        OGCRequest ogcrequest = new OGCRequest(url);
        if (ogcrequest.containsParameter(OGCConstants.WMS_REQUEST) &&
                !OGCConstants.WMS_REQUEST_GetCapabilities.equalsIgnoreCase(ogcrequest.getParameter(OGCConstants.WMS_REQUEST))) {
            log.error(KBConfiguration.UNSUPPORTED_REQUEST);
            throw new Exception(KBConfiguration.UNSUPPORTED_REQUEST);
        } else {
            ogcrequest.addOrReplaceParameter(OGCConstants.WMS_REQUEST, OGCConstants.WMS_REQUEST_GetCapabilities);
        }
        if (ogcrequest.containsParameter(OGCConstants.WMS_SERVICE) &&
                !OGCConstants.WMS_SERVICE_WMS.equalsIgnoreCase(ogcrequest.getParameter(OGCConstants.WMS_SERVICE))) {
            log.error(KBConfiguration.UNSUPPORTED_SERVICE);
            throw new Exception(KBConfiguration.UNSUPPORTED_SERVICE);
        } else {
            ogcrequest.addOrReplaceParameter(OGCConstants.WMS_SERVICE, OGCConstants.WMS_SERVICE_WMS);
        }
        if (ogcrequest.containsParameter(OGCConstants.WMS_VERSION) &&
                !OGCConstants.WMS_VERSION_111.equalsIgnoreCase(ogcrequest.getParameter(OGCConstants.WMS_VERSION))) {
            log.error(KBConfiguration.UNSUPPORTED_VERSION);
            throw new Exception(KBConfiguration.UNSUPPORTED_VERSION);
        } else {
            ogcrequest.addOrReplaceParameter(OGCConstants.WMS_VERSION, OGCConstants.WMS_VERSION_111);
        }
        return ogcrequest.getUrl();
    }

    protected boolean isAbbrUnique(ServiceProvider sp, DynaValidatorForm dynaForm, EntityManager em) {
        try {
            ServiceProvider dbSp = (ServiceProvider) em.createQuery(
                    "from ServiceProvider sp where " +
                    "lower(sp.abbr) = lower(:abbr) ").setParameter("abbr", FormUtils.nullIfEmpty(dynaForm.getString("abbr"))).getSingleResult();
            if (dbSp != null) {
                if (sp != null) {
                    if (dbSp.getId().equals(sp.getId())) {
                        return true;
                    }
                }
            }
            return false;
        } catch (NoResultException nre) {
            return true;
        }
    }
}