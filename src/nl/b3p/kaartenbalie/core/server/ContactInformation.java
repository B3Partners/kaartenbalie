/*
 * ContactInformation.java
 *
 * Created on 26 september 2006, 14:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server;

/**
 *
 * @author Nando De Goeij
 */
public class ContactInformation {
    
    private ContactPersonPrimary contactPersonPrimary;
    private ContactAddress contactAddress;
    private String contactPosition;
    private String contactVoiceTelephone;
    private String contactFacsimileTelephone;
    private String contactElectronicMailAddress;
    
    /** Creates a new instance of ContactInformation */

    public ContactPersonPrimary getContactPersonPrimary() {
        return contactPersonPrimary;
    }

    public void setContactPersonPrimary(ContactPersonPrimary contactPersonPrimary) {
        this.contactPersonPrimary = contactPersonPrimary;
    }

    public ContactAddress getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(ContactAddress contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getContactPosition() {
        return contactPosition;
    }

    public void setContactPosition(String contactPosition) {
        this.contactPosition = contactPosition;
    }

    public String getContactVoiceTelephone() {
        return contactVoiceTelephone;
    }

    public void setContactVoiceTelephone(String contactVoiceTelephone) {
        this.contactVoiceTelephone = contactVoiceTelephone;
    }

    public String getContactFacsimileTelephone() {
        return contactFacsimileTelephone;
    }

    public void setContactFacsimileTelephone(String contactFacsimileTelephone) {
        this.contactFacsimileTelephone = contactFacsimileTelephone;
    }
    
    public String getContactElectronicMailAddress() {
        return contactElectronicMailAddress;
    }

    public void setContactElectronicMailAddress(String contactElectronicMailAddress) {
        this.contactElectronicMailAddress = contactElectronicMailAddress;
    }
    
    
}
