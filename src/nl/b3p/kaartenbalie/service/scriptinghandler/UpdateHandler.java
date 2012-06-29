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

import javax.servlet.http.HttpServletRequest;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import nl.b3p.ogc.utils.OGCScriptingRequest;

public class UpdateHandler extends WmsWfsHandler {    
    public UpdateHandler() throws Exception{
        super();
    }
    
    /**
     * Processes the parameters and creates the specified urls from the given parameters.
     * Each url will be used to recieve the data from the ServiceProvider this url is refering to.
     *
     * @param request       The incoming request
     * @param dw DataWrapper which contains all information that has to be sent to the client
     * @param user User the user which invoked the request
     *
     * @throws Exception
     */
    @Override
    public void getRequest(HttpServletRequest request,DataWrapper dw, User user) throws Exception {
        this.request    = request;
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
        
        B3PDynaValidatorForm dynaForm  = new B3PDynaValidatorForm();
        dynaForm.set("regexp", "");
        dynaForm.set("replacement","");
        int errors  = parser.batchUpdate(dynaForm,service);
        notices.append(".\n Tijdens het updaten zijn er ").append(errors).append("opgetreden.");
        
        mailAllAdmins();
    }
}
