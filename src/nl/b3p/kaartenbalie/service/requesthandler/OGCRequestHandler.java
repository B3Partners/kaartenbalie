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

import nl.b3p.ogc.utils.SpLayerSummary;
import java.util.*;
import javax.persistence.EntityManager;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.accounting.AccountManager;
import nl.b3p.kaartenbalie.core.server.accounting.ExtLayerCalculator;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPriceComposition;
import nl.b3p.kaartenbalie.core.server.accounting.entity.Transaction;
import nl.b3p.kaartenbalie.core.server.b3pLayering.AllowTransactionsLayer;
import nl.b3p.kaartenbalie.core.server.b3pLayering.BalanceLayer;
import nl.b3p.kaartenbalie.core.server.b3pLayering.ConfigLayer;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.LayerSummary;
import nl.b3p.ogc.utils.OGCCommunication;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.Roles;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class OGCRequestHandler implements RequestHandler {

    private static final Log log = LogFactory.getLog(OGCRequestHandler.class);
    protected User user;
    protected String url;
    protected static long maxResponseTime = 10000;

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
    protected abstract SpLayerSummary getValidLayerObjects(EntityManager em, LayerSummary m, Integer[] orgIds, boolean b3pLayering) throws Exception;

    /**
     * methode die alleen een query nodig heeft om bestaan en rechten van layer
     * te bepalen.
     * <P>
     * @param em EntityManager
     * @param query query waarmee rechten en bestaan gecheckt kan worden
     * @param m
     * @param orgIds
     * @param b3pLayering bepaalt of service layers toegevoegd worden, true = alleen service layers,
     * false = alleen echte layers.
     * @param spAbbrUrl
     * @return object array met resultaat voor gezochte layer
     * @see Object[] getValidLayerObjects(EntityManager em, String query, String layer, Integer orgId, boolean b3pLayering) throws Exception
     * @throws java.lang.Exception indien gezochte layer niet bestaat of er geen rechten op zijn
     */
    protected SpLayerSummary getValidLayerObjects(EntityManager em, String query, LayerSummary m, Integer[] orgIds, boolean b3pLayering) throws Exception {        
        String layerCode = m.getSpAbbr();
        String layerName = OGCCommunication.buildLayerNameWithoutSp(m);

        log.debug("Collect layer info for layer: " + layerName + " and service provider: " + layerCode);

        if (layerCode == null){
            return null;
        }
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
        
        log.debug("BEGIN query getValidLayerObjects");
        
        long startTime = System.currentTimeMillis();
        
        List result = em.createQuery(query).
                setParameter("orgIds", Arrays.asList(orgIds)).
                setParameter("layerName", layerName).
                setParameter("layerCode", layerCode).
                getResultList();
        
        long endTime = System.currentTimeMillis();
        long dur = endTime - startTime;
        
        log.debug(dur + "ms: END query getValidLayerObjects");

        if (result == null || result.isEmpty()) {
            log.error("layer not valid or no rights, name: " + OGCCommunication.buildFullLayerName(m));
            throw new Exception(KBConfiguration.REQUEST_NORIGHTS_EXCEPTION + ": " + OGCCommunication.buildFullLayerName(m));
        } else if (result.size() > 1) {
            log.error("layers with duplicate names, name: " + OGCCommunication.buildFullLayerName(m));
            throw new Exception(KBConfiguration.REQUEST_DUPLICATE_EXCEPTION + ": " + OGCCommunication.buildFullLayerName(m));
        }

        return (SpLayerSummary) result.get(0);
    }
    
    public boolean isConfigInUrlAndAdmin(DataWrapper data, User user){
         /*
         * Only used if specific param is given (used for configuration)
         */
        boolean isAdmin = false;
        if ("true".equalsIgnoreCase(data.getOgcrequest().getParameter("_VIEWER_CONFIG"))) {
            Set userRoles = user.getRoles();
            Iterator rolIt = userRoles.iterator();
            while (rolIt.hasNext()) {
                Roles role = (Roles) rolIt.next();
                if (role.getRole().equalsIgnoreCase(Roles.ADMIN)) {
                    /* de gebruiker is een beheerder */
                    isAdmin = true;
                    break;
                }
            }
        }
        return isAdmin;
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
    protected List getServiceProviderURLS(List<LayerSummary> lsl, Integer[] orgIds, boolean checkForQueryable, DataWrapper dw, boolean maintainOrder) throws Exception {

        // Per kaartlaag wordt uitgezocht tot welke sp hij hoort,
        // er voldoende rechten zijn voor de kaart en of aan
        // de queryable voorwaarde is voldaan
        List<SpLayerSummary> eventualSPList = new ArrayList();

        log.debug("Getting entity manager ......");
        EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);

        Map config = dw.getLayeringParameterMap();
        configB3pLayering(lsl, config);

        String spAbbrUrl = dw.getOgcrequest().getServiceProviderName();
        
        //eerst geen b3pLayering meenemen
        boolean b3pLayering = false;
        for (LayerSummary m : lsl) {
            SpLayerSummary layerInfo = getValidLayerObjects(em, m, orgIds, b3pLayering);
            if (layerInfo == null ) {
                continue;
            }

            // layer toevoegen aan sp indien queryable voorwaarde ok
            if (!checkForQueryable
                    || (checkForQueryable && layerInfo.getQueryable() != null && layerInfo.getQueryable().equals("1"))) {
                addToServerProviderList(eventualSPList, layerInfo, maintainOrder);
            }
        }

        //als laatste b3pLayering meenemen
        b3pLayering = true;
        for (LayerSummary m : lsl) {
            SpLayerSummary layerInfo = getValidLayerObjects(em, m, orgIds, b3pLayering);
            if (layerInfo == null) {
                continue;
            }
            for (LayerSummary ls : layerInfo.getLayers()) {
                if (AllowTransactionsLayer.NAME.equalsIgnoreCase(OGCCommunication.buildLayerNameWithoutSp(ls))) {
                    config.put(AllowTransactionsLayer.foundAllowTransactionsLayer, Boolean.TRUE);
                }
            }
            addToServerProviderList(eventualSPList, layerInfo, maintainOrder);
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
    protected List<SpLayerSummary> prepareAccounting(Integer orgId, DataWrapper dw, List<SpLayerSummary> foundSpList) throws Exception {

        if (foundSpList == null) {
            return null;
        }
        List<SpLayerSummary> cleanedSpList = new ArrayList();

        AccountManager am = AccountManager.getAccountManager(orgId);
        Transaction tlu = am.beginTLU();

        Map config = dw.getLayeringParameterMap();

        /*
         * WFS doesn't use the AllowTransactionsLayer and the boolean will be always false
         * if a WFS layer has a price and never show it.
         */
        boolean bAllowTransactions = true;
        Boolean bat = (Boolean) config.get(AllowTransactionsLayer.allowTransactions);
        String service = dw.getService();
        if (service!=null && service.equals(OGCConstants.WMS_SERVICE_WMS)) {
            bAllowTransactions = bat == null ? false : bat.booleanValue();
        }

        ExtLayerCalculator lc = new ExtLayerCalculator();

        Iterator it = foundSpList.iterator();
        try {

            while (it.hasNext()) {
                SpLayerSummary spInfo = (SpLayerSummary) it.next();
                List<LayerSummary> layers = spInfo.getLayers();
                if (layers == null) {
                    continue;
                }
                List<LayerSummary> checkedLayers = new ArrayList();
                Iterator it2 = layers.iterator();
                while (it2.hasNext()) {
                    LayerSummary ls = (LayerSummary)it2.next();
                    LayerPriceComposition lpc = calculateLayerPriceComposition(dw, lc, ls.getSpAbbr(), OGCCommunication.buildLayerNameWithoutSp(ls));
                    boolean bIsFree = true;
                    if (lpc != null) {
                        tlu.registerUsage(lpc);
                        Boolean isFree = lpc.getLayerIsFree();
                        bIsFree = isFree == null ? false : isFree.booleanValue();
                    }
                    // Only add layer when free or when transactions allowed
                    if (bIsFree || bAllowTransactions) {
                        checkedLayers.add(ls);
                    }
                }
                if (checkedLayers.size() > 0) {
                    spInfo.setLayers(checkedLayers);
                    cleanedSpList.add(spInfo);
                }
            }
        } finally {
             lc.closeEntityManager();
        }

        /* 
         * WFS doesn't use the AllowTransactionsLayer and the boolean will be always false 
         * if a WFS layer has a price and never show it.
         */
        boolean bFoundAllowTransactionsLayer = true;
        Boolean bfat = (Boolean) config.get(AllowTransactionsLayer.foundAllowTransactionsLayer);
        if (service!=null && service.equals(OGCConstants.WMS_SERVICE_WMS)) {
            bFoundAllowTransactionsLayer = bfat == null ? false : bfat.booleanValue();
        }

        if (AccountManager.isEnableAccounting() && tlu.getCreditAlteration().doubleValue() > 0) {
            config.put(AllowTransactionsLayer.creditMutation, tlu.getCreditAlteration());
            config.put(AllowTransactionsLayer.pricedLayers, tlu.getPricedLayerNames());
            config.put(AllowTransactionsLayer.transactionsNeeded, Boolean.TRUE);
            if (!bAllowTransactions) {
                am.endTLU();
            }
            if (!bFoundAllowTransactionsLayer) {
                SpLayerSummary layerInfo = new SpLayerSummary(new Integer(-1),
                        new Integer(-1),
                        AllowTransactionsLayer.NAME,
                        KBConfiguration.SERVICEPROVIDER_BASE_HTTP,
                        KBConfiguration.SERVICEPROVIDER_BASE_ABBR,
                        null,null);
                addToServerProviderList(cleanedSpList, layerInfo, true);
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
        Transaction transaction = am.getTLU();
        am.commitTransaction(transaction, user);
        am.endTLU();
        data.getLayeringParameterMap().put(BalanceLayer.creditBalance, new Double(am.getBalance()));
    }

    protected Layer getLayerByUniqueName(String uniqueName) throws Exception {
        Object identity = null;
        try {
            identity = MyEMFDatabase.createEntityManager(MyEMFDatabase.INIT_EM);
            log.debug("Getting entity manager ......");
            EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.INIT_EM);

            String[] layerCodeAndName = OGCCommunication.toCodeAndName(uniqueName);
            String spAbbr = layerCodeAndName[0];
            String layerName = layerCodeAndName[1];

            String query = "from Layer where name = :layerName and serviceProvider.abbr = :spAbbr";
            List ll = em.createQuery(query).setParameter("layerName", layerName).setParameter("spAbbr", spAbbr).getResultList();

            if (ll == null || ll.isEmpty()) {
                return null;
            }
            // Dit is nodig omdat mysql case insensitive selecteert
            Iterator it = ll.iterator();
            while (it.hasNext()) {
                Layer l = (Layer) it.next();
                String dbLayerName = l.getName();
                String dbSpAbbr = l.getSpAbbr();
                if (dbLayerName != null && dbSpAbbr != null) {
                    if (dbLayerName.equals(layerName) && dbSpAbbr.equals(spAbbr)) {
                        return l;
                    }
                }
            }
        } catch (Throwable e) {
            log.warn("Error creating EntityManager: ", e);
        } finally {
            log.debug("Closing entity manager .....");
            MyEMFDatabase.closeEntityManager(identity, MyEMFDatabase.INIT_EM);
        }
        return null;
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
     * @param layerInfo
     * @param maintainOrder
      */
    protected void addToServerProviderList(List<SpLayerSummary> eventualSPList, SpLayerSummary layerInfo, boolean maintainOrder) {
        SpLayerSummary correctSpInfo = null;
        SpLayerSummary matchingSpInfo = null;
        SpLayerSummary lastSpInfo = null;
        int loopnum = 0;
        for (SpLayerSummary sls : eventualSPList) {
            Integer spId = sls.getServiceproviderId();
            if (spId != null && spId.intValue() == layerInfo.getServiceproviderId().intValue()) {
                matchingSpInfo = sls;
            }
            loopnum++;
            if (loopnum == eventualSPList.size()) {
                lastSpInfo = sls;
            }
        }
        if (!maintainOrder && matchingSpInfo != null) {
            // voeg layers toe aan overeenkomende sp
            correctSpInfo = matchingSpInfo;
        } else if (lastSpInfo!=null && lastSpInfo.equals(matchingSpInfo)) {
            // laatste sp is geschikt om toe te voegen
            correctSpInfo = lastSpInfo;
        }
        if (correctSpInfo == null) {
            correctSpInfo = layerInfo;
            eventualSPList.add(correctSpInfo);
        } else {
            correctSpInfo.addLayers(layerInfo.getLayers());
            //spInfo.addStyles(layerInfo.getLayerName(), layerInfo.getStyles(layerInfo.getLayerName()));
        }
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
    protected void configB3pLayering(List<LayerSummary> lsl, Map config) throws Exception {
        for (LayerSummary m : lsl) {
            String layerCode = m.getSpAbbr();
            String layerName = OGCCommunication.buildLayerNameWithoutSp(m);

            if (layerCode==null || layerName==null) {
                continue;
            }
            
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
