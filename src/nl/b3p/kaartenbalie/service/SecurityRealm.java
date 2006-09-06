/*
 * FrameworkSecurityRealm.java
 *
 * Created on 3 maart 2006, 10:38
 *
 */

package nl.b3p.lignl.viewer.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import nl.b3p.commons.encryption.Encryption;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.securityfilter.realm.SimpleSecurityRealmBase;

/**
 *
 * @author Chris
 */
public class SecurityRealm extends SimpleSecurityRealmBase {
    
    private static final long RECHECK_DELAY = 300000l;
    
    private final Log log = LogFactory.getLog(this.getClass());
    private static Hashtable usertimestamps = new Hashtable();
    private static Hashtable userroles = new Hashtable();
    
    public boolean booleanAuthenticate(String username, String password) {
        log.debug("Trying to login: " + username + " with password: " + password);
        if (username==null || password==null){
            return false;
        }
        return (checkTimestamp(username) || checkWithFramework(username, password));
    }
    
    public boolean isUserInRole(String username, String role) {
        log.debug("Checking role: " + role + " for user: " + username);
        //voor testen
        //kleine test om toch te vullen (voor test)
        if(username==null||username.length() < 1){
            username="gebruikersnaam";
        }
                if (username==null || role==null){
               return false;
        }
        if (userroles.containsKey(username)) {
            ArrayList roleList = (ArrayList) userroles.get(username);
            if (roleList.contains(role)) {
                log.debug("  OK!");
                return true;
            }
        }
        return false;
    }
    
    private boolean checkTimestamp(String username) {
        Date ts = (Date)usertimestamps.get(username);
        if (ts==null)
            return false;
        long lts = ts.getTime();
        long ltsn = (new Date()).getTime();
        if (lts + RECHECK_DELAY > ltsn)
            return true;
        return false;
    }
    
    private boolean checkWithFramework(String username, String password) {
        //dummie voor het krijgen van role
        //kleine test om toch te vullen (voor test)
        if(username==null||username.length() < 1){
            username="gebruikersnaam";
        }
        log.debug("toevoegen of update timestamp, user: " + username);
        usertimestamps.put(username, new Date());
        
        
        ArrayList roleList = new ArrayList();
        roleList.add("gebruiker");
        roleList.add("beheerder");
        
        userroles.put(username, roleList);
            
        return true;
    }
        

    
}
