/*
 * Organization.java
 *
 * Created on 11 oktober 2006, 15:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Nando De Goeij
 */
public class Organization {
    
    private Integer id;
    private String name;
    private String street;
    private String number;
    private String addition;
    private String province;
    private String country;
    private String postbox;
    private String billingAddress;
    private String visitorsAddress;
    private String telephone;
    private String fax;
    private Set user;
    private Set billing;
    private Set organizationLayer;

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Set getUser() {
        return user;
    }

    public void setUser(Set user) {
        this.user = user;
    }

    public Set getBilling() {
        return billing;
    }

    public void setBilling(Set billing) {
        this.billing = billing;
    }

    public Set getOrganizationLayer() {
        return organizationLayer;
    }

    public void setOrganizationLayer(Set organizationLayer) {
        this.organizationLayer = organizationLayer;
    }
    
    public void addOrganizationLayer(Layer layer) {
        if (null == organizationLayer) {
            organizationLayer = new HashSet();
        }
        organizationLayer.add(layer);
    }

    public String getPostbox() {
        return postbox;
    }

    public void setPostbox(String postbox) {
        this.postbox = postbox;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getVisitorsAddress() {
        return visitorsAddress;
    }

    public void setVisitorsAddress(String visitorsAddress) {
        this.visitorsAddress = visitorsAddress;
    }
}
