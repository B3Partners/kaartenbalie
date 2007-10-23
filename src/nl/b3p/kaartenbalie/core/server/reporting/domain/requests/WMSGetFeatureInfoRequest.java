/*
 * WMSGetCapabilitiesRequest.java
 *
 * Created on October 12, 2007, 1:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.domain.requests;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Chris Kramer
 */
public class WMSGetFeatureInfoRequest extends WMSRequest {
    
    /** Creates a new instance of WMSGetCapabilitiesRequest */
    public WMSGetFeatureInfoRequest() {
    }

    public Element toElement(Document doc, Element rootElement) {
        return null;
    }
    
}
