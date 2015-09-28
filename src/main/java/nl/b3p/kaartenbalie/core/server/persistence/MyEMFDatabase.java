/*
 * B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
 * for authentication/authorization, pricing and usage reporting.
 *
 * Copyright 2006, 2007, 2008 B3Partners BV
 *
 * This file is part of B3P Kaartenbalie.
 *
 * B3P Kaartenbalie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * B3P Kaartenbalie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.b3p.kaartenbalie.core.server.persistence;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import nl.b3p.kaartenbalie.core.server.accounting.AccountManager;
import nl.b3p.kaartenbalie.core.server.monitoring.DataMonitoring;
import nl.b3p.ogc.utils.KBConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MyEMFDatabase extends HttpServlet {

    //context params
    public static final String MAPFILE_DIR = "global.mapfile.dir";
    public static final String UPLOAD_DIR = "global.upload.dir";
    public static final String DEFAULT_ORGANIZATION = "default.organization";
    public static final String LDAP_USELDAP = "ldap.useldap";
    public static final String LDAP_DEFAULTGROUP = "ldap.default.group";
    public static final String LDAP_HOST = "ldap.host";
    public static final String LDAP_PORT = "ldap.port";
    public static final String LDAP_USERSUFFIX = "ldap.usersuffix";
    public static final String USER_UPDATELASTLOGINSTATUS = "user.updateLastLoginStatus";

    private static String mapfiles = null;
    private static String upload = null;
    private static String defaultOrganization = null;
    private static Boolean ldapUseLdap = false;
    private static String ldapDefaultGroup = null;
    private static String ldapHost = null;
    private static Integer ldapPort = null;
    private static String ldapUserSuffix = null;

    private static final Log log = LogFactory.getLog(MyEMFDatabase.class);
    public static final String MAIN_EM = "mainEM";
    public static final String INIT_EM = "initEM";
    public static final String REALM_EM = "realmEM";
    public static final String DWR_EM = "dwrEM";
    public static final String SLD_EM = "sldEM";
    private static EntityManagerFactory emf = null;
    private static ThreadLocal tlMap = new ThreadLocal();
    private static String defaultKaartenbaliePU = "defaultKaartenbaliePU";
    private static String cachePath = null;
    private static Random rg = null;
    private static String[] allowedUploadFiles = null;

    public static void openEntityManagerFactory(String persistenceUnit) throws Exception {
        log.debug("ManagedPersistence.openEntityManagerFactory(" + persistenceUnit + ")");

        if (emf != null) {
            log.warn("EntityManagerFactory already initialized: " + emf.toString());
            return;
        }
        if (persistenceUnit == null || persistenceUnit.trim().length() == 0) {
            throw new Exception("PersistenceUnit cannot be left empty.");
        }
        try {
            emf = Persistence.createEntityManagerFactory(persistenceUnit);
        } catch (Throwable t) {
            log.fatal("Error initializing EntityManagerFactory: ", t);
        }
        if (emf == null) {
            log.fatal("Cannot initialize EntityManagerFactory");
            throw new Exception("Cannot initialize EntityManagerFactory");
        }
        log.debug("EntityManagerFactory initialized: " + emf.toString());
    }

    public static EntityManagerFactory getEntityManagerFactory() throws Exception {
        if (emf == null) {
            openEntityManagerFactory(defaultKaartenbaliePU);
        }
        return emf;
    }

    /*
     * This will initialize the EntityManagerFactory when in a servlet context. There is no need
     * to call the method openEntityManagerFactory from anywhere else unless you're out of the
     * servlet context (testing, etc..).
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        DataMonitoring.setEnableMonitoring(getConfigValue(config, "reporting", "disabled").equalsIgnoreCase("enabled"));
        AccountManager.setEnableAccounting(getConfigValue(config, "accounting", "disabled").equalsIgnoreCase("enabled"));

        Object identity = null;
        try {
            openEntityManagerFactory(defaultKaartenbaliePU);
            identity = createEntityManager(MyEMFDatabase.INIT_EM);
            log.debug("Getting entity manager ......");
            EntityManager em = getEntityManager(MyEMFDatabase.INIT_EM);
            EntityTransaction tx = em.getTransaction();
            tx.begin();
//            ReportGenerator.startupClear(em);
            tx.commit();
        } catch (Throwable e) {
            log.warn("Error creating EntityManager: ", e);
            throw new ServletException(e);
        } finally {
            log.debug("Closing entity manager .....");
            closeEntityManager(identity, MyEMFDatabase.INIT_EM);
        }

        cachePath = getConfigValue(config, "cache", "/");
        if (cachePath != null) {
            cachePath = getServletContext().getRealPath(cachePath);
            log.debug("cache pad: " + cachePath);
        }

        rg = new Random();

        String allowedUpload = config.getInitParameter("allowed_upload_files");
        if(allowedUpload != null && allowedUpload.length() > 0){
            allowedUploadFiles = allowedUpload.split(",");
        }

        // load global context params
        initGlobalContextParams(config);

        // configure kb via properties
        KBConfiguration.configure();
     }

    public void initGlobalContextParams(ServletConfig config) {
       ServletContext context = config.getServletContext();

        String value = context.getInitParameter(MyEMFDatabase.MAPFILE_DIR);
        if (value != null && value.length() > 0) {
            mapfiles = value;
        }
        value = context.getInitParameter(MyEMFDatabase.UPLOAD_DIR);
        if (value != null && value.length() > 0) {
            upload = value;
        }
        value = context.getInitParameter(MyEMFDatabase.DEFAULT_ORGANIZATION);
        if (value != null && value.length() > 0) {
            defaultOrganization = value;
        }

        value = context.getInitParameter(MyEMFDatabase.LDAP_USELDAP);
        if (value != null && value.length() > 0) {
            ldapUseLdap = new Boolean(value);
        }
        value = context.getInitParameter(MyEMFDatabase.LDAP_DEFAULTGROUP);
        if (value != null && value.length() > 0) {
            ldapDefaultGroup = value;
        }
        value = context.getInitParameter(MyEMFDatabase.LDAP_HOST);
        if (value != null && value.length() > 0) {
            ldapHost = value;
        }
        value = context.getInitParameter(MyEMFDatabase.LDAP_PORT);
        if (value != null && value.length() > 0) {
            ldapPort = new Integer(value);
        }
        value = context.getInitParameter(MyEMFDatabase.LDAP_USERSUFFIX);
        if (value != null && value.length() > 0) {
            ldapUserSuffix = value;
        }
    }

    private static String getConfigValue(ServletConfig config, String parameter, String defaultValue) {
        String tmpval = config.getInitParameter(parameter);
        if (tmpval == null || tmpval.trim().length() == 0) {
            tmpval = defaultValue;
        }
        log.debug("ManagedPersistence.getConfigValue(config, " + parameter + ", " + tmpval + ")");
        return tmpval.trim();
    }
    /** The constants for describing the ownerships **/
    private static final Owner trueOwner = new Owner(true);
    private static final Owner fakeOwner = new Owner(false);

    /**
     * Internal class , for handling the identity. Hidden for the
     * developers
     */
    private static class Owner {

        public Owner(boolean identity) {
            this.identity = identity;
        }
        boolean identity = false;
    }

    /**
     * get the hibernate session and set it on the thread local. Returns trueOwner if
     * it actually opens a session
     */
    public static Object createEntityManager(String emKey) throws Exception {
        EntityManager localEm = (EntityManager) getThreadLocal(emKey);
        if (localEm == null) {
            log.debug("No EntityManager Found - Create and give the identity for key: " + emKey);
            localEm = getEntityManagerFactory().createEntityManager();
            if (localEm == null) {
                throw new Exception("EntityManager could not be initialized for key: " + emKey);
            }
            setThreadLocal(emKey, localEm);
            return trueOwner;
        }
        log.debug("EntityManager Found - Give a Fake identity for key: " + emKey);
        return fakeOwner;
    }

    public static EntityManager getEntityManager(String emKey) throws Exception {
        EntityManager localEm = (EntityManager) getThreadLocal(emKey);
        if (localEm == null) {
            throw new Exception("EntityManager could not be initialized for key: " + emKey);
        }
        return localEm;
    }

    /*
     * The method for closing a session. The close
     * will be executed only if the session is actually created
     * by this owner.
     */
    public static void closeEntityManager(Object ownership, String emKey) {
        if (ownership != null && ((Owner) ownership).identity) {
            log.debug("Identity is accepted. Now closing the session for key: " + emKey);
            EntityManager localEm = (EntityManager) getThreadLocal(emKey);
            if (localEm == null) {
                log.warn("EntityManager is missing. Either it's already closed or never initialized for key: " + emKey);
                return;
            }
            clearThreadLocal(emKey);
            localEm.close();
        } else {
            log.debug("Identity is rejected. Ignoring the request for key: " + emKey);
        }
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
        threadLocalMap.put(key, object);
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
    public static String localPath() {
        return localPath(null);
    }

    public static String localPath(String fileName) {
        if (fileName == null) {
            fileName = "";
        }
        if (!fileName.startsWith(File.separator)) {
            fileName = File.separator + fileName;
        }
        return cachePath + fileName;
    }

    /** Returns a unique name created with given parameters without taking the path into account.
     *
     * @param prefix String containing the prefix.
     * @param extension String containing the extension.
     *
     * @return a String representing a unique name for these parameters.
     */
    public static String uniqueName(String prefix, String extension) {
        return uniqueName(prefix, extension, false);
    }

    /** Returns a unique name created with given parameters.
     *
     * @param prefix String containing the prefix.
     * @param extension String containing the extension.
     * @param includePath boolean setting the including of the path to true or false.
     *
     * @return a String representing a unique name for these parameters.
     */
    public static String uniqueName(String prefix, String extension, boolean includePath) {
        // Gebruik tijd in milliseconden om gerekend naar een radix van 36.
        // Hierdoor ontstaat een lekker korte code.
        long now = (new Date()).getTime();
        String val1 = Long.toString(now, Character.MAX_RADIX).toUpperCase();
        // random nummer er aanplakken om zeker te zijn van unieke code
        if (rg == null) {
            rg = new Random();
        }
        long rnum = (long) rg.nextInt(1000);
        String val2 = Long.toString(rnum, Character.MAX_RADIX).toUpperCase();
        String thePath = "";
        if (includePath) {
            thePath = cachePath;
        }
        if (prefix == null) {
            prefix = "";
        }
        if (extension == null) {
            extension = "";
        }
        return thePath + prefix + val1 + val2 + extension;
    }

    public static String[] getAllowedUploadFiles() {
        return allowedUploadFiles;
    }

    public static String getMapfiles() {
        return mapfiles;
    }

    public static String getUpload() {
        return upload;
    }

    /**
     * @return the defaultOrganization
     */
    public static String getDefaultOrganization() {
        return defaultOrganization;
    }

    /**
     * @return the ldapUseLdap
     */
    public static Boolean getLdapUseLdap() {
        return ldapUseLdap;
    }

    /**
     * @return the ldapDefaultGroup
     */
    public static String getLdapDefaultGroup() {
        return ldapDefaultGroup;
    }

    /**
     * @return the ldapHost
     */
    public static String getLdapHost() {
        return ldapHost;
    }

    /**
     * @return the ldapPort
     */
    public static Integer getLdapPort() {
        return ldapPort;
    }

    /**
     * @return the ldapUserSuffix
     */
    public static String getLdapUserSuffix() {
        return ldapUserSuffix;
    }
}
