/**
 * @(#)MyDatabase.java
 * @author C. van Lith
 * @version 1.00 2006/03/03
 *
 * Purpose: a class checking basic authority for users.
 *
 * @copyright 2007 All rights reserved. B3Partners
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

public class SecurityRealm implements SecurityRealmInterface {
    private final Log log = LogFactory.getLog(this.getClass());
    
    /** Checks wether an user, given his username and password, is allowed to use the system.
     *
     * @param username String representing the username.
     * @param password String representing the password.
     *
     * @return a principal object containing the user if he has been found as a registered user. Otherwise this object wil be empty (null).
     */
    // <editor-fold defaultstate="" desc="authenticate(String username, String password) method.">
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
    // </editor-fold>
    
    /** Checks if a user is in the given role. A role represents a level of priviliges.
     *
     * @param principal Principal object representing the user.
     * @param role String representing the role this user has to checked against.
     *
     * @return a boolean which is true if the user is in the defined role otherwise false is returned.
     */
    // <<editor-fold defaultstate="" defaultstate="collapsed" desc="isUserInRole(Principal principal, String role) method.">
    public boolean isUserInRole(Principal principal, String role) {
        if(!(principal instanceof User)) {
            return false;
        }
        User user = (User)principal;
        //log.info("Check user principal has role");
        return user.getRole().equalsIgnoreCase(role);
    }
    // </editor-fold>
}