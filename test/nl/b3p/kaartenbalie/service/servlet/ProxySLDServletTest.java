/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.b3p.kaartenbalie.service.servlet;

import general.B3TestCase;
import stubs.ConfigStub;
import javax.servlet.ServletException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author rachelle
 */
public class ProxySLDServletTest extends B3TestCase {
    private ConfigStub configStub;
    private ProxySLDServlet servlet;
    
    public ProxySLDServletTest(String name) {
        super(name);
    }
    
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        this.configStub     = new ConfigStub();
        this.servlet        = new ProxySLDServlet();
    }
    
    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        
        this.configStub     = null;
        this.servlet        = null;
    }
    
    /**
     * Test of init method, of class ProxySLDServlet.
     */
    @Test
    public void testProxySLDServlet_Init() {
        try {
            this.servlet.init(this.configStub);
            
            assertTrue(true);
        }
        catch(ServletException e){
            fail("Initializing ProxySLDServlet failed");
            
            assertTrue(false);
        }
    }
    
    /**
     * Test of getServletInfo method, of class ProxySLDServlet.
     */
    @Test
    public void testProxySLDServlet_GetServletInfo(){
        assertStringEquals("Short description",this.servlet.getServletInfo());
    }
}
