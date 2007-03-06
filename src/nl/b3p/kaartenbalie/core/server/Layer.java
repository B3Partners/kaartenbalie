/**
 * @(#)Layer.java
 * @author N. de Goeij
 * @version 1.00 2006/10/11
 *
 * Purpose: Bean representing a Layer.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.core.server;

import java.io.ByteArrayOutputStream;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class Layer implements XMLElement {
    
    private static final Log log = LogFactory.getLog(Layer.class);
    
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
    private Set <Dimensions> dimensions;
    private Set <String> layerKeywordList;
    private Set <Style> styles;
    private Set <LayerDomainResource >domainResource;
    private Set <Identifier> identifiers;
    private Set <OrganizationLayer> organizationLayers;
    private Set <SRS> srs;
    private Set <Layer> layers;
    
    // <editor-fold defaultstate="collapsed" desc="getter and setter methods.">
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
    
    public void setDimensions(Set <Dimensions> dimensions) {
        this.dimensions = dimensions;
    }
    
    public void addDimension(Dimensions dimension) {
        if(dimensions == null) {
            dimensions = new HashSet <Dimensions>();
        }
        dimensions.add(dimension);
        dimension.setLayer(this);
    }
    
    public Set getLayerKeywordList() {
        return layerKeywordList;
    }
    
    public void setLayerKeywordList(Set <String> layerKeywordList) {
        this.layerKeywordList = layerKeywordList;
    }
    
    public void addKeyword(String keyword) {
        if(layerKeywordList == null) {
            layerKeywordList = new HashSet <String>();
        }
        layerKeywordList.add(keyword);
    }
    
    public Set getStyles() {
        return styles;
    }
    
    public void setStyles(Set <Style> styles) {
        this.styles = styles;
    }
    
    public void addStyle(Style style) {
        if (null == styles) {
            styles = new HashSet <Style>();
        }
        styles.add(style);
        style.setLayer(this);
    }
    
    public Set getDomainResource() {
        return domainResource;
    }
    
    public void setDomainResource(Set <LayerDomainResource> domainResource) {
        this.domainResource = domainResource;
    }
    
    public void addDomainResource(LayerDomainResource dr) {
        if(null == domainResource) {
            domainResource = new HashSet <LayerDomainResource>();
        }
        domainResource.add(dr);
        dr.setLayer(this);
    }
    
    public Set getIdentifiers() {
        return identifiers;
    }
    
    public void setIdentifiers(Set <Identifier> identifiers) {
        this.identifiers = identifiers;
    }
    
    public void addIdentifier(Identifier identifier) {
        if(null == identifiers) {
            identifiers = new HashSet <Identifier>();
        }
        identifiers.add(identifier);
        identifier.setLayer(this);
    }
    
    public Set getOrganizationLayers() {
        return organizationLayers;
    }
    
    public void setOrganizationLayers(Set <OrganizationLayer> organizationLayers) {
        this.organizationLayers = organizationLayers;
    }
    
    public Set getSrs() {
        return srs;
    }
    
    public void setSrs(Set <SRS> srs) {
        this.srs = srs;
    }
    
    public void addSrs(SRS s) {
        if(null == srs) {
            srs = new HashSet <SRS>();
        }
        srs.add(s);
        s.setLayer(this);
    }
    
    public Set <Layer> getLayers() {
        return layers;
    }
    
    public void setLayers(Set <Layer> layers) {
        this.layers = layers;
    }
    
    public void addLayer(Layer layer) {
        if (null == layers) {
            layers = new HashSet <Layer>();
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
    
    public String getUniqueName(){
        return ""+this.getId()+"_"+this.getName();
    }
    // </editor-fold>
    
    /** Method that will perfom a shallow clone of the given object into this object.
     *
     * @param l Layer objectfrom which the information has to be cloned.
     */
    // <editor-fold defaultstate="collapsed" desc="shallowClone(Layer l) method">
    public void shallowClone(Layer l) {
        this.id = l.id;
        this.title = l.title;
    }
    // </editor-fold>
    
    /** Method that will overwrite the URL's stored in the database with the URL specified for Kaartenbalie.
     * This new URL indicate the link to the kaartenbalie, while the old link is used to indicate the URL
     * to the real location of the service. Because the client which is connected to kaartenbalie has to send
     * his requests back to kaartenbalie and not directly to the official resource, the URL has to be replaced.
     *
     * @param newUrl String representing the URL the old URL has to be replaced with.
     */
    // <editor-fold defaultstate="collapsed" desc="overwriteURL(String newUrl) method">
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
    // </editor-fold>
    
    /** Method that will create a deep copy of this object.
     *
     * @return an object of type Object
     */
    // <editor-fold defaultstate="collapsed" desc="clone() method">
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
            cloneLayer.dimensions           = new HashSet <Dimensions>();
            Iterator it = this.dimensions.iterator();
            while (it.hasNext()) {
                Dimensions dim = (Dimensions)((Dimensions)it.next()).clone();
                dim.setLayer(cloneLayer);
                cloneLayer.dimensions.add(dim);
            }
        }
        if (null != this.layerKeywordList) {
            cloneLayer.layerKeywordList     = new HashSet <String>(this.layerKeywordList);
        }
        if (null != this.styles) {
            cloneLayer.styles               = new HashSet <Style>();
            Iterator it = this.styles.iterator();
            while (it.hasNext()) {
                Style style = (Style)((Style)it.next()).clone();
                style.setLayer(cloneLayer);
                cloneLayer.styles.add(style);
            }
        }
        if (null != this.domainResource) {
            cloneLayer.domainResource       = new HashSet <LayerDomainResource>(this.domainResource);
            Iterator it = this.domainResource.iterator();
            while (it.hasNext()) {
                LayerDomainResource ldr = (LayerDomainResource)((LayerDomainResource)it.next()).clone();
                ldr.setLayer(cloneLayer);
                cloneLayer.domainResource.add(ldr);
            }
        }
        if (null != this.identifiers) {
            cloneLayer.identifiers          = new HashSet <Identifier>(this.identifiers);
            Iterator it = this.identifiers.iterator();
            while (it.hasNext()) {
                Identifier identifier = (Identifier)((Identifier)it.next()).clone();
                identifier.setLayer(cloneLayer);
                cloneLayer.identifiers.add(identifier);
            }
        }
        if (null != this.organizationLayers) {
            cloneLayer.organizationLayers   = new HashSet <OrganizationLayer>(this.organizationLayers);
        }
        if (null != this.srs) {
            cloneLayer.srs                  = new HashSet <SRS>(this.srs);
            Iterator it = this.srs.iterator();
            while (it.hasNext()) {
                SRS s = (SRS)((SRS)it.next()).clone();
                s.setLayer(cloneLayer);
                cloneLayer.srs.add(s);
            }
        }
        if (null != this.layers) {
            cloneLayer.layers               = new HashSet <Layer>();
            Iterator it = this.layers.iterator();
            while (it.hasNext()) {
                Layer layer = (Layer)((Layer)it.next()).clone();
                layer.setParent(cloneLayer);
                cloneLayer.layers.add(layer);
            }
        }
        return cloneLayer;
    }
    // </editor-fold>
    
    /** Method that will create piece of the XML tree to create a proper XML docuement.
     *
     * @param doc Document object which is being used to create new Elements
     * @param rootElement The element where this object belongs to.
     *
     * @return an object of type Element
     */
    // <editor-fold defaultstate="collapsed" desc="toElement(Document doc, Element rootElement) method">
    public Element toElement(Document doc, Element rootElement) {
        
        Element layerElement = doc.createElement("Layer");
        
        if(null != this.getQueryable()) {
            layerElement.setAttribute("queryable", this.getQueryable());
        }
        if(null != this.getCascaded()) {
            layerElement.setAttribute("cascaded", this.getCascaded());
        }
        if(null != this.getOpaque()) {
            layerElement.setAttribute("opaque", this.getOpaque());
        }
        if(null != this.getNosubsets()) {
            layerElement.setAttribute("noSubsets", this.getNosubsets());
        }
        if(null != this.getFixedWidth()) {
            layerElement.setAttribute("fixedWidth", this.getFixedWidth());
        }
        if(null != this.getFixedHeight()) {
            layerElement.setAttribute("fixedHeight", this.getFixedHeight());
        }
        
        if(null != this.getName()) {
            Element nameElement = doc.createElement("Name");
            String nname="";
            if (this.getId()!=null){
                nname+=this.getId();
            }if (this.getName()!=null){
                if (nname.length()>0){
                    nname+="_";
                }
                nname+=this.getName();
            }                
            Text text = doc.createTextNode(nname);
            nameElement.appendChild(text);
            layerElement.appendChild(nameElement);
        }
        
        if(null != this.getTitle()) {
            Element titleElement = doc.createElement("Title");
            Text text = doc.createTextNode(this.getId() + "_" + this.getTitle());
            titleElement.appendChild(text);
            layerElement.appendChild(titleElement);
        }
        
        if(null != this.getAbstracts()) {
            Element abstractElement = doc.createElement("Abstract");
            Text text = doc.createTextNode(this.getAbstracts());
            abstractElement.appendChild(text);
            layerElement.appendChild(abstractElement);
        }
        
        if(null != this.getLayerKeywordList() && this.getLayerKeywordList().size() != 0) {
            Element keywordListElement = doc.createElement("KeywordList");
            Iterator it = this.getLayerKeywordList().iterator();
            while (it.hasNext()) {
                String keyword = (String)it.next();
                Element keywordElement = doc.createElement("Keyword");
                Text text = doc.createTextNode(keyword);
                keywordElement.appendChild(text);
                keywordListElement.appendChild(keywordElement);
            }
            layerElement.appendChild(keywordListElement);
        }
        
        
        if(null != this.getSrs() && this.getSrs().size() != 0) {
            Iterator it = srs.iterator();
            while (it.hasNext()) {
                SRS s = (SRS)it.next();
                layerElement = s.toElement(doc, layerElement);
            }
        }
        
        if(null != this.getLatLonBoundingBox()) {
            layerElement = this.getLatLonBoundingBox().toElement(doc, layerElement);
        }
        
        if(null != this.getScaleHintMin() && null != this.getScaleHintMax()) {
            Element scaleHintElement = doc.createElement("ScaleHint");
            scaleHintElement.setAttribute("min", this.getScaleHintMin());
            scaleHintElement.setAttribute("max", this.getScaleHintMax());
            layerElement.appendChild(scaleHintElement);
        }
        
        if(null != this.getDimensions() && this.getDimensions().size() != 0) {
            Iterator it = this.getDimensions().iterator();
            while (it.hasNext()) {
                Dimensions dim = (Dimensions)it.next();
                layerElement = dim.toElement(doc, layerElement);
            }
        }
        
        if(null != this.getStyles() && this.getStyles().size() != 0) {
            Iterator it = styles.iterator();
            while (it.hasNext()) {
                Style style = (Style)it.next();
                layerElement = style.toElement(doc, layerElement);
            }
        }
        
        if(null != this.getDomainResource() && this.getDomainResource().size() != 0) {
            Iterator it = domainResource.iterator();
            while (it.hasNext()) {
                LayerDomainResource ldr = (LayerDomainResource)it.next();
                layerElement = ldr.toElement(doc, layerElement);
            }
        }
        
        if(null != this.getIdentifiers() && this.getIdentifiers().size() != 0) {
            Iterator it = identifiers.iterator();
            while (it.hasNext()) {
                Identifier i = (Identifier)it.next();
                layerElement = i.toElement(doc, layerElement);
            }
        }
        
        if(null != this.getAttribution()) {
            layerElement = this.getAttribution().toElement(doc, layerElement);        }
        
        if(null != this.getLayers() && this.getLayers().size() != 0) {
            Iterator it = this.getLayers().iterator();
            while (it.hasNext()) {
                Layer l = (Layer)it.next();
                layerElement = l.toElement(doc, layerElement);
            }
        }
        
        rootElement.appendChild(layerElement);
        return rootElement;
    }
    // </editor-fold>
}