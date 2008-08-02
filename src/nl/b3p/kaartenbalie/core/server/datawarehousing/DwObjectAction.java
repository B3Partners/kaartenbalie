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
package nl.b3p.kaartenbalie.core.server.datawarehousing;

public class DwObjectAction {

    public static final int MERGE = 0;
    public static final int PERSIST = 1;
    public static final int REMOVE = 2;
    public static final int PERSIST_OR_MERGE = 3;
    private Class clazz;
    private Integer primaryKey;
    private int objectAction;

    public DwObjectAction(Class clazz, Integer primaryKey, int objectAction) {
        this.setClazz(clazz);
        this.setPrimaryKey(primaryKey);
        this.setObjectAction(objectAction);
    }

    public Integer getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Integer primaryKey) {
        this.primaryKey = primaryKey;
    }

    public int getObjectAction() {
        return objectAction;
    }

    public void setObjectAction(int objectAction) {
        this.objectAction = objectAction;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
