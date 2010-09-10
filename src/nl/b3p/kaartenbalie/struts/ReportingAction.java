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

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import nl.b3p.commons.struts.ExtendedMethodProperties;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.reporting.CastorXmlTransformer;
import nl.b3p.kaartenbalie.reporting.Report;
import nl.b3p.kaartenbalie.reporting.ReportThread;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Chris Kramer
 */
public class ReportingAction extends KaartenbalieCrudAction {

    private static final Log log = LogFactory.getLog(ReportingAction.class);
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
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
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
        prepareMethod(dynaForm, request, EDIT, LIST);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return getDefaultForward(mapping, request);
    }

    public ActionForward save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return getDefaultForward(mapping, request);
    }

    public ActionForward delete(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        String[] deleteList = (String[]) dynaForm.get("deleteReport");
        for (int i = 0; i < deleteList.length; i++) {
            Integer id = FormUtils.StringToInteger(deleteList[i]);
            Report report = (Report) em.find(Report.class, id);
            if (report != null) {
                em.remove(report);
            }
            em.flush();
        }

        dynaForm.initialize(mapping);
        prepareMethod(dynaForm, request, LIST, EDIT);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return getDefaultForward(mapping, request);
    }

    public ActionForward download(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer id = FormUtils.StringToInteger(dynaForm.getString("id"));

        log.debug("Getting entity manager ......");
        EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);

        if (id != null) {
            Report report = (Report) em.find(Report.class, id);
            if (report != null) {
                String fileName = reportingDate.format(report.getReportDate()) + "_report_" + id;
                String xml = report.getReportXML();
                if (xml == null || xml.length() == 0) {
                    xml = "empty or unfinished";
                }
                response.setContentType(report.getReportMime());
                response.setContentLength(xml.length());
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                PrintWriter pw = response.getWriter();
                pw.print(xml);
            }
        }
        return null;
    }

    public ActionForward create(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();

        Date startDate = reportingDate.parse((String) dynaForm.get("startDate"));
        Date endDate = reportingDate.parse((String) dynaForm.get("endDate"));
        String xsl = (String) dynaForm.get("xsl");
        String name = (String) dynaForm.get("name");

        Integer organizationId = (Integer) dynaForm.get("organizationId");
        Organization organization = (Organization) em.find(Organization.class, organizationId);
        Map parameterMap = new HashMap();
        parameterMap.put("organization", organization);
        parameterMap.put("endDate", endDate);
        parameterMap.put("startDate", startDate);
        parameterMap.put("xsl", xsl);
        parameterMap.put("name", name);

        // TODO via gui opvragen
        if (xsl == null || xsl.length() == 0) {
            parameterMap.put("type", CastorXmlTransformer.XML);
        } else {
            parameterMap.put("type", CastorXmlTransformer.HTML);
        }

        parameterMap.put("users", em.createQuery(
                "from User AS u "
                + "join u.userOrganizations as uo "
                + "where uo.organization = :organization").setParameter("organization", organization).getResultList());


        ReportThread rtt = new ReportThread();
        rtt.init(parameterMap);
        rtt.start();

        dynaForm.initialize(mapping);
        prepareMethod(dynaForm, request, EDIT, LIST);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return getDefaultForward(mapping, request);
    }

    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        super.createLists(form, request);
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        request.setAttribute("organizations", em.createQuery("FROM Organization AS org ORDER BY org.name ASC").getResultList());
        //TODO alleen eigen organisaties
        request.setAttribute("reports", em.createQuery("FROM Report AS rep ORDER BY rep.reportDate ASC").getResultList());
        checkDateFields(form);
    }
}
