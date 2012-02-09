package general;

import java.util.Enumeration;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 *
 * @author rachelle
 */
public class HttpSessionStub implements HttpSession{
    public long getCreationTime() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getLastAccessedTime() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ServletContext getServletContext() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setMaxInactiveInterval(int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getMaxInactiveInterval() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public HttpSessionContext getSessionContext() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getAttribute(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getValue(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Enumeration getAttributeNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String[] getValueNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setAttribute(String string, Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void putValue(String string, Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeAttribute(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeValue(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void invalidate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isNew() {
        throw new UnsupportedOperationException("Not supported yet.");
    }    
}
