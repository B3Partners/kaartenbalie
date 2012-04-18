package nl.b3p.kaartenbalie.dwr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.service.LayerTreeSupport;
import nl.b3p.ogc.ServiceProviderInterface;
import nl.b3p.ogc.wfs.v110.WfsLayer;
import nl.b3p.ogc.wfs.v110.WfsServiceProvider;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.Roles;
import nl.b3p.wms.capabilities.ServiceProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Methodes die vanuit struts pagina's worden aangeroepen om reload te voorkomen.
 *
 * @author Chris
 */
public class RightsSupport {

    private static final Log log = LogFactory.getLog(RightsSupport.class);

    public String getRightsTree(Map<String, String> params) throws Exception {
        if (!checkAccess()) {
            return null;
        }

        Organization org = getOrganization(params);
        ServiceProviderInterface sp = getServiceProvider(params);
        JSONObject tree = populateRightsTree(sp, org);

        return tree.toString();
    }

    private JSONObject populateRightsTree(ServiceProviderInterface sp, Organization org) throws Exception {

        JSONObject header = new JSONObject();
        header.put("name", "Tree Header");
        header.put("id", "header");
        JSONArray headerArray = new JSONArray();
        header.put("children", headerArray);

        if (sp==null) {
            return header;
        }

        JSONObject root = new JSONObject();
        root.put("name", sp.getGivenName());
        root.put("id", "ogc" + sp.getId().toString());
        root.put("type", "serviceprovider");
        headerArray.put(root);

        if (org==null) {
            return header;
        }

        Set layerSet = new HashSet();
        if (sp instanceof ServiceProvider) {
            Set<Layer> orgWmsLayers = org.getLayers();
            Layer topLayer = ((ServiceProvider) sp).getTopLayer();
            if (topLayer != null) {
                layerSet.add(topLayer);
                root = LayerTreeSupport.createTreeList(layerSet, orgWmsLayers, root, false);
            }

        } else if (sp instanceof WfsServiceProvider) {
            Set<WfsLayer> orgWfsLayers = org.getWfsLayers();
            Set layers = ((WfsServiceProvider) sp).getWfsLayers();
            layerSet.addAll(layers);
            root = LayerTreeSupport.createWfsTreeList(layerSet, orgWfsLayers, root, false);

        }
        return header;
    }

    public List<String> submitRightsForm(Map<String, String> params) throws Exception {
        if (!checkAccess()) {
            return null;
        }

        Organization org = getOrganization(params);
        ServiceProviderInterface sp = getServiceProvider(params);
        String selectedLayers = params.get("selectedLayers");

        List<String> layers = new ArrayList<String>();
        if (org==null) {
            return layers;
        }
        Set wmsLayers = new HashSet();
        Set wfsLayers = new HashSet();

        // add layers from other sp's
        Set<Layer> orgWmsLayerSet = org.getLayers();
        if (orgWmsLayerSet != null) {
            for (Layer l : orgWmsLayerSet) {
                ServiceProvider layerSp = l.getServiceProvider();
                if (sp != null && layerSp != null && !layerSp.getAbbr().equals(sp.getAbbr())) {
                    wmsLayers.add(l);
                    String lname = l.getName() + " (wms, " + l.getServiceProvider().getAbbr() + ")";
                    layers.add(lname);
                }
            }
        }
        Set<WfsLayer> orgWfsLayerSet = org.getWfsLayers();
        if (orgWfsLayerSet != null) {
            for (WfsLayer l : orgWfsLayerSet) {
                WfsServiceProvider layerSp = l.getWfsServiceProvider();
                if (!layerSp.getAbbr().equals(sp.getAbbr())) {
                    wfsLayers.add(l);
                    String lname = l.getName() + " (wfs, " + l.getWfsServiceProvider().getAbbr() + ")";
                    layers.add(lname);
                }
            }
        }

        EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.DWR_EM);
        // add selected layers from current sp
        if (selectedLayers!=null && selectedLayers.length()> 0){
            String[] selectedLayersArray = selectedLayers.split(",");
            int size = selectedLayersArray.length;
            for (int i = 0; i < size; i++) {
                String layerName = selectedLayersArray[i];
                WfsLayer wfsl = LayerTreeSupport.getWfsLayerByUniqueName(em, layerName);
                if (wfsl != null) {
                    wfsLayers.add(wfsl);
                    String lname = wfsl.getName() + " (wfs, " + wfsl.getWfsServiceProvider().getAbbr() + ")";
                    layers.add(lname);
                }
                Layer wmsl = LayerTreeSupport.getLayerByUniqueName(em, layerName);
                if (wmsl != null) {
                    wmsLayers.add(wmsl);
                    String lname = wmsl.getName() + " (wms, " + wmsl.getServiceProvider().getAbbr() + ")";
                    layers.add(lname);
                }
            }
        }

        org.setWfsLayers(wfsLayers);
        org.setLayers(wmsLayers);

        Collections.sort(layers);
        return layers;

    }

    public List<String> getValidLayers(Map<String, String> params) throws Exception {
        if (!checkAccess()) {
            return null;
        }

        List<String> layers = new ArrayList<String>();

        Organization org = getOrganization(params);
        if (org==null) {
            return layers;
        }
        
        Set<Layer> orgWmsLayerSet = org.getLayers();
        if (orgWmsLayerSet != null) {
            for (Layer l : orgWmsLayerSet) {
                String lname = l.getName() + " (wms, " + l.getServiceProvider().getAbbr() + ")";
                layers.add(lname);
            }
        }
        Set<WfsLayer> orgWfsLayerSet = org.getWfsLayers();
        if (orgWfsLayerSet != null) {
            for (WfsLayer l : orgWfsLayerSet) {
                String lname = l.getName() + " (wfs, " + l.getWfsServiceProvider().getAbbr() + ")";
                layers.add(lname);
            }
        }

        Collections.sort(layers);
        return layers;

    }

    private Organization getOrganization(Map<String, String> params) throws Exception {
        Integer orgId = FormUtils.StringToInteger(params.get("orgId"));
        if (orgId == null) {
            return null;
        }
        EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.DWR_EM);
        return (Organization) em.find(Organization.class, orgId);
    }

    private ServiceProviderInterface getServiceProvider(Map<String, String> params) throws Exception {
        Integer spId = FormUtils.StringToInteger(params.get("id"));
        String spType = params.get("type");
        if (spId == null || spId.intValue() == 0) {
            return null;
        }
        if (spType == null) {
            return null;
        }

        EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.DWR_EM);

        ServiceProviderInterface sp = null;
        if ("WMS".equalsIgnoreCase(spType)) {
            sp = (ServiceProviderInterface) em.find(ServiceProvider.class, new Integer(spId.intValue()));
        } else if ("WFS".equalsIgnoreCase(spType)) {
            sp = (ServiceProviderInterface) em.find(WfsServiceProvider.class, new Integer(spId.intValue()));
        }

        return sp;
    }

    private boolean checkAccess() {
        WebContext ctx = WebContextFactory.get();
        HttpServletRequest request = null;
        if (ctx != null) {
            request = ctx.getHttpServletRequest();
        }
        User user = (User) request.getUserPrincipal();
        if (user == null || !user.checkRole(Roles.ADMIN)) {
            return false;
        }
        return true;
    }
}
