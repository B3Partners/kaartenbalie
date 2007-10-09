/**
 * @(#)WMSUrlCreatorAction.java
 * @author R. Braam
 * @version 1.00 2007/02/20
 *
 * Purpose: a Struts action class defining all the Action for the ServiceProvider view.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.struts;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.struts.ExtendedMethodProperties;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.ServiceProvider;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.service.LayerValidator;
import nl.b3p.kaartenbalie.service.WMSParamUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.validator.DynaValidatorForm;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WMSUrlCreatorAction extends KaartenbalieCrudAction {
    
    private static final Log log = LogFactory.getLog(WMSUrlCreatorAction.class);
    
    protected static final String GETCAPABILITIES = "getCapabilities";
    protected static final String GETMAP = "getMapUrl";
    protected static final String DEFAULTVERSION = "1.1.1";
    protected static final String DEFAULTSERVICE = "WMS";
    protected static final String UNKNOWN_SES_USER_ERROR_KEY = "error.sesuser";
    protected static final String UNKNOWN_DB_USER_ERROR_KEY = "error.dbuser";
    protected static final String NO_LAYERS_SELECTED_ERROR_KEY = "error.nolayer";
    
    private static final String EXTRAREQUESTDATA="&VERSION=1.1.1&STYLES=&EXCEPTIONS=INIMAGE&WRAPDATELINE=true&BGCOLOR=0xF0F0F0";
    
    //-------------------------------------------------------------------------------------------------------
    // PUBLIC METHODS
    //-------------------------------------------------------------------------------------------------------
    
    /* Execute method which handles all unspecified requests.
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
        this.createLists(dynaForm, request);
        prepareMethod(dynaForm, request, LIST, LIST);
        addDefaultMessage(mapping, request);
        return mapping.findForward(SUCCESS);
    }
    // </editor-fold>
    
    /* Creates a list with all visible layers for the user and sets a couple of settings to the screen.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     *
     * @throws Exception, JSONException
     */
    // <editor-fold defaultstate="" desc="createLists(DynaValidatorForm form, HttpServletRequest request) method.">
    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws JSONException, Exception {
        super.createLists(form, request);
        
        User user = (User) request.getUserPrincipal();
        form.set("personalUrl", user.getPersonalURL());
        
        String[] formats=new String[5];
        formats[0]="image/gif";
        formats[1]="image/png";
        formats[2]="image/jpeg";
        formats[3]="image/bmp";
        formats[4]="image/tiff";
        request.setAttribute("formatList",formats);
        
        Set organizationLayers = user.getOrganization().getOrganizationLayer();
        List serviceProviders = em.createQuery("from ServiceProvider sp order by sp.name").getResultList();
        
        LayerValidator lv = new LayerValidator(organizationLayers);
        String[] alSrsen = lv.validateSRS();
        request.setAttribute("projectieList",alSrsen);
        
        JSONObject root = new JSONObject();
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
        root.put("name","root");
        root.put("children", rootArray);
        request.setAttribute("layerList", root);
    }
    // </editor-fold>
    
    /* Method which calculates the specific GetMap URL for the user given the input from the from.
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
    // <editor-fold defaultstate="" desc="getMapUrl(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    public ActionForward getMapUrl(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        /*
         * Before we can start checking for changes or adding a new GetMap URL, we first need to check if
         * everything is valid. First there will be checked if the request is valid. This means that every JSP
         * page, when it is requested, gets a unique hash token. This token is internally saved by Struts and
         * checked when an action will be performed.
         * Each time when a JSP page is opened (requested) a new hash token is made and added to the page. Now
         * when an action is performed and Struts reads this token we can perform a check if this token is an
         * old token (the page has been requested again with a new token) or the token has already been used for
         * an action).
         * This type of check performs therefore two safety's. First of all if a user clicks more then once on a
         * button this action will perform only the first click. Second, if a user has the same page opened twice
         * only on one page a action can be performed (this is the page which is opened last). The previous page
         * isn't valid anymore.
         */
        if (!isTokenValid(request)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        /*
         * If a token is valid the second validation is necessary. This validation performs a check on the
         * given parameters supported by the user. Off course this check should already have been performed
         * by a Javascript which does exactly the same, but some browsers might not support JavaScript or
         * JavaScript can be disabled by the browser/user.
         */
        ActionErrors errors = dynaForm.validate(mapping, request);
        if(!errors.isEmpty()) {
            super.addMessages(request, errors);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, VALIDATION_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        //Create the String
        String[] layers     = (String[])dynaForm.get("sortedLayers");
        String projectie    = (String)  dynaForm.get("selectedProjectie");
        String bbox         = (String)  dynaForm.get("bbox");
        Integer height      = (Integer) dynaForm.get("height");
        Integer width       = (Integer) dynaForm.get("width");
        String format       = (String)  dynaForm.get("selectedFormat");
        String pUrl         = (String)  dynaForm.get("personalUrl");
        
        if(layers.length == 0) {
            prepareMethod(dynaForm, request, LIST, LIST);
            addAlternateMessage(mapping, request, NO_LAYERS_SELECTED_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        /*
         * Get the URL to start with
         */
        User user = (User) request.getUserPrincipal();
        if(user == null) {
            prepareMethod(dynaForm, request, LIST, LIST);
            addAlternateMessage(mapping, request, UNKNOWN_SES_USER_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        String getMap = user.getPersonalURL();
        
        /*
         * Add the rest of the parameters
         */
        getMap += "?REQUEST=GetMap";
        getMap+="&LAYERS=";
        
        for (int i = 0; i < layers.length; i++){
            if (i == 0)
                getMap += layers[i];
            else{
                getMap += "," + layers[i];
            }
        }
        
        getMap += "&BBOX=" + bbox + "&SRS=" + projectie + "&HEIGHT=" + height + "&WIDTH=" + width + "&FORMAT=" + format + EXTRAREQUESTDATA;
        
        user.setDefaultGetMap(getMap);
        
        if (user.getId() == null) {
            em.persist(user);
        }
        
        populateForm(getMap, dynaForm, request);
        return mapping.findForward("success");
    }
    // </editor-fold>
    
    //-------------------------------------------------------------------------------------------------------
    // PROTECTED METHODS
    //-------------------------------------------------------------------------------------------------------
    
    /* Creates a list with the available layers.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="" desc="getActionMethodPropertiesMap() method.">
    protected Map getActionMethodPropertiesMap() {
        Map map = super.getActionMethodPropertiesMap();
        ExtendedMethodProperties crudProp = new ExtendedMethodProperties(GETCAPABILITIES);
        crudProp.setDefaultForwardName(SUCCESS);
        crudProp.setDefaultMessageKey("beheer.kaarten.wmsurlcreator.success");
        crudProp.setAlternateForwardName(FAILURE);
        crudProp.setAlternateMessageKey("beheer.kaarten.wmsurlcreator.failed");
        map.put(GETCAPABILITIES, crudProp);
        
        crudProp = new ExtendedMethodProperties(GETMAP);
        crudProp.setDefaultForwardName(SUCCESS);
        crudProp.setDefaultMessageKey("beheer.kaarten.wmsurlcreator.success");
        crudProp.setAlternateForwardName(FAILURE);
        crudProp.setAlternateMessageKey("beheer.kaarten.wmsurlcreator.failed");
        map.put(GETMAP, crudProp);
        
        return map;
    }
    // </editor-fold>
    
    //-------------------------------------------------------------------------------------------------------
    // PRIVATE METHODS
    //-------------------------------------------------------------------------------------------------------
    
    /* Creates a list with the available layers.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="" desc="populateForm(String getMap, DynaValidatorForm form) method.">
    private void populateForm(String getMap, DynaValidatorForm form, HttpServletRequest request) throws JSONException, Exception {
        this.createLists(form, request);
        if (WMSParamUtil.getParameter(WMSParamUtil.LAYERS, getMap) != null){
            String[] layers= WMSParamUtil.getParameter(WMSParamUtil.LAYERS,getMap).split(",");
            form.set("selectedLayers",layers);
        }
        if (WMSParamUtil.getParameter(WMSParamUtil.BBOX, getMap) != null){
            form.set("bbox",WMSParamUtil.getParameter(WMSParamUtil.BBOX,getMap));
        }
        if (WMSParamUtil.getParameter(WMSParamUtil.SRS, getMap) != null){
            form.set("selectedProjectie",WMSParamUtil.getParameter(WMSParamUtil.SRS,getMap));
        }
        if (WMSParamUtil.getParameter(WMSParamUtil.HEIGHT, getMap) != null){
            form.set("height",new Integer(WMSParamUtil.getParameter(WMSParamUtil.HEIGHT,getMap)));
        }
        if (WMSParamUtil.getParameter(WMSParamUtil.WIDTH, getMap) != null){
            form.set("width",new Integer(WMSParamUtil.getParameter(WMSParamUtil.WIDTH,getMap)));
        }
        form.set("defaultGetMap", getMap);
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

