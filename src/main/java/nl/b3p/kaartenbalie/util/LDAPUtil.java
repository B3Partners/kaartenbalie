package nl.b3p.kaartenbalie.util;

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import javax.persistence.EntityManager;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.wms.capabilities.Roles;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Boy de Wit
 */
public class LDAPUtil {
    
    protected static Log log = LogFactory.getLog(LDAPUtil.class);

    public LDAPUtil() {
    }

    public boolean userInLdap(String host, int port, String user, String password,
            String suffix) {

        String userDN = user + suffix;
        try {
            LDAPConnection conn = new LDAPConnection(host, port, userDN, password);

            return true;
        } catch (LDAPException ex) {
            log.debug("Fout tijdens bind ldap: ", ex);
        }

        return false;
    }

    public Date getDefaultTimeOut(int months) {
        Calendar gc = new GregorianCalendar();
        gc.setTimeZone(TimeZone.getTimeZone("GMT"));
        gc.add(Calendar.MONTH, months);

        return gc.getTime();
    }

    public Organization getLDAPOrg(EntityManager em, String orgName) {
        Organization org = null;

        try {
            org = (Organization) em.createQuery(
                    "from Organization o where "
                    + "lower(o.name) = lower(:orgName) ")
                    .setParameter("orgName", orgName)
                    .getSingleResult();
            
        } catch (Exception ex) {
            log.debug("Fout tijdens ophalen ldap org: ", ex);
        }

        return org;
    }

    public User getUserByName(EntityManager em, String username) {
        User user = null;
        try {
            user = (User) em.createQuery("from User u where "
                    + "lower(u.username) = lower(:username) ")
                    .setParameter("username", username)
                    .getSingleResult();

        } catch (Exception ex) {
            log.debug("Fout tijdens ophalen ldap user uit db: ", ex);
        }

        return user;
    }

    public List<Roles> getGebruikerRol(EntityManager em) {
        return em.createQuery("from Roles where id in (:ids)")
                .setParameter("ids", 2)
                .getResultList();
    }
}
