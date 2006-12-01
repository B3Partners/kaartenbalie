/*
 * Identifier.java
 *
 * Created on 26 september 2006, 16:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server;

/**
 *
 * @author Nando De Goeij
 */
public class Identifier {
    
    private Integer id;
    private String authorityName;
    private String value;
    private String authorityURL;
    private Layer layer;

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    public String getAuthorityURL() {
        return authorityURL;
    }

    public void setAuthorityURL(String authorityURL) {
        this.authorityURL = authorityURL;
    }

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }
    
    public Object clone() {
        Identifier cloneIdent           = new Identifier();
        if (null != this.id) {
            cloneIdent.id               = new Integer(this.id);
        }
        if (null != this.authorityName) {
            cloneIdent.authorityName    = new String(this.authorityName);
        }
        if (null != this.value) {
            cloneIdent.value            = new String(this.value);
        }
        if (null != this.authorityURL) {
            cloneIdent.authorityURL     = new String(this.authorityURL);
        }
        return cloneIdent;
    }
    
    public String toString(String tabulator) {
    	StringBuilder result = new StringBuilder();
    	final String newLine = System.getProperty("line.separator");
    	
        result.append(tabulator + "<AuthorityURL authority=\"" + this.getAuthorityName() + ">\n");
        result.append(tabulator + "\t<OnlineResource xmlns:xlink=\"http://www.w3.org/1999/xlink\" xlink:type=\"simple\" xlink:href=\"" + 
        	this.getAuthorityURL() + "\" />\n");
        result.append(tabulator + "</AuthorityURL>\n");
        
        result.append(tabulator + "<Identifier authority=\"" + this.getAuthorityName() + ">" + this.getAuthorityName() + "</Identifier>\n");
        
        return result.toString();
    }
}
