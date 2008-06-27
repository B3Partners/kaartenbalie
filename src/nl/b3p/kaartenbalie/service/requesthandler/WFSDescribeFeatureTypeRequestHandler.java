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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
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
import org.w3c.dom.Document;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jytte
 */
public class WFSDescribeFeatureTypeRequestHandler extends WFSRequestHandler {

    private static final Log log = LogFactory.getLog(WFSDescribeFeatureTypeRequestHandler.class);

    /** Creates a new instance of WFSRequestHandler */
    public WFSDescribeFeatureTypeRequestHandler() {
    }

    public void getRequest(DataWrapper data, User user) throws IOException, Exception {

        OGCResponse ogcresponse = new OGCResponse();
        OGCRequest ogcrequest = data.getOgcrequest();
        String layers = null;
        String[] layerNames = null;
        Integer orgId = user.getOrganization().getId();

        String request = ogcrequest.getParameter(OGCConstants.REQUEST);
        layers = ogcrequest.getParameter(OGCConstants.WFS_PARAM_TYPENAME);
        String[] allLayers = layers.split(",");

        if (allLayers.length > 1) {
            throw new UnsupportedOperationException(request + " request with more then one maplayer is not supported yet!");
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
        List spInfo = null;
        String prefix = null;

        spInfo = getSeviceProviderURLS(layerNames, orgId, false, data);
        if (spInfo == null || spInfo.isEmpty()) {
            throw new UnsupportedOperationException("No Serviceprovider available! User might not have rights to any Serviceprovider!");
        }
        
        Iterator iter = spInfo.iterator();
        while (iter.hasNext()) {
            HashMap sp = (HashMap) iter.next();
            url = sp.get("spUrl").toString();
            prefix = sp.get("spAbbr").toString();
            ogcrequest.addOrReplaceParameter(OGCConstants.WFS_PARAM_TYPENAME, "app:" + sp.get("lName"));
        }

        if (url == null || url == "") {
            throw new UnsupportedOperationException("No Serviceprovider for this service available!");
        }
        PostMethod method = null;
        HttpClient client = new HttpClient();
        OutputStream os = data.getOutputStream();
        String body = data.getOgcrequest().getXMLBody();

        String host = url;
        method = new PostMethod(host);
        method.setRequestEntity(new StringRequestEntity(body, "text/xml", "UTF-8"));
        int status = client.executeMethod(method);
        if (status == HttpStatus.SC_OK) {
            data.setContentType("text/xml");
            InputStream is = method.getResponseBodyAsStream();

            /*
             * Nothing has to be done with DescribeFeatureType so it will be sent to the client at once.
             */
            int len = 1;
            byte[] buffer = new byte[2024];
            while ((len = is.read(buffer, 0, len)) > 0) {
                os.write(buffer, 0, len);
            }
        } else {
            log.error("Failed to connect with " + url + " Using body: " + body);
            throw new UnsupportedOperationException("Failed to connect with " + url + " Using body: " + body);
        }
    }
}
