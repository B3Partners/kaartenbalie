/*
 * Warehouse.java
 *
 * Created on October 29, 2007, 9:02 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.datawarehousing;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.datawarehousing.domain.DataMappingTemplate;
import nl.b3p.kaartenbalie.core.server.datawarehousing.domain.DateDataMapping;
import nl.b3p.kaartenbalie.core.server.datawarehousing.domain.IntegerDataMapping;
import nl.b3p.kaartenbalie.core.server.datawarehousing.domain.StringDataMapping;
import nl.b3p.kaartenbalie.core.server.datawarehousing.domain.WarehousedEntity;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;

/**
 *
 * @author Chris Kramer
 */
public class Warehouse {
    
    private static Map dataMappings;
    
    private Warehouse() {
    }
    
    static {
        dataMappings = new HashMap();
        dataMappings.put(String.class,StringDataMapping.class);
        dataMappings.put(Integer.class,IntegerDataMapping.class);
        dataMappings.put(Date.class,DateDataMapping.class);
    }
    
    
    public static Object find(Class entityClass, Integer primaryKey) throws Exception {
        EntityManager em = MyEMFDatabase.createEntityManager();
        //First check if the entity still exists and possible save the trouble of building it again.
        Object object = em.find(entityClass, primaryKey);
        object = null;
        if (object != null) {
            return object;
        } else {
            /*
             * The object is probably deleted,.. Here is where the real work starts..
             * First lets see if we have a copy of this object somewhere in our warehouse..
             */
            WarehousedEntity we = null;
            try {
                we = (WarehousedEntity) em.createQuery(
                        "FROM WarehousedEntity AS we " +
                        "WHERE we.objectClass = :objectClass " +
                        "AND we.referencedId = :referencedId")
                        .setParameter("objectClass", entityClass)
                        .setParameter("referencedId", primaryKey)
                        .getSingleResult();
            } catch (NoResultException nre) {
                // If this happens, this object is not managed. :( Sorry!
                return null;
            }
            /*
             * At this point we have a WarehousedEntity that matched the find request.
             * Now we have to transform it to a matching object of the requested class!
             */
            object = entityClass.newInstance();
            Method[] methods =  entityClass.getDeclaredMethods();
            
            for (int i = 0; i< methods.length; i++) {
                Method method = methods[i];
                Class[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 1 && method.getName().startsWith("set")) {
                    if (getDataMappingForClass(parameterTypes[0]) != null) {
                        method.setAccessible(true);
                        String fieldReference = method.getName().substring(3,method.getName().length());
                        try {
                            DataMappingTemplate dmt = (DataMappingTemplate) em.createQuery(
                                    "FROM DataMappingTemplate AS dmt " +
                                    "WHERE dmt.fieldReference = :fieldReference " +
                                    "AND dmt.warehousedEntity = :warehousedEntity")
                                    .setParameter("fieldReference",fieldReference)
                                    .setParameter("warehousedEntity", we)
                                    .getSingleResult();
                            method.invoke(object, new Object[] {dmt.getValue()});
                        } catch (NoResultException nre) {
                            // nre.printStackTrace();
                        }
                    }
                }
            }
            //Close the entitymanager..
            em.close();
            //Finally return the object
            return object;
        }
    }
    
    public static void remove(Class entityClass, Integer primaryKey) throws Exception {
        EntityManager em = MyEMFDatabase.createEntityManager();
        WarehousedEntity we = getManagedEntity(entityClass, primaryKey, em);
        
        EntityTransaction et = em.getTransaction();
        et.begin();
        
        if (we == null) {
            /*
             * This is tricky! First you say you're about to delete an entity from the database. What is that entity is not
             * yet in our warehouse? We're going to add it first.. And then mark it as deleted.
             */
            persist(entityClass, primaryKey);
            //Then fetch it again..
            we = getManagedEntity(entityClass, primaryKey, em);
        }
        we.setDateDeleted(new Date());
        et.commit();
        em.close();
    }
    
    public static void persist(Class entityClass, Integer primaryKey) throws Exception{
        EntityManager em = MyEMFDatabase.createEntityManager();
        Object refDBObject = getReferedObject(entityClass, primaryKey, em);
        WarehousedEntity we = getManagedEntity(entityClass, primaryKey, em);
        if (we != null) {
            throw new Exception("Trying to persist an already existing object. Use " + Warehouse.class.getSimpleName() + ".merge() instead.");
        }
        EntityTransaction et = em.getTransaction();
        et.begin();
        we = new WarehousedEntity(entityClass, primaryKey);
        em.persist(we);
        reflectDataTemplates(we,entityClass, refDBObject, true, em);
        et.commit();
        em.close();
    }
    
    public static void merge(Class entityClass, Integer primaryKey) throws Exception{
        EntityManager em = MyEMFDatabase.createEntityManager();
        /*
         * First check if this entity is managed by hibernate and that it is persisted..
         */
        Object refDBObject = getReferedObject(entityClass, primaryKey, em);
        /*
         * Now check if the entity is already registered in the warehouse.
         */
        WarehousedEntity we = getManagedEntity(entityClass, primaryKey, em);
        if (we == null) {
            throw new Exception("Trying to merge a nonexisting object. Use " + Warehouse.class.getSimpleName() + ".persist() instead.");
        }
        if (we.getDateDeleted() != null) {
            throw new Exception("Trying to update an entity that is already deleted in the warehouse.");
        }
        /*
         * Now we have a WarehousedEntity. Thats good! Lets start reflecting our previously fetched entity
         * with its class.
         */
        EntityTransaction et = em.getTransaction();
        et.begin();
        reflectDataTemplates(we,entityClass, refDBObject, false, em);
        we.setDateLastUpdated(new Date());
        et.commit();
        em.close();
    }
    
    
    
