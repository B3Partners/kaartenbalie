package general;

import java.security.Principal;

/**
 *
 * @author rachelle
 */
public class PrincipalStub implements Principal{
    private String name = "";
    
    public void setName(String name){
        this.name   = name;
    }
    
    public String getName() {
        return this.name;
    }
    
}
