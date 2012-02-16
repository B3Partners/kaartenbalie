package general;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionContext;

/**
 *
 * @author rachelle
 */
public class HttpSessionStub implements HttpSession{
    private long creationTime;
    private String id;
    private boolean isNewSession;
    private long accessedTime;
    private ServletContext context;
    private int maxInactiveInterval;
    private HashMap<String,Object> attributes;
    private long now;
    
    public HttpSessionStub(boolean newSession,ServletContext context){
        Date date                   = new Date();
        
        this.now                    = date.getTime();
        this.creationTime           = date.getTime();
        this.accessedTime           = date.getTime();
        this.isNewSession           = newSession;
        this.context                = context;
        this.maxInactiveInterval    = 300; //default timeout 5 minutes
        this.attributes             = new HashMap<String,Object>();
        
        if( !this.isNewSession ){
            this.accessedTime -= (120*1000);// simulate session from 2 minutes ago
        }
    }
    
    /**
     * Returns the time when this session was created, measured in milliseconds since midnight January 1, 1970 GMT.
     * 
     * @return a long specifying when this session was created, expressed in milliseconds since 1/1/1970 GMT
     * @throws  IllegalStateException   if this method is called on an invalidated session
     */
    public long getCreationTime() throws IllegalStateException {
        this.checkState();
        
        return this.creationTime;
    }
    
    /**
     * Sets a string containing the unique identifier assigned to this session. The identifier is assigned by the servlet container and is implementation dependent.
     * @param id a string specifying the identifier assigned to this session
     */
    public void setId(String id){
        this.id = id;
    }

    /**
     * Returns a string containing the unique identifier assigned to this session. The identifier is assigned by the servlet container and is implementation dependent.
     * 
     * @return a string specifying the identifier assigned to this session
     */
    public String getId() {
        return this.id;
    }

    /**
     * Returns the last time the client sent a request associated with this session, as the number of milliseconds since midnight January 1, 1970 GMT, and marked by the time the container received the request. 
     * 
     * @return a long representing the last time the client sent a request associated with this session, expressed in milliseconds since 1/1/1970 GMT
     */
    public long getLastAccessedTime() {
        return this.accessedTime;
    }

    /**
     * Returns the ServletContext to which this session belongs.
     *
     * @return  The ServletContext object for the web application
     */
    public ServletContext getServletContext() {
        return this.context;
    }

    /**
     * Specifies the time, in seconds, between client requests before the servlet container will invalidate this session. A negative time indicates the session should never timeout.
     * 
     * @param interval An integer specifying the number of seconds
     */
    public void setMaxInactiveInterval(int interval) {
        this.maxInactiveInterval    = interval;
    }

    /**
     * Returns the maximum time interval, in seconds, that the servlet container will keep this session open between client accesses. After this interval, the servlet container will invalidate the session. The maximum time interval can be set with the setMaxInactiveInterval method. A negative time indicates the session should never timeout.
     * 
     * @return an integer specifying the number of seconds this session remains open between client requests
     */
    public int getMaxInactiveInterval() {
        return this.maxInactiveInterval;
    }

    /**
     * @deprecated  As of Version 2.1, this method is deprecated and has no replacement. It will be removed in a future version of the Java Servlet API. 
     * @return null
     */
    public HttpSessionContext getSessionContext() {
        System.err.println("Use of deprecated function HttpSession:getSessionContext. This function will be removed");
        
        return null;
    }
    
    /**
     * Returns the object bound with the specified name in this session, or null if no object is bound under the name.
     * 
     * @param name  a string specifying the name of the object
     * @return the object with the specified name
     * @throws IllegalStateException  if this method is called on an invalidated session
     */
    public Object getAttribute(String name) throws IllegalStateException {
        this.checkState();
        
        if( !this.attributes.containsKey(name) )    return null;
        
        return this.attributes.get(name);
    }

    /**
     * @deprecated  As of Version 2.2, this method is replaced by getAttribute(java.lang.String).
     * @param name    a string specifying the name of the object
     * @return  the object with the specified name
     * @throws IllegalStateException  if this method is called on an invalidated session
     */
    public Object getValue(String name) throws IllegalStateException {
        System.err.println("Call to deprecated function HttpSession:getValue(name). Use HttpSession:getAttribute(name) instead.");
        
        return this.getAttribute(name);
    }

    /**
     * Returns an Enumeration of String objects containing the names of all the objects bound to this session.
     * 
     * @return an Enumeration of String objects specifying the names of all the objects bound to this session
     * @throws IllegalStateException  if this method is called on an invalidated session
     */
    public Enumeration getAttributeNames() throws IllegalStateException {
        this.checkState();
        
        ServerDetailEnumeration enumeration = new ServerDetailEnumeration();
        
        Object[] keys   = this.attributes.keySet().toArray();
        for(int i=0; i<keys.length; i++){
            enumeration.addName((String) keys[i]);
        }
        
        return enumeration;
    }

