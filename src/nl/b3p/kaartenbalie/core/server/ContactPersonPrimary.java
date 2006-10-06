/*
 * ContactPersonPrimary.java
 *
 * Created on 26 september 2006, 14:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server;

/**
 *
 * @author Nando De Goeij
 */
public class ContactPersonPrimary {
    
    private String contactPerson;
    private String contactOrganization;
    
    /** Creates a new instance of ContactPersonPrimary */
    public ContactPersonPrimary() {
    }

    public String getContactPerson() {
        return contactPerson;
    }
    
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }
    
    public String getContactOrganization() {
        return contactOrganization;
    }
    
    public void setContactOrganization(String contactOrganization) {
        this.contactOrganization = contactOrganization;
    }
    
    
    
}
