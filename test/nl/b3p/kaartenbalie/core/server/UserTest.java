package nl.b3p.kaartenbalie.core.server;

import general.KaartenbalieTestCase;
import general.OrganizationOverwrite;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import nl.b3p.kaartenbalie.service.servlet.CallWMSServlet;
import nl.b3p.servletAPI.HttpServletRequestStub;
import nl.b3p.wms.capabilities.Roles;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author rachelle
 */
public class UserTest extends KaartenbalieTestCase {

    private nl.b3p.kaartenbalie.core.server.User instance;

    public UserTest(String name) {
        super(name);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        instance = new nl.b3p.kaartenbalie.core.server.User();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getId method, of class User.
     */
    @Test
    public void testGetId() {
        assertNull(instance.getId());
    }

    /**
     * Test of getFirstName method, of class User.
     */
    @Test
    public void testGetFirstName() {
        assertNull(instance.getFirstName());
    }

    /**
     * Test of setFirstName method, of class User.
     */
    @Test
    public void testSetFirstName() {
        String firstName = "setFirstName";
        instance.setFirstName(firstName);
        assertStringEquals(firstName, instance.getFirstName());
    }

    /**
     * Test of getSurname method, of class User.
     */
    @Test
    public void testGetSurname() {
        assertNull(instance.getSurname());
    }

    /**
     * Test of setSurname method, of class User.
     */
    @Test
    public void testSetSurname() {
        String surname = "setSurname";
        instance.setSurname(surname);
        assertStringEquals(surname, instance.getSurname());
    }

    /**
     * Test of getEmailAddress method, of class User.
     */
    @Test
    public void testGetEmailAddress() {
        assertNull(instance.getEmailAddress());
    }

    /**
     * Test of setEmailAddress method, of class User.
     */
    @Test
    public void testSetEmailAddress() {
        String emailAddress = "setEmailAddress";
        instance.setEmailAddress(emailAddress);
        assertStringEquals(emailAddress, instance.getEmailAddress());
    }

    /**
     * Test of getUsername method, of class User.
     */
    @Test
    public void testGetUsername() {
        assertNull(instance.getUsername());
    }

    /**
     * Test of setUsername method, of class User.
     */
    @Test
    public void testSetUsername() {
        String username = "setUsername";
        instance.setUsername(username);
        assertStringEquals(username, instance.getUsername());
    }

    /**
     * Test of getPassword method, of class User.
     */
    @Test
    public void testGetPassword() {
        assertNull(instance.getPassword());
    }

    /**
     * Test of setPassword method, of class User.
     */
    @Test
    public void testSetPassword() {
        String password = "setPassword";
        instance.setPassword(password);
        assertStringEquals(password, instance.getPassword());
    }

    /**
     * Test of getOrganizations method, of class User.
     */
    @Test
    public void testGetOrganizations() {
        Set organizations = new HashSet();
        assertEquals(organizations, instance.getOrganizations());
    }

    /**
     * Test of getAllOrganizations method, of class User.
     */
    @Test
    public void testGetAllOrganizations() {
        Set organizations = new HashSet();
        assertEquals(organizations, instance.getAllOrganizations());

        Organization mainOrganization = new Organization();
        instance.setMainOrganization(mainOrganization);

        organizations.add(mainOrganization);
        assertEquals(organizations, instance.getAllOrganizations());
    }

    /**
     * Test of setOrganizations method, of class User.
     */
    @Test
    public void testSetOrganizations() {
        Set organizations = new HashSet();
        assertEquals(organizations, instance.getOrganizations());

        Organization organization = new Organization();
        instance.setOrganizations(organizations);

        organizations.add(organization);
        assertEquals(organizations, instance.getOrganizations());
    }

    /**
     * Test of addOrganization method, of class User.
     */
    @Test
    public void testAddOrganization() {
        Set organizations = new HashSet();
        assertEquals(organizations, instance.getOrganizations());

        Organization organization = new Organization();
        instance.addOrganization(organization);

        organizations.add(organization);
        assertEquals(organizations, instance.getOrganizations());
    }

    /**
     * Test of getOrganisationCodes method, of class User.
     */
    @Test
    public void testGetOrganisationCodes() {
        assertStringEquals("", instance.getOrganisationCodes());
    }

    /**
     * Test of getMainOrganizationId method, of class User.
     */
    @Test
    public void testGetMainOrganizationId() {
        Integer id = new Integer(10);
        OrganizationOverwrite mainOrganization = new OrganizationOverwrite();
        mainOrganization.setTestId(id);
        instance.setMainOrganization((Organization) mainOrganization);

        assertEquals(id, instance.getMainOrganizationId());
    }

    /**
     * Test of getOrganizationIds method, of class User.
     */
    @Test
    public void testGetOrganizationIds() {
        Integer id = new Integer(10);
        OrganizationOverwrite mainOrganization = new OrganizationOverwrite();
        mainOrganization.setTestId(id);
        instance.addOrganization((Organization) mainOrganization);

        Integer[] result = instance.getOrganizationIds();
        assertEquals(id, result[0]);
    }

    /**
     * Test of setMainOrganization method, of class User.
     */
    @Test
    public void testSetMainOrganization() {
        Integer id = new Integer(10);
        OrganizationOverwrite mainOrganization = new OrganizationOverwrite();
        mainOrganization.setTestId(id);
        instance.setMainOrganization((Organization) mainOrganization);

        assertEquals(mainOrganization, instance.getMainOrganization());
    }

    /**
     * Test of getMainOrganization method, of class User.
     */
    @Test
    public void testGetMainOrganization() {
        assertNull(instance.getMainOrganization());
    }

    /**
     * Test of getLayers method, of class User.
     */
    @Test
    public void testGetLayers() {
        assertNull(instance.getLayers());
    }

    /**
     * Test of getName method, of class User.
     */
    @Test
    public void testGetName() {
        assertNull(instance.getName());
    }

    /**
     * Test of getPersonalURL method, of class User.
     */
    @Test
    public void testGetPersonalURL_HttpServletRequest() {
        HttpServletRequestStub request = new HttpServletRequestStub();
        String URL = "";

        assertNull(instance.getPersonalURL(request));

        instance.setPersonalURL(URL);
        StringBuffer expected = CallWMSServlet.createBaseUrl(request);
        expected.append("/services/");
        assertStringEquals(expected.toString(), instance.getPersonalURL(request));

        URL = "http://example.com";
        instance.setPersonalURL(URL);
        assertStringEquals(URL, instance.getPersonalURL(request));
    }

    /**
     * Test of getPersonalURL method, of class User.
     */
    @Test
    public void testGetPersonalURL_0args() {
        assertNull(instance.getPersonalURL());
    }

    /**
     * Test of setPersonalURL method, of class User.
     */
    @Test
    public void testSetPersonalURL() {
        String personalURL = "setPersonalURL";
        instance.setPersonalURL(personalURL);
        assertStringEquals(personalURL, instance.getPersonalURL());
    }

    /**
     * Test of getTimeout method, of class User.
     */
    @Test
    public void testGetTimeout() {
        assertNull(instance.getTimeout());
    }

    /**
     * Test of setTimeout method, of class User.
     */
    @Test
    public void testSetTimeout() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(112, 1, 1);
        Date timeout = calendar.getTime();

        instance.setTimeout(timeout);
        assertEquals(timeout, instance.getTimeout());
    }

