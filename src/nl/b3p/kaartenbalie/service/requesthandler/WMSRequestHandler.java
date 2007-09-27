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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import nl.b3p.wms.capabilities.KBConstants;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.ServiceProvider;
import nl.b3p.wms.capabilities.SrsBoundingBox;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.service.LayerValidator;
import nl.b3p.kaartenbalie.service.MyDatabase;
import nl.b3p.kaartenbalie.service.ServiceProviderValidator;
import nl.b3p.wms.capabilities.ElementHandler;
import nl.b3p.wms.capabilities.Switcher;
import org.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import java.util.HashMap;
import nl.b3p.kaartenbalie.service.ImageManager;
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
    
    public WMSRequestHandler() {}
        
    public ServiceProvider getServiceProvider() {
        Layer kaartenbalieTopLayer = new Layer();
        kaartenbalieTopLayer.setTitle(TOPLAYERNAME);        
        
        Session sess = MyDatabase.currentSession();
        Transaction tx = sess.beginTransaction();
        
        User dbUser = (User)sess.createQuery("from User u where " +
                "lower(u.id) = lower(:userid)").setParameter("userid", user.getId()).uniqueResult();
        
        Set organizationLayers = dbUser.getOrganization().getOrganizationLayer();
        if (organizationLayers == null)
            return null;
        
        Set clonedOrganizationLayers = new HashSet();
        Map serviceproviders = new HashMap();
        Iterator orgIt = organizationLayers.iterator();
        while(orgIt.hasNext()) {
            Layer tempLayer = (Layer)orgIt.next();
            clonedOrganizationLayers.add(tempLayer.clone());                    
                    
            ServiceProvider sp = (ServiceProvider)(tempLayer.getServiceProvider()).clone();
            if(!serviceproviders.containsKey(sp.getId())) {
                serviceproviders.put(sp.getId(), sp);
            }
        }
        
        LayerValidator lv = new LayerValidator(organizationLayers);
        kaartenbalieTopLayer.addSrsbb(lv.validateLatLonBoundingBox());
        
        HashMap topLayers = new HashMap();
        Iterator it = organizationLayers.iterator();
        while (it.hasNext()) {
            Layer orgLayer = (Layer) it.next();
            Layer topLayer = (Layer)orgLayer.getTopLayer().clone();
            topLayer.setLayers(null);
            if(!topLayers.containsKey(topLayer.getId())) {
                topLayers.put(topLayer.getId(), topLayer);
            }
        }
        
        Set topLayerList = new HashSet(topLayers.values());
        
        lv = new LayerValidator(topLayerList);
        String [] supportedSRS = lv.validateSRS();
        for (int i=0; i < supportedSRS.length; i++){
            SrsBoundingBox srsbb= new SrsBoundingBox();
            srsbb.setSrs(supportedSRS[i]);
            kaartenbalieTopLayer.addSrsbb(srsbb);
        }
        
        it = topLayerList.iterator();
        while (it.hasNext()) {
            Layer layer = (Layer)it.next();
            kaartenbalieTopLayer.addLayer(this.addToParent(organizationLayers, layer));
        }
        
        ServiceProviderValidator spv = new ServiceProviderValidator(new HashSet(serviceproviders.values()));
        ServiceProvider validServiceProvider = spv.getValidServiceProvider();
        
        Set roles = dbUser.getUserroles();
        if (roles!=null) {
            it = roles.iterator();
            while (it.hasNext()) {
                Roles role = (Roles)it.next();
                validServiceProvider.addRole(role);
            }
        }
        
        Layer clonedLayer = (Layer)kaartenbalieTopLayer.clone();
        validServiceProvider.addLayer(clonedLayer);
        tx.commit();
        return validServiceProvider;
    }
    
    public Layer addToParent(Set layers, Layer parent) {
        Iterator it = layers.iterator();
        while(it.hasNext()) {
            Layer possibleChild = (Layer)it.next();
            Layer childsParent = possibleChild.getParent();
            Layer possiblechildCloned = (Layer)possibleChild.clone();
            possiblechildCloned.setLayers(null);
            if(childsParent != null) {
                if (childsParent.getId() != null) {
                    if(childsParent.getId().equals(parent.getId())) {
                        //possibleChild is a direct child of this parent.
                        //Add this to child to it's parent and delete it from the list of layers.
                        parent.addLayer(this.addToParent(layers, possiblechildCloned));
                    }
                }
            }
        }
        return parent;
    }
    
    
    
    //Nieuwe methode om de urls samen te stellen en gelijk alle rechten te controleren
    //deze methode werkt sneller en efficienter dan de bovenstaande getserviceprovider
    //methode in combinatie met de code voor controle van layer rechten.
    
    protected ArrayList getSeviceProviderURLS(String [] layers, Integer orgId, boolean checkForQueryable) throws Exception {
        ArrayList spUrls = new ArrayList();
        Session sess = MyDatabase.currentSession();
        Transaction tx = sess.beginTransaction();
        
        String topLayerId = null;
        int layerlength = layers.length;
        if(layerlength >= 1) {
            for (int i = 0; i < layers.length; i++) {
                String layerid = "";
                String layername = "";
                try {
                    layerid = layers[i].substring(0, layers[i].indexOf("_"));
                    layername = layers[i].substring(layers[i].indexOf("_") + 1, layers[i].length());
                } catch (Exception e) {
                    throw new Exception(GETMAP_EXCEPTION);
                }
                String query =
                        "SELECT tempTabel.LAYER_ID, tempTabel.LAYER_NAME, tempTabel.LAYER_QUERYABLE, serviceprovider.SERVICEPROVIDERID, serviceprovider.URL" +
                        " FROM serviceprovider INNER JOIN (SELECT layer.LAYERID AS LAYER_ID, layer.NAME AS LAYER_NAME," +
                        " layer.QUERYABLE AS LAYER_QUERYABLE, layer.SERVICEPROVIDERID AS LAYER_SPID FROM layer JOIN" +
                        " organizationlayer ON organizationlayer.LAYERID = layer.LAYERID AND organizationlayer.ORGANIZATIONID = :orgId" +
                        " ) AS tempTabel ON tempTabel.LAYER_SPID = serviceprovider.SERVICEPROVIDERID AND tempTabel.LAYER_ID = :layerid" +
                        " AND tempTabel.LAYER_NAME = :layername";

                List sqlQuery = sess.createSQLQuery(query)
                        .setParameter("orgId", orgId)
                        .setParameter("layerid", layerid)
                        .setParameter("layername", layername)
                        .list();
                if(sqlQuery.isEmpty()) {
                    throw new Exception(GETMAP_EXCEPTION);
                }
                Object [] objecten = (Object [])sqlQuery.get(0);

                Integer layer_id            = (Integer)objecten[0];
                String layer_name           = (String)objecten[1];
                String layer_queryable      = (String)objecten[2];
                Integer serviceprovider_id  = (Integer)objecten[3];
                String serviceprovider_url  = (String)objecten[4];


                String [] sp_layerlist = null;
                boolean spUrlsEmpty = spUrls.isEmpty();
                boolean equalIds = false;
                if(!spUrlsEmpty) {
                    sp_layerlist = (String []) spUrls.get(spUrls.size() - 1);
                    equalIds = sp_layerlist[0].equals(serviceprovider_id.toString());
                }


                if(equalIds) {
                    layer_name = "," + layer_name;
                } else {
                    Layer layer = (Layer) sess.createQuery("from Layer where id = :layerid").setParameter("layerid", layer_id).uniqueResult();
                    Layer topLayer = layer.getTopLayer();
                    topLayerId = topLayer.getId().toString();
                }

                //TODO:
                //onderstaande klopt niet helemaal... stel er komt een layer die bij dezelfde serviceprovider hoort
                //dan wordt deze layer wel met behulp van de if aan deze urls toegevoegd, maar de layer_id blijft gewoon
                //staan op het id dat door de eerste layer gegeven werd.
                //daarnaast mmoet er per layer bij komen te staan of deze layer queryable is....


                if (checkForQueryable) {
                    if(layer_queryable.equals("1")) {
                        if(!spUrlsEmpty && equalIds) {
                            sp_layerlist[2] += (layer_name);
                            spUrls.set(spUrls.indexOf(sp_layerlist), sp_layerlist);
                        } else {
                            sp_layerlist = new String []{serviceprovider_id.toString(), serviceprovider_url, (layer_name), topLayerId};
                            spUrls.add(sp_layerlist);
                        }
                    }
                } else {
                    if(!spUrlsEmpty && equalIds) {
                        sp_layerlist[2] += (layer_name);
                        spUrls.set(spUrls.indexOf(sp_layerlist), sp_layerlist);
                    } else {
                        sp_layerlist = new String []{serviceprovider_id.toString(), serviceprovider_url, (layer_name), topLayerId};
                        spUrls.add(sp_layerlist);
                    }
                }
            }
        } else {
            throw new Exception(GETMAP_EXCEPTION);
        }
        tx.commit();
        return spUrls;
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
    protected static void getOnlineData(DataWrapper dw, ArrayList urls, boolean overlay, String REQUEST_TYPE) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedImage [] bi = null;
        
        /* To save time, this method checks first if the ArrayList contains more then one url
         * If it contains only one url then the method doesn't have to load the image into the G2D
         * environment, which saves a lot of time and capacity because it doesn't have to decode
         * and recode the image.
         */
        if (urls.size() > 1) {
            if (REQUEST_TYPE.equalsIgnoreCase(WMS_REQUEST_GetMap)) {
                ImageManager imagemanager = new ImageManager(urls);
                imagemanager.process();
                imagemanager.sendCombinedImages(dw);
            } else if (REQUEST_TYPE.equalsIgnoreCase(WMS_REQUEST_GetFeatureInfo)) {
                /*
                 * Create a DOM document and copy all the information of the several GetFeatureInfo
                 * responses into one document. This document has the same layout as the recieved
                 * documents and will hold all the information of the specified objects.
                 * After combining these documents, the new document will be sent onto the request.
                 */
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setValidating(true);
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
                for (int i = 0; i < urls.size(); i++) {
                    source = builder.parse( (String)urls.get(i) );
                    copyElements(source, destination);
                }
                
                OutputFormat format = new OutputFormat(destination);
                format.setIndenting(true);
                XMLSerializer serializer = new XMLSerializer(baos, format);
                serializer.serialize(destination);
                dw.write(baos);
            }
        } else {
            if(!urls.isEmpty()) {
                getOnlineData(dw, (String)urls.get(0));
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
    private static void getOnlineData(DataWrapper dw, String url) throws Exception {
        /*
         * Because only one url is defined, the images don't have to be loaded into a
         * BufferedImage. The data recieved from the url can be directly transported to the client.
         */
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url);
        client.getHttpConnectionManager().getParams().setConnectionTimeout((int)maxResponseTime);
        
        String rhValue = "";
        
        try {
            long startTime = System.currentTimeMillis();
            int statusCode = client.executeMethod(method);
            long time = System.currentTimeMillis() - startTime;
            dw.setHeader("X-Kaartenbalie-debug1", String.valueOf(time));
            
            if (statusCode != HttpStatus.SC_OK) {
                throw new Exception("Error connecting to server. Status code: " + statusCode);
            }
            
            rhValue = method.getResponseHeader("Content-Type").getValue();
            
            if (rhValue.equalsIgnoreCase(WMS_PARAM_EXCEPTION_XML)) {
                InputStream is = method.getResponseBodyAsStream();
                String body = getServiceException(is);
                throw new Exception(body);
            }
            
            dw.setContentType(rhValue);
            dw.write(method.getResponseBodyAsStream());
        } catch (HttpException e) {
            log.error("Fatal protocol violation: " + e.getMessage());
            throw new HttpException("Fatal protocol violation: " + e.getMessage());
        } catch (IOException e) {
            log.error("Fatal transport error: " + e.getMessage());
            throw new IOException("Fatal transport error: " + e.getMessage());
        } finally {
            method.releaseConnection();
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
    public abstract void getRequest(DataWrapper dw, Map params) throws IOException, Exception;
    // </editor-fold>
}