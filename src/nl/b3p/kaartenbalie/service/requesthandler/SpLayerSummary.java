/*
 * B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
 * for authentication/authorization, pricing and usage reporting.
 *
 * Copyright 2006, 2007, 2008 B3Partners BV
 * 
 * This file is part of B3P Kaartenbalie.
 * 
 * B3P Kaartenbalie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * B3P Kaartenbalie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.b3p.kaartenbalie.service.requesthandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import nl.b3p.ogc.wfs.v110.WfsLayer;
import nl.b3p.ogc.wfs.v110.WfsServiceProvider;
import nl.b3p.wms.capabilities.ServiceProvider;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.Style;

/**
 *
 * @author Chris
 */
public class SpLayerSummary {

    private Integer serviceproviderId = null;
    private Integer layerId = null;
    private String layerName = null;
    private String spUrl = null;
    private String spAbbr = null;
    private String username = null;
    private String password = null;
    private String queryable = null;
    private List<String> layers = null;
    private HashMap<String,Set<Style>> styles=null;

    public SpLayerSummary(
            Integer serviceproviderId,
            Integer layerId,
            String layerName,
            String spUrl,
            String spAbbr,
            String queryable, Set<Style> styles) {
        this(serviceproviderId,layerId,layerName,spUrl,spAbbr,queryable,styles,null,null);
    }
    
    public SpLayerSummary(
            Integer serviceproviderId,
            Integer layerId,
            String layerName,
            String spUrl,
            String spAbbr,
            String queryable, Set<Style> styles,
            String username,
            String password) {
        this.serviceproviderId = serviceproviderId;
        this.layerId = layerId;
        this.layerName = layerName;
        this.spUrl = spUrl;
        this.spAbbr = spAbbr;
        this.queryable = queryable;
        this.addStyles(layerName,styles);
        this.username   = username;
        this.password   = password;
    }

    public SpLayerSummary(Layer l, String queryable) {
        this(l.getServiceProvider().getId(),
                l.getId(),
                l.getName(),
                l.getServiceProvider().getUrl(),
                l.getServiceProvider().getAbbr(),
                queryable,l.getStyles(),l.getServiceProvider().getUserName(),l.getServiceProvider().getPassword());
        //add styles.
        /*if (l.getStyles()!=null){
            Set styles= l.getStyles();
            ArrayList<Style> clonedStyles= new ArrayList<Style>();        
            Iterator<Style> it=styles.iterator();
            while(it.hasNext()){
                clonedStyles.add((Style) it.next().clone());
            }
            this.styles=clonedStyles;
        }*/
        
        
    }

    public SpLayerSummary(WfsLayer l, String queryable) {
        this(l.getWfsServiceProvider().getId(),
                l.getId(),
                l.getName(),
                l.getWfsServiceProvider().getUrl(),
                l.getWfsServiceProvider().getAbbr(),
                queryable,null,l.getWfsServiceProvider().getUsername(),l.getWfsServiceProvider().getPassword());
    }
    
    public SpLayerSummary(WfsLayer l, String queryable,ServiceProvider sp) {
        this(l,queryable);
        
        setServiceProvider(sp);
    }
    
    public SpLayerSummary(WfsLayer l, String queryable,WfsServiceProvider sp) {
        this(l,queryable);
        
        setServiceProvider(sp);
    }
    
    public SpLayerSummary(Layer l, String queryable,ServiceProvider sp) {
        this(l,queryable);
        
        setServiceProvider(sp);
    }
    
    public SpLayerSummary(Layer l, String queryable,WfsServiceProvider sp) {
        this(l,queryable);
        
        setServiceProvider(sp);
    }

    public SpLayerSummary() {
    }

    public Integer getServiceproviderId() {
        return serviceproviderId;
    }

    public void setServiceproviderId(Integer serviceproviderId) {
        this.serviceproviderId = serviceproviderId;
    }

    public Integer getLayerId() {
        return layerId;
    }

    public void setLayerId(Integer layerId) {
        this.layerId = layerId;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public String getSpUrl() {
        return spUrl;
    }

    public void setSpUrl(String spUrl) {
        this.spUrl = spUrl;
    }

    public String getSpAbbr() {
        return spAbbr;
    }
    
    public void setServiceProvider(WfsServiceProvider sp){
        this.username   = sp.getUsername();
        this.password   = sp.getPassword();
    }
    
    public void setServiceProvider(ServiceProvider sp){
        if( sp == null )    return;
        this.username   = sp.getUserName();
        this.password   = sp.getPassword();
    }
    
    public String getUsername(){
        return username;
    }
    
    public String getPassword(){
        return password;
    }

    public void setSpAbbr(String spAbbr) {
        this.spAbbr = spAbbr;
    }

    public List<String> getLayers() {
        return layers;
    }

    public void setLayers(List<String> layers) {
        this.layers = layers;
    }

    public void addLayer(String layerName) {
        if (layers == null) {
            layers = new ArrayList<String>();
        }
        layers.add(layerName);
    }

    public String getLayersAsString() {
        if (layers == null) {
            return null;
        }
        StringBuffer ll = new StringBuffer();
        Iterator it = layers.iterator();
        while (it.hasNext()) {
            String l = (String) it.next();
            if (ll.length() != 0) {
                ll.append(",");
            }
            ll.append(l);
        }
        return ll.toString();
    }

    public String getQueryable() {
        return queryable;
    }

    public void setQueryable(String queryable) {
        this.queryable = queryable;
    }

    public Set<Style> getStyles(String layerName){
        if (this.styles==null)
            return null;
        return this.styles.get(layerName);
    }

    public void setStyles(String layerName,Set<Style> styles) {
        if(this.styles==null){
            this.styles=new HashMap<String,Set<Style>>();
        }
        this.styles.put(layerName, styles);
    }
    /**
     * Get the style with the given stylename
     * @return Returns null if no style with the given name is found.
     */
    public Style getStyle(String layerName,String styleName){
        if (styleName==null || this.styles==null || this.styles.get(layerName)==null)
            return null;
        Set<Style> layerStyles = this.styles.get(layerName);
        Iterator<Style> it=layerStyles.iterator();
        while(it.hasNext()){
            Style s= it.next();
            if (styleName.equals(s.getName())){
                return s;
            }
        }
        return null;
    }

    public void addStyles(String layerName, Set<Style> styles) {
        if (this.styles==null){
            this.styles = new HashMap<String,Set<Style>>();
        }
        if (this.styles.get(layerName)==null){
            this.styles.put(layerName, styles);
        }else{
            this.styles.get(layerName).addAll(styles);
        }        
    }
}