    /**
     * Test of getDefaultGetMap method, of class User.
     */
    @Test
    public void testGetDefaultGetMap() {
        assertNull(instance.getDefaultGetMap());
    }

    /**
     * Test of setDefaultGetMap method, of class User.
     */
    @Test
    public void testSetDefaultGetMap() {
        String map = "setDefaultGetMap";
        instance.setDefaultGetMap(map);
        assertStringEquals(map, instance.getDefaultGetMap());
    }

    /**
     * Test of getRoles method, of class User.
     */
    @Test
    public void testGetRoles() {
        Set roles = new HashSet();

        assertEquals(roles, instance.getRoles());
    }

    /**
     * Test of setRoles method, of class User.
     */
    @Test
    public void testSetRoles() {
        Set roles = new HashSet();
        roles.add(new Roles());

        instance.setRoles(roles);
        assertEquals(roles, instance.getRoles());
    }

    /**
     * Test of addRole method, of class User.
     */
    @Test
    public void testAddRole() {
        Set roles = new HashSet();
        Roles role = new Roles();
        roles.add(role);

        instance.addRole(role);
        assertEquals(roles, instance.getRoles());
    }

    /**
     * Test of deleteRole method, of class User.
     */
    @Test
    public void testDeleteRole() {
        Set roles = new HashSet();
        Roles role1 = new Roles();
        Roles role2 = new Roles();
        Roles role3 = new Roles();

        roles.add(role1);
        roles.add(role2);
        roles.add(role3);

        instance.setRoles(roles);
        assertEquals(roles, instance.getRoles());

        instance.deleteRole(role2);
        roles.remove(role2);
        assertEquals(roles, instance.getRoles());
    }

