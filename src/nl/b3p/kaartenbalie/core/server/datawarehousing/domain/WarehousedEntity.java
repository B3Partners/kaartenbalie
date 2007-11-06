/*
 * WarehousedEntity.java
 *
 * Created on November 6, 2007, 12:23 PM
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
    private Date dateDeleted;
    private Integer referencedId;
    private Set entityMutations;
    private EntityClass entityClass;
    public WarehousedEntity() {
        setEntityMutations(new HashSet());
        setDateCreated(new Date());
        
    }
    
    public WarehousedEntity(EntityClass entityClass, Integer referencedId) {
        this();
        this.setEntityClass(entityClass);
        this.setReferencedId(referencedId);
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Date getDateCreated() {
        return dateCreated;
    }
    
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    
    public Date getDateDeleted() {
        return dateDeleted;
    }
    
    public void setDateDeleted(Date dateDeleted) {
        this.dateDeleted = dateDeleted;
    }
    
    
    public Integer getReferencedId() {
        return referencedId;
    }
    
    public void setReferencedId(Integer referencedId) {
        this.referencedId = referencedId;
    }
    
    public Set getEntityMutations() {
        return entityMutations;
    }
    
    public void setEntityMutations(Set entityMutations) {
        this.entityMutations = entityMutations;
    }
    
    public EntityClass getEntityClass() {
        return entityClass;
    }
    
    public void setEntityClass(EntityClass entityClass) {
        this.entityClass = entityClass;
    }
    
    
    
    
}


