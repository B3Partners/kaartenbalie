package nl.b3p.kaartenbalie.service.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.xpath.XPathExpressionException;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.service.URLCache;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import nl.b3p.ogc.sld.SldNamedLayer;
import nl.b3p.ogc.sld.SldReader;
import nl.b3p.ogc.sld.SldWriter;
import nl.b3p.ogc.utils.OGCCommunication;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.ServiceProvider;
import nl.b3p.wms.capabilities.Style;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Roy
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
        EntityTransaction tx = null;

        try {
            identity = MyEMFDatabase.createEntityManager(MyEMFDatabase.MAIN_EM);
            EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);
            tx = em.getTransaction();

            tx.begin();

            String oriSldUrl = request.getParameter(PARAM_ORIGINAL_SLD_URL);
            String oriSldBody = request.getParameter(PARAM_ORIGINAL_SLD_BODY);
            String styleString = request.getParameter(PARAM_STYLES);

            log.debug("Incoming sld url: " + oriSldUrl);

            List<SldNamedLayer> returnedNamedLayers = new ArrayList<SldNamedLayer>();
            SldWriter sldFact = new SldWriter();

            if ((oriSldUrl != null && oriSldUrl.length() > 0)
                    || (oriSldBody != null && oriSldBody.length() > 0)) {

                String sld = null;
                if (oriSldUrl != null && oriSldUrl.length() > 0) {

                    try {
                        sld = cache.getFromCache(oriSldUrl);
                    } catch (InterruptedException ex) {
                        log.error("Fout ophalen sld uit cache: ", ex);
                    }

                } else {
                    sld = oriSldBody;
                }

                if (sld == null) {
                    response.getWriter().write("Error while getting SLD. Check the log for details");
                    return;
                }

                Integer servProvId = null;
                try {
                    servProvId = new Integer(request.getParameter(PARAM_SERVICEPROVIDER_ID));
                } catch (NumberFormatException e) {
                    log.debug("Fout parsen serviceprovider id.");
                }

                ServiceProvider sp = em.find(ServiceProvider.class, servProvId);
                //TODO: moet beter. Char set bepalen niet meegeven.
                SldReader sldReader = new SldReader();

                List<SldNamedLayer> namedLayers = new ArrayList();
                try {
                    namedLayers = sldReader.getNamedLayersBySld(sld, "UTF-8");
                } catch (Exception ex) {
                    log.error("Fout ophalen named layers: ", ex);
                }

                Iterator<SldNamedLayer> it = namedLayers.iterator();
                while (it.hasNext()) {
                    SldNamedLayer nl = it.next();

                    try {
                        String[] codeAndName = OGCCommunication
                                .toCodeAndName(nl.getName());

                        if (codeAndName[0] != null
                                && codeAndName[0].equals(sp.getAbbr())) {

                            nl.setName(codeAndName[1]);
                            returnedNamedLayers.add(nl);

                            /* 
                             *Als er geen afkorting in namedlayer zit is
                             * codeAndName[0] null. Kijken of name overeenkomt 
                             * met een layer uit service ?
                             */
                        } else if (codeAndName[0] == null && codeAndName[1] != null) {

                            Iterator<Layer> it2 = sp.getAllLayers().iterator();
                            while (it2.hasNext()) {
                                Layer l = it2.next();

                                if (l.getName().equals(codeAndName[1])) {
                                    nl.setName(codeAndName[1]);
                                    returnedNamedLayers.add(nl);
                                }
                            }
                        }

                    } catch (Exception e) {
                        log.error("Fout bij maken SLD", e);
                    }
                }
            }

            /**/
            if (styleString != null && styleString.length() > 0) {
                String[] styleStringTokens = styleString.split(",");
                Integer[] styleIds = new Integer[styleStringTokens.length];
                for (int i = 0; i < styleStringTokens.length; i++) {
                    styleIds[i] = new Integer(styleStringTokens[i]);
                }
                //haal de styles op.
                List<Style> styles = em.createQuery("from Style where id in (" + styleString + ")")
                        .getResultList();

                try {
                    returnedNamedLayers.addAll(sldFact.createNamedLayersWithKBStyles(styles));
                } catch (XPathExpressionException ex) {
                    log.error("Fout in xpath: ", ex);
                } catch (Exception ex) {
                    log.error("Fout: ", ex);
                }
            }

            /**/
            String xml = sldFact.createSLD(returnedNamedLayers);
            response.setContentType(mimeType);

            log.debug("Returned sld: \n" + xml);

            response.getWriter().write(xml);

            tx.commit();
        } catch (Exception ex) {
            log.error("Fout ophalen em: ", ex);
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
