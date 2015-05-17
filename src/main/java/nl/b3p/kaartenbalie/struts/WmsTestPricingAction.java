/*
 * B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
 * for authentication/authorization, pricing and usage reporting.
 *
 * Copyright 2006, 2007, 2008 B3Partners BV
 * 
 * This file is part of B3P Kaartenbalie.
 * 
 * B3P Kaartenbalie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * B3P Kaartenbalie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.b3p.kaartenbalie.struts;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.kaartenbalie.core.server.accounting.ExtLayerCalculator;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPriceComposition;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPricing;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.ServiceProvider;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Chris Kramer
 */
public class WmsTestPricingAction extends TestPricingAction {

    private static final Log log = LogFactory.getLog(WmsPricingAction.class);
    private static final String START_END_ERROR_KEY = "error.dateinput";
    private static final String LAYER_PLACEHOLDER_ERROR_KEY = "beheer.princing.placeholder.error";

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
        Layer layer = null;
        String id = FormUtils.nullIfEmpty(getLayerID(dynaForm));
        if (id != null) {
            layer = getLayerByUniqueName(id);
        }
        if (layer==null || layer.getName() == null || layer.getName().trim().length() == 0) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, LAYER_PLACEHOLDER_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        String layerName = layer.getName();
        String spAbbr = layer.getSpAbbr();

        String projection = dynaForm.getString("testProjection");
        if (projection != null && projection.trim().length() == 0) {
            projection = null;
        }
        if (projection == null) {
            log.error("Projection is required!");
            throw new Exception("Projection is required!");
        }
        BigDecimal testScale = FormUtils.bdValueNull(dynaForm.getString("testScale"));
        if (testScale == null || testScale.doubleValue() <= 0) {
            testScale = new BigDecimal("100000.0");
        }
        BigDecimal testStepSize = FormUtils.bdValueNull(dynaForm.getString("testStepSize"));
        if (testStepSize == null || testStepSize.doubleValue() <= 0) {
            testStepSize = new BigDecimal("100000.0");
        }
        int testSteps = FormUtils.StringToInt(dynaForm.getString("testSteps"));
        if (testSteps > 20 || testSteps <= 0) {
            testSteps = 20;
        }
        ExtLayerCalculator lc = new ExtLayerCalculator();
        try {
            //Get all the dates in an array..
            List testDates = new ArrayList();
            Calendar cal = Calendar.getInstance();
            cal.setTime(testFrom);
            int maxDays = 7;
            int dayCounter = 0;
            testDates.add(new Date());
            while (cal.getTime().before(testUntil) && dayCounter < maxDays) {
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
                while (iterDates.hasNext()) {
                    Date testDate = (Date) iterDates.next();
                    LayerPriceComposition lpc = lc.calculateLayerComplete(spAbbr, layerName, testDate, projection, testScale, new BigDecimal("1"), LayerPricing.PAY_PER_REQUEST, "WMS", "GetMap");
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
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return getDefaultForward(mapping, request);
    }

    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        super.createLists(form, request);
        request.setAttribute("projections", KBConfiguration.SUPPORTED_PROJECTIONS);
        Layer layer = getLayerByUniqueName(getLayerID(form));
        if (layer == null || layer.getName() == null) {
            return;
        }
        ServiceProvider sp = layer.getServiceProvider();
        request.setAttribute("spName", sp.getTitle());
        request.setAttribute("lName", layer.getName());
    }
    
}