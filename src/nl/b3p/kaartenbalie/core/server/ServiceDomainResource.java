/*
 * DomainResource.java
 *
 * Created on 11 oktober 2006, 15:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server;

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
public class ServiceDomainResource {
    
    private Integer id;
    private Set formats;
    private String getUrl;
    private String postUrl;
    private String domain;
    private ServiceProvider serviceProvider;

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public Set getFormats() {
        return formats;
    }

    public void setFormats(Set formats) {
        this.formats = formats;
    }
    
    public void addFormat(String f) {
        if (null == formats) {
            formats = new HashSet();
        }
        formats.add(f);
    }

    public String getGetUrl() {
        return getUrl;
    }

    public void setGetUrl(String getUrl) {
        this.getUrl = getUrl;
    }
    
    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }
    
    public Object clone() {
        ServiceDomainResource cloneSDR      = new ServiceDomainResource();
        if (null != this.id) {
            cloneSDR.id                     = new Integer(this.id);
        }
        if (null != this.formats) {
            cloneSDR.formats                = new HashSet(this.formats);
        }
        if (null != this.getUrl) {
            cloneSDR.getUrl                 = new String(this.getUrl);
        }
        if (null != this.postUrl) {
            cloneSDR.postUrl                = new String(this.postUrl);
        }
        if (null != this.domain) {
            cloneSDR.domain                 = new String(this.domain);
        }
        return cloneSDR;
    }
    
    protected void overwriteURL(String newUrl) {
        //First cut off only the part which is in front of the question mark.
        /*
        String temporaryURL;
        temporaryURL = this.getGetUrl();
        if (null != temporaryURL && !temporaryURL.equals("")) {
            int firstOccur = temporaryURL.indexOf("?");
            if(firstOccur != -1) {
                temporaryURL = temporaryURL.substring(firstOccur);
                //then add the newly given url in front of the cutted part
                temporaryURL = newUrl + temporaryURL;
                //save this new URL as the one to be used
                temporaryURL = temporaryURL.replace("&", "");
                this.setGetUrl(temporaryURL);
            } else {*/
                this.setGetUrl(newUrl);
//            }
//        }
        
        //With a post URL no question mark will be found because the data will appear in the message body and
        //not in the URL itself.
        /*
        temporaryURL = this.getPostUrl();
        if (null != temporaryURL && !temporaryURL.equals("")) {
            int firstOccur = temporaryURL.indexOf("?");
            if(firstOccur != -1) {
                temporaryURL = temporaryURL.substring(firstOccur);
                //then add the newly given url in front of the cutted part
                temporaryURL = newUrl + temporaryURL;
                //save this new URL as the one to be used
                temporaryURL = temporaryURL.replace("&", "");
                this.setGetUrl(temporaryURL);
            } else {*/
                this.setPostUrl(newUrl);
//            }
//        }
        
        
    }
    
    /*
    protected void overwriteURL(String newUrl) {
    	//First cut off only the part which is in front of the question mark.
    	
    	String temporaryURL;
    	temporaryURL = this.getGetUrl();
    	if (null != temporaryURL && !temporaryURL.equals("")) {
	    	int firstOccur = temporaryURL.indexOf("?");
	    	if(firstOccur != -1) {
	    		temporaryURL = temporaryURL.substring(firstOccur);
	    		//then add the newly given url in front of the cutted part
		    	temporaryURL = newUrl + temporaryURL;
		    	//save this new URL as the one to be used
		    	temporaryURL = temporaryURL.replace("&", "&amp;");
		    	this.setGetUrl(temporaryURL);
	    	} else {
	    		this.setGetUrl(newUrl);
	    	}
	    }
	    
	    //With a post URL no question mark will be found because the data will appear in the message body and
	    //not in the URL itself.	    
	    
	    temporaryURL = this.getPostUrl();
    	if (null != temporaryURL && !temporaryURL.equals("")) {
	    	int firstOccur = temporaryURL.indexOf("?");
	    	if(firstOccur != -1) {
	    		temporaryURL = temporaryURL.substring(firstOccur);
	    		//then add the newly given url in front of the cutted part
		    	temporaryURL = newUrl + temporaryURL;
		    	//save this new URL as the one to be used
		    	temporaryURL = temporaryURL.replace("&", "&amp;");
		    	this.setGetUrl(temporaryURL);
	    	} else {
	    		this.setPostUrl(newUrl);
	    	}
	    }

	    
    }*/
        
    Element toElement(Document doc) {
        Element rootElement = doc.createElement(this.getDomain());
        if (null != this.getFormats() && this.getFormats().size() != 0) {
            Iterator it = formats.iterator();
            while (it.hasNext()) {
                Element element = doc.createElement("Format");
                Text text = doc.createTextNode((String)it.next());
                element.appendChild(text);
                rootElement.appendChild(element);
            }
        }
        
        Element element = null;
        if (null != this.getGetUrl()) {
            Element subElement = doc.createElement("OnlineResource");
            subElement.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:type", "simple");
            subElement.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", this.getGetUrl());
            element = doc.createElement("Get");
            element.appendChild(subElement);
            
        }
        if (null != this.getPostUrl()) {
            Element subElement = doc.createElement("OnlineResource");
            subElement.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:type", "simple");
            subElement.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", this.getPostUrl());
            element = doc.createElement("Post");
            element.appendChild(subElement);
            
        }
        if (element!=null) {
            Element subElement = element;
            element = doc.createElement("HTTP");
            element.appendChild(subElement);
            
            subElement = element;
            element = doc.createElement("DCPType");
            element.appendChild(subElement);
            rootElement.appendChild(element);
        }
        
        return rootElement;
    }
    
}