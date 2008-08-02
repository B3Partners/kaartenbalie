/*
 * B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
 * for authentication/authorization, pricing and usage reporting.
 *
 * Copyright 2006, 2007, 2008 B3Partners BV
 * 
 * This file is part of B3P Kaartenbalie.
 * 
 * B3P Kaartenbalie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * B3P Kaartenbalie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
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


