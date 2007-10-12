/**
 * @(#)MyDatabase.java
 * @author R. Braam
 * @version 1.00 2006/09/07
 *
 * Purpose: a class for initializing the database and creating a connection to it.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.service;

import java.util.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.LogManager;
import org.hibernate.*;

public class MyDatabase extends HttpServlet {
    
    private static Log log = LogFactory.getLog(MyDatabase.class);
    private static Random rg = null;
    private static String cachePath = null;
    private static SessionFactory sessionFactory;
    private static String hibernateInitExceptionMessage;
    private static String dtd = "/dtd/capabilities_1_1_1.dtd";
    
    public static final long serialVersionUID = 24362462L;
    
    /** Initializes the servlet.
     *
     * @param config ServletConfig configuration file in which is described how to configure the servlet.
     */
    // <editor-fold defaultstate="" desc="init(ServletConfig config) method.">
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        // Zet de logger
        if (log.isInfoEnabled())
            log.info("Initializing MyDatabase servlet");
        
        String value = config.getInitParameter("dtd");
        if (value!=null && value.length()>0)
            dtd = value;
        value = config.getInitParameter("cache");
        if (value!=null && value.length()>0)
            cachePath = getServletContext().getRealPath( value );
        log.debug("cache pad: " + cachePath);
        
        // Randomizer
        rg = new Random();
        
        // Initialize Hibernate session
        try {
            initHibernate();
        } catch(Exception e) {
            throw new ServletException(e);
        }
        
    }
    // </editor-fold>
    
    /** Destroys the servlet.
     */
    // <editor-fold defaultstate="" desc="destroy() method.">
    public void destroy() {
        
        /* Matthijs: fix voor memleak bij vaak reloaden
         * http://marc.theaimsgroup.com/?t=109578393000004&r=1&w=2
         */
        java.beans.Introspector.flushCaches();
        
        /* sluit Hibernate af */
        log.info("closing Hibernate SessionFactory");
        sessionFactory.close();
        
        LogManager.shutdown();
        LogFactory.releaseAll();
        
        super.destroy();
    }
    // </editor-fold>
    
    /** Initializes Hibernate.
     */
    // <editor-fold defaultstate="" desc="initHibernate() method.">
    public static void initHibernate() throws Exception {
        
        try {
            // Create the SessionFactory
            sessionFactory = new org.hibernate.cfg.Configuration().configure().buildSessionFactory();
        } catch (Exception ex) {
            // Make sure you log the exception, as it might be swallowed
            log.error("Hibernate init error: Initial SessionFactory creation failed.", ex);
            
            hibernateInitExceptionMessage = ex.getMessage();
            if(ex.getCause() != null) {
                hibernateInitExceptionMessage += ": " + ex.getCause().getMessage();
            }
            throw ex;
        }
    }
    // </editor-fold>
    
    /** Returns the SessionFactory of Hibernate.
     */
    // <editor-fold defaultstate="" desc="getSessionFactory() method.">
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    // </editor-fold>
    
    /** Returns the current session of Hibernate.
     */
    // <editor-fold defaultstate="" desc="currentSession() method.">
    public static Session currentSession() {
        return getSessionFactory().getCurrentSession();
    }
    // </editor-fold>
    
    /** Closes the current session of Hibernate.
     */
    // <editor-fold defaultstate="" desc="closeSession() method.">
    public static void closeSession() {
        currentSession().close();
        return;
    }
    // </editor-fold>
    
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
    
    public static String getDtd() {
        return dtd;
    }
    
}