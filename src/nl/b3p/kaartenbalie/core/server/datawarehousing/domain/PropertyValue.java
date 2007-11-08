/*
 * PropertyValue.java
 *
 * Created on November 6, 2007, 12:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
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
    
    public byte[] getObjectData() {
        return objectData;
    }
    
    public void setObjectData(byte[] objectData) {
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
    
    
    /*
     * Methods for setting and obtaining the objectdata.
     */
    public Object requestValue() throws Exception {
        if (objectData == null) {
            return null;
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(getObjectData());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object object = ois.readObject();
        return object;
    }
    public void storeValue(Object object) throws Exception {
        if (object == null) {
            setObjectData(null);
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        setObjectData(baos.toByteArray());
    }
    
    /*
     * Statics for accesscontrol.
     */
    static{
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
    
    
    
}
