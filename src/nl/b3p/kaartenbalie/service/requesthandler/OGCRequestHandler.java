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
import nl.b3p.kaartenbalie.core.server.accounting.ExtLayerCalculator;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPriceComposition;
import nl.b3p.kaartenbalie.core.server.accounting.entity.TransactionLayerUsage;
import nl.b3p.kaartenbalie.core.server.b3pLayering.AllowTransactionsLayer;
import nl.b3p.kaartenbalie.core.server.b3pLayering.BalanceLayer;
import nl.b3p.kaartenbalie.core.server.b3pLayering.ConfigLayer;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.OGCConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import nl.b3p.kaartenbalie.service.requesthandler.SpLayerSummary;

public abstract class OGCRequestHandler implements RequestHandler {

    private static final Log log = LogFactory.getLog(OGCRequestHandler.class);
    protected User user;
    protected String url;
    protected static long maxResponseTime = 100000;

    public OGCRequestHandler() {
    }

    /**
     * abstracte methode die door implementatie van handler moet worden gedefinieerd.
     * feitelijke moet alleen de juiste query gedefinieerd te worden waar dan de
     * volgende invoerparameters bijhoren:
     * <ul>
     *    <li>orgId</li>
     *    <li>layerName</li>
     *    <li>layerCode (ofwel spAbbr)</li>
     * </ul>
     * als resultaat moet de query de volgende kolommen geven:
     * <ul>
     *    <li>layerQueryable</li>
     *    <li>serviceproviderId</li>
     *    <li>layerId</li>
     *    <li>layerName</li>
     *    <li>spUrl</li>
     *    <li>spAbbr</li>
     * </ul>
     * het echte werkt wordt gedaan door de volgende methode
     * <p>
     * @param em EntityManager
     * @param layer layer naam met sp abbrr (abbr_layer)
     * @param orgId organisatie id
     * @param b3pLayering bepaalt of service layers toegevoegd worden, true = alleen service layers,
     * false = alleen echte layers.
     * @return object array met resultaat voor gezochte layer
     * @see Object[] getValidLayerObjects(EntityManager em, String query, String layer, Integer orgId, boolean b3pLayering) throws Exception
     * @throws java.lang.Exception indien gezochte layer niet bestaat of er geen rechten op zijn
     */
    protected abstract SpLayerSummary getValidLayerObjects(EntityManager em, String layer, Integer orgId, boolean b3pLayering) throws Exception;

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
    protected SpLayerSummary getValidLayerObjects(EntityManager em, String query, String layer, Integer orgId, boolean b3pLayering) throws Exception {
        String[] layerCodeAndName = toCodeAndName(layer);
        String layerCode = layerCodeAndName[0];
        String layerName = layerCodeAndName[1];
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

        Object[] objecten = (Object[]) sqlQuery.get(0);
        SpLayerSummary layerInfo = new SpLayerSummary(
                (Integer) objecten[1],
                (Integer) objecten[2],
                (String) objecten[3],
                (String) objecten[4],
                (String) objecten[5],
                (String) objecten[0]);

        return layerInfo;
    }

    /**
     * methode is complexer dan op het eerste gezicht nodig lijkt. per service provider
     * worden de layers opgezocht. echter de volgorde van de layers moet bewaard blijven.
     * als voor een service provider meerdere layers voorkomen, maar tussen deze layers
     * zitten andere layers van andere service providers, dan dient de service provider
     * meerdere keren opgenomen te worden. later wordt dan ook meerdere keren een call
     * naar deze zelfde service provider gedaan.
     * <p>
     * een en ander is nodig bij map images om layers in exact de juiste volgorde op
     * elkaar te plaatsen. bij featureinfo dient de volgorde in het xml ook overeen
     * te komen met de volgorde in de request.
     * <p>
     * er zijn 2 soorten layers: normale layers zoals deze door de gebruiker zijn
     * geselecteerd en service layers (b3playering) welke gebruikt worden door
     * kaartenbalie om gebruikers in de gelegenheid te stellen afrekenen toe te staan,
     * hun credit hoeveelheid te zien en om over-head-display foutmeldingen te geven.
     * de layers van b3playering worden altijd boven op geplaatst, zodat de layers 2x
     * door lopen moeten worden.
     * <p>
     * @param layers array van layers waarvan de serviceprovider moet worden opgezocht
     * @param orgId organbisatie id
     * @param checkForQueryable true = zoek alleen layers die queryable zijn
     * @param dw datawrapper
     * @return lijst van serviceproviders met daarin bij behorende layers
     * @throws java.lang.Exception fout bij ophalen layer informatie
     */
    protected List getSeviceProviderURLS(String[] layers, Integer orgId, boolean checkForQueryable, DataWrapper dw) throws Exception {

        // Per kaartlaag wordt uitgezocht tot welke sp hij hoort,
        // er voldoende rechten zijn voor de kaart en of aan
        // de queryable voorwaarde is voldaan
        List eventualSPList = new ArrayList();

        EntityManager em = MyEMFDatabase.getEntityManager();

        Map config = dw.getLayeringParameterMap();
        
        configB3pLayering(layers, config);

        //eerst geen b3pLayering meenemen
        boolean b3pLayering = false;
        for (int i = 0; i < layers.length; i++) {
            SpLayerSummary layerInfo = getValidLayerObjects(em, layers[i], orgId, b3pLayering);
            if (layerInfo == null) {
                continue;
            }

            // layer toevoegen aan sp indien queryable voorwaarde ok
            if (!checkForQueryable || (checkForQueryable && layerInfo.getQueryable().equals("1"))) {
                addToServerProviderList(eventualSPList, layerInfo);
            }
        }

        //als laatste b3pLayering meenemen
        b3pLayering = true;
        for (int i = 0; i < layers.length; i++) {
            SpLayerSummary layerInfo = getValidLayerObjects(em, layers[i], orgId, b3pLayering);
            if (layerInfo == null) {
                continue;
            }
            if (AllowTransactionsLayer.NAME.equalsIgnoreCase(layerInfo.getLayerName())) {
                config.put(AllowTransactionsLayer.foundAllowTransactionsLayer, new Boolean(true));
            }
            addToServerProviderList(eventualSPList, layerInfo);
        }

        return eventualSPList;
    }

