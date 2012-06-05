package nl.b3p.kaartenbalie.core.server.accounting.entity;

import general.KaartenbalieTestCase;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author rachelle
 */
public class LayerPricingTest extends KaartenbalieTestCase {

    private LayerPricing instance;

    public LayerPricingTest(String name) {
        super(name);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        instance = new LayerPricing();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        instance = null;
    }

    /**
     * Test of getPlanType method, of class LayerPricing.
     */
    @Test
    public void testGetPlanType() {
        assertEquals(LayerPricing.PAY_PER_REQUEST, instance.getPlanType());
    }

    /**
     * Test of setPlanType method, of class LayerPricing.
     */
    @Test
    public void testSetPlanType() {
        int planType = 5;
        instance.setPlanType(planType);
        assertEquals(planType, instance.getPlanType());
    }

    /**
     * Test of getValidFrom method, of class LayerPricing.
     */
    @Test
    public void testGetValidFrom() {
        assertNull(instance.getValidFrom());
    }

    /**
     * Test of setValidFrom method, of class LayerPricing.
     */
    @Test
    public void testSetValidFrom() {
        Date validFrom = this.generateDate();

        instance.setValidFrom(validFrom);
        assertEquals(validFrom, instance.getValidFrom());
    }

    /**
     * Test of getValidUntil method, of class LayerPricing.
     */
    @Test
    public void testGetValidUntil() {
        assertNull(instance.getValidUntil());
    }

    /**
     * Test of setValidUntil method, of class LayerPricing.
     */
    @Test
    public void testSetValidUntil() {
        Date validUntil = this.generateDate();

        instance.setValidUntil(validUntil);
        assertEquals(validUntil, instance.getValidUntil());
    }

    /**
     * Test of getCreationDate method, of class LayerPricing.
     */
    @Test
    public void testGetCreationDate() {
        assertNotNull(instance.getCreationDate());
    }

    /**
     * Test of setCreationDate method, of class LayerPricing.
     */
    @Test
    public void testSetCreationDate() {
        Date creationDate = this.generateDate();
        instance.setCreationDate(creationDate);
        assertEquals(creationDate, instance.getCreationDate());
    }

    /**
     * Test of getUnitPrice method, of class LayerPricing.
     */
    @Test
    public void testGetUnitPrice() {
        assertNull(instance.getUnitPrice());
    }

    /**
     * Test of setUnitPrice method, of class LayerPricing.
     */
    @Test
    public void testSetUnitPrice() {
        BigDecimal unitPrice = new BigDecimal(25);
        instance.setUnitPrice(unitPrice);
        assertEquals(unitPrice, instance.getUnitPrice());
    }

    /**
     * Test of getId method, of class LayerPricing.
     */
    @Test
    public void testGetId() {
        assertNull(instance.getId());
    }

    /**
     * Test of setId method, of class LayerPricing.
     */
    @Test
    public void testSetId() {
        Integer id = new Integer(100);
        instance.setId(id);
        assertEquals(id, instance.getId());
    }

    /**
     * Test of getLayerIsFree method, of class LayerPricing.
     */
    @Test
    public void testGetLayerIsFree() {
        assertFalse(instance.getLayerIsFree());
    }

    /**
     * Test of setLayerIsFree method, of class LayerPricing.
     */
    @Test
    public void testSetLayerIsFree() {
        Boolean layerIsFree = true;
        instance.setLayerIsFree(layerIsFree);
        assertTrue(instance.getLayerIsFree());
    }

    /**
     * Test of getLayerName method, of class LayerPricing.
     */
    @Test
    public void testGetLayerName() {
        assertNull(instance.getLayerName());
    }

    /**
     * Test of setLayerName method, of class LayerPricing.
     */
    @Test
    public void testSetLayerName() {
        instance.setLayerName(layerName);
        assertStringEquals(layerName, instance.getLayerName());
    }

    /**
     * Test of getServerProviderPrefix method, of class LayerPricing.
     */
    @Test
    public void testGetServerProviderPrefix() {
        assertNull(instance.getServerProviderPrefix());
    }

    /**
     * Test of setServerProviderPrefix method, of class LayerPricing.
     */
    @Test
    public void testSetServerProviderPrefix() {
        String serverProviderPrefix = "WWW";
        instance.setServerProviderPrefix(serverProviderPrefix);
        assertStringEquals(serverProviderPrefix, instance.getServerProviderPrefix());
    }

    /**
     * Test of getDeletionDate method, of class LayerPricing.
     */
    @Test
    public void testGetDeletionDate() {
        assertNull(instance.getDeletionDate());
    }

    /**
     * Test of setDeletionDate method, of class LayerPricing.
     */
    @Test
    public void testSetDeletionDate() {
        Date deletionDate = this.generateDate();
        instance.setDeletionDate(deletionDate);
        assertEquals(deletionDate, instance.getDeletionDate());
    }

    /**
     * Test of getService method, of class LayerPricing.
     */
    @Test
    public void testGetService() {
        assertNull(instance.getService());
    }

    /**
     * Test of setService method, of class LayerPricing.
     */
    @Test
    public void testSetService() {
        String service = "setService";
        instance.setService(service);
        assertStringEquals(service, instance.getService());
    }

    /**
     * Test of getOperation method, of class LayerPricing.
     */
    @Test
    public void testGetOperation() {
        assertNull(instance.getOperation());
    }

    /**
     * Test of setOperation method, of class LayerPricing.
     */
    @Test
    public void testSetOperation() {
        String operation = "setOperation";
        instance.setOperation(operation);
        assertStringEquals(operation, instance.getOperation());
    }

    /**
     * Test of getMinScale method, of class LayerPricing.
     */
    @Test
    public void testGetMinScale() {
        assertNull(instance.getMinScale());
    }

    /**
     * Test of setMinScale method, of class LayerPricing.
     */
    @Test
    public void testSetMinScale() {
        BigDecimal minScale = new BigDecimal(1);
        instance.setMinScale(minScale);
        assertEquals(minScale, instance.getMinScale());
    }

    /**
     * Test of getMaxScale method, of class LayerPricing.
     */
    @Test
    public void testGetMaxScale() {
        assertNull(instance.getMaxScale());
    }

    /**
     * Test of setMaxScale method, of class LayerPricing.
     */
    @Test
    public void testSetMaxScale() {
        BigDecimal maxScale = new BigDecimal(50);
        instance.setMaxScale(maxScale);
        assertEquals(maxScale, instance.getMaxScale());
    }

    /**
     * Test of getProjection method, of class LayerPricing.
     */
    @Test
    public void testGetProjection() {
        assertNull(instance.getProjection());
    }

    /**
     * Test of setProjection method, of class LayerPricing.
     */
    @Test
    public void testSetProjection() {
        String projection = "setProjection";
        instance.setProjection(projection);
        assertStringEquals(projection, instance.getProjection());
    }

    /**
     * Generates a date object
     *
     * @return a date object
     */
    private Date generateDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(112, 1, 1);
        return calendar.getTime();
    }
}
