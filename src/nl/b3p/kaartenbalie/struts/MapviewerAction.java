/*
 * MapviewerAction.java
 *
 * Created on 24 oktober 2006, 15:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
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

/**
 *
 * @author Nando De Goeij
 */
public class MapviewerAction extends KaartenbalieCrudAction {
    //Moet toch een kaartenbaliecrudaction extenden, vanwege de createlist....
    
    /* forward name="success" path="" */
    private final static String SUCCESS = "success";
    
    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    public ActionForward execute(ActionMapping mapping, DynaValidatorForm dynaForm,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        System.out.println("The incoming request is: " + request);
        
        Map requestMap = request.getParameterMap();
        if(requestMap.get("REQUEST") != null || requestMap.get("request") != null) {
            System.out.println("REQUEST Binnengekregen!");
        }
        
        //get username
        String username = request.getUserPrincipal().getName();
        
        
        //DynaValidatorForm dynaForm= (DynaValidatorForm)dynaForm;
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
    
    private List layerList = new ArrayList();
    //layerList.
    
    //private Map layerMap;
    private int id;
    
    public void walkThroughLayers(Set layers, int id) {
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
    /*
    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        super.createLists(form, request);
        
        List organizationlist = getHibernateSession().createQuery("from Organization").list();
        request.setAttribute("organizationlist", organizationlist);
        
        List layerlist = getHibernateSession().createQuery(
                "from Layer l left join fetch l.latLonBoundingBox left join fetch l.attribution").list();
        HttpSession session = request.getSession();
        session.setAttribute("layerlist", layerlist);
        //request.setAttribute();
    }
    /*
    private void createList(HttpServletRequest request) {
        //User user = request.getUserPrincipal().getName();
        
//        Session sess = getHibernateSession();
//        User user = (User)(sess.createQuery("from User as u where u.username = :name").setString("name", request.getUserPrincipal().getName()).uniqueResult());
//        System.out.println(user.getFirstName() + user.getLastName());

        //query maken die alle layers van alle wms servers in een keer ophaalt.
        //Wel alvast nadenken over hoe dit straks in combinatie met gebruikersrechten gedaan moet worden.
        //List serviceproviderlist = getHibernateSession().createQuery("from ServiceProvider").list();
        //Iterator i = serviceproviderlist.iterator();
        //while(i.hasNext()) {
//            ServiceProvider sp = (ServiceProvider)i.next();
//            walkThroughLayers(sp.getLayers());            
//        }
        
        //Session sess;
        //sess.
        
    }
    */
    private static final Log log = LogFactory.getLog(MapviewerAction.class);
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
        
        List layerList = getHibernateSession().createQuery(
                "from Layer l left join fetch l.latLonBoundingBox left join fetch l.attribution").list();
        HttpSession session = request.getSession();
        //session.setAttribute("layerlist", layerlist);
        //request.setAttribute();
        //Eind van het stukje test code.
        
        session.setAttribute("layerList", layerList);//List);
        
                
    }
    
    public ActionForward delete(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("checkpoint 1");
        String [] selectedLayers = dynaForm.getStrings("selectedLayers");
        int size = selectedLayers.length;
        HttpSession session = request.getSession();
        List layerList = (ArrayList)session.getAttribute("layerList");
        ArrayList maps = new ArrayList();
        for(int i = 0; i < size; i++) {
            System.out.println("checkpoint 2");
            Integer id = Integer.parseInt(selectedLayers[i]);
            String foundLayer = findLayer(layerList, id.toString());
            
            if(foundLayer != null) {
                System.out.println("checkpoint 3");
                maps.add(foundLayer);
                System.out.println("Foundlayer = " + foundLayer);
            }
        }
        request.setAttribute("maps", maps);
        return mapping.findForward(SUCCESS);
    }
    
    

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
}
