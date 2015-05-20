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
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.ServletConfig;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.monitoring.DataMonitoring;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.servletTestAPI.ConfigStub;
import nl.b3p.servletTestAPI.HttpServletRequestStub;
import nl.b3p.servletTestAPI.HttpServletResponseStub;
import org.junit.Test;

public class CallWMSServletTest extends KaartenbalieTestCase {

    private ServletConfig configStumb;
    private HttpServletRequestStub requestStumb;
    private HttpServletResponseStub responseStumb;
    private User user;
    private CallWMSServlet servlet;

    public CallWMSServletTest(String name) {
        super(name);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.user = this.generateUser();
        this.configStumb = new ConfigStub();
        this.requestStumb = new HttpServletRequestStub();
        this.responseStumb = new HttpServletResponseStub();

        servlet = new CallWMSServlet();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        this.user = null;
        this.configStumb = null;
        this.requestStumb = null;
        this.responseStumb = null;
        this.servlet = null;
    }

    /**
     * Test of init method, of class CallWMSServlet.
     */
    @Test
    public void testCallWMSServlet_Init() {
        try {
            this.servlet.init(this.configStumb);

            assertTrue(true);
        } catch (Exception e) {
            fail("Init failed " + e.getLocalizedMessage());

            assertTrue(false);
        }
    }

    /**
     * Test of createBaseUrl method, of class CallWMSServlet.
     */
    @Test
    public void testCallWMSServlet_CreateBaseUrl() {
        StringBuffer baseUrl = CallWMSServlet.createBaseUrl(this.requestStumb);

        assertStringEquals(baseUrl.toString(), this.requestStumb.getServletPath());
    }

