/*
 * @(#)ImageManager.java
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

/**
 * ImageManager definition:
 *
 */

public class ImageManager {
    private ArrayList images;
    private int maxSize;
    
    private static int imageCounter;
    private boolean available = false;
    
    public ImageManager(int totalDownloads) {
        images = new ArrayList();
        this.maxSize = totalDownloads;
        imageCounter = 0;
    }
    
    public synchronized ArrayList get() {
        while (available == false) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        available = (imageCounter >= maxSize);
        imageCounter--;
        notifyAll();
        return images;
    }

    public synchronized void put(BufferedImage bi) {
        while (available == true) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
        images.add(bi);
        imageCounter++;
        available = (imageCounter >= maxSize);
        notifyAll();
    }
}
