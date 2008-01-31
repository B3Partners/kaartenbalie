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
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPricing;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.wms.capabilities.Layer;


/**
 *
 * @author Chris Kramer
 */
public class LayerCalculator {
    
    
    private static String inspringen = "";
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
    
    public static void bij() {
        inspringen = inspringen + "---";
    }
    
    public static void af() {
        inspringen = inspringen.substring(0,inspringen.length() -3);
    }
    public LayerCalculation calculateLayerComplete(Layer layer, Date validationDate, BigDecimal units, int planType) throws Exception {
        System.out.println(inspringen + "Start van het traject voor het opvragen van een prijs...");
        LayerCalculation tLC = new LayerCalculation(layer.getSpAbbr(), layer.getName(), validationDate, planType, units);
        BigDecimal layerPrice = null;
        try {
            layerPrice = calculateLayer(layer, validationDate, units, planType);
        } catch(NoPrizingException npe) {
            System.out.println(inspringen + "Begin zoektocht naar alternatieven!");
            System.out.println(inspringen + "Zoek bij de parentlayers voor een prijs... ");
            try {
                layerPrice = calculateParentLayer(layer, validationDate, units,  planType);
            } catch (NoPrizingException npeParent) {
                System.out.println(inspringen + "Er kon geen prijs worden bepaald via de parents... Zoek bij de childs...");
                try {
                    layerPrice = calculateChildLayers(layer, validationDate, units,  planType);
                } catch (NoPrizingException npeChilds) {
                    af();
                    System.out.println(inspringen + "Er kon geen prijs worden bepaald via de childs... Kaart is gratis :S");
                    //npeChilds.printStackTrace();
                }
            }
        }
        tLC.setLayerIsFree(new Boolean(layerPrice == null));
        if (layerPrice == null) {
            tLC.setLayerPrice(new BigDecimal(0));
        } else {
            tLC.setLayerPrice(layerPrice);
        }
        
        
        
        
        return tLC;
    }
    
    
    
    private BigDecimal calculateChildLayers(Layer layer, Date validationDate, BigDecimal units, int planType) throws Exception{
        bij();
        System.out.println(inspringen + "Controleer of " + layer.getName() + " childlayers heeft.");
        BigDecimal layerPrice = null;
        
        Set childLayers = layer.getLayers();
        
        if (childLayers != null && childLayers.size() > 0) {
            System.out.println(inspringen + "Er zijn " + childLayers.size() + " childlayers die we nu gaan opvragen...");
            Iterator iterChilds = childLayers.iterator();
            while (iterChilds.hasNext()) {
                Layer childLayer = (Layer) iterChilds.next();
                boolean hasNoPrice = false;
                try {
                    layerPrice = calculateLayer( childLayer,  validationDate,  units,  planType);
                } catch (NoPrizingException npe) {
                    //Deze child heeft geen prijs, niet erg, gewoon door met de volgende... Wel even notitie van maken.
                    hasNoPrice = true;
                }
                
                try {
                    BigDecimal childLayerPrice = calculateChildLayers(childLayer, validationDate, units, planType);
                    if (childLayerPrice != null) {
                        if (layerPrice == null ) {
                            layerPrice = childLayerPrice;
                        } else {
                            layerPrice = layerPrice.add(childLayerPrice);
                        }
                    }
                } catch (NoPrizingException npe) {
                    if (hasNoPrice) {
                        //throw npe;
                    }
                }
            }
        } else {
            System.out.println(inspringen + "Er zijn geen childlayers...");
            af();
            throw new NoPrizingException("Geen childlayers meer om prijzen voor te zoeken.");
        }
        
        
        af();
        return layerPrice;
    }
    private BigDecimal calculateParentLayer(Layer layer, Date validationDate, BigDecimal units, int planType) throws Exception{
        bij();
        System.out.println(inspringen + "Controleer of " + layer.getName() + " een parentlayer heeft.");
        BigDecimal layerPrice = null;
        Layer parentLayer = layer.getParent();
        if (parentLayer != null) {
            System.out.println(inspringen + "parent layer is != null");
            try {
                layerPrice = calculateLayer(parentLayer, validationDate, units, planType);
            } catch (NoPrizingException npe) {
                System.out.println(inspringen + "No prizing info available... ");
                layerPrice = calculateParentLayer(parentLayer, validationDate, units, planType);
            }
        } else {
            System.out.println(inspringen + "Er is geen parentlayer (meer) en dus ook geen prijs info.....");
            af();
            throw new NoPrizingException();
        }
        af();
        return layerPrice;
        
    }
    
    
    
