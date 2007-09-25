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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class User implements Principal {
    
    private Integer id;
    private String firstName;
    private String surname;
    private String emailAddress;
    private String username;
    private String password;
    private String personalURL;
    private String registeredIP;
    private String defaultGetMap;
    private Date timeout;
    private Organization organization;
    
    private Set userroles;
    
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    public Set getUserroles() {
        return userroles;
    }

    public void setUserroles(Set userroles) {
        this.userroles = userroles;
    }
    
    public void addUserRole (Roles role) {
        if(userroles == null) {
            userroles = new HashSet();
        }
        userroles.add(role);
    }
    
    public void deleteUserRole(Roles role) {
        if (userroles != null) {
            userroles.remove(role);
        }
    }
    
    public boolean checkRole(String role) {
        Iterator it = userroles.iterator();
        while (it.hasNext()) {
            Roles theUserroles = (Roles) it.next();
            if (role.equalsIgnoreCase(theUserroles.getRole())) {
                return true;
            }
        }
        return false;
    }
    
    public String getRolesAsString() {
        String roles = "";
        Iterator it = userroles.iterator();
        while (it.hasNext()) {
            Roles theUserroles = (Roles) it.next();
            roles = roles + theUserroles.getRole() + " ";
        }
        return roles;
    }
}
