/**
 * @author C. van Lith
 * @version 1.00 2006/03/03
 *
 * Purpose: a class checking basic authority for users.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.service;

import java.security.Principal;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.ogc.utils.KBCrypter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.securityfilter.realm.ExternalAuthenticatedRealm;
import org.securityfilter.realm.SecurityRealmInterface;

public class SecurityRealm implements SecurityRealmInterface, ExternalAuthenticatedRealm {
    private final Log log = LogFactory.getLog(this.getClass());
    
    
    public SecurityRealm() {
    }
    /** Checks wether an user, given his username and password, is allowed to use the system.
     *
     * @param username String representing the username.
     * @param password String representing the password.
     *
     * @return a principal object containing the user if he has been found as a registered user. Otherwise this object wil be empty (null).
     */
    public Principal authenticate(String username, String password) {
        
        String encpw = null;
        try {
            encpw = KBCrypter.encryptText(password);
        } catch (Exception ex) {
            log.error("error encrypting password: ", ex);
        }
        EntityManager em = MyEMFDatabase.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            User user = (User)em.createQuery(
                    "from User u where " +
                    "lower(u.username) = lower(:username) " +
                    "and u.password = :password")
                    .setParameter("username", username)
                    .setParameter("password", encpw)
                    .getSingleResult();
            return user;
        } catch (NoResultException nre) {
            log.debug("No results using encrypted password");
        } finally {
            tx.commit();
            em.close();
        }
        
        em = MyEMFDatabase.createEntityManager();
        tx = em.getTransaction();
        tx.begin();
        try {
            User user = (User)em.createQuery(
                    "from User u where " +
                    "lower(u.username) = lower(:username) " +
                    "and lower(u.password) = lower(:password)")
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getSingleResult();
            
            // Volgende keer dus wel encrypted
            user.setPassword(encpw);
            em.merge(user);
            em.flush();
            log.debug("Cleartext password encrypted!");
            return user;
        } catch (NoResultException nre) {
            log.debug("No results using cleartext password");
        } finally {
            tx.commit();
            em.close();
        }
        
        return null;
    }
    
    public Principal getAuthenticatedPrincipal(String username) {
        EntityManager em = MyEMFDatabase.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            User user = (User)em.createQuery(
                    "from User u where " +
                    "lower(u.username) = lower(:username) ")
                    .setParameter("username", username)
                    .getSingleResult();
            return user;
        } catch (NoResultException nre) {
            return null;
        } finally {
            tx.commit();
            em.close();
        }
        
    }
    
    /** Checks if a user is in the given role. A role represents a level of priviliges.
     *
     * @param principal Principal object representing the user.
     * @param role String representing the role this user has to checked against.
     *
     * @return a boolean which is true if the user is in the defined role otherwise false is returned.
     */
    public boolean isUserInRole(Principal principal, String role) {
        if(!(principal instanceof User)) {
            return false;
        }
        User user = (User)principal;
        //log.info("Check user principal has role");
        return user.checkRole(role);
    }
}