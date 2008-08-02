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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import nl.b3p.wms.capabilities.ContactInformation;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.wms.capabilities.ServiceDomainResource;
import nl.b3p.wms.capabilities.ServiceProvider;

public class ServiceProviderValidator {

    private Set serviceProviders;

    /**
     * Create a ServiceProvider object which has only Capabilities and Formats which are supported by all ServiceProviders.
     *
     * @return a valid ServiceProvider object.
     */
    // <editor-fold defaultstate="" desc="ServiceProvider constructor.">
    public ServiceProviderValidator(Set serviceProviders) {
        setServiceProviders(serviceProviders);
    }
    // </editor-fold>
    /**
     * Create a ServiceProvider object which has only Capabilities and Formats which are supported by all ServiceProviders.
     *
     * @return a valid ServiceProvider object.
     */
    // <editor-fold defaultstate="" desc="getValidServiceProvider() method.">
    public ServiceProvider getValidServiceProvider() {
        ServiceProvider newSP = new ServiceProvider();
        this.fillServiceProviderConstants(newSP);

        Set resources = new HashSet();
        if (serviceProviders != null && !serviceProviders.isEmpty()) {
            //create a set with domain resources which apply to all valid statements.
            String[] domains = this.validateDomains();
            for (int i = 0; i < domains.length; i++) {
                String domain = domains[i];
                if (domain != null && !"null".equalsIgnoreCase(domain)) {
                    String[] f = this.validateFormats(domain);

                    ServiceDomainResource sdr = new ServiceDomainResource();
                    sdr.setDomain(domain);
                    Set formats = new HashSet();
                    for (int j = 0; j < f.length; j++) {
                        if (f[j] != null && !"null".equalsIgnoreCase(f[j])) {
                            formats.add(f[j]);
                        }
                    }
                    sdr.setFormats(formats);
                    sdr.setServiceProvider(newSP);
                    sdr.setGetUrl(KBConfiguration.CONTACT_WEBSITE);
                    sdr.setPostUrl(KBConfiguration.CONTACT_WEBSITE);
                    resources.add(sdr);
                }
            }
        }
        if (resources.isEmpty()) {
            resources = getDefaultResources();
        }
        newSP.setDomainResource(resources);
        newSP.setAbbr(KBConfiguration.SERVICEPROVIDER_BASE_ABBR);

        Set exception = new HashSet();
        if (serviceProviders != null && !serviceProviders.isEmpty()) {
            //Now we still need to check for the right Exception formats.....
            String[] exceptions = this.validateExceptions();
            for (int j = 0; j < exceptions.length; j++) {
                if (exceptions[j] != null && !"null".equalsIgnoreCase(exceptions[j])) {
                    exception.add(exceptions[j]);
                }
            }
        }
        if (exception.isEmpty()) {
            exception = getDefaultException();
        }
        newSP.setExceptions(exception);

        return newSP;
    }
    // </editor-fold>
    private Set getDefaultException() {
        Set exception = new HashSet();
        exception.add("application/vnd.ogc.se_xml");
        exception.add("application/vnd.ogc.se_inimage");
        exception.add("application/vnd.ogc.se_blank");
        return exception;
    }

    private Set getDefaultResources() {
        Set resources = new HashSet();

        ServiceDomainResource sdr = new ServiceDomainResource();
        sdr.setDomain("GetCapabilities"); //
        Set formats = new HashSet();
        formats.add("application/vnd.ogc.wms_xml");
        sdr.setFormats(formats);
        resources.add(sdr);


        sdr = new ServiceDomainResource();
        sdr.setDomain("GetMap"); //
        formats = new HashSet();
        formats.add("image/png");
        formats.add("image/jpeg");
        formats.add("image/tiff");
        sdr.setFormats(formats);
        resources.add(sdr);

        sdr = new ServiceDomainResource();
        sdr.setDomain("GetFeatureInfo"); //
        formats = new HashSet();
        formats.add("text/plain");
        formats.add("application/vnd.ogc.gml");
        sdr.setFormats(formats);
        resources.add(sdr);

        return resources;
    }

