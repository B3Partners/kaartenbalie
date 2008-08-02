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
package nl.b3p.kaartenbalie.core.server.accounting.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Chris Kramer
 */
public class LayerPriceComposition {

    public static final int METHOD_OWN = 0;
    public static final int METHOD_PARENTS = 1;
    public static final int METHOD_CHILDS = 2;
    public static final int METHOD_NONE = 3;
    public static final int METHOD_BLOCKED = -1;
    private Integer id;
    /*
     *Constructor values
     */
    private String serverProviderPrefix;
    private String layerName;
    private Date calculationDate;
    private int planType;
    private BigDecimal units;
    private String projection;
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

    public LayerPriceComposition(String serverProviderPrefix, String layerName, Date calculationDate, BigDecimal scale, String projection, int planType, BigDecimal units, String service, String operation) {
        this();
        this.setServerProviderPrefix(serverProviderPrefix);
        this.setLayerName(layerName);
        this.setCalculationDate(calculationDate);
        this.setPlanType(planType);
        this.setUnits(units);
        this.setOperation(operation);
        this.setService(service);
        this.setScale(scale);
        this.setProjection(projection);
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
                "The used method is " + method + ". \n" +
                "Service/Operation: " + service + ", " + operation;
    }

    public BigDecimal getScale() {
        return scale;
    }

    public void setScale(BigDecimal scale) {
        this.scale = scale;
    }

    public String getProjection() {
        return projection;
    }

    public void setProjection(String projection) {
        this.projection = projection;
    }
}
