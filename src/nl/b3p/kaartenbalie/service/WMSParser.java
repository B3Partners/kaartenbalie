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
package nl.b3p.kaartenbalie.service;

import java.io.IOException;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.gis.B3PCredentials;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.accounting.LayerCalculator;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPricing;
import nl.b3p.ogc.sld.SldNamedLayer;
import nl.b3p.ogc.sld.SldReader;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.ogc.wfs.v110.WfsServiceProvider;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.ServiceProvider;
import nl.b3p.wms.capabilities.Style;
import nl.b3p.wms.capabilities.WMSCapabilitiesReader;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;
import org.xml.sax.SAXException;

/**
 *
 * @author rachelle
 */
public class WMSParser extends WmsWfsParser {

    private ServiceProvider oldServiceProvider = null;

    /**
     * Method for saving a new service provider from input of a user.
     *
     * @param request The HTTP Request we are processing.
     * @param dynaForm The DynaValidatorForm bean for this request.
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="" desc="save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    public String saveProvider(HttpServletRequest request, DynaValidatorForm dynaForm) throws Exception {
        EntityManager em = getEntityManager();

        /*
         * Check URL
         */
        String url = FormUtils.nullIfEmpty(dynaForm.getString("url"));
        String inputUrl = url.trim();
        try {
            url = checkWmsUrl(url.trim());
        } catch (Exception e) {
            exception = e;
            return ERROR_INVALID_URL;
        }

        /*
         * Check service provider
         */
        ServiceProvider newServiceProvider = null;
        oldServiceProvider = getServiceProvider(dynaForm, false);

        Integer oldId = null;
        if (oldServiceProvider != null) {
            oldId = oldServiceProvider.getId();
        }

        if (!isAbbrUnique(oldId, dynaForm, em)) {
            return NON_UNIQUE_ABBREVIATION_ERROR_KEY;
        }

        B3PCredentials credentials = new B3PCredentials();
        String username = dynaForm.getString("username");
        String password = dynaForm.getString("password");
        String abbreviation = FormUtils.nullIfEmpty(dynaForm.getString("abbr"));

        if (!username.equals("")) {
            credentials.setUserName(username);
            credentials.setPassword(password);
        }

        Boolean ignoreResource = (Boolean) dynaForm.get("ignoreResource");

        /*
         * This request can lead to several problems. The server can be down or
         * the url given isn't right. This means that the url is correct
         * according to the specification but is leading to the wrong address.
         * Or the address is OK, but the Capabilities of the provider do not
         * comply the specifications. Or there can be an other exception during
         * the process. Either way we need to inform the user about the error
         * which occured.
         */
        try {
            String serviceUrl = null;
            if (ignoreResource != null && ignoreResource) {
                String getCap = "&service=WMS&request=GetCapabilities&version=1.1.1";
                serviceUrl = inputUrl.trim() + getCap;

            } else {
                serviceUrl = url.trim();
            }

            String givenName = FormUtils.nullIfEmpty(dynaForm.getString("givenName"));
            newServiceProvider = saveServiceProvider(serviceUrl, credentials, givenName, abbreviation, em);
        } catch (IOException e) {
            log.error("Error saving server", e);
            return SERVER_CONNECTION_ERROR;
        } catch (SAXException e) {
            log.error("Error saving server", e);
            return MALFORMED_CAPABILITY_ERROR;
        } catch (Exception e) {
            log.error("Error saving server", e);
            this.exception = e;
            return SAVE_ERRORKEY;
        }

        if (!newServiceProvider.getWmsVersion().equalsIgnoreCase(OGCConstants.WMS_VERSION_111)) {
            return UNSUPPORTED_WMSVERSION_ERRORKEY;
        }

        /*
         * TODO: Kunnen we na bovenstaande checks de ingevoerde url gebruiken
         * als service provider url i.p.v. degene uit de online resource ? Dit
         * geeft namelijk bij het toevoegen van externe wms services waar dit
         * verkeerd staat welke je niet zelf kunt wijzigen problemen.
         */

