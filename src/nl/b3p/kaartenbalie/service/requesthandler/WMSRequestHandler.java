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

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import nl.b3p.kaartenbalie.service.KBConstants;
import nl.b3p.kaartenbalie.core.server.Layer;
import nl.b3p.kaartenbalie.core.server.ServiceDomainResource;
import nl.b3p.kaartenbalie.core.server.ServiceProvider;
import nl.b3p.kaartenbalie.core.server.SrsBoundingBox;
import nl.b3p.kaartenbalie.core.server.Style;
import nl.b3p.kaartenbalie.core.server.StyleDomainResource;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.service.LayerValidator;
import nl.b3p.kaartenbalie.service.MyDatabase;
import nl.b3p.kaartenbalie.service.ServiceProviderValidator;
import nl.b3p.kaartenbalie.struts.ElementHandler;
import nl.b3p.kaartenbalie.struts.Switcher;
import nl.b3p.kaartenbalie.service.KBImageTool;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HeaderElement;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.params.HttpMethodParams;
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
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;








//Even uitproberen hoe dit werkt en toepassen voor alle URL functies.
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.xml.sax.XMLReader;

public abstract class WMSRequestHandler implements RequestHandler, KBConstants {
    
    private static final Log log = LogFactory.getLog(WMSRequestHandler.class);
    protected User user;
    protected String url;
    protected static long maxResponseTime = 100000;
    
    private XMLReader parser;
    private static Stack stack = new Stack();
    private Switcher s;
    
    public WMSRequestHandler() { }
    
    /** Creates a List of ServiceProviders recieved from the Database.
     * If the boolean is set to true, this method creates a single ServiceProvider in a List which holds again all
     * layers supported by the kaartenbalie. Otherwise if the boolean is set to false the method will get each single
     * ServiceProvider from the database and stores these seperatly, with their own layers, in the List.
     *
     * @param combine boolean wether to combine the serviceproviders or not
     * @return a list of the serviceproviders
     */
    // <editor-fold defaultstate="" desc="getServiceProviders(boolean combine) method.">
    protected List getServiceProviders(boolean combine) {
        
        Session sess = MyDatabase.currentSession();
        Transaction tx = sess.beginTransaction();
        
        List sps  = new ArrayList();
        List clonedsps  = new ArrayList();
        Set dbLayers = null;
        Set clonedLayers = new HashSet();
        
        /*
         * First we a set with layers which are visible to the user doing the request
         */
        User dbUser = (User)sess.createQuery("from User u where " +
                "lower(u.id) = lower(:userid)").setParameter("userid", user.getId()).uniqueResult();

        dbLayers = dbUser.getOrganization().getOrganizationLayer();
        if (dbLayers == null)
            return null;

        /*
         * Now we have a set of layers with which we can perform our action.
         * There can be two types of actions; there can be a combination of all
         * serviceproviders or return each serviceprovider seperatly in a list.
         *
         * Before we can perform the action we need to take some action in order
         * to be able to perform the action.
         */

        /*
         * Getting the serviceproviders from the database layers.
         * By walking through all layers, checking if the layer has a parent we can
         * select only those layers which hang directly under a serviceprovider
         * 
         * First we need to clone each of the layers we recieved from the database.
         * Of these clones we need to select only the leaf layers and with those leaf layers
         * we can build up the tree to the top by calling the getParents method.
         */
        Set parents = getParents(getLeafLayers(cloneLayers(dbLayers)));

        Set serviceproviders = new HashSet();
        Iterator it = parents.iterator();
        while (it.hasNext()) {
            Layer parent = (Layer)it.next();
            ServiceProvider clonedSp = parent.getServiceProvider();
            clonedSp.setLayers(null);
            clonedSp.addLayer(parent);
            serviceproviders.add( clonedSp );
        }

        if(combine) {
            /* Now a top layer is available we need a ServiceProviderobject to store
             * this toplayer in. With the ServiceProvider there are also a couple of
             * constraints, concerning the DomainResources and their available return
             * formats. Therefore we have to validate the avaible ServiceProviders to
             * create a valid one.
             */
            ServiceProviderValidator spv = new ServiceProviderValidator(serviceproviders);
            ServiceProvider validServiceProvider = spv.getValidServiceProvider();

            /* We have still a Set of toplayers.
             * A ServiceProvider is allowed to have only one toplayer.
             * Therefore a toplayer has to be created manually.
             * This toplayer has to have a certain amount of SRS values
             * which apply to ALL layers which are child of this toplayer.
             */
            Layer layer = new Layer();
            layer.setTitle(TOPLAYERNAME);
            //layer.setName(TOPLAYERNAME);

            //Standaard LatLonBoundingBox
            //Het enige dat hier gedaan moet worden is een nieuwe methode van LayerValidator aanroepen
            //Die even controleert welke layers allemaal een llbb hebben, deze llbb's vervolgens naast
            //elkaar legt en even de minimale en maximale waarden van de verschillende minnen en maxen
            //er tussen uit pikt en deze vervolgens in een nieuw srs object plaatst en dit object terug
            //geeft aan de aanroep.
            //In het srs object moet vervolgens nog een kleine aanpassing gedaan worden mbt de getType
            //functie om de juiste types te selecteren en in de WMSCapabilityReader moet nog een kleine
            //aanpassing gedaan worden zodat een LLBB geen SRS meer meekrijgt aangezien een LLBB geen
            //SRS heeft.
            LayerValidator lv = new LayerValidator(dbLayers);
            layer.addSrsbb(lv.validateLatLonBoundingBox());
            layer.setLayers(parents);

            /* If, and only if, a layer has a <Name>, then it is a map layer that can be requested by using
             * that Name in the LAYERS parameter of a GetMap request. If the layer has a Title but no
             * Name, then that layer is only a category title for all the layers nested within. A Map
             * Server that advertises a Layer containing a Name element shall be able to accept that
             * Name as the value of LAYERS argument in a GetMap request and return the
             * corresponding map. A Client shall not attempt to request a layer that has a Title but no
             * Name.
             */
            lv = new LayerValidator(parents);
            String [] supportedSRS = lv.validateSRS();
            for (int i=0; i < supportedSRS.length; i++){
                SrsBoundingBox srsbb= new SrsBoundingBox();
                srsbb.setSrs(supportedSRS[i]);
                layer.addSrsbb(srsbb);
            }

            validServiceProvider.addLayer(layer);
            sps.add(validServiceProvider);
        } else {
            sps.addAll(serviceproviders);
        }
        tx.commit();
        return sps;
    }
    // </editor-fold>
    
