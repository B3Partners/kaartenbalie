package nl.b3p.kaartenbalie.service.requesthandler;

import general.KaartenbalieTestCase;
import nl.b3p.servletAPI.HttpServletRequestStub;
import nl.b3p.servletAPI.HttpServletResponseStub;
import nl.b3p.servletAPI.UserStub;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.monitoring.DataMonitoring;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author rachelle
 */
public class GetFeatureInfoRequestHandlerTest extends KaartenbalieTestCase {
    private HttpServletRequestStub request;
    private HttpServletResponseStub response;
    private GetFeatureInfoRequestHandler instance;
    private User user;
    
    public GetFeatureInfoRequestHandlerTest(String name){
        super(name);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        this.request        = new HttpServletRequestStub();
        this.response       = new HttpServletResponseStub();
        this.user           = this.generateUser();
        this.instance       = new GetFeatureInfoRequestHandler();
    }
    
    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        
        this.request        = null;
        this.response       = null;
        this.user           = null;
        this.instance       = null;
    }
    
    /**
     * Test of getRequest method, of class GetFeatureInfoRequestHandler.
     * Default call
     */
    @Test
    public void testGetFeatureInfoRequestHandlerTest_GetRequest_default(){
        /* Default request */
        try {
            DataWrapper wrapper    = new DataWrapper(this.request,this.response);
            OGCRequest ogRequest  = new OGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.WMS_PARAM_QUERY_LAYERS+"="+this.layerNames+"&"+OGCConstants.WMS_REQUEST_GetFeatureInfo+"="+OGCConstants.WMS_REQUEST_GetMap);
            wrapper.setOgcrequest(ogRequest);
            
            DataMonitoring monitoring   = new DataMonitoring();            
            wrapper.setRequestReporting(monitoring);
            
            this.instance.getRequest(wrapper, this.user);
            
            assertTrue(false);
        } 
        catch (Exception ex) {
            this.checkNotMapped(ex);
        }  
    }
    
    /**
     * Test of getRequest method, of class GetFeatureInfoRequestHandler.
     * HTML call
     */
    @Test
    public void testGetFeatureInfoRequestHandlerTest_GetRequest_HTML(){
        /* HTML request */
        try {
            DataWrapper wrapper    = new DataWrapper(this.request,this.response);
            OGCRequest ogRequest  = new OGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.WMS_PARAM_QUERY_LAYERS+"="+this.layerNames+"&"+OGCConstants.WMS_REQUEST_GetFeatureInfo+"="+OGCConstants.WMS_REQUEST_GetMap+"&"+OGCConstants.WMS_PARAM_INFO_FORMAT+"="+OGCConstants.WMS_PARAM_WMS_HTML);
            wrapper.setOgcrequest(ogRequest);
            
            DataMonitoring monitoring   = new DataMonitoring();            
            wrapper.setRequestReporting(monitoring);
            
            this.instance.getRequest(wrapper, this.user);
            
            assertTrue(false);
        } 
        catch (Exception ex) {
            this.checkNotMapped(ex);
        }  
    }
    
    /**
     * Test of getRequest method, of class GetFeatureInfoRequestHandler.
     * GML call
     */
    @Test
    public void testGetFeatureInfoRequestHandlerTest_GetRequest_GML(){
        /* GML request */
        try {
            DataWrapper wrapper    = new DataWrapper(this.request,this.response);
            OGCRequest ogRequest  = new OGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.WMS_PARAM_QUERY_LAYERS+"="+this.layerNames+"&"+OGCConstants.WMS_REQUEST_GetFeatureInfo+"="+OGCConstants.WMS_REQUEST_GetMap+"&"+OGCConstants.WMS_PARAM_INFO_FORMAT+"="+OGCConstants.WMS_PARAM_WMS_GML);
            wrapper.setOgcrequest(ogRequest);
            
            DataMonitoring monitoring   = new DataMonitoring();            
            wrapper.setRequestReporting(monitoring);
            
            this.instance.getRequest(wrapper, this.user);
            
            assertTrue(false);
        } 
        catch (Exception ex) {
            this.checkNotMapped(ex);
        }  
    }
    
    /**
     * Test of getRequest method, of class GetFeatureInfoRequestHandler.
     * XML call
     */
    @Test
    public void testGetFeatureInfoRequestHandlerTest_GetRequest_XML(){
        /* GML request */
        try {
            DataWrapper wrapper    = new DataWrapper(this.request,this.response);
            OGCRequest ogRequest  = new OGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.WMS_PARAM_QUERY_LAYERS+"="+this.layerNames+"&"+OGCConstants.WMS_REQUEST_GetFeatureInfo+"="+OGCConstants.WMS_REQUEST_GetMap+"&"+OGCConstants.WMS_PARAM_INFO_FORMAT+"="+OGCConstants.WMS_PARAM_WMS_XML);
            wrapper.setOgcrequest(ogRequest);
            
            DataMonitoring monitoring   = new DataMonitoring();            
            wrapper.setRequestReporting(monitoring);
            
            this.instance.getRequest(wrapper, this.user);
            
            assertTrue(false);
        } 
        catch (Exception ex) {
            this.checkNotMapped(ex);
        }  
    }
}
