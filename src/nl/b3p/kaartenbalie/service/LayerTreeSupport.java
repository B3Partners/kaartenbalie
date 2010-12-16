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

package nl.b3p.kaartenbalie.service;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.ogc.wfs.v110.WfsLayer;
import nl.b3p.ogc.wfs.v110.WfsServiceProvider;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.ServiceProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Chris
 */
public class LayerTreeSupport {

    private static final Log log = LogFactory.getLog(LayerTreeSupport.class);
    
    /* Method which checks if a certain layer is allowed to be shown on the screen.
     *
     * @param layer Layer object that has to be checked
     * @param organizationLayers Set of restrictions which define the visible and non visible layers
     *
     * @return boolean
     */
    public static boolean hasVisibility(Layer layer, Set organizationLayers) {
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

    /* Method which checks if a certain layer is allowed to be shown on the screen.
    *
    * @param layer Layer object that has to be checked
    * @param organizationLayers Set of restrictions which define the visible and non visible layers
    *
    * @return boolean
    */
   public static boolean hasVisibility(WfsLayer layer, Set organizationLayers) {
       if (layer == null || organizationLayers == null) {
           return false;
       }
       Iterator it = organizationLayers.iterator();
       while (it.hasNext()) {
           WfsLayer organizationLayer = (WfsLayer) it.next();
           if (layer.getId() == organizationLayer.getId()) {
               return true;
           }
       }
       return false;
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
    public static JSONObject createTreeList(Set layers, Set organizationLayers, JSONObject parent, boolean checkLayers) throws JSONException {
        Iterator layerIterator = layers.iterator();
        JSONArray parentArray = new JSONArray();
        while (layerIterator.hasNext()) {
            Layer layer = (Layer) layerIterator.next();
            boolean visible = hasVisibility(layer, organizationLayers);
            JSONObject layerObj = layerToJSON(layer, visible);

            Set childLayers = layer.getLayers();
            if (childLayers != null && !childLayers.isEmpty()) {
                layerObj = createTreeList(childLayers, organizationLayers, layerObj, checkLayers);
            }

            if (!checkLayers || visible || layerObj.has("children")) {
                parentArray.put(layerObj);
            }

        }
        if (parentArray.length() > 0) {
            parent.put("children", parentArray);
        }
        return parent;
    }

    public static JSONObject createWfsTreeList(Set layers, Set organizationLayers, JSONObject parent, boolean checkLayers) throws JSONException {
        Iterator layerIterator = layers.iterator();
        JSONArray parentArray = new JSONArray();
        while (layerIterator.hasNext()) {
            WfsLayer layer = (WfsLayer) layerIterator.next();
            boolean visible = hasVisibility(layer, organizationLayers);
            JSONObject layerObj = layerToJSON(layer, visible);
            if (!checkLayers || visible) {
            	parentArray.put(layerObj);
            }
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
    public static JSONObject serviceProviderToJSON(WfsServiceProvider serviceProvider) throws JSONException {
        JSONObject root = new JSONObject();
        root.put("id", "wfs" + serviceProvider.getId());
        root.put("name", serviceProvider.getGivenName());
        root.put("type", "serviceprovider");
        return root;
    }

    public static JSONObject serviceProviderToJSON(ServiceProvider serviceProvider) throws JSONException {
        JSONObject root = new JSONObject();
        root.put("id", "wms" + serviceProvider.getId());
        root.put("name", serviceProvider.getGivenName());
        root.put("type", "serviceprovider");
        return root;
    }

    private static JSONObject layerToJSON(WfsLayer layer, boolean visible) throws JSONException {
        JSONObject jsonLayer = new JSONObject();
        jsonLayer.put("name", layer.getTitle());
        String name = layer.getUniqueName();
        if (name == null) {
            String title = layer.getTitle();
            jsonLayer.put("id", MyEMFDatabase.uniqueName(title, null));
            jsonLayer.put("title", title);
            jsonLayer.put("type", "placeholder");
        } else {
            jsonLayer.put("id", name);
            jsonLayer.put("type", "layer");
            if (visible) {
                jsonLayer.put("visible", "true");
            } else {
                jsonLayer.put("visible", "false");
            }
        }
        return jsonLayer;
    }

    private static JSONObject layerToJSON(Layer layer, boolean visible) throws JSONException {
        JSONObject jsonLayer = new JSONObject();
        jsonLayer.put("name", layer.getTitle());
        String name = layer.getUniqueName();
        if (name == null) {
            String title = layer.getCompleteTitle();
            jsonLayer.put("id", MyEMFDatabase.uniqueName(title, null));
            jsonLayer.put("title", title);
            jsonLayer.put("type", "placeholder");
        } else {
            jsonLayer.put("id", name);
            jsonLayer.put("type", "layer");
            if (visible) {
                jsonLayer.put("visible", "true");
            } else {
                jsonLayer.put("visible", "false");
            }
        }
        return jsonLayer;
    }

    public static Layer getLayerByUniqueName(EntityManager em, String uniqueName) throws Exception {

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

    public static WfsLayer getWfsLayerByUniqueName(EntityManager em, String uniqueName) throws Exception {

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

    public static JSONObject createTree(EntityManager em, String rootName, Set organizationLayers, boolean checkLayers) throws Exception {
        JSONObject root = new JSONObject();
        root.put("name", rootName);
        root.put("id", "wms" + rootName);
        List serviceProviders = em.createQuery("from ServiceProvider sp order by sp.abbr").getResultList();
        JSONArray rootArray = new JSONArray();
        Iterator it = serviceProviders.iterator();
        while (it.hasNext()) {
            ServiceProvider sp = (ServiceProvider) it.next();
            JSONObject parentObj = LayerTreeSupport.serviceProviderToJSON(sp);

            HashSet set = new HashSet();
            Layer topLayer = sp.getTopLayer();
            if (topLayer != null) {
                set.add(topLayer);
                parentObj = LayerTreeSupport.createTreeList(set, organizationLayers, parentObj, checkLayers);
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
        return root;
    }

    public static JSONObject createWfsTree(EntityManager em, String rootName, Set organizationLayers, boolean checkLayers) throws Exception {

        JSONObject root = new JSONObject();
        root.put("name", rootName);
        root.put("id", "wfs" + rootName);
        List serviceProviders = em.createQuery("from WfsServiceProvider sp order by sp.abbr").getResultList();

        JSONArray rootArray = new JSONArray();
        Iterator it = serviceProviders.iterator();
        while (it.hasNext()) {
            WfsServiceProvider sp = (WfsServiceProvider) it.next();
            JSONObject parentObj = LayerTreeSupport.serviceProviderToJSON(sp);
            HashSet set = new HashSet();
            Set layers = sp.getWfsLayers();
            set.addAll(layers);
            parentObj = LayerTreeSupport.createWfsTreeList(set, organizationLayers, parentObj, checkLayers);
            if (parentObj.has("children")) {
                rootArray.put(parentObj);
            }
        }
        root.put("children", rootArray);
        return root;
    }

}
