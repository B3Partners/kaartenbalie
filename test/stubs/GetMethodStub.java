package stubs;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 *
 * @author rachelle
 */
public class GetMethodStub extends GetMethod {
    private URI uri;
    private boolean doAuthenticationStub;
    private boolean followsRedictsStub;
    private String pathStub; 
    private HashMap<String,Header> requestHeadersStub;
    private HashMap<String,Header> responseHeadersStub;
    private HashMap<String,Header> footers;
    private String queryStringStub;
    private int statusCodeStub;
    private long responseContentLength;
    private InputStreamStub responseStream;
    
    /**
     * No-arg constructor. 
     */
    public GetMethodStub(){        
        this.init();
    }
         
    /**
     * Constructor specifying a URI. 
     * 
     * @param uri 
     */
    public GetMethodStub(String uri){
        try {
            this.uri    = new URI(uri,true);
        }
        catch(Exception e){}
        
        this.init();
    }
    
    private void init(){
        //this.
        this.setDoAuthentication(true);
        this.setFollowRedirects(true);
        this.pathStub               = "/";
        this.queryStringStub        = "";
        this.statusCodeStub         = 200;
        this.statusLine             = null;
        this.responseContentLength  = -1;
        
        this.requestHeadersStub    = new HashMap<String,Header>();
        this.responseHeadersStub    = new HashMap<String,Header>();
        this.footers    = new HashMap<String,Header>();
        this.responseStream         = new InputStreamStub();
    }
    
    
    /**
     * Returns the URI of the HTTP method 
     * 
     * @return  The URI
     * @throws  URIException  If the URI cannot be created.
     */
    @Override
    public URI getURI() throws URIException {
        return this.uri;
    }
    
    /**
     * Sets the URI for this method. 
     * 
     * @param uri   URI to be set 
     * @throws URIException     if a URI cannot be set
     */
    @Override
    public void setURI(URI uri) throws URIException {
        this.uri    = uri;
    }
    
    /**
     * Sets whether or not the HTTP method should automatically follow HTTP redirects (status code 302, etc.) 
     * 
     * @param followRedirects   true if the method will automatically follow redirects, false otherwise.
     */
    @Override
    public void setFollowRedirects(boolean followRedirects){
        this.followsRedictsStub = followRedirects;
    }
    
    /**
     * Returns true if the HTTP method should automatically follow HTTP redirects (status code 302, etc.), false otherwise. 
     * 
     * @return  true if the method will automatically follow HTTP redirects, false otherwise.
     */
    @Override
    public boolean getFollowRedirects(){
        return this.followsRedictsStub;
    }
    
    /**
     * Sets whether version 1.1 of the HTTP protocol should be used per default. 
     * 
     * @param http11    true to use HTTP/1.1, false to use 1.0
     * @deprecated      Use HttpMethodParams.setVersion(HttpVersion)
     */
    @Override
    public void setHttp11(boolean http11){
        System.err.println("Use of deprecated function GetMethod::setHttp11().  Use HttpMethodParams.setVersion(HttpVersion) instead.");
        
        super.setHttp11(http11);
    }
    
    /**
     * Returns true if the HTTP method should automatically handle HTTP authentication challenges (status code 401, etc.), false otherwise 
     * 
     * @return  true if authentication challenges will be processed automatically, false otherwise.
     */
    @Override
    public boolean getDoAuthentication(){
        return this.doAuthenticationStub;
    }
    
    /**
     * Sets whether or not the HTTP method should automatically handle HTTP authentication challenges (status code 401, etc.) 
     * 
     * @param doAuthentication  doAuthentication - true to process authentication challenges authomatically, false otherwise.
     */
    @Override
    public void setDoAuthentication(boolean doAuthentication){
        this.doAuthenticationStub   = doAuthentication;
    }
    
    /**
     * 
     * @return 
     * @deprecated 
     */
    @Override
    public boolean isHttp11(){
        System.err.println("Use of deprecated function GetMethod::isHttp11().  Use HttpMethodParams.getVersion() instead.");
        
        return super.isHttp11();
    }
    
    /**
     * Sets the path of the HTTP method. It is responsibility of the caller to ensure that the path is properly encoded (URL safe). 
     * 
     * @param path  the path of the HTTP method. The path is expected to be URL-encoded
     */
    @Override
    public void setPath(String path){
        this.pathStub   = path;
    }
    
    /**
     * Adds the specified request header, NOT overwriting any previous value. Note that header-name matching is case insensitive. 
     * 
     * @param header the header to add to the request
     */
    @Override
    public void addRequestHeader(Header header){
        this.requestHeadersStub.put(this.getHeaderName(header),header);
    }
    
