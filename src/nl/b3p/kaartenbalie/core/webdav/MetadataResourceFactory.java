package nl.b3p.kaartenbalie.core.webdav;

import com.bradmcevoy.http.Resource;
import com.bradmcevoy.http.ResourceFactory;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.persistence.EntityManager;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.wms.capabilities.Layer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Chris
 */
public class MetadataResourceFactory implements ResourceFactory {

    private final Log log = LogFactory.getLog(this.getClass());

    public Resource getResource(String host, String url) {

        int lcPos = url.lastIndexOf("/");
        if (lcPos < url.length() - 1) {
            String lcFrag = url.substring(lcPos + 1);
            lcPos = lcFrag.indexOf(".");
            if (lcPos > 0) {
                String layerCode = lcFrag.substring(0, lcPos);
                return new MetadataFileResource(layerCode);
            }

        }
        return new MetadataFolderResource();
    }

    public String getSupportedLevels() {
        return "1";
    }

    public static String createName(Layer l) {
        return l.getSpAbbr() + "_" + l.getName() + ".xml";
    }
}
