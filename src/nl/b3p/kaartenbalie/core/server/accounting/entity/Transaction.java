/*
 * Transaction.java
 *
 * Created on November 27, 2007, 10:01 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.accounting.entity;

import java.math.BigDecimal;
import java.util.Date;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.datawarehousing.DataWarehousing;

/**
 *
 * @author Chris Kramer
 */
public abstract class Transaction {
    
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
    
    public Transaction() {
        setTransactionDate(new Date());
        setStatus(PENDING);
        setType(WITHDRAW);
        creditAlteration = new BigDecimal(0);
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
    
    public abstract void validate() throws Exception;
    
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
    
    public User getUser() {
        try {
            return (User) DataWarehousing.find(User.class, getUserId());
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
    
    
    
}
