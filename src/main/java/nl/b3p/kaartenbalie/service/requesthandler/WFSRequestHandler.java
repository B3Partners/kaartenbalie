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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import nl.b3p.commons.services.B3PCredentials;
import nl.b3p.commons.services.HttpClientConfigured;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.accounting.ExtLayerCalculator;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPriceComposition;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPricing;
import nl.b3p.kaartenbalie.core.server.monitoring.DataMonitoring;
import nl.b3p.kaartenbalie.core.server.monitoring.ServiceProviderRequest;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.service.ProviderException;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.LayerSummary;
import nl.b3p.ogc.utils.OGCCommunication;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.ogc.utils.OGCResponse;
import nl.b3p.ogc.utils.SpLayerSummary;
import nl.b3p.ogc.wfs.v110.WfsLayer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.CountingInputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.w3c.dom.Document;

/**
 *
 * @author Chris
 */
public abstract class WFSRequestHandler extends OGCRequestHandler {

    private static final Log log = LogFactory.getLog(WFSRequestHandler.class);

    /**
     * Creates a new instance of WFSRequestHandler
     */
    public WFSRequestHandler() {
    }

    public boolean mayDirectWrite() {
        return false;
    }

    /**
     * @return the maxResponseTime
     */
    public int getMaxResponseTime() {
        if (maxResponseTime <= 0) {
            try {
                maxResponseTime = new Integer(KBConfiguration.WFS_RESPONSE_TIME_LIMIT);
            } catch (NumberFormatException nfe) {
                maxResponseTime = 33333;
            }
        }
        return maxResponseTime;
    }

    public HttpPost createPostMethod(OGCRequest spOgcReq, SpLayerSummary sp, ServiceProviderRequest wfsRequest) throws Exception {
        String oldBody = spOgcReq.getXMLBody();
        String body = "";
        // staat bij transactie handler, waarom?
//        String[] temp = oldBody.split("id");
//        for (int x = 0; x < temp.length; x++) {
//            if (x < (temp.length - 1)) {
//                body += temp[x] + "gml:id";
//            } else {
//                body += temp[x];
//            }
//        }
        body = oldBody;

        log.debug("WFS POST to serviceprovider: '" + sp.getSpAbbr() + "' with url: '" + sp.getSpUrl() + "' and body:");
        log.debug(body);

        // TODO body cleanen
        if (KBConfiguration.SAVE_MESSAGES) {
            wfsRequest.setMessageSent(body);
        }
        wfsRequest.setBytesSent(new Long(body.getBytes().length));

        OGCRequest tmpReq = new OGCRequest(spOgcReq.fixHttpHost(sp.getSpUrl()));
        tmpReq.removeAllWFSParameters();
        String postUrl = spOgcReq.getUrl(tmpReq.getUrl());
        HttpPost method = new HttpPost(postUrl);
        //work around voor ESRI post Messages
        //method.setRequestEntity(new StringRequestEntity(body, "text/xml", "UTF-8"));
        method.setEntity(new StringEntity(body));
        return method;
    }

    public HttpGet createGetMethod(OGCRequest spOgcReq, SpLayerSummary sp, ServiceProviderRequest wfsRequest) throws Exception {

        OGCRequest tmpReq = new OGCRequest(spOgcReq.fixHttpHost(sp.getSpUrl()));
        tmpReq.removeAllWFSParameters();
        String getUrl = spOgcReq.getUrl(tmpReq.getUrl());

        log.debug("WFS GET to serviceprovider: '" + sp.getSpAbbr() + "' with url: '" + getUrl.toString() + "'");

        if (KBConfiguration.SAVE_MESSAGES) {
            wfsRequest.setMessageSent(getUrl.toString());
        }
        wfsRequest.setBytesSent(new Long(getUrl.length()));

        return new HttpGet(getUrl.toString());
    }

    protected boolean checkNumberOfSps(List<LayerSummary> lsl, int n) {
        List<String> spl = new ArrayList();
        for (LayerSummary ls : lsl) {
            if (!spl.contains(ls.getSpAbbr())) {
                spl.add(ls.getSpAbbr());
            }
        }
        return spl.size()==n;
    }

    public abstract String prepareRequest4Sp(OGCRequest ogcrequest, SpLayerSummary sp) throws Exception;

