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
import java.util.Iterator;
import java.util.List;
import nl.b3p.ogc.wfs.v110.WfsLayer;
import nl.b3p.wms.capabilities.Layer;

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

    public SpLayerSummary(Layer l, String queryable) {
        this(l.getServiceProvider().getId(),
                l.getId(),
                l.getName(),
                l.getServiceProvider().getUrl(),
                l.getServiceProvider().getAbbr(),
                queryable);
    }

    public SpLayerSummary(WfsLayer l, String queryable) {
        this(l.getWfsServiceProvider().getId(),
                l.getId(),
                l.getName(),
                l.getWfsServiceProvider().getUrl(),
                l.getWfsServiceProvider().getAbbr(),
                queryable);
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
