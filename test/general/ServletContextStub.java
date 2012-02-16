package general;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 *
 * @author rachelle
 */
class ServletContextStub implements ServletContext {
    private ServerDetailEnumeration serverNames;
    private final String logFile    = "ServletTestLog.log";
    private HashMap<String,String> initParameters;
    private HashMap<String,Object> attributes;
    
    public ServletContextStub() {
        this.serverNames    = new ServerDetailEnumeration();
        
        this.initParameters = new HashMap<String,String>();
        this.initParameters.put("name", "B3partners test server");
        this.initParameters.put("version","1.0");
        this.serverNames.addName("name");
        this.serverNames.addName("version");
        
        this.attributes = new HashMap<String,Object>();
    }
    
    /**
     * Returns a ServletContext object that corresponds to a specified URL on the server. 
     * 
     * @param uripath   a String specifying the context path of another web application in the container.
     * @return          the ServletContext object that corresponds to the named URL, or null if either none exists or the container wishes to restrict this access.
     */
    public ServletContext getContext(String uripath) {
        return this;
    }

    /**
     * Returns the major version of the JAVA API
     * 
     * @return The major version
     */
    public int getMajorVersion() {
        return 2;
    }

    /**
     * Returns the minor version of the JAVA API
     * 
     * @return The minor version
     */
    public int getMinorVersion() {
        return 3;
    }

    /**
     * Returns the MIME type of the specified file, or null if the MIME type is not known.
     * 
     * @param file      The filename
     * @return The mime type
     */
    public String getMimeType(String file) {
        if( file.endsWith("htm") || file.endsWith("html") || file.endsWith("jsp") ){
            return "text/html";
        }
        if( file.endsWith("png") ){
            return "image/png";
        }
        if( file.endsWith("jpg") || file.endsWith("jpeg") ){
            return "image/jpeg";
        }
        if( file.endsWith("bmp") ){
            return "image/bmp";
        }
        if( file.endsWith("gif") ){
            return "image/gif";
        }
        if( file.endsWith("xml") ){
            return "application/XML";
        }
        
        return null;
    }

    /**
     * Returns a directory-like listing of all the paths to resources within the web 
     * application whose longest sub-path matches the supplied path argument. 
     * Paths indicating subdirectory paths end with a '/'. The returned paths are all 
     * relative to the root of the web application and have a leading '/'.
     * 
     * @param path      The pathname
     * @return          The directory content
     */
    public Set getResourcePaths(String path) {
        if( !path.startsWith("/") ) return null;
        
        File dir = new File(path);
        if( dir.isFile() )      return null;
        
        String[] children = dir.list();
        if( children == null)   return null;
        
        if( !path.endsWith("/") )   path    += "/";
        
        Set content = new HashSet();
        File check;
        for (int i=0; i<children.length; i++) {
            if( children[i].equals(".") || children[i].equals("..") )   continue;
            
            check   = new File(children[i]);
            if( check.isDirectory() ){
                content.add(path+children[i]+"/");
            }
            else {
                content.add(path+children[i]);
            }
        }
        
        return content;
    }

    /**
     * 
     * @todo        Implement function ServletContextStumb::getResource
     * @param path
     * @return
     * @throws MalformedURLException 
     */
    public URL getResource(String path) throws MalformedURLException {
        if( !path.startsWith("/") ) throw new MalformedURLException("Path must start with a /");
        URL url = new URL(path);
       
        try {
            URLConnection connection = url.openConnection();
            connection = null;
            return url;
            
        }
        catch(IOException e){
            throw new MalformedURLException("Resource "+path+" does not exist");
        }
    }

