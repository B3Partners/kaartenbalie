package general;

import java.util.Locale;

/**
 *
 * @author rachelle
 */
public class ServerLocaleEnumeration extends ServerGeneralEnumeration{
    public ServerLocaleEnumeration(){
        super();
    }
    
    /**
     * Adds a new Locale
     * 
     * @param locale The new Locale
     */
    public void addLocale(Locale locale){
        super.add(locale);
    }
    
    /**
     * Returns the next element
     * 
     * @return The next element
     */
    @Override
    public Locale nextElement(){        
        return (Locale) super.nextElement();
    }
}
