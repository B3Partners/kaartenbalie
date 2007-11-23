/*
 * Account.java
 *
 * Created on November 15, 2007, 9:54 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.accounting.entity;

import javax.servlet.http.HttpServlet;

/**
 *
 * @author Chris Kramer
 */

public class Account{
    
    private Integer id;
    private Integer creditBalance;
    public Account() {
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getCreditBalance() {
        return creditBalance;
    }
    
    public void setCreditBalance(Integer creditBalance) {
        this.creditBalance = creditBalance;
    }


    
    
    
}
