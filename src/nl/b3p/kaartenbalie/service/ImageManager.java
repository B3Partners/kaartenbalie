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
import java.util.Iterator;
import nl.b3p.kaartenbalie.core.server.reporting.control.DataMonitoring;
import nl.b3p.kaartenbalie.core.server.reporting.domain.requests.WMSRequest;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ImageManager definition:
 *
 */

public class ImageManager {
    
    private final Log log = LogFactory.getLog(this.getClass());
    private ArrayList ics = new ArrayList();
    
    public ImageManager(ArrayList urlWrapper, DataWrapper dw) {
        if (urlWrapper==null || urlWrapper.isEmpty())
            return;
        Iterator it = urlWrapper.iterator();
        while (it.hasNext()) {
            WMSRequest wmsRequest = (WMSRequest) it.next();
            //String url = wmsRequest.getProviderRequestURI();
            ImageCollector ic = new ImageCollector(wmsRequest, dw);
            ics.add(ic);
        }
    }
    
    public void process() throws Exception {
        
        //log.debug("Starting process");
        
        // Hier worden de threads gestart
        ImageCollector ic = null;
        Iterator it = ics.iterator();
        while (it.hasNext()) {
            ic = (ImageCollector)it.next();
            if (ic.getStatus()==ImageCollector.NEW) {
                ic.processNew();
            }
        }
        //log.debug("Image collectors started");
        
        // Hier wordt op de threads gewacht tot ze klaar zijn.
        it = ics.iterator();
        while (it.hasNext()) {
            ic = (ImageCollector)it.next();
            if (ic.getStatus()==ImageCollector.ACTIVE) {//if (ic.isAlive()) { /
                ic.processWaiting();
            }
        }
        
        
    }
    
    public void sendCombinedImages(DataWrapper dw) throws Exception {
        //TODO beslissen of we plaatje gaan sturen als een van de onderliggende
        // image niet goed is opgehaald.
        // lastig omdat ook rekening met betaling gehouden moet worden
        
        ImageCollector ic = null;
        Iterator it = ics.iterator();
        
        Class requestClassType = dw.getRequestClassType();
        DataMonitoring rr = dw.getRequestReporting();
        
        while (it.hasNext()) {
            ic = (ImageCollector)it.next();
            int status = ic.getStatus();
            if (status != ImageCollector.COMPLETED || ic.getBufferedImage() == null) {
                // TODO alleen eerste foutmelding of ook nog de andere ???
                if (status == ImageCollector.ERROR) {
                    log.error(ic.getMessage());
                    throw new Exception(ic.getMessage());
                } else {
                    log.error(ic.getMessage() + " Download aborted.");
                    throw new Exception(ic.getMessage() + " Download aborted.");
                }
            }
            /* Do some reporting! */
            rr.addServiceProviderRequest(requestClassType, ic.getLocalParameterMap());
            
        }
        
        BufferedImage [] allImages = new BufferedImage[ics.size()];
        for (int i=0; i<ics.size(); i++) {
            ic = (ImageCollector)ics.get(i);
            allImages[i]= ic.getBufferedImage();
        }
        //log.debug("Image collection retrieved from providers");
        
        KBImageTool kbi = new KBImageTool();
        
        kbi.writeImage(allImages, "image/png", dw);
    }
    
}
