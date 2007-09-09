/*
 * @(#)ImageCollector.java
 * @author N. de Goeij
 * @version 1.00, 7 september 2007
 *
 * @copyright 2007 B3Partners. All rights reserved.
 * B3Partners PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package nl.b3p.kaartenbalie.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Stack;
import nl.b3p.wms.capabilities.ElementHandler;
import nl.b3p.wms.capabilities.KBConstants;
import nl.b3p.wms.capabilities.Switcher;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * ImageCollector definition:
 */

public class ImageCollector extends Thread implements KBConstants {
    
    private static final Log log = LogFactory.getLog(ImageCollector.class);
    private static final int maxResponseTime = 100000;
    
    public static final int NEW = 0;
    public static final int ACTIVE = 1;
    public static final int COMPLETED = 2;
    public static final int ERROR = 3;
    private int status = NEW;
    private Date operationStart = null;
    private Date operationEnd = null;
    private String message = null;
    
    private BufferedImage bufferedImage;
    private String url = null;
    private static Stack stack = new Stack();
    
    public ImageCollector(String url) {
        this.url = url;
    }
    
    public void processNew() {
        status = ACTIVE;
        start();
    }
    
    public void processWaiting() throws InterruptedException {
        join(maxResponseTime);
    }
    
    public void run() {
        
        log.info("Starting download of url: " + getUrl());
        setOperationStart(new Date());
        setOperationEnd(null);
        
        // TODO hier is multi thread versie volgens mij niet nodig omdat instance maar door een thread gebruikt wordt ?
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(getUrl());
        client.getHttpConnectionManager().getParams().setConnectionTimeout(maxResponseTime);
        
        try {
            /*
             * Now we have a client connection, we first need to check if the
             * connection is ok. If the connection isn't OK we need to report this and
             * further tries to download the image will be useless.
             */
            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                throw new Exception("Error connecting to server. Status code: " + statusCode);
            }
            
            /* The connection is OK and there is data in the response. Now we only need
             * to make sure that the data does not contains an error message from the server.
             * Therefore we need to check if the response header is giving away information
             * about the data.
             */
            String mime = method.getResponseHeader("Content-Type").getValue();
            if (mime.equalsIgnoreCase(WMS_PARAM_EXCEPTION_XML)) {
                InputStream is = method.getResponseBodyAsStream();
                String body = getServiceException(is);
                throw new Exception(body);
            }
            
            log.info("Downloaded and stored image of url: " + getUrl());
            setBufferedImage(KBImageTool.readImage(method, mime));
            
            setStatus(COMPLETED);
        } catch (Exception ex) {
            log.error("error callimage collector");
            setMessage(ex.getMessage());
            setStatus(ERROR);
        } finally {
            method.releaseConnection();
        }
        
        setOperationEnd(new Date());
        return;
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
    
    public Date getOperationStart() {
        return operationStart;
    }
    
    public void setOperationStart(Date operationStart) {
        this.operationStart = operationStart;
    }
    
    public Date getOperationEnd() {
        return operationEnd;
    }
    
    public void setOperationEnd(Date operationEnd) {
        this.operationEnd = operationEnd;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    
    
    /** Constructor of the WMSCapabilitiesReader.
     *
     * @param byteStream InputStream object in which the serviceexception is stored.
     *
     * @ return String with the given exception
     *
     * @throws IOException, SAXException
     */
    // <editor-fold defaultstate="" desc="getServiceException(InputStream byteStream)">
    private static String getServiceException(InputStream byteStream) throws IOException, SAXException {
        Switcher s = new Switcher();
        s.setElementHandler("ServiceException", new ServiceExceptionHandler());
        
        XMLReader reader = org.xml.sax.helpers.XMLReaderFactory.createXMLReader();
        reader.setContentHandler(s);
        InputSource is = new InputSource(byteStream);
        is.setEncoding(CHARSET);
        reader.parse(is);
        return (String)stack.pop();
    }
    // </editor-fold>
    
    /**
     * Below is the Handler defined which reads the Exception from a ServiceException recieved when an error occurs.
     */
    // <editor-fold defaultstate="" desc="private inner class ServiceExceptionHandler">
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
    // </editor-fold>
    
}