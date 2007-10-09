/**
 * @(#)MapviewerAction.java
 * @author N. de Goeij
 * @version 1.00 2006/10/24
 *
 * Purpose: a Struts action class defining all the Action for the map viewer.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.struts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.ServiceProvider;
import nl.b3p.kaartenbalie.core.server.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.validator.DynaValidatorForm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapviewerAction extends KaartenbalieCrudAction {
    //Moet toch een kaartenbaliecrudaction extenden, vanwege de createlist....
    
    /* forward name="success" path="" */
    private final static String SUCCESS = "success";
    private List layerList = new ArrayList();
    private int id;
    private static final Log log = LogFactory.getLog(MapviewerAction.class);
    
    /** Execute method which handles all executable requests.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     *
     * @return an Actionforward object.
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="" desc="unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        
        User sessionUser = (User) request.getUserPrincipal();
        if(sessionUser == null) {
            addAlternateMessage(mapping, request, UNKNOWN_SES_USER_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        String checkedLayers=request.getParameter("layers");
        String extend=request.getParameter("extent");
        request.setAttribute("checkedLayers",checkedLayers);
        request.setAttribute("extent",extend);
        return super.unspecified(mapping,dynaForm,request,response);
    }
    // </editor-fold>
    
    /** Creates a list with the available layers.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="" desc="createLists(DynaValidatorForm form, HttpServletRequest request) method.">
    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws JSONException, Exception {
        super.createLists(form, request);
        User sesuser = (User) request.getUserPrincipal();
        
        /*
         * THIS LINE CAN NOT BE REMOVED. THIS IS NOT REDUNDANT!!!!
         * IF A ADMINISTRATOR CHANGES THE RIGHTS AND WE WORK WITH A SESSION USER
         * THIS USER HAS STILL THE OLD RIGHT UNTILL HIS SESSION IS OVER!!!!
         * THERFORE THERE MUST BE ALWAYS CHECKED AGAINST THE DATABE.
         * RE-LOGIN OF THE USER IS NOT USEFULL HERE SINCE THE USER DID NOT MAKE
         * ANY CHANGES, THE ADMINISTRATOR DID!!!!
         */
        User user = (User)em.find(User.class, sesuser.getId());
        if (sesuser==null)
            return;
                
        Set organizationLayers = user.getOrganization().getOrganizationLayer();
        List serviceProviders = em.createQuery("from ServiceProvider sp order by sp.name").getResultList();
        
        JSONArray rootArray = new JSONArray();
        Iterator it = serviceProviders.iterator();
        while (it.hasNext()) {
            ServiceProvider sp = (ServiceProvider)it.next();
            
            JSONObject parentObj = this.serviceProviderToJSON(sp);
            HashSet set= new HashSet();
            set.add(sp.getTopLayer());
            parentObj = createTreeList(set, organizationLayers, parentObj);
            if (parentObj.has("children")){
                rootArray.put(parentObj);
            }
        }
        
        JSONObject root = new JSONObject();
        root.put("name","root");
        root.put("children", rootArray);
        request.setAttribute("layerList", root);
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
    private JSONObject createTreeList(Set layers, Set organizationLayers, JSONObject parent) throws JSONException {
        /* This method has a recusive function in it. Its functionality is to create a list of layers
         * in a tree like array which can be used to build up a menu structure.
         */
        Iterator layerIterator = layers.iterator();
        JSONArray parentArray = new JSONArray();
        while (layerIterator.hasNext()) {
            /* For each layer in the set we are going to create a JSON object which we will add to de total
             * list of layer objects.
             */
            Layer layer = (Layer)layerIterator.next();
            /* Before a layer is going to be added we need to make sure that this layer is allowed to be seen
             * If a layer is not allowed to be seen there is no use to create a JSON object for it, neither
             * is it necessary to check if child layers shoudl be added.
             */
            if(hasVisibility(layer, organizationLayers)) {
                /* When the visibility check has turned out to be ok. we start adding this layer to
                 * the list of layers.
                 */
                
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
                    layerObj = createTreeList(childLayers, organizationLayers, layerObj);
                }
                
                /* After creating the JSONObject for this layer and if necessary, filling this
                 * object with her childs, we can add this JSON layer object back into its parent array.
                 */
                parentArray.put(layerObj);
                
                
            }
        }
        if (parentArray.length()>0){
            parent.put("children",parentArray);
        }
        return parent;
    }
    // </editor-fold>
    
    /* Method which checks if a certain layer is allowed to be shown on the screen.
     *
     * @param layer Layer object that has to be checked
     * @param organizationLayers Set of restrictions which define the visible and non visible layers
     *
     * @return boolean
     */
    // <editor-fold defaultstate="" desc="hasVisibility(Layer layer, Set orgLayers) method.">
    private boolean hasVisibility(Layer layer, Set organizationLayers) {
        Iterator it = organizationLayers.iterator();
        while (it.hasNext()) {
            Layer organizationLayer = (Layer) it.next();
            System.out.println("---" + layer);
            System.out.println("---" + layer.getId());
            System.out.println("---" + organizationLayer);
            System.out.println("---" + organizationLayer.getId());
            if(layer.getId().equals(organizationLayer.getId())) {
                return true;
            }
        }
        return false;
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
