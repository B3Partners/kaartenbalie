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
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPriceComposition;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPricing;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.ogc.wfs.v110.WfsLayer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * @author Chris Kramer
 */
public class WfsLayerCalculator {
    
    private static final Log log = LogFactory.getLog(LayerCalculator.class);
    
    private EntityManager em;
    private boolean externalEm = false;
    
    
    public WfsLayerCalculator() {
        em = MyEMFDatabase.createEntityManager();
        
    }
    public WfsLayerCalculator(EntityManager em) {
        this.em = em;
        externalEm = true;
    }
    
    public LayerPriceComposition calculateLayerComplete(Integer layerId, Date validationDate, String projection,  BigDecimal scale, BigDecimal units, int planType, String service, String operation) throws Exception {
        WfsLayer layer = (WfsLayer) em.find(WfsLayer.class, layerId);
        return calculateLayerComplete(layer, validationDate, projection, scale, units, planType, service, operation);
    }
    
    public LayerPriceComposition calculateLayerComplete(WfsLayer layer, Date validationDate, String projection, BigDecimal scale,BigDecimal units, int planType, String service, String operation) throws Exception {
        long startTime = System.currentTimeMillis();
        if (!AccountManager.isEnableAccounting()) {
            return null;
        }
        /*
         * Start van het traject voor het opvragen van een prijs..
         */
        LayerPriceComposition tLC = new LayerPriceComposition(layer.getSpAbbr(), layer.getName(), validationDate, scale, projection,  planType, units, service, operation);
        BigDecimal layerPrice = null;
        try {
            try {
                layerPrice = calculateLayer(layer, validationDate,projection,scale, units, planType, service, operation);
                tLC.setMethod(LayerPriceComposition.METHOD_OWN);
            } catch(NoPrizingException npe) {
                
            }
        } catch (LayerNotAvailableException lnae) {
            tLC.setMethod(LayerPriceComposition.METHOD_BLOCKED);
        }
        tLC.setLayerIsFree(new Boolean(layerPrice == null));
        if (layerPrice == null) {
            tLC.setLayerPrice(new BigDecimal(0));
        } else {
            tLC.setLayerPrice(layerPrice);
        }
        tLC.setService("WFS");
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
    
     public List getSpLayerPricingList(String spabbr, Date validationDate) throws Exception{
        return em.createQuery(
                "FROM LayerPricing AS lp " +
                "WHERE (lp.deletionDate IS null OR lp.deletionDate > :validationDate) " +
                "AND lp.serverProviderPrefix = :serverProviderPrefix " +
                "AND lp.creationDate <= :validationDate " +
                "AND (lp.validFrom <= :validationDate OR lp.validFrom IS null) " +
                "AND (lp.validUntil >= :validationDate OR lp.validUntil IS null) " +
                "ORDER BY lp.indexCount DESC")
                .setParameter("serverProviderPrefix",spabbr)
                .setParameter("validationDate",validationDate)
                .getResultList();
    }
    
   
    private List getActiveLayerPricingList(WfsLayer layer, Date validationDate, int planType, String service, String operation) throws Exception{
        return em.createQuery(
                "FROM LayerPricing AS lp " +
                "WHERE (lp.deletionDate IS null OR lp.deletionDate > :validationDate) " +
                "AND lp.planType = :planType " +
                "AND lp.layerName = :layerName " +
                "AND lp.serverProviderPrefix = :serverProviderPrefix " +
                "AND lp.creationDate <= :validationDate " +
                "AND (lp.validFrom <= :validationDate OR lp.validFrom IS null) " +
                "AND (lp.validUntil >= :validationDate OR lp.validUntil IS null) " +
                "AND (lp.service = :service OR lp.service IS null) " +
                "AND (lp.operation = :operation OR lp.operation IS null) " +
                "ORDER BY lp.indexCount DESC")
                .setParameter("layerName",layer.getName())
                .setParameter("serverProviderPrefix",layer.getSpAbbr())
                .setParameter("validationDate",validationDate)
                .setParameter("planType",new Integer(planType))
                .setParameter("operation",operation)
                .setParameter("service",service)
                .getResultList();
    }
    
    public boolean ActiveLayerPricingExists(WfsLayer layer, Date validationDate, int planType, String service, String operation) {
        try {
            List possibleLayerPricings = getActiveLayerPricingList(layer, validationDate, planType, service, operation);
            if (possibleLayerPricings==null || possibleLayerPricings.size()==0)
                return false;
            return true;
        } catch (Exception e) {
            log.error("Error collecting layer pricings: ", e);
            return false;
        }
    }
    
    public LayerPricing getActiveLayerPricing(WfsLayer layer, Date validationDate,  String projection,  BigDecimal scale, int planType, String service, String operation) throws Exception{
        
        /*
         * Todo mag projection nie null zijn voor WFS?
         */
        /*if (projection == null) {
            log.error("Projection cannot be null");
            throw new Exception("Projection cannot be null");
        }*/
        if (scale == null) {
            scale = new BigDecimal(0);
        }
        
        List possibleLayerPricings = getActiveLayerPricingList(layer, validationDate, planType, service, operation);
        Iterator layerPricingIter = possibleLayerPricings.iterator();
        
        /*
         * This results in a list of all possible layerPricings. However, scale and projection have yet to be checked.
         */
        LayerPricing layerPricing = null;
        boolean lpsHaveProjection = false;
        while(layerPricingIter.hasNext()) {
            LayerPricing plp = (LayerPricing) layerPricingIter.next(); // plp == possibleLayerPricing
            if (plp.getProjection() == null) {
                layerPricing = plp;
                break;
            }
            lpsHaveProjection = true;
            if ((plp.getMinScale() == null || plp.getMinScale().compareTo(scale) <= 0) &&
                    (plp.getMaxScale() == null || plp.getMaxScale().compareTo(scale) >= 0) &&
                    plp.getProjection().equalsIgnoreCase(projection)){
                layerPricing = plp;
                break;
            }
        }
        
        if (layerPricing == null) {
            if (projection != null && lpsHaveProjection) {
                throw new LayerNotAvailableException();
            }
            throw new NoResultException();
        }
        return layerPricing;
    }
    
    
    
    
    public BigDecimal calculateLayer(WfsLayer layer, Date validationDate, String projection, BigDecimal scale, BigDecimal units, int planType, String service, String operation) throws Exception {
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
            log.error("Layer is required!");
            throw new Exception("Layer is required!");
        }
        if (validationDate == null) {
            log.error("ValidationDate is a required field!");
            throw new Exception("ValidationDate is a required field!");
        }
        if (units == null) {
            log.error("Units is a required field!");
            throw new Exception("Units is a required field!");
        }
        if (planType <= 0) {
            log.error("PlanType is a required field!");
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
        
        try {
            LayerPricing layerPricing = getActiveLayerPricing(layer, validationDate, projection, scale, planType, service, operation);
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
            log.error("Trying to close an external EntityManager. This is not the place to do it!");
            throw new Exception("Trying to close an external EntityManager. This is not the place to do it!");
        }
        em.close();
    }
    
    
}
