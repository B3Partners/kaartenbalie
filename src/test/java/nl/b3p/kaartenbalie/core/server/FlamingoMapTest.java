package nl.b3p.kaartenbalie.core.server;

import general.KaartenbalieTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author rachelle
 */
public class FlamingoMapTest extends KaartenbalieTestCase {

    private FlamingoMap instance;

    public FlamingoMapTest(String name) {
        super(name);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        instance = new FlamingoMap();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        instance = null;
    }

    /**
     * Test of getFMCWMSLink method, of class FlamingoMap.
     */
    @Test
    public void testGetFMCWMSLink() {
        assertNotNull(instance.getFMCWMSLink());
    }

    /**
     * Test of getLayers method, of class FlamingoMap.
     */
    @Test
    public void testGetLayers() {
        assertNotNull(instance.getLayers());
    }

    /**
     * Test of getId method, of class FlamingoMap.
     */
    @Test
    public void testGetId() {
        assertNull(instance.getId());
    }

    /**
     * Test of setId method, of class FlamingoMap.
     */
    @Test
    public void testSetId() {
        Integer test = new Integer(6);
        instance.setId(test);
        assertEquals(test, instance.getId());
    }

    /**
     * Test of getNaam method, of class FlamingoMap.
     */
    @Test
    public void testGetNaam() {
        assertNull(instance.getNaam());
    }

    /**
     * Test of setNaam method, of class FlamingoMap.
     */
    @Test
    public void testSetNaam() {
        String test = "naam";
        instance.setNaam(test);
        assertStringEquals(test, instance.getNaam());
    }

    /**
     * Test of getInlog method, of class FlamingoMap.
     */
    @Test
    public void testGetInlog() {
        Boolean result = instance.getInlog();
        assertFalse(result);
    }

    /**
     * Test of setInlog method, of class FlamingoMap.
     */
    @Test
    public void testSetInlog() {
        Boolean inlog = true;
        instance.setInlog(inlog);
        assertTrue(instance.getInlog());
    }

    /**
     * Test of getPublicatie method, of class FlamingoMap.
     */
    @Test
    public void testGetPublicatie() {
        Boolean result = instance.getPublicatie();
        assertFalse(result);
    }

    /**
     * Test of setPublicatie method, of class FlamingoMap.
     */
    @Test
    public void testSetPublicatie() {
        Boolean publicatie = true;
        instance.setPublicatie(publicatie);
        assertTrue(instance.getPublicatie());
    }

    /**
     * Test of getMetadataLink method, of class FlamingoMap.
     */
    @Test
    public void testGetMetadataLink() {
        assertNull(instance.getMetadataLink());
    }

    /**
     * Test of setMetadataLink method, of class FlamingoMap.
     */
    @Test
    public void testSetMetadataLink() {
        String metadatalink = "http://example.com";
        instance.setMetadataLink(metadatalink);
        assertStringEquals(metadatalink, instance.getMetadataLink());
    }

    /**
     * Test of getWMSLink method, of class FlamingoMap.
     */
    @Test
    public void testGetWMSLink() {
        assertNull(instance.getWMSLink());
    }

    /**
     * Test of setWMSLink method, of class FlamingoMap.
     */
    @Test
    public void testSetWMSLink() {
        String WMSlink = "http://example.com";
        instance.setWMSLink(WMSlink);
        assertStringEquals(WMSlink, instance.getWMSLink());
    }

    /**
     * Test of getWFSLink method, of class FlamingoMap.
     */
    @Test
    public void testGetWFSLink() {
        assertNull(instance.getWFSLink());
    }

    /**
     * Test of setWFSLink method, of class FlamingoMap.
     */
    @Test
    public void testSetWFSLink() {
        String WFSLink = "http://example.com";
        instance.setWFSLink(WFSLink);
        assertStringEquals(WFSLink, instance.getWFSLink());
    }
}
