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
    // <editor-fold defaultstate="collapsed" desc="execute(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    public ActionForward execute(ActionMapping mapping, DynaValidatorForm dynaForm,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        Map requestMap = request.getParameterMap();
        String username = request.getUserPrincipal().getName();
        
        dynaForm.set("minx","184479");
        dynaForm.set("miny","345822");
        dynaForm.set("maxx","194755");
        dynaForm.set("maxy","353322");
        
        //welke layers mag de gebruiker zien?
        createLists(dynaForm, request);
        Layer layers = new Layer();
        //layers setten in dynaform
        request.setAttribute("layers",layers);
        
        return mapping.findForward(SUCCESS);
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
        */
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
