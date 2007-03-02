/*
 * WMSUrlCreatorAction.java
 *
 * Created on 20 februari 2007, 9:44
 */

package nl.b3p.kaartenbalie.struts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.struts.ExtendedMethodProperties;
import nl.b3p.kaartenbalie.core.server.Layer;
import nl.b3p.kaartenbalie.core.server.SRS;
import nl.b3p.kaartenbalie.core.server.ServiceProvider;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.service.WMSParamUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.validator.DynaValidatorForm;
/**
 *
 * @author Roy
 * @version
 */

public class WMSUrlCreatorAction extends KaartenbalieCrudAction {
    
    private static final Log log = LogFactory.getLog(WMSUrlCreatorAction.class);
    
    protected static final String GETCAPABILITIES = "getCapabilities";
    protected static final String GETMAP = "getMapUrl";
    protected static final String DEFAULTVERSION = "1.1.1";
    protected static final String DEFAULTSERVICE = "WMS";
    
    private static final String EXTRAREQUESTDATA="&EXCEPTIONS=INIMAGE&WRAPDATELINE=true&BGCOLOR=0xF0F0F0";
    
    protected Map getActionMethodPropertiesMap() {
        Map map = super.getActionMethodPropertiesMap();
        ExtendedMethodProperties crudProp = new ExtendedMethodProperties(GETCAPABILITIES);
        crudProp.setDefaultForwardName(SUCCESS);
        crudProp.setDefaultMessageKey("beheer.kaarten.wmsurlcreator.success");
        crudProp.setAlternateForwardName(FAILURE);
        crudProp.setAlternateMessageKey("beheer.kaarten.wmsurlcreator.failed");
        map.put(GETCAPABILITIES, crudProp);
        
        crudProp = new ExtendedMethodProperties(GETMAP);
        crudProp.setDefaultForwardName(SUCCESS);
        crudProp.setDefaultMessageKey("beheer.kaarten.wmsurlcreator.success");
        crudProp.setAlternateForwardName(FAILURE);
        crudProp.setAlternateMessageKey("beheer.kaarten.wmsurlcreator.failed");
        map.put(GETMAP, crudProp);
        
        return map;
    }
    
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //if the wms url already is filled:
        
