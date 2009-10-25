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
package nl.b3p.kaartenbalie.struts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.service.DirectoryParser;
import nl.b3p.kaartenbalie.service.MapParser;
import nl.b3p.ogc.wfs.v110.WfsServiceProvider;
import nl.b3p.wms.capabilities.ServiceProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.validator.DynaValidatorForm;

public class MapServerAction extends KaartenbalieCrudAction {

    protected static final Log log = LogFactory.getLog(MapServerAction.class);

    @Override
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        prepareMethod(dynaForm, request, LIST, LIST);
        addDefaultMessage(mapping, request);
        return mapping.findForward(SUCCESS);
    }

    public ActionForward edit(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return super.edit(mapping, dynaForm, request, response);
    }

    @Override
    public ActionForward save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return super.save(mapping, dynaForm, request, response);
    }

    @Override
    protected void createLists(DynaValidatorForm form, HttpServletRequest request) {
        String mapdir = MyEMFDatabase.getMapfiles();
        List mapFiles = new ArrayList();
        File dir = new File(mapdir);
        if (dir.isDirectory()) {
            DirectoryParser directoryParser = new DirectoryParser();
            String[] allowed_files = {".map"};
            mapFiles = directoryParser.parse2List(dir, allowed_files);
        }

        List mapFilesInfoList = new ArrayList();
        Iterator it = mapFiles.iterator();
        while (it.hasNext()) {
            String mapFilePath = (String) it.next();
            try {
                MapParser mapParser = new MapParser(new File(mapFilePath));
                mapParser.parse();
                Map md = mapParser.getWebMetadata();
                String url = (String) md.get("wms_onlineresource");
                if (url == null || url.length() == 0) {
                    continue;
                }
                md.put("encoded_url", URLEncoder.encode(url, "UTF-8"));
                String title = (String) md.get("wms_title");
                if (title == null || title.length() == 0) {
                    title = "title" + mapFilesInfoList.size();
                }
                md.put("wms_title", title);
                md.put("encoded_title", URLEncoder.encode(title, "UTF-8"));
                md.put("map", mapFilePath);
                md.put("id", "map" + mapFilesInfoList.size());
                try {
                    md.put("kb_wms", findWMSService(url));
                } catch (Exception ex) {
                    log.error("", ex);
                }
                try {
                    md.put("kb_wfs", findWFSService(url));
                } catch (Exception ex) {
                    log.error("", ex);
                }
                mapFilesInfoList.add(md);
            } catch (FileNotFoundException ex) {
                log.error("map file '" + mapFilePath + "' not found:", ex);
            } catch (IOException ex) {
                log.error("map file '" + mapFilePath + "' not readable:", ex);
            }
        }
        request.setAttribute("mapfiles", mapFilesInfoList);
    }

    protected Integer findWMSService(String url) throws Exception {
        if (url == null || url.length() == 0) {
            return null;
        }
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        List serviceproviderlist = em.createQuery(
                "from ServiceProvider sp where " +
                "lower(sp.url) = lower(:url) ").setParameter("url", url.trim()).getResultList();
        if (serviceproviderlist == null || serviceproviderlist.isEmpty()) {
            return null;
        }
        ServiceProvider sp = (ServiceProvider) serviceproviderlist.get(0);
        return sp.getId();
    }

    protected Integer findWFSService(String url) throws Exception {
        if (url == null || url.length() == 0) {
            return null;
        }
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        List serviceproviderlist = em.createQuery(
                "from WfsServiceProvider sp where " +
                "lower(sp.url) = lower(:url) ").setParameter("url", url.trim()).getResultList();
        if (serviceproviderlist == null || serviceproviderlist.isEmpty()) {
            return null;
        }
        WfsServiceProvider sp = (WfsServiceProvider) serviceproviderlist.get(0);
        return sp.getId();
    }
}
