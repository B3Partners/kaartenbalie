/*
 * LayerCalculation.java
 *
 * Created on January 29, 2008, 10:41 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.accounting;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Chris Kramer
 */
public class LayerCalculation {
    
    
    /*
     *Constructor values
     */
    private String serverProviderPrefix;
    private String layerName;
    private Date calculationDate;
    private int planType;
    private BigDecimal units;
    
    /*
     * Results
     */
    private Boolean layerIsFree;
    private Long pricingLines;
    private BigDecimal layerPrice;
    
    private LayerCalculation() {
    }
    
    public LayerCalculation(String serverProviderPrefix, String layerName, Date calculationDate, int planType, BigDecimal units) {
        this.setServerProviderPrefix(serverProviderPrefix);
        this.setLayerName(layerName);
        this.setCalculationDate(calculationDate);
        this.setPlanType(planType);
        this.setUnits(units);
    }
    
    public Boolean getLayerIsFree() {
        return layerIsFree;
    }
    
    public void setLayerIsFree(Boolean layerIsFree) {
        this.layerIsFree = layerIsFree;
    }
    
    public Long getPricingLines() {
        return pricingLines;
    }
    
    public void setPricingLines(Long pricingLines) {
        this.pricingLines = pricingLines;
    }
    
    public BigDecimal getLayerPrice() {
        return layerPrice;
    }
    
    public void setLayerPrice(BigDecimal layerPrice) {
        this.layerPrice = layerPrice;
    }
    
    public String getServerProviderPrefix() {
        return serverProviderPrefix;
    }
    
    public void setServerProviderPrefix(String serverProviderPrefix) {
        this.serverProviderPrefix = serverProviderPrefix;
    }
    
    public String getLayerName() {
        return layerName;
    }
    
    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }
    
    public Date getCalculationDate() {
        return calculationDate;
    }
    
    public void setCalculationDate(Date calculationDate) {
        this.calculationDate = calculationDate;
    }
    
    public int getPlanType() {
        return planType;
    }
    
    public void setPlanType(int planType) {
        this.planType = planType;
    }
    
    public BigDecimal getUnits() {
        return units;
    }
    
    public void setUnits(BigDecimal units) {
        this.units = units;
    }
    
    
    public String toString() {
        return "LayerCalculation for '" + serverProviderPrefix + "_" + layerName + "'. \n" +
                "This calculation took place on " + calculationDate + " for planType '" + planType + "' and units '" + units + "'. \n" +
                "The total cost of this calculation was " + layerPrice + " credits. \n" +
                "This calculation was done using " + pricingLines + " pricingLines. The freeState of this layer is '" + layerIsFree + "'. \n";
        
    }
    
    
}
