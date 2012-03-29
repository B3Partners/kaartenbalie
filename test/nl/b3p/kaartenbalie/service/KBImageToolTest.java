package nl.b3p.kaartenbalie.service;

import general.KaartenbalieTestCase;
import java.awt.image.BufferedImage;
import nl.b3p.kaartenbalie.core.server.monitoring.ServiceProviderRequest;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import nl.b3p.servletAPI.GetMethodStub;
import nl.b3p.servletAPI.HttpServletRequestStub;
import nl.b3p.servletAPI.HttpServletResponseStub;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author rachelle
 */
public class KBImageToolTest extends KaartenbalieTestCase {    
    private HttpServletRequestStub request;
    private HttpServletResponseStub response;
    private DataWrapper wrapper;
    private KBImageTool instance;
    
    public KBImageToolTest(String name) {
        super(name);
    }
    
    @Before
    @Override
    public void setUp() throws Exception{
        super.setUp();
        
        this.request        = new HttpServletRequestStub();
        this.response       = new HttpServletResponseStub();
        this.wrapper        = new DataWrapper(this.request,this.response);
        this.instance       = new KBImageTool();
    }
    
    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        
        this.request        = null;
        this.response       = null;
        this.wrapper        = null;
        this.instance       = null;
    }

    /**
     * Test of readImage method, of class KBImageTool.
     */
    @Test
    public void testReadImage() throws Exception {
        GetMethodStub method                = new GetMethodStub();
        
        String mime                         = "bullshit";
        ServiceProviderRequest wmsRequest   = new ServiceProviderRequest();
        BufferedImage expResult             = new BufferedImage(20,20,BufferedImage.TYPE_3BYTE_BGR);
        
        try {
            KBImageTool.readImage(method, mime, wmsRequest);
            
            fail("Mimetype should not be accepted");
            
            assertTrue(false);
        }
        catch(Exception e){
            assertTrue(true);
        }
        
        mime    = "image/raw";
        
        try {
            KBImageTool.readImage(method, mime, wmsRequest);
            
            fail("Reader not expected");
            
            assertTrue(false);
        }
        catch(Exception e){
            assertTrue(true);
        }
        
        mime    = "image/jpeg";
        
        try {
            KBImageTool.readImage(method, mime, wmsRequest);
            
            assertTrue(true);
        }
        catch(Exception e){
            fail("Exception : "+e.getLocalizedMessage());
            assertTrue(false);
        }
    }

    /**
     * Test of writeImage method, of class KBImageTool.
     */
    @Test
    public void testWriteImage() throws Exception {
        System.out.println("writeImage");
        BufferedImage[] images = null;
        String mime = "";
        DataWrapper dw = null;
        //KBImageTool.writeImage(images, mime, dw);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of combineImages method, of class KBImageTool.
     */
    @Test
    public void testCombineImages() {
        System.out.println("combineImages");
        BufferedImage[] images = null;
        String mime = "";
        BufferedImage expResult = null;
        //BufferedImage result = KBImageTool.combineImages(images, mime);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getMimeType method, of class KBImageTool.
     */
    @Test
    public void testGetMimeType() {
        System.out.println("getMimeType");
        String mime = "";
        String expResult = "";
        //String result = KBImageTool.getMimeType(mime);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
}
