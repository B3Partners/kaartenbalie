/*
 * TransactionLayerUsage.java
 *
 * Created on November 27, 2007, 10:18 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.accounting.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import oracle.net.ano.SupervisorService;

/**
 *
 * @author Chris Kramer
 */
public class TransactionLayerUsage extends Transaction{
    private Set layerPriceCompositions;
    /** Creates a new instance of TransactionLayerUsage */
    public TransactionLayerUsage() {
        super();
        layerPriceCompositions = new HashSet();
        this.setType(WITHDRAW);
    }
    public void validate() throws Exception {
        if (this.getType() != WITHDRAW) {
            throw new Exception("Only WITHDRAW is allowed for this type of transaction.");
        }
    }
    
    public Set getLayerPriceCompositions() {
        return layerPriceCompositions;
    }
    
    public void setLayerPriceCompositions(Set layerPriceCompositions) {
        this.layerPriceCompositions = layerPriceCompositions;
    }
    
    public void registerUsage(LayerPriceComposition lpc) throws Exception{
        if (lpc == null) {
            throw new Exception("Not allowed to add a null value to registerUsage.");
        }
        if (lpc.getLayerPrice() == null || lpc.getLayerPrice().compareTo(new BigDecimal(0)) < 0) {
            throw new Exception("Invalid value for lpc.layerPrice: " + lpc.getLayerPrice());
        }
        if (lpc.getLayerIsFree() == null || (lpc.getLayerIsFree() != null && !lpc.getLayerIsFree().booleanValue())) {
            creditAlteration = creditAlteration.add(lpc.getLayerPrice());
        }
        lpc.setTransactionLayerUsage(this);
        layerPriceCompositions.add(lpc);
    }
    
    
}
