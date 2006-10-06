/*
 * Request.java
 *
 * Created on 18 september 2006, 11:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server;

/**
 *
 * @author Nando De Goeij
 */
public class Request {
    
    private Capabilities capabilities;
    private Map map;
    private FeatureInfo featureInfo;
    private DescribeLayer describeLayer;
    private LegendGraphic legendGraphic;
    private Styles styles;
    private PutStyles putStyles;
    
    /** Creates a new instance of Request */
    public Capabilities getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Capabilities capabilities) {
        this.capabilities = capabilities;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public FeatureInfo getFeatureInfo() {
        return featureInfo;
    }

    public void setFeatureInfo(FeatureInfo featureInfo) {
        this.featureInfo = featureInfo;
    }

    public DescribeLayer getDescribeLayer() {
        return describeLayer;
    }

    public void setDescribeLayer(DescribeLayer describeLayer) {
        this.describeLayer = describeLayer;
    }

    public LegendGraphic getLegendGraphic() {
        return legendGraphic;
    }

    public void setLegendGraphic(LegendGraphic legendGraphic) {
        this.legendGraphic = legendGraphic;
    }

    public Styles getStyles() {
        return styles;
    }

    public void setStyles(Styles styles) {
        this.styles = styles;
    }
    
    public PutStyles getPutStyles() {
        return putStyles;
    }

    public void setPutStyles(PutStyles putStyles) {
        this.putStyles = putStyles;
    }
    
}
