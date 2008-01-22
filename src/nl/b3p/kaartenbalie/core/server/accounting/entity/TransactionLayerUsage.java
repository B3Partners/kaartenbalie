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
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import nl.b3p.kaartenbalie.core.server.accounting.DownsizeWrapper;
import nl.b3p.kaartenbalie.core.server.accounting.LayerCalculator;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPricing;

/**
 *
 * @author Chris Kramer
 */
public class TransactionLayerUsage extends Transaction{
    
    /** Creates a new instance of TransactionLayerUsage */
    public TransactionLayerUsage() {
        super();
        this.setType(WITHDRAW);
    }
    public void validate() throws Exception {
        if (this.getType() != WITHDRAW) {
            throw new Exception("Only WITHDRAW is allowed for this type of transaction.");
        }
    }
    
}
