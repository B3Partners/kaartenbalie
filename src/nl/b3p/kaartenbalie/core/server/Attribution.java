package nl.b3p.kaartenbalie.core.server;

public class Attribution {
    
    private OnlineResource onlineResource;
    private LogoURL logoURL;

    public OnlineResource getOnlineResource() {
        return onlineResource;
    }

    public void setOnlineResource(OnlineResource onlineResource) {
        this.onlineResource = onlineResource;
    }

    public LogoURL getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(LogoURL logoURL) {
        this.logoURL = logoURL;
    }
}