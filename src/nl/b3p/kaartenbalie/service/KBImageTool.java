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
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class KBImageTool {
    
     private static final Log log = LogFactory.getLog(KBImageTool.class);
     private BufferedImage bi;
     
     private static final String TIFF  = "image/tiff";
     private static final String GIF   = "image/gif";
     private static final String JPEG  = "image/jpeg";
     private static final String PNG   = "image/png";
    
    /** Reads an image from an http input stream.
     *
     * @param method Apache HttpClient GetMethod object
     * @param mime String representing the mime type of the image.
     *
     * @return BufferedImage
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="" desc="readImage(GetMethod method, String mime) method.">
    public static BufferedImage readImage(GetMethod method, String mime) throws Exception {
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
    // </editor-fold>
    
    /** First combines the given images to one image and then sends this image back to the client.
     *
     * @param images BufferedImage array with the images tha have to be combined and sent to the client.
     * @param mime String representing the mime type of the image.
     * @param dw DataWrapper object in which the request object is stored.
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="" desc="writeImage(BufferedImage [] images, String mime, DataWrapper dw) method.">
    public static void writeImage(BufferedImage [] images, String mime, DataWrapper dw) throws Exception {
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
    // </editor-fold>
    
    /** Writes a TIFF image to the outputstream.
     *
     * @param bufferedImage BufferedImage created from the given images.
     * @param dw DataWrapper object in which the request object is stored.
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="" desc="getOnlineData(DataWrapper dw, ArrayList urls, boolean overlay, String REQUEST_TYPE) method.">
    private static void writeTIFFImage(BufferedImage bufferedImage, DataWrapper dw) throws Exception {
        //log.info("Writing TIFF using ImageIO.write");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "tif", baos);
        dw.write(baos);
    }
    // </editor-fold>
    
    /** Writes a JPEG, GIF or PNG image to the outputstream.
     *
     * @param bufferedImage BufferedImage created from the given images.
     * @param dw DataWrapper object in which the request object is stored.
     * @param extension String with the extension of the file
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="" desc="writeOtherImage(BufferedImage bufferedImage, DataWrapper dw, String extension) method.">
    private static void writeOtherImage(BufferedImage bufferedImage, DataWrapper dw, String extension) throws Exception {
        //log.info("Writing JPG, GIF or PNG using ImageIO.write");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
        ImageIO.write(bufferedImage, extension, ios);        
        dw.write(baos);
        ios.flush();
        ios.close();
    }
    // </editor-fold>
    
    /** Method which handles the combining of the images. This method redirects to the right method
     * for the different images, since not every image can be combined in the same way.
     *
     * @param images BufferedImage array with the images tha have to be combined.
     * @param mime String representing the mime type of the image.
     *
     * @return BufferedImage
     */
    // <editor-fold defaultstate="" desc="combineImages(BufferedImage [] images, String mime) method.">
    private static BufferedImage combineImages(BufferedImage [] images, String mime) {
        if(mime.equals(JPEG)) {
            return combineJPGImages(images);
        } else {
            return combineOtherImages(images);
        }
    }
    // </editor-fold>
    
    /** Combines JPG images. Combining JPG images is different from the other image types since JPG
     * has to use an other imageType: BufferedImage.TYPE_INT_RGB.
     *
     * @param images BufferedImage array with the images tha have to be combined.
     *
     * @return BufferedImage
     */
    // <editor-fold defaultstate="" desc="combineJPGImages(BufferedImage [] images) method.">
    private static BufferedImage combineJPGImages(BufferedImage [] images) {
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
    // </editor-fold>
    
    /** Combines GIF, TIFF or PNG images. Combining these images is different from the JPG image types since these
     * has to use an other imageType: BufferedImage.TYPE_INT_ARGB_PRE.
     *
     * @param images BufferedImage array with the images tha have to be combined.
     *
     * @return BufferedImage
     */
    // <editor-fold defaultstate="" desc="combineOtherImages(BufferedImage [] images) method.">
    private static BufferedImage combineOtherImages(BufferedImage [] images) {
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
    // </editor-fold>
                
    /** Private method which seeks through the supported MIME types to check if
     * a certain MIME is supported.
     *
     * @param mime String with the MIME to find.
     *
     * @return a String with the found MIME or null if no MIME was found.
     */
    // <editor-fold defaultstate="" desc="getMimeType(String mime) method.">
    private static String getMimeType(String mime) {
    	String [] mimeTypes = ImageIO.getReaderMIMETypes();
        for (int i = 0; i < mimeTypes.length; i++)
            if (mimeTypes[i].equalsIgnoreCase(mime))
                return mimeTypes[i];        
        return null;
    }
    // </editor-fold>
    
    /** Private method which seeks through the supported image readers to check if
     * a there is a reader which handles the specified MIME.
     *
     * @param mime String with the MIME to find.
     *
     * @return ImageReader which can handle the specified MIME or null if no reader was found.
     */
    // <editor-fold defaultstate="" desc="getReader(String mime) method.">
    private static ImageReader getReader(String mime) {
        if(mime.equals(JPEG) || mime.equals(PNG)) {
            return getJPGOrPNGReader(mime);
        } else {
            return getGIFOrTIFFReader(mime);
        }
    }
    // </editor-fold>
        
    /** Private method which seeks through the supported image readers to check if
     * a there is a reader which handles the specified MIME. This method checks spe-
     * cifically for JPG or PNG images because Sun's Java supports two kind of readers
     * for these particular formats. And because one of these readers doesn't function
     * well, we need to be sure we have the right reader.
     *
     * @param mime String with the MIME to find.
     *
     * @return ImageReader which can handle the specified MIME or null if no reader was found.
     */
    // <editor-fold defaultstate="" desc="getJPGOrPNGReader(String mime) method.">
    private static ImageReader getJPGOrPNGReader(String mime) {
        Iterator it = ImageIO.getImageReadersByMIMEType(mime);
        ImageReader imTest = null;
        String name = null;
        while(it.hasNext()) {
            imTest = (ImageReader)it.next();
            name = imTest.getClass().getPackage().getName();
            String generalPackage = name.substring(0, name.lastIndexOf("."));
            if(generalPackage.equalsIgnoreCase("com.sun.media.imageioimpl.plugins"))
                continue;
        }
        //log.info("Using ImageReader: " + name);
        return imTest;
    }
    // </editor-fold>
    
    /** Private method which seeks through the supported image readers to check if
     * a there is a reader which handles the specified MIME. This method checks spe-
     * cifically for GIF or TIFF images.
     *
     * @param mime String with the MIME to find.
     *
     * @return ImageReader which can handle the specified MIME or null if no reader was found.
     */
    // <editor-fold defaultstate="" desc="getGIFOrTIFFReader(String mime) method.">
    private static ImageReader getGIFOrTIFFReader(String mime) {
        Iterator it = ImageIO.getImageReadersByMIMEType(mime);
        ImageReader imTest = null;
        String name = null;
        while(it.hasNext()) {
            imTest = (ImageReader)it.next();
            name = imTest.getClass().getPackage().getName();
        }
        //log.info("Using ImageReader: " + name);
        return imTest;
    }
    // </editor-fold>
    
    /** Private method which seeks through the supported image writers to check if
     * a there is a writers which handles the specified MIME.
     *
     * @param mime String with the MIME to find.
     *
     * @return ImageWriter which can handle the specified MIME or null if no writer was found.
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
    
    /** Private method which seeks through the supported image writers to check if
     * a there is a writers which handles the specified MIME. This method checks spe-
     * cifically for JPG or PNG images because Sun's Java supports two kind of writers
     * for these particular formats. And because one of these writers doesn't function
     * well, we need to be sure we have the right writers.
     *
     * @param mime String with the MIME to find.
     *
     * @return ImageWriter which can handle the specified MIME or null if no writer was found.
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
    
    /** Private method which seeks through the supported image writers to check if
     * a there is a writers which handles the specified MIME. This method checks spe-
     * cifically for GIF or TIFF images.
     *
     * @param mime String with the MIME to find.
     *
     * @return ImageWriter which can handle the specified MIME or null if no writer was found.
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
