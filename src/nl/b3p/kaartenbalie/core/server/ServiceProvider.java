/**
 * @(#)ServiceProvider.java
 * @author N. de Goeij
 * @version 1.00 2006/10/11
 *
 * Purpose: Bean representing a ServiceProvider.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.core.server;

import java.util.Date;
import java.util.Hashtable;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class ServiceProvider implements XMLElement {
    
    private Integer id;
    private String name;
    private String title;
    private String abstracts;
    private String fees;
    private String accessConstraints;
    private String givenName;
    private String url;
    private Date updatedDate;
    private boolean reviewed;
    private ContactInformation contactInformation = new ContactInformation();
    private Set <ServiceDomainResource> domainResource;
    private Set <String> serviceProviderKeywordList;
    private Set <String> exceptions;
    private Set <Layer> layers;
    
    /** default ServiceProvider() constructor.
     */
    // <editor-fold defaultstate="" desc="default ServiceProvider() constructor">
    public ServiceProvider() {
        name = "OGC:WMS";
        title = "Kaartenbalie Map Portal";
        abstracts = "WMS-based access to different maps. Try B3Partners Portal System at http://www.b3p.nl/";
        fees = "None";
        accessConstraints = "None";
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="" desc="getter and setter methods.">
    public Integer getId() {
        return id;
    }
    
    private void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAbstracts() {
        return abstracts;
    }
    
    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }
    
    public String getFees() {
        return fees;
    }
    
    public void setFees(String fees) {
        this.fees = fees;
    }
    
    public String getAccessConstraints() {
        return accessConstraints;
    }
    
    public void setAccessConstraints(String accessConstraints) {
        this.accessConstraints = accessConstraints;
    }
    
    public String getGivenName() {
        return givenName;
    }
    
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public Date getUpdatedDate() {
        return updatedDate;
    }
    
    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
    
    public boolean isReviewed() {
        return reviewed;
    }
    
    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }
    
    public ContactInformation getContactInformation() {
        return contactInformation;
    }
    
    public void setContactInformation(ContactInformation contactInformation) {
        this.contactInformation = contactInformation;
        contactInformation.setServiceProvider(this);
    }
    
    public Set getExceptions() {
        return exceptions;
    }
    
    public void setExceptions(Set <String> exceptions) {
        this.exceptions = exceptions;
    }
    
    public void addException(String except) {
        if (exceptions == null) {
            exceptions = new HashSet <String>();
        }
        exceptions.add(except);
    }
    
    public Set getDomainResource() {
        return domainResource;
    }
    
    public void setDomainResource(Set <ServiceDomainResource> domainResource) {
        this.domainResource = domainResource;
    }
    
    public void addDomainResource(ServiceDomainResource dr) {
        if (domainResource == null) {
            domainResource = new HashSet <ServiceDomainResource>();
        }
        domainResource.add(dr);
        dr.setServiceProvider(this);
    }
    
    public Set getLayers() {
        return layers;
    }
    
    public void setLayers(Set <Layer> layers) {
        this.layers = layers;
    }
    
    public void addLayer(Layer layer) {
        if(null == layers) {
            layers = new HashSet <Layer>();
        }
        layers.add(layer);
        layer.setServiceProvider(this);
    }
    public Layer getTopLayer(){
        Set set= getLayers();
        if (set!=null){
            Iterator it=set.iterator();
            while (it.hasNext()){
                Layer layer=(Layer)it.next();
                if (layer.getParent()==null){
                    return layer;
                }
            }
        }
        return null;
    }
    public Set getServiceProviderKeywordList() {
        return serviceProviderKeywordList;
    }
    
    public void setServiceProviderKeywordList(Set <String> serviceProviderKeywordList) {
        this.serviceProviderKeywordList = serviceProviderKeywordList;
    }
    
    public void addKeyword(String keyword) {
        if(null == serviceProviderKeywordList) {
            serviceProviderKeywordList = new HashSet <String>();
        }
        serviceProviderKeywordList.add(keyword);
    }
    // </editor-fold>
    
    /** Method that will overwrite the URL's stored in the database with the URL specified for Kaartenbalie.
     * This new URL indicate the link to the kaartenbalie, while the old link is used to indicate the URL
     * to the real location of the service. Because the client which is connected to kaartenbalie has to send
     * his requests back to kaartenbalie and not directly to the official resource, the URL has to be replaced.
     *
     * @param newUrl String representing the URL the old URL has to be replaced with.
     */
    // <editor-fold defaultstate="" desc="overwriteURL(String newUrl) method">
    public void overwriteURL(String newUrl) {
        //String temporaryURL = newUrl;
        //int firstOccur = temporaryURL.indexOf("servlet");
        //if(firstOccur != -1) {
        //    temporaryURL = temporaryURL.substring(0, firstOccur);
        //save this new URL as the one to be used
        //    this.url = temporaryURL.replace("&", "&amp;");
        this.url = newUrl;
        //}
        
        //Now call the same method in the underlying objects which are stored in this object
        Iterator it;
        //ServiceDomainResource:
        it = domainResource.iterator();
        while (it.hasNext()) {
            ServiceDomainResource sdr = (ServiceDomainResource)it.next();
            sdr.overwriteURL(newUrl);
        }
        
        //Layers:
        it = layers.iterator();
        while (it.hasNext()) {
            Layer layer = (Layer)it.next();
            layer.overwriteURL(newUrl);
        }
    }
    // </editor-fold>
    
    /** Method that will create a deep copy of this object.
     *
     * @return an object of type Object
     */
    // <editor-fold defaultstate="" desc="clone() method">
    public Object clone() {
        ServiceProvider cloneSP = new ServiceProvider();
        
        if (null != this.id) {
            cloneSP.id                          = new Integer(this.id.intValue());
        }
        if (null != this.name) {
            cloneSP.name                        = new String(this.name);
        }
        if (null != this.title) {
            cloneSP.title                       = new String(this.title);
        }
        if (null != this.abstracts) {
            cloneSP.abstracts                   = new String(this.abstracts);
        }
        if (null != this.fees) {
            cloneSP.fees                        = new String(this.fees);
        }
        if (null != this.accessConstraints) {
            cloneSP.accessConstraints           = new String(this.accessConstraints);
        }
        if (null != this.givenName) {
            cloneSP.givenName                   = new String(this.givenName);
        }
        if (null != this.url) {
            cloneSP.url                         = new String(this.url);
        }
        if (null != this.updatedDate) {
            cloneSP.updatedDate                 = (Date)this.updatedDate.clone();
        }
        if (null != this.contactInformation) {
            cloneSP.contactInformation          = (ContactInformation)this.contactInformation.clone();
        }
        if (null != this.domainResource) {
            cloneSP.domainResource              = new HashSet <ServiceDomainResource>();
            Iterator it = this.domainResource.iterator();
            while (it.hasNext()) {
                ServiceDomainResource sdr       = (ServiceDomainResource)((ServiceDomainResource)it.next()).clone();
                sdr.setServiceProvider(cloneSP);
                cloneSP.domainResource.add(sdr);
            }
        }
        if (null != this.serviceProviderKeywordList) {
            cloneSP.serviceProviderKeywordList  = new HashSet <String>(this.serviceProviderKeywordList);
        }
        if (null != this.exceptions) {
            cloneSP.exceptions                  = new HashSet <String>(this.exceptions);
        }
        if (null != this.layers) {
            cloneSP.layers = new HashSet <Layer>();
            Iterator itlayer = this.layers.iterator();
            while (itlayer.hasNext()) {
                Layer layer = (Layer)((Layer)itlayer.next()).clone();
                layer.setServiceProvider(cloneSP);
                cloneSP.layers.add(layer);
            }
        }
        cloneSP.reviewed = this.reviewed;
        return cloneSP;
    }
    // </editor-fold>
    
    /** Method that will create piece of the XML tree to create a proper XML docuement.
     *
     * @param doc Document object which is being used to create new Elements
     * @param rootElement The element where this object belongs to.
     *
     * @return an object of type Element
     */
    // <editor-fold defaultstate="" desc="toElement(Document doc, Element rootElement) method">
    public Element toElement(Document doc, Element rootElement) {
        
        Element serviceElement = doc.createElement("Service");
        
        if(null != this.getName()) {
            Element nameElement = doc.createElement("Name");
            Text text = doc.createTextNode(this.getName());
            nameElement.appendChild(text);
            serviceElement.appendChild(nameElement);
        }
        
        if (null != this.getTitle()) {
            Element titleElement = doc.createElement("Title");
            Text text = doc.createTextNode(this.getTitle());
            titleElement.appendChild(text);
            serviceElement.appendChild(titleElement);
        }
        if (null != this.getAbstracts()) {
            Element abstractElement = doc.createElement("Abstract");
            Text text = doc.createTextNode(this.getAbstracts());
            abstractElement.appendChild(text);
            serviceElement.appendChild(abstractElement);
        }
/*        
        if(null != this.getServiceProviderKeywordList() && this.getServiceProviderKeywordList().size() != 0) {
            Element keywordListElement = doc.createElement("KeywordList");
            
            Iterator it = this.getServiceProviderKeywordList().iterator();
            while (it.hasNext()) {
                String keyword = (String)it.next();
                Element keywordElement = doc.createElement("Keyword");
                Text text = doc.createTextNode(keyword);
                keywordElement.appendChild(text);
                keywordListElement.appendChild(keywordElement);
            }
            
            serviceElement.appendChild(keywordListElement);
        }
        
        if (null != this.getUrl()) {
            Element onlineElement = doc.createElement("OnlineResource");
            onlineElement.setAttribute("xlink:href", this.getUrl());
            onlineElement.setAttribute("xlink:type", "simple");
            onlineElement.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
            serviceElement.appendChild(onlineElement);
        }
        
        if (null != this.getContactInformation()) {
            serviceElement = this.getContactInformation().toElement(doc, serviceElement);
        }
        
        if (null != this.getFees()) {
            Element feesElement = doc.createElement("Fees");
            Text text = doc.createTextNode(this.getFees());
            feesElement.appendChild(text);
            serviceElement.appendChild(feesElement);
        }
        if (null != this.getAccessConstraints()) {
            Element accessElement = doc.createElement("AccessConstraints");
            Text text = doc.createTextNode(this.getAccessConstraints());
            accessElement.appendChild(text);
            serviceElement.appendChild(accessElement);
        }
 */
        //End of Service
        
        //Start of Capability
        Element capabilityElement = doc.createElement("Capability");
        
        //De verschillende request mogelijkheden....
        Element requestElement = doc.createElement("Request");
        
        Hashtable sdrhash = new Hashtable();
        ServiceDomainResource sdr = null;
        Iterator it = domainResource.iterator();
        while (it.hasNext()) {
            sdr = (ServiceDomainResource)it.next();
            sdrhash.put(sdr.getDomain(),sdr);
        }
        sdr = (ServiceDomainResource) sdrhash.get("GetCapabilities");
        requestElement = sdr.toElement(doc, requestElement);
        sdr = (ServiceDomainResource) sdrhash.get("GetMap");
        requestElement = sdr.toElement(doc, requestElement);
        
        sdr = (ServiceDomainResource) sdrhash.get("GetFeatureInfo");
        if (sdr!=null)
            requestElement = sdr.toElement(doc, requestElement);
        sdr = (ServiceDomainResource) sdrhash.get("DescribeLayer");
        if (sdr!=null)
            requestElement = sdr.toElement(doc, requestElement);
        sdr = (ServiceDomainResource) sdrhash.get("GetLegendGraphic");
        if (sdr!=null)
            requestElement = sdr.toElement(doc, requestElement);
        sdr = (ServiceDomainResource) sdrhash.get("GetStyles");
        if (sdr!=null)
            requestElement = sdr.toElement(doc, requestElement);
        capabilityElement.appendChild(requestElement);
        
        //De exception formaten
        Element exceptionElement = doc.createElement("Exception");
        it = exceptions.iterator();
        while (it.hasNext()) {
            Element formatElement = doc.createElement("Format");
            Text text = doc.createTextNode((String)it.next());
            formatElement.appendChild(text);
            exceptionElement.appendChild(formatElement);
        }
        capabilityElement.appendChild(exceptionElement);
        
        //De beschikbare layers.
        it = layers.iterator();
        while (it.hasNext()) {
            Layer layer = (Layer)it.next();
            capabilityElement = layer.toElement(doc, capabilityElement);
        }
        //End of Capability
        
        rootElement.appendChild(serviceElement);
        rootElement.appendChild(capabilityElement);
        return rootElement;
    }
    // </editor-fold>
}