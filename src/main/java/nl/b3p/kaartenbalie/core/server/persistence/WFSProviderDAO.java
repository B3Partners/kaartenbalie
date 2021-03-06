package nl.b3p.kaartenbalie.core.server.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import nl.b3p.ogc.utils.SpLayerSummary;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.OGCCommunication;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WFSProviderDAO extends BaseDAO {
	
	private static final Log log = LogFactory.getLog(WFSProviderDAO.class);

	/**
	 * Instantiates a WFSProviderDao object with the main entity manager.
	 */
	public WFSProviderDAO(){
		try {
		   this.em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);
		} catch(Exception e){
			log.error("WfsProviderDao initialization failed.");
		}
	}
	
	public WFSProviderDAO(EntityManager em){
		this.em = em;
	}
	
	/**
	 * Get all TypeNames of WFS services that a user is authorized for.
	 * 
	 * @param orgIds - ids of the organizations a user belongs to
	 * @param version - null to retrieve all layers for all versions
	 * @param isAdmin - true if the user is administrator
	 * @return
	 * @throws Exception
	 */
    public String[] getAuthorizedFeatureTypeNames(Integer[] orgIds, String version, boolean isAdmin) throws Exception {
        List<SpLayerSummary> spLayers = null;
        if(!isAdmin) {
            String query = "select distinct new "
                    + "nl.b3p.ogc.utils.SpLayerSummary(l, 'true',sp) "
                    + "from Organization o "
                    + "join o.wfsLayers l "
                    + "join l.wfsServiceProvider sp "
                    + "where o.id in (:orgIds)";
            if (version!=null)
                query+=" and sp.wfsVersion = :version";

            Query q = em.createQuery(query);
            q.setParameter("orgIds", Arrays.asList(orgIds));
            if (version!=null)
                q.setParameter("version", version);
            spLayers = q.getResultList();
        } else {
            String query = "select distinct new "
                    + "nl.b3p.ogc.utils.SpLayerSummary(l, 'true',sp) "
                    + "from WfsLayer l "
                    + "join l.wfsServiceProvider sp";
            if (version!=null)
                query+=" where sp.wfsVersion = :version";

             Query q= em.createQuery(query);
             if (version!=null)
                q.setParameter("version", version);
             spLayers=q.getResultList();
        }
        
        List<String> layers = new ArrayList<String>();
        for (SpLayerSummary spls : spLayers) {
            String ln = OGCCommunication.attachSp(spls.getSpAbbr(), spls.getLayerName());
            layers.add(ln);
        }
        return (String[])layers.toArray(new String[] {});
    }

    
    public SpLayerSummary getAuthorizedFeatureTypeSummary(String layer, Integer[] orgIds, boolean b3pLayering) throws Exception {
        String query = "select distinct new " +
                "nl.b3p.ogc.utils.SpLayerSummary(l, 'true') " +
                "from WfsLayer l, Organization o, WfsServiceProvider sp join o.wfsLayers ol " +
                "where l = ol and " +
                "l.wfsServiceProvider = sp and " +
                "o.id in (:orgIds) and " +
                "l.name = :layerName and " +
                "sp.abbr = :layerCode";

        return getValidLayerObjects(query, layer, orgIds, b3pLayering);
    }
    
    /**
     * methode die alleen een query nodig heeft om bestaan en rechten van layer
     * te bepalen.
     * <P>
     * @param em EntityManager
     * @param query query waarmee rechten en bestaan gecheckt kan worden
     * @param layer layer naam met sp abbrr (abbr_layer)
     * @param orgIds organisatie id's
     * @param b3pLayering bepaalt of service layers toegevoegd worden, true = alleen service layers,
     * false = alleen echte layers.
     * @return object array met resultaat voor gezochte layer
     * @see Object[] getValidLayerObjects(EntityManager em, String query, String layer, Integer orgId, boolean b3pLayering) throws Exception
     * @throws java.lang.Exception indien gezochte layer niet bestaat of er geen rechten op zijn
     */
    protected SpLayerSummary getValidLayerObjects(String query, String layer, Integer[] orgIds, boolean b3pLayering) throws Exception {
        String[] layerCodeAndName = OGCCommunication.toCodeAndName(layer);
        String layerCode = layerCodeAndName[0];
        String layerName = layerCodeAndName[1];

        log.debug("Collect layer info for layer: " + layerName + " and service provider: " + layerCode);

        if (b3pLayering) {
            if (layerCode.equals(KBConfiguration.SERVICEPROVIDER_BASE_ABBR)) {
                SpLayerSummary layerInfo = new SpLayerSummary(new Integer(-1),
                        new Integer(-1),
                        layerName,
                        KBConfiguration.SERVICEPROVIDER_BASE_HTTP,
                        KBConfiguration.SERVICEPROVIDER_BASE_ABBR,
                        null,null);
                return layerInfo;
            }
            return null;
        } else if (layerCode.equals(KBConfiguration.SERVICEPROVIDER_BASE_ABBR)) {
            return null;
        }

        List result = em.createQuery(query).
                setParameter("orgIds", Arrays.asList(orgIds)).
                setParameter("layerName", layerName).
                setParameter("layerCode", layerCode).
                getResultList();
        if (result == null || result.isEmpty()) {
            log.error("layer not valid or no rights, name: " + layer);
            throw new Exception(KBConfiguration.REQUEST_NORIGHTS_EXCEPTION+": "+layer);
        } else if (result.size() > 1) {
            log.error("layers with duplicate names, name: " + layer);
            throw new Exception(KBConfiguration.REQUEST_DUPLICATE_EXCEPTION+": "+layer);
        }

        return (SpLayerSummary) result.get(0);
    }
}
