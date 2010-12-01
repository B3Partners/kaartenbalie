package nl.b3p.kaartenbalie.struts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.commons.struts.ExtendedMethodProperties;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.ogc.ServiceProviderInterface;
import nl.b3p.ogc.wfs.v110.WfsLayer;
import nl.b3p.ogc.wfs.v110.WfsServiceProvider;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.ServiceProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import org.hibernate.HibernateException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author B3Partners
 */
public class RightsAction extends KaartenbalieCrudAction {

    private static final Log log = LogFactory.getLog(RightsAction.class);

    /* Execute method which handles all unspecified requests.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     *
     * @return an Actionforward object.
     *
     * @throws Exception
     */
    @Override
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        prepareMethod(dynaForm, request, LIST, LIST);
        populateForm(null, null, dynaForm, request);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return mapping.findForward(SUCCESS);
    }

    @Override
    public ActionForward edit(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServiceProviderInterface sp = getServiceProvider(dynaForm);
        Organization org = getOrganization(dynaForm);

        prepareMethod(dynaForm, request, EDIT, LIST);
        populateForm(sp, org, dynaForm, request);

       if (sp == null || org == null) {
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        populateRightsTree(sp, org, dynaForm, request);
        return getDefaultForward(mapping, request);
    }

    public ActionForward save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws HibernateException, Exception {

        ServiceProviderInterface sp = getServiceProvider(dynaForm);
        Organization org = getOrganization(dynaForm);

        prepareMethod(dynaForm, request, LIST, LIST);
        populateForm(sp, org, dynaForm, request);

        if (sp == null || org == null) {
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }

        if (!isTokenValid(request)) {
            addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }

        populateOrganizationLayers(dynaForm, sp, org);

        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();

        em.merge(org);
        em.flush();


        prepareMethod(dynaForm, request, LIST, EDIT);
        populateForm(sp, org, dynaForm, request);
//        populateRightsTree(sp, org, dynaForm, request);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);

        return getDefaultForward(mapping, request);
    }

    private void populateOrganizationLayers(DynaValidatorForm dynaForm, ServiceProviderInterface sp, Organization org) throws Exception {
        Set wmsLayers = new HashSet();
        Set wfsLayers = new HashSet();

        // add layers from other sp's
        Set<Layer> orgWmsLayerSet = org.getLayers();
        if (orgWmsLayerSet != null) {
            for (Layer l : orgWmsLayerSet) {
                ServiceProvider layerSp = l.getServiceProvider();
                if (!layerSp.getAbbr().equals(sp.getAbbr())) {
                    wmsLayers.add(l);
                }
            }
        }
        Set<WfsLayer> orgWfsLayerSet = org.getWfsLayers();
        if (orgWfsLayerSet != null) {
            for (WfsLayer l : orgWfsLayerSet) {
                WfsServiceProvider layerSp = l.getWfsServiceProvider();
                if (!layerSp.getAbbr().equals(sp.getAbbr())) {
                    wfsLayers.add(l);
                }
            }
        }

        // add selected layers from current sp
        String[] selectedLayers = (String[]) dynaForm.get("selectedLayers");
        int size = selectedLayers.length;
        for (int i = 0; i < size; i++) {
            String layerName = selectedLayers[i];
            WfsLayer wfsl = getWfsLayerByUniqueName(layerName);
            if (wfsl != null) {
                wfsLayers.add(wfsl);
            }
            Layer wmsl = getLayerByUniqueName(layerName);
            if (wmsl != null) {
                wmsLayers.add(wmsl);
            }
        }

        org.setWfsLayers(wfsLayers);
        org.setLayers(wmsLayers);
    }

    protected void populateForm(ServiceProviderInterface sp, Organization organization, DynaValidatorForm dynaForm, HttpServletRequest request) throws Exception {
         if (sp==null) {
            List spDtoList = (List)request.getAttribute("serviceproviderlist");
            if (spDtoList!=null && !spDtoList.isEmpty()) {
                sp = (ServiceProviderInterface)spDtoList.get(0);
            }
        }
        if (organization==null) {
            List organizationlist = (List)request.getAttribute("organizationlist");
            if (organizationlist!=null && !organizationlist.isEmpty()) {
                organization = (Organization)organizationlist.get(0);
            }
        }
        if (sp!=null) {
        dynaForm.set("id", sp.getId().toString());
        dynaForm.set("type", sp.getType());
        }
        if (organization!=null) {
        dynaForm.set("orgId", organization.getId().toString());
        dynaForm.set("orgName", organization.getName());
        }
    }

    protected void populateRightsTree(ServiceProviderInterface sp, Organization organization, DynaValidatorForm dynaForm, HttpServletRequest request) throws Exception {

        JSONObject header = new JSONObject();
        header.put("name", "Tree Header");
        header.put("id", "header");
        JSONArray headerArray = new JSONArray();
        header.put("children", headerArray);

        JSONObject root = new JSONObject();
        root.put("name", sp.getGivenName());
        root.put("id", "ogc" + sp.getId().toString());
        root.put("type", "serviceprovider");
        headerArray.put(root);

        StringBuilder checkedLayers = new StringBuilder();
        Set layerSet = new HashSet();
        if (sp instanceof ServiceProvider) {

            Set wmsLayers = organization.getLayers();
            Object[] wmsOrganizationLayer = wmsLayers.toArray();
            for (int i = 0; i < wmsOrganizationLayer.length; i++) {
                checkedLayers.append(",");
                checkedLayers.append(((Layer) wmsOrganizationLayer[i]).getUniqueName());
            }

            Layer topLayer = ((ServiceProvider) sp).getTopLayer();
            if (topLayer != null) {
                layerSet.add(topLayer);
                root = createTreeList(layerSet, null, root, false);
            }

        } else if (sp instanceof WfsServiceProvider) {

            Set wfsLayers = organization.getWfsLayers();
            Object[] wfsOrganizationLayer = wfsLayers.toArray();
            for (int i = 0; i < wfsOrganizationLayer.length; i++) {
                checkedLayers.append(",");
                checkedLayers.append(((WfsLayer) wfsOrganizationLayer[i]).getUniqueName());
            }

            Set layers = ((WfsServiceProvider) sp).getWfsLayers();
            layerSet.addAll(layers);
            root = createWfsTreeList(layerSet, root, null, false);

        }

        if (checkedLayers.length() > 0) {
            request.setAttribute("checkedLayers", checkedLayers.substring(1));
        }
        request.setAttribute("layerList", header);
    }

    @Override
    protected void createLists(DynaValidatorForm form, HttpServletRequest request)
            throws HibernateException,
            JSONException,
            Exception {
        super.createLists(form, request);
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();

        List organizationlist = em.createQuery("from Organization order by name").getResultList();
        request.setAttribute("organizationlist", organizationlist);

        List wfsSpList = em.createQuery("from WfsServiceProvider order by given_name").getResultList();
        List wfsDtoList = ServiceProviderDTO.createList(wfsSpList);
        List wmsSpList = em.createQuery("from ServiceProvider order by given_name").getResultList();
        List wmsDtoList = ServiceProviderDTO.createList(wmsSpList);

        List spDtoList = new ArrayList();

        if (wmsDtoList != null)
            spDtoList.addAll(wmsDtoList);

        if (wfsDtoList != null)
            spDtoList.addAll(wfsDtoList);

        if (spDtoList.size() > 0)
            Collections.sort(spDtoList);

        request.setAttribute("serviceproviderlist", spDtoList);

    }

    protected ServiceProviderInterface getServiceProvider(DynaValidatorForm dynaForm) throws Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        Integer id = FormUtils.StringToInteger(dynaForm.getString("id"));
        if (id == null || id.intValue() == 0) {
            return null;
        }
        String type = FormUtils.nullIfEmpty(dynaForm.getString("type"));
        if (type == null) {
            return null;
        }
        ServiceProviderInterface serviceProvider = null;
        if ("WMS".equalsIgnoreCase(type)) {
            serviceProvider = (ServiceProviderInterface) em.find(ServiceProvider.class, new Integer(id.intValue()));
        } else if ("WFS".equalsIgnoreCase(type)) {
            serviceProvider = (ServiceProviderInterface) em.find(WfsServiceProvider.class, new Integer(id.intValue()));
        }
        return serviceProvider;
    }

    protected Organization getOrganization(DynaValidatorForm dynaForm) throws Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        Organization organization = null;
        Integer id = FormUtils.StringToInteger(dynaForm.getString("orgId"));
        ;

        if (null != id) {
            organization = (Organization) em.find(Organization.class, id);
        }
        return organization;
    }
}
