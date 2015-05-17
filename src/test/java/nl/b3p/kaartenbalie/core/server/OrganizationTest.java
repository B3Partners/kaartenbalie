package nl.b3p.kaartenbalie.core.server;

import general.KaartenbalieTestCase;
import java.util.HashSet;
import java.util.Set;
import nl.b3p.kaartenbalie.core.server.accounting.entity.Account;
import nl.b3p.kaartenbalie.reporting.Report;
import nl.b3p.wms.capabilities.Layer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author rachelle
 */
public class OrganizationTest extends KaartenbalieTestCase {

    private Organization instance;
    
    public OrganizationTest(String name) {
        super(name);
    }
    
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        instance = new Organization();
    }
    
    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        
        instance = null;
    }

    /**
     * Test of getId method, of class Organization.
     */
    @Test
    public void testGetId() {
        assertNull(instance.getId());
    }

    /**
     * Test of getName method, of class Organization.
     */
    @Test
    public void testGetName() {
        assertNull(instance.getName());
    }

    /**
     * Test of setName method, of class Organization.
     */
    @Test
    public void testSetName() {
        String name = "Name";
        instance.setName(name);
        assertStringEquals(name, instance.getName());
    }

    /**
     * Test of getStreet method, of class Organization.
     */
    @Test
    public void testGetStreet() {
        assertNull(instance.getStreet());
    }

    /**
     * Test of setStreet method, of class Organization.
     */
    @Test
    public void testSetStreet() {
        String street = "mainstreet";
        instance.setStreet(street);
        assertStringEquals(street, instance.getStreet());
    }

    /**
     * Test of getNumber method, of class Organization.
     */
    @Test
    public void testGetNumber() {
        assertNull(instance.getNumber());
    }

    /**
     * Test of setNumber method, of class Organization.
     */
    @Test
    public void testSetNumber() {
        String number = "233-12342343";
        instance.setNumber(number);
        assertStringEquals(number, instance.getNumber());
    }

    /**
     * Test of getAddition method, of class Organization.
     */
    @Test
    public void testGetAddition() {
        assertNull(instance.getAddition());
    }

    /**
     * Test of setAddition method, of class Organization.
     */
    @Test
    public void testSetAddition() {
        String addition = "Addition";
        instance.setAddition(addition);
        assertStringEquals(addition, instance.getAddition());
    }

    /**
     * Test of getPostalcode method, of class Organization.
     */
    @Test
    public void testGetPostalcode() {
        assertNull(instance.getPostalcode());
    }

    /**
     * Test of setPostalcode method, of class Organization.
     */
    @Test
    public void testSetPostalcode() {        
        String postalcode = "postal";
        instance.setPostalcode(postalcode);
        assertStringEquals(postalcode, instance.getPostalcode());
    }

    /**
     * Test of getProvince method, of class Organization.
     */
    @Test
    public void testGetProvince() {
        assertNull(instance.getProvince());
    }

    /**
     * Test of setProvince method, of class Organization.
     */
    @Test
    public void testSetProvince() {
        String province = "Province";
        instance.setProvince(province);
        assertStringEquals(province, instance.getProvince());
    }

    /**
     * Test of getCountry method, of class Organization.
     */
    @Test
    public void testGetCountry() {
        assertNull(instance.getCountry());
    }

    /**
     * Test of setCountry method, of class Organization.
     */
    @Test
    public void testSetCountry() {
        String country = "Country";
        instance.setCountry(country);
        assertStringEquals(country, instance.getCountry());
    }

    /**
     * Test of getTelephone method, of class Organization.
     */
    @Test
    public void testGetTelephone() {
        assertNull(instance.getTelephone());
    }

    /**
     * Test of setTelephone method, of class Organization.
     */
    @Test
    public void testSetTelephone() {
        String telephone = "telephone";
        instance.setTelephone(telephone);
        assertStringEquals(telephone, instance.getTelephone());
    }

    /**
     * Test of getFax method, of class Organization.
     */
    @Test
    public void testGetFax() {
        assertNull(instance.getFax());
    }

    /**
     * Test of setFax method, of class Organization.
     */
    @Test
    public void testSetFax() {
        String fax = "Fax";
        instance.setFax(fax);
        assertStringEquals(fax, instance.getFax());
    }

    /**
     * Test of getBilling method, of class Organization.
     */
    @Test
    public void testGetBilling() {
        Set billing = new HashSet();
        assertEquals(billing, instance.getBilling());
    }

    /**
     * Test of setBilling method, of class Organization.
     */
    @Test
    public void testSetBilling() {
        Set billing = new HashSet();
        billing.add("bill1");
        billing.add("bill2");
        billing.add("bill3");
        
        instance.setBilling(billing);
        assertEquals(billing, instance.getBilling());
    }

    /**
     * Test of getLayers method, of class Organization.
     */
    @Test
    public void testGetLayers() {
        Set layers = new HashSet();
        assertEquals(layers, instance.getLayers());
    }

    /**
     * Test of setLayers method, of class Organization.
     */
    @Test
    public void testSetLayers() {
        Set layers = new HashSet();
        layers.add("layer1");
        layers.add("layer2");
        layers.add("layer3");
        
        instance.setLayers(layers);
        assertEquals(layers, instance.getLayers());
    }

    /**
     * Test of addLayer method, of class Organization.
     */
    @Test
    public void testAddLayer() {
        Set layers = instance.getLayers();
        Layer layer1 = new Layer();
        layers.add(layer1);
        
        instance.addLayer(layer1);
        assertEquals(layers, instance.getLayers());
    }

    /**
     * Test of getWfsLayers method, of class Organization.
     */
    @Test
    public void testGetWfsLayers() {
        Set layers = new HashSet();
        assertEquals(layers, instance.getWfsLayers());
    }

    /**
     * Test of setWfsLayers method, of class Organization.
     */
    @Test
    public void testSetWfsLayers() {
        Set layers = new HashSet();
        layers.add("layer1");
        layers.add("layer2");
        layers.add("layer3");
        
        instance.setWfsLayers(layers);
        assertEquals(layers, instance.getWfsLayers());
    }

    /**
     * Test of addWfsLayer method, of class Organization.
     */
    @Test
    public void testAddWfsLayer() {
        Set layers = instance.getLayers();
        Layer layer1 = new Layer();
        layers.add(layer1);
        
        instance.addWfsLayer(layer1);
        assertEquals(layers, instance.getWfsLayers());
    }

    /**
     * Test of getPostbox method, of class Organization.
     */
    @Test
    public void testGetPostbox() {
        assertNull(instance.getPostbox());
    }

    /**
     * Test of setPostbox method, of class Organization.
     */
    @Test
    public void testSetPostbox() {
        String postbox = "Postbox";
        instance.setPostbox(postbox);
        assertStringEquals(postbox, instance.getPostbox());
    }

    /**
     * Test of getBillingAddress method, of class Organization.
     */
    @Test
    public void testGetBillingAddress() {
        assertNull(instance.getBillingAddress());
    }

    /**
     * Test of setBillingAddress method, of class Organization.
     */
    @Test
    public void testSetBillingAddress() {
        String billingAddress = "mainstreet";
        instance.setBillingAddress(billingAddress);
        assertStringEquals(billingAddress, instance.getBillingAddress());
    }

    /**
     * Test of getVisitorsAddress method, of class Organization.
     */
    @Test
    public void testGetVisitorsAddress() {
        assertNull(instance.getVisitorsAddress());
    }

    /**
     * Test of setVisitorsAddress method, of class Organization.
     */
    @Test
    public void testSetVisitorsAddress() {
        String visitorsAddress = "mainstreet";
        instance.setVisitorsAddress(visitorsAddress);
        assertStringEquals(visitorsAddress, instance.getVisitorsAddress());
    }

    /**
     * Test of getHasValidGetCapabilities method, of class Organization.
     */
    @Test
    public void testGetHasValidGetCapabilities() {
        assertFalse(instance.getHasValidGetCapabilities());
    }

    /**
     * Test of setHasValidGetCapabilities method, of class Organization.
     */
    @Test
    public void testSetHasValidGetCapabilities() {
        boolean hasValidGetCapabilities = true;
        instance.setHasValidGetCapabilities(hasValidGetCapabilities);
        assertTrue(instance.getHasValidGetCapabilities());
    }

    /**
     * Test of getBbox method, of class Organization.
     */
    @Test
    public void testGetBbox() {
        assertNull(instance.getBbox());
    }

    /**
     * Test of setBbox method, of class Organization.
     */
    @Test
    public void testSetBbox() {
        String bbox = "BBox";
        instance.setBbox(bbox);
        assertStringEquals(bbox, instance.getBbox());
    }

    /**
     * Test of getReports method, of class Organization.
     */
    @Test
    public void testGetReports() {
        Set reports = new HashSet();
        assertEquals(reports, instance.getReports());
    }

    /**
     * Test of setReports method, of class Organization.
     */
    @Test
    public void testSetReports() {
        Set reports = new HashSet();
        reports.add(new Report());
        
        instance.setReports(reports);
        assertEquals(reports, instance.getReports());
    }

    /**
     * Test of getReportStatus method, of class Organization.
     */
    @Test
    public void testGetReportStatus() {
        Set reportStatus = new HashSet();
        assertEquals(reportStatus, instance.getReportStatus());
    }

    /**
     * Test of setReportStatus method, of class Organization.
     */
    @Test
    public void testSetReportStatus() {
        Set reportStatus = new HashSet();
        reportStatus.add(new Report());
        
        instance.setReportStatus(reportStatus);
        assertEquals(reportStatus, instance.getReportStatus());
    }

    /**
     * Test of getAccount method, of class Organization.
     */
    @Test
    public void testGetAccount() {
        assertNull(instance.getAccount());
    }

    /**
     * Test of setAccount method, of class Organization.
     */
    @Test
    public void testSetAccount() {
        Account account = new Account();
        instance.setAccount(account);
        assertEquals(account, instance.getAccount());
    }

    /**
     * Test of getCode method, of class Organization.
     */
    @Test
    public void testGetCode() {
        assertNull(instance.getCode());
    }

    /**
     * Test of setCode method, of class Organization.
     */
    @Test
    public void testSetCode() {
        String code = "2321";
        instance.setCode(code);
        assertStringEquals(code, instance.getCode());
    }

    /**
     * Test of setAllowAccountingLayers method, of class Organization.
     */
    @Test
    public void testSetAllowAccountingLayers() {
        boolean allowAccountingLayers = true;
        instance.setAllowAccountingLayers(allowAccountingLayers);
        assertTrue(instance.getAllowAccountingLayers());
    }

    /**
     * Test of getAllowAccountingLayers method, of class Organization.
     */
    @Test
    public void testGetAllowAccountingLayers() {
        assertFalse(instance.getAllowAccountingLayers());
    }

    /**
     * Test of getUsers method, of class Organization.
     */
    @Test
    public void testGetUsers() {
        Set users = new HashSet();
        assertEquals(users, instance.getUsers());
    }

    /**
     * Test of setUsers method, of class Organization.
     */
    @Test
    public void testSetUsers() {
        Set users = new HashSet();
        users.add(this.generateUser());
        
        instance.setUsers(users);
        assertEquals(users, instance.getUsers());
    }

    /**
     * Test of getMainUsers method, of class Organization.
     */
    @Test
    public void testGetMainUsers() {
        Set mainUsers = new HashSet();
        assertEquals(mainUsers, instance.getMainUsers());
    }

    /**
     * Test of setMainUsers method, of class Organization.
     */
    @Test
    public void testSetMainUsers() {
        Set mainUsers = new HashSet();
        mainUsers.add(this.generateUser());
        
        instance.setMainUsers(mainUsers);
        assertEquals(mainUsers, instance.getMainUsers());
    }
}
