/*
 * Capability.java
 *
 * Created on 18 september 2006, 11:15
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
public class Capability {
    
    private Request request;
    private Exceptions exceptions;
    private Layers layers;
    private ArrayList vendorSpecificCapabilitiesList = new ArrayList();
    private UserDefinedSymbolization userDefinedSymbolization;
    private Layer layer;
    
    /** Creates a new instance of Capability */
    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Exceptions getExceptions() {
        return exceptions;
    }

    public void setExceptions(Exceptions exceptions) {
        this.exceptions = exceptions;
    }

    public Layer getLayer() {
        return layer;
    }
    
    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    public void addVendorSpecificCapabilities(VendorSpecificCapabilities vendorSpecificCapabilities) {
        vendorSpecificCapabilitiesList.add(vendorSpecificCapabilities);
    }
    
    public ArrayList getVendorSpecificCapabilitiesList(VendorSpecificCapabilities vendorSpecificCapabilities) {
        return vendorSpecificCapabilitiesList;
    }
    
    public void setVendorSpecificCapabilitiesList(ArrayList vendorSpecificCapabilitiesList) {
        this.vendorSpecificCapabilitiesList = vendorSpecificCapabilitiesList;
    }
    
    public UserDefinedSymbolization getUserDefinedSymbolization(UserDefinedSymbolization userDefinedSymbolization) {
        return userDefinedSymbolization;
    }

    public void setUserDefinedSymbolization(UserDefinedSymbolization userDefinedSymbolization) {
        this.userDefinedSymbolization = userDefinedSymbolization;        
    }
    
}
