/**
 * @(#)Switcher.java
 * @author N. de Goeij
 * @version 1.00 2006/11/08
 *
 * Purpose: a DocumentHandler that directs events to an appropriate element handler based on the element type.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.struts;

import org.xml.sax.*;
import java.util.*;

public class Switcher implements ContentHandler {
    private Hashtable <String, ElementHandler> rules = new Hashtable <String, ElementHandler>();
    private Stack <ElementHandler> stack = new Stack <ElementHandler>();
    
    /** Method which adds a new handler to the hash table.
     *
     * @param name String representing the name of the TAG to handle
     * @param handler String representing the name of the handler to handle this TAG
     */
    // <editor-fold defaultstate="collapsed" desc="setElementHandler(String name, ElementHandler handler) method.">
    public void setElementHandler(String name, ElementHandler handler) {
        rules.put(name, handler);
    }
    // </editor-fold>

    /** Method defining the start of an element.
     *
     * @param uri String representing the uri
     * @param localName String representing the local name
     * @param qName String representing the qName
     * @param atts Attributes of a specific element
     *
     * @throws SAXException
     */
    // <editor-fold defaultstate="collapsed" desc="startElement (String uri, String localName, String qName, Attributes atts) method.">
    public void startElement (String uri, String localName, String qName, Attributes atts) throws SAXException {
        ElementHandler handler = (ElementHandler)rules.get(localName);
        stack.push(handler);
        
        if (handler!=null) {
            handler.startElement(uri, localName, qName, atts);
        }
    }
    // </editor-fold>

    /** Method defining the end of an element.
     *
     * @param uri String representing the uri
     * @param localName String representing the local name
     * @param qName String representing the qName
     *
     * @throws SAXException
     */
    // <editor-fold defaultstate="collapsed" desc="endElement (String uri, String localName, String qName) method.">
    public void endElement (String uri, String localName, String qName) throws SAXException {
        ElementHandler handler = (ElementHandler)stack.pop();
        if (handler!=null) {
            handler.endElement(uri, localName, qName);
        }
    }
    // </editor-fold>

    /** Method defining the character data.
     *
     * @param ch char array
     * @param start integer where to start
     * @param length integer with the amount of characters to read
     *
     * @throws SAXException
     */
    // <editor-fold defaultstate="collapsed" desc="characters (char[] ch, int start, int length) method.">
    public void characters (char[] ch, int start, int length) throws SAXException {
        ElementHandler handler = (ElementHandler)stack.peek();
        if (handler!=null) {
            handler.characters(ch, start, length);
            String s = new String(ch, start, length);
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Methods overriding the methods declared in the interface.">
    public void skippedEntity (String name) throws SAXException {}
    
    public void setDocumentLocator (Locator locator) {}

    public void startDocument () throws SAXException{}

    public void endDocument() throws SAXException {}

    public void startPrefixMapping (String prefix, String uri) throws SAXException {}

    public void endPrefixMapping (String prefix) throws SAXException {}

    public void ignorableWhitespace (char ch[], int start, int length) throws SAXException {}

    public void processingInstruction (String target, String data) throws SAXException {}
    // </editor-fold>
}