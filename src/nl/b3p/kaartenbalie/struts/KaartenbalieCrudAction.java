/**
 * @(#)KaartenbalieCrudAction.java
 * @author R. Braam
 * @version 1.00 2006/09/08
 *
 * Purpose: a class handling the different actions which come from classes extending this class.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.struts;

import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.struts.CrudAction;
import nl.b3p.kaartenbalie.core.server.persistence.ManagedPersistence;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;
import org.hibernate.Session;

public class KaartenbalieCrudAction extends CrudAction{
    
    private static final Log log = LogFactory.getLog(KaartenbalieCrudAction.class);
    protected static final String UNKNOWN_SES_USER_ERROR_KEY = "error.sesuser";
    protected EntityManager em;
    
    public KaartenbalieCrudAction()
    {
        super();
        em = ManagedPersistence.getEntityManager();
        
    }
    protected ActionForward getUnspecifiedAlternateForward(ActionMapping mapping, HttpServletRequest request) {
        return mapping.findForward(FAILURE);
    }
    
    /** Protected method which returns the current Hibernate session.
     *
     * @return the current Hibernate Session
     */
    // <editor-fold defaultstate="" desc="getHibernateSession() method.">
     // </editor-fold>
    
    /** Execute method which handles all incoming request.
     *
     * @param mapping action mapping
     * @param dynaForm dyna validator form
     * @param request servlet request
     * @param response servlet response
     *
     * @return ActionForward defined by Apache foundation
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="" desc="execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) method.">
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception {
        
        //EntityManager crudEM = ManagedPersistence.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();        
        ActionForward forward = null;
        String msg = null;
        
        try {
           forward = super.execute(mapping, form, request, response);
            //throw new Exception("Lorem Ipsum");
           
            
            tx.commit();
            
            return forward;
        } catch(Exception e) {
            tx.rollback();
            log.error("Exception occured, rollback", e);
            MessageResources messages = getResources(request);
            
            if (e instanceof org.hibernate.JDBCException) {
                msg = e.toString();
                SQLException sqle = ((org.hibernate.JDBCException)e).getSQLException();
                msg = msg + ": " + sqle;
                SQLException nextSqlE = sqle.getNextException();
                if(nextSqlE != null) {
                    msg = msg + ": " + nextSqlE;
                }
            } else if (e instanceof java.sql.SQLException) {
                msg = e.toString();
                SQLException nextSqlE = ((java.sql.SQLException)e).getNextException();
                if(nextSqlE != null) {
                    msg = msg + ": " + nextSqlE;
                }
            } else {
                msg = e.toString();
            }
            addAlternateMessage(mapping, request, null, msg);
        }
        
        // Start tweede sessie om tenminste nog de lijsten op te halen
        //sess = getHibernateSession();
        //tx = sess.beginTransaction();
        tx.begin();
        
        try {
            prepareMethod((DynaValidatorForm)form, request, LIST, EDIT);
            //throw new Exception("Lorem Ipsum 2");
            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            log.error("Exception occured in second session, rollback", e);
            addAlternateMessage(mapping, request, null, e.toString());
        }
        //addAlternateMessage(mapping, request, null, message);
        return getAlternateForward(mapping, request);
    }
    // </editor-fold>
}
