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
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.EntityManager;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.accounting.TransactionDeniedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Chris Kramer
 */
public class Transaction {

    private static final Log log = LogFactory.getLog(Transaction.class);
    private Integer id;
    private Date transactionDate;
    private Date mutationDate;
    private Account account;
    protected BigDecimal creditAlteration;
    private int status;
    private int type;
    private String errorMessage;
    private String description;
    private Integer userId;
    public static int PENDING = 0;
    public static int ACCEPTED = 1;
    public static int REFUSED = 2;
    public static int WITHDRAW = 1;
    public static int DEPOSIT = 2;

    //TransactionPaymentDeposit
    private static Integer ExchangeRate = new Integer(100);
    private BigDecimal billingAmount;
    private Integer txExchangeRate;
    //TransactionLayerUsage
    private Set layerPriceCompositions = new HashSet();
    private Set pricedLayerNames = new HashSet();

    public Transaction() {
        setTransactionDate(new Date());
        setStatus(PENDING);
        setType(WITHDRAW);
        creditAlteration = new BigDecimal("0");
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Date getMutationDate() {
        return mutationDate;
    }

    public void setMutationDate(Date mutationDate) {
        this.mutationDate = mutationDate;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BigDecimal getCreditAlteration() {
        return creditAlteration;
    }

    public void setCreditAlteration(BigDecimal creditAlteration) {
        this.creditAlteration = creditAlteration;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void validate() throws TransactionDeniedException {
        if (getCreditAlteration().doubleValue() < 0) {
            throw new TransactionDeniedException("Transaction creditalteration cannot be less then zero.");
        }
        //Scale the creditAlteration...
        setCreditAlteration(getCreditAlteration().setScale(2, BigDecimal.ROUND_HALF_UP));

        if (billingAmount != null && billingAmount.doubleValue() > 9999) {
            log.error("Billingamount larger then 9999.");
            throw new TransactionDeniedException("Billingamount larger then 9999.");
        }
        if (getType() != WITHDRAW && getType() != DEPOSIT) {
            log.error("Only DEPOSIT or WITHDRAW is allowed for this transaction.");
            throw new TransactionDeniedException("Only DEPOSIT or WITHDRAW is allowed for this transaction.");
        }
    }

    private Integer getUserId() {
        return userId;
    }

    private void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUser(User user) {
        if (user != null) {
            setUserId(user.getId());
        } else {
            setUserId(null);
        }
    }

    public User getUser(EntityManager em) {
        try {
            return (User) em.find(User.class, getUserId());
        } catch (Exception e) {
            return null;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Set getLayerPriceCompositions() {
        return layerPriceCompositions;
    }

    public void setLayerPriceCompositions(Set layerPriceCompositions) {
        this.layerPriceCompositions = layerPriceCompositions;
    }

    public void registerUsage(LayerPriceComposition lpc) throws Exception {
        if (lpc == null) {
            log.error("Not allowed to add a null value to registerUsage.");
            throw new Exception("Not allowed to add a null value to registerUsage.");
        }
        if (lpc.getLayerPrice() == null || lpc.getLayerPrice().compareTo(new BigDecimal("0")) < 0) {
            log.error("Invalid value for lpc.layerPrice: " + lpc.getLayerPrice());
            throw new Exception("Invalid value for lpc.layerPrice: " + lpc.getLayerPrice());
        }
        if (lpc.getLayerIsFree() == null || (lpc.getLayerIsFree() != null && !lpc.getLayerIsFree().booleanValue())) {
            creditAlteration = creditAlteration.add(lpc.getLayerPrice());
            pricedLayerNames.add(lpc.getLayerName());
        }
        lpc.setTransaction(this);
        layerPriceCompositions.add(lpc);
    }

    public String getPricedLayerNames() {
        if (pricedLayerNames == null || pricedLayerNames.isEmpty()) {
            return null;
        }
        StringBuffer pln = new StringBuffer();
        Iterator it = pricedLayerNames.iterator();
        while (it.hasNext()) {
            if (pln.length() > 0) {
                pln.append(", ");
            }
            pln.append((String) it.next());
        }
        return pln.toString();
    }
}
