/*
 * StringDataMapping.java
 *
 * Created on October 29, 2007, 1:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.datawarehousing.domain;

import java.util.Date;

/**
 *
 * @author Chris Kramer
 */
public class DateDataMapping extends DataMappingTemplate{
    
    /** Creates a new instance of StringDataMapping */
    
    private Date dateValue;
    public DateDataMapping() {
    }
    public DateDataMapping(WarehousedEntity warehousedEntity, String fieldReference, Date dateValue) {
        super(warehousedEntity, fieldReference);
        this.setDateValue(dateValue);
    }
    
    
    
    public Object getValue() {
        return getDateValue();
    }
    
    public void setValue(Object object) {
        
        if (object != null && Date.class.isAssignableFrom(object.getClass())) {
            setDateValue((Date) object);

        }
    }
    
    public Date getDateValue() {
        return dateValue;
    }
    
    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }
    
    
    
    
    
}
