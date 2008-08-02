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
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.ogc.utils.OGCResponse;
import nl.b3p.wms.capabilities.Roles;
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
        String version = "";
        if (ogcrequest.getFinalVersion().equals(OGCConstants.WFS_VERSION_100) || ogcrequest.getFinalVersion().equals(OGCConstants.WFS_VERSION_110)) {
            version = ogcrequest.getFinalVersion();
        } else {
            version = OGCConstants.WFS_VERSION_110;
        }

        Set userRoles = user.getUserroles();
        boolean isAdmin = false;
        Iterator rolIt = userRoles.iterator();
        while (rolIt.hasNext()) {
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
            spInfo = getSPURLS(layerNames, em);
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

    private List getSPURLS(String[] layers, EntityManager em) throws Exception {
        List spList = new ArrayList();
        for (int i = 0; i < layers.length; i++) {
            String layer = layers[i];

            String[] split = layer.split("_");
            String abbr = split[0];
            String name = split[1];

            String query = "select 'true', l.wfsserviceproviderid, l.wfslayerid, l.name, sp.url, sp.abbr " +
                    "from wfs_Layer l, Wfs_ServiceProvider sp " +
                    "where l.wfsserviceproviderid = sp.wfsserviceproviderid and " +
                    "l.name = :layerName and " +
                    "sp.abbr = :layerCode";
            List sqlQuery = em.createNativeQuery(query).
                    setParameter("layerName", name).
                    setParameter("layerCode", abbr).
                    getResultList();

            if (sqlQuery == null || sqlQuery.isEmpty()) {
                log.error("layer not valid or no rights, name: " + layer);
                throw new Exception(KBConfiguration.GETMAP_EXCEPTION);
            } else if (sqlQuery.size() > 1) {
                log.error("layers with duplicate names, name: " + layer);
                throw new Exception(KBConfiguration.GETMAP_EXCEPTION);
            }

            Object[] objecten = (Object[]) sqlQuery.get(0);
            SpLayerSummary layerInfo = new SpLayerSummary(
                    (Integer) objecten[1],
                    (Integer) objecten[2],
                    (String) objecten[3],
                    (String) objecten[4],
                    (String) objecten[5],
                    (String) objecten[0]);
            spList.add(layerInfo);
        }
        return spList;
    }
}
