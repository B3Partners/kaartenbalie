/*
 * PricingPlan.java
 *
 * Created on November 29, 2007, 4:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.accounting.entity;

import java.math.BigDecimal;
import java.util.Date;
import nl.b3p.wms.capabilities.*;

/**
 *
 * @author Chris Kramer
 */
public class LayerPricing {
    
    public final static int PAY_PER_REQUEST = 1;
    
    private Integer id;
    private int planType;
    private Date validFrom;
    private Date validUntil;
    private Date creationDate;
    private Date deletionDate;
    private BigDecimal unitPrice;
    private Boolean layerIsFree;
    private String service;
    private String operation;
    
    
    /*Relation*/
    private String layerName;
    private String serverProviderPrefix;
    
    /*Uniqueness*/
    private Integer indexCount;
    
    
    public LayerPricing() {
        setPlanType(PAY_PER_REQUEST);
        setCreationDate(new Date());
    }
    
    
    public int getPlanType() {
        return planType;
    }
    
    public void setPlanType(int planType) {
        this.planType = planType;
    }
    
    public Date getValidFrom() {
        return validFrom;
    }
    
    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }
    
    public Date getValidUntil() {
        return validUntil;
    }
    
    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }
    
    public Date getCreationDate() {
        return creationDate;
    }
    
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
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
    
    public String getLayerName() {
        return layerName;
    }
    
    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }
    
    public String getServerProviderPrefix() {
        return serverProviderPrefix;
    }
    
    public void setServerProviderPrefix(String serverProviderPrefix) {
        this.serverProviderPrefix = serverProviderPrefix;
    }
    
    public Date getDeletionDate() {
        return deletionDate;
    }
    
    public void setDeletionDate(Date deletionDate) {
        this.deletionDate = deletionDate;
    }
    
    public Integer getIndexCount() {
        return indexCount;
    }
    
    public void setIndexCount(Integer indexCount) {
        this.indexCount = indexCount;
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
    
    
}
