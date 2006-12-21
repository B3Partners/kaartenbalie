/*
 * CallWMSServlet.java
 *
 * Created on 25 oktober 2006, 10:21
 */

package nl.b3p.kaartenbalie.service.servlet;

import java.util.HashMap;
import nl.b3p.kaartenbalie.service.requesthandler.*;
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
    private Organization organization = null;
    private String personalURL = "";
    
    // <editor-fold defaultstate="collapsed" desc="init method. Click on the + sign on the left to edit the code.">
    /** Initializes the servlet. */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        // Zet de logger
        log = LogFactory.getLog(this.getClass());
        
        if (log.isInfoEnabled()) {
            log.info("Initializing Call WMS Servlet");
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="processRequest method. Click on the + sign on the left to edit the code.">
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        byte[] imageData = null;
        byte[] data = null;
        String mime = null;
        String szRequest = null;
        organization = null;
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
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="checkLogin method. Click on the + sign on the left to edit the code.">
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
                    user = (User)sess.createQuery(
                        "from User u where " +
                        "lower(u.username) = lower(:username) ")
                        .setParameter("username", u)
                        .uniqueResult();
                    System.out.println("Bij dit request is er een user bekend : " + u);
                    organization = user.getOrganization();
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
                //System.out.println("De opgegeven user bestaat niet in het systeem");
                return false;
            }
            //check for all the layers this person and create an xml document with this information?
            //System.out.println("De opgegeven user bestaat in het systeem en alle inloggegevens zijn juist");
            organization = user.getOrganization();
            return true;
        }       
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="parseRequestAndData method. Click on the + sign on the left to edit the code.">
    public byte[] parseRequestAndData(HttpServletRequest request) throws Exception {
        Map parameters      = null;//request.getParameterMap();
        String givenRequest = request.getParameter("REQUEST");
        String service      = request.getParameter("SERVICE");
        
        
        /*
         * onderstaande manier zou een kopie moeten maken van de request.getParameterMap();
         * hiervan moet een kopie gemaakt worden omdat deze Map immutable is en er daardoor dus
         * geen objecten aan toegevoegd kunnen worden.
         * Na de kopie zou het wel mogelijk moeten zijn om er objecten aan toe te voegen, maar dan
         * speelt het probleem dat er een class cast exception gegenereerd wordt bij de klasse waar
         * dit object naar doorgestuurd wordt.
         *
        Map parameters      = new HashMap();//null;//request.getParameterMap();
        String givenRequest = request.getParameter("REQUEST");
        String service      = request.getParameter("SERVICE");
        
        Map rmap = request.getParameterMap();
        Set keyset = rmap.keySet();
        
        java.util.Iterator it = keyset.iterator();
        while (it.hasNext()) {
            String name = (String)it.next();
            Object obj  = rmap.get(name);
            parameters.put(name, obj) ;
        }
        */
        
        
        RequestHandler requestHandler = null;
        if(null != givenRequest && !(givenRequest == "")) {
            if(givenRequest.equalsIgnoreCase("GetCapabilities")) {
                parameters = new HashMap();
                parameters.put("version", request.getParameter("VERSION"));
                parameters.put("service", request.getParameter("SERVICE"));
                parameters.put("organization", this.organization);
                requestHandler = new GetCapabilitiesRequestHandler();
            } else if (givenRequest.equalsIgnoreCase("GetMap")) {
                parameters = new HashMap();
                parameters.put("version", request.getParameter("VERSION"));
                parameters.put("layers", request.getParameter("LAYERS"));
                parameters.put("styles", request.getParameter("STYLES"));
                parameters.put("srs", request.getParameter("SRS"));
                parameters.put("bbox", request.getParameter("BBOX"));
                parameters.put("width", request.getParameter("WIDTH"));
                parameters.put("height", request.getParameter("HEIGHT"));
                parameters.put("format", request.getParameter("FORMAT"));
                parameters.put("organization", this.organization);
                parameters.put("peronalURL", this.personalURL);
                requestHandler = new GetMapRequestHandler();
            } else if (givenRequest.equalsIgnoreCase("GetFeatureInfo")) {
                parameters = new HashMap();
                String mapRequestCopy = "";
                parameters.put("mapRequestCopy", mapRequestCopy);
                parameters.put("query_layers", request.getParameter("QUERY_LAYERS"));
                parameters.put("x", request.getParameter("x"));
                parameters.put("y", request.getParameter("y"));
                mapRequestCopy  = (String)parameters.get("mapRequestCopy");
                requestHandler = new GetFeatureInfoRequestHandler();
            } else if (givenRequest.equalsIgnoreCase("GetLegendGraphic")) {
                parameters = new HashMap();
                parameters.put("version", request.getParameter("VERSION"));
                parameters.put("styles", request.getParameter("STYLE"));
                parameters.put("layers", request.getParameter("LAYER"));
                parameters.put("featuretype", request.getParameter("FEATURETYPE"));
                parameters.put("rule", request.getParameter("RULE"));
                parameters.put("scale", request.getParameter("SCALE"));
                parameters.put("sld", request.getParameter("SLD"));
                parameters.put("sld_body", request.getParameter("SLD_BODY"));
                parameters.put("format", request.getParameter("FORMAT"));
                parameters.put("width", request.getParameter("WIDTH"));
                parameters.put("height", request.getParameter("HEIGHT"));
                parameters.put("organization", this.organization);
                parameters.put("peronalURL", this.personalURL);
                requestHandler = new GetLegendGraphicRequestHandler();
            } else if (givenRequest.equalsIgnoreCase("GetStyles")) {
                log.error("Unsupported request: " + givenRequest);
                return null;
            } else if (givenRequest.equalsIgnoreCase("PutStyles")) {
                log.error("Unsupported request: " + givenRequest);
                return null;
            } else if (givenRequest.equalsIgnoreCase("DescribeLayer")) {
                log.error("Unsupported request: " + givenRequest);
                return null;
            } else {
                log.error("Unsupported request: " + givenRequest);
                return null;
            }
        }
        return requestHandler.getRequest(parameters);
    }
    // </editor-fold>
            
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
