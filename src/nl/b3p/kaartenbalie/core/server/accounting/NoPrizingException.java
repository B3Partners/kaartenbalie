/*
 * NoPrizingInformationException.java
 *
 * Created on January 31, 2008, 2:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.accounting;

/**
 *
 * @author Chris Kramer
 */
public class NoPrizingException extends Exception {
    
    private static final long serialVersionUID = 9144089171508939394L;
    public NoPrizingException() {
        super("No price could be calculate for the requested layer...");
    }
    public NoPrizingException(String message) {
        super(message);
    }
    
    
}
