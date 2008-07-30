/*
 * WFSRequestHandler.java
 *
 * Created on June 10, 2008, 9:48 AM
 *
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
        BigDecimal scale = new BigDecimal(dw.getOgcrequest().calcScale());
        int planType = LayerPricing.PAY_PER_REQUEST;
        String service = OGCConstants.WFS_SERVICE_WFS;
        
        return lc.calculateLayerComplete(spAbbr, layerName, new Date(), projection, scale, new BigDecimal(1), planType, service, operation);
    }
    
    protected SpLayerSummary getValidLayerObjects(EntityManager em, String layer, Integer orgId, boolean b3pLayering) throws Exception {
        String query = "select 'true', l.wfsserviceproviderid, l.wfslayerid, l.name, sp.url, sp.abbr " +
                "from wfs_Layer l, Wfs_ServiceProvider sp, Wfs_OrganizationLayer ol " +
                "where l.wfslayerid = ol.wfslayerid and " +
                "l.wfsserviceproviderid = sp.wfsserviceproviderid and " +
                "ol.organizationid = :orgId and " +
                "l.name = :layerName and " +
                "sp.abbr = :layerCode";
        
        return getValidLayerObjects(em, query, layer, orgId, b3pLayering);
    }
    
    protected String[] getOrganisationLayers(EntityManager em, Integer orgId, String version, boolean isAdmin) throws Exception {
        List sqlQuery = new ArrayList();
        if(isAdmin == false){
            String query = "select sp.abbr, l.name " +
                    "from wfs_Layer l, Wfs_ServiceProvider sp, Wfs_OrganizationLayer o " +
                    "where o.organizationid = :orgId and l.wfslayerid = o.wfslayerid and " +
                    "l.wfsserviceproviderid = sp.wfsserviceproviderid and " +
                    "sp.wfsversion = :version";
            sqlQuery = em.createNativeQuery(query).
                    setParameter("orgId", orgId).
                    setParameter("version", version).
                    getResultList();
        }
        else{
            String query = "select sp.abbr, l.name " +
                    "from wfs_Layer l, Wfs_ServiceProvider sp " +
                    "where l.wfsserviceproviderid = sp.wfsserviceproviderid and " +
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
