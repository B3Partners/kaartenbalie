/**
 * @(#)ServiceDomainResource.java
 * @author N. de Goeij
 * @version 1.00 2006/10/11
 *
 * Purpose: Bean representing a ServiceDomainResource.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.core.server;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class ServiceDomainResource implements XMLElement {
    
    private Integer id;
    private Set <String> formats;
    private String getUrl;
    private String postUrl;
    private String domain;
    private ServiceProvider serviceProvider;

    // <editor-fold defaultstate="" desc="getter and setter methods.">
    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public Set getFormats() {
        return formats;
    }

    public void setFormats(Set <String> formats) {
        this.formats = formats;
    }
    
    public void addFormat(String f) {
        if (null == formats) {
            formats = new HashSet <String>();
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
    // </editor-fold>
    
    /** Method that will create a deep copy of this object.
     *
     * @return an object of type Object
     */
    // <editor-fold defaultstate="" desc="clone() method">
    public Object clone() {
        ServiceDomainResource cloneSDR      = new ServiceDomainResource();
        if (null != this.id) {
            cloneSDR.id                     = new Integer(this.id);
        }
        if (null != this.formats) {
            cloneSDR.formats                = new HashSet <String>(this.formats);
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
    // </editor-fold>
    
    /** Method that will overwrite the URL's stored in the database with the URL specified for Kaartenbalie.
     * This new URL indicate the link to the kaartenbalie, while the old link is used to indicate the URL
     * to the real location of the service. Because the client which is connected to kaartenbalie has to send
     * his requests back to kaartenbalie and not directly to the official resource, the URL has to be replaced.
     *
     * @param newUrl String representing the URL the old URL has to be replaced with.
     */
    // <editor-fold defaultstate="" desc="overwriteURL(String newUrl) method">
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
        Element domainElement = doc.createElement(this.getDomain());
        if (null != this.getFormats() && this.getFormats().size() != 0) {
            Iterator it = formats.iterator();
            while (it.hasNext()) {
                Element formatElement = doc.createElement("Format");
                Text text = doc.createTextNode((String)it.next());
                formatElement.appendChild(text);
                domainElement.appendChild(formatElement);
            }
        }
        
        if (null != this.getGetUrl() || null != this.getPostUrl()) {
            Element dcptElement = doc.createElement("DCPType");
            Element httpElement = doc.createElement("HTTP");
            if (null != this.getGetUrl()) {
                Element getElement = doc.createElement("Get");
                Element onlineElement = doc.createElement("OnlineResource");
                onlineElement.setAttribute("xlink:href", this.getGetUrl());
                onlineElement.setAttribute("xlink:type", "simple");
                onlineElement.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink"); 
                getElement.appendChild(onlineElement);
                httpElement.appendChild(getElement);
                dcptElement.appendChild(httpElement);
                domainElement.appendChild(dcptElement);
            }

            if (null != this.getPostUrl()) {
                Element postElement = doc.createElement("Post");
                Element onlineElement = doc.createElement("OnlineResource");
                onlineElement.setAttribute("xlink:href", this.getPostUrl());
                onlineElement.setAttribute("xlink:type", "simple");
                onlineElement.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");            
                postElement.appendChild(onlineElement);
                httpElement.appendChild(postElement);
                dcptElement.appendChild(httpElement);
                domainElement.appendChild(dcptElement);
            }
        }            
            
        rootElement.appendChild(domainElement);
        return rootElement;
    }
    // </editor-fold>
}