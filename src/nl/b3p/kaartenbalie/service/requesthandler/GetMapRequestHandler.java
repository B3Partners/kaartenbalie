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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import javax.persistence.EntityManager;
import nl.b3p.gis.B3PCredentials;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.monitoring.ServiceProviderRequest;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.service.servlet.ProxySLDServlet;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.SrsBoundingBox;
import nl.b3p.wms.capabilities.Style;
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
        this.url = user.getPersonalURL(dw.getRequest());
        Integer[] orgIds = user.getOrganizationIds();
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

        List spUrls = getSeviceProviderURLS(ogc.getParameter(OGCConstants.WMS_PARAM_LAYERS).split(","), orgIds, false, dw);
        if (spUrls == null || spUrls.isEmpty()) {
            log.error("No urls qualify for request.");
            throw new Exception(KBConfiguration.GETMAP_EXCEPTION);
        }
        spUrls = prepareAccounting(user.getMainOrganizationId(), dw, spUrls);
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
            B3PCredentials credentials  = new B3PCredentials();
            credentials.setUserName(spInfo.getUsername());
            credentials.setPassword(spInfo.getPassword());
            
            if (serviceProviderId != null && serviceProviderId.intValue() == -1) {
                //Say hello to B3P Layering!!                
                StringBuffer url = createOnlineUrl(spInfo, ogc,dw.getRequest().getRequestURL().toString());
                gmrWrapper.setProviderRequestURI(url.toString());
                gmrWrapper.setCredentials(credentials);
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

                //String sldUrl=dw.getRequest().getRequestURL().
                
                StringBuffer url = createOnlineUrl(spInfo, ogc,dw.getRequest().getRequestURL().toString());
                gmrWrapper.setProviderRequestURI(url.toString());
                
                gmrWrapper.setCredentials(credentials);
                urlWrapper.add(gmrWrapper);
            }
        }

        doAccounting(user.getMainOrganizationId(), dw, user);

        getOnlineData(dw, urlWrapper, true, OGCConstants.WMS_REQUEST_GetMap);
    }

    private StringBuffer createOnlineUrl(SpLayerSummary spInfo, OGCRequest ogc, String serviceUrl) throws UnsupportedEncodingException {

        /* TODO:
         * Check if the layer styles contain an Sld part. If so add &sld to this
         * url that refers to the kaartenbalie sld servlet. The servlet will combine
         * the parts to form the needed sld.
        */

        /* TODO:
         * Check if there are spaces allowed in &LAYER param. Layer A ?
         */
        
        StringBuffer returnValue = new StringBuffer();
        List<String> newSldParams= new ArrayList<String>();
        List<Integer> sldStyleIds= new ArrayList<Integer>();
        String layersString = spInfo.getLayersAsString();
        String kbProxySldUrl=serviceUrl.replace("/services/", "/ProxySLD/");
        List layersList = spInfo.getLayers();
        returnValue.append(spInfo.getSpUrl());
        if (returnValue.indexOf("?") != returnValue.length() - 1 && returnValue.indexOf("&") != returnValue.length() - 1) {
            if (returnValue.indexOf("?") >= 0) {
                returnValue.append("&");
            } else {
                returnValue.append("?");
            }
        }
        String[] params = ogc.getParametersArray();
        for (int i = 0; i < params.length; i++) {
            //In SLD_BODY zitten = tekens. Dus niet splitten
            String[] keyValuePair = new String[2];
            int indexOfIs=params[i].indexOf("=");
            if (indexOfIs==-1)
                continue;
            keyValuePair[0]=params[i].substring(0,indexOfIs);
            if (indexOfIs+1<params[i].length())
                keyValuePair[1]=params[i].substring(indexOfIs+1);
            else
                keyValuePair[1]="";
            
            if (keyValuePair[0].equalsIgnoreCase(OGCConstants.WMS_PARAM_LAYERS)) {
                returnValue.append(OGCConstants.WMS_PARAM_LAYERS);
                returnValue.append("=");
                returnValue.append(layersString);
            } else if (keyValuePair[0].equalsIgnoreCase(OGCConstants.WMS_PARAM_STYLES)) {
                returnValue.append(OGCConstants.WMS_PARAM_STYLES);
                returnValue.append("=");
                //maak alleen de styles goed als er geen sld= of sld_body= parameter aanwezig is.
                
                try {
                    if (layersList != null && layersList.size() > 0) {
                        String stylesParameter = ogc.getParameter(OGCConstants.WMS_PARAM_STYLES);
                        if (stylesParameter != null && stylesParameter.length() > 0) {
                            //splitten werkt niet. Een lege string (tussen 2 komma's) wordt dan niet gezien als waarde
                            //String[] stylesArray = stylesParameter.split(",");                            
                            String tempStyles = ""+stylesParameter;
                            List<String> styles= new ArrayList<String>();
                            while (tempStyles!=null){
                                if (tempStyles.length()==0){
                                    styles.add("");
                                    tempStyles=null;
                                    break;
                                }
                                int kommaIndex= tempStyles.indexOf(",");
                                if(kommaIndex<0){
                                    kommaIndex=tempStyles.length();
                                    styles.add(tempStyles.substring(0));
                                    tempStyles=null;
                                    break;
                                }else{
                                    styles.add(tempStyles.substring(0,kommaIndex));                                
                                    tempStyles=tempStyles.substring(kommaIndex+1,tempStyles.length());                                
                                }
                            }
                            
                            String layersParameter = ogc.getParameter(OGCConstants.WMS_PARAM_LAYERS);
                            if (layersParameter != null && layersParameter.length() > 0) {
                                String[] layersArray = layersParameter.split(",");
                                if (styles.size() == layersArray.length) {
                                    //StringBuffer stylesString = new StringBuffer();
                                    List<String> providerStyles= new ArrayList<String>();
                                    for (int j = 0; j < layersArray.length; j++) {
                                        Iterator it = layersList.iterator();
                                        while (it.hasNext()) {
                                            String l = (String) it.next();
                                            String completeName = completeLayerName(spInfo.getSpAbbr(), l);
                                            //TODO: Moet het toegevoegd worden als Style= of als sld                                            
                                            if (completeName.equals(layersArray[j])) {
                                                String style = styles.get(j);                                                
                                                //als er een style is gekozen met een SLDpart 
                                                //niet de style meenemen maar een sld bouwen
                                                Style s=spInfo.getStyle(l,style);
                                                if (s!=null && s.getSldPart()!=null){
                                                    providerStyles.add("");
                                                    sldStyleIds.add(s.getId());                                                    
                                                }else{
                                                    providerStyles.add(style);
                                                }
                                                break;
                                            }
                                        }
                                    }
                                    String stylesString="";
                                    for (int p=0; p < providerStyles.size(); p++){
                                        if (p!=0)
                                            stylesString+=",";
                                        stylesString+=providerStyles.get(p);
                                    }
                                    returnValue.append(stylesString);
                                    if (sldStyleIds.size()>0){                                        
                                        String styleIdParam="";
                                        for (Integer sldStyleId:sldStyleIds){
                                            if (styleIdParam.length()>0)
                                                styleIdParam+=",";
                                            styleIdParam+=sldStyleId;
                                        }
                                        StringBuffer sldUrl= new StringBuffer();                                                                                
                                        sldUrl.append(ProxySLDServlet.PARAM_STYLES);
                                        sldUrl.append("=");
                                        sldUrl.append(styleIdParam);
                                        newSldParams.add(sldUrl.toString());                                        
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    // so no styles param
                    log.debug(e);
                }
            } else if (keyValuePair[0].equalsIgnoreCase(OGCConstants.WMS_PARAM_SLD) ||
                    keyValuePair[0].equalsIgnoreCase(OGCConstants.WMS_PARAM_SLD_BODY)) {
                //als SLD= dan url alvast cachen.
                if(keyValuePair[0].equalsIgnoreCase(OGCConstants.WMS_PARAM_SLD))
                    ProxySLDServlet.addSLDToCache(keyValuePair[1]);
                //SLD of SLD Body
                StringBuffer sldParam= new StringBuffer();
                if(keyValuePair[0].equalsIgnoreCase(OGCConstants.WMS_PARAM_SLD)){
                    sldParam.append(ProxySLDServlet.PARAM_ORIGINAL_SLD_URL);
                }else{
                    sldParam.append(ProxySLDServlet.PARAM_ORIGINAL_SLD_BODY);
                }
                sldParam.append("=");
                sldParam.append(URLEncoder.encode(keyValuePair[1], "utf-8"));
                newSldParams.add(sldParam.toString());
                //service provider ID
                StringBuffer serviceProviderIds= new StringBuffer();
                serviceProviderIds.append(ProxySLDServlet.PARAM_SERVICEPROVIDER_ID);
                serviceProviderIds.append("=");
                serviceProviderIds.append(spInfo.getServiceproviderId());
                newSldParams.add(serviceProviderIds.toString());
                
            }else {
                returnValue.append(params[i]);
            }
            returnValue.append("&");
        }
        if (newSldParams.size()>0){
            
            //make a new SLD url to the proxySldServlet.
            StringBuffer sldUrl= new StringBuffer();
            sldUrl.append(kbProxySldUrl);
            sldUrl.append(sldUrl.indexOf("?") > 0 ? "&" : "?");
            for (String param : newSldParams){
                sldUrl.append(param);
                sldUrl.append(sldUrl.indexOf("?") > 0 ? "&" : "?");
            }            
            //altijd een sld=. Ook sld_body zodat we kunnen opsplitsen                
            returnValue.append(OGCConstants.WMS_PARAM_SLD);
            returnValue.append("=");
            returnValue.append(URLEncoder.encode(sldUrl.toString(), "utf-8")); 
            returnValue.append("&");
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
        if (l == null) {
            log.warn("getSRS(): layer with id " + layerId + " not found");
            return new HashSet<String>();
        }

        Set<String> srses = new HashSet<String>();

        srses.addAll(getSRSStrings(l.getSrsbb()));

        while (l.getParent() != null) {
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
        if (entities != null) { /* This check only necessary because Layer.srs isn't initialized in the standard way... */
            for (SrsBoundingBox srsbb : entities) {
                result.add(srsbb.getSrs());
            }
        }
        return result;
    }
}
