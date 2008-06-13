/*
 * WFSRequestHandler.java
 *
 * Created on June 10, 2008, 9:48 AM
 *
 */

package nl.b3p.kaartenbalie.service.requesthandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.accounting.AccountManager;
import nl.b3p.kaartenbalie.core.server.accounting.LayerCalculator;
import nl.b3p.kaartenbalie.core.server.accounting.WfsLayerCalculator;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPriceComposition;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPricing;
import nl.b3p.kaartenbalie.core.server.accounting.entity.TransactionLayerUsage;
import nl.b3p.kaartenbalie.core.server.b3pLayering.AllowTransactionsLayer;
import nl.b3p.kaartenbalie.core.server.b3pLayering.BalanceLayer;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.ogc.utils.OGCResponse;
import nl.b3p.ogc.wfs.v110.WfsLayer;
import nl.b3p.ogc.wfs.v110.WfsServiceProvider;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.w3c.dom.Document;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jytte
 */
public class WFSRequestHandler {
    
    private static final Log log = LogFactory.getLog(WFSRequestHandler.class);
    
    /** Creates a new instance of WFSRequestHandler */
    public WFSRequestHandler() {
    }
    
    public void recieveAndSend(DataWrapper data, User user) throws IllegalArgumentException, UnsupportedOperationException, IOException, Exception{
        OGCResponse ogcresponse = new OGCResponse();
        OGCRequest ogcrequest = data.getOgcrequest();
        String layers = null;
        String[] layerNames = null;
        int orgId = user.getOrganization().getId();
        
        String request = ogcrequest.getParameter(OGCConstants.REQUEST);
        if(request == OGCConstants.WFS_REQUEST_GetFeature || request == OGCConstants.WFS_REQUEST_DescribeFeatureType){
            layers = ogcrequest.getParameter(OGCConstants.WFS_PARAM_TYPENAME);
            String[] allLayers = layers.split(",");
            layerNames = new String[allLayers.length];
            for(int i = 0; i < allLayers.length; i++){
                String[] temp = allLayers[i].split("}");
                layerNames[i] = temp[1];
            }
        }
        String url = null;
        List spInfo = null;
        /*
         *Hier onderscheid maken tussen getCapabilities en de rest
         */      
        if(ogcrequest.getParameter(OGCConstants.REQUEST) != OGCConstants.WFS_REQUEST_GetCapabilities){
            spInfo = getServiceProviderURLS(layerNames, orgId, data);
            if(spInfo == null || spInfo.isEmpty()){
                throw new UnsupportedOperationException("No Serviceprovider available! User might not have rights to any Serviceprovider!");
            }
            Iterator iter = spInfo.iterator();
            while(iter.hasNext()){
                HashMap sp = (HashMap)iter.next();
                url = sp.get("spUrl").toString();
            }
        }else{
            Set serviceproviders = this.getServiceProviders(user, layerNames);
            WfsServiceProvider provider = null;
            
            if(serviceproviders == null || serviceproviders.isEmpty()){
                throw new UnsupportedOperationException("No Serviceprovider available! User might not have rights to any Serviceprovider!");
            }
            
            Iterator iter = serviceproviders.iterator();
            while(iter.hasNext()){
                provider = (WfsServiceProvider)iter.next();
            }
            url = provider.getUrl();
        }

        if(url == null || url == ""){
            throw new UnsupportedOperationException("No Serviceprovider for this service available!");
        }
        
        PostMethod method = null;
        HttpClient client = new HttpClient();        
        String host = url;
        method = new PostMethod(host); 
        String body = data.getOgcrequest().getXMLBody();
        method.setRequestEntity(new StringRequestEntity(body,"text/xml", "UTF-8"));
        int status=client.executeMethod(method);
        if (status == HttpStatus.SC_OK){
            data.setContentType("text/xml");
            OutputStream os = data.getOutputStream(); 
            InputStream is = method.getResponseBodyAsStream();
            
            if(ogcrequest.getParameter(OGCConstants.REQUEST) != OGCConstants.WFS_REQUEST_GetCapabilities){
                doAccounting(orgId, ogcresponse, data, spInfo, user);
            }
            
            if(data.getOgcrequest().getParameter(OGCConstants.REQUEST) == OGCConstants.WFS_REQUEST_DescribeFeatureType){
                int len = 1;
                byte[] buffer= new byte[2024];
                while((len=is.read(buffer,0,len))>0){
                    os.write(buffer,0,len); 
                }
            } else{
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = dbf.newDocumentBuilder();
                Document doc = builder.parse(is);
                ogcresponse.rebuildResponse(doc.getDocumentElement(), data.getOgcrequest().getHost());
                String responseBody = ogcresponse.getResponseBody();
                if(responseBody != null && !responseBody.equals("")){
                    byte[] buffer = responseBody.getBytes();
                    os.write(buffer);
                }else{
                    throw new UnsupportedOperationException("XMLbody empty!");
                }
            }
        }else{
            log.error("Failed to connect with "+ url +" Using body: "+body);
            throw new UnsupportedOperationException("Failed to connect with "+ url +" Using body: "+body);
        }
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
            int layerId = Integer.parseInt(layer.get("tlId").toString());

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
    
    public List getServiceProviderURLS(String[] layers, int orgID, DataWrapper dw)throws Exception{
        List eventualSPList = new ArrayList();
        EntityManager em = MyEMFDatabase.getEntityManager();
        
        if(layers==null || layers.length == 0) {
            log.error("No layers found!");
            throw new Exception(KBConfiguration.GETMAP_EXCEPTION);
        }

        String query = "select l.wfsserviceproviderid, l.wfslayerid, sp.url, l.name " +
                "from wfs_Layer l, Wfs_ServiceProvider sp, Wfs_OrganizationLayer o " +
                "where o.organizationid = :orgID and l.wfslayerid = o.wfslayerid and " +
                "l.wfsserviceproviderid = sp.wfsserviceproviderid";
        List layerQuery = em.createNativeQuery(query).setParameter("orgID", orgID).getResultList();
        if (layerQuery==null || layerQuery.isEmpty()) {
            log.error("no layers found!");
            throw new Exception(KBConfiguration.GETMAP_EXCEPTION);
        }


        Iterator layerit = layerQuery.iterator();
        List oldLayers = new ArrayList();
        while (layerit.hasNext()) {
            Object [] objecten = (Object [])layerit.next();
            for(int i = 0; i < layers.length; i++){
                if(layers[i].equalsIgnoreCase(objecten[3].toString())){
                    if(oldLayers != null && !oldLayers.isEmpty()){
                        Iterator it = oldLayers.iterator();
                        boolean isNewLayer = true;
                        while(it.hasNext()){
                            String oldlayer = it.next().toString();
                            if(oldlayer.equalsIgnoreCase(layers[i])){
                                isNewLayer = false;
                            }
                        }
                        if(isNewLayer == true){
                            Map spInfo = new HashMap();
                            spInfo.put("spId", (Integer)objecten[0]);
                            spInfo.put("tlId", (Integer)objecten[1]);
                            spInfo.put("spUrl", (String)objecten[2]);
                            eventualSPList.add(spInfo);
                            oldLayers.add(layers[i]);
                        }
                    }else{
                        Map spInfo = new HashMap();
                        spInfo.put("spId", (Integer)objecten[0]);
                        spInfo.put("tlId", (Integer)objecten[1]);
                        spInfo.put("spUrl", (String)objecten[2]);
                        eventualSPList.add(spInfo);
                        oldLayers.add(layers[i]);
                    }
                }
            }
            
        }

        return eventualSPList;
    }
    
     /*
     * Makes a list of serviceProviders the user has rights to.
     */
    public Set getServiceProviders(User user, String[] layerNames) throws NoResultException, Exception{
        EntityManager em = MyEMFDatabase.getEntityManager();
        User dbUser = null;
        
        try {
            dbUser = (User)em.createQuery("from User u where " +
                    "lower(u.id) = lower(:userid)").setParameter("userid", user.getId()).getSingleResult();
        } catch (NoResultException nre) {
            log.error("No serviceprovider for user found.");
            throw new Exception("No serviceprovider for user found.");
        }
        
        Set serviceproviders = null;
        Set organizationLayers = dbUser.getOrganization().getWfsOrganizationLayer();
            
        if(layerNames == null || layerNames.length == 0){
            if (organizationLayers != null && !organizationLayers.isEmpty()) {
                serviceproviders = new HashSet();
                Iterator it = organizationLayers.iterator();
                while(it.hasNext()) {
                    WfsLayer layer = (WfsLayer)it.next();
                    WfsServiceProvider sp = layer.getWfsServiceProvider();
                    if(!serviceproviders.contains(sp)){
                        serviceproviders.add(sp); 
                    }
                }
            }
            return serviceproviders;
        }
        else{
            if (organizationLayers != null && !organizationLayers.isEmpty()) {
                serviceproviders = new HashSet();
                Iterator it = organizationLayers.iterator();
                while(it.hasNext()) {
                    WfsLayer layer = (WfsLayer)it.next();
                    for(int x = 0; x < layerNames.length; x++){
                        if(layer.getName().equalsIgnoreCase(layerNames[x])){
                            WfsServiceProvider sp = layer.getWfsServiceProvider();
                            if(!serviceproviders.contains(sp)){
                                serviceproviders.add(sp); 
                            }
                        }
                    }
                }
            }
            return serviceproviders;
        }
    }
    
}
