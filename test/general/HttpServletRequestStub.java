package general;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author rachelle
 */
public class HttpServletRequestStub implements HttpServletRequest {
    private Cookie[] cookies;
    private ArrayList<String> headerNames;
    private ArrayList<String> headers;
    private String method;
    private String context;
    private final String seperator  = System.getProperty("file.separator");
    private String server;
    private String pathInfo;
    private String query;
    private nl.b3p.kaartenbalie.core.server.User user;
    private String filename;
    private int port;
    private String protocol;
    private HttpSessionStub currentSession;
    private String requestedSessionID;
    private HashMap<String,Object> attributes;
    private String characterEncoding;
    private ServletInputStreamStub inputStream;
    
    public HttpServletRequestStub(){
        this.cookies        = new Cookie[10];
        this.headerNames    = new ArrayList<String>();
        this.headers        = new ArrayList<String>();
        this.attributes     = new HashMap<String,Object>();
        
        this.protocol           = "http://";
        this.port               = 80;
        this.method             = "GET";
        this.filename           = "";
        this.context            = "";
        this.server             = "localhost";
        this.pathInfo           = null;
        this.query              = null;
        this.user               = null;
        this.currentSession     = null;
        this.requestedSessionID = "";
        this.characterEncoding  = null;
        this.inputStream        = null;
    }
    
    /**
     * Finds a header name from the begin
     * 
     * @param key  The head name
     * @return  The position or -1 if the header name does not exist.
     */
    private int findHeader(String key){
        return this.findHeader(key, 0);
    }
    
    /**
     * Finds a header name from the given position
     * 
     * @param   key     The head name
     * @param   offPos  The position to search from
     * @return  The position or -1 if the header name does not (more) exist.
     */
    private int findHeader(String key,int offPos){
        for(int i=offPos; i<this.headerNames.size(); i++){
            if( this.headerNames.get(i).equals(key) ){
                return i;
            }
        }
        
        return -1;
    }
    
    /**
     * Sets the protocol
     * 
     * @param protocol    The protocol (http://|https://)
     */
    public void setProtocol(String protocol){
        if( protocol.equals("http://") || protocol.equals("https://") )
            this.protocol = protocol;
    }
    
    /**
     * Sets the port
     * 
     * @param port The port
     */
    public void setPort(int port){
        if( port > 0 ){
            this.port   = port;
        }
    }
    
    /**
     * Sets the filename
     * 
     * @param filename The filename
     */
    public void setFilename(String filename){
        if( filename != null && !filename.equals("") )
            this.filename   = filename;
    }
    
    /**
     * Returns the name of the authentication scheme used to protect the servlet.
     * 
     * @return Autorisation scheme
     */
    public String getAuthType() {
        return HttpServletRequest.BASIC_AUTH;
    }

    /**
     * Returns an array containing all of the Cookie objects the client sent with this request. This method returns null if no cookies were sent. 
     * 
     * @return  an array of all the Cookies included with this request, or null if the request has no cookies
     */
    public Cookie[] getCookies() {
        if( this.cookies.length == 0 )  return null;
        
        return this.cookies;
    }

    /**
     * Returns the value of the specified request header as a long value that represents a Date object. Use this method with headers that contain dates, such as If-Modified-Since
     * 
     * @param name      a String specifying the name of the header 
     * @return          a long value representing the date specified in the header expressed as the number of milliseconds since January 1, 1970 GMT, or -1 if the named header was not included with the request 
     * @throws IllegalArgumentException             If the header value can't be converted to a date
     */
    public long getDateHeader(String name) throws IllegalArgumentException {
        name    = name.toLowerCase();
        int pos = this.findHeader(name);
        if( pos == -1 )     return -1;
        
        try {
            SimpleDateFormat format   = new SimpleDateFormat();
            return format.parse(this.headers.get(pos)).getTime();
        }
        catch(ParseException e){
            throw new IllegalArgumentException("Header "+name+" is not a date");
        }
    }

    /**
     * Returns the value of the specified request header as a String. If the request did not include a header of the specified name, this method returns null. If there are multiple headers with the same name, this method returns the first head in the request. The header name is case insensitive. You can use this method with any request header. 
     * 
     * @param name      a String specifying the header name 
     * @return  a String containing the value of the requested header, or null if the request does not have a header of that name
     */
    public String getHeader(String name) {
        name    = name.toLowerCase();
        int pos = this.findHeader(name);
        
        if( pos == -1 )   return null;
        
        return this.headers.get(pos);
    }

