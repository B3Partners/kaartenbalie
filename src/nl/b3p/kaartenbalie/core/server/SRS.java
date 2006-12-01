/*
 * SRS.java
 *
 * Created on 11 oktober 2006, 15:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nando De Goeij
 */
public class SRS {
    
    private Integer id;
    private String srs;
    private String minx;
    private String miny;
    private String maxx;
    private String maxy;
    private String resx;
    private String resy;
    private Layer layer;
    
//    public static final List srsen = new ArrayList();
    
    public SRS() {
 //       srsen.add(this);
    }

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

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

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }
    
    public Object clone() {
        SRS cloneSrs   = new SRS();
        if (null != this.id) {
            cloneSrs.id    = new Integer(this.id);
        }
        if (null != this.minx) {
            cloneSrs.minx  = new String(this.minx);
        }
        if (null != this.miny) {
            cloneSrs.miny  = new String(this.miny);
        }
        if (null != this.maxx) {
            cloneSrs.maxx  = new String(this.maxx);
        }
        if (null != this.maxy) {
            cloneSrs.maxy  = new String(this.maxy);
        }
        if (null != this.resx) {
            cloneSrs.resx  = new String(this.resx);
        }
        if (null != this.resy) {
            cloneSrs.resy  = new String(this.resy);
        }
        return cloneSrs;
    }
    
    public String toString(String tabulator) {
    	StringBuilder result = new StringBuilder();
    	final String newLine = System.getProperty("line.separator");
        if(null == this.getMinx()  && null == this.getMiny() && null == this.getMaxx() && null == this.getMaxy()
        	/*&& !(Integer.parseInt(this.getMinx()) < 0) && !(Integer.parseInt(this.getMiny()) < 0)
            && !(Integer.parseInt(this.getMiny()) < 0) && !(Integer.parseInt(this.getMaxx()) < 0)*/) {
        	//result.append("<SRS=\"" + this.getSrs() + "\" />\n");
        	result.append(tabulator + "<SRS>" + this.getSrs() + "</SRS>\n");
        } else {	        	
	        result.append(tabulator + "<BoundingBox SRS=\"" + this.getSrs() + "\" " +
	                      "minx=\"" + this.getMinx() + "\" " +
	                      "miny=\"" + this.getMiny() + "\" " +
	                      "maxx=\"" + this.getMaxx() + "\" " +
	                      "maxy=\"" + this.getMaxy() + "\" ");
	        if (null != this.getResx() && null != this.getResy()) {
	        	result.append("resx=\"" + this.getResx() + "\" " +
		                      "resy=\"" + this.getResy() + "\" ");
	        }
	        result.append("/>\n");
        }
    	return result.toString();
    }
}
