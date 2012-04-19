package nl.b3p.kaartenbalie.service;

import general.KaartenbalieTestCase;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.IIOException;
import nl.b3p.kaartenbalie.core.server.monitoring.DataMonitoring;
import nl.b3p.kaartenbalie.core.server.monitoring.ServiceProviderRequest;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import nl.b3p.servletAPI.GetMethodStub;
import nl.b3p.servletAPI.HttpServletRequestStub;
import nl.b3p.servletAPI.HttpServletResponseStub;
import nl.b3p.servletAPI.InputStreamStub;
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
        this.wrapper.setRequestReporting(new DataMonitoring());
        
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
    public void testReadImage(){
        GetMethodStub method                = new GetMethodStub();
        
        String mime                         = "bullshit";
        ServiceProviderRequest wmsRequest   = new ServiceProviderRequest();
                
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
            /* Set stream */
            InputStreamStub is = (InputStreamStub) method.getResponseBodyAsStream();
            ArrayList<Byte> content = new ArrayList<Byte>();
            byte c  = 10;
            content.add(c);
            content.add(c);
            content.add(c);
            is.setContent(content);
            
            KBImageTool.readImage(method, mime, wmsRequest);
            
            fail("Exception : not a valid JPEG. Should fail.");
            assertTrue(false);
        }
        catch(IIOException e){
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
    public void testWriteImage(){
        String mime                         = "bullshit";
        BufferedImage[] images              = this.getImages();
        
        try {
            KBImageTool.writeImage(images, mime, this.wrapper);
            
            fail("Mimetype should not be accepted");
            
            assertTrue(false);
        }
        catch(Exception e){
            assertTrue(true);
        }
        
        mime    = "image/jpeg";
        try {
            KBImageTool.writeImage(images, mime, this.wrapper);
            
            assertTrue(true);
        }
        catch(Exception e){
            fail("Exception : "+e.getLocalizedMessage());
            assertTrue(false);
        }
    }

    /**
     * Test of combineImages method, of class KBImageTool.
     */
    @Test
    public void testCombineImages() {
        try {
            String mime                         = "image/png";
            BufferedImage[] images              = this.getImages();
            
            KBImageTool.combineImages(images, mime);
        }
        catch(Exception e){
            fail("Exception : "+e.getLocalizedMessage());
        }
        
        try {
            String mime                         = "image/jpeg";
            BufferedImage[] images              = this.getImages();
            
            KBImageTool.combineImages(images, mime);
        }
        catch(Exception e){
            fail("Exception : "+e.getLocalizedMessage());
        }
    }

    /**
     * Test of getMimeType method, of class KBImageTool.
     */
    @Test
    public void testGetMimeType() {
        String mime                         = "bullshit";
        assertNull(KBImageTool.getMimeType(mime));
        
        mime                         = "image/png";
        assertNotNull(KBImageTool.getMimeType(mime));        
    }
    
    private BufferedImage[] getImages(){
        BufferedImage expResult             = new BufferedImage(20,20,BufferedImage.TYPE_3BYTE_BGR);
        BufferedImage expResult2            = new BufferedImage(20,20,BufferedImage.TYPE_INT_ARGB_PRE);        
        BufferedImage[] images              = new BufferedImage[2];
        images[0]   = expResult;
        images[1]   = expResult2;
        
        return images;
    }
}
