/*
 * ServerAction.java
 *
 * Created on 2 oktober 2006, 13:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.struts;

import java.util.HashSet;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.kaartenbalie.core.Server;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.validator.DynaValidatorForm;
import org.hibernate.Session;

/**
 *
 * @author Nando De Goeij
 */
public class ServerAction extends KaartenbalieCrudAction {
    
    /* forward name="success" path="" */
    private final static String SUCCESS = "success";
    
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer id = FormUtils.StringToInteger(dynaForm.getString("id"));
        Server server = this.getServer(dynaForm, request, false, id);
        
        if (null != server) {
            this.populateServerForm(server, dynaForm, request);
        }
        return super.unspecified(mapping, dynaForm, request, response);
    }
    
    /*
     * Returns the server with a specified id.
     */
    private Server getServer(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew, Integer id) {
        Session session = getHibernateSession();
        Server server = null;
        
        if(null == id && createNew) {
            server = new Server();
        } else if (null != id) {
            server = (Server)session.load(Server.class, new Long(id));
        }
        return server;
    }
    
    /*
     * If a server with a specified id is chosen this method will fill the JSP form with the dat of this server.
     */
    private void populateServerForm(Server server, DynaValidatorForm dynaForm, HttpServletRequest request) {
        dynaForm.set("serverName", server.getServerName());
        dynaForm.set("serverUrl", server.getServerUrl());
        dynaForm.set("serverUpdatedDate", server.getServerUpdatedDate());
        dynaForm.set("serverReviewed", server.getServerReviewed());
    }
    
    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        super.createLists(form, request);
        
        List serverlist = getHibernateSession().createQuery("from Server").list();
        request.setAttribute("serverlist", serverlist);        
    }
    
    private void populateServerObject(DynaValidatorForm dynaForm, Server server) {
        server.setServerCapa("1");
        server.setServerServ("1");
        server.setServerName(FormUtils.nullIfEmpty(dynaForm.getString("serverName")));
//        server.setCapa(FormUtils.nullIfEmpty(dynaForm.getString("capa")));
//        server.setServ(FormUtils.nullIfEmpty(dynaForm.getString("serv")));
        server.setServerUrl(dynaForm.getString("serverUrl"));
//        server.setServerUpdatedDate(FormUtils.nullIfEmpty(dynaForm.getString("serverUpdatedDate")));
//        server.setServerReviewed(FormUtils.nullIfEmpty(dynaForm.getString("serverReviewed")));
        server.setServerUpdatedDate("11-10-09");
        server.setServerReviewed("No"); 
    }
    
    //This method has not been implemented yet into the system.
    public ActionForward save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //if invalid
        if (!isTokenValid(request)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        // nieuwe default actie op delete zetten
        Session sess = getHibernateSession();
        //validate and check for errors
        ActionErrors errors = dynaForm.validate(mapping, request);
        if(!errors.isEmpty()) {
            addMessages(request, errors);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, VALIDATION_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        Integer id = FormUtils.StringToInteger(dynaForm.getString("id"));
        Server server = getServer(dynaForm,request,true, id);
        
        if (null == server) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        populateServerObject(dynaForm, server);
        //store in db
        sess.saveOrUpdate(server);
        sess.flush();
        
        dynaForm.set("id", "");
        dynaForm.set("serverName", "");
        dynaForm.set("serverUrl", "");
        dynaForm.set("serverUpdatedDate", "");
        dynaForm.set("serverReviewed", "");
        
        return super.save(mapping,dynaForm,request,response);
    }
    
    public ActionForward delete(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        String [] serverSelected = dynaForm.getStrings("serverSelected");
        int size = serverSelected.length;
        
        for(int i = 0; i < size; i++) {
            //if invalid
            if (!isTokenValid(request)) {
                prepareMethod(dynaForm, request, EDIT, LIST);
                addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
                return getAlternateForward(mapping, request);
            }

            // nieuwe default actie op delete zetten
            Session sess = getHibernateSession();
            //validate and check for errors
            ActionErrors errors = dynaForm.validate(mapping, request);
            if(!errors.isEmpty()) {
                addMessages(request, errors);
                prepareMethod(dynaForm, request, EDIT, LIST);
                addAlternateMessage(mapping, request, VALIDATION_ERROR_KEY);
                return getAlternateForward(mapping, request);
            }
            
            Integer id = Integer.parseInt(serverSelected[i]);
            Server server = getServer(dynaForm,request,true, id);
            
            if (null == server) {
                prepareMethod(dynaForm, request, LIST, EDIT);
                addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
                return getAlternateForward(mapping, request);
            }

            populateServerObject(dynaForm, server);
            //store in db
            sess.delete(server);
            sess.flush();
        }
        return super.delete(mapping, dynaForm, request, response);
    }
}