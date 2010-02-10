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
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.w3c.dom.Document;

public class WFSTransactionRequestHandler extends WFSRequestHandler {

    private static final Log log = LogFactory.getLog(WFSGetFeatureRequestHandler.class);

    /** Creates a new instance of WfsTransactionRequestHandler */
    public WFSTransactionRequestHandler() {
    }

    public void getRequest(DataWrapper data, User user) throws IOException, Exception {
        OGCResponse ogcresponse = new OGCResponse();
        OGCRequest ogcrequest = data.getOgcrequest();
        Integer orgId = user.getOrganization().getId();

        String request = ogcrequest.getParameter(OGCConstants.REQUEST);
        String url = "";
        String prefix = "";
        List layers = ogcrequest.getLayers();

        Object identity = null;
        try {
            identity = MyEMFDatabase.createEntityManager(MyEMFDatabase.MAIN_EM);

            /* Check if alle actions are on the same serviceprovider */
            String[] spList = ogcrequest.getTransactionSpList();
            if (spList == null && ogcrequest.getParameter(OGCConstants.WFS_PARAM_OPERATION) != null) {
                throw new UnsupportedOperationException("Transaction request with Key value Pairs is not suported yet!");
            } else if (spList.length > 1 || spList == null) {
                throw new UnsupportedOperationException("Transaction request for more then one serviceprovider is not suported yet!");
            }
            prefix = spList[0];

            List tempList = new ArrayList();
            Iterator it = layers.iterator();
            while (it.hasNext()) {
                String layer = it.next().toString();
                String[] temp = layer.split("}");
                if (temp.length > 1) {
                    layer = temp[1];
                }
                String[] layerSplit = layer.split("_");
                if (layerSplit[0].equals(prefix)) {
                    tempList.add(layer);
                }
            }

            String[] layerList = new String[tempList.size()];
            for (int i = 0; i < layerList.length; i++) {
                layerList[i] = tempList.get(i).toString();
            }

            List spUrls = getSeviceProviderURLS(layerList, orgId, false, data);
            if (spUrls == null || spUrls.isEmpty()) {
                throw new UnsupportedOperationException("No Serviceprovider available! User might not have rights to any Serviceprovider!");
            }
            // Is accounting nodig voor transaction request's?
            /*spUrls = prepareAccounting(orgId, data, spUrls);
            if(spUrls==null || spUrls.isEmpty()) {
            log.error("No urls qualify for request.");
            throw new UnsupportedOperationException("No Serviceprovider available! User might not have rights to any Serviceprovider!");
            }*/

            List spLayers = new ArrayList();
            Iterator iter = spUrls.iterator();
            while (iter.hasNext()) {
                SpLayerSummary sp = (SpLayerSummary) iter.next();
                List tlayers = sp.getLayers();
                if (tlayers == null) {
                    continue;
                }
                url = sp.getSpUrl();
                prefix = sp.getSpAbbr();
                Iterator it2 = layers.iterator();
                while (it2.hasNext()) {
                    String layerName = (String) it2.next();
                    HashMap layer = new HashMap();
                    layer.put("spAbbr", prefix);
                    layer.put("layer", layerName);
                    spLayers.add(layer);
                }
            }

            // layer names moeten nog vervangen worden voor het naar de service provider word gestuurt
            List elementList = ogcrequest.getTransactionElementList(prefix);
            List newElementList = new ArrayList();
            Iterator tor = elementList.iterator();
            while (tor.hasNext()) {
                Object o = tor.next();
                if (o instanceof nl.b3p.xml.wfs.v110.Delete) {
                    nl.b3p.xml.wfs.v110.Delete delete = (nl.b3p.xml.wfs.v110.Delete) o;
                    String[] typeName = delete.getTypeName().split("_");
                    delete.setTypeName("app:" + typeName[1]);
                    String oldId = delete.getFilter().getGmlObjectId().getId().toString();
                    String[] idSplit = oldId.split(prefix + "_");
                    String newId = "";
                    for (int i = 0; i < idSplit.length; i++) {
                        newId += idSplit[i];
                    }
                    delete.getFilter().getGmlObjectId().setId(newId);
                    newElementList.add(delete);
                } else if (o instanceof nl.b3p.xml.wfs.v110.Update) {
                    nl.b3p.xml.wfs.v110.Update update = (nl.b3p.xml.wfs.v110.Update) o;
                    String[] typeName = update.getTypeName().split("_");
                    update.setTypeName("app:" + typeName[1]);
                    String oldId = update.getFilter().getGmlObjectId().getId().toString();
                    String[] idSplit = oldId.split(prefix + "_");
                    String newId = "";
                    for (int i = 0; i < idSplit.length; i++) {
                        newId += idSplit[i];
                    }
                    update.getFilter().getGmlObjectId().setId(newId);
                    newElementList.add(update);
                } else if (o instanceof nl.b3p.xml.wfs.v110.Insert) {
                    nl.b3p.xml.wfs.v110.Insert insert = (nl.b3p.xml.wfs.v110.Insert) o;
                    StringWriter sw = new StringWriter();
                    Marshaller m = new Marshaller(sw);
                    m.marshal(insert);
                    String insertString = sw.toString();
                    String[] insertSplit = insertString.split(prefix + "_");
                    String makeInsert = "";
                    for (int i = 0; i < insertSplit.length; i++) {
                        if (i < (insertSplit.length - 1)) {
                            makeInsert += insertSplit[i] + "app:";
                        } else {
                            makeInsert += insertSplit[i];
                        }
                    }
                    nl.b3p.xml.wfs.v110.Insert newInsert = (nl.b3p.xml.wfs.v110.Insert) Unmarshaller.unmarshal(nl.b3p.xml.wfs.v110.Insert.class, new StringReader(makeInsert));
                    newElementList.add(newInsert);
                }
            }
            ogcrequest.setTransactionElementList(newElementList, prefix);
            ogcrequest.setAbbr(prefix);

            if (url == null || url.length() == 0) {
                throw new UnsupportedOperationException("No Serviceprovider for this service available!");
            }
            PostMethod method = null;
            HttpClient client = new HttpClient();
            client.getHttpConnectionManager().getParams().setConnectionTimeout((int) maxResponseTime);
            OutputStream os = data.getOutputStream();
            String oldBody = data.getOgcrequest().getXMLBody();
            String body = "";
            String[] temp = oldBody.split("id");
            for (int x = 0; x < temp.length; x++) {
                if (x < (temp.length - 1)) {
                    body += temp[x] + "gml:id";
                } else {
                    body += temp[x];
                }
            }

            String host = url;
            method = new PostMethod(host);
            //work around voor ESRI post Messages
            //method.setRequestEntity(new StringRequestEntity(body, "text/xml", "UTF-8"));
            method.setRequestEntity(new StringRequestEntity(body, null, null));
            int status = client.executeMethod(method);
            if (status == HttpStatus.SC_OK) {
                data.setContentType("text/xml");
                InputStream is = method.getResponseBodyAsStream();

                //doAccounting(orgId, data, user);

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = dbf.newDocumentBuilder();
                Document doc = builder.parse(is);

                ogcresponse.rebuildResponse(doc.getDocumentElement(), data.getOgcrequest(), prefix);
                String responseBody = ogcresponse.getResponseBody(spLayers);
                if (responseBody != null && !responseBody.equals("")) {
                    byte[] buffer = responseBody.getBytes();
                    os.write(buffer);
                } else {
                    throw new UnsupportedOperationException("XMLbody empty!");
                }

            } else {
                log.error("Failed to connect with " + url + " Using body: " + body);
                throw new UnsupportedOperationException("Failed to connect with " + url + " Using body: " + body);
            }
        } finally {
            log.debug("Closing entity manager .....");
            MyEMFDatabase.closeEntityManager(identity, MyEMFDatabase.MAIN_EM);
        }
    }
}
