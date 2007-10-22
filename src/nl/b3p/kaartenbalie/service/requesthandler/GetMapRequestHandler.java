/**
 * @(#)GetMapRequestHandler.java
 * @author N. de Goeij
 * @version 1.00 2006/12/13
 *
 * Purpose: the function of this class is to create a list of url's which direct to the right servers that have the desired layers
 * for the WMS GetMap request.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.service.requesthandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityTransaction;
import nl.b3p.wms.capabilities.KBConstants;
import nl.b3p.kaartenbalie.core.server.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetMapRequestHandler extends WMSRequestHandler implements KBConstants {
    
    private static final Log log = LogFactory.getLog(GetMapRequestHandler.class);
    
    // <editor-fold defaultstate="" desc="default GetMapRequestHandler() constructor.">
    public GetMapRequestHandler() {}
    // </editor-fold>
    
    /** Processes the parameters and creates the specified urls from the given parameters.
     * Each url will be used to recieve the data from the ServiceProvider this url is refering to.
     * @param parameters Map parameters
     * @return byte[]
     *
     * @throws IOException
     */
    // <editor-fold defaultstate="" desc="getRequest(Map parameters) method.">
    public void getRequest(DataWrapper dw, Map parameters) throws IOException, Exception {
        /*
         * Initialize some variables
         * And immediatly set the right output format (also for errors) because if an error occurs
         * with the GetMap functionality before the outputformat is set then the standard output
         * format would be used.
         */
        Long timeFromStart = new Long(dw.getRequestReporting().getMSSinceStart());
        String format = (String) parameters.get(WMS_PARAM_FORMAT);
        dw.setContentType(format);
        
        String inimageType = null;
        
        if (parameters.containsKey(WMS_PARAM_EXCEPTION_FORMAT)) {
            inimageType = format;
        }
        dw.setErrorContentType(inimageType);
        
        //Een aantal simpele controles. Deze staan vooraan omdat als een van deze
        //controles al faalt er dan niet onnodig op de zwaardere controles beslag
        //wordt gelegd.
        int width  = Integer.parseInt((String)parameters.get(WMS_PARAM_WIDTH));
        int height = Integer.parseInt((String)parameters.get(WMS_PARAM_HEIGHT));
        if(width < 1 || height < 1 || width > 2048 || height > 2048) {
            log.error("Image wrong size: width, height: " + width + ", " + height);
            throw new Exception(IMAGE_SIZE_EXCEPTION);
        }
        
        String [] boxx = ((String)parameters.get(WMS_PARAM_BBOX)).split(",");
        if(boxx.length < 4) {
            log.error("BBOX wrong size: " + boxx.length);
            throw new Exception(BBOX_EXCEPTION);
        }
        
        double minx=0.0, miny=0.0, maxx=-1.0, maxy=-1.0;
        try {
            minx = Double.parseDouble(boxx[0]);
            miny = Double.parseDouble(boxx[1]);
            maxx = Double.parseDouble(boxx[2]);
            maxy = Double.parseDouble(boxx[3]);
            if (minx > maxx || miny > maxy) {
                throw new Exception("");
            }
        } catch (Exception e) {
            log.error("BBOX error minx, miny, maxx, maxy: " + minx+ ", "+ miny+ ", "+maxx+ ", "+maxy);
            throw new Exception(BBOX_EXCEPTION);
        }
        dw.setRequestParameter("MsSinceRequestStart", timeFromStart);
        dw.setRequestParameter("Width", new Integer(width));
        dw.setRequestParameter("Height",new Integer(height));
        dw.setRequestParameter("WmsVersion", (String)parameters.get(WMS_VERSION));
        dw.setRequestParameter("Srs", null);
        dw.setRequestParameter("Format", (String)parameters.get(WMS_PARAM_FORMAT));
        dw.setRequestParameter("BoundingBox", (String)parameters.get(WMS_PARAM_BBOX));
        
        
        
        user = (User) parameters.get(KB_USER);
        Integer orgId = user.getOrganization().getId();
        
        url = (String) parameters.get(KB_PERSONAL_URL);
        String [] layers = ((String) parameters.get(WMS_PARAM_LAYERS)).split(",");
        
        Map userdefinedParams = filterUserdefinedParams(parameters);
        
        String givenSRS = (String)parameters.get(WMS_PARAM_SRS);
        
        List spUrls = getSeviceProviderURLS(layers, orgId, false);
        if(spUrls==null || spUrls.isEmpty()) {
            log.error("No urls qualify for request.");
            throw new Exception(GETMAP_EXCEPTION);
        }
        
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        
        ArrayList urls = new ArrayList();
        Iterator it = spUrls.iterator();
        while (it.hasNext()) {
            Map spInfo = (Map) it.next();
            StringBuffer layersList = (StringBuffer)spInfo.get("layersList");
            
            String query = "select distinct srs.srs from layer, srs " +
                    "where layer.layerid = srs.layerid and " +
                    "srs.srs is not null and " +
                    "layer.layerid = :toplayer";
            
            boolean srsFound = false;
            List sqlQuery = em.createNativeQuery(query).setParameter("toplayer", (Integer)spInfo.get("tlId")).getResultList();
            Iterator sqlIterator = sqlQuery.iterator();
            while (sqlIterator.hasNext()) {
                String srs = (String)sqlIterator.next();
                if(srs.equals(givenSRS)) {
                    srsFound = true;
                }
            }
            if(!srsFound) {
                log.error("No suitable srs found.");
                throw new Exception(SRS_EXCEPTION);
            }
            
            StringBuffer url = new StringBuffer();
            url.append((String)spInfo.get("spUrl"));
            url.append(WMS_VERSION);
            url.append("=");
            url.append((String)parameters.get(WMS_VERSION));
            url.append("&");
            url.append(WMS_REQUEST);
            url.append("=");
            url.append(WMS_REQUEST_GetMap);
            
            url.append("&");
            url.append(WMS_PARAM_BBOX);
            url.append("=");
            url.append((String)parameters.get(WMS_PARAM_BBOX));
            
            url.append("&");
            url.append(WMS_PARAM_SRS);
            url.append("=");
            url.append((String)parameters.get(WMS_PARAM_SRS));
            
            url.append("&");
            url.append(WMS_PARAM_TRANSPARENT);
            url.append("=");
            url.append(WMS_PARAM_TRANSPARENT_TRUE);
            
            url.append("&");
            url.append(WMS_PARAM_FORMAT);
            url.append("=");
            url.append((String)parameters.get(WMS_PARAM_FORMAT));
            
            url.append("&");
            url.append(WMS_PARAM_WIDTH);
            url.append("=");
            url.append((String)parameters.get(WMS_PARAM_WIDTH));
            
            url.append("&");
            url.append(WMS_PARAM_HEIGHT);
            url.append("=");
            url.append((String)parameters.get(WMS_PARAM_HEIGHT));
            
            url.append("&");
            url.append(WMS_PARAM_LAYERS);
            url.append("=");
            url.append(layersList);
            
            
            Iterator it2 = userdefinedParams.keySet().iterator();
            String urlstring=url.toString();
            while (it2.hasNext()) {
                String key = (String)it2.next();
                String value = (String)userdefinedParams.get(key);
                urlstring+="&";
                urlstring+=key;
                urlstring+="=";
                urlstring+=value.replaceAll("=", "%3D");
            }
            urls.add(urlstring.replaceAll(" ", "%20"));
        }
        tx.commit();
        
        getOnlineData(dw, urls, true, WMS_REQUEST_GetMap);
    }
    // </editor-fold>
    
    private Map filterUserdefinedParams(Map parameters) {
        Map map = new HashMap();
        map.putAll(parameters);
        
        List getmapParams = PARAMS_GetMap;
        Iterator it = getmapParams.iterator();
        while (it.hasNext()) {
            String param = (String) it.next();
            if (map.containsKey(param)) {
                map.remove(param);
            }
        }
        List nonRequiredParamsGetMap = NON_REQUIRED_PARAMS_GetMap;
        it = nonRequiredParamsGetMap.iterator();
        while (it.hasNext()) {
            String param = (String) it.next();
            if (map.containsKey(param)) {
                map.remove(param);
            }
        }
        return map;
    }
    
    public Map getReportingMap() throws Exception {
        //TODO
        return null;
    }
    
    
}