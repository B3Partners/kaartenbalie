/*
 * DescribeLayer.java
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
public class DescribeLayer {
    
    private ArrayList formats = new ArrayList();
    private ArrayList dcpTypes = new ArrayList();
    
    public void addFormat(String f) {
        getFormats().add(f);
    }

    public void addDCPType(DCPType dcpType) {
        getDcpTypes().add(dcpType);
    }

    public ArrayList getFormats() {
        return formats;
    }

    public void setFormats(ArrayList formats) {
        this.formats = formats;
    }

    public ArrayList getDcpTypes() {
        return dcpTypes;
    }

    public void setDcpTypes(ArrayList dcpTypes) {
        this.dcpTypes = dcpTypes;
    }
    
}
