/*
 * B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
 * for authentication/authorization, pricing and usage reporting.
 *
 * Copyright 2006, 2007, 2008 B3Partners BV
 * 
 * This file is part of B3P Kaartenbalie.
 * 
 * B3P Kaartenbalie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * B3P Kaartenbalie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.b3p.kaartenbalie.service.requesthandler;

import java.io.IOException;
import java.util.List;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.ogc.utils.LayerSummary;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.ogc.utils.OGCResponse;
import nl.b3p.ogc.utils.SpLayerSummary;
import nl.b3p.ogc.utils.WFSGetCapabilitiesResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author CHris
 */
public class WFSGetCapabilitiesRequestHandler extends WFSRequestHandler {

    private static final Log log = LogFactory.getLog(WFSGetCapabilitiesRequestHandler.class);

    /** Creates a new instance of WFSRequestHandler */
    public WFSGetCapabilitiesRequestHandler() {
    }
    
    public String prepareRequest4Sp(OGCRequest ogcrequest, SpLayerSummary sp) {
        return null;
    }
    
    public List<LayerSummary> prepareRequestLayers(OGCRequest ogcrequest) throws Exception {
        return null;
    }

    public OGCResponse getNewOGCResponse() {
        return new WFSGetCapabilitiesResponse();
    }
    
    public void getRequest(DataWrapper data, User user) throws IOException, Exception {
        writeResponse(data, user);
    }
}
