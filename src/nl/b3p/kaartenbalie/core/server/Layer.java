/*
 * Layer.java
 *
 * Created on 18 september 2006, 11:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.lang.reflect.Method;

/**
 *
 * @author Nando De Goeij
 */
public class Layer {
    
    private Integer id;
    private String name = "";
    private String title = "";
    private String abstracts;
    private String queryable;
    private String cascaded;
    private String opaque;
    private String nosubsets;
    private String fixedWidth;
    private String fixedHeight;
    private String scaleHintMin;
    private String scaleHintMax;
    private Attribution attribution;// = new Attribution();
    private Layer parent;
    private LatLonBoundingBox latLonBoundingBox;// = new LatLonBoundingBox();
    private ServiceProvider serviceProvider;// = new ServiceProvider();
    private Set dimensions;
    private Set layerKeywordList;
    private Set styles;
    private Set domainResource;
    private Set identifiers;
    private Set organizationLayers;
    private Set srs;
    private Set layers;

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
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

    public String getAbstracts() {
        return abstracts;
    }

    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
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

    public String getNosubsets() {
        return nosubsets;
    }

    public void setNosubsets(String nosubsets) {
        this.nosubsets = nosubsets;
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

    public String getScaleHintMin() {
        return scaleHintMin;
    }

    public void setScaleHintMin(String scaleHintMin) {
        this.scaleHintMin = scaleHintMin;
    }

    public String getScaleHintMax() {
        return scaleHintMax;
    }

    public void setScaleHintMax(String scaleHintMax) {
        this.scaleHintMax = scaleHintMax;
    }

    public Layer getParent() {
        return parent;
    }

    public void setParent(Layer parent) {
        this.parent = parent;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
        if(null != layers) {
            for (Iterator it = getLayers().iterator(); it.hasNext();) {
                Layer subLayer = (Layer) it.next();
                subLayer.setServiceProvider(serviceProvider);
            }
        }
    }

    public Set getDimensions() {
        return dimensions;
    }

    public void setDimensions(Set dimensions) {
        this.dimensions = dimensions;
    }
    
    public void addDimension(Dimensions dimension) {
        if(dimensions == null) {
            dimensions = new HashSet();
        }
        dimensions.add(dimension);
        dimension.setLayer(this);
    }

    public Set getLayerKeywordList() {
        return layerKeywordList;
    }

    public void setLayerKeywordList(Set layerKeywordList) {
        this.layerKeywordList = layerKeywordList;
    }
    
    public void addKeyword(String keyword) {
        if(layerKeywordList == null) {
            layerKeywordList = new HashSet();
        }
        layerKeywordList.add(keyword);
    }

    public Set getStyles() {
        return styles;
    }

    public void setStyles(Set styles) {
        this.styles = styles;
    }
    
    public void addStyle(Style style) {
        if (null == styles) {
            styles = new HashSet();
        }
        styles.add(style);
        style.setLayer(this);        
    }

    public Set getDomainResource() {
        return domainResource;
    }

    public void setDomainResource(Set domainResource) {
        this.domainResource = domainResource;
    }
    
    public void addDomainResource(LayerDomainResource dr) {
        if(null == domainResource) {
            domainResource = new HashSet();
        }
        domainResource.add(dr);
        dr.setLayer(this);        
    }

    public Set getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(Set identifiers) {
        this.identifiers = identifiers;
    }
    
    public void addIdentifier(Identifier identifier) {
        if(null == identifiers) {
            identifiers = new HashSet();
        }
        identifiers.add(identifier);
        identifier.setLayer(this);       
    }

    public Set getOrganizationLayers() {
        return organizationLayers;
    }

    public void setOrganizationLayers(Set organizationLayers) {
        this.organizationLayers = organizationLayers;
    }

    public Set getSrs() {
        return srs;
    }

    public void setSrs(Set srs) {
        this.srs = srs;
    }
    
    public void addSrs(SRS s) {
        if(null == srs) {
            srs = new HashSet();
        }
        srs.add(s);
        s.setLayer(this);
    }

    public Set getLayers() {
        return layers;
    }

    public void setLayers(Set layers) {
        this.layers = layers;
    }
    
    public void addLayer(Layer layer) {
        if (null == layers) {
            layers = new HashSet();
        }
        layers.add(layer);
        layer.setParent(this);
        //layer.setServiceProvider(serviceProvider);
        //layer.setStyles(styles);
    }    

    public LatLonBoundingBox getLatLonBoundingBox() {
        return latLonBoundingBox;
    }

    public void setLatLonBoundingBox(LatLonBoundingBox latLonBoundingBox) {
        this.latLonBoundingBox = latLonBoundingBox;
        if(null != latLonBoundingBox) {
            latLonBoundingBox.setLayer(this);
        }
    }

    public Attribution getAttribution() {
        return attribution;
    }

    public void setAttribution(Attribution attribution) {
        this.attribution = attribution;
        if(null != attribution) {
            attribution.setLayer(this);
        }
    }
    
    public void shallowClone(Layer l) {
        this.id = l.id;
        this.title = l.title;        
    }
    
    protected void overwriteURL(String newUrl) {
    	Iterator it;
    	//Layers:
    	if(null != this.getLayers() && this.getLayers().size() != 0) {  
	    	it = this.getLayers().iterator();
	    	while (it.hasNext()) {
	    		Layer l = (Layer)it.next();
	    		l.overwriteURL(newUrl);
	    	}
    	}
    	
    	//LayerDomainResource:
    	if(null != domainResource) {
    		it = domainResource.iterator();
	    	while (it.hasNext()) {
				LayerDomainResource ldr = (LayerDomainResource)it.next();
	            ldr.overwriteURL(newUrl);
	    	}
    	}
    	
    	//Styles:
    	if(null != this.getStyles() && this.getStyles().size() != 0) {       
	        it = styles.iterator();
	    	while (it.hasNext()) {
	    		Style style = (Style)it.next();
	    		style.overwriteURL(newUrl);
	    	}
        }
    }
    
    public Object clone() {
        Layer cloneLayer                    = new Layer();
        if (null != this.id) {
            cloneLayer.id                   = new Integer(this.id);
        }
        if (null != this.name) {
            cloneLayer.name                 = new String(this.name);
        }
        if (null != this.title) {
            cloneLayer.title                = new String(this.title);
        }
        if (null != this.abstracts) {
            cloneLayer.abstracts            = new String(this.abstracts);
        }
        if (null != this.queryable) {
            cloneLayer.queryable            = new String(this.queryable);
        }
        if (null != this.cascaded) {
            cloneLayer.cascaded             = new String(this.cascaded);
        }
        if (null != this.opaque) {
            cloneLayer.opaque               = new String(this.opaque);
        }
        if (null != this.nosubsets) {
            cloneLayer.nosubsets            = new String(this.nosubsets);
        }
        if (null != this.fixedWidth) {
            cloneLayer.fixedWidth           = new String(this.fixedWidth);
        }
        if (null != this.fixedHeight) {
            cloneLayer.fixedHeight          = new String(this.fixedHeight);
        }
        if (null != this.scaleHintMin) {
            cloneLayer.scaleHintMin         = new String(this.scaleHintMin);
        }
        if (null != this.scaleHintMax) {
            cloneLayer.scaleHintMax         = new String(this.scaleHintMax);
        }
        if (null != this.attribution) {
            cloneLayer.attribution          = (Attribution)this.attribution.clone();
            cloneLayer.attribution.setLayer(cloneLayer);
        }
        if (null != this.latLonBoundingBox) {
            cloneLayer.latLonBoundingBox    = (LatLonBoundingBox)this.latLonBoundingBox.clone();
        }
        if (null != this.dimensions) {
            cloneLayer.dimensions           = new HashSet();
            Iterator it = this.dimensions.iterator();
            while (it.hasNext()) {
                Dimensions dim = (Dimensions)((Dimensions)it.next()).clone();
                dim.setLayer(cloneLayer);
                cloneLayer.dimensions.add(dim);
            }
        }
        if (null != this.layerKeywordList) {
            cloneLayer.layerKeywordList     = new HashSet(this.layerKeywordList);
        }
        if (null != this.styles) {
            cloneLayer.styles               = new HashSet();
            Iterator it = this.styles.iterator();
            while (it.hasNext()) {
                Style style = (Style)((Style)it.next()).clone();
                style.setLayer(cloneLayer);
                cloneLayer.styles.add(style);
            }
        }
        if (null != this.domainResource) {
            cloneLayer.domainResource       = new HashSet(this.domainResource);
            Iterator it = this.domainResource.iterator();
            while (it.hasNext()) {
                LayerDomainResource ldr = (LayerDomainResource)((LayerDomainResource)it.next()).clone();
                ldr.setLayer(cloneLayer);
                cloneLayer.domainResource.add(ldr);
            }
        }
        if (null != this.identifiers) {
            cloneLayer.identifiers          = new HashSet(this.identifiers);
            Iterator it = this.identifiers.iterator();
            while (it.hasNext()) {
                Identifier identifier = (Identifier)((Identifier)it.next()).clone();
                identifier.setLayer(cloneLayer);
                cloneLayer.identifiers.add(identifier);
            }
        }
        if (null != this.organizationLayers) {
            cloneLayer.organizationLayers   = new HashSet(this.organizationLayers);
        }
        if (null != this.srs) {
            cloneLayer.srs                  = new HashSet(this.srs);
            Iterator it = this.srs.iterator();
            while (it.hasNext()) {
                SRS s = (SRS)((SRS)it.next()).clone();
                s.setLayer(cloneLayer);
                cloneLayer.srs.add(s);
            }
        }
        if (null != this.layers) {
            cloneLayer.layers               = new HashSet();
            Iterator it = this.layers.iterator();
            while (it.hasNext()) {
                Layer layer = (Layer)((Layer)it.next()).clone();
                layer.setParent(cloneLayer);
                cloneLayer.layers.add(layer);
            }
        }    
        return cloneLayer;
    }
    
    public String toString(String tabulator) {
    	Iterator it;
    	
    	StringBuilder result = new StringBuilder();
    	final String newLine = System.getProperty("line.separator");
        
        
        result.append(tabulator + "<Layer ");
        if(null != this.getQueryable()) {
        	result.append("queryable=\"" + this.getQueryable() + "\" ");
        }
        if(null != this.getCascaded()) {
        	result.append("cascaded=\"" + this.getCascaded() + "\" ");
        }
        if(null != this.getOpaque()) {
        	result.append("opaque=\"" + this.getOpaque() + "\" ");
        }
        if(null != this.getNosubsets()) {
        	result.append("noSubsets=\"" + this.getNosubsets() + "\" ");
        }
        if(null != this.getFixedWidth()) {
        	result.append("fixedWidth=\"" + this.getFixedWidth() + "\" ");
        }
        if(null != this.getFixedHeight()) {
        	result.append("fixedHeight=\"" + this.getFixedHeight() + "\" ");
        }        
        result.append(">\n");
        
        if(null != this.getName()) {
            result.append(tabulator + "\t<Name>" + this.getId() + "_" + this.getName() + "</Name>\n");
        }
        result.append(tabulator + "\t<Title>" + this.getId() + "_" + this.getTitle() + "</Title>\n");
        if(null != this.getAbstracts()) {
            result.append(tabulator + "\t<Abstract>" + this.getAbstracts() + "</Abstract>\n");
        }
        
        if(null != this.getLayerKeywordList() && this.getLayerKeywordList().size() != 0) {
            result.append(tabulator + "\t<KeywordList>\n");
            it = this.getLayerKeywordList().iterator();
            while (it.hasNext()) {
                    String keyword = (String)it.next();
                result.append(tabulator + "\t\t<Keyword>" + keyword + "</Keyword>\n");
            }
            result.append(tabulator + "\t</KeywordList>\n");
        }
        
        if(null != this.getSrs() && this.getSrs().size() != 0) {
	        it = srs.iterator();
	    	while (it.hasNext()) {
	            SRS s = (SRS)it.next();
	            result.append(s.toString(tabulator + "\t"));
	    	}
        }
        
        if(null != this.getLatLonBoundingBox()) {
        	result.append(this.getLatLonBoundingBox().toString(tabulator + "\t"));
        }
        
        if(null != this.getScaleHintMin() && null != this.getScaleHintMax()) {
	        result.append(tabulator + "\t<ScaleHint min=\"" + this.getScaleHintMin() + 
	                      "\" max=\"" + this.getScaleHintMax() + 
	                      "\"></ScaleHint>\n");
        }
        
        if(null != this.getDimensions() && this.getDimensions().size() != 0) {
	        it = this.getDimensions().iterator();
	    	while (it.hasNext()) {
	            Dimensions dim = (Dimensions)it.next();
	            result.append(dim.toString(tabulator + "\t"));
	    	}
        }
        
        if(null != this.getStyles() && this.getStyles().size() != 0) {        
	        it = styles.iterator();
	    	while (it.hasNext()) {
	    		Style style = (Style)it.next();
	    		result.append(style.toString(tabulator + "\t"));
	    	}
        }
    	
    	if(null != this.getDomainResource() && this.getDomainResource().size() != 0) {  
	    	it = domainResource.iterator();
	    	while (it.hasNext()) {
	    		LayerDomainResource ldr = (LayerDomainResource)it.next();
	    		result.append(ldr.toString(tabulator + "\t"));
	    	}
    	}
    	
    	if(null != this.getIdentifiers() && this.getIdentifiers().size() != 0) {  
	    	it = identifiers.iterator();
	    	while (it.hasNext()) {
	    		Identifier i = (Identifier)it.next();
	    		result.append(i.toString(tabulator + "\t"));
	    	}
    	}
    	
    	if(null != this.getAttribution()) {  
    		result.append(attribution.toString(tabulator + "\t"));
    	}
    	
    	
    	if(null != this.getLayers() && this.getLayers().size() != 0) {  
	    	it = this.getLayers().iterator();
	    	while (it.hasNext()) {
	    		Layer l = (Layer)it.next();
	    		result.append(l.toString(tabulator + "\t"));
	    	}
    	}
    	
    	result.append(tabulator + "</Layer>\n");
    	return result.toString();
    }
    
}