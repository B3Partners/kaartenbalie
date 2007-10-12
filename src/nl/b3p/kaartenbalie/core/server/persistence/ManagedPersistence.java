/*
 * ManagedPersistence.java
 *
 * Created on October 1, 2007, 9:50 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ManagedPersistence {
    
    private static EntityManagerFactory emf;
    private static ThreadLocal tlEM = new ThreadLocal();
    static {
        emf = Persistence.createEntityManagerFactory("kaartenbaliePU");
    }
    
    public static void closeEmf() {
        emf.close();
    }
    
    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }
    
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
    
    public static void closeEntityManager() {
        EntityManager localEm = (EntityManager) tlEM.get();
        if (localEm != null) {
            localEm.close();
            tlEM.set(null);
        }
    }
    public static void main(String [] args) throws Exception {
        
    }
    
}