        if (ignoreResource != null && ignoreResource) {
            newServiceProvider.setUrl(inputUrl);
        } else {
            newServiceProvider.setUrl(url);
        }

        if (username != null) {
            /*
             * Save username and password
             */
            newServiceProvider.setUserName(username);
            newServiceProvider.setPassword(password);
        }

        populateServerObject(dynaForm, newServiceProvider);

        em.persist(newServiceProvider);
        em.flush();

        /*
         * NamedLayers uit Sld ophalen
         */
        SldReader sldReader = new SldReader();
        String sldUrl = FormUtils.nullIfEmpty(dynaForm.getString("sldUrl"));
        List<SldNamedLayer> namedLayers = null;
        if (sldUrl != null && !sldUrl.equals("")) {
            namedLayers = sldReader.getNamedLayersByUrl(sldUrl, credentials);
        }

        Set layerSet = newServiceProvider.getAllLayers();
        Iterator dwIter = layerSet.iterator();

        while (dwIter.hasNext()) {
            Layer layer = (Layer) dwIter.next();

            /*
             * Kijken of voor deze layer UserStyles in bijbehorende NamedLayer
             * voorkomen. Zo ja, deze als Style opslaan in database
             */
            if (namedLayers != null && namedLayers.size() > 0) {
                Set<Style> styles = getSldStylesSet(namedLayers, layer);
                Iterator<Style> styleIt = styles.iterator();
                while (styleIt.hasNext()) {
                    Style style = styleIt.next();
                    //een style moet een name hebben om aan te roepen
                    if (style.getName() == null
                            || style.getName().length() == 0) {
                        style.setName(layer.getName() + "_SLD");
                    }

                    if (layer.getStyles() == null) {
                        layer.setStyles(new HashSet<Style>());
                    }
                    String newStyleName = getUniqueStyleName(layer.getStyles(), style.getName());
                    style.setName(newStyleName);
                    layer.getStyles().add(style);
                }
            }

            // Find old layer to be able to reuse metadata additions
            Set topLayerSet = new HashSet();
            if (oldServiceProvider != null) {
                topLayerSet.add(oldServiceProvider.getTopLayer());
            }
            Layer oldLayer = checkLayer(layer, topLayerSet);

            setMetadataFromLayerSource(layer, oldLayer, credentials);
        }

        if (oldServiceProvider != null) {
            /*
             * Then we need to call for a list with organizations. We walk
             * through this list and for each organization in the list we need
             * to check if this organization has connections with the old
             * serviceprovider.
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
                this.exception = e;
                return ERROR_DELETE_OLD_PROVIDER;
            }
        }
        
        /*
         * Upload geselecteerde file
         */
        FormFile thisFile = (FormFile) dynaForm.get("uploadFile");
        Boolean overwrite = (Boolean) dynaForm.get("overwrite");
        String uploadError = null;
        if(thisFile != null && thisFile.getFileName() != null && !thisFile.getFileName().equals("")){
            uploadError = uploadFile(thisFile, overwrite, abbreviation);
        }

        /*
         * geef rechten op alle layers voor aangevinkte groepen
         */
        String[] orgSelected = dynaForm.getStrings("orgSelected");

        GroupParser.addRightsForAllLayers(orgSelected, newServiceProvider, em);
        
        if(uploadError != null && !uploadError.equals(OK)){
            return uploadError;
        }

        return WMSParser.OK;
    }

