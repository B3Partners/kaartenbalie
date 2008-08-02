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

import java.util.Calendar;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.commons.struts.ExtendedMethodProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author Jytte
 */
public abstract class TestPricingAction extends KaartenbalieCrudAction {

    private static final Log log = LogFactory.getLog(PricingAction.class);
    private static final String TEST = "test";
    private static final String BACKFW = "back";
    private static final String BACK = "back";
    private static final String START_END_ERROR_KEY = "error.dateinput";
    private static final String LAYER_PLACEHOLDER_ERROR_KEY = "beheer.princing.placeholder.error";

    /** Creates a new instance of TestPricingAction */
    public TestPricingAction() {
    }

    protected Map getActionMethodPropertiesMap() {
        Map map = super.getActionMethodPropertiesMap();

        ExtendedMethodProperties crudProp = new ExtendedMethodProperties(TEST);
        crudProp.setDefaultForwardName(SUCCESS);
        crudProp.setDefaultMessageKey("beheer.pricing.test.succes");
        crudProp.setAlternateForwardName(FAILURE);
        crudProp.setAlternateMessageKey("beheer.pricing.test.failed");
        map.put(TEST, crudProp);

        crudProp = new ExtendedMethodProperties(BACK);
        crudProp.setDefaultForwardName(BACKFW);
        crudProp.setAlternateForwardName(BACKFW);
        map.put(BACK, crudProp);
        return map;
    }

    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Calendar cal = Calendar.getInstance();
        dynaForm.set("testFrom", FormUtils.DateToFormString(cal.getTime(), null));
        cal.add(Calendar.DAY_OF_YEAR, 7);
        dynaForm.set("testUntil", FormUtils.DateToFormString(cal.getTime(), null));

        prepareMethod(dynaForm, request, LIST, LIST);
        addDefaultMessage(mapping, request);
        return getDefaultForward(mapping, request);
    }

    public ActionForward back(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionRedirect redirect = new ActionRedirect(getDefaultForward(mapping, request));
        redirect.addParameter("id", dynaForm.getString("id"));
        redirect.addParameter("edit", "t");
        return redirect;
    }

    protected Integer getLayerID(DynaValidatorForm dynaForm) {
        return FormUtils.StringToInteger(dynaForm.getString("id"));
    }

    protected abstract ActionForward test(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception;
}