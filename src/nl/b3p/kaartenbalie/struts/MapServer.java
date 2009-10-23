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
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.service.DirectoryParser;
import nl.b3p.kaartenbalie.service.MapParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.validator.DynaValidatorForm;
import org.json.JSONObject;

public class MapServer extends ServerAction {

    //private static final Log log = LogFactory.getLog(MapServer.class);
    @Override
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // FOR TESTING
        MapParser mapParser = new MapParser(new File("C:/Documents and Settings/Gertjan/Mijn documenten/Downloads/basis.map"));
        mapParser.parse();

        Map<String, String> metadata = mapParser.getWebMetadata();


        // Note that wms_title can be found on two places, second one overwrites the first one
        String wms_title = metadata.get("wms_title");
        String wms_onlineresource = metadata.get("wms_onlineresource");
        String wms_srs = metadata.get("wms_srs");

        // \FOR TESTING

        createTreeview(request);
        return super.unspecified(mapping, dynaForm, request, response);
    }

    @Override
    public ActionForward save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        MapParser mapParser = new MapParser(new File("C:/Documents and Settings/Gertjan/Mijn documenten/Downloads/basis.map"));
        mapParser.parse();

        Map<String, String> metadata = mapParser.getWebMetadata();

        // Note that wms_title can be found on two places, second one overwrites the first one
        String wms_title = metadata.get("wms_title");
        String wms_onlineresource = metadata.get("wms_onlineresource");
        String wms_srs = metadata.get("wms_srs");

        createTreeview(request); // niet zeker of dit nodig is
        return super.unspecified(mapping, dynaForm, request, response);
    }

    private void createTreeview(HttpServletRequest request) throws Exception {
        JSONObject root = new JSONObject();
        DirectoryParser directoryParser = new DirectoryParser();
        File dir = new File(MyEMFDatabase.getMapfiles());
        String[] allowed_files = {".map"};

        if (dir.isDirectory()) {
            root.put("id", "root");
            root.put("children", directoryParser.stepIntoDirectory(dir, allowed_files));
            root.put("title", "bestanden");
        }

        request.setAttribute("mapfiles", root.toString(4));
    }
}