    public int test(DynaValidatorForm dynaForm) throws Exception {
        EntityManager em = getEntityManager();

        String regexp = FormUtils.nullIfEmpty(dynaForm.getString("regexp"));
        String replacement = FormUtils.nullIfEmpty(dynaForm.getString("replacement"));

        int fout = 0;

        try {
            List<ServiceProvider> wmsServices = em.createQuery("from ServiceProvider").getResultList();

            for (ServiceProvider sp : wmsServices) {
                String newUrl = sp.getUrl();
                B3PCredentials credentials = sp.getCredentials();

                if (regexp != null && replacement != null && !regexp.isEmpty()
                        && !replacement.isEmpty()) {
                    newUrl = newUrl.replaceAll(regexp, replacement);
                }

                ServiceProvider newSp = getTestServiceProvider(newUrl, credentials);

                if (newSp != null) {
                    sp.setStatus(SERVICE_STATUS_OK);
                } else {
                    sp.setStatus(SERVICE_STATUS_ERROR);
                    fout++;
                }
            }

            em.flush();
        } catch (Exception ex) {
            log.error("Er iets iets fout gegaan tijdens het testen van de WMS Services: " + ex);
        }

        return fout;
    }

    public int batchUpdate(DynaValidatorForm dynaForm) throws Exception {
        return batchUpdate(dynaForm, "");
    }

    public int batchUpdate(DynaValidatorForm dynaForm, String prefix) throws Exception {
        EntityManager em = getEntityManager();

        String regexp = FormUtils.nullIfEmpty(dynaForm.getString("regexp"));
        String replacement = FormUtils.nullIfEmpty(dynaForm.getString("replacement"));

        int fout = 0;

        try {
            List<ServiceProvider> wmsServices;
            if (prefix.equals("")) {
                wmsServices = em.createQuery("from ServiceProvider").getResultList();
            } else {
                wmsServices = em.createQuery("from ServiceProvider WHERE abbr=:abbr").setParameter("abbr", prefix).getResultList();
            }

            for (ServiceProvider serviceProvider : wmsServices) {
                String newUrl = serviceProvider.getUrl();
                B3PCredentials credentials = serviceProvider.getCredentials();

                if (regexp != null && replacement != null && !regexp.isEmpty()
                        && !replacement.isEmpty()) {
                    newUrl = newUrl.replaceAll(regexp, replacement);
                }

                ServiceProvider newServiceProvider = getTestServiceProvider(newUrl, credentials);

                if (newServiceProvider != null) {
                    newServiceProvider.setStatus(SERVICE_STATUS_OK);
                } else {
                    serviceProvider.setStatus(SERVICE_STATUS_ERROR);
                    fout++;
                }

                /*
                 * indien newServiceProvider ok dan bijwerken
                 */
                if (newServiceProvider != null) {
                    updateServiceProvider(serviceProvider, newServiceProvider);
                }
            }

            em.flush();
        } catch (Exception ex) {
            log.error("Er iets iets fout gegaan tijdens de batch update van de WMS Services: " + ex);
            this.exception = ex;
        }

        return fout;
    }

    protected String checkWmsUrl(String url) throws Exception {
        OGCRequest ogcrequest = new OGCRequest(url);
        if (ogcrequest.containsParameter(OGCConstants.WMS_REQUEST)
                && !OGCConstants.WMS_REQUEST_GetCapabilities.equalsIgnoreCase(ogcrequest.getParameter(OGCConstants.WMS_REQUEST))) {
            log.error(KBConfiguration.UNSUPPORTED_REQUEST);
            throw new Exception(KBConfiguration.UNSUPPORTED_REQUEST);
        } else {
            ogcrequest.addOrReplaceParameter(OGCConstants.WMS_REQUEST, OGCConstants.WMS_REQUEST_GetCapabilities);
        }
        if (ogcrequest.containsParameter(OGCConstants.WMS_SERVICE)
                && !OGCConstants.WMS_SERVICE_WMS.equalsIgnoreCase(ogcrequest.getParameter(OGCConstants.WMS_SERVICE))) {
            log.error(KBConfiguration.UNSUPPORTED_SERVICE);
            throw new Exception(KBConfiguration.UNSUPPORTED_SERVICE);
        } else {
            ogcrequest.addOrReplaceParameter(OGCConstants.WMS_SERVICE, OGCConstants.WMS_SERVICE_WMS);
        }
        if (ogcrequest.containsParameter(OGCConstants.WMS_VERSION)
                && !OGCConstants.WMS_VERSION_111.equalsIgnoreCase(ogcrequest.getParameter(OGCConstants.WMS_VERSION))) {
            log.error(KBConfiguration.UNSUPPORTED_VERSION);
            throw new Exception(KBConfiguration.UNSUPPORTED_VERSION);
        } else {
            ogcrequest.addOrReplaceParameter(OGCConstants.WMS_VERSION, OGCConstants.WMS_VERSION_111);
        }
        return ogcrequest.getUrl();
    }

