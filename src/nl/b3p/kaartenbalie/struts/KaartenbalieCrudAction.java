/*
 * KaartenbalieCrudAction.java
 *
 * Created on 8 september 2006, 10:28
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
import org.apache.struts.validator.DynaValidatorForm;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Roy
 */
public class KaartenbalieCrudAction extends CrudAction{
    
    private static final Log log = LogFactory.getLog(KaartenbalieCrudAction.class);
    
    protected Session getHibernateSession() {
        return MyDatabase.currentSession();
    }
    public ActionForward execute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        
        Session sess = getHibernateSession();
        Transaction tx = sess.beginTransaction();

        ActionForward forward = null;
        String msg = null;
        try {
            forward = super.execute(mapping, form, request, response);
            tx.commit();
            return forward;
        } catch(Exception e) {
            tx.rollback();
            log.error("Exception occured, rollback", e);
            /*melding etc. naar formulier/site*/
        }
        // Start tweede sessie om tenminste nog de lijsten op te halen
        sess = getHibernateSession();
        tx = sess.beginTransaction();
        try {
            prepareMethod((DynaValidatorForm)form, request, LIST, EDIT);
            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            log.error("Exception occured in second session, rollback", e);
        }
        return getAlternateForward(mapping, request);
    }
}
