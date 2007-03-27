/**
 * @(#)User.java
 * @author N. de Goeij
 * @version 1.00 2006/10/11
 *
 * Purpose: Bean representing a User.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.core.server;

import java.security.Principal;
import java.util.Date;

public class User implements Principal {
    
    private Integer id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String username;
    private String password;
    private String role;
    private String personalURL;
    private String registeredIP;
    private String defaultGetMap;
    private Date timeout;
    private Organization organization;
    
    // <editor-fold defaultstate="" desc="getter and setter methods.">
    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    /* impl principal*/
    public String getName() {
        return username;
    }

    public String getPersonalURL() {
        return personalURL;
    }

    public void setPersonalURL(String personalURL) {
        this.personalURL = personalURL;
    }
    
    public String getRegisteredIP() {
        return registeredIP;
    }

    public void setRegisteredIP(String registeredIP) {
        this.registeredIP = registeredIP;
    }

    public Date getTimeout() {
        return timeout;
    }

    public void setTimeout(Date timeout) {
        this.timeout = timeout;
    }
    
    public String getDefaultGetMap(){
        return defaultGetMap;
    }
    
    public void setDefaultGetMap(String s){
        defaultGetMap=s;
    }
    // </editor-fold>
}
