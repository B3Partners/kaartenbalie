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
 * 
 * @author Rachelle Scheijen
 */
package nl.b3p.kaartenbalie.service.scriptinghandler;

import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.service.WFSParser;
import nl.b3p.kaartenbalie.service.WMSParser;
import nl.b3p.kaartenbalie.service.WmsWfsParser;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import nl.b3p.ogc.utils.OGCScriptingRequest;
import org.apache.struts.validator.DynaValidatorForm;

public class UpdateHandler extends ScriptingHandler { 
    private WmsWfsParser parser;
    private String type;
    
    public UpdateHandler() throws Exception{
        super();
    }
    
    public void setWFS(){
        parser  = new WFSParser();
        type    = "WFS";
    }
    
    public void setWMS(){
        parser  = new WMSParser();
        type    = "WMS";
    }
    
    public void getRequest(DataWrapper dw, User user) throws Exception {
        this.ogcrequest = (OGCScriptingRequest) dw.getOgcrequest();
        this.dw         = dw;
        this.user       = user;
        
        batchUpdate();
    }
    
    private void batchUpdate() throws Exception {       
        String service          = ogcrequest.getParameter(OGCScriptingRequest.SERVICE);
        notices.append(type).append("Batch update van");
        if( service.equals("") ){
            notices.append(" alle services");
        }
        else {
            notices.append(" services met de prefix ").append(service);
        }
        
        DynaValidatorForm dynaForm  = new DynaValidatorForm();
        int errors  = parser.batchUpdate(dynaForm,service);
        notices.append(".\n Tijdens het updaten zijn er ").append(errors).append("opgetreden.");
        
        mailAllAdmins();
    }
}
