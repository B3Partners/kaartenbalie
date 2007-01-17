/**
 * @(#)Attribution.java
 * @author N. de Goeij
 * @version 1.00 2006/10/11
 *
 * Purpose: Bean representing an Attribution.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.core.server;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class Attribution implements XMLElement {
    
    private Integer id;
    private String title;
    private String attributionURL;
    private String logoURL;
    private String logoFormat;
    private String logoWidth;
    private String logoHeight;
    private Layer layer;
    
    // <editor-fold defaultstate="collapsed" desc="getter and setter methods.">
    public Integer getId() {
        return id;
    }
    
    private void setId(Integer id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAttributionURL() {
        return attributionURL;
    }
    
    public void setAttributionURL(String attributionURL) {
        this.attributionURL = attributionURL;
    }
    
    public String getLogoURL() {
        return logoURL;
    }
    
    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }
    
    public String getLogoWidth() {
        return logoWidth;
    }
    
    public void setLogoWidth(String logoWidth) {
        this.logoWidth = logoWidth;
    }
    
    public String getLogoHeight() {
        return logoHeight;
    }
    
    public void setLogoHeight(String logoHeight) {
        this.logoHeight = logoHeight;
    }
    
    public String getLogoFormat() {
        return logoFormat;
    }
    
    public void setLogoFormat(String logoFormat) {
        this.logoFormat = logoFormat;
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
    // <editor-fold defaultstate="collapsed" desc="clone() method">
    public Object clone() {
        Attribution cloneAtt        = new Attribution();
        if (null != this.id) {
            cloneAtt.id             = new Integer(this.id.intValue());
        }
        if (null != this.title) {
            cloneAtt.title          = new String(this.title);
        }
        if (null != this.attributionURL) {
            cloneAtt.attributionURL = new String(this.attributionURL);
        }
        if (null != this.logoURL) {
            cloneAtt.logoURL        = new String(this.logoURL);
        }
        if (null != this.logoFormat) {
            cloneAtt.logoFormat     = new String(this.logoFormat);
        }
        if (null != this.logoWidth) {
            cloneAtt.logoWidth      = new String(this.logoWidth);
        }
        if (null != this.logoHeight) {
            cloneAtt.logoHeight     = new String(this.logoHeight);
        }
        return cloneAtt;
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
        Element attributionElement = doc.createElement("Attribution");
        
        //title element
        Element titleElement = doc.createElement("Title");
        Text text = doc.createTextNode(this.getTitle());
        titleElement.appendChild(text);
        attributionElement.appendChild(titleElement);
        //end title element
        
        //onlineResource element
        Element attOnlineElement = doc.createElement("OnlineResource");
        attOnlineElement.setAttribute("xlink:href", this.getAttributionURL());
        attOnlineElement.setAttribute("xlink:type", "simple");
        attOnlineElement.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink"); 
        attributionElement.appendChild(attOnlineElement);
        //end onlineResource element
        
        //Logo element
        Element logoElement = doc.createElement("LogoURL");
        logoElement.setAttribute("width", this.getLogoWidth());
        logoElement.setAttribute("height", this.getLogoHeight());
        
        //format element
        Element formatElement = doc.createElement("Format");
        text = doc.createTextNode(this.getLogoFormat());
        formatElement.appendChild(text);
        logoElement.appendChild(formatElement);
        //end format element
        
        //onlineResource element
        Element logoOnlineElement = doc.createElement("OnlineResource");
        logoOnlineElement.setAttribute("xlink:href", this.getLogoURL());
        logoOnlineElement.setAttribute("xlink:type", "simple");
        logoOnlineElement.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
        logoElement.appendChild(logoOnlineElement);
        //end onlineResource element
        
        attributionElement.appendChild(logoElement);
        //end Logo element
        
        rootElement.appendChild(attributionElement);        
        return rootElement;
    }
    // </editor-fold>
}