    /*
     * Method which returns the service provider with a specified id or a new
     * object if no id is given.
     *
     * @param form The DynaValidatorForm bean for this request. @param request
     * The HTTP Request we are processing. @param createNew A boolean which
     * indicates if a new object has to be created. @param id An Integer
     * indicating which organization id has to be searched for.
     *
     * @return a service provider object.
     */
    // <editor-fold defaultstate="" desc="getServiceProvider(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew, Integer id) method.">
    public ServiceProvider getServiceProvider(DynaValidatorForm dynaForm, boolean createNew) throws Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        Integer id = getInt(dynaForm, "id");
        ServiceProvider serviceProvider = null;
        if (null == id && createNew) {
            serviceProvider = new ServiceProvider();
        } else if (null != id) {
            serviceProvider = (ServiceProvider) em.find(ServiceProvider.class, new Integer(id.intValue()));
        }

        return serviceProvider;
    }

    public static ServiceProvider saveServiceProvider(String url, B3PCredentials credentials, String givenName, String abbreviation, EntityManager em) throws Exception {

        WMSCapabilitiesReader wms = new WMSCapabilitiesReader();
        ServiceProvider serviceProvider = wms.getProvider(url, credentials);
        serviceProvider.setGivenName(givenName);
        serviceProvider.setAbbr(abbreviation);
        serviceProvider.setUpdatedDate(new Date());
        serviceProvider.setUserName("");
        serviceProvider.setPassword("");
        // haal set op om vulling van set af te dwingen
        Set layerSet = serviceProvider.getAllLayers();

        em.persist(serviceProvider);
        em.flush();
        return serviceProvider;
    }

    /*
     * Method that fills a serive provider object with the user input from the
     * forms.
     *
     * @param form The DynaValidatorForm bean for this request. @param
     * serviceProvider ServiceProvider object that to be filled
     */
    // <editor-fold defaultstate="" desc="populateServerObject(DynaValidatorForm dynaForm, ServiceProvider serviceProvider) method.">
    public void populateServerObject(DynaValidatorForm dynaForm, ServiceProvider serviceProvider) {
        serviceProvider.setUpdatedDate(new Date());
        serviceProvider.setUserName(FormUtils.nullIfEmpty(dynaForm.getString("username")));
        serviceProvider.setPassword(FormUtils.nullIfEmpty(dynaForm.getString("password")));

        String sldUrl = FormUtils.nullIfEmpty(dynaForm.getString("sldUrl"));

        if (sldUrl != null && !sldUrl.equals("")) {
            serviceProvider.setSldUrl(sldUrl);
        }

        /*
         * set ignoreResource
         */
        Boolean ignoreResource = (Boolean) dynaForm.get("ignoreResource");
        if (ignoreResource != null && ignoreResource) {
            serviceProvider.setIgnoreResource(ignoreResource);
        } else {
            serviceProvider.setIgnoreResource(false);
        }
    }

    private void updateServiceProvider(ServiceProvider oldServiceProvider,
            ServiceProvider newServiceProvider) throws Exception {

        EntityManager em = getEntityManager();

        String username = oldServiceProvider.getUserName();
        String password = oldServiceProvider.getPassword();
        newServiceProvider.setGivenName(oldServiceProvider.getGivenName());
        newServiceProvider.setUpdatedDate(new Date());
        newServiceProvider.setAbbr(oldServiceProvider.getAbbr());
        newServiceProvider.setUserName(username);
        newServiceProvider.setPassword(password);

        B3PCredentials credentials = oldServiceProvider.getCredentials();

        Set layerSet = newServiceProvider.getAllLayers();
        em.persist(newServiceProvider);
        em.flush();
        Iterator dwIter = layerSet.iterator();
        while (dwIter.hasNext()) {
            Layer layer = (Layer) dwIter.next();
            // Find old layer to be able to reuse metadata additions
            Set topLayerSet = new HashSet();
            if (oldServiceProvider != null) {
                topLayerSet.add(oldServiceProvider.getTopLayer());
            }
            Layer oldLayer = checkLayer(layer, topLayerSet);

            setMetadataFromLayerSource(layer, oldLayer, credentials);
        }

        if (oldServiceProvider != null) {
            /*
             * Then we need to call for a list with organizations. We walk
             * through this list and for each organization in the list we need
             * to check if this organization has connections with the old
             * serviceprovider.
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
                log.error("Fout tijdens verwijderen oude serviceprovider", e);
            }
        }
    }

    private ServiceProvider getTestServiceProvider(String url, B3PCredentials credentials) throws Exception {
        /*
         * WMS GetCap Url opbouwen
         */
        String newUrl = checkWmsUrl(url);

        WMSCapabilitiesReader wms = new WMSCapabilitiesReader();
        ServiceProvider sp = null;

        try {
            sp = wms.getProvider(newUrl.trim(), credentials);
        } catch (IOException ioex) {
            return null;
        } catch (SAXException saxex) {
            return null;
        } catch (Exception ex) {
            return null;
        }

        return sp;
    }

    public String deleteConfirm(DynaValidatorForm dynaForm, HttpServletRequest request) throws Exception {
        log.debug("Getting entity manager ......");

        this.parseMessages.clear();
        EntityManager em = getEntityManager();
        ServiceProvider serviceProvider = getServiceProvider(dynaForm, false);
        if (null == serviceProvider) {
            return NOTFOUND_ERROR_KEY;
        }

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
                    if (organizationLayerTopLayer != null
                            && organizationLayerTopLayer.getId() == serviceProviderTopLayer.getId()) {
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
            if (strMessage.length() > 0) {
                parseMessages.add(strMessage.toString());
            }
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
                if (strMessage.length() > 0) {
                    parseMessages.add(strMessage.toString());
                }
            }
        }

        return ACKNOWLEDGE_MESSAGES;
    }

    /**
     * Method for deleting a serviceprovider.
     *
     * @param dynaForm The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     *
     * @return the status code
     *
     * @throws Exception
     */
    public String delete(DynaValidatorForm dynaForm, HttpServletRequest request) throws Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        /*
         * Change DataWarehousing mode to performance as this is a very
         * complicated process which will otherwise consume a lot of time.
         */
        if (!isTokenValid(request)) {
            return TOKEN_ERROR_KEY;
        }

        ServiceProvider serviceProvider = getServiceProvider(dynaForm, false);
        if (null == serviceProvider) {
            return NOTFOUND_ERROR_KEY;
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
                    if (organizationLayerTopLayer != null
                            && organizationLayerTopLayer.getId() == serviceProviderTopLayer.getId()) {
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

        return OK;
    }

    public ServiceProvider getOldServiceProvider() {
        return this.oldServiceProvider;
    }

    /**
     * Checks if the abbr exists
     *
     * @param abbr The abbr
     * @param em The entity manager
     * @return True if the abbr exists
     */
    @Override
    public boolean abbrExists(String abbr, EntityManager em) {
        try {
            ServiceProvider dbSp = (ServiceProvider) em.createQuery(
                    "from ServiceProvider sp where "
                    + "lower(sp.abbr) = lower(:abbr) ").setParameter("abbr", abbr).getSingleResult();

            if (dbSp != null) {
                return true;
            }
            return false;
        } catch (NoResultException nre) {
            return false;
        }
    }

    public static void addRightsForAllLayers(String[] orgSelected, ServiceProvider sp, EntityManager em) throws Exception {
        GroupParser.addRightsForAllLayers(orgSelected, sp, em);
    }

    public static void addAllLayersToGroup(Organization org, ServiceProvider sp, EntityManager em) throws Exception {
        GroupParser.addAllLayersToGroup(org, sp, em);
    }
    
    /**
     * Returns all the not allowed services
     *
     * @param em The entityManager
     * @return The allowed services
     */
    public List<ServiceProvider> getNotAllowedServices(EntityManager em) {
        try {
            List<ServiceProvider> providers = em.createQuery(
                    "from ServiceProvider sp WHERE sp.allowed=:allowed").setParameter("allowed",false).getResultList();

            return providers;
        } catch (Exception ex) {
            log.error("error collecting allowed ServiceProviders", ex);
            return null;
        }
    }

    /**
     * Returns all the allowed services
     *
     * @param em The entityManager
     * @return The allowed services
     */
    public List<ServiceProvider> getAllowedServices(EntityManager em) {
        try {
            List<ServiceProvider> providers = em.createQuery(
                    "from ServiceProvider sp WHERE sp.allowed=:allowed").setParameter("allowed",true).getResultList();

            return providers;
        } catch (Exception ex) {
            log.error("error collecting allowed ServiceProviders", ex);
            return null;
        }
    }

    /**
     * Sets the service with the given abbr as allowed
     *
     * @param abbr The abbr to search on
     * @param em The entityManager
     */
    public void addAllowedService(String abbr, EntityManager em) throws Exception {
        ServiceProvider sp = this.getProviderByUrl(abbr, em);
        if (sp == null) {
            throw new Exception("Adding unknown WMS service with name " + abbr);
        }
        
        if( sp.getAllowed() ){
            throw new Exception("Trying to add the service " + sp.getAbbr() + " wich is allready added.");
        }
        
        sp.setAllowed(true);
        
        em.persist(sp);
        em.flush();
    }

    /**
     *
     * Removes the service with the given abbr as allowed
     *
     * @param abbr The abbr to search on
     * @param em The entityManager
     */
    public void deleteAllowedService(String abbr, EntityManager em) throws Exception {
        ServiceProvider sp = this.getProviderByUrl(abbr, em);
        if (sp == null) {
            throw new Exception("Deleting unknown WMS service with name " + abbr);
        }
        
        if( !sp.getAllowed() ){
            throw new Exception("Trying to delete the service " + sp.getAbbr() + " wich is not added.");
        }
        
        sp.setAllowed(false);
        em.persist(sp);
        em.flush();
    }

    /**
     * Clears the allowed services list
     *
     * @param em The entityManager
     */
    public void deleteAllAllowedServices(EntityManager em) {
        em.createQuery("UPDATE ServiceProvider SET allowed=false").executeUpdate();
    }

    /**
     * Searches the ServiceProvider with the given abbr
     *
     * @param abbr The abbr to search on
     * @param em The entityManager
     * @return The found ServiceProvider, otherwise null
     */
    private ServiceProvider getProviderByUrl(String abbr, EntityManager em) {
        try {
            ServiceProvider dbSp = (ServiceProvider) em.createQuery(
                    "from ServiceProvider sp where "
                    + "sp.abbr=:abbr ").setParameter("abbr", abbr).getSingleResult();

            return dbSp;
        } catch (Exception ex) {
            log.error("error locating ServiceProvider", ex);
            return null;
        }
    }
}
