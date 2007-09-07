/*
 * @(#)ImageReader.java
 * @author N. de Goeij
 * @version 1.00, 7 september 2007
 *
 * @copyright 2007 B3Partners. All rights reserved.
 * B3Partners PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package nl.b3p.kaartenbalie.service;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ImageReader definition:
 *
 */

public class ImageReader extends Thread {
    
    private static final Log log = LogFactory.getLog(ImageReader.class);
    
    private ImageManager imagemanager;
    private BufferedImage bufferedImage;
    private ArrayList images;
    private DataWrapper dw;

    public ImageReader(ImageManager imagemanager, BufferedImage bufferedImage, DataWrapper datawrapper) {
        this.imagemanager = imagemanager;
        this.bufferedImage = bufferedImage;
        this.dw = datawrapper;
    }

    public void run() {
        images = imagemanager.get();
        this.sendCombinedImages();
    }
    
    public void sendCombinedImages() {
        BufferedImage [] allImages = new BufferedImage[images.size()];
        int counter = 0;
        Iterator it = images.iterator();
        while (it.hasNext()) {
            BufferedImage bf = (BufferedImage) it.next();
            allImages[counter] = bf;
            counter++;
        }
        
        try {
            KBImageTool kbi = new KBImageTool();
            kbi.writeImage(allImages, "image/png", dw);
        } catch (Exception ex) {
            log.error(ex);
            ex.printStackTrace();
        }
    }   
}