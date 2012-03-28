package nl.b3p.kaartenbalie.service.requesthandler;

import general.B3TestCase;
import stubs.HttpServletRequestStub;
import stubs.HttpServletResponseStub;
import stubs.UserStub;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.ogc.utils.OGCRequest;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author rachelle
 */
public class GetCapabilitiesRequestHandlerTest extends B3TestCase {
    private GetCapabilitiesRequestHandler instance;
    private HttpServletRequestStub request;
    private HttpServletResponseStub response;
    
    public GetCapabilitiesRequestHandlerTest(String name){
        super(name);
    }
    
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        this.request        = new HttpServletRequestStub();
        this.response       = new HttpServletResponseStub();
        this.instance       = new GetCapabilitiesRequestHandler();
    }
    
    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        
        this.request        = null;
        this.response       = null;
        this.instance       = null;
    }

    /**
     * Test of getRequest method, of class GetCapabilitiesRequestHandler.
     */
    @Test
    public void testGetCapabilitiesRequestHandlerTest_GetRequest(){
        try {
            DataWrapper wrapper = new DataWrapper(this.request,this.response);
            User user           = UserStub.generateServerUser();
            user.setPersonalURL("http://example.com/personal");

            OGCRequest OGrequest  = new OGCRequest();
            OGrequest.addOrReplaceParameters("VIEWER_CONFIG=false");
            wrapper.setOgcrequest(OGrequest);
        
            this.instance.getRequest(wrapper, user);
            
            assertTrue(true);
        }
        catch(IllegalArgumentException e){
            /* Expected
             * Unknow user
             */
            assertTrue(true);
        }
        catch(Exception e){
            e.printStackTrace();
            fail(e.getLocalizedMessage());
            assertFalse(true);
        }
    }
}
