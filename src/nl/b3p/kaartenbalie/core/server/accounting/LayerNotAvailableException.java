/*
 * LayerNotAvailableException.java
 *
 * Created on February 19, 2008, 1:36 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.accounting;

/**
 *
 * @author Chris Kramer
 */
public class LayerNotAvailableException extends Exception{
    
    private static final long serialVersionUID = 9144089171508939394L;
    /** Creates a new instance of LayerNotAvailableException */
    public LayerNotAvailableException() {
        super("The requested layer is not available");
    }
    /**
     * 
     * @param message
     */
    public LayerNotAvailableException(String message) {
        super(message);
    }
    
}
