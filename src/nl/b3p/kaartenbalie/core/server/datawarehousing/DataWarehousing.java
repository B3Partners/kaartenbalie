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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.datawarehousing.domain.EntityClass;
import nl.b3p.kaartenbalie.core.server.datawarehousing.domain.EntityMutation;
import nl.b3p.kaartenbalie.core.server.datawarehousing.domain.EntityProperty;
import nl.b3p.kaartenbalie.core.server.datawarehousing.domain.PropertyValue;
import nl.b3p.kaartenbalie.core.server.datawarehousing.domain.WarehousedEntity;

import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;

/**
 *
 * @author Chris Kramer
 */
public class DataWarehousing {
    
    private static ThreadLocal tlObjects = new ThreadLocal();
    
    /*
     * Basically use this boolean to enable or disable the warehousing mechanism.
     */
    private static boolean enableWarehousing = false;
    private DataWarehousing() {
    }
    
    
    public static void setEnableDatawarehousing(boolean state) {
        enableWarehousing = state;
    }
    
    public static Long sizeOnDisk(Class objectClass, Integer primaryKey) throws Exception {
        EntityManager em = MyEMFDatabase.createEntityManager();
        Long dataSize = (Long) em.createQuery(
                "SELECT (SUM(LENGTH(pv.stringData)) + SUM(LENGTH(pv.objectData)))" +
                "FROM PropertyValue AS pv " +
                "WHERE pv.entityMutation.warehousedEntity.referencedId = :primaryKey " +
                "AND pv.entityMutation.warehousedEntity.entityClass.objectClass = :objectClass")
                .setParameter("primaryKey",primaryKey)
                .setParameter("objectClass",objectClass)
                .getSingleResult();
        em.close();
        return dataSize;
    }
    public static Object find(Class objectClass, Integer primaryKey) throws Exception {
        
        EntityManager em = MyEMFDatabase.createEntityManager();
        //First check if the entity still exists and possible save the trouble of building it again.
        Object object = em.find(objectClass, primaryKey);
        object = null;
        if (object != null) {
            return object;
        } else if (enableWarehousing) {
            
            /*
             * The object is probably deleted,.. Here is where the real work starts..
             * First lets see if we have a copy of this object somewhere in our warehouse..
             */
            WarehousedEntity we = null;
            try {
                we = (WarehousedEntity) em.createQuery(
                        "FROM WarehousedEntity AS we " +
                        "WHERE we.entityClass.objectClass = :objectClass " +
                        "AND we.referencedId = :referencedId")
                        .setParameter("objectClass", objectClass)
                        .setParameter("referencedId", primaryKey)
                        .getSingleResult();
            } catch (NoResultException nre) {
                // If this happens, this object is not managed. :( Sorry!
                return null;
            }
            /*
             * At this point we have a WarehousedEntity that matched the find request.
             * Now we have to transform it to a matching object of the requested class!
             * But first check if there is a mutation on this class.. There always should be one,
             * but you never know... Plus we need the latest mutation anyways...
             */
            EntityMutation entityMutation;
            try {
                entityMutation = (EntityMutation) em.createQuery(
                        "FROM EntityMutation AS ea " +
                        "WHERE ea.warehousedEntity.id = :warehousedEntityId " +
                        "ORDER BY ea.mutationDate DESC")
                        .setParameter("warehousedEntityId", we.getId())
                        .setMaxResults(1)
                        .getSingleResult();
            } catch (NoResultException nre) {
                throw new Exception("No EntityMutation found for WarehousedEntity with id " + we.getId() +". This should'nt be possible.");
            }
            object = objectClass.newInstance();
            
            /*
             * Now get the matching properties... (Only valid properties though)..
             *
             */
            
            List propertiesList = em.createQuery(
                    "FROM PropertyValue AS pv " +
                    "WHERE pv.entityMutation.id = :entityMutationId " +
                    "AND pv.entityProperty.dateDeleted = null")
                    .setParameter("entityMutationId", entityMutation.getId())
                    .getResultList();
            
            Iterator iterProps = propertiesList.iterator();
            while (iterProps.hasNext()) {
                PropertyValue propertyValue = (PropertyValue) iterProps.next();
                EntityProperty ep = propertyValue.getEntityProperty();
                Method setMethod = objectClass.getDeclaredMethod("set" + ep.getFieldName(),new Class[] {ep.getFieldClass()});
                setMethod.setAccessible(true);
                setMethod.invoke(object,new Object[] {propertyValue.requestValue()});
            }
            
            //Close the entitymanager..
            em.close();
            //Finally return the object
            return object;
        }
        return null;
    }
    
    /*
     * The DataWarehousing is processbased and runs in a ThreadLocal.
     */
    
