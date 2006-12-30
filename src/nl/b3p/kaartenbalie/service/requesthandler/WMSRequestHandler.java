package nl.b3p.kaartenbalie.service.requesthandler;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nl.b3p.kaartenbalie.core.KBConstants;
import nl.b3p.kaartenbalie.core.server.Layer;
import nl.b3p.kaartenbalie.core.server.ServiceDomainResource;
import nl.b3p.kaartenbalie.core.server.ServiceProvider;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.service.MyDatabase;
import org.hibernate.Session;
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

public abstract class WMSRequestHandler implements RequestHandler, KBConstants {
    
    private static final Log log = LogFactory.getLog(WMSRequestHandler.class);
    
    protected User user;
    protected String url;
    
    public WMSRequestHandler() { }
    
    //make a connection to the database and get all the layers which the user is allowed to see
    //use the organizationID in the SQL statement to get only those Layers which can be viewed by this organization
    //Hoe te werk gaan?
    //als gecombineerd moet worden dan moet er van alle sp's een sp gemaakt worden....
    
    protected List getServiceProviders(boolean combine) {
        
        Session sess = MyDatabase.currentSession();
        //sess.createQuery("from Layer l left join fetch l.latLonBoundingBox left join fetch l.attribution").list();
        Set dbLayers = user.getOrganization().getOrganizationLayer();
        
        if (dbLayers==null)
            return null;
        
        //Initialize a couple of variables
        ServiceProvider sp = null;
        Set layers = new HashSet();
        int shortestCapabilities = 7;
        List sps = new ArrayList();
        
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
                    // TODO
                    // wat gebeurt hier???
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
        
        if (combine) {
            Layer layer = new Layer();
            layer.setLayers(layers);
            sp.addLayer(layer);
            sps.add(sp);
        }
        return sps;
    }
    
    protected static byte[] getOnlineData(String xml) throws IOException {
        // TODO hoe zit dit met de encoding
        return xml.getBytes();
    }
    
    protected static byte[] getOnlineData(StringBuffer [] urls) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        for (int i = 0; i < urls.length; i++) {
            
            if (null != urls[i]) {
                String url = urls[i].toString();
                log.debug("url: " + url);
                
                //hier de controle uitvoeren
                String findString = "LAYERS=";
                
                if (url.indexOf(findString)==-1) {
                    // geen layers
                    continue;
                }
                
                // snap ik niet
                // TODO
//                boolean foundString = false;
//                int len = findString.length();
//                int y = 0;
//                while (!url.regionMatches(y, findString, 0, len)) {
//                    y++;
//                    foundString = true;
//                }
//                if (foundString) {
//                    if(!url.substring(y+len, y+len + 1).equals("&")) {
//                       //
//                    }
//                }
                
                URL u = new URL(url);
                HttpURLConnection con = (HttpURLConnection) u.openConnection();
                
                BufferedInputStream bis = null;
                try {
                    bis = new BufferedInputStream(con.getInputStream());
                    
                    int read;
                    byte[] buffer = new byte[8192];
                    
                    while ((read = bis.read()) > -1) {
                        baos.write(read);
                    }
                } finally {
                    if (bis!=null)
                        bis.close();
                }
            }
        }
        return baos.toByteArray();
    }
    
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
    
    protected String calcFormattedLayers(ServiceProvider s, String[] layer) {
        if (s==null)
            return null;
        Set spLayers = s.getLayers();
        if (spLayers==null || spLayers.size()==0)
            return null;
        StringBuffer requestedLayers = null;
        for (int i = 0; i < layer.length; i++) {
            String foundLayer = findLayer(spLayers, layer[i], s);
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
    
    protected StringBuffer calcRequestUrl(ServiceProvider s, String request) {
        Set domain = s.getDomainResource();
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
    
    public abstract byte[] getRequest(Map params) throws IOException, Exception;
    
    
    public User getUser() {
        return user;
    }
    
    public String getUrl() {
        return url;
    }
}