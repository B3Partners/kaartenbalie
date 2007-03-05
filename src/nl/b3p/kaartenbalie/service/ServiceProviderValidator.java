/*
 * ServiceProviderValidator.java
 *
 * Created on 2 maart 2007, 16:13
 *
 * Autor: Roy
 */

package nl.b3p.kaartenbalie.service;

import java.text.Format;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import nl.b3p.kaartenbalie.core.KBConstants;
import nl.b3p.kaartenbalie.core.server.ServiceDomainResource;
import nl.b3p.kaartenbalie.core.server.ServiceProvider;

/**
 *
 * @author Roy
 */
public class ServiceProviderValidator {
    
    private Set <ServiceProvider> serviceProviders;
    /** Creates a new instance of ServiceProviderValidator */
    public ServiceProviderValidator() {
    }
    
    /** Creates a new instance of ServiceProviderValidator */
    public ServiceProviderValidator(Set <ServiceProvider> serviceProviders) {
        setServiceProviders(serviceProviders);
    }
    /* Getters and setters*/
    //<editor-fold defaultstate="collapsed">
    public Set<ServiceProvider> getServiceProviders() {
        return serviceProviders;
    }

    public void setServiceProviders(Set<ServiceProvider> serviceProviders) {
        this.serviceProviders = serviceProviders;
    }
    //</editor-fold>
    
    /** Validate GetmapFormats
     */
    public boolean validateGetMapFormat() {
        return validateFormats(KBConstants.WMS_REQUEST_GetMap).length > 0;
    }
    /**validate GetCapabilitiesFormat
     * @return true if there is at least 1 combined format
     */
    public boolean validateGetCapabilitiesFormat(){
        return validateFormats(KBConstants.WMS_REQUEST_GetCapabilities).length > 0;
    }
    /**validate GetFeatureInfoFormat
     *@return true if there is at least 1 combined format
     */
    public boolean validateGetFeatureInfoFormat(){
        return validateFormats(KBConstants.WMS_REQUEST_GetFeatureInfo).length > 0;
    }
    /**validate DescribeLayerFormat
     *@return true if there is at least 1 combined format
     */
    public boolean validateDescribeLayerFormat(){
        return validateFormats(KBConstants.WMS_REQUEST_DescribeLayer).length > 0;
    }
    /** validate GetLegendGraphicFormat
     *@return true if there is at least 1 combined format
     */
    public boolean validateGetLegendGraphicFormat(){
        return validateFormats(KBConstants.WMS_REQUEST_GetLegendGraphic).length > 0;
    }
    
    /** Check the formats that are supported by all the serviceproviders
     * @param domain The name of the domain to be checked.
     * @return String[] with only the formats that are supported by all the serviceproviders.
     */
    public String[] validateFormats(String domain){
        HashMap hm= new HashMap();
        Iterator its=serviceProviders.iterator();
        while (its.hasNext()){
            ServiceProvider sp= (ServiceProvider)its.next();
            Iterator itd=sp.getDomainResource().iterator();
            while(itd.hasNext()){
                ServiceDomainResource sdr= (ServiceDomainResource)itd.next();
                if (sdr.getDomain().equalsIgnoreCase(domain)){
                    Set <String> formats= sdr.getFormats();
                    Iterator itf = formats.iterator();
                    while(itf.hasNext()){
                        String format=(String)itf.next();
                        addFormatCount(hm,format);
                    }
                }
            }            
        }
        ArrayList supportedFormats=new ArrayList();
        Iterator it=hm.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry entry=(Map.Entry)it.next();
            int i= ((Integer)entry.getValue()).intValue();
            if (i>=serviceProviders.size()){
                supportedFormats.add((String)entry.getKey());                
            }
        }
        String[] returnValue= new String[supportedFormats.size()];
        for (int i=0; i < returnValue.length; i ++){
            if(supportedFormats.get(i)!=null)
                returnValue[i]=(String)supportedFormats.get(i);
        }
        return returnValue;
    }
    /** Methode that counts the Formats. Each format is counted sepperated
     * @parameter hm The hashmap that contains the counted formats
     * @parameter format The format to add to the count.
     */
    private void addFormatCount(HashMap hm, String format){
        if (hm.containsKey(format)){
            int i= ((Integer)hm.get(format)).intValue()+1;
            hm.put(format,new Integer(i));
        }else{
            hm.put(format,new Integer("1"));
        }
    }
}
