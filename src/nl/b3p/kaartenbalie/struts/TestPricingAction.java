/*
 * TestPricingAction.java
 *
 * Created on July 22, 2008, 1:30 PM
 *
 */

package nl.b3p.kaartenbalie.struts;

import java.util.Calendar;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.commons.struts.ExtendedMethodProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author Jytte
 */
public abstract class TestPricingAction extends KaartenbalieCrudAction {
    
    private static final Log log = LogFactory.getLog(PricingAction.class);
    
    private static final String TEST = "test";
    private static final String BACKFW = "back";
    private static final String BACK = "back";
    private static final String START_END_ERROR_KEY = "error.dateinput";
    private static final String LAYER_PLACEHOLDER_ERROR_KEY = "beheer.princing.placeholder.error";
    
    /** Creates a new instance of TestPricingAction */
    public TestPricingAction() {
    }
    
    protected Map getActionMethodPropertiesMap() {
        Map map = super.getActionMethodPropertiesMap();
        
        ExtendedMethodProperties crudProp = new ExtendedMethodProperties(TEST);
        crudProp.setDefaultForwardName(SUCCESS);
        crudProp.setDefaultMessageKey("beheer.pricing.test.succes");
        crudProp.setAlternateForwardName(FAILURE);
        crudProp.setAlternateMessageKey("beheer.pricing.test.failed");
        map.put(TEST, crudProp);
        
        crudProp = new ExtendedMethodProperties(BACK);
        crudProp.setDefaultForwardName(BACKFW);
        crudProp.setAlternateForwardName(BACKFW);
        map.put(BACK, crudProp);
        return map;
    }
    
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Calendar cal = Calendar.getInstance();
        dynaForm.set("testFrom", FormUtils.DateToFormString(cal.getTime(), null));
        cal.add(Calendar.DAY_OF_YEAR,7);
        dynaForm.set("testUntil", FormUtils.DateToFormString(cal.getTime(), null));
        
        prepareMethod(dynaForm, request, LIST, LIST);
        addDefaultMessage(mapping, request);
        return getDefaultForward(mapping, request);
    }
    
    public ActionForward back(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionRedirect redirect = new ActionRedirect(getDefaultForward(mapping, request));
        redirect.addParameter("id", dynaForm.getString("id"));
        redirect.addParameter("edit", "t");
        return redirect;
    }
    
    protected Integer getLayerID(DynaValidatorForm dynaForm) {
        return FormUtils.StringToInteger(dynaForm.getString("id"));
    }
    
    protected abstract ActionForward test(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception;
}