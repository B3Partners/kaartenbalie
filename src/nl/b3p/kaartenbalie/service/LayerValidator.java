/*
 * LayerValidator.java
 *
 * Created on 2 maart 2007, 16:13
 *
 * Autor: Roy
 */

package nl.b3p.kaartenbalie.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import nl.b3p.kaartenbalie.core.server.Layer;
import nl.b3p.kaartenbalie.core.server.SrsBoundingBox;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Roy
 */
public class LayerValidator {
    private static final Log log = LogFactory.getLog(LayerValidator.class);
    private Set <Layer> layers;
    
    /** Creates a new Instance of LayerValidator with the given layers
     */
    public LayerValidator(Set <Layer> layers){
        setLayers(layers);
    }
    /* Getters and setters */
    public Set<Layer> getLayers() {
        return layers;
    }
    
    public void setLayers(Set<Layer> layers) {
        this.layers = layers;
        
        Iterator it = layers.iterator();
        while (it.hasNext()) {
            Layer l = (Layer) it.next();
            Set srsbb = l.getSrsbb();
            if (srsbb == null) {
                log.debug("Layer: "+l.getName()+" does not have a SRS");
            }
        }
    }
    
    public boolean validate() {
        return this.validateSRS().length > 0;
    }
    /** add a srs supported by this layer or a parent of the layer to the supported hashmap
     *
     * When a Layer is available in several Spatial Reference Systems, there are two ways to
     * encode the list of SRS values. The first of these is new in this version of the
     * specification, the second is deprecated but still included for backwards compatibility.
     * 1. Optional, recommended: Multiple single-valued <SRS> elements: a list of SRS
     * values is represented as a sequence of <SRS> elements, each of which contains only a
     * single SRS name. Example: <SRS>EPSG:1234</SRS> <SRS>EPSG:5678</SRS>.
     * 2. Deprecated: Single list-valued <SRS> element: a list of SRS values is represented as
     * a whitespace-separated list of SRS names contained within a single <SRS> element.
     * Example: <SRS>EPSG:1234 EPSG:5678</SRS>.
     * WMS 1.1.1 Clients shall be prepared to handle either encoding.
     */
    public void addLayerSupportedSRS(Layer l, HashMap supported){
        Set srsen=l.getSrsbb();
        if (srsen==null)
            return;
        Iterator i=srsen.iterator();
        while (i.hasNext()){
            SrsBoundingBox srsbb = (SrsBoundingBox)i.next();
            if (srsbb.getSrs()!=null){
                if (srsbb.getSrs().contains(" ")){
                    String[] tokens= srsbb.getSrs().split(" ");
                    //doorloop de door spatie gescheiden srsen
                    for (int t=0; t < tokens.length; t++){
                        supported.put(tokens[t],tokens[t]);                           
                    }
                }else{ 
                    supported.put(srsbb.getSrs(),srsbb.getSrs());
                }
            }
        }
        if (l.getParent()!=null){
            addLayerSupportedSRS(l.getParent(),supported);
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
    public String[] validateSRS(){
        HashMap hm= new HashMap();
        Iterator lit=layers.iterator();
        //Een teller die alle layers telt die een SRS hebben.
        int tellerMeeTellendeLayers=0;
        //doorloop de layers
        while(lit.hasNext()){
            HashMap supportedByLayer=new HashMap();
            addLayerSupportedSRS((Layer)lit.next(),supportedByLayer);
            if (supportedByLayer.size()>0){
                tellerMeeTellendeLayers++;
                Iterator i=supportedByLayer.values().iterator();
                while(i.hasNext()){
                    String srs= (String)i.next();
                    addSrsCount(hm,srs);
                }
            }            
        }
        ArrayList supportedSrsen=new ArrayList();
        Iterator it=hm.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry entry=(Map.Entry)it.next();
            int i= ((Integer)entry.getValue()).intValue();
            if (i>=tellerMeeTellendeLayers){
                supportedSrsen.add((String)entry.getKey());                
            }
        }
        //Voeg lege srs toe indien geen overeenkomstige gevonden
        if (supportedSrsen.isEmpty())
            supportedSrsen.add("");
        
        String[] returnValue= new String[supportedSrsen.size()];
        for (int i=0; i < returnValue.length; i ++){
            if(supportedSrsen.get(i)!=null)
                returnValue[i]=(String)supportedSrsen.get(i);
        }
        return returnValue;
    }
    /** Methode that counts the different SRS's
     * @parameter hm The hashmap that contains the counted srsen
     * @parameter srs The srs to add to the count.
     */
     private void addSrsCount(HashMap hm, String srs){
        if (hm.containsKey(srs)){
            int i= ((Integer)hm.get(srs)).intValue()+1;
            hm.put(srs,new Integer(i));
        }else{
            hm.put(srs,new Integer("1"));
        }
    }
}
