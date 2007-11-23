package nl.b3p.kaartenbalie.core.server.datawarehousing;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.datawarehousing.domain.EntityClass;
import nl.b3p.kaartenbalie.core.server.datawarehousing.domain.EntityMutation;
import nl.b3p.kaartenbalie.core.server.datawarehousing.domain.EntityProperty;
import nl.b3p.kaartenbalie.core.server.datawarehousing.domain.PropertyValue;
import nl.b3p.kaartenbalie.core.server.datawarehousing.domain.WarehousedEntity;

import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.ServiceProvider;

/**
 *
 * @author Chris Kramer
 */
public class DataWarehousing {
    
    
    
    /*
     * Basically use this boolean to enable or disable the warehousing mechanism.
     */
    private static boolean enableWarehousing = false;
    
    public static int PERFORMANCE = 2;
    public static int SECURITY = 1;
    public static int DEFAULT = 0;
    private static int setSafety = DEFAULT;
    
    
    
    //
    private List objects;
    private int safetyMode;
    private Map entityClassMap;
    
    public DataWarehousing() {
        if (!enableWarehousing) {return;}
    }
    
    /*
     * The DataWarehousing is processbased... The sequence is begin, enlist and end. Before the end function is
     * fired, the changeProcessSafetymode may be changed.
     */
    public void begin() {
        if (!enableWarehousing) { return;}
        objects = new ArrayList();
        safetyMode = setSafety;
        entityClassMap = new HashMap();
    }
    public void changeProcessSafetymode(int safetyMode) {
        if (!enableWarehousing) { return;}
        this.safetyMode = safetyMode;
    }
    public void enlist(Class clazz, Integer primaryKey, int objectAction) {
        if (!enableWarehousing) { return;}
        DwObjectAction doa = new DwObjectAction(clazz, primaryKey, objectAction);
        objects.add(doa);
    }
    public void end(){
        if (!enableWarehousing) { return;}
        Iterator i = objects.iterator();
        EntityManager em = MyEMFDatabase.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        if (safetyMode == DEFAULT) {
            safetyMode = setSafety;
        }
        try {
            
            long time = System.currentTimeMillis();
            long persist = 0;
            long merge = 0;
            long delete = 0;
            long persistmerge = 0;
            int r_persist = 0;
            int r_merge = 0;
            int r_delete = 0;
            int r_persistmerge = 0;
            while (i.hasNext()) {
                DwObjectAction doa = (DwObjectAction) i.next();
                if (doa != null) {
                    switch(doa.getObjectAction()) {
                        case DwObjectAction.PERSIST:
                            long tp = System.currentTimeMillis();
                            persist(doa.getClazz(), doa.getPrimaryKey(), em, safetyMode);
                            persist += (System.currentTimeMillis() - tp);
                            r_persist++;
                            break;
                        case DwObjectAction.MERGE:
                            long tm = System.currentTimeMillis();
                            merge(doa.getClazz(), doa.getPrimaryKey(), em,safetyMode);
                            merge += (System.currentTimeMillis() - tm);
                            r_merge++;
                            break;
                        case DwObjectAction.REMOVE:
                            long tr = System.currentTimeMillis();
                            remove(doa.getClazz(), doa.getPrimaryKey(), em, safetyMode);
                            delete += (System.currentTimeMillis() - tr);
                            r_delete++;
                            break;
                        case DwObjectAction.PERSIST_OR_MERGE:
                            long tpm = System.currentTimeMillis();
                            persistOrMerge(doa.getClazz(), doa.getPrimaryKey(), em, safetyMode);
                            persistmerge+= (System.currentTimeMillis() - tpm);
                            r_persistmerge ++;
                            break;
                    }
                }
            }
            et.commit();
            
            
        } catch(Exception e) {
            e.printStackTrace();
            
            et.rollback();
        }
        em.close();
        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public static void setDefaultSafety(String safety) {
        safety =  safety.trim().toLowerCase();
        if (safety.equals("performance")) {
            setSafety = PERFORMANCE;
        } else if (safety.equals("security")) {
            setSafety = SECURITY;
        } else {
            setSafety = DEFAULT;
        }
    }
    public static void setEnableDatawarehousing(boolean state) {
        enableWarehousing = state;
    }
    
    public static Object find(Class objectClass, Integer primaryKey) throws Exception {
        EntityManager em = MyEMFDatabase.createEntityManager();
        //First check if the entity still exists and possible save the trouble of building it again.
        
        Object object = em.find(objectClass, primaryKey);
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
     * Functions for Remove, persist and merge...
     */
    private void remove(Class objectClass, Integer primaryKey,EntityManager em, int safetyMode) throws Exception {
        
        if (safetyMode == PERFORMANCE) {
            
            
            EntityClass entityClass = getEntityClass(objectClass, em);
            em.createQuery(
                    "UPDATE WarehousedEntity  " +
                    "SET dateDeleted = :dateDeleted " +
                    "WHERE referencedId = :referencedId AND entityClass.id = :entityClassId")
                    .setParameter("dateDeleted", new Date())
                    .setParameter("referencedId",primaryKey)
                    .setParameter("entityClassId",entityClass.getId())
                    .executeUpdate();
            
        } else {
            WarehousedEntity we = getManagedEntity(objectClass, primaryKey, em);
            
            if (we != null) {
                we.setDateDeleted(new Date());
                if (safetyMode != PERFORMANCE) {
                    em.flush();
                }
                
            }
            
            
            
        }
        
        
    }
    
    
    private void persistOrMerge(Class objectClass, Integer primaryKey,EntityManager em,int safetyMode ) throws Exception{
        WarehousedEntity we = getManagedEntity(objectClass, primaryKey, em);
        if (we == null) {
            persist(objectClass, primaryKey,em, safetyMode);
        } else {
            merge(objectClass, primaryKey,em, safetyMode);
        }
        
    }
    private void persist(Class objectClass, Integer primaryKey,EntityManager em,int safetyMode ) throws Exception{
        Object refDBObject = getReferedObject(objectClass, primaryKey, em);
        WarehousedEntity we = null;
        
        if (safetyMode != PERFORMANCE) {
            we = getManagedEntity(objectClass, primaryKey, em);
            if (we != null) {
                throw new Exception("Trying to persist an already existing object. Use " + DataWarehousing.class.getSimpleName() + ".merge() instead.");
            }
        }
        EntityClass entityClass = getEntityClass(objectClass, em);
        we = new WarehousedEntity(entityClass, primaryKey);
        
        
        reflectDataTemplates(we,refDBObject, true, em);
        em.persist(we);
        if (safetyMode != PERFORMANCE) {
            em.flush();
        }
        
    }
    
    private EntityClass getEntityClass(Class objectClass, EntityManager em) {
        
        if (entityClassMap.get(objectClass) != null) {
            return (EntityClass) entityClassMap.get(objectClass);
        } else {
            EntityClass entityClass = (EntityClass) em.createQuery(
                    "FROM EntityClass AS ec " +
                    "WHERE ec.objectClass = :objectClass")
                    .setParameter("objectClass", objectClass)
                    .getSingleResult();
            entityClassMap.put(objectClass, entityClass);
            return entityClass;
        }
    }
    
    private static void merge(Class objectClass, Integer primaryKey,EntityManager em,int safetyMode ) throws Exception{
        
        if (safetyMode != PERFORMANCE) {
        }
        /*
         * First check if this entity is managed by hibernate and that it is persisted..
         */
        Object refDBObject = getReferedObject(objectClass, primaryKey, em);
        /*
         * Now check if the entity is already registered in the warehouse.
         */
        WarehousedEntity we = getManagedEntity(objectClass, primaryKey, em);
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
        if (safetyMode != PERFORMANCE) {
            em.flush();
        }
        
    }
    
    public static void registerClass(Class clazz, String[] fields) throws Exception {
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
        Method[] methods = null;
        if (fields == null ) {
            methods =  clazz.getDeclaredMethods();
        } else {
            methods = new Method[fields.length *2];
            for (int i = 0; i < fields.length ;i++) {
                try {
                    Method getterMethod = clazz.getDeclaredMethod("get" + fields[i], new Class[]{});
                    Method setterMethod = clazz.getDeclaredMethod("set" + fields[i], new Class[] {getterMethod.getReturnType()});
                    methods[i*2] = getterMethod;
                    methods[(i*2) + 1] = setterMethod;
                } catch (Exception e) {
                    et.rollback();
                    throw e;
                }
            }
        }
        
        for (int i = 0; i< methods.length; i++) {
            
            Method method = methods[i];
            Class[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 1 && method.getName().startsWith("set")) {
                Class fieldClass = parameterTypes[0];
                if (PropertyValue.isAllowed(fieldClass)) {
                    method.setAccessible(true);
                    String fieldName = method.getName().substring(3,method.getName().length());
                    try {
                        
                        EntityProperty ep = (EntityProperty) em.createQuery(
                                "FROM EntityProperty AS ep " +
                                "WHERE ep.fieldName = :fieldName " +
                                "AND ep.fieldClass = :fieldClass " +
                                "AND ep.entityClass.id = :entityClassId ")
                                .setParameter("fieldName", fieldName)
                                .setParameter("fieldClass",fieldClass)
                                .setParameter("entityClassId", ec.getId())
                                .getSingleResult();
                        
                        if (ep.getDateDeleted() != null) {
                            ep.setDateDeleted(null);
                        }
                    } catch (NoResultException nre) {
                        EntityProperty ep = new EntityProperty(ec, fieldClass, fieldName);
                        em.persist(ep);
                    }
                }
            }
        }
        /*
         * Now check for methods that are in de DB but not in the method set and delete these..
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
            boolean found = false;
            for (int i = 0; i< methods.length; i++) {
                Method method = methods[i];
                if (ep.getFieldClass().equals(method.getReturnType())
                && method.getName().equals("get" + ep.getFieldName())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                ep.setDateDeleted(new Date());
            }
            /*
            try {
                clazz.getDeclaredMethod("set" + ep.getFieldName(),new Class[] {ep.getFieldClass()});
                clazz.getDeclaredMethod("get" + ep.getFieldName(),null);
            } catch (NoSuchMethodException nsme) {
             
            }
             */
            
        }
        et.commit();
        em.close();
    }
    
    public static void main(String [] args) throws Exception {
        
        
        MyEMFDatabase.openEntityManagerFactory(MyEMFDatabase.nonServletKaartenbaliePU);
        
        DataWarehousing.setEnableDatawarehousing(true);
        
        
        Class userClass = User.class;
        String[] fields = new String[] {};
        
        
        DataWarehousing.registerClass(User.class, null);
        DataWarehousing.registerClass(Organization.class,null);
        DataWarehousing.registerClass(ServiceProvider.class,null);
        DataWarehousing.registerClass(Layer.class, new String[]{"Id","Name","Title"});
        EntityManager em = MyEMFDatabase.createEntityManager();
        EntityTransaction et = em.getTransaction();
        List list = em.createQuery("FROM User").getResultList();
        et.begin();
        em.createQuery("DELETE FROM PropertyValue").executeUpdate();
        em.createQuery("DELETE FROM EntityMutation").executeUpdate();
        em.createQuery("DELETE FROM WarehousedEntity").executeUpdate();
        em.createQuery("DELETE FROM EntityProperty").executeUpdate();
        em.createQuery("DELETE FROM EntityClass").executeUpdate();
        et.commit();
        System.exit(0);
        DataWarehousing dw = new DataWarehousing();
        
        for (int k = 0; k< 3; k++) {
            
            long time = 0;
            for (int l = 0; l< 1; l++) {
                
                Iterator i = null;
                
                for (int j = 0; j< 10; j++) {
                    
                    i = list.iterator();
                    dw.begin();
                    dw.changeProcessSafetymode(k);
                    while (i.hasNext()) {
                        User object = (User) i.next();
                        dw.enlist(User.class,object.getId(), DwObjectAction.PERSIST_OR_MERGE);
                    }
                    dw.end();
                }
                
                
                
                
            }
            
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

