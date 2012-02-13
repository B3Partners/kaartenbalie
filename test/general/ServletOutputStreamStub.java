package general;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.ServletOutputStream;

/**
 *
 * @author rachelle
 */
class ServletOutputStreamStub extends ServletOutputStream {
    private HashMap<Integer,String> errors;    
    private MultikeyHashMap headers;
    private String redict;
    private boolean hasWritten;
    private boolean hasLoaded;
    private String contentType;
    private String characterEncoding;
    private int contentLength;
    private int bufferSize;
    private StringBuffer output;
    private int status;
    
    public ServletOutputStreamStub() {
        this.hasLoaded      = false;
        this.contentType    = null;
        this.contentType    = null;
        this.contentLength  = 0;
        this.bufferSize     = 1024;
        
        this.reset();
    }
    
    /**
     * Resets the stream. Clears also the errors, headers and status code
     */
    public void reset() {
        this.errors         = new HashMap<Integer,String>();
        this.headers        = new MultikeyHashMap();
        this.hasWritten     = false;
        this.redict         = null;
        this.status         = 200;
        
        this.resetBuffer();
    }
    
    /**
     * Clears the content of the underlying buffer in the response without clearing headers or status code. 
     */
    public void resetBuffer(){
        this.output         = new StringBuffer();
    }

    /**
     * Writes a int
     * 
     * @param i         The int to write
     * @throws IOException 
     */
    @Override
    public void write(int i) throws IOException {
        
    }
    
    /**
     * Returns if the stream is already loaded in a writer
     * 
     * @return True if the stream is already loaded, otherwise false
     */
    public boolean hasLoaded(){
        return this.hasLoaded;
    }
    
    /**
     * Returns a ServletOutputStream suitable for writing binary data in the response. 
     * 
     * @return  a ServletOutputStream for writing binary data 
     */
    public ServletOutputStream getStream(){
        this.hasLoaded  = true;
        
        return this;
    }

    /**
     * Returns if output is written
     * 
     * @return True if output is written, otherwise false
     */
    public boolean hasWritten() {
        return this.hasWritten;
    }

    /**
     * Writes the error message
     * 
     * @param sc        The error code
     * @param msg       The error message
     * @throws IOException 
     */
    public void writeError(int sc, String msg) throws IOException{
        this.errors.put(sc, msg);
    }

    /**
     * Writes the redirect header
     * 
     * @param location          The new location URI
     * @throws IOException 
     */
    public void writeRedirect(String location) throws IOException {
        this.redict = location;
    }

    /**
     * Writes the http-status
     * 
     * @param sc    The http-status
     */
    public void writeStatus(int sc) {
        this.status = sc;
    }

    /**
     * Sets the content type of the response being sent to the client, if the response has not been committed yet. The given content type may include a character encoding specification, for example, text/html;charset=UTF-8. 
     * 
     * This method may be called repeatedly to change content type and character encoding. This method has no effect if called after the response has been committed. It does not set the response's character encoding if it is called after getWriter has been called or after the response has been committed.
     * 
     * @param type a String specifying the MIME type of the content
     */
    public void setContentType(String type) {
        this.contentType    = type;
    }

    /**
     * Returns the content type used for the MIME body sent in this response. The content type proper must have been specified using setContentType(java.lang.String) before the response is committed. If no content type has been specified, this method returns null. If a content type has been specified, and a character encoding has been explicitly or implicitly specified as described in getCharacterEncoding() has been called, the charset parameter is included in the string returned. If no character encoding has been specified, the charset parameter is omitted. 
     * 
     * @return a String specifying the content type, for example, text/html; charset=UTF-8, or null
     */
    public String getContentType() {
        if( this.contentType == null )  return null;
        
        if( this.characterEncoding != null )    return this.contentType+"; charset="+this.characterEncoding;
        
        return this.contentType;
    }

