package general;

import javax.persistence.EntityManager;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.servletAPI.ConfigStub;
import nl.b3p.testStreet.B3TestCase;

/**
 *
 * @author rachelle
 */
public class KaartenbalieTestCase extends B3TestCase {
    protected MyEMFDatabase DB;
    protected EntityManager entityManager;
    
    public KaartenbalieTestCase(String name){
        super(name);
    }
    
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        ConfigStub config    = new ConfigStub();
        DB                   = new MyEMFDatabase();
        DB.init(config);
        MyEMFDatabase.createEntityManager(MyEMFDatabase.MAIN_EM);
        this.entityManager   = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);
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
