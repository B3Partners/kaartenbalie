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

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.commons.struts.ExtendedMethodProperties;
import nl.b3p.ogc.wfs.v110.WfsServiceProvider;
import nl.b3p.wms.capabilities.ServiceProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public abstract class ServerAction extends KaartenbalieCrudAction {

    private static final Log log = LogFactory.getLog(ServerAction.class);

    protected static final String ADD = "add";

    protected static final String SERVER_CONNECTION_ERRORKEY = "error.serverconnection";
    protected static final String MALFORMED_URL_ERRORKEY = "error.malformedurl";
    protected static final String MALFORMED_CREDENTIALS_ERRORKEY = "error.malformedcredentials";
    protected static final String MALFORMED_CAPABILITY_ERRORKEY = "error.malformedcapability";
    protected static final String SERVICE_LINKED_ERROR_KEY = "error.servicestilllinked";
    protected static final String UNSUPPORTED_WMSVERSION_ERRORKEY = "error.wmsversion";
    protected static final String SP_NOTFOUND_ERROR_KEY = "error.spnotfound";
    protected static final String NON_UNIQUE_ABBREVIATION_ERROR_KEY = "error.abbr.notunique";
    protected static final String NON_ALPHANUMERIC_ABBREVIATION_ERROR_KEY = "error.abbr.notalphanumeric";
    protected static final String ABBR_RESERVED_ERROR_KEY = "error.abbr.reserved";
    protected static final String ORG_JOINED_KEY = "beheer.server.org.joined";
    protected static final String LAYER_JOINED_KEY = "beheer.server.layer.joined";
    protected static final String PRICING_JOINED_KEY = "beheer.server.pricing.joined";

    @Override
    protected Map getActionMethodPropertiesMap() {
        Map map = super.getActionMethodPropertiesMap();

        ExtendedMethodProperties crudProp = null;

        crudProp = new ExtendedMethodProperties(ADD);
        crudProp.setDefaultForwardName(SUCCESS);
        crudProp.setAlternateForwardName(LISTFW);
        map.put(ADD, crudProp);

        return map;
    }

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
        prepareMethod(dynaForm, request, LIST, LIST);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward add(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // eerst alles wissen
        dynaForm.initialize(mapping);
        dynaForm.set("givenName", request.getParameter("givenName"));
        dynaForm.set("url", request.getParameter("url"));
        dynaForm.set("updatedDate", request.getParameter("updatedDate"));
        dynaForm.set("abbr", request.getParameter("abbr"));

        prepareMethod(dynaForm, request, EDIT, LIST);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return getDefaultForward(mapping, request);
    }

    protected Integer getID(DynaValidatorForm dynaForm) {
        return FormUtils.StringToInteger(dynaForm.getString("id"));
    }
    // </editor-fold>
    protected boolean isAlphaNumeric(String s) {
        if (s == null) {
            return false;
        }
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(s);
        if (m.find()) {
            return false;
        }
        return true;
    }

    protected boolean isAbbrUnique(Integer spId, DynaValidatorForm dynaForm, EntityManager em) {
        return isWFSAbbrUnique(spId, dynaForm, em) && isWMSAbbrUnique(spId, dynaForm, em);
    }

    protected boolean isWFSAbbrUnique(Integer spId, DynaValidatorForm dynaForm, EntityManager em) {
        try {
            WfsServiceProvider dbSp = (WfsServiceProvider) em.createQuery(
                    "from WfsServiceProvider sp where " +
                    "lower(sp.abbr) = lower(:abbr) ").setParameter("abbr", FormUtils.nullIfEmpty(dynaForm.getString("abbr"))).getSingleResult();

            if (dbSp != null) {
                if (spId != null) {
                    if (spId.equals(dbSp.getId())) {
                        return true;
                    }
                }
            }
            return false;
        } catch (NoResultException nre) {
            return true;
        }
    }

    protected boolean isWMSAbbrUnique(Integer spId, DynaValidatorForm dynaForm, EntityManager em) {
        try {
            ServiceProvider dbSp = (ServiceProvider) em.createQuery(
                    "from ServiceProvider sp where " +
                    "lower(sp.abbr) = lower(:abbr) ").setParameter("abbr", FormUtils.nullIfEmpty(dynaForm.getString("abbr"))).getSingleResult();
            if (dbSp != null) {
                if (spId != null) {
                    if (spId.equals(dbSp.getId())) {
                        return true;
                    }
                }
            }
            return false;
        } catch (NoResultException nre) {
            return true;
        }
    }

}
