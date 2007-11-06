/*
 * EntityProperty.java
 *
 * Created on November 6, 2007, 12:18 PM
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
public class EntityProperty {
    
    private Integer id;
    private String fieldName;
    private Class fieldClass;
    private Date dateCreated;
    private Date dateDeleted;
    
    private Set propertyValues;
    private EntityClass entityClass;
    public EntityProperty() {
        setPropertyValues(new HashSet());
        setDateCreated(new Date());
    }
    
    public EntityProperty(EntityClass entityClass, Class fieldClass, String fieldName) {
        this();
        this.setEntityClass(entityClass);
        this.setFieldClass(fieldClass);
        this.setFieldName(fieldName);
    }
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getFieldName() {
        return fieldName;
    }
    
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    
    
    public Set getPropertyValues() {
        return propertyValues;
    }
    
    public void setPropertyValues(Set propertyValues) {
        this.propertyValues = propertyValues;
    }
    
    public Class getFieldClass() {
        return fieldClass;
    }
    
    public void setFieldClass(Class fieldClass) {
        this.fieldClass = fieldClass;
    }
    
    public EntityClass getEntityClass() {
        return entityClass;
    }
    
    public void setEntityClass(EntityClass entityClass) {
        this.entityClass = entityClass;
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
    
    
    
}
