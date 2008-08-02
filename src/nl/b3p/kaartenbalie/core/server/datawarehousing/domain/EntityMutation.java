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
