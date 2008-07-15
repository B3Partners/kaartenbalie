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
import javax.persistence.EntityManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
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
public class WFSGetCapabilitiesRequestHandler extends WFSRequestHandler {

    private static final Log log = LogFactory.getLog(WFSGetCapabilitiesRequestHandler.class);

    /** Creates a new instance of WFSRequestHandler */
    public WFSGetCapabilitiesRequestHandler() {
    }

    public void getRequest(DataWrapper data, User user) throws IOException, Exception {

        OGCResponse ogcresponse = new OGCResponse();
        OGCRequest ogcrequest = data.getOgcrequest();
        Integer orgId = user.getOrganization().getId();

        String request = ogcrequest.getParameter(OGCConstants.REQUEST);
        String url = null;
        List spInfo = null;
        String prefix = null;

        EntityManager em = MyEMFDatabase.getEntityManager();
        String[] layerNames = getOrganisationLayers(em, orgId);

        spInfo = getSeviceProviderURLS(layerNames, orgId, false, data);
        if (spInfo == null || spInfo.isEmpty()) {
            throw new UnsupportedOperationException("No Serviceprovider available! User might not have rights to any Serviceprovider!");
        }
        Iterator iter = spInfo.iterator();
        List spLayers = new ArrayList();
        while (iter.hasNext()) {
            SpLayerSummary sp = (SpLayerSummary) iter.next();
            HashMap layer = new HashMap();
            layer.put("spAbbr", sp.getSpAbbr());
            layer.put("layer", sp.getLayerName());
            spLayers.add(layer);
        }

        if (spLayers == null || spLayers.size() == 0) {
            throw new UnsupportedOperationException("No Serviceprovider for this service available!");
        }
        PostMethod method = null;
        HttpClient client = new HttpClient();
        OutputStream os = data.getOutputStream();
        String body = data.getOgcrequest().getXMLBody();

        Iterator it = spInfo.iterator();
        List servers = new ArrayList();
        while (it.hasNext()) {
            SpLayerSummary sp = (SpLayerSummary) it.next();

            if (!servers.contains(sp.getSpAbbr())) {
                servers.add(sp.getSpAbbr());
                url = sp.getSpUrl();
                prefix = sp.getSpAbbr();

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
        }
        String responseBody = ogcresponse.getResponseBody(spLayers);
        if (responseBody != null && !responseBody.equals("")) {
            byte[] buffer = responseBody.getBytes();
            os.write(buffer);
        } else {
            throw new UnsupportedOperationException("XMLbody empty!");
        }
    }
}
