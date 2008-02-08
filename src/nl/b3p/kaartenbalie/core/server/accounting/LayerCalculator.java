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
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPriceComposition;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPricing;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.wms.capabilities.Layer;


/**
 *
 * @author Chris Kramer
 */
public class LayerCalculator {
    
    
    private EntityManager em;
    private boolean externalEm = false;
    public static final boolean aggregateLayerPricings = false;
    
    public LayerCalculator() {
        em = MyEMFDatabase.createEntityManager();
        
    }
    public LayerCalculator(EntityManager em) {
        this.em = em;
        externalEm = true;
    }
    
    public LayerPriceComposition calculateLayerComplete(Integer layerId, Date validationDate, BigDecimal units, int planType, String service, String operation) throws Exception {
        Layer layer = (Layer) em.find(Layer.class, layerId);
        return calculateLayerComplete(layer, validationDate, units, planType, service, operation);
    }
    
    public LayerPriceComposition calculateLayerComplete(Layer layer, Date validationDate, BigDecimal units, int planType, String service, String operation) throws Exception {
        long startTime = System.currentTimeMillis();
        if (!AccountManager.isEnableAccounting()) {
            return null;
        }
        /*
         * Start van het traject voor het opvragen van een prijs..
         */
        LayerPriceComposition tLC = new LayerPriceComposition(layer.getSpAbbr(), layer.getName(), validationDate, planType, units, service, operation);
        BigDecimal layerPrice = null;
        try {
            layerPrice = calculateLayer(layer, validationDate, units, planType, service, operation);
            tLC.setMethod(LayerPriceComposition.METHOD_OWN);
        } catch(NoPrizingException npe) {
            /*
             * Begin zoektocht naar alternatieven!
             * Zoek bij de parentlayers voor een prijs...
             */
            try {
                layerPrice = calculateParentLayer(layer, validationDate, units,  planType, service, operation);
                tLC.setMethod(LayerPriceComposition.METHOD_PARENTS);
            } catch (NoPrizingException npeParent) {
                 /*
                  * Er kon geen prijs worden bepaald via de parents... Zoek bij de childs...
                  */
                try {
                    layerPrice = calculateChildLayers(layer, validationDate, units,  planType, service, operation);
                    tLC.setMethod(LayerPriceComposition.METHOD_CHILDS);
                } catch (NoPrizingException npeChilds) {
                    tLC.setMethod(LayerPriceComposition.METHOD_NONE);
                     /*
                      * Er kon geen prijs worden bepaald via de childs... Kaart is gratis :S
                      */
                }
            }
        }
        tLC.setLayerIsFree(new Boolean(layerPrice == null));
        if (layerPrice == null) {
            tLC.setLayerPrice(new BigDecimal(0));
        } else {
            tLC.setLayerPrice(layerPrice);
        }
        tLC.setCalculationTime(System.currentTimeMillis() - startTime);
        return tLC;
    }
    
    
    private BigDecimal addToLayerPrice(BigDecimal returnValue, BigDecimal addValue) {
        if  (addValue != null) {
            if (returnValue == null) {
                returnValue = new BigDecimal(0);
            }
            returnValue = returnValue.add(addValue);
        }
        
        return returnValue;
    }
    
