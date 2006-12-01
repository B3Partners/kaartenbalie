/*
 * DomainResource.java
 *
 * Created on 11 oktober 2006, 15:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author Nando De Goeij
 */
public class StyleDomainResource {
    
    private Integer id;
    private Set formats;
    private String url;
    private String domain;
    private String width;
    private String height;
    private Style style;

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public Set getFormats() {
        return formats;
    }

    public void setFormats(Set formats) {
        this.formats = formats;
    }
    
    public void addFormat(String f) {
        if (null == formats) {
            formats = new HashSet();
        }
        formats.add(f);
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
    
    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
    
    public Object clone() {
        StyleDomainResource cloneSDR    = new StyleDomainResource();
        if (null != this.id) {
            cloneSDR.id                     = new Integer(this.id.intValue());
        }
        if (null != this.formats) {
            cloneSDR.formats                = new HashSet(this.formats);
        }
        if (null != this.url) {
            cloneSDR.url                    = new String(this.url);
        }
        if (null != this.domain) {
            cloneSDR.domain                 = new String(this.domain);
        }
        if (null != this.width) {
            cloneSDR.width                  = new String(this.width);
        }
        if (null != this.height) {
            cloneSDR.height                 = new String(this.height);
        }
        return cloneSDR;
    }
    
    protected void overwriteURL(String newUrl) {
    	//First cut off only the part which is in front of the question mark.
    	String temporaryURL;
    	temporaryURL = this.getUrl();
    	if (null != temporaryURL && !temporaryURL.equals("")) {
	    	int firstOccur = temporaryURL.indexOf("?");
	    	if(firstOccur != -1) {
	    		temporaryURL = temporaryURL.substring(firstOccur);
	    		//then add the newly given url in front of the cutted part
		    	temporaryURL = newUrl + temporaryURL;
		    	//save this new URL as the one to be used
		    	temporaryURL = temporaryURL.replace("&", "&amp;");
		    	this.setUrl(temporaryURL);
	    	}
	    }
    }
    
    public String toString(String tabulator) {
    	StringBuilder result = new StringBuilder();
    	final String newLine = System.getProperty("line.separator");
        
        result.append(tabulator + "<" + this.getDomain() + ">\n");
        if (null != this.getFormats() && this.getFormats().size() != 0) {
	        Iterator it = formats.iterator();
	    	while (it.hasNext()) {
	    		result.append(tabulator + "\t<Format>" + (String)it.next() + "</Format>\n");
	    	}
        }
        if (null != this.getUrl()) {
        result.append(tabulator + "\t<OnlineResource xmlns:xlink=\"http://www.w3.org/1999/xlink\" xlink:type=\"simple\" xlink:href=\"" + 
        	this.getUrl() + "\" />\n");
        }
        result.append(tabulator + "</" + this.getDomain() + ">\n");
            	
    	return result.toString();   	
    }
}