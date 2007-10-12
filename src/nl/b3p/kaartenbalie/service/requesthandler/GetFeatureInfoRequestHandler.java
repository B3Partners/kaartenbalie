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
import java.util.Map;
import nl.b3p.kaartenbalie.core.server.User;

public class GetFeatureInfoRequestHandler extends WMSRequestHandler {
    
    // <editor-fold defaultstate="" desc="default GetFeatureInfoRequestHandler() constructor.">
    public GetFeatureInfoRequestHandler() {}
    // </editor-fold>
    
    /** Processes the parameters and creates the specified urls from the given parameters.
     * Each url will be used to recieve the data from the ServiceProvider this url is refering to.
     * @param parameters Map parameters
     * @return byte[]
     *
     * @throws IOException
     */
    // <editor-fold defaultstate="" desc="getRequest(Map parameters) method.">
    public void getRequest(DataWrapper dw, Map parameters) throws IOException, Exception {
        /* 
         * Initialize some variables
         * And immediatly set the right output format (also for errors) because if an error occurs
         * with the GetMap functionality before the outputformat is set then the standard output
         * format would be used.
         */
        dw.setHeader("Content-Disposition", "inline; filename=\"GetFeatureInfo.xml\";");
        
        String format = (String) parameters.get(WMS_PARAM_FORMAT);
        dw.setContentType(format);
        
        String inimageType = null;
        
        if (parameters.containsKey(WMS_PARAM_EXCEPTION_FORMAT)) {
            inimageType = format;
        }
        dw.setErrorContentType(inimageType);        
        
        user = (User) parameters.get(KB_USER);
        Integer orgId = user.getOrganization().getId();
        
        url = (String) parameters.get(KB_PERSONAL_URL);
        String [] layers = ((String) parameters.get(WMS_PARAM_LAYERS)).split(",");
        
        ArrayList spUrls = getSeviceProviderURLS(layers, orgId, true);        
        ArrayList urls = new ArrayList();
        Iterator it = spUrls.iterator();
        while (it.hasNext()) {
            String [] sp_layerlist = (String []) it.next();            
            
            StringBuffer url = new StringBuffer();
            url.append(sp_layerlist[1]);
            url.append(WMS_VERSION);
            url.append("=");
            url.append((String)parameters.get(WMS_VERSION));
            url.append("&");
            url.append(WMS_REQUEST);
            url.append("=");
            url.append(WMS_REQUEST_GetFeatureInfo);


            String infoFormat = (String)parameters.get(WMS_PARAM_INFO_FORMAT);
            if(null != infoFormat) {
                url.append("&");
                url.append(WMS_PARAM_INFO_FORMAT);
                url.append("=");
                url.append(FEATURE_INFO_FORMAT);
            }

            String featureCount = (String)parameters.get(WMS_PARAM_FEATURECOUNT);
            if (null != featureCount) {
                url.append("&");
                url.append(WMS_PARAM_FEATURECOUNT);
                url.append("=");
                url.append(featureCount);
            }

            url.append("&");
            url.append(WMS_PARAM_X);
            url.append("=");
            url.append((String)parameters.get(WMS_PARAM_X));
            url.append("&");
            url.append(WMS_PARAM_Y);
            url.append("=");
            url.append((String)parameters.get(WMS_PARAM_Y));
            url.append("&");
            url.append(WMS_PARAM_LAYERS);
            url.append("=");
            url.append(sp_layerlist[2]);
            url.append("&");
            url.append(WMS_PARAM_SRS);
            url.append("=");
            url.append((String)parameters.get(WMS_PARAM_SRS));
            url.append("&");
            url.append(WMS_PARAM_BBOX);
            url.append("=");
            url.append((String)parameters.get(WMS_PARAM_BBOX));
            url.append("&");
            url.append(WMS_PARAM_WIDTH);
            url.append("=");
            url.append((String)parameters.get(WMS_PARAM_WIDTH));
            url.append("&");
            url.append(WMS_PARAM_HEIGHT);
            url.append("=");
            url.append((String)parameters.get(WMS_PARAM_HEIGHT));  
            url.append("&");
            url.append(WMS_PARAM_QUERY_LAYERS);
            url.append("=");
            url.append(sp_layerlist[2]);
            urls.add(url.toString());            
        }        
        
        if(urls == null) {            
            throw new Exception("msWMSFeatureInfo(): WMS server error. Requested layer(s) are not queryable.");
        }
        
        getOnlineData(dw, urls, false, WMS_REQUEST_GetFeatureInfo);
    }
    // </editor-fold>
}