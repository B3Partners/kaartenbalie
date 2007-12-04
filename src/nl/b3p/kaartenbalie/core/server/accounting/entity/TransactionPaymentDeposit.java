/*
 * TransactionPaymentDeposit.java
 *
 * Created on November 27, 2007, 10:21 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.accounting.entity;

/**
 *
 * @author Chris Kramer
 */
public class TransactionPaymentDeposit extends Transaction{
    
    /** Creates a new instance of TransactionPaymentDeposit */
    public TransactionPaymentDeposit() {
        super();
        this.setType(DEPOSIT);
    }
    
    public void validate() throws Exception {
        if (this.getType() != DEPOSIT) {
            throw new Exception("Only DEPOSIT is allowed for this type of transaction.");
        }
    }
    
}
