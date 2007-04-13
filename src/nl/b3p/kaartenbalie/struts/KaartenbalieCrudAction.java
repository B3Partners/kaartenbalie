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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.struts.CrudAction;
import nl.b3p.kaartenbalie.service.MyDatabase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class KaartenbalieCrudAction extends CrudAction{
    
    private static final Log log = LogFactory.getLog(KaartenbalieCrudAction.class);
    protected String msg;
    
    /** Protected method which returns the current Hibernate session.
     *
     * @return the current Hibernate Session
     */
    // <editor-fold defaultstate="" desc="getHibernateSession() method.">
    protected Session getHibernateSession() {
        return MyDatabase.currentSession();
    }
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
        
        Session sess = getHibernateSession();
        Transaction tx = sess.beginTransaction();
        
        ActionForward forward = null;
        String message = null;
        
        try {
            forward = super.execute(mapping, form, request, response);
            tx.commit();
            return forward;
        } catch(Exception e) {
            request.setAttribute("message", msg);
            tx.rollback();
            log.error("Exception occured, rollback", e);
            /*melding etc. naar formulier/site*/
            MessageResources messages = getResources(request);
            message = e.toString();
            /*
            if (e instanceof org.hibernate.JDBCException) {
                msg = e.toString();
                SQLException sqle = ((org.hibernate.JDBCException)e).getSQLException();
                msg = msg + ": " + sqle;
                SQLException nextSqlE = sqle.getNextException();
                if(nextSqlE != null) {
                    msg = msg + ": " + nextSqlE;
                }
            }
            */
            
            
            
            
        }
        
        // Start tweede sessie om tenminste nog de lijsten op te halen
        sess = getHibernateSession();
        tx = sess.beginTransaction();
        
        try {
            prepareMethod((DynaValidatorForm)form, request, LIST, EDIT);
            tx.commit();
        } catch(Exception e) {
            //request.setAttribute("message", e);
            tx.rollback();
            log.error("Exception occured in second session, rollback", e);
        }
        //addAlternateMessage(mapping, request, null, message);
        return getAlternateForward(mapping, request);
    }
    // </editor-fold>
}
