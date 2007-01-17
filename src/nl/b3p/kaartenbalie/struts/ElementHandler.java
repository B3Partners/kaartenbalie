/**
 * @(#)ElementHandler.java
 * @author N. de Goeij
 * @version 1.00 2006/11/08
 *
 * Purpose: a class defining the various methods which can be used when parsing an XML document.
 * This class itself does nothing; the real processing should be defined in a subclass.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.struts;

import org.xml.sax.*;

public class ElementHandler {
    
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
    public void startElement (String uri, String localName, String qName, Attributes atts) throws SAXException {}
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
    public void endElement (String uri, String localName, String qName) throws SAXException {}
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
    public void characters (char[] ch, int start, int length) throws SAXException {}
    // </editor-fold>
}