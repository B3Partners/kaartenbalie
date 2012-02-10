package general;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private MultikeyHashMap headers;
    private String method;
    private String context;
    private final String seperator  = System.getProperty("file.separator");
    private String server;
    private String serverIP;
    private String pathInfo;
    private String query;
    private nl.b3p.kaartenbalie.core.server.User user;
    private String filename;
    private int serverPort;
    private int clientPort;
    private String protocol;
    private HttpSessionStub currentSession;
    private String requestedSessionID;
    private HashMap<String,Object> attributes;
    private MultikeyHashMap parameters;
    private String characterEncoding;
    private ServletInputStreamStub inputStream;
    private String remoteAddress;
    private ArrayList<Locale> clientLocales;
    private Locale serverLocale;
    private ServletContextStub servletContext;
    
    public HttpServletRequestStub(){
        this.cookies        = new Cookie[10];
        this.headers        = new MultikeyHashMap();
        this.attributes     = new HashMap<String,Object>();
        this.parameters     = new MultikeyHashMap();
        
        this.protocol           = "http://";
        this.serverPort         = 80;
        this.clientPort         = 4000;
        this.method             = "GET";
        this.filename           = "";
        this.context            = "";
        this.server             = "localhost";
        this.serverIP           = "127.0.0.1";
        this.pathInfo           = null;
        this.query              = null;
        this.user               = null;
        this.currentSession     = null;
        this.requestedSessionID = "";
        this.characterEncoding  = null;
        this.inputStream        = null;
        this.remoteAddress      = "127.0.0.1";
        this.clientLocales      = new ArrayList<Locale>();
        this.serverLocale       = Locale.ENGLISH;
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
     * Sets the port from the server
     * 
     * @param port The port
     */
    public void setServerPort(int port){
        if( port > 0 ){
            this.serverPort   = port;
        }
    }
    
    /**
     * Sets the port from the client
     * 
     * @param port The port
     */
    public void setClientPort(int port){
        if( port > 0 ){
            this.clientPort   = port;
        }
    }
    
    /**
     * Sets the ip from the client
     * 
     * @param ip    The ip address
     */
    public void setClientAddress(String ip){
        this.remoteAddress  = ip;
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
     * Sets the server locale setting
     * 
     * @param locale The locale setting
     */
    public void setServerLocale(Locale locale){
        this.serverLocale    = locale;
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
     * Sets a client header
     * 
     * @param name      The name of the header
     * @param value     The value of the header
     */
    public void setHeader(String name,String value){
        name    = name.toLowerCase();
        
        this.headers.add(name, value);
        
        if( name.equals("accept-language") ){
            String[] languages  = value.split(",");
            for(int i=0; i<languages.length; i++)
                this.clientLocales.add(new Locale(languages[i].trim()) );
        }
    }

    /**
     * Returns the value of the specified request header as a long value that represents a Date object. Use this method with headers that contain dates, such as If-Modified-Since
     * 
     * @param name      a String specifying the name of the header 
     * @return          a long value representing the date specified in the header expressed as the number of milliseconds since January 1, 1970 GMT, or -1 if the named header was not included with the request 
     * @throws IllegalArgumentException             If the header value can't be converted to a date
     */
    public long getDateHeader(String name) throws IllegalArgumentException {
        String header   = this.headers.get(name.toLowerCase(), 0);
        if( header == null )     return -1;
        
        try {
            SimpleDateFormat format   = new SimpleDateFormat();
            return format.parse(header).getTime();
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
        return this.headers.get(name.toLowerCase(), 0);
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
            pos = this.headers.findKey(name, pos);            
            if( pos == -1 ) break; // no more items
            
            headernames.addName(this.headers.get(name,pos));
        }
        
        return headernames;        
    }

    /**
     * Returns an enumeration of all the header names this request contains. If the request has no headers, this method returns an empty enumeration. 
     * 
     * @return an enumeration of all the header names sent with this request. if the request has no headers, an empty enumeration
     */
    public Enumeration getHeaderNames() {
        return this.headers.getKeys();
    }

    /**
     * Returns the value of the specified request header as an int. If the request does not have a header of the specified name, this method returns -1. If the header cannot be converted to an integer, this method throws a NumberFormatException. 
     * 
     * @param   name  a String specifying the header name. The header name is case insensitive.
     * @return  an integer expressing the value of the request header or -1 if the request doesn't have a header of this name 
     * @throws  NumberFormatException If the header value can't be converted to an int
     */
    public int getIntHeader(String name) throws NumberFormatException{
        String header   = this.headers.get(name.toLowerCase(), 0);
        
        if( header == null ) return -1;
        
        return Integer.parseInt(header);
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
     * @param server    The server hostname
     * @param IP        The server ip address
     */
    public void setServer(String server,String IP){
        if( server != null )    this.server     = server;
        if( IP != null )        this.serverIP   = IP;
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
        if( this.serverPort != 80 ) uri.append(":").append(this.serverPort);
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
        if( this.serverPort != 80 ) uri.append(":").append(this.serverPort);
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
        if( this.inputStream == null )  throw new IOException("Stream loading failed");
        
        if( this.inputStream.isRead() ) throw new IllegalStateException("Stream is allready been read.");
        
        return this.inputStream;
    }
    
    /**
     * Adds a parameter
     * 
     * @param name      a String specifying the name of the parameter
     * @param value     a String representing the single value of the parameter
     */
    public void addParameter(String name,String value){
        this.parameters.add(name, value);
    }

    /**
     * Returns the value of a request parameter as a String, or null if the parameter does not exist. Request parameters are extra information sent with the request. For HTTP servlets, parameters are contained in the query string or posted form data. 
     * 
     * @param name      a String specifying the name of the parameter 
     * @return          a String representing the single value of the parameter
     */
    public String getParameter(String name) {
        return this.parameters.get(name, 0);
    }

    /**
     * Returns an Enumeration of String objects containing the names of the parameters contained in this request. If the request has no parameters, the method returns an empty Enumeration. 
     * 
     * @return an Enumeration of String objects, each String containing the name of a request parameter; or an empty Enumeration if the request has no parameters
     */
    public Enumeration getParameterNames() {
        return this.parameters.getKeys();
    }

    /**
     * Returns an array of String objects containing all of the values the given request parameter has, or null if the parameter does not exist. 
     * 
     * @param   name      a String containing the name of the parameter whose value is requested 
     * @return  an array of String objects containing the parameter's values
     */
    public String[] getParameterValues(String name) {
        int pos = 0;
        ArrayList<String> values    = new ArrayList<String>();
        
        while(true){
            pos = this.parameters.findKey(name, pos);
            if( pos == -1 ) break;
            
            values.add(this.parameters.get(name, pos));
        }
        
        if( values.isEmpty() )    return null;
        
        return (String[]) values.toArray();
    }

    /**
     * Returns a java.util.Map of the parameters of this request. 
     * 
     * @return  an immutable java.util.Map containing parameter names as keys and parameter values as map values. The keys in the parameter map are of type String. The values in the parameter map are of type String array.
     */
    public Map getParameterMap() {
        return this.parameters.toMap();
    }

    /**
     * Returns the name and version of the protocol the request uses in the form protocol/majorVersion.minorVersion, for example, HTTP/1.1.
     * 
     * @return a String containing the protocol name and version number
     */
    public String getProtocol() {
        return this.protocol+"/1.1";
    }

    /**
     * Returns the name of the scheme used to make this request, for example, http, https, or ftp. Different schemes have different rules for constructing URLs, as noted in RFC 1738. 
     * 
     * @return a String containing the name of the scheme used to make this request
     */
    public String getScheme() {
        return this.protocol.replace("://", "").replace(":","");
    }

    /**
     * Returns the host name of the server to which the request was sent. It is the value of the part before ":" in the Host header value, if any, or the resolved server name, or the server IP address.
     * 
     * @return  a String containing the name of the server
     */
    public String getServerName() {
        return this.server;
    }

    /**
     * Returns the port number to which the request was sent. It is the value of the part after ":" in the Host header value, if any, or the server port where the client connection was accepted on.
     * 
     * @return an integer specifying the port number
     */
    public int getServerPort() {
        return this.serverPort;
    }

    /**
     * Retrieves the body of the request as character data using a BufferedReader. The reader translates the character data according to the character encoding used on the body. Either this method or getInputStream() may be called to read the body, not both. 
     * 
     * @return  a BufferedReader containing the body of the request 
     * @throws UnsupportedEncodingException     if the character set encoding used is not supported and the text cannot be decoded 
     * @throws IOException                      if an input or output exception occurred
     * @throws IllegalStateException            if getInputStream() method has been called on this request 
     */
    public BufferedReader getReader() throws UnsupportedEncodingException, IOException, IllegalStateException {
        if( this.inputStream == null )  throw new IOException("Stream loading failed");
        
        if( this.inputStream.isRead() ) throw new IllegalStateException("Stream is allready been read.");
        
        return new BufferedReader(new InputStreamReader(this.inputStream) );
    }

    /**
     * Returns the Internet Protocol (IP) address of the client or last proxy that sent the request. 
     * 
     * @return a String containing the IP address of the client that sent the request
     */
    public String getRemoteAddr() {
        return this.remoteAddress;
    }

    /**
     * Returns the fully qualified name of the client or the last proxy that sent the request. If the engine cannot or chooses not to resolve the hostname (to improve performance), this method returns the dotted-string form of the IP address. 
     * 
     * @return         a String containing the fully qualified name of the client
     */
    public String getRemoteHost() {
        return this.getRemoteAddr();
    }

    /**
     * Stores an attribute in this request. Attributes are reset between requests. This method is most often used in conjunction with RequestDispatcher. 
     * 
     * @param name      a String specifying the name of the attribute
     * @param o         the Object to be stored
     */
    public void setAttribute(String name, Object o) {
        if( o == null ){
            this.removeAttribute(name);
        }
        else {
            this.attributes.put(name, o);
        }
    }

    /**
     * Removes an attribute from this request. This method is not generally needed as attributes only persist as long as the request is being handled. 
     * 
     * @param name  a String specifying the name of the attribute 
     */
    public void removeAttribute(String name) {
        this.attributes.remove(name);
    }

    /**
     * Returns the preferred Locale that the client will accept content in, based on the Accept-Language header. If the client request doesn't provide an Accept-Language header, this method returns the default locale for the server. 
     * 
     * @return the preferred Locale for the client
     */
    public Locale getLocale() {
        if( this.clientLocales.isEmpty() )  return this.serverLocale;
        
        return this.clientLocales.get(0);
    }

    /**
     * Returns an Enumeration of Locale objects indicating, in decreasing order starting with the preferred locale, the locales that are acceptable to the client based on the Accept-Language header. If the client request doesn't provide an Accept-Language header, this method returns an Enumeration containing one Locale, the default locale for the server. 
     * 
     * @return an Enumeration of preferred Locale objects for the client
     */
    public Enumeration getLocales(){
        ServerLocaleEnumeration locales = new ServerLocaleEnumeration();
        
        if( this.clientLocales.size() > 0 ){
            for(int i=0; i<this.clientLocales.size(); i++){
                locales.addLocale(this.clientLocales.get(i));
            }
        }
        else {
            locales.addLocale(this.serverLocale);
        }
        
        return locales;
    }

    /**
     * Returns a boolean indicating whether this request was made using a secure channel, such as HTTPS.
     * 
     * @return a boolean indicating if the request was made using a secure channel
     */
    public boolean isSecure() {
        return this.protocol.contains("https");
    }

    /**
     * Returns a RequestDispatcher object that acts as a wrapper for the resource located at the given path.
     * 
     * @param string    a String specifying the pathname to the resource. If it is relative, it must be relative against the current servlet. 
     * @return  a RequestDispatcher object that acts as a wrapper for the resource at the specified path, or null if the servlet container cannot return a RequestDispatcher
     */
    public RequestDispatcher getRequestDispatcher(String string) {
        return null;
    }

    /**
     * @deprecated As of Version 2.1 of the Java Servlet API, use ServletContext#getRealPath instead.
     */
    public String getRealPath(String path) {
        System.err.println("Use of deprecated function getRealPath. Use ServletContext.getRealPath instead.");
        
        return this.servletContext.getRealPath(path);
    }

    /**
     * Returns the Internet Protocol (IP) source port of the client or last proxy that sent the request.
     * 
     * @return an integer specifying the port number
     */
    public int getRemotePort() {
        return this.clientPort;
    }

    /**
     * Returns the host name of the Internet Protocol (IP) interface on which the request was received.
     * 
     * @return a String containing the host name of the IP on which the request was received.
     */
    public String getLocalName() {
        return this.server;
    }

    /**
     * Returns the Internet Protocol (IP) address of the interface on which the request was received. 
     * 
     * @return  a String containing the IP address on which the request was received.
     */
    public String getLocalAddr() {
        return this.serverIP;
    }

    /**
     * Returns the Internet Protocol (IP) port number of the interface on which the request was received.
     * 
     * @return an integer specifying the port number
     */
    public int getLocalPort() {
        return this.getServerPort();
    }

}
