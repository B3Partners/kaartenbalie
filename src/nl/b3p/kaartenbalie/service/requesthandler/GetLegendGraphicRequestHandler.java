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
import java.util.ArrayList;
import java.util.List;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.reporting.domain.requests.ServiceProviderRequest;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetLegendGraphicRequestHandler extends WMSRequestHandler {

    private static final Log log = LogFactory.getLog(GetLegendGraphicRequestHandler.class);
    // <editor-fold defaultstate="" desc="default GetLegendGraphicRequestHandler() constructor.">
    public GetLegendGraphicRequestHandler() {
    }
    // </editor-fold>
    /**
     * Processes the parameters and creates the specified urls from the given parameters.
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
    public void getRequest(DataWrapper dw, User user) throws IOException, Exception {
        this.user = user;
        this.url = user.getPersonalURL();
        Integer orgId = user.getOrganization().getId();
        OGCRequest ogc = dw.getOgcrequest();

        String value = "";
        if (ogc.containsParameter(OGCConstants.WMS_PARAM_FORMAT)) {
            value = ogc.getParameter(OGCConstants.WMS_PARAM_FORMAT);
            if (value != null && value.length() > 0) {
                dw.setContentType(value);
            } else {
                dw.setContentType(OGCConstants.WMS_PARAM_WMS_XML);
            }
        }

        String[] layers = ogc.getParameter(OGCConstants.WMS_PARAM_LAYER).split(",");
        if (layers.length != 1) {
            log.error("Only one layer for legend graphic.");
            throw new Exception(KBConfiguration.LEGENDGRAPHIC_EXCEPTION);
        }

        List spUrls = getSeviceProviderURLS(layers, orgId, false, dw);
        if (spUrls == null || spUrls.size() != 1) {
            log.error("Only one layer for legend graphic.");
            throw new Exception(KBConfiguration.LEGENDGRAPHIC_EXCEPTION);
        }

        SpLayerSummary spInfo = (SpLayerSummary) spUrls.get(0);
        if (spInfo == null) {
            log.error("No urls found!");
            throw new Exception(KBConfiguration.LEGENDGRAPHIC_EXCEPTION);
        }


        ArrayList urlWrapper = new ArrayList();
        ServiceProviderRequest lgrWrapper = new ServiceProviderRequest();
        Integer serviceProviderId = spInfo.getServiceproviderId();

        if (serviceProviderId != null && serviceProviderId.intValue() == -1) {
            //Say hello to B3P Layering!!
        } else {
            lgrWrapper.setServiceProviderId(serviceProviderId);
            StringBuffer url = new StringBuffer();
            url.append(spInfo.getSpUrl());
            String[] params = ogc.getParametersArray();
            for (int i = 0; i < params.length; i++) {
                String[] keyValuePair = params[i].split("=");
                if (keyValuePair[0].equalsIgnoreCase(OGCConstants.WMS_PARAM_LAYER)) {
                    url.append(OGCConstants.WMS_PARAM_LAYER);
                    url.append("=");
                    url.append(spInfo.getLayersAsString());
                    url.append("&");
                } else {
                    url.append(params[i]);
                    url.append("&");
                }
            }
            lgrWrapper.setProviderRequestURI(url.toString());
            urlWrapper.add(lgrWrapper);
            getOnlineData(dw, urlWrapper, false, OGCConstants.WMS_REQUEST_GetLegendGraphic);
        }

    }
    // </editor-fold>
}