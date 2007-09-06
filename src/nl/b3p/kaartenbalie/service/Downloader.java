/*
 * @(#)Downloader.java
 * @author N. de Goeij
 * @version 1.00, 6 september 2007
 *
 * @copyright 2007 B3Partners. All rights reserved.
 * B3Partners PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package nl.b3p.kaartenbalie.service;

import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Downloader definition:
 *
 */

public class Downloader implements Runnable {
    
    private static final Log log = LogFactory.getLog(Downloader.class);
    
    private GetMethod method;
    private String contentType;
    private BufferedImage image;

    public Downloader (GetMethod method, String contentType) {
        this.method = method;
        this.contentType = contentType;
        this.download();
    }

    private void download() {
        //TODO: delete the code below.
        //Temporary information, can be deleted afterwards.
        long time = System.currentTimeMillis();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
        Date date = new Date(time);
        String starttime = df.format(date);
        log.info("Going to start Thread at time: " + starttime);
        //---------------------------------------------------------------
        
        
        Thread thread = new Thread(this);
        thread.start();
    }

    public void run() {
        try {
            BufferedImage bi = KBImageTool.readImage(method, contentType);
            setImage(bi);
        } catch (Exception ex) {
            log.error(ex);
        }
    }

    private void setImage(BufferedImage image) {
        this.image = image;
        
        //TODO: delete the code below.
        //Temporary information, can be deleted afterwards.
        long time = System.currentTimeMillis();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
        Date date = new Date(time);
        String endtime = df.format(date);
        log.info("Going to stop Thread at time: " + endtime);
        //---------------------------------------------------------------
    }

    public BufferedImage getImage() throws InterruptedException {
        while (image == null) {
            continue;
        }
        return image;
    }
}
