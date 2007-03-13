
/*
 * @(#)ImageTest.java
 * @author N. de Goeij
 * @version 1.00, 9 maart 2007
 *
 * @copyright 2007 B3Partners. All rights reserved.
 * B3Partners PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package nl.b3p.kaartenbalie.test;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ImageTest definition:
 *
 */

public class ImageTest {
    private static final Log log = LogFactory.getLog(ImageTest.class);
    private Object text;
    
    /** Creates a new instance of ImageTest */
    public ImageTest() throws IOException {
        //combineImages("c:\\tilestest\\");
        String sourcepath = "c:\\testtiles\\";
        int xStartCoord = 12;
        int yStartCoord = 308;
        

        int step = 4;
        for (int y = 0; y <= 78; y += 2) {
            for (int x = 0; x <= 78; x += 2) {
                int xStep1 = xStartCoord + (x*4);
                int yStep1 = yStartCoord + (y*4);
                int xStep2 = xStartCoord + (x*4) + step;
                int yStep2 = yStartCoord + (y*4) + step;
                
                String file1 = "" + xStep1 + "_" + yStep1 + ".tif";
                String file2 = "" + xStep2 + "_" + yStep1 + ".tif";
                String file3 = "" + xStep1 + "_" + yStep2 + ".tif";
                String file4 = "" + xStep2 + "_" + yStep2 + ".tif";
                
                String [] fileName = new String [4];
                fileName[0] = file1;
                fileName[1] = file2;
                fileName[2] = file3;
                fileName[3] = file4;
                //create new name for image
                String destFileName = xStep1 + "-" + xStep2 + "_" + yStep1 + "-" + yStep2 + ".tif";
                String destpath = "c:\\test";
                
                combineImages(sourcepath, fileName, destpath, destFileName);
            }
        }
    }
    
    public void combineImages(String sourcepath, String [] files, String destpath, String destFileName) throws IOException {
        BufferedImage [] bis = loadImages(sourcepath, files);
        int w = 0;
        int h = 0;
        
        boolean atLeastOneImage = false;
        for (int i = 0; i < bis.length; i++ ) {
            BufferedImage b = bis[i];
            if (b != null) {
                w = b.getWidth();
                h = b.getHeight();
                atLeastOneImage = true;
            }
        }
        
        if(atLeastOneImage) {
            BufferedImage total = new BufferedImage(2*w, 2*h, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = total.createGraphics();
            
            g.drawImage(bis[0], 0, 0, null);
            g.drawImage(bis[1], w, 0, null);
            g.drawImage(bis[2], 0, h, null);
            g.drawImage(bis[3], w, h, null);
            
            FileOutputStream os = new FileOutputStream(destpath + destFileName);
            ImageIO.write(total, "tif", os);
            os.close();
        }        
    }
    
    private BufferedImage [] loadImages(String sourcepath, String [] files) throws IOException {
        BufferedImage [] bi = new BufferedImage [files.length];
        
        for (int i = 0; i < files.length; i++) {
            String filePathName = sourcepath + files[i];
            
            File file = new File(sourcepath + "\\" + files[i]);
            if (file.isFile()) {
                bi[i] = ImageIO.read(file);
                bi[i] = scale(bi[i]);
            }
        }
        return bi;
    }
    
    private BufferedImage scale(BufferedImage bi) {
        AffineTransform tx = new AffineTransform();
        tx.scale(0.25, 0.25);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BICUBIC);
        return op.filter(bi, null);
    }
    
    private String [] loadImageList(String pathName) {
        String directoryName;              
        String [] files = null;
        
        File directory = new File(pathName);
        if (!directory.isDirectory()) {
            if (!directory.exists()) {
                log.error("There is no such directory: "+pathName);
            } else {
                log.error("That file is not a directory: "+pathName);
            }
        } else {
            files = directory.list();
            
        }
        return files;
    }
    
    public static void main(String [] args) throws IOException {
        ImageTest it = new ImageTest();   
    }
}
