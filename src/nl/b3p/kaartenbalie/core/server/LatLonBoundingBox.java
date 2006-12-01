/*
 * LatLonBoundingBox.java
 *
 * Created on 11 oktober 2006, 16:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server;

/**
 *
 * @author Nando De Goeij
 */
public class LatLonBoundingBox {
    
    private Integer id;
    private String minx;
    private String miny;
    private String maxx;
    private String maxy;
    private Layer layer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }
    
    public Object clone() {
        LatLonBoundingBox cloneLlbb = new LatLonBoundingBox();
        if (null != this.id) {
            cloneLlbb.id                = new Integer(this.id);
        }
        if (null != this.minx) {
            cloneLlbb.minx              = new String(this.minx);
        }
        if (null != this.miny) {
            cloneLlbb.miny              = new String(this.miny);
        }
        if (null != this.maxx) {
            cloneLlbb.maxx              = new String(this.maxx);
        }
        if (null != this.maxy) {
            cloneLlbb.maxy              = new String(this.maxy);
        }
        return cloneLlbb;
    }
    
    public String toString(String tabulator) {
    	StringBuilder result = new StringBuilder();
    	final String newLine = System.getProperty("line.separator");
    	
    	result.append(tabulator + "<LatLonBoundingBox " + 
                      "minx=\"" + this.getMinx() + "\" " +
                      "miny=\"" + this.getMiny() + "\" " +
                      "maxx=\"" + this.getMaxx() + "\" " +
                      "maxy=\"" + this.getMaxy() + "\" " + "/>\n");
    	
    	return result.toString();
    }
}
