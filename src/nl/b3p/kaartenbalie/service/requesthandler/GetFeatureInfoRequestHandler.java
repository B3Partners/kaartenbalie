package nl.b3p.kaartenbalie.service.requesthandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nl.b3p.kaartenbalie.core.server.ServiceDomainResource;
import nl.b3p.kaartenbalie.core.server.ServiceProvider;
import nl.b3p.kaartenbalie.core.server.User;

/**
 * @(#)GetFeatureInfoRequestHandler.java
 *
 *
 * @author N. de Goeij
 * @version 1.00 2006/12/13
 */


public class GetFeatureInfoRequestHandler extends WMSRequestHandler {
    
    public GetFeatureInfoRequestHandler() {}
    
    public byte[] getRequest(Map parameters) throws IOException, Exception {
        
        user = (User) parameters.get(KB_USER);
        url = (String) parameters.get(KB_PERSONAL_URL);
        
        List tempSP = getServiceProviders(false);
        if (tempSP==null)
            return null;
        
        ArrayList urls = new ArrayList();
        
        Iterator it = tempSP.iterator();
        while (it.hasNext()) {
            ServiceProvider s = (ServiceProvider)it.next();
            StringBuffer spUrl = null;
            
            //Lets first check if this ServiceProvider can provide us the asked layers
            //otherwise it is not necessary at all to look further and ask for a lot of resources
            String[] layer = (String[])parameters.get(WMS_PARAM_LAYERS);
            String spls = calcFormattedLayers(s, layer);
            if (spls==null)
                continue;
            
            //Since some or all layers were found, we need to build up a string to become an url
            //Find the beginning of the url which thise layers belong to
            spUrl = calcRequestUrl(s, WMS_REQUEST_GetFeatureInfo);
            if (spUrl==null)
                continue;
            
            // toevoegen van andere parameters
            spUrl.append(WMS_VERSION);
            spUrl.append("=");
            spUrl.append((String)parameters.get(WMS_VERSION));
            spUrl.append("&");
            spUrl.append(WMS_REQUEST);
            spUrl.append("=");
            spUrl.append(WMS_REQUEST_GetFeatureInfo);
            spUrl.append("&");
            spUrl.append(WMS_PARAM_QUERY_LAYERS);
            spUrl.append("=");
            spUrl.append(spls);
            
            String infoFormat = (String)parameters.get(WMS_PARAM_INFO_FORMAT);
            if(null != infoFormat) {
                spUrl.append("&");
                spUrl.append(WMS_PARAM_INFO_FORMAT);
                spUrl.append("=");
                spUrl.append(infoFormat);
            }
            
            String featureCount = (String)parameters.get(WMS_PARAM_FEATURECOUNT);
            if (null != featureCount) {
                spUrl.append("&");
                spUrl.append(WMS_PARAM_FEATURECOUNT);
                spUrl.append("=");
                spUrl.append(featureCount);
            }
            
            spUrl.append("&");
            spUrl.append(WMS_PARAM_X);
            spUrl.append("=");
            spUrl.append((String)parameters.get(WMS_PARAM_X));
            spUrl.append("&");
            spUrl.append(WMS_PARAM_Y);
            spUrl.append("=");
            spUrl.append((String)parameters.get(WMS_PARAM_Y));
        }
        
        StringBuffer [] url = null;
        return getOnlineData((StringBuffer[])urls.toArray(url));
    }
    
    
}