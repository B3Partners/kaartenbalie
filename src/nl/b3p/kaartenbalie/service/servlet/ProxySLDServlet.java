package nl.b3p.kaartenbalie.service.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.kaartenbalie.service.URLCache;
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
public class ProxySLDServlet extends AbstractSimpleKbService {
    public static final String PARAM_ORIGINAL_SLD_URL="oriSldUrl";
    public static final String PARAM_ORIGINAL_SLD_BODY="oriSldBody";
    public static final String PARAM_SERVICEPROVIDER_ID="servProvId";    
    public static final String PARAM_STYLES="styles";
    public static final String mimeType = "application/xml";
    
    private static final URLCache cache= new URLCache(60000);
    private static final Log log = LogFactory.getLog(ProxySLDServlet.class);
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws ServletException, IOException, InterruptedException, Exception {
        List<SldNamedLayer> returnedNamedLayers=new ArrayList<SldNamedLayer>();        
        EntityManager em = getEntityManager();
        SldWriter sldFact = new SldWriter(); 
            
        String oriSldUrl=request.getParameter(PARAM_ORIGINAL_SLD_URL);
        String oriSldBody=request.getParameter(PARAM_ORIGINAL_SLD_BODY);
        String styleString=request.getParameter(PARAM_STYLES);
                
        if ((oriSldUrl!=null && oriSldUrl.length()>0) || 
                (oriSldBody!=null && oriSldBody.length()>0)){
            
            String sld=null;
            if (oriSldUrl!=null && oriSldUrl.length()>0)
                sld= cache.getFromCache(oriSldUrl);    
            else
                sld=oriSldBody;

            if (sld==null){
                throw new Exception("Error while getting SLD. Check the log for details");
            }
            Integer servProvId=null;
            try{
                servProvId=new Integer(request.getParameter(PARAM_SERVICEPROVIDER_ID));
            }catch(Exception e){
                throw new Exception ("No param "+PARAM_SERVICEPROVIDER_ID+" provided or not a number",e);
            }
            ServiceProvider sp = em.find(ServiceProvider.class, servProvId);
            //TODO: moet beter. Char set bepalen niet meegeven.
            SldReader sldReader = new SldReader();
            List<SldNamedLayer> namedLayers=sldReader.getNamedLayersBySld(sld,"UTF-8");

            Iterator<SldNamedLayer> it =namedLayers.iterator();
            while (it.hasNext()){
                SldNamedLayer nl= it.next();
                
                try{
                    String[] codeAndName = OGCCommunication
                            .toCodeAndName(nl.getName());                    
                    
                    if (codeAndName[0] != null &&
                            codeAndName[0].equals(sp.getAbbr())) {
                        
                        nl.setName(codeAndName[1]);
                        returnedNamedLayers.add(nl);
                        
                    /* 
                        *Als er geen afkorting in namedlayer zit is
                        * codeAndName[0] null. Kijken of name overeenkomt 
                        * met een layer uit service ?
                    */
                    } else if (codeAndName[0] == null && codeAndName[1] != null) {
                        
                        Iterator<Layer> it2 = sp.getAllLayers().iterator();
                        while (it2.hasNext()){
                            Layer l = it2.next();
                            
                            if (l.getName().equals(codeAndName[1])) {
                                nl.setName(codeAndName[1]);
                                returnedNamedLayers.add(nl);
                            }
                        }                        
                    }
                    
                } catch(Exception e){
                    log.error("Fout bij maken SLD",e);
                }
            }
        }
        
        /**/
        if (styleString!=null && styleString.length()>0){
            String[] styleStringTokens=styleString.split(",");
            Integer[] styleIds= new Integer[styleStringTokens.length];
            for (int i=0; i < styleStringTokens.length; i++){
                styleIds[i]= new Integer(styleStringTokens[i]);
            }
            //haal de styles op.
            List<Style> styles= em.createQuery("from Style where id in ("+styleString+")")
                    .getResultList();
            
            returnedNamedLayers.addAll(sldFact.createNamedLayersWithKBStyles(styles));
        }
        
        /**/
        String xml = "";
        xml=sldFact.createSLD(returnedNamedLayers);
        response.setContentType(mimeType);    
        
        log.debug("Returned sld: \n"+xml);
        
        out.write(xml);
    }
    
    public static void addSLDToCache(String sldUrl){
        cache.cacheUrl(sldUrl, false);
    }
    public static void clearCache(){
        cache.clearCache();
    }
}