    /**
     * abstracte methode die voor wms en wfs specifiek geimplmenteerd word.
     * @param dw datawrapper
     * @param lc LayerCalculator bepaalt de calculatie methode
     * @param layerId laag id waarvoor prijs berekend moet worden
     * @return geeft prijs
     * @throws java.lang.Exception fout bij prijsbepaling
     */
    protected abstract LayerPriceComposition calculateLayerPriceComposition(DataWrapper dw, ExtLayerCalculator lc, String spAbbr, String layerName) throws Exception;

    /**
     * deze methode doorloopt alle service providers en daarbinnen weer alle layers
     * om vast te stellen of er betaling noodzakelijk is. de bepaalde beprijzing wordt
     * opgenomen in een transactie van de AccountManager. later wordt de daadwerkelijke
     * afrekening geeffectueerd als het vervolgproces goed verlopen is.
     * <p>
     * indien wel betaling noodzakelijk is, maar er is geen toestemming of credit
     * dan wordt een over-head-display image aangemaakt met melding.
     * <p>
     * @param orgId organisatie id
     * @param dw datawrapper
     * @param foundSpList lijst van service providers die doorlopen moet worden
     * @return opgeschoonde lijst van layers die getoond mogen worden.
     * @throws java.lang.Exception fout bij bepaling prijs
     */
    protected List prepareAccounting(Integer orgId, DataWrapper dw, List foundSpList) throws Exception {

        if (foundSpList == null) {
            return null;
        }
        List cleanedSpList = new ArrayList();

        AccountManager am = AccountManager.getAccountManager(orgId);
        TransactionLayerUsage tlu = am.beginTLU();

        Map config = dw.getLayeringParameterMap();
        Boolean bat = (Boolean) config.get(AllowTransactionsLayer.allowTransactions);
        boolean bAllowTransactions = bat == null ? false : bat.booleanValue();
        
        /* 
         * WFS doesn't use the AllowTransactionsLayer and the boolean will be always false 
         * if a WFS layer has a price and never show it.
         */
        if(dw.getOgcrequest().getParameter(OGCConstants.SERVICE).equals(OGCConstants.WMS_SERVICE_WMS)){
            bAllowTransactions = bat == null ? false : bat.booleanValue();
        }else{
            bAllowTransactions = true;
        }

        ExtLayerCalculator lc = new ExtLayerCalculator();

        Iterator it = foundSpList.iterator();
        try {

            while (it.hasNext()) {
                SpLayerSummary spInfo = (SpLayerSummary) it.next();
                String spAbbr = spInfo.getSpAbbr();
                List layers = spInfo.getLayers();
                if (layers == null) {
                    continue;
                }
                List checkedLayers = new ArrayList();
                Iterator it2 = layers.iterator();
                while (it2.hasNext()) {
                    String layerName = (String) it2.next();
                    LayerPriceComposition lpc = calculateLayerPriceComposition(dw, lc, spAbbr, layerName);
                    if (lpc != null) {
                        // Only add layer when free or when transactions allowed
                        Boolean isFree = lpc.getLayerIsFree();
                        boolean bIsFree = isFree == null ? false : isFree.booleanValue();
                        if (!bIsFree && !bAllowTransactions) {
                            tlu.registerUsage(lpc);
                            continue;
                        }
                        tlu.registerUsage(lpc);
                    }
                    checkedLayers.add(layerName);
                }
                if (checkedLayers.size() > 0) {
                    spInfo.setLayers(checkedLayers);
                    cleanedSpList.add(spInfo);
                }
            }
        } finally {
            if (lc != null) {
                lc.closeEntityManager();
            }
        }
        

        
        
        Boolean bfat = (Boolean) config.get(AllowTransactionsLayer.foundAllowTransactionsLayer);
        boolean bFoundAllowTransactionsLayer = bfat == null ? false : bfat.booleanValue();
        
        /* 
         * WFS doesn't use the AllowTransactionsLayer and the boolean will be always false 
         * if a WFS layer has a price and never show it.
         */
        if(dw.getOgcrequest().getParameter(OGCConstants.SERVICE).equals(OGCConstants.WMS_SERVICE_WMS)){
            bFoundAllowTransactionsLayer = bfat == null ? false : bfat.booleanValue();
        }else{
            bFoundAllowTransactionsLayer = true;
        }
        
        if (AccountManager.isEnableAccounting() && tlu.getCreditAlteration().doubleValue() > 0) {
            config.put(AllowTransactionsLayer.creditMutation, tlu.getCreditAlteration());
            config.put(AllowTransactionsLayer.pricedLayers, tlu.getPricedLayerNames());
            config.put(AllowTransactionsLayer.transactionsNeeded, new Boolean(true));
            if (!bAllowTransactions) {
                am.endTLU();
            }
            if (!bFoundAllowTransactionsLayer) {
                SpLayerSummary layerInfo = new SpLayerSummary(new Integer(-1),
                        new Integer(-1),
                        AllowTransactionsLayer.NAME,
                        KBConfiguration.SERVICEPROVIDER_BASE_HTTP,
                        KBConfiguration.SERVICEPROVIDER_BASE_ABBR,
                        null);
                addToServerProviderList(cleanedSpList, layerInfo);
            }
        }
        
        return cleanedSpList;
    }

