package nl.b3p.kaartenbalie.core.webdav;

import com.bradmcevoy.http.Auth;
import com.bradmcevoy.http.PropFindableResource;
import com.bradmcevoy.http.Request;
import com.bradmcevoy.http.Request.Method;
import com.bradmcevoy.http.Resource;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.ogc.utils.KBCrypter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Chris
 */
public abstract class MetadataResource implements PropFindableResource {

    protected final Log log = LogFactory.getLog(this.getClass());
    protected String uniqueId = null;
    protected String name = null;
    protected Date modificationDate = null;
    protected Date creationDate = null;
    protected User user = null;

    protected MetadataResource() {
        init("metadata", new Date());
    }

    protected void init(String uid, Date creationDate) {
        this.creationDate = creationDate;
        this.modificationDate = creationDate;
        this.uniqueId = uid;
        this.name = uid;
    }

    public Object authenticate(String username, String password) {
        String encpw = null;
        try {
            encpw = KBCrypter.encryptText(password);
        } catch (Exception ex) {
            log.error("error encrypting password: ", ex);
        }
        Object identity = null;
        try {
            identity = MyEMFDatabase.createEntityManager(MyEMFDatabase.REALM_EM);
            log.debug("Getting entity manager ......");
            EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.REALM_EM);
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            try {
                User user = (User) em.createQuery(
                        "from User u where " +
                        "lower(u.username) = lower(:username) " +
                        "and u.password = :password").setParameter("username", username).setParameter("password", encpw).getSingleResult();
                return user;
            } catch (NoResultException nre) {
                log.debug("No results using encrypted password");
            } finally {
                tx.commit();
            }
        } catch (Throwable e) {
            log.warn("Error creating EntityManager: ", e);
        } finally {
            log.debug("Closing entity manager .....");
            MyEMFDatabase.closeEntityManager(identity, MyEMFDatabase.REALM_EM);
        }
        return null;
    }

    public boolean authorise(Request request, Method method, Auth auth) {
        if (auth == null || auth.getTag() == null) {
            return false;
        }
        user = (User) auth.getTag();
        return true;
    }

    public String getRealm() {
        return "Kaartenbalie Metadata";
    }

    public String checkRedirect(Request arg0) {
        return null;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public String getName() {
        return name;
    }

    public Date getModifiedDate() {
        return modificationDate;
    }

    public int compareTo(Resource o) {
        return this.getName().compareTo(o.getName());
    }

    public Date getCreateDate() {
        return creationDate;
    }
}
