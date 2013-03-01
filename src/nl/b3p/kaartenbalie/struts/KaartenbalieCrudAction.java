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
package nl.b3p.kaartenbalie.struts;

import java.sql.SQLException;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.struts.CrudAction;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.service.LayerTreeSupport;
import nl.b3p.ogc.wfs.v110.WfsLayer;
import nl.b3p.wms.capabilities.Layer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import org.json.JSONObject;

public class KaartenbalieCrudAction extends CrudAction {

    private static final Log log = LogFactory.getLog(KaartenbalieCrudAction.class);
    protected static final String UNKNOWN_SES_USER_ERROR_KEY = "error.sesuser";
    protected static final String ACKNOWLEDGE_MESSAGES = "acknowledgeMessages";

    protected ActionForward getUnspecifiedAlternateForward(ActionMapping mapping, HttpServletRequest request) {
        return mapping.findForward(FAILURE);
    }

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
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object identity = null;
        
        try {
            identity = MyEMFDatabase.createEntityManager(MyEMFDatabase.MAIN_EM);
            
            ActionForward forward = null;
            String msg = null;
            
            EntityManager em = getEntityManager();
            EntityTransaction tx = em.getTransaction();
            
            try {
                tx.begin();
                
                forward = super.execute(mapping, form, request, response);
                
                tx.commit();
                
                return forward;
            } catch (Exception e) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                    
                log.error("Exception occured, rollback", e);

                if (e instanceof org.hibernate.JDBCException) {
                    msg = e.toString();
                    SQLException sqle = ((org.hibernate.JDBCException) e).getSQLException();
                    msg = msg + ": " + sqle;
                    SQLException nextSqlE = sqle.getNextException();
                    if (nextSqlE != null) {
                        msg = msg + ": " + nextSqlE;
                    }
                } else if (e instanceof java.sql.SQLException) {
                    msg = e.toString();
                    SQLException nextSqlE = ((java.sql.SQLException) e).getNextException();
                    if (nextSqlE != null) {
                        msg = msg + ": " + nextSqlE;
                    }
                } else {
                    msg = e.toString();
                }
                
                addAlternateMessage(mapping, request, null, msg);
            }

            try {
                tx.begin();
                
                prepareMethod((DynaValidatorForm) form, request, LIST, EDIT);
                
                tx.commit();
            } catch (Exception e) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                
                log.error("Exception occured in second session, rollback", e);
                
                addAlternateMessage(mapping, request, null, e.toString());
            }
        } catch (Throwable e) {
            log.error("Exception occured while getting EntityManager: ", e);
            addAlternateMessage(mapping, request, null, e.toString());
            
        } finally {
            log.debug("Closing entity manager .....");
            MyEMFDatabase.closeEntityManager(identity, MyEMFDatabase.MAIN_EM);
        }
        
        return getAlternateForward(mapping, request);
    }

    protected static EntityManager getEntityManager() throws Exception {
        log.debug("Getting entity manager ......");
        return MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);
    }

    public Layer getLayerByUniqueName(String uniqueName) throws Exception {
        EntityManager em = getEntityManager();
        return LayerTreeSupport.getLayerByUniqueName(em, uniqueName);
    }

    public WfsLayer getWfsLayerByUniqueName(String uniqueName) throws Exception {
        EntityManager em = getEntityManager();
        return LayerTreeSupport.getWfsLayerByUniqueName(em, uniqueName);
    }

    protected String getLayerID(DynaValidatorForm dynaForm) {
        return dynaForm.getString("id");
    }

    public static JSONObject createTree(String rootName) throws Exception {
        return createTree(rootName, null, false);
    }

    public static JSONObject createTree(String rootName, Set organizationLayers) throws Exception {
        return createTree(rootName, organizationLayers, true);
    }

    public static JSONObject createTree(String rootName, Set organizationLayers, boolean checkLayers) throws Exception {
        EntityManager em = getEntityManager();
        return LayerTreeSupport.createTree(em, rootName, organizationLayers, checkLayers);
    }

    public static JSONObject createWfsTree(String rootName) throws Exception {
        return createWfsTree(rootName, null, false);
    }

    public static JSONObject createWfsTree(String rootName, Set organizationLayers, boolean checkLayers) throws Exception {
        EntityManager em = getEntityManager();
        return LayerTreeSupport.createWfsTree(em, rootName, organizationLayers, checkLayers);
    }

}
