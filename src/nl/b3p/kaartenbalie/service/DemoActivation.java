/**
 * @(#)DemoActivation.java
 * @author N. de Goeij
 * @version 1.00 2007/05/31
 *
 * Purpose: a class for initializing the demo.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.service;

import java.util.*;
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

public class DemoActivation extends HttpServlet {
    
    private static Log log = LogFactory.getLog(MyDatabase.class);
    private static String active = null;
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
            log.info("Initializing DemoActivation servlet");
        
        active = config.getInitParameter("demoActive");
    }
    // </editor-fold>
    
    /** Initializes the servlet.
     *
     * @param config ServletConfig configuration file in which is described how to configure the servlet.
     */
    // <editor-fold defaultstate="" desc="init(ServletConfig config) method.">
    public String isActive() throws ServletException {
        log.debug("Demo has been actived is: " + active);
        return active;
    }
    // </editor-fold>
}