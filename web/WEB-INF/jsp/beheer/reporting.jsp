<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<html:javascript formName="reportingForm" staticJavascript="false"/>


<jsp:include page="/WEB-INF/jsp/inc_calendar.jsp" flush="true"/>
<c:set var="checkAllSrc" scope="page"><html:rewrite page='/js/selectall.js' module=''/></c:set>
<script language="JavaScript" type="text/javascript" src="${checkAllSrc}"></script>
<div class="" style="">

<H1>Beheer Reporting</H1>
    
    <html:form action="/reporting" focus="startDate" onsubmit="return validateReportingForm(this)">
        <html:hidden property="action"/>
        <html:hidden property="alt_action"/>
        <html:hidden property="id" />
        
        <fieldset>
            <legend>Rapport Generatie</legend>
            <label>Start Datum :</label><html:text styleClass="reportInputField" property="startDate" styleId="startDate"/>
            <jsp:include page="/WEB-INF/jsp/item_calendar.jsp" flush="true">
                <jsp:param name="elementStyleId" value="startDate"/>
            </jsp:include>
            <br/>
            <label>Eind Datum :</label><html:text styleClass="reportInputField" property="endDate" styleId="endDate"/>
            <jsp:include page="/WEB-INF/jsp/item_calendar.jsp" flush="true">
                <jsp:param name="elementStyleId" value="endDate"/>
            </jsp:include>
            <br/>
            <label>Gebruikers :</label>alle<br/>
            <html:submit property="create" styleClass="submit">Maak rapport</html:submit>
        </fieldset>
    
        <fieldset>
            <legend>Reporting Server Load</legend>

            <fmt:formatNumber maxFractionDigits="0" var="percentage" value="${workloadData[0]}"/>
            <c:choose>
                <c:when test="${workloadData[0] < 50}">
                     <c:set var="green" scope="page" value="${100}"/>
                </c:when>
                <c:otherwise>
                     <c:set var="green" scope="page" value="${255- (255*((percentage-50)/50))}"/>
                     <fmt:formatNumber maxFractionDigits="0" var="green" value="${green}"/>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${workloadData[0] > 50}">
                    <c:set var="red" scope="page" value="${255}"/>
                </c:when>
                <c:otherwise>
                     <c:set var="red" scope="page" value="${(percentage/50)*255}"/>
                     <fmt:formatNumber maxFractionDigits="0" var="red" value="${red}"/>
                </c:otherwise>
            </c:choose>

            
            <div id="progressbar">
                <div id="background">&nbsp;</div>
                <div id="mask" style="width:${percentage}%; background-color:rgb(${red}, ${green}, 0);">&nbsp;</div>
                <div id="indicator" >${percentage}%</div>
            </div>
            <label>Status:</label>
            <c:choose>
                <c:when test="${workloadData[0] > 95}">
                    Extremely Busy
                </c:when>
                <c:when test="${workloadData[0] > 0}">
                    Busy
                </c:when>
                <c:otherwise>
                    Idle
                </c:otherwise>
            </c:choose>
            <br/>

            <label>Reports waiting in Queue:</label> ${workloadData[1]}
            </p>
        </fieldset>
        <fieldset>
            <legend>Reports</legend>
            <html:submit property="delete" styleClass="submit" styleId="removeChecked" onclick="bCancel=true">Verwijder Geselecteerd</html:submit>
            <html:submit property="refresh" styleClass="submit" onclick="bCancel=true">Ververs</html:submit>
            <div class="transactieRijTitel">
                <div style="width: 30px;height:14px;"><input type="checkbox" onclick="checkAll(1,this);"/></div>
                <div style="width: 150px;">Datum</div>
                <div style="width: 75px;">Status</div>
                <div style="width: 400px;">Bericht</div>
                <div style="width: 30px;">[D]</div>
            </div>
            <c:set var="hoogte" value="200" />
            <c:if test="${hoogte > 230}">
                <c:set var="hoogte" value="230" />
            </c:if>
            <div class="tableContainer" id="tableContainer" style="height: ${hoogte}px">          
                <c:forEach var="trs" items="${reportStatus}">
                    <div class="transactieRij">
                        <div style="width: 30px;" class="">
                            <c:if test="${trs.state == 0 || trs.state == 1}">
                                <html:checkbox property="deleteReport" value="${trs.id}"/>
                            </c:if>
                        </div>
                        <div style="width: 150px;" class="">
                            ${trs.creationDate}
                        </div>
                        <div style="width: 75px;" class="">
                            <c:choose>
                                <c:when test="${trs.state == 0}">
                                    Voltooid
                                </c:when>
                                <c:when test="${trs.state == 1}">
                                    Geannuleerd
                                </c:when>
                                <c:when test="${trs.state == 2}">
                                    Aangemaakt
                                </c:when>
                                <c:when test="${trs.state == 3}">
                                    Bezig
                                </c:when>
                                <c:when test="${trs.state == 4}">
                                    Wachtrij
                                </c:when>
                                <c:otherwise></c:otherwise>
                            </c:choose>
                        </div>
                        <div style="width: 400px;" class="">
                            ${trs.statusMessage}
                        </div>
                        <div style="width: 30px;" class="">
                                <c:if test="${trs.state == 0}">
                                    [<a href="reporting.do?download=submit&id=${trs.id}">D</a>]
                                </c:if>
                        </div>
                    </div>                
                </c:forEach>
            </div>           
        </fieldset>
    </html:form>

</div>