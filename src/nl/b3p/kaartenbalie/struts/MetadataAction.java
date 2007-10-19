/*
 * @(#)MetadataAction.java
 * @author N. de Goeij
 * @version 1.00, 11 juni 2007
 *
 * @copyright 2007 B3Partners. All rights reserved.
 * B3Partners PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package nl.b3p.kaartenbalie.struts;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.ServiceProvider;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.validator.DynaValidatorForm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MetadataAction extends KaartenbalieCrudAction {
    
    private final static Log log = LogFactory.getLog(MetadataAction.class);
    
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        showLayerTree(request);
        return mapping.findForward(SUCCESS);
    }
    
    private void showLayerTree(HttpServletRequest request) throws Exception {
        JSONObject root = this.createTree();
        request.setAttribute("layerList", root);
    }
    
    public ActionForward edit(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String layerUniqueName = (String)dynaForm.get("id");
        Layer layer = getLayerByUniqueName(layerUniqueName);
        populateMetadataEditorForm(layer, dynaForm, request);
        return mapping.findForward(SUCCESS);
    }
    
    public ActionForward save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        EntityManager em = getEntityManager();
        String layerUniqueName = (String)dynaForm.get("id");
        Layer layer = getLayerByUniqueName(layerUniqueName);
        if (layer==null) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        String metadata = StringEscapeUtils.unescapeXml((String)dynaForm.get("metadata"));
        layer.setMetaData(metadata);
        
        //Possible TODO!
        em.merge(layer);
        //getHibernateSession().update(layer);
        
        showLayerTree(request);
        
        return getDefaultForward(mapping, request);
    }
    
    private void populateMetadataEditorForm(Layer layer, DynaValidatorForm dynaForm, HttpServletRequest request) {
        if (layer==null)
            return;
        dynaForm.set("id", layer.getUniqueName());
        dynaForm.set("name", layer.getTitle());
        String metadata = layer.getMetaData();
        if (metadata != null) {
            // remove all newline and return characters using RegEx
            metadata = metadata.replaceAll("[\\n\\r]+", "");
        }
        String escapedMetadata = StringEscapeUtils.escapeXml(metadata);
        dynaForm.set("metadata", metadata);
    }
    
     
    //-------------------------------------------------------------------------------------------------------
    // PRIVATE METHODS
    //-------------------------------------------------------------------------------------------------------
    
    /* Creates a JSON tree from a list of serviceproviders from the database.
     *
     * @param layers Set of layers from which the part of the tree ahs to be build
     * @param organizationLayers Set of restrictions which define the visible and non visible layers
     * @param parent JSONObject which represents the parent object to which this set of layers should be added
     *
     * @throws JSONException
     */
    // <editor-fold defaultstate="" desc="createTree() method.">
    private JSONObject createTree() throws JSONException {
        
        EntityManager em = getEntityManager();
        List serviceProviders = em.createQuery("from ServiceProvider sp order by sp.name").getResultList();
        
        JSONObject root = new JSONObject();
        JSONArray rootArray = new JSONArray();
        
        Iterator it = serviceProviders.iterator();
        while (it.hasNext()) {
            ServiceProvider sp = (ServiceProvider)it.next();
            JSONObject parentObj = this.serviceProviderToJSON(sp);
            Layer topLayer = sp.getTopLayer();
            if (topLayer!=null) {
                HashSet set= new HashSet();
                set.add(topLayer);
                parentObj = createTreeList(set, parentObj);
                if (parentObj.has("children")){
                    rootArray.put(parentObj);
                }
            }
        }
        root.put("name","root");
        root.put("children", rootArray);
        return root;
    }
    // </editor-fold>
    
    /* Creates a JSON tree list of a given set of Layers and a set of restrictions
     * of which layer is visible and which isn't.
     *
     * @param layers Set of layers from which the part of the tree ahs to be build
     * @param organizationLayers Set of restrictions which define the visible and non visible layers
     * @param parent JSONObject which represents the parent object to which this set of layers should be added
     *
     * @throws JSONException
     */
    // <editor-fold defaultstate="" desc="createTreeList(Set layers, Set organizationLayers, JSONObject parent) method.">
    private JSONObject createTreeList(Set layers, JSONObject parent) throws JSONException {
        /* This method has a recusive function in it. Its function is to create a list of layers
         * in a tree like array which can be used to build up a menu structure.
         */
        Iterator layerIterator = layers.iterator();
        JSONArray parentArray = new JSONArray();
        while (layerIterator.hasNext()) {
            /* For each layer in the set we are going to create a JSON object which we will add to de total
             * list of layer objects.
             */
            Layer layer = (Layer)layerIterator.next();
            
            /* When we have retrieved this array we are able to save our object we are working with
             * at the moment. This object is our present layer object. This object first needs to be
             * transformed into a JSONObject, which we do by calling the method to do so.
             */
            JSONObject layerObj = this.layerToJSON(layer);
            
            /* Before we are going to save the present object we can first use our object to recieve and store
             * any information which there might be for the child layers. First we check if the set of layers
             * is not empty, because if it is, no effort has to be taken.
             * If, on the other hand, this layer does have children then the method is called recursivly to
             * add these childs to the present layer we are working on.
             */
            Set childLayers = layer.getLayers();
            if (childLayers != null && !childLayers.isEmpty()) {
                layerObj = createTreeList(childLayers, layerObj);
            }
            
            /* After creating the JSONObject for this layer and if necessary, filling this
             * object with her childs, we can add this JSON layer object back into its parent array.
             */
            parentArray.put(layerObj);
        }
        if (parentArray.length() > 0){
            parent.put("children", parentArray);
        }
        return parent;
    }
    // </editor-fold>
    
    /* Creates a JSON object from the ServiceProvider with its given name and id.
     *
     * @param serviceProvider The ServiceProvider object which has to be converted
     *
     * @return JSONObject
     *
     * @throws JSONException
     */
    // <editor-fold defaultstate="" desc="serviceProviderToJSON(ServiceProvider serviceProvider) method.">
    private JSONObject serviceProviderToJSON(ServiceProvider serviceProvider) throws JSONException {
        JSONObject root = new JSONObject();
        root.put("id", serviceProvider.getId());
        root.put("name", serviceProvider.getGivenName());
        root.put("type", "serviceprovider");
        return root;
    }
    // </editor-fold>
    
    /* Creates a JSON object from the Layer with its given name and id.
     *
     * @param layer The Layer object which has to be converted
     *
     * @return JSONObject
     *
     * @throws JSONException
     */
    // <editor-fold defaultstate="" desc="layerToJSON(Layer layer) method.">
    private JSONObject layerToJSON(Layer layer) throws JSONException{
        JSONObject jsonLayer = new JSONObject();
        jsonLayer.put("id", layer.getUniqueName());
        jsonLayer.put("name", layer.getTitle());
        jsonLayer.put("type", "layer");
        return jsonLayer;
    }
    // </editor-fold>
}
