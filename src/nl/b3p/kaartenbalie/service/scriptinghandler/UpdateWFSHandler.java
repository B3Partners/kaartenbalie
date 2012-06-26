package nl.b3p.kaartenbalie.service.scriptinghandler;

import java.io.IOException;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import nl.b3p.ogc.utils.OGCScriptingRequest;

/**
 *
 * @author rachelle
 */
public class UpdateWFSHandler extends ScriptingHandler {    
    public UpdateWFSHandler() throws Exception{
        super();
    }
    
    public void getRequest(DataWrapper dw, User user) throws IOException, Exception {
        this.ogcrequest = (OGCScriptingRequest) dw.getOgcrequest();
        this.dw         = dw;
        this.user       = user;
        
        String service          = ogcrequest.getParameter(OGCScriptingRequest.SERVICE);
        if( service.equals("") ){
            updateAllServices();
        }
        else {
            updateServices(service);
        }
    }
    
    private void updateAllServices(){
        
    }
    
    private void updateServices(String service){
        
    }
    
}
