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
import java.util.Arrays;

import java.util.List;
import java.util.Set;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.ogc.utils.LayerSummary;
import nl.b3p.ogc.utils.OGCCommunication;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.ogc.utils.OGCResponse;
import nl.b3p.ogc.utils.SpLayerSummary;
import nl.b3p.ogc.utils.WFSDescribeFeatureTypeResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Chris
 */
public class WFSDescribeFeatureTypeRequestHandler extends WFSRequestHandler {

    private static final Log log = LogFactory.getLog(WFSDescribeFeatureTypeRequestHandler.class);

    /** Creates a new instance of WFSRequestHandler */
    public WFSDescribeFeatureTypeRequestHandler() {
    }
    
    public String prepareRequest4Sp(OGCRequest ogcrequest, SpLayerSummary sp) {
        List<LayerSummary> lsl = sp.getLayers();
        String layerParam = "";
        for (LayerSummary ls : lsl) {
            if (!layerParam.isEmpty()) {
                layerParam += ",";
            }
            layerParam += OGCCommunication.buildLayerNameWithoutSp(ls);
        }
        ogcrequest.addOrReplaceParameter(OGCConstants.WFS_PARAM_TYPENAME, layerParam);
        
        return null;
    }
    
    public List<LayerSummary> prepareRequestLayers(OGCRequest ogcrequest) throws Exception {
        List<String> allLayers = null;
        if (ogcrequest.getHttpMethod().equals("POST")) {
            Set layers = ogcrequest.getGetFeatureFilterMap().keySet();
            allLayers.addAll(layers);
        } else {
            String typeName = ogcrequest.getParameter(OGCConstants.WFS_PARAM_TYPENAME);
            allLayers = Arrays.asList(typeName.split(","));
        }

        String spInUrl = ogcrequest.getServiceProviderName();
        
        List<LayerSummary> lsl = LayerSummary.createLayerSummaryList(allLayers,
                ogcrequest.getServiceProviderName(), (spInUrl==null));
        
        if (!checkNumberOfSps(lsl, 1)) {
            log.error("More then 1 service provider addressed. Not supported (yet)");
            throw new UnsupportedOperationException("More then 1 service provider addressed. Not supported (yet)");
        }
        return lsl;
    }
    
    public OGCResponse getNewOGCResponse() {
        return new WFSDescribeFeatureTypeResponse();
    }

   public void getRequest(DataWrapper data, User user) throws IOException, Exception {
        writeResponse(data, user);
    }

}
