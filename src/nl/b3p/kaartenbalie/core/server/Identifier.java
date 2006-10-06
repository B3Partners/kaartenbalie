/*
 * Identifier.java
 *
 * Created on 26 september 2006, 16:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server;

/**
 *
 * @author Nando De Goeij
 */
public class Identifier {
    
    private String authority;
    
    /** Creates a new instance of Identifier */
    public Identifier() {
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
    
}