    /**
     * @deprecated  As of Version 2.2, this method is replaced by getAttributeNames()
     * @return  an array of String objects specifying the names of all the objects bound to this session
     * @throws IllegalStateException  if this method is called on an invalidated session
     */
    public String[] getValueNames() throws IllegalStateException {
        System.err.println("Call to deprecated function HttpSession:getValueNames(). Use HttpSession:getAttributeNames() instead.");
        
        this.checkState();
        
        String[] names  = new String[this.attributes.size()];
        Object[] keys   = this.attributes.keySet().toArray();
        for(int i=0; i<keys.length; i++){
            names[i]   = (String) keys[i];
        }
        
        return names;
    }

    /**
     * Binds an object to this session, using the name specified. If an object of the same name is already bound to the session, the object is replaced.
     * 
     * After this method executes, and if the new object implements HttpSessionBindingListener, the container calls HttpSessionBindingListener.valueBound. The container then notifies any HttpSessionAttributeListeners in the web application.
     * 
     * If an object was already bound to this session of this name that implements HttpSessionBindingListener, its HttpSessionBindingListener.valueUnbound method is called.
     * 
     * If the value passed in is null, this has the same effect as calling removeAttribute().
     * 
     * @param name  the name to which the object is bound; cannot be null
     * @param value the object to be bound
     * @throws IllegalStateException  if this method is called on an invalidated session
     */
    public void setAttribute(String name, Object value )  throws IllegalStateException{
        if( name == null) return;
        
        this.checkState();
        
        if( value == null ){
            this.removeAttribute(name);
        }
        else {
            this.attributes.put(name, value);
            
            if( value instanceof HttpSessionBindingListener ){
                HttpSessionBindingListener caller   = (HttpSessionBindingListener) value;
                caller.valueBound(new HttpSessionBindingEvent(this,name));
            }
        }
    }

    /**
     * @deprecated  As of Version 2.2, this method is replaced by setAttribute(java.lang.String, java.lang.Object)
     * @param name      the name to which the object is bound; cannot be null
     * @param value     the object to be bound; cannot be null
     * @throws IllegalStateException  if this method is called on an invalidated session
     */
    public void putValue(String name, Object value) throws IllegalStateException {
        System.err.println("Call to deprecated function HttpSession:putValue(name,value). Use HttpSession:setAttribute(name,value) instead.");
        
        if( name == null || value == null ) return;
        
        this.setAttribute(name, value);
    }

    /**
     * Removes the object bound with the specified name from this session. If the session does not have an object bound with the specified name, this method does nothing.
     * 
After this method executes, and if the object implements HttpSessionBindingListener, the container calls HttpSessionBindingListener.valueUnbound. The container then notifies any HttpSessionAttributeListeners in the web application
     * 
     * @param name the name of the object to remove from this session
     * @throws IllegalStateException  if this method is called on an invalidated session
     */
    public void removeAttribute(String name) throws IllegalStateException {
        if( this.attributes.containsKey(name) ){
            Object value    = this.attributes.get(name);
            
            if( value instanceof HttpSessionBindingListener ){
                HttpSessionBindingListener caller   = (HttpSessionBindingListener) value;
                caller.valueBound(new HttpSessionBindingEvent(this,name));
            }
                    
            this.attributes.remove(name);
        }
    }

    /**
     * @deprecated As of Version 2.2, this method is replaced by removeAttribute(java.lang.String)
     * @param name the name of the object to remove from this session
     * @throws IllegalStateException  if this method is called on an invalidated session
     */
    public void removeValue(String name) throws IllegalStateException{
        System.err.println("Use of deprecated function HttpSession:removeValue(name). Use HttpSession:removeAttribute(name) instead");
        
        this.removeAttribute(name);
    }

    /**
     * Invalidates this session then unbinds any objects bound to it.
     * 
     * @throws IllegalStateException if this method is called on an already invalidated session
     */
    public void invalidate() throws IllegalStateException {
        this.checkState();
        
        Object[] keys   = this.attributes.keySet().toArray();
        for(int i=0; i<keys.length; i++){
            this.removeAttribute((String) keys[i]);
        }
        
        this.now    = this.accessedTime + this.maxInactiveInterval + 10;
    }

    /**
     * Returns true if the client does not yet know about the session or if the client chooses not to join the session. For example, if the server used only cookie-based sessions, and the client had disabled the use of cookies, then a session would be new on each request.
     * 
     * @return true if the server has created a session, but the client has not yet joined
     * @throws IllegalStateException if this method is called on an already invalidated session
     */
    public boolean isNew() throws IllegalStateException {
        this.checkState();
        
        return this.isNewSession;
    }

    /**
     * Checks if a session is valid
     * 
     * @throws IllegalStateException if the session is not valid
     */
    private void checkState() throws IllegalStateException {
        if( this.maxInactiveInterval != -1 && (this.now - this.accessedTime) > this.maxInactiveInterval ){
            throw new IllegalStateException("Invalid session");
        }
    }
}
