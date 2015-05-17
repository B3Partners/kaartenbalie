package nl.b3p.kaartenbalie.service;

import general.KaartenbalieTestCase;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import nl.b3p.wms.capabilities.ServiceDomainResource;
import nl.b3p.wms.capabilities.ServiceProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author rachelle
 */
public class ServiceProviderValidatorTest extends KaartenbalieTestCase {

    private Set serviceProviders;
    private ServiceProviderValidator instance;
    private Set filledServiceProviders;

    public ServiceProviderValidatorTest(String name) {
        super(name);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.createProviders();
        this.createFilledProviders();
        this.instance = new ServiceProviderValidator(this.serviceProviders);
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        this.serviceProviders = null;
        this.instance = null;
    }

    /**
     * Test of getValidServiceProvider method, of class
     * ServiceProviderValidator.
     */
    @Test
    public void testGetValidServiceProvider() {
        /*
         * Default service provider
         */
        ServiceProvider provider = this.instance.getValidServiceProvider();
        Iterator it = provider.getExceptions().iterator();
        int counter = 0;
        while (it.hasNext()) {
            it.next();
            counter++;
        }
        assertEquals(3, counter);

        /*
         * Configed service provider
         */
        this.instance.setServiceProviders(this.filledServiceProviders);
        provider = this.instance.getValidServiceProvider();
        it = provider.getExceptions().iterator();
        counter = 0;
        while (it.hasNext()) {
            it.next();
            counter++;
        }
        assertEquals(3, counter);
    }

    /**
     * Test of validate method, of class ServiceProviderValidator.
     */
    @Test
    public void testValidate() {
        /*
         * Default service provider
         */
        assertTrue(this.instance.validate());

        /*
         * Configed service provider
         */
        this.instance.setServiceProviders(this.filledServiceProviders);
        assertTrue(this.instance.validate());
    }

    /**
     * Test of validateGetMapFormat method, of class ServiceProviderValidator.
     */
    @Test
    public void testValidateGetMapFormat() {
        /*
         * Default service provider
         */
        assertTrue(this.instance.validateGetMapFormat());

        /*
         * Configed service provider
         */
        this.instance.setServiceProviders(this.filledServiceProviders);
        assertTrue(this.instance.validateGetMapFormat());
    }

    /**
     * Test of validateGetCapabilitiesFormat method, of class
     * ServiceProviderValidator.
     */
    @Test
    public void testValidateGetCapabilitiesFormat() {
        /*
         * Default service provider
         */
        assertTrue(this.instance.validateGetCapabilitiesFormat());

        /*
         * Configed service provider
         */
        this.instance.setServiceProviders(this.filledServiceProviders);
        assertTrue(this.instance.validateGetCapabilitiesFormat());
    }

    /**
     * Test of validateGetFeatureInfoFormat method, of class
     * ServiceProviderValidator.
     */
    @Test
    public void testValidateGetFeatureInfoFormat() {
        /*
         * Default service provider
         */
        assertTrue(this.instance.validateGetFeatureInfoFormat());

        /*
         * Configed service provider
         */
        this.instance.setServiceProviders(this.filledServiceProviders);
        assertTrue(this.instance.validateGetFeatureInfoFormat());
    }

    /**
     * Test of validateDescribeLayerFormat method, of class
     * ServiceProviderValidator.
     */
    @Test
    public void testValidateDescribeLayerFormat() {
        /*
         * Default service provider
         */
        assertTrue(this.instance.validateDescribeLayerFormat());

        /*
         * Configed service provider
         */
        this.instance.setServiceProviders(this.filledServiceProviders);
        assertTrue(this.instance.validateDescribeLayerFormat());
    }

    /**
     * Test of validateGetLegendGraphicFormat method, of class
     * ServiceProviderValidator.
     */
    @Test
    public void testValidateGetLegendGraphicFormat() {
        /*
         * Default service provider
         */
        assertTrue(this.instance.validateGetLegendGraphicFormat());

        /*
         * Configed service provider
         */
        this.instance.setServiceProviders(this.filledServiceProviders);
        assertTrue(this.instance.validateGetLegendGraphicFormat());
    }

    /**
     * Test of getServiceProviders method, of class ServiceProviderValidator.
     */
    @Test
    public void testGetServiceProviders() {
        /*
         * Default service provider
         */
        assertEquals(this.serviceProviders, this.instance.getServiceProviders());

        /*
         * Configed service provider
         */
        this.instance.setServiceProviders(this.filledServiceProviders);
        assertEquals(this.filledServiceProviders, this.instance.getServiceProviders());
    }

    /**
     * Test of setServiceProviders method, of class ServiceProviderValidator.
     */
    @Test
    public void testSetServiceProviders() {
        /*
         * Default service provider
         */
        assertEquals(this.serviceProviders, this.instance.getServiceProviders());

        /*
         * Configed service provider
         */
        this.instance.setServiceProviders(this.filledServiceProviders);
        assertEquals(this.filledServiceProviders, this.instance.getServiceProviders());
    }

    /**
     * Creates a empty service provider set
     */
    private void createProviders() {
        HashSet<ServiceProvider> providers = new HashSet<ServiceProvider>();

        this.serviceProviders = providers;
    }

    /**
     * Creates a service provider set with three service providers
     */
    private void createFilledProviders() {
        HashSet<ServiceProvider> providers = new HashSet<ServiceProvider>();

        ServiceProvider provider;
        for (int i = 0; i < 3; i++) {
            provider = new ServiceProvider();

            ServiceDomainResource sdr = new ServiceDomainResource();
            sdr.setDomain("GetCapabilities");
            Set formats = new HashSet();
            formats.add("application/vnd.ogc.wms_xml");
            sdr.setFormats(formats);
            provider.addDomainResource(sdr);

            sdr = new ServiceDomainResource();
            sdr.setDomain("GetMap");
            formats = new HashSet();
            formats.add("image/bmp");
            formats.add("image/svg");
            formats.add("image/png");
            formats.add("image/jpeg");
            formats.add("image/tiff");
            sdr.setFormats(formats);
            provider.addDomainResource(sdr);

            sdr = new ServiceDomainResource();
            sdr.setDomain("GetFeatureInfo"); //
            formats = new HashSet();
            formats.add("text/plain");
            formats.add("application/vnd.ogc.gml");
            formats.add("application/json");
            sdr.setFormats(formats);
            provider.addDomainResource(sdr);

            provider.addException("application/vnd.ogc.se_xml");
            provider.addException("application/vnd.ogc.se_inimage");
            provider.addException("application/vnd.ogc.se_blank");

            providers.add(provider);
        }

        this.filledServiceProviders = providers;
    }
}
