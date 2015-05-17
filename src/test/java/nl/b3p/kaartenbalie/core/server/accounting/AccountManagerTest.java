package nl.b3p.kaartenbalie.core.server.accounting;

import general.KaartenbalieTestCase;
import java.util.List;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.accounting.entity.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author rachelle
 */
public class AccountManagerTest extends KaartenbalieTestCase {

    private AccountManager instance;

    public AccountManagerTest(String name) {
        super(name);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        instance = AccountManager.getAccountManager(20);
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        instance = null;
    }

    /**
     * Test of getAccountManager method, of class AccountManager.
     */
    @Test
    public void testGetAccountManager() {
        int id = 10;

        try {
            AccountManager manager = AccountManager.getAccountManager(id);
        } catch (Exception e) {
            fail("Exception : " + e.getLocalizedMessage());
        }
    }

    /**
     * Test of prepareTransaction method, of class AccountManager.
     */
    @Test
    public void testPrepareTransaction() {
        try {
            assertNull(instance.prepareTransaction(10, ";"));
        } catch (Exception e) {
            fail("Exception : " + e.getLocalizedMessage());
        }
    }

    /**
     * Test of beginTLU method, of class AccountManager.
     */
    @Test
    public void testBeginTLU() {
        try {
            assertNull(instance.beginTLU());
        } catch (Exception e) {
            fail("Exception : " + e.getLocalizedMessage());
        }
    }

    /**
     * Test of getTLU method, of class AccountManager.
     */
    @Test
    public void testGetTLU() {
        try {
            assertNull(instance.getTLU());
        } catch (Exception e) {
            fail("Exception : " + e.getLocalizedMessage());
        }
    }

    /**
     * Test of endTLU method, of class AccountManager.
     */
    @Test
    public void testEndTLU() {
        try {
            instance.endTLU();
        } catch (Exception e) {
            fail("Exception : " + e.getLocalizedMessage());
        }
    }

    /**
     * Test of commitTransaction method, of class AccountManager.
     */
    @Test
    public void testCommitTransaction() {
        User user = this.generateUser();

        try {
            Transaction transaction = instance.beginTLU();

            instance.commitTransaction(transaction, user);

            AccountManager.setEnableAccounting(true);

            instance.commitTransaction(transaction, user);
        } catch (Exception e) {
            fail("Exception : " + e.getLocalizedMessage());
        }
    }

    /**
     * Test of getBalance method, of class AccountManager.
     */
    @Test
    public void testGetBalance() {
        try {
            instance.getBalance();

            AccountManager.setEnableAccounting(true);

            instance.getBalance();
            fail("Expecting a entity exception");
        } catch (Exception e) {
            String error = e.getLocalizedMessage();
            if (!error.contains("Unknown entity")) {
                fail("Exception : " + error);
            }
        }
    }

    /**
     * Test of getTransactions method, of class AccountManager.
     */
    @Test
    public void testGetTransactions_int_int() {
        try {
            List result = instance.getTransactions(10, 1);
        } catch (Exception e) {
            String error = e.getLocalizedMessage();

            if (!error.contains("Transaction is not mapped")) {
                fail("Exception : " + error);
            }
        }
    }

    /**
     * Test of getTransactions method, of class AccountManager.
     */
    @Test
    public void testGetTransactions_3args() {
        try {
            List result = instance.getTransactions(0, 10, 1);
        } catch (Exception e) {
            String error = e.getLocalizedMessage();

            if (!error.contains("Transaction is not mapped")) {
                fail("Exception : " + error);
            }
        }
    }

    /**
     * Test of setEnableAccounting method, of class AccountManager.
     */
    @Test
    public void testSetEnableAccounting() {
        AccountManager.setEnableAccounting(true);
        assertTrue(AccountManager.isEnableAccounting());
    }

    /**
     * Test of isEnableAccounting method, of class AccountManager.
     */
    @Test
    public void testIsEnableAccounting() {
        assertFalse(AccountManager.isEnableAccounting());
    }
}