        return super.unspecified(mapping,form,request,response);
    }
    /**create the lists for this action.
     */
    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        /*String cap=form.getString("getCapabilitiesUrl");
        if (cap!=null && cap.length()>0){
            createLists(form,request,cap);
        }*/
        String getMap=request.getParameter("getMap");
        String pUrl=request.getParameter("pUrl");
        
        //create a getCap url from the getMap url
        if (getMap!=null || pUrl!=null){
            if (getMap==null){
                getMap=pUrl;
            }
            String version=WMSParamUtil.getParameter(WMSParamUtil.VERSION,getMap);
            if (version==null)
                version=DEFAULTVERSION;
            String service=WMSParamUtil.getParameter(WMSParamUtil.SERVICE,getMap);
            if (service==null)
                service=DEFAULTSERVICE;
            String getCap=null;
            if (pUrl!=null){
                if (!getMap.startsWith(pUrl)){
                    getCap=pUrl;
                }
            }
            if (getCap==null){
                getCap=WMSParamUtil.removeAllWMSParameters(getMap);
            }
            if (getCap.indexOf("?")>0)
                getCap+="&VERSION="+version;
            else{
                getCap+="?VERSION="+version;
            }
            getCap+="&SERVICE="+service;
            getCap+="&REQUEST=getCapabilities";
            form.set("getCapabilitiesUrl",getCap);
            createLists(form,request,getCap);
            if (WMSParamUtil.getParameter(WMSParamUtil.LAYERS,getMap)!=null){
                String[] layers= WMSParamUtil.getParameter(WMSParamUtil.LAYERS,getMap).split(",");
                form.set("selectedLayers",layers);
            }
            if (WMSParamUtil.getParameter(WMSParamUtil.BBOX,getMap)!=null){
                form.set("bbox",WMSParamUtil.getParameter(WMSParamUtil.BBOX,getMap));
            }
            if (WMSParamUtil.getParameter(WMSParamUtil.SRS,getMap)!=null){
                form.set("selectedProjectie",WMSParamUtil.getParameter(WMSParamUtil.SRS,getMap));
            }
        }        
    }
    public void createLists(DynaValidatorForm form, HttpServletRequest request,String cap) throws Exception{
        ServiceProvider sp = new ServiceProvider();
        WMSCapabilitiesReader wcr= new WMSCapabilitiesReader(sp);
        try{
            sp=wcr.getProvider(cap);
        } catch(Exception e){
            request.setAttribute("message","Kan ingevulde getMap niet vinden, vul de getCapabilities in");
            form.set("getCapabilitiesUrl","");
        }
        if(sp.getLayers()!=null){
            ArrayList alLayers=new ArrayList();
            putLayersInArrayList(sp.getLayers(),alLayers);
            Collections.sort(alLayers,String.CASE_INSENSITIVE_ORDER);
            request.setAttribute("layerList",alLayers);
            
        }
        if(sp.getTopLayer()!=null && sp.getTopLayer().getSrs()!=null){
            ArrayList alSrsen= new ArrayList();
            String bbox=null;
            String chooseSRS=null;
            for (Iterator it = sp.getTopLayer().getSrs().iterator(); it.hasNext();) {
                SRS srs= (SRS)it.next();
                if (!srs.getSrs().contains(" ")){
                    alSrsen.add(srs.getSrs());
                    //als de srs een bbox heeft en als de bbox nog niet is gevonden dan vullen. Tevens deze SRS selecteren als default.
                    if (srs.getMaxx()!=null && srs.getMaxy()!=null && srs.getMinx()!=null && srs.getMiny()!=null&& bbox==null){
                        bbox=srs.getMinx()+","+srs.getMiny()+","+srs.getMaxx()+","+srs.getMaxy();
                        chooseSRS=srs.getSrs();
                    }
                }
            }
            Collections.sort(alSrsen,String.CASE_INSENSITIVE_ORDER);
            if (bbox!=null){
                form.set("bbox",bbox);
            }
            if (chooseSRS!=null){
                form.set("selectedProjectie",chooseSRS);
            }
            request.setAttribute("projectieList",alSrsen);
        }
    }
    public ActionForward getCapabilities(ActionMapping mapping, DynaValidatorForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String cap=form.getString("getCapabilitiesUrl");
        if (cap!=null && cap.length()>0){
            //controleer of version en service zijn ingevulde
            String version=WMSParamUtil.getParameter(WMSParamUtil.VERSION,cap);
            if (version==null)
                cap+="&"+WMSParamUtil.VERSION+DEFAULTVERSION;
            String service=WMSParamUtil.getParameter(WMSParamUtil.SERVICE,cap);
            if (service==null)
                cap+="&"+WMSParamUtil.SERVICE+DEFAULTSERVICE;
            createLists(form,request,cap);
        }
        return mapping.findForward("success");
    }
    public ActionForward getMapUrl(ActionMapping mapping, DynaValidatorForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = (User) request.getUserPrincipal();
        if (user==null){
            log.error("ingelogde user == null");
            return mapping.findForward("failure");
        }
        User dbuser = (User) getHibernateSession().get(User.class,user.getId());
        if (dbuser==null){
            log.error("Ingelogde user bestaat niet meer in database");
            return mapping.findForward("failure");
        }
        String[] layers=null;
        String projectie=null;
        String bbox=null;
        String getCap=null;
        if (form.get("selectedLayers")!=null)
            layers=(String[]) form.get("selectedLayers");
        if (form.get("selectedProjectie")!=null)
            projectie= (String)form.get("selectedProjectie");
        if (form.get("bbox")!=null)
            bbox=(String)form.get("bbox");
        if (form.get("getCapabilitiesUrl")!=null)
            getCap=(String)form.get("getCapabilitiesUrl");
        if (layers==null || layers.length==0 || projectie==null || bbox==null ||getCap==null){
            if (getCap!=null){
                request.setAttribute("message","Er moet minimaal 1 layer worden geselecteerd, een bbox worden opgegeven en een projectie worden geselecteerd.");
                createLists(form,request,getCap);
            }
            return mapping.findForward("failure");
        }
        String getMap=getCap;
        getMap=WMSParamUtil.removeParameter(WMSParamUtil.REQUEST,getMap);
        getMap+="&REQUEST=getMap";
        //layers toevoegen
        getMap+="&LAYERS=";
        for (int i=0; i < layers.length; i++){
            if (i==0)
                getMap+=layers[i];
            else{
                getMap+=","+layers[i];
            }
        }
        //bbox toevoegen
        getMap+="&BBOX="+bbox;
        //SRS toevoegen
        getMap+="&SRS="+projectie;
        //extra info toevoegen
        getMap+=EXTRAREQUESTDATA;
        
        dbuser.setDefaultGetMap(getMap);
        user.setDefaultGetMap(getMap);
        getHibernateSession().save(dbuser);
        /* Laat bij deze attribute er javascript er voor zorgen dat de window closed
         *en dat de parent window wordt vernieuwd
         */
        request.setAttribute("getMapMadeSuccesfull","gelukt");
        return mapping.findForward("success");        
    }
    
    private void putLayersInArrayList(Set set, ArrayList alLayers) {
        for (Iterator it = set.iterator(); it.hasNext();) {
            Layer layer=(Layer)it.next();
            alLayers.add(layer.getName());
            if (layer.getLayers()!=null && layer.getLayers().size()>0){
                putLayersInArrayList(layer.getLayers(),alLayers);
            }
        }
    }
    
}

