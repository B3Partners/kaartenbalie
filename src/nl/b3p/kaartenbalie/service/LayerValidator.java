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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.SrsBoundingBox;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Roy
 */
public class LayerValidator {

    private static final Log log = LogFactory.getLog(LayerValidator.class);
    private Set layers;

    /** Creates a new Instance of LayerValidator with the given layers
     */
    public LayerValidator(Set layers) {
        setLayers(layers);
    }
    /* Getters and setters */

    public Set getLayers() {
        return layers;
    }

    public void setLayers(Set layers) {
        this.layers = layers;

        Iterator it = layers.iterator();
        while (it.hasNext()) {
            Layer l = (Layer) it.next();
            Set srsbb = l.getSrsbb();
            if (srsbb == null) {
                log.debug("Layer: " + l.getUniqueName() + " does not have a SRS");
            }
        }
    }

    public boolean validate() {
        return this.validateSRS().length > 0;
    }

    public SrsBoundingBox validateLatLonBoundingBox() {
        Iterator it = layers.iterator();
        ArrayList supportedLLBB = new ArrayList();
        while (it.hasNext()) {
            addLayerSupportedLLBB((Layer) it.next(), supportedLLBB);
        }

        //nu hebben we een lijst met alle LLBB's
        //van deze LLBB's moet nu per item bekeken worden welke de uiterste waarden
        //heeft voor de minx, miny, maxx, maxy
        // volgende waarden geinitialiseerd op extreme omgekeerde waarde
        double minx = 180.0, miny = 90.0, maxx = -180.0, maxy = -90.0;
        it = supportedLLBB.iterator();
        while (it.hasNext()) {
            SrsBoundingBox llbb = (SrsBoundingBox) it.next();
            double xmin = Double.parseDouble(llbb.getMinx());
            double ymin = Double.parseDouble(llbb.getMiny());
            double xmax = Double.parseDouble(llbb.getMaxx());
            double ymax = Double.parseDouble(llbb.getMaxy());

            if (xmin < minx) {
                minx = xmin;
            }

            if (ymin < miny) {
                miny = ymin;
            }

            if (xmax > maxx) {
                maxx = xmax;
            }

            if (ymax > maxy) {
                maxy = ymax;
            }
        }



        SrsBoundingBox llbb = new SrsBoundingBox();


        llbb.setMinx(Double.toString(minx));
        llbb.setMiny(Double.toString(miny));
        llbb.setMaxx(Double.toString(maxx));
        llbb.setMaxy(Double.toString(maxy));


        return llbb;
    }

    /** 
     * Checks wether or not a layer has a LatLonBoundingBox. If so this LatLonBoundingBox is added to the supported hashmap
     */
    // <editor-fold defaultstate="" desc="default DescribeLayerRequestHandler() constructor">
    private void addLayerSupportedLLBB(Layer layer, ArrayList supported) {
        Set srsen = layer.getSrsbb();
        if (srsen == null) {
            return;
        }

        Iterator it = srsen.iterator();
        while (it.hasNext()) {
            SrsBoundingBox srsbb = (SrsBoundingBox) it.next();
            String type = srsbb.getType();

            if (type != null) {
                if (type.equalsIgnoreCase("LatLonBoundingBox")) {
                    supported.add(srsbb);
                }
            }
        }

        if (layer.getParent() != null) {
            addLayerSupportedLLBB(layer.getParent(), supported);
        }
    }
    // </editor-fold>
    /** add a srs supported by this layer or a parent of the layer to the supported hashmap
     */
    public void addLayerSupportedSRS(Layer l, HashMap supported) {
        Set srsen = l.getSrsbb();
        if (srsen == null) {
            return;
        }
        Iterator i = srsen.iterator();
        while (i.hasNext()) {
            SrsBoundingBox srsbb = (SrsBoundingBox) i.next();
            if (srsbb.getSrs() != null) {
                // alleen srs zonder boundingbox coords
                if (srsbb.getMinx() == null && srsbb.getMiny() == null && srsbb.getMaxx() == null && srsbb.getMaxy() == null) {
                    supported.put(srsbb.getSrs(), srsbb.getSrs());
                }
            }
        }
        if (l.getParent() != null) {
            addLayerSupportedSRS(l.getParent(), supported);
        }
    }

    /** Returns the combined srs's that all layers given supports
     *
     * Every Layer shall have at least one <SRS> element that is either stated explicitly or
     * inherited from a parent Layer (Section 7.1.4.6). The root <Layer> element shall include a
     * sequence of zero or more SRS elements listing all SRSes that are common to all
     * subsidiary layers. Use a single SRS element with empty content (like so: "<SRS></SRS>") if
     * there is no common SRS. Layers may optionally add to the global SRS list, or to the list
     * inherited from a parent layer. Any duplication shall be ignored by clients.
     */
    public String[] validateSRS() {
        HashMap hm = new HashMap();
        Iterator lit = layers.iterator();
        //Een teller die alle layers telt die een SRS hebben.
        int tellerMeeTellendeLayers = 0;
        //doorloop de layers
        while (lit.hasNext()) {
            HashMap supportedByLayer = new HashMap();
            addLayerSupportedSRS((Layer) lit.next(), supportedByLayer);
            if (supportedByLayer.size() > 0) {
                tellerMeeTellendeLayers++;
                Iterator i = supportedByLayer.values().iterator();
                while (i.hasNext()) {
                    String srs = (String) i.next();
                    addSrsCount(hm, srs);
                }
            }
        }
        ArrayList supportedSrsen = new ArrayList();
        Iterator it = hm.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            int i = ((Integer) entry.getValue()).intValue();
            if (i >= tellerMeeTellendeLayers) {
                supportedSrsen.add((String) entry.getKey());
            }
        }
        //Voeg lege srs toe indien geen overeenkomstige gevonden
        if (supportedSrsen.isEmpty()) {
            supportedSrsen.add("");
        }
        String[] returnValue = new String[supportedSrsen.size()];
        for (int i = 0; i < returnValue.length; i++) {
            if (supportedSrsen.get(i) != null) {
                returnValue[i] = (String) supportedSrsen.get(i);
            }
        }
        return returnValue;
    }

    /** Methode that counts the different SRS's
     * @parameter hm The hashmap that contains the counted srsen
     * @parameter srs The srs to add to the count.
     */
    private void addSrsCount(HashMap hm, String srs) {
        if (hm.containsKey(srs)) {
            int i = ((Integer) hm.get(srs)).intValue() + 1;
            hm.put(srs, new Integer(i));
        } else {
            hm.put(srs, new Integer("1"));
        }
    }
}
