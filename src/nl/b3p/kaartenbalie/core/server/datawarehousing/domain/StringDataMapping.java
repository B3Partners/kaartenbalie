/*
 * StringDataMapping.java
 *
 * Created on October 29, 2007, 1:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.datawarehousing.domain;

/**
 *
 * @author Chris Kramer
 */
public class StringDataMapping extends DataMappingTemplate{
    
    /** Creates a new instance of StringDataMapping */
    
    private String stringValue;
    public StringDataMapping() {
    }
    public StringDataMapping(WarehousedEntity warehousedEntity, String fieldReference, String value) {
        super(warehousedEntity, fieldReference);
        this.setStringValue(value);
    }
    
    public String getStringValue() {
        return stringValue;
    }
    
    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
    
    public Object getValue() {
        return stringValue;
    }
    
    public void setValue(Object object) {
        if (object != null && object.getClass().equals(String.class)) {
            stringValue = (String) object;
        }
    }
    
    
    
}
