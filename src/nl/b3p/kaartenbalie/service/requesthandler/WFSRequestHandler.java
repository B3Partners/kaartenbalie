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
        String prefix = null;
        
        spInfo = getServiceProviders(layerNames, orgId, data);
        if(spInfo == null || spInfo.isEmpty()){
                throw new UnsupportedOperationException("No Serviceprovider available! User might not have rights to any Serviceprovider!");
        }
        Iterator iter = spInfo.iterator();
        while(iter.hasNext()){
            HashMap sp = (HashMap)iter.next();
            url = sp.get("spUrl").toString();
            prefix = sp.get("spAbbr").toString();
            ogcrequest.addOrReplaceParameter(OGCConstants.WFS_PARAM_TYPENAME, "app:" + sp.get("lName"));
        }

        if(url == null || url == ""){
            throw new UnsupportedOperationException("No Serviceprovider for this service available!");
        }
        PostMethod method = null;
        HttpClient client = new HttpClient();
        OutputStream os = data.getOutputStream();
        String body = data.getOgcrequest().getXMLBody();
        
        if(ogcrequest.getParameter(OGCConstants.REQUEST) != OGCConstants.WFS_REQUEST_GetCapabilities){ 
            String host = url;
            method = new PostMethod(host); 
            method.setRequestEntity(new StringRequestEntity(body,"text/xml", "UTF-8"));
            int status=client.executeMethod(method);
            if (status == HttpStatus.SC_OK){
                data.setContentType("text/xml");
                InputStream is = method.getResponseBodyAsStream();

                doAccounting(orgId, ogcresponse, data, spInfo, user);

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

                    ogcresponse.rebuildResponse(doc.getDocumentElement(), data.getOgcrequest().getHost(), prefix); //url, spInfo);
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
        }else{
            Iterator it = spInfo.iterator();
            while(it.hasNext()){
                HashMap sp = (HashMap)it.next();
                
                url = sp.get("spUrl").toString();
                prefix = sp.get("spAbbr").toString();
                      
                String host = url;
                method = new PostMethod(host); 
                method.setRequestEntity(new StringRequestEntity(body,"text/xml", "UTF-8"));
                int status=client.executeMethod(method);
                if (status == HttpStatus.SC_OK){
                    data.setContentType("text/xml");
                    InputStream is = method.getResponseBodyAsStream();
                    
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = dbf.newDocumentBuilder();
                    Document doc = builder.parse(is);

                    ogcresponse.rebuildResponse(doc.getDocumentElement(), data.getOgcrequest().getHost(), prefix); //url, spInfo);
                }else{
                    log.error("Failed to connect with "+ url +" Using body: "+body);
                    throw new UnsupportedOperationException("Failed to connect with "+ url +" Using body: "+body);
                }  
            }
            String responseBody = ogcresponse.getResponseBody();
            if(responseBody != null && !responseBody.equals("")){
                byte[] buffer = responseBody.getBytes();
                os.write(buffer);
            }else{
                throw new UnsupportedOperationException("XMLbody empty!");
            }
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
    
     /*
     * Makes a list of serviceProviders the user has rights to.
     */ 
    public List getServiceProviders(String[] layers, int orgID, DataWrapper dw)throws Exception{
        List eventualSPList = new ArrayList();
        EntityManager em = MyEMFDatabase.getEntityManager();
        String request = dw.getOgcrequest().getParameter(OGCConstants.REQUEST);
        
        if((layers==null || layers.length == 0) && !request.equalsIgnoreCase(OGCConstants.WFS_REQUEST_GetCapabilities)) {
            log.error("No layers found!");
            throw new Exception(KBConfiguration.GETMAP_EXCEPTION);
        }
        String query = "select l.wfsserviceproviderid, l.wfslayerid, sp.url, sp.abbr, l.name " +
            "from wfs_Layer l, Wfs_ServiceProvider sp, Wfs_OrganizationLayer o " +
            "where o.organizationid = :orgID and l.wfslayerid = o.wfslayerid and " +
            "l.wfsserviceproviderid = sp.wfsserviceproviderid";
        List layerQuery = em.createNativeQuery(query).setParameter("orgID", orgID).getResultList();
        if (layerQuery==null || layerQuery.isEmpty()) {
            log.error("no layers found!");
            throw new Exception(KBConfiguration.GETMAP_EXCEPTION);
        }
        
        if(layers==null || layers.length == 0){    
            Iterator layerit = layerQuery.iterator();
            while (layerit.hasNext()) {
                Object [] objecten = (Object [])layerit.next();
                Map spInfo = new HashMap();
                spInfo.put("spId", (Integer)objecten[0]);
                spInfo.put("lId", (Integer)objecten[1]);
                spInfo.put("spUrl", (String)objecten[2]);
                spInfo.put("spAbbr", (String)objecten[3]);
                spInfo.put("lName", (String)objecten[4]);
                eventualSPList.add(spInfo);
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
                        Map spInfo = new HashMap();
                        spInfo.put("spId", (Integer)objecten[0]);
                        spInfo.put("lId", (Integer)objecten[1]);
                        spInfo.put("spUrl", (String)objecten[2]);
                        spInfo.put("spAbbr", (String)objecten[3]);
                        spInfo.put("lName", (String)objecten[4]);
                        eventualSPList.add(spInfo);
                    }
                }
            }
        }

        return eventualSPList;
    }
    
}
