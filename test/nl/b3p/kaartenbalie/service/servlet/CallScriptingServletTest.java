package nl.b3p.kaartenbalie.service.servlet;

import general.KaartenbalieTestCase;
import javax.servlet.ServletConfig;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author rachelle
 */
public class CallScriptingServletTest extends KaartenbalieTestCase {
    private CallScriptingServlet instance;
    
    public CallScriptingServletTest(String name) {
        super(name);
    }
    
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        instance    = new CallScriptingServlet();
    }
    
    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        
        instance    = null;
    }

    /**
     * Test of init method, of class CallScriptingServlet.
     */
    @Test
    public void testInit() throws Exception {
        System.out.println("init");
        ServletConfig config = null;
        CallScriptingServlet instance = new CallScriptingServlet();
        instance.init(config);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of parseRequestAndData method, of class CallScriptingServlet.
     */
    @Test
    public void testParseRequestAndData() throws Exception {
        System.out.println("parseRequestAndData");
        DataWrapper data = null;
        User user = null;
        CallScriptingServlet instance = new CallScriptingServlet();
        instance.parseRequestAndData(data, user);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getServletInfo method, of class CallScriptingServlet.
     */
    @Test
    public void testGetServletInfo() {
        String expResult = "CallScriptingServlet info";
        String result = instance.getServletInfo();
        assertEquals(expResult, result);
    }
}
