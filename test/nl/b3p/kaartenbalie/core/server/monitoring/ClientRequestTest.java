/*
 * B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
 * for authentication/authorization, pricing and usage reporting.
 *
 * Copyright 2012,2013,2014 B3Partners BV
 * 
 * author : Rachelle Scheijen
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
package nl.b3p.kaartenbalie.core.server.monitoring;

import general.KaartenbalieTestCase;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.accounting.NoPrizingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author rachelle
 */
public class ClientRequestTest extends KaartenbalieTestCase {
    private ClientRequest instance;
    
    public ClientRequestTest(String name) {
        super(name);
    }
    
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        instance    = new ClientRequest();
    }
    
    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        
        instance    = null;
    }

    /**
     * Test of getId method, of class ClientRequest.
     */
    @Test
    public void testGetId() {
        assertNull(instance.getId());
    }

    /**
     * Test of setId method, of class ClientRequest.
     */
    @Test
    public void testSetId() {
        Integer id = new Integer(5);
        instance.setId(id);
        assertEquals(id,instance.getId());
    }

    /**
     * Test of getTimestamp method, of class ClientRequest.
     */
    @Test
    public void testGetTimestamp() {
        assertNotNull(instance.getTimestamp());
    }

    /**
     * Test of setTimestamp method, of class ClientRequest.
     */
    @Test
    public void testSetTimestamp() {
        Date timestamp = this.generateDate();
        instance.setTimestamp(timestamp);
        assertEquals(timestamp,instance.getTimestamp());
    }

    /**
     * Test of getServiceProviderRequests method, of class ClientRequest.
     */
    @Test
    public void testGetServiceProviderRequests() {
        Set expResult = new HashSet();
        assertEquals(expResult, instance.getServiceProviderRequests());
    }

    /**
     * Test of setServiceProviderRequests method, of class ClientRequest.
     */
    @Test
    public void testSetServiceProviderRequests() {
        Set serviceProviderRequests = new HashSet();
        serviceProviderRequests.add("Request 1");
        
        instance.setServiceProviderRequests(serviceProviderRequests);
        assertEquals(serviceProviderRequests,instance.getServiceProviderRequests());
    }

    /**
     * Test of getClientRequestURI method, of class ClientRequest.
     */
    @Test
    public void testGetClientRequestURI() {
        assertNull(instance.getClientRequestURI());
    }

    /**
     * Test of setClientRequestURI method, of class ClientRequest.
     */
    @Test
    public void testSetClientRequestURI() {
        String clientRequestURI = "setClientRequestURI";
        instance.setClientRequestURI(clientRequestURI);
        assertStringEquals(clientRequestURI,instance.getClientRequestURI());
    }

    /**
     * Test of getRequestOperations method, of class ClientRequest.
     */
    @Test
    public void testGetRequestOperations() {
        Set expResult = new HashSet();
        assertEquals(expResult,instance.getRequestOperations());
    }

    /**
     * Test of setRequestOperations method, of class ClientRequest.
     */
    @Test
    public void testSetRequestOperations() {
        Set requestOperations = new HashSet();
        requestOperations.add("request operation");
        
        instance.setRequestOperations(requestOperations);
        assertEquals(requestOperations,instance.getRequestOperations());
    }

    /**
     * Test of setUser method, of class ClientRequest.
     */
    @Test
    public void testSetUser() {
        User user   = this.generateUser();
        
        instance.setUser(user);
    }

    /**
     * Test of getUser method, of class ClientRequest.
     */
    @Test
    public void testGetUser() {
        User user   = this.generateUser();
        
        instance.setUser(user);
        assertEquals(user.getId(),instance.getUser(this.entityManager).getId());
    }

    /**
     * Test of setOrganization method, of class ClientRequest.
     */
    @Test
    public void testSetOrganization() {
        Organization organization   = this.generateOrganization();        
        instance.setOrganization(organization);
    }

    /**
     * Test of getOrganization method, of class ClientRequest.
     */
    @Test
    public void testGetOrganization() {
        Organization organization   = this.generateOrganization();        
        instance.setOrganization(organization);
        assertEquals(organization.getId(),instance.getOrganization(entityManager).getId());
    }

    /**
     * Test of getClientIp method, of class ClientRequest.
     */
    @Test
    public void testGetClientIp() {
        assertNull(instance.getClientIp());
    }

    /**
     * Test of setClientIp method, of class ClientRequest.
     */
    @Test
    public void testSetClientIp() {
        String clientIp = "setClientIp";
        instance.setClientIp(clientIp);
        assertStringEquals(clientIp,instance.getClientIp());
    }

    /**
     * Test of getMethod method, of class ClientRequest.
     */
    @Test
    public void testGetMethod() {
        assertNull(instance.getMethod());
    }

    /**
     * Test of setMethod method, of class ClientRequest.
     */
    @Test
    public void testSetMethod() {
        String method = "GET";
        instance.setMethod(method);
        assertStringEquals(method,instance.getMethod());
    }

    /**
     * Test of getService method, of class ClientRequest.
     */
    @Test
    public void testGetService() {
        assertNull(instance.getService());
    }

    /**
     * Test of setService method, of class ClientRequest.
     */
    @Test
    public void testSetService() {
        String service = "setService";
        instance.setService(service);
        assertStringEquals(service,instance.getService());
    }

    /**
     * Test of getOperation method, of class ClientRequest.
     */
    @Test
    public void testGetOperation() {
        assertNull(instance.getOperation());
    }

    /**
     * Test of setOperation method, of class ClientRequest.
     */
    @Test
    public void testSetOperation() {
        String operation = "setOperation";
        instance.setOperation(operation);
        assertStringEquals(operation,instance.getOperation());
    }

    /**
     * Test of getExceptionClass method, of class ClientRequest.
     */
    @Test
    public void testGetExceptionClass() {
        assertNull(instance.getExceptionClass());
    }

    /**
     * Test of setExceptionClass method, of class ClientRequest.
     */
    @Test
    public void testSetExceptionClass() {
        instance.setExceptionClass(NoPrizingException.class);
        assertEquals(NoPrizingException.class,instance.getExceptionClass());
    }

    /**
     * Test of getExceptionMessage method, of class ClientRequest.
     */
    @Test
    public void testGetExceptionMessage() {
        assertNull(instance.getExceptionMessage());
    }

    /**
     * Test of setExceptionMessage method, of class ClientRequest.
     */
    @Test
    public void testSetExceptionMessage() {
        String exceptionMessage = "404 Not Found";
        instance.setExceptionMessage(exceptionMessage);
        assertStringEquals(exceptionMessage,instance.getExceptionMessage());
    }
}
