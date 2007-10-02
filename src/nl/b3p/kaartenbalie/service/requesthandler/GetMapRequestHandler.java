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
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.KBConstants;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.service.MyDatabase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
            throw new Exception("msWMSLoadGetMapParams(): WMS server error. Image size out of range, WIDTH and HEIGHT must be between 1 and 2048 pixels.");
        }
        
        String [] boxx = ((String)parameters.get(WMS_PARAM_BBOX)).split(",");
        if(boxx.length < 4) {
            throw new Exception("msWMSLoadGetMapParams(): WMS server error. Invalid values for BBOX.");
        }
        
        double minx = Double.parseDouble(boxx[0]);
        double miny = Double.parseDouble(boxx[1]);
        double maxx = Double.parseDouble(boxx[2]);
        double maxy = Double.parseDouble(boxx[3]);
        
        if (minx > maxx || miny > maxy) {
            throw new Exception("msWMSLoadGetMapParams(): WMS server error. Invalid values for BBOX.");
        }
        
        user = (User) parameters.get(KB_USER);
        Integer orgId = user.getOrganization().getId();
        
        url = (String) parameters.get(KB_PERSONAL_URL);
        String [] layers = ((String) parameters.get(WMS_PARAM_LAYERS)).split(",");
        
        ArrayList spUrls = getSeviceProviderURLS(layers, orgId, false);
        
        Map userdefinedParams = filterUserdefinedParams(parameters);
        
        Session sess = MyDatabase.currentSession();
        Transaction tx = sess.beginTransaction();        
        String givenSRS = (String)parameters.get(WMS_PARAM_SRS);        
        HashMap spMap = new HashMap();
        ArrayList urls = new ArrayList();
        Iterator it = spUrls.iterator();
        while (it.hasNext()) {
            String [] sp_layerlist = (String []) it.next();
            
            //Eerst controle of deze spid al geweest is.... en als niet dan controleren of SRS ok is
            if(!spMap.containsKey(sp_layerlist[0])) {
                String query = 
                        "SELECT DISTINCT temptabel.LAYERID, srs.SRS FROM srs INNER JOIN (" + 
                        " SELECT layer.LAYERID, layer.PARENTID FROM layer WHERE layer.PARENTID IS NULL)" + 
                        " AS temptabel ON temptabel.LAYERID = srs.LAYERID" + 
                        " AND srs.SRS IS NOT NULL AND temptabel.LAYERID = :toplayer";
                
                boolean srsFound = false;
                List sqlQuery = sess.createSQLQuery(query).setParameter("toplayer", sp_layerlist[3]).list();
                Iterator sqlIterator = sqlQuery.iterator();
                while (sqlIterator.hasNext()) {
                    Object [] objecten = (Object [])sqlIterator.next();
                    String srs = (String) objecten[1];
                    if(srs.equals(givenSRS)) {
                        srsFound = true;
                    }
                }
                
                if(srsFound) {
                    spMap.put(sp_layerlist[3], new Boolean(true));
                } else {
                    throw new Exception("msWMSLoadGetMapParams(): WMS server error. Invalid SRS given : SRS must be valid for all requested layers.");
                }
            }
            
            StringBuffer url = new StringBuffer();
            url.append(sp_layerlist[1]);
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
            url.append(sp_layerlist[2]);
            
            Iterator it2 = userdefinedParams.keySet().iterator();
            while (it2.hasNext()) {
                String key = (String)it2.next();
                String value = (String)userdefinedParams.get(key);
                url.append("&");
                url.append(key);
                url.append("=");
                url.append(value);
            }
            urls.add(url.toString().replaceAll(" ", "%20"));
        }
        tx.commit();
        
        if(urls == null) {
            throw new Exception("msWMSLoadGetMapParams(): WMS server error. Invalid layer(s) given in the LAYERS parameter.");
        }
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
    
    
}