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
import java.io.StringReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.ogc.utils.OGCResponse;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.exolab.castor.xml.Unmarshaller;
import org.w3c.dom.Document;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jytte
 */
public class WFSGetFeatureRequestHandler extends WFSRequestHandler {
    
    private static final Log log = LogFactory.getLog(WFSGetFeatureRequestHandler.class);
    
    /** Creates a new instance of WFSRequestHandler */
    public WFSGetFeatureRequestHandler() {
    }
    
    public void getRequest(DataWrapper data, User user) throws IOException, Exception {
        OGCResponse ogcresponse = new OGCResponse();
        OGCRequest ogcrequest = data.getOgcrequest();
        Set layers = null;
        String[] layerNames = null;
        Integer orgId = user.getOrganization().getId();
        
        String request = ogcrequest.getParameter(OGCConstants.REQUEST);
        layers = ogcrequest.getGetFeatureMap().keySet();
        Iterator it = layers.iterator();
        String[] allLayers = new String[layers.size()];
        int x = 0;
        while(it.hasNext()){
            String layer = it.next().toString();
            allLayers[x] = layer;
            x++;
        }
        
        if (allLayers.length < 1) {
            throw new UnsupportedOperationException(request + " request with less then one maplayer is not supported!");
        }
        
        layerNames = new String[allLayers.length];
        for (int i = 0; i < allLayers.length; i++) {
            String[] temp = allLayers[i].split("}");
            if (temp.length > 1) {
                layerNames[i] = temp[1];
            } else {
                String temp2[] = temp[0].split(":");
                if (temp2.length < 1) {
                    layerNames[i] = temp2[1];
                } else {
                    layerNames[i] = allLayers[i];
                }
            }
        }
        
        String url = null;
        String prefix = null;
        
        List spUrls = getSeviceProviderURLS(layerNames, orgId, false, data);
        if (spUrls == null || spUrls.isEmpty()) {
            throw new UnsupportedOperationException("No Serviceprovider available! User might not have rights to any Serviceprovider!");
        }
        spUrls = prepareAccounting(orgId, data, spUrls);
        if(spUrls==null || spUrls.isEmpty()) {
            log.error("No urls qualify for request.");
            throw new UnsupportedOperationException("No Serviceprovider available! User might not have rights to any Serviceprovider!");
        }
        
        List spLayers = new ArrayList();
        Iterator iter = spUrls.iterator();
        while (iter.hasNext()) {
            SpLayerSummary sp = (SpLayerSummary) iter.next();
            HashMap layer = new HashMap();
            layer.put("spAbbr", sp.getSpAbbr());
            layer.put("layer", sp.getLayerName());
            spLayers.add(layer);
        }
        
        if (spLayers == null || spLayers.size()==0) {
            throw new UnsupportedOperationException("No Serviceprovider for this service available!");
        }
        PostMethod method = null;
        HttpClient client = new HttpClient();
        OutputStream os = data.getOutputStream();
        
        Iterator spIt = spUrls.iterator();
        while (spIt.hasNext()) {
            SpLayerSummary sp = (SpLayerSummary) spIt.next();
            url = sp.getSpUrl();
            prefix = sp.getSpAbbr();
            ogcrequest.addOrReplaceParameter(OGCConstants.WFS_PARAM_TYPENAME, "app:" + sp.getLayerName());
            String filter = ogcrequest.getGetFeatureFilter(sp.getSpAbbr() + "_" + sp.getLayerName());
            if(filter != null){
                ogcrequest.addOrReplaceParameter(OGCConstants.WFS_PARAM_FILTER, filter);
            }
            
            String body = data.getOgcrequest().getXMLBody();
            
            String host = url;
            method = new PostMethod(host);
            method.setRequestEntity(new StringRequestEntity(body, "text/xml", "UTF-8"));
            int status = client.executeMethod(method);
            if (status == HttpStatus.SC_OK) {
                data.setContentType("text/xml");
                InputStream is = method.getResponseBodyAsStream();
                
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = dbf.newDocumentBuilder();
                Document doc = builder.parse(is);
                
                ogcresponse.rebuildResponse(doc.getDocumentElement(), data.getOgcrequest(), prefix);
            } else {
                log.error("Failed to connect with " + url + " Using body: " + body);
                throw new UnsupportedOperationException("Failed to connect with " + url + " Using body: " + body);
            }
        }
        doAccounting(orgId, data, user);
        String responseBody = ogcresponse.getResponseBody(spLayers);
        if (responseBody != null && !responseBody.equals("")) {
            byte[] buffer = responseBody.getBytes();
            os.write(buffer);
        } else {
            throw new UnsupportedOperationException("XMLbody empty!");
        }
    }
}
