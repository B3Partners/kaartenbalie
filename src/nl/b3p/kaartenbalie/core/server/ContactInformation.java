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
    
    private Integer id;
    private String contactPerson;
    private String contactPosition;
    private String contactOrganization;
    private String address;
    private String addressType;
    private String postcode;
    private String city;
    private String stateOrProvince;
    private String country;
    private String voiceTelephone;
    private String fascimileTelephone;
    private String emailAddress;
    private ServiceProvider serviceProvider;
    
    public ContactInformation() {
    	contactPerson = "Chris van Lith";
	    contactPosition = "Software Developer";
	    contactOrganization = "B3Partners";
	    address = "Zonnebaan 12C";
	    addressType = "Postal";
	    postcode = "3542 EC";
	    city = "Utrecht";
	    stateOrProvince = "Utrecht";
	    country = "The Netherlands";
	    voiceTelephone = "0(031) 30 214 20 81";
	    fascimileTelephone = "";
	    emailAddress = "info@b3p.nl";
    }

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPosition() {
        return contactPosition;
    }

    public void setContactPosition(String contactPosition) {
        this.contactPosition = contactPosition;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateOrProvince() {
        return stateOrProvince;
    }

    public void setStateOrProvince(String stateOrProvince) {
        this.stateOrProvince = stateOrProvince;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getVoiceTelephone() {
        return voiceTelephone;
    }

    public void setVoiceTelephone(String voiceTelephone) {
        this.voiceTelephone = voiceTelephone;
    }

    public String getFascimileTelephone() {
        return fascimileTelephone;
    }

    public void setFascimileTelephone(String fascimileTelephone) {
        this.fascimileTelephone = fascimileTelephone;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public String getContactOrganization() {
        return contactOrganization;
    }

    public void setContactOrganization(String contactOrganization) {
        this.contactOrganization = contactOrganization;
    }
    
    
    /*
    The String class does not allow you to clone String objects because Java has a policy where it shares a single 
    String object among multiple references, and cloning violates that policy. The String sharing policy helps reduce 
    a program's memory requirements. For example, suppose you create a String object that contains a sequence of 100 
    characters. (Note: A String object treats a string as a sequence of characters.) Because each character occupies 
    two bytes (remember Unicode?), 200 bytes of storage dedicate to containing the string. Now, suppose you need an 
    array of 1,000 copies of the object. If each array element references a separate String object containing the same 
    sequence of characters, the program will require 200,000 bytes to hold all the characters in all copies of the 
    string. However, by sharing the single String object, the program only requires 200 characters for a single string, 
    and each element in the array references that same String object. Because of the String sharing policy, strings are 
    considered immutable.
    */
    public Object clone() {
        ContactInformation cloneCI      = new ContactInformation();
        if (null != this.id) {
            cloneCI.id                  = new Integer(this.id.intValue());
        }
        if (null != this.contactPerson) {
            cloneCI.contactPerson       = new String(this.contactPerson);
        }
        if (null != this.contactPosition) {
            cloneCI.contactPosition     = new String(this.contactPosition);
        }
        if (null != this.contactOrganization) {
            cloneCI.contactOrganization = new String(this.contactOrganization);
        }
        if (null != this.address) {
            cloneCI.address             = new String(this.address);
        }
        if (null != this.addressType) {
            cloneCI.addressType         = new String(this.addressType);
        }
        if (null != this.postcode) {
            cloneCI.postcode            = new String(this.postcode);
        }
        if (null != this.city) {
            cloneCI.city                = new String(this.city);
        }
        if (null != this.stateOrProvince) {
            cloneCI.stateOrProvince     = new String(this.stateOrProvince);
        }
        if (null != this.country) {
            cloneCI.country             = new String(this.country);
        }
        if (null != this.voiceTelephone) {
            cloneCI.voiceTelephone      = new String(this.voiceTelephone);
        }
        if (null != this.fascimileTelephone) {
            cloneCI.fascimileTelephone  = new String(this.fascimileTelephone);
        }
        if (null != this.emailAddress) {
            cloneCI.emailAddress        = new String(this.emailAddress);
        }
        return cloneCI;
    }
    
    public String toString(String tabulator) {
    	StringBuilder result = new StringBuilder();
    	final String newLine = System.getProperty("line.separator");
    	
    	if(null == this.getContactPerson() && null == this.getContactOrganization() && 
    		null == this.getContactPosition() && null == this.getAddress() &&
	    	null == this.getCity() && null == this.getVoiceTelephone() &&
	    	null == this.getFascimileTelephone() && null == this.getEmailAddress()) {
    		return result.toString();
    	}
    	    						
    	result.append(tabulator + "<ContactInformation>\n");
        result.append(tabulator + "\t<ContactPersonPrimary>\n");
        if(null != this.getContactPerson()) {
        	result.append(tabulator + "\t\t<ContactPerson>" + this.getContactPerson() + "</ContactPerson>\n");
        }
        if(null != this.getContactOrganization()) {
        	result.append(tabulator + "\t\t<ContactOrganization>" + this.getContactOrganization() + "</ContactOrganization>\n");
        }
        result.append(tabulator + "\t</ContactPersonPrimary>\n");
        if(null != this.getContactPosition()) {
        	result.append(tabulator + "\t<ContactPosition>" + this.getContactPosition() + "</ContactPosition>\n");
        }
        result.append(tabulator + "\t<ContactAddress>\n");
        if(null != this.getAddressType()) {
        	result.append(tabulator + "\t\t<AddressType>" + this.getAddressType() + "</AddressType>\n");
        }
        if(null != this.getAddress()) {
        	result.append(tabulator + "\t\t<Address>" + this.getAddress() + "</Address>\n");
        }
        if(null != this.getCity()) {
        	result.append(tabulator + "\t\t<City>" + this.getCity() + "</City>\n");
        }
        if(null != this.getStateOrProvince()) {
        	result.append(tabulator + "\t\t<StateOrProvince>" + this.getStateOrProvince() + "</StateOrProvince>\n");
        }
        if(null != this.getPostcode()) {
        	result.append(tabulator + "\t\t<PostCode>" + this.getPostcode() + "</PostCode>\n");
        }
        if(null != this.getCountry()) {
        	result.append(tabulator + "\t\t<Country>" + this.getCountry() + "</Country>\n");
        }
        result.append(tabulator + "\t</ContactAddress>\n");
        if(null != this.getVoiceTelephone()) {
        	result.append(tabulator + "\t<ContactVoiceTelephone>" + this.getVoiceTelephone() + "</ContactVoiceTelephone>\n");
        }
        if(null != this.getFascimileTelephone()) {
        	result.append(tabulator + "\t<ContactFacsimileTelephone>" + this.getFascimileTelephone() + "</ContactFacsimileTelephone>\n");
        }
        if(null != this.getEmailAddress()) {
        	result.append(tabulator + "\t<ContactElectronicMailAddress>" + this.getEmailAddress() + "</ContactElectronicMailAddress>\n");
        }        
        result.append(tabulator + "</ContactInformation>\n");
    	
    	return result.toString();
    }
}