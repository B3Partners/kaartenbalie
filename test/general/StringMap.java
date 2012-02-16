package general;

import java.util.*;

/**
 *
 * @author rachelle
 */
public class StringMap implements Map{
    private String[] keys;
    private String[][] values;
    
    /**
     * Generates a empty StringMap
     */
    public StringMap(){
        this(new ArrayList<String>(),new ArrayList<String>());
    }
    
    /**
     * Generates a new StringMap with the given keys and values
     * 
     * @param keys      The keys to add
     * @param values    The values to add
     */
    public StringMap(ArrayList<String> keys,ArrayList<String> values){
        this.keys              = new String[keys.size()];
        this.values            = new String[values.size()][1];
        
        for(int i=0; i<keys.size(); i++){
            this.keys[i]      = keys.get(i);
            this.values[i][0] = values.get(i);
        }
    }
    
    /**
     * Returns the number of key-value mappings in this map. If the map contains more than Integer.MAX_VALUE elements, returns Integer.MAX_VALUE. 
     * 
     * @return  the number of key-value mappings in this map
     */
    public int size() {
        return this.keys.length;
    }

    /**
     * Returns true if this map contains no key-value mappings. 
     * 
     * @return true if this map contains no key-value mappings, otherwise false
     */
    public boolean isEmpty() {
        return this.keys.length == 0;
    }

    /**
     * if this map contains a mapping for the specified key 
     * 
     * @param key key whose presence in this map is to be tested , must be a string
     * @return True if the key contains the given key, otherwise false
     * @throws ClassCastException   if the key is of an inappropriate type for this map (optional) 
     * @throws NullPointerException if the specified key is null and this map does not permit null keys
     */
    public boolean containsKey(Object key) throws ClassCastException, NullPointerException {
        if( key == null )   throw new NullPointerException("Key may not be null");
        if( !(key instanceof String) ) throw new ClassCastException("Wrong type for key. Expected String");
        
        String check = (String) key;
        
        for(int i=0; i<this.keys.length; i++){
            if( this.keys[i].equals(check) ) return true;
        }
        
        return false;
    }

    /**
     * Returns true if this map maps one or more keys to the specified value
     * 
     * @param value  value whose presence in this map is to be tested, must be a string array
     * @return true if this map maps one or more keys to the specified value , otherwise false
     * @throws ClassCastException   if the value is of an inappropriate type for this map (optional) 
     * @throws NullPointerException if the specified value is null and this map does not permit null values
     */
    public boolean containsValue(Object value) {
        if( value == null ) throw new NullPointerException("Value may not be null");
        if( !(value instanceof String[]) ) throw new ClassCastException("Wrong type for key. Expected String[]");
        
        String[] check  = (String[]) value;
        
        for(int i=0; i<this.values.length; i++){
            if( this.values[i] == check ) return true;
        }
        
        return false;
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key. 
     * 
     * @param key   the key whose associated value is to be returned 
     * @return the value to which the specified key is mapped, or null if this map contains no mapping for the key 
     * @throws ClassCastException   if the value is of an inappropriate type for this map (optional) 
     * @throws NullPointerException if the specified value is null and this map does not permit null values
     */
    public Object get(Object key)  throws ClassCastException, NullPointerException {
        if( key == null )   throw new NullPointerException("Key may not be null");
        if( !(key instanceof String) ) throw new ClassCastException("Wrong type for key. Expected String");
        String check = (String) key;
        
        for(int i=0; i<this.keys.length; i++){
            if( this.keys[i].equals(check) ) return this.values[i];
        }
        
        return null;
    }

    /**
     * Read only map. put(Object key,Object value) is not supported
     * 
     * @param key   key with which the specified value is to be associated  
     * @param value value to be associated with the specified key 
     * @return  null
     * @throws UnsupportedOperationException if the put operation is not supported by this map 
     */
    public Object put(Object key, Object value) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Map is read only");
    }

    /**
     * Read only map. remove(Object key) is not supported
     * 
     * @param key   key whose mapping is to be removed from the map 
     * @return null
     * @throws  UnsupportedOperationException if the remove operation is not supported by this map
     */
    public Object remove(Object key) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Map is read only");
    }

    /**
     * Read only map.  putAll(Map map) is not supported
     * 
     * @param map   mappings to be stored in this map 
     * @throws UnsupportedOperationException if the putAll operation is not supported by this map 
     */
    public void putAll(Map map) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Map is read only");
    }

    /**
     * Read only map.  clear() is not supported
     * 
     * @throws UnsupportedOperationException if the putAll operation is not supported by this map 
     */
    public void clear() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Map is read only");
    }

    /**
     * Returns a Set view of the keys contained in this map. The set is backed by the map, so changes to the map are reflected in the set, and vice-versa. 
     * 
     * @return a set view of the keys contained in this map
     */
    public Set keySet() {
        HashSet<String> set = new HashSet<String>();
        set.addAll(Arrays.asList(this.keys));
        
        return set;
    }

    /**
     * Returns a Collection view of the values contained in this map. The collection is backed by the map, so changes to the map are reflected in the collection, and vice-versa.
     * 
     * @return a collection view of the values contained in this map
     */
    public Collection values() {
        HashSet<String[]> set = new HashSet<String[]>();
        set.addAll(Arrays.asList(this.values));
        
        return set;
    }

    /**
     * Returns a Set view of the mappings contained in this map. The set is backed by the map, so changes to the map are reflected in the set, and vice-versa. 
     * 
     * @return a set view of the mappings contained in this map
     */
    public Set entrySet() {
        HashSet<AbstractMap.SimpleEntry> set = new HashSet<AbstractMap.SimpleEntry>();
        for(int i=0; i<this.keys.length; i++){
            set.add(new AbstractMap.SimpleEntry(this.keys[i],this.values[i]));
        }
        
        return set;
    }
    
}
