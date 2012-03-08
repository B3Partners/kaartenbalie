package general;



import java.util.Enumeration;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 *
 * @author rachelle
 */
public class ConfigStub implements ServletConfig {
    private ServletContextStub context;
    
    public ConfigStub(){
        this.context    = new ServletContextStub();
        
        String homedir      = System.getProperty("user.home");
        String separator    = System.getProperty("file.separator");
        this.setInitParameter("mapfiles", homedir+separator+"mapfiles");
    }
    
    /**
     * Returns the name of this servlet instance. The name may be provided via server administration, assigned in the web application deployment descriptor, or for an unregistered (and thus unnamed) servlet instance it will be the servlet's class name.
     * 
     * @return the name of the servlet instance
     */
    public String getServletName() {
        return "B3Partners testing Servlet Config";
    }

    /**
     * Returns a reference to the ServletContext in which the caller is executing.
     * 
     * @return a ServletContext object, used by the caller to interact with its servlet container
     */
    public ServletContext getServletContext() {
        return this.context;
    }

    /**
     * Returns a String containing the value of the named initialization parameter, or null if the parameter does not exist.
     * 
     * @param name  a String specifying the name of the initialization parameter
     * @return a String containing the value of the initialization parameter
     */
    public String getInitParameter(String name) {
        return this.context.getInitParameter(name);
    }

    /**
     * Returns the names of the servlet's initialization parameters as an Enumeration of String objects, or an empty Enumeration if the servlet has no initialization parameters.
     * 
     * @return     an Enumeration of String objects containing the names of the servlet's initialization parameters
     */
    public Enumeration getInitParameterNames() {
        return this.context.getInitParameterNames();
    }    
    
    /**
     * Sets the initialization parameter with the given name and value
     * 
     * @param name      a String specifying the name of the initialization parameter
     * @param value     a String specifying the value of the initialization parameter
     */
    public void setInitParameter(String name,String value){
        this.context.setInitParameter(name,value);
    }
}