    /**
     * Test of parseRequestAndData method, of class CallWMSServlet.
     * Proxy
     */
    public void testCallWMSServlet_ParseRequestAndData_Proxy() {
        /*
         * Proxy
         */
        try {
            DataWrapper data = this.getWrapper();

            OGCRequest ogRequest = this.getOGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.PROXY_URL + "=http://localhost:8000&" + OGCConstants.SERVICE + "=" + OGCConstants.NONOGC_SERVICE_PROXY);
            data.setOgcrequest(ogRequest);

            this.servlet.parseRequestAndData(data, user);
            
            fail("Function should throw a IllegalBlockSizeException exception");
        } catch (IllegalBlockSizeException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception " + e.getLocalizedMessage());
        }
    }

    /**
     * Test of parseRequestAndData method, of class CallWMSServlet.
     * Meta data
     */
    @Test
    public void testCallWMSServlet_ParseRequestAndData_MetaData() {
        /*
         * Meta data
         */
        try {
            DataWrapper data = this.getWrapper();

            OGCRequest ogRequest = this.getOGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.PROXY_URL + "=http://localhost:8000&" + OGCConstants.SERVICE + "=" + OGCConstants.NONOGC_SERVICE_METADATA + "&" + OGCConstants.METADATA_LAYER + "=" + layerName);
            data.setOgcrequest(ogRequest);

            this.servlet.parseRequestAndData(data, user);
        } catch (Exception e) {
            if (e.getLocalizedMessage().contains("Layer not found with name: " + layerName)) {
                assertTrue(true);
            } else {
                fail("Exception " + e.getLocalizedMessage());
            }
        }
    }

    /**
     * Test of parseRequestAndData method, of class CallWMSServlet.
     * WMS
     */
    @Test
    public void testCallWMSServlet_ParseRequestAndData_WMS() {
        /*
         * WMS
         */
        try {
            DataWrapper data = this.getWrapper();

            OGCRequest ogRequest = this.getOGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.REQUEST + "=" + OGCConstants.WMS_REQUEST_GetCapabilities + "&" + OGCConstants.SERVICE + "=" + OGCConstants.NONOGC_SERVICE_METADATA + "&" + OGCConstants.METADATA_LAYER + "=" + layerName);
            data.setOgcrequest(ogRequest);

            this.servlet.parseRequestAndData(data, user);

            fail("Function should throw a layer not found exception");
        } catch (Exception e) {
            if (e.getLocalizedMessage().contains("Layer not found with name: " + layerName)) {
                assertTrue(true);
            } else {
                fail("Exception " + e.getLocalizedMessage());
            }
        }
    }

    /**
     * Test of parseRequestAndData method, of class CallWMSServlet.
     * WFS
     */
    @Test
    public void testCallWMSServlet_ParseRequestAndData_WFS() {
        /*
         * WFS
         */
        try {
            DataWrapper data = this.getWrapper();

            OGCRequest ogRequest = this.getOGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.REQUEST + "=" + OGCConstants.WFS_REQUEST_GetCapabilities + "&" + OGCConstants.SERVICE + "=" + OGCConstants.NONOGC_SERVICE_METADATA + "&" + OGCConstants.METADATA_LAYER + "=" + layerName);
            data.setOgcrequest(ogRequest);

            this.servlet.parseRequestAndData(data, user);

            fail("Function should throw a layer not found exception");
        } catch (Exception e) {
            if (e.getLocalizedMessage().contains("Layer not found with name: " + layerName)) {
                assertTrue(true);
            } else {
                fail("Exception " + e.getLocalizedMessage());
            }
        }
    }

    /**
     * Test of parseRequestAndData method, of class CallWMSServlet.
     * WMS Get map
     */
    @Test
    public void testCallWMSServlet_ParseRequestAndData_WMS_GetMap() {
        /*
         * WMS GetMap
         */
        try {
            DataWrapper data = this.getWrapper();

            OGCRequest ogRequest = this.getOGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.REQUEST + "=" + OGCConstants.WMS_REQUEST_GetMap + "&" + OGCConstants.WMS_PARAM_LAYERS + "=" + this.layerName);
            data.setOgcrequest(ogRequest);

            this.servlet.parseRequestAndData(data, user);

            fail("Function should throw a not mapped exception");
        } catch (Exception e) {
            this.checkNotMapped(e);
        }
    }

    /**
     * Test of parseRequestAndData method, of class CallWMSServlet.
     * WMS Get legend graphics
     */
    @Test
    public void testCallWMSServlet_ParseRequestAndData_WMS_GetLegendGraphic() {
        /*
         * WMS GetMap
         */
        try {
            DataWrapper data = this.getWrapper();

            OGCRequest ogRequest = this.getOGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.REQUEST + "="
                    + OGCConstants.WMS_REQUEST_GetLegendGraphic + "&"
                    + OGCConstants.WMS_PARAM_LAYER + "=" + this.layerName);
            data.setOgcrequest(ogRequest);

            this.servlet.parseRequestAndData(data, user);

            fail("Function should throw a not mapped exception");
        } catch (Exception e) {
            this.checkNotMapped(e);
        }
    }

    /**
     * Test of parseRequestAndData method, of class CallWMSServlet.
     * WMS Describe layer
     */
    @Test
    public void testCallWMSServlet_ParseRequestAndData_WMS_DescribeLayer() {
        /*
         * WMS DescribeLayer
         */
        try {
            DataWrapper data = this.getWrapper();

            OGCRequest ogRequest = this.getOGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.REQUEST + "=" + OGCConstants.WMS_REQUEST_DescribeLayer + "&" + OGCConstants.WMS_PARAM_LAYERS + "=" + this.layerNames);
            data.setOgcrequest(ogRequest);

            this.servlet.parseRequestAndData(data, this.user);

            fail("Function should throw a not mapped exception");
        } catch (Exception e) {
            this.checkNotMapped(e);
        }
    }

    /**
     * Test of parseRequestAndData method, of class CallWMSServlet.
     * WMS describeFeatureType
     */
    @Test
    public void testCallWMSServlet_ParseRequestAndData_WMS_DescribeFeatureType() {
        /*
         * WMS DescribeFeatureType
         */
        try {
            DataWrapper data = this.getWrapper();

            OGCRequest ogRequest = this.getOGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.REQUEST + "=" + OGCConstants.WFS_REQUEST_DescribeFeatureType + "&" + OGCConstants.WFS_PARAM_TYPENAME + "=" + this.layerNames);

            data.setOgcrequest(ogRequest);

            this.servlet.parseRequestAndData(data, user);

            fail("Function should throw a not mapped exception");
        } catch (Exception e) {
            this.checkNotMapped(e);
        }
    }

    /**
     * Test of parseRequestAndData method, of class CallWMSServlet.
     * WMS getFeature
     */
    @Test
    public void testCallWMSServlet_ParseRequestAndData_WMS_GetFeature() {
        /*
         * WMS GetFeature
         */
        try {
            DataWrapper data = this.getWrapper();

            OGCRequest ogRequest = this.getOGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.REQUEST + "=" + OGCConstants.WFS_REQUEST_GetFeature + "&" + OGCConstants.WFS_PARAM_TYPENAME + "=" + this.layerNames);
            ogRequest.setHttpMethod("GET");
            data.setOgcrequest(ogRequest);


            this.servlet.parseRequestAndData(data, user);

            assertTrue(false);
        } catch (Exception e) {
            this.checkNotMapped(e);
        }
    }

     /**
     * Test of parseRequestAndData method, of class CallWMSServlet.
     * WMS Transaction
     */
    @Test
    public void testCallWMSServlet_ParseRequestAndData_WMS_Transaction() {
        /*
         * WMS Transaction
         */
        try {
            DataWrapper data = this.getWrapper();

            OGCRequest ogRequest = this.getOGCRequest();
            ogRequest.addOrReplaceParameters(OGCConstants.REQUEST + "=" + OGCConstants.WFS_REQUEST_Transaction + "&" + OGCConstants.WFS_PARAM_OPERATION + "=true");
            //ogRequest.addElementToTransactionList("polygon", "testwaarde");
            data.setOgcrequest(ogRequest);

            this.servlet.parseRequestAndData(data, user);

            fail("Function should go a UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            /*
             * Expected Don't want the database to be changed
             */
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception Expected UnsupportedOperationException. Check on transactionlist incorrect?");
        }
    }

    /**
     * Test of getServletInfo method, of class CallWMSServlet.
     */
    @Test
    public void testCallWMSServlet_GetServletInfo() {
        assertEquals(this.servlet.getServletInfo(), "CallWMSServlet info");
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
     * Generates the OGCRequest
     * 
     * @return The OGCRequest
     */
    private OGCRequest getOGCRequest() {
        OGCRequest ogRequest = new OGCRequest();
        ogRequest.fixHttpHost(this.requestStumb.getServerName());

        return ogRequest;
    }
}
