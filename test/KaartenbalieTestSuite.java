import nl.b3p.kaartenbalie.service.requesthandler.DOMValidatorTest;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapperTest;
import nl.b3p.kaartenbalie.service.requesthandler.DescribeLayerRequestHandlerTest;
import nl.b3p.kaartenbalie.service.servlet.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author rachelle
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({CallWMSServletTest.class, ProxySLDServletTest.class,
            SelectCSSTest.class, DOMValidatorTest.class, DataWrapperTest.class,
            DescribeLayerRequestHandlerTest.class, DataWrapperTest.class })
public class KaartenbalieTestSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
