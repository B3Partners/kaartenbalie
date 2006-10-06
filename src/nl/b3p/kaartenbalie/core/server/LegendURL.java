/*
 * LegendURL.java
 *
 * Created on 18 september 2006, 11:17
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
public class LegendURL {
    
    private ArrayList formats = new ArrayList();
    private ArrayList onlineResources = new ArrayList();
    private String width;
    private String height;
    
    /**
     * Creates a new instance of LegendURL
     */
    public void addFormat(String f) {
        formats.add(f);
    }

    public void addOnlineResource(OnlineResource onlineResource) {
        onlineResources.add(onlineResource);
    }

    public ArrayList getFormats() {
        return formats;
    }

    public void setFormats(ArrayList formats) {
        this.formats = formats;
    }

    public ArrayList getOnlineResources() {
        return onlineResources;
    }

    public void setOnlineResources(ArrayList onlineResources) {
        this.onlineResources = onlineResources;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
