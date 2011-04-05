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
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.commons.struts.ExtendedMethodProperties;
import nl.b3p.kaartenbalie.core.server.accounting.LayerCalculator;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPricing;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.wfs.v110.WfsCapabilitiesReader;
import nl.b3p.ogc.wfs.v110.WfsLayer;
import nl.b3p.ogc.wfs.v110.WfsServiceProvider;
import nl.b3p.kaartenbalie.core.server.Organization;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;
import org.xml.sax.SAXException;

public class WfsServerAction extends ServerAction {

    private static final Log log = LogFactory.getLog(WfsServerAction.class);
    
    protected static final String MAPPING_TEST = "test";
    protected static final String MAPPING_BATCH_UPDATE = "batchUpdate";
    
    private static final String SERVICE_STATUS_ERROR = "FOUT";
    private static final String SERVICE_STATUS_OK = "GOED";

    private String SERVICE_TEST_FOUT = "beheer.wms.test.fout";
    private String SERVICE_BATCH_UPDATE_FOUT = "beheer.wms.batchupdate.fout";

    @Override
    protected Map getActionMethodPropertiesMap() {
        Map map = super.getActionMethodPropertiesMap();
        ExtendedMethodProperties crudProp = null;

        crudProp = new ExtendedMethodProperties(MAPPING_TEST);
        crudProp.setDefaultForwardName(SUCCESS);
        crudProp.setDefaultMessageKey("beheer.wfs.test.succes");
        crudProp.setAlternateForwardName(FAILURE);
        crudProp.setAlternateMessageKey("beheer.wfs.test.failed");
        map.put(MAPPING_TEST, crudProp);

        crudProp = new ExtendedMethodProperties(MAPPING_BATCH_UPDATE);
        crudProp.setDefaultForwardName(SUCCESS);
        crudProp.setDefaultMessageKey("beheer.wfs.batchupdate.succes");
        crudProp.setAlternateForwardName(FAILURE);
        crudProp.setAlternateMessageKey("beheer.wfs.batchupdate.failed");
        map.put(MAPPING_BATCH_UPDATE, crudProp);

        return map;
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
    // <editor-fold defaultstate="" desc="edit(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    @Override
    public ActionForward edit(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        WfsServiceProvider serviceprovider = getServiceProvider(dynaForm, request, false);
        if (serviceprovider == null) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, SP_NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        populateServiceProviderForm(serviceprovider, dynaForm, request);
        prepareMethod(dynaForm, request, EDIT, LIST);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return getDefaultForward(mapping, request);
    }
    // </editor-fold>
    @Override
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        prepareMethod(dynaForm, request, EDIT, LIST);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return mapping.findForward(SUCCESS);
    }

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
    @Override
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
        // tot hier het zelfde
        WfsServiceProvider newServiceProvider = null;
        WfsServiceProvider oldServiceProvider = getServiceProvider(dynaForm, request, false);
        Integer oldId = null;
        if (oldServiceProvider!=null) {
            oldId = oldServiceProvider.getId();
        }
        WfsCapabilitiesReader wfs = new WfsCapabilitiesReader();

