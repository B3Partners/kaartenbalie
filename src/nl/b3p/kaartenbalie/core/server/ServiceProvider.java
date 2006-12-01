/*
 * ServiceProvider.java
 *
 * Created on 18 september 2006, 11:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

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
    
    protected void overwriteURL(String newUrl) {
    	//The given URL is actually to be used for requests
    	//In order to define the original URL to access the website itself we need to cutt off the last part
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

    public String toString() {
    	String tabulator = "\t";
        Iterator it;
        
        StringBuilder result = new StringBuilder();
    	final String newLine = System.getProperty("line.separator");
    	
        //Bouw eerst het gedeelte van de Service op en ga daarna verder met de Capabilities....
        //Na alle opbouw moet er nog een controle uitgevoerd worden, oftewel iedere waarde moet eerst van
        //gekeken worden of deze beschikbaar is, zo niet dan ook niet toevoegen aan het document.
        result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        result.append("<WMT_MS_Capabilities version=\"1.1.1\" updateSequence=\"0\">\n");
        //Start of Service
        result.append(tabulator + "<Service>\n");
        
        if(null != this.getName()) {
            result.append(tabulator + "\t<Name>" + this.getName() + "</Name>\n");
        }
        if (null != this.getTitle()) {
            result.append(tabulator + "\t<Title>" + this.getTitle() + "</Title>\n");
        }
        if (null != this.getAbstracts()) {
            result.append(tabulator + "\t<Abstract>" + this.getAbstracts() + "</Abstract>\n");
        }
        if (null != this.getServiceProviderKeywordList() && this.getServiceProviderKeywordList().size() != 0) {
            result.append(tabulator + "\t<KeywordList>\n");
            it = serviceProviderKeywordList.iterator();
            while (it.hasNext()) {
                    String keyword = (String)it.next();
                    result.append("\t\t<Keyword>" + keyword + "</Keyword>\n");
            }
            result.append(tabulator + "\t</KeywordList>\n");
        }
        if(null != this.getUrl()) {
            result.append(tabulator + "\t<OnlineResource xmlns:xlink=\"http://www.w3.org/1999/xlink\" xlink:type=\"simple\" xlink:href=\"" + 
				this.getUrl() + "\" />\n");
        }
        if (null != this.getContactInformation()) {
            result.append(this.getContactInformation().toString(tabulator + "\t"));
        }       
        if (null != this.getFees()) {
            result.append(tabulator + "<Fees>" + this.getFees() + "</Fees>\n");
        }
        if (null != this.getAccessConstraints()) {
            result.append(tabulator + "<AccessConstraints>" + this.getAccessConstraints() + "</AccessConstraints>\n");
        }
        result.append(tabulator + "</Service>\n");
        //End of Service
        
        //Start of Capability
        
        result.append(tabulator + "<Capability>\n");
        
        //De verschillende request mogelijkheden....
        result.append(tabulator + "\t<Request>\n");
        it = domainResource.iterator();
    	while (it.hasNext()) {
            ServiceDomainResource sdr = (ServiceDomainResource)it.next();
            result.append(sdr.toString(tabulator + "\t\t"));
    	}
        result.append(tabulator + "\t</Request>\n");
        
        //De exception formaten
        result.append(tabulator + "\t<Exception>\n");
        it = exceptions.iterator();
    	while (it.hasNext()) {
            String except = (String)it.next();
            result.append(tabulator + "\t\t<Format>" + except + "</Format>\n");
    	}
        result.append(tabulator + "\t</Exception>\n");
        
        //De beschikbare layers...
        it = layers.iterator();
    	while (it.hasNext()) {
            Layer layer = (Layer)it.next();
            result.append(layer.toString(tabulator + "\t"));
    	}
        
        result.append(tabulator + "</Capability>\n");
        //End of Capability
        result.append("</WMT_MS_Capabilities>");
        
        
        
        

        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        /*
    	StringBuilder result = new StringBuilder();
    	final String newLine = System.getProperty("line.separator");
    	
    	result.append(" Name: ServiceProvider");
    	result.append(newLine);
    	
    	result.append(" ID: ");
    	result.append(id);
    	result.append(newLine);
    	
    	result.append(" Name: ");
    	result.append(name);
    	result.append(newLine);
    	
    	result.append(" Title: ");
    	result.append(title);
    	result.append(newLine);
    	
    	result.append(" Abstracts: ");
    	result.append(abstracts);
    	result.append(newLine);
    	
    	result.append(" Fees: ");
    	result.append(fees);
    	result.append(newLine);
    	
    	result.append(" AccessConstraints: ");
    	result.append(accessConstraints);
    	result.append(newLine);
    	
    	result.append(" GivenName: ");
    	result.append(givenName);
    	result.append(newLine);
    	
    	result.append(" URL: ");
    	result.append(url);
    	result.append(newLine);
    	
    	result.append(" UpdatedDate: ");
    	result.append(updatedDate);
    	result.append(newLine);
    	
    	result.append(" ContactInformation: ");
	    result.append(contactInformation.toString());
    	result.append(newLine);
    	
    	Iterator it;
    	
    	it = domainResource.iterator();
    	while (it.hasNext()) {
    		ServiceDomainResource sdr = (ServiceDomainResource)it.next();
    		result.append(" ServiceDomainResource: ");
    		result.append(sdr.toString());
    		result.append(newLine);
    	}
    	
    	it = serviceProviderKeywordList.iterator();
    	while (it.hasNext()) {
    		String keyword = (String)it.next();
    		result.append(" Keyword: ");
    		result.append(keyword);
    		result.append(newLine);
    	}
        
        it = exceptions.iterator();
    	while (it.hasNext()) {
    		String except = (String)it.next();
    		result.append(" Exception: ");
    		result.append(except);
    		result.append(newLine);
    	}
    	
//    	result.append(" ExceptionFormats: ");
//    	result.append(exceptions.toString());
//    	result.append(newLine);
    	
    	result.append(" ---------------------------------------- ");
    	result.append(newLine);
    	result.append(" Known layers by this ServiceProvider: " + layers.size());
    	result.append(newLine);
    	result.append(" ---------------------------------------- ");
    	result.append(newLine);
    	   	    	
    	it = layers.iterator();
    	while (it.hasNext()) {
    		Layer layer = (Layer)it.next();
    		result.append(" Layer: ");
    		result.append(layer.toString());
    		result.append(newLine);
    	}
    	
    	result.append("}");
    	*/
    	return result.toString();
        
    }
}