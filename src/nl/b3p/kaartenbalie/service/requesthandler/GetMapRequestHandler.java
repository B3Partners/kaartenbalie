package nl.b3p.kaartenbalie.service.requesthandler;

/**
 * @(#)GetMapRequestHandler.java
 *
 *
 * @author N. de Goeij
 * @version 1.00 2006/12/13
 *
 * The function of this class is to create a list of url's which direct to the right servers that have the desired layers
 * for WMS requests
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.ServiceDomainResource;
import nl.b3p.kaartenbalie.core.server.ServiceProvider;
import nl.b3p.kaartenbalie.core.server.User;

public class GetMapRequestHandler extends WMSRequestHandler {
    
    public GetMapRequestHandler() {}
    
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
            spUrl = calcRequestUrl(s, WMS_REQUEST_GetMap);
            if (spUrl==null)
                continue;
            
            // toevoegen van andere parameters
            spUrl.append(WMS_VERSION);
            spUrl.append("=");
            spUrl.append((String)parameters.get(WMS_VERSION));
            spUrl.append("&");
            spUrl.append(WMS_REQUEST);
            spUrl.append("=");
            spUrl.append(WMS_REQUEST_GetMap);
            spUrl.append("&");
            spUrl.append(WMS_PARAM_LAYERS);
            spUrl.append("=");
            spUrl.append(spls);
            
            spUrl.append("&");
            spUrl.append(WMS_PARAM_BBOX);
            spUrl.append("=");
            spUrl.append((String)parameters.get(WMS_PARAM_BBOX));
            
            spUrl.append("&");
            spUrl.append(WMS_PARAM_SRS);
            spUrl.append("=");
            spUrl.append((String)parameters.get(WMS_PARAM_SRS));
            
            spUrl.append("&");
            spUrl.append(WMS_PARAM_TRANSPARENT);
            spUrl.append("=");
            spUrl.append(WMS_PARAM_TRANSPARENT_TRUE);
            
            spUrl.append("&");
            spUrl.append(WMS_PARAM_FORMAT);
            spUrl.append("=");
            spUrl.append((String)parameters.get(WMS_PARAM_FORMAT));
            
            spUrl.append("&");
            spUrl.append(WMS_PARAM_WIDTH);
            spUrl.append("=");
            spUrl.append((String)parameters.get(WMS_PARAM_WIDTH));
            
            spUrl.append("&");
            spUrl.append(WMS_PARAM_HEIGHT);
            spUrl.append("=");
            spUrl.append((String)parameters.get(WMS_PARAM_HEIGHT));
        }
        
        StringBuffer [] url = null;
        return getOnlineData((StringBuffer[])urls.toArray(url));
        

    }
}