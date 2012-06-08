package nl.b3p.kaartenbalie.core.server.accounting.entity;

import general.KaartenbalieTestCase;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.accounting.TransactionDeniedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author rachelle
 */
public class TransactionTest extends KaartenbalieTestCase {
    private Transaction instance;
    
    public TransactionTest(String name) {
        super(name);
    }
    
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        instance    = new Transaction();
    }
    
    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        
        instance    = null;
    }

    /**
     * Test of getTransactionDate method, of class Transaction.
     */
    @Test
    public void testGetTransactionDate() {
        assertNotNull(instance.getTransactionDate());
    }

    /**
     * Test of setTransactionDate method, of class Transaction.
     */
    @Test
    public void testSetTransactionDate() {
        Date transactionDate = this.generateDate();
        instance.setTransactionDate(transactionDate);
        assertEquals(transactionDate,instance.getTransactionDate());
    }

    /**
     * Test of getMutationDate method, of class Transaction.
     */
    @Test
    public void testGetMutationDate() {
        assertNull(instance.getMutationDate());
    }

    /**
     * Test of setMutationDate method, of class Transaction.
     */
    @Test
    public void testSetMutationDate() {
        Date mutationDate = this.generateDate();
        instance.setMutationDate(mutationDate);
        assertEquals(mutationDate,instance.getMutationDate());
    }

    /**
     * Test of getAccount method, of class Transaction.
     */
    @Test
    public void testGetAccount() {
        assertNull(instance.getAccount());
    }

    /**
     * Test of setAccount method, of class Transaction.
     */
    @Test
    public void testSetAccount() {
        Account account = new Account();
        instance.setAccount(account);
        assertEquals(account,instance.getAccount());
    }

    /**
     * Test of getCreditAlteration method, of class Transaction.
     */
    @Test
    public void testGetCreditAlteration() {
        BigDecimal expResult = new BigDecimal(0);
        assertEquals(expResult, instance.getCreditAlteration());
    }

    /**
     * Test of setCreditAlteration method, of class Transaction.
     */
    @Test
    public void testSetCreditAlteration() {
        BigDecimal creditAlteration = new BigDecimal(10);
        instance.setCreditAlteration(creditAlteration);
        assertEquals(creditAlteration,instance.getCreditAlteration());
    }

    /**
     * Test of getStatus method, of class Transaction.
     */
    @Test
    public void testGetStatus() {
        assertEquals(Transaction.PENDING,instance.getStatus());
    }

    /**
     * Test of setStatus method, of class Transaction.
     */
    @Test
    public void testSetStatus() {
        int status = Transaction.ACCEPTED;
        instance.setStatus(status);
        assertEquals(status,instance.getStatus());
    }

    /**
     * Test of getId method, of class Transaction.
     */
    @Test
    public void testGetId() {
        assertNull(instance.getId());
    }

    /**
     * Test of setId method, of class Transaction.
     */
    @Test
    public void testSetId() {
        Integer id = new Integer(5);
        instance.setId(id);
        assertEquals(id,instance.getId());
    }

    /**
     * Test of getType method, of class Transaction.
     */
    @Test
    public void testGetType() {
        int expResult = Transaction.WITHDRAW;
        assertEquals(expResult,instance.getType());
    }

    /**
     * Test of setType method, of class Transaction.
     */
    @Test
    public void testSetType() {
        int type = Transaction.REFUSED;
        instance.setType(type);
        assertEquals(type,instance.getType());
    }

    /**
     * Test of getErrorMessage method, of class Transaction.
     */
    @Test
    public void testGetErrorMessage() {
        assertNull(instance.getErrorMessage());
    }

    /**
     * Test of setErrorMessage method, of class Transaction.
     */
    @Test
    public void testSetErrorMessage() {
        String errorMessage = "setErrorMessage";
        instance.setErrorMessage(errorMessage);
        assertStringEquals(errorMessage,instance.getErrorMessage());
    }

    /**
     * Test of validate method, of class Transaction.
     */
    @Test
    public void testValidate() {
        /* Check on credit Alteration border */
        try {
            instance.setCreditAlteration(new BigDecimal(-10));
            
            instance.validate();
            
            fail("Function should throw a TransactionDeniedException.");
        }
        catch(TransactionDeniedException e){  }
        
        /* Check on billing amount border */
        try {
            instance.setCreditAlteration(new BigDecimal(10));
            instance.setBillingAmount(new BigDecimal(10000));
            
            instance.validate();
            
            fail("Function should throw a TransactionDeniedException.");
        }
        catch(TransactionDeniedException e){  }
        
        /* Check on transaction type */
        try {
            instance.setCreditAlteration(new BigDecimal(10));
            instance.setBillingAmount(new BigDecimal(10));
            instance.setType(Transaction.PENDING);
            
            instance.validate();
            
            fail("Function should throw a TransactionDeniedException.");
        }
        catch(TransactionDeniedException e){  }
        
        /* Check on correct call */
        try {
            instance.setCreditAlteration(new BigDecimal(10));
            instance.setBillingAmount(new BigDecimal(10));
            instance.setType(Transaction.WITHDRAW);
            
            instance.validate();
        }
        catch(TransactionDeniedException e){ 
            fail("Exception : "+e.getLocalizedMessage());
        }
    }

    /**
     * Test of setUser method, of class Transaction.
     * @todo  Uitzoeken hoe entities toe te voegen aan de DB
     */
    @Test
    public void testSetUser() {
        User user = this.generateUser();
        instance.setUser(user);
        assertEquals(user.getId(),instance.getUser(this.entityManager));
    }

    /**
     * Test of getUser method, of class Transaction.
     * @todo  Uitzoeken hoe entities toe te voegen aan de DB
     */
    @Test
    public void testGetUser() {
        assertNull(instance.getUser(this.entityManager));
        
        /*
         * User user    = this.generateUser();
         * instance.setUser(user);
         * assertEquals(user.getId(),instance.getUser(this.entityManager));
         */
    }

    /**
     * Test of getDescription method, of class Transaction.
     */
    @Test
    public void testGetDescription() {
        assertNull(instance.getDescription());
    }

    /**
     * Test of setDescription method, of class Transaction.
     */
    @Test
    public void testSetDescription() {
        String description = "setDescription";
        instance.setDescription(description);
        assertStringEquals(description,instance.getDescription());
    }

    /**
     * Test of getExchangeRate method, of class Transaction.
     */
    @Test
    public void testGetExchangeRate() {
        Integer expResult =  new Integer(100);
        assertEquals(expResult, Transaction.getExchangeRate());
    }

    /**
     * Test of setExchangeRate method, of class Transaction.
     */
    @Test
    public void testSetExchangeRate() {
        Integer aExchangeRate = new Integer(50);
        Transaction.setExchangeRate(aExchangeRate);
        assertEquals(aExchangeRate, Transaction.getExchangeRate());
    }

    /**
     * Test of getBillingAmount method, of class Transaction.
     */
    @Test
    public void testGetBillingAmount() {
        assertNull(instance.getBillingAmount());
    }

    /**
     * Test of setBillingAmount method, of class Transaction.
     */
    @Test
    public void testSetBillingAmount() {
        BigDecimal billingAmount = new BigDecimal(100);
        instance.setBillingAmount(billingAmount);
        assertEquals(billingAmount,instance.getBillingAmount());
    }

    /**
     * Test of getTxExchangeRate method, of class Transaction.
     */
    @Test
    public void testGetTxExchangeRate() {
        assertNull(instance.getTxExchangeRate());
    }

    /**
     * Test of setTxExchangeRate method, of class Transaction.
     */
    @Test
    public void testSetTxExchangeRate() {
        Integer txExchangeRate = new Integer(15);
        instance.setTxExchangeRate(txExchangeRate);
        assertEquals(txExchangeRate,instance.getTxExchangeRate());
    }

    /**
     * Test of getLayerPriceCompositions method, of class Transaction.
     */
    @Test
    public void testGetLayerPriceCompositions() {
        Set expResult = new HashSet();
        assertEquals(expResult, instance.getLayerPriceCompositions());
    }

    /**
     * Test of setLayerPriceCompositions method, of class Transaction.
     */
    @Test
    public void testSetLayerPriceCompositions() {
        Set layerPriceCompositions = new HashSet();
        layerPriceCompositions.add(new LayerPriceComposition());
        layerPriceCompositions.add(new LayerPriceComposition());
                
        instance.setLayerPriceCompositions(layerPriceCompositions);
        assertEquals(layerPriceCompositions,instance.getLayerPriceCompositions());
    }

    /**
     * Test of registerUsage method, of class Transaction.
     */
    @Test
    public void testRegisterUsage(){
        /* Check on nullpointer */
        try {
            instance.registerUsage(null);
            
            fail("Function should throw a exception");
        }
        catch(Exception e){ }
        
        /* Check on empty layer price */
        try {
            LayerPriceComposition lpc = new LayerPriceComposition();
            instance.registerUsage(lpc);
            
            fail("Function should throw a exception");
        }
        catch(Exception e){ }
                
        /* Check on correct call */
        try {
            LayerPriceComposition lpc = new LayerPriceComposition();
            lpc.setLayerPrice(new BigDecimal(10));
            lpc.setLayerIsFree(true);
            lpc.setLayerName("Layer_1");
            
            instance.registerUsage(lpc);
            Set layerPriceCompositions = instance.getLayerPriceCompositions();
            assertTrue(layerPriceCompositions.contains(lpc));
        }
        catch(Exception e){
            fail("Exception : "+e.getLocalizedMessage());
        }
    }

    /**
     * Test of getPricedLayerNames method, of class Transaction.
     */
    @Test
    public void testGetPricedLayerNames() {
        try {
            LayerPriceComposition lpc = new LayerPriceComposition();
            lpc.setLayerPrice(new BigDecimal(10));
            lpc.setLayerIsFree(false);
            String name    = "Layer_1";
            lpc.setLayerName(name);
            
            instance.registerUsage(lpc);
            String result = instance.getPricedLayerNames();
            assertStringEquals(name, result);
        }
        catch(Exception e){
            fail("Exception : "+e.getLocalizedMessage());
        }
    }
}
