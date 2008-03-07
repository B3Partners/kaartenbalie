/**
 * @(#)GetCapabilitiesRequestHandler.java
 * @author N. de Goeij
 * @version 1.00 2006/12/13
 *
 * Purpose: the function of this class is to create a list of url's which direct to the right servers that have the desired layers
 * for the WMS GetFeatureInfo request.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.service.requesthandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.reporting.domain.requests.WMSGetFeatureInfoRequest;
import nl.b3p.ogc.utils.OGCRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetFeatureInfoRequestHandler extends WMSRequestHandler {
    
    private static final Log log = LogFactory.getLog(GetFeatureInfoRequestHandler.class);
    
    // <editor-fold defaultstate="" desc="default GetFeatureInfoRequestHandler() constructor.">
    public GetFeatureInfoRequestHandler() {}
    // </editor-fold>
    
    /**
     * Processes the parameters and creates the specified urls from the given parameters.
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
    public void getRequest(DataWrapper dw, User user) throws IOException, Exception {
        dw.setHeader("Content-Disposition", "inline; filename=\"GetFeatureInfo.xml\";");
        
        OGCRequest ogcrequest = dw.getOgcrequest();
        String value = "";
        if (ogcrequest.containsParameter(FEATURE_INFO_FORMAT)) {
            value = ogcrequest.getParameter(FEATURE_INFO_FORMAT);
            if(value != null && value.length() > 0) {
                dw.setContentType(value);
            } else {
                dw.setContentType(WMS_PARAM_WMS_XML);
            }
        }
        
        Long timeFromStart = new Long(dw.getRequestReporting().getMSSinceStart());
        dw.setRequestParameter("MsSinceRequestStart", timeFromStart);
        
        this.user       = user;
        this.url        = user.getPersonalURL();
        Integer orgId   = user.getOrganization().getId();
        OGCRequest ogc  = dw.getOgcrequest();
        
        List spUrls = getSeviceProviderURLS(ogc.getParameter(WMS_PARAM_QUERY_LAYERS).split(","), orgId, true,dw);
        if(spUrls==null || spUrls.isEmpty()) {
            log.error("No urls qualify for request.");
            throw new Exception(FEATUREINFO_QUERYABLE_EXCEPTION);
        }
        
        ArrayList urlWrapper = new ArrayList();
        Iterator it = spUrls.iterator();
        while (it.hasNext()) {
            
            Map spInfo = (Map) it.next();
            WMSGetFeatureInfoRequest firWrapper = new WMSGetFeatureInfoRequest();
            Integer serviceProviderId = (Integer)spInfo.get("spId");
            if (serviceProviderId != null && serviceProviderId.intValue() == -1) {
                //Say hello to B3P Layering!!
            } else {
                firWrapper.setServiceProviderId(serviceProviderId);
                StringBuffer layersList = (StringBuffer)spInfo.get("layersList");

                StringBuffer url = new StringBuffer();
                url.append((String)spInfo.get("spUrl"));
                String [] params = dw.getOgcrequest().getParametersArray();
                for (int i = 0; i < params.length; i++) {
                    String [] keyValuePair = params[i].split("=");
                    if (keyValuePair[0].equalsIgnoreCase(WMS_PARAM_LAYERS)) {
                        url.append(WMS_PARAM_LAYERS);
                        url.append("=");
                        url.append(layersList);
                        url.append("&");
                    } else if (keyValuePair[0].equalsIgnoreCase(WMS_PARAM_QUERY_LAYERS)) {
                        url.append(WMS_PARAM_QUERY_LAYERS);
                        url.append("=");
                        url.append(layersList);
                        url.append("&");
                    } else {
                        url.append(params[i]);
                        url.append("&");
                    }
                }
                firWrapper.setProviderRequestURI(url.toString());
                urlWrapper.add(firWrapper);
            }
        }
        
        getOnlineData(dw, urlWrapper, false, WMS_REQUEST_GetFeatureInfo);
    }
}