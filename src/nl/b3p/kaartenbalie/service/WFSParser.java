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
 * 
 * @author Rachelle Scheijen
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
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.wfs.v110.WfsCapabilitiesReader;
import nl.b3p.ogc.wfs.v110.WfsLayer;
import nl.b3p.ogc.wfs.v110.WfsServiceProvider;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;
import org.xml.sax.SAXException;

public class WFSParser extends WmsWfsParser {
    
    /** 
     * Method for saving a new service provider from input of a user.
     *
     * @param request The HTTP Request we are processing.
     * @param dynaForm The DynaValidatorForm bean for this request.
     * 
     * @return The status code
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="" desc="save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    public String saveProvider(HttpServletRequest request,DynaValidatorForm dynaForm) throws Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        
        WfsServiceProvider newServiceProvider = null;
        WfsServiceProvider oldServiceProvider = getServiceProvider(dynaForm, false);
        Integer oldId = null;
        if (oldServiceProvider!=null) {
            oldId = oldServiceProvider.getId();
        }
        WfsCapabilitiesReader wfs = new WfsCapabilitiesReader();
        
        if (!isAbbrUnique(oldId, dynaForm, em)) {
            return NON_UNIQUE_ABBREVIATION_ERROR_KEY;
        }
        
        String abbreviation = FormUtils.nullIfEmpty(dynaForm.getString("abbr"));
        if (!isAlphaNumeric(abbreviation)) {
            return NON_ALPHANUMERIC_ABBREVIATION_ERROR_KEY;
        }
        if (abbreviation.equalsIgnoreCase(KBConfiguration.SERVICEPROVIDER_BASE_ABBR)) {
            return ABBR_RESERVED_ERROR_KEY;
        }
        
        B3PCredentials credentials  = new B3PCredentials();
        String username = dynaForm.getString("username");
        String password = dynaForm.getString("password");
        String url = FormUtils.nullIfEmpty(dynaForm.getString("url"));
        
        if( !username.equals("") ){
            credentials.setUserName(username);
            credentials.setPassword(password);
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
            newServiceProvider = wfs.getProvider(url.trim(),credentials);
            newServiceProvider.setAbbr(dynaForm.getString("abbr"));
            newServiceProvider.setGivenName(dynaForm.getString("givenName"));
            
        } catch (IOException e) {
            return SERVER_CONNECTION_ERROR;
        } catch (Exception e) {
            exception   = e;
            log.error("Error saving server", e);
            return SAVE_ERRORKEY;
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
        if (oldServiceProvider != null) {
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
                        if (orgLayerServiceProvider.getId() == oldId) {
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
                exception   = e;
                log.error("Error deleting the old serviceprovider", e);
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

        /* geef rechten op alle layers voor aangevinkte groepen */
        String[] orgSelected = dynaForm.getStrings("orgSelected");
        GroupParser.addRightsForAllLayers(orgSelected, newServiceProvider,em);
        
        if(uploadError != null && !uploadError.equals(OK)){
            return uploadError;
        }
        
