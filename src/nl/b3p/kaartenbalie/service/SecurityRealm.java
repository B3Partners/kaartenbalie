/*
 * FrameworkSecurityRealm.java
 *
 * Created on 3 maart 2006, 10:38
 *
 */

package nl.b3p.kaartenbalie.service;

import java.security.Principal;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.service.MyDatabase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.securityfilter.realm.SecurityRealmInterface;




/**
 *
 * @author Chris
 */
public class SecurityRealm implements SecurityRealmInterface {
    private final Log log = LogFactory.getLog(this.getClass());
    
    public Principal authenticate(String username, String password) {
        Session sess = MyDatabase.getSessionFactory().getCurrentSession();
        Transaction tx = sess.beginTransaction();
        try {
            User user = (User)sess.createQuery(
                    "from User u where " +
                    "lower(u.username) = lower(:username) " +
                    "and lower(u.password) = lower(:password)")
                .setParameter("username", username)
                .setParameter("password", password)
                .uniqueResult();
            if(user != null) {
                Hibernate.initialize(user.getRole());
            }
            return user;
        } finally {
            tx.commit();
        }
    }
    public boolean isUserInRole(Principal principal, String role) {
        if(!(principal instanceof User)) {
            return false;
        }
        User user = (User)principal;
        log.info("Check user principal has role");
        return user.getRole().equalsIgnoreCase(role);
    }   
}