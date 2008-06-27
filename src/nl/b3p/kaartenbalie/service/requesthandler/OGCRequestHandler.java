/**
 * @(#)OGCRequestHandler.java
 *
 */
package nl.b3p.kaartenbalie.service.requesthandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.accounting.AccountManager;
import nl.b3p.kaartenbalie.core.server.accounting.LayerCalculator;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPriceComposition;
import nl.b3p.kaartenbalie.core.server.accounting.entity.TransactionLayerUsage;
import nl.b3p.kaartenbalie.core.server.b3pLayering.AllowTransactionsLayer;
import nl.b3p.kaartenbalie.core.server.b3pLayering.BalanceLayer;
import nl.b3p.kaartenbalie.core.server.b3pLayering.ConfigLayer;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.ogc.utils.KBConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class OGCRequestHandler implements RequestHandler {

    private static final Log log = LogFactory.getLog(OGCRequestHandler.class);
    protected User user;
    protected String url;
    protected static long maxResponseTime = 100000;

    public OGCRequestHandler() {
    }

    protected abstract Object[] getValidLayerObjects(EntityManager em, String layer, Integer orgId, boolean b3pLayering) throws Exception;

    protected Object[] getValidLayerObjects(EntityManager em, String query, String layer, Integer orgId, boolean b3pLayering) throws Exception {
        String[] layerCodeAndName = toCodeAndName(layer);
        String layerCode = layerCodeAndName[0];
        String layerName = layerCodeAndName[1];
        if (b3pLayering) {
            if (layerCode.equals(KBConfiguration.SERVICEPROVIDER_BASE_ABBR)) {
                return new Object[]{"0", new Integer(-1), new Integer(-1),
                            layerName,
                            KBConfiguration.SERVICEPROVIDER_BASE_HTTP,
                            KBConfiguration.SERVICEPROVIDER_BASE_ABBR
                        };
            }
            return null;
        } else if (layerCode.equals(KBConfiguration.SERVICEPROVIDER_BASE_ABBR)) {
            return null;
        }

        List sqlQuery = em.createNativeQuery(query).
                setParameter("orgId", orgId).
                setParameter("layerName", layerName).
                setParameter("layerCode", layerCode).
                getResultList();
        if (sqlQuery == null || sqlQuery.isEmpty()) {
            log.error("layer not valid or no rights, name: " + layer);
            throw new Exception(KBConfiguration.GETMAP_EXCEPTION);
        } else if (sqlQuery.size() > 1) {
            log.error("layers with duplicate names, name: " + layer);
            throw new Exception(KBConfiguration.GETMAP_EXCEPTION);
        }
        return (Object[]) sqlQuery.get(0);
    }

    protected abstract LayerPriceComposition calculateLayerPriceComposition(DataWrapper dw, LayerCalculator lc, Integer layerId) throws Exception;
    //Nieuwe methode om de urls samen te stellen en gelijk alle rechten te controleren
    //deze methode werkt sneller en efficienter dan de bovenstaande getserviceprovider
    //methode in combinatie met de code voor controle van layer rechten.
    List getSeviceProviderURLS(String[] layers, Integer orgId, boolean checkForQueryable, DataWrapper dw) throws Exception {

        // Per kaartlaag wordt uitgezocht tot welke sp hij hoort,
        // er voldoende rechten zijn voor de kaart en of aan
        // de queryable voorwaarde is voldaan
        List eventualSPList = new ArrayList();

        EntityManager em = MyEMFDatabase.getEntityManager();

        AccountManager am = AccountManager.getAccountManager(orgId);
        TransactionLayerUsage tlu = am.beginTLU();

        LayerCalculator lc = new LayerCalculator();

        Map config = dw.getLayeringParameterMap();
        configB3pLayering(layers, config);
        Boolean bat = (Boolean) config.get(AllowTransactionsLayer.allowTransactions);
        boolean bAllowTransactions = bat == null ? false : bat.booleanValue();

        //eerst geen b3pLayering meenemen
        boolean b3pLayering = false;
        try {
            for (int i = 0; i < layers.length; i++) {
                Object[] objecten = getValidLayerObjects(em, layers[i], orgId, b3pLayering);
                if (objecten == null) {
                    continue;
                }
                String layerQueryable = (String) objecten[0];
                Integer serviceproviderId = (Integer) objecten[1];
                Integer layerId = (Integer) objecten[2];
                String layerName = (String) objecten[3];
                String spUrl = (String) objecten[4];
                String spAbbr = (String) objecten[5];

                // layer toevoegen aan sp indien queryable voorwaarde ok
                if (!checkForQueryable || (checkForQueryable && layerQueryable.equals("1"))) {
                    LayerPriceComposition lpc = calculateLayerPriceComposition(dw, lc, layerId);
                    if (lpc != null) {
                        tlu.registerUsage(lpc);
                        // Only add layer when free or when transactions allowed
                        Boolean isFree = lpc.getLayerIsFree();
                        if ((isFree == null || (isFree != null && !isFree.booleanValue())) && !bAllowTransactions) {
                            continue;
                        }
                    }
                    addToServerProviderList(eventualSPList, serviceproviderId, layerId, layerName, spUrl, spAbbr);
                }
            }
        } finally {
            lc.closeEntityManager();
        }

        //als laatste b3pLayering meenemen
        b3pLayering = true;
        for (int i = 0; i < layers.length; i++) {
            Object[] objecten = getValidLayerObjects(em, layers[i], orgId, b3pLayering);
            if (objecten == null) {
                continue;
            }
            Integer serviceproviderId = (Integer) objecten[1];
            Integer layerId = (Integer) objecten[2];
            String layerName = (String) objecten[3];
            String spUrl = (String) objecten[4];
            String spAbbr = (String) objecten[5];
            if (AllowTransactionsLayer.NAME.equalsIgnoreCase(layerName)) {
                config.put(AllowTransactionsLayer.foundAllowTransactionsLayer, new Boolean(true));
            }
            addToServerProviderList(eventualSPList, serviceproviderId, layerId, layerName, spUrl, spAbbr);
        }

        Boolean bfat = (Boolean) config.get(AllowTransactionsLayer.foundAllowTransactionsLayer);
        boolean bFoundAllowTransactionsLayer = bfat == null ? false : bfat.booleanValue();
        if (AccountManager.isEnableAccounting() && tlu.getCreditAlteration().doubleValue() > 0) {
            config.put(AllowTransactionsLayer.creditMutation, tlu.getCreditAlteration());
            config.put(AllowTransactionsLayer.pricedLayers, tlu.getPricedLayerNames());
            config.put(AllowTransactionsLayer.transactionsNeeded, new Boolean(true));
            if (!bAllowTransactions) {
                am.endTLU();
            }
            if (!bFoundAllowTransactionsLayer) {
                addToServerProviderList(eventualSPList,
                        new Integer(-1), new Integer(-1),
                        AllowTransactionsLayer.NAME,
                        KBConfiguration.SERVICEPROVIDER_BASE_HTTP,
                        KBConfiguration.SERVICEPROVIDER_BASE_ABBR);
            }
        }

        return eventualSPList;
    }

    protected List prepareAccounting(Integer orgId, DataWrapper dw, List foundSpList) throws Exception {

        if (foundSpList==null)
            return null;
        
        List cleanedSpList = new ArrayList();
        
        AccountManager am = AccountManager.getAccountManager(orgId);
        TransactionLayerUsage tlu = am.beginTLU();

        LayerCalculator lc = new LayerCalculator();

        Map config = dw.getLayeringParameterMap();
        Boolean bat = (Boolean) config.get(AllowTransactionsLayer.allowTransactions);
        boolean bAllowTransactions = bat == null ? false : bat.booleanValue();

        Iterator it = foundSpList.iterator();
        try {
            
            while (it.hasNext()) {
                Map spInfo = (Map)it.next();
                Integer layerId = (Integer) spInfo.get("lId");

                    LayerPriceComposition lpc = calculateLayerPriceComposition(dw, lc, layerId);
                    if (lpc != null) {
                        tlu.registerUsage(lpc);
                        // Only add layer when free or when transactions allowed
                        Boolean isFree = lpc.getLayerIsFree();
                        if ((isFree == null || (isFree != null && !isFree.booleanValue())) && !bAllowTransactions) {
                            continue;
                        }
                    }
                    cleanedSpList.add(spInfo);
            }
        } finally {
            lc.closeEntityManager();
        }

        Boolean bfat = (Boolean) config.get(AllowTransactionsLayer.foundAllowTransactionsLayer);
        boolean bFoundAllowTransactionsLayer = bfat == null ? false : bfat.booleanValue();
        if (AccountManager.isEnableAccounting() && tlu.getCreditAlteration().doubleValue() > 0) {
            config.put(AllowTransactionsLayer.creditMutation, tlu.getCreditAlteration());
            config.put(AllowTransactionsLayer.pricedLayers, tlu.getPricedLayerNames());
            config.put(AllowTransactionsLayer.transactionsNeeded, new Boolean(true));
            if (!bAllowTransactions) {
                am.endTLU();
            }
            if (!bFoundAllowTransactionsLayer) {
                addToServerProviderList(cleanedSpList,
                        new Integer(-1), new Integer(-1),
                        AllowTransactionsLayer.NAME,
                        KBConfiguration.SERVICEPROVIDER_BASE_HTTP,
                        KBConfiguration.SERVICEPROVIDER_BASE_ABBR);
            }
        }
        return cleanedSpList;
    }
    
    public void doAccounting(Integer orgId, DataWrapper data, User user) throws Exception {
        AccountManager am = AccountManager.getAccountManager(orgId);
        TransactionLayerUsage transaction = am.getTLU();
        am.commitTransaction(transaction, user);
        am.endTLU();
        data.getLayeringParameterMap().put(BalanceLayer.creditBalance, new Double(am.getBalance()));
    }
    
    protected String[] toCodeAndName(String completeLayerName) throws Exception {
        // Check of layers[i] juiste format heeft
        int pos = completeLayerName.indexOf("_");
        if (pos == -1 || completeLayerName.length() <= pos + 1) {
            log.error("layer not valid: " + completeLayerName);
            throw new Exception(KBConfiguration.GETMAP_EXCEPTION);
        }
        String layerCode = completeLayerName.substring(0, pos);
        String layerName = completeLayerName.substring(pos + 1);
        if (layerCode.length() == 0 || layerName.length() == 0) {
            log.error("layer name or code not valid: " + layerCode + ", " + layerName);
            throw new Exception(KBConfiguration.GETMAP_EXCEPTION);
        }
        return new String[]{layerCode, layerName};
    }

    protected void addToServerProviderList(List eventualSPList,
            Integer serviceproviderId,
            Integer layerId,
            String layerName,
            String spUrl,
            String spAbbr) {
        Map spInfo = null;
        int size = eventualSPList.size();
        if (size > 0) {
            Map lastSpInfo = (Map) eventualSPList.get(size - 1);
            Integer spId = (Integer) lastSpInfo.get("spId");
            if (spId != null && spId.intValue() == serviceproviderId.intValue()) {
                spInfo = lastSpInfo;
            }
        }
        if (spInfo == null) {
            spInfo = new HashMap();
            spInfo.put("spId", serviceproviderId);
            spInfo.put("lId", layerId);
            spInfo.put("spUrl", spUrl);
            spInfo.put("spAbbr", spAbbr);
            eventualSPList.add(spInfo);
        }
        StringBuffer sp_layerlist = (StringBuffer) spInfo.get("layersList");
        if (sp_layerlist == null) {
            sp_layerlist = new StringBuffer(layerName);
            spInfo.put("layersList", sp_layerlist);
        } else {
            sp_layerlist.append(",");
            sp_layerlist.append(layerName);
        }
    }

    private void configB3pLayering(String[] layers, Map config) throws Exception {
        for (int i = 0; i < layers.length; i++) {
            String[] layerCodeAndName = toCodeAndName(layers[i]);
            String layerCode = layerCodeAndName[0];
            String layerName = layerCodeAndName[1];
            if (layerCode.equals(KBConfiguration.SERVICEPROVIDER_BASE_ABBR)) {
                ConfigLayer cl = ConfigLayer.forName(layerName);
                if (cl == null) {
                    log.error("Config Layer " + layerName + " not found!");
                    throw new Exception("Config Layer " + layerName + " not found!");
                }
                cl.processConfig(config);
            }
        }
    }

}
