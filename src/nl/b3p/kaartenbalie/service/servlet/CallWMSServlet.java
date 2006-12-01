/*
 * CallWMSServlet.java
 *
 * Created on 25 oktober 2006, 10:21
 */

package nl.b3p.kaartenbalie.service.servlet;

import java.io.*;
import java.lang.reflect.Method;
import java.net.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.*;
import javax.servlet.http.*;
import nl.b3p.kaartenbalie.core.server.ContactInformation;
import nl.b3p.kaartenbalie.core.server.Layer;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.ServiceDomainResource;
import nl.b3p.kaartenbalie.core.server.ServiceProvider;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.service.MyDatabase;
import nl.b3p.kaartenbalie.struts.WMSCapabilitiesReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import org.xml.sax.SAXException;

/**
 *
 * @author Nando De Goeij
 * @version
 * http://localhost:8084/kaartenbalie/servlet/CallWMSServlet/98100ae84e47961542bc82c14f19aa4f?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetCapabilities 
 * http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/ijmond.map&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetCapabilities
 * http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gbkn.map&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetCapabilities
 * http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gstreet-demo.map&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetCapabilities
 * 
 */
public class CallWMSServlet extends HttpServlet {
    private static Log log = null;
    private Integer organizationID = null;
    private String personalURL = "";
    
    
    /** Initializes the servlet. */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        // Zet de logger
        log = LogFactory.getLog(this.getClass());
        
        if (log.isInfoEnabled()) {
            log.info("Initializing Call WMS Servlet");
        }
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        byte[] imageData = null;
        byte[] data = null;
        String mime = null;
        String szRequest = null;
        
        //System.out.println("Er komt ondanks alles wel een reuqest binnen");
        
