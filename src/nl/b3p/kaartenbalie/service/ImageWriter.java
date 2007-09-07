/*
 * @(#)ImageWriter.java
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import nl.b3p.wms.capabilities.ElementHandler;
import nl.b3p.wms.capabilities.KBConstants;
import nl.b3p.wms.capabilities.Switcher;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * ImageWriter definition:
 *
 */

public class ImageWriter extends Thread implements KBConstants {
    
    private static final Log log = LogFactory.getLog(ImageWriter.class);
    private static final int maxResponseTime = 100000;
    
    private ImageManager imagemanager;
    private BufferedImage bufferedImage;
    private ArrayList urls;
    private static Stack stack = new Stack();

    public ImageWriter(ImageManager imagemanager, BufferedImage bufferedImage, ArrayList urls) {
        this.imagemanager = imagemanager;
        this.bufferedImage = bufferedImage;
        this.urls = urls;
    }

    public void run() {
        //Hier moet een nieuwe HttpClient opgezet worden
        //En een nieuwe download starten......
        //dit moet in een while loop komen te staan waarbij geitereerd wordt over de urls....
        Iterator urlIterator = urls.iterator();
        while (urlIterator.hasNext()) {
            String url = (String) urlIterator.next();
            
            try {
                /*
                 * Create a new HttpClient and a new GetMethod which can be used to
                 * download the images from the specified URL.
                 */
                log.info("Starting download of url: " + url);
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
                String mime = method.getResponseHeader("Content-Type").getValue();
                if (mime.equalsIgnoreCase(WMS_PARAM_EXCEPTION_XML)) {
                    InputStream is = method.getResponseBodyAsStream();
                    String body = getServiceException(is);
                    throw new Exception(body);            
                }

                /*
                 * All checks have been made and the data can be downloaded from the server.
                 */
                imagemanager.put(KBImageTool.readImage(method, mime));
                log.info("Downloaded and stored image of url: " + url);
                //BufferedImage bi = KBImageTool.readImage(method, mime);
                //setImage(bi);
                method.releaseConnection();
            } catch (Exception ex) {
                log.error("Unexpected error occured at line 138: ", ex);
                //throw new Exception(ex);
            }        
        }
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