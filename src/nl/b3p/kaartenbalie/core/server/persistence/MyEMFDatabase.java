/*
 * ManagedPersistence.java
 *
 * Created on October 1, 2007, 9:50 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.persistence;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.accounting.AccountManager;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPricing;
import nl.b3p.kaartenbalie.core.server.datawarehousing.DataWarehousing;
import nl.b3p.kaartenbalie.core.server.reporting.control.DataMonitoring;
import nl.b3p.kaartenbalie.core.server.reporting.control.ReportGenerator;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.ServiceProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MyEMFDatabase extends HttpServlet {
    
    private static final Log log = LogFactory.getLog(MyEMFDatabase.class);

    public static final long serialVersionUID = 24574462L;
    
    private static EntityManagerFactory emf;
    private static ThreadLocal tlMap = new ThreadLocal();
    private static String entityManagerName = "entityManager";
    private static String datawarehouseName = "datawareHouse";
    private static String defaultKaartenbaliePU = "defaultKaartenbaliePU";
    public static String nonServletKaartenbaliePU = "nonServletPU";
    
    public static String capabilitiesdtd = "/dtd/capabilities_1_1_1.dtd";
    public static String exceptiondtd = "/dtd/exception_1_1_1.dtd";
    
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
        AccountManager.setEnableAccounting(getConfigValue(config, "accounting","disabled").equalsIgnoreCase("enabled"));
        ReportGenerator.startupClear();
        try {
            DataWarehousing.registerClass(User.class, null);
            DataWarehousing.registerClass(Organization.class,null);
            DataWarehousing.registerClass(ServiceProvider.class,null);
            DataWarehousing.registerClass(LayerPricing.class,null);
            DataWarehousing.registerClass(Layer.class, new String[]{"Id","Name","Title"});
        } catch (Exception e) {
            throw new ServletException(e);
        }
        
        
        capabilitiesdtd = getConfigValue(config, "dtd","/dtd/capabilities_1_1_1.dtd");
        exceptiondtd = getConfigValue(config, "dtd","/dtd/exception_1_1_1.dtd");
        
        cachePath = getConfigValue(config, "cache", "/");
        if (cachePath != null) {
            cachePath = getServletContext().getRealPath( cachePath );
            log.debug("cache pad: " + cachePath);
        }
        
        // configure kb via properties
        KBConfiguration.configure();
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
    * These functions are used to generate a request save EntityManger by storing it in the local thread. This method is
    * familiar to the methods used in the SessionContext with the exceptions that we do not register a cleanup after
    * the transaction has been finalized, transactions are not forced to start if queries are fired and there is no
    * support for multiple EntityManagers at this time. This last feature can be easily implemented by registering
    * a hashmap in the ThreadLocal.
    *
    * - initEntityManager() will start a new EntityManager
    * - getEntityManager() is used to retrieve the created EntityManager
    * - closeEntityManager() is the final step in the proces.
    */
    public static void initEntityManager() throws Error {
        if (getThreadLocal(entityManagerName) != null) {
            throw new Error("Trying to init a new entityManager without closing the previous first!");
        }
        EntityManager entityManager = createEntityManager();
        setThreadLocal(entityManagerName, entityManager);
    }
    
    public static EntityManager getEntityManager() throws Error{
        EntityManager localEm = (EntityManager) getThreadLocal(entityManagerName);
        if (localEm == null) {
            throw new Error("EntityManager not yet initialized. Either it's already closed or never initialized.");
        }
        return localEm;
    }
    
    /*
     * Always close your entitymanager when you're done with it. Else you might stumble into awkward situations where
     * multiple calls are done on the same entitymanager
     */
    public static void closeEntityManager() throws Error{
        EntityManager localEm = (EntityManager) getThreadLocal(entityManagerName);
        if (localEm == null) {
            throw new Error("EntityManager is missing. Either it's already closed or never initialized.");
        }
        localEm.close();
        clearThreadLocal(entityManagerName);
        
    }
    
    /*
     * Just as getEntitymanager, this function retrieves an object from the local thread. This time it is the
     * DataWarehousing Object.
     */
    public static DataWarehousing getDataWarehouse() {
        DataWarehousing localDw = (DataWarehousing) getThreadLocal(datawarehouseName);
        if (localDw == null) {
            localDw = new DataWarehousing();
            setThreadLocal(datawarehouseName, localDw);
        }
        return localDw;
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
        EntityManager em = MyEMFDatabase.createEntityManager();
        em.createQuery("From Layer").getResultList();
        /* Snippet to make a PNG
        BufferedImage bi = new BufferedImage(200,200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) bi.getGraphics();
        Color color = new Color(100,100,100);
        AlphaComposite myAlpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f);
        g2d.setColor(color);
        g2d.setComposite(myAlpha);
        g2d.fillRect(0,0,200,200);
         
        ImageIO.write(bi, "png", new File("C:\\alphabackground.png"));
         */
        
    }

    public static String getCapabilitiesdtd() {
        return capabilitiesdtd;
    }

    public static String getExceptiondtd() {
        return exceptiondtd;
    }
    
}
