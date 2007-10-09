/*
 * ManagedPersistence.java
 *
 * Created on October 1, 2007, 9:50 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ManagedPersistence {
    
    protected EntityManager em;
    private static EntityManagerFactory emf;

    public ManagedPersistence() {
        em = emf.createEntityManager();
    }

    public void close() {
        if (em != null) {
            em.close();
        }
    }
    
    static {
        emf = Persistence.createEntityManagerFactory("kaartenbaliePU");
    }
    
    public static EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }
    
    public static void closeEmf()
    {
        emf.close();
    }
    
    
}
