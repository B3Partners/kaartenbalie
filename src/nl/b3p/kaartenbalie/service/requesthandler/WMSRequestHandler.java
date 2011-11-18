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

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import nl.b3p.commons.xml.IgnoreEntityResolver;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.accounting.ExtLayerCalculator;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPriceComposition;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPricing;
import nl.b3p.kaartenbalie.core.server.b3pLayering.ConfigLayer;
import nl.b3p.kaartenbalie.core.server.monitoring.DataMonitoring;
import nl.b3p.kaartenbalie.core.server.monitoring.Operation;
import nl.b3p.kaartenbalie.core.server.monitoring.ServiceProviderRequest;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.service.ImageManager;
import nl.b3p.kaartenbalie.service.KBImageTool;
import nl.b3p.kaartenbalie.service.LayerValidator;
import nl.b3p.kaartenbalie.service.ServiceProviderValidator;
import nl.b3p.kaartenbalie.service.persistence.WFSProviderDAO;
import nl.b3p.kaartenbalie.service.servlet.CallWMSServlet;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.wms.capabilities.ElementHandler;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.Roles;
import nl.b3p.wms.capabilities.ServiceProvider;
import nl.b3p.wms.capabilities.SrsBoundingBox;
import nl.b3p.wms.capabilities.Switcher;

