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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.monitoring.DataMonitoring;
import nl.b3p.kaartenbalie.core.server.monitoring.ServiceProviderRequest;
import nl.b3p.kaartenbalie.service.servlet.CallWMSServlet;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.wms.capabilities.Roles;
import nl.b3p.wms.capabilities.ServiceProvider;
import nl.b3p.wms.capabilities.WMSCapabilitiesReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

public class GetCapabilitiesRequestHandler extends WMSRequestHandler {

    private static final Log log = LogFactory.getLog(GetCapabilitiesRequestHandler.class);
    // <editor-fold defaultstate="" desc="default GetCapabilitiesRequestHandler() constructor.">

    public GetCapabilitiesRequestHandler() {
    }
    // </editor-fold>

    /**
     * Processes the parameters and creates a DocumentBuilder from the given parameters.
     * This DocumentBuilder will be used to create a XML based String which can be returned to the client.
     *
     * @param dw DataWrapper which contains all information that has to be sent to the client
     * @param user User the user which invoked the request
     *
     * @return byte[]
     *
     * @throws Exception
     * @throws IOException
     */
    // <editor-fold defaultstate="" desc="getRequest(DataWrapper dw, User user) method.">
    public void getRequest(DataWrapper dw, User user) throws IOException, Exception {
        dw.setHeader("Content-Disposition", "inline; filename=\"GetCapabilities.xml\";");
        dw.setContentType(OGCConstants.WMS_PARAM_WMS_XML);

        String spAbbrUrl = dw.getOgcrequest().getServiceProviderName();
        ByteArrayOutputStream output = null;
        this.user = user;
        this.url = user.getPersonalURL(dw.getRequest(),spAbbrUrl);
        if (url == null) {
            throw new Exception("No personal url for user found.");
        }


         /*
         * Only used if specific param is given (used for configuration)
         */
        boolean isAdmin = false;
        if ("true".equalsIgnoreCase(dw.getOgcrequest().getParameter("_VIEWER_CONFIG"))) {
            Set userRoles = user.getRoles();
            Iterator rolIt = userRoles.iterator();
            while (rolIt.hasNext()) {
                Roles role = (Roles) rolIt.next();
                if (role.getRole().equalsIgnoreCase(Roles.ADMIN)) {
                    /* de gebruiker is een beheerder */
                    isAdmin = true;
                    break;
                }
            }
        }

        /*
         * Only used if specific param is given (used for monitoring)
         */
        if ("true".equalsIgnoreCase(dw.getOgcrequest().getParameter("_FORCE_FETCH"))) {
            forceFetch(dw, isAdmin);
        }

        ServiceProvider s = getServiceProvider(isAdmin, spAbbrUrl);

        if (user != null) {
            s.setOrganizationCode(user.getOrganisationCodes());
            s.setExpireDate(user.getTimeout());
            s.setUserName(user.getName());
            s.setPersonalCode(user.getPersonalURL());
        }
        HashMap conversionValues = new HashMap();
        conversionValues.put("url", url);
        s.convertValues2KB(conversionValues);

        /*
         * Create a DocumentBuilderFactory from which a new document can be created
         */
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        DocumentBuilder db = dbf.newDocumentBuilder();
        DOMImplementation di = db.getDOMImplementation();

        // <!DOCTYPE WMT_MS_Capabilities SYSTEM "http://schemas.opengeospatial.net/wms/1.1.1/WMS_MS_Capabilities.dtd"
        // [
        // <!ELEMENT VendorSpecificCapabilities EMPTY>
        // ]>  <!-- end of DOCTYPE declaration -->

        DocumentType dt = di.createDocumentType("WMT_MS_Capabilities", null, CallWMSServlet.CAPABILITIES_DTD);
        Document dom = di.createDocument(null, "WMT_MS_Capabilities", dt);
        Element rootElement = dom.getDocumentElement();
        rootElement = s.toElement(dom, rootElement);
        rootElement.setAttribute("version", "1.1.1");

        /*
         * Create a new output format to which this document should be translated and
         * serialize the tree to an XML document type
         */
        OutputFormat format = new OutputFormat(dom, KBConfiguration.CHARSET, true);
        format.setIndenting(true);
        output = new ByteArrayOutputStream();
        XMLSerializer serializer = new XMLSerializer(output, format);
        serializer.serialize(dom);

        DOMValidator dv = new DOMValidator();
        dv.parseAndValidate(new ByteArrayInputStream(output.toString().getBytes(KBConfiguration.CHARSET)));

        dw.write(output);
    }
    // </editor-fold>

    /**
     * Do a forced fetch for monitoring
     */
    private void forceFetch(DataWrapper dw, boolean isAdmin) throws Exception {
        DataMonitoring rr = dw.getRequestReporting();

        Set serviceProviders = this.getServiceProviders(isAdmin);
        for (Object serviceProvider : serviceProviders) {
            ServiceProvider sp = (ServiceProvider) serviceProvider;
            long startprocestime = System.currentTimeMillis();

            ServiceProviderRequest wmsRequest = new ServiceProviderRequest();
            wmsRequest.setMsSinceRequestStart(new Long(rr.getMSSinceStart()));
            wmsRequest.setServiceProviderId(sp.getId());
            wmsRequest.setWmsVersion(sp.getWmsVersion());

            WMSCapabilitiesReader wms = new WMSCapabilitiesReader();
            try {
                OGCRequest or = new OGCRequest(sp.getUrl());
                or.addOrReplaceParameter(OGCConstants.WMS_REQUEST, OGCConstants.WMS_REQUEST_GetCapabilities);
                or.addOrReplaceParameter(OGCConstants.SERVICE, OGCConstants.WMS_SERVICE_WMS);
                or.addOrReplaceParameter(OGCConstants.VERSION, sp.getWmsVersion());
                String url = or.getUrl();
                wmsRequest.setProviderRequestURI(url);
                wmsRequest.setBytesSent((long) url.getBytes().length);

                ByteArrayOutputStream baos = wms.getCapabilities(url);

                String xml = baos.toString(KBConfiguration.CHARSET);

                wmsRequest.setResponseStatus(new Integer(200));
                wmsRequest.setRequestResponseTime(System.currentTimeMillis() - startprocestime);
                wmsRequest.setBytesReceived((long) xml.getBytes().length);
                wmsRequest.setMessageReceived(xml);
            } catch (Exception e) {
                wmsRequest.setExceptionMessage("Failed to send bytes to client: " + e.getMessage());
                wmsRequest.setExceptionClass(e.getClass());
                throw e;
            } finally {
                rr.addServiceProviderRequest(wmsRequest);
            }
        }
    }
}
