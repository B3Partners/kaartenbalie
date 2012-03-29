package nl.b3p.kaartenbalie.service.servlet;

import general.KaartenbalieTestCase;
import nl.b3p.servletAPI.HttpServletResponseStub;
import nl.b3p.servletAPI.HttpServletRequestStub;
import nl.b3p.servletAPI.ConfigStub;
import nl.b3p.testStreet.*;
import java.io.IOException;
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.ServletConfig;
import org.junit.Test;
import static org.junit.Assert.*;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.monitoring.DataMonitoring;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;

/**
 *
 * @author rachelle
 */
public class CallWMSServletTest extends KaartenbalieTestCase {
    private ServletConfig configStumb;
    private HttpServletRequestStub requestStumb;
    private HttpServletResponseStub responseStumb;    
    private User user;    
    private CallWMSServlet servlet;
    
    public CallWMSServletTest(String name){
        super(name);
    }
    
    @Override
    public void setUp() throws Exception{
        super.setUp();
        
        this.user           = this.generateUser();
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
        this.requestStumb   = null;
        this.responseStumb  = null;
        this.servlet        = null;
    }
    
    /**
     * Log directory misses
     */
    @Test
    public void testCallWMSServlet_Init(){
        try {
            this.servlet.init(this.configStumb);
            
            assertTrue(true);
        }
        catch(Exception e){
            e.printStackTrace();
            fail("Init failed "+e.getLocalizedMessage());
            
            assertTrue(false);
        }
    }
    
    @Test
    public void testCallWMSServlet_CreateBaseUrl(){
         StringBuffer baseUrl = this.servlet.createBaseUrl(this.requestStumb);
        
         assertStringEquals(baseUrl.toString(),this.requestStumb.getServletPath());
    }
    
    public void testCallWMSServlet_ParseRequestAndData_Proxy(){
        /* Proxy */
        try {
            DataWrapper data    = this.getWrapper();
            
            OGCRequest ogRequest  = this.getOGCRequest();
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
    
    @Test
    public void testCallWMSServlet_ParseRequestAndData_MetaData(){
        /* Meta data */        
        try {
            DataWrapper data    = this.getWrapper();
            
            OGCRequest ogRequest  = this.getOGCRequest();
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
    
    @Test
    public void testCallWMSServlet_ParseRequestAndData_WMS(){
        /* WMS */
        try {
            DataWrapper data    = this.getWrapper();
            
            OGCRequest ogRequest  = this.getOGCRequest();
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
    
    @Test
    public void testCallWMSServlet_ParseRequestAndData_WFS(){
        /* WFS */
        try {
            DataWrapper data    = this.getWrapper();
            
            OGCRequest ogRequest  = this.getOGCRequest();
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
    @Test
    public void testCallWMSServlet_ParseRequestAndData_WMS_GetMap(){
        /* WMS GetMap */
        try {
            DataWrapper data    = this.getWrapper();
            
            OGCRequest ogRequest  = this.getOGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.REQUEST+"="+OGCConstants.WMS_REQUEST_GetMap+"&"+OGCConstants.WMS_PARAM_LAYERS+"="+this.layerName);
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
    
    @Test
    public void testCallWMSServlet_ParseRequestAndData_WMS_GetLegendGraphic(){
        /* WMS GetMap */
        try {
            DataWrapper data    = this.getWrapper();
            
            OGCRequest ogRequest  = this.getOGCRequest();
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
    
    @Test
    public void testCallWMSServlet_ParseRequestAndData_WMS_DescribeLayer(){
        /* WMS DescribeLayer */
        try {
            DataWrapper data    = this.getWrapper();
            
            OGCRequest ogRequest  = this.getOGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.REQUEST+"="+OGCConstants.WMS_REQUEST_DescribeLayer+"&"+OGCConstants.WMS_PARAM_LAYERS+"="+this.layerNames);
            data.setOgcrequest(ogRequest);
            
            this.servlet.parseRequestAndData(data, this.user);
            
            assertTrue(true);
        }
        catch(Exception e){
            e.printStackTrace();
            fail("Exception "+e.getLocalizedMessage());
            assertTrue(false);
        }
    }
    
    @Test
    public void testCallWMSServlet_ParseRequestAndData_WMS_DescribeFeatureType(){
        /* WMS DescribeFeatureType */
        try {
            DataWrapper data    = this.getWrapper();
            
            OGCRequest ogRequest  = this.getOGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.REQUEST+"="+OGCConstants.WFS_REQUEST_DescribeFeatureType+"&"+OGCConstants.WFS_PARAM_TYPENAME+"="+this.layerNames);
            
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
    
    @Test
    public void testCallWMSServlet_ParseRequestAndData_WMS_GetFeature(){
        /* WMS GetFeature */
        try {
            DataWrapper data    = this.getWrapper();
            
            OGCRequest ogRequest  = this.getOGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.REQUEST+"="+OGCConstants.WFS_REQUEST_GetFeature+"&"+OGCConstants.WFS_PARAM_TYPENAME+"="+this.layerNames);
            ogRequest.setHttpMethod("GET");
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
     
    @Test
    public void testCallWMSServlet_ParseRequestAndData_WMS_Transaction(){
        /* WMS Transaction */
        try {
            DataWrapper data    = this.getWrapper();
            
            OGCRequest ogRequest  = this.getOGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.REQUEST+"="+OGCConstants.WFS_REQUEST_Transaction+"&"+OGCConstants.WFS_PARAM_OPERATION+"=true");
            //ogRequest.addElementToTransactionList("polygon", "testwaarde");
            data.setOgcrequest(ogRequest);
            
            this.servlet.parseRequestAndData(data, user);
            
            assertTrue(true);
        }
        catch(UnsupportedOperationException e){
            /* Expected
             * Don't want the database to be changed
             */
            assertTrue(true);
        }
        catch(Exception e){
            fail("Exception Expected UnsupportedOperationException. Check on transactionlist incorrect?");
            assertTrue(false);
        }
    }
    
    @Test
    public void testCallWMSServlet_GetServletInfo(){
        assertEquals(this.servlet.getServletInfo(),"CallWMSServlet info");
    }
    
    private DataWrapper getWrapper() throws IOException{
        DataWrapper data    = new DataWrapper(this.requestStumb,this.responseStumb);
        
        DataMonitoring monitoring   = new DataMonitoring();
        data.setRequestReporting(monitoring);
        
        return data;
    }
    
    private OGCRequest getOGCRequest(){
        OGCRequest ogRequest  = new OGCRequest();
        ogRequest.setHttpHost(this.requestStumb.getServerName());
        
        return ogRequest;
    }
}
