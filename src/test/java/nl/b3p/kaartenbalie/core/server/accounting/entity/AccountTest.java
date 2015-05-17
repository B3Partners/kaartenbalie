package nl.b3p.kaartenbalie.core.server.accounting.entity;

import general.KaartenbalieTestCase;
import general.OrganizationOverwrite;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import nl.b3p.kaartenbalie.core.server.Organization;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author rachelle
 */
public class AccountTest extends KaartenbalieTestCase {

    private Account instance;

    public AccountTest(String name) {
        super(name);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        instance = new Account();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        instance = null;
    }

    /**
     * Test of getId method, of class Account.
     */
    @Test
    public void testGetId() {
        assertNull(instance.getId());
    }

    /**
     * Test of setId method, of class Account.
     */
    @Test
    public void testSetId() {
        Integer id = new Integer(10);
        instance.setId(id);
        assertEquals(id, instance.getId());
    }

    /**
     * Test of getOrganization method, of class Account.
     */
    @Test
    public void testGetOrganization() {
        assertNull(instance.getOrganization());
    }

    /**
     * Test of setOrganization method, of class Account.
     */
    @Test
    public void testSetOrganization() {
        Organization organization = new OrganizationOverwrite();
        instance.setOrganization(organization);
        assertEquals(organization, instance.getOrganization());
    }

    /**
     * Test of getCreditBalance method, of class Account.
     */
    @Test
    public void testGetCreditBalance() {
        BigDecimal expected = new BigDecimal(0);

        assertEquals(expected, instance.getCreditBalance());
    }

    /**
     * Test of setCreditBalance method, of class Account.
     */
    @Test
    public void testSetCreditBalance() {
        BigDecimal expected = new BigDecimal(10);

        instance.setCreditBalance(expected);
        assertEquals(expected, instance.getCreditBalance());
    }

    /**
     * Test of getTransactions method, of class Account.
     */
    @Test
    public void testGetTransactions() {
        assertNull(instance.getTransactions());
    }

    /**
     * Test of setTransactions method, of class Account.
     */
    @Test
    public void testSetTransactions() {
        Set transactions = new HashSet();
        transactions.add(new Transaction());

        instance.setTransactions(transactions);
        assertEquals(transactions, instance.getTransactions());
    }
}
