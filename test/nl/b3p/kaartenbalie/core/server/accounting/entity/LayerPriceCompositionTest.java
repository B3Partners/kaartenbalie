package nl.b3p.kaartenbalie.core.server.accounting.entity;

import general.KaartenbalieTestCase;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author rachelle
 */
public class LayerPriceCompositionTest extends KaartenbalieTestCase {
    private LayerPriceComposition instance;
    
    public LayerPriceCompositionTest(String name) {
        super(name);
    }
    
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        instance    = new LayerPriceComposition();
    }
    
    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        
        instance    = null;
    }

    /**
     * Test of getId method, of class LayerPriceComposition.
     */
    @Test
    public void testGetId() {
        assertNull(instance.getId());
    }

    /**
     * Test of setId method, of class LayerPriceComposition.
     */
    @Test
    public void testSetId() {
        Integer id  = new Integer(10);
        
        instance.setId(id);
        assertEquals(id,instance.getId());
    }

    /**
     * Test of getLayerIsFree method, of class LayerPriceComposition.
     */
    @Test
    public void testGetLayerIsFree() {
        assertFalse(instance.getLayerIsFree());
    }

    /**
     * Test of setLayerIsFree method, of class LayerPriceComposition.
     */
    @Test
    public void testSetLayerIsFree() {
        Boolean layerIsFree = true;
        
        instance.setLayerIsFree(layerIsFree);
        assertTrue(instance.getLayerIsFree());
    }

    /**
     * Test of getLayerPrice method, of class LayerPriceComposition.
     */
    @Test
    public void testGetLayerPrice() {
        assertNull(instance.getLayerPrice());
    }

    /**
     * Test of setLayerPrice method, of class LayerPriceComposition.
     */
    @Test
    public void testSetLayerPrice() {
        BigDecimal layerPrice   = new BigDecimal(10);
        instance.setLayerPrice(layerPrice);
        assertEquals(layerPrice,instance.getLayerPrice());
    }

    /**
     * Test of getServerProviderPrefix method, of class LayerPriceComposition.
     */
    @Test
    public void testGetServerProviderPrefix() {
        assertNull(instance.getServerProviderPrefix());
    }

    /**
     * Test of setServerProviderPrefix method, of class LayerPriceComposition.
     */
    @Test
    public void testSetServerProviderPrefix() {
        String serverProviderPrefix = "www";
        instance.setServerProviderPrefix(serverProviderPrefix);
        assertStringEquals(serverProviderPrefix,instance.getServerProviderPrefix());
    }

    /**
     * Test of getLayerName method, of class LayerPriceComposition.
     */
    @Test
    public void testGetLayerName() {
        assertNull(instance.getLayerName());
    }

    /**
     * Test of setLayerName method, of class LayerPriceComposition.
     */
    @Test
    public void testSetLayerName() {
        instance.setLayerName(layerName);
        assertStringEquals(layerName,instance.getLayerName());
    }

    /**
     * Test of getCalculationDate method, of class LayerPriceComposition.
     */
    @Test
    public void testGetCalculationDate() {
        assertNull(instance.getCalculationDate());
    }

    /**
     * Test of setCalculationDate method, of class LayerPriceComposition.
     */
    @Test
    public void testSetCalculationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(112,1,1);
        Date calculationDate  = calendar.getTime();
        
        instance.setCalculationDate(calculationDate);
        assertEquals(calculationDate,instance.getCalculationDate());
    }

    /**
     * Test of getPlanType method, of class LayerPriceComposition.
     */
    @Test
    public void testGetPlanType() {
        assertEquals(0,instance.getPlanType());
    }

    /**
     * Test of setPlanType method, of class LayerPriceComposition.
     */
    @Test
    public void testSetPlanType() {
        int planType = 10;
        instance.setPlanType(planType);
        assertEquals(planType,instance.getPlanType());
    }

    /**
     * Test of getUnits method, of class LayerPriceComposition.
     */
    @Test
    public void testGetUnits() {
        assertNull(instance.getUnits());
    }

    /**
     * Test of setUnits method, of class LayerPriceComposition.
     */
    @Test
    public void testSetUnits() {
        BigDecimal units = new BigDecimal(25);
        instance.setUnits(units);
        assertEquals(units,instance.getUnits());
    }

    /**
     * Test of getMethod method, of class LayerPriceComposition.
     */
    @Test
    public void testGetMethod() {
        assertEquals(LayerPriceComposition.METHOD_NONE,instance.getMethod());
    }

    /**
     * Test of setMethod method, of class LayerPriceComposition.
     */
    @Test
    public void testSetMethod() {
        int method = 5;
        
        instance.setMethod(method);
        assertEquals(method,instance.getMethod());
    }

    /**
     * Test of getCalculationTime method, of class LayerPriceComposition.
     */
    @Test
    public void testGetCalculationTime() {
        assertEquals(0,instance.getCalculationTime());
    }

    /**
     * Test of setCalculationTime method, of class LayerPriceComposition.
     */
    @Test
    public void testSetCalculationTime() {
        long calculationTime = 1000L;
        instance.setCalculationTime(calculationTime);
        assertEquals(calculationTime,instance.getCalculationTime());
    }

    /**
     * Test of getTransaction method, of class LayerPriceComposition.
     */
    @Test
    public void testGetTransaction() {
        assertNull(instance.getTransaction());
    }

    /**
     * Test of setTransaction method, of class LayerPriceComposition.
     */
    @Test
    public void testSetTransaction() {
        Transaction transaction = new Transaction();
        instance.setTransaction(transaction);
        assertEquals(transaction,instance.getTransaction());
    }

    /**
     * Test of getService method, of class LayerPriceComposition.
     */
    @Test
    public void testGetService() {
        assertNull(instance.getService());
    }

    /**
     * Test of setService method, of class LayerPriceComposition.
     */
    @Test
    public void testSetService() {
        String service = "service 1";
        instance.setService(service);
        assertStringEquals(service,instance.getService());
    }

    /**
     * Test of getOperation method, of class LayerPriceComposition.
     */
    @Test
    public void testGetOperation() {
        assertNull(instance.getOperation());
    }

    /**
     * Test of setOperation method, of class LayerPriceComposition.
     */
    @Test
    public void testSetOperation() {
        String operation = "operation A";
        instance.setOperation(operation);
        assertStringEquals(operation,instance.getOperation());
    }

    /**
     * Test of toString method, of class LayerPriceComposition.
     */
    @Test
    public void testToString() {
         String serverProviderPrefix    = "WWW";
         Calendar calendar = Calendar.getInstance();
         calendar.set(112,1,1);
         Date calculationDate  = calendar.getTime();
         int  planType                  = 1;
         BigDecimal units               = new BigDecimal(1);
         int method                     = LayerPriceComposition.METHOD_PARENTS;
         String service                 = "service_1";
         String operation               = "operation A";
         Boolean layerIsFree            = true;
         
         instance.setServerProviderPrefix(serverProviderPrefix);
         instance.setCalculationDate(calculationDate);
         instance.setPlanType(planType);
         instance.setUnits(units);
         instance.setMethod(method);
         instance.setService(service);
         instance.setOperation(operation);
         instance.setLayerIsFree(layerIsFree);
         instance.setLayerName(layerName);
         
         String result  = instance.toString();         
         if( !result.contains("LayerCalculation for '" + serverProviderPrefix + "_" + layerName + "'") ||
                 !result.contains("on " + calculationDate + " for planType '" + planType + "' and units '" + units + "'") ||
                 !result.contains("The freeState of this layer is '" + layerIsFree + "'") ||
                 !result.contains("The used method is " + method) ||
                 !result.contains("Service/Operation: " + service + ", " + operation) ){
             fail("Invalid toString output");
         }
    }

    /**
     * Test of getScale method, of class LayerPriceComposition.
     */
    @Test
    public void testGetScale() {
        assertNull(instance.getScale());
    }

    /**
     * Test of setScale method, of class LayerPriceComposition.
     */
    @Test
    public void testSetScale() {
        BigDecimal scale = new BigDecimal(5);
        instance.setScale(scale);
        assertEquals(scale,instance.getScale());
    }

    /**
     * Test of getProjection method, of class LayerPriceComposition.
     */
    @Test
    public void testGetProjection() {
        assertNull(instance.getProjection());
    }

    /**
     * Test of setProjection method, of class LayerPriceComposition.
     */
    @Test
    public void testSetProjection() {
        String projection = "setProjection";
        instance.setProjection(projection);
        assertStringEquals(projection,instance.getProjection());
    }
}