    public abstract List<LayerSummary> prepareRequestLayers(OGCRequest ogcrequest) throws Exception;

    public abstract OGCResponse getNewOGCResponse();

    public byte[] prepareDirectWrite(InputStream isx) throws IOException {
        return null;
    }

    public void writeResponse(DataWrapper data, User user) throws Exception  {
        OGCResponse ogcresponse = getNewOGCResponse();
        OGCRequest ogcrequest = data.getOgcrequest();

        String version = ogcrequest.getFinalVersion();
        String spInUrl = ogcrequest.getServiceProviderName();

        Integer[] orgIds = user.getOrganizationIds();
        OutputStream os = data.getOutputStream();

        Object identity = null;
        try {
            identity = MyEMFDatabase.createEntityManager(MyEMFDatabase.MAIN_EM);

            boolean forAdmin = isConfigInUrlAndAdmin(data, user);
            // zet layers uit request in een list
            List<LayerSummary> layerSummaryList = prepareRequestLayers(ogcrequest);
            if (layerSummaryList == null) {
                // als geen layers meegegeven, dan alle layers gebruiken
                // alleen bij getcapabilities
                EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);
                String[] al = getOrganisationLayers(em, orgIds, version, forAdmin);
                layerSummaryList = LayerSummary.createLayerSummaryList(Arrays.asList(al), spInUrl, true);
            }

            // maak lijst waarin de layers per sp zijn verzameld
            // boolean om volgorde van de lagen te bewaren
            List<SpLayerSummary> spLayerSummaries = null;
            if (forAdmin) {
                spLayerSummaries = getLayerSummaries(layerSummaryList, spInUrl);
            } else {
                spLayerSummaries = getServiceProviderURLS(layerSummaryList, orgIds, false, data, false);
            }
            if (spLayerSummaries == null || spLayerSummaries.isEmpty()) {
                throw new UnsupportedOperationException("No Serviceprovider available! User might not have rights to any Serviceprovider!");
            }
            if (spLayerSummaries.size()>1 && version.equals(OGCConstants.WFS_VERSION_UNSPECIFIED)) {
                // forceren dat alle sp dezelfde versie retourneren, indien meer dan 1
                ogcrequest.addOrReplaceParameter(OGCConstants.VERSION, OGCConstants.WFS_VERSION_110);
            }

            DataMonitoring rr = data.getRequestReporting();
            long startprocestime = System.currentTimeMillis();
            String xmlEncoding = "UTF-8";

            for (SpLayerSummary sp : spLayerSummaries) {
                if (spInUrl!=null && !spInUrl.equals(sp.getSpAbbr())) {
                    // sp in url en dit is een andere sp
                    continue;
                }
                sp.setSpInUrl(spInUrl);

                // zet de juiste layers voor deze sp
                OGCRequest sprequest = (OGCRequest) ogcrequest.clone();
                prepareRequest4Sp(sprequest, sp);

                String lurl = sp.getSpUrl();
                if (lurl.length() == 0) {
                    throw new UnsupportedOperationException("No Serviceprovider for this service available!");
                }
                ServiceProviderRequest wfsRequest = this.createServiceProviderRequest(
                    data, lurl, sp.getServiceproviderId(), 0l);

                B3PCredentials credentials = new B3PCredentials();
                credentials.setUserName(sp.getUsername());
                credentials.setPassword(sp.getPassword());
                credentials.setUrl(lurl);
                HttpClientConfigured hcc = new HttpClientConfigured(credentials);

                HttpUriRequest method = null;
                if (sprequest.getHttpMethod().equalsIgnoreCase("POST")) {
                    method = createPostMethod(sprequest, sp, wfsRequest);
                } else { // get
                    method = createGetMethod(sprequest, sp, wfsRequest);
                }

                try {
                    HttpResponse response = hcc.execute(method);

                    try {

                        int statusCode = response.getStatusLine().getStatusCode();
                        wfsRequest.setResponseStatus(statusCode);
                        HttpEntity entity = response.getEntity();

                        if (statusCode != 200) {
                            log.error("Failed to connect with " + method.getURI()
                                    + " Using body: " + sprequest.getXMLBody());
                            throw new UnsupportedOperationException("Failed to connect with "
                                    + method.getURI() + " Using body: " + sprequest.getXMLBody());
                        }

                        wfsRequest.setRequestResponseTime(System.currentTimeMillis() - startprocestime);

                        data.setContentType("text/xml");
                        InputStream is = entity.getContent();

                        InputStream isx = null;
                        byte[] bytes = null;
                        int rsl = 0;
                        try {
                            rsl = new Integer(KBConfiguration.RESPONSE_SIZE_LIMIT);
                        } catch (NumberFormatException nfe) {
                            log.debug("KBConfiguration.RESPONSE_SIZE_LIMIT not properly configured: " + nfe.getLocalizedMessage());
                        }
                        if (KBConfiguration.SAVE_MESSAGES) {
                            int len = 1;
                            byte[] buffer = new byte[2024];
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            while ((len = is.read(buffer, 0, buffer.length)) > 0) {
                                bos.write(buffer, 0, len);
                                if (buffer.length > rsl && rsl > 0) {
                                    throw new ProviderException("Response size exceeds maximum set in configuration:" + buffer.length + ", max is: " + rsl);
                                }
                            }
                            bytes = bos.toByteArray();
                            isx = new ByteArrayInputStream(bytes);
                        } else {
                            isx = new CountingInputStream(is);
                        }

                        if (KBConfiguration.SAVE_MESSAGES
                                || spInUrl == null
                                || !mayDirectWrite()) {
                            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                            dbf.setNamespaceAware(true);
                            DocumentBuilder builder = dbf.newDocumentBuilder();
                            Document doc = builder.parse(isx);
                            // indien meerdere sp met verschillende encodings
                            // dan wint de laatste!
                            String docEncoding = doc.getXmlEncoding();
                            if (docEncoding != null) {
                                xmlEncoding = docEncoding;
                            }

                            int len = 0;
                            if (KBConfiguration.SAVE_MESSAGES) {
                                wfsRequest.setMessageReceived(new String(bytes));
                            } else {
                                len = new Integer(((CountingInputStream) isx).getCount());
                                wfsRequest.setBytesReceived(new Long(len));
                            }
                            if (len > rsl && rsl > 0) {
                                throw new ProviderException("Response size exceeds maximum set in configuration:" + len + ", max is: " + rsl);
                            }

                            String prefix = sp.getSpAbbr();
                            if (spInUrl != null && !spInUrl.isEmpty()) {
                                // sp in url dus geen prefix toevoegen
                                prefix = null;
                            }

                            if (OGCResponse.isWfsV100ErrorResponse(doc.getDocumentElement())) {
                                // wfs 1.0.0 error
                                ogcresponse.rebuildWfsV100ErrorResponse(doc, sprequest, prefix);
                            } else if (OGCResponse.isOwsV100ErrorResponse(doc.getDocumentElement())) {
                                // wfs 1.1.0 error
                                ogcresponse.rebuildOwsV100ErrorResponse(doc, sprequest, prefix);
                            } else {
                                // normale response
                                ogcresponse.rebuildResponse(doc, sprequest, prefix);
                            }
                        } else {
                            /**
                             * Deze methode kan alleen aangeroepen worden als
                             * aan de volgende voorwaarden is voldaan:
                             * <li> slechts één sp nodig voor aanroep
                             * <li> spabbr zit in de url en niet als prefix in
                             * de layer name
                             * <li> KBConfiguration.SAVE_MESSAGES is false Als
                             * aan voorwaarden is voldaan dat wordt direct
                             * doorgestreamd indien er geen fout is opgetreden.
                             * <li> de aanroep methode mayDirectWrite is true.
                             */
                            // direct write possible
                            byte[] h = prepareDirectWrite(isx);
                            if (h != null) {
                                os.write(h);
                            }
                            // write rest
                            IOUtils.copy(isx, os);
                            wfsRequest.setBytesReceived(new Long(((CountingInputStream) isx).getCount()));
                            ogcresponse.setAlreadyDirectWritten(true);
                            break;
                        }
                    } finally {
                        hcc.close(response);
                        hcc.close();
                    }

                } catch (Exception e) {
                    wfsRequest.setExceptionMessage("Failed to send bytes to client: " + e.getMessage());
                    wfsRequest.setExceptionClass(e.getClass());

                    throw e;
                } finally {
                    rr.addServiceProviderRequest(wfsRequest);
                }
            }

            // only write when not already direct written
            if (!ogcresponse.isAlreadyDirectWritten()) {
                String responseBody = ogcresponse.getResponseBody(spLayerSummaries, ogcrequest, xmlEncoding);
                if (responseBody != null && !responseBody.equals("")) {
                    byte[] buffer = responseBody.getBytes(xmlEncoding);
                    os.write(buffer);
                } else {
                    throw new UnsupportedOperationException("XMLbody empty!");
                }
            }
            doAccounting(user.getMainOrganizationId(), data, user);

        } finally {
            log.debug("Closing entity manager .....");
            MyEMFDatabase.closeEntityManager(identity, MyEMFDatabase.MAIN_EM);
        }
    }

    protected LayerPriceComposition calculateLayerPriceComposition(DataWrapper dw, ExtLayerCalculator lc, String spAbbr, String layerName) throws Exception {
        String operation = dw.getOperation();
        if (operation == null) {
            log.error("Operation can not be null");
            throw new Exception("Operation can not be null");
        }
        String projection = dw.getOgcrequest().getParameter(OGCConstants.WFS_PARAM_SRSNAME); // todo klopt dit?
        /*
         * De srs parameter word nu alleen gevult met null. Hier moet misschien
         * nog naar gekeken worden, maar nu werk het zo wel.
         */
        BigDecimal scale = (new BigDecimal(dw.getOgcrequest().calcScale())).setScale(2, BigDecimal.ROUND_HALF_UP);
        int planType = LayerPricing.PAY_PER_REQUEST;
        String service = OGCConstants.WFS_SERVICE_WFS;

        return lc.calculateLayerComplete(spAbbr, layerName, new Date(), projection, scale, new BigDecimal("1"), planType, service, operation);
    }

    protected SpLayerSummary getValidLayerObjects(EntityManager em, LayerSummary m, Integer[] orgIds, boolean b3pLayering) throws Exception {
        String query = "select distinct new "
                + "nl.b3p.ogc.utils.SpLayerSummary(l, 'true',sp) "
                + "from WfsLayer l, Organization o, WfsServiceProvider sp join o.wfsLayers ol "
                + "where l = ol and "
                + "l.wfsServiceProvider = sp and "
                + "o.id in (:orgIds) and "
                + "l.name = :layerName and "
                + "sp.abbr = :layerCode and "
                + "sp.allowed = true";

        return getValidLayerObjects(em, query, m, orgIds, b3pLayering);
    }

    protected String[] getOrganisationLayers(EntityManager em, Integer[] orgIds, String version, boolean isAdmin) throws Exception {
        List<SpLayerSummary> spLayers = null;
        if (!isAdmin) {
            String query = "select distinct new "
                    + "nl.b3p.ogc.utils.SpLayerSummary(l, 'true',sp) "
                    + "from Organization o "
                    + "join o.wfsLayers l "
                    + "join l.wfsServiceProvider sp "
                    + "where o.id in (:orgIds) "
                    + "and sp.allowed = true";
            if (version != null && !version.equals(OGCConstants.WFS_VERSION_UNSPECIFIED)) {
                query += " and sp.wfsVersion = :version";
            }

            Query q = em.createQuery(query);

            if (orgIds != null) {
                List lijst = Arrays.asList(orgIds);
                q.setParameter("orgIds", lijst);
            } else {
                q.setParameter("orgIds", null);
            }

            if (version != null && !version.equals(OGCConstants.WFS_VERSION_UNSPECIFIED)) {
                q.setParameter("version", version);
            }
            spLayers = q.getResultList();
        } else {
            String query = "select distinct new "
                    + "nl.b3p.ogc.utils.SpLayerSummary(l, 'true',sp) "
                    + "from WfsLayer l "
                    + "join l.wfsServiceProvider sp";
            if (version != null && !version.equals(OGCConstants.WFS_VERSION_UNSPECIFIED)) {
                query += " where sp.wfsVersion = :version";
            }

            Query q = em.createQuery(query);
            if (version != null && !version.equals(OGCConstants.WFS_VERSION_UNSPECIFIED)) {
                q.setParameter("version", version);
            }
            spLayers = q.getResultList();
        }
        
        List<String> layers = new ArrayList<String>();
        for (SpLayerSummary spls : spLayers) {
            String ln = OGCCommunication.attachSp(spls.getSpAbbr(), spls.getLayerName());
            layers.add(ln);
        }
        return (String[]) layers.toArray(new String[]{});
    }

    /**
     * Create a URI from the ogcrequest with provided URL, by adding the request
     * parameters.
     *
     * @param ogcrequest
     * @param lurl
     * @param version
     * @return
     */
    protected String createUriString(OGCRequest ogcrequest, String lurl,
            String version) {
        StringBuffer url = new StringBuffer(lurl);
        if (!lurl.endsWith("?")) {
            url.append("?");
        }
        String[] params = ogcrequest.getParametersArray();
        for (int i = 0; i < params.length; i++) {
            String key = params[i].split("=")[0];
            if (key.equalsIgnoreCase(OGCRequest.VERSION)) {
                if (version != null) {// if version not given exclude from params
                    url.append(OGCRequest.VERSION);
                    url.append("=");
                    url.append(version);
                }
            } else {
                url.append(params[i]);
            }
            url.append("&");
        }
        return url.toString();
    }

    /**
     * Create a ServiceProviderRequest for WFS requests.
     *
     * @param data
     * @param url
     * @param spId
     * @param bytesSent
     * @return
     */
    protected ServiceProviderRequest createServiceProviderRequest(
            DataWrapper data, String url,
            Integer spId, Long bytesSent) {

        DataMonitoring rr = data.getRequestReporting();
        OGCRequest ogcrequest = data.getOgcrequest();

        ServiceProviderRequest wfsRequest = new ServiceProviderRequest();
        wfsRequest.setMsSinceRequestStart(new Long(rr.getMSSinceStart()));
        wfsRequest.setServiceProviderId(spId);
        String version = ogcrequest.getFinalVersion();
        if (version.equals(OGCConstants.WFS_VERSION_UNSPECIFIED)) {
            version = null;
        }
        String uri = createUriString(ogcrequest, url, version);
        wfsRequest.setProviderRequestURI(uri);
        wfsRequest.setWmsVersion(version);
        wfsRequest.setBytesSent(bytesSent);

        return wfsRequest;
    }

    protected List<SpLayerSummary> getLayerSummaries(List<LayerSummary> lsl) throws Exception {
        return getLayerSummaries(LayerSummary.getLayersAsArray(lsl), null);
    }

    protected List<SpLayerSummary> getLayerSummaries(List<LayerSummary> lsl, String serviceName) throws Exception {
        return getLayerSummaries(LayerSummary.getLayersAsArray(lsl), serviceName);
    }

    private List<SpLayerSummary> getLayerSummaries(String[] layers) throws Exception {
        return getLayerSummaries(layers, null);
    }

    private List<SpLayerSummary> getLayerSummaries(String[] layers, String serviceName) throws Exception {
        EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);

        List<SpLayerSummary> eventualSPList = new ArrayList();
        for (int i = 0; i < layers.length; i++) {
            String layer = layers[i];

            boolean splitName = (serviceName==null || serviceName.isEmpty())?true:false;
            LayerSummary m = OGCCommunication.splitLayerWithoutNsFix(layer, splitName, serviceName, null);
            String abbr = m.getSpAbbr();
            String name = OGCCommunication.buildLayerNameWithoutSp(m);

            List matchingLayers = em.createQuery("from WfsLayer l where l.name = :name and l.wfsServiceProvider.abbr = :abbr").setParameter("name", name).setParameter("abbr", abbr).getResultList();

            if (matchingLayers.isEmpty()) {
                /*
                 * XXX "or no rights" ?? No rights are checked...
                 */
                log.error("layer not found: " + layer);
                throw new Exception(KBConfiguration.REQUEST_NORIGHTS_EXCEPTION + ": " + layer);
            }

            if (matchingLayers.size() > 1) {
                log.error("layers with duplicate names, name: " + layer);
                throw new Exception(KBConfiguration.GETMAP_EXCEPTION);
            }

            WfsLayer l = (WfsLayer) matchingLayers.get(0);
            SpLayerSummary layerInfo = new SpLayerSummary(l, "true");

            addToServerProviderList(eventualSPList, layerInfo, false);
        }

        return eventualSPList;
    }

}
