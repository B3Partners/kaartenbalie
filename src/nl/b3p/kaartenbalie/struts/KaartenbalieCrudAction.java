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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.struts.CrudAction;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.ogc.wfs.v110.WfsLayer;
import nl.b3p.ogc.wfs.v110.WfsServiceProvider;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.ServiceProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object identity = null;
        try {
            identity = MyEMFDatabase.createEntityManager(MyEMFDatabase.MAIN_EM);
            EntityManager em = getEntityManager();

            EntityTransaction tx = em.getTransaction();
            tx.begin();
            ActionForward forward = null;
            String msg = null;
            try {
                forward = super.execute(mapping, form, request, response);
                tx.commit();
                return forward;
            } catch (Exception e) {
                tx.rollback();
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


            tx.begin();

            try {
                prepareMethod((DynaValidatorForm) form, request, LIST, EDIT);
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
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
        // Dit is nodig omdat mysql case insensitive selecteert
        Iterator it = ll.iterator();
        while (it.hasNext()) {
            Layer l = (Layer) it.next();
            String dbLayerName = l.getName();
            String dbSpAbbr = l.getSpAbbr();
            if (dbLayerName != null && dbSpAbbr != null) {
                if (dbLayerName.equals(layerName) && dbSpAbbr.equals(spAbbr)) {
                    return l;
                }
            }
        }
        return null;
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

    protected String getLayerID(DynaValidatorForm dynaForm) {
        return dynaForm.getString("id");
    }

    /* Method which checks if a certain layer is allowed to be shown on the screen.
     *
     * @param layer Layer object that has to be checked
     * @param organizationLayers Set of restrictions which define the visible and non visible layers
     *
     * @return boolean
     */
    protected boolean hasVisibility(Layer layer, Set organizationLayers) {
        if (layer == null || organizationLayers == null) {
            return false;
        }
        Iterator it = organizationLayers.iterator();
        while (it.hasNext()) {
            Layer organizationLayer = (Layer) it.next();
            if (layer.getId().equals(organizationLayer.getId())) {
                return true;
            }
        }
        return false;
    }

    protected JSONObject createTree() throws JSONException {
        return createTree(null, false);
    }

    protected JSONObject createTree(Set organizationLayers) throws JSONException {
        return createTree(organizationLayers, true);
    }

    private JSONObject createTree(Set organizationLayers, boolean checkLayers) throws JSONException {
        JSONObject root = new JSONObject();
        root.put("name", "root");
        try {
            log.debug("Getting entity manager ......");
            EntityManager em = getEntityManager();
            List serviceProviders = em.createQuery("from ServiceProvider sp order by sp.abbr").getResultList();
            JSONArray rootArray = new JSONArray();
            Iterator it = serviceProviders.iterator();
            while (it.hasNext()) {
                ServiceProvider sp = (ServiceProvider) it.next();
                JSONObject parentObj = this.serviceProviderToJSON(sp);

                HashSet set = new HashSet();
                Layer topLayer = sp.getTopLayer();
                if (topLayer != null) {
                    set.add(topLayer);
                    parentObj = createTreeList(set, organizationLayers, parentObj, checkLayers);
                    if (parentObj.has("children")) {
                        rootArray.put(parentObj);
                    }
                } else {
                    String name = sp.getGivenName();
                    if (name == null) {
                        name = "onbekend";
                    }
                    log.debug("Toplayer is null voor serviceprovider: " + name);
                }
            }
            root.put("children", rootArray);
        } catch (Throwable e) {
            log.warn("Error creating EntityManager: ", e);
        }

        return root;
    }

    protected JSONObject createWfsTree() throws JSONException {
        JSONObject root = new JSONObject();
        root.put("name", "root");
        try {
            log.debug("Getting entity manager ......");
            EntityManager em = getEntityManager();
            List serviceProviders = em.createQuery("from WfsServiceProvider sp order by sp.abbr").getResultList();

            JSONArray rootArray = new JSONArray();
            Iterator it = serviceProviders.iterator();
            while (it.hasNext()) {
                WfsServiceProvider sp = (WfsServiceProvider) it.next();
                JSONObject parentObj = this.serviceProviderToJSON(sp);
                HashSet set = new HashSet();
                Set layers = sp.getWfsLayers();
                set.addAll(layers);
                parentObj = createWfsTreeList(set, parentObj);
                if (parentObj.has("children")) {
                    rootArray.put(parentObj);
                }
            }
            root.put("children", rootArray);
        } catch (Throwable e) {
            log.warn("Error creating EntityManager: ", e);
        }
        return root;
    }

    /* Creates a JSON tree list of a given set of Layers and a set of restrictions
     * of which layer is visible and which isn't.
     *
     * @param layers Set of layers from which the part of the tree ahs to be build
     * @param organizationLayers Set of restrictions which define the visible and non visible layers
     * @param parent JSONObject which represents the parent object to which this set of layers should be added
     *
     * @throws JSONException
     */
    private JSONObject createTreeList(Set layers, Set organizationLayers, JSONObject parent, boolean checkLayers) throws JSONException {
        Iterator layerIterator = layers.iterator();
        JSONArray parentArray = new JSONArray();
        while (layerIterator.hasNext()) {
            Layer layer = (Layer) layerIterator.next();
            JSONObject layerObj = this.layerToJSON(layer);

            Set childLayers = layer.getLayers();
            if (childLayers != null && !childLayers.isEmpty()) {
                layerObj = createTreeList(childLayers, organizationLayers, layerObj, checkLayers);
            }

            if (!checkLayers || hasVisibility(layer, organizationLayers) || layerObj.has("children")) {
                parentArray.put(layerObj);
            }

        }
        if (parentArray.length() > 0) {
            parent.put("children", parentArray);
        }
        return parent;
    }

    private JSONObject createWfsTreeList(Set layers, JSONObject parent) throws JSONException {
        Iterator layerIterator = layers.iterator();
        JSONArray parentArray = new JSONArray();
        while (layerIterator.hasNext()) {
            WfsLayer layer = (WfsLayer) layerIterator.next();
            JSONObject layerObj = this.layerToJSON(layer);
            parentArray.put(layerObj);
        }
        if (parentArray.length() > 0) {
            parent.put("children", parentArray);
        }
        return parent;
    }

    /* Creates a JSON object from the ServiceProvider with its given name and id.
     *
     * @param serviceProvider The ServiceProvider object which has to be converted
     *
     * @return JSONObject
     *
     * @throws JSONException
     */
    private JSONObject serviceProviderToJSON(WfsServiceProvider serviceProvider) throws JSONException {
        JSONObject root = new JSONObject();
        root.put("id", serviceProvider.getId());
        root.put("name", serviceProvider.getGivenName());
        root.put("type", "serviceprovider");
        return root;
    }

    private JSONObject serviceProviderToJSON(ServiceProvider serviceProvider) throws JSONException {
        JSONObject root = new JSONObject();
        root.put("id", serviceProvider.getId());
        root.put("name", serviceProvider.getGivenName());
        root.put("type", "serviceprovider");
        return root;
    }

    private JSONObject layerToJSON(WfsLayer layer) throws JSONException {
        JSONObject jsonLayer = new JSONObject();
        jsonLayer.put("name", layer.getTitle());
        String name = layer.getUniqueName();
        if (name == null) {
            jsonLayer.put("id", layer.getTitle().replace(" ", ""));
            jsonLayer.put("type", "placeholder");
        } else {
            jsonLayer.put("id", name);
            jsonLayer.put("type", "layer");
        }
        return jsonLayer;
    }

    private JSONObject layerToJSON(Layer layer) throws JSONException {
        JSONObject jsonLayer = new JSONObject();
        jsonLayer.put("name", layer.getTitle());
        String name = layer.getUniqueName();
        if (name == null) {
            String title="";
            if (layer.getSpAbbr()!=null){
                title+=layer.getSpAbbr();
            }
            title+="_"+layer.getTitle().replace(" ", "");
            jsonLayer.put("id", title);
            jsonLayer.put("type", "placeholder");
        } else {
            jsonLayer.put("id", name);
            jsonLayer.put("type", "layer");
        }
        return jsonLayer;
    }
}