    /**
     * Returns all the values of the specified request header as an Enumeration of String objects. 
     * 
     * @param   name  a String specifying the header name. The header name is case insensitive.
     * @return  an Enumeration containing the values of the requested header. If the request does not have any headers of that name return an empty enumeration. 
     */
    public Enumeration getHeaders(String name) {
        name    = name.toLowerCase();
        ServerDetailEnumeration headernames = new ServerDetailEnumeration();
        
        int pos = 0;
        while( true ){            
            pos = this.findHeader(name, pos);            
            if( pos == -1 ) break; // no more items
            
            headernames.addName(this.headers.get(pos));
        }
        
        return headernames;        
    }

    /**
     * Returns an enumeration of all the header names this request contains. If the request has no headers, this method returns an empty enumeration. 
     * 
     * @return an enumeration of all the header names sent with this request. if the request has no headers, an empty enumeration
     */
    public Enumeration getHeaderNames() {
        ServerDetailEnumeration headernames = new ServerDetailEnumeration();
        
        for(int i=0; i<this.headerNames.size(); i++){
            headernames.addName(this.headerNames.get(i));
        }
        
        return headernames;
    }

    /**
     * Returns the value of the specified request header as an int. If the request does not have a header of the specified name, this method returns -1. If the header cannot be converted to an integer, this method throws a NumberFormatException. 
     * 
     * @param   name  a String specifying the header name. The header name is case insensitive.
     * @return  an integer expressing the value of the request header or -1 if the request doesn't have a header of this name 
     * @throws  NumberFormatException If the header value can't be converted to an int
     */
    public int getIntHeader(String name) throws NumberFormatException{
        name    = name.toLowerCase();
        int pos = this.findHeader(name);
        
        if( pos == -1 ) return -1;
        
        return Integer.parseInt(this.headers.get(pos));
    }
    
    /**
     * Sets the name of the HTTP method with wich this request was made
     * 
     * @param method The HTTP method (GET|POST|PUT)
     */
    public void setMethod(String method){
        if( method.equals("GET") || method.equals("POST") || method.equals("PUT") )
            this.method = method;
    }

    /**
     * Returns the name of the HTTP method with which this request was made
     * 
     * @return a String specifying the name of the method with which this request was made
     */
    public String getMethod() {
        return this.method;
    }
    
    /**
     * Sets the path info
     * 
     * @param pathInfo  The path info
     */
    public void setPathInfo(String pathInfo){
        this.pathInfo   = pathInfo;
    }

    /**
     * Returns any extra path information associated with the URL the client sent when it made this request. The extra path information follows the servlet path but precedes the query string and will start with a "/" character. 
     * 
     * @return  a String, decoded by the web container, specifying extra path information that comes 
     * after the servlet path but before the query string in the request URL or null if the URL does 
     * not have any extra path information
     */
    public String getPathInfo() {
        if( this.pathInfo == null ) return null;
        
        return this.pathInfo;
    }
    
    /**
     * Sets the server address
     * 
     * @param server    The server address
     */
    public void setServer(String server){
        if( server != null )    this.server     = server;
    }

    /**
     * Returns any extra path information after the servlet name but before the query string
     * 
     * @return a String specifying the real path, or null if the URL does not have any extra path information
     */
    public String getPathTranslated() {
        return this.server+this.seperator+this.pathInfo;
    }

    /**
     * Sets the portion of the request URI that indicates the context of the request. The context path always comes first in a request URI. 
     * The path must start with a / and may not end with a /.
     * 
     * @param   path a String specifying the portion of the request URI that indicates the context of the request
     * @throws  IllegalArgumentException    if the path is invalid
     */
    public void setContextPath(String path) throws IllegalArgumentException {
        if( !path.startsWith("") || path.endsWith("/") )    throw new IllegalArgumentException("Context "+path+ " is invalid");
        
        this.context    = path;
    }
    
    /**
     * Returns the portion of the request URI that indicates the context of the request. The context path always comes first in a request URI. 
     * 
     * @return a String specifying the portion of the request URI that indicates the context of the request
     */
    public String getContextPath() {
        return this.context;
    }

    /**
     * Sets the query string that is contained in the request URL after the path.
     * 
     * @param query a String containing the query string or null if the URL contains no query string. 
     */
    public void setQueryString(String query){
        if( query.equals("") )  query   = null;
        this.query  = query;
    }
    
    /**
     * Returns the query string that is contained in the request URL after the path. This method returns null if the URL does not have a query string. 
     * 
     * @return a String containing the query string or null if the URL contains no query string. 
     */
    public String getQueryString() {
        return this.query;
    }
    
