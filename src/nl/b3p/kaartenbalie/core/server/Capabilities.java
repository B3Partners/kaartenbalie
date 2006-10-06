/*
 * Capabilities.java
 *
 * Created on 18 september 2006, 11:51
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
public class Capabilities {
    
    private ArrayList formats = new ArrayList();
    private ArrayList dcpTypes = new ArrayList();
    
    public ArrayList getFormats() {
        return formats;
    }

    public void setFormats(ArrayList formats) {
        this.formats = formats;
    }
    
    public void addFormat(String f) {
        formats.add(f);
    }
    
    public ArrayList getDCPTypes() {
        return dcpTypes;
    }

    public void setDCPTypes(ArrayList dcpTypes) {
        this.dcpTypes = dcpTypes;
    }

    public void addDCPType(DCPType dcpType) {
        dcpTypes.add(dcpType);
    }
    
}
