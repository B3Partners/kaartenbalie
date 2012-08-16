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
import nl.b3p.kaartenbalie.service.WMSParser;
import nl.b3p.kaartenbalie.service.WmsWfsParser;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import nl.b3p.ogc.utils.OGCScriptingRequest;
import org.apache.struts.action.DynaActionFormClass;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.FormPropertyConfig;
import org.apache.struts.validator.DynaValidatorForm;

public class AddHandler extends WmsWfsHandler {

    private String url;

    public AddHandler() throws Exception {
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
     * @throws IOException
     */
    @Override
    public void getRequest(HttpServletRequest request, DataWrapper dw, User user) throws IOException, Exception {
        this.request = request;
        this.ogcrequest = (OGCScriptingRequest) dw.getOgcrequest();
        this.dw = dw;
        this.user = user;

        parseRequest();
    }

    /**
     * Parses the add request
     */
    private void parseRequest() throws IllegalAccessException, InstantiationException {
        String name = this.ogcrequest.getParameter(OGCScriptingRequest.NAME);
        String abbr = this.ogcrequest.getParameter(OGCScriptingRequest.ABBR);
        this.url = this.ogcrequest.getParameter(OGCScriptingRequest.URL);
        Boolean update = Boolean.valueOf(this.ogcrequest.getParameter(OGCScriptingRequest.UPDATE));

        /*
         * Check for existing service
         */
        Boolean exists = parser.abbrExists(abbr, em);
        
        FormBeanConfig config = new FormBeanConfig();
        
        config.setName("serverForm");
        config.setType(DynaValidatorForm .class.getName());
        
        FormPropertyConfig property1 = new FormPropertyConfig();
        property1.setName("givenName");
        property1.setType("java.lang.String");
        
        FormPropertyConfig property2 = new FormPropertyConfig();
        property2.setName("abbr");
        property2.setType("java.lang.String");
        
        FormPropertyConfig property3 = new FormPropertyConfig();
        property3.setName("url");
        property3.setType("java.lang.String");
        
        FormPropertyConfig property4 = new FormPropertyConfig();
        property4.setName("sldUrl");
        property4.setType("java.lang.String");
        
        FormPropertyConfig property5 = new FormPropertyConfig();
        property5.setName("username");
        property5.setType("java.lang.String");
        
        FormPropertyConfig property6 = new FormPropertyConfig();
        property6.setName("password");
        property6.setType("java.lang.String");
        
        FormPropertyConfig property7 = new FormPropertyConfig();
        property7.setName("orgSelected");
        property7.setType("java.lang.String[]");
        
        FormPropertyConfig property8 = new FormPropertyConfig();
        property8.setName("ignoreResource");
        property8.setType("java.lang.Boolean");
        
        FormPropertyConfig property9 = new FormPropertyConfig();
        property9.setName("id");
        property9.setType("java.lang.String");
        
        FormPropertyConfig property10 = new FormPropertyConfig();
        property10.setName("updatedDate");
        property10.setType("java.lang.String");
        
        FormPropertyConfig property11 = new FormPropertyConfig();
        property11.setName("regexp");
        property11.setType("java.lang.String");
        
        FormPropertyConfig property12 = new FormPropertyConfig();
        property12.setName("replacement");
        property12.setType("java.lang.String");
        
        FormPropertyConfig property13 = new FormPropertyConfig();
        property13.setName("providerId");
        property13.setType("java.lang.String");
        
        FormPropertyConfig property14 = new FormPropertyConfig();
        property14.setName("overwrite");
        property14.setType("java.lang.Boolean");
        
        FormPropertyConfig property15 = new FormPropertyConfig();
        property15.setName("uploadFile");
        property15.setType("org.apache.struts.upload.FormFile");

        config.addFormPropertyConfig(property1);
        config.addFormPropertyConfig(property2);
        config.addFormPropertyConfig(property3);
        config.addFormPropertyConfig(property4);
        config.addFormPropertyConfig(property5);
        config.addFormPropertyConfig(property6);
        config.addFormPropertyConfig(property7);
        config.addFormPropertyConfig(property8);
        config.addFormPropertyConfig(property9);
        config.addFormPropertyConfig(property10);
        config.addFormPropertyConfig(property11);
        config.addFormPropertyConfig(property12);
        config.addFormPropertyConfig(property13);
        config.addFormPropertyConfig(property14);
        config.addFormPropertyConfig(property15);

        DynaActionFormClass dynaClass = DynaActionFormClass.createDynaActionFormClass(config);
        DynaValidatorForm form = (DynaValidatorForm) dynaClass.newInstance();
        
        form.set("givenName", name);
        form.set("abbr", abbr);
        form.set("url", url);
        form.set("sldUrl", this.ogcrequest.getParameter(OGCScriptingRequest.SLD));
        form.set("username", this.ogcrequest.getParameter(OGCScriptingRequest.USERNAME));
        form.set("password", this.ogcrequest.getParameter(OGCScriptingRequest.PASSWORD));
        form.set("orgSelected", this.ogcrequest.getParameter(OGCScriptingRequest.GROUPS).split(","));
        
        if (exists) {
            if (!update) {
                this.notices.append("Fout : ").append(type).append(" service ").append(name).append(" bestaat al en overschrijven is false.");
            } else {
                try {
                    updateService(form);
                    
                    this.notices.append(type).append(" Service ").append(name).append(" is bijgewerkt.");
                }
                catch(Exception e){
                    this.notices.append("Fout : ").append(type).append(" service ").append(name).append(" kon niet bijgewerkt worden. Foutmelding : ").append(e.getLocalizedMessage());
                }
            }
        } else {
            try {
                addService(form);

                this.notices.append(type).append(" Service ").append(name).append(" is toegevoegd.");
            } catch (Exception e) {
                this.notices.append("Fout : ").append(type).append(" service ").append(name).append(" kon niet toegevoegd worden. Foutmelding : ").append(e.getLocalizedMessage());
            }
        }
    }

    /**
     * Adds the service
     * 
     * @param dynaForm      The form data
     * @throws Exception 
     */
    private void addService(DynaValidatorForm dynaForm) throws Exception {
        String code = parser.saveProvider(request, dynaForm);

        if (code.equals(WMSParser.ERROR_INVALID_URL)) {
            throw new Exception("URL " + this.url + " is ongeldig.");
        } else if (code.equals(WmsWfsParser.SERVER_CONNECTION_ERROR)) {
            throw new Exception("URL " + this.url + " is niet bestaand.");
        } else if (code.equals(WMSParser.MALFORMED_CAPABILITY_ERROR)) {
            /*
             * data mallformed
             */
            throw new Exception("URL " + this.url + " is geen " + this.type + " service.");
        } else if (code.equals(WMSParser.SAVE_ERRORKEY)) {
            throw new Exception("Opslaan van de service is mislukt.");
        } else if (code.equals(WMSParser.UNSUPPORTED_WMSVERSION_ERROR)) {
            /*
             * WMS version unsupported
             */
            throw new Exception("Url " + this.url + " bevat een niet gesupported versie.");
        } else if (code.equals(WMSParser.ERROR_DELETE_OLD_PROVIDER)) {
            throw new Exception("Verwijderen van de oude service is mislukt");
        }
    }

    /**
     * Updates the service
     * 
     * @param dynaForm      The form data
     * @throws Exception 
     */
    private void updateService(DynaValidatorForm dynaForm) throws Exception {
        String abbr = String.valueOf(dynaForm.get("abbr"));
        
        if( parser.batchUpdate(dynaForm,abbr) > 0 ){
            throw new Exception("Fout tijdens het updaten van "+this.type+" service met de prefix "+abbr+" : "+parser.getException().getLocalizedMessage());
        }
    }
}
