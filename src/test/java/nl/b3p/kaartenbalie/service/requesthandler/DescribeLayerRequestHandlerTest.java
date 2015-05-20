package nl.b3p.kaartenbalie.service.requesthandler;

import general.KaartenbalieTestCase;
import nl.b3p.servletTestAPI.HttpServletRequestStub;
import nl.b3p.servletTestAPI.HttpServletResponseStub;
import java.util.Date;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.monitoring.DataMonitoring;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author rachelle
 */
public class DescribeLayerRequestHandlerTest extends KaartenbalieTestCase {   
    private HttpServletRequestStub httpServletRequestStub;
    private HttpServletResponseStub httpServletResponseStub;
    private DescribeLayerRequestHandler instance;
    
    public DescribeLayerRequestHandlerTest(String name){
        super(name);
    }
    
    @Before
    @Override
    public void setUp() throws Exception{
        super.setUp();
        
        this.httpServletRequestStub     = new HttpServletRequestStub();
        this.httpServletResponseStub    = new HttpServletResponseStub();
        this.instance                   = new DescribeLayerRequestHandler();
    }
    
    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        
        this.httpServletRequestStub     = null;
        this.httpServletResponseStub    = null;
        this.instance                   = null;
    }

    /**
     * Test of getRequest method, of class DescribeLayerRequestHandler.
     */
    @Test
    public void testDescribeLayerRequestHandler_GetRequest() throws Exception {
        DataWrapper wrapper = new DataWrapper(this.httpServletRequestStub,this.httpServletResponseStub);
        
        DataMonitoring monitoring   = new DataMonitoring();
        Date date                   = new Date();
        monitoring.startClientRequest("index.jsp",64, date.getTime(), "127.0.0.1", "http");
        User user   = this.generateUser();
        
        wrapper.setRequestReporting(monitoring);
        
        OGCRequest request  = new OGCRequest();
        request.addOrReplaceParameters(OGCConstants.REQUEST+"=127.0.0.1&"+OGCConstants.WMS_PARAM_LAYERS+"=background");
        wrapper.setOgcrequest(request);
        
        try {
            this.instance.getRequest(wrapper, this.generateUser());
            
            fail("Function schould fail. need real content server");
            assertTrue(false);
        }
        catch(Exception e){
            if( e.getLocalizedMessage().contains("EntityManager") ){
                fail(e.getLocalizedMessage());
                assertTrue(false);
            }
            
            assertTrue(true);
        }
    }
}