    /** Creates a new Set of layers which are an exact clonecopy of the original ones.
     *
     * @param originalLayers
     * 
     * @return Set with the cloned layers
     */
    // <editor-fold defaultstate="" desc="cloneLayers(Set originalLayers) method.">
    private Set cloneLayers(Set originalLayers) {
        Set cloneLayers = new HashSet();
        Iterator orglayerIterator = originalLayers.iterator();
        while(orglayerIterator.hasNext()) {
            cloneLayers.add(cloneLayer( (Layer) orglayerIterator.next() ));
        }
        return cloneLayers;
    }
    // </editor-fold>
    
    /** Creates a new layer which is an exact clonecopy of the original one.
     *
     * @param original
     * 
     * @return Layer exact copy of the original layer
     */
    // <editor-fold defaultstate="" desc="cloneLayer(Layer original) method.">
    private Layer cloneLayer(Layer original) {
        Layer cloneLayer = (Layer) original.clone();
        if(original.getParent() != null) {
            cloneLayer.setParent(cloneLayer(original.getParent()));
        }
        cloneLayer.setServiceProvider((ServiceProvider) original.getServiceProvider().clone());
        return cloneLayer;
    }
    // </editor-fold>
    
    /** Defines a Set with layers in which only leafs are added. These have no childs.
     *
     * @param originalLayers
     * 
     * @return Set with only leaf layers
     */
    // <editor-fold defaultstate="" desc="getLeafLayers(Set orgLayers) method.">
    private Set getLeafLayers(Set originalLayers) {
        Set leafLayers = new HashSet();
        Iterator orglayerIterator = originalLayers.iterator();
        while(orglayerIterator.hasNext()) {
            Layer orglayer = (Layer)orglayerIterator.next();
            if(orglayer.getLayers().isEmpty()) {
                leafLayers.add(orglayer);
            }
        }
        return leafLayers;
    }
    // </editor-fold>
    
