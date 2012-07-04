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
package nl.b3p.kaartenbalie.service.servlet;

import general.KaartenbalieTestCase;
import java.io.IOException;
import javax.servlet.ServletConfig;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.monitoring.DataMonitoring;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.ogc.utils.OGCScriptingRequest;
import nl.b3p.servletAPI.ConfigStub;
import nl.b3p.servletAPI.HttpServletRequestStub;
import nl.b3p.servletAPI.HttpServletResponseStub;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author rachelle
 */
public class CallScriptingServletTest extends KaartenbalieTestCase {
    private ServletConfig configStumb;
    private HttpServletRequestStub requestStumb;
    private HttpServletResponseStub responseStumb;
    private User user;
    private CallScriptingServlet instance;
    
    public CallScriptingServletTest(String name) {
        super(name);
    }
    
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        this.user = this.generateUser();
        this.configStumb = new ConfigStub();
        this.requestStumb = new HttpServletRequestStub();
        this.responseStumb = new HttpServletResponseStub();
        instance    = new CallScriptingServlet();
    }
    
    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        
        instance    = null;
        this.user   = null;
        this.configStumb    = null;
        this.requestStumb   = null;
        this.responseStumb  = null;
    }

    /**
     * Test of init method, of class CallScriptingServlet.
     */
    @Test
    public void testInit(){
        try {
            this.instance.init(this.configStumb);
            
            assertTrue(true);
        } catch (Exception e) {
            fail("Init failed " + e.getLocalizedMessage());
            
            assertTrue(false);
        }
    }
    
    /**
     * Test of parseRequestAndData method, of class CallScriptingServlet.
     * Batch update
     */
    @Test
    public void testParseRequestAndData_Update(){
        try {
            this.requestStumb.setClientAddress("127.0.0.1");
            DataWrapper data = this.getWrapper();
            OGCRequest ogRequest = this.getOGCRequest();
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.COMMAND,OGCScriptingRequest.UPDATE_SERVICES);
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.SERVICE_TYPE,"WMS");
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.SERVICE,"");
            data.setOgcrequest(ogRequest);
            
            instance.parseRequestAndData(data, user);
        }
        catch(Exception e){
            fail("Exception : "+e.getLocalizedMessage());
        }
        
        try {
            this.requestStumb.setClientAddress("127.0.0.1");
            DataWrapper data = this.getWrapper();
            OGCRequest ogRequest = this.getOGCRequest();
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.COMMAND,OGCScriptingRequest.UPDATE_SERVICES);
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.SERVICE_TYPE,"WFS");
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.SERVICE,"");
            data.setOgcrequest(ogRequest);
            
            instance.parseRequestAndData(data, user);
        }
        catch(Exception e){
            fail("Exception : "+e.getLocalizedMessage());
        }
    }
    
    /**
     * Test of parseRequestAndData method, of class CallScriptingServlet.
     * Adding a service
     */
    @Test
    public void testParseRequestAndData_Add(){
        try {
            this.requestStumb.setClientAddress("127.0.0.1");
            DataWrapper data = this.getWrapper();
            OGCRequest ogRequest = this.getOGCRequest();
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.COMMAND,OGCScriptingRequest.ADD_SERVICE);
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.SERVICE_TYPE,"WMS");
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.NAME,"openwion");
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.ABBR,"ow");
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.URL,sourceURI);
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.SLD,sourceURI);
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.UPDATE,"true");
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.GROUPS,"beheerders");
            data.setOgcrequest(ogRequest);
            
            instance.parseRequestAndData(data, user);
        }
        catch(Exception e){
            fail("Exception : "+e.getLocalizedMessage());
        }
        
        try {
            this.requestStumb.setClientAddress("127.0.0.1");
            DataWrapper data = this.getWrapper();
            OGCRequest ogRequest = this.getOGCRequest();
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.COMMAND,OGCScriptingRequest.ADD_SERVICE);
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.SERVICE_TYPE,"WMS");
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.NAME,"openwion");
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.ABBR,"ow");
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.URL,sourceURI);
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.SLD,sourceURI);
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.UPDATE,"true");
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.GROUPS,"beheerders");
            data.setOgcrequest(ogRequest);
            
            instance.parseRequestAndData(data, user);
        }
        catch(Exception e){
            fail("Exception : "+e.getLocalizedMessage());
        }
    }
    
    /**
     * Test of parseRequestAndData method, of class CallScriptingServlet.
     * Adding a allowed service
     */
    @Test
    public void testParseRequestAndData_Add_allowed_services(){
        try {
            this.requestStumb.setClientAddress("127.0.0.1");
            DataWrapper data = this.getWrapper();
            OGCRequest ogRequest = this.getOGCRequest();
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.COMMAND,OGCScriptingRequest.ADD_ALLOWED_SERVICES);
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.SERVICE_TYPE,"WMS");
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.URL,sourceURI);
            data.setOgcrequest(ogRequest);
            
            instance.parseRequestAndData(data, user);
        }
        catch(Exception e){
            fail("Exception : "+e.getLocalizedMessage());
        }
        
        try {
            this.requestStumb.setClientAddress("127.0.0.1");
            DataWrapper data = this.getWrapper();
            OGCRequest ogRequest = this.getOGCRequest();
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.COMMAND,OGCScriptingRequest.ADD_ALLOWED_SERVICES);
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.SERVICE_TYPE,"WMS");
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.URL,sourceURI);
            data.setOgcrequest(ogRequest);
            
            instance.parseRequestAndData(data, user);
        }
        catch(Exception e){
            fail("Exception : "+e.getLocalizedMessage());
        }
    }
    
    /**
     * Test of parseRequestAndData method, of class CallScriptingServlet.
     * Deleting a allowed service
     */
    @Test
    public void testParseRequestAndData_delete_allowed_services(){
        try {
            this.requestStumb.setClientAddress("127.0.0.1");
            DataWrapper data = this.getWrapper();
            OGCRequest ogRequest = this.getOGCRequest();
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.COMMAND,OGCScriptingRequest.DELETE_ALLOWED_SERVICES);
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.SERVICE_TYPE,"WMS");
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.URL,sourceURI);
            data.setOgcrequest(ogRequest);
            
            instance.parseRequestAndData(data, user);
        }
        catch(Exception e){
            fail("Exception : "+e.getLocalizedMessage());
        }
        
        try {
            this.requestStumb.setClientAddress("127.0.0.1");
            DataWrapper data = this.getWrapper();
            OGCRequest ogRequest = this.getOGCRequest();
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.COMMAND,OGCScriptingRequest.DELETE_ALLOWED_SERVICES);
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.SERVICE_TYPE,"WMS");
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.URL,sourceURI);
            data.setOgcrequest(ogRequest);
            
            instance.parseRequestAndData(data, user);
        }
        catch(Exception e){
            fail("Exception : "+e.getLocalizedMessage());
        }
    }
    
    /**
     * Test of parseRequestAndData method, of class CallScriptingServlet.
     * Deleting all allowed services
     */
    @Test
    public void testParseRequestAndData_delete_all_allowed_services(){
        try {
            this.requestStumb.setClientAddress("127.0.0.1");
            DataWrapper data = this.getWrapper();
            OGCRequest ogRequest = this.getOGCRequest();
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.COMMAND,OGCScriptingRequest.DELETE_ALL_ALLOWED_SERVICES);
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.SERVICE_TYPE,"WMS");
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.URL,sourceURI);
            data.setOgcrequest(ogRequest);
            
            instance.parseRequestAndData(data, user);
        }
        catch(Exception e){
            fail("Exception : "+e.getLocalizedMessage());
        }
        
        try {
            this.requestStumb.setClientAddress("127.0.0.1");
            DataWrapper data = this.getWrapper();
            OGCRequest ogRequest = this.getOGCRequest();
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.COMMAND,OGCScriptingRequest.DELETE_ALL_ALLOWED_SERVICES);
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.SERVICE_TYPE,"WMS");
            ogRequest.addOrReplaceParameter(OGCScriptingRequest.URL,sourceURI);
            data.setOgcrequest(ogRequest);
            
            instance.parseRequestAndData(data, user);
        }
        catch(Exception e){
            fail("Exception : "+e.getLocalizedMessage());
        }
    }

    /**
     * Test of getServletInfo method, of class CallScriptingServlet.
     */
    @Test
    public void testGetServletInfo() {
        String expResult = "CallScriptingServlet info";
        String result = instance.getServletInfo();
        assertEquals(expResult, result);
    }
    
    /**
     * Generates the DataWrapper
     * 
     * @return  The DataWrapper
     * @throws IOException 
     */
    private DataWrapper getWrapper() throws IOException {
        DataWrapper data = new DataWrapper(this.requestStumb, this.responseStumb);
        
        DataMonitoring monitoring = new DataMonitoring();
        data.setRequestReporting(monitoring);
        
        return data;
    }
    
    /**
     * Generates the OGCScriptingRequest
     * 
     * @return The OGCScriptingRequest
     */
    private OGCScriptingRequest getOGCRequest() {
        OGCScriptingRequest ogRequest = new OGCScriptingRequest();
        ogRequest.setHttpHost(this.requestStumb.getServerName());
        
        return ogRequest;
    }
}