import nl.b3p.wms.capabilities.TileSet;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.geotools.data.ows.LayerDescription;
import org.geotools.data.wms.response.DescribeLayerResponse;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public abstract class WMSRequestHandler extends OGCRequestHandler {

    private static final Log log = LogFactory.getLog(WMSRequestHandler.class);
    private XMLReader parser;
    private static Stack stack = new Stack();
    private Switcher s;

    public WMSRequestHandler() {
    }

    protected Set getValidLayers(User user, EntityManager em, boolean isAdmin) throws Exception {

        Set organizationLayers = new HashSet();

        if (isAdmin) {
            List layerlist = em.createQuery("from Layer").getResultList();
            organizationLayers.addAll(layerlist);
        } else {
            Set orgs = user.getAllOrganizations();
            if (orgs != null) {
                Iterator it = orgs.iterator();
                while (it.hasNext()) {
                    Organization org = (Organization) it.next();
                    organizationLayers.addAll(org.getLayers());
                }
            }
            
        }
        return organizationLayers;
    }

    public Set getServiceProviders(boolean isAdmin) throws Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);
        User dbUser = null;
        try {
            dbUser = (User) em.createQuery("from User u where "
                    + "u.id = :userid").setParameter("userid", user.getId()).getSingleResult();
        } catch (NoResultException nre) {
            log.error("No serviceprovider for user found.");
            throw new Exception("No serviceprovider for user found.");
        }
        Set organizationLayers = getValidLayers(dbUser, em, isAdmin);

        Set serviceproviders = null;
        if (organizationLayers != null && !organizationLayers.isEmpty()) {

            serviceproviders = new HashSet();
            Iterator it = organizationLayers.iterator();

            while (it.hasNext()) {
                Layer layer = (Layer) it.next();
                ServiceProvider sp = layer.getServiceProvider();
                if (!serviceproviders.contains(sp)) {
                    serviceproviders.add(sp);
                }
            }
        }

        return serviceproviders;
    }

    public ServiceProvider getServiceProvider(boolean isAdmin) throws Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);
        User dbUser = null;
        try {
            dbUser = (User) em.createQuery("from User u where "
                    + "u.id = :userid").setParameter("userid", user.getId()).getSingleResult();
        } catch (NoResultException nre) {
            log.error("User not found in database.");
            throw new Exception("User not found in database.");
        }
        Set organizationLayers = getValidLayers(dbUser, em, isAdmin);
        Set serviceproviders = null;
        Layer kaartenbalieTopLayer = null;
        Set<TileSet> tileSets = null;

        if (organizationLayers != null && !organizationLayers.isEmpty()) {

            serviceproviders = new HashSet();
            Set topLayers = new HashSet();
            Set orgLayerIds = new HashSet();
            Iterator it = organizationLayers.iterator();

            while (it.hasNext()) {
                Layer layer = (Layer) it.next();
                orgLayerIds.add(layer.getId());
                Layer topLayer = layer.getTopLayer();
                if (!topLayers.contains(topLayer)) {
                    topLayers.add(topLayer);
                }
                ServiceProvider sp = layer.getServiceProvider();
                if (!serviceproviders.contains(sp)) {
                    serviceproviders.add(sp);                    
                }                
            }
            Iterator spIt = serviceproviders.iterator();
            while(spIt.hasNext()){
                ServiceProvider sp = (ServiceProvider) spIt.next();
                if (sp.getTileSets()!=null){
                    Iterator<TileSet> tileSetIt= sp.getTileSets().iterator();
                    while(tileSetIt.hasNext()){
                        TileSet ts=tileSetIt.next();
                        if (ts.getLayers()!=null){
                            Iterator<Layer> layerIt = ts.getLayers().iterator();
                            boolean hasRight=false;
                            while(layerIt.hasNext()){
                                Layer l = layerIt.next();
                                if (!organizationLayers.contains(l)){
                                    hasRight=false;
                                    break;
                                }else{
                                    hasRight=true;
                                }
                            }
                            if (hasRight){
                                if (tileSets==null){
                                    tileSets=new HashSet<TileSet>();
                                }
                                tileSets.add(ts);
                            }                            
                        }
                    }
                }
            }

            if (!topLayers.isEmpty()) {
                kaartenbalieTopLayer = new Layer();
                kaartenbalieTopLayer.setTitle(KBConfiguration.TOPLAYERNAME);
                LayerValidator lv = new LayerValidator(organizationLayers);
                kaartenbalieTopLayer.addSrsbb(lv.validateLatLonBoundingBox());

                Iterator tlId = topLayers.iterator();

                /* To prevent multiple topLayers as a child of the kaartenbalieTopLayer
                 * with duplicate name, title and cascaded properties (but not
                 * service provider abbr!), keep count of how many duplicates
                 * there are and a duplicate number after the layer title in braces.
                 *
                 * NOTE: theoretically a duplicate can only occur when name is
                 * null, otherwise a unique abbreviation prefix has been added
                 * to the name
                 */

                /* Map of as keys layer identity tuples maps; see Layer.getIdentityMap()
                 * and as value an Integer of the duplicate count. No map entry means a
                 * count of 0, naturally.
                 */
                Map topLayerDuplicateCounts = new HashMap();

                while (tlId.hasNext()) {
                    Layer layer = (Layer) tlId.next();
                    Layer layerCloned = (Layer) layer.clone();
                    Set authSubLayers = layerCloned.getAuthSubLayersClone(orgLayerIds);
                    // niet toevoegen indien deze layer en alle sublayers niet toegankelijk
                    boolean layerAuthorized = orgLayerIds.contains(layer.getId());
                    if (!layerAuthorized && (authSubLayers == null || authSubLayers.isEmpty())) {
                        continue;
                    }
                    layerCloned.setLayers(authSubLayers);
                    // layer alleen als placeholder indien niet toegankelijk maar wel toegankelijke sublayers heeft
                    if (!layerAuthorized) {
                        layerCloned.setName(null);
                    }
                    if (authSubLayers == null) {
                        authSubLayers = new HashSet();
                    }
                    Map topLayerIdentity = layerCloned.getIdentityMap(false);
                    Integer duplicateCount = (Integer) topLayerDuplicateCounts.get(topLayerIdentity);
                    if (duplicateCount == null) {
                        /* First time this identity combo has been encountered... Do not add
                         * a counter to the title
                         */
                        topLayerDuplicateCounts.put(topLayerIdentity, new Integer(1));
                    } else {
                        /* Add a counter to the title */
                        int count = duplicateCount.intValue() + 1;
                        layerCloned.setTitle(layerCloned.getTitle().trim() + " (" + count + ")");
                        topLayerDuplicateCounts.put(topLayerIdentity, new Integer(count));
                    }
                    kaartenbalieTopLayer.addLayer(layerCloned);

                }

                // Valideer SRS van toplayers
                lv = new LayerValidator(kaartenbalieTopLayer.getLayers());
                String[] supportedSRS = lv.validateSRS();
                for (int i = 0; i < supportedSRS.length; i++) {
                    SrsBoundingBox srsbb = new SrsBoundingBox();
                    srsbb.setSrs(supportedSRS[i]);
                    kaartenbalieTopLayer.addSrsbb(srsbb);
                }
            }
        }



        //controleer of een organisatie een bepaalde startpostitie heeft voor de BBOX
        //indien dit het geval is, voeg deze bbox toe aan de toplayer.
        if (kaartenbalieTopLayer != null) {
            SrsBoundingBox srsbb = calcSrsBoundingBox(dbUser);
            if (srsbb != null) {
                kaartenbalieTopLayer.addSrsbb(srsbb);
            }
        }

        // Creeer geldige service provider
        ServiceProviderValidator spv = new ServiceProviderValidator(serviceproviders);
        ServiceProvider validServiceProvider = spv.getValidServiceProvider();
        validServiceProvider.setTopLayer(kaartenbalieTopLayer);

        /*
         * B3Partners Configuration Layers..
         */
        boolean allowAccountingLayers = false;
        Set orgs = dbUser.getAllOrganizations();
        Iterator it = orgs.iterator();
        while (it.hasNext()) {
            Organization org = (Organization) it.next();
            if (org.getAllowAccountingLayers()) {
                allowAccountingLayers = true;
                break;
            }
        }

        /*
         *Only adds AllowTransaction layer and creditInfo layer if the user has the rights to see these layers.
         */
        if (allowAccountingLayers == true && kaartenbalieTopLayer != null) {
            Map configLayers = ConfigLayer.getConfigLayers();
            Iterator iterLayerKeys = configLayers.keySet().iterator();
            while (iterLayerKeys.hasNext()) {
                Layer configLayer = ConfigLayer.forName((String) iterLayerKeys.next());
                configLayer.setServiceProvider(validServiceProvider);

                kaartenbalieTopLayer.addLayer(configLayer);
            }
        }

        Set roles = dbUser.getRoles();
        if (roles != null) {
            Iterator roleIt = roles.iterator();
            while (roleIt.hasNext()) {
                Roles role = (Roles) roleIt.next();
                validServiceProvider.addRole(role);
            }
        }
        //if tileSets then add!
        if (tileSets!=null){
            validServiceProvider.setTileSets(tileSets);
        }
        return validServiceProvider;
    }

    private SrsBoundingBox calcSrsBoundingBox(User user) {
        SrsBoundingBox srsbb = new SrsBoundingBox();
        String orgBbox = user.getMainOrganization().getBbox();

        if (orgBbox != null) {
            String[] values = orgBbox.split(",");

            srsbb.setSrs("EPSG:28992");
            srsbb.setMinx(values[0]);
            srsbb.setMiny(values[1]);
            srsbb.setMaxx(values[2]);
            srsbb.setMaxy(values[3]);
        }

        return srsbb;
    }

    /** Gets the data from a specific set of URL's and converts the information to the format usefull to the
     * REQUEST_TYPE. Once the information is collected and converted the method calls for a write in the
     * DataWrapper, which will sent the data to the client requested for this information.
     *
     * @param dw DataWrapper object containing the clients request information
     * @param urls StringBuffer with the urls where kaartenbalie should connect to to recieve the requested data.
     * @param overlay A boolean setting the overlay to true or false. If false is chosen the images are placed under eachother.
     *
     * @return byte[]
     *
     * @throws Exception
     */
    protected void getOnlineData(DataWrapper dw, ArrayList urlWrapper, boolean overlay, String REQUEST_TYPE) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedImage[] bi = null;

        //The list is given in the opposit ranking. Therefore we first need to swap the list.
        int size = urlWrapper.size();
        ArrayList swaplist = new ArrayList(size);
        for (int i = size - 1; i >= 0; i--) {
            swaplist.add(urlWrapper.get(i));
            log.debug("Outgoing url: "+((ServiceProviderRequest)urlWrapper.get(i)).getProviderRequestURI());
        }
        urlWrapper = swaplist;

        /* To save time, this method checks first if the ArrayList contains more then one url
         * If it contains only one url then the method doesn't have to load the image into the G2D
         * environment, which saves a lot of time and capacity because it doesn't have to decode
         * and recode the image.
         */
        long startprocestime = System.currentTimeMillis();

        DataMonitoring rr = dw.getRequestReporting();
        if (urlWrapper.size() > 1) {
            if (REQUEST_TYPE.equalsIgnoreCase(OGCConstants.WMS_REQUEST_GetMap)) {
                /*
                 * Log the time in ms from the start of the clientrequest.. (Reporting)
                 */
                Operation o = new Operation();
                o.setType(Operation.SERVER_TRANSFER);
                o.setMsSinceRequestStart(new Long(rr.getMSSinceStart()));

                ImageManager imagemanager = new ImageManager(urlWrapper, dw);
                imagemanager.process();
                long endprocestime = System.currentTimeMillis();
                Long time = new Long(endprocestime - startprocestime);
                dw.setHeader("X-Kaartenbalie-ImageServerResponseTime", time.toString());

                o.setDuration(time);
                rr.addRequestOperation(o);

                imagemanager.sendCombinedImages(dw);
            } else if (REQUEST_TYPE.equalsIgnoreCase(OGCConstants.WMS_REQUEST_GetFeatureInfo)) {

                /*
                 * Create a DOM document and copy all the information of the several GetFeatureInfo
                 * responses into one document. This document has the same layout as the received
                 * documents and will hold all the information of the specified objects.
                 * After combining these documents, the new document will be sent onto the request.
                 */
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setValidating(false);
                dbf.setNamespaceAware(true);
                dbf.setIgnoringElementContentWhitespace(true);

                DocumentBuilder builder = dbf.newDocumentBuilder();
                Document destination = builder.newDocument();
                Element rootElement = destination.createElement("msGMLOutput");
                destination.appendChild(rootElement);
                rootElement.setAttribute("xmlns:gml", "http://www.opengis.net/gml");
                rootElement.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
                rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
                Document source = null;
                for (int i = 0; i < urlWrapper.size(); i++) {
                    ServiceProviderRequest wmsRequest = (ServiceProviderRequest) urlWrapper.get(i);
                    String url = wmsRequest.getProviderRequestURI();
                    // code below could be used to get bytes received
                    //InputStream is = new URL(url).openStream();
                    //CountingInputStream cis = new CountingInputStream(is);
                    //source = builder.parse(cis);
                    source = builder.parse(url); //what if url points to non MapServer service?
                    copyElements(source, destination);

                    wmsRequest.setBytesSent(new Long(url.getBytes().length));
                    wmsRequest.setProviderRequestURI(url);
                    wmsRequest.setMsSinceRequestStart(new Long(rr.getMSSinceStart()));
                    wmsRequest.setBytesReceived(new Long(-1));
                    //wmsRequest.setBytesReceived(new Long(cis.getByteCount()));
                    wmsRequest.setResponseStatus(new Integer(-1));
                    rr.addServiceProviderRequest(wmsRequest);
                }

                OutputFormat format = new OutputFormat(destination);
                format.setIndenting(true);
                XMLSerializer serializer = new XMLSerializer(baos, format);
                serializer.serialize(destination);
                dw.write(baos);
            } else if (REQUEST_TYPE.equalsIgnoreCase(OGCConstants.WMS_REQUEST_DescribeLayer)) {

                //TODO: implement and refactor so there is less code duplication with getFeatureInfo
            	/*
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setValidating(false);
                dbf.setNamespaceAware(true);
                dbf.setIgnoringElementContentWhitespace(true);

                DocumentBuilder builder = dbf.newDocumentBuilder();
                Document destination = builder.newDocument();
                
                //root element is different
                Element rootElement = destination.createElement("WMS_DescribeLayerResponse");
                destination.appendChild(rootElement);
                //set version attribute
                Document source = null;
                for (int i = 0; i < urlWrapper.size(); i++) {
                ServiceProviderRequest dlRequest = (ServiceProviderRequest) urlWrapper.get(i);
                String url = dlRequest.getProviderRequestURI();
                source = builder.parse(url);
                copyElements(source,destination);
                }*/

                throw new Exception(REQUEST_TYPE + " request with more then one service url is not supported yet!");
            }

        } else {
            //urlWrapper not > 1, so only 1 url or zero urls
            if (!urlWrapper.isEmpty()) {
                getOnlineData(dw, (ServiceProviderRequest) urlWrapper.get(0), REQUEST_TYPE);
            } else {
                if (REQUEST_TYPE.equalsIgnoreCase(OGCConstants.WMS_REQUEST_GetFeatureInfo)) {
                    log.error(KBConfiguration.FEATUREINFO_EXCEPTION);
                    throw new Exception(KBConfiguration.FEATUREINFO_EXCEPTION);
                } else if (REQUEST_TYPE.equalsIgnoreCase(OGCConstants.WMS_REQUEST_GetLegendGraphic)) {
                    log.error(KBConfiguration.LEGENDGRAPHIC_EXCEPTION);
                    throw new Exception(KBConfiguration.LEGENDGRAPHIC_EXCEPTION);
                }
            }
        }
    }

    /** Private method getOnlineData which handels the throughput of information when it is only
     *  about one URL. This is a slightly different method, because no checks have to be done or
     *  information has to be stored. Everything can be directly send through the open connection.
     *
     * @param dw DataWrapper object which handles sending the information over the request.
     * @param url String object which has the specific url where the information should come from.
     *
     * @throws Exception
     */
    private void getOnlineData(DataWrapper dw, ServiceProviderRequest wmsRequest, String REQUEST_TYPE) throws Exception {
        /*
         * Because only one url is defined, the images don't have to be loaded into a
         * BufferedImage. The data received from the url can be directly transported to the client.
         */
        String url = wmsRequest.getProviderRequestURI();
        DataMonitoring rr = dw.getRequestReporting();
        wmsRequest.setBytesSent(new Long(url.getBytes().length));
        long startTime = System.currentTimeMillis();
        try {
            if (REQUEST_TYPE.equalsIgnoreCase(OGCConstants.WMS_REQUEST_GetMap)
                    && url.startsWith(KBConfiguration.SERVICEPROVIDER_BASE_HTTP)) {
                //B3PLayering...
                long time = System.currentTimeMillis() - startTime;
                try {
                    BufferedImage[] bi = new BufferedImage[]{ConfigLayer.handleRequest(url, dw.getLayeringParameterMap())};
                    KBImageTool.writeImage(bi, "image/png", dw);
                    wmsRequest.setBytesReceived(new Long(dw.getContentLength()));
                    wmsRequest.setResponseStatus(new Integer(200));
                } catch (Exception e) {
                    wmsRequest.setResponseStatus(new Integer(404));
                    throw e;
                }
                wmsRequest.setRequestResponseTime(new Long(time));
            } else {
                //Get data for GetFeatureInfo and DescribeLayer (and GetLegend?)
                HttpClient client = new HttpClient();

                // TODO: Wel goed doen
                url = url.replaceAll(" ", "%20");

                GetMethod method = new GetMethod(url);
                client.getHttpConnectionManager().getParams().setConnectionTimeout((int) maxResponseTime);
                String rhValue = "";

                try {
                    int statusCode = client.executeMethod(method);
                    long time = System.currentTimeMillis() - startTime;
                    dw.setHeader("X-Kaartenbalie-ImageServerResponseTime", String.valueOf(time));
                    wmsRequest.setResponseStatus(new Integer(statusCode));
                    wmsRequest.setRequestResponseTime(new Long(time));
                    if (statusCode != HttpStatus.SC_OK) {
                        log.error("Error connecting to server. Status code: " + statusCode);
                        throw new Exception("Error connecting to server. Status code: " + statusCode);
                    }

                    rhValue = method.getResponseHeader("Content-Type").getValue();

                    if (rhValue.equalsIgnoreCase(OGCConstants.WMS_PARAM_EXCEPTION_XML)) {
                        log.error("xml error response for request: " + dw.getOgcrequest().toString());
                        InputStream is = method.getResponseBodyAsStream();
                        String body = getServiceException(is);
                        log.debug("error xml body: " + body);
                        throw new Exception(body);
                    }

                    dw.setContentType(rhValue);
                    if (REQUEST_TYPE.equalsIgnoreCase(OGCConstants.WMS_REQUEST_GetFeatureInfo)) {
                        
                        dw.write(method.getResponseBodyAsStream());
                        wmsRequest.setBytesReceived(new Long(dw.getContentLength()));
                                              
                    } else if (REQUEST_TYPE.equalsIgnoreCase(OGCConstants.WMS_REQUEST_DescribeLayer)) {
                        
                        DescribeLayerResponse wmsResponse = new DescribeLayerResponse(rhValue, method.getResponseBodyAsStream());
                        DescribeLayerData data = new DescribeLayerData(wmsRequest.getServiceProviderAbbreviation(), wmsResponse);
                        List<DescribeLayerData> dataList = new ArrayList<DescribeLayerData>();
                        dataList.add(data);
                        Document newResponse = createKBDescribeLayerResponse(dw, dataList);
                        OutputFormat format = new OutputFormat(newResponse, KBConfiguration.CHARSET, true);
                        format.setIndenting(true);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        XMLSerializer serializer = new XMLSerializer(baos, format);
                        serializer.serialize(newResponse);
                        dw.write(baos);
                        wmsRequest.setBytesReceived(new Long(dw.getContentLength()));
                        
                    } else {                        
                        /* GetLegend, GetStyles, PutStyles */
                        Header cacheControl = method.getResponseHeader("Cache-Control");
                        Header expires = method.getResponseHeader("Expires");
                        Header pragma = method.getResponseHeader("Pragma");
                        
                        if (cacheControl != null) {
                            dw.setHeader(cacheControl.getName(), cacheControl.getValue());
                        }
                        if (expires != null) {
                            dw.setHeader(expires.getName(), expires.getValue());
                        }
                        if (pragma != null) {
                            dw.setHeader(pragma.getName(), pragma.getValue());
                        }
                        
                        dw.write(method.getResponseBodyAsStream());
                        wmsRequest.setBytesReceived(new Long(dw.getContentLength()));                     
                    }


                } catch (HttpException e) {
                    log.error("Fatal protocol violation: " + e.getMessage());
                    throw new HttpException("Fatal protocol violation: " + e.getMessage());
                } catch (IOException e) {
                    log.error("Fatal transport error: " + e.getMessage());
                    throw new IOException("Fatal transport error: " + e.getMessage());
                } catch (Exception e) {
                    throw e;
                } finally {
                    method.releaseConnection();
                }
            }
        } catch (Exception e) {
            wmsRequest.setExceptionMessage(e.getMessage());
            wmsRequest.setExceptionClass(e.getClass());
            throw e;
        } finally {
            rr.addServiceProviderRequest(wmsRequest);
        }
    }

    private Document createKBDescribeLayerResponse(DataWrapper dw,
            List<DescribeLayerData> describeLayerData) throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        DocumentBuilder db = dbf.newDocumentBuilder();
        DOMImplementation di = db.getDOMImplementation();

        // <!DOCTYPE WMS_DescribeLayerResponse SYSTEM "http://schemas.opengis.net/wms/1.1.1/WMS_DescribeLayerResponse.dtd">
        // [ <WMS_DescribeLayerResponse version="1.1.1" > (...) </WMS_DescribeLayerResponse>]        
        DocumentType dt = di.createDocumentType("WMS_DescribeLayerResponse", null, CallWMSServlet.DESCRIBELAYER_DTD);
        Document dom = di.createDocument(null, "WMS_DescribeLayerResponse", dt);
        Element rootElement = dom.getDocumentElement();
        rootElement.setAttribute("version", "1.1.1"); //describeLayer version in kbconfig?

        String personalUrl = this.user.getPersonalURL(dw.getRequest());

        Integer[] orgIds = this.user.getOrganizationIds();

        WFSProviderDAO wfsProviderDao = new WFSProviderDAO();
        String[] validLayerNames = wfsProviderDao.getAuthorizedFeatureTypeNames(orgIds, null, false);
        //it is not possible to use getSeviceProviderURLS because that will call getValidObjects implementation of WMSRequestHandler
        //therefore build spInfo here in loop
        //also, B3PLayering is not relevant here, because describeLayer should not be subject to pricing
        List spInfo = new ArrayList();
        for (String name : validLayerNames) {
            SpLayerSummary layerInfo = wfsProviderDao.getAuthorizedFeatureTypeSummary(name, orgIds, false);
            if (layerInfo == null) {
                continue;
            }
            spInfo.add(layerInfo);
        }

        for (DescribeLayerData resp : describeLayerData) {
            for (LayerDescription descr : resp.getDescribeLayerResponse().getLayerDescs()) {
                Element layerDescriptionElement = dom.createElement("LayerDescription");
                layerDescriptionElement.setAttribute("name", completeLayerName(resp.getWmsPrefix(),descr.getName()));

                descr.getOwsURL();

                //additional info should only be returned for WMS layer that has corresponding WFS type that is served by Kaartenbalie
                String wfsPrefix = getAuthorizedWFSPrefix(spInfo, descr);
                if (wfsPrefix != null) {
                    layerDescriptionElement.setAttribute("wfs", personalUrl);
                    layerDescriptionElement.setAttribute("owsType", descr.getOwsType());
                    layerDescriptionElement.setAttribute("owsURL", personalUrl);

                    Element queryElement = dom.createElement("Query");
                    queryElement.setAttribute("typeName", completeLayerName(wfsPrefix, descr.getName()) ); //this needs to be fixed
                    layerDescriptionElement.appendChild(queryElement);
                }
                rootElement.appendChild(layerDescriptionElement);
            }
        }
        return dom;
    }

    private String getAuthorizedWFSPrefix(List spLayerSummaries, LayerDescription descr) {
        String wfsPrefix = null;
        Iterator it = spLayerSummaries.iterator();
        while (it.hasNext()) {
            SpLayerSummary spLayerSummary = (SpLayerSummary) it.next();

            String spUrl = spLayerSummary.getSpUrl();
            //will KB remove '?' char?
            if (spUrl.endsWith("?")) {
                spUrl = spUrl.substring(0, (spUrl.length() - 1));
            }
            String owsUrl = descr.getOwsURL().toString();
            if (owsUrl.endsWith("?")) {
                owsUrl = owsUrl.substring(0, (owsUrl.length() - 1));
            }

            if (spUrl.equals(owsUrl) && descr.getName().equalsIgnoreCase(spLayerSummary.getLayerName())) {
                wfsPrefix = spLayerSummary.getSpAbbr();
            }
        }
        return wfsPrefix;
    }

    protected LayerPriceComposition calculateLayerPriceComposition(DataWrapper dw, ExtLayerCalculator lc, String spAbbr, String layerName) throws Exception {
        String operation = dw.getOperation();
        if (operation == null) {
            log.error("Operation can not be null");
            throw new Exception("Operation can not be null");
        } else if (operation.equalsIgnoreCase(OGCConstants.WMS_REQUEST_GetLegendGraphic)) {
            log.debug("Never pricing for GetLegendGraphic.");
            return null;
        }
        String projection = dw.getOgcrequest().getParameter(OGCConstants.WMS_PARAM_SRS);
        BigDecimal scale = (new BigDecimal(dw.getOgcrequest().calcScale())).setScale(2, BigDecimal.ROUND_HALF_UP);
        int planType = LayerPricing.PAY_PER_REQUEST;
        String service = OGCConstants.WMS_SERVICE_WMS;

        return lc.calculateLayerComplete(spAbbr, layerName, new Date(), projection, scale, new BigDecimal("1"), planType, service, operation);
    }

    protected SpLayerSummary getValidLayerObjects(EntityManager em, String layer, Integer[] orgIds, boolean b3pLayering) throws Exception {
        String query = "select distinct new "
                + "nl.b3p.kaartenbalie.service.requesthandler.SpLayerSummary(l, l.queryable) "
                + "from Layer l, Organization o, ServiceProvider sp join o.layers ol "
                + "where l = ol and "
                + "l.serviceProvider = sp and "
                + "o.id in (:orgIds) and "
                + "l.name = :layerName and "
                + "sp.abbr = :layerCode";

        return getValidLayerObjects(em, query, layer, orgIds, b3pLayering);
    }

    /**
     *
     * @param byteStream InputStream object in which the serviceexception is stored.
     *
     * @ return String with the given exception
     *
     * @throws IOException, SAXException
     */
    private static String getServiceException(InputStream byteStream) throws IOException, SAXException {
        Switcher s = new Switcher();
        s.setElementHandler("ServiceException", new ServiceExceptionHandler());

        XMLReader reader = org.xml.sax.helpers.XMLReaderFactory.createXMLReader();

        IgnoreEntityResolver r = new IgnoreEntityResolver();
        reader.setEntityResolver(r);

        reader.setContentHandler(s);
        InputSource is = new InputSource(byteStream);
        is.setEncoding(KBConfiguration.CHARSET);
        reader.parse(is);
        return (String) stack.pop();
    }

    /**
     * Below is the Handler defined which reads the Exception from a ServiceException recieved when an error occurs.
     */
    private static class ServiceExceptionHandler extends ElementHandler {

        StringBuffer sb;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) {
            sb = new StringBuffer();
        }

        @Override
        public void characters(char[] chars, int start, int len) {
            sb.append(chars, start, len);
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            stack.push(sb.toString());
        }
    }

    private class DescribeLayerData {

        String prefix;
        DescribeLayerResponse resp;

        DescribeLayerData(String wmsPrefix, DescribeLayerResponse wmsResponse) {
            this.prefix = wmsPrefix;
            this.resp = wmsResponse;
        }

        String getWmsPrefix() {
            return this.prefix;
        }

        DescribeLayerResponse getDescribeLayerResponse() {
            return this.resp;
        }
    }

    /** Method which copies information from one XML document to another document.
     * It adds information to an document and with this method it's possible to create
     * one document from several other documents as used to create an GetFeatureInfo
     * document.
     *
     * @param source Document object
     * @param destination Document object
     */
    private static void copyElements(Document source, Document destination) {
        Element root_source = source.getDocumentElement();
        NodeList nodelist_source = root_source.getChildNodes();
        int size_source = nodelist_source.getLength();

        for (int i = 0; i < size_source; i++) {
            Node node_source = nodelist_source.item(i);
            if (node_source instanceof Element) {
                Element element_source = (Element) node_source;
                String tagName = element_source.getTagName();
                if (!tagName.equalsIgnoreCase("ServiceException")) {
                    Node importedNode = destination.importNode(element_source, true);
                    Element root_destination = destination.getDocumentElement();
                    root_destination.appendChild(importedNode);
                }
            }
        }
    }

    /* Voor GetFeatureInfo de _layer en _feature prefixen met  serviceprovider abbr
     Kijken welke degree Elementen een prefix moeten krijgen */
    private static void prefixElements(Document source, Document destination, String spAbbr) {
        Element root_source = source.getDocumentElement();
        NodeList nodelist_source = root_source.getChildNodes();
        int size_source = nodelist_source.getLength();

        for (int i = 0; i < size_source; i++) {
            Node node_source = nodelist_source.item(i);
            
            if (node_source instanceof Element) {
                Element element_source = (Element) node_source;
                String tagName = element_source.getTagName();

                if (!tagName.equalsIgnoreCase("ServiceException")) {
                    Node importedNode = destination.importNode(element_source, true);
                    Node newNode = destination.renameNode(importedNode, importedNode.getNamespaceURI(), spAbbr + "_" + tagName);
                    
                    Element root_destination = destination.getDocumentElement();
                    root_destination.appendChild(newNode);
                }
            }
        }
    }
}
