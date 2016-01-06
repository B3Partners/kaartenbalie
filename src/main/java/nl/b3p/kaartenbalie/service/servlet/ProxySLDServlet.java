package nl.b3p.kaartenbalie.service.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.service.URLCache;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import nl.b3p.ogc.sld.SldWriter;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.ServiceProvider;
import nl.b3p.wms.capabilities.Style;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Chris van Lith
 */
public class ProxySLDServlet extends GeneralServlet {

    public static final String PARAM_ORIGINAL_SLD_URL = "oriSldUrl";
    public static final String PARAM_ORIGINAL_SLD_BODY = "oriSldBody";
    public static final String PARAM_SERVICEPROVIDER_ID = "servProvId";
    public static final String PARAM_STYLES = "styles";
    public static final String mimeType = "application/xml";

    private static final URLCache cache = new URLCache(60000);
    private static final Log log = LogFactory.getLog(ProxySLDServlet.class);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Object identity = null;
        try {
            identity = MyEMFDatabase.createEntityManager(MyEMFDatabase.MAIN_EM);
            log.debug("Getting entity manager ......");
            EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);

            String oriSldBody = request.getParameter(PARAM_ORIGINAL_SLD_BODY);
            log.debug("Incoming sld body: " + oriSldBody);
            String oriSldUrl = request.getParameter(PARAM_ORIGINAL_SLD_URL);
            log.debug("Incoming sld url: " + oriSldUrl);
            String styleString = request.getParameter(PARAM_STYLES);
            log.debug("Incoming sld style id's: " + styleString);
            String spId = request.getParameter(PARAM_SERVICEPROVIDER_ID);
            log.debug("Incoming sld sp id: " + styleString);

            SldWriter sldFact = new SldWriter();
            
            if (oriSldBody != null && oriSldBody.length() > 0) {
                sldFact.parseString(oriSldBody, null);
                
            } else if (oriSldUrl != null && oriSldUrl.length() > 0) {
                String sld = cache.getFromCache(oriSldUrl);
                sldFact.parseString(sld, null);
                
            } else if (styleString != null && styleString.length() > 0) {
                //haal de styles op.
                List<Style> styles = em.createQuery("from Style where id in (" + styleString + ")")
                        .getResultList();
                sldFact.addNamedLayers(sldFact.createNamedLayersWithKBStyles(styles));
            }
            
            // Gebruik alleen het deel uit de SLD dat betrekking heeft op
            // deze service provider, zoek daarom abbr en layer names op
            // als sp is null dan alles meenemen
            if (spId != null && spId.length() > 0) {
                String spAbbr = null;
                List<String> spLayerNames = new ArrayList<String>();
                Integer servProvId = null;
                try {
                    servProvId = new Integer(spId);
                    ServiceProvider sp = em.find(ServiceProvider.class, servProvId);
                    if (sp != null) {
                        spAbbr = sp.getAbbr();
                        Iterator<Layer> it2 = sp.getAllLayers().iterator();
                        while (it2.hasNext()) {
                            Layer l = it2.next();
                            spLayerNames.add(l.getName());
                        }
                    }
                } catch (NumberFormatException e) {
                    log.debug("Fout parsen serviceprovider id.");
                }
                sldFact.replaceAndFilterOnNames(spAbbr, spLayerNames);
            }
            
            String xml = sldFact.createSLD();
            response.setContentType(mimeType);
            log.debug("Returned sld: \n" + xml);
            response.getWriter().write(xml);

        } catch (Exception ex) {
            response.getWriter().write("Error while getting SLD. Check the log for details");
            log.error("Cannot proxy SLD:",ex);
        } finally {
            log.debug("Closing entity manager .....");
            MyEMFDatabase.closeEntityManager(identity, MyEMFDatabase.MAIN_EM);
        }
    }

    public static void addSLDToCache(String sldUrl) {
        cache.cacheUrl(sldUrl, false);
    }

    public static void clearCache() {
        cache.clearCache();
    }

    @Override
    public void parseRequestAndData(DataWrapper data, User user) throws IllegalArgumentException, UnsupportedOperationException, IOException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getServletInfo() {
        return "";
    }
}
