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

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import nl.b3p.ogc.utils.OGCScriptingRequest;

public class DeleteAllowedHandler extends WmsWfsHandler {
    private String typeDelete = "single";
    public static final String DELETE_SINGLE = "single";
    public static final String DELETE_ALL = "all";
    
    public DeleteAllowedHandler() throws Exception{
        super();
    }
    
    public void setType(String type){
        if( type.equals(DELETE_SINGLE) || type.equals(DELETE_ALL) ){
            this.typeDelete = type;
        }
    }

    @Override
    public void getRequest(HttpServletRequest request, DataWrapper dw, User user) throws IOException, Exception {
        this.request = request;
        this.ogcrequest = (OGCScriptingRequest) dw.getOgcrequest();
        this.dw = dw;
        this.user = user;
        String abbr  = "";
        
        try {
            if( this.typeDelete.equals(DELETE_SINGLE) ){
                abbr = this.ogcrequest.getParameter(OGCScriptingRequest.ABBR);
                
                this.parser.deleteAllowedService(abbr, em);
            }
            else {
                this.parser.deleteAllAllowedServices(em);
            }
        }
        catch(Exception ex){
            if( this.type.equals(DELETE_SINGLE) ){
                this.notices.append("Excepting during deleting all ").append(this.type).append(" services.\n Exception : ").append(ex.getLocalizedMessage());
            }
            else {
                this.notices.append("Excepting during deleting ").append(this.type).append(" service with abbr ").append(abbr).append(".\n Exception : ").append(ex.getLocalizedMessage());
            }
            
            this.mailAllAdmins();
        }
    }
    
}
