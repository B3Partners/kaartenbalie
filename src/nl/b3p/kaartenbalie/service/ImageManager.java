/*
 * B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
 * for authentication/authorization, pricing and usage reporting.
 *
 * Copyright 2006, 2007, 2008 B3Partners BV
 * 
 * This file is part of B3P Kaartenbalie.
 * 
 * B3P Kaartenbalie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * B3P Kaartenbalie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.b3p.kaartenbalie.service;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import nl.b3p.kaartenbalie.core.server.monitoring.DataMonitoring;
import nl.b3p.kaartenbalie.core.server.monitoring.ServiceProviderRequest;
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
        if (urlWrapper == null || urlWrapper.isEmpty()) {
            return;
        }
        Iterator it = urlWrapper.iterator();
        while (it.hasNext()) {
            ServiceProviderRequest wmsRequest = (ServiceProviderRequest) it.next();
            //String url = wmsRequest.getProviderRequestURI();
            ImageCollector ic = new ImageCollector(wmsRequest, dw);
            ics.add(ic);
        }
    }

    public void process() throws Exception {

        // Hier worden de threads gestart
        ImageCollector ic = null;
        Iterator it = ics.iterator();
        while (it.hasNext()) {
            ic = (ImageCollector) it.next();
            if (ic.getStatus() == ImageCollector.NEW) {
                ic.processNew();
            }
        }

        // Hier wordt op de threads gewacht tot ze klaar zijn.
        it = ics.iterator();
        while (it.hasNext()) {
            ic = (ImageCollector) it.next();
            if (ic.getStatus() == ImageCollector.ACTIVE) {//if (ic.isAlive()) { /
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

        DataMonitoring rr = dw.getRequestReporting();

        while (it.hasNext()) {
            ic = (ImageCollector) it.next();
            int status = ic.getStatus();
            if (status == ImageCollector.ERROR || ic.getBufferedImage() == null) {
                throw new ProviderException(ic.getMessage() + " (Status: " + status + ")");
            }
            if (status != ImageCollector.COMPLETED) {
                // problem with one of sp's, but we continue with the rest!
                log.error(ic.getMessage() + " (Status: " + status + ")");
            }
            /* Do some reporting! */
            ServiceProviderRequest s = ic.getWmsRequest();
            rr.addServiceProviderRequest(s);

        }

        BufferedImage[] allImages = new BufferedImage[ics.size()];
        for (int i = 0; i < ics.size(); i++) {
            ic = (ImageCollector) ics.get(i);
            allImages[i] = ic.getBufferedImage();
        }

        KBImageTool.writeImage(allImages, "image/png", dw);
    }
}
