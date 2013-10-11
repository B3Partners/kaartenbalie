package nl.b3p.kaartenbalie.core.webdav;

import com.bradmcevoy.http.Resource;
import com.bradmcevoy.http.ResourceFactory;
import nl.b3p.ogc.utils.OGCCommunication;
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
     
        return OGCCommunication.attachSp(l.getSpAbbr(), l.getName()) + ".xml";
    }
}
