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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import nl.b3p.kaartenbalie.core.server.accounting.ExtLayerCalculator;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPriceComposition;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPricing;
import nl.b3p.kaartenbalie.core.server.monitoring.DataMonitoring;
import nl.b3p.kaartenbalie.core.server.monitoring.ServiceProviderRequest;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.ogc.wfs.v110.WfsLayer;
import nl.b3p.ogc.wfs.v110.WfsServiceProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jytte
 */
public abstract class WFSRequestHandler extends OGCRequestHandler {

    private static final Log log = LogFactory.getLog(WFSRequestHandler.class);

    /**
     * Creates a new instance of WFSRequestHandler
     */
    public WFSRequestHandler() {
    }

    protected LayerPriceComposition calculateLayerPriceComposition(DataWrapper dw, ExtLayerCalculator lc, String spAbbr, String layerName) throws Exception {
        String operation = dw.getOperation();
        if (operation == null) {
            log.error("Operation can not be null");
            throw new Exception("Operation can not be null");
        }
        String projection = dw.getOgcrequest().getParameter(OGCConstants.WFS_PARAM_SRSNAME); // todo klopt dit?
        /*
         * De srs parameter word nu alleen gevult met null. Hier moet misschien
         * nog naar gekeken worden, maar nu werk het zo wel.
         */
        BigDecimal scale = (new BigDecimal(dw.getOgcrequest().calcScale())).setScale(2, BigDecimal.ROUND_HALF_UP);
        int planType = LayerPricing.PAY_PER_REQUEST;
        String service = OGCConstants.WFS_SERVICE_WFS;

        return lc.calculateLayerComplete(spAbbr, layerName, new Date(), projection, scale, new BigDecimal("1"), planType, service, operation);
    }

    protected SpLayerSummary getValidLayerObjects(EntityManager em, String layer, Integer[] orgIds, boolean b3pLayering) throws Exception {
        String query = "select distinct new "
                + "nl.b3p.kaartenbalie.service.requesthandler.SpLayerSummary(l, 'true',sp) "
                + "from WfsLayer l, Organization o, WfsServiceProvider sp join o.wfsLayers ol "
                + "where l = ol and "
                + "l.wfsServiceProvider = sp and "
                + "o.id in (:orgIds) and "
                + "l.name = :layerName and "
                + "sp.abbr = :layerCode and "
                + "sp.allowed = true";

        return getValidLayerObjects(em, query, layer, orgIds, b3pLayering);
    }

    protected String[] getOrganisationLayers(EntityManager em, Integer[] orgIds, String version, boolean isAdmin) throws Exception {
        List layers = null;
        if (!isAdmin) {
            String query = "select distinct sp.abbr || '_' || l.name "
                    + "from Organization o "
                    + "join o.wfsLayers l "
                    + "join l.wfsServiceProvider sp "
                    + "where o.id in (:orgIds) "
                    + "and sp.allowed = true";
            if (version != null) {
                query += " and sp.wfsVersion = :version";
            }

            Query q = em.createQuery(query);

            if (orgIds != null) {
                List lijst = Arrays.asList(orgIds);
                q.setParameter("orgIds", lijst);
            } else {
                q.setParameter("orgIds", null);
            }

            if (version != null) {
                q.setParameter("version", version);
            }
            layers = q.getResultList();
        } else {
            String query = "select sp.abbr || '_' || l.name "
                    + "from WfsLayer l "
                    + "join l.wfsServiceProvider sp";
            if (version != null) {
                query += " where sp.wfsVersion = :version";
            }

            Query q = em.createQuery(query);
            if (version != null) {
                q.setParameter("version", version);
            }
            layers = q.getResultList();
        }
        return (String[]) layers.toArray(new String[]{});
    }

    /**
     * Get version from ogcrequest otherwise return 1.1.0.
     *
     * @param ogcrequest
     * @return
     */
    protected String getVersion(OGCRequest ogcrequest) {
        String finalVersion = ogcrequest.getFinalVersion();
        String version = "";
        if (OGCConstants.WFS_VERSION_100.equals(finalVersion) || OGCConstants.WFS_VERSION_110.equals(finalVersion)) {
            version = ogcrequest.getFinalVersion();
        } else {
            version = null;
        }

        return version;
    }

