package general;

import nl.b3p.wms.capabilities.Layer;

/**
 *
 * @author rachelle
 */
public class LayerTest extends Layer {
    public void setTestId(Integer id){
        super.setId(id);
        
        this.setName("layer "+id);
    }
}
