package nl.b3p.kaartenbalie.service.servlet;

import general.ConfigStub;
import general.HttpServletRequestStub;
import general.HttpServletResponseStub;
import general.UserStub;
import javax.servlet.ServletConfig;
import junit.framework.TestCase;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;

/**
 *
 * @author rachelle
 */
public class TestCallWMSServlet extends TestCase {
    private ServletConfig configStumb;
    private HttpServletRequestStub requestStumb;
    private HttpServletResponseStub responseStumb;    
    private User user;    
    private CallWMSServlet servlet;
    
    public TestCallWMSServlet(String name){
        super(name);
    }
    
    @Override
    public void setUp(){
        this.user           = UserStub.generateServerUser();
        this.configStumb    = new ConfigStub();
        this.requestStumb   = new HttpServletRequestStub();
        this.responseStumb  = new HttpServletResponseStub();
        
        servlet             = new CallWMSServlet();        
    }
    
    @Override
    public void tearDown(){
        this.user           = null;
        this.configStumb    = null;
        this.servlet        = null;
    }
    
    public void testInit(){
        try {
            this.servlet.init(this.configStumb);
            
            assertTrue(true);
        }
        catch(Exception e){
            e.printStackTrace();
            fail("Init failed");
            
            assertTrue(false);
        }
    }
    
    public void testCreateBaseUrl(){
         StringBuffer baseUrl = this.servlet.createBaseUrl(this.requestStumb);
         
         assertEquals(baseUrl,"http://"+this.requestStumb.getContextPath());
    }
    
    public void testParseRequestAndData(){
        try {
            DataWrapper data    = new DataWrapper(this.requestStumb,this.responseStumb);
            
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