    /**
     * Sets the remote user
     * 
     * @param user The remote user
     */
    public void setRemoteUser(nl.b3p.kaartenbalie.core.server.User user){
        this.user   = user;
    }

    /**
     * Returns the login of the user making this request, if the user has been authenticated, or null if the user has not been authenticated.
     * 
     * @return a String specifying the login of the user making this request, or null if the user login is not known
     */
    public String getRemoteUser() {
        if( this.user == null || this.user.getUsername() == null ){
            return null;
        }
        
        return this.user.getUsername();
    }

    /**
     * Returns a boolean indicating whether the authenticated user is included in the specified logical "role". If the user has not been authenticated, the method returns false. 
     * 
     * @param   role  a String specifying the name of the role
     * @return  a boolean indicating whether the user making this request belongs to a given role. False if the user has not been authenticated
     */
    public boolean isUserInRole(String role) {
        if( this.user == null )     return false;
        
        return this.user.checkRole(role);
    }

    /**
     * Returns a java.security.Principal object containing the name of the current authenticated user. If the user has not been authenticated, the method returns null. 
     * 
     * @return a java.security.Principal containing the name of the user making this request. Null if the user has not been authenticated
     */
    public Principal getUserPrincipal() {
        if( this.user == null || this.user.getUsername() == null )  return null;
        
        PrincipalStub principal = new PrincipalStub();
        principal.setName(this.user.getUsername());
        
        return principal;
    }

    /**
     * Returns the session ID specified by the client. This may not be the same as the ID of the current valid session for this request. If the client did not specify a session ID, this method returns null. 
     * 
     * @return a String specifying the session ID, or null if the request did not specify a session ID
     */
    public String getRequestedSessionId() {
        if( this.currentSession == null )  return null;
        
        return this.currentSession.getId();
    }

    /**
     * Returns the part of this request's URL from the protocol name up to the query string in the first line of the HTTP request.
     * 
     * @return  a String containing the part of the URL from the protocol name up to the query string
     */
    public String getRequestURI() {
        String uri  = "";
        if( this.context != null )  uri += this.context + "/";
        
        return uri+this.filename;
    }

    /**
     * Reconstructs the URL the client used to make the request. The returned URL contains a protocol, server name, port number, and server path, but it does not include query string parameters. 
     * 
     * @return a StringBuffer object containing the reconstructed URL
     */
    public StringBuffer getRequestURL() {
        StringBuffer uri = new StringBuffer(this.protocol+this.server);
        if( this.port != 80 )       uri.append(":").append(this.port);
        if( this.context != null )  uri.append("/").append(this.context);
        
        return uri.append("/").append(this.filename);
    }

    /**
     * Returns the part of this request's URL that calls the servlet.
     * 
     * @return a String containing the name or path of the servlet being called, as specified in the request URL
     */
    public String getServletPath() {
        StringBuilder uri = new StringBuilder(this.protocol+this.server);
        if( this.port != 80 )       uri.append(":").append(this.port);
        if( this.context != null )  uri.append("/").append(this.context);
        
        return uri.append("/").append(this.filename).toString();
    }

    /**
     * Sets the Http Session
     * 
     * @param session The Http Session
     */
    public void setSession(HttpSessionStub session){
        this.currentSession = session;
    }
    
    /**
     * Returns the current HttpSession associated with this request or, if there is no current session and create is true, returns a new session. 
     * 
     * @param create    true to create a new session for this request if necessary. False to return null if there's no current session 
     * @return the HttpSession associated with this request or null if create is false and the request has no valid session
     */
    public HttpSession getSession(boolean create) {
        if( this.currentSession != null )   return this.currentSession;
        
        if( create ){
            this.currentSession = new HttpSessionStub();
            this.currentSession.setAttribute("username", this.user.getUsername());
            
            return this.currentSession;
        }
        return null;
    }

    /**
     * Returns the current session associated with this request, or if the request does not have a session, creates one. 
     * 
     * @return the HttpSession associated with this request
     */
    public HttpSession getSession() {
        return this.getSession(true);
    }

    /**
     * Sets the requested session ID
     * 
     * @param sessionID     The requested session ID
     */
    public void setSessionID(String sessionID){
        this.requestedSessionID = sessionID;
    }
    
    /**
     * Checks whether the requested session ID is still valid. 
     * 
     * @return true if this request has an id for a valid session in the current session context; false otherwise
     */
    public boolean isRequestedSessionIdValid() {
        if( this.currentSession != null && this.currentSession.getId().equals(this.requestedSessionID) )
            return true;
                   
        return false;
    }

    
    public boolean isRequestedSessionIdFromCookie() {
        for(int i=0; i<this.cookies.length; i++){
            if( this.cookies[i].getName().equals("sessionID") ) return true;
        }        
        
        return false;
    }

