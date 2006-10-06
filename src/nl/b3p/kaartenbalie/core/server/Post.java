package nl.b3p.kaartenbalie.core.server;

import java.util.ArrayList;

public class Post {
    private ArrayList onlineResources = new ArrayList();
    
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
}