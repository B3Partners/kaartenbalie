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
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
    
    
    public LayerCalculation calculateLayerComplete(Layer layer, Date validationDate, BigDecimal units, int planType) throws Exception {
        
        
        LayerCalculation tLC = calculateLayer(layer, validationDate, units, planType);
        System.out.println(tLC);
        /*
         * When the layer is not free, and there is no price for this layer, do other things to find a price.
         */
        if (!tLC.getLayerIsFree().booleanValue() && tLC.getLayerPrice().compareTo(new BigDecimal(0)) <= 0) {
            System.out.println("Alternatieven!!");
            /*
             * First check the parentLayer for a price..
             */
            LayerCalculation parentCalculation = null;
            if (layer.getParent() != null) {
                parentCalculation = calculateLayer(layer.getParent(), validationDate, units,  planType);
            } else {
                
            }
        }
        return tLC;
    }
    
    
    public LayerPricing getActiveLayerPricing(String layerName, String serverProviderPrefix, Date validationDate, BigDecimal units, int planType) throws Exception{
        if (aggregateLayerPricings) {
            throw new Exception("This function is not supported when layerPriceAggregation is enabled.");
        }
        
        
        LayerPricing layerPricing = (LayerPricing) em.createQuery(
                "FROM LayerPricing AS lp " +
                "WHERE (lp.deletionDate IS null OR lp.deletionDate > :validationDate) " +
                "AND lp.planType = :planType " +
                "AND lp.layerName = :layerName " +
                "AND lp.serverProviderPrefix = :serverProviderPrefix " +
                "AND lp.creationDate < :validationDate " +
                "AND (lp.validFrom < :validationDate OR lp.validFrom IS null) " +
                "AND (lp.validUntil > :validationDate OR lp.validUntil IS null) " +
                "ORDER BY lp.indexCount DESC")
                .setParameter("layerName",layerName)
                .setParameter("serverProviderPrefix",serverProviderPrefix)
                .setParameter("validationDate",validationDate)
                .setParameter("planType",new Integer(planType))
                .setMaxResults(1)
                .getSingleResult();
        return layerPricing;
    }
    public LayerCalculation calculateLayer(Layer layer, Date validationDate, BigDecimal units, int planType) throws Exception {
        
        
        String serverProviderPrefix = layer.getSpAbbr();
        String layerName = layer.getName();
        if (validationDate == null) {
            validationDate = new Date();
        }
        BigDecimal layerPrice = null;
        Boolean layerIsFree = null;
        LayerCalculation lc = new LayerCalculation(serverProviderPrefix, layerName, validationDate, planType, units);
        
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
                    "ORDER BY lp.indexCount DESC")
                    .setParameter("layerName",layerName)
                    .setParameter("serverProviderPrefix",serverProviderPrefix)
                    .setParameter("validationDate",validationDate)
                    .setParameter("units",units)
                    .setParameter("planType",new Integer(planType))
                    .getSingleResult();
            
            layerPrice = (BigDecimal)resultSet[1];
            layerIsFree = (Boolean) resultSet[2];
            lc.setPricingLines((Long)resultSet[0]);
        } else {
            try {
                LayerPricing layerPricing = getActiveLayerPricing(layerName,serverProviderPrefix, validationDate, units, planType);
                lc.setPricingLines(new Long(1));
                if (layerPricing != null) {
                    if (layerPricing.getUnitPrice() != null) {
                        layerPrice = layerPricing.getUnitPrice().multiply(units);
                    }
                    layerIsFree = layerPricing.getLayerIsFree();
                }
                
            } catch (NoResultException nre) {
                //Nothing found!
            }
            
        }
        
        if (layerIsFree == null) {
            layerIsFree = new Boolean(false);
        }
        
        if (layerPrice == null || (layerPrice != null && layerPrice.compareTo(new BigDecimal(0)) < 0)) {
            layerPrice = new BigDecimal(0);
        }
        
        lc.setLayerPrice(layerPrice);
        lc.setLayerIsFree(layerIsFree);
        return lc;
        
    }
    
    public void closeEntityManager() throws Exception {
        if (externalEm) {
            throw new Exception("Trying to close an external EntityManager. This is not the place to do it!");
        }
        em.close();
    }
    
    public static void main(String[] args) throws Exception {
        MyEMFDatabase.openEntityManagerFactory(MyEMFDatabase.nonServletKaartenbaliePU);
        LayerCalculator lc = new LayerCalculator();
        EntityManager em = MyEMFDatabase.createEntityManager();
        try {
            Layer layer = (Layer) em.find(Layer.class, new Integer(1173));
            System.out.println(layer);
            
            Calendar cal = Calendar.getInstance();
            System.out.println(cal.getTime());
            lc.calculateLayerComplete(layer, cal.getTime(), new BigDecimal(1), LayerPricing.PAY_PER_REQUEST);
            
            cal.set(2008,0,29,14,00,00);
            System.out.println(cal.getTime());
            lc.calculateLayerComplete(layer, cal.getTime(), new BigDecimal(1), LayerPricing.PAY_PER_REQUEST);
            
            cal.set(2008,0,29,14,03,00);
            System.out.println(cal.getTime());
            lc.calculateLayerComplete(layer, cal.getTime(), new BigDecimal(1), LayerPricing.PAY_PER_REQUEST);
            
            
            cal.set(2008,0,29,14,05,00);
            System.out.println(cal.getTime());
            lc.calculateLayerComplete(layer, cal.getTime(), new BigDecimal(1), LayerPricing.PAY_PER_REQUEST);
            
            cal.set(2008,1,3,14,05,00);
            System.out.println(cal.getTime());
            lc.calculateLayerComplete(layer, cal.getTime(), new BigDecimal(1), LayerPricing.PAY_PER_REQUEST);
            
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lc.closeEntityManager();
            em.close();
        }
    }
    
    
    
}
