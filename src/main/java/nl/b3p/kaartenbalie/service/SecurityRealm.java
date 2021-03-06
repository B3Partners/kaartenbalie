/*
 * B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
 * for authentication/authorization, pricing and usage reporting.
 *
 * Copyright 2006, 2007, 2008 B3Partners BV
 *
 * This file is part of B3P Kaartenbalie.
 *
 * B3P Kaartenbalie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * B3P Kaartenbalie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.b3p.kaartenbalie.service;

import java.security.Principal;
import java.util.Date;
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
    @Override
    public Principal authenticate(String username, String password) {

        String encpw = null;
        try {
            encpw = KBCrypter.encryptText(password);
        } catch (Exception ex) {
            log.error("error encrypting password: ", ex);
        }
        Object identity = null;
        EntityTransaction tx = null;
        try {
            identity = MyEMFDatabase.createEntityManager(MyEMFDatabase.REALM_EM);
            EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.REALM_EM);
            tx = em.getTransaction();
            tx.begin();
            try {
                User user = (User) em.createQuery(
                        "from User u where " +
                        "u.timeout > :nu " +
                        "and lower(u.username) = lower(:username) " +
                        "and u.password = :password")
                        .setParameter("nu", new Date())
                        .setParameter("username", username)
                        .setParameter("password", encpw)
                        .getSingleResult();
                // if we get a result, this means successful login
                // set lastloginstatus to null to indicate success
                user.setLastLoginStatus(null);
                
                return user;
            } catch (NoResultException nre) {
                log.debug("No results using encrypted password");
            }
            // if login not succesful, set userstate to LOGIN_STATE_WRONG_PASSW
                    User wrong_password_user = (User) em.createQuery(
                    "from User u where "
                    + "u.timeout > :nu "
                    + "and lower(u.username) = lower(:username) ")
                    .setParameter("nu", new Date())
                    .setParameter("username", username)
                    .getSingleResult();
                    wrong_password_user.setLastLoginStatus(User.LOGIN_STATE_WRONG_PASSW_OR_ACCOUNT_EXPIRED);
                    em.flush();
            log.warn("Login failure for username " + username);
        } catch(Exception e) {
            log.error("Exception checking user credentails", e);
            if(tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            if(tx != null && tx.isActive() && !tx.getRollbackOnly()) {
                tx.commit();
            }
            MyEMFDatabase.closeEntityManager(identity, MyEMFDatabase.REALM_EM);
        }

        return null;
    }

    @Override
    public Principal getAuthenticatedPrincipal(String username, String password) {
        Object identity = null;
        EntityTransaction tx = null;
        try {
            identity = MyEMFDatabase.createEntityManager(MyEMFDatabase.REALM_EM);
            EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.REALM_EM);
            tx = em.getTransaction();
            tx.begin();
            try {
                User user = (User) em.createQuery(
                        "from User u where " +
                        "lower(u.username) = lower(:username) ").setParameter("username", username).getSingleResult();
                return user;
            } catch (NoResultException nre) {
                return null;
            }
        } catch(Exception e) {
            log.error("Exception getting authenticated user from database", e);
            if(tx != null && tx.isActive()) {
                tx.rollback();
            }
        } finally {
            if(tx != null && tx.isActive() && !tx.getRollbackOnly()) {
                tx.commit();
            }
            MyEMFDatabase.closeEntityManager(identity, MyEMFDatabase.REALM_EM);
        }
        return null;
    }

    /** Checks if a user is in the given role. A role represents a level of priviliges.
     *
     * @param principal Principal object representing the user.
     * @param role String representing the role this user has to checked against.
     *
     * @return a boolean which is true if the user is in the defined role otherwise false is returned.
     */
    @Override
    public boolean isUserInRole(Principal principal, String role) {
        if (!(principal instanceof User)) {
            return false;
        }
        User user = (User) principal;
        //log.info("Check user principal has role");
        return user.checkRole(role);
    }
}