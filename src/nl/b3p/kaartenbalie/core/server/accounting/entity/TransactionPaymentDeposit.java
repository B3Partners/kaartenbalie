/*
 * TransactionPaymentDeposit.java
 *
 * Created on November 27, 2007, 10:21 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.accounting.entity;

import java.math.BigDecimal;

/**
 *
 * @author Chris Kramer
 */
public class TransactionPaymentDeposit extends Transaction{
    
    private static Integer ExchangeRate = new Integer(100);
    private BigDecimal billingAmount;
    private Integer txExchangeRate;
    /** Creates a new instance of TransactionPaymentDeposit */
    public TransactionPaymentDeposit() {
        super();
        this.setType(DEPOSIT);
    }
    
    public void validate() throws Exception {
        if (this.getType() != DEPOSIT) {
            throw new Exception("Only DEPOSIT is allowed for this type of transaction.");
        }
        
        if (billingAmount != null && billingAmount.doubleValue() > 9999)
        {
            throw new Exception("Billingamount larger then 9999.");
        }
    }
    
    public static Integer getExchangeRate() {
        return ExchangeRate;
    }
    
    public static void setExchangeRate(Integer aExchangeRate) {
        ExchangeRate = aExchangeRate;
    }
    
    public BigDecimal getBillingAmount() {
        return billingAmount;
    }
    
    public void setBillingAmount(BigDecimal billingAmount) {
        this.billingAmount = billingAmount;
    }
    
    public Integer getTxExchangeRate() {
        return txExchangeRate;
    }
    
    public void setTxExchangeRate(Integer txExchangeRate) {
        this.txExchangeRate = txExchangeRate;
    }
    
    
    
    
    
    
}
