/*
 * AccountingAction.java
 *
 * Created on November 19, 2007, 9:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.struts;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import nl.b3p.commons.struts.ExtendedMethodProperties;
import nl.b3p.kaartenbalie.core.server.UniqueIndex;
import nl.b3p.kaartenbalie.core.server.accounting.LayerCalculator;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPricing;
import nl.b3p.kaartenbalie.core.server.datawarehousing.DwObjectAction;
import nl.b3p.ogc.utils.KBConstants;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.ServiceProvider;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import org.hibernate.HibernateException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Chris Kramer
 */
public class PricingAction extends KaartenbalieCrudAction implements KBConstants {
    
    private static final String DETAILS = "details";
    public static SimpleDateFormat pricingDate = new SimpleDateFormat("yyyy-MM-dd");
    
    protected Map getActionMethodPropertiesMap() {
        Map map = super.getActionMethodPropertiesMap();
        ExtendedMethodProperties crudProp = new ExtendedMethodProperties(DETAILS);
        crudProp.setDefaultForwardName(SUCCESS);
        crudProp.setDefaultMessageKey("beheer.reporting.details.succes");
        crudProp.setAlternateForwardName(FAILURE);
        crudProp.setAlternateMessageKey("beheer.reporting.details.failed");
        map.put(DETAILS, crudProp);
        return map;
    }
    
    
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("id", request.getParameter("id"));
        return super.unspecified(mapping, dynaForm, request, response);
    }
    
    public ActionForward edit(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("id", request.getParameter("id"));
        return super.edit(mapping, dynaForm, request, response);
    }
    public ActionForward details(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws HibernateException, Exception {
        return super.unspecified(mapping, dynaForm, request, response);
    }
    
    public ActionForward delete(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws HibernateException, Exception {
        EntityManager em = getEntityManager();
        request.setAttribute("id", request.getParameter("id"));
        String strPricingId = request.getParameter("pricingid");
        if (strPricingId != null && strPricingId.trim().length() > 0) {
            strPricingId = strPricingId.trim();
            Integer pricingId = new Integer(Integer.parseInt(strPricingId));
            LayerPricing lp =(LayerPricing) em.find(LayerPricing.class, pricingId);
            if (lp.getDeletionDate() != null) {
                throw new Exception("Trying to delete an already deleted LayerPricing");
            }
            lp.setDeletionDate(new Date());
        }
        return  super.delete(mapping, dynaForm, request, response);
    }
    
    
    public ActionForward save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        EntityManager em = getEntityManager();
        request.setAttribute("id", request.getParameter("id"));
        if (!isTokenValid(request)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        ActionErrors errors = dynaForm.validate(mapping, request);
        if(!errors.isEmpty()) {
            addMessages(request, errors);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, VALIDATION_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        Integer planType = (Integer) dynaForm.get("planType");
        Date validFrom = null;
        try {
            validFrom =  pricingDate.parse(dynaForm.getString("validFrom"));
        } catch (Exception e) {
        }
        Date validUntil = null;
        try {
            validUntil =  pricingDate.parse(dynaForm.getString("validUntil"));
        } catch (Exception e) {
        }
        
        if (validUntil != null && validFrom != null) {
            if (validUntil.before(validFrom)) {
                throw new Exception("Enddate cannot be before startdate.");
            }
        }
        
        
        
        String idString = (String) request.getAttribute("id");
        
        
        if (idString != null && idString.trim().length() > 0) {
            
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
            
            Boolean layerIsFree = (Boolean) dynaForm.get("layerIsFree");
            Double unitPrice = null;
            if (layerIsFree == null) {
                layerIsFree = new Boolean(false);
            }
            
            if (layerIsFree.booleanValue() == false) {
                unitPrice = (Double) dynaForm.get("unitPrice");
            }
            
            Integer layerId = new Integer(Integer.parseInt(idString));
            Layer layer = (Layer) em.find(Layer.class, layerId);
            if (layer.getName() == null || layer.getName().trim().length() == 0) {
                throw new Exception("Requested layer is not able to register pricinginformation...");
            }
            LayerPricing lp = new LayerPricing();
            
            Double minScale = (Double) dynaForm.get("minScale");
            Double maxScale = (Double) dynaForm.get("maxScale");
            
            if (minScale != null && maxScale != null && minScale.doubleValue() > 0 && maxScale.doubleValue() > 0 ) {
                if (maxScale.compareTo(minScale) < 0) {
                    throw new Exception("maxScale should always be larger then the minScale.");
                }
            }
            
            if (minScale != null) {
                if (minScale.doubleValue() < 0) {
                    throw new Exception("minScale cannot be a negative value!");
                } else if (minScale.doubleValue()> 0) {
                    lp.setMinScale(new BigDecimal(minScale.doubleValue()));
                }
            }
            
            if (maxScale != null) {
                if (maxScale.doubleValue() < 0) {
                    throw new Exception("maxScale cannot be a negative value!");
                } else if (maxScale.doubleValue() > 0) {
                    lp.setMaxScale(new BigDecimal(maxScale.doubleValue()));
                }
            }
            
            
            lp.setPlanType(planType.intValue());
            Calendar cal = Calendar.getInstance();
            if (validFrom != null) {
                cal.setTime(validFrom);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                lp.setValidFrom(cal.getTime());
            }
            if (validUntil != null) {
                cal.setTime(validUntil);
                cal.set(Calendar.HOUR_OF_DAY,23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.MILLISECOND, 99);
                lp.setValidUntil(cal.getTime());
            }
            if (unitPrice != null) {
                lp.setUnitPrice(new BigDecimal(unitPrice.doubleValue()));
            } else {
                lp.setLayerIsFree(layerIsFree);
            }
            lp.setServerProviderPrefix(layer.getSpAbbr());
            lp.setLayerName(layer.getName());
            lp.setIndexCount(UniqueIndex.createNextUnique(UniqueIndex.INDEX_LAYER_PRICING));
            lp.setService(service);
            lp.setOperation(operation);
            em.persist(lp);
            getDataWarehousing().enlist(LayerPricing.class, lp.getId(), DwObjectAction.PERSIST_OR_MERGE);
            
        }
        
        request.setAttribute("gotoTab", new String("details"));
        return super.save(mapping, dynaForm, request, response);
    }
    
    
    
    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        super.createLists(form, request);
        HttpSession session = request.getSession();
        EntityManager em = getEntityManager();
        String idString = (String) request.getAttribute("id");
        if (idString != null && idString.length() > 0) {
            String summary = request.getParameter("summary");
            if (summary != null && summary.trim().length() > 0) {
                session.setAttribute("summary", summary);
            } else {
                summary = (String) session.getAttribute("summary");
            }
            request.setAttribute("summary", summary);
            
            /*
             * First load the possible WMS and WFS requests..
             */
            request.setAttribute("wmsRequests",ACCOUNTING_WMS_REQUESTS);
            request.setAttribute("wfsRequests",ACCOUNTING_WFS_REQUESTS);
            
            
            /*
             * Now set the default service to WMS - if appliable..
             */
            if (form.getString("service") == null || form.getString("service").trim().length() == 0) {
                form.set("service", new String("WMS"));
                form.set("operationWMS", new String("GetMap"));
            }
            
            /*
             * Now gather layerinformation...
             */
            Integer layerId = new Integer(Integer.parseInt(idString));
            Layer layer = (Layer) em.find(Layer.class, layerId);
            if (layer.getName() == null || layer.getName().trim().length() == 0) {
                request.setAttribute("id", null);
                throw new Exception("Requested layer is not able to register pricinginformation. It is probably a folder!");
            }
            ServiceProvider sp = layer.getServiceProvider();
            request.setAttribute("spName", sp.getTitle());
            request.setAttribute("lName", layer.getName());
            /*
             * Now fetch the layerpricings that match with this layer.
             */
            request.setAttribute("layerPricings",
                    em.createQuery(
                    "FROM LayerPricing AS lp " +
                    "WHERE lp.layerName = :layerName AND lp.serverProviderPrefix = :serverProviderPrefix ORDER BY  lp.deletionDate ASC, lp.creationDate DESC")
                    .setParameter("layerName", layer.getName())
                    .setParameter("serverProviderPrefix", layer.getSpAbbr())
                    .getResultList());
            /*
             * Then calculate all the different prizes for all requesttypes..
             */
            LayerCalculator lc = new LayerCalculator(em);
            
            if (summary != null && summary.equalsIgnoreCase("true")) {
                Object[][] tableData = new Object[ACCOUNTING_WMS_REQUESTS.length + ACCOUNTING_WFS_REQUESTS.length][3];
                
                Map activePricingData = new HashMap();
                Date now = new Date();
                
                BigDecimal units  = new BigDecimal(1);
                int totalWMSRequests = ACCOUNTING_WMS_REQUESTS.length;
                for (int i = 0; i < totalWMSRequests; i++) {
                    
                    tableData[i][0] = "WMS";
                    tableData[i][1] = ACCOUNTING_WMS_REQUESTS[i];
                    try {
                        tableData[i][2] = lc.calculateLayerComplete(layer, now, null, units, LayerPricing.PAY_PER_REQUEST, "WMS", ACCOUNTING_WMS_REQUESTS[i]);
                    } catch (NoResultException nre) {
                        tableData[i][2] = null;
                    }
                    try {
                        LayerPricing lp = lc.getActiveLayerPricing(layer, now, null, LayerPricing.PAY_PER_REQUEST, "WMS", ACCOUNTING_WMS_REQUESTS[i]);
                        if(lp != null) {
                            Map someData = (Map) activePricingData.get(lp.getId());
                            if (someData == null) {
                                someData = new HashMap();
                                activePricingData.put(lp.getId(), someData);
                            }
                            someData.put("WMS_" + ACCOUNTING_WMS_REQUESTS[i], null);
                            
                        }
                    } catch (NoResultException nre) {
                    }
                    
                }
                int totalWMFRequests = ACCOUNTING_WFS_REQUESTS.length;
                for (int i = 0; i < totalWMFRequests; i++) {
                    tableData[i +totalWMSRequests ][0] = "WFS";
                    tableData[i + totalWMSRequests][1] = ACCOUNTING_WFS_REQUESTS[i];
                    try {
                        tableData[i + totalWMSRequests][2] = lc.calculateLayerComplete(layer, now,  null, units,LayerPricing.PAY_PER_REQUEST, "WFS", ACCOUNTING_WFS_REQUESTS[i]);
                        
                    } catch (NoResultException nre) {
                        tableData[i + totalWMSRequests][2] = null;
                    }
                    try {
                        activePricingData.put("WFS_" + ACCOUNTING_WFS_REQUESTS[i], lc.getActiveLayerPricing(layer, now, null, LayerPricing.PAY_PER_REQUEST, "WFS", ACCOUNTING_WFS_REQUESTS[i]));
                    } catch (NoResultException nre) {
                    }
                }
                request.setAttribute("tableData",tableData);
                request.setAttribute("activePricingData",activePricingData);
            }
        } else {
            JSONObject root = this.createTree();
            request.setAttribute("layerList", root);
        }
        
    }
    
    
    
    private JSONObject createTree() throws JSONException {
        EntityManager em = getEntityManager();
        List serviceProviders = em.createQuery("from ServiceProvider sp order by sp.title ASC").getResultList();
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
                parentObj = createTreeList(set, parentObj);
                if (parentObj.has("children")){
                    rootArray.put(parentObj);
                }
            }
        }
        root.put("name","root");
        root.put("children", rootArray);
        return root;
    }
    
    
    private JSONObject createTreeList(Set layers, JSONObject parent) throws JSONException {
        Iterator layerIterator = layers.iterator();
        JSONArray parentArray = new JSONArray();
        while (layerIterator.hasNext()) {
            Layer layer = (Layer)layerIterator.next();
            JSONObject layerObj = this.layerToJSON(layer);
            Set childLayers = layer.getLayers();
            if (childLayers != null && !childLayers.isEmpty()) {
                layerObj = createTreeList(childLayers, layerObj);
            }
            parentArray.put(layerObj);
        }
        if (parentArray.length() > 0){
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
    
    private JSONObject layerToJSON(Layer layer) throws JSONException{
        JSONObject jsonLayer = new JSONObject();
        jsonLayer.put("id", layer.getId());
        jsonLayer.put("name", layer.getTitle());
        jsonLayer.put("layerName", layer.getName());
        jsonLayer.put("type", "layer");
        return jsonLayer;
    }
    
    
    
}
