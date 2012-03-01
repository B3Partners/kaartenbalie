package general;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author rachelle
 */
public class HttpServletResponseStub implements HttpServletResponse{
    private Cookie[] cookies;
    private int cookiePointer;
    private String sessionID;
    private ServletOutputStreamStub writer;
    
    private boolean needsEncoding;
    private String protocol;
    private String server;
    private String context;
    private Locale locale;
    
    public HttpServletResponseStub(){
        this.cookies            = new Cookie[10];
        this.cookiePointer      = 0;
        this.needsEncoding      = false;
        this.writer             = new ServletOutputStreamStub();
        
        this.protocol           = "http://";
        this.server             = "localhost";  
        this.context            = "";
        this.locale             = new Locale("nl");
        
        this.writer             = new ServletOutputStreamStub();
        this.writer.setContentType("text/html");
    }
    
    /**
     * Sets the server address
     * 
     * @param server    The server hostname
     */
    public void setServer(String server){
        if( server != null )    this.server     = server;
    }
    
    /**
     * Sets the protocol
     * 
     * @param protocol    The protocol (http://|https://|file:)
     */
    public void setProtocol(String protocol){
        if( protocol.equals("http://") || protocol.equals("https://") || protocol.equals("file:") )
            this.protocol = protocol;
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
     * Adds the specified cookie to the response. This method can be called multiple times to set more than one cookie.
     * 
     * @param cookie the Cookie to return to the client
     */
    public void addCookie(Cookie cookie) {
        this.cookies[this.cookiePointer]    = cookie;
        this.cookiePointer++;
    }

    /**
     * Returns a boolean indicating whether the named response header has already been set. 
     * 
     * @param name      the header name 
     * @return true if the named response header has already been set, false otherwise
     */
    public boolean containsHeader(String name) {
        return this.writer.containsHeader(name);
    }
    
    /**
     * Adds a response header with the given name and value. This method allows response headers to have multiple values. 
     * 
     * @param name      the name of the header
     * @param value     the additional header value If it contains octet string, it should be encoded according to RFC 2047 (http://www.ietf.org/rfc/rfc2047.txt)
     */
    public void addHeader(String name, String value) {
        this.writer.addHeader(name,value,true);
    }
    
    /**
     * Sets a response header with the given name and value. If the header had already been set, the new value overwrites the previous one. The containsHeader method can be used to test for the presence of a header before setting its value.
     * 
     * @param name      the name of the header
     * @param value     the header value If it contains octet string, it should be encoded according to RFC 2047 (http://www.ietf.org/rfc/rfc2047.txt)
     */
    public void setHeader(String name, String value) {        
        this.writer.addHeader(name, value, false);
    } 
    
    /**
     * Sets a response header with the given name and date-value. The date is specified in terms of milliseconds since the epoch. If the header had already been set, the new value overwrites the previous one. The containsHeader method can be used to test for the presence of a header before setting its value. 
     * 
     * @param name      the name of the header to set
     * @param date      the additional date value
     */
    public void setDateHeader(String name, long date) {
        this.writer.addHeader(name,String.valueOf(date),false);
    }

    /**
     * Adds a response header with the given name and date-value. The date is specified in terms of milliseconds since the epoch. This method allows response headers to have multiple values. 
     * 
     * @param name      the name of the header to set
     * @param date      the additional date value
     */
    public void addDateHeader(String name, long date) {
        this.writer.addHeader(name, String.valueOf(date),true);
    }   
    
    /**
     * Gets the date-value from the response header with the given name. The date is specified in terms of milliseconds since the epoch. If the header had already been set, the new value overwrites the previous one. 
     * 
     * @param name      the name of the header to set
     * @return the additional date value or null if the header does not exist
     */
    public long getDateHeader(String name){
        return new Long(this.writer.getHeader(name,0));
    }
    
    /**
     * Gets the value from the response header with the given name. 
     * 
     * @param name      the name of the header
     * @return      the header value If it contains octet string, it should be encoded according to RFC 2047 (http://www.ietf.org/rfc/rfc2047.txt) or null if the header does not exists.
     */
    public String getheader(String name){
        return this.writer.getHeader(name, 0);
    }

    /**
     * Sets a response header with the given name and integer value. If the header had already been set, the new value overwrites the previous one. The containsHeader method can be used to test for the presence of a header before setting its value. 
     * 
     * @param name      the name of the header
     * @param value     the assigned integer value
     */
    public void setIntHeader(String name, int value) {
        this.writer.addHeader(name, String.valueOf(value),false);
    }

    /**
     * Adds a response header with the given name and integer value. This method allows response headers to have multiple values. 
     * 
     * @param name      the name of the header
     * @param value     the assigned integer value
     */
    public void addIntHeader(String name, int value) {
        this.writer.addHeader(name, String.valueOf(value),true);
    }
    
    /**
     * Gets the response header with the given name.
     * 
     * @param name      the name of the header
     * @return          the assigned integer value or -1 if the header does not exist.
     */
    public int getIntHeader(String name){
        if( !this.writer.containsHeader(name) ) return -1;
        
        return Integer.parseInt(this.writer.getHeader(name, 0));
    }

    /**
     * Encodes the specified URL by including the session ID in it, or, if encoding is not needed, returns the URL unchanged. The implementation of this method includes the logic to determine whether the session ID needs to be encoded in the URL. For example, if the browser supports cookies, or session tracking is turned off, URL encoding is unnecessary. 
     * 
     * @param url       the url to be encoded.
     * @return          the encoded URL if encoding is needed; the unchanged URL otherwise.
     */
    public String encodeURL(String url) {
        if( this.needsEncoding ){
            if( url.contains("?") ){
                url += "&sessionID="+this.sessionID;
            }
            else {
                url += "?sessionID="+this.sessionID;
            }
        }
        return url;
    }

    /**
     * Encodes the specified URL for use in the sendRedirect method or, if encoding is not needed, returns the URL unchanged. The implementation of this method includes the logic to determine whether the session ID needs to be encoded in the URL. Because the rules for making this determination can differ from those used to decide whether to encode a normal link, this method is separated from the encodeURL method. 
     * 
     * @param url
     * @return 
     */
    public String encodeRedirectURL(String url) {
        if( this.needsEncoding ){
            if( url.contains("?") ){
                url += "&sessionID="+this.sessionID;
            }
            else {
                url += "?sessionID="+this.sessionID;
            }
        }
        return url;
    }

    /**
     * Deprecated. As of version 2.1, use encodeURL(String url) instead
     * 
     * @param url   the url to be encoded. 
     * @return the encoded URL if encoding is needed, the unchanged URL otherwise.
     */
    public String encodeUrl(String url) {
        System.err.println("Use of deprecated function encodeUrl. Use encodeURL instead");
        
        return this.encodeURL(url);
    }

    /**
     * Deprecated. As of version 2.1, use encodeRedirectURL(String url) instead
     * 
     * @param url   the url to be encoded.
     * @return the encoded URL if encoding is needed, the unchanged URL otherwise.
     */
    public String encodeRedirectUrl(String url) {
        System.err.println("Use of deprecated function encodeRedirectUrl. Use encodeRedirectURL instead");
        
        return this.encodeRedirectURL(url);
    }
    
    /**
     * Sends an error response to the client using the specified status. The server defaults to creating the response to look like an HTML-formatted server error page containing the specified message, setting the content type to "text/html", leaving cookies and other headers unmodified. If an error-page declaration has been made for the web application corresponding to the status code passed in, it will be served back in preference to the suggested msg parameter.
     * 
     * If the response has already been committed, this method throws an IllegalStateException. After using this method, the response should be considered to be committed and should not be written to. 
     * 
     * @param sc    the error status code
     * @param msg   the descriptive message 
     * @throws IOException              If an input or output exception occurs 
     * @throws IllegalStateException    If the response was committed
     */
    public void sendError(int sc, String msg) throws IOException, IllegalStateException {
        if( this.writer.hasWritten() ){
            throw new IllegalStateException("Error sending error code. Output allready send");
        }
        
        this.writer.writeError(sc,msg);
    }

    /**
     * Sends an error response to the client using the specified status code and clearing the buffer.
     * 
     * If the response has already been committed, this method throws an IllegalStateException. After using this method, the response should be considered to be committed and should not be written to. 
     * 
     * @param sc            the error status code
     * @throws IOException              If an input or output exception occurs 
     * @throws IllegalStateException    If the response was committed
     */
    public void sendError(int sc) throws IOException,IllegalStateException {
        if( this.writer.hasWritten() ){
            throw new IllegalStateException("Error sending error code. Output allready send");
        }
        
        this.writer.writeError(sc,"");
    }

    /**
     * Sends a temporary redirect response to the client using the specified redirect location URL. This method can accept relative URLs; the servlet container must convert the relative URL to an absolute URL before sending the response to the client. If the location is relative without a leading '/' the container interprets it as relative to the current request URI. If the location is relative with a leading '/' the container interprets it as relative to the servlet container root.
     * 
     * If the response has already been committed, this method throws an IllegalStateException. After using this method, the response should be considered to be committed and should not be written to. 
     * 
     * @param   location      the redirect location URL 
     * @throws  IllegalStateException   If the response was committed or if a partial URL is given and cannot be converted into a valid URL
     * @throws  IOException             If an input or output exception occurs 
     */
    public void sendRedirect(String location) throws IOException, IllegalStateException {
        if( this.writer.hasWritten() )  throw new IllegalStateException("Error sending redict. Output allready send");
        
        if( !location.startsWith("http") && !location.startsWith("ftp") && !location.startsWith("file") ){
            /* Relative url */
            if( location.startsWith("/") ){
                location    = this.protocol+this.server+location;
            }
            else {
                location    = this.protocol+this.server+this.context+location;
            }
        }
       
        this.writer.writeRedirect(location);
    }

    /**
     * Sets the status code for this response. This method is used to set the return status code when there is no error (for example, for the status codes SC_OK or SC_MOVED_TEMPORARILY). If there is an error, and the caller wishes to invoke an error-page defined in the web application, the sendError method should be used instead.
     * 
     * The container clears the buffer and sets the Location header, preserving cookies and other headers. 
     * 
     * @param sc        the status code
     */
    public void setStatus(int sc) {
        this.writer.writeStatus(sc);
    }

    /**
     * Sets the status code and message for this response.
     * 
     * @deprecated As of version 2.1, due to ambiguous meaning of the message parameter. To set a status code use setStatus(int), to send an error with a description use sendError(int, String). 
     * 
     * @param sc
     * @param sm
     */
    public void setStatus(int sc, String sm) {
        System.err.println("Use of deprecated function setStatus(sc,sm). Use setStatus(sc) instead");
        
        if( sc == HttpServletResponse.SC_OK || sc == HttpServletResponse.SC_CONTINUE || sc == HttpServletResponse.SC_CREATED ||
                sc == HttpServletResponse.SC_MOVED_PERMANENTLY || sc == HttpServletResponse.SC_MOVED_TEMPORARILY ||
                sc == HttpServletResponse.SC_NOT_MODIFIED || sc == HttpServletResponse.SC_SEE_OTHER ||
                sc == HttpServletResponse.SC_SWITCHING_PROTOCOLS || sc == HttpServletResponse.SC_TEMPORARY_REDIRECT ||
                sc == HttpServletResponse.SC_FOUND){
            this.setStatus(sc);
        }
        else {
            try {
                this.sendError(sc, sm);
            }
            catch(Exception e){ }
        }
    }
    
    /**
     * Gets the status code for this response.
     * 
     * @return       the status code
     */
    public int getStatus(){
        return this.writer.getStatus();
    }
    
    /**
     * Overrides the name of the character encoding used in the body of this request. This method must be called prior to reading request parameters or reading input using getReader(). Otherwise, it has no effect. 
     * 
     * @param env   String containing the name of the character encoding. 
     */
    public void setCharacterEncoding(String env){
        this.writer.setCharacterEncoding(env);        
    }

    /**
     * Returns the name of the character encoding (MIME charset) used for the body sent in this response. The character encoding may have been specified explicitly using the setCharacterEncoding(java.lang.String) or setContentType(java.lang.String) methods, or implicitly using the setLocale(java.util.Locale) method. Explicit specifications take precedence over implicit specifications. Calls made to these methods after getWriter has been called or after the response has been committed have no effect on the character encoding. If no character encoding has been specified, ISO-8859-1 is returned. 
     * 
     * @return  a String specifying the name of the character encoding, for example, UTF-8
     */
    public String getCharacterEncoding() {
        return this.writer.getCharacterEncoding();        
    }
    
    /**
     * Sets the content type of the response being sent to the client, if the response has not been committed yet. The given content type may include a character encoding specification, for example, text/html;charset=UTF-8. The response's character encoding is only set from the given content type if this method is called before getWriter is called.
     * 
     * This method may be called repeatedly to change content type and character encoding. This method has no effect if called after the response has been committed. It does not set the response's character encoding if it is called after getWriter has been called or after the response has been committed.
     * 
     * Containers must communicate the content type and the character encoding used for the servlet response's writer to the client if the protocol provides a way for doing so. In the case of HTTP, the Content-Type header is used. 
     * 
     * @param type a String specifying the MIME type of the content
     */
    public void setContentType(String type) {
        this.writer.setContentType(type);        
    }

    /**
     * Returns the content type used for the MIME body sent in this response. The content type proper must have been specified using setContentType(java.lang.String) before the response is committed. If no content type has been specified, this method returns null. If a content type has been specified, and a character encoding has been explicitly or implicitly specified as described in getCharacterEncoding() or getWriter() has been called, the charset parameter is included in the string returned. If no character encoding has been specified, the charset parameter is omitted. 
     * 
     * @return a String specifying the content type, for example, text/html; charset=UTF-8, or null
     */
    public String getContentType() {
        return this.writer.getContentType();
    }

    /**
     * Returns a ServletOutputStream suitable for writing binary data in the response. The servlet container does not encode the binary data.
     * 
     * This method is for unit-testing only. For the rest use getOutputStream()
     * 
     * Calling flush() on the ServletOutputStream commits the response. Either this method or getWriter() may be called to write the body, not both. 
     * 
     * @return  a ServletOutputStream for writing binary data 
     * @throws IOException           if the getWriter method has been called on this response 
     * @throws IllegalStateException if an input or output exception occurred
     */
    public ServletOutputStream getOutputStream(boolean test) throws IOException, IllegalStateException {
        if( this.writer == null )               throw new IOException("Error loading stream");
        if( this.writer.hasLoaded() && !test )  throw new IllegalStateException("Stream allready called");
        
        return this.writer.getStream();
    }
    
    /**
     * Returns a ServletOutputStream suitable for writing binary data in the response. The servlet container does not encode the binary data.
     * 
     * Calling flush() on the ServletOutputStream commits the response. Either this method or getWriter() may be called to write the body, not both. 
     * 
     * @return  a ServletOutputStream for writing binary data 
     * @throws IOException           if the getWriter method has been called on this response 
     * @throws IllegalStateException if an input or output exception occurred
     */
    public ServletOutputStream getOutputStream() throws IOException, IllegalStateException {
        return this.getOutputStream(false);
    }

    /**
     * Returns a PrintWriter object that can send character text to the client. The PrintWriter uses the character encoding returned by getCharacterEncoding(). If the response's character encoding has not been specified as described in getCharacterEncoding (i.e., the method just returns the default value ISO-8859-1), getWriter updates it to ISO-8859-1.
     * 
     * Calling flush() on the PrintWriter commits the response.
     * 
     * Either this method or getOutputStream() may be called to write the body, not both. 
     * 
     * @return  a PrintWriter object that can return character data to the client 
     * @throws  UnsupportedEncodingException    if the character encoding returned by getCharacterEncoding cannot be used 
     * @throws IOException                      if the getOutputStream method has already been called for this response object 
     * @throws IllegalStateException            if an input or output exception occurred
     */
    public PrintWriter getWriter() throws UnsupportedEncodingException, IOException,IllegalStateException {
        if( this.writer == null )       throw new IOException("Error loading stream");
        if( this.writer.hasLoaded() )  throw new IllegalStateException("Stream allready called");
        
        if( this.writer.getCharacterEncoding() == null )    this.writer.setCharacterEncoding("ISO-8859-1");
        
        return new PrintWriter(this.writer.getStream());
    }

    /**
     * Sets the length of the content body in the response In HTTP servlets, this method sets the HTTP Content-Length header. 
     * 
     * @param len   an integer specifying the length of the content being returned to the client. Sets the Content-Length header
     */
    public void setContentLength(int len) {
        this.writer.setContentlength(len);
    }

    /**
     * Sets the preferred buffer size for the body of the response. The servlet container will use a buffer at least as large as the size requested. The actual buffer size used can be found using getBufferSize.
     * 
     * A larger buffer allows more content to be written before anything is actually sent, thus providing the servlet with more time to set appropriate status codes and headers. A smaller buffer decreases server memory load and allows the client to start receiving data more quickly.
     * 
     * This method must be called before any response body content is written; if content has been written or the response object has been committed, this method throws an IllegalStateException. 
     * 
     * @param size the preferred buffer size 
     * @throws IllegalStateException    
     */
    public void setBufferSize(int size) throws IllegalStateException{
        if( this.writer.hasWritten() )  throw new IllegalStateException("Error setting buffer size. Output allready send");
        
        this.writer.setBufferSize(size);
    }

    /**
     * Returns the actual buffer size used for the response. If no buffering is used, this method returns 0. 
     * 
     * @return the actual buffer size used
     */
    public int getBufferSize() {
        return this.writer.getBufferSize();
    }

    /**
     * Forces any content in the buffer to be written to the client. A call to this method automatically commits the response, meaning the status code and headers will be written. 
     * 
     * @throws IOException 
     */
    public void flushBuffer() throws IOException {
        this.writer.writeBuffer();
    }

    /**
     * Clears the content of the underlying buffer in the response without clearing headers or status code. 
     */
    public void resetBuffer() {
        this.writer.resetBuffer();
    }

    /**
     * Returns a boolean indicating if the response has been committed. A committed response has already had its status code and headers written
     * 
     * @return a boolean indicating if the response has been committed
     */
    public boolean isCommitted() {
        return this.writer.hasWritten();
    }

    /**
     * Clears any data that exists in the buffer as well as the status code and headers. If the response has been committed, this method throws an IllegalStateException. 
     * 
     * @throws IllegalStateException    if the response has already been committed
     */
    public void reset() throws IllegalStateException {
        if( this.writer.hasWritten() )  throw new IllegalStateException("Error resetting buffer. Output allready send");
        
        this.writer.reset();
    }

    /**
     * Sets the locale of the response, if the response has not been committed yet. It also sets the response's character encoding appropriately for the locale, if the character encoding has not been explicitly set using setContentType(java.lang.String) or setCharacterEncoding(java.lang.String), getWriter hasn't been called yet, and the response hasn't been committed yet. If the deployment descriptor contains a locale-encoding-mapping-list element, and that element provides a mapping for the given locale, that mapping is used. Otherwise, the mapping from locale to character encoding is container dependent. 
     * 
     * @param locale the locale of the response
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
        
        if( this.writer.getCharacterEncoding() == null ){
            this.writer.setCharacterEncoding(locale.getLanguage());
        }
    }

    /**
     * Returns the locale specified for this response using the setLocale(java.util.Locale) method. Calls made to setLocale after the response is committed have no effect. If no locale has been specified, the container's default locale is returned. 
     * 
     * @return The set locale of the response
     */
    public Locale getLocale() {
        return this.locale;
    }
    
}
