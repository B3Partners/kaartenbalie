/*
 * BoundingBox.java
 *
 * Created on 26 september 2006, 16:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server;

/**
 *
 * @author Nando De Goeij
 */
public class BoundingBox {
    
    private String srs;
    private String minx;
    private String miny;
    private String maxx;
    private String maxy;
    private String resx;
    private String resy;
    
    /** Creates a new instance of BoundingBox */

    public String getSrs() {
        return srs;
    }

    public void setSrs(String srs) {
        this.srs = srs;
    }

    public String getMinx() {
        return minx;
    }

    public void setMinx(String minx) {
        this.minx = minx;
    }

    public String getMiny() {
        return miny;
    }

    public void setMiny(String miny) {
        this.miny = miny;
    }

    public String getMaxx() {
        return maxx;
    }

    public void setMaxx(String maxx) {
        this.maxx = maxx;
    }

    public String getMaxy() {
        return maxy;
    }

    public void setMaxy(String maxy) {
        this.maxy = maxy;
    }

    public String getResx() {
        return resx;
    }

    public void setResx(String resx) {
        this.resx = resx;
    }

    public String getResy() {
        return resy;
    }

    public void setResy(String resy) {
        this.resy = resy;
    }
}