    private BigDecimal calculateChildLayers(Layer layer, Date validationDate, BigDecimal units, int planType, String service, String operation) throws Exception{
         /*
          * "Controleer of " + layer.getName() + " childlayers heeft.");
          */
        BigDecimal layerPrice = null;
        
        Set childLayers = layer.getLayers();
        
        if (childLayers != null && childLayers.size() > 0) {
            /*
             * Er zijn " + childLayers.size() + " childlayers die we nu gaan opvragen...");
             */
            Iterator iterChilds = childLayers.iterator();
            while (iterChilds.hasNext()) {
                Layer childLayer = (Layer) iterChilds.next();
                boolean hasNoPrice = false;
                BigDecimal thisLayerPrice = null;
                try {
                    layerPrice = addToLayerPrice(layerPrice, calculateLayer( childLayer,  validationDate,  units,  planType, service, operation));
                } catch (NoPrizingException npe) {
                    /*
                     * Geen prijs gevonden, zoek bij de childs van " + layer.getName() + ".");
                     */
                    try {
                        layerPrice = addToLayerPrice(layerPrice, calculateChildLayers( childLayer,  validationDate,  units,  planType, service, operation));
                    } catch (NoPrizingException npe2) {
                        
                    }
                }
                
            }
        } else {
            /*
             * Er zijn geen childlayers...
             */
            throw new NoPrizingException("Geen childlayers meer om prijzen voor te zoeken.");
        }
        /*
         * Prijs wordt teruggegen.
         */
        return layerPrice;
    }
    private BigDecimal calculateParentLayer(Layer layer, Date validationDate, BigDecimal units, int planType, String service, String operation) throws Exception{
        /*
         * Controleer of " + layer.getName() + " een parentlayer heeft.
         */
        BigDecimal layerPrice = null;
        Layer parentLayer = layer.getParent();
        if (parentLayer != null) {
           /*
            * parent layer is != null");
            */
            try {
                layerPrice = calculateLayer(parentLayer, validationDate, units, planType, service, operation);
            } catch (NoPrizingException npe) {
               /*
                * No prizing info available...
                */
                layerPrice = calculateParentLayer(parentLayer, validationDate, units, planType, service, operation);
            }
        } else {
            /*
             * Er is geen parentlayer (meer) en dus ook geen prijs info.....
             */
            throw new NoPrizingException();
        }
        return layerPrice;
        
    }
    public LayerPricing getActiveLayerPricing(Layer layer, Date validationDate, int planType, String service, String operation) throws Exception{
        if (aggregateLayerPricings) {
            throw new Exception("Not possible in aggregationmode..");
        }
        return (LayerPricing) em.createQuery(
                "FROM LayerPricing AS lp " +
                "WHERE (lp.deletionDate IS null OR lp.deletionDate > :validationDate) " +
                "AND lp.planType = :planType " +
                "AND lp.layerName = :layerName " +
                "AND lp.serverProviderPrefix = :serverProviderPrefix " +
                "AND lp.creationDate <= :validationDate " +
                "AND (lp.validFrom <= :validationDate OR lp.validFrom IS null) " +
                "AND (lp.validUntil > :validationDate OR lp.validUntil IS null) " +
                "AND (lp.validUntil > :validationDate OR lp.validUntil IS null) " +
                "AND (lp.service = :service OR lp.service IS null) " +
                "AND (lp.operation = :operation OR lp.operation IS null) " +
                "ORDER BY lp.indexCount DESC")
                .setParameter("layerName",layer.getName())
                .setParameter("serverProviderPrefix",layer.getSpAbbr())
                .setParameter("validationDate",validationDate)
                .setParameter("planType",new Integer(planType))
                .setParameter("operation",operation)
                .setParameter("service",service)
                .setMaxResults(1)
                .getSingleResult();
    }
    
    
    
    
    public BigDecimal calculateLayer(Layer layer, Date validationDate, BigDecimal units, int planType, String service, String operation) throws Exception {
        /*
         *  This function can return four different states/values.
         *  1: NoPrizingException; there is nothing defined for this layer.
         *  2: null; this layer is free!
         *  3: 0.00; For some reason the total sum of layerprizings is less then or equal to 0.
         *  4: >0.00; the actual cost of this layer...
         */
        /*
         * Check the input parameters!
         */
        if (layer == null) {
            throw new Exception("Layer is required!");
        }
        if (validationDate == null) {
            throw new Exception("ValidationDate is a required field!");
        }
        if (units == null) {
            throw new Exception("Units is a required field!");
        }
        if (planType <= 0) {
            throw new Exception("PlanType is a required field!");
        }
        /*
         * If the layer has no name, it's a placeholder.. No need to query...
         */
        if (layer.getName() == null || layer.getName().trim().length()==0) {
            throw new NoPrizingException("Layer is a placeholder and therefor cannot hold pricingInformation.");
        }
        /*
         * De prijs voor " + layer.getSpAbbr() + "_" + layer.getName() + " wordt opgevraagd...
         */
        BigDecimal layerPrice = null;
        if (aggregateLayerPricings) {
            Object[] resultSet = (Object[]) em.createQuery(
                    "SELECT COUNT(*) AS lines, SUM(lp.unitPrice * :units) AS layerPrice, SUM(lp.layerIsFree) AS layerIsFree " +
                    "FROM LayerPricing AS lp " +
                    "WHERE (lp.deletionDate IS null OR  lp.deletionDate > :validationDate) " +
                    "AND lp.planType = :planType AND lp.layerName = :layerName " +
                    "AND lp.serverProviderPrefix = :serverProviderPrefix " +
                    "AND lp.creationDate < :validationDate " +
                    "AND (lp.validFrom < :validationDate OR lp.validFrom IS null) " +
                    "AND (lp.validUntil > :validationDate OR lp.validUntil IS null) " +
                    "AND (lp.service = :service OR lp.service IS null) " +
                    "AND (lp.operation = :operation OR lp.operation IS null) " +
                    "ORDER BY lp.indexCount DESC")
                    .setParameter("layerName",layer.getName())
                    .setParameter("serverProviderPrefix",layer.getSpAbbr())
                    .setParameter("validationDate",validationDate)
                    .setParameter("units",units)
                    .setParameter("planType",new Integer(planType))
                    .setParameter("operation",operation)
                    .setParameter("service",service)
                    .getSingleResult();
            
            Long pricingLines = (Long) resultSet[0];
            /*
             * If there are no lines, thrown an exception!
             */
            if (pricingLines.longValue() == 0) {
                /*
                 * layer.getSpAbbr() + "_" + layer.getName() + " bevat geen prijsinformatie.");
                 */
                throw new NoPrizingException();
            }
            
            /*
             * Check if the layer is free, if it's not, then set the layerPrice..
             */
            Boolean layerIsFree = (Boolean) resultSet[2];
            
            
            if (layerIsFree == null || (layerIsFree != null && !layerIsFree.booleanValue())) {
                layerPrice = (BigDecimal)resultSet[1];
            }
        } else {
            try {
                
                LayerPricing layerPricing = getActiveLayerPricing(layer, validationDate, planType, service, operation);
                System.out.println(layerPricing);
                if (layerPricing.getUnitPrice() != null &&
                        (layerPricing.getLayerIsFree() == null  || (layerPricing.getLayerIsFree() != null && layerPricing.getLayerIsFree().booleanValue() == false))) {
                    
                    layerPrice = layerPricing.getUnitPrice().multiply(units);
                }
                /*
                 * layer.getSpAbbr() + "_" + layer.getName() + " bevat prijsinformatie, prijs: " + layerPrice);
                 */
            } catch (NoResultException nre) {
                /*
                 * layer.getSpAbbr() + "_" + layer.getName() + " bevat geen prijsinformatie.");
                 */
                throw new NoPrizingException(nre.getMessage());
            }
            
        }
        /*
         * Final check!
         */
        if (layerPrice != null && layerPrice.compareTo(new BigDecimal(0)) < 0) {
            layerPrice = new BigDecimal(0);
        }
        return layerPrice;
        
    }
    
    public void closeEntityManager() throws Exception {
        if (externalEm) {
            throw new Exception("Trying to close an external EntityManager. This is not the place to do it!");
        }
        em.close();
    }
    
    
}
