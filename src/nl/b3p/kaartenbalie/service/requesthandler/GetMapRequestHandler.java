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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityTransaction;
import nl.b3p.kaartenbalie.core.server.accounting.AccountManager;
import nl.b3p.kaartenbalie.core.server.accounting.entity.TransactionLayerUsage;
import nl.b3p.ogc.utils.KBConstants;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.reporting.domain.requests.WMSGetMapRequest;
import nl.b3p.ogc.utils.OGCRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetMapRequestHandler extends WMSRequestHandler implements KBConstants {
    
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
        /*
         * Initialize some variables
         * And immediatly set the right output format (also for errors) because if an error occurs
         * with the GetMap functionality before the outputformat is set then the standard output
         * format would be used.
         */
        this.user       = user;
        this.url        = user.getPersonalURL();
        Integer orgId   = user.getOrganization().getId();
        OGCRequest ogc  = dw.getOgcrequest();
        
        Long timeFromStart = new Long(dw.getRequestReporting().getMSSinceStart());
        dw.setRequestParameter("MsSinceRequestStart", timeFromStart);
        
        Integer width = null;
        try {
            width = new Integer(ogc.getParameter(WMS_PARAM_WIDTH));
        } catch (NumberFormatException nfe) {
            width = new Integer(-1);
        }
        dw.setRequestParameter("Width", width);
        
        Integer height = null;
        try {
            height =  new Integer(ogc.getParameter(WMS_PARAM_HEIGHT));
        } catch (NumberFormatException nfe) {
            height = new Integer(-1);
        }
        dw.setRequestParameter("Height",height);
        
        dw.setRequestParameter("WmsVersion", ogc.getParameter(WMS_VERSION));
        dw.setRequestParameter("Srs", null);
        dw.setRequestParameter("Format", ogc.getParameter(WMS_PARAM_FORMAT));
        dw.setRequestParameter("BoundingBox", ogc.getParameter(WMS_PARAM_BBOX));
        
        String givenSRS         = ogc.getParameter(WMS_PARAM_SRS);
        Map userdefinedParams   = ogc.getNonOGCParameters();
        
        List spUrls = getSeviceProviderURLS(ogc.getParameter(WMS_PARAM_LAYERS).split(","), orgId, false);
        if(spUrls==null || spUrls.isEmpty()) {
            log.error("No urls qualify for request.");
            throw new Exception(GETMAP_EXCEPTION);
        }
        
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        ArrayList urlWrapper = new ArrayList();
        //ArrayList urls = new ArrayList();
        Iterator it = spUrls.iterator();
        AccountManager am = AccountManager.getAccountManager(orgId);
        while (it.hasNext()) {
            
            
            Map spInfo = (Map) it.next();
            WMSGetMapRequest gmrWrapper = new WMSGetMapRequest();
            Integer serviceProviderId = (Integer)spInfo.get("spId");
            gmrWrapper.setServiceProviderId(serviceProviderId);
            
            StringBuffer layersList = (StringBuffer)spInfo.get("layersList");
            
            String query = "select distinct srs.srs from layer, srs " +
                    "where layer.layerid = srs.layerid and " +
                    "srs.srs is not null and " +
                    "layer.layerid = :toplayer";
            
            boolean srsFound = false;
            List sqlQuery = em.createNativeQuery(query).setParameter("toplayer", (Integer)spInfo.get("tlId")).getResultList();
            Iterator sqlIterator = sqlQuery.iterator();
            while (sqlIterator.hasNext()) {
                String srs = (String)sqlIterator.next();
                if(srs.equals(givenSRS)) {
                    srsFound = true;
                }
            }
            if(!srsFound) {
                log.error("No suitable srs found.");
                throw new Exception(SRS_EXCEPTION);
            }
            
            StringBuffer url = new StringBuffer();
            url.append((String)spInfo.get("spUrl"));
            String [] params = ogc.getParametersArray();
            for (int i = 0; i < params.length; i++) {
                String [] keyValuePair = params[i].split("=");
                if (keyValuePair[0].equalsIgnoreCase(WMS_PARAM_LAYERS)) {
                    url.append(WMS_PARAM_LAYERS);
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
        tx.commit();
        am.doTransaction(am.getTLU(), user);
        am.endTLU();
        
        
        getOnlineData(dw, urlWrapper, true, WMS_REQUEST_GetMap);
    }
    // </editor-fold>
    
    
}