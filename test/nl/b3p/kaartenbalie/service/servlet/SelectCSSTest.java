package nl.b3p.kaartenbalie.service.servlet;

import general.KaartenbalieTestCase;
import nl.b3p.servletAPI.ConfigStub;
import nl.b3p.servletAPI.HttpServletRequestStub;
import nl.b3p.servletAPI.HttpServletResponseStub;
import javax.servlet.ServletException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author rachelle
 */
public class SelectCSSTest extends KaartenbalieTestCase {   
    private ConfigStub configStub;
    private SelectCSS servlet;
    private HttpServletRequestStub httpServletRequestStub;
    private HttpServletResponseStub httpServletResponseStub;
    
    public SelectCSSTest(String name) {
        super(name);
    }
    
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        this.configStub                 = new ConfigStub();
        this.servlet                    = new SelectCSS();
        this.httpServletRequestStub     = new HttpServletRequestStub();
        this.httpServletResponseStub    = new HttpServletResponseStub();
        
        this.configStub.setInitParameter("csspath_default","css/style.css");
        this.configStub.setInitParameter("server1","http://server1.com");
        this.configStub.setInitParameter("csspath1","css/css1.css");
        this.configStub.setInitParameter("server2","http://server2.com");
        this.configStub.setInitParameter("csspath2","css/css2.css");
    }
    
    @After
    @Override
    public void tearDown() throws Exception{
        super.setUp();
        
        this.configStub                 = null;
        this.servlet                    = null;
        this.httpServletRequestStub     = null;
        this.httpServletResponseStub    = null;
    }

    /**
     * Test of init method, of class SelectCSS.
     */
    @Test
    public void testSelectCSS_Init() {
        try {            
            this.servlet.init(this.configStub);
            
            assertTrue(true);
        }
        catch(ServletException e){
            fail("Init of SelectCSS failed "+e.getLocalizedMessage());
            
            assertTrue(false);
        }
    }

    /**
     * Test of doGet method, of class SelectCSS.
     */
    @Test
    public void testSelectCSS_DoGet(){
        try {
            this.servlet.init(this.configStub);
            
            this.servlet.doGet(this.httpServletRequestStub, this.httpServletResponseStub);
            
            assertTrue(true);
        }
        catch(ServletException e){
            fail(e.getLocalizedMessage());
            
            assertTrue(false);
        }
    }
}
