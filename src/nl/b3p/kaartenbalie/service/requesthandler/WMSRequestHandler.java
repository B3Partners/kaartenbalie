/**
 * @(#)WMSRequestHandler.java
 * @author N. de Goeij
 * @version 1.00 2006/12/13
 *
 * Purpose: Superclass of all the WMS request classes. Subclasses which belong to this superclass are:
 * - GetCapabilitiesRequestHandler
 * - GetMapRequestHandler
 * - GetFeatureInfoRequestHandler
 * - GetLegendGraphicRequestHandler
 * - GetStylesRequestHandler
 * - DescribeLayerRequestHandler
 *
 * @copyright 2007 All rights reserved. B3Partners
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
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPricing;
import nl.b3p.kaartenbalie.core.server.reporting.control.DataMonitoring;
import nl.b3p.ogc.utils.KBConstants;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.ServiceProvider;
import nl.b3p.wms.capabilities.SrsBoundingBox;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.accounting.AccountManager;
import nl.b3p.kaartenbalie.core.server.accounting.LayerCalculator;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPriceComposition;
import nl.b3p.kaartenbalie.core.server.accounting.entity.TransactionLayerUsage;
import nl.b3p.kaartenbalie.core.server.b3pLayering.AllowTransactionsLayer;
import nl.b3p.kaartenbalie.core.server.b3pLayering.ConfigLayer;
import nl.b3p.kaartenbalie.core.server.b3pLayering.ConfigLayerException;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.core.server.reporting.domain.operations.ServerTransferOperation;
import nl.b3p.kaartenbalie.core.server.reporting.domain.requests.WMSRequest;
import nl.b3p.kaartenbalie.service.LayerValidator;
import nl.b3p.kaartenbalie.service.ServiceProviderValidator;
import nl.b3p.wms.capabilities.ElementHandler;
import nl.b3p.wms.capabilities.Switcher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import nl.b3p.kaartenbalie.service.ImageManager;
import nl.b3p.kaartenbalie.service.KBImageTool;
import nl.b3p.wms.capabilities.Roles;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.xml.sax.XMLReader;

public abstract class WMSRequestHandler implements RequestHandler, KBConstants {
    
    private static final Log log = LogFactory.getLog(WMSRequestHandler.class);
    protected User user;
    protected String url;
    protected static long maxResponseTime = 100000;
    
    private XMLReader parser;
    private static Stack stack = new Stack();
    private Switcher s;
    
    public WMSRequestHandler() {
    }
    
    public ServiceProvider getServiceProvider() throws Exception {
        EntityManager em = MyEMFDatabase.getEntityManager();
        User dbUser = null;
        try {
            dbUser = (User)em.createQuery("from User u where " +
                    "lower(u.id) = lower(:userid)").setParameter("userid", user.getId()).getSingleResult();
        } catch (NoResultException nre) {
            throw new Exception("No serviceprovider for user found.");
        }
        
        Set serviceproviders = null;
        Layer kaartenbalieTopLayer = null;
        
        Set organizationLayers = dbUser.getOrganization().getOrganizationLayer();
        if (organizationLayers != null && !organizationLayers.isEmpty()) {
            
            serviceproviders = new HashSet();
            Set topLayers = new HashSet();
            Set orgLayerIds = new HashSet();
            Iterator it = organizationLayers.iterator();
            
            while(it.hasNext()) {
                Layer layer = (Layer)it.next();
                orgLayerIds.add(layer.getId());
                Layer topLayer = layer.getTopLayer();
                if(!topLayers.contains(topLayer))
                    topLayers.add(topLayer);
                ServiceProvider sp = layer.getServiceProvider();
                if(!serviceproviders.contains(sp))
                    serviceproviders.add(sp);
            }
            
            
            if (!topLayers.isEmpty()) {
                kaartenbalieTopLayer = new Layer();
                kaartenbalieTopLayer.setTitle(TOPLAYERNAME);
                LayerValidator lv = new LayerValidator(organizationLayers);
                kaartenbalieTopLayer.addSrsbb(lv.validateLatLonBoundingBox());
                
                
                
                Iterator tlId= topLayers.iterator();
                while (tlId.hasNext()) {
                    Layer layer = (Layer)tlId.next();
                    Layer layerCloned = (Layer)layer.clone();
                    Set authSubLayers = layerCloned.getAuthSubLayersClone(orgLayerIds);
                    // niet toevoegen indien deze layer en alle sublayers niet toegankelijk
                    boolean layerAuthorized = orgLayerIds.contains(layer.getId());
                    if (!layerAuthorized && (authSubLayers==null || authSubLayers.isEmpty()))
                        continue;
                    layerCloned.setLayers(authSubLayers);
                    // layer alleen als placeholder indien niet toegankelijk maar wel toegankelijke sublayers heeft
                    if (!layerAuthorized)
                        layerCloned.setName(null);
                    if (authSubLayers==null)
                        authSubLayers = new HashSet();
                    kaartenbalieTopLayer.addLayer(layerCloned);
                }
                
                // Valideer SRS van toplayers
                lv = new LayerValidator(kaartenbalieTopLayer.getLayers());
                String [] supportedSRS = lv.validateSRS();
                for (int i=0; i < supportedSRS.length; i++){
                    SrsBoundingBox srsbb= new SrsBoundingBox();
                    srsbb.setSrs(supportedSRS[i]);
                    kaartenbalieTopLayer.addSrsbb(srsbb);
                }
            }
        }
        
        //controleer of een organisatie een bepaalde startpostitie heeft voor de BBOX
        //indien dit het geval is, voeg deze bbox toe aan de toplayer.
        String orgBbox = dbUser.getOrganization().getBbox();
        if(orgBbox != null) {
            String [] values = orgBbox.split(",");
            SrsBoundingBox srsbb= new SrsBoundingBox();
            srsbb.setSrs("EPSG:28992");
            srsbb.setMinx(values[0]);
            srsbb.setMiny(values[1]);
            srsbb.setMaxx(values[2]);
            srsbb.setMaxy(values[3]);
            kaartenbalieTopLayer.addSrsbb(srsbb);
        }
        
        // Creeer geldige service provider
        ServiceProviderValidator spv = new ServiceProviderValidator(serviceproviders);
        ServiceProvider validServiceProvider = spv.getValidServiceProvider();
        validServiceProvider.setTopLayer(kaartenbalieTopLayer);
        
        /*
         * B3Partners Configuration Layers..
         */
        Map configLayers = ConfigLayer.getConfigLayers();
        Iterator iterLayerKeys = configLayers.keySet().iterator();
        while(iterLayerKeys.hasNext()) {
            Layer configLayer = ConfigLayer.forName((String) iterLayerKeys.next());
            configLayer.setServiceProvider(validServiceProvider);
            kaartenbalieTopLayer.addLayer(configLayer);
        }
        
        
        Set roles = dbUser.getUserroles();
        if (roles!=null) {
            Iterator roleIt = roles.iterator();
            while (roleIt.hasNext()) {
                Roles role = (Roles)roleIt.next();
                validServiceProvider.addRole(role);
            }
        }
        return validServiceProvider;
    }
    
    //Nieuwe methode om de urls samen te stellen en gelijk alle rechten te controleren
    //deze methode werkt sneller en efficienter dan de bovenstaande getserviceprovider
    //methode in combinatie met de code voor controle van layer rechten.
    
    protected List getSeviceProviderURLS(String [] layers, Integer orgId, boolean checkForQueryable, DataWrapper dw) throws Exception {
        
        EntityManager em = MyEMFDatabase.getEntityManager();
        
        // Hier komt elke sp precies een keer uit als tenminste de database
        // correct is en er maar een toplayer (parent==null) is!
        Map toplayerMap = new HashMap();
        String query = "select l.serviceproviderid, l.layerid, sp.url " +
                "from layer l, serviceprovider sp " +
                "where l.parentid IS NULL and " +
                "l.serviceproviderid = sp.serviceproviderid";
        List toplayerQuery = em.createNativeQuery(query).getResultList();
        if (toplayerQuery==null || toplayerQuery.isEmpty()) {
            log.error("no toplayers found!");
            throw new Exception(GETMAP_EXCEPTION);
        }
        
        Iterator toplayerit = toplayerQuery.iterator();
        while (toplayerit.hasNext()) {
            Object [] objecten = (Object [])toplayerit.next();
            Map spInfo = new HashMap();
            spInfo.put("spId", (Integer)objecten[0]);
            spInfo.put("tlId", (Integer)objecten[1]);
            spInfo.put("spUrl", (String)objecten[2]);
            toplayerMap.put(spInfo.get("spId"), spInfo);
        }
        
        /* B3P Layering...*/
        Map spInfo = new HashMap();
        spInfo.put("spId", new Integer(-1));
        spInfo.put("tlId", new Integer(-1));
        spInfo.put("spUrl", SERVICEPROVIDER_BASE_HTTP);
        toplayerMap.put(spInfo.get("spId"), spInfo);
        /* EO B3P Layering... */
        
        // Per kaartlaag wordt uitgezocht tot welke sp hij hoort,
        // er voldoende rechten zijn voor de kaart en of aan
        // de queryable voorwaarde is voldaan
        List eventualSPList = new ArrayList();
        //List spList = null;
        
        
        /* Accounting... */
        AccountManager am = AccountManager.getAccountManager(orgId);
        TransactionLayerUsage tlu = am.beginTLU();
        LayerCalculator lc = new LayerCalculator();
        /* End of Accounting */
        
        /*B3Partners Layers ConfigMap */
        Map config = new HashMap();
        /*
         */
        try {
            Date validationDate = new Date();
            BigDecimal units = new BigDecimal(1);
            if(layers.length >= 1) {
                
                /*
                 * B3P Layering...
                 * Order the layers so that all the b3p layers are on top!
                 */
                layers = sortB3PLayering(layers);
                for (int i = 0; i < layers.length; i++) {
                    String[] layerCodeAndName = toCodeAndName(layers[i]);
                    String layerCode = layerCodeAndName[0];
                    String layerName= layerCodeAndName[1];
                    /*
                     * Check if the layer is an configurationlayer, if it is, proces the info..
                     */
                    
                    if (layerCode.equals(SERVICEPROVIDER_BASE_ABBR)) {
                        ConfigLayer cl = ConfigLayer.forName(layerName);
                        if (cl == null) {
                            throw new Exception("Config Layer " + layerName + " not found!");
                        }
                        cl.processConfig(config);
                        addToServerProviderList(eventualSPList,new Integer(-1), toplayerMap, layerName);
                    } else {
                        
                        
                        
                        // Check of voldoende rechten op layer bestaan en ophalen url
                        query = "select layer.queryable, layer.serviceproviderid, layer.layerid " +
                                "from layer, organizationlayer, serviceprovider " +
                                "where organizationlayer.layerid = layer.layerid and " +
                                "layer.serviceproviderid = serviceprovider.serviceproviderid and " +
                                "organizationlayer.organizationid = :orgId and " +
                                "layer.name = :layerName and " +
                                "serviceprovider.abbr = :layerCode";
                        List sqlQuery = em.createNativeQuery(query)
                        .setParameter("orgId", orgId)
                        .setParameter("layerName", layerName)
                        .setParameter("layerCode", layerCode)
                        .getResultList();
                        if(sqlQuery.isEmpty()) {
                            log.error("layer not valid or no rights, name: " + layers[i]);
                            throw new Exception(GETMAP_EXCEPTION);
                        }
                        Object [] objecten = (Object [])sqlQuery.get(0);
                        String layer_queryable      = (String)objecten[0];
                        Integer serviceprovider_id  = (Integer)objecten[1];
                        Integer layerId  = (Integer)objecten[2];
                        if (serviceprovider_id==null)
                            continue;
                        
                        // layer toevoegen aan sp indien queryable voorwaarde ok
                        if (!checkForQueryable || (checkForQueryable && layer_queryable.equals("1"))) {
                            /* Accounting... */
                            if (AccountManager.isEnableAccounting()) {
                                LayerPriceComposition lpc = lc.calculateLayerComplete(layerId,validationDate,  units, LayerPricing.PAY_PER_REQUEST, "WMS", dw.getOperation());
                                tlu.registerUsage(lpc);
                            }
                            /* End of Accounting */
                            
                            // Haal de laatst opgehaalde sp info er bij.
                            // Hier worden nu ook de layers aan toegevoegd indien
                            // zelfde sp, anders nieuwe sp aanmaken en list toevoegen
                            addToServerProviderList(eventualSPList,serviceprovider_id, toplayerMap, layerName);
                        }
                    }
                }
            } else {
                log.error("No layers found!");
                throw new Exception(GETMAP_EXCEPTION);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            lc.closeEntityManager();
        }
        
        /* Accounting... */
        if (AccountManager.isEnableAccounting()) {
            if (tlu.getCreditAlteration().doubleValue()> 0) {
                Boolean allowTransactions = (Boolean) config.get(AllowTransactionsLayer.configValue);
                if (allowTransactions == null || (allowTransactions != null && allowTransactions.booleanValue() == false)) {
                    throw new ConfigLayerException(ConfigLayer.forName(AllowTransactionsLayer.NAME));
                }
            }
        }
        /* End of Accounting */
        
        
        return eventualSPList;
    }
    
    
    private void addToServerProviderList(List eventualSPList, Integer serviceprovider_id, Map toplayerMap, String layerName) {
        Map spInfo = null;
        int size = eventualSPList.size();
        if (size>0) {
            Map lastSpInfo = (Map) eventualSPList.get(size - 1);
            Integer spId = (Integer)lastSpInfo.get("spId");
            if (spId!=null && spId.intValue()==serviceprovider_id.intValue())
                spInfo = lastSpInfo;
        }
        if (spInfo == null) {
            spInfo = new HashMap();
            Map tlSpInfo = (Map)toplayerMap.get(serviceprovider_id);
            spInfo.put("spId", tlSpInfo.get("spId"));
            spInfo.put("tlId", tlSpInfo.get("tlId"));
            spInfo.put("spUrl", tlSpInfo.get("spUrl"));
            eventualSPList.add(spInfo);
        }
        StringBuffer sp_layerlist = (StringBuffer)spInfo.get("layersList");
        if (sp_layerlist==null) {
            sp_layerlist = new StringBuffer(layerName);
            spInfo.put("layersList",sp_layerlist);
        } else {
            sp_layerlist.append(",");
            sp_layerlist.append(layerName);
        }
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
    // <editor-fold defaultstate="" desc="getOnlineData(DataWrapper dw, ArrayList urls, boolean overlay, String REQUEST_TYPE) method.">
    protected static void getOnlineData(DataWrapper dw, ArrayList urlWrapper, boolean overlay, String REQUEST_TYPE) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedImage [] bi = null;
        
        //The list is given in the opposit ranking. Therefore we first need to swap the list.
        int size = urlWrapper.size();
        ArrayList swaplist = new ArrayList(size);
        for (int i = size - 1; i >= 0; i--) {
            swaplist.add(urlWrapper.get(i));
        }
        urlWrapper = swaplist;
        
        /* To save time, this method checks first if the ArrayList contains more then one url
         * If it contains only one url then the method doesn't have to load the image into the G2D
         * environment, which saves a lot of time and capacity because it doesn't have to decode
         * and recode the image.
         */
        long startprocestime = System.currentTimeMillis();
        Map parameterMap = new HashMap();
        DataMonitoring rr = dw.getRequestReporting();
        parameterMap.put("MsSinceRequestStart", new Long(rr.getMSSinceStart()));
        if (urlWrapper.size() > 1) {
            if (REQUEST_TYPE.equalsIgnoreCase(WMS_REQUEST_GetMap)) {
                /*
                 * Log the time in ms from the start of the clientrequest.. (Reporting)
                 */
                
                ImageManager imagemanager = new ImageManager(urlWrapper, dw);
                imagemanager.process();
                long endprocestime = System.currentTimeMillis();
                Long time = new Long(endprocestime - startprocestime);
                dw.setHeader("X-Kaartenbalie-ImageServerResponseTime", time.toString());
                
                
                parameterMap.put("Duration", time);
                rr.addRequestOperation(ServerTransferOperation.class, parameterMap);
                
                imagemanager.sendCombinedImages(dw);
            } else if (REQUEST_TYPE.equalsIgnoreCase(WMS_REQUEST_GetFeatureInfo)) {
                
                int totalDataSend = 0;
                /*
                 * Create a DOM document and copy all the information of the several GetFeatureInfo
                 * responses into one document. This document has the same layout as the recieved
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
                HashMap localParameterMap = new HashMap(dw.getRequestParameterMap());
                for (int i = 0; i < urlWrapper.size(); i++) {
                    WMSRequest wmsRequest = (WMSRequest)urlWrapper.get(i);
                    String url = wmsRequest.getProviderRequestURI();
                    source = builder.parse( url );
                    copyElements(source, destination);
                    localParameterMap.put("BytesSend", new Long(url.getBytes().length));
                    localParameterMap.put("ProviderRequestURI", url);
                    localParameterMap.put("ServiceProviderId", wmsRequest.getServiceProviderId());
                    localParameterMap.put("MsSinceRequestStart", new Long(rr.getMSSinceStart()));
                    //TODO Make smarter and more complete!
                    localParameterMap.put("BytesReceived", new Long(-1));
                    localParameterMap.put("ResponseStatus", new Integer(-1));
                    rr.addServiceProviderRequest(dw.getRequestClassType(),localParameterMap);
                }
                
                OutputFormat format = new OutputFormat(destination);
                format.setIndenting(true);
                XMLSerializer serializer = new XMLSerializer(baos, format);
                serializer.serialize(destination);
                dw.write(baos);
            }
        } else {
            if(!urlWrapper.isEmpty()) {
                getOnlineData(dw, (WMSRequest)urlWrapper.get(0), REQUEST_TYPE);
            } else {
                if (REQUEST_TYPE.equalsIgnoreCase(WMS_REQUEST_GetFeatureInfo)) {
                    throw new Exception(FEATUREINFO_EXCEPTION);
                } else if (REQUEST_TYPE.equalsIgnoreCase(WMS_REQUEST_GetLegendGraphic)) {
                    throw new Exception(LEGENDGRAPHIC_EXCEPTION);
                }
            }
        }
    }
    // </editor-fold>
    
    /** Private method getOnlineData which handels the throughput of information when it is only
     *  about one URL. This is a slightly different method, because no checks have to be done or
     *  information has to be stored. Everything can be directly send through the open connection.
     *
     * @param dw DataWrapper object which handles sending the information over the request.
     * @param url String object which has the specific url where the information should come from.
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="" desc="getOnlineData(DataWrapper dw, String url)">
    private static void getOnlineData(DataWrapper dw, WMSRequest wmsRequest, String REQUEST_TYPE) throws Exception {
        /*
         * Because only one url is defined, the images don't have to be loaded into a
         * BufferedImage. The data recieved from the url can be directly transported to the client.
         */
        String url = wmsRequest.getProviderRequestURI();
        DataMonitoring rr = dw.getRequestReporting();
        HashMap localParameterMap = new HashMap(dw.getRequestParameterMap());
        localParameterMap.put("ServiceProviderId", wmsRequest.getServiceProviderId());
        localParameterMap.put("ProviderRequestURI", url);
        localParameterMap.put("BytesSend", new Long(url.getBytes().length));
        long startTime = System.currentTimeMillis();
        try {
            if (REQUEST_TYPE.equalsIgnoreCase(WMS_REQUEST_GetMap) && url.startsWith(SERVICEPROVIDER_BASE_HTTP)) {
                //B3PLayering...
                long time = System.currentTimeMillis() - startTime;
                try {
                    BufferedImage[] bi = new BufferedImage[] {ConfigLayer.handleRequest(url, dw.getLayeringParameterMap()) } ;
                    KBImageTool.writeImage(bi, "image/png", dw);
                    localParameterMap.put("BytesReceived", new Long(dw.getContentLength()));
                    localParameterMap.put("ResponseStatus", new Integer(200));
                } catch (Exception e) {
                    localParameterMap.put("ResponseStatus", new Integer(404));
                    throw e;
                }
                localParameterMap.put("RequestResponseTime", new Long(time));
            } else {
                HttpClient client = new HttpClient();
                GetMethod method = new GetMethod(url);
                client.getHttpConnectionManager().getParams().setConnectionTimeout((int)maxResponseTime);
                String rhValue = "";
                
                try {
                    int statusCode = client.executeMethod(method);
                    long time = System.currentTimeMillis() - startTime;
                    dw.setHeader("X-Kaartenbalie-ImageServerResponseTime", String.valueOf(time));
                    localParameterMap.put("ResponseStatus", new Integer(statusCode));
                    localParameterMap.put("RequestResponseTime", new Long(time));
                    if (statusCode != HttpStatus.SC_OK) {
                        log.error("Error connecting to server. Status code: " + statusCode);
                        throw new Exception("Error connecting to server. Status code: " + statusCode);
                    }
                    
                    rhValue = method.getResponseHeader("Content-Type").getValue();
                    
                    if (rhValue.equalsIgnoreCase(WMS_PARAM_EXCEPTION_XML)) {
                        InputStream is = method.getResponseBodyAsStream();
                        String body = getServiceException(is);
                        log.error("xml error response for request identified by: " + dw.getOgcrequest().getParametersArray().toString());
                        throw new Exception(body);
                    }
                    
                    dw.setContentType(rhValue);
                    dw.write(method.getResponseBodyAsStream());
                    localParameterMap.put("BytesReceived", new Long(dw.getContentLength()));
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
            localParameterMap.put("ExceptionMessage", e.getMessage());
            localParameterMap.put("ExceptionClass", e.getClass());
            throw e;
        } finally {
            rr.addServiceProviderRequest(dw.getRequestClassType(),localParameterMap);
        }
    }
    // </editor-fold>
    
    /** Constructor of the WMSCapabilitiesReader.
     *
     * @param byteStream InputStream object in which the serviceexception is stored.
     *
     * @ return String with the given exception
     *
     * @throws IOException, SAXException
     */
    // <editor-fold defaultstate="" desc="getServiceException(InputStream byteStream)">
    private static String getServiceException(InputStream byteStream) throws IOException, SAXException {
        Switcher s = new Switcher();
        s.setElementHandler("ServiceException", new ServiceExceptionHandler());
        
        XMLReader reader = org.xml.sax.helpers.XMLReaderFactory.createXMLReader();
        reader.setContentHandler(s);
        InputSource is = new InputSource(byteStream);
        is.setEncoding(CHARSET);
        reader.parse(is);
        return (String)stack.pop();
    }
    // </editor-fold>
    
    /**
     * Below is the Handler defined which reads the Exception from a ServiceException recieved when an error occurs.
     */
    // <editor-fold defaultstate="" desc="private inner class ServiceExceptionHandler">
    private static class ServiceExceptionHandler extends ElementHandler {
        StringBuffer sb;
        public void startElement(String uri, String localName, String qName, Attributes atts) {
            sb = new StringBuffer();
        }
        
        public void characters(char[] chars, int start, int len) {
            sb.append(chars, start, len);
        }
        
        public void endElement(String uri, String localName, String qName) {
            stack.push(sb.toString());
        }
    }
    // </editor-fold>
    
    /** Method which copies information from one XML document to another document.
     * It adds information to an document and with this method it's possible to create
     * one document from several other documents as used to create an GetFeatureInfo
     * document.
     *
     * @param source Document object
     * @param destination Document object
     */
    // <editor-fold defaultstate="" desc="copyElements(Document source, Document destination)">
    private static void copyElements(Document source, Document destination) {
        Element root_source = source.getDocumentElement();
        NodeList nodelist_source = root_source.getChildNodes();
        int size_source = nodelist_source.getLength();
        
        for (int i = 0; i < size_source; i ++) {
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
    // </editor-fold>
    
    /** Processes the parameters and creates a byte array with the needed information.
     *
     * @param parameters Map parameters
     *
     * @return byte[]
     *
     * @throws IOException
     */
    // <editor-fold defaultstate="" desc="abstract getRequest(Map params) method, overriding the getRequest(Map params) declared in the interface.">
    public abstract void getRequest(DataWrapper dw, User user) throws IOException, Exception;
    // </editor-fold>
    
    private String[] sortB3PLayering(String[] layers) throws Exception {
        String[] sortedLayers = new String[layers.length];
        int index= 0;
        for (int i = 0; i < layers.length; i++) {
            String[] layerCodeAndName = toCodeAndName(layers[i]);
            if (!layerCodeAndName[0].equals(SERVICEPROVIDER_BASE_ABBR)) {
                sortedLayers[index] = layers[i];
                index++;
            }
        }
        for (int i = 0; i < layers.length; i++) {
            String[] layerCodeAndName = toCodeAndName(layers[i]);
            if (layerCodeAndName[0].equals(SERVICEPROVIDER_BASE_ABBR)) {
                sortedLayers[index] = layers[i];
                index++;
            }
        }
        return sortedLayers;
    }
    
    private String[] toCodeAndName(String completeLayerName) throws Exception{
        // Check of layers[i] juiste format heeft
        int pos = completeLayerName.indexOf("_");
        if (pos==-1 || completeLayerName.length()<=pos+1) {
            log.error("layer not valid: " + completeLayerName);
            throw new Exception(GETMAP_EXCEPTION);
        }
        String layerCode = completeLayerName.substring(0, pos);
        String layerName = completeLayerName.substring(pos + 1);
        if (layerCode.length()==0 || layerName.length()==0) {
            log.error("layer name or code not valid: " + layerCode + ", " + layerName);
            throw new Exception(GETMAP_EXCEPTION);
        }
        return new String[] {layerCode, layerName};
    }
    
    
}