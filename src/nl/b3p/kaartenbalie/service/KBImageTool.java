/*
 * @(#)KBImageTool.java
 * @author N. de Goeij
 * @version 1.00, 24 april 2007
 *
 * @copyright 2007 B3Partners. All rights reserved.
 * B3Partners PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package nl.b3p.kaartenbalie.service;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
//import com.sun.media.imageio.plugins.tiff.*;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * KBImageTool definition:
 */

public class KBImageTool {
     private static final Log log = LogFactory.getLog(ImageReader.class);
     private BufferedImage bi;
    /**
     * Creates a new instance of KBImageTool
     */
    public KBImageTool() {
    }
    
    public BufferedImage readImage(GetMethod method, String MIME) throws Exception {
        /*
         * First we need to check wether or not this MIME type is supported by the program.
         */
        String mimeType = getMimeType(MIME);
        /*
        if(mimeType == null) {
            log.error("Unknown imageformat: " + MIME);
            throw new Exception("Unknown imageformat: " + MIME);
        }
        */
        
        /*
         * Then we need to check if we can find a reader which can handle this MIME.
         */
        ImageReader ir = getReader(MIME);
        if(ir == null) {
            log.error("No imagereader available for imageformat: " + MIME);
            throw new Exception("No imagereader available for imageformat: " + MIME);
        }
        
        /*
         * If we have a reader for this mime, we can read the input with the right format.
         */
        ImageInputStream stream = ImageIO.createImageInputStream(method.getResponseBodyAsStream());
        ir.setInput(stream, true);
        bi = ir.read(0);
        return bi;
    }
    
    public BufferedImage combineImages(BufferedImage [] images) {
        int width = images[0].getWidth();
        int height = images[0].getHeight();
        
        BufferedImage newBufIm = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gbi = newBufIm.createGraphics();
                    
        /* Onto this Graphics 2D object we draw the layer which is the lowest in ranking.
         * After drawing this layer we draw all the other layers on top of it, setting the
         * AlphaComposite on the highest alpha (1.0f) with a DST_OVER
         */
        gbi.drawImage(images[0], 0, 0, null);
        gbi.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OVER, 1.0f));
        
        for (int i = 1; i < images.length; i++) {
            gbi.drawImage(images[i], 0, 0, null);
        }
        return newBufIm;
    }
    
    public void writeImage(BufferedImage bufferedImage, String MIME, DataWrapper dw) throws Exception {
        /*
         * First we need to check wether or not this MIME type is supported by the program.
         */
        String mimeType = getMimeType(MIME);
        if(mimeType == null) {
            log.error("No imagereader available for imageformat: " + MIME);
            throw new Exception("Unknown imageformat: " + MIME);
        }
        
        /*
         * Then we need to check if we can find a reader which can handle this MIME.
         */
        ImageWriter iw = getWriter(mimeType);
        if(iw == null) {
            log.error("No imagewriter available for imageformat: " + MIME);
            throw new Exception("No imagewriter available for imageformat: " + MIME);
        }
                
        //We hebben nu een writer nodig die precies overweg kan met
        // een specifiek MIME type. Hiervoor kan dezelfde methode gebruikt worden als
        //hierboven voor het inlezen van een plaatje, maar om een of andere reden gaat dit helaas 
        //nog niet helemaal zoals het moet gaan.
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
        iw.setOutput(ios);
        iw.write(new IIOImage(bufferedImage, null, null));
        dw.write(baos);
        
        // Cleanup
        ios.flush();
        iw.dispose();
        ios.close();
    }
                
    private String getMimeType(String MIME) {
        String [] mimeTypes = ImageIO.getReaderMIMETypes();
        String mime = MIME.toUpperCase();
                
        if(MIME.equalsIgnoreCase("image/tiff")) {
            mime = "image/tif";
        }
        
        for (int i = 0; i < mimeTypes.length; i++) {
            if (mimeTypes[i].equalsIgnoreCase(mime)) {
                return mimeTypes[i];
            }
        }
        
        return null;
    }
    
    private ImageReader getReader(String MIME) {
        ImageReader ir = null;
        
        String [] fnames = ImageIO.getReaderFormatNames();
        for (int i = 0; i < fnames.length; i++) {
            System.out.println(fnames[i]);
        }
        
        Iterator it = ImageIO.getImageReadersByMIMEType(MIME);
        while(it.hasNext()) {
            ir = (ImageReader)it.next();
        }
        
        return ir;
    }
    
    private ImageReader getReader(ImageInputStream stream) {
        ImageReader ir = null;
        
        Iterator it = ImageIO.getImageReaders(stream);
        while(it.hasNext()) {
            ir = (ImageReader)it.next();
        }
        
        return ir;
    }
    
    private ImageWriter getWriter(String MIME) {
        ImageWriter iw = null;
        
        String [] fnames = ImageIO.getWriterFormatNames();
        for (int i = 0; i < fnames.length; i++) {
            System.out.println(fnames[i]);
        }
        
        Iterator it = ImageIO.getImageWritersByMIMEType(MIME);
        while(it.hasNext()) {
            iw = (ImageWriter)it.next();
        }
        
        return iw;
    }
}
