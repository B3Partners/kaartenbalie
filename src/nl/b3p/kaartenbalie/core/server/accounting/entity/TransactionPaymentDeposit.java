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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Chris Kramer
 */
public class TransactionPaymentDeposit extends Transaction {

    private static final Log log = LogFactory.getLog(TransactionPaymentDeposit.class);
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
            log.error("Only DEPOSIT is allowed for this type of transaction.");
            throw new Exception("Only DEPOSIT is allowed for this type of transaction.");
        }

        if (billingAmount != null && billingAmount.doubleValue() > 9999) {
            log.error("Billingamount larger then 9999.");
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
