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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Chris Kramer
 */
public class PropertyValue {

    private Integer id;
    private byte[] objectData;
    private String stringData;
    private EntityProperty entityProperty;
    private EntityMutation entityMutation;
    private static List allowedMappings = new ArrayList();

    public PropertyValue() {
    }

    public PropertyValue(EntityProperty entityProperty, EntityMutation entityMutation) {
        this();
        this.setEntityMutation(entityMutation);
        this.setEntityProperty(entityProperty);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private byte[] getObjectData() {
        return objectData;
    }

    private void setObjectData(byte[] objectData) {
        this.objectData = objectData;
    }

    public EntityProperty getEntityProperty() {
        return entityProperty;
    }

    public void setEntityProperty(EntityProperty entityProperty) {
        this.entityProperty = entityProperty;
    }

    public EntityMutation getEntityMutation() {
        return entityMutation;
    }

    public void setEntityMutation(EntityMutation entityMutation) {
        this.entityMutation = entityMutation;
    }

    private String getStringData() {
        return stringData;
    }

    private void setStringData(String stringData) {
        this.stringData = stringData;
    }

    /*
     * Methods for setting and obtaining the objectdata.
     */
    public Object requestValue() throws Exception {

        if (getObjectData() == null && getStringData() == null) {
            return null;
        }

        Class objectClass = entityProperty.getFieldClass();
        if (String.class.isAssignableFrom(objectClass)) {
            return getStringData();
        } else if (Date.class.isAssignableFrom(objectClass)) {
            return stringToDate(getStringData());
        } else if (Integer.class.isAssignableFrom(objectClass)) {
            return stringToInteger(getStringData());
        } else {
            ByteArrayInputStream bais = new ByteArrayInputStream(getObjectData());
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object object = ois.readObject();
            return object;
        }
    }

    public void storeValue(Object object) throws Exception {
        if (object == null) {
            setObjectData(null);
            setStringData(null);
            return;
        }

        Class objectClass = object.getClass();
        if (String.class.isAssignableFrom(objectClass)) {
            setStringData(object.toString());
        } else if (Date.class.isAssignableFrom(objectClass)) {
            setStringData(dateToString((Date) object));
        } else if (Integer.class.isAssignableFrom(objectClass)) {
            setStringData(integerToString((Integer) object));
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            setObjectData(baos.toByteArray());
        }
    }
    /*
     * Statics for accesscontrol.
     */
    

    static {
        allowedMappings.add(String.class);
        allowedMappings.add(Integer.class);
        allowedMappings.add(Date.class);
    }

    public static boolean isAllowed(Class clazz) {
        Iterator i = allowedMappings.iterator();
        while (i.hasNext()) {
            Class testClazz = (Class) i.next();
            if (testClazz.isAssignableFrom(clazz)) {
                return true;
            }
        }
        return false;
    }
    //Methods for parsing Integers..
    private static String integerToString(Integer integer) {
        if (integer == null) {
            return null;
        }
        return integer.toString();
    }

    private static Integer stringToInteger(String string) {
        if (string == null) {
            return null;
        }
        try {
            return new Integer(string);
        } catch (Exception e) {
            return null;
        }
    }
    //Methods for parsing Dates
    private static String dateToString(Date date) {
        if (date == null) {
            return null;
        }
        return new String("" + date.getTime());
    }

    private static Date stringToDate(String string) {
        if (string == null) {
            return null;
        }
        try {
            return new Date(Long.parseLong(string));
        } catch (Exception e) {
            return null;
        }
    }
}
