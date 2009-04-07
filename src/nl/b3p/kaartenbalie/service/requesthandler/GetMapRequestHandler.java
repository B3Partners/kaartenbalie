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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.core.server.monitoring.ServiceProviderRequest;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.SrsBoundingBox;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetMapRequestHandler extends WMSRequestHandler {

    private static final Log log = LogFactory.getLog(GetMapRequestHandler.class);

    public GetMapRequestHandler() {
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

        log.debug("Getting entity manager ......");
        EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);

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

        Long timeFromStart = new Long(dw.getRequestReporting().getMSSinceStart());

        Integer width = null;
        try {
            width = new Integer(ogc.getParameter(OGCConstants.WMS_PARAM_WIDTH));
        } catch (NumberFormatException nfe) {
            width = new Integer(-1);
        }

        Integer height = null;
        try {
            height = new Integer(ogc.getParameter(OGCConstants.WMS_PARAM_HEIGHT));
        } catch (NumberFormatException nfe) {
            height = new Integer(-1);
        }

        String givenSRS = ogc.getParameter(OGCConstants.WMS_PARAM_SRS);

        List spUrls = getSeviceProviderURLS(ogc.getParameter(OGCConstants.WMS_PARAM_LAYERS).split(","), orgId, false, dw);
        if (spUrls == null || spUrls.isEmpty()) {
            log.error("No urls qualify for request.");
            throw new Exception(KBConfiguration.GETMAP_EXCEPTION);
        }
        spUrls = prepareAccounting(orgId, dw, spUrls);
        if (spUrls == null || spUrls.isEmpty()) {
            log.error("No urls qualify for request.");
            throw new Exception(KBConfiguration.GETMAP_EXCEPTION);
        }

        ArrayList urlWrapper = new ArrayList();
        Iterator it = spUrls.iterator();
        while (it.hasNext()) {

            SpLayerSummary spInfo = (SpLayerSummary) it.next();

            ServiceProviderRequest gmrWrapper = new ServiceProviderRequest();
            gmrWrapper.setMsSinceRequestStart(timeFromStart);
            gmrWrapper.setWidth(width);
            gmrWrapper.setHeight(height);
            gmrWrapper.setWmsVersion(ogc.getParameter(OGCConstants.WMS_VERSION));
            gmrWrapper.setSrs(null);
            gmrWrapper.setFormat(ogc.getParameter(OGCConstants.WMS_PARAM_FORMAT));
            gmrWrapper.setBoundingBox(ogc.getParameter(OGCConstants.WMS_PARAM_BBOX));

            Integer serviceProviderId = spInfo.getServiceproviderId();

            if (serviceProviderId != null && serviceProviderId.intValue() == -1) {
                //Say hello to B3P Layering!!
                StringBuffer url = createOnlineUrl(spInfo,ogc);
                gmrWrapper.setProviderRequestURI(url.toString());
                urlWrapper.add(gmrWrapper);

            } else {
                gmrWrapper.setServiceProviderId(serviceProviderId);

                boolean srsFound = false;
                Set<String> sqlQuery = getSRS(spInfo.getLayerId(), em);
                if (sqlQuery != null) {
                    Iterator sqlIterator = sqlQuery.iterator();
                    while (sqlIterator.hasNext()) {
                        String srs = (String) sqlIterator.next();
                        if (srs != null && srs.equals(givenSRS)) {
                            srsFound = true;
                        }
                    }
                } else {
                    log.error("No srs found");
                }
                if (!srsFound) {
                    log.error("No suitable srs found.");
                    throw new Exception(KBConfiguration.SRS_EXCEPTION);
                }

                
                StringBuffer url = createOnlineUrl(spInfo,ogc);
                gmrWrapper.setProviderRequestURI(url.toString());
                urlWrapper.add(gmrWrapper);
            }
        }

        doAccounting(orgId, dw, user);

        getOnlineData(dw, urlWrapper, true, OGCConstants.WMS_REQUEST_GetMap);
    }

    private StringBuffer createOnlineUrl(SpLayerSummary spInfo, OGCRequest ogc) {
        StringBuffer returnValue = new StringBuffer();
        String layersList = spInfo.getLayersAsString();
        returnValue.append(spInfo.getSpUrl());
        if (returnValue.indexOf("?")!=returnValue.length()-1 && returnValue.indexOf("&")!= returnValue.length()-1){
            if (returnValue.indexOf("?")>=0){
                returnValue.append("&");
            }else{
                returnValue.append("?");
            }
        }
        String[] params = ogc.getParametersArray();
        for (int i = 0; i < params.length; i++) {
            String[] keyValuePair = params[i].split("=");
            if (keyValuePair[0].equalsIgnoreCase(OGCConstants.WMS_PARAM_LAYERS)) {
                returnValue.append(OGCConstants.WMS_PARAM_LAYERS);
                returnValue.append("=");
                returnValue.append(layersList);
                returnValue.append("&");
            } else {
                returnValue.append(params[i]);
                returnValue.append("&");
            }
        }
        return returnValue;
    }

    /**
     * Returns a set of all SRSes (not SrsBoundingBox entities, the SRS id
     * strings) of the layer and all parent layers upto the toplayer. May return
     * an empty Set if the layer id is unknown or there simply are no SRSes for
     * the layers.
     */
    private Set<String> getSRS(int layerId, EntityManager em) {
        Layer l = em.find(Layer.class, layerId);
        if(l == null) {
            log.warn("getSRS(): layer with id " + layerId + " not found");
            return new HashSet<String>();
        }

        Set<String> srses = new HashSet<String>();

        srses.addAll(getSRSStrings(l.getSrsbb()));

        while(l.getParent() != null) {
            /* This is not the toplayer, so get the SRS's of the
             * layer higher up in the tree
             */
            l = l.getParent();
            srses.addAll(getSRSStrings(l.getSrsbb()));
        }
        return srses;
    }

    /**
     * Converts a Set<SrsBoundingBox> to a Set<String> with the
     * SrsBoundingBox.getSrs() values
     */
    private Set<String> getSRSStrings(Set<SrsBoundingBox> entities) {
        Set<String> result = new HashSet<String>();
        if(entities != null) { /* This check only necessary because Layer.srs isn't initialized in the standard way... */
            for(SrsBoundingBox srsbb: entities) {
                result.add(srsbb.getSrs());
            }
        }
        return result;
    }
}