/**
 * @(#)OGCRequestHandler.java
 *
 */

package nl.b3p.kaartenbalie.service.requesthandler;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.accounting.AccountManager;
import nl.b3p.kaartenbalie.core.server.accounting.LayerCalculator;
import nl.b3p.kaartenbalie.core.server.accounting.WfsLayerCalculator;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPriceComposition;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPricing;
import nl.b3p.kaartenbalie.core.server.accounting.entity.TransactionLayerUsage;
import nl.b3p.kaartenbalie.core.server.b3pLayering.AllowTransactionsLayer;
import nl.b3p.kaartenbalie.core.server.b3pLayering.BalanceLayer;
import nl.b3p.kaartenbalie.core.server.b3pLayering.ConfigLayer;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class OGCRequestHandler implements RequestHandler {
    
    private static final Log log = LogFactory.getLog(OGCRequestHandler.class);
    protected User user;
    protected String url;
    protected static long maxResponseTime = 100000;
    
    
    public OGCRequestHandler() {
    }
    
    public abstract void getRequest(DataWrapper dw, User user) throws IOException, Exception;
         /*
          * Makes a list of serviceProviders the user has rights to.
          */
    public List getServiceProviders(String [] layers, Integer orgID, boolean checkForQueryable, DataWrapper dw) throws Exception{
        
        if((layers==null || layers.length == 0) && !request.equalsIgnoreCase(OGCConstants.WFS_REQUEST_GetCapabilities)) {
            log.error("No layers found!");
            throw new Exception(KBConfiguration.GETMAP_EXCEPTION); //todo message aanpassen
        }
        
        Map eventualSPMap = new HashMap();
        EntityManager em = MyEMFDatabase.getEntityManager();
        String request = dw.getOgcrequest().getParameter(OGCConstants.REQUEST);
        
        String query = createLayerQuery();
        List layerQuery = em.createNativeQuery(query).setParameter("orgID", orgID).getResultList();
        if (layerQuery==null || layerQuery.isEmpty()) {
            log.error("no layers found!");
            throw new Exception(KBConfiguration.GETMAP_EXCEPTION);
        }
        
        if(layers==null || layers.length == 0){
            Iterator layerit = layerQuery.iterator();
            while (layerit.hasNext()) {
                Object [] objecten = (Object [])layerit.next();
                eventualSPMap.add(fillSpInfo(objecten));
            }
        }else{
            String[][] splitLayer = new String[layers.length][2];
            for(int i = 0; i < layers.length; i++){
                String[] serverLayer = layers[i].split("_");
                if(serverLayer.length <= 1){
                    throw new Exception("Layer is not correct. Layer should look like: serverPrefix_layerName!");
                }
                splitLayer[i][0] = serverLayer[0];
                splitLayer[i][1] = serverLayer[1];
            }
            Iterator layerit = layerQuery.iterator();
            while (layerit.hasNext()) {
                Object [] objecten = (Object [])layerit.next();
                String abbr = objecten[3].toString();
                String layerName = objecten[4].toString();
                for(int b = 0; b < splitLayer.length; b++){
                    if(abbr.equalsIgnoreCase(splitLayer[b][0]) && layerName.equalsIgnoreCase(splitLayer[b][1])){
                        eventualSPMap.add(fillSpInfo(objecten));
                    }
                }
            }
        }
        
        return eventualSPMap;
    }
    
    public void doAccounting(int orgId, OGCResponse ogcresponse, DataWrapper data, List spInfo, User user)throws Exception{
        AccountManager am = AccountManager.getAccountManager(orgId);
        TransactionLayerUsage tlu = am.beginTLU();
        WfsLayerCalculator lc = new WfsLayerCalculator();
        String projection = ogcresponse.getSrs();
        if(projection == null){
            projection = "";
        }
        //BigDecimal scale = new BigDecimal(data.getOgcrequest().calcScale());
        BigDecimal scale = null;
        
        for(int i = 0; i < spInfo.size(); i++){
            HashMap layer = (HashMap)spInfo.get(i);
            int layerId = Integer.parseInt(layer.get("lId").toString());
            
            if (AccountManager.isEnableAccounting()) {
                String operation = data.getOperation();
                if(operation==null) {
                    log.error("Operation can not be null");
                    throw new Exception("Operation can not be null");
                } else {
                    Date validationDate = new Date(System.currentTimeMillis()); // today
                    BigDecimal units = new BigDecimal(1);
                    LayerPriceComposition lpc = lc.calculateLayerComplete(layerId, validationDate,  projection, scale, units, LayerPricing.PAY_PER_REQUEST, "WFS", operation);
                    tlu.registerUsage(lpc);
                    
                    // Only add layer when free or when transactions allowed
                    //Boolean isFree = lpc.getLayerIsFree();
                }
            }
        }
        
        TransactionLayerUsage transaction = am.getTLU();
        am.commitTransaction(transaction, user);
        am.endTLU();
        data.getLayeringParameterMap().put(BalanceLayer.creditBalance, new Double(am.getBalance()));
    }
    
    protected String createLayerQuery() {
        if (true) {
            return "select l.wfsserviceproviderid, l.wfslayerid, sp.url, sp.abbr, l.name " +
                    "from wfs_Layer l, Wfs_ServiceProvider sp, Wfs_OrganizationLayer o " +
                    "where o.organizationid = :orgID and l.wfslayerid = o.wfslayerid and " +
                    "l.wfsserviceproviderid = sp.wfsserviceproviderid";
        } else {
            return "select l.serviceproviderid, l.layerid, sp.url, sp.abbr, l.name " +
                    "from layer l, serviceprovider sp " +
                    "where l.parentid IS NULL and " +
                    "l.serviceproviderid = sp.serviceproviderid";
        }
        
    }
    //Nieuwe methode om de urls samen te stellen en gelijk alle rechten te controleren
    //deze methode werkt sneller en efficienter dan de bovenstaande getserviceprovider
    //methode in combinatie met de code voor controle van layer rechten.
    
    protected List getSeviceProviderURLS(String [] layers, Integer orgId, boolean checkForQueryable, DataWrapper dw) throws Exception {
        
        if(layers==null || layers.length == 0) {
            log.error("No layers found!");
            throw new Exception(KBConfiguration.GETMAP_EXCEPTION);
        }
        
        EntityManager em = MyEMFDatabase.getEntityManager();
        
        // Hier komt elke sp precies een keer uit als tenminste de database
        // correct is en er maar een toplayer (parent==null) is!
        Map toplayerMap = new HashMap();
        String query = createLayerQuery();
        
        List toplayerQuery = em.createNativeQuery(query).getResultList();
        if (toplayerQuery==null || toplayerQuery.isEmpty()) {
            log.error("no toplayers found!");
            throw new Exception(KBConfiguration.GETMAP_EXCEPTION);
        }
        
        toplayerMap = fillSpMap(toplayerQuery, toplayerMap);
        
        toplayerMap = addB3PLayering(layers, dw, toplayerMap);
        
        // Per kaartlaag wordt uitgezocht tot welke sp hij hoort,
        // er voldoende rechten zijn voor de kaart en of aan
        // de queryable voorwaarde is voldaan
        List eventualSPList = new ArrayList();
        
        
        /* Accounting... */
        AccountManager am = AccountManager.getAccountManager(orgId);
        TransactionLayerUsage tlu = am.beginTLU();
        LayerCalculator lc = new LayerCalculator();
        String projection = dw.getOgcrequest().getParameter(OGCConstants.WMS_PARAM_SRS);
        BigDecimal scale = new BigDecimal(dw.getOgcrequest().calcScale());
        Boolean bat = (Boolean) config.get(AllowTransactionsLayer.allowTransactions);
        boolean bAllowTransactions = bat==null?false:bat.booleanValue();
        /* End of Accounting */
        
        // Do normal layers
        try {
            for (int i = 0; i < layers.length; i++) {
                String[] layerCodeAndName = toCodeAndName(layers[i]);
                String layerCode = layerCodeAndName[0];
                String layerName= layerCodeAndName[1];
                
                if (!layerCode.equals(KBConfiguration.SERVICEPROVIDER_BASE_ABBR)) {
                    // Check of voldoende rechten op layer bestaan en ophalen url
                    query = "select layer.queryable, layer.serviceproviderid, layer.layerid " +
                            "from layer, organizationlayer, serviceprovider " +
                            "where organizationlayer.layerid = layer.layerid and " +
                            "layer.serviceproviderid = serviceprovider.serviceproviderid and " +
                            "organizationlayer.organizationid = :orgId and " +
                            "layer.name = :layerName and " +
                            "serviceprovider.abbr = :layerCode";
                    List sqlQuery = em.createNativeQuery(query)
                    .setParameter("orgId", orgId)
                    .setParameter("layerName", layerName)
                    .setParameter("layerCode", layerCode)
                    .getResultList();
                    if(sqlQuery.isEmpty()) {
                        log.error("layer not valid or no rights, name: " + layers[i]);
                        throw new Exception(KBConfiguration.GETMAP_EXCEPTION);
                    }
                    Object [] objecten = (Object [])sqlQuery.get(0);
                    String layer_queryable      = (String)objecten[0];
                    Integer serviceprovider_id  = (Integer)objecten[1];
                    Integer layerId  = (Integer)objecten[2];
                    if (serviceprovider_id==null)
                        continue;
                    
                    // layer toevoegen aan sp indien queryable voorwaarde ok
                    if (!checkForQueryable || (checkForQueryable && layer_queryable.equals("1"))) {
                        /* Accounting... */
                        if (AccountManager.isEnableAccounting()) {
                            String operation = dw.getOperation();
                            if(operation==null) {
                                log.error("Operation can not be null");
                                throw new Exception("Operation can not be null");
                            } else if (operation.equalsIgnoreCase(OGCConstants.WMS_REQUEST_GetLegendGraphic)) {
                                log.debug("Never pricing for GetLegendGraphic.");
                            } else {
                                Date validationDate = new Date(); // today
                                BigDecimal units = new BigDecimal(1);
                                LayerPriceComposition lpc = lc.calculateLayerComplete(layerId,validationDate,  projection, scale, units, LayerPricing.PAY_PER_REQUEST, "WMS", operation);
                                tlu.registerUsage(lpc);
                                
                                // Only add layer when free or when transactions allowed
                                Boolean isFree = lpc.getLayerIsFree();
                                if ((isFree == null || (isFree!=null && !isFree.booleanValue())) && !bAllowTransactions)
                                    continue;
                            }
                        }
                        /* End of Accounting */
                        
                        addToServerProviderList(eventualSPList,serviceprovider_id, toplayerMap, layerName);
                    }
                }
            }
        } finally {
            lc.closeEntityManager();
        }
        
        // B3P Layering: add layers on top
        boolean foundAllowTransactionsLayer = false;
        for (int i = 0; i < layers.length; i++) {
            String[] layerCodeAndName = toCodeAndName(layers[i]);
            String layerCode = layerCodeAndName[0];
            String layerName= layerCodeAndName[1];
            if (layerCode.equals(KBConfiguration.SERVICEPROVIDER_BASE_ABBR)) {
                if (AllowTransactionsLayer.NAME.equalsIgnoreCase(layerName))
                    foundAllowTransactionsLayer = true;
                addToServerProviderList(eventualSPList,new Integer(-1), toplayerMap, layerName);
            }
        }
        
        /* Accounting... */
        Map config = dw.getLayeringParameterMap();
        if (AccountManager.isEnableAccounting() && tlu.getCreditAlteration().doubleValue()> 0) {
            config.put(AllowTransactionsLayer.creditMutation, tlu.getCreditAlteration());
            config.put(AllowTransactionsLayer.pricedLayers, tlu.getPricedLayerNames());
            config.put(AllowTransactionsLayer.transactionsNeeded, new Boolean(true));
            if (!bAllowTransactions)
                am.endTLU();
            if (!foundAllowTransactionsLayer)
                addToServerProviderList(eventualSPList,new Integer(-1), toplayerMap, AllowTransactionsLayer.NAME);
        }
        /* End of Accounting */
        
        
        return eventualSPList;
    }
    
    protected List getSeviceProviderURLS2(String [] layers, Integer orgId, boolean checkForQueryable, DataWrapper dw) throws Exception {
        
        // Per kaartlaag wordt uitgezocht tot welke sp hij hoort,
        // er voldoende rechten zijn voor de kaart en of aan
        // de queryable voorwaarde is voldaan
        List eventualSPList = new ArrayList();
        
        EntityManager em = MyEMFDatabase.getEntityManager();
        
        /* Accounting... */
        AccountManager am = AccountManager.getAccountManager(orgId);
        TransactionLayerUsage tlu = am.beginTLU();
        
        LayerCalculator lc = new LayerCalculator();
        String projection = dw.getOgcrequest().getParameter(OGCConstants.WMS_PARAM_SRS);
        BigDecimal scale = new BigDecimal(dw.getOgcrequest().calcScale());
        
        Map config = dw.getLayeringParameterMap();
        Boolean bat = (Boolean) config.get(AllowTransactionsLayer.allowTransactions);
        boolean bAllowTransactions = bat==null?false:bat.booleanValue();
        
        /* End of Accounting */
        
        try {
            for (int i = 0; i < layers.length; i++) {
                String[] layerCodeAndName = toCodeAndName(layers[i]);
                String layerCode = layerCodeAndName[0];
                String layerName= layerCodeAndName[1];
                
                if (!layerCode.equals(KBConfiguration.SERVICEPROVIDER_BASE_ABBR)) {
                    // Check of voldoende rechten op layer bestaan en ophalen url
                    String query = "select layer.queryable, layer.serviceproviderid, layer.layerid " +
                            "from layer, organizationlayer, serviceprovider " +
                            "where organizationlayer.layerid = layer.layerid and " +
                            "layer.serviceproviderid = serviceprovider.serviceproviderid and " +
                            "organizationlayer.organizationid = :orgId and " +
                            "layer.name = :layerName and " +
                            "serviceprovider.abbr = :layerCode";
                    List sqlQuery = em.createNativeQuery(query)
                    .setParameter("orgId", orgId)
                    .setParameter("layerName", layerName)
                    .setParameter("layerCode", layerCode)
                    .getResultList();
                    if(sqlQuery.isEmpty()) {
                        log.error("layer not valid or no rights, name: " + layers[i]);
                        throw new Exception(KBConfiguration.GETMAP_EXCEPTION);
                    }
                    Object [] objecten = (Object [])sqlQuery.get(0);
                    String layer_queryable      = (String)objecten[0];
                    Integer serviceprovider_id  = (Integer)objecten[1];
                    Integer layerId  = (Integer)objecten[2];
                    if (serviceprovider_id==null)
                        continue;
                    
                    // layer toevoegen aan sp indien queryable voorwaarde ok
                    if (!checkForQueryable || (checkForQueryable && layer_queryable.equals("1"))) {
                        String operation = dw.getOperation();
                        if(operation==null) {
                            log.error("Operation can not be null");
                            throw new Exception("Operation can not be null");
                        } else if (operation.equalsIgnoreCase(OGCConstants.WMS_REQUEST_GetLegendGraphic)) {
                            log.debug("Never pricing for GetLegendGraphic.");
                        }
                        
                        addToServerProviderList(eventualSPList,serviceprovider_id, toplayerMap, layerName);
                    }
                }
            }
        } finally {
            lc.closeEntityManager();
        }
        
        /* Accounting... */
        if (AccountManager.isEnableAccounting() && tlu.getCreditAlteration().doubleValue()> 0) {
            config.put(AllowTransactionsLayer.creditMutation, tlu.getCreditAlteration());
            config.put(AllowTransactionsLayer.pricedLayers, tlu.getPricedLayerNames());
            config.put(AllowTransactionsLayer.transactionsNeeded, new Boolean(true));
            if (!bAllowTransactions)
                am.endTLU();
            if (!foundAllowTransactionsLayer)
                addToServerProviderList(eventualSPList,new Integer(-1), toplayerMap, AllowTransactionsLayer.NAME);
        }
        /* End of Accounting */
        
        return eventualSPList;
    }
    
    protected String[] toCodeAndName(String completeLayerName) throws Exception{
        // Check of layers[i] juiste format heeft
        int pos = completeLayerName.indexOf("_");
        if (pos==-1 || completeLayerName.length()<=pos+1) {
            log.error("layer not valid: " + completeLayerName);
            throw new Exception(KBConfiguration.GETMAP_EXCEPTION);
        }
        String layerCode = completeLayerName.substring(0, pos);
        String layerName = completeLayerName.substring(pos + 1);
        if (layerCode.length()==0 || layerName.length()==0) {
            log.error("layer name or code not valid: " + layerCode + ", " + layerName);
            throw new Exception(KBConfiguration.GETMAP_EXCEPTION);
        }
        return new String[] {layerCode, layerName};
    }
    
    protected void addToServerProviderList(List eventualSPList, Integer serviceprovider_id, Map toplayerMap, String layerName) {
        Map spInfo = null;
        int size = eventualSPList.size();
        if (size>0) {
            Map lastSpInfo = (Map) eventualSPList.get(size - 1);
            Integer spId = (Integer)lastSpInfo.get("spId");
            if (spId!=null && spId.intValue()==serviceprovider_id.intValue())
                spInfo = lastSpInfo;
        }
        if (spInfo == null) {
            spInfo = new HashMap();
            Map tlSpInfo = (Map)toplayerMap.get(serviceprovider_id);
            spInfo.put("spId", tlSpInfo.get("spId"));
            spInfo.put("lId", tlSpInfo.get("lId"));
            spInfo.put("spUrl", tlSpInfo.get("spUrl"));
            spInfo.put("spAbbr", tlSpInfo.get("spAbbr"));
            spInfo.put("lName", tlSpInfo.get("lName"));
            eventualSPList.add(spInfo);
        }
        StringBuffer sp_layerlist = (StringBuffer)spInfo.get("layersList");
        if (sp_layerlist==null) {
            sp_layerlist = new StringBuffer(layerName);
            spInfo.put("layersList",sp_layerlist);
        } else {
            sp_layerlist.append(",");
            sp_layerlist.append(layerName);
        }
    }
    
    private Map fillSpMap(List layerQuery, Map eventualSPMap) {
        if (layerQuery==null)
            return null;
        if (eventualSPMap==null)
            eventualSPMap = new HashMap();
        Iterator layerit = layerQuery.iterator();
        while (layerit.hasNext()) {
            Object [] objecten = (Object [])layerit.next();
            Map spInfo = new HashMap();
            spInfo.put("spId", (Integer)objecten[0]);
            spInfo.put("lId", (Integer)objecten[1]);
            spInfo.put("spUrl", (String)objecten[2]);
            spInfo.put("spAbbr", (String)objecten[3]);
            spInfo.put("lName", (String)objecten[4]);
            eventualSPMap.put(spInfo.get("spId"), spInfo);
        }
        return eventualSPMap;
    }
    
    private Map addB3PLayering(String [] layers, DataWrapper dw, Map layerMap) {
        if (layerMap==null)
            layerMap = new HashMap();
        /* B3P Layering...*/
        Map spInfo = new HashMap();
        spInfo.put("spId", new Integer(-1));
        spInfo.put("tlId", new Integer(-1));
        spInfo.put("spUrl", KBConfiguration.SERVICEPROVIDER_BASE_HTTP);
        spInfo.put("spAbbr", "b3p");
        spInfo.put("lName", "layering");
        layerMap.put(spInfo.get("spId"), spInfo);
        /* EO B3P Layering... */
        
        //B3Partners Layers ConfigMap
        Map config = dw.getLayeringParameterMap();
        for (int i = 0; i < layers.length; i++) {
            String[] layerCodeAndName = toCodeAndName(layers[i]);
            String layerCode = layerCodeAndName[0];
            String layerName= layerCodeAndName[1];
            if (layerCode.equals(KBConfiguration.SERVICEPROVIDER_BASE_ABBR)) {
                ConfigLayer cl = ConfigLayer.forName(layerName);
                if (cl == null) {
                    log.error("Config Layer " + layerName + " not found!");
                    throw new Exception("Config Layer " + layerName + " not found!");
                }
                cl.processConfig(config);
            }
        }
        
        return layerMap;
    }
    
}