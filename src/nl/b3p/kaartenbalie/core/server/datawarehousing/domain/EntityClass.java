/*
 * EntityClass.java
 *
 * Created on November 6, 2007, 2:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.datawarehousing.domain;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Chris Kramer
 */
public class EntityClass {
    
    private Integer id;
    private Class objectClass;
    private Set warehousedEntities;
    private Set entityProperties;
    public EntityClass() {
        setWarehousedEntities(new HashSet());
        setEntityProperties(new HashSet());
    }
    public EntityClass(Class objectClass) {
        this();
        this.setObjectClass(objectClass);
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Class getObjectClass() {
        return objectClass;
    }
    
    public void setObjectClass(Class objectClass) {
        this.objectClass = objectClass;
    }
    
    public Set getWarehousedEntities() {
        return warehousedEntities;
    }
    
    public void setWarehousedEntities(Set warehousedEntities) {
        this.warehousedEntities = warehousedEntities;
    }
    
    public Set getEntityProperties() {
        return entityProperties;
    }
    
    public void setEntityProperties(Set entityProperties) {
        this.entityProperties = entityProperties;
    }
    
    
    
}
