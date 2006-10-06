/*
 * UserDefinedSymbolization.java
 *
 * Created on 26 september 2006, 15:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server;

/**
 *
 * @author Nando De Goeij
 */
public class UserDefinedSymbolization {
    
    private String supportSLD;
    private String userLayer;
    private String userStyle;
    private String remoteWFS;    

    public String getSupportSLD() {
        return supportSLD;
    }

    public void setSupportSLD(String supportSLD) {
        this.supportSLD = supportSLD;
    }

    public String getUserLayer() {
        return userLayer;
    }

    public void setUserLayer(String userLayer) {
        this.userLayer = userLayer;
    }

    public String getUserStyle() {
        return userStyle;
    }

    public void setUserStyle(String userStyle) {
        this.userStyle = userStyle;
    }

    public String getRemoteWFS() {
        return remoteWFS;
    }

    public void setRemoteWFS(String remoteWFS) {
        this.remoteWFS = remoteWFS;
    }
}
