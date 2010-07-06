/*
 * B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
 * for authentication/authorization, pricing and usage reporting.
 *
 * Copyright 2006, 2007, 2008 B3Partners BV
 * 
 * This file is part of B3P Kaartenbalie.
 * 
 * B3P Kaartenbalie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * B3P Kaartenbalie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.b3p.kaartenbalie.service.requesthandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.monitoring.DataMonitoring;
import nl.b3p.kaartenbalie.core.server.monitoring.ServiceProviderRequest;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.ogc.utils.OGCResponse;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.input.CountingInputStream;
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
        Integer[] orgIds = user.getOrganizationIds();

        String request = ogcrequest.getParameter(OGCConstants.REQUEST);
        layers = ogcrequest.getGetFeatureFilterMap().keySet();
        Iterator it = layers.iterator();
        String[] allLayers = new String[layers.size()];
        int x = 0;
        while (it.hasNext()) {
            String layer = it.next().toString();
            allLayers[x] = layer;
            x++;
        }

        if (allLayers.length < 1) {
            throw new UnsupportedOperationException(request + " request with less then one maplayer is not supported!");
        }

        layerNames = new String[allLayers.length];
        String[] prefixes = new String[allLayers.length];
        for (int i = 0; i < allLayers.length; i++) {
            String[] temp = allLayers[i].split("}");
            if (temp.length > 1) {
                layerNames[i] = temp[1];
                int index1 = allLayers[i].indexOf("{");
                int index2 = allLayers[i].indexOf("}");
                prefixes[i] = ogcrequest.getPrefix(allLayers[i].substring(index1 + 1, index2));
            } else {
                String temp2[] = temp[0].split(":");
                if (temp2.length > 1) {
                    layerNames[i] = temp2[1];
                } else {
                    layerNames[i] = allLayers[i];
                }
            }
        }

        String url = null;
        String prefix = null;

        List spUrls = getSeviceProviderURLS(layerNames, orgIds, false, data);
        if (spUrls == null || spUrls.isEmpty()) {
            throw new Exception("No Serviceprovider available! User might not have rights to any Serviceprovider!");
        }
        spUrls = prepareAccounting(user.getMainOrganizationId(), data, spUrls);
        if (spUrls == null || spUrls.isEmpty()) {
            log.error("No urls qualify for request.");
            throw new Exception("No Serviceprovider available! User might not have rights to any Serviceprovider!");
        }
        if (spUrls.size() > 1) {
            log.error("More then 1 service provider addressed. Not supported (yet)");
            throw new Exception("More then 1 service provider addressed. Not supported (yet)");
        }
        String layerParam = "";
        for (int i = 0; i < layerNames.length; i++) {
            if (layerParam.length() != 0) {
                layerParam += ",";
            }
            if (prefixes[i] != null) {
                layerParam += prefixes[i] + ":";
            }
            int index1 = layerNames[i].indexOf("_");
            if (index1 != -1) {
                layerParam += layerNames[i].substring(index1 + 1);
            } else {
                layerParam += layerNames[i];
            }
        }

        Iterator iter = spUrls.iterator();
        List spLayers = new ArrayList();
        while (iter.hasNext()) {
            SpLayerSummary sp = (SpLayerSummary) iter.next();
            String spAbbr = sp.getSpAbbr();
            List tlayers = sp.getLayers();
            if (tlayers == null) {
                String layerName = sp.getLayerName();
                HashMap layer = new HashMap();
                layer.put("spAbbr", spAbbr);
                layer.put("layer", layerName);
                spLayers.add(layer);
                continue;
            }
            Iterator it2 = tlayers.iterator();
            while (it2.hasNext()) {
                String layerName = (String) it2.next();
                HashMap layer = new HashMap();
                layer.put("spAbbr", spAbbr);
                layer.put("layer", layerName);
                spLayers.add(layer);
            }
        }
        if (spLayers == null || spLayers.size() == 0) {
            throw new UnsupportedOperationException("No Serviceprovider for this service available!");
        }

        DataMonitoring rr = data.getRequestReporting();
        long startprocestime = System.currentTimeMillis();

        PostMethod method = null;
        HttpClient client = new HttpClient();
        client.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_0);
        client.getHttpConnectionManager().getParams().setConnectionTimeout((int) maxResponseTime);
        OutputStream os = data.getOutputStream();

        Iterator spIt = spUrls.iterator();
        while (spIt.hasNext()) {
            SpLayerSummary sp = (SpLayerSummary) spIt.next();
            url = sp.getSpUrl();
            prefix = sp.getSpAbbr();
            ogcrequest.addOrReplaceParameter(OGCConstants.WFS_PARAM_TYPENAME, layerParam);
            String filter = ogcrequest.getGetFeatureFilter(sp.getSpAbbr() + "_" + sp.getLayerName());
            if (filter != null) {
                ogcrequest.addOrReplaceParameter(OGCConstants.WFS_PARAM_FILTER, filter);
            }
            String propertyNames = ogcrequest.getGetFeaturePropertyNameList(sp.getSpAbbr() + "_" + sp.getLayerName());
            if (propertyNames != null) {
                ogcrequest.addOrReplaceParameter(OGCConstants.WFS_PARAM_PROPERTYNAME, propertyNames);
            }

            String body = data.getOgcrequest().getXMLBody();
            // TODO body cleanen

            ServiceProviderRequest wfsRequest = this.createServiceProviderRequest(
                    data, url, sp.getServiceproviderId(), new Long(body.getBytes().length));

            String host = url;
            method = new PostMethod(host);
            //work around voor ESRI post Messages
            //method.setRequestEntity(new StringRequestEntity(body, "text/xml", "UTF-8"));
            method.setRequestEntity(new StringRequestEntity(body, null, null));
            int status = client.executeMethod(method);
            try {
                if (status == HttpStatus.SC_OK) {
                    wfsRequest.setResponseStatus(new Integer(200));
                    wfsRequest.setRequestResponseTime(System.currentTimeMillis() - startprocestime);

                    data.setContentType("text/xml");
                    InputStream is = method.getResponseBodyAsStream();
                    InputStream isx = null;
                    byte[] bytes = null;
                    if (KBConfiguration.SAVE_MESSAGES) {
                        int len = 1;
                        byte[] buffer = new byte[2024];
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        while ((len = is.read(buffer, 0, buffer.length)) > 0) {
                            bos.write(buffer, 0, len);
                        }
                        bytes = bos.toByteArray();
                        isx = new ByteArrayInputStream(bytes);
                    } else {
                        isx = new CountingInputStream(is);
                    }

                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = dbf.newDocumentBuilder();
                    Document doc = builder.parse(isx);

                    if (KBConfiguration.SAVE_MESSAGES) {
                        wfsRequest.setMessageSent(body);
                        wfsRequest.setMessageReceived(new String(bytes));
                        wfsRequest.setBytesReceived(new Long(bytes.length));
                    } else {
                        wfsRequest.setBytesReceived(new Long(((CountingInputStream) isx).getCount()));
                    }

                    ogcresponse.rebuildResponse(doc.getDocumentElement(), data.getOgcrequest(), prefix);
                } else {
                    wfsRequest.setResponseStatus(status);
                    wfsRequest.setExceptionMessage("" + status + ": Failed to connect with " + url + " Using body: " + body);
                    wfsRequest.setExceptionClass(UnsupportedOperationException.class);

                    log.error("Failed to connect with " + url + " Using body: " + body);
                    throw new UnsupportedOperationException("Failed to connect with " + url + " Using body: " + body);
                }
            } catch (Exception e) {
                wfsRequest.setExceptionMessage("Failed to send bytes to client: " + e.getMessage());
                wfsRequest.setExceptionClass(e.getClass());

                throw e;
            } finally {
                rr.addServiceProviderRequest(wfsRequest);
            }
        }
        doAccounting(user.getMainOrganizationId(), data, user);
        String responseBody = ogcresponse.getResponseBody(spLayers);
        if (responseBody != null && !responseBody.equals("")) {
            byte[] buffer = responseBody.getBytes();
            os.write(buffer);
        } else {
            throw new UnsupportedOperationException("XMLbody empty!");
        }
    }
}
