/**
 * @(#)GetCapabilitiesRequestHandler.java
 * @author N. de Goeij
 * @version 1.00 2006/12/13
 *
 * Purpose: *
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.service.requesthandler;

import java.io.IOException;
import java.util.Map;

public class DescribeLayerRequestHandler extends WMSRequestHandler {
    
    // <editor-fold defaultstate="" desc="default DescribeLayerRequestHandler() constructor">
    public DescribeLayerRequestHandler() {
    }
    // </editor-fold>
    
    // TODO: onderstaande getRequest methode heeft nog geen implementatie. Deze moet nog uitgebreid worden.
    
    /** Processes the parameters and creates the specified urls from the given parameters.
     * Each url will be used to recieve the data from the ServiceProvider this url is refering to.
     * @param parameters Map parameters
     * @return byte[]
     *
     * @throws IOException
     */
    // <editor-fold defaultstate="" desc="getRequest(Map parameters) method.">
    public DataWrapper getRequest(DataWrapper dw, Map parameters) throws IOException {
        return null;
    }
    // </editor-fold>
}