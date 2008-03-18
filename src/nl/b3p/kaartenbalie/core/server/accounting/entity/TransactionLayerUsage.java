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
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Chris Kramer
 */
public class TransactionLayerUsage extends Transaction{
    private static final Log log = LogFactory.getLog(TransactionLayerUsage.class);
    
    private Set layerPriceCompositions;
    private Set pricedLayerNames;
    /** Creates a new instance of TransactionLayerUsage */
    public TransactionLayerUsage() {
        super();
        layerPriceCompositions = new HashSet();
        pricedLayerNames = new HashSet();
        this.setType(WITHDRAW);
    }
    public void validate() throws Exception {
        if (this.getType() != WITHDRAW) {
            log.error("Only WITHDRAW is allowed for this type of transaction.");
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
            log.error("Not allowed to add a null value to registerUsage.");
            throw new Exception("Not allowed to add a null value to registerUsage.");
        }
        if (lpc.getLayerPrice() == null || lpc.getLayerPrice().compareTo(new BigDecimal(0)) < 0) {
            log.error("Invalid value for lpc.layerPrice: " + lpc.getLayerPrice());
            throw new Exception("Invalid value for lpc.layerPrice: " + lpc.getLayerPrice());
        }
        if (lpc.getLayerIsFree() == null || (lpc.getLayerIsFree() != null && !lpc.getLayerIsFree().booleanValue())) {
            creditAlteration = creditAlteration.add(lpc.getLayerPrice());
            pricedLayerNames.add(lpc.getLayerName());
        }
        lpc.setTransactionLayerUsage(this);
        layerPriceCompositions.add(lpc);
    }
    
    public String getPricedLayerNames() {
        if (pricedLayerNames==null || pricedLayerNames.isEmpty()) {
            return null;
        }
        StringBuffer pln = new StringBuffer();
        Iterator it = pricedLayerNames.iterator();
        while (it.hasNext()) {
            if (pln.length()>0)
                pln.append(", ");
            pln.append((String)it.next());
        }
        return pln.toString();
    }
}
