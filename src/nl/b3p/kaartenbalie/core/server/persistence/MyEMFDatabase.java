/*
 * ManagedPersistence.java
 *
 * Created on October 1, 2007, 9:50 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.persistence;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import nl.b3p.kaartenbalie.core.server.datawarehousing.DataWarehousing;
import nl.b3p.kaartenbalie.core.server.reporting.control.DataMonitoring;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MyEMFDatabase extends HttpServlet{
    public static final long serialVersionUID = 24574462L;
    private static final Log log = LogFactory.getLog(MyEMFDatabase.class);
    private static EntityManagerFactory emf;
    private static ThreadLocal tlMap = new ThreadLocal();
    private static String entityManagerName = "entityManager";
    private static String defaultKaartenbaliePU = "defaultKaartenbaliePU";
    public static String nonServletKaartenbaliePU = "nonServletPU";
    public static String dtd = "/dtd/capabilities_1_1_1.dtd";
    private static String cachePath = null;
    private static Random rg = null;
    
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
        
        /*
         * First init the EntityManagerFactory
         */
        openEntityManagerFactory(defaultKaartenbaliePU);
        
        /*
         * Now check various initialization parameters..
         */
        DataMonitoring.setEnableMonitoring(getConfigValue(config, "reporting","disabled").equalsIgnoreCase("enabled"));
        DataWarehousing.setEnableDatawarehousing(getConfigValue(config, "warehousing","disabled").equalsIgnoreCase("enabled"));
        
        dtd = getConfigValue(config, "dtd","/dtd/capabilities_1_1_1.dtd");
        cachePath = getConfigValue(config, "cache",null);
        if (cachePath != null) {
            cachePath = getServletContext().getRealPath( cachePath );
            log.debug("cache pad: " + cachePath);
        }
        
        //
    }
    
    private static String getConfigValue(ServletConfig config, String parameter, String defaultValue) {
        log.info("ManagedPersistence.getConfigValue(config, " + parameter + ", " + defaultValue + ")");
        String tmpval =  config.getInitParameter(parameter);
        if (tmpval == null || tmpval.trim().length() == 0) {
            return defaultValue;
        }
        
        return tmpval.trim();
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
        EntityManager localEm = (EntityManager) getThreadLocal(entityManagerName);
        if (localEm == null) {
            localEm = emf.createEntityManager();
            setThreadLocal("entityManager", localEm);
        }
        return localEm;
    }
    
    
    /*
     * DataWarehousing Objects placeholder... This is required as objects can only be managed after the process completed...
     */
    
    public static void entityToPersist(Object object) {
        
    }
    
    public static void entityToMerge(Object object) {
        
    }
    public static void entityToRemove(Object object) {
        
    }
    
    public static void purgeDataWarehouse() {
        
    }
    
    /*
     * Thread Local Map Management...
     */
    private static void initThreadLocal() {
        tlMap.set(new HashMap());
    }
    
    private static void clearThreadLocal(String key) {
        Map threadLocalMap = (Map) tlMap.get();
        threadLocalMap.remove(key);
    }
    
    private static void setThreadLocal(String key, Object object) {
        if (tlMap.get() == null) {
            initThreadLocal();
        }
        Map threadLocalMap = (Map) tlMap.get();
        threadLocalMap.put(key,object);
    }
    
    private static Object getThreadLocal(String key) {
        if (tlMap.get() == null) {
            initThreadLocal();
            return null;
        }
        Map threadLocalMap = (Map) tlMap.get();
        return threadLocalMap.get(key);
    }
    
    /*
     * Always close your entitymanager when you're done with it. Else you might stumble into awkward situations where
     * multiple calls are done on the same entitymanager
     */
    public static void closeEntityManager() {
        EntityManager localEm = (EntityManager) getThreadLocal(entityManagerName);
        if (localEm != null) {
            localEm.close();
            clearThreadLocal(entityManagerName);
        }
    }
    
    
    /** Returns the local path of a filename.
     *
     * @param fileName String containing the fileName
     *
     * @return string containing the local path
     */
    // <editor-fold defaultstate="" desc="localPath(String fileName) method.">
    public static String localPath(String fileName) {
        if (fileName==null)
            return "";
        return cachePath + fileName;
    }
    // </editor-fold>
    
    /** Returns a unique name created with given parameters without taking the path into account.
     *
     * @param prefix String containing the prefix.
     * @param extension String containing the extension.
     *
     * @return a String representing a unique name for these parameters.
     */
    // <editor-fold defaultstate="" desc="uniqueName(String prefix, String extension) method.">
    public static String uniqueName(String prefix, String extension) {
        return uniqueName(prefix, extension, false);
    }
    // </editor-fold>
    
    /** Returns a unique name created with given parameters.
     *
     * @param prefix String containing the prefix.
     * @param extension String containing the extension.
     * @param includePath boolean setting the including of the path to true or false.
     *
     * @return a String representing a unique name for these parameters.
     */
    // <editor-fold defaultstate="" desc="uniqueName(String prefix, String extension, boolean includePath) method.">
    public static String uniqueName(String prefix, String extension, boolean includePath) {
        // Gebruik tijd in milliseconden om gerekend naar een radix van 36.
        // Hierdoor ontstaat een lekker korte code.
        long now = (new Date()).getTime();
        String val1 = Long.toString(now, Character.MAX_RADIX).toUpperCase();
        // random nummer er aanplakken om zeker te zijn van unieke code
        long rnum = (long) rg.nextInt(1000);
        String val2 = Long.toString(rnum, Character.MAX_RADIX).toUpperCase();
        String thePath = "";
        if (includePath)
            thePath = cachePath ;
        if (prefix==null)
            prefix = "";
        if (extension==null)
            extension = "";
        return thePath + prefix + val1 + val2 + extension;
    }
    // </editor-fold>
    
    /* Run this to sync your DB using the nonServlet PersistenceUnit*/
    public static void main(String [] args) throws Exception {
        MyEMFDatabase.openEntityManagerFactory(MyEMFDatabase.nonServletKaartenbaliePU);
        
    }
    
    public static String getDtd() {
        return dtd;
    }
    
}
