package nl.b3p.kaartenbalie.service.servlet;

import general.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.ServletConfig;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;

/**
 *
 * @author rachelle
 */
public class CallWMSServletTest extends B3TestCase {
    private ServletConfig configStumb;
    private HttpServletRequestStub requestStumb;
    private HttpServletResponseStub responseStumb;    
    private User user;    
    private CallWMSServlet servlet;
    private String layerName    = "testLayer";
    
    public CallWMSServletTest(String name){
        super(name);
    }
    
    @Override
    public void setUp() throws Exception{
        super.setUp();
        
        this.user           = UserStub.generateServerUser();
        this.configStumb    = new ConfigStub();
        this.requestStumb   = new HttpServletRequestStub();
        this.responseStumb  = new HttpServletResponseStub();
        
        servlet             = new CallWMSServlet();        
    }
    
    @Override
    public void tearDown() throws Exception{
        super.tearDown();
        
        this.user           = null;
        this.configStumb    = null;
        this.servlet        = null;
    }
    
    /**
     * Log directory misses
     */
    public void testInit(){
        try {
            this.servlet.init(this.configStumb);
            
            /* check for login file */
            File file = new File("/logs/kaartenbalie.log");
            if( !file.exists() ){
                throw new IOException("Logfile /logs/kaartenbalie.log does not exist");
            }
            assertTrue(true);
        }
        catch(Exception e){
            e.printStackTrace();
            fail("Init failed "+e.getLocalizedMessage());
            
            assertTrue(false);
        }
    }
    
    public void testCreateBaseUrl(){
         StringBuffer baseUrl = this.servlet.createBaseUrl(this.requestStumb);
        
         assertStringEquals(baseUrl.toString(),this.requestStumb.getServletPath());
    }
    