    /**
     * Test of checkRole method, of class User.
     */
    @Test
    public void testCheckRole() {
        Set roles = new HashSet();
        String role = "Admin";
        Roles role1 = new Roles();
        Roles role2 = new Roles();
        Roles role3 = new Roles();

        role1.setRole(role);

        roles.add(role1);
        roles.add(role2);
        roles.add(role3);

        assertFalse(instance.checkRole(role));

        instance.setRoles(roles);
        assertTrue(instance.checkRole(role));
    }

    /**
     * Test of getRolesAsString method, of class User.
     */
    @Test
    public void testGetRolesAsString() {
        Set roles = new HashSet();
        String role = "Admin";
        String role_2 = "User";
        Roles role1 = new Roles();
        Roles role2 = new Roles();

        role1.setRole(role);
        role2.setRole(role_2);

        roles.add(role1);
        roles.add(role2);

        instance.setRoles(roles);
        String result = instance.getRolesAsString();
        if (!result.contains(role) || !result.contains(role_2)) {
            fail("Expected to find " + role + " and " + role_2 + " but found " + result);
        }
    }

    /**
     * Test of getIps method, of class User.
     */
    @Test
    public void testGetIps() {
        Set ips = new HashSet();
        assertEquals(ips, instance.getIps());
    }

    /**
     * Test of setIps method, of class User.
     */
    @Test
    public void testSetIps() {
        Set ips = new HashSet();
        ips.add("192.168.0.1");

        instance.setIps(ips);
        assertEquals(ips, instance.getIps());
    }

    /**
     * Test of addIps method, of class User.
     */
    @Test
    public void testAddIps() {
        Set ips = new HashSet();
        String ip = "192.168.0.1";
        ips.add(ip);

        instance.addIps(ip);
        assertEquals(ips, instance.getIps());
    }

    /**
     * Test of equals method, of class User.
     */
    @Test
    public void testEquals() {
        User user = this.generateUser();
        assertFalse(instance.equals(user));
        String username = "Username";
        user.setUsername(username);

        instance.setUsername(username);

        assertTrue(instance.equals(user));
    }

    /**
     * Test of hashCode method, of class User.
     */
    @Test
    public void testHashCode() {
        int expResult = 3 * 29;
        assertEquals(expResult, instance.hashCode());
    }

    /**
     * Test of isValidCode method, of class User.
     */
    @Test
    public void testIsValidCode() {
        assertFalse(User.isValidCode(null));

        String hex = "fe800000000000000202b3fffe1e8329";
        boolean result = User.isValidCode(hex);
        assertTrue(result);
    }

    /**
     * Test of createCode method, of class User.
     */
    @Test
    public void testCreateCode() {
        User user = this.generateUser();
        Date newDate = new Date();
        HttpServletRequest request = new HttpServletRequestStub();

        try {
            User.createCode(user, newDate, request);
        } catch (Exception e) {
            fail("Exception : " + e.getLocalizedMessage());
        }
    }
}
