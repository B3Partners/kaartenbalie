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
