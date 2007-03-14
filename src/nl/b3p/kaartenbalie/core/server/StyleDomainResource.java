/**
 * @(#)StyleDomainResource.java
 * @author N. de Goeij
 * @version 1.00 2006/10/11
 *
 * Purpose: Bean representing a StyleDomainResource.
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

public class StyleDomainResource implements XMLElement {
    
    private Integer id;
    private Set <String> formats;
    private String url;
    private String domain;
    private String width;
    private String height;
    private Style style;
    
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
    
    public String getDomain() {
        return domain;
    }
    
    public void setDomain(String domain) {
        this.domain = domain;
    }
    
    public Style getStyle() {
        return style;
    }
    
    public void setStyle(Style style) {
        this.style = style;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getWidth() {
        return width;
    }
    
    public void setWidth(String width) {
        this.width = width;
    }
    
    public String getHeight() {
        return height;
    }
    
    public void setHeight(String height) {
        this.height = height;
    }
    // </editor-fold>
    
    /** Method that will create a deep copy of this object.
     *
     * @return an object of type Object
     */
    // <editor-fold defaultstate="" desc="clone() method">
    public Object clone() {
        StyleDomainResource cloneSDR    = new StyleDomainResource();
        if (null != this.id) {
            cloneSDR.id                     = new Integer(this.id.intValue());
        }
        if (null != this.formats) {
            cloneSDR.formats                = new HashSet <String>(this.formats);
        }
        if (null != this.url) {
            cloneSDR.url                    = new String(this.url);
        }
        if (null != this.domain) {
            cloneSDR.domain                 = new String(this.domain);
        }
        if (null != this.width) {
            cloneSDR.width                  = new String(this.width);
        }
        if (null != this.height) {
            cloneSDR.height                 = new String(this.height);
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
        String temporaryURL;
        temporaryURL = this.getUrl();
        if (null != temporaryURL && !temporaryURL.equals("")) {
            int firstOccur = temporaryURL.indexOf("?");
            if(firstOccur != -1) {
                temporaryURL = temporaryURL.substring(firstOccur);
                //then add the newly given url in front of the cutted part
                temporaryURL = newUrl + temporaryURL;
                //save this new URL as the one to be used
                temporaryURL = temporaryURL.replace("&", "&amp;");
                this.setUrl(temporaryURL);
            }
        }
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
        
        if (this.getWidth()!=null)
            domainElement.setAttribute("width", this.getWidth());
        if (this.getHeight()!=null)
            domainElement.setAttribute("height", this.getHeight());
        
        if (null != this.getFormats() && this.getFormats().size() != 0) {
            Iterator it = formats.iterator();
            while (it.hasNext()) {
                Element formatElement = doc.createElement("Format");
                Text text = doc.createTextNode((String)it.next());
                formatElement.appendChild(text);
                domainElement.appendChild(formatElement);
            }
        }
        if (null != this.getUrl()) {
            Element onlineElement = doc.createElement("OnlineResource");
            onlineElement.setAttribute("xlink:href", this.getUrl());
            onlineElement.setAttribute("xlink:type", "simple");
            onlineElement.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
            domainElement.appendChild(onlineElement);
        }
        
        rootElement.appendChild(domainElement);
        return rootElement;
    }
    // </editor-fold>
}