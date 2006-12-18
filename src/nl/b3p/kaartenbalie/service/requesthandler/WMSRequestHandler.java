package nl.b3p.kaartenbalie.service.requesthandler;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nl.b3p.kaartenbalie.core.server.Layer;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.ServiceProvider;
import nl.b3p.kaartenbalie.service.MyDatabase;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * @(#)WMSRequestHandler.java
 *
 *
 * @author N. de Goeij
 * @version 1.00 2006/12/13
 *
 * Superclass of all the WMS request classes. Subclasses which belong to this superclass are:
 * - GetCapabilitiesRequestHandler
 * - GetMapRequestHandler
 * - GetFeatureInfoRequestHandler
 * - GetLegendGraphicRequestHandler
 * - GetStylesRequestHandler
 * - DescribeLayerRequestHandler
 */

public abstract class WMSRequestHandler implements RequestHandler {
    protected String url = "http://localhost:8084/kaartenbalie/servlet/CallWMSServlet/98100ae84e47961542bc82c14f19aa4f";
    protected String version = "1.1.1";
    protected String service = "WMS";
    protected String request;
    protected static Log log = null;
    protected Organization organization;
    private Object [] params;
    
    public WMSRequestHandler() { this.init(); }
    
    // <editor-fold defaultstate="collapsed" desc="init().">
    private void init() {
        // Zet de logger
        log = LogFactory.getLog(this.getClass());
        
        if (log.isInfoEnabled()) {
            log.info("Initializing Call WMS Servlet");
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="getServiceProviders(boolean combine).">
    //make a connection to the database and get all the layers which the user is allowed to see
    //use the organizationID in the SQL statement to get only those Layers which can be viewed by this organization
    //Hoe te werk gaan?
    //als gecombineerd moet worden dan moet er van alle sp's een sp gemaakt worden....
    
    protected List getServiceProviders(boolean combine) {
        //Initialize a couple of variables
        ServiceProvider sp          = null;
        List sps                    = new ArrayList();
        Set layers                  = new HashSet();
        Set dbLayers               = null;
        int shortestCapabilities    = 7;
        Session sess                = null;
        
        sess = MyDatabase.getSessionFactory().getCurrentSession();
        Transaction tx = sess.beginTransaction();
        try {
            dbLayers = getOrganization().getOrganizationLayer();//sess.createQuery("from Layer l left join fetch l.latLonBoundingBox left join fetch l.attribution").list();
        } catch (Exception e){
            log.error("Exception occured in getServiceProvider: " + e);
        }
        
        if(null != dbLayers) {
            Iterator it = dbLayers.iterator();
            while (it.hasNext()) {
                Layer dbLayer = (Layer)it.next();
                if(null == dbLayer.getParent()) {
                    ServiceProvider cloneSP = (ServiceProvider)(dbLayer.getServiceProvider().clone());
                    Layer cloneLayer = (Layer)dbLayer.clone();
                    
                    cloneSP.setLayers(null);
                    cloneSP.addLayer(cloneLayer);
                    
                    if (combine) {
                        int size = cloneSP.getDomainResource().size();
                        if (size < shortestCapabilities) {
                            shortestCapabilities = size;
                            sp = cloneSP;
                            sp.setLayers(null);
                        }
                        layers.add(cloneLayer);
                    } else {
                        sps.add(cloneSP);
                    }
                }
            }
        } else {
            log.error("No results were found in DB. Empty list.");
        }
        
        if (combine) {
            Layer layer = new Layer();
            layer.setLayers(layers);
            sp.addLayer(layer);
            sps.add(sp);
        }
        sess.clear();
        return sps;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="getOnlineData(String xml).">
    protected static byte[] getOnlineData(String xml) throws IOException {
        InputStream byteArrayInputStream = null;
        ByteArrayOutputStream baos = null;
        byte[] bytes = null;
        
        try {
            byte currentXMLBytes[] = xml.getBytes();
            byteArrayInputStream = new ByteArrayInputStream(currentXMLBytes);
            baos = new ByteArrayOutputStream();
            
            int read;
            byte[] buffer = new byte[8192];
            
            while ((read = byteArrayInputStream.read()) > -1) {
                baos.write(read);
            }
            
            baos.flush();
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            byteArrayInputStream.close();
            baos.close();
        }
        return bytes;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="getOnlineData(String [] urls).">
    protected static byte[] getOnlineData(StringBuffer [] urls) throws IOException {
        DataInputStream di = null;
        FileOutputStream fo = null;
        InputStream is = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedInputStream bis = null;
        byte[] bytes = null;
        
        for (int i = 0; i < urls.length; i++) {
            
            if (null != urls[i]) {
                    System.out.println(urls[i]);
                    //hier de controle uitvoeren
                    
                    
                    String url = urls[i].toString();
                    String findString = "LAYERS=";
                    int len = findString.length();
                    boolean foundString = false;
                    
                    int y = 0;
                    while (!url.regionMatches(y, findString, 0, len)) {
                        y++;
                        foundString = true;
                    }
                    if (foundString) {
                        if(!url.substring(y+len, y+len + 1).equals("&")) {
                            URL u = new URL(urls[i].toString());
                            HttpURLConnection con = (HttpURLConnection) u.openConnection();
                            
                            try {
                                is = con.getInputStream();
                                bis = new BufferedInputStream(is);
                                
                                int read;
                                byte[] buffer = new byte[8192];
                                
                                while ((read = is.read()) > -1) {
                                    baos.write(read);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (NullPointerException npe) {
                                System.out.println("Going wrong here!!!!");
                            } finally {
                                bis.close();
                                is.close();
                            }
                        }
                    }
            }
        }
        baos.flush();
        bytes = baos.toByteArray();
        baos.close();        
        return bytes;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="findLayer(Set layers, String l, ServiceProvider s).">
    //private boolean found;
    private String found = null;
    private int depth = 0;
    protected String findLayer(Set layers, String l, ServiceProvider s) {
        if (depth == 0) {
            found = null;
        }
        if(null != layers) {
            Iterator it = layers.iterator();
            while (it.hasNext()) {
                if(found != null) {
                    return found;
                } else {
                    Object obj = it.next();
                    Layer layer = (Layer) obj;
                    String identity = layer.getId() + "_" + layer.getName();
                    if(identity.equalsIgnoreCase(l) && layer.getServiceProvider().getId().equals(s.getId())) {
                        found = layer.getName();
                        return found;
                    } else {
                        if(null != layer.getLayers()) {
                            depth++;
                            findLayer(layer.getLayers(), l, s);
                            depth--;
                        }
                    }
                }
            }
            
        }
        return found;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Inherited methods from interface RequestHandler.">    
    public abstract byte[] getRequest(Map params) throws IOException, Exception;
        
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
    
    public String getRequestType() {
        return request;
    }    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Get and Set methods.">
    protected void setRequestType(String request) {
        this.request = request;
    }
    
    protected Organization getOrganization() {
        return organization;
    }

    protected void setOrganization(Organization organization) {
        this.organization = organization;
    }
    
    protected String getUrl() {
        return url;
    }
    
    protected void setUrl(String url) {
        this.url = url;
    }
    
    public void setParameters(Object [] parameters) {
        this.params = parameters;
    }
    // </editor-fold>
}