package nl.b3p.kaartenbalie.service.requesthandler;

import general.KaartenbalieTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import nl.b3p.servletTestAPI.ByteArrayInputStreamStub;
import nl.b3p.servletTestAPI.LocatorStub;

/**
 *
 * @author rachelle
 */
public class DOMValidatorTest extends KaartenbalieTestCase {    
    private ByteArrayInputStreamStub byteArrayInputStreamStub;
    private DOMValidator validator;
    private SAXParseException exception;
    
    public DOMValidatorTest(String name) {
        super(name);
    }
    
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        byte[] buffer                   = new byte[100];
        this.byteArrayInputStreamStub   = new ByteArrayInputStreamStub(buffer);
        this.exception                  = new SAXParseException("Testing exception",new LocatorStub());
                
        this.validator                  = new DOMValidator();
    }
    
    @After
    @Override
     public void tearDown() throws Exception {
        super.setUp();
        
        this.byteArrayInputStreamStub   = null;
        this.validator                  = null;
        this.exception                  = null;
    }

    /**
     * Test of parseAndValidate method, of class DOMValidator.
     */
    @Test
    public void testDOMValidator_ParseAndValidate(){
        try {
            this.validator.parseAndValidate(byteArrayInputStreamStub);
            
            fail("Test schould fail on empty buffer");
            assertTrue(false);
        }
        catch(Exception e){
            /* Empty buffer. expected to fail */
            assertTrue(true);
        }
    }

    /**
     * Test of warning method, of class DOMValidator.
     */
    @Test
    public void testDOMValidator_Warning(){
        try {
            this.validator.warning(this.exception);
            
            fail("Function schould throw a SAXParseException");
            
            assertTrue(false);
        }
        catch(SAXParseException e){
            assertTrue(true);
        }
    }

    /**
     * Test of error method, of class DOMValidator.
     */
    @Test
    public void testDOMValidator_Error(){
        try {
            this.validator.error(this.exception);
            
            fail("Function schould throw a SAXParseException");
            
            assertTrue(false);
        }
        catch(SAXParseException e){
            assertTrue(true);
        }
    }

    /**
     * Test of fatalError method, of class DOMValidator.
     */
    @Test
    public void testDOMValidator_FatalError(){
        try {
            this.validator.fatalError(this.exception);
            
            fail("Function schould throw a SAXException");
            
            assertTrue(false);
        }
        catch(SAXException e){
            assertTrue(true);
        }
    }
}
