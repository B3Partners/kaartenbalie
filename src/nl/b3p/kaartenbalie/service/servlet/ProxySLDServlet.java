/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import nl.b3p.kaartenbalie.service.requesthandler.OGCRequestHandler;
import nl.b3p.ogc.sld.SldNamedLayer;
import nl.b3p.ogc.sld.SldNamedLayer;
import nl.b3p.ogc.sld.SldReader;
import nl.b3p.ogc.sld.SldWriter;
import nl.b3p.wms.capabilities.ServiceProvider;

/**
 *
 * @author Roy
 */
public class ProxySLDServlet extends AbstractSimpleKbService {
    public static final String PARAM_ORIGINAL_SLD_URL="oriSldUrl";
    public static final String PARAM_SERVICEPROVIDER_ID="servProvId";
    public static final String mimeType = "application/xml";
    
    private static final URLCache cache= new URLCache(60000);
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws ServletException, IOException, InterruptedException, Exception {
        String oriSldUrl=request.getParameter(PARAM_ORIGINAL_SLD_URL);
        if (oriSldUrl==null || oriSldUrl.length()==0)
            throw new Exception("No param "+PARAM_ORIGINAL_SLD_URL+" provided");
        String sld= cache.getFromCache(oriSldUrl);        
        if (sld==null){
            throw new Exception("Error while getting SLD. Check the log for details");
        }
        Integer servProvId=null;
        try{
            servProvId=new Integer(request.getParameter(PARAM_SERVICEPROVIDER_ID));
        }catch(Exception e){
            throw new Exception ("No param "+PARAM_SERVICEPROVIDER_ID+" provided or not a number",e);
        }
        EntityManager em = getEntityManager();
        ServiceProvider sp = em.find(ServiceProvider.class, servProvId);
        //TODO: moet beter. Char set bepalen niet meegeven.
        SldReader sldReader = new SldReader();
        List<SldNamedLayer> namedLayers=sldReader.getNamedLayersBySld(sld,"UTF-8");
        
        List<SldNamedLayer> returnedNamedLayers=new ArrayList<SldNamedLayer>();
        Iterator<SldNamedLayer> it =namedLayers.iterator();
        while(it.hasNext()){
            SldNamedLayer nl= it.next();
            try{
                String[] codeAndName=OGCRequestHandler.toCodeAndName(nl.getName());
                if (codeAndName[0].equals(sp.getAbbr())){
                    nl.setName(codeAndName[1]);
                    returnedNamedLayers.add(nl);
                }
            }catch(Exception e){
                log.error("Fout bij maken SLD",e);
            }
        }
        SldWriter sldFact = new SldWriter();       
        String xml = "";
        xml=sldFact.createSLD(returnedNamedLayers);
        
        response.setContentType(mimeType);            
        out.write(xml);           
                
    }
    public static void addSLDToCache(String sldUrl){
        cache.cacheUrl(sldUrl, true);
    }
    public static void clearCache(){
        cache.clearCache();
    }
}
