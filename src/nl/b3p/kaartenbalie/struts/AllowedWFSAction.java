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
 * 
 * @author Rachelle Scheijen
 */
package nl.b3p.kaartenbalie.struts;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.commons.struts.ExtendedMethodProperties;
import nl.b3p.kaartenbalie.service.WFSParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public class AllowedWFSAction extends ServerAction {
    private static final Log log = LogFactory.getLog(WmsServerAction.class);
    protected static final String ABBR_WARN_KEY = "warning.abbr.changed";
    protected static final String MAPPING_TEST = "test";
    protected static final String MAPPING_BATCH_UPDATE = "batchUpdate";
    protected static long maxResponseTime = 10000;
    protected WFSParser parser;

    public AllowedWFSAction() {
        parser = new WFSParser();
    }

    @Override
    protected Map getActionMethodPropertiesMap() {
        Map map = super.getActionMethodPropertiesMap();

        ExtendedMethodProperties crudProp = null;

        crudProp = new ExtendedMethodProperties(SAVE);
        crudProp.setDefaultForwardName(SUCCESS);
        crudProp.setAlternateForwardName(LISTFW);
        map.put(SAVE, crudProp);

        return map;
    }
    
    /*
     * Method for saving a new service provider from input of a user.
     *
     * @param mapping The ActionMapping used to select this instance. @param
     * form The DynaValidatorForm bean for this request. @param request The HTTP
     * Request we are processing. @param response The HTTP Response we are
     * processing.
     *
     * @return an Actionforward object.
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="" desc="save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">

    @Override
    public ActionForward save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        /*
         * Change DataWarehousing mode to performance as this is a very
         * complicated process which will otherwise consume a lot of time.
         */
        if (!isTokenValid(request)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        String url = FormUtils.getString(dynaForm.getString("providerId"));
        String action = FormUtils.getString(dynaForm.getString("action"));
        log.error("test save.  url : "+url);
        
        if( action.equalsIgnoreCase("delete") ){
            /* Delete service from list */
            this.parser.deleteAllowedService(url, em);
        }
        else {
            /* Add service to list */
            this.parser.addAllowedService(url, em);
        }
        
        dynaForm.initialize(mapping);
        prepareMethod(dynaForm, request, LIST, EDIT);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return getDefaultForward(mapping, request);
    }

    /*
     * Creates a list of all the service providers in the database.
     *
     * @param form The DynaValidatorForm bean for this request. @param request
     * The HTTP Request we are processing.
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="" desc="createLists(DynaValidatorForm form, HttpServletRequest request) method.">
    @Override
    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        super.createLists(form, request);
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        
        List currentServiceslist    = parser.getAllowedServices(em);
        List otherServiceslist      = parser.getNotAllowedServices(em);
        
        request.setAttribute("currentServiceslist", currentServiceslist);
        request.setAttribute("otherServiceslist", otherServiceslist);
    }
}
