/*
 * Style.java
 *
 * Created on 18 september 2006, 11:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 *
 * @author Nando De Goeij
 */
public class Style {
    
    private Integer id;
    private String name = "default";
    private String title;
    private String abstracts;
    private Layer layer;
    private Set domainResource;
    
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
    
    public void setDomainResource(Set domainResource) {
        this.domainResource = domainResource;
    }
    
    public void addDomainResource(StyleDomainResource dr) {
        if (null == domainResource) {
            domainResource = new HashSet();
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
            cloneStyle.domainResource   = new HashSet();
            Iterator it = this.domainResource.iterator();
            while (it.hasNext()) {
                StyleDomainResource sdr = (StyleDomainResource)((StyleDomainResource)it.next()).clone();
                sdr.setStyle(cloneStyle);
                cloneStyle.domainResource.add(sdr);
            }
        }
        return cloneStyle;
    }
    
    public Element toElement(Document doc) {
        Element rootElement = doc.createElement("Style");
        if(null != this.getName()) {
            Element element = doc.createElement("Name");
            Text text = doc.createTextNode(this.getName());
            element.appendChild(text);
            rootElement.appendChild(element);
        }
        if(null != this.getTitle()) {
            Element element = doc.createElement("Title");
            Text text = doc.createTextNode(this.getTitle());
            element.appendChild(text);
            rootElement.appendChild(element);
        }
        if (null != this.getDomainResource() && this.getDomainResource().size() != 0) {
            Iterator it = this.getDomainResource().iterator();
            while (it.hasNext()) {
                StyleDomainResource sdr = (StyleDomainResource)it.next();
                rootElement.appendChild(sdr.toElement(doc));
            }
        }
        return rootElement;
    }
}