    /**
     * Returns the name of the character encoding (MIME charset) used for the body sent in this response. The character encoding may have been specified explicitly using the setCharacterEncoding(java.lang.String) or setContentType(java.lang.String) methods, or implicitly using the setLocale(java.util.Locale) method. Explicit specifications take precedence over implicit specifications. Calls made to these methods after getWriter has been called or after the response has been committed have no effect on the character encoding. If no character encoding has been specified, ISO-8859-1 is returned. 
     * 
     * @return  a String specifying the name of the character encoding, for example, UTF-8
     */
    public String getCharacterEncoding() {
        if( this.characterEncoding == null )     return "ISO-8859-1";
        
        return this.characterEncoding;
    }

    /**
     * Overrides the name of the character encoding used in the body of this request. This method must be called prior to reading request parameters or reading input using getStream(). Otherwise, it has no effect. 
     * 
     * @param env   String containing the name of the character encoding. 
     */
    void setCharacterEncoding(String env) {
        if( env.equals("") || !env.matches("^[a-zA-Z0-9]") || !env.matches("^[a-zA-Z0-9-:_]+$") )
            return;
        
        this.characterEncoding  = env;
    }

    /**
     * Sets the length of the content body in the response In HTTP servlets, this method sets the HTTP Content-Length header. 
     * 
     * @param len   an integer specifying the length of the content being returned to the client. Sets the Content-Length header
     */
    public void setContentlength(int len) {
        this.contentLength  = len;
    }

    /**
     * Sets the preferred buffer size for the body of the response.  
     * 
     * @param size the preferred buffer size 
     */
    public void setBufferSize(int size) {
        this.bufferSize = size;
    }

    /**
     * Returns the actual buffer size used for the response. If no buffering is used, this method returns 0. 
     * 
     * @return the actual buffer size used
     */
    public int getBufferSize() {
        return this.bufferSize;
    }

    /**
     * Generates the header-output
     */
    public void writeBuffer() {
        this.hasWritten = true;
        
        if( !this.errors.isEmpty() ){
            Object[] keys   = this.errors.keySet().toArray();
            
            Integer key;
            for(int i=0; i<keys.length; i++){
                key = (Integer) keys[i];
                
                this.output.append(key).append("\n");
                this.output.append(this.errors.get(key)).append("\n");
            }
            
        }
        else if( this.redict != null ){
            this.output.append("302 \n");
            this.output.append("Content-Length: ").append(this.redict.length()).append("\n");
            this.output.append(this.redict);
        }
        else {            
            this.output.append(this.status).append("\n");
            this.output.append(this.getContentType()).append("\n");
            this.output.append("Content-Length: ").append(this.contentLength).append("\n");
            
            Enumeration keys    = this.headers.getKeys();
            String key;
            int pos = 0;
            while(keys.hasMoreElements() ){
                key = (String) keys.nextElement();
                
                pos = this.headers.findKey(key, pos);
                while( pos != -1 ){
                    this.output.append(key).append(": ").append(this.headers.get(key,pos)).append("\n");
                    
                    pos = this.headers.findKey(key, pos+1);
                }
            }
            
            this.output.append("\n");
        }
    }
    
    /**
     * Returns the header output. Call writeBuffer() before
     * 
     * @return  The header output
     */
    public String getHeader(){
        return this.output.toString();
    }

    /**
     * Checks if the header is already defined
     * 
     * @param name  The name of the header
     * @return      True if the header is already defined, otherwise false
     */
    public boolean containsHeader(String name) {
        return this.headers.findKey(name, 0) != -1;
    }

    /**
     * Adds a HTTP header
     * 
     * @param name          The name of the header
     * @param value         The value of the header
     * @param multiply      Set to true if the name is not unique
     */
    public void addHeader(String name, String value, boolean multiply) {
        if( !multiply ){
            this.headers.addUnique(name, value);
        }        
        else {
            this.headers.add(name, value);
        }
    }
}
