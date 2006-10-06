/*
 * Server.java
 *
 * Created on 13 september 2006, 14:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core;

import nl.b3p.kaartenbalie.core.server.*;

//import javax.servlet.http.*;
//import org.apache.struts.action.*;

/**
 *
 * @author Nando De Goeij
 */
public class Server {// extends ActionForm {
    private long id;
    private Service service;
    private Capability capability;
    private String serverServ;
    private String serverCapa;
    private String serverName;
    private String serverUrl;
    private String serverUpdatedDate;
    private String serverReviewed;
    
    //Four temporary setters/getters
    public String getServerServ() {
        return serverServ;
    }

    public void setServerServ(String serverServ) {
        this.serverServ = serverServ;
    }

    public String getServerCapa() {
        return serverCapa;
    }

    public void setServerCapa(String serverCapa) {
        this.serverCapa = serverCapa;
    }
    //End temporary--------------------------------------------------------
    /** Creates a new instance of WMSBean */
    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Capability getCapability() {
        return capability;
    }

    public void setCapability(Capability capability) {
        this.capability = capability;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getServerUpdatedDate() {
        return serverUpdatedDate;
    }

    public void setServerUpdatedDate(String serverUpdatedDate) {
        this.serverUpdatedDate = serverUpdatedDate;
    }

    public String getServerReviewed() {
        return serverReviewed;
    }

    public void setServerReviewed(String serverReviewed) {
        this.serverReviewed = serverReviewed;
    }
}