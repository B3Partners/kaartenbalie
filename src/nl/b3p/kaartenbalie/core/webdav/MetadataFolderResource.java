package nl.b3p.kaartenbalie.core.webdav;

import com.bradmcevoy.http.CollectionResource;
import com.bradmcevoy.http.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.persistence.EntityManager;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.wms.capabilities.Layer;

/**
 *
 * @author Chris
 */
public class MetadataFolderResource extends MetadataResource implements CollectionResource {

    protected Map<String, Resource> childs = null;

    public Resource child(String childName) {
        if (childs == null) {
            createChildmap();
            if (childs == null) {
                return null;
            }
        }
        return childs.get(childName);
    }

    public List<Resource> getChildren() {
        if (childs == null) {
            createChildmap();
            if (childs == null) {
                return null;
            }
        }
        List<Resource> l = new ArrayList<Resource>();
        l.addAll(childs.values());
        return l;
    }

    protected void createChildmap() {
        childs = new TreeMap<String, Resource>();
        Object identity = null;
        try {
            identity = MyEMFDatabase.createEntityManager(MyEMFDatabase.INIT_EM);
            log.debug("Getting entity manager ......");
            EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.INIT_EM);

            User lUser = em.find(User.class, user.getId());
            Set orgs = lUser.getAllOrganizations();
            Iterator it = orgs.iterator();
            while (it.hasNext()) {
                Organization org = (Organization) it.next();
                Set ll = org.getLayers();

                Iterator it2 = ll.iterator();
                while (it2.hasNext()) {
                    Layer l = (Layer) it2.next();
                    // N+1 select voor LayerMetadata, indien niet te veel performance
                    // kost geen probleem
                    if (l.getMetadata() != null) {
                        childs.put(MetadataResourceFactory.createName(l),
                                new MetadataFileResource(l));
                    }
                }
            }
        } catch (Throwable e) {
            log.warn("Error creating EntityManager: ", e);
        } finally {
            log.debug("Closing entity manager .....");
            MyEMFDatabase.closeEntityManager(identity, MyEMFDatabase.INIT_EM);
        }
    }
}