    /** Builds a new tree from the bottom up with only the leafs as beginning point.
     *
     * @param leafLayers
     * 
     * @return Set with the top layers which hold all children.
     */
    // <editor-fold defaultstate="" desc="getParents(Set leafLayers) method.">
    private Set getParents(Set leafLayers) {
        boolean found = false;
        Set parents = new HashSet();
        
        Iterator leaflayerIterator = leafLayers.iterator();
        while(leaflayerIterator.hasNext()) {
            Layer leafLayer = (Layer)leaflayerIterator.next();
            Layer parent = leafLayer.getParent();
            if(parent != null) {
                //This leafLayer has a parent, this means we need to take this parentlayer and put it
                //in the list of parent layers and make this leafLayer his child again.
                Iterator parentsIterator = parents.iterator();
                while (parentsIterator.hasNext()) {
                    Layer parentInSet = (Layer) parentsIterator.next();
                    if (parentInSet.getId().equals(parent.getId())) {
                        parentInSet.addLayer(leafLayer);
                        found = true;
                        break;
                    }
                }
                
                //Before we are going to add this layer in the Set of parent layers we need to check if
                //this layer is not already added by a previous child layer. If so the there is no need to
                //add it again and it was only necessary to add the leaf layer to this parent.
                if(!found) {
                    parent.setLayers(null);
                    parent.addLayer(leafLayer);
                    parents.add(parent);
                }
            } else {
                //Since this method checks in the end if we have only toplayers, we must make sure that
                //toplayers are saved each time we get into this loop for a new check.
                parents.add(leafLayer);
            }
            
            //reset the boolean
            found = false;
        }
        
        //Here we check if the created list contains only top layers.
        //If not then we need to go on recursivly untill we have only
        //toplayers in the list.
        Iterator parentsIterator = parents.iterator();
        while(parentsIterator.hasNext()) {
            Layer parent = (Layer)parentsIterator.next();
            if(parent.getParent() != null) {
                return getParents(parents);
            }
        }
        
        //Once we have a list of only top layers we can return this list.
        return parents;
    }
    // </editor-fold>
    
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
                /* Read each of the strings and create an URL for each one of them
                 * With the defined URL create a BufferedImage which will contain the
                 * image the URL was loacting to.
                 * Before we can combine each of the read images, we first need to 
                 * check for for each image we read if an error occurs during reading 
                 * or if we get an error message from the server without even reading
                 * an image. It could be that the reading of any input could go right
                 * but that the information we get during reading is not the information
                 * we expect it to be.
                 *
                 * In order to check if the information is what we expect of it we need
                 * to check what contenttype is given when the information comes is. If
                 * this contenttype is of the format EXCEPTION_XML we need to throw an
                 * exception with the same exception as we recieved from the service-
                 * provider.
                 *
                 * After all images are loaded into the memory, these images
                 * can be combined (blended) to make it one image. In order to
                 * be able to blend the images, an empty Graphics 2D object is
                 * needed to project all the other images onto. This Graphics
                 * object is recieved through a new and empty BufferedImage which
                 * has the same size as our BufferedImages.
                 */
                bi = new BufferedImage [urls.size()];
                KBImageTool kbir = new KBImageTool();
                String contentType = "";
                
