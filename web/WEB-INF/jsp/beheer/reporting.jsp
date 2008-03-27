<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<html:javascript formName="reportingForm" staticJavascript="false"/>

<script type="text/javascript" src="<html:rewrite page="/js/niftycube.js" module="" />"></script>
<link rel="stylesheet" type="text/css" href="<html:rewrite page="/styles/niftyCorners.css" module="" />">

<div id="calDiv" style="position:absolute; visibility:hidden; background-color:white; layer-background-color:white;"></div>
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
    
    
    <div class="tabcollection" id="collection1" style="">
        <div id="tabs">
            <ul id="tabul" style="width: 450px;">
                <li id="DataUsageReport" onclick="displayTabBySource(this);"><a href="#" style="width: 200px;">DataUsageReport</a></li>
                <li id="ServerPerformanceReport" onclick="displayTabBySource(this);"><a href="#" style="width: 200px;">ServerPerformanceReport</a></li>
            </ul>
        </div>
        <script type="text/javascript">Nifty("ul#tabul a","medium transparent top");</script>
        <div id="sheets" style="height:200px;">           
            <div id="DataUsageReport" class="sheet">
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
                </table>                
                <html:submit property="create" styleClass="submit">
                    <fmt:message key="button.report"/>
                </html:submit>
            </div>
            <div id="ServerPerformanceReport" class="sheet">
                Under construction
            </div>
        </div>
    </div>
    <div style="height: 25px;"></div>
    <fieldset class="reportingFieldset">
        <legend><fmt:message key="beheer.reporting.load"/></legend>
        
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
        
        <table>
            <tr>
                <td>
                    <div id="progressbar">
                        <div id="background">&nbsp;</div>
                        <div id="mask" style="width:${percentage}%; background-color:rgb(${red}, ${green}, 0);">&nbsp;</div>
                        <div id="indicator" >${percentage}%</div>
                    </div>
                </td>
            </tr>
             <tr>
                <td>
                    Wachtrij rapporten: ${workloadData[1]}
                </td>
            </tr>
        </table>       
    </fieldset>
    <div style="height: 5px;"></div>
    <fieldset class="reportingFieldset">
        <legend><fmt:message key="beheer.reporting.reports"/></legend>
        <c:choose>
            <c:when test="${fn:length(reportStatus) > 0}">
                <html:submit property="refresh" styleClass="submit" onclick="bCancel=true">Vernieuw</html:submit>
                <html:submit property="delete" styleClass="submit" styleId="removeChecked" onclick="bCancel=true">Verwijder geselecteerd</html:submit>
                <table id="reportTable" style="padding:0px; margin:0px; border-collapse: collapse; margin-left: 10px;" class="table-stripeclass:table_alternate_tr">
                    <thead>
                        <tr class="serverRijTitel">
                            <th align="center" style="width: 30px; height:14px;"><input type="checkbox" onclick="checkAll(1,this);"/></th>
                            <th style="width: 170px;">Datum</th>
                            <th style="width: 75px;">Status</th>
                            <th style="width: 320px;">Bericht</th>
                            <th style="width: 40px;"><html:img page="/images/icons/xml.gif" module=""/></th>
                            <th style="width: 40px;"><html:img page="/images/icons/xslt.gif" module=""/></th>
                            <th style="width: 40px;"><html:img page="/images/icons/html.gif" module=""/></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="trs" items="${reportStatus}">
                            <tr>
                                <td align="center">
                                    <c:if test="${trs.state == 0 || trs.state == 1}">
                                        <html:checkbox property="deleteReport" value="${trs.id}"/>
                                    </c:if>
                                </td>
                                <td>
                                    <fmt:formatDate pattern="dd-MM-yyyy, HH:mm:ss" value="${trs.creationDate}"/>
                                </td>
                                <td>
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
                                </td>
                                <td>
                                    ${trs.statusMessage}
                                </td>
                                <c:choose>
                                    <c:when test="${trs.state == 0}">                        
                                        <td>
                                            <html:link page="/reporting.do?download=submit&id=${trs.id}&type=0"><html:img page="/images/icons/xml.gif"  border="0" module=""/></html:link>
                                        </td>
                                        <td>
                                            <html:link page="/reporting.do?download=submit&id=${trs.id}&type=1"><html:img page="/images/icons/xslt.gif" border="0" module=""/></html:link>
                                        </td>
                                        <td>
                                            <html:link page="/reporting.do?download=submit&id=${trs.id}&type=2"><html:img page="/images/icons/html.gif" border="0" module=""/></html:link>
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        <td>&nbsp;</td>
                                        <td>&nbsp;</td>
                                        <td>&nbsp;</td>
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
<script language="JavaScript" type="text/javascript">
        window.onLoad = registerCollection('collection1', 'DataUsageReport');
</script>