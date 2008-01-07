/*
 * @(#)DOMValidator.java
 * @author N. de Goeij
 * @version 1.00, 3 april 2007
 *
 * @copyright 2007 B3Partners. All rights reserved.
 * B3Partners PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package nl.b3p.kaartenbalie.service.requesthandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class DOMValidator extends DefaultHandler {
    private boolean documentIsValid = true;
    private boolean documentHadWarnings = false;
    
    public void parseAndValidate(ByteArrayInputStream uri) throws SAXParseException, ParserConfigurationException, SAXException, IOException {
        Document doc = null;
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setValidating(false);
        
        DocumentBuilder db = dbf.newDocumentBuilder();
        db.setErrorHandler(this);
        doc = db.parse(uri);
        doc = null;
    }
    
    /** Warning. */
    public void warning(SAXParseException ex) throws SAXParseException {
        throw ex;
    }
    
    /** Error. */
    public void error(SAXParseException ex) throws SAXParseException {
        throw ex;
    }
    
    /** Fatal error. */
    public void fatalError(SAXParseException ex) throws SAXException {
        throw ex;
    }
}
