/*
 * Style.java
 *
 * Created on 18 september 2006, 11:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server;

/**
 *
 * @author Nando De Goeij
 */
public class Style {
    
    private String name = "";
    private String title = "";
    private LegendURL legendURL;
    private String abstracts;
    private StyleSheetURL styleSheetURL;
    private StyleURL styleURL;
    
    /** Creates a new instance of Style */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LegendURL getLegendURL() {
        return legendURL;
    }

    public void setLegendURL(LegendURL legendURL) {
        this.legendURL = legendURL;
    }    

    public String getAbstracts() {
        return abstracts;
    }

    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }

    public StyleSheetURL getStyleSheetURL() {
        return styleSheetURL;
    }

    public void setStyleSheetURL(StyleSheetURL styleSheetURL) {
        this.styleSheetURL = styleSheetURL;
    }
    
    public StyleURL getStyleURL() {
        return styleURL;
    }

    public void setStyleURL(StyleURL styleURL) {
        this.styleURL = styleURL;
    }
}
