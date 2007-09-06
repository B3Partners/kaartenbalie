/*
 * @(#)ThreadedImageReader.java
 * @author N. de Goeij
 * @version 1.00, 30 augustus 2007
 *
 * @copyright 2007 B3Partners. All rights reserved.
 * B3Partners PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package nl.b3p.kaartenbalie.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Stack;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import nl.b3p.wms.capabilities.KBConstants;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import nl.b3p.wms.capabilities.ElementHandler;
import nl.b3p.wms.capabilities.Switcher;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;

/**
 * ThreadedImageReader definition:
 *
 */

public class ThreadedImageReader implements KBConstants {
    private static final Log log = LogFactory.getLog(ThreadedImageReader.class);
    
    private GetMethod method;
    private String mime;
    private XMLReader parser;
    private Switcher s;
    private String url;
    private ArrayList urls;
    
    private static ArrayList images;
    private static long maxResponseTime;
    private static Stack stack;
    
    private static ReentrantLock [] downloads;
    private Semaphore sem;
    
    public ThreadedImageReader(ArrayList urls) {
        this.urls = urls;
        
        images = new ArrayList();
        maxResponseTime = 100000;
        stack = new Stack();
        
        Iterator it = urls.iterator();
        while (it.hasNext()) {
            String url = (String)it.next();
            try {
                addURL(url);
            } catch (Exception ex) {
                log.error("Error occured during adding a download URL for an image: ", ex);
            }
        }
    }
    
    public void addURL(String url) throws IOException, Exception {
        /*
         * First check if the URL is a valid URL.
         */
        if(!verifyURL(url)) {
            throw new Exception("Invalid URL given.");
        }
        
        /*
         * Create a new HttpClient and a new GetMethod which can be used to
         * download the images from the specified URL.
         */
        MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        HttpClient client = new HttpClient(connectionManager);
        
        GetMethod method = new GetMethod(url);
        client.getHttpConnectionManager().getParams().setConnectionTimeout((int)maxResponseTime);
        
        /*
         * Now we have a client connection, we first need to check if the 
         * connection is ok. If the connection isn't OK we need to report this and
         * further tries to download the image will be useless.
         */
        int statusCode = 0;
        try {
            statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                throw new Exception("Error connecting to server. Status code: " + statusCode);
            }
        } catch (IOException ex) {
            throw new IOException("Unexpected exception occured: " + ex.getMessage());
        }
        /*
        if (method.getResponseContentLength() < 1) {
            throw new Exception("Error in content length. Length is 0.");
        }
        
        
         * The connection is OK and there is data in the response. Now we only need 
         * to make sure that the data does not contains an error message from the server.
         * Therefore we need to check if the response header is giving away information
         * about the data.
         */
        String contentType = method.getResponseHeader("Content-Type").getValue();
        if (contentType.equalsIgnoreCase(WMS_PARAM_EXCEPTION_XML)) {
            InputStream is = method.getResponseBodyAsStream();
            String body = getServiceException(is);
            throw new Exception(body);            
        }
        this.mime = contentType;
        
        /*
         * All checks have been made and the data can be downloaded from the server.
         */        
        Downloader downloader = new Downloader(method, contentType);
        addImage(downloader.getImage(), url);
        method.releaseConnection();
    }
    
    private void addImage(BufferedImage bufferedImage, String url) {
        images.add(bufferedImage);
    }
    
    public void sendCombinedImages(DataWrapper dw) {
        //Kunnen we hier een check inbouwen die controleert of alle plaatjes al binnen zijn?
        //TODO: delete the code below.
        //Temporary information, can be deleted afterwards.
        long time = System.currentTimeMillis();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
        Date date = new Date(time);
        String beginwaittime = df.format(date);
        log.info("Asking for combined images: " + beginwaittime);
        //---------------------------------------------------------------
        
        
        while (images.size() != urls.size()) {}
        
        BufferedImage [] allImages = new BufferedImage[images.size()];
        int counter = 0;
        Iterator it = images.iterator();
        while (it.hasNext()) {
            BufferedImage bf = (BufferedImage) it.next();
            allImages[counter] = bf;
            counter++;
        }
        try {
            
            //TODO: delete the code below.
            //Temporary information, can be deleted afterwards.
            time = System.currentTimeMillis();
            df = new SimpleDateFormat("HH:mm:ss.SSS");
            date = new Date(time);
            String endwaittime = df.format(date);
            log.info("Got through for combined images: " + endwaittime);
            //---------------------------------------------------------------
            
            KBImageTool kbi = new KBImageTool();
            kbi.writeImage(allImages, mime, dw);
        } catch (Exception ex) {
            log.error(ex);
        }
    }
    
    private boolean verifyURL(String url) {
        if (!url.toLowerCase().startsWith("http://"))
            return false;
        
        URL verifiedUrl = null;
        try {
            verifiedUrl = new URL(url);
        } catch (Exception e) {
            return false;
        }        
        return true;
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