    /**
     * Use this method internally to add footers. 
     * 
     * @param footer    The footer to add.
     */
    @Override
    public void addResponseFooter(Header footer){
        this.footers.put(this.getHeaderName(footer),footer);
    }
    
    /**
     * Gets the path of this HTTP method. Calling this method after the request has been executed will return the actual path, following any redirects automatically handled by this HTTP method. 
     * 
     * @return the path to request or "/" if the path is blank.
     */
    @Override
    public String getPath(){
        return this.pathStub;
    }
    
    /**
     * Sets the query string of this HTTP method. The caller must ensure that the string is properly URL encoded. The query string should not start with the question mark character. 
     * 
     * @param queryString   the query string
     */
    @Override
    public void setQueryString(String queryString){
        this.queryStringStub    = queryString;
    }
    
    /**
     * Sets the query string of this HTTP method. The pairs are encoded as UTF-8 characters. To use a different charset the parameters can be encoded manually using EncodingUtil and set as a single String. 
     * 
     * @param params an array of NameValuePairs to add as query string parameters. The name/value pairs will be automcatically URL encoded
     */
    @Override
    public void setQueryString(NameValuePair[] params){
        for(int i=0; i<params.length; i++){
            if( !this.queryStringStub.equals("") )  this.queryStringStub += "&";
            
            this.queryStringStub    += params[i].getName()+"="+params[i].getValue();
        }
    }
    
    /**
     * Gets the query string of this HTTP method. 
     * 
     * @return  The query string
     */
    @Override
    public String getQueryString(){
        return this.queryStringStub;
    }
    
    /**
     * Set the specified request header, overwriting any previous value. Note that header-name matching is case-insensitive. 
     * 
     * @param headerName    the header's name
     * @param headerValue   the header's value
     */
    @Override
    public void setRequestHeader(String headerName,String headerValue){
        this.setRequestHeader(new Header(headerName,headerValue));
    }
    
    /**
     * Sets the specified request header, overwriting any previous value. Note that header-name matching is case insensitive. 
     * 
     * @param header the header
     */
    @Override
    public void setRequestHeader(Header header){
        String checkName    = this.getHeaderName(header);
        if( this.requestHeadersStub.containsKey(checkName) )    this.requestHeadersStub.remove(checkName);
        
        this.requestHeadersStub.put(checkName, header);
    }
    
    /**
     * Returns the specified request header. Note that header-name matching is case insensitive. null will be returned if either headerName is null or there is no matching header for headerName. 
     * 
     * @param headerName    The name of the header to be returned. 
     * @return  The specified request header.
     */
    @Override
    public Header getRequestHeader(String headerName){
        if( headerName == null )    return null;
        headerName  = headerName.toLowerCase();
        
        if( !this.requestHeadersStub.containsKey(headerName) ) return null;
        
        return this.requestHeadersStub.get(headerName);
    }
    
    /**
     * Returns an array of the requests headers that the HTTP method currently has 
     * 
     * @return an array of my request headers.
     */
    @Override
    public Header[] getRequestHeaders(){
        Object[] keys   = this.requestHeadersStub.keySet().toArray();
        Header[] out    = new Header[this.requestHeadersStub.size()];
        
        for(int i=0; i<keys.length; i++){
            out[i]  = this.requestHeadersStub.get((String) keys[i]);
        }
        
        return out;
    }
    
    /**
     * Returns the request headers with the given name. Note that header-name matching is case insensitive. 
     * 
     * @param headerName    the name of the headers to be returned. 
     * @return an array of zero or more headers
     */
    @Override
    public Header[] getRequestHeaders(String headerName){
        Object[] keys   = this.requestHeadersStub.keySet().toArray();
        ArrayList<Header> out    = new ArrayList<Header>();
        
        String key;
        for(int i=0; i<keys.length; i++){
            key = (String) keys[i];
            if( !key.equalsIgnoreCase(headerName) )   continue;
            
            
            out.add(this.requestHeadersStub.get(key));
        }
        
        return (Header[]) out.toArray();
    }
    
    /**
     * Returns the response headers with the given name. Note that header-name matching is case insensitive. 
     * 
     * @param headerName    the name of the headers to be returned. 
     * @return  an array of zero or more headers
     */
    @Override
    public Header[] getResponseHeaders(String headerName){
        Object[] keys   = this.responseHeadersStub.keySet().toArray();
        ArrayList<Header> out    = new ArrayList<Header>();
        
        String key;
        for(int i=0; i<keys.length; i++){
            key = (String) keys[i];
            if( !key.equalsIgnoreCase(headerName) )   continue;
            
            
            out.add(this.responseHeadersStub.get(key));
        }
        
        return (Header[]) out.toArray();
    }
    
