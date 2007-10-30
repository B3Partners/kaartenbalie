/*
 * EntityDataValue.java
 *
 * Created on October 29, 2007, 10:11 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.datawarehousing.domain;

/**
 *
 * @author Chris Kramer
 */
public abstract class  DataMappingTemplate {
    
    private Integer id;
    
    private String fieldReference;
    private WarehousedEntity warehousedEntity;
    
    public DataMappingTemplate() {
    }
    public DataMappingTemplate(WarehousedEntity warehousedEntity, String fieldReference) {
        this.setWarehousedEntity(warehousedEntity);
        this.fieldReference = fieldReference;
    }
    
    public WarehousedEntity getWarehousedEntity() {
        return warehousedEntity;
    }
    
    public void setWarehousedEntity(WarehousedEntity warehousedEntity) {
        this.warehousedEntity = warehousedEntity;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getFieldReference() {
        return fieldReference;
    }
    
    public void setFieldReference(String fieldReference) {
        this.fieldReference = fieldReference;
    }
    
    public abstract Object getValue();
    public abstract void setValue(Object object);
}
