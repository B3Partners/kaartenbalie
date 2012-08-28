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
import javax.persistence.EntityManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import nl.b3p.gis.B3PCredentials;
import nl.b3p.gis.CredentialsParser;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.monitoring.DataMonitoring;
import nl.b3p.kaartenbalie.core.server.monitoring.ServiceProviderRequest;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.ogc.utils.OGCResponse;
import nl.b3p.wms.capabilities.Roles;
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
public class WFSGetCapabilitiesRequestHandler extends WFSRequestHandler {

    private static final Log log = LogFactory.getLog(WFSGetCapabilitiesRequestHandler.class);

    /** Creates a new instance of WFSRequestHandler */
    public WFSGetCapabilitiesRequestHandler() {
    }

    public void getRequest(DataWrapper data, User user) throws IOException, Exception {

        OGCResponse ogcresponse = new OGCResponse();
        Integer[] orgIds = user.getOrganizationIds();

        String lurl = null;
        List spInfo = null;
        String prefix = null;

        EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);

        //String version = this.getVersion(ogcrequest);
        String version = null;

        /*
         * Only used if specific param is given (used for configuration)
         */
        boolean isAdmin = false;
        if ("true".equalsIgnoreCase(data.getOgcrequest().getParameter("_VIEWER_CONFIG"))) {
            Set userRoles = user.getRoles();
            Iterator rolIt = userRoles.iterator();
            while (rolIt.hasNext()) {
                Roles role = (Roles) rolIt.next();
                if (role.getRole().equalsIgnoreCase(Roles.ADMIN)) {
                    /* de gebruiker is een beheerder */
                    isAdmin = true;
                    break;
                }
            }
        }

        // TODO: hieronder wordt iets dubbel gedaan
        // rol wordt 2x gebruikt om te testen
        // een organisatiebeheerder krijgt alleen de kaarten van zijn eigen organisatie
        String[] layerNames = getOrganisationLayers(em, orgIds, version, isAdmin);

        String serviceName  = data.getOgcrequest().getServiceName();
        if (isAdmin) {
            spInfo = getLayerSummaries(layerNames,serviceName);
        } else {
            spInfo = getSeviceProviderURLS(layerNames, orgIds, false, data);
        }
        
        boolean hasServiceProviderCode = false;
        if(data.getServiceProviderCode() != null && !data.getServiceProviderCode().equals("")){
            hasServiceProviderCode = true;
        }

        if (spInfo == null || spInfo.isEmpty()) {
            throw new UnsupportedOperationException("No Serviceprovider available! User might not have rights to any Serviceprovider!");
        }
        Iterator iter = spInfo.iterator();
        List spLayers = new ArrayList();
        while (iter.hasNext()) {
            SpLayerSummary sp = (SpLayerSummary) iter.next();
            String spAbbr = sp.getSpAbbr();
            List layers = sp.getLayers();
            if (layers == null) {
                String layerName = sp.getLayerName();
                HashMap layer = new HashMap();
                if(!hasServiceProviderCode){
                    layer.put("spAbbr", spAbbr);
                }else{
                    layer.put("spAbbr", ""); 
                }
                layer.put("layer", layerName);
                spLayers.add(layer);
                continue;
            }
            Iterator it2 = layers.iterator();
            while (it2.hasNext()) {
                String layerName = (String) it2.next();
                HashMap layer = new HashMap();
                if(!hasServiceProviderCode){
                    layer.put("spAbbr", spAbbr);
                }else{
                    layer.put("spAbbr", ""); 
                }
                layer.put("layer", layerName);
                spLayers.add(layer);
            }
        }
        if (spLayers == null || spLayers.isEmpty()) {
            throw new UnsupportedOperationException("No Serviceprovider for this service available!");
        }

        HttpMethod method = null;
            
        OutputStream os = data.getOutputStream();
        String body = data.getOgcrequest().getXMLBody();
        // TODO body cleanen

