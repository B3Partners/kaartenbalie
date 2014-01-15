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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.ogc.utils.LayerSummary;
import nl.b3p.ogc.utils.OGCCommunication;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.ogc.utils.OGCResponse;
import nl.b3p.ogc.utils.SpLayerSummary;
import nl.b3p.ogc.utils.WFSGetFeatureResponse;
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
   
    private String repairIntersects(String version, String filter) {
        if (filter !=null && version != null && version.equals(OGCConstants.WFS_VERSION_100)) {
            // check of geotools onterecht intersects (met s) heeft gebruikt bij wfs 1.0.0
            // later oplossen in geotools
            // nu wat testen en hier fixen
            if (filter.contains("<Intersects>") && filter.contains("</Intersects>")) {
                return filter.replace("<Intersects>", "<Intersect>").replace("</Intersects>", "</Intersect>");
            }
        }
        return filter;
    }

    public String prepareRequest4Sp(OGCRequest ogcrequest, SpLayerSummary sp) {
        
        if (ogcrequest.getHttpMethod().equalsIgnoreCase("POST")) {
            LayerSummary l = null;
            try {
                l = OGCCommunication.splitLayerWithoutNsFix(sp.getLayerName(), false, sp.getSpAbbr(), null);
            } catch (Exception ex) {
                // ignore
            }
        
            if (l != null) {
                String filter = ogcrequest.getGetFeatureFilter(OGCCommunication.buildFullLayerName(l));
                filter = repairIntersects(ogcrequest.getFinalVersion(), filter);
                if (filter != null) {
                    ogcrequest.addOrReplaceParameter(OGCConstants.WFS_PARAM_FILTER, filter);
                }
                String propertyNames = ogcrequest.getGetFeaturePropertyNameList(OGCCommunication.buildFullLayerName(l));
                if (propertyNames != null) {
                    ogcrequest.addOrReplaceParameter(OGCConstants.WFS_PARAM_PROPERTYNAME, propertyNames);
                }
            }

        } else { // get
            String filter = ogcrequest.getParameter(OGCConstants.WFS_PARAM_FILTER);
            filter = repairIntersects(ogcrequest.getFinalVersion(), filter);
            ogcrequest.addOrReplaceParameter(OGCConstants.WFS_PARAM_FILTER, filter);
        }

        String layerParam = "";
        List<LayerSummary> lsl = sp.getLayers();
        for (LayerSummary ls : lsl) {
            if (!layerParam.isEmpty()) {
                layerParam += ",";
            }
            layerParam += OGCCommunication.buildLayerNameWithoutSp(ls);
        }

        ogcrequest.addOrReplaceParameter(OGCConstants.WFS_PARAM_TYPENAME, layerParam);
        return null;
    }
    
    public List<LayerSummary> prepareRequestLayers(OGCRequest ogcrequest) throws Exception {
        List<String> allLayers = null;
        if (ogcrequest.getHttpMethod().equals("POST")) {
            Set layers = ogcrequest.getGetFeatureFilterMap().keySet();
            allLayers.addAll(layers);
        } else {
            String typeName = ogcrequest.getParameter(OGCConstants.WFS_PARAM_TYPENAME);
            allLayers = Arrays.asList(typeName.split(","));
        }

        String spInUrl = ogcrequest.getServiceProviderName();
        
        List<LayerSummary> lsl = LayerSummary.createLayerSummaryList(allLayers,
                ogcrequest.getServiceProviderName(), (spInUrl==null));
        
        if (!checkNumberOfSps(lsl, 1)) {
            log.error("More then 1 service provider addressed. Not supported (yet)");
            throw new UnsupportedOperationException("More then 1 service provider addressed. Not supported (yet)");
        }
        return lsl;
    }

    public OGCResponse getNewOGCResponse() {
        return new WFSGetFeatureResponse();
    }

    public void getRequest(DataWrapper data, User user) throws IOException, Exception {
        writeResponse(data, user);
    }
