/**
 * @(#)LatLonBoundingBox.java
 * @author N. de Goeij
 * @version 1.00 2006/10/11
 *
 * Purpose: Bean representing a LatLonBoundingBox.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.core.server;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class LatLonBoundingBox implements XMLElement {
    
    private Integer id;
    private String minx;
    private String miny;
    private String maxx;
    private String maxy;
    private Layer layer;
    
    // <editor-fold defaultstate="collapsed" desc="getter and setter methods.">
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getMinx() {
        return minx;
    }
    
    public void setMinx(String minx) {
        this.minx = minx;
    }
    
    public String getMiny() {
        return miny;
    }
    
    public void setMiny(String miny) {
        this.miny = miny;
    }
    
    public String getMaxx() {
        return maxx;
    }
    
    public void setMaxx(String maxx) {
        this.maxx = maxx;
    }
    
    public String getMaxy() {
        return maxy;
    }
    
    public void setMaxy(String maxy) {
        this.maxy = maxy;
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
        LatLonBoundingBox cloneLlbb = new LatLonBoundingBox();
        if (null != this.id) {
            cloneLlbb.id                = new Integer(this.id);
        }
        if (null != this.minx) {
            cloneLlbb.minx              = new String(this.minx);
        }
        if (null != this.miny) {
            cloneLlbb.miny              = new String(this.miny);
        }
        if (null != this.maxx) {
            cloneLlbb.maxx              = new String(this.maxx);
        }
        if (null != this.maxy) {
            cloneLlbb.maxy              = new String(this.maxy);
        }
        return cloneLlbb;
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
        
        Element latLonElement = doc.createElement("LatLonBoundingBox");
        
        latLonElement.setAttribute("minx", this.getMinx());
        latLonElement.setAttribute("miny", this.getMiny());
        latLonElement.setAttribute("maxx", this.getMaxx());
        latLonElement.setAttribute("maxy", this.getMaxy());
        
        rootElement.appendChild(latLonElement);
        return rootElement;
    }
    // </editor-fold>
}
