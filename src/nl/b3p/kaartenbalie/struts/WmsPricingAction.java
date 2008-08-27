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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.kaartenbalie.core.server.UniqueIndex;
import nl.b3p.kaartenbalie.core.server.accounting.LayerCalculator;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPricing;
import nl.b3p.kaartenbalie.core.server.datawarehousing.DwObjectAction;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.ServiceProvider;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Chris Kramer
 */
public class WmsPricingAction extends PricingAction {

    private static final Log log = LogFactory.getLog(WmsPricingAction.class);
    private static final String START_END_ERROR_KEY = "error.dateinput";
    private static final String LAYER_PLACEHOLDER_ERROR_KEY = "beheer.princing.placeholder.error";
    private static final String SCALE_ERROR_KEY = "beheer.pricing.scale.error";

    public ActionForward save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        EntityManager em = getEntityManager();
        request.setAttribute("id", request.getParameter("id"));
        if (!isTokenValid(request)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        ActionErrors errors = dynaForm.validate(mapping, request);
        if (!errors.isEmpty()) {
            addMessages(request, errors);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, VALIDATION_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        Date validFrom = FormUtils.FormStringToDate(dynaForm.getString("validFrom"), null);
        Date validUntil = FormUtils.FormStringToDate(dynaForm.getString("validUntil"), null);
        if (validUntil != null && validFrom != null) {
            if (validUntil.before(validFrom)) {
                prepareMethod(dynaForm, request, EDIT, LIST);
                addAlternateMessage(mapping, request, START_END_ERROR_KEY);
                return getAlternateForward(mapping, request);
            }
        }
        LayerPricing lp = getLayerPricing(dynaForm, request, true);
        if (lp == null) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        lp.setValidFrom(validFrom);
        lp.setValidUntil(validUntil);
        Layer layer = getLayer(dynaForm, request);
        if (layer.getName() == null || layer.getName().trim().length() == 0) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, LAYER_PLACEHOLDER_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        lp.setServerProviderPrefix(layer.getSpAbbr());
        lp.setLayerName(layer.getName());
        lp.setPlanType(FormUtils.StringToInt(dynaForm.getString("planType")));
        String service = dynaForm.getString("service");
        String operation = null;
        if (service != null && service.equalsIgnoreCase("WMS")) {
            operation = dynaForm.getString("operationWMS");
        } else if (service != null && service.equalsIgnoreCase("WFS")) {
            operation = dynaForm.getString("operationWFS");
        } else {
            service = null;
        }
        if (operation != null && operation.trim().length() == 0) {
            operation = null;
        }
        lp.setService(service);
        lp.setOperation(operation);
        BigDecimal minScale = FormUtils.bdValueNull(dynaForm.getString("minScale"));
        BigDecimal maxScale = FormUtils.bdValueNull(dynaForm.getString("maxScale"));
        String projection = dynaForm.getString("projection");
        if (projection != null && projection.trim().length() == 0) {
            projection = null;
        }
        if (projection != null && (minScale != null || maxScale != null)) {
            boolean scaleOK = false;
            if (minScale != null && minScale.doubleValue() > 0) {
                if (maxScale != null && maxScale.doubleValue() > 0) {
                    if (maxScale.compareTo(minScale) > 0) {
                        scaleOK = true;
                    }
                }
            }
            if (!scaleOK) {
                prepareMethod(dynaForm, request, LIST, EDIT);
                addAlternateMessage(mapping, request, SCALE_ERROR_KEY);
                return getAlternateForward(mapping, request);
            }
            if (minScale != null) {
                lp.setMinScale(minScale.setScale(2, RoundingMode.HALF_UP));
            }
            if (maxScale != null) {
                lp.setMaxScale(maxScale.setScale(2, RoundingMode.HALF_UP));
            }
            lp.setProjection(projection);
        }
        BigDecimal unitPrice = FormUtils.bdValueNull(dynaForm.getString("unitPrice"));
        /* 
         * || door && vervangen. Price is namelijk verplicht en dus nooit null
         * en hij kwam dus altijd door de check. Ook als het bedrag 0 was.
         */
        if (unitPrice != null && unitPrice.doubleValue() > 0.0) {
            lp.setUnitPrice(unitPrice.setScale(2, RoundingMode.HALF_UP));
        } else {
            lp.setLayerIsFree(Boolean.TRUE);
        }
        lp.setIndexCount(UniqueIndex.createNextUnique(UniqueIndex.INDEX_LAYER_PRICING));
        em.persist(lp);
        getDataWarehousing().enlist(LayerPricing.class, lp.getId(), DwObjectAction.PERSIST_OR_MERGE);
        return super.save(mapping, dynaForm, request, response);
    }

    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        super.createLists(form, request);
        EntityManager em = getEntityManager();
        /*
         * Set the allowed projectsion
         */
        request.setAttribute("projections", KBConfiguration.SUPPORTED_PROJECTIONS);
        request.setAttribute("wmsRequests", KBConfiguration.ACCOUNTING_WMS_REQUESTS);
        request.setAttribute("wfsRequests", KBConfiguration.ACCOUNTING_WFS_REQUESTS);
        /*
         * Now set the default service to WMS - if appliable..
         */
        if (form.getString("service") == null || form.getString("service").trim().length() == 0) {
            form.set("service", new String("WMS"));
            form.set("operationWMS", new String("GetMap"));
        }
        Layer layer = getLayer(form, request);
        if (layer == null || layer.getName() == null || layer.getName().trim().length() == 0) {
            return;
        }
        String layerName = layer.getName();
        String spAbbr = layer.getSpAbbr();
        ServiceProvider sp = layer.getServiceProvider();
        request.setAttribute("spName", sp.getTitle());
        request.setAttribute("lName", layer.getName());
        /*
         * Now fetch the layerpricings that match with this layer.
         */
        request.setAttribute("layerPricings",
                em.createQuery(
                "FROM LayerPricing AS lp " +
                "WHERE lp.layerName = :layerName AND lp.serverProviderPrefix = :serverProviderPrefix AND lp.service = :service " +
                "ORDER BY  lp.deletionDate ASC, lp.creationDate DESC").setParameter("layerName", layer.getName()).setParameter("serverProviderPrefix", layer.getSpAbbr()).setParameter("service", "WMS").getResultList());
        /*
         * Then calculate all the different prices for all requesttypes..
         */
        LayerCalculator lc = new LayerCalculator(em);
        Object[][] tableData = new Object[KBConfiguration.ACCOUNTING_WMS_REQUESTS.length /*+ KBConfiguration.ACCOUNTING_WFS_REQUESTS.length*/][3];
        Date now = new Date();
        BigDecimal units = new BigDecimal(1);
        int totalWMSRequests = KBConfiguration.ACCOUNTING_WMS_REQUESTS.length;
        for (int i = 0; i < totalWMSRequests; i++) {
            tableData[i][0] = "WMS";
            tableData[i][1] = KBConfiguration.ACCOUNTING_WMS_REQUESTS[i];
            try {
                tableData[i][2] = lc.calculateLayerComplete(spAbbr, layerName, now, KBConfiguration.DEFAULT_PROJECTION, null, units, LayerPricing.PAY_PER_REQUEST, "WMS", KBConfiguration.ACCOUNTING_WMS_REQUESTS[i]);
            } catch (NoResultException nre) {
                tableData[i][2] = null;
            }
        }
        /*
         * Uitgezet omdat er een aparte pagina is voor WFS. Later kan het altijd nog samengevoegt worden
         * Er moet dan wel op gelet worden dat WFS layers anders zijn dan WMS layers en ook in aparte tabellen
         * zitten en aparte modelen hebben.
         */
        /*int totalWMFRequests = KBConfiguration.ACCOUNTING_WFS_REQUESTS.length;
        for (int i = 0; i < totalWMFRequests; i++) {
        tableData[i +totalWMSRequests ][0] = "WFS";
        tableData[i + totalWMSRequests][1] = KBConfiguration.ACCOUNTING_WFS_REQUESTS[i];
        try {
        tableData[i + totalWMSRequests][2] = lc.calculateLayerComplete(layer, now,  KBConfiguration.DEFAULT_PROJECTION, null, units,LayerPricing.PAY_PER_REQUEST, "WFS", KBConfiguration.ACCOUNTING_WFS_REQUESTS[i]);
        
        } catch (NoResultException nre) {
        tableData[i + totalWMSRequests][2] = null;
        }
        }*/
        request.setAttribute("tableData", tableData);
    }

