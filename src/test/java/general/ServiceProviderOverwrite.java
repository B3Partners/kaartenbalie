package general;

import nl.b3p.wms.capabilities.ServiceProvider;

/**
 *
 * @author rachelle
 */
public class ServiceProviderOverwrite extends ServiceProvider {
     public void setTestId(Integer id){
        super.setId(id);
        
        this.setName("layer "+id);
    }
}