        return OK;
    }
    
    public int test(DynaValidatorForm dynaForm) throws Exception {
        EntityManager em = getEntityManager();

        String regexp = FormUtils.nullIfEmpty(dynaForm.getString("regexp"));
        String replacement = FormUtils.nullIfEmpty(dynaForm.getString("replacement"));

        int fout = 0;

        try {
            List<WfsServiceProvider> wmsServices = em.createQuery("from WfsServiceProvider").getResultList();

            for (WfsServiceProvider sp : wmsServices) {
                String newUrl = sp.getUrl();
                B3PCredentials credentials = sp.getCredentials();

                if (regexp != null && replacement != null && !regexp.isEmpty()
                        && !replacement.isEmpty()) {
                    newUrl = newUrl.replaceAll(regexp, replacement);
                }

                WfsServiceProvider newSp = getTestServiceProvider(newUrl, credentials);

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
        
        return fout;
    }
    
    /**
     * Method that fills a service provider object with the user input from the forms.
     *
     * @param dynaForm The DynaValidatorForm bean for this request.
     * @param serviceProvider ServiceProvider object that to be filled
     */
    // <editor-fold defaultstate="" desc="populateServerObject(DynaValidatorForm dynaForm, ServiceProvider serviceProvider) method.">
    protected void populateServerObject(DynaValidatorForm dynaForm, WfsServiceProvider serviceProvider) {
        serviceProvider.setGivenName(FormUtils.nullIfEmpty(dynaForm.getString("givenName")));
        serviceProvider.setUpdatedDate(new Date());
        serviceProvider.setAbbr(dynaForm.getString("abbr"));
    }

    @Override
    public int batchUpdate(DynaValidatorForm dynaForm) throws Exception {
        return batchUpdate(dynaForm,"");
    }    

    @Override
    public int batchUpdate(DynaValidatorForm dynaForm, String prefix) throws Exception {
        EntityManager em = getEntityManager();

        String regexp = FormUtils.nullIfEmpty(dynaForm.getString("regexp"));
        String replacement = FormUtils.nullIfEmpty(dynaForm.getString("replacement"));

        int fout = 0;

        try {
            List<WfsServiceProvider> wmsServices;
            if( prefix.equals("") ){
                wmsServices = em.createQuery("from WfsServiceProvider").getResultList();
            }
            else {
                wmsServices = em.createQuery("from WfsServiceProvider WHERE abbr=:abbr").setParameter("abbr", prefix).getResultList();
            }

            for (WfsServiceProvider oldServiceProvider : wmsServices) {
                String newUrl = oldServiceProvider.getUrl();

                B3PCredentials credentials = oldServiceProvider.getCredentials();

                if (regexp != null && replacement != null && !regexp.isEmpty()
                        && !replacement.isEmpty()) {
                    newUrl = newUrl.replaceAll(regexp, replacement);
                }

                WfsServiceProvider newServiceProvider = getTestServiceProvider(newUrl, credentials);

                if (newServiceProvider != null) {
                    newServiceProvider.setStatus(SERVICE_STATUS_OK);
                } else {
                    oldServiceProvider.setStatus(SERVICE_STATUS_ERROR);
                    fout++;
                }

                /*
                 * indien newServiceProvider ok dan bijwerken
                 */
                if (newServiceProvider != null) {
                    updateServiceProvider(oldServiceProvider, newServiceProvider);
                }
            }

            em.flush();
        } catch (Exception ex) {
            log.error("Er iets iets fout gegaan tijdens de batch update van de WFS Services: " + ex);
            this.exception  = ex;
        }
        
        return fout;
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
    
    public String deleteConfirm(DynaValidatorForm dynaForm, HttpServletRequest request) throws Exception {
        this.parseMessages.clear();
        
        EntityManager em = getEntityManager();
        WfsServiceProvider serviceProvider = getServiceProvider(dynaForm, false);
        if (null == serviceProvider) {
            return NOTFOUND_ERROR_KEY;
        }
        
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
                parseMessages.add(strMessage.toString());
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
                    parseMessages.add(strMessage.toString());
                }
            }
        }
        
        return OK;
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
        
        WfsServiceProvider serviceProvider = getServiceProvider(dynaForm, false);
        if (null == serviceProvider) {
            return NOTFOUND_ERROR_KEY;
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
        
        return OK;
    }
    
      /**
       * Method which returns the service provider with a specified id or a new object if no id is given.
     *
     * @param dynaForm The DynaValidatorForm bean for this request.
     * @param createNew A boolean which indicates if a new object has to be created.
     *
     * @return a service provider object.
     * @throws Exception  
     */
    // <editor-fold defaultstate="" desc="getServiceProvider(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew, Integer id) method.">
    public WfsServiceProvider getServiceProvider(DynaValidatorForm dynaForm,boolean createNew) throws Exception {
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
    
    /**
     * Tries to find a specified layer given for a certain ServiceProvider.
     *
     * @param layers the set with layers which the method has to surch through
     * @param orgLayer the layer to be found
     *
     * @return layer if found.
     */
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
    
    private WfsServiceProvider getTestServiceProvider(String url, B3PCredentials credentials) throws Exception {
        WfsCapabilitiesReader wfs = new WfsCapabilitiesReader();
        WfsServiceProvider sp = null;

        try {
            sp = wfs.getProvider(url.trim(), credentials);
        } catch (IOException ioex) {
            return null;
        } catch (SAXException saxex) {
            return null;
        } catch (Exception ex) {
            return null;
        }

        return sp;
    }
    
    /**
     * Checks if the abbr exists
     * 
     * @param abbr The abbr
     * @param em    The entity manager
     * @return  True if the abbr exists
     */
    @Override
    public boolean abbrExists(String abbr,EntityManager em) {
        try {
            WfsServiceProvider dbSp = (WfsServiceProvider) em.createQuery(
                    "from WfsServiceProvider sp where " +
                    "lower(sp.abbr) = lower(:abbr) ").setParameter("abbr", abbr).getSingleResult();

            if (dbSp != null) {
                return true;
            }
            return false;
        } catch (NoResultException nre) {
            return false;
        }
    }
    
    public static void addRightsForAllLayers(String[] orgSelected, WfsServiceProvider sp,EntityManager em) throws Exception {
        GroupParser.addRightsForAllLayers(orgSelected, sp,em);
    }

    public static void addAllLayersToGroup(Organization org, WfsServiceProvider sp,EntityManager em) throws Exception {
        GroupParser.addAllLayersToGroup(org, sp,em);
    }
    
    /**
     * Returns all the not allowed services
     *
     * @param em The entityManager
     * @return The allowed services
     */
    public List<WfsServiceProvider> getNotAllowedServices(EntityManager em) {
        try {
            List<WfsServiceProvider> providers = em.createQuery(
                    "from WfsServiceProvider sp WHERE sp.allowed=:allowed").setParameter("allowed",false).getResultList();

            return providers;
        } catch (Exception ex) {
            log.error("error collecting allowed WfsServiceProviders", ex);
            return null;
        }
    }

    /**
     * Returns all the allowed services
     *
     * @param em The entityManager
     * @return The allowed services
     */
    public List<WfsServiceProvider> getAllowedServices(EntityManager em) {
        try {
            List<WfsServiceProvider> providers = em.createQuery(
                    "from WfsServiceProvider sp WHERE sp.allowed=:allowed").setParameter("allowed",true).getResultList();

            return providers;
        } catch (Exception ex) {
            log.error("error collecting allowed WfsServiceProviders", ex);
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
        WfsServiceProvider sp = this.getProviderByUrl(abbr, em);
        if (sp == null) {
            throw new Exception("Adding unknown WFS service with " + abbr);
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
        WfsServiceProvider sp = this.getProviderByUrl(abbr, em);
        if (sp == null) {
            throw new Exception("Deleting unknown WFS service with name " + abbr);
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
        em.createQuery("UPDATE WfsServiceProvider SET allowed=false").executeUpdate();
    }

    /**
     * Searches the WfsServiceProvider with the given abbr
     *
     * @param abbr The abbr to search on
     * @param em The entityManager
     * @return The found WfsServiceProvider, otherwise null
     */
    private WfsServiceProvider getProviderByUrl(String abbr, EntityManager em) {
        try {
            WfsServiceProvider dbSp = (WfsServiceProvider) em.createQuery(
                    "from WfsServiceProvider sp where "
                    + "sp.abbr=:abbr ").setParameter("abbr", abbr).getSingleResult();

            return dbSp;
        } catch (Exception ex) {
            log.error("error locating WfsServiceProvider", ex);
            return null;
        }
    }
}
