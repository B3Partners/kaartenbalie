package nl.b3p.kaartenbalie.core.server.b3pLayering;

import general.KaartenbalieTestCase;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.servletTestAPI.HttpServletRequestStub;
import nl.b3p.servletTestAPI.HttpServletResponseStub;
import org.junit.Test;

/**
 *
 * @author rachelle
 */
abstract public class ConfigLayerTest extends KaartenbalieTestCase { 
    protected ConfigLayer instance;
    
    public ConfigLayerTest(String name) {
        super(name);
    }

    /**
     * Test of forName method, of class ConfigLayer.
     */
    @Test
    public void testForName(){
        /* Check for null */
        try {
            ConfigLayer.forName(null);
            
            fail("Function should throw a exception");
        }
        catch(Exception e){}
        
        /* Check for invalid name */
        try {
            ConfigLayer.forName("Config layer");
            
            fail("Function should throw a exception");
        }
        catch(Exception e){}
        
        /* Check for valid call */
        try {
            ConfigLayer.forName(AllowTransactionsLayer.NAME);
        }
        catch(Exception e){
            fail("Exception : "+e.getLocalizedMessage());
        }
    }
    
    /**
     * Test of handleRequest method, of class ConfigLayer.
     */
    @Test
    public void testHandleRequest(){
        try {
            String url = this.getRequestUrl();
            Map parameterMap = this.getParameterMap();
            ConfigLayer.handleRequest(url, parameterMap);
        }
        catch(Exception e){
            fail("Exception : "+e.getLocalizedMessage());
        }
    }
    
    /**
     * Test of getConfigLayers method, of class ConfigLayer.
     */
    @Test
    public void testGetConfigLayers() {
        Map result = ConfigLayer.getConfigLayers();
        assertTrue(result.containsKey(AllowTransactionsLayer.NAME));
        assertTrue(result.containsKey(KBTitleLayer.NAME));
    }
    /**
     * Test of drawImage method, of class ConfigLayer.
     */
    @Test
    public void testDrawImage(){
        try {
            OGCRequest ogcrequest   = this.getRequest();
            Map parameterMap = this.getParameterMap();
        
            instance.drawImage(ogcrequest, parameterMap);
        }
        catch(Exception e){
            fail("Exception : "+e.getLocalizedMessage());
        }
    }

    /**
     * Test of imageHandler method, of class ConfigLayer.
     */
    @Test
    public void testImageHandler(){
        OGCRequest ogcrequest   = this.getRequest();
        Map parameterMap = this.getParameterMap();
        parameterMap.isEmpty();
        try {
            instance.imageHandler(ogcrequest, parameterMap);
        }
        catch(Exception e){
            fail("Exception : "+e.getLocalizedMessage());
        }
    }

    /**
     * Test of sendImage method, of class ConfigLayer.
     */
    @Test
    public void testSendImage() {
        HttpServletRequestStub  request = new HttpServletRequestStub();
        HttpServletResponseStub response = new HttpServletResponseStub();
        try {
            DataWrapper data        = new DataWrapper(request,response);
            
            /* Check image/jpeg */
            OGCRequest OGCrequest    = this.getRequest();
            OGCrequest.addOrReplaceParameter(OGCConstants.WMS_PARAM_FORMAT, "image/jpeg");
            data.setOgcrequest(OGCrequest);            
            BufferedImage bufImage  = new BufferedImage(10,10,BufferedImage.TYPE_INT_RGB);
            instance.sendImage(data, bufImage);
            
            /* Check image/png */
            OGCrequest    = this.getRequest();
            OGCrequest.addOrReplaceParameter(OGCConstants.WMS_PARAM_FORMAT, "image/png");
            data.setOgcrequest(OGCrequest);            
            bufImage  = new BufferedImage(10,10,BufferedImage.TYPE_INT_RGB);
            instance.sendImage(data, bufImage);
            
            /* Check image/gif */
            OGCrequest    = this.getRequest();
            OGCrequest.addOrReplaceParameter(OGCConstants.WMS_PARAM_FORMAT, "image/gif");
            data.setOgcrequest(OGCrequest);            
            bufImage  = new BufferedImage(10,10,BufferedImage.TYPE_INT_RGB);
            instance.sendImage(data, bufImage);
        }
        catch(Exception e){
            fail("Exception : "+e.getLocalizedMessage());
        }
    }
    
    protected HashMap<Object,Object> getParameterMap(){
        HashMap<Object,Object> parameterMap = new HashMap<Object,Object>();        
        parameterMap.put("transparant", Boolean.FALSE);
        parameterMap.put("showname", Boolean.TRUE);
        
        return parameterMap;
    }
    
    protected String getRequestUrl(){
        return "http://example.com?"+OGCConstants.WMS_PARAM_LAYERS+"="+
                    AllowTransactionsLayer.NAME+"&"+OGCConstants.WMS_PARAM_WIDTH+
                    "=10&"+OGCConstants.WMS_PARAM_HEIGHT+"=10";
    }
    
    protected OGCRequest getRequest(){
        return new OGCRequest(this.getRequestUrl());
    }
}
