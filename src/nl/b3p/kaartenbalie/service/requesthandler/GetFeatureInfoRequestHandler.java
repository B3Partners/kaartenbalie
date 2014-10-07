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

import nl.b3p.ogc.utils.SpLayerSummary;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import nl.b3p.commons.services.B3PCredentials;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.monitoring.ServiceProviderRequest;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.LayerSummary;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetFeatureInfoRequestHandler extends WMSRequestHandler {

    private static final Log log = LogFactory.getLog(GetFeatureInfoRequestHandler.class);
    // <editor-fold defaultstate="" desc="default GetFeatureInfoRequestHandler() constructor.">
    public GetFeatureInfoRequestHandler() {
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
    public void getRequest(DataWrapper dw, User user) throws IOException, Exception {
        //Waarom de Content-Disposition header setten? Je weet niet of het .xml is.
        //dw.setHeader("Content-Disposition", "inline; filename=\"GetFeatureInfo.xml\";");
        OGCRequest ogcrequest = dw.getOgcrequest();
        String value = "";
        if (ogcrequest.containsParameter(OGCConstants.WMS_PARAM_INFO_FORMAT)) {
            String fileName=null;
            value = ogcrequest.getParameter(OGCConstants.FEATURE_INFO_FORMAT);
            if (value != null && value.length() > 0) {
                dw.setContentType(value);
                if (value.equalsIgnoreCase(OGCConstants.WMS_PARAM_WMS_HTML)){
                    fileName="GetFeature.htm";
                }else if (value.equalsIgnoreCase(OGCConstants.WMS_PARAM_WMS_GML) ||
                        value.equalsIgnoreCase(OGCConstants.WMS_PARAM_GML)){
                    fileName="GetFeature.gml";
                }else if (value.equalsIgnoreCase(OGCConstants.WMS_PARAM_WMS_XML) ||
                        value.equalsIgnoreCase(OGCConstants.WMS_PARAM_XML)){
                    fileName="GetFeature.xml";
                }
            } else {
                dw.setContentType(OGCConstants.WMS_PARAM_WMS_XML);
                fileName="GetFeature.xml";
            }
            if (fileName!=null){
                dw.setHeader("Content-Disposition", "inline; filename=\""+fileName+"\";");
            }
        }

        Long timeFromStart = new Long(dw.getRequestReporting().getMSSinceStart());

        this.user = user;
        this.url = user.getPersonalURL(dw.getRequest(), dw.getOgcrequest().getServiceProviderName());
        Integer[] orgIds = this.user.getOrganizationIds();
        OGCRequest ogc = dw.getOgcrequest();

        String spInUrl = ogc.getServiceProviderName();
        String[] la = ogc.getParameter(OGCConstants.WMS_PARAM_QUERY_LAYERS).split(",");
        List<LayerSummary> lsl = LayerSummary.createLayerSummaryList(Arrays.asList(la), spInUrl, (spInUrl==null)); 
        
        List spUrls = getServiceProviderURLS(lsl, orgIds, true, dw, false);
        if (spUrls == null || spUrls.isEmpty()) {
            log.error("No urls qualify for request.");
            throw new Exception(KBConfiguration.FEATUREINFO_QUERYABLE_EXCEPTION);
        }

        ArrayList urlWrapper = new ArrayList();
        Iterator it = spUrls.iterator();
        while (it.hasNext()) {

            SpLayerSummary spInfo = (SpLayerSummary) it.next();

            ServiceProviderRequest firWrapper = new ServiceProviderRequest();
            firWrapper.setMsSinceRequestStart(timeFromStart);

            Integer serviceProviderId = spInfo.getServiceproviderId();
            if (serviceProviderId != null && serviceProviderId.intValue() == -1) {
                //Say hello to B3P Layering!!
            } else {
                firWrapper.setServiceProviderId(serviceProviderId);

                String abbr = spInfo.getSpAbbr();
                firWrapper.setServiceProviderAbbreviation(abbr);
                
                B3PCredentials credentials  = new B3PCredentials();
                credentials.setUserName(spInfo.getUsername());
                credentials.setPassword(spInfo.getPassword());
                firWrapper.setCredentials(credentials);
                
             
                String layersList = spInfo.getLayersAsString();

                StringBuilder url = new StringBuilder();
                url.append(spInfo.getSpUrl());
                if (url.indexOf("?")!=url.length()-1 && url.indexOf("&")!= url.length()-1){
                    if (url.indexOf("?")>=0){
                        url.append("&");
                    }else{
                        url.append("?");
                    }
                }
                String[] params = dw.getOgcrequest().getParametersArray();
                for (int i = 0; i < params.length; i++) {
                    String[] keyValuePair = params[i].split("=");
                    if (keyValuePair[0].equalsIgnoreCase(OGCConstants.WMS_PARAM_LAYERS)) {
                        url.append(OGCConstants.WMS_PARAM_LAYERS);
                        url.append("=");
                        url.append(layersList);
                        url.append("&");
                    } else if (keyValuePair[0].equalsIgnoreCase(OGCConstants.WMS_PARAM_QUERY_LAYERS)) {
                        url.append(OGCConstants.WMS_PARAM_QUERY_LAYERS);
                        url.append("=");
                        url.append(layersList);
                        url.append("&");
                    } else {
                        url.append(params[i]);
                        url.append("&");
                    }
                }
                firWrapper.setProviderRequestURI(url.toString());
                urlWrapper.add(firWrapper);
            }
        }

        getOnlineData(dw, urlWrapper, false, OGCConstants.WMS_REQUEST_GetFeatureInfo);
    }
}