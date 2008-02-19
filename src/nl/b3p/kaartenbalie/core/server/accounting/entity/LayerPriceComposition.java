/*
 * LayerCalculation.java
 *
 * Created on January 29, 2008, 10:41 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.accounting.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author Chris Kramer
 */
public class LayerPriceComposition {
    
    
    public static final int METHOD_OWN = 0;
    public static final int METHOD_PARENTS = 1;
    public static final int METHOD_CHILDS = 2;
    public static final int METHOD_NONE = 3;
    
    
    private Integer id;
    /*
     *Constructor values
     */
    private String serverProviderPrefix;
    private String layerName;
    private Date calculationDate;
    private int planType;
    private BigDecimal units;
    private BigDecimal scale;
    private String service;
    private String operation;
    
    
    
    /*
     * Results
     */
    private Boolean layerIsFree;
    private BigDecimal layerPrice;
    private int method;
    private long calculationTime;
    
    /*
     * Relational
     */
    private TransactionLayerUsage transactionLayerUsage;
    
    /*
     * Constructors
     */
    public LayerPriceComposition() {
        
        method = METHOD_NONE;
    }
    
    public LayerPriceComposition(String serverProviderPrefix, String layerName, Date calculationDate, BigDecimal scale, int planType, BigDecimal units, String service, String operation) {
        this();
        this.setServerProviderPrefix(serverProviderPrefix);
        this.setLayerName(layerName);
        this.setCalculationDate(calculationDate);
        this.setPlanType(planType);
        this.setUnits(units);
        this.setOperation(operation);
        this.setService(service);
        this.setScale(scale);

    }
    
    /*
     * Getters & Setters
     */
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Boolean getLayerIsFree() {
        return layerIsFree;
    }
    
    public void setLayerIsFree(Boolean layerIsFree) {
        this.layerIsFree = layerIsFree;
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
    
    public int getMethod() {
        return method;
    }
    
    public void setMethod(int method) {
        this.method = method;
    }
    
    public long getCalculationTime() {
        return calculationTime;
    }
    
    public void setCalculationTime(long calculationTime) {
        this.calculationTime = calculationTime;
    }
    
    public TransactionLayerUsage getTransactionLayerUsage() {
        return transactionLayerUsage;
    }
    
    public void setTransactionLayerUsage(TransactionLayerUsage transactionLayerUsage) {
        this.transactionLayerUsage = transactionLayerUsage;
    }
    
    public String getService() {
        return service;
    }
    
    public void setService(String service) {
        this.service = service;
    }
    
    public String getOperation() {
        return operation;
    }
    
    public void setOperation(String operation) {
        this.operation = operation;
    }
    /*
     * Methods
     */
    public String toString() {
        return "LayerCalculation for '" + serverProviderPrefix + "_" + layerName + "'. \n" +
                "This calculation took place on " + calculationDate + " for planType '" + planType + "' and units '" + units + "'. \n" +
                "The total cost of this calculation was " + layerPrice + " credits. \n" +
                "The freeState of this layer is '" + layerIsFree + "'. \n" +
                "The used method is " + method + ". \n";
        
    }

    public BigDecimal getScale() {
        return scale;
    }

    public void setScale(BigDecimal scale) {
        this.scale = scale;
    }
    
    
    
    
    
    
}
