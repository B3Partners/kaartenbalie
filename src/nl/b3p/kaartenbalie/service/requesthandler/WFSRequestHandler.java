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

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import nl.b3p.kaartenbalie.core.server.accounting.ExtLayerCalculator;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPriceComposition;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPricing;
import nl.b3p.ogc.utils.OGCConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jytte
 */
public abstract class WFSRequestHandler extends OGCRequestHandler {

    private static final Log log = LogFactory.getLog(WFSRequestHandler.class);

    /** Creates a new instance of WFSRequestHandler */
    public WFSRequestHandler() {
    }

    protected LayerPriceComposition calculateLayerPriceComposition(DataWrapper dw, ExtLayerCalculator lc, String spAbbr, String layerName) throws Exception {
        String operation = dw.getOperation();
        if (operation == null) {
            log.error("Operation can not be null");
            throw new Exception("Operation can not be null");
        }
        String projection = dw.getOgcrequest().getParameter(OGCConstants.WFS_PARAM_SRSNAME); // todo klopt dit?
        /* De srs parameter word nu alleen gevult met null. Hier moet misschien nog naar gekeken worden, maar
        nu werk het zo wel. */
        BigDecimal scale = (new BigDecimal(dw.getOgcrequest().calcScale())).setScale(2, BigDecimal.ROUND_HALF_UP);
        int planType = LayerPricing.PAY_PER_REQUEST;
        String service = OGCConstants.WFS_SERVICE_WFS;

        return lc.calculateLayerComplete(spAbbr, layerName, new Date(), projection, scale, new BigDecimal("1"), planType, service, operation);
    }

    protected SpLayerSummary getValidLayerObjects(EntityManager em, String layer, Integer orgId, boolean b3pLayering) throws Exception {
        String query = "select 'true', l.wfsserviceproviderid, l.id, l.name, sp.url, sp.abbr " +
                "from wfs_Layer l, Wfs_ServiceProvider sp, Wfs_OrganizationLayer ol " +
                "where l.id = ol.wfslayerid and " +
                "l.wfsserviceproviderid = sp.id and " +
                "ol.organizationid = :orgId and " +
                "l.name = :layerName and " +
                "sp.abbr = :layerCode";

        return getValidLayerObjects(em, query, layer, orgId, b3pLayering);
    }

    protected String[] getOrganisationLayers(EntityManager em, Integer orgId, String version, boolean isAdmin) throws Exception {
        List sqlQuery = new ArrayList();
        if (isAdmin == false) {
            String query = "select sp.abbr, l.name " +
                    "from wfs_Layer l, Wfs_ServiceProvider sp, Wfs_OrganizationLayer o " +
                    "where o.organizationid = :orgId and l.id = o.wfslayerid and " +
                    "l.wfsserviceproviderid = sp.id and " +
                    "sp.wfsversion = :version";
            sqlQuery = em.createNativeQuery(query).
                    setParameter("orgId", orgId).
                    setParameter("version", version).
                    getResultList();
        } else {
            String query = "select sp.abbr, l.name " +
                    "from wfs_Layer l, Wfs_ServiceProvider sp " +
                    "where l.wfsserviceproviderid = sp.id and " +
                    "sp.wfsversion = :version";
            sqlQuery = em.createNativeQuery(query).
                    setParameter("version", version).
                    getResultList();
        }
        if (sqlQuery == null) {
            return null;
        }
        int size = sqlQuery.size();
        String[] layerNames = new String[size];
        for (int i = 0; i < size; i++) {
            String spAbbr = (String) ((Object[]) sqlQuery.get(i))[0];
            String lName = (String) ((Object[]) sqlQuery.get(i))[1];
            layerNames[i] = spAbbr + "_" + lName;
        }
        return layerNames;
    }
}
