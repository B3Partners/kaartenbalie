package nl.b3p.kaartenbalie.service.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.service.requesthandler.SpLayerSummary;
import nl.b3p.ogc.utils.KBConfiguration;

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
	 * @param orgId - id of the organization a user belongs to
	 * @param version - null to retrieve all layers for all versions
	 * @param isAdmin - true if the user is administrator
	 * @return
	 * @throws Exception
	 */
    public String[] getAuthorizedFeatureTypeNames(Integer orgId, String version, boolean isAdmin) throws Exception {
        List layers = null;
        if(!isAdmin) {
            String query = "select sp.abbr || '_' || l.name " +
                           "from Organization o " +
                           "join o.wfsLayers l " +
                           "join l.wfsServiceProvider sp " +
                           "where o.id = :orgId";
            if (version!=null)
                query+=" and sp.wfsVersion = :version";

            Query q = em.createQuery(query);
            q.setParameter("orgId", orgId);
            if (version!=null)
                q.setParameter("version", version);
            layers = q.getResultList();
        } else {
            String query = "select sp.abbr || '_' || l.name " +
                           "from WfsLayer l " +
                           "join l.wfsServiceProvider sp";
            if (version!=null)
                query+=" where sp.wfsVersion = :version";

             Query q= em.createQuery(query);
             if (version!=null)
                q.setParameter("version", version);
             layers=q.getResultList();
        }
        return (String[])layers.toArray(new String[] {});
    }

    
    public SpLayerSummary getAuthorizedFeatureTypeSummary(String layer, Integer orgId, boolean b3pLayering) throws Exception {
        String query = "select new " +
                "nl.b3p.kaartenbalie.service.requesthandler.SpLayerSummary(l, 'true') " +
                "from WfsLayer l, Organization o, WfsServiceProvider sp join o.wfsLayers ol " +
                "where l = ol and " +
                "l.wfsServiceProvider = sp and " +
                "o.id = :orgId and " +
                "l.name = :layerName and " +
                "sp.abbr = :layerCode";

        return getValidLayerObjects(query, layer, orgId, b3pLayering);
    }
    
    /**
     * methode die alleen een query nodig heeft om bestaan en rechten van layer
     * te bepalen.
     * <P>
     * @param em EntityManager
     * @param query query waarmee rechten en bestaan gecheckt kan worden
     * @param layer layer naam met sp abbrr (abbr_layer)
     * @param orgId organisatie id
     * @param b3pLayering bepaalt of service layers toegevoegd worden, true = alleen service layers,
     * false = alleen echte layers.
     * @return object array met resultaat voor gezochte layer
     * @see Object[] getValidLayerObjects(EntityManager em, String query, String layer, Integer orgId, boolean b3pLayering) throws Exception
     * @throws java.lang.Exception indien gezochte layer niet bestaat of er geen rechten op zijn
     */
    protected SpLayerSummary getValidLayerObjects(String query, String layer, Integer orgId, boolean b3pLayering) throws Exception {
        String[] layerCodeAndName = toCodeAndName(layer);
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
                        null);
                return layerInfo;
            }
            return null;
        } else if (layerCode.equals(KBConfiguration.SERVICEPROVIDER_BASE_ABBR)) {
            return null;
        }

        List result = em.createQuery(query).
                setParameter("orgId", orgId).
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
    
    
    //TODO: this looks like a generic utility method
    /**
     * methode splitst lange layer naam volgens abbr_layer in een service provider
     * deel (layerCode genoemd) en een echte layer naam (layerName)
     * <p>
     * @param completeLayerName lange layer naam
     * @return straing array met 2 strings: abbr en layer
     * @throws java.lang.Exception fout in format lange layer naam
     */
    protected String[] toCodeAndName(String completeLayerName) throws Exception {
        // Check of layers[i] juiste format heeft
        int pos = completeLayerName.indexOf("_");
        if (pos == -1 || completeLayerName.length() <= pos + 1) {
            log.error("layer not valid: " + completeLayerName);
            throw new Exception(KBConfiguration.REQUEST_LAYERNAME_EXCEPTION+ ": "+completeLayerName);
        }
        String layerCode = completeLayerName.substring(0, pos);
        String layerName = completeLayerName.substring(pos + 1);
        if (layerCode.length() == 0 || layerName.length() == 0) {
            log.error("layer name or code not valid: " + layerCode + ", " + layerName);
            throw new Exception(KBConfiguration.REQUEST_LAYERNAME_EXCEPTION+ ": "+completeLayerName);
        }
        return new String[]{layerCode, layerName};
    }    
}
