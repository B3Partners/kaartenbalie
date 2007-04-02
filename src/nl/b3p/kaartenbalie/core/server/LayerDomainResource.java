/**
 * @(#)LayerDomainResource.java
 * @author N. de Goeij
 * @version 1.00 2006/10/11
 *
 * Purpose: Bean representing a LayerDomainResource.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.core.server;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class LayerDomainResource implements XMLElement {
    
    private static final Log log = LogFactory.getLog(LayerDomainResource.class);
    
    private Integer id;
    private Set formats;
    private String url;
    private String domain;
    private Layer layer;
    
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
    
    public void setFormats(Set formats) {
        this.formats = formats;
    }
    
    public void addFormat(String f) {
        if (null == formats) {
            formats = new HashSet();
        }
        formats.add(f);
    }
    
    public String getDomain() {
        return domain;
    }
    
    public void setDomain(String domain) {
        this.domain = domain;
    }
    
    public Layer getLayer() {
        return layer;
    }
    
    public void setLayer(Layer layer) {
        this.layer = layer;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    // </editor-fold>
    
    /** Method that will create a deep copy of this object.
     *
     * @return an object of type Object
     */
    // <editor-fold defaultstate="" desc="clone() method">
    public Object clone() {
        LayerDomainResource cloneLDR        = new LayerDomainResource();
        if (null != this.id) {
            cloneLDR.id                     = new Integer(this.id.intValue());
        }
        if (null != this.formats) {
            cloneLDR.formats                = new HashSet(this.formats);
        }
        if (null != this.url) {
            cloneLDR.url                    = new String(this.url);
        }
        if (null != this.domain) {
            cloneLDR.domain                 = new String(this.domain);
        }
        return cloneLDR;
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