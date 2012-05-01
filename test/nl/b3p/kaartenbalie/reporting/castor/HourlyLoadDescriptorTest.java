package nl.b3p.kaartenbalie.reporting.castor;

import general.KaartenbalieTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author rachelle
 */
public class HourlyLoadDescriptorTest extends KaartenbalieTestCase {

    private HourlyLoadDescriptor instance;

    public HourlyLoadDescriptorTest(String name) {
        super(name);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.instance = new HourlyLoadDescriptor();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        this.instance = null;
    }

    /**
     * Test of getAccessMode method, of class HourlyLoadDescriptor.
     */
    @Test
    public void testGetAccessMode() {
        assertNull(this.instance.getAccessMode());
    }

    /**
     * Test of getExtends method, of class HourlyLoadDescriptor.
     */
    @Test
    public void testGetExtends() {
        assertNull(this.instance.getExtends());
    }

    /**
     * Test of getIdentity method, of class HourlyLoadDescriptor.
     */
    @Test
    public void testGetIdentity() {
        org.exolab.castor.mapping.FieldDescriptor indentity = this.instance.getIdentity();

        assertNull(indentity);
    }

    /**
     * Test of getJavaClass method, of class HourlyLoadDescriptor.
     */
    @Test
    public void testGetJavaClass() {
        Class test = this.instance.getJavaClass();

        assertStringEquals("nl.b3p.kaartenbalie.reporting.castor.HourlyLoad", test.getCanonicalName());
    }

    /**
     * Test of getNameSpacePrefix method, of class HourlyLoadDescriptor.
     */
    @Test
    public void testGetNameSpacePrefix() {
        assertNull(this.instance.getNameSpacePrefix());
    }

    /**
     * Test of getNameSpaceURI method, of class HourlyLoadDescriptor.
     */
    @Test
    public void testGetNameSpaceURI() {
        assertNull(this.instance.getNameSpacePrefix());
    }

    /**
     * Test of getValidator method, of class HourlyLoadDescriptor.
     */
    @Test
    public void testGetValidator() {
        org.exolab.castor.xml.TypeValidator test = this.instance.getValidator();

        assertEquals(test, this.instance);
    }

    /**
     * Test of getXMLName method, of class HourlyLoadDescriptor.
     */
    @Test
    public void testGetXMLName() {
        assertStringEquals("hourly-load", this.instance.getXMLName());
    }

    /**
     * Test of isElementDefinition method, of class HourlyLoadDescriptor.
     */
    @Test
    public void testIsElementDefinition() {
        assertTrue(this.instance.isElementDefinition());
    }
}
