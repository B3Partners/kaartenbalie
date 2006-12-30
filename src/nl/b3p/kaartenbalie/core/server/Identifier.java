/*
 * Identifier.java
 *
 * Created on 26 september 2006, 16:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 *
 * @author Nando De Goeij
 */
public class Identifier {
    
    private Integer id;
    private String authorityName;
    private String value;
    private String authorityURL;
    private Layer layer;
    
    public Integer getId() {
        return id;
    }
    
    private void setId(Integer id) {
        this.id = id;
    }
    
    public String getAuthorityName() {
        return authorityName;
    }
    
    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }
    
    public String getAuthorityURL() {
        return authorityURL;
    }
    
    public void setAuthorityURL(String authorityURL) {
        this.authorityURL = authorityURL;
    }
    
    public Layer getLayer() {
        return layer;
    }
    
    public void setLayer(Layer layer) {
        this.layer = layer;
    }
    
    public Object clone() {
        Identifier cloneIdent           = new Identifier();
        if (null != this.id) {
            cloneIdent.id               = new Integer(this.id);
        }
        if (null != this.authorityName) {
            cloneIdent.authorityName    = new String(this.authorityName);
        }
        if (null != this.value) {
            cloneIdent.value            = new String(this.value);
        }
        if (null != this.authorityURL) {
            cloneIdent.authorityURL     = new String(this.authorityURL);
        }
        return cloneIdent;
    }
    
    public Element toElement(Document doc) {
        Element rootElement = doc.createElement("AuthorityURL");
        rootElement.setAttribute("authority", this.getAuthorityName());
        
        Element element = doc.createElement("OnlineResource");
        rootElement.appendChild(element);
        
        element.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:type", "simple");
        element.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", this.getAuthorityURL());
        
        // TODO, hoe moet dit????
        Element rootElement2 = doc.createElement("Identifier");
        rootElement2.setAttribute("authority", this.getAuthorityName());
        Text text = doc.createTextNode(this.getAuthorityName());
        rootElement2.appendChild(text);
        
        return rootElement;
    }
}