        if (!isAbbrUnique(oldId, dynaForm, em)) {
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
            newServiceProvider = wfs.getProvider(url.trim());
            newServiceProvider.setAbbr(dynaForm.getString("abbr"));
            newServiceProvider.setGivenName(dynaForm.getString("givenName"));
        } catch (IOException e) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, SERVER_CONNECTION_ERRORKEY);
            return getAlternateForward(mapping, request);
        } catch (Exception e) {
            log.error("Error saving server", e);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, null, e.getMessage());
            return getAlternateForward(mapping, request);
        }
        populateServerObject(dynaForm, newServiceProvider);
        // haal set op om vulling van set af te dwingen
        Set testSet = newServiceProvider.getWfsLayers();
        em.persist(newServiceProvider);
        em.flush();
        Iterator dwIter = testSet.iterator();
        while (dwIter.hasNext()) {
            WfsLayer layer = (WfsLayer) dwIter.next();
        }
        /*
         * All tests have been completed succesfully.
         * We have now a newServiceProvider object and all we need to check
         * is if this serviceprovider has been changed or newly added. The
         * easiest way of doing so, is by checking the id.
         */
        try {
            int id = oldServiceProvider.getId();
            /*
             * The serviceProviderId already existed so the old serviceprovide has to removed from the database.
             */
            try {
                List orgList = em.createQuery("from Organization").getResultList();
                Iterator orgit = orgList.iterator();
                while (orgit.hasNext()) {
                    Set newOrganizationLayer = new HashSet();
                    Organization org = (Organization) orgit.next();
                    Set orgLayers = org.getWfsLayers();
                    Iterator layerit = orgLayers.iterator();
                    while (layerit.hasNext()) {
                        WfsLayer organizationLayer = (WfsLayer) layerit.next();
                        WfsServiceProvider orgLayerServiceProvider = organizationLayer.getWfsServiceProvider();
                        if (orgLayerServiceProvider.getId() == oldServiceProvider.getId()) {
                            WfsLayer newLayer = checkLayer(organizationLayer, newServiceProvider.getWfsLayers());
                            if (newLayer != null) {
                                newOrganizationLayer.add(newLayer);
                            }
                        } else {
                            newOrganizationLayer.add(organizationLayer);
                        }
                    }
                    //vervang de oude set met layers in de organisatie voor de nieuwe set
                    org.setWfsLayers(newOrganizationLayer);
                    em.flush();
                }
                Set oldLayers = oldServiceProvider.getWfsLayers();
                Iterator oldLayersIter = oldLayers.iterator();
                while (oldLayersIter.hasNext()) {
                    WfsLayer oldLayer = (WfsLayer) oldLayersIter.next();
                }
                em.remove(oldServiceProvider);
                em.flush();
            } catch (Exception e) {
                log.error("Error deleting the old serviceprovider", e);
                prepareMethod(dynaForm, request, EDIT, LIST);
                addAlternateMessage(mapping, request, null, e.getMessage());
                return getAlternateForward(mapping, request);
            }
        } catch (Exception e) {
            /*
             * If he failed to get a serviceProviderId from the form on the website it means
             * that the user was adding a new server.
             * The new serviceProvide has been saved in the database and nothing has to be
             * done here.
             *
             * It can't be checked in an if-statment, because integers can't be checked if they are null.
             */
        }
        dynaForm.set("id", null);
         prepareMethod(dynaForm, request, LIST, EDIT);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return getDefaultForward(mapping, request);
    }
    // </editor-fold>

    public ActionForward test(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        EntityManager em = getEntityManager();

        String regexp = FormUtils.nullIfEmpty(dynaForm.getString("regexp"));
        String replacement = FormUtils.nullIfEmpty(dynaForm.getString("replacement"));

        int fout = 0;

        try {
            List<WfsServiceProvider> wmsServices = em.createQuery("from WfsServiceProvider")
                    .getResultList();

            for (WfsServiceProvider sp : wmsServices) {
                String newUrl = sp.getUrl();

                if (regexp != null && replacement != null && !regexp.isEmpty()
                        && !replacement.isEmpty()) {
                    newUrl = newUrl.replaceAll(regexp, replacement);
                }

                WfsServiceProvider newSp = getTestServiceProvider(newUrl);

                if (newSp != null) {
                    sp.setStatus(SERVICE_STATUS_OK);
                } else {
                    sp.setStatus(SERVICE_STATUS_ERROR);
                    fout++;
                }
            }

            em.flush();
        } catch (Exception ex) {
            log.error("Er iets iets fout gegaan tijdens het testen van de WFS Services: " + ex);
        }

        dynaForm.initialize(mapping);
        prepareMethod(dynaForm, request, LIST, EDIT);

        if (fout > 0) {
            ActionMessages messages = getMessages(request);
            ActionMessage message = new ActionMessage(SERVICE_TEST_FOUT, fout);

            messages.add(ActionMessages.GLOBAL_MESSAGE, message);
            saveMessages(request, messages);
        }

        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);

        return getDefaultForward(mapping, request);
    }

    public ActionForward batchUpdate(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        EntityManager em = getEntityManager();

        String regexp = FormUtils.nullIfEmpty(dynaForm.getString("regexp"));
        String replacement = FormUtils.nullIfEmpty(dynaForm.getString("replacement"));

        int fout = 0;

        try {
            List<WfsServiceProvider> wmsServices = em.createQuery("from WfsServiceProvider")
                    .getResultList();

            for (WfsServiceProvider oldServiceProvider : wmsServices) {
                String newUrl = oldServiceProvider.getUrl();

                if (regexp != null && replacement != null && !regexp.isEmpty()
                        && !replacement.isEmpty()) {
                    newUrl = newUrl.replaceAll(regexp, replacement);
                }

                WfsServiceProvider newServiceProvider = getTestServiceProvider(newUrl);

                if (newServiceProvider != null) {
                    newServiceProvider.setStatus(SERVICE_STATUS_OK);
                } else {
                    oldServiceProvider.setStatus(SERVICE_STATUS_ERROR);
                    fout++;
                }

                /* indien newServiceProvider ok dan bijwerken */
                if (newServiceProvider != null) {
                    updateServiceProvider(oldServiceProvider, newServiceProvider);
                }
            }

            em.flush();
        } catch (Exception ex) {
            log.error("Er iets iets fout gegaan tijdens de batch update van de WFS Services: " + ex);
        }

        dynaForm.initialize(mapping);
        prepareMethod(dynaForm, request, LIST, EDIT);

        if (fout > 0) {
            ActionMessages messages = getMessages(request);
            ActionMessage message = new ActionMessage(SERVICE_BATCH_UPDATE_FOUT, fout);

            messages.add(ActionMessages.GLOBAL_MESSAGE, message);
            saveMessages(request, messages);
        }

        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);

        return getDefaultForward(mapping, request);
    }

    private void updateServiceProvider(WfsServiceProvider oldServiceProvider,
            WfsServiceProvider newServiceProvider) throws Exception {

        EntityManager em = getEntityManager();

        newServiceProvider.setGivenName(oldServiceProvider.getGivenName());
        newServiceProvider.setUpdatedDate(new Date());
        newServiceProvider.setAbbr(oldServiceProvider.getAbbr());

        Set testSet = newServiceProvider.getWfsLayers();
        em.persist(newServiceProvider);
        em.flush();

        Iterator dwIter = testSet.iterator();
        while (dwIter.hasNext()) {
            WfsLayer layer = (WfsLayer) dwIter.next();
        }

        try {
            List orgList = em.createQuery("from Organization").getResultList();
            Iterator orgit = orgList.iterator();
            while (orgit.hasNext()) {
                Set newOrganizationLayer = new HashSet();
                Organization org = (Organization) orgit.next();
                Set orgLayers = org.getWfsLayers();
                Iterator layerit = orgLayers.iterator();
                while (layerit.hasNext()) {
                    WfsLayer organizationLayer = (WfsLayer) layerit.next();
                    WfsServiceProvider orgLayerServiceProvider = organizationLayer.getWfsServiceProvider();
                    if (orgLayerServiceProvider.getId() == oldServiceProvider.getId()) {
                        WfsLayer newLayer = checkLayer(organizationLayer, newServiceProvider.getWfsLayers());
                        if (newLayer != null) {
                            newOrganizationLayer.add(newLayer);
                        }
                    } else {
                        newOrganizationLayer.add(organizationLayer);
                    }
                }
                //vervang de oude set met layers in de organisatie voor de nieuwe set
                org.setWfsLayers(newOrganizationLayer);
                em.flush();
            }
            Set oldLayers = oldServiceProvider.getWfsLayers();
            Iterator oldLayersIter = oldLayers.iterator();
            while (oldLayersIter.hasNext()) {
                WfsLayer oldLayer = (WfsLayer) oldLayersIter.next();
            }
            em.remove(oldServiceProvider);
            em.flush();
        } catch (Exception e) {
            log.error("Fout tijdens verwijderen oude serviceprovider", e);
        }
    }

    private WfsServiceProvider getTestServiceProvider(String url) throws Exception {
        WfsCapabilitiesReader wfs = new WfsCapabilitiesReader();
        WfsServiceProvider sp = null;

        try {
            sp = wfs.getProvider(url.trim());
        } catch (IOException ioex) {
            return null;
        } catch (SAXException saxex) {
            return null;
        } catch (Exception ex) {
            return null;
        }

        return sp;
    }

    @Override
    public ActionForward deleteConfirm(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        WfsServiceProvider serviceProvider = getServiceProvider(dynaForm, request, false);
        if (null == serviceProvider) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        prepareMethod(dynaForm, request, DELETE, EDIT);

        Set serviceLayers = serviceProvider.getWfsLayers();
        if (serviceLayers.size() > 0) {
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
                Set orgLayers = org.getWfsLayers();
                if (orgLayers.size() > 0) {
                    Iterator orgLayerIterator = orgLayers.iterator();
                    boolean notFirstLayer = false;

                    while (orgLayerIterator.hasNext()) {
                        WfsLayer orgLayer = (WfsLayer) orgLayerIterator.next();
                        Iterator serviceLayerIter = serviceLayers.iterator();
                        while (serviceLayerIter.hasNext()) {
                            WfsLayer serviceLayer = (WfsLayer) serviceLayerIter.next();
                            if (orgLayer.equals(serviceLayer)) {
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
                                strMessage.append(orgLayer.getName());
                            }
                        }
                    }
                    if (notFirstLayer) {
                        strMessage.append("]");
                    }
                }
            }
            // Small check so it doesn't show empty warning boxes
            if ((strMessage.toString() == null ? "" != null : !strMessage.toString().equals("")) && strMessage != null && strMessage.length() > 0) {
                addAlternateMessage(mapping, request, null, strMessage.toString());
            //Check if current pricing is bound to this provider
            }
            List lpList = null;
            LayerCalculator lc = new LayerCalculator();
            try {
                lpList = lc.getSpLayerPricingList(serviceProvider.getAbbr(), new Date(), OGCConstants.WFS_SERVICE_WFS);
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
                // Small check so it doesn't show empty warning boxes
                if ((strMessage.toString() == null ? "" != null : !strMessage.toString().equals("")) && strMessage != null && strMessage.length() > 0) {
                    addAlternateMessage(mapping, request, null, strMessage.toString());
                }
            }
        }
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
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
    @Override
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
        WfsServiceProvider serviceProvider = getServiceProvider(dynaForm, request, false);
        if (null == serviceProvider) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        Set serviceProviderLayers = serviceProvider.getWfsLayers();
        if (serviceProviderLayers.size() > 0) {
            List orgList = em.createQuery("from Organization").getResultList();
            Iterator orgit = orgList.iterator();
            while (orgit.hasNext()) {
                Organization org = (Organization) orgit.next();
                Set orgLayers = org.getWfsLayers();
                HashSet clonedOrgLayers = new HashSet();
                clonedOrgLayers.addAll(orgLayers);
                Iterator orgLayerIterator = orgLayers.iterator();
                while (orgLayerIterator.hasNext()) {
                    WfsLayer organizationLayer = (WfsLayer) orgLayerIterator.next();
                    Iterator serviceLayerIter = serviceProviderLayers.iterator();
                    while (serviceLayerIter.hasNext()) {
                        WfsLayer serviceProviderLayer = (WfsLayer) serviceLayerIter.next();
                        if (organizationLayer != null && organizationLayer.getId() == serviceProviderLayer.getId()) {
                            clonedOrgLayers.remove(organizationLayer);
                        }
                    }
                }
                if (orgLayers.size() != clonedOrgLayers.size()) {
                    org.setWfsLayers(clonedOrgLayers);
                    em.merge(org);
                }
            }
        }
        em.remove(serviceProvider);
        em.flush();

        dynaForm.initialize(mapping);
        prepareMethod(dynaForm, request, LIST, EDIT);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return getDefaultForward(mapping, request);
    }

    /* Creates a list of all the service providers in the database.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="" desc="createLists(DynaValidatorForm form, HttpServletRequest request) method.">
    @Override
    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        super.createLists(form, request);
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        List serviceproviderlist = em.createQuery("from WfsServiceProvider order by given_name").getResultList();
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
    protected WfsServiceProvider getServiceProvider(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew) throws Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        WfsServiceProvider serviceProvider = null;
        Integer id = getID(dynaForm);
        if (null == id && createNew) {
            serviceProvider = new WfsServiceProvider();
        } else if (null != id) {
            serviceProvider = (WfsServiceProvider) em.find(WfsServiceProvider.class, new Integer(id.intValue()));
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
    protected void populateServerObject(DynaValidatorForm dynaForm, WfsServiceProvider serviceProvider) {
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
    private void populateServiceProviderForm(WfsServiceProvider serviceProvider, DynaValidatorForm dynaForm, HttpServletRequest request) {
        dynaForm.set("id", serviceProvider.getId() + "");
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
    private WfsLayer checkLayer(WfsLayer orgLayer, Set layers) {
        if (layers == null || layers.isEmpty()) {
            return null;
        }
        Iterator it = layers.iterator();
        while (it.hasNext()) {
            WfsLayer layer = (WfsLayer) it.next();

            if (orgLayer.getName() == null && layer.getName() == null &&
                    orgLayer.getTitle().equalsIgnoreCase(layer.getTitle())) {
                return layer;
            }
            if (orgLayer.getName() != null && layer.getName() != null &&
                    orgLayer.getName().equalsIgnoreCase(layer.getName())) {
                return layer;
            }
        }
        return null;
    }
    // </editor-fold>
}