    public static void begin() {
        if (!enableWarehousing) { return;}
        tlObjects.set(new ArrayList());
    }
    public static void enlist(Class clazz, Integer primaryKey, int objectAction) {
        if (!enableWarehousing) { return;}
        DwObjectAction doa = new DwObjectAction(clazz, primaryKey, objectAction);
        List objects = (List) tlObjects.get();
        objects.add(doa);
    }
    public static void end(){
        if (!enableWarehousing) { return;}
        List objects = (List) tlObjects.get();
        Iterator i = objects.iterator();
        EntityManager em = MyEMFDatabase.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        try {
            
            while (i.hasNext()) {
                
                DwObjectAction doa = (DwObjectAction) i.next();
                if (doa != null) {
                    switch(doa.getObjectAction()) {
                        case DwObjectAction.PERSIST:
                            persist(doa.getClazz(), doa.getPrimaryKey(), em);
                            break;
                        case DwObjectAction.MERGE:
                            merge(doa.getClazz(), doa.getPrimaryKey(), em);
                            break;
                        case DwObjectAction.REMOVE:
                            remove(doa.getClazz(), doa.getPrimaryKey(), em);
                            break;
                        case DwObjectAction.PERSIST_OR_MERGE:
                            persistOrMerge(doa.getClazz(), doa.getPrimaryKey(), em);
                            break;
                            
                    }
                }
            }
            et.commit();
        } catch(Exception e) {
            et.rollback();
        }
        em.close();
        
    }
    
    /*
     * Functions for Remove, persist and merge...
     */
    private static void remove(Class entityClass, Integer primaryKey,EntityManager em) throws Exception {
        
        WarehousedEntity we = getManagedEntity(entityClass, primaryKey, em);
        
        if (we != null) {
            we.setDateDeleted(new Date());
            em.flush();
        }
        
    }
    
    
    private static void persistOrMerge(Class entityClass, Integer primaryKey,EntityManager em ) throws Exception{
        WarehousedEntity we = getManagedEntity(entityClass, primaryKey, em);
        if (we == null) {
            persist(entityClass, primaryKey,em);
        } else {
            merge(entityClass, primaryKey,em);
        }
        
    }
    private static void persist(Class objectClass, Integer primaryKey,EntityManager em ) throws Exception{
        Object refDBObject = getReferedObject(objectClass, primaryKey, em);
        WarehousedEntity we = getManagedEntity(objectClass, primaryKey, em);
        if (we != null) {
            throw new Exception("Trying to persist an already existing object. Use " + DataWarehousing.class.getSimpleName() + ".merge() instead.");
        }
        EntityClass entityClass = getEntityClass(objectClass, em);
        we = new WarehousedEntity(entityClass, primaryKey);
        em.persist(we);
        reflectDataTemplates(we,refDBObject, true, em);
        em.flush();
    }
    
    private static EntityClass getEntityClass(Class objectClass, EntityManager em) {
        
        return (EntityClass) em.createQuery(
                "FROM EntityClass AS ec " +
                "WHERE ec.objectClass = :objectClass")
                .setParameter("objectClass", objectClass)
                .getSingleResult();
    }
    
    private static void merge(Class entityClass, Integer primaryKey,EntityManager em ) throws Exception{
        /*
         * First check if this entity is managed by hibernate and that it is persisted..
         */
        Object refDBObject = getReferedObject(entityClass, primaryKey, em);
        /*
         * Now check if the entity is already registered in the warehouse.
         */
        WarehousedEntity we = getManagedEntity(entityClass, primaryKey, em);
        if (we == null) {
            throw new Exception("Trying to merge a nonexisting object. Use " + DataWarehousing.class.getSimpleName() + ".persist() instead.");
        }
        if (we.getDateDeleted() != null) {
            throw new Exception("Trying to update an entity that is already deleted in the warehouse.");
        }
        /*
         * Now we have a WarehousedEntity. Thats good! Lets start reflecting our previously fetched entity
         * with its class.
         */
        reflectDataTemplates(we,refDBObject, false, em);
        em.flush();
    }
    
