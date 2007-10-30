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
public class IntegerDataMapping extends DataMappingTemplate{
    
    /** Creates a new instance of StringDataMapping */
    
    private Integer integerValue;
    public IntegerDataMapping() {
    }
    public IntegerDataMapping(WarehousedEntity warehousedEntity, String fieldReference, Integer value) {
        super(warehousedEntity, fieldReference);
        this.integerValue = integerValue;
    }
    
    
    
    public Object getValue() {
        return integerValue;
    }
    
    public void setValue(Object object) {
        if (object != null && object.getClass().equals(Integer.class)) {
            integerValue = (Integer) object;
        }
    }
    
    public Integer getIntegerValue() {
        return integerValue;
    }
    
    public void setIntegerValue(Integer integerValue) {
        this.integerValue = integerValue;
    }
    
    
    
}
