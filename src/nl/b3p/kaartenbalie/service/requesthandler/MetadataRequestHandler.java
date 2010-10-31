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
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.wms.capabilities.Layer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MetadataRequestHandler extends WMSRequestHandler {

    private static final Log log = LogFactory.getLog(MetadataRequestHandler.class);

    public MetadataRequestHandler() {
    }

    /**
     * Get metadata belonging to layer
     *
     * @param dw DataWrapper which contains all information that has to be sent to the client
     * @param user User the user which invoked the request
     *
     * @return byte[]
     *
     * @throws Exception
     * @throws IOException
     */
    public void getRequest(DataWrapper dw, User user) throws IOException, Exception {

        OGCRequest ogcrequest = dw.getOgcrequest();
        String mdLayer = ogcrequest.getParameter(OGCConstants.METADATA_LAYER);
        if (mdLayer == null || mdLayer.length() == 0) {
            throw new Exception("Layer name is required, but not provided!");
        }
        dw.setHeader("Content-Disposition", "inline; filename=\"" + mdLayer + ".xml\";");
        dw.setContentType(OGCConstants.METADATA_XML);

        this.user = user;
        // TODO checken op rechten
        Layer layer = getLayerByUniqueName(mdLayer);
        if (layer == null) {
            throw new Exception("Layer not found with name: " + mdLayer);
        }
        if (layer.getMetadata() == null) {
            throw new Exception("Metadata for layer not found with name: " + mdLayer);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String md = layer.getMetadata();
        baos.write(md.getBytes());
        dw.write(baos);
    }

}
