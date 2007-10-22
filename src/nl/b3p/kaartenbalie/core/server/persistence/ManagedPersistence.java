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
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import nl.b3p.kaartenbalie.core.server.reporting.control.RequestReporting;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ManagedPersistence extends HttpServlet{
    public static final long serialVersionUID = 24574462L;
    private static final Log log = LogFactory.getLog(ManagedPersistence.class);
    private static EntityManagerFactory emf;
    private static ThreadLocal tlEM = new ThreadLocal();
    private static String defaultKaartenbaliePU = "defaultKaartenbaliePU";
    public static String nonServletKaartenbaliePU = "nonServletPU";
    
    public static void closeEmf() {
        if (emf == null)
            throw new Error("Cannot close emf as it is not yet initialized.");
        if (!emf.isOpen())
            throw new Error("Emf was already closed.");
        emf.close();
    }
    
    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null){
            throw new Error("EntityManagerFactory not yet initialized.");
        }
        return emf;
    }
    
    public static void openEntityManagerFactory(String persistenceUnit) {
        log.info("ManagedPersistence.openEntityManagerFactory(" + persistenceUnit + ")");
        if (emf != null)
            throw new Error("EntityManager already initialized or open.");
        if (persistenceUnit == null || persistenceUnit.trim().length() == 0) {
            throw new Error("PersistenceUnit cannot be left empty.");
        }
        emf = Persistence.createEntityManagerFactory(persistenceUnit);
    }
    
    
    
    /*
     * This will initialize the EntityManagerFactory when in a servlet context. There is no need
     * to call the method openEntityManagerFactory from anywhere else unless you're out of the 
     * servlet context (testing, etc..).
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        log.info("ManagedPersistence.init(" + config + ")");
        openEntityManagerFactory(defaultKaartenbaliePU);
        if (config.getInitParameter("reporting") != null) {
            RequestReporting.setReporting(config.getInitParameter("reporting").equalsIgnoreCase("enabled"));
        }
    }
    
    
    
    /*
     * Use this function where a ThreadLocal entityManager is not required i.e. in
     * controller classes and non servlet classes.
     */
    public static EntityManager createEntityManager() {
        return getEntityManagerFactory().createEntityManager();
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
