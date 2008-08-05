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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.struts.CrudAction;
import nl.b3p.kaartenbalie.core.server.accounting.AccountManager;
import nl.b3p.kaartenbalie.core.server.datawarehousing.DataWarehousing;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.core.server.reporting.control.DataMonitoring;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.wfs.v110.WfsLayer;
import nl.b3p.wms.capabilities.Layer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;

public class KaartenbalieCrudAction extends CrudAction {

    private static final Log log = LogFactory.getLog(KaartenbalieCrudAction.class);
    protected static final String UNKNOWN_SES_USER_ERROR_KEY = "error.sesuser";

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
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        MyEMFDatabase.initEntityManager();
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        ActionForward forward = null;
        String msg = null;
        setMenuParams(request);
        getDataWarehousing().begin();
        try {
            forward = super.execute(mapping, form, request, response);
            tx.commit();
            MyEMFDatabase.closeEntityManager();
            getDataWarehousing().end();
            return forward;
        } catch (Exception e) {
            tx.rollback();
            log.error("Exception occured, rollback", e);
            MessageResources messages = getResources(request);

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


        tx.begin();

        try {
            prepareMethod((DynaValidatorForm) form, request, LIST, EDIT);
            tx.commit();
        } catch (Exception e) {
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

    private static void setMenuParams(HttpServletRequest request) {
        Map menuParamMap = new HashMap();
        menuParamMap.put("pricing", new Boolean(AccountManager.isEnableAccounting()));
        menuParamMap.put("reporting", new Boolean(DataMonitoring.isEnableMonitoring()));
        menuParamMap.put("accounting", new Boolean(AccountManager.isEnableAccounting()));
        menuParamMap.put("warehousing", new Boolean(DataWarehousing.isEnableWarehousing()));
        menuParamMap.put("metadata", new Boolean(KBConfiguration.METADATA_ENABLED));
        request.setAttribute("menuParameters", menuParamMap);
    }

    public Layer getLayerByUniqueName(String uniqueName) throws Exception {
        EntityManager em = getEntityManager();

        // Check of selectedLayers[i] juiste format heeft
        int pos = uniqueName.indexOf("_");
        if (pos == -1 || uniqueName.length() <= pos + 1) {
            log.error("layer not valid: " + uniqueName);
            throw new Exception("Unieke kaartnaam niet geldig: " + uniqueName);
        }
        String spAbbr = uniqueName.substring(0, pos);
        String layerName = uniqueName.substring(pos + 1);
        if (spAbbr.length() == 0 || layerName.length() == 0) {
            log.error("layer name or code not valid: " + spAbbr + ", " + layerName);
            throw new Exception("Unieke kaartnaam niet geldig: " + spAbbr + ", " + layerName);
        }

        String query = "from Layer where name = :layerName and serviceProvider.abbr = :spAbbr";
        List ll = em.createQuery(query).setParameter("layerName", layerName).setParameter("spAbbr", spAbbr).getResultList();

        if (ll == null || ll.isEmpty()) {
            return null;
        }
        return (Layer) ll.get(0);
    }

    public WfsLayer getWfsLayerByUniqueName(String uniqueName) throws Exception {
        EntityManager em = getEntityManager();

        // Check of selectedLayers[i] juiste format heeft
        int pos = uniqueName.indexOf("_");
        if (pos == -1 || uniqueName.length() <= pos + 1) {
            log.error("layer not valid: " + uniqueName);
            throw new Exception("Unieke kaartnaam niet geldig: " + uniqueName);
        }
        String spAbbr = uniqueName.substring(0, pos);
        String layerName = uniqueName.substring(pos + 1);
        if (spAbbr.length() == 0 || layerName.length() == 0) {
            log.error("layer name or code not valid: " + spAbbr + ", " + layerName);
            throw new Exception("Unieke kaartnaam niet geldig: " + spAbbr + ", " + layerName);
        }

        String query = "from WfsLayer where name = :layerName and wfsServiceProvider.abbr = :spAbbr";
        List ll = em.createQuery(query).setParameter("layerName", layerName).setParameter("spAbbr", spAbbr).getResultList();

        if (ll == null || ll.isEmpty()) {
            return null;
        }
        return (WfsLayer) ll.get(0);
    }

    public static DataWarehousing getDataWarehousing() {
        return MyEMFDatabase.getDataWarehouse();
    }
}
