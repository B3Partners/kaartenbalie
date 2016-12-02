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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import nl.b3p.ogc.utils.WFSGetFeatureResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jytte
 */
public class WFSGetFeatureRequestHandler extends WFSRequestHandler {

    private static final Log log = LogFactory.getLog(WFSGetFeatureRequestHandler.class);

    /** Creates a new instance of WFSRequestHandler */
    public WFSGetFeatureRequestHandler() {
    }
    
    /**
     * This handler allows de input stream to be written directly to the
     * output stream in some cases, other conditions apply.
     * 
     * @return allow direct write
     */
    @Override
    public boolean mayDirectWrite() {
        return true;
    }
    
    public byte[] prepareDirectWrite(InputStream isx) throws IOException {
        byte[] buffer = new byte[2024];
        int len = isx.read(buffer, 0, buffer.length);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(buffer, 0, len);
        // encoding is onbekend, gebruik default!!
        String header = new String(bos.toByteArray());
        // dit is nu voor getfeature, moet nog naar juiste handler
        int posSL = header.toLowerCase().indexOf("schemalocation");
        String newHeader = header;
        if (posSL > 0) {
            int posQO = header.indexOf("\"", posSL);
            if (posQO > 0) {
                int posQC = header.indexOf("\"", posQO + 1);
                if (posQC > 0) {
                    newHeader = header.substring(0, posQO + 1) + header.substring(posQC);
                }
            }
        }

        return newHeader.getBytes();
    }
   
    private String repairIntersects(String version, String filter) {
        if (filter !=null && version != null && version.equals(OGCConstants.WFS_VERSION_100)) {
            // check of geotools onterecht intersects (met s) heeft gebruikt bij wfs 1.0.0
            // later oplossen in geotools
            // nu wat testen en hier fixen
            if (filter.contains("<Intersects>") && filter.contains("</Intersects>")) {
                return filter.replace("<Intersects>", "<Intersect>").replace("</Intersects>", "</Intersect>");
            }
        }
        return filter;
    }

    public String prepareRequest4Sp(OGCRequest ogcrequest, SpLayerSummary sp) {
        
        if (ogcrequest.getHttpMethod().equalsIgnoreCase("POST")) {
            LayerSummary l = null;
            try {
                l = OGCCommunication.splitLayerWithoutNsFix(sp.getLayerName(), false, sp.getSpAbbr(), null);
            } catch (Exception ex) {
                // ignore
            }
        
            if (l != null) {
                String filter = ogcrequest.getGetFeatureFilter(OGCCommunication.buildLayerNameWithoutNs(l));
                filter = repairIntersects(ogcrequest.getFinalVersion(), filter);
                if (filter != null) {
                    ogcrequest.addOrReplaceParameter(OGCConstants.WFS_PARAM_FILTER, filter);
                }
                String propertyNames = ogcrequest.getGetFeaturePropertyNameList(OGCCommunication.buildFullLayerName(l));
                if (propertyNames != null) {
                    ogcrequest.addOrReplaceParameter(OGCConstants.WFS_PARAM_PROPERTYNAME, propertyNames);
                }
            }

        } else { // get
            String filter = ogcrequest.getParameter(OGCConstants.WFS_PARAM_FILTER);
            filter = repairIntersects(ogcrequest.getFinalVersion(), filter);
            ogcrequest.addOrReplaceParameter(OGCConstants.WFS_PARAM_FILTER, filter);
        }

        String layerParam = "";
        List<LayerSummary> lsl = sp.getLayers();
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
            allLayers = new ArrayList();
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
        return new WFSGetFeatureResponse();
    }

    public void getRequest(DataWrapper data, User user) throws IOException, Exception {
        writeResponse(data, user);
    }

}
