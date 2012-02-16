package general;

/**
 *
 * @author rachelle
 */
public class ServerDetailEnumeration extends ServerGeneralEnumeration {
    public ServerDetailEnumeration(){
        super();
    }
  
    /**
     * Adds the new element
     * 
     * @param name  The element
     */
    public void addName(String name){
        super.add(name);
    }
    
    /**
     * Returns the next element
     * 
     * @return  The next element
     */
    @Override
    public String nextElement(){        
        return (String) super.nextElement();
    }
}
