/*
 * WMSUrlCreatorAction.java
 *
 * Created on 20 februari 2007, 9:44
 */

package nl.b3p.kaartenbalie.struts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.struts.ExtendedMethodProperties;
import nl.b3p.kaartenbalie.core.server.Layer;
import nl.b3p.kaartenbalie.core.server.ServiceProvider;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.service.LayerValidator;
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
    
    private static final String EXTRAREQUESTDATA="&VERSION=1.1.1&STYLES=&EXCEPTIONS=INIMAGE&WRAPDATELINE=true&BGCOLOR=0xF0F0F0";
    
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
        String getMap=request.getParameter("getMap");
        String pUrl=request.getParameter("pUrl");
        if (getMap!=null){
            populateForm(getMap, form);
        }
        if (pUrl!=null){
            form.set("personalUrl",pUrl);
        }
        return super.unspecified(mapping,form,request,response);
    }
    /**create the lists for this action.
     */
    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        User user= (User)request.getUserPrincipal();
        Set <Layer> layerSet=user.getOrganization().getOrganizationLayer();
        Set <ServiceProvider> serviceProviderSet= new HashSet();
        
        ArrayList alLayers= new ArrayList();
        putLayersInArrayList(layerSet,alLayers);
        //Collections.sort(alLayers,String.CASE_INSENSITIVE_ORDER);
        request.setAttribute("layerList",alLayers);
        
        Iterator it=layerSet.iterator();
        while(it.hasNext()){
            Layer layer= (Layer)it.next();
            if (layer.getParent()==null){
                serviceProviderSet.add(layer.getServiceProvider());
            }
        }
        //werkt niet ivm lazy exception
        /*ServiceProviderValidator spv = new ServiceProviderValidator();
        //spv.setServiceProviders(serviceProviderSet);
        String[] formats=spv.validateFormats(KBConstants.WMS_REQUEST_GetMap,serviceProviderSet);
        request.setAttribute("formatList",formats);*/
        
        String[] formats=new String[5];
        formats[0]="image/gif";
        formats[1]="image/png";
        formats[2]="image/jpeg";
        formats[3]="image/bmp";
        formats[4]="image/tiff";
        request.setAttribute("formatList",formats);
        
        LayerValidator lv= new LayerValidator(layerSet);
        String[] alSrsen=lv.validateSRS();
        request.setAttribute("projectieList",alSrsen);
        
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
        String pUrl=null;
        Integer height=null;
        Integer width=null;
        String format=null;
        if (form.get("selectedLayers")!=null)
            layers=(String[]) form.get("selectedLayers");
        if (form.get("selectedProjectie")!=null)
            projectie= (String)form.get("selectedProjectie");
        if (form.get("bbox")!=null)
            bbox=(String)form.get("bbox");
        if (form.get("personalUrl")!=null)
            pUrl=(String)form.get("personalUrl");
        if (form.get("height")!=null)
            height=(Integer)form.get("height");
        if (form.get("width")!=null)
            width=(Integer)form.get("width");
        if (form.get("selectedFormat")!=null)
            format=(String) form.get("selectedFormat");
        if (layers==null || layers.length==0 || projectie==null || bbox==null ||pUrl==null){
            return mapping.findForward("failure");
        }
        String getMap=pUrl;
        getMap=WMSParamUtil.removeParameter(WMSParamUtil.REQUEST,getMap);
        if (getMap.contains("?"))
            getMap+="&";
        else
            getMap+="?";
        getMap+="REQUEST=getMap";
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
        if(height!=null)
            getMap+="&HEIGHT="+height;
        if(width!=null)
            getMap+="&WIDTH="+width;
        if(format!=null)
            getMap+="&FORMAT="+format;
        //extra info toevoegen
        getMap+=EXTRAREQUESTDATA;
        
        dbuser.setDefaultGetMap(getMap);
        user.setDefaultGetMap(getMap);
        getHibernateSession().save(dbuser);
        /* Laat bij deze attribute er javascript er voor zorgen dat de window closed
         *en dat de parent window wordt vernieuwd
         */
        request.setAttribute("getMapMade",getMap);
        return mapping.findForward("success");        
    }
    
    private void putLayersInArrayList(Set set, ArrayList alLayers) {
        for (Iterator it = set.iterator(); it.hasNext();) {
            Layer layer=(Layer)it.next();
            alLayers.add(layer);            
        }
    }

    private void populateForm(String getMap, DynaValidatorForm form) {
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
        if (WMSParamUtil.getParameter(WMSParamUtil.HEIGHT,getMap)!=null){
            form.set("height",new Integer(WMSParamUtil.getParameter(WMSParamUtil.HEIGHT,getMap)));
        }
        if (WMSParamUtil.getParameter(WMSParamUtil.WIDTH,getMap)!=null){
            form.set("width",new Integer(WMSParamUtil.getParameter(WMSParamUtil.WIDTH,getMap)));
        }
    }
    
}

