package general;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rachelle
 */
public class UserStumb {
    public static nl.b3p.kaartenbalie.core.server.User generateServerUser(){
        nl.b3p.kaartenbalie.core.server.User user   = new nl.b3p.kaartenbalie.core.server.User();
        //user.setId(1);
        user.setFirstName("Beheerder");
        user.setEmailAddress("Beheerder@b3partners.nl");
        user.setSurname("Beheerder");
        user.setUsername("Beheerder 1");
        
        user.setPassword("***REMOVED***");
        
        return user;
    }
}
