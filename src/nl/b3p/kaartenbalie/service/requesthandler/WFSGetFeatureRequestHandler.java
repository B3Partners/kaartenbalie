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
import java.util.Map;
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
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.methods.GetMethod;
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
        OGCRequest ogcrequest = data.getOgcrequest();
        Integer[] orgIds = user.getOrganizationIds();

        String request = ogcrequest.getParameter(OGCConstants.REQUEST);

        String[] allLayers = null;
        if (ogcrequest.getHttpMethod().equals("POST")) {
            Set layers = ogcrequest.getGetFeatureFilterMap().keySet();
            Iterator it = layers.iterator();
            allLayers = new String[layers.size()];
            int x = 0;
            while (it.hasNext()) {
                String layer = it.next().toString();
                allLayers[x] = layer;
                x++;
            }
        } else {
            String typeName = ogcrequest.getParameter(OGCConstants.WFS_PARAM_TYPENAME);
            allLayers = typeName.split(",");
        }

        if (allLayers.length < 1) {
            throw new UnsupportedOperationException(request + " request with less then one maplayer is not supported!");
        }

        List requestLayers = new ArrayList();
        for (int i = 0; i < allLayers.length; i++) {
             requestLayers.add(ogcrequest.splitLayerInParts(allLayers[i], true));
        }

        String[] layerNames = new String[requestLayers.size()];
        for (int i = 0; i < requestLayers.size(); i++) {
            Map rl = (Map)requestLayers.get(i);
            layerNames[i] = (String)rl.get("spLayerName");
        }

        List spUrls = getSeviceProviderURLS(layerNames, orgIds, false, data);
        if (spUrls == null || spUrls.isEmpty()) {
            throw new Exception("No Serviceprovider available! User might not have rights to any Serviceprovider!");
        }
        spUrls = prepareAccounting(user.getMainOrganizationId(), data, spUrls);
        if (spUrls == null || spUrls.isEmpty()) {
            log.error("No urls qualify for request.");
            throw new Exception("No Serviceprovider available! User might not have rights to any Serviceprovider!");
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

        OGCResponse ogcresponse = new OGCResponse();

        DataMonitoring rr = data.getRequestReporting();
        long startprocestime = System.currentTimeMillis();

        HttpClient client = new HttpClient();
        client.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_0);
        client.getHttpConnectionManager().getParams().setConnectionTimeout((int) maxResponseTime);
        OutputStream os = data.getOutputStream();

        String spUrl = null;
        String prefix = null;
        Iterator spIt = spUrls.iterator();
        while (spIt.hasNext()) {
            SpLayerSummary sp = (SpLayerSummary) spIt.next();
            spUrl = sp.getSpUrl();
            prefix = sp.getSpAbbr();

            String layerParam = "";
            for (int i = 0; i < requestLayers.size(); i++) {
                Map rl = (Map) requestLayers.get(i);
                if (prefix.equals(rl.get("spAbbr"))) {
                    if (layerParam.length() != 0) {
                        layerParam += ",";
                    }
                    String nsPrefix = (String) rl.get("prefix");
                    if (nsPrefix != null) {
                        layerParam += nsPrefix + ":";
                    }
                    layerParam += (String) rl.get("layerName");
                }
             }

            OGCRequest spOgcReq = (OGCRequest)ogcrequest.clone();
            spOgcReq.addOrReplaceParameter(OGCConstants.WFS_PARAM_TYPENAME, layerParam);

            ServiceProviderRequest wfsRequest = this.createServiceProviderRequest(
                    data, spUrl, sp.getServiceproviderId(), 0l);

            HttpMethod  method = null;
            if (spOgcReq.getHttpMethod().equalsIgnoreCase("POST")) {

                String filter = spOgcReq.getGetFeatureFilter(completeLayerName(sp.getSpAbbr(),sp.getLayerName()));
                if (filter != null) {
                    String version = spOgcReq.getFinalVersion();
                    if (version!=null && version.equals(OGCConstants.WFS_VERSION_100)) {
                        // check of geotools onterecht intersects (met s) heeft gebruikt bij wfs 1.0.0
                        // later oplossen in geotools
                        // nu wat testen en hier fixen
                        if (filter.contains("<Intersects>") && filter.contains("</Intersects>")) {
                            filter = filter.replace("<Intersects>", "<Intersect>").replace("</Intersects>", "</Intersect>");
                        }
                    }
                    spOgcReq.addOrReplaceParameter(OGCConstants.WFS_PARAM_FILTER, filter);
                }
                String propertyNames = spOgcReq.getGetFeaturePropertyNameList(completeLayerName(sp.getSpAbbr(),sp.getLayerName()));
                if (propertyNames != null) {
                    spOgcReq.addOrReplaceParameter(OGCConstants.WFS_PARAM_PROPERTYNAME, propertyNames);
                }

                String body = spOgcReq.getXMLBody();
                log.debug("WFS POST to serviceprovider: '" + prefix + "' with url: '" + spUrl + "' and body:");
                log.debug(body);
                // TODO body cleanen
                if (KBConfiguration.SAVE_MESSAGES) {
                    wfsRequest.setMessageSent(body);
                }
                wfsRequest.setBytesSent(new Long(body.getBytes().length));

                method = new PostMethod(spUrl);
                //work around voor ESRI post Messages
                //method.setRequestEntity(new StringRequestEntity(body, "text/xml", "UTF-8"));
                ((PostMethod)method).setRequestEntity(new StringRequestEntity(body, null, null));
            } else { // get

                StringBuffer getHost = new StringBuffer(spUrl);
                if (getHost.indexOf("?") != getHost.length() - 1 && getHost.indexOf("&") != getHost.length() - 1) {
                    if (getHost.indexOf("?") >= 0) {
                        getHost.append("&");
                    } else {
                        getHost.append("?");
                    }
                }

                String getUrl = spOgcReq.getUrl(getHost.toString());
                
                if (getUrl.contains(OGCConstants.WFS_PARAM_FILTER)) {
                    String version = spOgcReq.getFinalVersion();
                    if (version != null && version.equals(OGCConstants.WFS_VERSION_100)) {
                        // check of geotools onterecht intersects (met s) heeft gebruikt bij wfs 1.0.0
                        // later oplossen in geotools
                        // nu wat testen en hier fixen
                        if (getUrl.contains("<Intersects>") && getUrl.contains("</Intersects>")) {
                            getUrl = getUrl.replace("<Intersects>", "<Intersect>").replace("</Intersects>", "</Intersect>");
                        }
                    }
                }

                log.debug("WFS GET to serviceprovider: '" + prefix + "' with url: '" + getUrl.toString() + "'");
                
                if (KBConfiguration.SAVE_MESSAGES) {
                    wfsRequest.setMessageSent(getUrl.toString());
                }
                wfsRequest.setBytesSent(new Long(getUrl.length()));
                method = new GetMethod(getUrl.toString());
            }
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
                    dbf.setNamespaceAware(true);
                    DocumentBuilder builder = dbf.newDocumentBuilder();
                    Document doc = builder.parse(isx);

                    ogcresponse.findNameSpace(doc);

                    if (KBConfiguration.SAVE_MESSAGES) {
                        wfsRequest.setMessageReceived(new String(bytes));
                        wfsRequest.setBytesReceived(new Long(bytes.length));
                    } else {
                        wfsRequest.setBytesReceived(new Long(((CountingInputStream) isx).getCount()));
                    }

                    ogcresponse.rebuildResponse(doc.getDocumentElement(), spOgcReq, prefix);
                } else {
                    wfsRequest.setResponseStatus(status);
                    wfsRequest.setExceptionMessage("" + status + ": Failed to connect with " + spUrl);
                    wfsRequest.setExceptionClass(UnsupportedOperationException.class);

                    log.error("Failed to connect with " + spUrl);
                    throw new UnsupportedOperationException("Failed to connect with " + spUrl);
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
        String responseBody = null;
        if (ogcresponse!=null) {
            responseBody = ogcresponse.getResponseBody(spLayers);
        }
        if (responseBody != null && !responseBody.equals("")) {
            byte[] buffer = responseBody.getBytes();
            os.write(buffer);
        } else {
            throw new UnsupportedOperationException("XMLbody empty!");
        }
    }
}
