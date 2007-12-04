/*
 * TransactionLayerUsage.java
 *
 * Created on November 27, 2007, 10:18 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.accounting.entity;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Chris Kramer
 */
public class TransactionLayerUsage extends Transaction{
    
    /** Creates a new instance of TransactionLayerUsage */
    private Set layerUsageMutations;
    
    public TransactionLayerUsage() {
        super();
        setLayerUsageMutations(new HashSet());
        this.setType(WITHDRAW);
    }
    public void validate() throws Exception {
        if (this.getType() != WITHDRAW) {
            throw new Exception("Only WITHDRAW is allowed for this type of transaction.");
        }
    }
    
    
    public void registerUsage(Integer layerId) {
        LayerUsageMutation lum = new LayerUsageMutation(this, layerId);
        getLayerUsageMutations().add(lum);
    }
    
    public Set getLayerUsageMutations() {
        return layerUsageMutations;
    }
    
    public void setLayerUsageMutations(Set layerUsageMutations) {
        this.layerUsageMutations = layerUsageMutations;
    }
    
    
    
    
}
