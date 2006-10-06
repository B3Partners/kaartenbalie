package nl.b3p.kaartenbalie.core.server;

import java.util.ArrayList;

public class AuthorityURL {
    private ArrayList onlineResources = new ArrayList();
    private String name;
    
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}