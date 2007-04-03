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
        dbf.setValidating(true);
        
        /*
        if (useSchema)
            dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
        */
        
        DocumentBuilder db = dbf.newDocumentBuilder();
        db.setErrorHandler(this);
        doc = db.parse(uri);
        doc = null;
    }
    
    /** Warning. */
    public void warning(SAXParseException ex) throws SAXParseException {
        throw ex;
        /*
        documentHadWarnings = true;
        documentIsValid = false;
        System.err.println(+ getLocationString(ex)+": "+ ex.getMessage());
        */
    }
    
    /** Error. */
    public void error(SAXParseException ex) throws SAXParseException {
        throw ex;
        /*
        documentIsValid = false;
        System.err.println("[Error] "+ getLocationString(ex)+": "+ ex.getMessage());
        */
    }
    
    /** Fatal error. */
    public void fatalError(SAXParseException ex) throws SAXException {
        throw ex;
        /*
        documentIsValid = false;
        System.err.println("[Fatal Error] "+ getLocationString(ex)+": "+ ex.getMessage());
        */
    }
    
    /** Returns a string of the location. 
    private String getLocationString(SAXParseException ex) {
        StringBuffer str = new StringBuffer();
        String systemId = ex.getSystemId();
        
        if (systemId != null) {
            int index = systemId.lastIndexOf('/');
            if (index != -1)
                systemId = systemId.substring(index + 1);
            str.append(systemId);
        }
        
        str.append(':');
        str.append(ex.getLineNumber());
        str.append(':');
        str.append(ex.getColumnNumber());
        
        return str.toString();
    } // getLocationString(SAXParseException):String
    */
    /** Main program entry point. 
    public static void main(String argv[]) {
        if (argv.length == 0 || (argv.length == 1 && argv[0].equals("-help"))) {
            System.out.println("\nUsage:  java DomValidator uri dtd | schema");
            System.out.println("   where uri is the URI of the XML " + "document you want to ");
            System.out.println("   print, and dtd or schema is the " + "validation language.");
            System.out.println("\n   Sample:  java DomValidator sonnet.xml");
            System.out.println("\nParses an XML document, then writes " + "any namespace information to the console.");
            System.exit(1);
        }
        
        boolean useSchema = true;
        if (argv.length == 2 && argv[1].equalsIgnoreCase("dtd"))
            useSchema = false;
        
        DOMValidator dv = new DOMValidator();
        dv.parseAndValidate(argv[0], useSchema);
    }*/
}
