package nl.b3p.kaartenbalie.service.requesthandler;
import general.B3TestCase;
import stubs.HttpServletRequestStub;
import stubs.HttpServletResponseStub;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author rachelle
 */
public class TextToImageTest extends B3TestCase {
    private HttpServletRequestStub request;
    private HttpServletResponseStub response;
    private DataWrapper wrapper;
    private TextToImage instance;
    private String message = "Text message";
    
    public TextToImageTest(String name){
        super(name);
    }
    
    @Before
    @Override
    public void setUp() throws Exception{
        super.setUp();
        
        this.request        = new HttpServletRequestStub();
        this.response       = new HttpServletResponseStub();
        this.wrapper        = new DataWrapper(this.request,this.response);
        this.instance       = new TextToImage();
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
     * Test of createImage method, of class TextToImage.
     * JPEG image
     */
    @Test
    public void testCreateImage_JPEG(){
        try {
            OGCRequest OGrequest    = new OGCRequest();
            OGrequest.addOrReplaceParameters(OGCConstants.WMS_PARAM_FORMAT+"=image/jpeg&"+OGCConstants.WMS_PARAM_WIDTH+"=50&"+OGCConstants.WMS_PARAM_HEIGHT+"=50");
            
            this.wrapper.setOgcrequest(OGrequest);
            
            this.instance.createImage(message,this.wrapper);
            
            assertTrue(true);
        }
        catch(Exception e){
            fail("Exception "+e.getLocalizedMessage());
            
            assertTrue(false);
        }
    }
    
    /**
     * Test of createImage method, of class TextToImage.
     * GIF image
     */
    @Test
    public void testCreateImage_GIF(){
        try {
            OGCRequest OGrequest    = new OGCRequest();
            OGrequest.addOrReplaceParameters(OGCConstants.WMS_PARAM_FORMAT+"=image/gif&"+OGCConstants.WMS_PARAM_WIDTH+"=50&"+OGCConstants.WMS_PARAM_HEIGHT+"=50");
            
            this.wrapper.setOgcrequest(OGrequest);
            
            this.instance.createImage(message,this.wrapper);
            
            assertTrue(true);
        }
        catch(Exception e){
            fail("Exception "+e.getLocalizedMessage());
            
            assertTrue(false);
        }
    }
    
    /**
     * Test of createImage method, of class TextToImage.
     * PNG image
     */
    @Test
    public void testCreateImage_PNG(){
        try {
            OGCRequest OGrequest    = new OGCRequest();
            OGrequest.addOrReplaceParameters(OGCConstants.WMS_PARAM_FORMAT+"=image/png&"+OGCConstants.WMS_PARAM_WIDTH+"=50&"+OGCConstants.WMS_PARAM_HEIGHT+"=50");
            
            this.wrapper.setOgcrequest(OGrequest);
            
            this.instance.createImage(message,this.wrapper);
            
            assertTrue(true);
        }
        catch(Exception e){
            fail("Exception "+e.getLocalizedMessage());
            
            assertTrue(false);
        }
    }
    
    /**
     * Test of createImage method, of class TextToImage.
     * Check on bullshit width
     * Function should set with to 200
     */
    @Test
    public void testCreateImage_width(){
        try {
            OGCRequest OGrequest    = new OGCRequest();
            OGrequest.addOrReplaceParameters(OGCConstants.WMS_PARAM_FORMAT+"=image/jpeg&"+OGCConstants.WMS_PARAM_WIDTH+"=-50&"+OGCConstants.WMS_PARAM_HEIGHT+"=50");
            
            this.wrapper.setOgcrequest(OGrequest);
            
            this.instance.createImage(message,this.wrapper);
            
            assertTrue(true);
        }
        catch(Exception e){
            fail("Exception "+e.getLocalizedMessage());
            
            assertTrue(false);
        }
    }
    
    /**
     * Test of createImage method, of class TextToImage.
     * Check on bullshit height
     * Function should set height to 200
     */
    @Test
    public void testCreateImage_height(){
        try {
            OGCRequest OGrequest    = new OGCRequest();
            OGrequest.addOrReplaceParameters(OGCConstants.WMS_PARAM_FORMAT+"=image/jpeg&"+OGCConstants.WMS_PARAM_WIDTH+"=50&"+OGCConstants.WMS_PARAM_HEIGHT+"=-50");
            
            this.wrapper.setOgcrequest(OGrequest);
            
            this.instance.createImage(message,this.wrapper);
            
            assertTrue(true);
        }
        catch(Exception e){
            fail("Exception "+e.getLocalizedMessage());
            
            assertTrue(false);
        }
    }
    
    /**
     * Test of createImage method, of class TextToImage.
     * Check on bullshit image type
     * Function should ignore the image-type
     */
    @Test
    public void testCreateImage_imageType(){
        try {
            OGCRequest OGrequest    = new OGCRequest();
            OGrequest.addOrReplaceParameters(OGCConstants.WMS_PARAM_FORMAT+"=image/bullshit&"+OGCConstants.WMS_PARAM_WIDTH+"=50&"+OGCConstants.WMS_PARAM_HEIGHT+"=50");
            
            this.wrapper.setOgcrequest(OGrequest);
            
            this.instance.createImage(message,this.wrapper);
            
            assertTrue(true);
        }
        catch(Exception e){
            fail("Exception "+e.getLocalizedMessage());
            
            assertTrue(false);
        }
    }
}
