/**
 * @(#)Identifier.java
 * @author N. de Goeij
 * @version 1.00 2006/10/11
 *
 * Purpose: Bean representing a Identifier.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.core.server;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class Identifier implements XMLElement {
    
    private Integer id;
    private String authorityName;
    private String value;
    private String authorityURL;
    private Layer layer;
    
    // <editor-fold defaultstate="" desc="getter and setter methods.">
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
    // </editor-fold>
    
    /** Method that will create a deep copy of this object.
     *
     * @return an object of type Object
     */
    // <editor-fold defaultstate="" desc="clone() method">
    public Object clone() {
        Identifier cloneIdent           = new Identifier();
        if (null != this.id) {
            cloneIdent.id               = new Integer(this.id.intValue());
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
        
        Element authorityElement = doc.createElement("AuthorityURL");
        authorityElement.setAttribute("authority", this.getAuthorityName());
        
        Element onlineElement = doc.createElement("OnlineResource");
        onlineElement.setAttribute("xlink:href", this.getAuthorityURL());
        onlineElement.setAttribute("xlink:type", "simple");
        onlineElement.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
        authorityElement.appendChild(onlineElement);
        
        rootElement.appendChild(authorityElement);
        
        Element identifierElement = doc.createElement("Identifier");
        identifierElement.setAttribute("authority", this.getAuthorityName());
        Text text = doc.createTextNode(this.getAuthorityName());
        identifierElement.appendChild(text);
        
        rootElement.appendChild(identifierElement);        
        return rootElement;
    }
    // </editor-fold>
}
