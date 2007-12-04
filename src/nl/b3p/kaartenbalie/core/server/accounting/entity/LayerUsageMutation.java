/*
 * LayerUsageMutation.java
 *
 * Created on November 29, 2007, 3:51 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.accounting.entity;

import nl.b3p.kaartenbalie.core.server.datawarehousing.DataWarehousing;
import nl.b3p.wms.capabilities.Layer;

/**
 *
 * @author Chris Kramer
 */
public class LayerUsageMutation {
    
    private Integer id;
    private Integer layerId;
    private TransactionLayerUsage transactionLayerUsage;
    
    
    public LayerUsageMutation() {
    }
    public LayerUsageMutation(TransactionLayerUsage transactionLayerUsage, Layer layer) {
        this.setTransactionLayerUsage(transactionLayerUsage);
        this.setLayer(layer);
    }
    
    public LayerUsageMutation(TransactionLayerUsage transactionLayerUsage, Integer layerId) {
        this.setTransactionLayerUsage(transactionLayerUsage);
        this.setLayerId(layerId);
    }
    
    
    
    private Integer getLayerId() {
        return layerId;
    }
    
    private void setLayerId(Integer layerId) {
        this.layerId = layerId;
    }

    public TransactionLayerUsage getTransactionLayerUsage() {
        return transactionLayerUsage;
    }
    
    public void setTransactionLayerUsage(TransactionLayerUsage transactionLayerUsage) {
        this.transactionLayerUsage = transactionLayerUsage;
    }
    
    public void setLayer(Layer layer) {
        if (layer != null) {
            setLayerId(layer.getId());
        } else {
            setLayerId(null);
        }
    }
    
    public Layer getLayer() {
        try {
            return (Layer) DataWarehousing.find(Layer.class, getLayerId());
        } catch (Exception e) {
            return null;
        }
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    
    
    
    
}