    public static void registerClass(Class clazz) {
        if (!enableWarehousing) { return;}
        EntityManager em = MyEMFDatabase.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        EntityClass ec = null;
   
        try {
            
            ec = (EntityClass) em.createQuery(
                    "FROM EntityClass AS ec " +
                    "WHERE ec.objectClass = :objectClass")
                    .setParameter("objectClass", clazz)
                    .getSingleResult();
        } catch (NoResultException nre) {
            ec = new EntityClass(clazz);
            em.persist(ec);
        }
        
        /*
         * Now add methods to your mapped class..
         */
        Method[] methods =  clazz.getDeclaredMethods();
        for (int i = 0; i< methods.length; i++) {
            Method method = methods[i];
            Class[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 1 && method.getName().startsWith("set")) {
                Class fieldClass = parameterTypes[0];
                if (PropertyValue.isAllowed(fieldClass)) {
                    method.setAccessible(true);
                    String fieldName = method.getName().substring(3,method.getName().length());
                    try {
                        em.createQuery(
                                "FROM EntityProperty AS ep " +
                                "WHERE ep.fieldName = :fieldName " +
                                "AND ep.fieldClass = :fieldClass " +
                                "AND ep.entityClass.id = :entityClassId " +
                                "AND ep.dateDeleted = null")
                                .setParameter("fieldName", fieldName)
                                .setParameter("fieldClass",fieldClass)
                                .setParameter("entityClassId", ec.getId())
                                .getSingleResult();
                    } catch (NoResultException nre) {
                        EntityProperty ep = new EntityProperty(ec, fieldClass, fieldName);
                        em.persist(ep);
                    }
                }
            }
        }
        /*
         * Now check for methods that are in de DB but not in the class and delete these..
         */
        List entityProperties = em.createQuery(
                "FROM EntityProperty AS ep " +
                "WHERE ep.entityClass.id = :entityClassId " +
                "AND ep.dateDeleted = null ")
                .setParameter("entityClassId", ec.getId())
                .getResultList();
        Iterator iterProps = entityProperties.iterator();
        while(iterProps.hasNext()) {
            
            EntityProperty ep = (EntityProperty) iterProps.next();
            try {
                clazz.getDeclaredMethod("set" + ep.getFieldName(),new Class[] {ep.getFieldClass()});
                clazz.getDeclaredMethod("get" + ep.getFieldName(),null);
            } catch (NoSuchMethodException nsme) {
                ep.setDateDeleted(new Date());
            }
            
        }
        et.commit();
        em.close();
    }
    
    public static void main(String [] args) throws Exception {
        
        
        MyEMFDatabase.openEntityManagerFactory(MyEMFDatabase.nonServletKaartenbaliePU);
        
        DataWarehousing.setEnableDatawarehousing(true);
        
        registerClass(User.class);
        EntityManager em = MyEMFDatabase.createEntityManager();
        List list = em.createQuery("FROM User").getResultList();
        
        Iterator i = null;
        for (int j = 0; j< 1; j++) {
            
            i = list.iterator();
            DataWarehousing.begin();
            while (i.hasNext()) {
                User object = (User) i.next();
                DataWarehousing.enlist(User.class,object.getId(), DwObjectAction.PERSIST_OR_MERGE);
            }
            DataWarehousing.end();
        }
        
        i = list.iterator();
        while (i.hasNext()) {
            User object = (User) i.next();
            Integer id = object.getId();
            User nextUser = (User) DataWarehousing.find(User.class, id);
            System.out.println(nextUser.getUsername() + ", something=" + nextUser.getTimeout() + ", bytes=" + sizeOnDisk(User.class, nextUser.getId()));
        }
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
     * Helper function that will return the warehousedEntity for an entityClass and primaryKey - that is, if it exists.
     * Else it will return null.
     */
    private static WarehousedEntity getManagedEntity(Class entityClass, Integer primaryKey, EntityManager em) {
        try {
            WarehousedEntity we = (WarehousedEntity) em.createQuery(
                    "FROM WarehousedEntity AS we " +
                    "WHERE we.entityClass.objectClass = :objectClass " +
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
    private static void reflectDataTemplates(WarehousedEntity we, Object refDBObject,  boolean isNewEntity, EntityManager em) throws Exception{
        
        EntityMutation entityMutation = new EntityMutation(we);
        em.persist(entityMutation);
        EntityClass entityClass = we.getEntityClass();
        Class objectClass = entityClass.getObjectClass();
        Iterator iterProps = entityClass.getEntityProperties().iterator();
        while (iterProps.hasNext()) {
            EntityProperty ep = (EntityProperty) iterProps.next();
            if (ep.getDateDeleted() == null) {
                
                try {
                    Method getMethod = objectClass.getDeclaredMethod("get" + ep.getFieldName(),null);
                    getMethod.setAccessible(true);
                    
                    Object object = getMethod.invoke(refDBObject, null);
                    
                    if (!PropertyValue.isAllowed(getMethod.getReturnType())) {
                        throw new Exception("Trying to persist an unsupported property '" + object.getClass().getSimpleName() + "'");
                    }
                    PropertyValue pv = new PropertyValue(ep, entityMutation);
                    pv.storeValue(object);
                    em.persist(pv);
                } catch (NoSuchMethodException nsme) {
                    throw new NoSuchMethodException(
                            "Could not handle get" + ep.getFieldName() + " for class " + objectClass + ". \n" +
                            "Your copy of this class may be out of date. \n" +
                            "Use " + DataWarehousing.class.getSimpleName() + ".registerClass(" + objectClass.getSimpleName() + ".class) to fix the problem. \n");
                }
            }
            
        }
    }
}