    /**
     * Create a URI from the ogcrequest with provided URL, by adding the request
     * parameters.
     *
     * @param ogcrequest
     * @param lurl
     * @param version
     * @return
     */
    protected String createUriString(OGCRequest ogcrequest, String lurl,
            String version) {
        StringBuffer url = new StringBuffer(lurl);
        if (!lurl.endsWith("?")) {
            url.append("?");
        }
        String[] params = ogcrequest.getParametersArray();
        for (int i = 0; i < params.length; i++) {
            String key = params[i].split("=")[0];
            if (key.equalsIgnoreCase(OGCRequest.VERSION)) {
                if (version != null) {// if version not given exclude from params
                    url.append(OGCRequest.VERSION);
                    url.append("=");
                    url.append(version);
                }
            } else {
                url.append(params[i]);
            }
            url.append("&");
        }
        return url.toString();
    }

    /**
     * Create a ServiceProviderRequest for WFS requests.
     *
     * @param data
     * @param url
     * @param spId
     * @param bytesSent
     * @return
     */
    protected ServiceProviderRequest createServiceProviderRequest(
            DataWrapper data, String url,
            Integer spId, Long bytesSent) {

        DataMonitoring rr = data.getRequestReporting();
        OGCRequest ogcrequest = data.getOgcrequest();

        ServiceProviderRequest wfsRequest = new ServiceProviderRequest();
        wfsRequest.setMsSinceRequestStart(new Long(rr.getMSSinceStart()));
        wfsRequest.setServiceProviderId(spId);
        String version = this.getVersion(ogcrequest);
        String uri = createUriString(ogcrequest, url, version);
        wfsRequest.setProviderRequestURI(uri);
        wfsRequest.setWmsVersion(version);
        wfsRequest.setBytesSent(bytesSent);

        return wfsRequest;
    }

    protected List getLayerSummaries(String[] layers) throws Exception {
        return getLayerSummaries(layers, null);
    }

    protected List getLayerSummaries(String[] layers, String serviceName) throws Exception {
        EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);

        List spList = new ArrayList();
        for (int i = 0; i < layers.length; i++) {
            String layer = layers[i];

            String[] layerAndCode = toCodeAndName(layer);
            String abbr = layerAndCode[0];
            String name = layerAndCode[1];

            if (serviceName != null && !serviceName.isEmpty()) {
                abbr = serviceName;
                name = layer;
            }

            List matchingLayers = em.createQuery("from WfsLayer l where l.name = :name and l.wfsServiceProvider.abbr = :abbr").setParameter("name", name).setParameter("abbr", abbr).getResultList();

            if (matchingLayers.isEmpty()) {
                /*
                 * XXX "or no rights" ?? No rights are checked...
                 */
                log.error("layer not found: " + layer);
                throw new Exception(KBConfiguration.REQUEST_NORIGHTS_EXCEPTION + ": " + layer);
            }

            if (matchingLayers.size() > 1) {
                log.error("layers with duplicate names, name: " + layer);
                throw new Exception(KBConfiguration.GETMAP_EXCEPTION);
            }

            WfsLayer l = (WfsLayer) matchingLayers.get(0);
            spList.add(new SpLayerSummary(l, "true"));
        }
        return spList;
    }

    static protected String cleanPrefixInBody(String body, String prefix, String nsUrl, String ns) {
        String old = "";
        if (nsUrl != null) {
            old += nsUrl;
        }
        if (prefix != null) {
            old += prefix;
        }
        if (old.length() == 0) {
            return body;
        }
        String nsnew = "";
        if (ns != null) {
            nsnew += ns;
        }
        StringBuffer bBody = new StringBuffer(body);
        for (int start = bBody.indexOf(old); start >= 0;) {
            bBody.replace(start, start + old.length(), nsnew);
        }
        return bBody.toString();
    }
}
