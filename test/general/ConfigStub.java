package general;



import java.util.Enumeration;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 *
 * @author rachelle
 */
public class ConfigStub implements ServletConfig {
    private ServletContext context;
    
    public ConfigStub(){
        this.context    = new ServletContextStub();
    }
    
    public String getServletName() {
        return "B3Partners testing Servlet Config";
    }

    public ServletContext getServletContext() {
        return this.context;
    }

    public String getInitParameter(String value) {
        return this.context.getInitParameter(value);
    }

    public Enumeration getInitParameterNames() {
        return this.context.getInitParameterNames();
    }    
}
