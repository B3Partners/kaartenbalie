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
import java.io.InputStream;
import java.io.OutputStream;

import java.util.List;
import java.util.Iterator;
import java.util.Set;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.monitoring.DataMonitoring;
import nl.b3p.kaartenbalie.core.server.monitoring.ServiceProviderRequest;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.wms.capabilities.Roles;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jytte
 */
public class WFSDescribeFeatureTypeRequestHandler extends WFSRequestHandler {

    private static final Log log = LogFactory.getLog(WFSDescribeFeatureTypeRequestHandler.class);

    /** Creates a new instance of WFSRequestHandler */
    public WFSDescribeFeatureTypeRequestHandler() {
    }

    public void getRequest(DataWrapper data, User user) throws IOException, Exception {

        OGCRequest ogcrequest = (OGCRequest) data.getOgcrequest().clone();
        String layers = null;
        String[] layerNames = null;
        Integer[] orgIds = user.getOrganizationIds();

        String request = ogcrequest.getParameter(OGCConstants.REQUEST);
        layers = ogcrequest.getParameter(OGCConstants.WFS_PARAM_TYPENAME);
        String[] allLayers = layers.split(",");

        if (allLayers.length > 1) {
            throw new Exception(request + " request with more then one maplayer is not supported yet!");
        }
        layerNames = new String[allLayers.length];
        String[] prefixes=new String[allLayers.length];
        for (int i = 0; i < allLayers.length; i++) {
            String[] temp = allLayers[i].split("}");
            if (temp.length > 1) {
                layerNames[i] = temp[1];
                int index1=allLayers[i].indexOf("{");
                int index2=allLayers[i].indexOf("}");
                prefixes[i]=ogcrequest.getPrefix(allLayers[i].substring(index1+1,index2));
            } else {
                String temp2[] = temp[0].split(":");
                if (temp2.length > 1) {
                    layerNames[i] = temp2[1];
                } else {
                    layerNames[i] = allLayers[i];
                }
            }
        }
        String url = null;
        List spInfo = null;

        Set userRoles = user.getRoles();
        boolean isAdmin = false;
        boolean isOrgAdmin = false;
        Iterator rolIt = userRoles.iterator();
        while (rolIt.hasNext()) {
            Roles role = (Roles) rolIt.next();
            if (role.getRole().equalsIgnoreCase(Roles.ADMIN)) {
                /* de gebruiker is een beheerder */
                isAdmin = true;
            }
            if (role.getRole().equalsIgnoreCase(Roles.ORG_ADMIN)) {
                /* de gebruiker is een organisatiebeheerder */
                isOrgAdmin = true;
            }
        }

        if (isAdmin && !isOrgAdmin) {
            spInfo = getLayerSummaries(layerNames);
        } else {
            spInfo = getSeviceProviderURLS(layerNames, orgIds, false, data);
        }

        if (spInfo == null || spInfo.isEmpty()) {
            throw new UnsupportedOperationException("No Serviceprovider available! User might not have rights to any Serviceprovider!");
        }
        String layerParam="";
        for (int i=0; i< layerNames.length; i++){
            if (layerParam.length()!=0){
                layerParam+=",";
            }
            if (prefixes[i]!=null){
                layerParam+=prefixes[i]+":";
            }
            int index1 = layerNames[i].indexOf("_");
            if (index1!=-1){
                layerParam+=layerNames[i].substring(index1+1);
            }else{
                layerParam+=layerNames[i];
            }
        }
        /*dit stukje gaat fout als er meerder serviceproviders zijn!
         * Er moet een layerParam per service provider worden gemaakt
         * en een ogcRequest per sp.
         */
        Iterator iter = spInfo.iterator();
        SpLayerSummary sp = null;
        while (iter.hasNext()) {
            sp = (SpLayerSummary) iter.next();
            url = sp.getSpUrl();
            ogcrequest.addOrReplaceParameter(OGCConstants.WFS_PARAM_TYPENAME, layerParam);
        }

        if (url == null || url.length()==0) {
            throw new UnsupportedOperationException("No Serviceprovider for this service available!");
        }
        HttpMethod method = null;
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout((int) maxResponseTime);
        OutputStream os = data.getOutputStream();
        String body = data.getOgcrequest().getXMLBody();
        // TODO body cleanen

        DataMonitoring rr = data.getRequestReporting();
        long startprocestime = System.currentTimeMillis();
        
        ServiceProviderRequest wfsRequest = this.createServiceProviderRequest(
				data, url, sp.getServiceproviderId(), new Long(body.getBytes().length));
        
        String host = url;
        ogcrequest.setHttpHost(host);
        //probeer eerst met Http getMethod op te halen.
        String reqUrl=ogcrequest.getUrl();
        method= new GetMethod(reqUrl);
        int status = client.executeMethod(method);
        if (status!=HttpStatus.SC_OK){
            method = new PostMethod(host);
            //work around voor ESRI post request. Content type mag geen text/xml zijn.
            //((PostMethod)method).setRequestEntity(new StringRequestEntity(body, "text/xml", "UTF-8"));
            ((PostMethod)method).setRequestEntity(new StringRequestEntity(body, null,null));
            status = client.executeMethod(method);
        }
        try {
	        if (status == HttpStatus.SC_OK) {
	            wfsRequest.setResponseStatus(new Integer(200));
	            wfsRequest.setRequestResponseTime(System.currentTimeMillis() - startprocestime);
	            
	            data.setContentType("text/xml");
	            InputStream is = method.getResponseBodyAsStream();
	
	            /*
	             * Nothing has to be done with DescribeFeatureType so it will be sent to the client at once.
	             */
	            int len = 1;
	            int byteCount = 0;
	            byte[] buffer = new byte[2024];
	            StringBuffer message = new StringBuffer();
	            while ((len = is.read(buffer, 0, buffer.length)) > 0) {
	                os.write(buffer, 0, len);
	                if (KBConfiguration.SAVE_MESSAGES) {
	                	message.append(new String(buffer, 0, len));
	                }
	                byteCount += len;
	            }
	            wfsRequest.setBytesReceived(new Long(byteCount));
	            if (KBConfiguration.SAVE_MESSAGES) {
	            	wfsRequest.setMessageSent(body);
		            wfsRequest.setMessageReceived(message.toString());
	            }
	        } else {
	        	wfsRequest.setResponseStatus(status);
	            wfsRequest.setExceptionMessage("Failed to connect with " + url + " Using body: " + body);
	            wfsRequest.setExceptionClass(UnsupportedOperationException.class);
	            
	            log.error("Failed to connect with " + url + " Using body: " + body);
	            throw new UnsupportedOperationException("Failed to connect with " + url + " Using body: " + body);
	        }
        } catch (Exception e) {
            wfsRequest.setExceptionMessage("Failed to send bytes to client: " + e.getMessage());
            wfsRequest.setExceptionClass(e.getClass());
        	
        	throw e;
        } finally {
            rr.addServiceProviderRequest(wfsRequest);
        }
    }
}