    public static void main(String [] args) throws Exception {
        
        MyEMFDatabase.openEntityManagerFactory(MyEMFDatabase.nonServletKaartenbaliePU);
        
        for (int i = 0; i< 20; i++) {
            try {
                int nextRandom = 1 + (int)(Math.random() * 20);
                Warehouse.remove(User.class,new Integer(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("fetching...");
        /*
        for (int i = 0; i< 5; i++) {
         
         
            int nextRandom = 1 + (int)(Math.random() * 20);
            Warehouse.remove(User.class, new Integer(nextRandom));
         
            User userFromWarehouse = (User) Warehouse.find(User.class, new Integer(i));
            System.out.println("User:"+ userFromWarehouse);
            if (userFromWarehouse != null){
                System.out.println("UserName:" + userFromWarehouse.getUsername());
                System.out.println("Id:" + userFromWarehouse.getId());
         
            }
         
        }
         */
        
    }
    
   /*
    * Helper function to find a referedobject in the DB or to throw an exception if the object is not managed.
    */
    private static Object getReferedObject(Class entityClass, Integer primaryKey, EntityManager em) throws Exception {
        Object refDBObject = em.find(entityClass,primaryKey);
        if (refDBObject == null) {
            throw new NullPointerException("Entity for " + entityClass.getCanonicalName() + " with primarykey '" + primaryKey + "' could not be found.");
        }
        return refDBObject;
    }
    
    /*
     * Helper function which gets the right DataMappingClass for a requestedClass or returns null if the requestedClass
     * is not supported.
     */
    private static Class getDataMappingForClass(Class requestedClass) throws Exception {
        if (dataMappings.containsKey(requestedClass)) {
            return  (Class) dataMappings.get(requestedClass);
        }
        return null;
    }
    
    /*
     * Helper function that will return the warehousedEntity for an entityClass and primaryKey - that is, if it exists.
     * Else it will return null.
     */
    private static WarehousedEntity getManagedEntity(Class entityClass, Integer primaryKey, EntityManager em) {
        try {
            WarehousedEntity we = (WarehousedEntity) em.createQuery(
                    "FROM WarehousedEntity AS we " +
                    "WHERE we.objectClass = :objectClass " +
                    "AND we.referencedId = :referencedId")
                    .setParameter("objectClass", entityClass)
                    .setParameter("referencedId", primaryKey)
                    .getSingleResult();
            return we;
        }catch (NoResultException nre) {
            return null;
        }
    }
    
    /*
     * Helper function that will update or create dataTemplates for the given warehousedEntity.
     */
    private static void reflectDataTemplates(WarehousedEntity we, Class entityClass, Object refDBObject,  boolean isNewEntity, EntityManager em) throws Exception{
        
        Method[] methods = entityClass.getDeclaredMethods();
        List dmtList = null;
        if (!isNewEntity) {
            dmtList = em.createQuery(
                    "FROM DataMappingTemplate AS dmt " +
                    "WHERE dmt.warehousedEntity.id = :warehousedEntityId")
                    .setParameter("warehousedEntityId", we.getId())
                    .getResultList();
        }
        
        for (int i = 0; i< methods.length; i++) {
            Method method = methods[i];
            // Check if it is a getter method.. Those are the methods we wanna have!
            if (method.getName().toLowerCase().startsWith("get")) {
                //Make is accessible...
                method.setAccessible(true);
                //Create a field reference.. This will used for many things..
                String fieldReference = method.getName().substring(3,method.getName().length());
                //Find the returnType of this method.
                Class methodReturnType = method.getReturnType();
                Class dmtClass = getDataMappingForClass(method.getReturnType());
                
                /*
                 * Now check if this there is already an entity that matches this template.
                 * If there is one, use it. If there is none, create it...
                 */
                DataMappingTemplate dmt = null;
                if (dmtClass != null) {
                    boolean newDmt = true;
                    if (!isNewEntity) {
                        Iterator j = dmtList.iterator();
                        while (j.hasNext()) {
                            DataMappingTemplate tempDmt = (DataMappingTemplate) j.next();
                            if (tempDmt.getFieldReference().equals(fieldReference) && tempDmt.getClass().equals(dmtClass)) {
                                dmt = tempDmt;
                                newDmt = false;
                            }
                        }
                    }
                    if (newDmt) {
                        dmt=  (DataMappingTemplate) dmtClass.newInstance();
                    }
                    /*
                     * Finally set the DataMappingTemplate value and if its new, set some other things aswell.
                     */
                    if (dmt != null) {
                        dmt.setValue( method.invoke(refDBObject, null));
                        if (newDmt) {
                            dmt.setWarehousedEntity(we);
                            dmt.setFieldReference(fieldReference);
                            em.persist(dmt);
                        }
                    }
                }
            }
        }
    }
    
    
    
}
