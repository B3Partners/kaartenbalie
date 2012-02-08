package nl.b3p.kaartenbalie.service.servlet;

import general.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import junit.framework.TestCase;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;

/**
 *
 * @author rachelle
 */
public class TestCallWMSServlet extends TestCase {
    private ServletConfig configStumb;
    private ServletConfig badConfigStumb;
    private HttpServletRequest requestStumb;
    private HttpServletResponse responseStumb;
    
    private User user;
    
    private CallWMSServlet servlet;
    
    public TestCallWMSServlet(String name){
        super(name);
    }
    
    @Override
    public void setUp(){
        this.user           = UserStumb.generateServerUser();
        this.configStumb    = new ConfigStumb();
        
        
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
        catch(ServletException e){
            fail("Init failed");
            
            assertTrue(false);
        }
        
        try {
            this.servlet.init(this.badConfigStumb);
            
            assertTrue(false);
        }
        catch(ServletException e){
            fail("Init failed");
            
            assertTrue(true);
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
            assertTrue(false);
        }
    }
    
    public void testGetServletInfo(){
        assertEquals(this.servlet.getServletInfo(),"CallWMSServlet info");
    }
}
