<%--
B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
for authentication/authorization, pricing and usage reporting.

Copyright 2006, 2007, 2008 B3Partners BV

This file is part of B3P Kaartenbalie.

B3P Kaartenbalie is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

B3P Kaartenbalie is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
--%>
<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<html:javascript formName="reportingForm" staticJavascript="false"/>

<script type="text/javascript" src="<html:rewrite page="/js/niftycube.js" module="" />"></script>
<link rel="stylesheet" type="text/css" href="<html:rewrite page="/styles/niftyCorners.css" module="" />">

<div id="calDiv" style="position:absolute; visibility:hidden; background-color:white;"></div>
<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/calendar/CalendarPopup.js' module='' />"></script>
<link rel="stylesheet" type="text/css" media="all" href="<html:rewrite page='/styles/calendar/calendar-style.css' module='' />" title="calendar-style" />
<script type="text/javascript">
    var cal = new CalendarPopup("calDiv");
    cal.setCssPrefix("calcss_");
</script>

<c:set var="checkAllSrc" scope="page"><html:rewrite page='/js/selectall.js' module=''/></c:set>
<script language="JavaScript" type="text/javascript" src="${checkAllSrc}"></script>

<H1><fmt:message key="beheer.reporting.title"/></H1>


<html:form action="/reporting" focus="startDate" onsubmit="return validateReportingForm(this)">
    <html:hidden property="action"/>
    <html:hidden property="alt_action"/>
    <html:hidden property="id" />

    <table>
        <tr>
            <td><b><fmt:message key="beheer.reporting.organization"/>:</b></td>
            <td>
                <html:select property="organizationId">
                    <c:forEach var="organization" items="${organizations}">
                        <html:option value="${organization.id}"> ${organization.name} (${organization.id})</html:option>
                    </c:forEach>
                </html:select>
            </td>
        </tr>
        <tr>
            <td><b><fmt:message key="beheer.reporting.startDate"/>:</b></td>
            <td>
                <html:text property="startDate" styleId="startDate"/>
                <img src="<html:rewrite page='/images/siteImages/calendar_image.gif' module='' />" id="cal-button1"
                     style="cursor: pointer; border: 1px solid red; vertical-align:text-bottom;"
                     title="Date selector"
                     alt="Date selector"
                     onmouseover="this.style.background='red';"
                     onmouseout="this.style.background=''"
                     onClick="cal.select(document.getElementById('startDate'),'cal-button1','yyyy-MM-dd', document.getElementById('startDate').value); return false;"
                     name="cal-button1"
                     />
            </td>
        </tr>
        <tr>
            <td><b><fmt:message key="beheer.reporting.endDate"/>:</b></td>
            <td>
                <html:text property="endDate" styleId="endDate"/>
                <img src="<html:rewrite page='/images/siteImages/calendar_image.gif' module='' />" id="cal-button2"
                     style="cursor: pointer; border: 1px solid red; vertical-align:text-bottom;"
                     title="Date selector"
                     alt="Date selector"
                     onmouseover="this.style.background='red';"
                     onmouseout="this.style.background=''"
                     onClick="cal.select(document.getElementById('endDate'),'cal-button2','yyyy-MM-dd', document.getElementById('endDate').value); return false;"
                     name="cal-button2"
                     />
            </td>
        </tr>
        <tr>
            <td><b><fmt:message key="beheer.reporting.name"/>:</b></td>
            <td>
                <html:text property="name" maxlength="255" size="40"/>
            </td>
        </tr>
        <tr>
            <td><b><fmt:message key="beheer.reporting.xsl"/>:</b></td>
            <td>
                <html:select property="xsl">
                    <html:option value="usage-wms.xsl">Algemeen overzicht WMS</html:option>
                    <html:option value="usage-wfs.xsl">Algemeen overzicht WFS</html:option>
                    <html:option value="">XML</html:option>
                </html:select>
            </td>
        </tr>
    </table>
    <html:submit property="create" styleClass="submit" onclick="javascript: alert('Het duurt enige tijd voor uw rapport is aangemaakt. Klik vernieuwen tot uw rapport verschijnt.');">
        <fmt:message key="button.report"/>
    </html:submit>
    <div style="height: 25px;"></div>
    <fieldset class="reportingFieldset">
        <legend><fmt:message key="beheer.reporting.reports"/></legend>
        <c:choose>
            <c:when test="${fn:length(reports) > 0}">
                <html:submit property="refresh" styleClass="submit" onclick="bCancel=true">Vernieuw</html:submit>
                <html:submit property="delete" styleClass="submit" styleId="removeChecked" onclick="bCancel=true">Verwijder geselecteerd</html:submit>
                <table id="reportTable" style="padding:0px; margin:0px; border-collapse: collapse; margin-left: 10px;" class="table-stripeclass:table_alternate_tr">
                    <thead>
                        <tr class="serverRijTitel">
                            <th align="center" style="width: 30px; height:14px;"><input type="checkbox" onclick="checkAll(1,this);"/></th>
                            <th style="width: 170px;">Tijdstip Rapport</th>
                            <th style="width: 170px;">Naam</th>
                            <th style="width: 120px;">Begindatum</th>
                            <th style="width: 120px;">Einddatum</th>
                            <th style="width: 75px;">Download</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="report" items="${reports}">
                            <tr>
                                <td align="center">
                                    <html:checkbox property="deleteReport" value="${report.id}"/>
                                </td>
                                <td>
                                    <fmt:formatDate pattern="dd-MM-yyyy, HH:mm:ss" value="${report.reportDate}"/>
                                </td>
                                <td>
                                    <c:out value="${report.name}"/>
                                </td>
                                <td>
                                    <fmt:formatDate pattern="dd-MM-yyyy" value="${report.startDate}"/>
                                </td>
                                <td>
                                    <fmt:formatDate pattern="dd-MM-yyyy" value="${report.endDate}"/>
                                </td>
                                <c:choose>
                                    <c:when test="${report.reportMime == 'text/html'}">
                                        <td>
                                            <html:link page="/reporting.do?download=submit&id=${report.id}" target="rapport"><html:img page="/images/icons/html.gif" border="0" module=""/></html:link>
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>
                                            <html:link page="/reporting.do?download=submit&id=${report.id}" target="rapport"><html:img page="/images/icons/xml.gif"  border="0" module=""/></html:link>
                                        </td>
                                    </c:otherwise>
                                </c:choose>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <script type="text/javascript">
                    Table.stripe(document.getElementById('reportTable'), 'table_alternate_tr');
                </script>
            </c:when>
            <c:otherwise>
                <fmt:message key="beheer.reporting.noreports"/>
            </c:otherwise>
        </c:choose>
    </fieldset>
</html:form>
