/*
 * EntityMutation.java
 *
 * Created on November 6, 2007, 12:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.datawarehousing.domain;

import java.util.Date;
import java.util.Set;

/**
 *
 * @author Chris Kramer
 */
public class EntityMutation {
    
    private Integer id;
    private Date mutationDate;
    private WarehousedEntity warehousedEntity;
    private Set propertyValues;
    public EntityMutation() {
        setMutationDate(new Date());
    }
    public EntityMutation(WarehousedEntity warehousedEntity) {
        this();
        this.warehousedEntity = warehousedEntity;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public WarehousedEntity getWarehousedEntity() {
        return warehousedEntity;
    }
    
    public void setWarehousedEntity(WarehousedEntity warehousedEntity) {
        this.warehousedEntity = warehousedEntity;
    }
    
    public Set getPropertyValues() {
        return propertyValues;
    }
    
    public void setPropertyValues(Set propertyValues) {
        this.propertyValues = propertyValues;
    }
    
    public Date getMutationDate() {
        return mutationDate;
    }
    
    public void setMutationDate(Date mutationDate) {
        this.mutationDate = mutationDate;
    }
    
    
    
}
