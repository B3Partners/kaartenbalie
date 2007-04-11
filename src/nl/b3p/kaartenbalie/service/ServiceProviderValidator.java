/**
 * @(#)GetCapabilitiesRequestHandler.java
 * @author R. Braam
 * @version 1.00 2007/03/02
 *
 * Purpose: the function of this class is to create a list of url's which direct to the right servers that 
 * have the desired layers for the WMS GetCapabilities request.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.service;

import java.text.Format;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import nl.b3p.kaartenbalie.core.KBConstants;
import nl.b3p.kaartenbalie.core.server.ContactInformation;
import nl.b3p.kaartenbalie.core.server.ServiceDomainResource;
import nl.b3p.kaartenbalie.core.server.ServiceProvider;


public class ServiceProviderValidator implements KBConstants {
    
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
        
        //create a set with domain resources which apply to all valid statements.
        String [] domains = this.validateDomains();
        Set resources = new HashSet();
        for (int i = 0; i < domains.length; i++) {
            String domain = domains[i];
            if(domain != null && domain != "null") {
                String [] f = this.validateFormats(domain);

                ServiceDomainResource sdr = new ServiceDomainResource();
                sdr.setDomain(domain);
                Set formats = new HashSet();
                for(int j = 0; j < f.length; j++) {
                    if (f[j] != null && f[j] != "null")
                        formats.add(f[j]);
                }
                sdr.setFormats(formats);
                sdr.setServiceProvider(newSP);
                sdr.setGetUrl("http://www.b3p.nl/");
                sdr.setPostUrl("http://www.b3p.nl/");
                resources.add(sdr);
            }
        }
        newSP.setDomainResource(resources);
        
        //Now we still need to check for the right Exception formats.....
        String [] exceptions = this.validateExceptions();
        Set exception = new HashSet();
        for(int j = 0; j < exceptions.length; j++) {
            if (exceptions[j] != null && exceptions[j] != "null")
                exception.add(exceptions[j]);
        }
        newSP.setExceptions(exception);
        return newSP;
    }
    // </editor-fold>
    
    /** 
     * Fill the serviceprovider object with predefined static constants.
     *
     * @param serviceProvider The ServiceProvider object that has to be filled.
     */
    // <editor-fold defaultstate="" desc="fillServiceProviderConstants(ServiceProvider serviceProvider) method.">
    private void fillServiceProviderConstants(ServiceProvider serviceProvider) {
        serviceProvider.setName(SERVICE_NAME);
        serviceProvider.setTitle(SERVICE_TITLE);
        serviceProvider.setAbstracts(SERVICE_ABSTRACT);
        serviceProvider.setAccessConstraints(SERVICE_FEES);
        serviceProvider.setFees(SERVICE_CONSTRAINTS);
        
        ContactInformation ci = new ContactInformation();
        ci.setContactPerson(CONTACT_PERSON);
        ci.setContactPosition(CONTACT_POSITION);
        ci.setContactOrganization(CONTACT_ORGANIZATION);
        ci.setAddress(CONTACT_ADDRESS);
        ci.setAddressType(CONTACT_ADDRESS_TYPE);
        ci.setPostcode(CONTACT_POSTCODE);
        ci.setCity(CONTACT_CITY);
        ci.setStateOrProvince(CONTACT_STATE_OR_PROVINCE);
        ci.setCountry(CONTACT_COUNTRY);
        ci.setVoiceTelephone(CONTACT_VOICETELEPHONE);
        ci.setFascimileTelephone(CONTACT_FASCIMILEPHONE);
        ci.setEmailAddress(CONTACT_EMAIL);
        
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
        return (validateFormats(KBConstants.WMS_REQUEST_GetMap).length > 0) &&
               (validateFormats(KBConstants.WMS_REQUEST_GetCapabilities).length > 0) &&
               (validateFormats(KBConstants.WMS_REQUEST_GetFeatureInfo).length > 0) &&
               (validateFormats(KBConstants.WMS_REQUEST_DescribeLayer).length > 0) &&
               (validateFormats(KBConstants.WMS_REQUEST_GetLegendGraphic).length > 0);
    }
    // </editor-fold>
    
    /** 
     * Check whether the set of ServiceProviders all have a GetMap Capability.
     *
     * @return a boolean which is true if all ServiceProviders in the set have a GetMap Capability.
     */
    // <editor-fold defaultstate="" desc="validateGetMapFormat() method.">
    public boolean validateGetMapFormat() {
        return validateFormats(KBConstants.WMS_REQUEST_GetMap).length > 0;
    }
    // </editor-fold>
    
    /** 
     * Check whether the set of ServiceProviders all have a GetCapabilities Capability.
     *
     * @return a boolean which is true if all ServiceProviders in the set have a GetCapabilities Capability.
     */
    // <editor-fold defaultstate="" desc="validateGetCapabilitiesFormat() method.">
    public boolean validateGetCapabilitiesFormat(){
        return validateFormats(KBConstants.WMS_REQUEST_GetCapabilities).length > 0;
    }
    // </editor-fold>
    
    /** 
     * Check whether the set of ServiceProviders all have a GetFeatureInfo Capability.
     *
     * @return a boolean which is true if all ServiceProviders in the set have a GetFeatureInfo Capability.
     */
    // <editor-fold defaultstate="" desc="validateGetFeatureInfoFormat() method.">
    public boolean validateGetFeatureInfoFormat(){
        return validateFormats(KBConstants.WMS_REQUEST_GetFeatureInfo).length > 0;
    }
    // </editor-fold>
    
    /** 
     * Check whether the set of ServiceProviders all have a DescribeLayer Capability.
     *
     * @return a boolean which is true if all ServiceProviders in the set have a DescribeLayer Capability.
     */
    // <editor-fold defaultstate="" desc="validateDescribeLayerFormat() method.">
    public boolean validateDescribeLayerFormat(){
        return validateFormats(KBConstants.WMS_REQUEST_DescribeLayer).length > 0;
    }
    // </editor-fold>
    
    /** 
     * Check whether the set of ServiceProviders all have a GetLegendGraphic Capability.
     *
     * @return a boolean which is true if all ServiceProviders in the set have a GetLegendGraphic Capability.
     */
    // <editor-fold defaultstate="" desc="validateGetLegendGraphicFormat() method.">
    public boolean validateGetLegendGraphicFormat(){
        return validateFormats(KBConstants.WMS_REQUEST_GetLegendGraphic).length > 0;
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
            ServiceProvider sp = (ServiceProvider)serviceProviderIterator.next();
            Iterator exceptionIterator = sp.getExceptions().iterator();
            while(exceptionIterator.hasNext()) {
                String exception = (String)exceptionIterator.next();
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
            ServiceProvider sp = (ServiceProvider)serviceProviderIterator.next();
            Iterator domainResourceIterator = sp.getDomainResource().iterator();
            while(domainResourceIterator.hasNext()) {
                ServiceDomainResource sdr = (ServiceDomainResource)domainResourceIterator.next();
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
            ServiceProvider sp = (ServiceProvider)serviceProviderIterator.next();
            Iterator domainResourceIterator = sp.getDomainResource().iterator();
            while(domainResourceIterator.hasNext()) {
                ServiceDomainResource sdr = (ServiceDomainResource)domainResourceIterator.next();
                if (sdr.getDomain().equalsIgnoreCase(domain)) {
                    Set formats= sdr.getFormats();
                    Iterator itf = formats.iterator();
                    while(itf.hasNext()){
                        String format=(String)itf.next();
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
    private String [] formats(HashMap hashMap) {
        ArrayList supportedFormats = new ArrayList();
        Iterator it = hashMap.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            int i = ((Integer)entry.getValue()).intValue();
            if (i >= serviceProviders.size()) {
                supportedFormats.add((String)entry.getKey());                
            }
        }
        String [] formats = new String[supportedFormats.size() + 1];
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
    private void hashmapFilter(HashMap results, String filterValue){
        if (results.containsKey(filterValue)) {
            int i = ((Integer)results.get(filterValue)).intValue() + 1;
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