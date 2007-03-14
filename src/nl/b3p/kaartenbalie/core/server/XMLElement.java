/**
 * @(#)XMLElement.java
 * @author N. de Goeij
 * @version 1.00 2007/01/10
 *
 * Purpose: Interface of all the RequestHandlers. Classes implementing this interface are:
 * - WMSRequestHandler
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.core.server;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface XMLElement {
    
    /** Method that will create piece of the XML tree to create a proper XML docuement.
     *
     * @param doc Document object which is being used to create new Elements
     * @param rootElement The element where this object belongs to.
     *
     * @return an object of type Element
     */
    // <editor-fold defaultstate="" desc="public Element toElement(Document doc, Element rootElement);">
    public Element toElement(Document doc, Element rootElement);
    // </editor-fold>
}
