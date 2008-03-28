/*
 * AccountingAction.java
 *
 * Created on November 19, 2007, 9:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.struts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.commons.struts.ExtendedMethodProperties;
import nl.b3p.kaartenbalie.core.server.UniqueIndex;
import nl.b3p.kaartenbalie.core.server.accounting.LayerCalculator;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPriceComposition;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPricing;
import nl.b3p.kaartenbalie.core.server.datawarehousing.DwObjectAction;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.ServiceProvider;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.validator.DynaValidatorForm;
import org.hibernate.HibernateException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Chris Kramer
 */
public class TestPricingAction extends KaartenbalieCrudAction {
    
    private static final Log log = LogFactory.getLog(PricingAction.class);
    
    private static final String TEST = "test";
    private static final String BACKFW = "back";
    private static final String BACK = "back";
    
    private static final String START_END_ERROR_KEY = "error.dateinput";
    private static final String LAYER_PLACEHOLDER_ERROR_KEY = "beheer.princing.placeholder.error";
    
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
    
    public ActionForward test(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        Date testFrom = FormUtils.FormStringToDate(dynaForm.getString("testFrom"), null);
        Date testUntil = FormUtils.FormStringToDate(dynaForm.getString("testUntil"), null);
        
        if (testUntil != null && testFrom != null) {
            if (testUntil.before(testFrom)) {
                prepareMethod(dynaForm, request, EDIT, LIST);
                addAlternateMessage(mapping, request, START_END_ERROR_KEY);
                return getAlternateForward(mapping, request);
            }
        }
        
        Layer layer = getLayer(dynaForm, request);
        if (layer.getName() == null || layer.getName().trim().length() == 0) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, LAYER_PLACEHOLDER_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        String projection = dynaForm.getString("testProjection");
        if (projection != null && projection.trim().length() == 0) {
            projection = null;
        }
        if (projection == null) {
            log.error("Projection is required!");
            throw new Exception("Projection is required!");
        }
        
        BigDecimal testScale = FormUtils.bdValueNull(dynaForm.getString("testScale"));
        if (testScale == null || testScale.doubleValue() <= 0 )
            testScale = new BigDecimal(100000.0);
        BigDecimal testStepSize = FormUtils.bdValueNull(dynaForm.getString("testStepSize"));
        if (testStepSize == null || testStepSize.doubleValue() <= 0 )
            testStepSize = new BigDecimal(100000.0);
        int testSteps = FormUtils.StringToInt(dynaForm.getString("testSteps"));
        if (testSteps>20 || testSteps<=0)
            testSteps = 20;
        
        LayerCalculator lc = new LayerCalculator();
        try {
            
            //Get all the dates in an array..
            List testDates = new ArrayList();
            Calendar cal = Calendar.getInstance();
            cal.setTime(testFrom);
            int maxDays = 7;
            int dayCounter = 0;
            testDates.add(new Date());
            while(cal.getTime().before(testUntil) && dayCounter < maxDays) {
                Date testDate = cal.getTime();
                testDates.add(testDate);
                cal.add(Calendar.DAY_OF_YEAR, 1);
                dayCounter++;
            }
            
            List resultSet = new ArrayList();
            List scaleSet = new ArrayList();
            for (int i = 0; i < testSteps; i++) {
                List subSet = new ArrayList();
                Iterator iterDates = testDates.iterator();
                scaleSet.add(testScale);
                while(iterDates.hasNext()) {
                    Date testDate = (Date) iterDates.next();
                    LayerPriceComposition lpc = lc.calculateLayerComplete(layer.getId(), testDate, projection, testScale, new BigDecimal(1), LayerPricing.PAY_PER_REQUEST, "WMS", "GetMap");
                    subSet.add(lpc);
                }
                testScale = testScale.add(testStepSize);
                resultSet.add(subSet);
            }
            request.setAttribute("resultSet", resultSet);
            request.setAttribute("testDates", testDates);
            request.setAttribute("scaleSet", scaleSet);
        } finally {
            lc.closeEntityManager();
        }
        
        prepareMethod(dynaForm, request, LIST, LIST);
        addDefaultMessage(mapping, request);
        return getDefaultForward(mapping, request);
    }
    
    
    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        super.createLists(form, request);
        
        request.setAttribute("projections", KBConfiguration.SUPPORTED_PROJECTIONS);
        
        Layer layer = getLayer(form, request);
        if (layer==null || layer.getName() == null)
            return;
        
        ServiceProvider sp = layer.getServiceProvider();
        request.setAttribute("spName", sp.getTitle());
        request.setAttribute("lName", layer.getName());
        
    }
        
    private Layer getLayer(DynaValidatorForm dynaForm, HttpServletRequest request) {
        
        EntityManager em = getEntityManager();
        LayerPricing lp = null;
        Integer id = getLayerID(dynaForm);
        
        if(id==null)
            return null;
        return (Layer)em.find(Layer.class, id);
    }
    
    private Integer getLayerID(DynaValidatorForm dynaForm) {
        return FormUtils.StringToInteger(dynaForm.getString("id"));
    }
    
}
