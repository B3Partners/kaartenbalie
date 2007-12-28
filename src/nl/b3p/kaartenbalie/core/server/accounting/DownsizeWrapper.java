/*
 * DownsizeWrapper.java
 *
 * Created on December 27, 2007, 11:01 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.accounting;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import nl.b3p.wms.capabilities.Layer;

/**
 *
 * @author Chris Kramer
 */
public class DownsizeWrapper {
    
    private Layer layer;
    private BigDecimal layerPriceGross;
    private BigDecimal layerPriceNet;
    private List childWrappers;
    private List pricingPlans;
    
    public DownsizeWrapper() {
        setChildWrappers(new ArrayList());
        setPricingPlans(new ArrayList());
    }
    
    public DownsizeWrapper(Layer layer) {
        this();
        this.setLayer(layer);
    }
    
    public Layer getLayer() {
        return layer;
    }
    
    public void setLayer(Layer layer) {
        this.layer = layer;
    }
    
    
    public List getChildWrappers() {
        return childWrappers;
    }
    
    public void setChildWrappers(List childWrappers) {
        this.childWrappers = childWrappers;
    }
    
    public List getPricingPlans() {
        return pricingPlans;
    }
    
    public void setPricingPlans(List pricingPlans) {
        this.pricingPlans = pricingPlans;
    }
    
    public BigDecimal getLayerPriceGross() {
        return layerPriceGross;
    }
    
    public void setLayerPriceGross(BigDecimal layerPriceGross) {
        this.layerPriceGross = layerPriceGross;
    }
    
    public BigDecimal getLayerPriceNet() {
        return layerPriceNet;
    }
    
    public void setLayerPriceNet(BigDecimal layerPriceNet) {
        this.layerPriceNet = layerPriceNet;
    }
    
    
    
}
