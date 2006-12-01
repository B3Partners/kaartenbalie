package nl.b3p.kaartenbalie.core.server;

public class Attribution {
    
    private Integer id;
    private String title;
    private String attributionURL;
    private String logoURL;
    private String logoFormat;
    private String logoWidth;
    private String logoHeight;
    private Layer layer;

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAttributionURL() {
        return attributionURL;
    }

    public void setAttributionURL(String attributionURL) {
        this.attributionURL = attributionURL;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public String getLogoWidth() {
        return logoWidth;
    }

    public void setLogoWidth(String logoWidth) {
        this.logoWidth = logoWidth;
    }

    public String getLogoHeight() {
        return logoHeight;
    }

    public void setLogoHeight(String logoHeight) {
        this.logoHeight = logoHeight;
    }
    
    public String getLogoFormat() {
        return logoFormat;
    }

    public void setLogoFormat(String logoFormat) {
        this.logoFormat = logoFormat;
    }

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }
    
    public Object clone() {
        Attribution cloneAtt        = new Attribution();
        if (null != this.id) {
            cloneAtt.id             = new Integer(this.id.intValue());
        }
        if (null != this.title) {
            cloneAtt.title          = new String(this.title);
        }
        if (null != this.attributionURL) {
            cloneAtt.attributionURL = new String(this.attributionURL);
        }
        if (null != this.logoURL) {
            cloneAtt.logoURL        = new String(this.logoURL);
        }
        if (null != this.logoFormat) {
            cloneAtt.logoFormat     = new String(this.logoFormat);
        }
        if (null != this.logoWidth) {
            cloneAtt.logoWidth      = new String(this.logoWidth);
        }
        if (null != this.logoHeight) {
            cloneAtt.logoHeight     = new String(this.logoHeight);
        }
        return cloneAtt;
    }
    
    public String toString(String tabulator) {
    	StringBuilder result = new StringBuilder();
    	final String newLine = System.getProperty("line.separator");
        
        result.append(tabulator + "<Attribution>\n");
        result.append(tabulator + "\t<Title>" + this.getTitle() + "</Title>\n");
        result.append(tabulator + "\t<OnlineResource xmlns:xlink=\"http://www.w3.org/1999/xlink\" xlink:type=\"simple\" xlink:href=\"" + 
        	this.getAttributionURL() + "\" />\n");
        result.append(tabulator + "\t<LogoURL width=\"" + this.getLogoWidth() + "\" height=\"" + this.getLogoHeight() + "\">\n");
        result.append(tabulator + "\t<Format>" + this.getLogoFormat() + "</Format>\n");
        result.append(tabulator + "\t<OnlineResource xmlns:xlink=\"http://www.w3.org/1999/xlink\" xlink:type=\"simple\" xlink:href=\"" + 
        	this.getLogoURL() + "\" />\n");
        result.append(tabulator + "\t</LogoURL>\n");
        result.append(tabulator + "</Attribution>\n");
        
    	return result.toString();
    }
}