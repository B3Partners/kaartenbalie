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
 */
package nl.b3p.kaartenbalie.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import nl.b3p.commons.xml.IgnoreEntityResolver;
import nl.b3p.commons.services.B3PCredentials;
import nl.b3p.commons.services.HttpClientConfigured;
import nl.b3p.kaartenbalie.core.server.b3pLayering.ConfigLayer;
import nl.b3p.kaartenbalie.core.server.b3pLayering.ExceptionLayer;
import nl.b3p.kaartenbalie.core.server.monitoring.ServiceProviderRequest;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.wms.capabilities.ElementHandler;
import nl.b3p.wms.capabilities.Switcher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * ImageCollector definition:
 */
public class ImageCollector extends Thread {

    private static final Log log = LogFactory.getLog(ImageCollector.class);
    private static final int maxResponseTime = new Integer(KBConfiguration.WMS_RESPONSE_TIME_LIMIT);
    public static final int NEW = 0;
    public static final int ACTIVE = 1;
    public static final int COMPLETED = 2;
    public static final int WARNING = 3;
    public static final int ERROR = 4;
    private int status = NEW;
    private String message = null;
    private BufferedImage bufferedImage;
    private static Stack stack = new Stack();
    private ServiceProviderRequest wmsRequest;
    private DataWrapper dw;
    private int index;
    private B3PCredentials credentials;

    public ImageCollector(ServiceProviderRequest wmsRequest, DataWrapper dw, int index) {
        this.wmsRequest = wmsRequest;
        this.dw = dw;
        this.index = index;
        this.setMessage("Request started ...");
        
        this.credentials    = this.wmsRequest.getCredentials();
    }

    public void processNew() throws InterruptedException {
        status = ACTIVE;
        start();
    }

    public void processWaiting() throws InterruptedException {
        join(maxResponseTime * 5);
        if (status == ACTIVE) {
            handleRequestException(new Exception("Provider did not respond in due time!"));
            getWmsRequest().setRequestResponseTime(new Long(maxResponseTime));
        }
    }    

    @Override
    public void run() {
        Date operationStart = new Date();
        Date operationEnd = null;

        String url = getWmsRequest().getProviderRequestURI();
        log.debug("Outgoing URL: " + url);

        if (url.startsWith(KBConfiguration.SERVICEPROVIDER_BASE_HTTP)) {
            try {
                //Say hello to B3P Layering!!
                setBufferedImage(ConfigLayer.handleRequest(url, dw.getLayeringParameterMap()));
                setStatus(COMPLETED);
            } catch (Exception ex) {
                handleRequestException(ex);
            }
        } else {
            try {
                HttpClientConfigured hcc = new HttpClientConfigured(credentials);
                HttpGet httpget = new HttpGet(url);
                HttpResponse response = hcc.execute(httpget);
                
                try {
                    HttpEntity entity = response.getEntity();

                    int statusCode = response.getStatusLine().getStatusCode();
                    getWmsRequest().setBytesSent(new Long(url.getBytes().length));
                    getWmsRequest().setResponseStatus(statusCode);

                    if (statusCode != 200 || entity == null) {
                        throw new Exception("Error connecting to server. HTTP status code: " + statusCode);
                    }

                    String mime = entity.getContentType().getValue();
                    if (mime.equalsIgnoreCase(OGCConstants.WMS_PARAM_EXCEPTION_XML)) {
                        InputStream is = entity.getContent();
                        throw new Exception(getServiceException(is));
                    }

                    InputStream instream = entity.getContent();
                    setBufferedImage(KBImageTool.readImage(instream, mime, getWmsRequest()));
                    
                } finally {
                    hcc.close(response);
                    hcc.close();
                }

                setMessage("");
                setStatus(COMPLETED);
                    
            } catch (Exception ex) {
                handleRequestException(ex);
            }
        }

        operationEnd = new Date();
        getWmsRequest().setRequestResponseTime(new Long(operationEnd.getTime() - operationStart.getTime()));
        return;
    }

    private void handleRequestException(Exception ex) {
        log.error("error callimage collector: ", ex);
        // check if client wants xml or image error
        OGCRequest ogcr = dw.getOgcrequest();
        if (ogcr.containsParameter(OGCConstants.WMS_PARAM_EXCEPTIONS)
                && OGCConstants.WMS_PARAM_EXCEPTION_XML.equalsIgnoreCase(ogcr.getParameter(OGCConstants.WMS_PARAM_EXCEPTIONS))) {
            // if xml handle it elsewhere
            setMessage(ex.getMessage());
            setStatus(ERROR);
        } else {
            // if image it will be added to the stack of images collected
            try {
                ExceptionLayer el = new ExceptionLayer();
                Map parameterMap = new HashMap();
                parameterMap.put("type", ex.getClass());
                parameterMap.put("message", ex.getMessage());
                parameterMap.put("stacktrace", ex.getStackTrace());
                parameterMap.put("transparant", Boolean.TRUE);
                parameterMap.put("index", new Integer(index));
                setBufferedImage(el.drawImage(ogcr, parameterMap));
                setMessage(ex.getMessage());
                setStatus(WARNING);
            } catch (Exception e) {
                log.error("error during callimage collector error handling: ", e);
                // if no image produced revert to original error message
                setMessage(e.getMessage());
                setStatus(ERROR);
            }
        }
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return getWmsRequest().getProviderRequestURI();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /** 
     *
     * @param byteStream InputStream object in which the serviceexception is stored.
     *
     * @ return String with the given exception
     *
     * @throws IOException, SAXException
     */
    private static String getServiceException(InputStream byteStream) throws IOException, SAXException {
        Switcher s = new Switcher();
        s.setElementHandler("ServiceException", new ServiceExceptionHandler());

        XMLReader reader = org.xml.sax.helpers.XMLReaderFactory.createXMLReader();

        IgnoreEntityResolver r = new IgnoreEntityResolver();
        reader.setEntityResolver(r);

        reader.setContentHandler(s);
        InputSource is = new InputSource(byteStream);
        is.setEncoding(KBConfiguration.CHARSET);
        reader.parse(is);
        return (String) stack.pop();
    }

    /**
     * @return the wmsRequest
     */
    public ServiceProviderRequest getWmsRequest() {
        return wmsRequest;
    }

    /**
     * Below is the Handler defined which reads the Exception from a ServiceException recieved when an error occurs.
     */
    private static class ServiceExceptionHandler extends ElementHandler {

        StringBuffer sb;

        public void startElement(String uri, String localName, String qName, Attributes atts) {
            sb = new StringBuffer();
        }

        public void characters(char[] chars, int start, int len) {
            sb.append(chars, start, len);
        }

        public void endElement(String uri, String localName, String qName) {
            stack.push(sb.toString());
        }
    }
}
