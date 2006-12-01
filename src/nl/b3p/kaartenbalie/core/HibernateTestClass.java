/*
 * HibernateTestClass.java
 *
 * Created on 10 oktober 2006, 11:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core;

import nl.b3p.kaartenbalie.core.server.ContactInformation;
import nl.b3p.kaartenbalie.core.server.ServiceProvider;
import org.hibernate.*;
import nl.b3p.kaartenbalie.service.MyDatabase;

/**
 *
 * @author Nando De Goeij
 */
public class HibernateTestClass {
    
    /** Creates a new instance of HibernateTestClass */
    public HibernateTestClass() {      
        try {
            HibernateUtil.currentSession().beginTransaction();
            
            ContactInformation ci = new ContactInformation();
            ci.setAddress("Dahliastraat");
            ci.setAddressType("Personal");
            ci.setCity("Utrecht");
            ci.setContactPerson("Nando de Goeij");
            ci.setContactPosition("President");
            ci.setCountry("The Netherlands");
 //           ci.setEmailaddress("degoeij@orange.nl");
            ci.setFascimileTelephone("030-2433416");
            ci.setPostcode("3551SV");
            ci.setStateOrProvince("Utrecht");
 //           ci.setVoiceTelehpone("030-2433416");

            ServiceProvider sp = new ServiceProvider();
            sp.setAbstracts("This is just a test service, if it works more info will follow.");
            sp.setAccessConstraints("None");
            sp.setContactInformation(ci);
            sp.setFees("None");
            sp.setName("My service provider");
            sp.setTitle("Nando services");
            
            HibernateUtil.currentSession().save(sp);
            
            HibernateUtil.currentSession().getTransaction().commit();
        } catch (Exception e) {
            HibernateUtil.currentSession().getTransaction().rollback();
        }
    }
    
    public static void main(String [] args) {
        HibernateTestClass htc = new HibernateTestClass();
    }    
}