    /**
     * * @todo        Implement function ServletContextStumb::getResourceAsStream
     * @param string
     * @return 
     */
    public InputStream getResourceAsStream(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public RequestDispatcher getRequestDispatcher(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public RequestDispatcher getNamedDispatcher(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * This method was originally defined to return an Enumeration of all the 
     * servlets known to this servlet context. In this version, 
     * this method always returns an empty enumeration and remains only to preserve 
     * binary compatibility. This method will be permanently removed in a future 
     * version of the Java Servlet API.
     * 
     * @deprecated      As of Java Servlet API 2.0, with no replacement. 
     * @param string    The servlet name
     * @return          null
     * @throws ServletException 
     */
    public Servlet getServlet(String string) throws ServletException {
        return null;
    }

    /**
     * This method was originally defined to return an Enumeration of all the servlets 
     * known to this servlet context. In this version, this method always returns an 
     * empty enumeration and remains only to preserve binary compatibility. 
     * This method will be permanently removed in a future version of the Java Servlet API.
     * 
     * @deprecated As of Java Servlet API 2.0, with no replacement.
     * @return     Empty Enumeration
     */
    public Enumeration getServlets() {
        return new ServerDetailEnumeration();
    }

    /**
     * This method was originally defined to return an Enumeration of all the servlet 
     * names known to this context. In this version, this method always returns an 
     * empty Enumeration and remains only to preserve binary compatibility. This method 
     * 1will be permanently removed in a future version of the Java Servlet API.
     * 
     * @deprecated  As of Java Servlet API 2.1, with no replacement. 
     * @return      Empty Enumeration
     */
    public Enumeration getServletNames() {
        return new ServerDetailEnumeration();
    }

    /**
     * Writes the specified message to a servlet log file, usually an event log.
     * @param msg       The log message
     */
    public void log(String msg) {
        try {
            FileWriter logger   = new FileWriter(new File(this.logFile));
            logger.write(msg+"\n");
            logger.close();
        }
        catch(IOException e){ }
    }

    /**
     * This method was originally defined to write an exception's stack trace and an 
     * explanatory error message to the servlet log file.
     * 
     * @deprecated          As of Java Servlet API 2.1, use log(String message, Throwable throwable) instead. 
     * @param exception     The exception
     * @param msg           The log message
     */
    public void log(Exception exception, String msg) {
        this.log(msg, exception);
    }

    /**
     * Writes an explanatory message and a stack trace for a given Throwable exception 
     * to the servlet log file. The name and type of the servlet log file is specific 
     * to the servlet container, usually an event log.
     * 
     * @param message       The log message
     * @param thrwbl        The exception
     */
    public void log(String message, Throwable thrwbl) {
        this.log(message+" : \n"+thrwbl.getMessage());
    }

    /**
     * Returns a String containing the real path for a given virtual path.
     * 
     * @param path      The relative path
     * @return          null
     */
    public String getRealPath(String path) {
        return null;
    }

    /**
     * Returns the name and version of the servlet container on which the servlet is running. 
     * 
     * @return a String containing at least the servlet container name and version number
     */
    public String getServerInfo() {
        return this.initParameters.get("name")+"/"+this.initParameters.get("version");        
    }

    /**
     * Returns a String containing the value of the named context-wide initialization parameter, or null if the parameter does not exist. 
     * 
     * @param name  The parameter name
     * @return      a String containing at least the servlet container name and version number
     */
    public String getInitParameter(String name) {
        if( this.initParameters.containsKey(name) ){
            return this.initParameters.get(name);
        }
        return null;
    }

    /**
     * Returns the names of the context's initialization parameters as an Enumeration of String objects, or an empty Enumeration if the context has no initialization parameters.
     * 
     * @return an Enumeration of String objects containing the names of the context's initialization parameters
     */
    public Enumeration getInitParameterNames() {
        return this.serverNames;
    }

    /**
     * Returns the servlet container attribute with the given name, or null if there is no attribute by that name. 
     * 
     * @param name      The attribute name
     * @return          The attribute value
     */
    public Object getAttribute(String name) {
        if( this.attributes.containsKey(name) ){
            return this.attributes.get(name);
        }
        
        return null;
    }

    /**
     * Returns an Enumeration containing the attribute names available within this servlet context. 
     * 
     * @return  an Enumeration of attribute names
     */
    public Enumeration getAttributeNames() {
        ServerDetailEnumeration names   = new ServerDetailEnumeration();
        Object[] keys    = this.attributes.keySet().toArray();
        for(int i=0; i<keys.length; i++){
            names.addName((String) keys[i]);
        }
        return names;
    }

    /**
     * Binds an object to a given attribute name in this servlet context. 
     * If the name specified is already used for an attribute, this method will 
     * replace the attribute with the new to the new attribute. 
     * 
     * @param name        a String specifying the name of the attribute
     * @param object      an Object representing the attribute to be bound   
     */
    public void setAttribute(String name, Object object) {
        if( this.attributes.containsKey(name) ){
            this.removeAttribute(name);
            
            if( object != null ){
                this.attributes.put(name, object);
            }
        }
        else {
            this.attributes.put(name, object);
        }
    }

    /**
     * Removes the attribute with the given name from the servlet context. 
     * 
     * @param name a String specifying the name of the attribute to be removed
     */
    public void removeAttribute(String name){
        if( this.attributes.containsKey(name) ){
            this.attributes.remove(name);
        }
    }

    /**
     * Returns the name of this web application correponding to this ServletContext as specified in the deployment descriptor for this web application by the display-name element.
     * 
     * @return The name of the web application or null if no name has been declared in the deployment descriptor.
     */
    public String getServletContextName() {
        return null;
    }    
}
