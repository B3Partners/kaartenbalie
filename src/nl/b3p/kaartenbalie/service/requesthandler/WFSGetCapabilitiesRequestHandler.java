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

import java.io.BufferedInputStream;
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
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.monitoring.DataMonitoring;
import nl.b3p.kaartenbalie.core.server.monitoring.ServiceProviderRequest;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.ogc.utils.OGCResponse;
import nl.b3p.ogc.wfs.v110.WfsLayer;
import nl.b3p.wms.capabilities.Roles;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
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
        OGCRequest ogcrequest = data.getOgcrequest();
        Integer orgId = user.getOrganization().getId();

        String lurl = null;
        List spInfo = null;
        String prefix = null;

        EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);

        //String version = this.getVersion(ogcrequest);
        String version = null;
        Set userRoles = user.getRoles();
        boolean isAdmin = false;
        Iterator rolIt = userRoles.iterator();
        while (rolIt.hasNext() && !isAdmin) {
            Roles role = (Roles) rolIt.next();
            if (role.getId() == 1 && role.getRole().equalsIgnoreCase("beheerder")) {
                /* de gebruiker is een beheerder */
                isAdmin = true;
            }
        }

        String[] layerNames = getOrganisationLayers(em, orgId, version, isAdmin);

        if (isAdmin == false) {
            spInfo = getSeviceProviderURLS(layerNames, orgId, false, data);
        } else {
            spInfo = getLayerSummaries(layerNames);
        }

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
        HttpMethod method = null;
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout((int) maxResponseTime);
        OutputStream os = data.getOutputStream();
        String body = data.getOgcrequest().getXMLBody();

        DataMonitoring rr = data.getRequestReporting();
        long startprocestime = System.currentTimeMillis();
        
        Iterator it = spInfo.iterator();
        List servers = new ArrayList();
        while (it.hasNext()) {
            SpLayerSummary sp = (SpLayerSummary) it.next();

            if (!servers.contains(sp.getSpAbbr())) {
                servers.add(sp.getSpAbbr());
                lurl = sp.getSpUrl();
                prefix = sp.getSpAbbr();
                
                ServiceProviderRequest wfsRequest = this.createServiceProviderRequest(
    					data, lurl, sp.getServiceproviderId(), new Long(body.getBytes().length));

                String host = lurl;
                OGCRequest or = new OGCRequest(host);
                or.addOrReplaceParameter(OGCConstants.WMS_REQUEST, OGCConstants.WFS_REQUEST_GetCapabilities);
                or.addOrReplaceParameter(OGCConstants.SERVICE, OGCConstants.WMS_PARAM_WFS);
                if(data.getOgcrequest().getParameter(OGCConstants.WMS_VERSION)!=null){
                    or.addOrReplaceParameter(OGCConstants.VERSION, data.getOgcrequest().getParameter(OGCConstants.WMS_VERSION));
                }
                method= new GetMethod(or.getUrl());
                int status = client.executeMethod(method);
                if (status != HttpStatus.SC_OK){
                    method = new PostMethod(host);
                    //work around voor ESRI post Messages
                     //((PostMethod)method).setRequestEntity(new StringRequestEntity(body, "text/xml", "UTF-8"));
                    ((PostMethod)method).setRequestEntity(new StringRequestEntity(body, null,null));
                    status = client.executeMethod(method);
                }
                try {
	                if (status == HttpStatus.SC_OK) {
	                    if (method instanceof PostMethod)
                            wfsRequest.setBytesReceived(new Long(((PostMethod)method).getResponseContentLength()));
                        if (method instanceof GetMethod)
                            wfsRequest.setBytesReceived(new Long(((GetMethod)method).getResponseContentLength()));
	                    wfsRequest.setResponseStatus(new Integer(200));
	                    wfsRequest.setRequestResponseTime(System.currentTimeMillis() - startprocestime);
	                    
	                    data.setContentType("text/xml");
	                    InputStream is = method.getResponseBodyAsStream();
	                    CountingInputStream cis = new CountingInputStream(is);
	
	                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	                    DocumentBuilder builder = dbf.newDocumentBuilder();
	                    Document doc = builder.parse(cis);
	                    
	                    wfsRequest.setBytesReceived(new Long(cis.getByteCount()));
	
	                    ogcresponse.rebuildResponse(doc.getDocumentElement(), data.getOgcrequest(), prefix);
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
        }
        String responseBody = ogcresponse.getResponseBody(spLayers);
        if (responseBody != null && !responseBody.equals("")) {
            byte[] buffer = responseBody.getBytes();
            os.write(buffer);
        } else {
            throw new UnsupportedOperationException("XMLbody empty!");
        }
    }

    private List getLayerSummaries(String[] layers) throws Exception {
        EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);

        List spList = new ArrayList();
        for (int i = 0; i < layers.length; i++) {
            String layer = layers[i];

            String abbr = null, name = null;
            int idx = layer.indexOf('_');
            if(idx != -1) {
                abbr = layer.substring(0, idx);
                name = layer.substring(idx+1);
            }

            if(abbr == null || abbr.length() == 0 || name == null || name.length() == 0) {
                if(log.isDebugEnabled()) {
                    log.error("invalid layer name: " + layer);
                }
                throw new Exception(KBConfiguration.REQUEST_LAYERNAME_EXCEPTION+": "+layer);
            }

            List matchingLayers = em.createQuery("from WfsLayer l where l.name = :name and l.wfsServiceProvider.abbr = :abbr")
                    .setParameter("name", name)
                    .setParameter("abbr", abbr)
                    .getResultList();

            if(matchingLayers.isEmpty()) {
                /* XXX "or no rights" ?? No rights are checked... */
                log.error("layer not found: " + layer);
                throw new Exception(KBConfiguration.REQUEST_NORIGHTS_EXCEPTION+": "+layer);
            }

            if(matchingLayers.size() > 1) {
                log.error("layers with duplicate names, name: " + layer);
                throw new Exception(KBConfiguration.GETMAP_EXCEPTION);
            }

            WfsLayer l = (WfsLayer)matchingLayers.get(0);
            spList.add(new SpLayerSummary(l, "true"));
        }
        return spList;
    }
}