/*     
    public void getRequestOld(DataWrapper data, User user) throws IOException, Exception {
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
        
        //als sp in url dan splitten met splitName = false
        boolean splitName = true;
        String spName = ogcrequest.getServiceProviderName();
        if (spName != null) {
            splitName = false;
        }
                
        List requestLayers = new ArrayList();
        for (int i = 0; i < allLayers.length; i++) {
            LayerSummary splitLayer = ogcrequest.splitLayerInParts(allLayers[i], splitName, spName, null);
            requestLayers.add(splitLayer);
        }

        // voor opzoeken in kaartenbalie, rechten en zo, met sp
        String[] layerNames = new String[requestLayers.size()];
        for (int i = 0; i < requestLayers.size(); i++) {
            LayerSummary rl = (LayerSummary)requestLayers.get(i);
            layerNames[i] = OGCCommunication.buildLayerNameWithoutNs(rl);
        }

        List spUrls = getServiceProviderURLS(layerNames, orgIds, false, data);
        if (spUrls == null || spUrls.isEmpty()) {
            throw new Exception("No Serviceprovider available! User might not have rights to any Serviceprovider!");
        }
        spUrls = prepareAccounting(user.getMainOrganizationId(), data, spUrls);
        if (spUrls == null || spUrls.isEmpty()) {
            log.error("No urls qualify for request.");
            throw new Exception("No Serviceprovider available! User might not have rights to any Serviceprovider!");
        }

 
        OGCResponse ogcresponse = null;

        DataMonitoring rr = data.getRequestReporting();
        long startprocestime = System.currentTimeMillis();

        HttpClient client;
        B3PCredentials credentials;
        OutputStream os = data.getOutputStream();

        String spUrl = null;
        String spAbbr = null;
        Iterator spIt = spUrls.iterator();
        // voor vervangen in response, alleen sp indien niet in url
        List<LayerSummary> spLayers = new ArrayList();
        while (spIt.hasNext()) {
            SpLayerSummary sp = (SpLayerSummary) spIt.next();
            spUrl = sp.getSpUrl();
            spAbbr = sp.getSpAbbr();
            if (spName != null) {
                if (spName.equalsIgnoreCase(spAbbr)) {
                    spAbbr = null;
                } else {
                    // sp in url, andere sp dan weglaten
                    continue;
                }
            }
            List tlayers = sp.getLayers();
            if (tlayers == null) {
                String layerName = sp.getLayerName();
                     LayerSummary layer = new LayerSummary();
                    layer.setSpAbbr(spAbbr);
                    layer.setLayerName(layerName);
                spLayers.add(layer);
                continue;
            }
            Iterator it2 = tlayers.iterator();
            while (it2.hasNext()) {
                String layerName = (String) it2.next();
                     LayerSummary layer = new LayerSummary();
                    layer.setSpAbbr(spAbbr);
                    layer.setLayerName(layerName);
               spLayers.add(layer);
            }
            
            // voor url naar backend, zonder sp
            String layerParam = "";
            for (int i = 0; i < requestLayers.size(); i++) {
                LayerSummary rl = (LayerSummary) requestLayers.get(i);
                if (layerParam.length() != 0) {
                    layerParam += ",";
                }
                layerParam += ogcrequest.buildLayerNameWithoutSp(rl);
            }

            OGCRequest spOgcReq = (OGCRequest)ogcrequest.clone();
            spOgcReq.addOrReplaceParameter(OGCConstants.WFS_PARAM_TYPENAME, layerParam);

            ServiceProviderRequest wfsRequest = this.createServiceProviderRequest(
                    data, spUrl, sp.getServiceproviderId(), 0l);
            
            credentials = new B3PCredentials();
            credentials.setUserName(sp.getUsername());
            credentials.setPassword(sp.getPassword());
            
            client = CredentialsParser.CommonsHttpClientCredentials(credentials, CredentialsParser.HOST, CredentialsParser.PORT, (int) maxResponseTime, HttpVersion.HTTP_1_0);

            HttpMethod  method = null;
            if (spOgcReq.getHttpMethod().equalsIgnoreCase("POST")) {

                String filter = spOgcReq.getGetFeatureFilter(OGCCommunication.attachSp(sp.getSpAbbr(),sp.getLayerName()));
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
                String propertyNames = spOgcReq.getGetFeaturePropertyNameList(OGCCommunication.attachSp(sp.getSpAbbr(),sp.getLayerName()));
                if (propertyNames != null) {
                    spOgcReq.addOrReplaceParameter(OGCConstants.WFS_PARAM_PROPERTYNAME, propertyNames);
                }

                String body = spOgcReq.getXMLBody();
                log.debug("WFS POST to serviceprovider: '" + spAbbr + "' with url: '" + spUrl + "' and body:");
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

                String getUrl = spOgcReq.getUrl(spOgcReq.fixHttpHost(spUrl));
                
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

                log.debug("WFS GET to serviceprovider: '" + spAbbr + "' with url: '" + getUrl.toString() + "'");
                
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

                    if (OGCResponse.isErrorResponse(doc.getDocumentElement())) {
                        ogcresponse = new WFSExceptionResponse();
                    } else {
                        ogcresponse = new WFSGetFeatureResponse();
                    }
                     if (ogcresponse == null) {
                        throw new UnsupportedOperationException("Unknown response!");
                    }
                    ogcresponse.findNameSpace(doc);

                    if (KBConfiguration.SAVE_MESSAGES) {
                        wfsRequest.setMessageReceived(new String(bytes));
                        wfsRequest.setBytesReceived(new Long(bytes.length));
                    } else {
                        wfsRequest.setBytesReceived(new Long(((CountingInputStream) isx).getCount()));
                    }

                        ogcresponse.rebuildResponse(doc.getDocumentElement(), spOgcReq, spAbbr);
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
        if (spLayers == null || spLayers.isEmpty()) {
            throw new UnsupportedOperationException("No Serviceprovider for this service available!");
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
    */
}