    public LayerPricing getActiveLayerPricing(Layer layer, Date validationDate, BigDecimal units, int planType) throws Exception{
        if (aggregateLayerPricings) {
            throw new Exception("This function is not supported when layerPriceAggregation is enabled.");
        }
        return (LayerPricing) em.createQuery(
                "FROM LayerPricing AS lp " +
                "WHERE (lp.deletionDate IS null OR lp.deletionDate > :validationDate) " +
                "AND lp.planType = :planType " +
                "AND lp.layerName = :layerName " +
                "AND lp.serverProviderPrefix = :serverProviderPrefix " +
                "AND lp.creationDate < :validationDate " +
                "AND (lp.validFrom < :validationDate OR lp.validFrom IS null) " +
                "AND (lp.validUntil > :validationDate OR lp.validUntil IS null) " +
                "ORDER BY lp.indexCount DESC")
                .setParameter("layerName",layer.getName())
                .setParameter("serverProviderPrefix",layer.getSpAbbr())
                .setParameter("validationDate",validationDate)
                .setParameter("planType",new Integer(planType))
                .setMaxResults(1)
                .getSingleResult();
        
    }
    public BigDecimal calculateLayer(Layer layer, Date validationDate, BigDecimal units, int planType) throws Exception {
        bij();
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
        
        System.out.println(inspringen + "De prijs voor " + layer.getSpAbbr() + "_" + layer.getName() + " wordt opgevraagd...");
        /*
         * Check the mode and use the specified function...
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
                    "ORDER BY lp.indexCount DESC")
                    .setParameter("layerName",layer.getName())
                    .setParameter("serverProviderPrefix",layer.getSpAbbr())
                    .setParameter("validationDate",validationDate)
                    .setParameter("units",units)
                    .setParameter("planType",new Integer(planType))
                    .getSingleResult();
            
            Long pricingLines = (Long) resultSet[0];
            /*
             * If there are no lines, thrown an exception!
             */
            if (pricingLines.longValue() == 0) {
                System.out.println(inspringen + layer.getSpAbbr() + "_" + layer.getName() + " bevat geen prijsinformatie.");
                af();
                throw new NoPrizingException();
            }
            
            /*
             * Check if the layer is free, if it's not, then set the layerPrice..
             */
            if (!((Boolean) resultSet[2]).booleanValue()) {
                layerPrice = (BigDecimal)resultSet[1];
            }
        } else {
            try {
                LayerPricing layerPricing = getActiveLayerPricing(layer, validationDate, units, planType);
                
                if (layerPricing.getUnitPrice() != null &&
                        (layerPricing.getLayerIsFree() == null  || (layerPricing.getLayerIsFree() != null && layerPricing.getLayerIsFree().booleanValue() == false))) {
                    layerPrice = layerPricing.getUnitPrice().multiply(units);
                }
                System.out.println(inspringen + layer.getSpAbbr() + "_" + layer.getName() + " bevat prijsinformatie, status: " + layerPrice);
            } catch (NoResultException nre) {
                System.out.println(inspringen + layer.getSpAbbr() + "_" + layer.getName() + " bevat geen prijsinformatie.");
                af();
                throw new NoPrizingException(nre.getMessage());
            }
            
        }
        /*
         * Final check!
         */
        if (layerPrice != null && layerPrice.compareTo(new BigDecimal(0)) < 0) {
            layerPrice = new BigDecimal(0);
        }
        af();
        return layerPrice;
        
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
            Layer kaartenbalie= (Layer) em.find(Layer.class, new Integer(1311));
            Layer grenzen = (Layer) em.find(Layer.class, new Integer(1314));
            Layer autowegen_nl = (Layer) em.find(Layer.class, new Integer(1302));
            
            Calendar cal = Calendar.getInstance();
            System.out.println(cal.getTime());
            System.out.println("===================================================");
            System.out.println(lc.calculateLayerComplete(kaartenbalie, cal.getTime(), new BigDecimal(1), LayerPricing.PAY_PER_REQUEST));
            System.out.println("===================================================");
            //System.out.println(lc.calculateLayerComplete(grenzen, cal.getTime(), new BigDecimal(1), LayerPricing.PAY_PER_REQUEST));
            System.out.println("===================================================");
            //System.out.println(lc.calculateLayerComplete(autowegen_nl, cal.getTime(), new BigDecimal(1), LayerPricing.PAY_PER_REQUEST));
            System.out.println("===================================================");
            
            /*
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
             */
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lc.closeEntityManager();
            em.close();
        }
    }
    
    
    
}
