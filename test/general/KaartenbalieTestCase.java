package general;

import nl.b3p.testStreet.B3TestCase;

/**
 *
 * @author rachelle
 */
public class KaartenbalieTestCase extends B3TestCase {
    public KaartenbalieTestCase(String name){
        super(name);
    }
    
    protected nl.b3p.kaartenbalie.core.server.User generateUser(){
        nl.b3p.kaartenbalie.core.server.User user = new nl.b3p.kaartenbalie.core.server.User();
        user.setUsername("Beheerder");
        user.setPassword("dsfdes23423");
        user.setFirstName("Beheerder");
        user.setSurname("Beheerders");
        user.setEmailAddress("beheerder@b3partners.nl");
        
        return user;
    }
}