    /**
     * Fill the serviceprovider object with predefined static constants.
     *
     * @param serviceProvider The ServiceProvider object that has to be filled.
     */
    // <editor-fold defaultstate="" desc="fillServiceProviderConstants(ServiceProvider serviceProvider) method.">
    private void fillServiceProviderConstants(ServiceProvider serviceProvider) {
        serviceProvider.setName(KBConfiguration.SERVICE_NAME);
        serviceProvider.setTitle(KBConfiguration.SERVICE_TITLE);
        serviceProvider.setAbstracts(KBConfiguration.SERVICE_ABSTRACT);
        serviceProvider.setAccessConstraints(KBConfiguration.SERVICE_FEES);
        serviceProvider.setFees(KBConfiguration.SERVICE_CONSTRAINTS);

        ContactInformation ci = new ContactInformation();
        ci.setContactPerson(KBConfiguration.CONTACT_PERSON);
        ci.setContactPosition(KBConfiguration.CONTACT_POSITION);
        ci.setContactOrganization(KBConfiguration.CONTACT_ORGANIZATION);
        ci.setAddress(KBConfiguration.CONTACT_ADDRESS);
        ci.setAddressType(KBConfiguration.CONTACT_ADDRESS_TYPE);
        ci.setPostcode(KBConfiguration.CONTACT_POSTCODE);
        ci.setCity(KBConfiguration.CONTACT_CITY);
        ci.setStateOrProvince(KBConfiguration.CONTACT_STATE_OR_PROVINCE);
        ci.setCountry(KBConfiguration.CONTACT_COUNTRY);
        ci.setVoiceTelephone(KBConfiguration.CONTACT_VOICETELEPHONE);
        ci.setFascimileTelephone(KBConfiguration.CONTACT_FASCIMILEPHONE);
        ci.setEmailAddress(KBConfiguration.CONTACT_EMAIL);

        serviceProvider.setContactInformation(ci);
    }
    // </editor-fold>
    /**
     * Check whether the set of ServiceProviders all have a GetMap Capability.
     *
     * @return a boolean which is true if all ServiceProviders in the set have a GetMap Capability.
     */
    // <editor-fold defaultstate="" desc="validateGetMapFormat() method.">
    public boolean validate() {
        return (validateFormats(OGCConstants.WMS_REQUEST_GetMap).length > 0) &&
                (validateFormats(OGCConstants.WMS_REQUEST_GetCapabilities).length > 0) &&
                (validateFormats(OGCConstants.WMS_REQUEST_GetFeatureInfo).length > 0) &&
                (validateFormats(OGCConstants.WMS_REQUEST_DescribeLayer).length > 0) &&
                (validateFormats(OGCConstants.WMS_REQUEST_GetLegendGraphic).length > 0);
    }
    // </editor-fold>
    /**
     * Check whether the set of ServiceProviders all have a GetMap Capability.
     *
     * @return a boolean which is true if all ServiceProviders in the set have a GetMap Capability.
     */
    // <editor-fold defaultstate="" desc="validateGetMapFormat() method.">
    public boolean validateGetMapFormat() {
        return validateFormats(OGCConstants.WMS_REQUEST_GetMap).length > 0;
    }
    // </editor-fold>
    /**
     * Check whether the set of ServiceProviders all have a GetCapabilities Capability.
     *
     * @return a boolean which is true if all ServiceProviders in the set have a GetCapabilities Capability.
     */
    // <editor-fold defaultstate="" desc="validateGetCapabilitiesFormat() method.">
    public boolean validateGetCapabilitiesFormat() {
        return validateFormats(OGCConstants.WMS_REQUEST_GetCapabilities).length > 0;
    }
    // </editor-fold>
    /**
     * Check whether the set of ServiceProviders all have a GetFeatureInfo Capability.
     *
     * @return a boolean which is true if all ServiceProviders in the set have a GetFeatureInfo Capability.
     */
    // <editor-fold defaultstate="" desc="validateGetFeatureInfoFormat() method.">
    public boolean validateGetFeatureInfoFormat() {
        return validateFormats(OGCConstants.WMS_REQUEST_GetFeatureInfo).length > 0;
    }
    // </editor-fold>
    /**
     * Check whether the set of ServiceProviders all have a DescribeLayer Capability.
     *
     * @return a boolean which is true if all ServiceProviders in the set have a DescribeLayer Capability.
     */
    // <editor-fold defaultstate="" desc="validateDescribeLayerFormat() method.">
    public boolean validateDescribeLayerFormat() {
        return validateFormats(OGCConstants.WMS_REQUEST_DescribeLayer).length > 0;
    }
    // </editor-fold>
    /**
     * Check whether the set of ServiceProviders all have a GetLegendGraphic Capability.
     *
     * @return a boolean which is true if all ServiceProviders in the set have a GetLegendGraphic Capability.
     */
    // <editor-fold defaultstate="" desc="validateGetLegendGraphicFormat() method.">
    public boolean validateGetLegendGraphicFormat() {
        return validateFormats(OGCConstants.WMS_REQUEST_GetLegendGraphic).length > 0;
    }
    // </editor-fold>
    /**
     * Check the domains that are supported by all the Serviceproviders.
     *
     * @return String[] with only the domains supported by all the serviceproviders.
     */
    // <editor-fold defaultstate="" desc="validateDomains() method.">
    private String[] validateExceptions() {
        HashMap hm = new HashMap();
        Iterator serviceProviderIterator = serviceProviders.iterator();
        while (serviceProviderIterator.hasNext()) {
            ServiceProvider sp = (ServiceProvider) serviceProviderIterator.next();
            Iterator exceptionIterator = sp.getExceptions().iterator();
            while (exceptionIterator.hasNext()) {
                String exception = (String) exceptionIterator.next();
                hashmapFilter(hm, exception);
            }
        }
        return formats(hm);
    }
    // </editor-fold>
    /**
     * Check the domains that are supported by all the Serviceproviders.
     *
     * @return String[] with only the domains supported by all the serviceproviders.
     */
    // <editor-fold defaultstate="" desc="validateDomains() method.">
    private String[] validateDomains() {
        HashMap hm = new HashMap();
        Iterator serviceProviderIterator = serviceProviders.iterator();
        while (serviceProviderIterator.hasNext()) {
            ServiceProvider sp = (ServiceProvider) serviceProviderIterator.next();
            Iterator domainResourceIterator = sp.getDomainResource().iterator();
            while (domainResourceIterator.hasNext()) {
                ServiceDomainResource sdr = (ServiceDomainResource) domainResourceIterator.next();
                String domain = sdr.getDomain();
                hashmapFilter(hm, domain);
            }
        }
        return formats(hm);
    }
    // </editor-fold>
    /**
     * Check the formats that are supported by all the serviceproviders
     *
     * @param domain The name of the domain to be checked.
     *
     * @return String[] with only the formats that are supported by all the serviceproviders.
     */
    // <editor-fold defaultstate="" desc="validateFormats(String domain) method.">
    private String[] validateFormats(String domain) {
        HashMap hm = new HashMap();
        Iterator serviceProviderIterator = serviceProviders.iterator();
        while (serviceProviderIterator.hasNext()) {
            ServiceProvider sp = (ServiceProvider) serviceProviderIterator.next();
            Iterator domainResourceIterator = sp.getDomainResource().iterator();
            while (domainResourceIterator.hasNext()) {
                ServiceDomainResource sdr = (ServiceDomainResource) domainResourceIterator.next();
                if (sdr.getDomain().equalsIgnoreCase(domain)) {
                    Set formats = sdr.getFormats();
                    Iterator itf = formats.iterator();
                    while (itf.hasNext()) {
                        String format = (String) itf.next();
                        hashmapFilter(hm, format);
                    }
                }
            }
        }
        return formats(hm);
    }
    // </editor-fold>
    /**
     * Methode that creates an Array of supported formats, given a certain HashMap.
     *
     * @param hashMap The hashmap that contains the counted values.
     *
     * @return a String Array with the supported formats.
     */
    // <editor-fold defaultstate="" desc="formats(HashMap hashMap) method.">
    private String[] formats(HashMap hashMap) {
        ArrayList supportedFormats = new ArrayList();
        Iterator it = hashMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            int i = ((Integer) entry.getValue()).intValue();
            if (i >= serviceProviders.size()) {
                supportedFormats.add((String) entry.getKey());
            }
        }
        String[] formats = new String[supportedFormats.size() + 1];
        return (String[]) supportedFormats.toArray(formats);
    }
    // </editor-fold>
    /**
     * Methode that counts the values of filterValue. Each value is counted seperatly.
     *
     * @param results The hashmap that contains the counted values.
     * @param filterValue The value to add to the count.
     */
    // <editor-fold defaultstate="" desc="hashmapFilter(HashMap results, String filterValue) method.">
    private void hashmapFilter(HashMap results, String filterValue) {
        if (results.containsKey(filterValue)) {
            int i = ((Integer) results.get(filterValue)).intValue() + 1;
            results.put(filterValue, new Integer(i));
        } else {
            results.put(filterValue, new Integer("1"));
        }
    }
    // </editor-fold>
    //<editor-fold defaultstate="" desc="Getter and setter methods.">
    public Set getServiceProviders() {
        return serviceProviders;
    }

    public void setServiceProviders(Set serviceProviders) {
        this.serviceProviders = serviceProviders;
    }
    //</editor-fold>
}
