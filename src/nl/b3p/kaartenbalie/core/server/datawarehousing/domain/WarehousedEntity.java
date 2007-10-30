/*
 * WarehousedEntity.java
 *
 * Created on October 29, 2007, 9:20 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.datawarehousing.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Chris Kramer
 */
public class WarehousedEntity {
    
    private Integer id;
    private Date dateCreated;
    private Date dateLastUpdated;
    private Date dateDeleted;
    private Class objectClass;
    private Integer referencedId;
    private Set entityDataMappings;
    
    private WarehousedEntity() {
        setDateCreated(new Date());
        entityDataMappings = new HashSet();
    }
    
    public WarehousedEntity(Class objectClass, Integer referencedId) {
        this();
        this.setObjectClass(objectClass);
        this.setReferencedId(referencedId);
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
    
    public Integer getReferencedId() {
        return referencedId;
    }
    
    public void setReferencedId(Integer referencedId) {
        this.referencedId = referencedId;
    }
    
    public Date getDateCreated() {
        return dateCreated;
    }
    
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    
    public Date getDateLastUpdated() {
        return dateLastUpdated;
    }
    
    public void setDateLastUpdated(Date dateLastUpdated) {
        this.dateLastUpdated = dateLastUpdated;
    }
    
    public Date getDateDeleted() {
        return dateDeleted;
    }
    
    public void setDateDeleted(Date dateDeleted) {
        this.dateDeleted = dateDeleted;
    }
    
    public Set getEntityDataMappings() {
        return entityDataMappings;
    }
    
    public void setEntityDataMappings(Set entityDataMappings) {
        this.entityDataMappings = entityDataMappings;
    }
    
    
    
    
}
