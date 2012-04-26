package nl.b3p.kaartenbalie.service;

import general.KaartenbalieTestCase;
import java.security.Principal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author rachelle
 */
public class SecurityRealmTest extends KaartenbalieTestCase {
    private SecurityRealm instance;

    public SecurityRealmTest(String name) {
        super(name);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.instance = new SecurityRealm();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        this.instance = null;
    }

    /**
     * Test of authenticate method, of class SecurityRealm. Function should
     * return a null => user unknown
     */
    @Test
    public void testAuthenticate() {
        Principal user = this.instance.authenticate("admin", "admin");
        assertNull(user);
    }

    /**
     * Test of getAuthenticatedPrincipal method, of class SecurityRealm.
     * Function should return a null => user unknown
     */
    @Test
    public void testGetAuthenticatedPrincipal() {
        Principal user = this.instance.getAuthenticatedPrincipal("admin", "admin");
        assertNull(user);
    }

    /**
     * Test of isUserInRole method, of class SecurityRealm. Function should
     * return false => invalid principal
     */
    @Test
    public void testIsUserInRole() {
        assertFalse(this.instance.isUserInRole(null, "admin"));
    }
}
