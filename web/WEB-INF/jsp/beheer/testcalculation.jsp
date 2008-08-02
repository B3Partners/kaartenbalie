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

<c:set var="form" value="${testPricingForm}"/>
<c:set var="action" value="${form.map.action}"/>
<c:set var="id" value="${form.map.id}"/>

<div id="content_c">
    <h1>Proefberekening</h1>
    <label>Service Provider:</label> ${spName} <br/>
    <label style="margin-bottom:10px;">Kaartlaag:</label> <b>${lName}</b><br/><br />
    
    <div id="calDiv" style="position:absolute; visibility:hidden; background-color:white;"></div>
    <script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/calendar/CalendarPopup.js' module='' />"></script>
    <link rel="stylesheet" type="text/css" media="all" href="<html:rewrite page='/styles/calendar/calendar-style.css' module='' />" title="calendar-style" />
    <script type="text/javascript">
        var cal = new CalendarPopup("calDiv");
        cal.setCssPrefix("calcss_");
    </script>
    
    <html:javascript formName="testPricingForm" staticJavascript="false"/>
    <html:form action="/pricingtest.do" focus="testFrom" onsubmit="return validateTestPricingForm(this)">
        <html:hidden property="action"/>
        <html:hidden property="alt_action"/>
        <html:hidden property="id"/>
        <table>
            <tr>
                <td>
                    <label><fmt:message key="beheer.pricing.test.testFrom"/>:</label><html:text styleClass="testFrom" property="testFrom" styleId="testFrom"/>
                    <img src="<html:rewrite page='/images/siteImages/calendar_image.gif' module='' />" id="cal-button1"
                         style="cursor: pointer; border: 1px solid red; vertical-align:text-bottom;" 
                         title="Date selector"
                         alt="Date selector"
                         onmouseover="this.style.background='red';" 
                         onmouseout="this.style.background=''"
                         onClick="cal.select(document.getElementById('testFrom'),'cal-button1','yyyy-MM-dd', document.getElementById('testFrom').value); return false;"
                         name="cal-button1"
                         />
                </td>
            </tr>
            <tr>
                <td>
                    <label><fmt:message key="beheer.pricing.test.testUntil"/>:</label><html:text styleClass="testUntil" property="testUntil" styleId="testUntil"/>
                    <img src="<html:rewrite page='/images/siteImages/calendar_image.gif' module='' />" id="cal-button2"
                         style="cursor: pointer; border: 1px solid red; vertical-align:text-bottom;" 
                         title="Date selector"
                         alt="Date selector"
                         onmouseover="this.style.background='red';" 
                         onmouseout="this.style.background=''"
                         onClick="cal.select(document.getElementById('testUntil'),'cal-button2','yyyy-MM-dd', document.getElementById('testUntil').value); return false;"
                         name="cal-button2"
                         /> (Max. 7 dagen)
                </td>
            </tr>
            <tr>
                <td>
                    <label><fmt:message key="beheer.pricing.test.testProjection"/>:</label>
                    <html:select styleId="" property="testProjection" onchange="">
                        <c:forEach var="projectionString" items="${projections}">
                            <html:option value="${projectionString}" >${projectionString}</html:option>
                        </c:forEach>
                    </html:select>
                </td>
            </tr>
            <tr>
                <td>
                    <label><fmt:message key="beheer.pricing.test.testScale"/>:</label><html:text property="testScale" style="width:100px;"/>
                </td>
            </tr>
            <tr>
                <td>
                    <label><fmt:message key="beheer.pricing.test.testStepSize"/>:</label><html:text property="testStepSize" style="width:100px;"/>
                </td>
            </tr>
            <tr>
                <td>
                    <label><fmt:message key="beheer.pricing.test.testSteps"/>:</label><html:text property="testSteps" style="width:100px;"/> (Max. 20 stappen)
                </td>
            </tr>
        </table>
        <html:submit property="test" style="margin-top: 10px; margin-bottom: 10px;">Voer proefberekening uit</html:submit>
        <button style="margin-top: 10px; margin-bottom: 10px;" onclick="location.href = '<html:rewrite page="/pricingtest.do?back=t&id=${id}" module="/beheer"/>'">Annuleren</button>
    </html:form>
    
    <c:if test="${not empty resultSet}">
        <c:set var="lpWidth" value="60"/>
        <c:set var="mWidth" value="60"/>
        <c:set var="ctWidth" value="50"/>
        <c:set var="tWidth" value="${lpWidth + mWidth + ctWidth}"/>
        <table id="testCalcTable" style="width:${(fn:length(testDates) * tWidth) + tWidth}px;padding:0px;margin:0px;border-collapse: collapse;" class="table-stripeclass:table_alternate_tr">
            <thead>
                <tr class="serverRijTitel">
                    <th style="width: 120px;">Schaal/Datum</th>
                    <c:set var="firstDateCheck" value="${false}"/>
                    <c:forEach items="${testDates}" var="date">
                        <c:choose>
                            <c:when test="${firstDateCheck == false}">
                                <c:set var="firstDateCheck" value="${true}"/>        
                                <th colspan="3" style="width:${tWidth}px;">Huidige tijd</th>        
                            </c:when>
                            <c:otherwise>
                                <th colspan="3"  style="width:${tWidth}px;"><fmt:formatDate pattern="yyyy-MM-dd @ HH:mm" value="${date}"/></th>        
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </tr>
            </thead>
            <tbody>
                <c:set var="count" value="0"/>
                <c:forEach items="${resultSet}" var="subSet">
                    <tr>
                        <td>${scaleSet[count]}</td>
                        <c:forEach items="${subSet}" var="lpc" varStatus="counter">
                            <c:choose>
                                <c:when test="${counter.count % 2 == 1}">
                                    <c:set var="columnColor" value="#E9F1F7" />
                                </c:when>
                                <c:otherwise>
                                    <c:set var="columnColor" value="auto" />
                                </c:otherwise>
                            </c:choose>
                            <td style="width:${lpWidth}px; background-color:${columnColor};">
                                <fmt:formatNumber value="${lpc.layerPrice}" minFractionDigits="2"  maxFractionDigits="2"/> c
                            </td>
                            <td style="width:${mWidth}px; background-color:${columnColor};">
                                <c:choose>
                                    <c:when test="${lpc.method == '-1'}"><img src="../images/icons/blocked.gif" alt="Kaart geblokkeerd."></c:when>
                                    <c:when test="${lpc.method == '0'}"><img src="../images/icons/owner.gif" alt="Prijsinformatie via eigen prijsbepalingen."></c:when>
                                    <c:when test="${lpc.method == '1'}"><img src="../images/icons/parent.gif" alt="Prijsinformatie via ouders."></c:when>
                                    <c:when test="${lpc.method == '2'}"><img src="../images/icons/childs.gif" alt="Prijsinformatie via childs."></c:when>                                        
                                    <c:otherwise><img src="../images/icons/na.gif" alt="All"></c:otherwise>
                                </c:choose>
                            </td>
                            <td style="width:${ctWidth}px; background-color:${columnColor};">
                                ${lpc.calculationTime}ms
                            </td>
                        </c:forEach>
                    </tr>
                    <c:set var="count" value="${count + 1}"/>
                </c:forEach>
            </tbody>
        </table>
        <script type="text/javascript">
            Table.stripe(document.getElementById('testCalcTable'), 'table_alternate_tr');
        </script>
    </c:if>
</div>
