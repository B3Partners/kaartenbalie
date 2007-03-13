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
import nl.b3p.kaartenbalie.core.server.SRS;
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
            Set srs = l.getSrs();
            if (srs == null) {
                log.debug("Layer: "+l.getName()+"don't got a SRS");
            }
        }
    }
    
    public boolean validate() {
        return this.validateSRS().length > 0;
    }
    
    /** Returns the combined srs's that all layers given supports
     */
    public String[] validateSRS(){
        HashMap hm= new HashMap();
        //Layer[] layerArray= (Layer[])layers.toArray();
        Iterator lit=layers.iterator();
        //Een teller die alle layers telt die een SRS hebben.
        int tellerMeeTellendeLayers=0;
        boolean layersHasSRS;
        //doorloop de layers
        while(lit.hasNext()){
            layersHasSRS=false;
            Set <SRS> srsen= ((Layer)lit.next()).getSrs();
            Iterator it= srsen.iterator();
            //doorloop de srsen van de layers
            while (it.hasNext()){
                SRS srs= (SRS)it.next();
                if (srs.getSrs()!=null && srs.getMaxx()==null){
                    layersHasSRS=true;
                    if (srs.getSrs().contains(" ")){
                        String[] tokens= srs.getSrs().split(" ");
                        //doorloop de door komma gescheiden srsen
                        for (int t=0; t < tokens.length; t++){
                            addSrsCount(hm,tokens[t]);                           
                        }
                    }else{
                        addSrsCount(hm,srs.getSrs());
                    }
                }
            }
            if (layersHasSRS){
                 //Teller ophogen: Layer heeft srs
                tellerMeeTellendeLayers++;
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
