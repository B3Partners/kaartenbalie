<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<div id="content_c">
    <h1>Proefberekening</h1>
    
    <div id="calDiv" style="position:absolute; visibility:hidden; background-color:white; layer-background-color:white;"></div>
    <script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/calendar/CalendarPopup.js' module='' />"></script>
    <link rel="stylesheet" type="text/css" media="all" href="<html:rewrite page='/styles/calendar/calendar-style.css' module='' />" title="calendar-style" />
    <script type="text/javascript">
    var cal = new CalendarPopup("calDiv");
    cal.setCssPrefix("calcss_");
    </script>
    
    <html:form action="/pricingtestcalc.do?id=${id}" focus="startDate" onsubmit="return validateReportingForm(this)">
        <table>
            <tr>
                <td>
                    <label>Test vanaf :</label><html:text styleClass="testFrom" property="testFrom" styleId="testFrom"/>
                    <img src="<html:rewrite page='/images/siteImages/calendar_image.gif' module='' />" id="cal-button"
                         style="cursor: pointer; border: 1px solid red; vertical-align:text-bottom;" 
                         title="Date selector"
                         alt="Date selector"
                         onmouseover="this.style.background='red';" 
                         onmouseout="this.style.background=''"
                         onClick="cal.select(document.getElementById('testFrom'),'cal-button','yyyy-MM-dd', document.getElementById('testFrom').value); return false;"
                         name="cal-button"
                    />
                </td>
            </tr>
            <tr>
                <td>
                    <label>Test tot en met :</label><html:text styleClass="testUntil" property="testUntil" styleId="testUntil"/>
                    <img src="<html:rewrite page='/images/siteImages/calendar_image.gif' module='' />" id="cal-button"
                         style="cursor: pointer; border: 1px solid red; vertical-align:text-bottom;" 
                         title="Date selector"
                         alt="Date selector"
                         onmouseover="this.style.background='red';" 
                         onmouseout="this.style.background=''"
                         onClick="cal.select(document.getElementById('testUntil'),'cal-button','yyyy-MM-dd', document.getElementById('testUntil').value); return false;"
                         name="cal-button"
                    /> (Max. 7 dagen)
                </td>
            </tr>
            <tr>
                <td>
                    <label>Projectie :</label>
                    <html:select styleId="" property="testProjection" onchange="">
                        <c:forEach var="projectionString" items="${projections}">
                            <html:option value="${projectionString}" >${projectionString}</html:option>
                        </c:forEach>
                    </html:select>
                </td>
            </tr>
            <tr>
                <td>
                    <label>startSchaal :</label><html:text property="testScale" style="width:100px;"/>
                </td>
            </tr>
            <tr>
                <td>
                    <label>stapGrootte :</label><html:text property="testStepSize" style="width:100px;"/>
                </td>
            </tr>
            <tr>
                <td>
                    <label>Stappen :</label><html:text property="testSteps" style="width:100px;"/> (Max. 20 stappen)
                </td>
            </tr>
        </table>
        <html:submit property="test" style="margin-top: 10px; margin-bottom: 10px;">Voer proefberekening uit</html:submit>
    </html:form>
    <c:if test="${not empty resultSet}">
        <c:set var="lpWidth" value="${60}" scope="page"/>
        <c:set var="mWidth" value="${60}" scope="page"/>
        <c:set var="ctWidth" value="${50}" scope="page"/>
        <c:set var="tWidth" value="${lpWidth + mWidth + ctWidth}" scope="page"/>
        <table id="testCalcTable" style="width:${(fn:length(testDates) * tWidth) + tWidth}px;padding:0px;margin:0px;border-collapse: collapse;" class="table-stripeclass:table_alternate_tr">
            <thead>
                <tr class="serverRijTitel">
                    <th style="width: 120px;">Schaal/Datum</th>
                    <c:set var="firstDateCheck" scope="page" value="${false}"/>
                    <c:forEach items="${testDates}" var="date">
                        <c:choose>
                            <c:when test="${firstDateCheck == false}">
                                <c:set var="firstDateCheck" scope="page" value="${true}"/>        
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
                <c:set var="count" value="${0}" scope="page"/>
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
                    <c:set var="count" value="${count + 1}" scope="page"/>
                </c:forEach>
            </tbody>
        </table>
        <script type="text/javascript">
        Table.stripe(document.getElementById('testCalcTable'), 'table_alternate_tr');
        </script>
    </c:if>
</div>
