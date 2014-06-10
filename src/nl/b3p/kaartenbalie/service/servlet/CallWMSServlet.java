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
package nl.b3p.kaartenbalie.service.servlet;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.b3pLayering.ExceptionLayer;
import nl.b3p.kaartenbalie.core.server.monitoring.DataMonitoring;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.service.AccessDeniedException;
import nl.b3p.kaartenbalie.service.ProviderException;
import nl.b3p.kaartenbalie.service.requesthandler.*;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class CallWMSServlet extends GeneralServlet {
    public static final long serialVersionUID = 24362462L;
    public static String CAPABILITIES_DTD = null;
    public static String EXCEPTION_DTD = null;
    public static String DESCRIBELAYER_DTD = null;

    /** Initializes the servlet.
     * Turns the logging of the servlet on.
     *
     * @param config ServletConfig config
     *
     * @throws ServletException
     */
    // <editor-fold defaultstate="" desc="init(ServletConfig config) method.">
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // Zet de logger
        log = LogFactory.getLog(this.getClass());
        log.debug("Initializing Call WMS Servlet");
    }
    // </editor-fold>

    /** Processes the incoming request and calls the various methods to create the right output stream.
     *
     * @param request servlet request
     * @param response servlet response
     *
     * @throws ServletException
     * @throws IOException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();

        StringBuffer baseUrl = createBaseUrl(request, true);
        if (CAPABILITIES_DTD == null) {
            CAPABILITIES_DTD = baseUrl.toString() + "/dtd/capabilities_1_1_1.dtd";
        }
        if (EXCEPTION_DTD == null) {
            EXCEPTION_DTD = baseUrl.toString() + "/dtd/exception_1_1_1.dtd";
        }
        if (DESCRIBELAYER_DTD == null) {
            DESCRIBELAYER_DTD = baseUrl.toString() + "/dtd/WMS_DescribeLayerResponse.dtd";
        }

        DataWrapper data = new DataWrapper(request, response);

        Object identity = null;
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            identity = MyEMFDatabase.createEntityManager(MyEMFDatabase.MAIN_EM);
            log.debug("Getting entity manager ......");
            em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);
            tx = em.getTransaction();
            tx.begin();

            DataMonitoring rr = new DataMonitoring();
            data.setRequestReporting(rr);

            String serviceName = OGCConstants.WMS_SERVICE_WMS;

            try {
                OGCRequest ogcrequest = calcOGCRequest(request);
                
                String personalCode = null;
                if (ogcrequest != null)
                    personalCode = ogcrequest.getPersonalCode();
                
                data.setOgcrequest(ogcrequest);

                String serviceParam = ogcrequest.getParameter(OGCConstants.SERVICE);
                if (serviceParam != null || !"".equals(serviceParam)) {
                    serviceName = serviceParam;
                }

                String iUrl = ogcrequest.getUrl();
                rr.startClientRequest(iUrl, iUrl.getBytes().length, startTime, request.getRemoteAddr(), request.getMethod());

                User user = checkLogin(request, personalCode);
                
                ogcrequest.checkRequestURL();
                
                Organization mainOrg = null;
                String userName = null;
                
                if (user != null) {
                    mainOrg = user.getMainOrganization();
                    userName = user.getUsername();
                }
                
                rr.setUserAndOrganization(user, mainOrg);
                data.setHeader("X-Kaartenbalie-User", userName);
                
                parseRequestAndData(data, user);

            } catch (ProviderException pex) {
            	
                log.error("Error while communicating with provider: " + pex.getLocalizedMessage());
                rr.setClientRequestException(pex);
                handleRequestException(pex, data);
            } catch (AccessDeniedException adex) {
                log.error("Error while logging in: " + adex.getLocalizedMessage());
                rr.setClientRequestException(adex);
                handleRequestException(adex, data);
            } catch (Exception ex) {
                log.error("Error while handling request: ", ex);
                
                rr.setClientRequestException(ex);
                handleRequestException(ex, data);
            } finally {
                rr.endClientRequest(serviceName, data.getOperation(), data.getContentLength(), System.currentTimeMillis() - startTime);
            }
            tx.commit();
        } catch (Exception ex) {
            log.error("Error creating EntityManager: ", ex);
            try {
                tx.rollback();
            } catch (Exception ex2) {
                log.error("Error trying to rollback: ", ex2);
            }
            handleRequestException(ex, data);
        } finally {
            //log.debug("Closing entity manager .....");
            MyEMFDatabase.closeEntityManager(identity, MyEMFDatabase.MAIN_EM);
        }
    }

    protected void handleRequestException(Exception ex, DataWrapper data) throws IOException {
        OGCRequest ogcrequest = data.getOgcrequest();

        if (ogcrequest == null) {
            data.setContentType(OGCConstants.WMS_PARAM_EXCEPTION_XML);
            handleRequestExceptionAsXML(ex, data);
        } else if (ogcrequest.getParameter(OGCConstants.SERVICE) != null
                && ogcrequest.getParameter(OGCConstants.SERVICE).equals(OGCConstants.WFS_SERVICE_WFS)) {
            data.setContentType(OGCConstants.WMS_PARAM_EXCEPTION_XML);
            handleRequestExceptionAsXML(ex, data);
        } else {
            String exType = "";
            if (ogcrequest.containsParameter(OGCConstants.WMS_PARAM_EXCEPTIONS)) {
                exType = ogcrequest.getParameter(OGCConstants.WMS_PARAM_EXCEPTIONS);
            }
            String requestparam = "";
            if (ogcrequest.containsParameter(OGCConstants.REQUEST)) {
                requestparam = ogcrequest.getParameter(OGCConstants.REQUEST);
            }
            if ((requestparam.equalsIgnoreCase(OGCConstants.WMS_REQUEST_GetMap)
                    || requestparam.equalsIgnoreCase(OGCConstants.WMS_REQUEST_GetLegendGraphic))
                    && (exType.equalsIgnoreCase(OGCConstants.WMS_PARAM_EXCEPTION_INIMAGE)
                    || exType.equalsIgnoreCase(OGCConstants.WMS_PARAM_SHORT_EXCEPTION_INIMAGE))
                    && ogcrequest.containsParameter(OGCConstants.WMS_PARAM_FORMAT)
                    && ogcrequest.containsParameter(OGCConstants.WMS_PARAM_WIDTH)
                    && ogcrequest.containsParameter(OGCConstants.WMS_PARAM_HEIGHT)) {
                data.setContentType(ogcrequest.getParameter(OGCConstants.WMS_PARAM_FORMAT));
                handleRequestExceptionAsImage(ex, data);
            } else {
                data.setContentType(OGCConstants.WMS_PARAM_EXCEPTION_XML);
                handleRequestExceptionAsXML(ex, data);
            }
        }
    }

    private void handleRequestExceptionAsImage(Exception ex, DataWrapper data) throws IOException {
        String message = ex.getMessage();
        try {
            ExceptionLayer el = new ExceptionLayer();
            Map parameterMap = new HashMap();
            parameterMap.put("type", ex.getClass());
            parameterMap.put("transparant", Boolean.TRUE);
            parameterMap.put("message", message);
            parameterMap.put("stacktrace", ex.getStackTrace());
            el.sendImage(data, el.drawImage(data.getOgcrequest(), parameterMap));
        } catch (Exception e) {
            log.error("error handling exception: ", e);
            TextToImage tti = new TextToImage();
            try {
                tti.createImage(message, data);
            } catch (Exception lex) {
                log.error("error creating error-image: ", lex);
            }
        }
    }

    private void handleRequestExceptionAsXML(Exception ex, DataWrapper data) throws IOException {
        ByteArrayOutputStream output = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        dbf.setNamespaceAware(true);
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (Exception e) {
            log.error("error: ", e);
            throw new IOException("Exception occured during creation of error message: " + e);
        }

        DOMImplementation di = db.getDOMImplementation();

        // <!DOCTYPE ServiceExceptionReport SYSTEM "http://schemas.opengeospatial.net/wms/1.1.1/exception_1_1_1.dtd"
        // <!-- end of DOCTYPE declaration -->
        DocumentType dt = di.createDocumentType("ServiceExceptionReport", null, CallWMSServlet.EXCEPTION_DTD);
        Document dom = di.createDocument(null, "ServiceExceptionReport", dt);
        Element rootElement = dom.getDocumentElement();
        rootElement.setAttribute("version", "1.1.1");

        Element serviceExceptionElement = dom.createElement("ServiceException");

        String exceptionName = ex.getClass().getName();
        String message = ex.getMessage();
        Throwable cause = ex.getCause();

        serviceExceptionElement.setAttribute("code", exceptionName);
        CDATASection cdata = null;
        if (cause != null) {
            cdata = dom.createCDATASection(message + " - " + cause);
        } else {
            cdata = dom.createCDATASection(message);
        }

        serviceExceptionElement.appendChild(cdata);
        rootElement.appendChild(serviceExceptionElement);

        OutputFormat format = new OutputFormat(dom);
        format.setIndenting(true);
        output = new ByteArrayOutputStream();
        XMLSerializer serializer = new XMLSerializer(output, format);
        serializer.serialize(dom);

        DOMValidator dv = new DOMValidator();
        try {
            dv.parseAndValidate(new ByteArrayInputStream(output.toString().getBytes(KBConfiguration.CHARSET)));
        } catch (Exception e) {
            log.error("error: ", e);
            throw new IOException("Exception occured during validation of error message: " + e);
        }

        data.setHeader("Content-Disposition", "inline; filename=\"ServiceException.xml\";");
        data.write(output);
    }

    /** Checks if an user is allowed to make any requests.
     * Therefore there is checked if a user is logged in or if a user is using a private unique IP address.
     *
     * @param parameters The parameters of a given request
     *
     * @return Map with the same parameters and same key all in uppercase
     */
    // <editor-fold defaultstate="" desc="getUpperCaseParameterMap(Map parameters) method.">
    private Map getUpperCaseParameterMap(Map parameters) {
        Map newParameterMap = new HashMap();
        Set paramKeySet = parameters.keySet();
        Iterator keySetIterator = paramKeySet.iterator();
        while (keySetIterator.hasNext()) {
            String key = (String) keySetIterator.next();
            String value = ((String[]) parameters.get(key))[0];

            String caseInsensitiveKey = key.toUpperCase();
            String caseInsensitiveValue = value.toUpperCase();

            newParameterMap.put(caseInsensitiveKey, caseInsensitiveValue);
        }
        return newParameterMap;
    }
    // </editor-fold>

    /** Parses any incoming request and redirects this request to the right handler.
     *
     * @param parameters map with the given parameters
     *
     * @return byte array with the requested data
     *
     * @throws IllegalArgumentException
     * @throws UnsupportedOperationException
     * @throws IOException
     */
    public void parseRequestAndData(DataWrapper data, User user) throws IllegalArgumentException, UnsupportedOperationException, IOException, Exception {        
        String request = data.getOgcrequest().getParameter(OGCConstants.REQUEST);
        String service = data.getOgcrequest().getParameter(OGCConstants.SERVICE);

        RequestHandler requestHandler = null;
        if (service != null && service.length() > 0) {
            if (service.equalsIgnoreCase(OGCConstants.NONOGC_SERVICE_PROXY)) {
                requestHandler = new ProxyRequestHandler();
            } else if (service.equalsIgnoreCase(OGCConstants.NONOGC_SERVICE_METADATA)) {
                requestHandler = new MetadataRequestHandler();
            } else if (service.equalsIgnoreCase(OGCConstants.WMS_SERVICE_WMS)) {
                if (request.equalsIgnoreCase(OGCConstants.WMS_REQUEST_GetCapabilities)) {
                    requestHandler = new GetCapabilitiesRequestHandler();
                }
            } else if (service.equalsIgnoreCase(OGCConstants.WFS_SERVICE_WFS)) {
                if (request.equalsIgnoreCase(OGCConstants.WFS_REQUEST_GetCapabilities)) {
                    requestHandler = new WFSGetCapabilitiesRequestHandler();
                }
            }
        }

        if (requestHandler == null) {
            if (request.equalsIgnoreCase(OGCConstants.WMS_REQUEST_GetMap)) {
                requestHandler = new GetMapRequestHandler();
                service = OGCConstants.WMS_SERVICE_WMS;
            } else if (request.equalsIgnoreCase(OGCConstants.WMS_REQUEST_GetFeatureInfo)) {
                requestHandler = new GetFeatureInfoRequestHandler();
                service = OGCConstants.WMS_SERVICE_WMS;
            } else if (request.equalsIgnoreCase(OGCConstants.WMS_REQUEST_GetLegendGraphic)) {
                requestHandler = new GetLegendGraphicRequestHandler();
                service = OGCConstants.WMS_SERVICE_WMS;
            } else if (request.equalsIgnoreCase(OGCConstants.WMS_REQUEST_DescribeLayer)) {
                requestHandler = new DescribeLayerRequestHandler();
                service = OGCConstants.WMS_SERVICE_WMS;
            } else if (request.equalsIgnoreCase(OGCConstants.WFS_REQUEST_DescribeFeatureType)) {
                requestHandler = new WFSDescribeFeatureTypeRequestHandler();
                service = OGCConstants.WFS_SERVICE_WFS;
            } else if (request.equalsIgnoreCase(OGCConstants.WFS_REQUEST_GetFeature)) {
                requestHandler = new WFSGetFeatureRequestHandler();
                service = OGCConstants.WFS_SERVICE_WFS;
            } else if (request.equalsIgnoreCase(OGCConstants.WFS_REQUEST_Transaction)) {
                requestHandler = new WFSTransactionRequestHandler();
                service = OGCConstants.WFS_SERVICE_WFS;
            }
        }

        if (requestHandler == null) {
            throw new UnsupportedOperationException("Request " + request + " is not suported!");
        }

        data.setOperation(request);
        data.setService(service);
        
        try {
            requestHandler.getRequest(data, user);   
        } catch (Exception ex) {
            log.error("Geen persoonlijke url gevonden bij deze gebruiker.");
        }
        
    }

    /** Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "CallWMSServlet info";
    }

    public static void main(String[] args) throws UnsupportedEncodingException, IOException, ParserConfigurationException, SAXException {
        HttpClient client = new HttpClient();
        client.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_0);
        client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials("Beheerder", "***REMOVED***");
        AuthScope authScope = new AuthScope("localhost", 8084);
        client.getState().setCredentials(authScope, defaultcreds);

        HttpMethod method = null;


        // POST
        String postUrl = "http://localhost:8084/kaartenbalie/services/";
        String postBody = "<GetFeature xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.opengis.net/wfs\" xsi:schemaLocation=\"http://www.opengis.net/wfs ../wfs/1.1.0/WFS.xsd\" version=\"1.0.0\" service=\"WFS\"><Query typeName=\"app:Bestemmingsplangebied\"  /></GetFeature>";
//        xmlns:app=\"http://bla\"
        method = new PostMethod(postUrl);
        //work around voor ESRI post Messages
        //method.setRequestEntity(new StringRequestEntity(body, "text/xml", "UTF-8"));
        ((PostMethod) method).setRequestEntity(new StringRequestEntity(postBody, null, null));


        // GET
//        String getUrl = "http://localhost:8084/kaartenbalie/services/";
//        method = new GetMethod(getUrl);

        int status = client.executeMethod(method);
        if (status == HttpStatus.SC_OK) {
            InputStream is = method.getResponseBodyAsStream();
            InputStream isx = null;
            byte[] bytes = null;
            int len = 1;
            byte[] buffer = new byte[2024];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while ((len = is.read(buffer, 0, buffer.length)) > 0) {
                bos.write(buffer, 0, len);
            }
            bytes = bos.toByteArray();
            System.out.println(new String(bytes));
            isx = new ByteArrayInputStream(bytes);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document doc = builder.parse(isx);
            System.out.println("parse klaar");
        }

        System.out.println("klaar");
    }
}
