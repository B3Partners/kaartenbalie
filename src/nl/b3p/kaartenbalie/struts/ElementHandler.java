package nl.b3p.kaartenbalie.struts;

import org.xml.sax.*;

/*
 * ElementHandler is a class that process the start and end tags and character data
 * for one element type. This class itself does nothing; the real processing should
 * be defined in a subclass
 */

public class ElementHandler {
    /**
     * Start of an element
     */
    public void startElement (String uri, String localName, String qName, Attributes atts) throws SAXException {}

    /**
     * End of an element
     */
    public void endElement (String uri, String localName, String qName) throws SAXException {}

    /**
     * Character data
     */
    public void characters (char[] ch, int start, int length) throws SAXException {}
}