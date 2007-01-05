package nl.b3p.kaartenbalie.service.requesthandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import nl.b3p.kaartenbalie.core.server.Layer;
import nl.b3p.kaartenbalie.core.server.ServiceProvider;
import nl.b3p.kaartenbalie.core.server.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @(#)GetCapabilitiesRequestHandler.java
 *
 * @author N. de Goeij
 * @version 1.00 2006/12/13
 *
 * The function of this class is to create a well formed XML document of the capabilities that the server has
 * for WMS requests
 */

public class GetCapabilitiesRequestHandler extends WMSRequestHandler {
    
    private static final Log log = LogFactory.getLog(GetCapabilitiesRequestHandler.class);
    
    public GetCapabilitiesRequestHandler() {}
    
    public byte[] getRequest(Map parameters) throws IOException, Exception {
        user = (User) parameters.get(KB_USER);
        url = (String) parameters.get(KB_PERSONAL_URL);
        
        ServiceProvider s = null;
        List sps = getServiceProviders(true);
        Iterator it = sps.iterator();
        
        while (it.hasNext()) {
            s = (ServiceProvider)it.next();
            s.overwriteURL(url);
        }
        
        // TODO waarom iteratie en dan alleen laatste returnen ?????
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
        
        
        doc.appendChild(s.toElement(doc));
        
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        transformer.transform(new DOMSource(doc), new StreamResult(output));
        
        //TODO utf-8 of latin-1
        return getOnlineData(output.toString("iso-8859-1"));
    }
    
}