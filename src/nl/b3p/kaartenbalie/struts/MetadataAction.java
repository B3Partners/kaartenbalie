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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.kaartenbalie.core.server.Layer;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.ServiceProvider;
import nl.b3p.kaartenbalie.core.server.SrsBoundingBox;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.service.LayerValidator;
import nl.b3p.kaartenbalie.service.ServiceProviderValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.Form;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.validator.DynaValidatorForm;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MetadataAction extends KaartenbalieCrudAction {
    
    private final static String SUCCESS = "success";
    private static final Log log = LogFactory.getLog(MetadataAction.class);
    
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        JSONObject root = this.createTree();
        request.setAttribute("layerList", root);
        return mapping.findForward(SUCCESS);
    }
    // </editor-fold>
    
    /* Creates a list with the available layers.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     *
     * @throws HibernateException, JSONException, Exception
     */
    // <editor-fold defaultstate="" desc="createLists(DynaValidatorForm form, HttpServletRequest request) method.">
    public ActionForward edit(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String layerid = (String)dynaForm.get("id");
        Integer id = new Integer(Integer.parseInt(layerid.substring(0, layerid.indexOf("_"))));
        
        Layer layer = (Layer)getHibernateSession().createQuery(
                    "from Layer l where " +
                    "(l.id) = lower(:id) ")
                .setParameter("id", id)
                .uniqueResult();
        
        populateMetadataEditorForm(layer, dynaForm, request);
        return getDefaultForward(mapping, request);
    }
    // </editor-fold>
    
    
    /* Method which will fill the JSP form with the data of  a given organization.
     *
     * @param organization Organization object from which the information has to be printed.
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     */
    // <editor-fold defaultstate="" desc="populateOrganizationForm(Organization organization, DynaValidatorForm dynaForm, HttpServletRequest request) method.">
    private void populateMetadataEditorForm(Layer layer, DynaValidatorForm dynaForm, HttpServletRequest request) {
        dynaForm.set("id", layer.getId().toString());
        dynaForm.set("name", layer.getName());
        dynaForm.set("xml", layer.getMetaData());
    }
    // </editor-fold>
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /** Tries to find a specified layer given for a certain ServiceProvider.
     *
     * @param layers the set with layers which the method has to surch through
     * @param layerToBeFound the layer to be found
     *
     * @return string with the name of the found layer or null if no layer was found
     */
    // <editor-fold defaultstate="" desc="findLayer(String layerToBeFound, Set layers) method.">
    private String findLayer(String layerToBeFound, Set layers) {
        if (layers==null || layers.isEmpty())
            return null;
        
        Iterator it = layers.iterator();
        while (it.hasNext()) {
            Layer layer = (Layer) it.next();
            String identity = layer.getId() + "_" + layer.getName();
            if(identity.equalsIgnoreCase(layerToBeFound))
                return layer.getName();
            
            String foundLayer = findLayer(layerToBeFound, layer.getLayers());
            if (foundLayer != null)
                return foundLayer;
        }
        return null;
    }
    // </editor-fold>
    
    
    
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
        List serviceProviders = getHibernateSession().createQuery("from ServiceProvider sp order by sp.name").list();
        
        JSONObject root = new JSONObject(); 
        JSONArray rootArray = new JSONArray(); 
        
        Iterator it = serviceProviders.iterator();
        while (it.hasNext()) {
            ServiceProvider sp = (ServiceProvider)it.next();
            JSONObject parentObj = this.serviceProviderToJSON(sp);
            HashSet set= new HashSet();
            set.add(sp.getTopLayer());
            parentObj = createTreeList(set, parentObj);
            if (parentObj.has("children")){
                rootArray.put(parentObj);
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
        jsonLayer.put("id", layer.getId() + "_" + layer.getName());
        jsonLayer.put("name", layer.getName());
        jsonLayer.put("type", "layer");
        return jsonLayer;
    }
    // </editor-fold>
}
