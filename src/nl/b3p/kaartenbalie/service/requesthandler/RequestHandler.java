/**
 * @(#)RequestHandler.java
 * @author N. de Goeij
 * @version 1.00 2006/12/13
 *
 * Purpose: Interface of all the RequestHandlers. Classes implementing this interface are:
 * - WMSRequestHandler
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.service.requesthandler;

import java.io.IOException;
import nl.b3p.kaartenbalie.core.server.User;

public interface RequestHandler {
    public void getRequest(DataWrapper dw, User user) throws IOException, Exception;
    
}