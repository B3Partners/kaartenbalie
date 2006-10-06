/*
 * Layers.java
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
public class Layers {
    
    private String title;
    private String abstr;
    private ArrayList srs = new ArrayList();
    private ArrayList layerPackages = new ArrayList();
    
    /** Creates a new instance of Layers */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstr() {
        return abstr;
    }

    public void setAbstr(String abstr) {
        this.abstr = abstr;
    }

    public ArrayList getSrs() {
        return srs;
    }

    public void setSrs(ArrayList srs) {
        this.srs = srs;
    }

    public ArrayList getLayerPackages() {
        return layerPackages;
    }

    public void setLayerPackages(ArrayList layerPackages) {
        this.layerPackages = layerPackages;
    }
    
    public void addSrs(String s) {
        srs.add(s);
    }
    
    public void addLayerPackage(LayerPackage layerPackage) {
        layerPackages.add(layerPackage);
    }    
}
