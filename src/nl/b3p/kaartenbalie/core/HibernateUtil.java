/**
 * @(#)HibernateUtil.java
 * @author R. Braam
 * @version 1.00 2006/10/02
 *
 * Purpose: Hibernate utility class for getting the current session and closing the current session.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.*;
import org.hibernate.cfg.*;

public class HibernateUtil {
    
    private static Log log = LogFactory.getLog(HibernateUtil.class);
    private static final SessionFactory sessionFactory;
    
    public static final ThreadLocal threadLocal = new ThreadLocal();
    
    /** Static declaration for creating a new SessionFactory.
     *
     * @throws ExceptionInInitializerError
     */
    // <editor-fold defaultstate="" desc="static declaration for creating a new SessionFactory">
    static {
        try {
            // Create the SessionFactory
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            log.error("Initial SessionFactory creation failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    // </editor-fold>
    
    /** Method which returns the current Hibernate session.
     *
     * @return a Session object defining the current session.
     *
     * @throws HibernateException
     */
    // <editor-fold defaultstate="" desc="currentSession() method.">
    public static Session currentSession() throws HibernateException {
        Session session = (Session) threadLocal.get();
        // Open a new Session, if this Thread has none yet
        if (session == null) {
            session = sessionFactory.openSession();
            threadLocal.set(session);
        }
        return session;
    }
    // </editor-fold>

    /** Method which closes the current Hibernate session.
     *
     * @throws HibernateException
     */
    // <editor-fold defaultstate="" desc="closeSession() method.">
    public static void closeSession() throws HibernateException {
        Session session = (Session) threadLocal.get();
        threadLocal.set(null);
        if (session != null)
            session.close();
    }
    // </editor-fold>
}