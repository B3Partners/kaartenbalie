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
package nl.b3p.kaartenbalie.core.server;

import java.util.HashSet;
import java.util.Set;
import nl.b3p.kaartenbalie.core.server.accounting.entity.Account;
import nl.b3p.wms.capabilities.Layer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Organization {

    private static final Log log = LogFactory.getLog(User.class);

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
    private Set users = new HashSet();
    private Set mainUsers = new HashSet();
    private Set billing = new HashSet();
    private Set layers = new HashSet();
    private Set wfsLayers = new HashSet();
    private Set reports = new HashSet();
    private Set reportStatus = new HashSet();
    private Account account;
    private boolean hasValidGetCapabilities;
    private String bbox;
    private String code;
    private boolean allowAccountingLayers;

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


    public Set getBilling() {
        return billing;
    }

    public void setBilling(Set billing) {
        this.billing = billing;
    }

    public Set getLayers() {
        return layers;
    }

    public void setLayers(Set layers) {
        this.layers = layers;
    }

    public void addLayer(Layer layer) {
        if (null == getLayers()) {
            setLayers(new HashSet());
        }
        getLayers().add(layer);
    }

    public Set getWfsLayers() {
        return wfsLayers;
    }

    public void setWfsLayers(Set wfsLayer) {
        this.wfsLayers = wfsLayer;
    }

    public void addWfsLayer(Layer layer) {
        if (null == getWfsLayers()) {
            setWfsLayers(new HashSet());
        }
        getWfsLayers().add(layer);
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

    public void setAllowAccountingLayers(boolean allowAccountingLayers) {
        this.allowAccountingLayers = allowAccountingLayers;
    }

    public boolean getAllowAccountingLayers() {
        return allowAccountingLayers;
    }

    /**
     * @return the users
     */
    public Set getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(Set users) {
        this.users = users;
    }

    /**
     * @return the mainUsers
     */
    public Set getMainUsers() {
        return mainUsers;
    }

    /**
     * @param mainUsers the mainUsers to set
     */
    public void setMainUsers(Set mainUsers) {
        this.mainUsers = mainUsers;
    }


}
