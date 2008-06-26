/**
 * @(#)GetMapRequestHandler.java
 * @author N. de Goeij
 * @version 1.00 2006/12/13
 *
 * Purpose: the function of this class is to create a list of url's which direct to the right servers that have the desired layers
 * for the WMS GetMap request.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.service.requesthandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import nl.b3p.kaartenbalie.core.server.accounting.AccountManager;
import nl.b3p.kaartenbalie.core.server.accounting.entity.TransactionLayerUsage;
import nl.b3p.kaartenbalie.core.server.b3pLayering.BalanceLayer;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.core.server.reporting.domain.requests.WMSGetMapRequest;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetMapRequestHandler extends WMSRequestHandler {
    
    private static final Log log = LogFactory.getLog(GetMapRequestHandler.class);
    
    // <editor-fold defaultstate="" desc="default GetMapRequestHandler() constructor.">
    public GetMapRequestHandler() {}
    // </editor-fold>
    
    /** Processes the parameters and creates the specified urls from the given parameters.
     * Each url will be used to recieve the data from the ServiceProvider this url is refering to.
     *
     * @param dw DataWrapper which contains all information that has to be sent to the client
     * @param user User the user which invoked the request
     *
     * @return byte[]
     *
     * @throws Exception
     * @throws IOException
     */
    // <editor-fold defaultstate="" desc="getRequest(DataWrapper dw, User user) method.">
    public void getRequest(DataWrapper dw, User user) throws IOException, Exception {
        
        EntityManager em = MyEMFDatabase.getEntityManager();
        this.user       = user;
        this.url        = user.getPersonalURL();
        Integer orgId   = user.getOrganization().getId();
        OGCRequest ogc  = dw.getOgcrequest();
        
        String value = "";
        if (ogc.containsParameter(OGCConstants.WMS_PARAM_FORMAT)) {
            value = ogc.getParameter(OGCConstants.WMS_PARAM_FORMAT);
            if(value != null && value.length() > 0) {
                dw.setContentType(value);
            } else {
                dw.setContentType(OGCConstants.WMS_PARAM_WMS_XML);
            }
        }
        
        Long timeFromStart = new Long(dw.getRequestReporting().getMSSinceStart());
        dw.setRequestParameter("MsSinceRequestStart", timeFromStart);
        
        Integer width = null;
        try {
            width = new Integer(ogc.getParameter(OGCConstants.WMS_PARAM_WIDTH));
        } catch (NumberFormatException nfe) {
            width = new Integer(-1);
        }
        dw.setRequestParameter("Width", width);
        
        Integer height = null;
        try {
            height =  new Integer(ogc.getParameter(OGCConstants.WMS_PARAM_HEIGHT));
        } catch (NumberFormatException nfe) {
            height = new Integer(-1);
        }
        dw.setRequestParameter("Height",height);
        dw.setRequestParameter("WmsVersion", ogc.getParameter(OGCConstants.WMS_VERSION));
        dw.setRequestParameter("Srs", null);
        dw.setRequestParameter("Format", ogc.getParameter(OGCConstants.WMS_PARAM_FORMAT));
        dw.setRequestParameter("BoundingBox", ogc.getParameter(OGCConstants.WMS_PARAM_BBOX));
        
        String givenSRS         = ogc.getParameter(OGCConstants.WMS_PARAM_SRS);
        Map userdefinedParams   = ogc.getNonOGCParameters();
        
        List spUrls = getSeviceProviderURLS(ogc.getParameter(OGCConstants.WMS_PARAM_LAYERS).split(","), orgId, false, dw);
        if(spUrls==null || spUrls.isEmpty()) {
            log.error("No urls qualify for request.");
            throw new Exception(KBConfiguration.GETMAP_EXCEPTION);
        }
        
        ArrayList urlWrapper = new ArrayList();
        //ArrayList urls = new ArrayList();
        Iterator it = spUrls.iterator();
        AccountManager am = AccountManager.getAccountManager(orgId);
        while (it.hasNext()) {
            
            
            Map spInfo = (Map) it.next();
            WMSGetMapRequest gmrWrapper = new WMSGetMapRequest();
            Integer serviceProviderId = (Integer)spInfo.get("spId");
            
            if (serviceProviderId != null && serviceProviderId.intValue() == -1) {
                //Say hello to B3P Layering!!
                StringBuffer url = new StringBuffer();
                StringBuffer layersList = (StringBuffer)spInfo.get("layersList");
                url.append((String)spInfo.get("spUrl"));
                String [] params = ogc.getParametersArray();
                for (int i = 0; i < params.length; i++) {
                    String [] keyValuePair = params[i].split("=");
                    if (keyValuePair[0].equalsIgnoreCase(OGCConstants.WMS_PARAM_LAYERS)) {
                        url.append(OGCConstants.WMS_PARAM_LAYERS);
                        url.append("=");
                        url.append(layersList);
                        url.append("&");
                    } else {
                        url.append(params[i]);
                        url.append("&");
                    }
                }
                gmrWrapper.setProviderRequestURI(url.toString());
                urlWrapper.add(gmrWrapper);
                
            } else {
                gmrWrapper.setServiceProviderId(serviceProviderId);
                
                StringBuffer layersList = (StringBuffer)spInfo.get("layersList");
                
                String query = "select distinct srs.srs from layer, srs " +
                        "where layer.layerid = srs.layerid and " +
                        "srs.srs is not null and " +
                        "layer.layerid = :toplayer";
                
                boolean srsFound = false;
                List sqlQuery = em.createNativeQuery(query).setParameter("toplayer", (Integer)spInfo.get("lId")).getResultList();
                Iterator sqlIterator = sqlQuery.iterator();
                while (sqlIterator.hasNext()) {
                    String srs = (String)sqlIterator.next();
                    if(srs.equals(givenSRS)) {
                        srsFound = true;
                    }
                }
                if(!srsFound) {
                    log.error("No suitable srs found.");
                    throw new Exception(KBConfiguration.SRS_EXCEPTION);
                }
                
                StringBuffer url = new StringBuffer();
                url.append((String)spInfo.get("spUrl"));
                String [] params = ogc.getParametersArray();
                for (int i = 0; i < params.length; i++) {
                    String [] keyValuePair = params[i].split("=");
                    if (keyValuePair[0].equalsIgnoreCase(OGCConstants.WMS_PARAM_LAYERS)) {
                        url.append(OGCConstants.WMS_PARAM_LAYERS);
                        url.append("=");
                        url.append(layersList);
                        url.append("&");
                    } else {
                        url.append(params[i]);
                        url.append("&");
                    }
                }
                gmrWrapper.setProviderRequestURI(url.toString());
                urlWrapper.add(gmrWrapper);
            }
        }
        
        TransactionLayerUsage transaction = am.getTLU();
        am.commitTransaction(transaction, user);
        am.endTLU();
        dw.getLayeringParameterMap().put(BalanceLayer.creditBalance, new Double(am.getBalance()));
        getOnlineData(dw, urlWrapper, true, OGCConstants.WMS_REQUEST_GetMap);
    }
    // </editor-fold>
    
    
}