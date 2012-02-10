package general;

/**
 *
 * @author rachelle
 */
public class ServerDetailEnumeration extends ServerGeneralEnumeration {
    public ServerDetailEnumeration(){
        super();
    }
    
    public void addName(String name){
        super.add(name);
    }
    
    @Override
    public String nextElement(){        
        return (String) super.nextElement();
    }
}
