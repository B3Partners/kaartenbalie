package nl.b3p.kaartenbalie.core.server.accounting;

import general.KaartenbalieTestCase;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPriceComposition;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPricing;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author rachelle
 */
public class LayerCalculatorTest extends KaartenbalieTestCase {

    private LayerCalculator instance;

    public LayerCalculatorTest(String name) {
        super(name);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        instance = new LayerCalculator();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        instance = null;
    }

    /**
     * Test of calculateLayerComplete method, of class LayerCalculator.
     */
    @Test
    public void testCalculateLayerComplete() {
        String serverProviderPrefix = "";
        Date validationDate = new Date();
        String projection = "";
        BigDecimal scale = BigDecimal.ONE;
        BigDecimal units = BigDecimal.ONE;
        int planType = 1;
        String service = "";
        String operation = "";

        try {
            assertNull(instance.calculateLayerComplete(serverProviderPrefix, layerName, validationDate, projection, scale, units, planType, service, operation));

            AccountManager.setEnableAccounting(true);

            LayerPriceComposition tCL = instance.calculateLayerComplete(serverProviderPrefix, layerName, validationDate, projection, scale, units, planType, service, operation);

            fail("Function should trow a QuerySyntaxException");
        } catch (Exception e) {
            String error = e.getLocalizedMessage();
            if (!error.contains("LayerPricing is not mapped")) {
                fail("Exception : " + error);
            }
        }
    }

    /**
     * Test of getSpLayerPricingList method, of class LayerCalculator.
     */
    @Test
    public void testGetSpLayerPricingList() {
        Date validationDate = new Date();
        String service = "";

        try {
            instance.getSpLayerPricingList(layerName, validationDate, service);

            fail("Function should trow a QuerySyntaxException");
        } catch (Exception e) {
            String error = e.getLocalizedMessage();
            if (!error.contains("LayerPricing is not mapped")) {
                fail("Exception : " + error);
            }
        }
    }

    /**
     * Test of ActiveLayerPricingExists method, of class LayerCalculator.
     */
    @Test
    public void testActiveLayerPricingExists() {
        String serverProviderPrefix = "";
        Date validationDate = new Date();
        int planType = 1;
        String service = "";
        String operation = "";

        boolean result = instance.ActiveLayerPricingExists(serverProviderPrefix, layerName, validationDate, planType, service, operation);
        assertFalse(result);
    }

    /**
     * Test of getActiveLayerPricing method, of class LayerCalculator.
     */
    @Test
    public void testGetActiveLayerPricing() {
        String serverProviderPrefix = "";
        Date validationDate = new Date();
        String projection = "";
        BigDecimal scale = BigDecimal.ONE;
        int planType = 1;
        String service = "";
        String operation = "";

        try {
            instance.getActiveLayerPricing(serverProviderPrefix, layerName, validationDate, projection, scale, planType, service, operation);

            fail("Function should trow a QuerySyntaxException");
        } catch (Exception e) {
            String error = e.getLocalizedMessage();
            if (!error.contains("LayerPricing is not mapped")) {
                fail("Exception : " + error);
            }
        }
    }

    /**
     * Test of calculateLayer method, of class LayerCalculator.
     */
    @Test
    public void testCalculateLayer() {
        String serverProviderPrefix = "";
        Date validationDate = new Date();
        String projection = "";
        BigDecimal scale = BigDecimal.ONE;
        BigDecimal units = BigDecimal.ONE;
        int planType = 1;
        String service = "";
        String operation = "";

        try {
            instance.calculateLayer(serverProviderPrefix, layerName, validationDate, projection, scale, units, planType, service, operation);

            fail("Function should trow a QuerySyntaxException");
        } catch (Exception e) {
            String error = e.getLocalizedMessage();
            if (!error.contains("LayerPricing is not mapped")) {
                fail("Exception : " + error);
            }
        }
    }

    /**
     * Test of closeEntityManager method, of class LayerCalculator.
     */
    @Test
    public void testCloseEntityManager() {
        try {
            instance.closeEntityManager();
        } catch (Exception e) {
            fail("Exception : " + e.getLocalizedMessage());
        }
    }
}
