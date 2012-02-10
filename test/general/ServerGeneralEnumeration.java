package general;

import java.util.ArrayList;
import java.util.Enumeration;

/**
 *
 * @author rachelle
 */
abstract class ServerGeneralEnumeration implements Enumeration{
    protected int counter;
    protected int elements;
    protected ArrayList<Object> values;
    
    public ServerGeneralEnumeration(){
        this.reset();
        this.values         = new ArrayList<Object>();
        this.elements       = 0;
    }
    
    protected void add(Object name){
        this.values.add(name);
        
        this.elements++;
    }

    public Object nextElement(){
        this.counter++;
        
        return this.values.get(this.counter-1);
    }

    public boolean hasMoreElements() {
        return this.counter < this.elements;
    }
    
    public void reset(){
        this.counter    = 0;
    }    
}
