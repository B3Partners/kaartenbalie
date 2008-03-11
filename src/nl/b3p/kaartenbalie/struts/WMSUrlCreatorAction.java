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
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.struts.ExtendedMethodProperties;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.ServiceProvider;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.service.LayerValidator;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.OGCRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.validator.DynaValidatorForm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WMSUrlCreatorAction extends KaartenbalieCrudAction {
    
    private static final Log log = LogFactory.getLog(WMSUrlCreatorAction.class);
    protected static final String GETMAP                        = "getMapUrl";
    protected static final String UNKNOWN_SES_USER_ERROR_KEY    = "error.sesuser";
    protected static final String UNKNOWN_DB_USER_ERROR_KEY     = "error.dbuser";
    protected static final String NO_LAYERS_SELECTED_ERROR_KEY  = "error.nolayer";
    
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
        
        EntityManager em = getEntityManager();
        User sesuser = (User) request.getUserPrincipal();
        if (sesuser==null)
            return;
        User user = (User)em.find(User.class, sesuser.getId());
        if (user==null)
            return;
        
        form.set("personalUrl", user.getPersonalURL());
        String bbox = (String)form.get("bbox");
        if (bbox==null || bbox.length()==0)
            form.set("bbox","12000,304000,280000,620000"); // heel nederland
        
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
            Layer topLayer = sp.getTopLayer();
            if (topLayer!=null) {
                HashSet set= new HashSet();
                set.add(topLayer);
                parentObj = createTreeList(set, organizationLayers, parentObj);
                if (parentObj.has("children")){
                    rootArray.put(parentObj);
                }
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
        if (!isTokenValid(request)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
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
        
        StringBuffer layerString = new StringBuffer();
        for (int i = 0; i < layers.length; i++) {
            layerString.append(layers[i]);
            layerString.append(",");
        }
        String layer = layerString.substring(0, layerString.lastIndexOf(","));
        
        StringBuffer getMapUrl = new StringBuffer(user.getPersonalURL());
        getMapUrl.append("?");
        getMapUrl.append(OGCConstants.WMS_VERSION);
        getMapUrl.append("=");
        getMapUrl.append(OGCConstants.WMS_VERSION_111);
        getMapUrl.append("&");
        getMapUrl.append(OGCConstants.WMS_REQUEST);
        getMapUrl.append("=");
        getMapUrl.append(OGCConstants.WMS_REQUEST_GetMap);
        getMapUrl.append("&");
        getMapUrl.append(OGCConstants.WMS_PARAM_LAYERS);
        getMapUrl.append("=");        
        getMapUrl.append(layer);        
        getMapUrl.append("&");
        getMapUrl.append(OGCConstants.WMS_PARAM_BBOX);
        getMapUrl.append("=");
        getMapUrl.append(bbox);
        getMapUrl.append("&");
        getMapUrl.append(OGCConstants.WMS_PARAM_SRS);
        getMapUrl.append("=");
        getMapUrl.append(projectie);
        getMapUrl.append("&");
        getMapUrl.append(OGCConstants.WMS_PARAM_HEIGHT);
        getMapUrl.append("=");
        getMapUrl.append(height);
        getMapUrl.append("&");
        getMapUrl.append(OGCConstants.WMS_PARAM_WIDTH);
        getMapUrl.append("=");
        getMapUrl.append(width);
        getMapUrl.append("&");
        getMapUrl.append(OGCConstants.WMS_PARAM_FORMAT);
        getMapUrl.append("=");
        getMapUrl.append(format);
        getMapUrl.append("&");
        getMapUrl.append(OGCConstants.WMS_PARAM_BGCOLOR);
        getMapUrl.append("=");
        getMapUrl.append("0xF0F0F0");
        getMapUrl.append("&");
        getMapUrl.append(OGCConstants.WMS_PARAM_EXCEPTIONS);
        getMapUrl.append("=");
        getMapUrl.append(OGCConstants.WMS_PARAM_EXCEPTION_INIMAGE);
        getMapUrl.append("&");
        getMapUrl.append(OGCConstants.WMS_PARAM_STYLES);
        getMapUrl.append("=");
        
        user.setDefaultGetMap(getMapUrl.toString());
        
        EntityManager em = getEntityManager();
        if (user.getId() == null) {
            em.persist(user);
        } else {
            em.merge(user);
        }
        em.flush();
        
        populateForm(getMapUrl.toString(), dynaForm, request);
        prepareMethod(dynaForm, request, LIST, EDIT);
        addDefaultMessage(mapping, request);
        return getDefaultForward(mapping, request);
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
        ExtendedMethodProperties crudProp = new ExtendedMethodProperties(GETMAP);
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
    private void populateForm(String getMapUrl, DynaValidatorForm form, HttpServletRequest request) throws JSONException, Exception {
        this.createLists(form, request);
        OGCRequest ogcrequest = new OGCRequest(getMapUrl);
        
        if (ogcrequest.containsParameter(OGCConstants.WMS_PARAM_LAYERS)) {
            form.set("selectedLayers", ogcrequest.getParameter(OGCConstants.WMS_PARAM_LAYERS).split(","));
        }
        if (ogcrequest.containsParameter(OGCConstants.WMS_PARAM_BBOX)) {
            form.set("bbox", ogcrequest.getParameter(OGCConstants.WMS_PARAM_BBOX));
        }
        if (ogcrequest.containsParameter(OGCConstants.WMS_PARAM_SRS)) {
            form.set("selectedProjectie", ogcrequest.getParameter(OGCConstants.WMS_PARAM_SRS));
        }
        if (ogcrequest.containsParameter(OGCConstants.WMS_PARAM_WIDTH)) {
            form.set("height", new Integer(ogcrequest.getParameter(OGCConstants.WMS_PARAM_WIDTH)));
        }
        if (ogcrequest.containsParameter(OGCConstants.WMS_PARAM_HEIGHT)) {
            form.set("width", new Integer(ogcrequest.getParameter(OGCConstants.WMS_PARAM_HEIGHT)));
        }
        
        form.set("defaultGetMap", getMapUrl);
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
            Layer layer = (Layer)layerIterator.next();
            
            JSONObject layerObj = this.layerToJSON(layer);
            
            Set childLayers = layer.getLayers();
            if (childLayers != null && !childLayers.isEmpty()) {
                layerObj = createTreeList(childLayers, organizationLayers, layerObj);
            }
            
            if(hasVisibility(layer, organizationLayers) || layerObj.has("children")) {
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
        if (layer==null || organizationLayers==null)
            return false;
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

