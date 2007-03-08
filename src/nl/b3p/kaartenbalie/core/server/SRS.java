/**
 * @(#)SRS.java
 * @author N. de Goeij
 * @version 1.00 2006/10/11
 *
 * Purpose: Bean representing a SRS.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.core.server;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class SRS implements XMLElement {
    
    private Integer id;
    private String srs;
    private String minx;
    private String miny;
    private String maxx;
    private String maxy;
    private String resx;
    private String resy;
    private Layer layer;
    
    // <editor-fold defaultstate="collapsed" desc="getter and setter methods.">
    public SRS() {
        //       srsen.add(this);
    }
    
    public Integer getId() {
        return id;
    }
    
    private void setId(Integer id) {
        this.id = id;
    }
    
    public String getSrs() {
        return srs;
    }
    
    public void setSrs(String srs) {
        this.srs = srs;
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
    
    public String getResx() {
        return resx;
    }
    
    public void setResx(String resx) {
        this.resx = resx;
    }
    
    public String getResy() {
        return resy;
    }
    
    public void setResy(String resy) {
        this.resy = resy;
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
        SRS cloneSrs   = new SRS();
        if (null != this.id) {
            cloneSrs.id    = new Integer(this.id);
        }
        if (null != this.minx) {
            cloneSrs.minx  = new String(this.minx);
        }
        if (null != this.miny) {
            cloneSrs.miny  = new String(this.miny);
        }
        if (null != this.maxx) {
            cloneSrs.maxx  = new String(this.maxx);
        }
        if (null != this.maxy) {
            cloneSrs.maxy  = new String(this.maxy);
        }
        if (null != this.resx) {
            cloneSrs.resx  = new String(this.resx);
        }
        if (null != this.resy) {
            cloneSrs.resy  = new String(this.resy);
        }
        return cloneSrs;
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
        
        Element srsBBElement = null;
        
        if(null != this.getSrs() && null == this.getMinx() && null == this.getMiny() && null == this.getMaxx() && null == this.getMaxy()) {
            Element srsElement = doc.createElement("SRS");
            Text text = doc.createTextNode(this.getSrs());
            srsElement.appendChild(text);
            rootElement.appendChild(srsElement);
        } 
        if (null != this.getSrs() && !(null == this.getMinx() && null == this.getMiny() && null == this.getMaxx() && null == this.getMaxy())) {
            Element bbElement = doc.createElement("BoundingBox");
            bbElement.setAttribute("SRS", this.getSrs());
            bbElement.setAttribute("minx", this.getMinx());
            bbElement.setAttribute("miny", this.getMiny());
            bbElement.setAttribute("maxx", this.getMaxx());
            bbElement.setAttribute("maxy", this.getMaxy());
            
            if (null != this.getResx() && null != this.getResy()) {
                bbElement.setAttribute("resx", this.getResx());
                bbElement.setAttribute("resy", this.getResy());
            }
            rootElement.appendChild(bbElement);
        }
        
        return rootElement;
    }
    // </editor-fold>
}
