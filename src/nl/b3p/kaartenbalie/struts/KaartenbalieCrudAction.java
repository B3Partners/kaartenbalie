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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.struts.CrudAction;
import nl.b3p.kaartenbalie.core.server.datawarehousing.DataWarehousing;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.wms.capabilities.Layer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;

public class KaartenbalieCrudAction extends CrudAction{
    
    private static final Log log = LogFactory.getLog(KaartenbalieCrudAction.class);
    protected static final String UNKNOWN_SES_USER_ERROR_KEY = "error.sesuser";
    
    public KaartenbalieCrudAction() {
        super();
        
        
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
        MyEMFDatabase.initEntityManager();
        EntityManager em = getEntityManager();
        //EntityManager crudEM = ManagedPersistence.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        ActionForward forward = null;
        String msg = null;
        getDataWarehousing().begin();
        try {
            forward = super.execute(mapping, form, request, response);
            tx.commit();
            MyEMFDatabase.closeEntityManager();
            getDataWarehousing().end();
            return forward;
        } catch(Exception e) {
            e.printStackTrace();
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
        MyEMFDatabase.closeEntityManager();
        return getAlternateForward(mapping, request);
    }
    
    public static EntityManager getEntityManager() {
        return MyEMFDatabase.getEntityManager();
    }
    
    public Layer getLayerByUniqueName(String uniqueName) throws Exception {
        EntityManager em = getEntityManager();
        
        // Check of selectedLayers[i] juiste format heeft
        int pos = uniqueName.indexOf("_");
        if (pos==-1 || uniqueName.length()<=pos+1) {
            log.error("layer not valid: " + uniqueName);
            throw new Exception("Unieke kaartnaam niet geldig: " + uniqueName);
        }
        String spAbbr = uniqueName.substring(0, pos);
        String layerName = uniqueName.substring(pos + 1);
        if (spAbbr.length()==0 || layerName.length()==0) {
            log.error("layer name or code not valid: " + spAbbr + ", " + layerName);
            throw new Exception("Unieke kaartnaam niet geldig: " + spAbbr + ", " + layerName);
        }
        
        String query = "from Layer where name = :layerName and serviceProvider.abbr = :spAbbr";
        List ll = em.createQuery(query)
        .setParameter("layerName", layerName)
        .setParameter("spAbbr", spAbbr)
        .getResultList();
        
        if (ll==null || ll.isEmpty())
            return null;
        return (Layer)ll.get(0);
    }
    
    public String findLayer(String layerToBeFound, Set layers) {
        if (layers==null || layers.isEmpty())
            return null;
        
        Iterator it = layers.iterator();
        while (it.hasNext()) {
            Layer layer = (Layer) it.next();
            String identity = layer.getUniqueName();
            if(identity.equalsIgnoreCase(layerToBeFound))
                return layer.getName();
            
            String foundLayer = findLayer(layerToBeFound, layer.getLayers());
            if (foundLayer != null)
                return foundLayer;
        }
        return null;
    }
    
    
    public static DataWarehousing getDataWarehousing() {
        return MyEMFDatabase.getDataWarehouse();
    }
}
