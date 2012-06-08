package nl.b3p.kaartenbalie.core.server.b3pLayering;

import org.junit.After;
import org.junit.Before;

/**
 *
 * @author rachelle
 */
public class BalanceLayerTest extends ConfigLayerTest {

    public BalanceLayerTest(String name) {
        super(name);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        instance = new BalanceLayer();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        instance = null;
    }
}
