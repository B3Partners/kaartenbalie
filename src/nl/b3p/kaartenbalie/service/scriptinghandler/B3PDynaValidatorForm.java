package nl.b3p.kaartenbalie.service.scriptinghandler;


import java.util.HashMap;
import javax.measure.converter.ConversionException;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author rachelle
 */
public class B3PDynaValidatorForm extends DynaValidatorForm {
    private HashMap<String,Object> values;
    
    public B3PDynaValidatorForm(){
        super();
        
        values  = new HashMap<String,Object>();
    }
    
    @Override
    public void set(String name,Object value) throws ConversionException,IllegalArgumentException ,NullPointerException{
        values.put(name, value);
    }
    
    @Override
    public Object get(String name) throws IllegalArgumentException, NullPointerException{
        if( !values.containsKey(name) ) throw new IllegalArgumentException("Key "+name+" does not exist!");
        
        return values.get(name);
    }
}