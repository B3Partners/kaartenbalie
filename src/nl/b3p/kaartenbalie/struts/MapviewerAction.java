/**
 * @(#)MapviewerAction.java
 * @author N. de Goeij
 * @version 1.00 2006/10/24
 *
 * Purpose: a Struts action class defining all the Action for the map viewer.
 *
 * @copyright 2007 All rights reserved. B3Partners
 */

package nl.b3p.kaartenbalie.struts;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import nl.b3p.kaartenbalie.core.server.Layer;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.ServiceProvider;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.service.MyDatabase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.validator.DynaValidatorForm;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapviewerAction extends KaartenbalieCrudAction {
    //Moet toch een kaartenbaliecrudaction extenden, vanwege de createlist....
    
    /* forward name="success" path="" */
    private final static String SUCCESS = "success";
    private List <Object []> layerList = new ArrayList <Object []>();
    private int id;
    private static final Log log = LogFactory.getLog(MapviewerAction.class);
        
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
    // <editor-fold defaultstate="collapsed" desc="unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        Map requestMap = request.getParameterMap();
        String username = request.getUserPrincipal().getName();
        
/*        dynaForm.set("minx","184479");
        dynaForm.set("miny","345822");
        dynaForm.set("maxx","194755");
        dynaForm.set("maxy","353322");
        createLists(dynaForm, request);    
        
        //welke layers mag de gebruiker zien?
        Layer layers = new Layer();
        //layers setten in dynaform
        request.setAttribute("layers",layers);*/
        
        String checkedLayers=request.getParameter("layers");
        request.setAttribute("checkedLayers",checkedLayers);
        return super.unspecified(mapping,dynaForm,request,response);
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
        
        //createList(request);
        /*  List splist = getHibernateSession().createQuery(
                "SELECT DISTINCT l.id, l.Parent.id, layer.serviceProvider.id, l.title " +
                "FROM Layer" +
                "ServiceProvider AS sp").list();
        */
        
        /*--------------------------------------
        List layers = getHibernateSession().createQuery(
                "from Layer l left join fetch l.latLonBoundingBox left join fetch l.attribution").list();
 
        List serviceProviders = getHibernateSession().createQuery("from ServiceProvider sp left join fetch sp.contactInformation").list();
 
        Map layerSublayers = new HashMap();
        Map serviceProviderToLayersMap = new HashMap();
        for (Iterator it = layers.iterator(); it.hasNext();) {
            Layer l = (Layer) it.next();
            if(l.getParent() != null) {
                List parentSublayers = (List) layerSublayers.get(l.getParent());
                if(parentSublayers == null) {
                    parentSublayers = new ArrayList();
                    parentSublayers.add(l);
                    layerSublayers.put(l.getParent(), parentSublayers);
                } else {
                    parentSublayers.add(l);
                }
            } else {
                List serviceProviderLayers = (List) serviceProviderToLayersMap.get(l.getServiceProvider());
                if(serviceProviderLayers == null) {
                    serviceProviderLayers = new ArrayList();
                    serviceProviderLayers.add(l);
                    serviceProviderToLayersMap.put(l.getServiceProvider(), serviceProviderLayers);
                } else {
                    serviceProviderLayers.add(l);
                }
            }
        }
        for (Iterator it = layers.iterator(); it.hasNext();) {
            Layer l = (Layer) it.next();
            List sublayers = (List) layerSublayers.get(l);
            if(sublayers == null) {
                l.setLayers(new HashSet());
            } else {
                l.setLayers(new HashSet(sublayers));
            }
        }
        for (Iterator it = serviceProviders.iterator(); it.hasNext();) {
            ServiceProvider sp = (ServiceProvider) it.next();
            List serviceProviderLayers = (List)serviceProviderToLayersMap.get(sp);
            if(layers == null) {
                sp.setLayers(new HashSet());
            } else {
                sp.setLayers(new HashSet(serviceProviderLayers));
            }
        }
 
        //Layer l = (Layer)getHibernateSession().load(Layer.class, new Integer(15));
        //printLayer(l, 0);
 
 
        for (Iterator it = serviceProviders.iterator(); it.hasNext();) {
            ServiceProvider elem = (ServiceProvider) it.next();
            LogFactory.getLog(MapviewerAction.class).debug("Service provider " + elem.getName());
            for (Iterator it2 = elem.getLayers().iterator(); it2.hasNext();) {
                printLayer((Layer)it2.next(),0);
            }
        }
 
 
        /*
        List serviceproviderlist = getHibernateSession().createQuery("from ServiceProvider").list();
 
        id = 0;
        for (Iterator it = serviceproviderlist.iterator(); it.hasNext();) {
            ServiceProvider sp = (ServiceProvider) it.next();
            Set layers = sp.getLayers();
            walkThroughLayers(layers, id);
            id++;
        }
 
        serviceproviderlist = null;
        */
        
        
        /*
        List serviceproviderlist = getHibernateSession().createQuery("from ServiceProvider").list();
        Iterator i = serviceproviderlist.iterator();
        while(i.hasNext()) {
            ServiceProvider sp = (ServiceProvider)i.next();
            walkThroughLayers(sp.getLayers());
        }
         **/
        /*
        for (Iterator it = layerList.iterator(); it.hasNext();) {
            //Object [] layerInfo = {layer.getId(), layer.getTitle()};
            //Object [] tuple = {new Integer(id), layerInfo};
         
            Object [] tuple = (Object[])it.next();
            Integer levelid = (Integer)tuple[0];
         
            Object [] layerInfo = (Object[])tuple[1];
            Integer layerid = (Integer)layerInfo[0];
            String layerTitle = (String)layerInfo[1];
         
         
            //Object [] set = (Object[])it.next();
            //System.out.println((Integer)set[0]);
            //Layer l = (Layer)set[1];
            System.out.println("Level id : " + levelid);
            System.out.println("Layer id : " + layerid);
            System.out.println("Layer title : " + layerTitle);
        }
         */
        
        //Hieronder is een tijdelijk stukje code om te testen of het systeem uberhaupt goed werkt.
        //super.createLists(form, request);
        
        //List organizationlist = getHibernateSession().createQuery("from Organization").list();
        //request.setAttribute("organizationlist", organizationlist);
        
        
        
        
        
        
        //Onderstaande regels moeten toegevoegd worden om er voor te zorgen dat alleen die layers
        //zichtbaar zijn die voor de huidige gebruiker beschikbaar zijn.
        /*
        User dbUser = (User)sess.createQuery("from User u where " +
                    "lower(u.id) = lower(:userid)").setParameter("userid", user.getId()).uniqueResult();
        
        System.out.println("An user has been found : " + dbUser.getFirstName());
        Set dbLayers = dbUser.getOrganization().getOrganizationLayer();
        
        User user = (User) request.getUserPrincipal();
        Set dbLayers = user.getOrganization().getOrganizationLayer();
        
        List layerList = new ArrayList();
        layerList.addAll(dbLayers);
        //dbLayers.
        
        //List layerList = getHibernateSession().createQuery(
//                "from Layer l left join fetch l.latLonBoundingBox left join fetch l.attribution").list();
        HttpSession session = request.getSession();
        //session.setAttribute("layerlist", layerlist);
        //request.setAttribute();
        //Eind van het stukje test code.
        
        session.setAttribute("layerList", layerList);//List);
        */
        
        
        User user = (User) request.getUserPrincipal();
        Set organizationLayers = user.getOrganization().getOrganizationLayer();
        List serviceProviders = getHibernateSession().createQuery("from ServiceProvider sp").list();
        
        JSONObject root = new JSONObject(); 
        JSONArray rootArray = new JSONArray(); 
        
        Iterator it = serviceProviders.iterator();
        while (it.hasNext()) {
            ServiceProvider sp = (ServiceProvider)it.next();
            JSONObject parentObj = this.serviceProviderToJSON(sp);            
            parentObj = createTreeList(sp.getLayers(), organizationLayers, parentObj);
            rootArray.put(parentObj);
            
//            parent = createTreeList(sp.getLayers(), organizationLayers, parent);
            //root.put("serviceprovider", parent);
            //System.out.println(parentObj.toString());
        }
        root.put("name","root");
        root.put("children", rootArray);
        
        System.out.println(root.toString());
        
        //HttpSession session = request.getSession();
        //session.setAttribute("layerList", root);//List);
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
    private JSONObject createTreeList(Set layers, Set organizationLayers, JSONObject parent) throws JSONException {
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
            /* Before a layer is going to be added we need to make sure that this layer is allowed to be seen
             * If a layer is not allowed to be seen there is no use to create a JSON object for it, neither
             * is it necessary to check if child layers shoudl be added.
             */
            if(hasVisibility(layer, organizationLayers)) {
                /* When the visibility check has turned out to be ok. we start adding this layer to
                 * the list of layers.
                 */
                
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
                    layerObj = createTreeList(childLayers, organizationLayers, layerObj);
                }
                
                /* After creating the JSONObject for this layer and if necessary, filling this
                 * object with her childs, we can add this JSON layer object back into its parent array.
                 */
                parentArray.put(layerObj);
                
                              
            }
        }
        if (parentArray.length()>0){
            parent.put("children",parentArray);
        }
        return parent;
    }
    // </editor-fold>
    
    /* Method which checks if a certain layer is allowed to be shown on the screen.
     * 
     * @param layer Layer object that has to be checked
     * @param organizationLayers Set of restrictions which define the visible and non visible layers
     *
     * @return boolean
     */
    // <editor-fold defaultstate="collapsed" desc="hasVisibility(Layer layer, Set orgLayers) method.">
    private boolean hasVisibility(Layer layer, Set organizationLayers) {
        Iterator it = organizationLayers.iterator();
        while (it.hasNext()) {
            Layer organizationLayer = (Layer) it.next();
            if(layer.getId().equals(organizationLayer.getId())) {
                return true;
            }
        }
        return false;
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
    
    
    
    
    
    
    
    // TODO: de onderstaande methode heet delete en is alleen zo genoemd omdat deze door bovenliggende klasse
    // ondersteunt wordt. Deze naamgeving komt dus helemaal niet overeen met wat de klasse doet. De naam van
    // deze klasse moet dan nog zeker even aangepast worden!!!
    
    /** Method that checks layers are selected and looks if these layers can be found in the whole list of layers from the database.
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
        String [] selectedLayers = dynaForm.getStrings("selectedLayers");
        int size = selectedLayers.length;
        HttpSession session = request.getSession();
        List layerList = (ArrayList)session.getAttribute("layerList");
        ArrayList <String> maps = new ArrayList <String>();
        for(int i = 0; i < size; i++) {
            Integer id = Integer.parseInt(selectedLayers[i]);
            String foundLayer = findLayer(layerList, id.toString());
            
            if(foundLayer != null) {
                maps.add(foundLayer);
            }
        }
        request.setAttribute("maps", maps);
        return mapping.findForward(SUCCESS);
    }
    // </editor-fold>
    
    /** Method which walks through a set of layers.
     *
     * @param layers A set of layers where has to be stepped through.
     * @param id Integer of the id the walkthrough has to begin from.
     */
    // <editor-fold defaultstate="collapsed" desc="walkThroughLayers(Set layers, int id) method.">
    public void walkThroughLayers(Set <Layer> layers, int id) {
        id++;
        
        for (Iterator it = layers.iterator(); it.hasNext();) {
            Layer layer = (Layer)it.next();
            
            if(layer != null) {
                //Object [] layerInfo = {layer.getId(), layer.getTitle()};
                Object [] triple = {new Integer(id), layer.getId(), layer.getTitle()};
                layerList.add(triple);
                
                if (layer.getLayers() != null) {
                    walkThroughLayers(layer.getLayers(), id);
                }
            }
        }
        id--;
    }
    // </editor-fold>
        
    /** Method which prints a specified layer at the right location of the screen.
     *
     * @param l Layer that has to be printed.
     * @param depth Integer representing the depth of this specific layer in the tree.
     */
    // <editor-fold defaultstate="collapsed" desc="printLayer(Layer l, int depth) method.">
    private static void printLayer(Layer l, int depth) {
        String s = "";
        for (int i = 0; i < depth; i++) {
            s += "- ";
        }
        log.debug(s + ": " + l.getTitle());
        for (Iterator it = l.getLayers().iterator(); it.hasNext();) {
            Layer elem = (Layer) it.next();
            printLayer(elem, depth+1);
            
        }
    }
    // </editor-fold>
        
    /** Method which adds a new handler to the hash table.
     *
     * @param l String representing the name of the layer that has to be found.
     * @param layers List of layers in which the layer has to be found.
     *
     * @return a string representing the name of the layer that matched the search.
     */
    // <editor-fold defaultstate="collapsed" desc="findLayer(List layers, String l) method.">
    private String findLayer(List layers, String l) {
        String found = null;
        if(null != layers) {
            Iterator it = layers.iterator();
            while (it.hasNext()) {
                Layer layer = (Layer) it.next();
                if(layer.getId().toString().equals(l)) {
                    found = layer.getId() + "_" + layer.getName();
                    break;
                }
            }
        }
        return found;
    }
    // </editor-fold>
}
