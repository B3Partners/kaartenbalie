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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
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
        if (sessionUser == null) {
            addAlternateMessage(mapping, request, UNKNOWN_SES_USER_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }

        String checkedLayers = request.getParameter("layers");
        String extend = request.getParameter("extent");
        request.setAttribute("checkedLayers", checkedLayers);
        request.setAttribute("extent", extend);

        prepareMethod(dynaForm, request, EDIT, LIST);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return mapping.findForward(SUCCESS);
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
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();

        User sesuser = (User) request.getUserPrincipal();
        if (sesuser == null) {
            return;
        /*
         * THIS LINE CAN NOT BE REMOVED. THIS IS NOT REDUNDANT!!!!
         * IF A ADMINISTRATOR CHANGES THE RIGHTS AND WE WORK WITH A SESSION USER
         * THIS USER HAS STILL THE OLD RIGHT UNTILL HIS SESSION IS OVER!!!!
         * THERFORE THERE MUST BE ALWAYS CHECKED AGAINST THE DATABE.
         * RE-LOGIN OF THE USER IS NOT USEFULL HERE SINCE THE USER DID NOT MAKE
         * ANY CHANGES, THE ADMINISTRATOR DID!!!!
         */
        }
        User user = (User) em.find(User.class, sesuser.getId());
        if (user == null) {
            return;
        }
        Set userLayers = user.getLayers();
        JSONObject root = createTree(userLayers);
        request.setAttribute("layerList", root);
    }
    // </editor-fold>

}
