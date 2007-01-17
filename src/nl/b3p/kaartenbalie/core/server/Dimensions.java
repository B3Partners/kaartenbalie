/**
 * @(#)Dimensions.java
 * @author N. de Goeij
 * @version 1.00 2006/10/11
 *
 * Purpose: Bean representing a Dimensions.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.core.server;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Dimensions implements XMLElement {
    
    private Integer id;
    private String dimensionsName;
    private String dimensionsUnit;
    private String dimensionsUnitSymbol;
    private String extentName;
    private String extentDefaults;
    private String extentNearestValue;
    private String extentMultipleValues;
    private String extentCurrent;
    private Layer layer;
    
    // <editor-fold defaultstate="collapsed" desc="getter and setter methods.">
    public Integer getId() {
        return id;
    }
    
    private void setId(Integer id) {
        this.id = id;
    }
    
    public String getDimensionsName() {
        return dimensionsName;
    }
    
    public void setDimensionsName(String dimensionsName) {
        this.dimensionsName = dimensionsName;
    }
    
    public String getDimensionsUnit() {
        return dimensionsUnit;
    }
    
    public void setDimensionsUnit(String dimensionsUnit) {
        this.dimensionsUnit = dimensionsUnit;
    }
    
    public String getDimensionsUnitSymbol() {
        return dimensionsUnitSymbol;
    }
    
    public void setDimensionsUnitSymbol(String dimensionsUnitSymbol) {
        this.dimensionsUnitSymbol = dimensionsUnitSymbol;
    }
    
    public String getExtentName() {
        return extentName;
    }
    
    public void setExtentName(String extentName) {
        this.extentName = extentName;
    }
    
    public String getExtentDefaults() {
        return extentDefaults;
    }
    
    public void setExtentDefaults(String extentDefaults) {
        this.extentDefaults = extentDefaults;
    }
    
    public String getExtentNearestValue() {
        return extentNearestValue;
    }
    
    public void setExtentNearestValue(String extentNearestValue) {
        this.extentNearestValue = extentNearestValue;
    }
    
    public String getExtentMultipleValues() {
        return extentMultipleValues;
    }
    
    public void setExtentMultipleValues(String extentMultipleValues) {
        this.extentMultipleValues = extentMultipleValues;
    }
    
    public String getExtentCurrent() {
        return extentCurrent;
    }
    
    public void setExtentCurrent(String extentCurrent) {
        this.extentCurrent = extentCurrent;
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
        Dimensions cloneDim                 = new Dimensions();
        if (null != this.id) {
            cloneDim.id                     = new Integer(this.id.intValue());
        }
        if (null != this.dimensionsName) {
            cloneDim.dimensionsName         = new String(this.dimensionsName);
        }
        if (null != this.dimensionsUnit) {
            cloneDim.dimensionsUnit         = new String(this.dimensionsUnit);
        }
        if (null != this.dimensionsUnitSymbol) {
            cloneDim.dimensionsUnitSymbol   = new String(this.dimensionsUnitSymbol);
        }
        if (null != this.extentName) {
            cloneDim.extentName             = new String(this.extentName);
        }
        if (null != this.extentDefaults) {
            cloneDim.extentDefaults         = new String(this.extentDefaults);
        }
        if (null != this.extentNearestValue) {
            cloneDim.extentNearestValue     = new String(this.extentNearestValue);
        }
        if (null != this.extentMultipleValues) {
            cloneDim.extentMultipleValues   = new String(this.extentMultipleValues);
        }
        if (null != this.extentCurrent) {
            cloneDim.extentCurrent          = new String(this.extentCurrent);
        }
        return cloneDim;
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
        
        Element dimensionElement = doc.createElement("Dimension");
        dimensionElement.setAttribute("name", this.getDimensionsName());
        dimensionElement.setAttribute("units", this.getDimensionsUnit());
        dimensionElement.setAttribute("unitSymbol", this.getDimensionsUnitSymbol());
        rootElement.appendChild(dimensionElement);
        
        Element extentElement = doc.createElement("Extent");
        extentElement.setAttribute("name", this.getExtentName());
        if(null != this.getExtentDefaults()) {
            extentElement.setAttribute("default", this.getExtentDefaults());
        }
        if(null != this.getExtentNearestValue()) {
            extentElement.setAttribute("nearestValue", this.getExtentNearestValue());
        }
        if(null != this.getExtentMultipleValues()) {
            extentElement.setAttribute("multipleValues", this.getExtentMultipleValues());
        }
        if(null != this.getExtentCurrent()) {
            extentElement.setAttribute("current", this.getExtentCurrent());
        }
        rootElement.appendChild(extentElement);
        
        return rootElement;
    }
    // </editor-fold>
}