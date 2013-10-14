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
import java.util.Iterator;
import java.util.List;
import nl.b3p.gis.B3PCredentials;

import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.monitoring.ServiceProviderRequest;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** 
 * A RequestHandler for the DescribeLayerRequest.
 *  
 * The DescribeLayer request is an optional operation that applies to Styled Layer Descriptor WMS.
 * It is a mechanism by which a client can obtain feature/coverage-type information for a named layer.
 * 
 *
 */
public class DescribeLayerRequestHandler extends WMSRequestHandler {

	private static final Log log = LogFactory.getLog(DescribeLayerRequestHandler.class);
	
    public DescribeLayerRequestHandler() {
    }
    
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

    public void getRequest(DataWrapper dw, User user) throws IOException, Exception {
        
    	dw.setHeader("Content-Disposition", "inline; filename=\"DescribeLayer.xml\";");
    	
    	this.user = user;
        this.url = user.getPersonalURL(dw.getRequest(), dw.getOgcrequest().getServiceProviderName());
        Integer[] orgIds = user.getOrganizationIds(); //for authorization
        OGCRequest ogcRequest = dw.getOgcrequest();
        
        //should DescribeLayer do anything with format param? at present it doesn't
        dw.setContentType(OGCConstants.WMS_PARAM_WMS_XML);
                
        Long timeFromStart = new Long(dw.getRequestReporting().getMSSinceStart());
        
        // --
        // -- get params from ogcrequest
        // --
        String requestParam = ogcRequest.getParameter(OGCConstants.REQUEST);
        String layersParam = ogcRequest.getParameter(OGCConstants.WMS_PARAM_LAYERS);
        
        // --
        // -- check if there are service provider urls to collect data from
        // -- getSeviceProviderURLS returns list with SpLayerSummary objects
        // --
        List spInfo = getServiceProviderURLS(layersParam.split(","), orgIds, false, dw);
        if (spInfo == null || spInfo.isEmpty()) {
        	//Error message from KBConfiguration in b3p-commons-gis?
        	log.error(requestParam + ": no urls qualify for request.");
            throw new Exception("No Serviceprovider available! User might not have rights to any Serviceprovider!");
        }
        
        // --
        // -- prepare to collect data
        // --
        ArrayList urlWrapper = new ArrayList();
        Iterator it = spInfo.iterator();
        while (it.hasNext()) {

            SpLayerSummary spLayerSummary = (SpLayerSummary) it.next();
            
            ServiceProviderRequest dlrWrapper = new ServiceProviderRequest();
            dlrWrapper.setMsSinceRequestStart(timeFromStart);
            
            dlrWrapper.setWmsVersion(ogcRequest.getParameter(OGCConstants.WMS_VERSION));            
            
            Integer serviceProviderId = spLayerSummary.getServiceproviderId();
            if(serviceProviderId != null && serviceProviderId.intValue() == -1){
            	//B3P layering necessary for DescribeLayer?
            	// when is Id < -1 ?
            } else {
                B3PCredentials credentials  = new B3PCredentials();
                credentials.setUserName(spLayerSummary.getUsername());
                credentials.setPassword(spLayerSummary.getPassword());
            	dlrWrapper.setServiceProviderId(serviceProviderId);
            	dlrWrapper.setServiceProviderAbbreviation(spLayerSummary.getSpAbbr());
                dlrWrapper.setCredentials(credentials);
                
            	String layersList = spLayerSummary.getLayersAsString(); //
            	
                StringBuffer url = new StringBuffer();
                url.append(spLayerSummary.getSpUrl());
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
                    } else {
                        url.append(params[i]);
                        url.append("&");
                    }
                }
                dlrWrapper.setProviderRequestURI(url.toString());
                urlWrapper.add(dlrWrapper);  
                
            }
        }
        
        getOnlineData(dw, urlWrapper, false, OGCConstants.WMS_REQUEST_DescribeLayer);        
    }

}