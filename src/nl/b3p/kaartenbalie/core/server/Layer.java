/*
 * Layer.java
 *
 * Created on 18 september 2006, 11:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server;

import java.util.ArrayList;

/**
 *
 * @author Nando De Goeij
 */
public class Layer {
    
    private String name = "";
    private String title = "";
    private String abstr = "";
    private String queryable = "";
    private String cascaded = "";
    private String opaque ="";
    private String noSubsets = "";
    private String fixedWidth = "";
    private String fixedHeight = "";
    private ScaleHint scaleHint;
    private ArrayList styles = new ArrayList();
    private ArrayList layers = new ArrayList();
    private ArrayList featureListURLs = new ArrayList();
    private ArrayList metadataURLs = new ArrayList();
    private ArrayList extents = new ArrayList();
    private ArrayList boundingBoxes = new ArrayList();
    private ArrayList authorityURLs = new ArrayList();
    private ArrayList dimensions = new ArrayList();
    private ArrayList dataURLs = new ArrayList();
    private ArrayList identifiers = new ArrayList();
    private Attribution attribution;
    private LatLonBoundingBox latLonBoundingBox;
    
    /** Creates a new instance of Layer */
    public Layer() {
    }
    
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
    
    public String getAbstr() {
        return abstr;
    }

    public void setAbstr(String abstr) {
        this.abstr = abstr;
    }
    
    public ArrayList getStyles() {
        return styles;
    }
    
    public void setStyles(ArrayList styles) {
        this.styles = styles;
    }
    
    public void addStyle(Style style) {
        styles.add(style);
    }
    
    public ArrayList getLayers() {
        return layers;
    }

    public void setLayers(ArrayList layers) {
        this.layers = layers;
    }
    
    public void addLayer(Layer layer) {
        layers.add(layer);
    }

    public ScaleHint getScaleHint() {
        return scaleHint;
    }

    public void setScaleHint(ScaleHint scaleHint) {
        this.scaleHint = scaleHint;
    }

    public String getQueryable() {
        return queryable;
    }

    public void setQueryable(String queryable) {
        this.queryable = queryable;
    }

    public String getCascaded() {
        return cascaded;
    }

    public void setCascaded(String cascaded) {
        this.cascaded = cascaded;
    }

    public String getOpaque() {
        return opaque;
    }

    public void setOpaque(String opaque) {
        this.opaque = opaque;
    }

    public String getNoSubsets() {
        return noSubsets;
    }

    public void setNoSubsets(String noSubsets) {
        this.noSubsets = noSubsets;
    }

    public String getFixedWidth() {
        return fixedWidth;
    }

    public void setFixedWidth(String fixedWidth) {
        this.fixedWidth = fixedWidth;
    }

    public String getFixedHeight() {
        return fixedHeight;
    }

    public void setFixedHeight(String fixedHeight) {
        this.fixedHeight = fixedHeight;
    }
    
    public Attribution getAttribution() {
        return attribution;
    }

    public void setAttribution(Attribution attribution) {
        this.attribution = attribution;
    }

    public void addFeatureListURL(FeatureListURL featureListURL) {
        featureListURLs.add(featureListURL);
    }

    public ArrayList getFeatureListURLs() {
        return featureListURLs;
    }

    public void setFeatureListURLs(ArrayList featureListURLs) {
        this.featureListURLs = featureListURLs;
    }

    public void addMetadataURL(MetadataURL metadataURL) {
        metadataURLs.add(metadataURL);
    }

    public ArrayList getMetadataURLs() {
        return metadataURLs;
    }

    public void setMetadataURLs(ArrayList metadataURLs) {
        this.metadataURLs = metadataURLs;
    }

    public void addExtent(Extent extent) {
        extents.add(extent);
    }

    public ArrayList getExtents() {
        return extents;
    }

    public void setExtents(ArrayList extents) {
        this.extents = extents;
    }

    public void addBoundingBox(BoundingBox boundingBox) {
        boundingBoxes.add(boundingBox);
    }

    public ArrayList getBoundingBoxes() {
        return boundingBoxes;
    }

    public void setBoundingBoxes(ArrayList boundingBoxes) {
        this.boundingBoxes = boundingBoxes;
    }

    public void addAuthorityURL(AuthorityURL authorityURL) {
        authorityURLs.add(authorityURL);
    }

    public ArrayList getAuthorityURLs() {
        return authorityURLs;
    }

    public void setAuthorityURLs(ArrayList authorityURLs) {
        this.authorityURLs = authorityURLs;
    }

    public void addDimension(Dimension dimension) {
        dimensions.add(dimension);
    }

    public ArrayList getDimensions() {
        return dimensions;
    }

    public void setDimensions(ArrayList dimensions) {
        this.dimensions = dimensions;
    }
    
    public LatLonBoundingBox getLatLonBoundingBox() {
        return latLonBoundingBox;
    }

    public void setLatLonBoundingBox(LatLonBoundingBox latLonBoundingBox) {
        this.latLonBoundingBox = latLonBoundingBox;
    }

    public void addDataURL(DataURL dataURL) {
        dataURLs.add(dataURL);
    }

    public ArrayList getDataURLs() {
        return dataURLs;
    }

    public void setDataURLs(ArrayList dataURLs) {
        this.dataURLs = dataURLs;
    }

    public void addIdentifier(Identifier identifier) {
        identifiers.add(identifier);
    }

    public ArrayList getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(ArrayList identifiers) {
        this.identifiers = identifiers;
    }
}