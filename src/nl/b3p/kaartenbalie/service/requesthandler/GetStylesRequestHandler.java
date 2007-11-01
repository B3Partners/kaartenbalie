/**
 * @(#)GetStylesRequestHandler.java
 * @author N. de Goeij
 * @version 1.00 2006/12/13
 *
 * Purpose: *
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.service.requesthandler;

import java.io.IOException;
import nl.b3p.kaartenbalie.core.server.User;

public class GetStylesRequestHandler extends WMSRequestHandler {
    
    // <editor-fold defaultstate="" desc="default GetStylesRequestHandler() constructor.">
    public GetStylesRequestHandler() {}
    // </editor-fold>
    
    // TODO: onderstaande getRequest methode heeft nog geen implementatie. Deze moet nog uitgebreid worden.
    
    /** Processes the parameters and creates the specified urls from the given parameters.
     * Each url will be used to recieve the data from the ServiceProvider this url is refering to.
     * 
     * @param dw DataWrapper which contains all information that has to be sent to the client
     * @param user User the user which invoked the request
     *
     * @return byte[]
     *
     * @throws Exception
     * @throws IOException
     */
    // <editor-fold defaultstate="" desc="getRequest(DataWrapper dw, User user) method.">
    public void getRequest(DataWrapper dw, User user) throws IOException {
        return;
    }
    // </editor-fold>
    
}