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

import java.util.List;
import java.util.Iterator;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathExpressionException;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.monitoring.DataMonitoring;
import nl.b3p.kaartenbalie.core.server.monitoring.ServiceProviderRequest;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.ogc.utils.OGCResponse;
import nl.b3p.wms.capabilities.Roles;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

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

        OGCRequest ogcrequest = (OGCRequest) data.getOgcrequest().clone();
        Integer[] orgIds = user.getOrganizationIds();

        String layers = ogcrequest.getParameter(OGCConstants.WFS_PARAM_TYPENAME);
        String[] allLayers = layers.split(",");

        String[] layerNames = new String[allLayers.length];
        String[] prefixes = new String[allLayers.length];
        String[] spAbbrs = new String[allLayers.length];
        String[] spLayerNames = new String[allLayers.length];
        for (int i = 0; i < allLayers.length; i++) {
            String tPrefix = null;
            String tLayerName = null;
            String tSpAbbr = null;
            String tSpLayerName = null;
            String[] temp = allLayers[i].split("}");
            if (temp.length > 1) {
                tSpLayerName = temp[1];
                int index1 = allLayers[i].indexOf("{");
                int index2 = allLayers[i].indexOf("}");
                tPrefix = ogcrequest.getPrefix(allLayers[i].substring(index1 + 1, index2));
            } else {
                String temp2[] = temp[0].split(":");
                if (temp2.length > 1) {
                    tSpLayerName = temp2[1];
                    tPrefix = temp2[0];
                } else {
                    tSpLayerName = allLayers[i];
                }
            }
            int index1 = tSpLayerName.indexOf("_");
            if (index1 != -1) {
                tSpAbbr = tSpLayerName.substring(0, index1);
                tLayerName = tSpLayerName.substring(index1 + 1);
            }
            prefixes[i] = tPrefix;
            spAbbrs[i] = tSpAbbr;
            layerNames[i] = tLayerName;
            spLayerNames[i] = tSpLayerName;
        }

        Set userRoles = user.getRoles();
        boolean isAdmin = false;
        boolean isOrgAdmin = false;
        Iterator rolIt = userRoles.iterator();
        while (rolIt.hasNext()) {
            Roles role = (Roles) rolIt.next();
            if (role.getRole().equalsIgnoreCase(Roles.ADMIN)) {
                /* de gebruiker is een beheerder */
                isAdmin = true;
            }
            if (role.getRole().equalsIgnoreCase(Roles.ORG_ADMIN)) {
                /* de gebruiker is een organisatiebeheerder */
                isOrgAdmin = true;
            }
        }

        List spInfo = null;
        if (isAdmin && !isOrgAdmin) {
            spInfo = getLayerSummaries(spLayerNames);
        } else {
            spInfo = getSeviceProviderURLS(spLayerNames, orgIds, false, data);
        }
        if (spInfo == null || spInfo.isEmpty()) {
            throw new UnsupportedOperationException("No Serviceprovider available! User might not have rights to any Serviceprovider!");
        }

        Integer spId = null;
        String spurl = null;
        Iterator spIt = spInfo.iterator();
        while (spIt.hasNext()) {
            SpLayerSummary sp = (SpLayerSummary) spIt.next();
            Integer tSpId = sp.getServiceproviderId();
            if (spId!=null && tSpId != null && tSpId.compareTo(spId) != 0) {
                log.error("More then 1 service provider addressed. Not supported (yet)");
                throw new UnsupportedOperationException("More then 1 service provider addressed. Not supported (yet)");
            }
            spId = tSpId;
            spurl = sp.getSpUrl();
        }
        if (spurl == null || spurl.length() == 0 || spId==null) {
            throw new UnsupportedOperationException("No Serviceprovider for this service available!");
        }

        String layerParam = "";
        for (int i = 0; i < layerNames.length; i++) {
            if (layerParam.length() != 0) {
                layerParam += ",";
            }
            if (prefixes[i] != null) {
                layerParam += prefixes[i] + ":";
            }
            layerParam += layerNames[i];
        }
        ogcrequest.addOrReplaceParameter(OGCConstants.WFS_PARAM_TYPENAME, layerParam);

        HttpMethod method = null;
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout((int) maxResponseTime);
        OutputStream os = data.getOutputStream();
        String body = ogcrequest.getXMLBody();
        // TODO body cleanen

        DataMonitoring rr = data.getRequestReporting();
        long startprocestime = System.currentTimeMillis();

        ServiceProviderRequest wfsRequest = this.createServiceProviderRequest(
                data, spurl, spId, new Long(body.getBytes().length));

        String host = spurl;
        ogcrequest.setHttpHost(host);
        //probeer eerst met Http getMethod op te halen.
        String reqUrl = ogcrequest.getUrl();
        method = new GetMethod(reqUrl);
        int status = client.executeMethod(method);
        if (status != HttpStatus.SC_OK) {
            method = new PostMethod(host);
            //work around voor ESRI post request. Content type mag geen text/xml zijn.
            //((PostMethod)method).setRequestEntity(new StringRequestEntity(body, "text/xml", "UTF-8"));
            ((PostMethod) method).setRequestEntity(new StringRequestEntity(body, null, null));
            status = client.executeMethod(method);
        }
        try {
            if (status == HttpStatus.SC_OK) {
                wfsRequest.setResponseStatus(new Integer(200));
                wfsRequest.setRequestResponseTime(System.currentTimeMillis() - startprocestime);

                data.setContentType("text/xml");
                InputStream is = method.getResponseBodyAsStream();

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setNamespaceAware(true);
                DocumentBuilder builder = dbf.newDocumentBuilder();
                Document doc = builder.parse(is);

                OGCResponse ogcresponse = new OGCResponse(doc);

                String[] oldVals = new String[layerNames.length];
                String[] newVals = new String[layerNames.length];
                for (int i = 0; i < layerNames.length; i++) {
                    oldVals[i] = "";
                    newVals[i] = "";
                    if (prefixes[i] != null) {
                        oldVals[i] += prefixes[i] + ":";
                        newVals[i] += prefixes[i] + ":";
                    }
                    if (spAbbrs[i] != null) {
                        newVals[i] += spAbbrs[i];
                        newVals[i] += "_";
                    }
                    oldVals[i] += layerNames[i];
                    newVals[i] += layerNames[i];
                }
                replaceStringInElementName(ogcresponse, oldVals, newVals);

                String output = ogcresponse.serializeNode(doc);
                os.write(output.getBytes());

            } else {
                wfsRequest.setResponseStatus(status);
                wfsRequest.setExceptionMessage("Failed to connect with " + spurl + " Using body: " + body);
                wfsRequest.setExceptionClass(UnsupportedOperationException.class);
                log.error("Failed to connect with " + spurl + " Using body: " + body);
                throw new UnsupportedOperationException("Failed to connect with " + spurl + " Using body: " + body);
            }
        } catch (Exception e) {
            wfsRequest.setExceptionMessage("Failed to send bytes to client: " + e.getMessage());
            wfsRequest.setExceptionClass(e.getClass());
            throw e;
        } finally {
            rr.addServiceProviderRequest(wfsRequest);
        }
    }


        /**
     * Replaces a string value in a XSD document for the name attribute of an
     * element tag.
     *
     * @param oldVal old name
     * @param newVal new name
     * @throws XPathExpressionException
     */
    public void replaceStringInElementName(OGCResponse ogcresponse, String[] oldVals, String[] newVals) throws Exception {
        String prefix = ogcresponse.getNameSpacePrefix("http://www.w3.org/2001/XMLSchema");
        StringBuilder sb = new StringBuilder();
        sb.append("//");
        if (prefix != null && prefix.length() > 0) {
            sb.append(prefix);
            sb.append(":");
        }
        sb.append("element/@name");
        ogcresponse.replaceStringInNode(sb.toString(),oldVals, newVals);
    }


}
