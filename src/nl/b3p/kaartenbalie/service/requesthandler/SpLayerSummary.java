/*
 * SpLayerSummary.java
 *
 * Created on June 28, 2008, 9:48 AM
 */
package nl.b3p.kaartenbalie.service.requesthandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    private String queryable = null;
    private List layers = null;

    public SpLayerSummary(
            Integer serviceproviderId,
            Integer layerId,
            String layerName,
            String spUrl,
            String spAbbr,
            String queryable) {
        this.serviceproviderId = serviceproviderId;
        this.layerId = layerId;
        this.layerName = layerName;
        this.spUrl = spUrl;
        this.spAbbr = spAbbr;
        this.queryable = queryable;
    }
    
    public SpLayerSummary(){};

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

    public void setSpAbbr(String spAbbr) {
        this.spAbbr = spAbbr;
    }

    public List getLayers() {
        return layers;
    }

    public void setLayers(List layers) {
        this.layers = layers;
    }

    public void addLayer(String layerName) {
        if (layers == null) {
            layers = new ArrayList();
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

}
