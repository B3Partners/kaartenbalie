/*
 * Organization.java
 *
 * Created on 3 oktober 2006, 10:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core;

import java.util.ArrayList;

/**
 *
 * @author Nando De Goeij
 */

public class Organization {
    
    private long id;
    private String name;
    private String organizationStreet;
    private String organizationNumber;
    private String organizationAddition;
    private String organizationProvince;
    private String organizationPostbox;
    private String organizationBillingAddress;
    private String organizationVisitorsAddress;
    private String organizationTelephone;
    private String organizationFax;
    private ArrayList layerids;
    
    /**
     * Creates a new instance of Organization
     */
    public Organization() {
    }

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganizationStreet() {
        return organizationStreet;
    }

    public void setOrganizationStreet(String organizationStreet) {
        this.organizationStreet = organizationStreet;
    }
    
    public String getOrganizationNumber() {
        return organizationNumber;
    }

    public void setOrganizationNumber(String organizationNumber) {
        this.organizationNumber = organizationNumber;
    }

    public String getOrganizationAddition() {
        return organizationAddition;
    }

    public void setOrganizationAddition(String organizationAddition) {
        this.organizationAddition = organizationAddition;
    }

    public String getOrganizationProvince() {
        return organizationProvince;
    }

    public void setOrganizationProvince(String organizationProvince) {
        this.organizationProvince = organizationProvince;
    }

    public String getOrganizationPostbox() {
        return organizationPostbox;
    }

    public void setOrganizationPostbox(String organizationPostbox) {
        this.organizationPostbox = organizationPostbox;
    }

    public String getOrganizationBillingAddress() {
        return organizationBillingAddress;
    }

    public void setOrganizationBillingAddress(String organizationBillingAddress) {
        this.organizationBillingAddress = organizationBillingAddress;
    }

    public String getOrganizationVisitorsAddress() {
        return organizationVisitorsAddress;
    }

    public void setOrganizationVisitorsAddress(String organizationVisitorsAddress) {
        this.organizationVisitorsAddress = organizationVisitorsAddress;
    }

    public String getOrganizationTelephone() {
        return organizationTelephone;
    }

    public void setOrganizationTelephone(String organizationTelephone) {
        this.organizationTelephone = organizationTelephone;
    }

    public String getOrganizationFax() {
        return organizationFax;
    }

    public void setOrganizationFax(String organizationFax) {
        this.organizationFax = organizationFax;
    }
    
/*
    public ArrayList getLayerids() {
        return layerids;
    }

    public void setLayerid(ArrayList layerids) {
        this.layerids = layerids;
    }
*/

    
}