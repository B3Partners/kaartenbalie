/*
 * Account.java
 *
 * Created on November 15, 2007, 9:54 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.accounting.entity;

import java.math.BigDecimal;
import java.util.Set;
import nl.b3p.kaartenbalie.core.server.Organization;

/**
 *
 * @author Chris Kramer
 */

public class Account{
    
    private Integer id;
    private BigDecimal creditBalance;
    private Organization organization;
    private Set transactions;
    public Account() {
        setCreditBalance(new BigDecimal(0));
    }
    
    public Account(Organization organization) {
        this();
        this.organization = organization;
    }
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Organization getOrganization() {
        return organization;
    }
    
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
    
    public BigDecimal getCreditBalance() {
        return creditBalance;
    }
    
    public void setCreditBalance(BigDecimal creditBalance) {
        this.creditBalance = creditBalance;
    }
    
    public Set getTransactions() {
        return transactions;
    }
    
    public void setTransactions(Set transactions) {
        this.transactions = transactions;
    }
    
    
    
}
