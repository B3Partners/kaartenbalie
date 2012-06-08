package general;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
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

    public KaartenbalieTestCase(String name) {
        super(name);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        try {
            ConfigStub config = new ConfigStub();
            DB = new MyEMFDatabase();
            DB.init(config);
            MyEMFDatabase.createEntityManager(MyEMFDatabase.MAIN_EM);
            this.entityManager = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);
        } catch (ServletException e) {
            System.out.println("Error initializing : " + e.getLocalizedMessage());
            throw (e);
        }
    }

    protected nl.b3p.kaartenbalie.core.server.User generateUser() {
        UserOverwrite user = new UserOverwrite();
        
        user.setIdTest(new Integer(1));
        user.setUsername("beheerder");
        user.setPassword("dsfdes23423");
        user.setFirstName("Beheerder");
        user.setSurname("Beheerders");
        user.setEmailAddress("beheerder@b3partners.nl");

        return user;
    }

    protected void checkNotMapped(Exception e) {
        String error = e.getLocalizedMessage();
        if (error.contains("Layer is not mapped")) {
            assertTrue(true);
        } else {
            fail("Exception " + error);
            assertTrue(false);
        }
    }
}