    public void testParseRequestAndData_Proxy(){
        /* Proxy */
        try {
            DataWrapper data    = new DataWrapper(this.requestStumb,this.responseStumb);
            
            OGCRequest ogRequest  = new OGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.PROXY_URL+"=http://localhost:8000&"+OGCConstants.SERVICE+"="+OGCConstants.NONOGC_SERVICE_PROXY);
            data.setOgcrequest(ogRequest);
            
            this.servlet.parseRequestAndData(data, user);
        }
        catch(IllegalBlockSizeException e){
            assertTrue(true);
        }
        catch(Exception e){
            e.printStackTrace();
            fail("Exception "+e.getLocalizedMessage());
            assertTrue(false);
        }
    }
    
    public void testParseRequestAndData_MetaData(){
        /* Meta data */        
        try {
            DataWrapper data    = new DataWrapper(this.requestStumb,this.responseStumb);
            
            OGCRequest ogRequest  = new OGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.PROXY_URL+"=http://localhost:8000&"+OGCConstants.SERVICE+"="+OGCConstants.NONOGC_SERVICE_METADATA+"&"+OGCConstants.METADATA_LAYER+"="+layerName);
            data.setOgcrequest(ogRequest);
            
            this.servlet.parseRequestAndData(data, user);
        }
        catch(Exception e){
            if( e.getLocalizedMessage().contains("Layer not found with name: "+layerName) ){
                assertTrue(true);
            }
            else {
                fail("Exception "+e.getLocalizedMessage());
                assertTrue(false);
            }
        }
    }
    
    public void testParseRequestAndData_WMS(){
        /* WMS */
        try {
            DataWrapper data    = new DataWrapper(this.requestStumb,this.responseStumb);
            
            OGCRequest ogRequest  = new OGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.REQUEST+"="+OGCConstants.WMS_REQUEST_GetCapabilities+"&"+OGCConstants.SERVICE+"="+OGCConstants.NONOGC_SERVICE_METADATA+"&"+OGCConstants.METADATA_LAYER+"="+layerName);
            data.setOgcrequest(ogRequest);
            
            this.servlet.parseRequestAndData(data, user);
            
            assertTrue(true);
        }
        catch(Exception e){
            if( e.getLocalizedMessage().contains("Layer not found with name: "+layerName) ){
                assertTrue(true);
            }
            else {
                fail("Exception "+e.getLocalizedMessage());
                assertTrue(false);
            }
        }
    }
    
    public void testParseRequestAndData_WFS(){
        /* WFS */
        try {
            DataWrapper data    = new DataWrapper(this.requestStumb,this.responseStumb);
            
            OGCRequest ogRequest  = new OGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.REQUEST+"="+OGCConstants.WFS_REQUEST_GetCapabilities+"&"+OGCConstants.SERVICE+"="+OGCConstants.NONOGC_SERVICE_METADATA+"&"+OGCConstants.METADATA_LAYER+"="+layerName);
            data.setOgcrequest(ogRequest);
            
            this.servlet.parseRequestAndData(data, user);
            
            assertTrue(true);
        }
        catch(Exception e){
            if( e.getLocalizedMessage().contains("Layer not found with name: "+layerName) ){
                assertTrue(true);
            }
            else {
                fail("Exception "+e.getLocalizedMessage());
                assertTrue(false);
            }
        }
    }
    
    /**
     * EntityManager mainEM can not be loaded
     */
    public void testParseRequestAndData_WMS_GetMap(){
        /* WMS GetMap */
        try {
            DataWrapper data    = new DataWrapper(this.requestStumb,this.responseStumb);
            
            OGCRequest ogRequest  = new OGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.REQUEST+"="+OGCConstants.WMS_REQUEST_GetMap);
            data.setOgcrequest(ogRequest);
            
            this.servlet.parseRequestAndData(data, user);
            
            assertTrue(true);
        }
        catch(Exception e){
            e.printStackTrace();
            fail("Exception "+e.getLocalizedMessage());
            assertTrue(false);
        }
    }
    
    public void testParseRequestAndData_WMS_GetLegendGraphic(){
        /* WMS GetMap */
        try {
            DataWrapper data    = new DataWrapper(this.requestStumb,this.responseStumb);
            
            OGCRequest ogRequest  = new OGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.REQUEST+"="+
                    OGCConstants.WMS_REQUEST_GetLegendGraphic+"&"+
                    OGCConstants.WMS_PARAM_LAYER+"="+this.layerName);
            data.setOgcrequest(ogRequest);
            
            this.servlet.parseRequestAndData(data, user);
            
            assertTrue(true);
        }
        catch(Exception e){
            e.printStackTrace();
            fail("Exception "+e.getLocalizedMessage());
            assertTrue(false);
        }
    }
    
    public void testParseRequestAndData_WMS_DescribeLayer(){
        /* WMS DescribeLayer */
        try {
            DataWrapper data    = new DataWrapper(this.requestStumb,this.responseStumb);
            
            OGCRequest ogRequest  = new OGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.REQUEST+"="+OGCConstants.WMS_REQUEST_DescribeLayer);
            data.setOgcrequest(ogRequest);
            
            this.servlet.parseRequestAndData(data, user);
            
            assertTrue(true);
        }
        catch(Exception e){
            e.printStackTrace();
            fail("Exception "+e.getLocalizedMessage());
            assertTrue(false);
        }
    }
    
    public void testParseRequestAndData_WMS_DescribeFeatureType(){
        /* WMS DescribeFeatureType */
        try {
            DataWrapper data    = new DataWrapper(this.requestStumb,this.responseStumb);
            
            OGCRequest ogRequest  = new OGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.REQUEST+"="+OGCConstants.WFS_REQUEST_DescribeFeatureType);
            data.setOgcrequest(ogRequest);
            
            this.servlet.parseRequestAndData(data, user);
            
            assertTrue(true);
        }
        catch(Exception e){
            e.printStackTrace();
            fail("Exception "+e.getLocalizedMessage());
            assertTrue(false);
        }
    }
    
     public void testParseRequestAndData_WMS_GetFeature(){
        /* WMS GetFeature */
        try {
            DataWrapper data    = new DataWrapper(this.requestStumb,this.responseStumb);
            
            OGCRequest ogRequest  = new OGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.REQUEST+"="+OGCConstants.WFS_REQUEST_GetFeature);
            data.setOgcrequest(ogRequest);
            
            this.servlet.parseRequestAndData(data, user);
            
            assertTrue(true);
        }
        catch(Exception e){
            e.printStackTrace();
            fail("Exception "+e.getLocalizedMessage());
            assertTrue(false);
        }
    }
     
    public void testParseRequestAndData_WMS_Transaction(){
        /* WMS Transaction */
        try {
            DataWrapper data    = new DataWrapper(this.requestStumb,this.responseStumb);
            
            OGCRequest ogRequest  = new OGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.REQUEST+"="+OGCConstants.WFS_REQUEST_Transaction);
            data.setOgcrequest(ogRequest);
            
            this.servlet.parseRequestAndData(data, user);
            
            assertTrue(true);
        }
        catch(Exception e){
            e.printStackTrace();
            fail("Exception "+e.getLocalizedMessage());
            assertTrue(false);
        }
    }
    
    public void testGetServletInfo(){
        assertEquals(this.servlet.getServletInfo(),"CallWMSServlet info");
    }
}