                for (int i = 0; i < urls.size(); i++) {
                    String url = ((StringBuffer)urls.get(i)).toString();
                    URL u = new URL(url);
                                        
                    HttpClient client = new HttpClient();
                    GetMethod method = new GetMethod(url);
                    checkHttpMethodConnection(client, method);
                    client.getHttpConnectionManager().getParams().setConnectionTimeout((int)maxResponseTime);
                    
                    contentType = method.getResponseHeader("Content-Type").getValue();
                        
                    if (contentType.equalsIgnoreCase(WMS_PARAM_EXCEPTION_XML)) {
                        InputStream is = method.getResponseBodyAsStream();
                        String body = getServiceException(is);
                        throw new Exception(body);
                    }
                    
                    bi[i] = kbir.readImage(method, contentType);
                    method.releaseConnection();
                }
                kbir.writeImage(bi, contentType, dw);
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
                    source = builder.parse( ((StringBuffer)urls.get(i)).toString() );
                    copyElements(source, destination);
                }
                
                OutputFormat format = new OutputFormat(destination);
                format.setIndenting(true);
                XMLSerializer serializer = new XMLSerializer(baos, format);
                serializer.serialize(destination);
                dw.write(baos);
            }
        } else {
            getOnlineData(dw, ((StringBuffer)urls.get(0)).toString());
        }
    }
    // </editor-fold>
    
    /** Checks wether this httpclient can setup a connection to a given url.
     *
     * @param client HttpClient object.
     * @param method GetMethod method.
     *
     * @throws IOException
     */
    // <editor-fold defaultstate="" desc="checkHttpMethodConnection(HttpClient client, GetMethod method) method.">
    private static void checkHttpMethodConnection(HttpClient client, GetMethod method) throws Exception {
        int statusCode = client.executeMethod(method);
        if (statusCode != HttpStatus.SC_OK) {
            throw new Exception("Error connecting to server. Status code: " + statusCode);
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
            int statusCode = client.executeMethod(method);
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
    
    /** Tries to find a specified layer given for a certain ServiceProvider.
     *
     * @param layers the set with layers which the method has to surch through
     * @param l the layer to be found
     * @param s the ServiceProvider which the layer belongs to
     *
     * @return string with the name of the found layer or null if no layer was found
     */
    // <editor-fold defaultstate="" desc="findLayer(Set layers, String l, ServiceProvider s) method.">
    protected String findLayer(Set layers, String l, ServiceProvider s) {
        if (layers==null || layers.isEmpty())
            return null;
        
        Iterator it = layers.iterator();
        while (it.hasNext()) {
            Layer layer = (Layer) it.next();
            String identity = layer.getId() + "_" + layer.getName();
            if(identity.equalsIgnoreCase(l) && layer.getServiceProvider().getId().equals(s.getId()))
                return layer.getName();
            
            String foundLayer = findLayer(layer.getLayers(), l, s);
            if (foundLayer != null)
                return foundLayer;
        }
        
        return null;
    }
    // </editor-fold>
    
    /** Tries to find a specified layer given for a certain ServiceProvider.
     *
     * @param layers the set with layers which the method has to surch through
     * @param layerToBeFound the layer to be found
     *
     * @return string with the name of the found layer or null if no layer was found
     */
    // <editor-fold defaultstate="" desc="findLayer(String layerToBeFound, Set layers) method.">
    protected String findLayer(String layerToBeFound, Set layers) {
        if (layers==null || layers.isEmpty())
            return null;
        
        Iterator it = layers.iterator();
        while (it.hasNext()) {
            Layer layer = (Layer) it.next();
            String identity = layer.getId() + "_" + layer.getName();
            if(identity.equalsIgnoreCase(layerToBeFound))
                return layer.getName();
            
            String foundLayer = findLayer(layerToBeFound, layer.getLayers());
            if (foundLayer != null)
                return foundLayer;
        }
        return null;
    }
    // </editor-fold>
    
    /** Tries to find a specified layer given for a certain ServiceProvider.
     *
     * @param layers the set with layers which the method has to surch through
     * @param layerToBeFound the layer to be found
     *
     * @return string with the name of the found layer or null if no layer was found
     */
    // <editor-fold defaultstate="" desc="findQueryableLayer(String layerToBeFound, Set layers) method.">
    protected String findQueryableLayer(String layerToBeFound, Set layers) {
        if (layers==null || layers.isEmpty())
            return null;
        
        Iterator it = layers.iterator();
        while (it.hasNext()) {
            Layer layer = (Layer) it.next();
            String identity = layer.getId() + "_" + layer.getName();
            if(identity.equalsIgnoreCase(layerToBeFound)) {
                if (layer.getQueryable().equals("1")) {
                    return layer.getName();
                } else {
                    return null;
                }
            }
            
            String foundLayer = findQueryableLayer(layerToBeFound, layer.getLayers());
            if (foundLayer != null)
                return foundLayer;
        }
        return null;
    }
    // </editor-fold>
    
    /** Builds a Stringbuffer of the layers found in the database and compared with the requested layers.
     * @param serviceProvider ServiceProvider to which the layers belong
     * @param layer string array with layersto be found and added to the list
     *
     * @return string with all the request layers
     */
    // <editor-fold defaultstate="" desc="calcFormattedLayers(ServiceProvider serviceProvider, String[] layer) method.">
    protected String calcFormattedLayers(ServiceProvider serviceProvider, String[] layer) {
        if (serviceProvider==null)
            return null;
        Set spLayers = serviceProvider.getLayers();
        if (spLayers==null || spLayers.size()==0)
            return null;
        StringBuffer requestedLayers = null;
        for (int i = 0; i < layer.length; i++) {
            String foundLayer = findLayer(spLayers, layer[i], serviceProvider);
            if (foundLayer==null)
                continue;
            if (requestedLayers==null)
                requestedLayers = new StringBuffer();
            else
                requestedLayers.append(",");
            requestedLayers.append(foundLayer);
        }
        if (requestedLayers==null)
            return null;
        return requestedLayers.toString();
    }
    // </editor-fold>
    
    /** Checks wether a post or get url is available for an incomming request.
     * @param serviceProvider ServiceProvider to which the request belongs
     * @param request String with the specified request
     * @return stringbuffer with the url found in the database for this specific request
     */
    // <editor-fold defaultstate="" desc="calcRequestUrl(ServiceProvider serviceProvider, String request) method.">
    protected StringBuffer calcRequestUrl(ServiceProvider serviceProvider, String request) {
        Set domain = serviceProvider.getDomainResource();
        Iterator domainIter = domain.iterator();
        while (domainIter.hasNext()) {
            ServiceDomainResource sdr = (ServiceDomainResource)domainIter.next();
            if(sdr.getDomain().equalsIgnoreCase(request)) {
                if(sdr.getPostUrl() != null) {
                    return new StringBuffer(sdr.getPostUrl());
                } else {
                    return new StringBuffer(sdr.getGetUrl());
                }
            }
        }
        return null;
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
    
    // <editor-fold defaultstate="" desc="getter methods.">
    public User getUser() {
        return user;
    }
    
    public String getUrl() {
        return url;
    }
    // </editor-fold>
}