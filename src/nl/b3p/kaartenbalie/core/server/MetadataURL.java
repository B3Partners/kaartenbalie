package nl.b3p.kaartenbalie.core.server;

import java.util.ArrayList;

public class MetadataURL {
    private ArrayList onlineResources = new ArrayList();
    private String type;
    
    /**
     * Creates a new instance of LegendURL
     */
    public void addOnlineResource(OnlineResource onlineResource) {
        onlineResources.add(onlineResource);
    }

    public ArrayList getOnlineResources() {
        return onlineResources;
    }

    public void setOnlineResources(ArrayList onlineResources) {
        this.onlineResources = onlineResources;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}