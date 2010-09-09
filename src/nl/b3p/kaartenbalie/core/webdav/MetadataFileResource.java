package nl.b3p.kaartenbalie.core.webdav;

import com.bradmcevoy.http.Auth;
import com.bradmcevoy.http.GetableResource;
import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.Request;
import com.bradmcevoy.http.Request.Method;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.wms.capabilities.Layer;

/**
 *
 * @author Chris
 */
public class MetadataFileResource extends MetadataResource implements GetableResource {

    protected Layer layer = null;

    public MetadataFileResource(Layer l) {
        init(l);
    }

    public MetadataFileResource(String layerCode) {
        try {
            init(getLayerByUniqueName(layerCode));
        } catch (Exception ex) {
            log.error("no layer found for metadata resource, cause: " + ex.getLocalizedMessage());
        }
    }

    protected void init(Layer l) {
        if (l == null) {
            return;
        }
        layer = l;
        String uid = MetadataResourceFactory.createName(l);
        Date date = layer.getServiceProvider().getUpdatedDate();
        super.init(uid, date, new Date());
    }

    public void sendContent(OutputStream out, Range arg1, Map<String, String> arg2) throws IOException {
        if (layer == null || layer.getMetadata() == null) {
            throw new IOException("No metadata found!");
        }
        out.write(layer.getMetadata().getBytes("utf-8"));
    }

    public Long getMaxAgeSeconds() {
        return 60l;
    }

    public String getContentType(String arg0) {
        return "text/xml";
    }

    public Long getContentLength() {
        if (layer == null || layer.getMetadata() == null) {
            return new Long("0");
        }
        return new Long(layer.getMetadata().length());
    }

    public boolean authorise(Request request, Method method, Auth auth) {
        if (!super.authorise(request, method, auth)) {
            return false;
        }
        Object identity = null;
        try {
            identity = MyEMFDatabase.createEntityManager(MyEMFDatabase.INIT_EM);
            log.debug("Getting entity manager ......");
            EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.INIT_EM);

            User lUser = em.find(User.class, user.getId());
            Set orgs = lUser.getOrganizations();
            Organization mainOrg = lUser.getMainOrganization();
            if (!orgs.contains(mainOrg)) {
                orgs.add(mainOrg);
            }
            Iterator it = orgs.iterator();
            while (it.hasNext()) {
                Organization org = (Organization) it.next();
                Set ll = org.getLayers();
                Iterator it2 = ll.iterator();
                while (it2.hasNext()) {
                    Layer l = (Layer) it2.next();
                    if (l.equals(layer)) {
                        return true;
                    }
                }
            }
        } catch (Throwable e) {
            log.warn("Error creating EntityManager: ", e);
        } finally {
            log.debug("Closing entity manager .....");
            MyEMFDatabase.closeEntityManager(identity, MyEMFDatabase.INIT_EM);
        }
        return false;
    }

    private Layer getLayerByUniqueName(String uniqueName) throws Exception {
        Object identity = null;
        try {
            identity = MyEMFDatabase.createEntityManager(MyEMFDatabase.INIT_EM);
            log.debug("Getting entity manager ......");
            EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.INIT_EM);

            int pos = uniqueName.indexOf("_");
            if (pos == -1 || uniqueName.length() <= pos + 1) {
                throw new Exception("Unique layer name not valid: " + uniqueName);
            }
            String spAbbr = uniqueName.substring(0, pos);
            String layerName = uniqueName.substring(pos + 1);
            if (spAbbr.length() == 0 || layerName.length() == 0) {
                throw new Exception("Unique layer name not valid: " + spAbbr + ", " + layerName);
            }

            String query = "from Layer where name = :layerName and serviceProvider.abbr = :spAbbr";
            List ll = em.createQuery(query).setParameter("layerName", layerName).setParameter("spAbbr", spAbbr).getResultList();

            if (ll == null || ll.isEmpty()) {
                return null;
            }
            // Dit is nodig omdat mysql case insensitive selecteert
            Iterator it = ll.iterator();
            while (it.hasNext()) {
                Layer l = (Layer) it.next();
                String dbLayerName = l.getName();
                String dbSpAbbr = l.getSpAbbr();
                if (dbLayerName != null && dbSpAbbr != null) {
                    if (dbLayerName.equals(layerName) && dbSpAbbr.equals(spAbbr)) {
                        return l;
                    }
                }
            }
        } catch (Throwable e) {
            log.warn("Error creating EntityManager: ", e);
        } finally {
            log.debug("Closing entity manager .....");
            MyEMFDatabase.closeEntityManager(identity, MyEMFDatabase.INIT_EM);
        }
        return null;
    }
}
