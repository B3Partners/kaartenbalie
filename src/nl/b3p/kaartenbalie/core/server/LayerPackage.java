/*
 * LayerPackage.java
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
public class LayerPackage {
    
    private String title;
    private ArrayList layers = new ArrayList();
    
    /** Creates a new instance of LayerPackage */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList getLayers() {
        return layers;
    }

    public void setLayers(ArrayList layers) {
        this.layers = layers;
    }
    
    public void addLayer(Layer layer) {
        layers.add(layer);
    }
    
}
