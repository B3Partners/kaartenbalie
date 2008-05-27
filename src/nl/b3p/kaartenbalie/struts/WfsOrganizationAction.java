/**
 * @(#)OrganizationAction.java
 * @author N. de Goeij
 * @version 1.00 2006/10/02
 *
 * Copy for WFS 2008/05/27
 * By Jytte
 *
 * Purpose: a Struts action class defining all the Action for the Organization view.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.struts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.accounting.entity.Account;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.wfs.v110.WfsLayer;
import nl.b3p.ogc.wfs.v110.WfsServiceProvider;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.datawarehousing.DwObjectAction;
import nl.b3p.wms.capabilities.ServiceProvider;
import nl.b3p.kaartenbalie.service.WfsLayerValidator;
import nl.b3p.kaartenbalie.service.ServiceProviderValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;
import org.dom4j.CDATA;
import org.hibernate.HibernateException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WfsOrganizationAction extends KaartenbalieCrudAction {
    
    private final static String SUCCESS = "success";
    private static final Log log = LogFactory.getLog(OrganizationAction.class);
    protected static final String ORGANIZATION_LINKED_ERROR_KEY = "error.organizationstilllinked";
    protected static final String CAPABILITY_WARNING_KEY = "warning.saveorganization";
    protected static final String ORG_NOTFOUND_ERROR_KEY = "error.organizationnotfound";
    protected static final String DELETE_ADMIN_ERROR_KEY = "error.deleteadmin";
    
    protected static final String USER_JOINED_KEY = "beheer.org.user.joined";
    protected static final String CREDITS_JOINED_KEY = "beheer.org.credits.joined";
    
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
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.createLists(dynaForm, request);
        prepareMethod(dynaForm, request, LIST, LIST);
        addDefaultMessage(mapping, request);
        return mapping.findForward(SUCCESS);
    }
    
    /* Edit method which handles all editable requests.
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
    // <editor-fold defaultstate="" desc="edit(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    public ActionForward edit(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Organization organization = getOrganization(dynaForm, request, false);
        if (organization == null) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, ORG_NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        populateOrganizationForm(organization, dynaForm, request);
        return super.edit(mapping, dynaForm, request, response);
    }
    // </editor-fold>
    
    /* Method for saving a new organization from input of a user.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     *
     * @return an Actionforward object.
     *
     * @throws Exception, HibernateException
     */
    public ActionForward save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws HibernateException, Exception {
        
        EntityManager em = getEntityManager();
        if (!isTokenValid(request)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        ActionErrors errors = dynaForm.validate(mapping, request);
        if(!errors.isEmpty()) {
            addMessages(request, errors);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, VALIDATION_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        Organization organization = getOrganization(dynaForm, request, true);
        if (null == organization) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        populateOrganizationObject(dynaForm, organization);
        
        /*
         * A warning has to be given if the organization has an invalid capability with the
         * selected layers.
         */
        /*if(!organization.getHasValidGetCapabilities()) {
            addAlternateMessage(mapping, request, null, CAPABILITY_WARNING_KEY);
        }*/
        
        if (organization.getId() == null) {
            em.persist(organization);
        } else {
            em.merge(organization);
        }
        em.flush();
        getDataWarehousing().enlist(Organization.class, organization.getId(), DwObjectAction.PERSIST_OR_MERGE);
        return super.save(mapping,dynaForm,request,response);
    }
    
    public ActionForward deleteConfirm(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        EntityManager em = getEntityManager();
        Organization organization = getOrganization(dynaForm, request, false);
        if (null == organization) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        MessageResources messages = getResources(request);
        Locale locale = getLocale(request);
        String userJoinedMessage = messages.getMessage(locale, USER_JOINED_KEY);
        String creditsJoinedMessage = messages.getMessage(locale, CREDITS_JOINED_KEY);
        StringBuffer strMessage = null;
        
        Set userList = organization.getUser();
        if(userList!=null && !userList.isEmpty()) {
            
            strMessage = new StringBuffer();
            
            Iterator it = userList.iterator();
            boolean notFirstUser = false;
            while (it.hasNext()) {
                User u = (User)it.next();
                if (notFirstUser)
                    strMessage.append(", ");
                else {
                    strMessage.append(userJoinedMessage);
                    strMessage.append(": ");
                    notFirstUser = true;
                }
                strMessage.append(u.getSurname());
                
            }
            addAlternateMessage(mapping, request, null, strMessage.toString());
        }
        
        Account acc = organization.getAccount();
        if (acc!=null) {
            BigDecimal cb = acc.getCreditBalance();
            if (cb!=null && cb.doubleValue()>0) {
                cb.setScale(2, RoundingMode.HALF_UP);
                strMessage = new StringBuffer();
                strMessage.append(creditsJoinedMessage);
                strMessage.append(": ");
                strMessage.append(cb.toString());
                addAlternateMessage(mapping, request, null, strMessage.toString());
            }
        }
        
        User sessionUser = (User) request.getUserPrincipal();
        Organization sessionOrg = null;
        if (sessionUser!=null) 
            sessionOrg = sessionUser.getOrganization();
        if(sessionOrg!=null && sessionOrg.getId().equals(organization.getId())) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, DELETE_ADMIN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }

        prepareMethod(dynaForm, request, DELETE, EDIT);
        addDefaultMessage(mapping, request);
        return getDefaultForward(mapping, request);
    }
    
    /* Method for deleting an organization selected by a user.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     *
     * @return an Actionforward object.
     *
     * @throws Exception, HibernateException
     */
    public ActionForward delete(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws HibernateException, Exception {
        
        EntityManager em = getEntityManager();
        if (!isTokenValid(request)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        Organization organization = getOrganization(dynaForm, request, false);
        if (null == organization) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        User sessionUser = (User) request.getUserPrincipal();
        Organization sessionOrg = null;
        if (sessionUser!=null) 
            sessionOrg = sessionUser.getOrganization();
        if(sessionOrg!=null && sessionOrg.getId().equals(organization.getId())) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, DELETE_ADMIN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }

        em.remove(organization);
        em.flush();
        getDataWarehousing().enlist(Organization.class, organization.getId(), DwObjectAction.REMOVE);
        return super.delete(mapping, dynaForm, request, response);
    }
    
    /* Creates a list with the available layers.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     *
     * @throws HibernateException, JSONException, Exception
     */
// <editor-fold defaultstate="" desc="create(DynaValidatorForm form, HttpServletRequest request) method.">
    public ActionForward create(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws HibernateException, JSONException, Exception {
        request.setAttribute("layerList", createTree());
        return super.create(mapping, dynaForm, request, response);
    }
// </editor-fold>
    
    /* Creates a list with the available layers.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     *
     * @throws HibernateException, JSONException, Exception
     */
// <editor-fold defaultstate="" desc="createLists(DynaValidatorForm form, HttpServletRequest request) method.">
    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws HibernateException, JSONException, Exception {
        super.createLists(form, request);
        
        EntityManager em = getEntityManager();
        List organizationlist = em.createQuery("from Organization").getResultList();
        request.setAttribute("organizationlist", organizationlist);
    }
// </editor-fold>
    
//-------------------------------------------------------------------------------------------------------
// PRIVATE METHODS
//-------------------------------------------------------------------------------------------------------
    
    /* Method which returns the organization with a specified id.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param createNew A boolean which indicates if a new object has to be created.
     * @param id An Integer indicating which organization id has to be searched for.
     *
     * @return an Organization object.
     */
    private Organization getOrganization(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew) {
        
        EntityManager em = getEntityManager();
        Organization organization = null;
        Integer id = getID(dynaForm);
        
        if(null == id && createNew) {
            organization = new Organization();
        } else if (null != id) {
            organization = (Organization)em.find(Organization.class, id);
        }
        
        return organization;
    }
    
    /* Method which gets the hidden id in a form.
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
    private Integer getID(DynaValidatorForm dynaForm) {
        return FormUtils.StringToInteger(dynaForm.getString("id"));
    }
    
    /* Method which will fill the JSP form with the data of  a given organization.
     *
     * @param organization Organization object from which the information has to be printed.
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     */
// <editor-fold defaultstate="" desc="populateOrganizationForm(Organization organization, DynaValidatorForm dynaForm, HttpServletRequest request) method.">
    private void populateOrganizationForm(Organization organization, DynaValidatorForm dynaForm, HttpServletRequest request) throws JSONException {
        dynaForm.set("id", organization.getId().toString());
        dynaForm.set("name", organization.getName());
        dynaForm.set("street", organization.getStreet());
        dynaForm.set("number", organization.getNumber());
        dynaForm.set("addition", organization.getAddition());
        dynaForm.set("postalcode", organization.getPostalcode());
        dynaForm.set("province", organization.getProvince());
        dynaForm.set("country", organization.getCountry());
        dynaForm.set("postbox", organization.getPostbox());
        dynaForm.set("billingAddress", organization.getBillingAddress());
        dynaForm.set("visitorsAddress", organization.getVisitorsAddress());
        dynaForm.set("telephone", organization.getTelephone());
        dynaForm.set("fax", organization.getFax());
        dynaForm.set("bbox", organization.getBbox());
        dynaForm.set("code", organization.getCode());
        
        Set l = organization.getWfsOrganizationLayer();
        Object [] organizationLayer = l.toArray();
        String checkedLayers = "";
        for (int i = 0; i < organizationLayer.length; i++) {
            if (i < organizationLayer.length - 1) {
                checkedLayers += ((WfsLayer)organizationLayer[i]).getUniqueName() + ",";
            } else {
                checkedLayers += ((WfsLayer)organizationLayer[i]).getUniqueName();
            }
        }
        JSONObject root = this.createTree();
        request.setAttribute("layerList", root);
        request.setAttribute("checkedLayers", checkedLayers);
        
    }
// </editor-fold>
    
    /* Method that fills an organization object with the user input from the forms.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param organization Organization object that to be filled
     * @param layerList List with all the layers
     * @param selectedLayers String array with the selected layers for this organization
     */
// <editor-fold defaultstate="" desc="populateOrganizationObject(DynaValidatorForm dynaForm, Organization organization, List layerList, String [] selectedLayers) method.">
    private void populateOrganizationObject(DynaValidatorForm dynaForm, Organization organization) throws Exception {
        
        EntityManager em = getEntityManager();
        organization.setName(FormUtils.nullIfEmpty(dynaForm.getString("name")));
        organization.setStreet(FormUtils.nullIfEmpty(dynaForm.getString("street")));
        organization.setNumber(FormUtils.nullIfEmpty(dynaForm.getString("number")));
        organization.setAddition(FormUtils.nullIfEmpty(dynaForm.getString("addition")));
        organization.setPostalcode(FormUtils.nullIfEmpty(dynaForm.getString("postalcode")));
        organization.setProvince(FormUtils.nullIfEmpty(dynaForm.getString("province")));
        organization.setCountry(FormUtils.nullIfEmpty(dynaForm.getString("country")));
        organization.setPostbox(FormUtils.nullIfEmpty(dynaForm.getString("postbox")));
        organization.setBillingAddress(FormUtils.nullIfEmpty(dynaForm.getString("billingAddress")));
        organization.setVisitorsAddress(FormUtils.nullIfEmpty(dynaForm.getString("visitorsAddress")));
        organization.setTelephone(FormUtils.nullIfEmpty(dynaForm.getString("telephone")));
        organization.setFax(FormUtils.nullIfEmpty(dynaForm.getString("fax")));
        
        String bbox = FormUtils.nullIfEmpty(dynaForm.getString("bbox"));
        if(bbox != null) {
            String [] boxxvalues = bbox.split(",");
            if(boxxvalues.length != 4) {
                log.error("BBOX wrong size: " + boxxvalues.length);
                throw new Exception(KBConfiguration.BBOX_EXCEPTION + " Usage: minx,miny,maxx,maxy");
            }
            
            double minx=0.0, miny=0.0, maxx=-1.0, maxy=-1.0;
            try {
                minx = Double.parseDouble(boxxvalues[0]);
                miny = Double.parseDouble(boxxvalues[1]);
                maxx = Double.parseDouble(boxxvalues[2]);
                maxy = Double.parseDouble(boxxvalues[3]);
                if (minx > maxx || miny > maxy) {
                    log.error("BBOX values out of range (minx, miny, maxx, maxy): " + minx+ ", "+ miny+ ", "+maxx+ ", "+maxy);
                    throw new Exception("BBOX values out of range (minx, miny, maxx, maxy): " + minx+ ", "+ miny+ ", "+maxx+ ", "+maxy);
                }
            } catch (Exception e) {
                log.error("BBOX error minx, miny, maxx, maxy: " + minx+ ", "+ miny+ ", "+maxx+ ", "+maxy);
                throw new Exception(KBConfiguration.BBOX_EXCEPTION + " Usage: minx,miny,maxx,maxy");
            }
        }
        organization.setBbox(bbox);
        organization.setCode(FormUtils.nullIfEmpty(dynaForm.getString("code")));
        
        Set layers = new HashSet();
        Set serviceProviders = new HashSet();
        
        String [] selectedLayers = (String [])dynaForm.get("selectedLayers");
        int size = selectedLayers.length;
        for(int i = 0; i < size; i++) {
            // nieuwe methode voor maken
            WfsLayer l = getWfsLayerByUniqueName(selectedLayers[i]);
            if (l==null)
                continue;
            layers.add(l);
            WfsServiceProvider sp = l.getWfsServiceProvider();
            if (!serviceProviders.contains(sp))
                serviceProviders.add(sp);
        }
        
        /* There is a possibility that some serviceproviders do not support the same SRS's or image formats.
         * Some might have compatibility some others not. To make sure this wont give any problems, we need to
         * check which formats and srs's are the same. If and only if this complies we can say for sure that
         * the GetCapabilities request which is going to be sent to the client is valid. In all other cases
         * we need to give a warning that the GetCapabilities can have problems when used with certain viewers.
         *
         * In order to give the user the same warning as the supervisor and in order to keep the administration
         * up to date a boolean hasValidGetCapabilities will be set to false if a GetCapabilities is not stictly
         * according to the WMS rules. This will prevent the user from being kept in the dark if something doesn't
         * work properly.
         */
        //WfsLayerValidator lv = new WfsLayerValidator(layers);
        //ServiceProviderValidator spv = new ServiceProviderValidator(serviceProviders);
        
        //organization.setHasValidGetCapabilities(lv.validate() && spv.validate());
        
        organization.setWfsOrganizationLayer(layers);
    }
// </editor-fold>
    
    /* Creates a JSON tree from a list of serviceproviders from the database.
     *
     * @param layers Set of layers from which the part of the tree ahs to be build
     * @param organizationLayers Set of restrictions which define the visible and non visible layers
     * @param parent JSONObject which represents the parent object to which this set of layers should be added
     *
     * @throws JSONException
     */
// <editor-fold defaultstate="" desc="createTree() method.">
    private JSONObject createTree() throws JSONException {
        
        EntityManager em = getEntityManager();
        List serviceProviders =em.createQuery("from WfsServiceProvider sp order by sp.abbr").getResultList();
        
        JSONArray rootArray = new JSONArray();
        Iterator it = serviceProviders.iterator();
        while (it.hasNext()) {
            WfsServiceProvider sp = (WfsServiceProvider)it.next();
            JSONObject parentObj = this.serviceProviderToJSON(sp);
            HashSet set= new HashSet();
            Set layers = sp.getWfsLayers();
            set.addAll(layers);
            
            parentObj = createTreeList(set, parentObj);
                if (parentObj.has("children")){
                    rootArray.put(parentObj);
                }
        }
        
        JSONObject root = new JSONObject();
        root.put("name","root");
        root.put("children", rootArray);
        return root;
    }
// </editor-fold>
    
    /* Creates a JSON tree list of a given set of Layers and a set of restrictions
     * of which layer is visible and which isn't.
     *
     * @param layers Set of layers from which the part of the tree ahs to be build
     * @param organizationLayers Set of restrictions which define the visible and non visible layers
     * @param parent JSONObject which represents the parent object to which this set of layers should be added
     *
     * @throws JSONException
     */
// <editor-fold defaultstate="" desc="createTreeList(Set layers, Set organizationLayers, JSONObject parent) method.">
    private JSONObject createTreeList(Set layers, JSONObject parent) throws JSONException {
        /* This method has a recusive function in it. Its function is to create a list of layers
         * in a tree like array which can be used to build up a menu structure.
         */
        Iterator layerIterator = layers.iterator();
        JSONArray parentArray = new JSONArray();
        while (layerIterator.hasNext()) {
            /* For each layer in the set we are going to create a JSON object which we will add to de total
             * list of layer objects.
             */
            WfsLayer layer = (WfsLayer)layerIterator.next();
            
            /* When we have retrieved this array we are able to save our object we are working with
             * at the moment. This object is our present layer object. This object first needs to be
             * transformed into a JSONObject, which we do by calling the method to do so.
             */
            JSONObject layerObj = this.layerToJSON(layer);
            
            /* After creating the JSONObject for this layer and if necessary, filling this
             * object with her childs, we can add this JSON layer object back into its parent array.
             */
            parentArray.put(layerObj);
            
        }
        if (parentArray.length() > 0){
            parent.put("children", parentArray);
        }
        return parent;
    }
// </editor-fold>
    
    /* Creates a JSON object from the ServiceProvider with its given name and id.
     *
     * @param serviceProvider The ServiceProvider object which has to be converted
     *
     * @return JSONObject
     *
     * @throws JSONException
     */
// <editor-fold defaultstate="" desc="serviceProviderToJSON(ServiceProvider serviceProvider) method.">
    private JSONObject serviceProviderToJSON(WfsServiceProvider serviceProvider) throws JSONException {
        JSONObject root = new JSONObject();
        root.put("id", serviceProvider.getId());
        root.put("name", serviceProvider.getGivenName());
        root.put("type", "wfsserviceprovider");
        return root;
    }
// </editor-fold>
    
    /* Creates a JSON object from the Layer with its given name and id.
     *
     * @param layer The Layer object which has to be converted
     *
     * @return JSONObject
     *
     * @throws JSONException
     */
// <editor-fold defaultstate="" desc="layerToJSON(Layer layer) method.">
    private JSONObject layerToJSON(WfsLayer layer) throws JSONException{
        
        JSONObject jsonLayer = new JSONObject();
        jsonLayer.put("name", layer.getTitle());
        String name = layer.getUniqueName();
        if (name==null) {
            jsonLayer.put("id", layer.getTitle().replace(" ",""));
            jsonLayer.put("type", "placeholder");
        } else {
            jsonLayer.put("id", name);
            jsonLayer.put("type", "layer");
        }
        return jsonLayer;
    }
// </editor-fold>
}
