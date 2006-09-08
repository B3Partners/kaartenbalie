/*
 * MyDatabase.java
 *
 * Created on 7 september 2006, 13:22
 */

package nl.b3p.kaartenbalie.service;

/**
 *
 * @author Roy
 */import java.util.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.NullEnumeration;
import org.hibernate.*;

public class MyDatabase extends HttpServlet {
    
    private static Log log = LogFactory.getLog(MyDatabase.class);
    
    private static Random rg = null;
    private static String cachePath = null;
    
    private static SessionFactory sessionFactory;
    private static String hibernateInitExceptionMessage;
    
    
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        // Zet de logger
        if (log.isInfoEnabled())
            log.info("Initializing MyDatabase servlet");
        
        // Initialize cache pad
        try {
            cachePath = getServletContext().getRealPath( config.getInitParameter("cache") );
            log("cache pad: " + cachePath);
        } catch (Exception e) {
            log("Cache Path load exception", e);
            throw new ServletException("Cannot load cache path");
        }
        
        // Randomizer
        rg = new Random();
        
        // Initialize Hibernate session
        try {
            initHibernate();
        } catch(Exception e) {
            throw new ServletException(e);
        }
        
    }
    
    /** Destroys the servlet.
     */
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
    
    /**
     * Initializes Hibernate
     */
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
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static Session currentSession() {
        return getSessionFactory().getCurrentSession();
    }
    
    public static void closeSession() {
        currentSession().close();
        return;
    }
    
    public static String localPath(String fileName) {
        if (fileName==null)
            return "";
        return cachePath + fileName;
    }
    
    public static String uniqueName(String prefix, String extension) {
        return uniqueName(prefix, extension, false);
    }
    
    public static String uniqueName(String prefix, String extension, boolean includePath) {
        // Gebruik tijd in milliseconden om gerekend naar een radix van 36.
        // Hierdoor ontstaat een lekker korte code.
        long now = (long) (new Date()).getTime();
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
    
}