        String WFSVersionUsed = null;
        if (data.getOgcrequest().getParameter(OGCConstants.WMS_VERSION) != null) {
            WFSVersionUsed = data.getOgcrequest().getParameter(OGCConstants.WMS_VERSION);
        } else if (spInfo != null && spInfo.size() > 1) {
            // assume that wfs 1.0.0 will be supported by all providers
            WFSVersionUsed = OGCConstants.WFS_VERSION_100;
        }

        DataMonitoring rr = data.getRequestReporting();
        long startprocestime = System.currentTimeMillis();

        Iterator it = spInfo.iterator();
        List servers = new ArrayList();
        while (it.hasNext()) {
            SpLayerSummary sp = (SpLayerSummary) it.next();
            if (servers.contains(sp.getSpAbbr())) {
                continue;
            }
            B3PCredentials credentials = new B3PCredentials();
            credentials.setUserName(sp.getUsername());
            credentials.setPassword(sp.getPassword());
            
        HttpClient client = CredentialsParser.CommonsHttpClientCredentials(credentials, CredentialsParser.HOST, CredentialsParser.PORT, (int) maxResponseTime);
            
            servers.add(sp.getSpAbbr());
            lurl = sp.getSpUrl();
            prefix = sp.getSpAbbr();

            ServiceProviderRequest wfsRequest = this.createServiceProviderRequest(
                    data, lurl, sp.getServiceproviderId(), new Long(body.getBytes().length));

            String host = lurl;
            OGCRequest or = new OGCRequest(host);
            or.addOrReplaceParameter(OGCConstants.WMS_REQUEST, OGCConstants.WFS_REQUEST_GetCapabilities);
            or.addOrReplaceParameter(OGCConstants.SERVICE, OGCConstants.WMS_PARAM_WFS);
            if (WFSVersionUsed != null) {
                or.addOrReplaceParameter(OGCConstants.VERSION, WFSVersionUsed);
            }
            
            method = new GetMethod(or.getUrl());
            int status = client.executeMethod(method);
            if (status != HttpStatus.SC_OK) {
                method = new PostMethod(host);
                //work around voor ESRI post Messages
                //((PostMethod)method).setRequestEntity(new StringRequestEntity(body, "text/xml", "UTF-8"));
                ((PostMethod) method).setRequestEntity(new StringRequestEntity(body, null, null));
                status = client.executeMethod(method);
            }
            try {
                if (status == HttpStatus.SC_OK) {
                    if (method instanceof PostMethod) {
                        wfsRequest.setBytesReceived(new Long(((PostMethod) method).getResponseContentLength()));
                    }
                    if (method instanceof GetMethod) {
                        wfsRequest.setBytesReceived(new Long(((GetMethod) method).getResponseContentLength()));
                    }
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
                    } else {
                        wfsRequest.setBytesReceived(new Long(((CountingInputStream) isx).getCount()));
                    }

                    if(hasServiceProviderCode){
                        ogcresponse.rebuildResponse(doc.getDocumentElement(), data.getOgcrequest(), "");
                    }else{
                        ogcresponse.rebuildResponse(doc.getDocumentElement(), data.getOgcrequest(), prefix);
                    }
                } else {
                    wfsRequest.setResponseStatus(status);
                    wfsRequest.setExceptionMessage("Failed to connect with " + lurl + " Using body: " + body);
                    wfsRequest.setExceptionClass(UnsupportedOperationException.class);

                    log.error("Failed to connect with " + lurl + " Using body: " + body);
                    throw new UnsupportedOperationException("Failed to connect with " + lurl + " Using body: " + body);
                }
            } catch (Exception e) {
                wfsRequest.setExceptionMessage("Failed to send bytes to client: " + e.getMessage());
                wfsRequest.setExceptionClass(e.getClass());

                throw e;
            } finally {
                rr.addServiceProviderRequest(wfsRequest);
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
