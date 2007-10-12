/*
 * ManagedPersistence.java
 *
 * Created on October 1, 2007, 9:50 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.persistence;

import java.net.InetAddress;
import java.net.URL;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ManagedPersistence {
    
    private static EntityManagerFactory emf;
    private static ThreadLocal tlEM = new ThreadLocal();
    private static String defaultKaartenbaliePU = "defaultKaartenbaliePU";
    
    
    /* 
     * This initializes the EntityManagerFactory. Very useful!
     */
    static {
        emf = Persistence.createEntityManagerFactory(getPersistenceUnitName());
    }
    
    /*
     * This function will load the right persistenceunit from the persistenceunit.properties file. It will search
     * the propertiesfile for a property that matches the computers hostname. This way, there is no need to edit 
     * persistenceunits for everyone individually. If you want to change your persistence unit, look up the properties
     * file and add or change the mapping that belongs to you.
     */
    public static String getPersistenceUnitName() {
        Properties props = new java.util.Properties();
        URL url = ClassLoader.getSystemResource("persistenceunit.properties");
        try {
            props.load(url.openStream());
            String result =  (String) props.get(InetAddress.getLocalHost().getHostName());
            if (result == null) {
                throw new Exception("Property for hostname not found. Using default.") ;
            }
            return result;
            
        } catch (Exception e) {
            return defaultKaartenbaliePU;
        }
    }

    
    public static void closeEmf() {
        emf.close();
    }
    
    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    /*
     * Use this function where a ThreadLocal entityManager is not required i.e. in 
     * controller classes and non servlet classes.
     */
    public static EntityManager createEntityManager() {
        return emf.createEntityManager();
    }
    
   /*
    This function is used to generate a request save EntityManger by storing it in the local thread. This method is
    * familiar to the methods used in the SessionContext with the exceptions that we do not register a cleanup after
    * the transaction has been finalized, transactions are not forced to start if queries are fired and there is no
    * support for multiple EntityManagers at this time. This last feature can be easily implemented by registering
    * a hashmap in the ThreadLocal.
    */
    public static EntityManager getEntityManager() {
        EntityManager localEm = (EntityManager) tlEM.get();
        if (localEm == null) {
            localEm = emf.createEntityManager();
            tlEM.set(localEm);
        }
        return localEm;
    }
    
    /* 
     * Always close your entitymanager when you're done with it. Else you might stumble into awkward situations where
     * multiple calls are done on the same entitymanager
     */
    public static void closeEntityManager() {
        EntityManager localEm = (EntityManager) tlEM.get();
        if (localEm != null) {
            localEm.close();
            tlEM.set(null);
        }
    }
    
}
