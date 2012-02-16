package general;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;

/**
 *
 * @author rachelle
 */
public class MultikeyHashMap {
    private ArrayList<String> names;
    private ArrayList<String> values;
    private int counter         = 0;
    private int lastPosition    = 0;
    
    public MultikeyHashMap(){
        this.names  = new ArrayList<String>(50);
        this.values = new ArrayList<String>(50);
    }
    
    /**
     * Clears the HashMap
     */
    public void clear(){
        this.names.clear();
        this.values.clear();
        
        this.counter        = 0;
        this.lastPosition   = 0;
    }
    
    /**
     * Finds the key position from the given key
     * 
     * @param   key     The key to search on
     * @param   offset  The position to search from
     * 
     * @return  The position or -1 if the key does not (more) exist.
     */
    public int findKey(String key,int offset){
        if( offset < 0 )    offset = 0;
        
        for(int i=offset; i<this.lastPosition; i++){
            if( this.names.get(i).equals(key) ) return i;
        }
        
        return -1;
    }
    
    /**
     * Returns the requested value
     * 
     * @param key       The key to search on
     * @param offset    The position to search from
     * 
     * @return The value or null if the key is not present
     */
    public String get(String key,int offset){
        int pos = this.findKey(key,offset);
        if( pos == -1 ) return null;
        
        return this.values.get(pos);
    }
        
    /**
     * Adds a new value to the HashMap
     * 
     * @param key       The key
     * @param value     The value
     */
    public void add(String key,String value){
        this.names.add(key);
        this.values.add(value);
        
        this.counter++;
        this.lastPosition++;
        
        /* Check for hashmap size */
        if( this.names.size()/this.lastPosition < 0.1 ){
            this.names.ensureCapacity(this.names.size()*2);
            this.values.ensureCapacity(this.values.size()*2);
        }
    }
    
    /**
     * Adds a new value to the HashMap
     * Deletes the old values if the key is already present
     * 
     * @param key       The key
     * @param value     The value
     */
    public void addUnique(String key,String value){
        int pos = this.findKey(key, 0);
        while( pos != -1 ){
            this.delete(key, pos);
            
            pos = this.findKey(key,pos);
        }
        
        this.add(key, value);
    }
    
    /**
     * Updates the given key with the given value.
     * The value will be added if the key does not exist.
     * 
     * @param key       The key
     * @param value     The value
     * @param offset    The position to search from
     */
    public void update(String key,String value,int offset){
        int pos = this.findKey(key, offset);
        if( pos != -1 ){
            this.values.set(pos, value);
        }
        else {
            this.add(key, value);
        }
    }
    
    /**
     * Deletes the value with the given key
     * 
     * @param   key     The key to search on
     * @param   offset  The position to search from
     */
    public void delete(String key,int offset){
        int pos = this.findKey(key,offset);
        if( pos != -1 ){
            this.names.set(pos, null);
            this.values.set(pos,null);
            
            this.counter--;
        }
    }
    
    /**
     * Returns an enumeration of all the keys in the hashmap
     * 
     * @return an enumeration of all the keys in the hashmap. if the hashmap has no keys, an empty enumeration
     */
    public Enumeration getKeys() {
        ServerDetailEnumeration headernames = new ServerDetailEnumeration();
        
        for(int i=0; i<this.names.size(); i++){
            headernames.addName(this.names.get(i));
        }
        
        return headernames;
    }

    /**
     * Returns the number of elements 
     * 
     * @return The number of elements
     */
    public int size(){
        return this.counter;
    }

    /**
     * Returns a map representing the HashMap
     * 
     * @return  The map
     */
    public Map toMap() {
        ArrayList<String> namesTemp     = new ArrayList<String>();
        ArrayList<String> valuesTemp    = new ArrayList<String>();
        
        for(int i=0; i<this.lastPosition; i++){
            if( this.names.get(i) == null ) continue;
            
            namesTemp.add(this.names.get(i));
            valuesTemp.add(this.values.get(i));
        }
        
        namesTemp.trimToSize();
        valuesTemp.trimToSize();
        
        return  new StringMap(namesTemp,valuesTemp);
    }
}
