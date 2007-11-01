/**
 * @(#)GetCapabilitiesRequestHandler.java
 * @author N. de Goeij
 * @version 1.00 2006/12/13
 *
 * Purpose: the function of this class is to create a list of url's which direct to the right servers that
 * have the desired layers for the WMS GetCapabilities request.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.service.requesthandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import nl.b3p.kaartenbalie.service.servlet.CallWMSServlet;
import nl.b3p.wms.capabilities.ServiceProvider;
import nl.b3p.kaartenbalie.core.server.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

public class GetCapabilitiesRequestHandler extends WMSRequestHandler {
    
    private static final Log log = LogFactory.getLog(GetCapabilitiesRequestHandler.class);
    
    // <editor-fold defaultstate="" desc="default GetCapabilitiesRequestHandler() constructor.">
    public GetCapabilitiesRequestHandler() {}
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
        
        ByteArrayOutputStream output    = null;
        this.user                       = user;
        this.url                        = user.getPersonalURL();
        ServiceProvider s               = getServiceProvider();
        s.overwriteURL(url);
        
        /*
         * Create a DocumentBuilderFactory from which a new document can be created
         */
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        DOMImplementation di = db.getDOMImplementation();

        // <!DOCTYPE WMT_MS_Capabilities SYSTEM "http://schemas.opengeospatial.net/wms/1.1.1/WMS_MS_Capabilities.dtd"
        // [
        // <!ELEMENT VendorSpecificCapabilities EMPTY>
        // ]>  <!-- end of DOCTYPE declaration -->
        
        DocumentType dt     = di.createDocumentType("WMT_MS_Capabilities", null, CallWMSServlet.CAPABILITIES_DTD);
        Document dom        = di.createDocument(null, "WMT_MS_Capabilities", dt);
        Element rootElement = dom.getDocumentElement();
        rootElement         = s.toElement(dom, rootElement);
        rootElement.setAttribute("version", "1.1.1");
        
        /*
         * Create a new output format to which this document should be translated and
         * serialize the tree to an XML document type
         */
        OutputFormat format = new OutputFormat(dom, CHARSET, true);
        format.setIndenting(true);
        output = new ByteArrayOutputStream();
        XMLSerializer serializer = new XMLSerializer(output, format);
        serializer.serialize(dom);
        
        DOMValidator dv = new DOMValidator();
        dv.parseAndValidate(new ByteArrayInputStream(output.toString().getBytes(CHARSET)));
        
        byte[] data = output.toByteArray();
        dw.write(output);
    }
    // </editor-fold>
}