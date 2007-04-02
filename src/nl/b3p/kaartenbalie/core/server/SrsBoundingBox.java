/**
 * @(#)SrsBoundingBox.java
 * @author N. de Goeij
 * @version 1.00 2006/10/11
 *
 * Purpose: Bean representing a SRS or Bounding Box.
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

public class SrsBoundingBox implements XMLElement {
    
    private Integer id;
    private String srs;
    private String minx;
    private String miny;
    private String maxx;
    private String maxy;
    private String resx;
    private String resy;
    private Layer layer;
    
    // <editor-fold defaultstate="" desc="getter and setter methods.">
    public SrsBoundingBox() {
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
    // <editor-fold defaultstate="" desc="clone() method">
    public Object clone() {
        SrsBoundingBox cloneSrs   = new SrsBoundingBox();
        if (null != this.id) {
            cloneSrs.id    = new Integer(this.id.intValue());
        }
        if (null != this.srs) {
            cloneSrs.srs  = new String(this.srs);
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
    
    
    public String getType() {
        if (getSrs()==null && getMinx()==null && getMiny()==null &&  getMaxx()==null &&  getMaxy()==null)
            return null;
        if (getSrs()==null)
            return "LatLonBoundingBox";
        if (getMinx()==null && getMiny()==null &&  getMaxx()==null &&  getMaxy()==null)
            return "SRS";
        return "BoundingBox";
    }
    
    /** Method that will create piece of the XML tree to create a proper XML docuement.
     *
     * @param doc Document object which is being used to create new Elements
     * @param rootElement The element where this object belongs to.
     *
     * @return an object of type Element
     */
    // <editor-fold defaultstate="" desc="toElement(Document doc, Element rootElement) method">
    public Element toElement(Document doc, Element rootElement) {
        
        Element srsBBElement = null;
        String type = getType();
        
        if (type.equalsIgnoreCase("SRS")) {
            Element srsElement = doc.createElement("SRS");
            Text text = doc.createTextNode(this.getSrs());
            srsElement.appendChild(text);
            rootElement.appendChild(srsElement);
            
        } else if (type.equalsIgnoreCase("LatLonBoundingBox")) {
            Element bbElement = doc.createElement("LatLonBoundingBox");
            bbElement.setAttribute("minx", this.getMinx());
            bbElement.setAttribute("miny", this.getMiny());
            bbElement.setAttribute("maxx", this.getMaxx());
            bbElement.setAttribute("maxy", this.getMaxy());
            rootElement.appendChild(bbElement);
            
        } else if (type.equalsIgnoreCase("BoundingBox")) {
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
