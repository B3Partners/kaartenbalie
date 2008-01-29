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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.struts.ExtendedMethodProperties;
import nl.b3p.kaartenbalie.core.server.UniqueIndex;
import nl.b3p.kaartenbalie.core.server.accounting.LayerCalculator;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPricing;
import nl.b3p.kaartenbalie.core.server.datawarehousing.DwObjectAction;
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
public class PricingAction extends KaartenbalieCrudAction {
    protected static final String DOWNSIZE                        = "downsize";
    
    public static SimpleDateFormat pricingDate = new SimpleDateFormat("yyyy-MM-dd");
    
    protected Map getActionMethodPropertiesMap() {
        Map map = super.getActionMethodPropertiesMap();
        ExtendedMethodProperties crudProp = new ExtendedMethodProperties(DOWNSIZE);
        crudProp.setDefaultForwardName(SUCCESS);
        crudProp.setDefaultMessageKey("beheer.pricing.downsize.succes");
        crudProp.setAlternateForwardName(FAILURE);
        crudProp.setAlternateMessageKey("beheer.pricing.downsize.failed");
        map.put(DOWNSIZE, crudProp);
        return map;
    }
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject root = this.createTree();
        request.setAttribute("layerList", root);
        request.setAttribute("id", request.getParameter("id"));
        return super.unspecified(mapping, dynaForm, request, response);
    }
    
    public ActionForward edit(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("id", request.getParameter("id"));
        return super.edit(mapping, dynaForm, request, response);
    }
    
    public ActionForward downsize(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws HibernateException, Exception {
        request.setAttribute("id", request.getParameter("id"));
        String strLevel = request.getParameter("level");
        request.setAttribute("level", strLevel);
        int level = 0;
        try {
            level = Integer.parseInt(strLevel);
        } catch (Exception e) {
        }
        
        String strDetails = request.getParameter("details");
        boolean details = true;
        if (strDetails != null) {
            details = strDetails.trim().equalsIgnoreCase("true");
        }
        request.setAttribute("details", new Boolean(details));
        EntityManager em = getEntityManager();
        String idString = (String) request.getAttribute("id");
        if (idString != null && idString.length() > 0) {
            Integer layerId = new Integer(Integer.parseInt(idString));
            Layer layer = (Layer) em.find(Layer.class, layerId);
            LayerCalculator lc = new LayerCalculator();
            try {
                //lc.calculateLayer();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lc.closeEntityManager();
            }
            
            
            
            //request.setAttribute("downsize", LayerCalculator.downSize(layer, LayerPricing.PAY_PER_REQUEST, em, level,details, new Date()));
        }
        return super.edit(mapping, dynaForm, request, response);
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
        
        String idString = (String) request.getAttribute("id");
        
        if (idString != null && idString.length() > 0) {
            
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
            LayerPricing lp = new LayerPricing();
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
            em.persist(lp);
            getDataWarehousing().enlist(LayerPricing.class, lp.getId(), DwObjectAction.PERSIST_OR_MERGE);
            
        }
        
        return super.save(mapping, dynaForm, request, response);
    }
    
    
    
    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        super.createLists(form, request);
        
        EntityManager em = getEntityManager();
        String idString = (String) request.getAttribute("id");
        if (idString != null && idString.length() > 0) {
            Integer layerId = new Integer(Integer.parseInt(idString));
            Layer layer = (Layer) em.find(Layer.class, layerId);
            ServiceProvider sp = layer.getServiceProvider();
            request.setAttribute("spName", sp.getTitle());
            request.setAttribute("lName", layer.getName());
            
            //request.setAttribute("priceRequestSingle", LayerCalculator.calculateLayerPrice(layer, LayerPricing.getPAY_PER_REQUEST(), new BigDecimal(1), em, new Date()));
            //request.setAttribute("priceRequestCascade", LayerCalculator.calculateCompleteLayerPrice(layer, LayerPricing.getPAY_PER_REQUEST(), new BigDecimal(1), em,new Date()));
            
            request.setAttribute("layerPricings",
                    em.createQuery(
                    "FROM LayerPricing AS lp " +
                    "WHERE lp.layerName = :layerName AND lp.serverProviderPrefix = :serverProviderPrefix ORDER BY  lp.deletionDate ASC, lp.creationDate DESC")
                    .setParameter("layerName", layer.getName())
                    .setParameter("serverProviderPrefix", layer.getSpAbbr())
                    .getResultList());
            LayerCalculator lc = new LayerCalculator(em);
            request.setAttribute("aggregateLayerPricings", new Boolean(LayerCalculator.aggregateLayerPricings));
            if (!LayerCalculator.aggregateLayerPricings) {
                try {
                    request.setAttribute("activePricing", lc.getActiveLayerPricing(layer.getName(),layer.getSpAbbr(), new Date(), new BigDecimal(1), LayerPricing.PAY_PER_REQUEST));
                } catch (NoResultException nre) {
                    nre.printStackTrace();
                }
            }
        }
        
    }
    
    
    
    private JSONObject createTree() throws JSONException {
        EntityManager em = getEntityManager();
        List serviceProviders = em.createQuery("from ServiceProvider sp order by sp.name").setMaxResults(1).getResultList();
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
        jsonLayer.put("type", "layer");
        return jsonLayer;
    }
    
    
    
}
