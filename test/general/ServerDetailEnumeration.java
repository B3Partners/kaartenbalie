package general;

import java.util.ArrayList;
import java.util.Enumeration;

/**
 *
 * @author rachelle
 */
public class ServerDetailEnumeration implements Enumeration {
    ArrayList<String> serverNames;
    private int counter;
    
    public ServerDetailEnumeration(){
        this.reset();
        this.serverNames    = new ArrayList<String>();
    }
    
    public void addName(String name){
        this.serverNames.add(name);
    }
    
    public Object nextElement(){
        this.counter++;
        
        return this.serverNames.get(this.counter-1);
    }

    public boolean hasMoreElements() {
        return this.counter < this.serverNames.size();
    }
    
    public void reset(){
        this.counter    = 0;
    }
}