    /**
     * Checks whether the requested session ID came in as part of the request URL. 
     * 
     * @return     true if the session ID came in as part of a URL; otherwise, false
     */
    public boolean isRequestedSessionIdFromURL() {
        for(int i=0; i<this.cookies.length; i++){
            if( this.cookies[i].getName().equals("sessionID") ) return false;
        }        
        
        return true;
    }

    /**
     * Checks whether the requested session ID came in as part of the request URL. 
     * 
     * @deprecated As of Version 2.1 of the Java Servlet API, use isRequestedSessionIdFromURL() instead.
     * @return      true if the session ID came in as part of a URL; otherwise, false
     */
    public boolean isRequestedSessionIdFromUrl() {
        System.err.println("Use of deprecated function isRequestedSessionIdFromUrl. Use isRequestedSessionIdFromURL instead");
        
        return this.isRequestedSessionIdFromURL();
    }

    /**
     * Returns the value of the named attribute as an Object, or null if no attribute of the given name exists. 
     * 
     * @param name  a String specifying the name of the attribute 
     * @return      an Object containing the value of the attribute, or null if the attribute does not exist
     */
    public Object getAttribute(String name){
        if( !this.attributes.containsKey(name) )    return null;
        return this.attributes.get(name);
    }

    /**
     * Returns an Enumeration containing the names of the attributes available to this request. This method returns an empty Enumeration if the request has no attributes available to it. 
     * 
     * @return     an Enumeration of strings containing the names of the request's attributes
     */
    public Enumeration getAttributeNames() {
        ServerDetailEnumeration names   = new ServerDetailEnumeration();
        Object[] keys   = this.attributes.keySet().toArray();
        
        for(int i=0; i<keys.length; i++){
            names.addName((String) keys[i]);
        }
        
        return names;
    }

    /**
     * Returns the name of the character encoding used in the body of this request. This method returns null if the request does not specify a character encoding 
     * 
     * @return  a String containing the name of the character encoding, or null if the request does not specify a character encoding
     */
    public String getCharacterEncoding() {
        return this.characterEncoding;
    }
    
    /**
     * Overrides the name of the character encoding used in the body of this request. This method must be called prior to reading request parameters or reading input using getReader(). Otherwise, it has no effect. 
     * 
     * @param env   String containing the name of the character encoding. 
     * @throws UnsupportedEncodingException     if this ServletRequest is still in a state where a character encoding may be set, but the specified encoding is invalid
     */
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        if( env.equals("") || !env.matches("^[a-zA-Z0-9]") || !env.matches("^[a-zA-Z0-9-:_]+$") )
            throw new UnsupportedEncodingException("Encoding "+env+" is not valid");
        
        this.characterEncoding  = env;
    }

    /**
     * Returns the length, in bytes, of the request body and made available by the input stream, or -1 if the length is not known.
     * 
     * @return  an integer containing the length of the request body or -1 if the length is not known
     */
    public int getContentLength() {
        if( this.inputStream == null )  return -1;
        
        return this.inputStream.getLength();
    }

    /**
     * Returns the MIME type of the body of the request, or null if the type is not known. 
     * 
     * @return  a String containing the name of the MIME type of the request, or null if the type is not known
     */
    public String getContentType() {
        if( this.inputStream == null )  return null;
        
        return this.inputStream.getContentType();
    }

    /**
     * Retrieves the body of the request as binary data using a ServletInputStream. Either this method or getReader() may be called to read the body, not both. 
     * 
     * @return  a ServletInputStream object containing the body of the request 
     * @throws  IllegalStateException   if the getReader() method has already been called for this request 
     * @throws  IOException             if an input or output exception occurred
     */
    public ServletInputStream getInputStream() throws IOException {
        return this.inputStream;
    }

    public String getParameter(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Enumeration getParameterNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String[] getParameterValues(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Map getParameterMap() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getProtocol() {
        return this.protocol;
    }

    public String getScheme() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getServerName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getServerPort() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public BufferedReader getReader() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getRemoteAddr() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getRemoteHost() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setAttribute(String string, Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeAttribute(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Locale getLocale() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Enumeration getLocales() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isSecure() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public RequestDispatcher getRequestDispatcher(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getRealPath(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getRemotePort() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getLocalName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getLocalAddr() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getLocalPort() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
