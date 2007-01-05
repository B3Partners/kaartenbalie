/*
 * ServiceProvider.java
 *
 * Created on 18 september 2006, 11:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 *
 * @author Nando De Goeij
 */
public class ServiceProvider {
    
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
    private Set domainResource = new HashSet();
    private Set serviceProviderKeywordList = new HashSet();
    private Set exceptions = new HashSet();
    private Set layers;// = new HashSet();
    
    public ServiceProvider() {
        name = "OGC:WMS";
        title = "Kaartenbalie Map Portal";
        abstracts = "WMS-based access to different maps. Try B3Partners Portal System at http://www.b3p.nl/";
        fees = "None";
        accessConstraints = "None";
    }
    
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
    
    public void setExceptions(Set exceptions) {
        this.exceptions = exceptions;
    }
    
    public void addException(String except) {
        exceptions.add(except);
    }
    
    public Set getDomainResource() {
        return domainResource;
    }
    
    public void setDomainResource(Set domainResource) {
        this.domainResource = domainResource;
    }
    
    public void addDomainResource(ServiceDomainResource dr) {
        domainResource.add(dr);
        dr.setServiceProvider(this);
    }
    
    public Set getLayers() {
        return layers;
    }
    
    public void setLayers(Set layers) {
        this.layers = layers;
    }
    
    public void addLayer(Layer layer) {
        if(null == layers) {
            layers = new HashSet();
        }
        layers.add(layer);
        layer.setServiceProvider(this);
    }
    
    public Set getServiceProviderKeywordList() {
        return serviceProviderKeywordList;
    }
    
    public void setServiceProviderKeywordList(Set serviceProviderKeywordList) {
        this.serviceProviderKeywordList = serviceProviderKeywordList;
    }
    
    public void addKeyword(String keyword) {
        serviceProviderKeywordList.add(keyword);
    }
    
    public void overwriteURL(String newUrl) {
        //The given URL is actually to be used for requests
        //In order to define the original URL to access the website itself we need to cutt off the last part
        
        // TODO check of dit zonder servlet string kan, pad onafhankelijk
        // documentatie hoe dit werkt
        String temporaryURL = newUrl;
        int firstOccur = temporaryURL.indexOf("servlet");
        if(firstOccur != -1) {
            temporaryURL = temporaryURL.substring(0, firstOccur);
            //save this new URL as the one to be used
            this.url = temporaryURL.replace("&", "&amp;");
        }
        
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
            cloneSP.domainResource              = new HashSet();
            Iterator it = this.domainResource.iterator();
            while (it.hasNext()) {
                ServiceDomainResource sdr       = (ServiceDomainResource)((ServiceDomainResource)it.next()).clone();
                sdr.setServiceProvider(cloneSP);
                cloneSP.domainResource.add(sdr);
            }
        }
        if (null != this.serviceProviderKeywordList) {
            cloneSP.serviceProviderKeywordList  = new HashSet(this.serviceProviderKeywordList);
        }
        if (null != this.exceptions) {
            cloneSP.exceptions                  = new HashSet(this.exceptions);
        }
        if (null != this.layers) {
            cloneSP.layers = new HashSet();
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
    
    public Element toElement(Document doc) {
        //Bouw eerst het gedeelte van de Service op en ga daarna verder met de Capabilities....
        //Na alle opbouw moet er nog een controle uitgevoerd worden, oftewel iedere waarde moet eerst van
        //gekeken worden of deze beschikbaar is, zo niet dan ook niet toevoegen aan het document.
        Element rootElement = doc.createElement("WMT_MS_Capabilities");
        rootElement.setAttribute("version", "1.1.1");
        rootElement.setAttribute("updateSequence", "0");
        
        Element element = doc.createElement("Service");
        rootElement.appendChild(element);
        
        if(null != this.getName()) {
            Element subElement = doc.createElement("Name");
            Text text = doc.createTextNode(this.getName());
            subElement.appendChild(text);
            element.appendChild(subElement);
        }
        if (null != this.getTitle()) {
            Element subElement = doc.createElement("Title");
            Text text = doc.createTextNode(this.getTitle());
            subElement.appendChild(text);
            element.appendChild(subElement);
        }
        if (null != this.getAbstracts()) {
            Element subElement = doc.createElement("Abstract");
            Text text = doc.createTextNode(this.getAbstracts());
            subElement.appendChild(text);
            element.appendChild(subElement);
        }
        
        if(null != this.getServiceProviderKeywordList() && this.getServiceProviderKeywordList().size() != 0) {
            Element subElement = doc.createElement("KeywordList");
            element.appendChild(subElement);
            Iterator it = this.getServiceProviderKeywordList().iterator();
            while (it.hasNext()) {
                String keyword = (String)it.next();
                Element sub2Element = doc.createElement("Keyword");
                Text text = doc.createTextNode(keyword);
                sub2Element.appendChild(text);
                subElement.appendChild(sub2Element);
            }
        }
        
        if (null != this.getUrl()) {
            Element subElement = doc.createElement("OnlineResource");
            element.appendChild(subElement);
            
            subElement.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:type", "simple");
            subElement.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", this.getUrl());
        }
        
        if (null != this.getContactInformation()) {
            element.appendChild(this.getContactInformation().toElement(doc));
        }
        
        if (null != this.getFees()) {
            Element subElement = doc.createElement("Fees");
            Text text = doc.createTextNode(this.getFees());
            subElement.appendChild(text);
            element.appendChild(subElement);
        }
        if (null != this.getAccessConstraints()) {
            Element subElement = doc.createElement("AccessConstraints");
            Text text = doc.createTextNode(this.getAccessConstraints());
            subElement.appendChild(text);
            element.appendChild(subElement);
        }
        //End of Service
        
        //Start of Capability
        element = doc.createElement("Capability");
        rootElement.appendChild(element);
        
        //De verschillende request mogelijkheden....
        Element subElement = doc.createElement("Request");
        element.appendChild(subElement);
        Iterator it = domainResource.iterator();
        while (it.hasNext()) {
            ServiceDomainResource sdr = (ServiceDomainResource)it.next();
            subElement.appendChild(sdr.toElement(doc));
        }
        
        //De exception formaten
        element = doc.createElement("Exception");
        rootElement.appendChild(element);
        it = exceptions.iterator();
        while (it.hasNext()) {
            subElement = doc.createElement("Format");
            Text text = doc.createTextNode((String)it.next());
            subElement.appendChild(text);
            element.appendChild(subElement);
        }
        
        //De beschikbare layers...
        it = layers.iterator();
        while (it.hasNext()) {
            Layer layer = (Layer)it.next();
            rootElement.appendChild(layer.toElement(doc));
        }
        
        //End of Capability
        
        return rootElement;
    }
    
}