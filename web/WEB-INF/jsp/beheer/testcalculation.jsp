<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<jsp:include page="/WEB-INF/jsp/inc_calendar.jsp" flush="true"/>
<html:form action="/pricingtestcalc.do?id=${id}" focus="startDate" onsubmit="return validateReportingForm(this)">
    
    <label>Test vanaf :</label><html:text styleClass="testFrom" property="testFrom" styleId="testFrom"/>
    <jsp:include page="/WEB-INF/jsp/item_calendar.jsp" flush="true">
        <jsp:param name="elementStyleId" value="testFrom"/>
    </jsp:include>
    <br/>
    
    <label>Test tot en met :</label><html:text styleClass="testUntil" property="testUntil" styleId="testUntil"/>
    <jsp:include page="/WEB-INF/jsp/item_calendar.jsp" flush="true">
        <jsp:param name="elementStyleId" value="testUntil"/>
    </jsp:include> (Max. 7 dagen)
    <br/>
    <label>Projectie :</label>
    <html:select styleId="" property="testProjection" onchange="">
        <c:forEach var="projectionString" items="${projections}">
            <html:option value="${projectionString}" >${projectionString}</html:option>
        </c:forEach>
    </html:select><br/>
    <label>startSchaal :</label><html:text property="testScale" style="width:100px;"/><br/>
    <label>stapGrootte :</label><html:text property="testStepSize" style="width:100px;"/><br/>
    <label>Stappen :</label><html:text property="testSteps" style="width:100px;"/> (Max. 20 stappen)<br/> 
    
    <html:submit property="test" styleClass="submit">Run Pricing Test</html:submit>
</html:form>
<c:set var="lpWidth" value="${60}" scope="page"/>
<c:set var="mWidth" value="${60}" scope="page"/>
<c:set var="ctWidth" value="${50}" scope="page"/>
<c:set var="tWidth" value="${lpWidth + mWidth + ctWidth}" scope="page"/>
<table style="width:${(fn:length(testDates) * tWidth) + tWidth}px;border-collapse:collapse;">
    <thead>
        <tr>
            <th style="width:${tWidth}px;">Schaal/Datum</th>
            <c:set var="firstDateCheck" scope="page" value="${false}"/>
            <c:forEach items="${testDates}" var="date">
                <c:choose>
                    <c:when test="${firstDateCheck == false}">
                        <c:set var="firstDateCheck" scope="page" value="${true}"/>        
                        <th colspan="3" style="width:${tWidth}px;border-left:1px Solid Black;">Huidige tijd</th>        
                    </c:when>
                    <c:otherwise>
                        <th colspan="3"  style="width:${tWidth}px;border-left:1px Solid Black;"><fmt:formatDate pattern="yyyy-MM-dd @ HH:mm" value="${date}"/></th>        
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </tr>
    </thead>
    <c:set var="count" value="${0}" scope="page"/>
    <c:forEach items="${resultSet}" var="subSet">
        <tr>
            <th>${scaleSet[count]}</th>
            <c:forEach items="${subSet}" var="lpc">
                <td style="width:${lpWidth}px;border-left:1px Solid Black;">
                    <fmt:formatNumber value="${lpc.layerPrice}" minFractionDigits="2"  maxFractionDigits="2"/> c
                    
                </td>
                <td style="width:${mWidth}px;">
                    <c:choose>
                        <c:when test="${lpc.method == '-1'}"><img src="../images/icons/blocked.gif" alt="Kaart geblokkeerd."></c:when>
                        <c:when test="${lpc.method == '0'}"><img src="../images/icons/owner.gif" alt="Prijsinformatie via eigen prijsbepalingen."></c:when>
                        <c:when test="${lpc.method == '1'}"><img src="../images/icons/parent.gif" alt="Prijsinformatie via ouders."></c:when>
                        <c:when test="${lpc.method == '2'}"><img src="../images/icons/childs.gif" alt="Prijsinformatie via childs."></c:when>                                        
                        <c:otherwise><img src="../images/icons/na.gif" alt="All"></c:otherwise>
                    </c:choose>
                </td>
                <td style="width:${ctWidth}px;">
                    ${lpc.calculationTime}ms
                </td>
            </c:forEach>
        </tr>
        <c:set var="count" value="${count + 1}" scope="page"/>
    </c:forEach>
</table>

