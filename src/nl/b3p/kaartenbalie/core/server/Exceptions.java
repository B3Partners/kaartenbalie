/*
 * Exceptions.java
 *
 * Created on 18 september 2006, 11:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server;

import java.util.ArrayList;

/**
 *
 * @author Nando De Goeij
 */
public class Exceptions {
    
    private ArrayList format = new ArrayList();
    
    /** Creates a new instance of Exceptions */
    public ArrayList getFormat() {
        return format;
    }

    public void setFormat(ArrayList format) {
        this.format = format;
    }
    
    public void addFormat(String f) {
        format.add(f);
    }
    
    
}
