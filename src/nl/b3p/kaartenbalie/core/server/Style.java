/**
 * @(#)Style.java
 * @author N. de Goeij
 * @version 1.00 2006/10/11
 *
 * Purpose: Bean representing a Style.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.core.server;

import java.util.Set;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class Style implements XMLElement {
    
    private Integer id;
    private String name = "default";
    private String title;
    private String abstracts;
    private Layer layer;
    private Set <StyleDomainResource> domainResource;
    
    // <editor-fold defaultstate="collapsed" desc="getter and setter methods.">
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
    
    public Set getDomainResource() {
        return domainResource;
    }
    
    public void setDomainResource(Set <StyleDomainResource> domainResource) {
        this.domainResource = domainResource;
    }
    
    public void addDomainResource(StyleDomainResource dr) {
        if (null == domainResource) {
            domainResource = new HashSet <StyleDomainResource>();
        }
        domainResource.add(dr);
        dr.setStyle(this);
    }
    
    public Layer getLayer() {
        return layer;
    }
    
    public void setLayer(Layer layer) {
        this.layer = layer;
    }
    // </editor-fold>
    
    /** Method that will overwrite the URL's stored in the database with the URL specified for Kaartenbalie.
     * This new URL indicate the link to the kaartenbalie, while the old link is used to indicate the URL
     * to the real location of the service. Because the client which is connected to kaartenbalie has to send
     * his requests back to kaartenbalie and not directly to the official resource, the URL has to be replaced.
     *
     * @param newUrl String representing the URL the old URL has to be replaced with.
     */
    // <editor-fold defaultstate="collapsed" desc="overwriteURL(String newUrl) method">
    protected void overwriteURL(String newUrl) {
        Iterator it;
        //StyleDomainResource:
        if (null != this.getDomainResource() && this.getDomainResource().size() != 0) {
            it = this.getDomainResource().iterator();
            while (it.hasNext()) {
                StyleDomainResource sdr = (StyleDomainResource)it.next();
                sdr.overwriteURL(newUrl);
            }
        }
    }
    // </editor-fold>
    
    /** Method that will create a deep copy of this object.
     *
     * @return an object of type Object
     */
    // <editor-fold defaultstate="collapsed" desc="clone() method">
    public Object clone() {
        Style cloneStyle            = new Style();
        if (null != this.id) {
            cloneStyle.id               = new Integer(this.id);
        }
        if (null != this.name) {
            cloneStyle.name             = new String(this.name);
        }
        if (null != this.title) {
            cloneStyle.title            = new String(this.title);
        }
        if (null != this.abstracts) {
            cloneStyle.abstracts        = new String(this.abstracts);
        }
        if (null != this.domainResource) {
            cloneStyle.domainResource   = new HashSet <StyleDomainResource>();
            Iterator it = this.domainResource.iterator();
            while (it.hasNext()) {
                StyleDomainResource sdr = (StyleDomainResource)((StyleDomainResource)it.next()).clone();
                sdr.setStyle(cloneStyle);
                cloneStyle.domainResource.add(sdr);
            }
        }
        return cloneStyle;
    }
    // </editor-fold>
    
    /** Method that will create piece of the XML tree to create a proper XML docuement.
     *
     * @param doc Document object which is being used to create new Elements
     * @param rootElement The element where this object belongs to.
     *
     * @return an object of type Element
     */
    // <editor-fold defaultstate="collapsed" desc="toElement(Document doc, Element rootElement) method">
    public Element toElement(Document doc, Element rootElement) {
        Element styleElement = doc.createElement("Style");
        if(null != this.getName()) {
            Element nameElement = doc.createElement("Name");
            Text text = doc.createTextNode(this.getName());
            nameElement.appendChild(text);
            styleElement.appendChild(nameElement);
        }
        if(null != this.getTitle()) {
            Element titleElement = doc.createElement("Title");
            Text text = doc.createTextNode(this.getTitle());
            titleElement.appendChild(text);
            styleElement.appendChild(titleElement);
        }

        if (null != this.getDomainResource() && this.getDomainResource().size() != 0) {
            Iterator it = this.getDomainResource().iterator();
            while (it.hasNext()) {
                StyleDomainResource sdr = (StyleDomainResource)it.next();
                sdr.toElement(doc, styleElement);
            }
        }
        
        if(null != this.getDomainResource() && this.getDomainResource().size() != 0) {
            Hashtable sdrhash = new Hashtable();
            StyleDomainResource sdr = null;
            Iterator it = domainResource.iterator();
            while (it.hasNext()) {
                sdr = (StyleDomainResource)it.next();
                if (sdr.getDomain()==null)
                    continue;
                else if (sdr.getDomain().equalsIgnoreCase("LegendURL")) {
                    sdrhash.put("LegendURL",sdr);
                } else if (sdr.getDomain().equalsIgnoreCase("StyleSheetURL")) {
                    sdrhash.put("StyleSheetURL",sdr);
                } else if (sdr.getDomain().equalsIgnoreCase("StyleURL")) {
                    sdrhash.put("StyleURL",sdr);
                } else {
                    continue;
                }
            }
            sdr = (StyleDomainResource) sdrhash.get("LegendURL");
            if (sdr!=null)
                styleElement = sdr.toElement(doc, styleElement);
            sdr = (StyleDomainResource) sdrhash.get("StyleSheetURL");
            if (sdr!=null)
                styleElement = sdr.toElement(doc, styleElement);
            sdr = (StyleDomainResource) sdrhash.get("StyleURL");
            if (sdr!=null)
                styleElement = sdr.toElement(doc, styleElement);
        }
        
        rootElement.appendChild(styleElement);
        return rootElement;
    }
    // </editor-fold>
}