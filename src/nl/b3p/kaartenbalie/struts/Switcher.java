package nl.b3p.kaartenbalie.struts;

import org.xml.sax.*;
import java.util.*;
/**
 * Switcher is a DocumentHandler that directs events to an appropriate element
 * handler based on the element type.
 */

public class Switcher implements ContentHandler {// extends HandlerBase {
    private Hashtable rules = new Hashtable();
    private Stack stack = new Stack();
//    private int level = 0;

    /**
     * Define processing for an element type.
     */
    public void setElementHandler(String name, ElementHandler handler) {
        rules.put(name, handler);
        //System.out.println(handler.getClass().getName());
    }

    /**
     * Start of an element. Decide what handler to use, and call it.
     */
    public void startElement (String uri, String localName, String qName, Attributes atts) throws SAXException {
        ElementHandler handler = (ElementHandler)rules.get(localName);
        stack.push(handler);
        
        if (handler!=null) {
//            String tab = "";
//            for (int i = 0; i <= level; i ++) {
//                tab += "\t";
//            }
            //System.out.println(tab + "StartElement. ElementHandler: " + handler.getClass().getSimpleName());
            handler.startElement(uri, localName, qName, atts);
        }
//        level++;
    }

    /**
     * End of an element.
     */
    public void endElement (String uri, String localName, String qName) throws SAXException {
        ElementHandler handler = (ElementHandler)stack.pop();
//        level--;
        if (handler!=null) {
//          String tab = "";
//            for (int i = 0; i <= level; i ++) {
//                tab += "\t";
//            }
            //System.out.println(tab + "EndElement. ElementHandler: " + handler.getClass().getSimpleName());
            handler.endElement(uri, localName, qName);
        }
    }

    /**
     * Character data.
     */
    public void characters (char[] ch, int start, int length) throws SAXException {
        ElementHandler handler = (ElementHandler)stack.peek();
        if (handler!=null) {
            handler.characters(ch, start, length);
            String s = new String(ch, start, length);
            System.out.println("caharacters : " + s);
        }
    }

    public void skippedEntity (String name) throws SAXException {
//        System.out.println("We have a skipped entity");
    }
    
    public void setDocumentLocator (Locator locator) {
//        System.out.println("We are in the setDocumentLocator");
    }

    public void startDocument () throws SAXException{
//        System.out.println("We are in the startDocument");
    }

    public void endDocument() throws SAXException {
//        System.out.println("We are in the endDocument");
    }

    public void startPrefixMapping (String prefix, String uri) throws SAXException {
//        System.out.println("We are in the startPrefixMapping");
//        System.out.println("start prefix : " + prefix);
//        System.out.println("uri prefix : " + uri);
    }

    public void endPrefixMapping (String prefix) throws SAXException {
//        System.out.println("We are in the endPrefixMapping");
//        System.out.println("end prefix : " + prefix);
    }

    public void ignorableWhitespace (char ch[], int start, int length) throws SAXException {
//        System.out.println("We are in the ignorableWhiteSpace");
//        String s = new String(ch, start, length);
//        System.out.println("ignorable white space : " + s);
    }

    public void processingInstruction (String target, String data) throws SAXException {
//        System.out.println("We are in the processingInstruction");
    }
}