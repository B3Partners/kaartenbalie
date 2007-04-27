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

import com.sun.imageio.plugins.png.PNGImageReader;
import java.awt.AlphaComposite;
import java.awt.Color;
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
     
     private final String TIFF  = "image/tiff";
     private final String GIF   = "image/gif";
     private final String JPEG  = "image/jpeg";
     private final String PNG   = "image/png";
    /**
     * Creates a new instance of KBImageTool
     */
    public KBImageTool() {
    }
    
    public BufferedImage readImage(GetMethod method, String mime) throws Exception {
        String mimeType = getMimeType(mime);
    	if (mimeType == null)
    		throw new Exception ("unsupported mime type: " + mime);
    	
        ImageReader ir = getReader(mimeType);
        if (ir == null)
            throw new Exception ("no reader available for imageformat: " + mimeType.substring(mimeType.lastIndexOf("/") + 1));
        
        ImageInputStream stream = ImageIO.createImageInputStream(method.getResponseBodyAsStream());
        ir.setInput(stream, true);
        return ir.read(0);
    }
    
    public void writeImage(BufferedImage [] images, String mime, DataWrapper dw) throws Exception {
        String mimeType = getMimeType(mime);
    	if (mimeType == null)
    		throw new Exception ("unsupported mime type: " + mime);
        
        BufferedImage bufferedImage = combineImages(images, mime);
        if(mime.equals(TIFF)) {
            writeTIFFImage(bufferedImage, dw);
        } else {
            writeOtherImage(bufferedImage, dw, mimeType.substring(mimeType.lastIndexOf("/") + 1));
        }
    }
    
    private void writeTIFFImage(BufferedImage bufferedImage, DataWrapper dw) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "tif", baos);
        dw.write(baos);
    }
    
    private void writeOtherImage(BufferedImage bufferedImage, DataWrapper dw, String extension) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
        ImageIO.write(bufferedImage, extension, ios);
        dw.write(baos);
        ios.flush();
        ios.close();
    }
    
    private BufferedImage combineImages(BufferedImage [] images, String mime) {
        if(mime.equals(JPEG)) {
            return combineJPGImages(images);
        } else {
            return combineOtherImages(images);
        }
    }
    
    private BufferedImage combineJPGImages(BufferedImage [] images) {
        int width = images[0].getWidth();
        int height = images[0].getHeight();
        
        BufferedImage newBufIm = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D gbi = newBufIm.createGraphics();
        gbi.drawImage(images[0], 0, 0, null);
        
        for (int i = 1; i < images.length; i++) {
            gbi.drawImage(images[i], 0, 0, null);
        }
        return newBufIm;
    }
    
    private BufferedImage combineOtherImages(BufferedImage [] images) {
        int width = images[0].getWidth();
        int height = images[0].getHeight();
        
        BufferedImage newBufIm = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D gbi = newBufIm.createGraphics();
        
        gbi.drawImage(images[0], 0, 0, null);
        gbi.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OVER, 1.0f));
        
        for (int i = 1; i < images.length; i++) {
            gbi.drawImage(images[i], 0, 0, null);
        }
        return newBufIm;
    }
                
    /** Private method which seeks through the specified MIME type to check if
     * a certain MIME is supported.
     *
     * @param MIME String with the MIME to find.
     *
     * @return a String with the found MIME or null if no MIME was found.
     */
    // <editor-fold defaultstate="" desc="getMimeType(String MIME) method.">
    private String getMimeType(String mime) {
    	String [] mimeTypes = ImageIO.getReaderMIMETypes();
        for (int i = 0; i < mimeTypes.length; i++)
            if (mimeTypes[i].equalsIgnoreCase(mime))
                return mimeTypes[i];        
        return null;
    }
    // </editor-fold>
    
    /** Private method which seeks through the specified MIME type to check if
     * a certain MIME is supported.
     *
     * @param MIME String with the MIME to find.
     *
     * @return a String with the found MIME or null if no MIME was found.
     */
    // <editor-fold defaultstate="" desc="getReader(String mime) method.">
    private ImageReader getReader(String mime) {
        if(mime.equals(JPEG) || mime.equals(PNG)) {
            return getJPGOrPNGReader(mime);
        } else {
            return getGIFOrTIFFReader(mime);
        }
    }
    // </editor-fold>
        
    /** Private method which seeks through the specified MIME type to check if
     * a certain MIME is supported.
     *
     * @param MIME String with the MIME to find.
     *
     * @return a String with the found MIME or null if no MIME was found.
     */
    // <editor-fold defaultstate="" desc="getJPGOrPNGReader(String mime) method.">
    private ImageReader getJPGOrPNGReader(String mime) {
        Iterator it = ImageIO.getImageReadersByMIMEType(mime);
        ImageReader imTest = null;
        while(it.hasNext()) {
            imTest = (ImageReader)it.next();
            String name = imTest.getClass().getPackage().getName();
            String generalPackage = name.substring(0, name.lastIndexOf("."));
            if(generalPackage.equalsIgnoreCase("com.sun.media.imageioimpl.plugins"))
                continue;
        }
        return imTest;
    }
    // </editor-fold>
    
    /** Private method which seeks through the specified MIME type to check if
     * a certain MIME is supported.
     *
     * @param MIME String with the MIME to find.
     *
     * @return a String with the found MIME or null if no MIME was found.
     */
    // <editor-fold defaultstate="" desc="getGIFOrTIFFReader(String mime) method.">
    private ImageReader getGIFOrTIFFReader(String mime) {
        Iterator it = ImageIO.getImageReadersByMIMEType(mime);
        ImageReader imTest = null;
        while(it.hasNext()) {
            imTest = (ImageReader)it.next();
        }
        return imTest;
    }
    // </editor-fold>
    
    /*
     * TODO:
     * Writers below are defined but possibly not used. Delete after testing!
     */
        
    /** Private method which seeks through the specified MIME type to check if
     * a certain MIME is supported.
     *
     * @param MIME String with the MIME to find.
     *
     * @return a String with the found MIME or null if no MIME was found.
     */
    // <editor-fold defaultstate="" desc="getWriter(String mime) method.">
    private ImageWriter getWriter(String mime) {
        if(mime.equals(JPEG) || mime.equals(PNG)) {
            return getJPGOrPNGWriter(mime);
        } else {
            return getGIFOrTIFFWriter(mime);
        }
    }
    // </editor-fold>
    
    /** Private method which seeks through the specified MIME type to check if
     * a certain MIME is supported.
     *
     * @param MIME String with the MIME to find.
     *
     * @return a String with the found MIME or null if no MIME was found.
     */
    // <editor-fold defaultstate="" desc="getJPGOrPNGWriter(String mime) method.">
    private ImageWriter getJPGOrPNGWriter(String mime) {
        Iterator it = ImageIO.getImageReadersByMIMEType(mime);
        ImageWriter imTest = null;
        while(it.hasNext()) {
            imTest = (ImageWriter)it.next();
            String name = imTest.getClass().getPackage().getName();
            String generalPackage = name.substring(0, name.lastIndexOf("."));
            if(generalPackage.equalsIgnoreCase("com.sun.media.imageioimpl.plugins"))
                continue;
        }
        return imTest;
    }
    // </editor-fold>
    
    /** Private method which seeks through the specified MIME type to check if
     * a certain MIME is supported.
     *
     * @param MIME String with the MIME to find.
     *
     * @return a String with the found MIME or null if no MIME was found.
     */
    // <editor-fold defaultstate="" desc="getGIFOrTIFFWriter(String mime) method.">
    private ImageWriter getGIFOrTIFFWriter(String mime) {
        Iterator it = ImageIO.getImageReadersByMIMEType(mime);
        ImageWriter imTest = null;
        while(it.hasNext()) {
            imTest = (ImageWriter)it.next();
        }
        return imTest;
    }
    // </editor-fold>
}
