/**
 * @(#)Organization.java
 * @author N. de Goeij
 * @version 1.00 2006/10/11
 *
 * Purpose: Bean representing a Organization.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.core.server;

import java.util.HashSet;
import java.util.Set;
import nl.b3p.kaartenbalie.core.server.accounting.entity.Account;
import nl.b3p.wms.capabilities.Layer;

public class Organization {
    
    private Integer id;
    private String name;
    private String street;
    private String number;
    private String addition;
    private String postalcode;
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
    private Set wfsOrganizationLayer;
    private Set reports;
    private Set reportStatus;
    private Account account;
    private boolean hasValidGetCapabilities;
    private String bbox;
    private String code;
    
    
    
    // <editor-fold defaultstate="" desc="getter and setter methods.">
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
    
    public String getPostalcode() {
        return postalcode;
    }
    
    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
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
        if (null == getOrganizationLayer()) {
            setOrganizationLayer(new HashSet());
        }
        getOrganizationLayer().add(layer);
    }
    
    public Set getWfsOrganizationLayer() {
        return wfsOrganizationLayer;
    }
    public void setWfsOrganizationLayer(Set wfsOrganizationLayer) {
        this.wfsOrganizationLayer = wfsOrganizationLayer;
    }
    
    public void addWfsOrganizationLayer(Layer layer) {
        if (null == getWfsOrganizationLayer()) {
            setWfsOrganizationLayer(new HashSet());
        }
        getWfsOrganizationLayer().add(layer);
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
    
    public boolean getHasValidGetCapabilities() {
        return isHasValidGetCapabilities();
    }
    
    public void setHasValidGetCapabilities(boolean hasValidGetCapabilities) {
        this.hasValidGetCapabilities = hasValidGetCapabilities;
    }
    // </editor-fold>
    
    public String getBbox() {
        return bbox;
    }
    
    public void setBbox(String bbox) {
        this.bbox = bbox;
    }
    
    public Set getReports() {
        return reports;
    }
    
    public void setReports(Set reports) {
        this.reports = reports;
    }
    
    private boolean isHasValidGetCapabilities() {
        return hasValidGetCapabilities;
    }
    
    public Set getReportStatus() {
        return reportStatus;
    }
    
    public void setReportStatus(Set reportStatus) {
        this.reportStatus = reportStatus;
    }
    
    public Account getAccount() {
        return account;
    }
    
    public void setAccount(Account account) {
        this.account = account;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    
    
    
}