    public JSONObject createTree() throws JSONException {
        JSONObject root = new JSONObject();
        root.put("name", "root");
        try {
            EntityManager em = getEntityManager();
            List serviceProviders = em.createQuery("from ServiceProvider sp order by sp.givenName").getResultList();
            JSONArray rootArray = new JSONArray();
            Iterator it = serviceProviders.iterator();
            while (it.hasNext()) {
                ServiceProvider sp = (ServiceProvider) it.next();
                JSONObject parentObj = this.serviceProviderToJSON(sp);
                Layer topLayer = sp.getTopLayer();
                if (topLayer != null) {
                    HashSet set = new HashSet();
                    set.add(topLayer);
                    parentObj = createTreeList(set, parentObj);
                    if (parentObj.has("children")) {
                        rootArray.put(parentObj);
                    }
                }
            }
            root.put("children", rootArray);
        } catch (Throwable e) {
            log.warn("Error creating EntityManager: ", e);
        }
        return root;
    }

    private JSONObject createTreeList(
            Set layers, JSONObject parent) throws JSONException {
        Iterator layerIterator = layers.iterator();
        JSONArray parentArray = new JSONArray();
        while (layerIterator.hasNext()) {
            Layer layer = (Layer) layerIterator.next();
            JSONObject layerObj = this.layerToJSON(layer);
            Set childLayers = layer.getLayers();
            if (childLayers != null && !childLayers.isEmpty()) {
                layerObj = createTreeList(childLayers, layerObj);
            }

            parentArray.put(layerObj);
        }

        if (parentArray.length() > 0) {
            parent.put("children", parentArray);
        }

        return parent;
    }

    private JSONObject serviceProviderToJSON(ServiceProvider serviceProvider) throws JSONException {
        JSONObject root = new JSONObject();
        root.put("id", serviceProvider.getId());
        root.put("name", serviceProvider.getGivenName());
        root.put("type", "serviceprovider");
        return root;
    }

    private JSONObject layerToJSON(Layer layer) throws JSONException {
        JSONObject jsonLayer = new JSONObject();
        jsonLayer.put("id", layer.getId());
        jsonLayer.put("name", layer.getTitle());
        jsonLayer.put("layerName", layer.getName());
        jsonLayer.put("type", "layer");
        return jsonLayer;
    }

    private Layer getLayer(DynaValidatorForm dynaForm, HttpServletRequest request) throws Exception {
        EntityManager em = getEntityManager();
        LayerPricing lp = null;
        Integer id = getLayerID(dynaForm);
        if (id == null) {
            return null;
        }
        return (Layer) em.find(Layer.class, id);
    }
}