    /**
     * methode sluit transactie van AccountManager af waarbij alle aangemelde
     * betalingen daadwerkelijk worden afgeboekt en de transactie wordt gesloten.
     * <p>
     * @param orgId organisatie id
     * @param data datawrapper
     * @param user user object
     * @throws java.lang.Exception fout bij afronden transactie
     */
    public void doAccounting(Integer orgId, DataWrapper data, User user) throws Exception {
        AccountManager am = AccountManager.getAccountManager(orgId);
        TransactionLayerUsage transaction = am.getTLU();
        am.commitTransaction(transaction, user);
        am.endTLU();
        data.getLayeringParameterMap().put(BalanceLayer.creditBalance, new Double(am.getBalance()));
    }

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

    /**
     * methode om nieuwe layer toe te voegen aan lijst met service providers. eerst wordt
     * de laatste service provider opgezocht. als de nieuwe layer bij deze
     * service provider hoort dan kan de layer aan deze service provider worden
     * toegevoegd. Als de nieuwe layer bij een andere service provider hoort, dan
     * moet een nieuwe service provider worden aangemaakt en de layer hier aan toe
     * gevoegd worden.
     * <p>
     * een nieuwe layer mag niet aan een eerder in de lijst geplaatste service provider
     * worden toegevoegd (ook al is dat wel de zelfde) omdat dan de volgorde van
     * de layers niet meer klopt. Consequentie is dat service providers soms meerdere
     * keren worden aangeroepen.
     * <p>
     * @param eventualSPList lijst met service providers in juiste volgore
     * @param serviceproviderId sp id van nieuwe layer
     * @param layerId id van nieuwe layer
     * @param layerName name van nieuwe layer
     * @param spUrl url van sp van nieuwe layer
     * @param spAbbr abbr van sp van nieuwe layer
     */
    protected void addToServerProviderList(List eventualSPList, SpLayerSummary layerInfo) {
        SpLayerSummary spInfo = null;
        int size = eventualSPList.size();
        if (size > 0) {
            SpLayerSummary lastSpInfo = (SpLayerSummary) eventualSPList.get(size - 1);
            Integer spId = lastSpInfo.getServiceproviderId();
            if (spId != null && spId.intValue() == layerInfo.getServiceproviderId().intValue()) {
                spInfo = lastSpInfo;
            }
        }
        if (spInfo == null) {
            spInfo = layerInfo;
            eventualSPList.add(spInfo);
        }

        spInfo.addLayer(layerInfo.getLayerName());
    }

    /**
     * methode doorloopt alle layers om te zien of er service layers bij zitten
     * (b3playering). als zo'n layer wordt gevonden dan wordt de processConfig van
     * die service layer aangeroepen. Deze processConfig kan dan informatie toevoegen
     * aan de algemene config map.
     * <p>
     * de service layer AllowTransactionsLayer plaatst bv een boolean in de config
     * map, zodat verder op de accouting methodes weten dan de gebruiker afrekenen
     * heeft toegestaan.
     * <p>
     * @param layers lijst van alle layers
     * @param config map met configuratie info
     * @throws java.lang.Exception fout bij configureren
     */
    protected void configB3pLayering(String[] layers, Map config) throws Exception {
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
