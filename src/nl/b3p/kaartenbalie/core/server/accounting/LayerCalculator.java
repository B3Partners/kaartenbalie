/*
 * LayerCalculator.java
 *
 * Created on December 24, 2007, 1:52 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.accounting;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import javax.persistence.EntityManager;
import nl.b3p.wms.capabilities.Layer;


/**
 *
 * @author Chris Kramer
 */
public class LayerCalculator {
    
    
    
    public static BigDecimal calculateCompleteLayerPrice(Layer layer, int planType, BigDecimal units, EntityManager em, Date pricingDate) {
        BigDecimal childLayersPrice = calculateChildLayersPrice(layer, planType, units, em, pricingDate);
        BigDecimal layerPrice = calculateLayerPrice(layer, planType, units, em, pricingDate);
        
        
        BigDecimal totalPrice = layerPrice.add(childLayersPrice);
        if (totalPrice.doubleValue() < 0) {
            totalPrice =  new BigDecimal(0);
        }
        return totalPrice;
    }
    
    public static BigDecimal calculateLayerPrice(Layer layer, int planType, BigDecimal units, EntityManager em, Date pricingDate) {
        BigDecimal layerPrice  = (BigDecimal) em.createQuery(
                "SELECT SUM(unitPrice * :units) " +
                "FROM LayerPricing AS lp " +
                "WHERE lp.layer = :layer " +
                "AND lp.planType = :planType " +
                "AND (lp.validFrom < :currentDate OR lp.validFrom is null) " +
                "AND (lp.validUntil > :currentDate OR lp.validUntil is null)")
                .setParameter("layer", layer)
                .setParameter("units", units)
                .setParameter("planType", new Integer(planType))
                .setParameter("currentDate", pricingDate)
                .getSingleResult();
        
        
        if (layerPrice == null) {
            layerPrice =  new BigDecimal(0);
        }
        return layerPrice;
    }
    
    public static BigDecimal calculateChildLayersPrice(Layer layer, int planType, BigDecimal units, EntityManager em, Date pricingDate) {
        BigDecimal layerPrice  = new BigDecimal(0);
        if (layer.getLayers() != null) {
            Iterator layerIter = layer.getLayers().iterator();
            while (layerIter.hasNext()) {
                Layer subLayer = (Layer) layerIter.next();
                layerPrice = layerPrice.add(calculateLayerPrice(subLayer, planType, units, em, pricingDate));
                layerPrice = layerPrice.add(calculateChildLayersPrice(subLayer, planType, units, em, pricingDate));
            }
        }
        if (layerPrice.doubleValue() < 0) {
            layerPrice =  new BigDecimal(0);
        }
        return layerPrice;
    }
    
    
    public static DownsizeWrapper downSize(Layer layer, int planType, EntityManager em, int maxLevels, boolean details, Date pricingDate) {
        DownsizeWrapper dw = new DownsizeWrapper(layer);
        dw.setLayerPriceGross(calculateChildLayersPrice(layer, planType,new BigDecimal(1.0),em, pricingDate));
        dw.setLayerPriceNet(calculateCompleteLayerPrice(layer, planType,new BigDecimal(1.0),em, pricingDate));
        if (details) {
            dw.setPricingPlans(em.createQuery(
                    "FROM LayerPricing AS lp " +
                    "WHERE lp.layer = :layer " +
                    "AND lp.planType = :planType " +
                    "AND (lp.validFrom < :currentDate OR lp.validFrom is null) " +
                    "AND (lp.validUntil > :currentDate OR lp.validUntil is null)")
                    .setParameter("layer", layer)
                    .setParameter("planType", new Integer(planType))
                    .setParameter("currentDate", pricingDate)
                    .getResultList());
        }
        if (maxLevels != 0) {
            Iterator layerIter = layer.getLayers().iterator();
            while (layerIter.hasNext()) {
                Layer childLayer = (Layer) layerIter.next();
                dw.getChildWrappers().add(downSize(childLayer, planType, em,maxLevels -1,details, pricingDate));
            }
        }
        return dw;
    }
    
    
    
}
