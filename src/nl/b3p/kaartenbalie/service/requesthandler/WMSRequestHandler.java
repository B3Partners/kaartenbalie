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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import nl.b3p.kaartenbalie.core.KBConstants;
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
import org.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public abstract class WMSRequestHandler implements RequestHandler, KBConstants {
    
    private static final Log log = LogFactory.getLog(WMSRequestHandler.class);
    protected User user;
    protected String url;
    
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
        try {
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
             */ 
            Set serviceproviders = new HashSet();
            Iterator it = dbLayers.iterator();
            while (it.hasNext()) {
                Layer dbLayer = (Layer)it.next();
                if(null == dbLayer.getParent()) {
                    serviceproviders.add( (ServiceProvider)(dbLayer.getServiceProvider()) );
                    clonedLayers.add((Layer)dbLayer.clone());
                    
                    ServiceProvider clonedServiceprovider = (ServiceProvider)(((ServiceProvider)(dbLayer.getServiceProvider())).clone());
                    clonedServiceprovider.setLayers(null);
                    
                    clonedServiceprovider.addLayer((Layer)dbLayer.clone());
                    clonedsps.add(clonedServiceprovider);
                }
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
                layer.setLayers(clonedLayers);
                
                /* If, and only if, a layer has a <Name>, then it is a map layer that can be requested by using
                 * that Name in the LAYERS parameter of a GetMap request. If the layer has a Title but no
                 * Name, then that layer is only a category title for all the layers nested within. A Map
                 * Server that advertises a Layer containing a Name element shall be able to accept that
                 * Name as the value of LAYERS argument in a GetMap request and return the
                 * corresponding map. A Client shall not attempt to request a layer that has a Title but no
                 * Name.
                 */
                lv = new LayerValidator(clonedLayers);
                String [] supportedSRS = lv.validateSRS();
                for (int i=0; i < supportedSRS.length; i++){
                    SrsBoundingBox srsbb= new SrsBoundingBox();
                    srsbb.setSrs(supportedSRS[i]);
                    layer.addSrsbb(srsbb);
                }
                
                validServiceProvider.addLayer(layer);
                sps.add(validServiceProvider);
            } else {
                sps = clonedsps;
            }
        } finally {
            tx.commit();
        }
        return sps;
    }
    // </editor-fold>
        
    /** Creates a byte array of a given StringBuffer array with urls. Each of the url will be used for a connection to
     * the ServiceProvider which this url holds.
     *
     * @param urls StringBuffer with the urls where kaartenbalie should connect to to recieve the requested data.
     * @param overlay A boolean setting the overlay to true or false. If false is chosen the images are placed under eachother
     * @return byte[]
     *
     * @throws IOException
     */
    // <editor-fold defaultstate="" desc="getOnlineData(StringBuffer [] urls) method.">
    protected static void getOnlineData(DataWrapper dw, ArrayList urls, boolean overlay, String REQUEST_TYPE) throws ParserConfigurationException, IOException, SAXException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedImage [] bi = null;
        
        /* To save time, this method checks first if the ArrayList contains more then one url
         * If it contains only one url then the method doesn't have to load the image into the G2D
         * environment, which saves a lot of time and capacity because it doesn't have to decode
         * and recode the image.
         */
        if (urls.size() > 1) {
            if (REQUEST_TYPE.equalsIgnoreCase(WMS_REQUEST_GetMap)) {
                bi = new BufferedImage [urls.size()];
                
                /* Read each of the strings and create an URL for each one of them
                 * With the defined URL create a BufferedImage which will contain the
                 * image the URL was loacting to.
                 */
                for (int i = 0; i < urls.size(); i++) {
                    String url = ((StringBuffer)urls.get(i)).toString();
                    URL u = new URL(url);
                    bi[i] = ImageIO.read(u);
                }
                
                /* After all images are loaded into the memory, these images
                 * can be combined (blended) to make it one image. In order to
                 * be able to blend the images, an empty Graphics 2D object is
                 * needed to project all the other images onto. This Graphics
                 * object is recieved through a new and empty BufferedImage which
                 * has the same size as our BufferedImages.
                 */
                BufferedImage buffImg = null;
                
                if(overlay) {
                    buffImg = new BufferedImage(bi[0].getWidth(), bi[0].getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics2D gbi = buffImg.createGraphics();
                    
                    /* Onto this Graphics 2D object we draw the layer which is the lowest in ranking.
                     * After drawing this layer we draw all the other layers on top of it, setting the
                     * AlphaComposite on the highest alpha (1.0f) with a DST_OVER
                     */
                    gbi.drawImage(bi[0], 0, 0, null);
                    gbi.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OVER, 1.0f));
                    
                    for (int i = 1; i < urls.size(); i++) {
                        gbi.drawImage(bi[i], 0, 0, null);
                    }
                } else {
                    int [] width = new int[urls.size()];
                    int [] height= new int[urls.size()];
                    
                    for (int i = 1; i < urls.size(); i++) {
                        width[i] = bi[i].getWidth();
                        height[i] = bi[i].getHeight();
                    }
                    
                    int maxWidth = 0;
                    int maxHeight= 0;
                    for (int i = 0; i < urls.size(); i++) {
                        if (width[i] > maxWidth) {
                            maxWidth = width[i];
                        }
                        maxHeight += height[i];
                    }
                    
                    buffImg = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_RGB);
                    Graphics2D gbi = buffImg.createGraphics();
                    maxHeight = 0;
                    for (int i = 0; i < urls.size(); i++) {
                        gbi.drawImage(bi[i], 0, maxHeight, null);
                        maxHeight = bi[i].getHeight();
                    }
                    
                    //gbi.dispose();
                }
                
                /* All images have been drawn onto the Graphics 2D object so now
                 * it is possible to create a byte output stream of this newly created
                 * BufferedImage
                 */
                ImageIO.write(buffImg, dw.getContentType().substring(dw.getContentType().indexOf("/") + 1), baos);
            } else if (REQUEST_TYPE.equalsIgnoreCase(WMS_REQUEST_GetFeatureInfo)) {
                //combineer de featureinfo....
                //Setting up the DocumentBuilderFactory
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                //Setting the validations, namespace awareness and whitespace handling
                dbf.setValidating(true);
                dbf.setNamespaceAware(true);
                dbf.setIgnoringElementContentWhitespace(true);

                //Creating a new document builder
                DocumentBuilder builder = dbf.newDocumentBuilder();

                //Creating a new destination
                Document destination = builder.newDocument();
                Element rootElement = destination.createElement("msGMLOutput");
                destination.appendChild(rootElement);
                rootElement.setAttribute("xmlns:gml", "http://www.opengis.net/gml");
                rootElement.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
                rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");

                Document source = null;
                for (int i = 0; i < urls.size(); i++) {
                    //Creating the source
                    source = builder.parse( ((StringBuffer)urls.get(i)).toString() );

                    //Copying the elements from one document to the other
                    copyElements(source, destination);
                }

                /*
                 * Create a new output format to which this document should be translated and
                 * serialize the tree to an XML document type
                 */
                OutputFormat format = new OutputFormat(destination);
                format.setIndenting(true);
                XMLSerializer serializer = new XMLSerializer(baos, format);
                serializer.serialize(destination);
            }
            dw.write(baos);
            //return dw;
        } else {            
            /*
             * Because only one url is defined, the images don't have to be loaded into a
             * BufferedImage. The data recieved from the url can be directly transported to the client.
             */
            URL url = new URL( ((StringBuffer)urls.get(0)).toString() );
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            dw.setContentType(con.getContentType());
            dw.write(new BufferedInputStream(con.getInputStream()));
            //return dw;
        }
    }
    // </editor-fold>
    
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