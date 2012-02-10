package general;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author rachelle
 */
public class StringMap implements Map{
    private String[] keys;
    private String[][] values;
    
    public StringMap(ArrayList<String> keys,ArrayList<String> values){
        this.keys              = new String[keys.size()];
        this.values            = new String[values.size()][1];
        
        for(int i=0; i<keys.size(); i++){
            this.keys[i]      = keys.get(i);
            this.values[i][0] = values.get(i);
        }
    }
    
    public int size() {
        return this.keys.length;
    }

    public boolean isEmpty() {
        return this.keys.length == 0;
    }

    public boolean containsKey(Object name) {
        if( !(name instanceof String) ) return false;
        String key = (String) name;
        
        for(int i=0; i<this.keys.length; i++){
            if( this.keys[i].equals(key) ) return true;
        }
        
        return false;
    }

    public boolean containsValue(Object value) {
        if( !(value instanceof String[]) ) return false;
        
        String[] check  = (String[]) value;
        
        for(int i=0; i<this.values.length; i++){
            if( this.values[i] == check ) return true;
        }
        
        return false;
    }

    public Object get(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object put(Object k, Object v) {
        return null;
    }

    public Object remove(Object o) {
        return null;
    }

    public void putAll(Map map) {
        
    }

    public void clear() {
        
    }

    public Set keySet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection values() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set entrySet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
