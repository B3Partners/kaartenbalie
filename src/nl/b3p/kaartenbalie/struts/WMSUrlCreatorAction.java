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
    protected static final String GETMAP = "getMapUrl";
    protected static final String UNKNOWN_SES_USER_ERROR_KEY = "error.sesuser";
    protected static final String UNKNOWN_DB_USER_ERROR_KEY = "error.dbuser";
    protected static final String NO_LAYERS_SELECTED_ERROR_KEY = "error.nolayer";
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

        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        User sesuser = (User) request.getUserPrincipal();
        if (sesuser == null) {
            return;
        }
        User user = (User) em.find(User.class, sesuser.getId());
        if (user == null) {
            return;
        }
        form.set("personalUrl", user.getPersonalURL());
        String bbox = (String) form.get("bbox");
        if (bbox == null || bbox.length() == 0) {
            form.set("bbox", "12000,304000,280000,620000"); // heel nederland
        }
        String[] formats = new String[5];
        formats[0] = "image/gif";
        formats[1] = "image/png";
        formats[2] = "image/jpeg";
        formats[3] = "image/bmp";
        formats[4] = "image/tiff";
        request.setAttribute("formatList", formats);

        Set organizationLayers = user.getOrganization().getOrganizationLayer();
        JSONObject root = createTree(organizationLayers);
        request.setAttribute("layerList", root);

        LayerValidator lv = new LayerValidator(organizationLayers);
        String[] alSrsen = lv.validateSRS();
        request.setAttribute("projectieList", alSrsen);

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
        if (!errors.isEmpty()) {
            super.addMessages(request, errors);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, VALIDATION_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }

        //Create the String
        String[] layers = (String[]) dynaForm.get("sortedLayers");
        String projectie = (String) dynaForm.get("selectedProjectie");
        String bbox = (String) dynaForm.get("bbox");
        Integer height = (Integer) dynaForm.get("height");
        Integer width = (Integer) dynaForm.get("width");
        String format = (String) dynaForm.get("selectedFormat");
        String pUrl = (String) dynaForm.get("personalUrl");

        if (layers.length == 0) {
            prepareMethod(dynaForm, request, LIST, LIST);
            addAlternateMessage(mapping, request, NO_LAYERS_SELECTED_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }

        /*
         * Get the URL to start with
         */
        User user = (User) request.getUserPrincipal();
        if (user == null) {
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
        getMapUrl.append(OGCConstants.WMS_SERVICE);
        getMapUrl.append("=");
        getMapUrl.append(OGCConstants.WMS_SERVICE_WMS);
        getMapUrl.append("&");
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

        log.debug("Getting entity manager ......");
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
}

