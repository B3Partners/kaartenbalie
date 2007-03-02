/**
 * @(#)OrganizationAction.java
 * @author N. de Goeij
 * @version 1.00 2006/10/02
 *
 * Purpose: a Struts action class defining all the Action for the Organization view.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.struts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.kaartenbalie.core.server.Layer;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.SRS;
import nl.b3p.kaartenbalie.core.server.ServiceProvider;
import nl.b3p.kaartenbalie.service.LayerValidator;
import nl.b3p.kaartenbalie.service.MyDatabase;
import org.apache.commons.validator.Form;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.validator.DynaValidatorForm;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrganizationAction extends KaartenbalieCrudAction {
    
    /* forward name="success" path="" */
    private final static String SUCCESS = "success";
    
    
    /** Execute method which handles all executable requests.
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
    // <editor-fold defaultstate="collapsed" desc="execute(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer id = FormUtils.StringToInteger(dynaForm.getString("id"));
        Organization organization = this.getOrganization(dynaForm, request, false, id);
        
        if (null != organization) {
            Set l = organization.getOrganizationLayer();
            
            Object [] organizationLayer = l.toArray();            
            String [] layerid = new String[l.size()];
            
            for (int i = 0; i < organizationLayer.length; i++) {
                layerid [i] = ((Layer)organizationLayer[i]).getId().toString();
            }
            String checkedLayers = "";
            for (int i = 0; i < organizationLayer.length; i++) {
                if (i < organizationLayer.length - 1) {
                    checkedLayers += ((Layer)organizationLayer[i]).getId().toString() + "_" + ((Layer)organizationLayer[i]).getName() + ",";
                } else {
                    checkedLayers += ((Layer)organizationLayer[i]).getId().toString() + "_" + ((Layer)organizationLayer[i]).getName();
                }
            }
            request.setAttribute("checkedLayers",checkedLayers);
            this.populateOrganizationForm(organization, dynaForm, request);
        }
        return super.unspecified(mapping, dynaForm, request, response);
    }
    // </editor-fold>
    
    /** Method which returns the organization with a specified id.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param createNew A boolean which indicates if a new object has to be created.
     * @param id An Integer indicating which organization id has to be searched for.
     *
     * @return an Organization object.
     */
    // <editor-fold defaultstate="collapsed" desc="getOrganization(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew, Integer id) method.">
    private Organization getOrganization(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew, Integer id) {
        Session session = getHibernateSession();
        Organization organization = null;
        
        if(null == id && createNew) {
            organization = new Organization();
        } else if (null != id) {
            try {
                organization = (Organization)session.createQuery(
                        "from Organization o where " +
                        "lower(o.id) = lower(:id) ").setParameter("id", id).uniqueResult();
            } catch(Exception e){}
            //organization = (Organization)session.load(Organization.class, new Integer(id));
        }
        return organization;
    }
    // </editor-fold>
    
    /** Method which will fill the JSP form with the data of  a given organization.
     *
     * @param organization Organization object from which the information has to be printed.
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     */
    // <editor-fold defaultstate="collapsed" desc="populateOrganizationForm(Organization organization, DynaValidatorForm dynaForm, HttpServletRequest request) method.">
    private void populateOrganizationForm(Organization organization, DynaValidatorForm dynaForm, HttpServletRequest request) {
        dynaForm.set("name", organization.getName());
        dynaForm.set("organizationStreet", organization.getStreet());
        dynaForm.set("organizationNumber", organization.getNumber());
        dynaForm.set("organizationAddition", organization.getAddition());
        dynaForm.set("organizationPostalcode", organization.getPostalcode());
        dynaForm.set("organizationProvince", organization.getProvince());
        dynaForm.set("organizationCountry", organization.getCountry());
        dynaForm.set("organizationPostbox", organization.getPostbox());
        dynaForm.set("organizationBillingAddress", organization.getBillingAddress());
        dynaForm.set("organizationVisitorsAddress", organization.getVisitorsAddress());
        dynaForm.set("organizationTelephone", organization.getTelephone());
        dynaForm.set("organizationFax", organization.getFax()); 
    }
    // </editor-fold>
    
    
    
    /** Method that fills an organization object with the user input from the forms.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param organization Organization object that to be filled
     * @param layerList List with all the layers
     * @param layerSelected String array with the selected layers for this organization
     */
    // <editor-fold defaultstate="collapsed" desc="populateOrganizationObject(DynaValidatorForm dynaForm, Organization organization, List layerList, String [] layerSelected) method.">
    private void populateOrganizationObject(DynaValidatorForm dynaForm, Organization organization, String [] selectedLayers) {// List layerList, String layerSelected) {
        organization.setName(FormUtils.nullIfEmpty(dynaForm.getString("name")));
        organization.setStreet(FormUtils.nullIfEmpty(dynaForm.getString("organizationStreet")));
        organization.setNumber(FormUtils.nullIfEmpty(dynaForm.getString("organizationNumber")));
        organization.setAddition(FormUtils.nullIfEmpty(dynaForm.getString("organizationAddition")));
        organization.setPostalcode(FormUtils.nullIfEmpty(dynaForm.getString("organizationPostalcode")));
        organization.setProvince(FormUtils.nullIfEmpty(dynaForm.getString("organizationProvince")));
        organization.setCountry(FormUtils.nullIfEmpty(dynaForm.getString("organizationCountry")));
        organization.setPostbox(FormUtils.nullIfEmpty(dynaForm.getString("organizationPostbox")));
        organization.setBillingAddress(FormUtils.nullIfEmpty(dynaForm.getString("organizationBillingAddress")));
        organization.setVisitorsAddress(FormUtils.nullIfEmpty(dynaForm.getString("organizationVisitorsAddress")));
        organization.setTelephone(FormUtils.nullIfEmpty(dynaForm.getString("organizationTelephone")));
        organization.setFax(FormUtils.nullIfEmpty(dynaForm.getString("organizationFax")));
        
        List layerList = getHibernateSession().createQuery(
                "from Layer l left join fetch l.latLonBoundingBox left join fetch l.attribution").list();
        
        /* If a user selects layers from the treeview. He/she selects only sublayers. Because the parent
         * layers are not automaticaly selected too, we need to do this ourselfs. Therefore there must be
         * checked if a layer has any parents and if so this has to be checked recursively until there
         * aren't any parents anymore. Each of the parents found have to be added to the list of layers 
         * which are allowed to be requested.
         */
        int size = selectedLayers.length;
        Set <Layer> layers = new HashSet <Layer>();
        for(int i = 0; i < size; i++) {
            int select = Integer.parseInt(selectedLayers[i].substring(0, selectedLayers[i].indexOf("_")));
            //System.out.println("Select is : " +  select);
            Iterator it = layerList.iterator();
            while (it.hasNext()) {
                Layer layer = (Layer)it.next();
                if (layer.getId() == select) {
                    //layers.add(layer);
                    layers = getAllParentLayers(layer,  layers );
                    break;
                }
            }
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
        LayerValidator lv = new LayerValidator();
        lv.setLayers(layers);
        //lv.setLayers(layerList);
        boolean valid = lv.validate();
        organization.setHasValidGetCapabilities(false);
        organization.setOrganizationLayer(layers);
    }
    // </editor-fold>
    
    
    private void addSrsCount(HashMap hm, String srs){
        if (hm.containsKey(srs)){
            int i= ((Integer)hm.get(srs)).intValue()+1;
            hm.put(srs,new Integer(i));
        }else{
            hm.put(srs,new Integer("1"));
        }
    }
    
    /** Creates a list with the available layers.
     *
     * @param layer The layer of which we have to find the parent layers.
     * @param layers Set <Layer> with all direct and indirect parental layers..
     *
     * @return the same set Set <Layer> as given.
     */
    // <editor-fold defaultstate="collapsed" desc="getTopSRS(Layer layer) method.">
    private Set <SRS> getTopSRS(Layer layer) {
        System.out.println("In TOPSRS!");
        if(layer.getParent() != null) {
            System.out.println("Layer has a parent!");
            this.getTopSRS(layer.getParent());
        }
        System.out.println("Top is found!");
        return layer.getSrs();
    }
    // </editor-fold>
    
    
    /** Creates a list with the available layers.
     *
     * @param layer The layer of which we have to find the parent layers.
     * @param layers Set <Layer> with all direct and indirect parental layers..
     *
     * @return the same set Set <Layer> as given.
     */
    // <editor-fold defaultstate="collapsed" desc="getTopLayer(Layer layer) method.">
    private Layer getTopLayer(Layer layer) {
        if(layer.getParent() != null) {
            this.getTopLayer(layer.getParent());
        }
        return layer.getParent();
    }
    // </editor-fold>
    
    
    
    
    
    
    
    /** Creates a list with the available layers.
     *
     * @param layer The layer of which we have to find the parent layers.
     * @param layers Set <Layer> with all direct and indirect parental layers..
     *
     * @return the same set Set <Layer> as given.
     */
    // <editor-fold defaultstate="collapsed" desc="getAllParentLayers(Layer layer, Set <Layer> layers) method.">
    private Set <Layer> getAllParentLayers(Layer layer, Set <Layer> layers) {
        if(layer.getParent() != null) {
            layers.add(layer);
            this.getAllParentLayers(layer.getParent(), layers);
        } else {
            layers.add(layer);
        }
        return layers;
    }
    // </editor-fold>
    
    /** Creates a list with the available layers.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="collapsed" desc="createLists(DynaValidatorForm form, HttpServletRequest request) method.">
    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        super.createLists(form, request);
        
        super.createLists(form, request);
        
        List organizationlist = getHibernateSession().createQuery("from Organization").list();
        request.setAttribute("organizationlist", organizationlist);
        
        List serviceProviders = getHibernateSession().createQuery("from ServiceProvider sp order by sp.name").list();
        
        JSONObject root = new JSONObject(); 
        JSONArray rootArray = new JSONArray(); 
        
        Iterator it = serviceProviders.iterator();
        while (it.hasNext()) {
            ServiceProvider sp = (ServiceProvider)it.next();
            JSONObject parentObj = this.serviceProviderToJSON(sp);
            HashSet<Layer> set= new HashSet<Layer>();
            set.add(sp.getTopLayer());
            parentObj = createTreeList(set, parentObj);
            if (parentObj.has("children")){
                rootArray.put(parentObj);
            }
        }
        root.put("name","root");
        root.put("children", rootArray);
        request.setAttribute("layerList", root);
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
    // <editor-fold defaultstate="collapsed" desc="createTreeList(Set layers, Set organizationLayers, JSONObject parent) method.">
    private JSONObject createTreeList(Set layers, JSONObject parent) throws JSONException {
        /* This method has a recusive function in it. Its functionality is to create a list of layers
         * in a tree like array which can be used to build up a menu structure. 
         */
        Iterator layerIterator = layers.iterator();
        JSONArray parentArray = new JSONArray();
        while (layerIterator.hasNext()) {
            /* For each layer in the set we are going to create a JSON object which we will add to de total
             * list of layer objects.
             */
            Layer layer = (Layer)layerIterator.next();
            
            /* When we have retrieved this array we are able to save our object we are working with
             * at the moment. This object is our present layer object. This object first needs to be
             * transformed into a JSONObject, which we do by calling the method to do so.
             */
            JSONObject layerObj = this.layerToJSON(layer);

            /* Before we are going to save the present object we can first use our object to recieve and store
             * any information which there might be for the child layers. First we check if the set of layers 
             * is not empty, because if it is, no effort has to be taken.
             * If, on the other hand, this layer does have children then the method is called recursivly to
             * add these childs to the present layer we are working on.
             */
            Set childLayers = layer.getLayers();
            if (childLayers != null && !childLayers.isEmpty()) {
                layerObj = createTreeList(childLayers, layerObj);
            }

            /* After creating the JSONObject for this layer and if necessary, filling this
             * object with her childs, we can add this JSON layer object back into its parent array.
             */
            parentArray.put(layerObj);            
        }
        if (parentArray.length()>0){
            parent.put("children",parentArray);
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
    // <editor-fold defaultstate="collapsed" desc="serviceProviderToJSON(ServiceProvider serviceProvider) method.">
    private JSONObject serviceProviderToJSON(ServiceProvider serviceProvider) throws JSONException {
        JSONObject root = new JSONObject();
        root.put("id", serviceProvider.getId());
        root.put("name", serviceProvider.getGivenName());
        root.put("type", "serviceprovider");
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
    // <editor-fold defaultstate="collapsed" desc="layerToJSON(Layer layer) method.">
    private JSONObject layerToJSON(Layer layer) throws JSONException{
        JSONObject jsonLayer = new JSONObject();
        jsonLayer.put("id", layer.getId() + "_" + layer.getName());
        jsonLayer.put("name", layer.getName());
        jsonLayer.put("type", "layer");
        return jsonLayer;
    }
    // </editor-fold>
    
    /** Method for saving a new organization from input of a user.
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
    // <editor-fold defaultstate="collapsed" desc="save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    public ActionForward save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //if invalid
        //String layerSelected = dynaForm.getString("layerSelected");
        HttpSession session = request.getSession();
        //sess.setAttribute("layerlist", layerlist);
        
        String [] selectedLayers = (String [])dynaForm.get("selectedLayers");
        //List layerList = (List)session.getAttribute("layerlist");        
        
        if (!isTokenValid(request)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        // nieuwe default actie op delete zetten
        Session sess = getHibernateSession();
        //validate and check for errors
        ActionErrors errors = dynaForm.validate(mapping, request);
        if(!errors.isEmpty()) {
            addMessages(request, errors);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, VALIDATION_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        Integer id = FormUtils.StringToInteger(dynaForm.getString("id"));
        Organization organization = getOrganization(dynaForm, request, true, id);
        if (null == organization) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        populateOrganizationObject(dynaForm, organization, selectedLayers);//layerList, layerSelected);
        
        if(!organization.getHasValidGetCapabilities()) {
            request.setAttribute("warning", "De combinatie van de verschillende " +
                    "servers heeft problemen opgeleverd.\n De selectie is wel opgeslagen " +
                    "maar kan problemen opleveren bij het opvragen van de GetCapabilities. " +
                    "Het probleem dat opgetreden is een conflict in de ondersteuning van " +
                    "de Spatial reference en/of de image format.");
        }
        
        //store in db
        sess.saveOrUpdate(organization);
        sess.flush();
        
        dynaForm.set("id", "");
        dynaForm.set("name", "");
        dynaForm.set("organizationStreet", "");
        dynaForm.set("organizationNumber", "");
        dynaForm.set("organizationAddition", "");
        dynaForm.set("organizationPostalcode", "");
        dynaForm.set("organizationProvince", "");
        dynaForm.set("organizationCountry", "");
        dynaForm.set("organizationPostbox", "");
        dynaForm.set("organizationBillingAddress", "");
        dynaForm.set("organizationVisitorsAddress", "");
        dynaForm.set("organizationTelephone", "");
        dynaForm.set("organizationFax", ""); 
        dynaForm.set("layerSelected", null);
        
        return super.save(mapping,dynaForm,request,response);
    }
    // </editor-fold>
    
    /** Method for deleting an organization selected by a user.
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
    // <editor-fold defaultstate="collapsed" desc="delete(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    public ActionForward delete(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String [] organizationSelected = dynaForm.getStrings("organizationSelected");
        int size = organizationSelected.length;
        
        String [] selectedLayers = (String [])dynaForm.get("selectedLayers");
        
        //String layerSelected = dynaForm.getString("layerSelected");
        HttpSession session = request.getSession();
        //List layerList = (List)session.getAttribute("layerlist"); 
        
        for(int i = 0; i < size; i++) {
            //if invalid
            if (!isTokenValid(request)) {
                prepareMethod(dynaForm, request, EDIT, LIST);
                addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
                return getAlternateForward(mapping, request);
            }

            // nieuwe default actie op delete zetten
            Session sess = getHibernateSession();
            //validate and check for errors
            ActionErrors errors = dynaForm.validate(mapping, request);
            if(!errors.isEmpty()) {
                addMessages(request, errors);
                prepareMethod(dynaForm, request, EDIT, LIST);
                addAlternateMessage(mapping, request, VALIDATION_ERROR_KEY);
                return getAlternateForward(mapping, request);
            }
            
            Integer id = Integer.parseInt(organizationSelected[i]);
            Organization organization = getOrganization(dynaForm, request, false, id);
            
            if (null == organization) {
                prepareMethod(dynaForm, request, LIST, EDIT);
                addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
                return getAlternateForward(mapping, request);
            }

            populateOrganizationObject(dynaForm, organization, selectedLayers);//layerList, layerSelected);
            //store in db
            try {
                sess.delete(organization);
                sess.flush();
            } catch (Exception e) {
                request.setAttribute("message", "De organizatie is niet verwijderd: Er zijn nog gebruikers gekoppeld aan deze organisatie.");
            }            
        }
        return super.delete(mapping, dynaForm, request, response);
    }
    // </editor-fold>
}