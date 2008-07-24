/*
 * ServerAction.java
 *
 * Created on July 18, 2008, 3:26 PM
 *
 * @author Jytte
 */

package nl.b3p.kaartenbalie.struts;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

public abstract class ServerAction extends KaartenbalieCrudAction {
    
    private static final Log log = LogFactory.getLog(ServerAction.class);
    protected static final String SERVER_CONNECTION_ERRORKEY = "error.serverconnection";
    protected static final String MALFORMED_URL_ERRORKEY = "error.malformedurl";
    protected static final String MALFORMED_CAPABILITY_ERRORKEY = "error.malformedcapability";
    protected static final String SERVICE_LINKED_ERROR_KEY = "error.servicestilllinked";
    protected static final String UNSUPPORTED_WMSVERSION_ERRORKEY = "error.wmsversion";
    protected static final String SP_NOTFOUND_ERROR_KEY = "error.spnotfound";
    protected static final String NON_UNIQUE_ABBREVIATION_ERROR_KEY = "error.abbr.notunique";
    protected static final String NON_ALPHANUMERIC_ABBREVIATION_ERROR_KEY = "error.abbr.notalphanumeric";
    protected static final String ABBR_RESERVED_ERROR_KEY = "error.abbr.reserved";
    
    protected static final String ORG_JOINED_KEY = "beheer.server.org.joined";
    protected static final String LAYER_JOINED_KEY = "beheer.server.layer.joined";
    protected static final String PRICING_JOINED_KEY = "beheer.server.pricing.joined";
    
    /** Creates a new instance of ServerAction */
    public ServerAction() {
    }
    
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
    // <editor-fold defaultstate="" desc="unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.createLists(dynaForm, request);
        prepareMethod(dynaForm, request, LIST, LIST);
        addDefaultMessage(mapping, request);
        return mapping.findForward(SUCCESS);
    }
    // </editor-fold>
    
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
    // <editor-fold defaultstate="" desc="getID(DynaValidatorForm dynaForm) method.">
    protected Integer getID(DynaValidatorForm dynaForm) {
        return FormUtils.StringToInteger(dynaForm.getString("id"));
    }
    // </editor-fold>
    
    protected boolean isAlphaNumeric(String s) {
        if (s==null)
            return false;
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(s);
        if(m.find()) {
            return false;
        }
        return true;
    }
}