        organizationID = null;
        boolean login = checkLogin(request);
        if(login) {
            try {
                data = parseRequestAndData(request);
                String s = new String(data);
                System.out.println(s);
            } catch (Exception e) {
                log.error(e.toString());
                return;
            }
            response.setContentType("text/xml;charset=UTF-8");
            try {
                OutputStream sos = response.getOutputStream();
                sos.write(data);
                sos.close();
            } catch (IOException ioe) {
                log.error("Response write error: ", ioe);
            }
        } else {
            response.sendError(response.SC_UNAUTHORIZED);
        }
    }
    
    public boolean checkLogin(HttpServletRequest request) throws IOException {
        //Hier moet een manier komen om te controleren of de gebruiker vanaf de eigen viewer of vanaf een
        //externe viewer het request doet.
        String u = null;
        //Lets first try to find out if someone is using a personal URL
        personalURL = request.getRequestURL().toString();
        Session sess = MyDatabase.currentSession();
        Transaction tx = sess.beginTransaction();
        User user = (User)sess.createQuery(
                "from User u where " +
                "lower(u.personalURL) = lower(:personalURL) ")
                .setParameter("personalURL", personalURL)
                .uniqueResult();
        
        //Check the result given by the database
        //If null is returned this means that the person who sent the request has
        //or not a legitimate URL
        //or has no URL given but is trying to login with his username and password
        if (null == user) {
            try{
                u = request.getUserPrincipal().getName();
                //System.out.println("Blijkbaar is dit al voldoende om mee te controleren!!!! : " + u);
                if (u == null) {
                    System.out.println("Bij dit request is er geen user bekend : " + u);
                }

                if(u != null) {
                    System.out.println("Bij dit request is er een user bekend : " + u);
                    return true;
                }
            } catch (Exception e) {}
            return false;
        }  else {
            String registeredIP = request.getRemoteAddr();
            String username = user.getUsername();
            String password = user.getPassword();


            String hashedString = "";
            try {
                String toBeHashedString = registeredIP + username + password;
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(toBeHashedString.getBytes("8859_1"));
                BigInteger hash = new BigInteger(1, md.digest());
                hashedString = hash.toString( 16 );
            } catch (NoSuchAlgorithmException nsae) {
                nsae.printStackTrace();
            }

            String pathInfo = request.getPathInfo();
            pathInfo = pathInfo.substring(1);

            if (!pathInfo.equals(hashedString)) {
                return false;
            }
            //check for all the layers this person and create an xml document with this information?
            organizationID = user.getOrganization().getId();
            return true;
        }       
    }
    
    
    //er is een capabilities request
    //aan welke voorwaarden moet dit request voldoen?
    //Welke SERVICE (R), welke VERSION (O), welke REQUEST (R), welke UPDATESEQUENCE (O)
    
    
    public byte[] parseRequestAndData(HttpServletRequest request) throws Exception {
        byte [] data = null;
        String givenRequest = request.getParameter("REQUEST");
        
        if(null !=givenRequest && !(givenRequest == "")) {
            
 //           System.out.println("There is an incoming request!");
 //           System.out.println("The request path info is : " + request.getPathInfo());
 //           System.out.println("The given request is " + givenRequest);

            if(givenRequest.equalsIgnoreCase("GetCapabilities")) {
    
                String version          = request.getParameter("VERSION");
                String service          = request.getParameter("SERVICE");
                String updateSequence   = request.getParameter("UPDATESEQUENCE");
                
                if(service.equalsIgnoreCase("WMS")) {
                    if(version.equalsIgnoreCase("1.1.1") || version.equalsIgnoreCase("1.1.0")) {
                        //do something
                        ServiceProvider s = null;
                        List sps = this.getServiceProviders(true);
                        Iterator it = sps.iterator();
                        while (it.hasNext()) {
                            s = (ServiceProvider)it.next();
                            try{
                                Method m = s.getClass().getDeclaredMethod("overwriteURL", String.class);
                                m.setAccessible(true);
                                m.invoke(s, new String(personalURL));
                            } catch (Exception e) { log.error("Error rewriting the URL's : " + e); }
                        }
                        data = this.getOnlineData(s);
                    } else {
                        log.error("Unsupported wms VERSION: " + version);
                        throw new Exception("Not supported wms VERSION: " + version);
                    }
                } else {
                    log.error("Unsupported GetCapabilities SERVICE: " + service);
                    throw new Exception("Not supported GetCapabilities SERVICE: " + service);
                }
            } else if (givenRequest.equalsIgnoreCase("GetMap")) {
                //There is another kind of request
                //Create the right request for getting the maps which are requested
                //First get all the information from the request
                /* REQUIRED:
                 * - VERSION
                 * - REQUEST
                 * - LAYERS
                 * - STYLES
                 * - SRS
                 * - BBOX
                 * - WIDTH
                 * - HEIGHT
                 * - FORMAT
                 * OPTIONAL:
                 * - TRANSPARENT
                 * - BGCOLOR
                 * - EXCEPTIONS
                 * - TIME
                 * - ELEVATION
                */
                String version  = request.getParameter("VERSION");
                String service  = request.getParameter("SERVICE");
                String layer    = request.getParameter("LAYERS");
                String styles   = request.getParameter("STYLES");
                String srs      = request.getParameter("SRS");
                String bbox     = request.getParameter("BBOX");
                String width    = request.getParameter("WIDTH");
                String height   = request.getParameter("HEIGHT");
                String format   = request.getParameter("FORMAT");
                
//                System.out.println("VERSION="+version);
//                System.out.println("SERVICE="+service);
//                System.out.println("LAYERS="+layer);
//                System.out.println("STYLES="+styles);
//                System.out.println("SRS="+srs);
                System.out.println("BBOX="+bbox);
//                System.out.println("WIDTH="+width);
//                System.out.println("HEIGHT="+height);
//                System.out.println("FORMAT="+format);
                

                
                
                //Check first if all the required field are given
                if(!version.equalsIgnoreCase("1.1.1")) {
                    throw new Exception("Not supported wms VERSION: " + version);
                }
                if(null == layer) {
                    throw new Exception("Parameter required, wms LAYERS");
                }
                if(null == srs) {
                    throw new Exception("Parameter required, wms SRS");
                }
                if(null == bbox) {
                    throw new Exception("Parameter required, wms BBOX");
                }
                if(null == width) {
                    throw new Exception("Parameter required, wms WIDTH");
                }
                if(null == height) {
                    throw new Exception("Parameter required, wms HEIGHT");
                }
                if(null == format) {
                    throw new Exception("Parameter required, wms FORMAT");
                }
                
                String layers[]= layer.split(",");
                List tempSP = this.getServiceProviders(false);
                StringBuffer [] url = new StringBuffer[tempSP.size()];
                boolean [] urlLogger = new boolean[tempSP.size()];
                if(null != tempSP) {
                    int counter = 0;
                    Iterator it = tempSP.iterator();
                    
                    int loopje1 = 0;
                    int loopje2 = 0;
                    while (it.hasNext()) {
                        ServiceProvider s = (ServiceProvider)it.next();
                        Set tempLayers = s.getLayers();
                        System.out.println("loopje1 : " + loopje1);
                        loopje1++;
                        if(null != tempLayers) {
                            Set domain = s.getDomainResource();
                            Iterator domainIter = domain.iterator();
                            while (domainIter.hasNext()) {
                                ServiceDomainResource sdr = (ServiceDomainResource)domainIter.next();
                                System.out.println("loopje2 : " + loopje2);
                                loopje2++;
                                if(sdr.getDomain().equalsIgnoreCase(givenRequest)) {
                                    System.out.println("givenRequest : " + givenRequest);
                                    System.out.println("sdr.getDomain() : " + sdr.getDomain());
                                    if(null != sdr.getPostUrl()) {
                                        url[counter] = new StringBuffer (sdr.getPostUrl());
                                    } else {
                                        url[counter] = new StringBuffer (sdr.getGetUrl());
                                    }
                                    /*
                                    try {
                                        int pos = url[counter].lastIndexOf("?");
                                    } catch (Exception e){
                                        url[counter].append("?");
                                    }*/
                                }
                            }
                            /*
                            try {
                                int length = url[counter].length();
                                int pos = url[counter].lastIndexOf("?");
                                
                                if(length > pos) {
                                    url[counter].append("?");
                                }
                            } catch (Exception e){}        
                            */
                            
                            url[counter].append("VERSION=" + version + "&REQUEST=" + givenRequest + "&LAYERS=");
                            
                            for (int i = 0; i < layers.length; i++) {
                                String foundLayer = this.findLayer(tempLayers, layers[i], s);
                                if(null != foundLayer) {
                                    url[counter].append(foundLayer);
                                    url[counter].append(",");
                                    urlLogger[counter] = true;
                                }                            
                            }
                            
                            try {
                                url[counter].deleteCharAt(url[counter].lastIndexOf(","));
                            } catch (Exception e){}
                            url[counter].append("&BBOX=" + bbox + "&SRS=" + srs + "&WIDTH=" + width + "&HEIGHT=" + height + "&FORMAT=" + format + "&TRANSPARENT=TRUE");
                            counter++;
                        }
                    }
                }
                data = this.getOnlineData(url, urlLogger);
            } else if (givenRequest.equalsIgnoreCase("GetFeatureInfo")) {
                String version      = request.getParameter("VERSION");
                String layer        = request.getParameter("QUERY_LAYERS");
                String infoFormat   = request.getParameter("INFO_FORMAT");
                String featurecount = request.getParameter("FEATURECOUNT");
                String x            = request.getParameter("X");
                String y            = request.getParameter("Y");
                
                //Check first if all the required field are given
                if(null == layer) {
                    throw new Exception("Parameter required, wms LAYERS");
                }
                if(null == x) {
                    throw new Exception("Parameter required, wms x");
                }
                if(null == y) {
                    throw new Exception("Parameter required, wms y");
                }

                String layers[]= layer.split(",");
                List tempSP = this.getServiceProviders(false);
                StringBuffer [] url = new StringBuffer[tempSP.size()];
                boolean [] urlLogger = new boolean[tempSP.size()];
                if(null != tempSP) {
                    int counter = 0;
                    Iterator it = tempSP.iterator();
                    while (it.hasNext()) {
                        ServiceProvider s = (ServiceProvider)it.next();
                        Set tempLayers = s.getLayers();
                        
                        if(null != tempLayers) {
                            Set domain = s.getDomainResource();
                            Iterator domainIter = domain.iterator();
                            while (domainIter.hasNext()) {
                                ServiceDomainResource sdr = (ServiceDomainResource)domainIter.next();
                                if(sdr.getDomain().equalsIgnoreCase(givenRequest)) {
                                    if(null != sdr.getPostUrl()) {
                                        url[counter] = new StringBuffer (sdr.getPostUrl());
                                    } else {
                                        url[counter] = new StringBuffer (sdr.getGetUrl());
                                    }
                                }
                            }
                            
                            try {
                                int length = url[counter].length();
                                int pos = url[counter].lastIndexOf("?");
                                
                                if(length != pos) {
                                    url[counter].append("?");
                                }
                            } catch (Exception e){
                                url[counter].append("?");
                            }
                            
                            url[counter].append("REQUEST=" + givenRequest + "&VERSION=" + version + "&QUERY_LAYERS=");
                            
                            for (int i = 0; i < layers.length; i++) {
                                String foundLayer = this.findLayer(tempLayers, layers[i], s);
                                if(null != foundLayer) {
                                    url[counter].append(foundLayer);
                                    url[counter].append(",");
                                    urlLogger[counter] = true;
                                }                            
                            }
                            
                            try {
                                url[counter].deleteCharAt(url[counter].lastIndexOf(","));
                            } catch (Exception e){}
                            if(null != infoFormat) {
                                url[counter].append("&INFO_FORMAT=" + infoFormat);
                            }
                            if (null != featurecount) {
                                url[counter].append("&FEATURECOUNT=" + featurecount);
                            }
                            url[counter].append("&X=" + x + "&Y=" + y);
                            counter++;
                        }
                    }
                }
                data = this.getOnlineData(url, urlLogger);
            } else if (givenRequest.equalsIgnoreCase("GetStyles")) {
                System.out.println("Not implemented yet. Unsupported request: " + givenRequest);
            } else if (givenRequest.equalsIgnoreCase("GetLegendGraphic")) {
                String version      = request.getParameter("VERSION");
                String style        = request.getParameter("STYLE");
                String layer        = request.getParameter("LAYER");
                String featureType  = request.getParameter("FEATURETYPE");
                String rule         = request.getParameter("RULE");
                String scale        = request.getParameter("SCALE");
                String sld          = request.getParameter("SLD");
                String sldBody      = request.getParameter("SLD_BODY");
                String format       = request.getParameter("FORMAT");
                String width        = request.getParameter("WIDTH");
                String height       = request.getParameter("HEIGHT");
                
                //Check first if all the required field are given
                if(null == style) {
                    throw new Exception("Parameter required, wms LAYERS");
                }

                String layers[]= layer.split(",");
                List tempSP = this.getServiceProviders(false);
                StringBuffer [] url = new StringBuffer[tempSP.size()];
                boolean [] urlLogger = new boolean[tempSP.size()];
                if(null != tempSP) {
                    int counter = 0;
                    Iterator it = tempSP.iterator();
                    while (it.hasNext()) {
                        ServiceProvider s = (ServiceProvider)it.next();
                        Set tempLayers = s.getLayers();
                        
                        if(null != tempLayers) {
                            Set domain = s.getDomainResource();
                            Iterator domainIter = domain.iterator();
                            while (domainIter.hasNext()) {
                                ServiceDomainResource sdr = (ServiceDomainResource)domainIter.next();
                                if(sdr.getDomain().equalsIgnoreCase(givenRequest)) {
                                    if(null != sdr.getPostUrl()) {
                                        url[counter] = new StringBuffer (sdr.getPostUrl());
                                    } else {
                                        url[counter] = new StringBuffer (sdr.getGetUrl());
                                    }
                                }
                            }
                            
                            try {
                                int length = url[counter].length();
                                int pos = url[counter].lastIndexOf("?");
                                
                                if(length != pos) {
                                    url[counter].append("?");
                                }
                            } catch (Exception e){
                                url[counter].append("?");
                            }
                            
                            url[counter].append("REQUEST=" + givenRequest + "STYLE=" + style + "&LAYER=");
                            
                            for (int i = 0; i < layers.length; i++) {
                                String foundLayer = this.findLayer(tempLayers, layers[i], s);
                                if(null != foundLayer) {
                                    url[counter].append(foundLayer);
                                    url[counter].append(",");
                                    urlLogger[counter] = true;
                                }                            
                            }
                            
                            try {
                                url[counter].deleteCharAt(url[counter].lastIndexOf(","));
                            } catch (Exception e){}
                            
                            if(null != featureType) {
                                url[counter].append("&FEATURETYPE=" + featureType);
                            }
                            if(null != rule) {
                                url[counter].append("&RULE=" + rule);
                            }
                            if(null != scale) {
                                url[counter].append("&SCALE=" + scale);
                            }
                            if(null != sld) {
                                url[counter].append("&SLD=" + sld);
                            }
                            if(null != sldBody) {
                                url[counter].append("&SLD_BODY=" + sldBody);
                            }
                            if(null != format) {
                                url[counter].append("&FORMAT=" + format);
                            }
                            if(null != width) {
                                url[counter].append("&WIDTH=" + width);
                            }
                            if(null != height) {
                                url[counter].append("&HEIGHT=" + height);
                            }
                            counter++;
                        }
                    }
                }
                data = this.getOnlineData(url, urlLogger);
            } else if (givenRequest.equalsIgnoreCase("DescribeLayer")) {
                System.out.println("Not implemented yet. Unsupported request: " + givenRequest);
            } else {
                log.error("Unsupported request: " + givenRequest);
                data = null;
            }
            
        }
        return data;
    }
    
    //private boolean found; 
    String found = null;
    int depth = 0;
    private String findLayer(Set layers, String l, ServiceProvider s) {
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
       
    //make a connection to the database and get all the layers which the user is allowed to see
    //use the organizationID in the SQL statement to get only those Layers which can be viewed by this organization
    //Hoe te werk gaan?
    //als gecombineerd moet worden dan moet er van alle sp's een sp gemaakt worden....
    
    private List getServiceProviders(boolean combine) {
        //Initialize a couple of variables
        ServiceProvider sp          = null;
        List sps                    = new ArrayList();
        Set layers                  = new HashSet();
        List dbLayers               = null;
        int shortestCapabilities    = 7;
        Session sess                = null;
        
        sess = MyDatabase.getSessionFactory().getCurrentSession();
        Transaction tx = sess.beginTransaction();        
        try {
            dbLayers = sess.createQuery("from Layer l left join fetch l.latLonBoundingBox left join fetch l.attribution").list();
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
       
    public static byte[] getOnlineData(ServiceProvider serviceProvider) throws IOException {
        InputStream byteArrayInputStream = null;
        ByteArrayOutputStream baos = null;
        byte[] bytes = null;
        
        try {
            String myString = serviceProvider.toString();
            byte currentXMLBytes[] = myString.getBytes();
            byteArrayInputStream = new ByteArrayInputStream(currentXMLBytes); 
            baos = new ByteArrayOutputStream();
            
            int read;
            //Verschil met image Reader
            byte[] buffer = new byte[8192];
            //Einde vershil
            
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
    
    public static byte[] getOnlineData(StringBuffer [] urls, boolean [] urlLogger) throws IOException {
        DataInputStream di = null;
        FileOutputStream fo = null;
        InputStream is = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedInputStream bis = null;
        byte[] bytes = null;
        
//        System.out.println("The amount urls are : " + urls.length);
        //controleer voor iedere url of deze informatie heeft bij de layers, indien niet het geval laat deze urls dan met rust...
        
        
        for (int i = 0; i < urls.length; i++) {
            
            if (null != urls[i]) {
                if(urlLogger[i]) {
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
    ///                        System.out.println(url.substring(y, y+len + 1));
    //                        System.out.println("De URL is : " + urls[i]);
                            //found location of LAYERS=
                            //now find location of first & sign after LAYERS=
                            //if this location is straight after LAYERS=
                            //cutt it out...



                            URL u = new URL(urls[i].toString());
                            HttpURLConnection con = (HttpURLConnection) u.openConnection();

                            try {
                                is = con.getInputStream();
                //                baos = new ByteArrayOutputStream();
                                bis = new BufferedInputStream(is);

                                int read;
                                //Verschil met image Reader
                                byte[] buffer = new byte[8192];
                                //Einde vershil

                                while ((read = is.read()) > -1) {
                                    baos.write(read);
                                }
                                /*
                                //Verschil met image Reader
                                while ((bis.read(buffer, 0, buffer.length)) > -1) {
                                    baos.write(buffer, 0, bis.read(buffer, 0, buffer.length));
                                }
                                //Einde verschil
                                 */
                //                baos.flush();
                //                bytes = baos.toByteArray();
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
        }
        //http://localhost:8084/kaartenbalie/servlet/CallWMSServlet/nando
//        System.out.println("Gaat het hier nog goed!!!!");
        baos.flush();
        bytes = baos.toByteArray();
        baos.close();
//        System.out.println("En hier ook???!!!!");

        return bytes;
    }
    
    //Onderstaande methoden worden niet meer gebruikt?????
    public static byte[] getOnlineData(String request) throws IOException {
        DataInputStream di = null;
        FileOutputStream fo = null;
        
        URL u = new URL(request);
        HttpURLConnection con = (HttpURLConnection) u.openConnection();
        
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        BufferedInputStream bis = null;
        byte[] bytes = null;
        
        try {
            is = con.getInputStream();
            baos = new ByteArrayOutputStream();
            bis = new BufferedInputStream(is);
            
            int read;
            //Verschil met image Reader
            byte[] buffer = new byte[8192];
            //Einde vershil
            
            while ((read = is.read()) > -1) {
                baos.write(read);
            }
            /*
            //Verschil met image Reader
            while ((bis.read(buffer, 0, buffer.length)) > -1) {
                baos.write(buffer, 0, bis.read(buffer, 0, buffer.length));
            }
            //Einde verschil
             */
            baos.flush();
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bis.close();
            baos.close();
            is.close();
        }
        return bytes;
    }
    
    public void getPreferences(String username) {
        String imageformat;
        String srsFormat;
        boolean transparency;
        
        Session sess = null;
        sess = MyDatabase.getSessionFactory().getCurrentSession();
        Transaction tx = sess.beginTransaction();
        
        try {
            User u = (User) sess.createQuery(
                    "from User u where " +
                    "u.username = :username").setParameter("username", username).uniqueResult();
            
            Organization o = (Organization) sess.createQuery(
                    "from Organization o where " +
                    "o.name = :name").setParameter("name", u.getOrganization()).uniqueResult();
//            imageFormat = o.getPreferedImageFormat();
//            srsFormat = o.getPreferedSRSFormat();
//            transparency = o.getPreferedTransparency();
        } catch (Exception e){
            System.out.println("Exception occured: " + e);
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}
