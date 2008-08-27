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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.kaartenbalie.core.server.reporting.control.ReportGenerator;
import nl.b3p.kaartenbalie.core.server.reporting.datausagereport.DataUsageReportThread;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import nl.b3p.commons.struts.ExtendedMethodProperties;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Chris Kramer
 */
public class ReportingAction extends KaartenbalieCrudAction {

    private static final Log log = LogFactory.getLog(ReportingAction.class);
    private static String reportGeneratorName = "reportgenerator";
    public static SimpleDateFormat reportingDate = new SimpleDateFormat("yyyy-MM-dd");
    protected static final String DOWNLOAD = "download";

    protected Map getActionMethodPropertiesMap() {
        Map map = super.getActionMethodPropertiesMap();
        ExtendedMethodProperties crudProp = new ExtendedMethodProperties(DOWNLOAD);
        crudProp.setDefaultForwardName(SUCCESS);
        crudProp.setDefaultMessageKey("beheer.reporting.download.succes");
        crudProp.setAlternateForwardName(FAILURE);
        crudProp.setAlternateMessageKey("beheer.reporting.download.failed");
        map.put(DOWNLOAD, crudProp);
        return map;
    }

    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        prepareMethod(dynaForm, request, LIST, LIST);
        addDefaultMessage(mapping, request);

        checkDateFields(dynaForm);
        this.createLists(dynaForm, request);
        return mapping.findForward(SUCCESS);
    }

    private static void checkDateFields(DynaValidatorForm dynaForm) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        if (dynaForm.get("endDate") == null || dynaForm.getString("endDate").length() == 0) {
            dynaForm.set("endDate", reportingDate.format(cal.getTime()));
        }
        if (dynaForm.get("startDate") == null || dynaForm.getString("startDate").length() == 0) {
            cal.add(Calendar.MONTH, -1);
            dynaForm.set("startDate", reportingDate.format(cal.getTime()));
        }

    }

    public ActionForward edit(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        EntityManager em = getEntityManager();
        return super.edit(mapping, dynaForm, request, response);
    }

    public ActionForward save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        EntityManager em = getEntityManager();
        return getDefaultForward(mapping, request);
    }

    public ActionForward delete(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        EntityManager em = getEntityManager();
        ReportGenerator rg = getReportGenerator(request);
        String[] deleteList = (String[]) dynaForm.get("deleteReport");
        for (int i = 0; i < deleteList.length; i++) {
            Integer id = FormUtils.StringToInteger(deleteList[i]);
            rg.removeReport(id);
        }
        return super.delete(mapping, dynaForm, request, response);
    }

    public ActionForward download(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer id = FormUtils.StringToInteger(dynaForm.getString("id"));
        Integer type = FormUtils.StringToInteger(request.getParameter("type"));
        if (type == null) {
            type = new Integer(0);
        }
        String[] resultInfo = ReportGenerator.getReportTypeInfo(type.intValue());
        if (resultInfo == null) {
            log.error("Unsupported resultType.");
            throw new Exception("Unsupported resultType.");
        }
        response.setContentType(resultInfo[0]);
        ReportGenerator rg = getReportGenerator(request);
        String fileName = rg.reportName(id) + "." + resultInfo[1];
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        rg.fetchReport(id, response.getOutputStream(), type.intValue(), getServlet().getServletContext());
        return null;
    }

    public ActionForward create(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        EntityManager em = getEntityManager();



        Date startDate = reportingDate.parse((String) dynaForm.get("startDate"));
        Date endDate = reportingDate.parse((String) dynaForm.get("endDate"));

        Integer organizationId = (Integer) dynaForm.get("organizationId");
        Organization organization = (Organization) em.find(Organization.class, organizationId);
        Map parameterMap = new HashMap();
        parameterMap.put("organization", organization);
        parameterMap.put("endDate", endDate);
        parameterMap.put("startDate", startDate);
        parameterMap.put("users", em.createQuery(
                "FROM User AS u " +
                "WHERE u.organization.id = :organizationId").setParameter("organizationId", organization.getId()).getResultList());
        ReportGenerator rg = getReportGenerator(request);
        rg.createReport(DataUsageReportThread.class, parameterMap);
        checkDateFields(dynaForm);
        return super.create(mapping, dynaForm, request, response);
    }

    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        super.createLists(form, request);
        EntityManager em = getEntityManager();
        ReportGenerator rg = getReportGenerator(request);
        request.setAttribute("workloadData", rg.getWorkload());
        request.setAttribute("reportStatus", rg.requestReportStatus());
        request.setAttribute("organizations", em.createQuery("FROM Organization AS org ORDER BY org.name ASC").getResultList());

    }

    private static ReportGenerator getReportGenerator(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        ReportGenerator rg = null;
        if (session.getAttribute(reportGeneratorName) != null) {
            rg = (ReportGenerator) session.getAttribute(reportGeneratorName);
        } else {
            EntityManager em = getEntityManager();
            User user = (User) em.find(User.class, ((User) request.getUserPrincipal()).getId());
            rg = new ReportGenerator(user, user.getOrganization());
            session.setAttribute(reportGeneratorName, rg);
        }
        return rg;
    }
}
