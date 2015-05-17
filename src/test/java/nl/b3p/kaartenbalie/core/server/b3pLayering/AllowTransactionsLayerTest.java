package nl.b3p.kaartenbalie.core.server.b3pLayering;

import java.util.HashMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author rachelle
 */
public class AllowTransactionsLayerTest extends ConfigLayerTest {

    public AllowTransactionsLayerTest(String name) {
        super(name);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        instance = new AllowTransactionsLayer();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        instance = null;
    }

    /**
     * Test of processConfig method, of class AllowTransactionsLayer.
     */
    @Test
    public void testProcessConfig() {
        try {
            HashMap<Object, Object> configMap = this.getParameterMap();

            instance.processConfig(configMap);
            assertTrue(configMap.containsKey(AllowTransactionsLayer.allowTransactions));
            assertTrue(configMap.containsKey(AllowTransactionsLayer.transactionsNeeded));
        } catch (Exception e) {
            fail("Exception : " + e.getLocalizedMessage());
        }
    }
}
