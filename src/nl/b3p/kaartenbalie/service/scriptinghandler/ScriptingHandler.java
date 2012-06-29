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
 * @author  Rachelle Scheijen
 */
package nl.b3p.kaartenbalie.service.scriptinghandler;

import java.io.IOException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import nl.b3p.kaartenbalie.struts.Mailer;
import nl.b3p.ogc.utils.OGCScriptingRequest;
import nl.b3p.wms.capabilities.ServiceProvider;

abstract public class ScriptingHandler {
    protected final EntityManager em;
    protected DataWrapper dw;
    protected User user;
    protected OGCScriptingRequest ogcrequest;
    protected StringBuffer notices;
    protected HttpServletRequest request;
    
    public ScriptingHandler() throws Exception{
        this.em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);
        
        notices = new StringBuffer();
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
    abstract public void getRequest(HttpServletRequest request,DataWrapper dw, User user) throws IOException, Exception;
    
    /**
     * Mails all the admins
     */
    protected void mailAllAdmins(){
        /* Get all admins */
        try {
            List<User> users = em.createQuery(
                            "from User u INNER JOIN users_roles ul ON u.id = ul.users"
                            + "where lower(u.username) = lower(:username) "
                            + "and u.password = :password"
                            + "and u.role = 1").getResultList();

            Mailer mailer   = new Mailer();
            
            for(User admin : users){
                mailer.setMailBcc(admin.getEmailAddress());
            }
            
            String host = request.getLocalName();
            mailer.setMailTo("noreply@"+host);
            mailer.setSubject("Scripting rapport");
            mailer.setMailFrom("noreply@"+host);
            mailer.setBody(this.notices.toString());
            mailer.send();
        }
        catch (Exception ex) {}
        
    }
}