    /**
     * Returns the response status code. 
     * 
     * @return  the status code associated with the latest response.
     */
    @Override
    public int getStatusCode(){
        return this.statusCodeStub;
    }
    
    /**
     * Returns an array of the response headers that the HTTP method currently has in the order in which they were read. 
     * 
     * @return  an array of response headers.
     */
    @Override
    public Header[] getResponseHeaders(){
        Object[] keys   = this.responseHeadersStub.keySet().toArray();
        Header[] out    = new Header[this.responseHeadersStub.size()];
        
        for(int i=0; i<keys.length; i++){
            out[i]  = this.responseHeadersStub.get((String) keys[i]);
        }
        
        return out;
    }
    
    /**
     * Gets the response header associated with the given name. Header name matching is case insensitive. null will be returned if either headerName is null or there is no matching header for headerName. 
     * 
     * @param   headerName    the header name to match 
     * @return  the matching header
     */
    @Override
    public Header getResponseHeader(String headerName){
        Object[] keys   = this.responseHeadersStub.keySet().toArray();
        
        String key;
        for(int i=0; i<keys.length; i++){
            key = (String) keys[i];
            if( !key.equalsIgnoreCase(headerName) )   continue;
            
            return this.responseHeadersStub.get(key);
        }
        
        return null;
    }
    
    /**
     * Return the length (in bytes) of the response body, as specified in a Content-Length header.
     * Return -1 when the content-length is unknown. 
     * 
     * @return  content length, if Content-Length header is available. 0 indicates that the request has no body. If Content-Length header is not present, the method returns -1.
     */
    @Override
    public long getResponseContentLength(){
        return this.responseContentLength;
    }
    
    public byte[] getResponseBody() throws IOException {
        return null;
        //return this.responseStream.;
    }
    
    public byte[] getResponseBody(int maxlen) throws IOException {
        return null;
    }
    
    /**
     * Returns the response body of the HTTP method, if any, as an InputStream. If response body is not available, returns null. If the response has been buffered this method returns a new stream object on every call. If the response has not been buffered the returned stream can only be read once. 
     * 
     * @return  The response body or null. 
     * @throws IOException  If an I/O (transport) problem occurs while obtaining the response body.
     */
    @Override
    public InputStream getResponseBodyAsStream() throws IOException {
        return this.responseStream;
    }
    
    /**
     * Returns the response body of the HTTP method, if any, as a String. 
     * If response body is not available or cannot be read, returns null The string conversion on the data is done using 
     * the character encoding specified in Content-Type header. Buffers the response and this method can be called several 
     * times yielding the same result each time. Note: This will cause the entire response body to be buffered in memory. 
     * A malicious server may easily exhaust all the VM memory. It is strongly recommended, to use getResponseAsStream if 
     * the content length of the response is unknown or resonably large. 
     * 
     * @return  The response body or null. 
     * @throws  IOException     If an I/O (transport) problem occurs while obtaining the response body.
     */
    @Override
    public String getResponseBodyAsString() throws IOException {
        return this.getResponseBodyAsString(-1);
    }
    
    /**
     * Returns the response body of the HTTP method, if any, as a String. If response body is not available or 
     * cannot be read, returns null The string conversion on the data is done using the character encoding 
     * specified in Content-Type header. Buffers the response and this method can be called several times yielding 
     * the same result each time.
     * 
     * Note: This will cause the entire response body to be buffered in memory. This method is safe if the content 
     * length of the response is unknown, because the amount of memory used is limited.
     * 
     * If the response is large this method involves lots of array copying and many object allocations, which 
     * makes it unsuitable for high-performance / low-footprint applications. Those applications should use 
     * getResponseBodyAsStream(). 
     * 
     * @param maxlen    the maximum content length to accept (number of bytes). Note that, depending on the encoding, this is not equal to the number of characters. 
     * @return  The response body or null. 
     * @throws IOException  If an I/O (transport) problem occurs while obtaining the response body.
     * @todo    implement method
     */
    @Override
    public String getResponseBodyAsString(int maxlen) throws IOException {
        return null;
    }
    
    private String getHeaderName(Header header) {
        HeaderElement[] values    = header.getElements();
        
        String name = values[0].getName();
        return name.toLowerCase();
    }
}
