/*
 * Dimensions.java
 *
 * Created on 26 september 2006, 16:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server;

/**
 *
 * @author Nando De Goeij
 */
public class Dimensions {
    
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
    
    public String toString(String tabulator) {
    	StringBuilder result = new StringBuilder();
    	final String newLine = System.getProperty("line.separator");
        
        result.append(tabulator + "<Dimension name=\"" + this.getDimensionsName() + "units=\"" + this.getDimensionsUnit() + "/>\n");
        result.append(tabulator + "<Extent name=\"" + this.getExtentName() + "\" ");
        if(null != this.getExtentDefaults()) {
        	result.append("default=\"" + this.getExtentDefaults() + "\" ");
        }
        if(null != this.getExtentNearestValue()) {
        	result.append("nearestValue=\"" + this.getExtentNearestValue() + "\" ");
        }
        if(null != this.getExtentMultipleValues()) {
        	result.append("multipleValues=\"" + this.getExtentMultipleValues() + "\" ");
        }
        if(null != this.getExtentCurrent()) {
        	result.append("current=\"" + this.getExtentCurrent() + "\"");
        }
        result.append(" />\n");
        
        return result.toString();
    }
}