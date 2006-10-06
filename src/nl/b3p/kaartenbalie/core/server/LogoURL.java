package nl.b3p.kaartenbalie.core.server;

import java.util.ArrayList;

public class LogoURL {
    
    private ArrayList onlineResources = new ArrayList();
    private String width;
    private String height;
    
    public void addOnlineResource(OnlineResource onlineResource) {
        onlineResources.add(onlineResource);
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