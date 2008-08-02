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
import java.util.Map;
import javax.persistence.EntityManager;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.core.server.reporting.domain.requests.WMSGetMapRequest;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetMapRequestHandler extends WMSRequestHandler {

    private static final Log log = LogFactory.getLog(GetMapRequestHandler.class);
    // <editor-fold defaultstate="" desc="default GetMapRequestHandler() constructor.">
    public GetMapRequestHandler() {
    }
    // </editor-fold>
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
    // <editor-fold defaultstate="" desc="getRequest(DataWrapper dw, User user) method.">
    public void getRequest(DataWrapper dw, User user) throws IOException, Exception {

        EntityManager em = MyEMFDatabase.getEntityManager();
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
        dw.setRequestParameter("MsSinceRequestStart", timeFromStart);

        Integer width = null;
        try {
            width = new Integer(ogc.getParameter(OGCConstants.WMS_PARAM_WIDTH));
        } catch (NumberFormatException nfe) {
            width = new Integer(-1);
        }
        dw.setRequestParameter("Width", width);

        Integer height = null;
        try {
            height = new Integer(ogc.getParameter(OGCConstants.WMS_PARAM_HEIGHT));
        } catch (NumberFormatException nfe) {
            height = new Integer(-1);
        }
        dw.setRequestParameter("Height", height);
        dw.setRequestParameter("WmsVersion", ogc.getParameter(OGCConstants.WMS_VERSION));
        dw.setRequestParameter("Srs", null);
        dw.setRequestParameter("Format", ogc.getParameter(OGCConstants.WMS_PARAM_FORMAT));
        dw.setRequestParameter("BoundingBox", ogc.getParameter(OGCConstants.WMS_PARAM_BBOX));

        String givenSRS = ogc.getParameter(OGCConstants.WMS_PARAM_SRS);
        Map userdefinedParams = ogc.getNonOGCParameters();

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
            WMSGetMapRequest gmrWrapper = new WMSGetMapRequest();
            Integer serviceProviderId = spInfo.getServiceproviderId();

            if (serviceProviderId != null && serviceProviderId.intValue() == -1) {
                //Say hello to B3P Layering!!
                StringBuffer url = new StringBuffer();
                String layersList = spInfo.getLayersAsString();
                url.append(spInfo.getSpUrl());
                String[] params = ogc.getParametersArray();
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
                gmrWrapper.setProviderRequestURI(url.toString());
                urlWrapper.add(gmrWrapper);

            } else {
                gmrWrapper.setServiceProviderId(serviceProviderId);

                String layersList = spInfo.getLayersAsString();

                String query = "select distinct srs.srs from layer, srs " +
                        "where layer.layerid = srs.layerid and " +
                        "srs.srs is not null and " +
                        "layer.layerid = :toplayer";

                boolean srsFound = false;
                List sqlQuery = em.createNativeQuery(query).setParameter("toplayer", spInfo.getLayerId()).getResultList();

                /* 
                 * If there isn't a SRS it will try to get the SRS of the parentlayer 
                 */
                if (sqlQuery.size() == 0 || sqlQuery == null) {
                    sqlQuery = getSRS(spInfo.getLayerId(), em);
                }

                if (sqlQuery != null) {
                    Iterator sqlIterator = sqlQuery.iterator();
                    while (sqlIterator.hasNext()) {
                        String srs = (String) sqlIterator.next();
                        if (srs.equals(givenSRS)) {
                            srsFound = true;
                        }
                    }
                }
                if (!srsFound) {
                    log.error("No suitable srs found.");
                    throw new Exception(KBConfiguration.SRS_EXCEPTION);
                }

                StringBuffer url = new StringBuffer();
                url.append(spInfo.getSpUrl());
                String[] params = ogc.getParametersArray();
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
                gmrWrapper.setProviderRequestURI(url.toString());
                urlWrapper.add(gmrWrapper);
            }
        }

        doAccounting(orgId, dw, user);

        getOnlineData(dw, urlWrapper, true, OGCConstants.WMS_REQUEST_GetMap);
    }
    // </editor-fold>
    /*
     * Recursieve functie om de SRS van een parentlayer op te halen.
     * Return null als er geen parentlayers meer zijn en er geen SRS gevonden is
     */
    private List getSRS(int layerId, EntityManager em) {
        String parentId = em.createNativeQuery("select parentid from layer where layer.layerid = :layerid").setParameter("layerid", layerId).getSingleResult().toString();
        if (parentId == null || parentId.equals("")) {
            return null;
        } else {
            String query = "select distinct srs.srs from layer, srs " +
                    "where layer.layerid = srs.layerid and " +
                    "srs.srs is not null and " +
                    "layer.layerid = :toplayer";
            List sqlQuery = em.createNativeQuery(query).setParameter("toplayer", parentId).getResultList();
            if (sqlQuery.size() == 0 || sqlQuery == null) {
                int parent = Integer.parseInt(parentId);
                return getSRS(parent, em);
            } else {
                return sqlQuery;
            }
        }
    }
}