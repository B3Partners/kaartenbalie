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

import java.util.Date;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPricing;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import org.hibernate.HibernateException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Jytte
 */
public abstract class PricingAction extends KaartenbalieCrudAction {

    private static final Log log = LogFactory.getLog(PricingAction.class);
    private static final String TESTFW = "test";
    private static final String START_END_ERROR_KEY = "error.dateinput";
    private static final String LAYER_PLACEHOLDER_ERROR_KEY = "beheer.princing.placeholder.error";
    private static final String SCALE_ERROR_KEY = "beheer.pricing.scale.error";

    /** Creates a new instance of PricingAction */
    public PricingAction() {
    }

    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject root = this.createTree();
        request.setAttribute("layerList", root);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward delete(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws HibernateException, Exception {
        LayerPricing lp = getLayerPricing(dynaForm, request, false);
        if (lp == null) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        lp.setDeletionDate(new Date());
        prepareMethod(dynaForm, request, LIST, EDIT);
        addDefaultMessage(mapping, request);
        return getDefaultForward(mapping, request);
    }

    public LayerPricing getLayerPricing(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew) {
        EntityManager em = getEntityManager();
        LayerPricing lp = null;
        Integer id = getPricingID(dynaForm);
        if (null == id && createNew) {
            lp = new LayerPricing();
        } else if (null != id) {
            lp = (LayerPricing) em.find(LayerPricing.class, id);
        }
        return lp;
    }

    private Integer getPricingID(DynaValidatorForm dynaForm) {
        return FormUtils.StringToInteger(dynaForm.getString("pricingid"));
    }

    protected Integer getLayerID(DynaValidatorForm dynaForm) {
        return FormUtils.StringToInteger(dynaForm.getString("id"));
    }

    protected abstract JSONObject createTree() throws JSONException;
}