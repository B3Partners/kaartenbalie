/*
 * Extent.java
 *
 * Created on 26 september 2006, 16:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server;

/**
 *
 * @author Nando De Goeij
 */
public class Extent {
    
    private String name;
    private String defaults;
    private String nearestValue;
    private String multipleValues;
    private String current;
    
    /** Creates a new instance of Extent */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaults() {
        return defaults;
    }

    public void setDefaults(String defaults) {
        this.defaults = defaults;
    }

    public String getNearestValue() {
        return nearestValue;
    }

    public void setNearestValue(String nearestValue) {
        this.nearestValue = nearestValue;
    }

    public String getMultipleValues() {
        return multipleValues;
    }

    public void setMultipleValues(String multipleValues) {
        this.multipleValues = multipleValues;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }
}