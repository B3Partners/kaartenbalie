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
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import org.junit.*;
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author rachelle
 */
public class DataMonitoringTest extends KaartenbalieTestCase {

    private DataMonitoring instance;
    private long operationStartTime;

    public DataMonitoringTest(String name) {
        super(name);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        instance = new DataMonitoring();
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        instance = null;
    }

    /**
     * Test of setEnableMonitoring method, of class DataMonitoring.
     */
    @Test
    public void testSetEnableMonitoring() {
        boolean state = true;
        DataMonitoring.setEnableMonitoring(state);
        assertTrue(DataMonitoring.isEnableMonitoring());
    }

    /**
     * Test of setUserAndOrganization method, of class DataMonitoring.
     */
    @Test
    public void testSetUserAndOrganization() {
        User user = this.generateUser();
        Organization organization = this.generateOrganization();
        instance.setUserAndOrganization(user, organization);
    }

    /**
     * Test of startClientRequest method, of class DataMonitoring.
     */
    @Test
    public void testStartClientRequest() {
        try {
            this.startClientRequest();
        } catch (Exception e) {
            fail("Exception : " + e.getLocalizedMessage());
        }
    }

    /**
     * Test of getMSSinceStart method, of class DataMonitoring.
     */
    @Test
    public void testGetMSSinceStart() {
        try {
            this.startClientRequest();

            long expected = System.currentTimeMillis() - operationStartTime;

            assertEquals(expected, instance.getMSSinceStart(), 200);
        } catch (Exception e) {
            fail("Exception : " + e.getLocalizedMessage());
        }
    }

    /**
     * Test of addServiceProviderRequest method, of class DataMonitoring.
     */
    @Test
    public void testAddServiceProviderRequest() {
        try {
            this.startClientRequest();
            ServiceProviderRequest s = new ServiceProviderRequest();
            instance.addServiceProviderRequest(s);
        } catch (Exception e) {
            fail("Exception : " + e.getLocalizedMessage());
        }
    }

    /**
     * Test of addRequestOperation method, of class DataMonitoring.
     */
    @Test
    public void testAddRequestOperation() {
        try {
            this.startClientRequest();
            Operation o = new Operation();
            instance.addRequestOperation(o);
        } catch (Exception e) {
            fail("Exception : " + e.getLocalizedMessage());
        }
    }

    /**
     * Test of setClientRequestException method, of class DataMonitoring.
     */
    @Test
    public void testSetClientRequestException() {
        try {
            this.startClientRequest();
            instance.setClientRequestException(new Exception());
        } catch (Exception e) {
            fail("Exception : " + e.getLocalizedMessage());
        }
    }

    /**
     * Test of endClientRequest method, of class DataMonitoring.
     */
    @Test
    public void testEndClientRequest() {
        try {
            this.startClientRequest();
            instance.endClientRequest("service1", "GET", 100, 10);
        } catch (Exception e) {
            fail("Exception : " + e.getLocalizedMessage());
        }
    }

    /**
     * Test of isEnableMonitoring method, of class DataMonitoring.
     */
    @Test
    public void testIsEnableMonitoring() {
        assertFalse(DataMonitoring.isEnableMonitoring());
    }

    private void startClientRequest() {
        String clientRequestURI = "index.jsp";
        int bytesReceivedFromUser = 0;
        this.operationStartTime = 43L;
        String clientIp = "127.0.0.1";
        String method = "GET";
        instance.startClientRequest(clientRequestURI, bytesReceivedFromUser, operationStartTime, clientIp, method);
    }
}
