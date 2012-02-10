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
    
    public void addLocale(Locale locale){
        super.add(locale);
    }
    
    @Override
    public Locale nextElement(){        
        return (Locale) super.nextElement();
    }
}
