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

<c:set var="form" value="${serverForm.map}"/>
<c:set var="action" value="${form.action}"/>
<c:set var="mainid" value="${form.id}"/>

<c:set var="save" value="${action == 'save'}"/>
<c:set var="delete" value="${action == 'delete'}"/>

<script type="text/javascript" src="<html:rewrite page='/js/beheerJS.js' module='' />"></script>
<html:javascript formName="serverForm" staticJavascript="false"/>

<html:form action="/wfsserver" onsubmit="return validateServerForm(this)" focus="givenName">
    <html:hidden property="action"/>
    <html:hidden property="alt_action"/>
    <html:hidden property="id" />

    <div class="containerdiv" style="float: left; clear: none;">
        <H1><fmt:message key="beheer.wfsserver.title"/></H1>

        <c:choose>
            <c:when test="${!empty serviceproviderlist}">
                <c:set var="hoogte" value="${(fn:length(serviceproviderlist) * 28) + 28}" />
                <c:if test="${hoogte > 400}">
                    <c:set var="hoogte" value="400" />
                </c:if>
                <div class="scroll" style="height: ${hoogte}px; width: 840px;">
                    <table id="server_table" class="tablesorter">
                        <thead>
                            <tr>
                                <th style="width: 47%;" id="sort_col1"><fmt:message key="beheer.wfsserver.naam"/></th>
                                <th style="width: 34%;" id="sort_col2"><fmt:message key="beheer.wfsserver.afkorting"/></th>
                                <th style="width: 19%;" id="sort_col3"><fmt:message key="beheer.wfsserver.datumupdate"/></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="nServiceProvider" varStatus="status" items="${serviceproviderlist}">
                                <c:set var="id_selected" value='' />
                                <c:if test="${nServiceProvider.id == mainid}"><c:set var="id_selected" value=' id="regel_selected"' /></c:if>
                                <tr onmouseover="showLabel(${nServiceProvider.id})" onmouseout="hideLabel(${nServiceProvider.id});"${id_selected}>
                                    <td width="350">
                                        <div style="width: 340px; overflow: hidden;">
                                            <html:link page="/wfsserver.do?edit=submit&id=${nServiceProvider.id}">
                                                <c:out value="${nServiceProvider.givenName}"/>
                                            </html:link>
                                        </div>
                                    </td>
                                    <td width="250">
                                        <div style="width: 240px; overflow: hidden;"><c:out value="${nServiceProvider.abbr}"/></div>
                                    </td>
                                    <td width="140">
                                        <div style="width: 130px; overflow: hidden;"><fmt:formatDate pattern="dd-MM-yyyy" value="${nServiceProvider.updatedDate}"/></div>
                                    </td>
                                </tr>
                            <div id="infoLabel${nServiceProvider.id}" class="infoLabelClass">
                                <strong><fmt:message key="beheer.wfsserver.naam"/>:</strong> ${nServiceProvider.givenName}<br />
                                <strong><fmt:message key="beheer.wfsserver.afkorting"/>:</strong> ${nServiceProvider.abbr}<br />
                                <strong><fmt:message key="beheer.wfsserver.url"/>:</strong> ${nServiceProvider.url}<br />
                                <strong><fmt:message key="beheer.wfsserver.datumupdate"/>:</strong> <fmt:formatDate pattern="dd-MM-yyyy" value="${nServiceProvider.updatedDate}"/>
                            </div>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <script type="text/javascript">
                    if(document.getElementById('regel_selected')) {
                        $("#regel_selected").addClass('selected');
                        if(${hoogte} == 400) $(".scroll").scrollTop(($("#regel_selected").position().top - $("#regel_selected").parent().position().top));
                    }
                    $("#server_table").tablesorter({
                        widgets: ['zebra', 'hoverRows', 'fixedHeaders'],
                        sortList: [[0,0]],
                        headers: {
                            2: {
                                sorter:'dutchdates'
                            }
                        },
                        textExtraction: linkExtract
                    });
                </script>
            </c:when>
            <c:otherwise>
                <fmt:message key="beheer.wfsserver.noggeenbeschikbaar"/>
            </c:otherwise>
        </c:choose>
    </div>

    <div id="serverDetails" class="containerdiv" style="clear: left; padding-top: 15px; height: 150px;">
        <c:choose>
            <c:when test="${action != 'list'}">
                <div class="serverDetailsClass"> 
                    <table>
                        <tr>
                            <td><B><fmt:message key="beheer.serverName"/>:</B></td>
                            <td><html:text property="givenName" size="35" maxlength="60"/></td>
                        </tr>
                        <tr>
                            <td><B><fmt:message key="beheer.serviceProviderAbbr"/>:</B></td>
                            <td><html:text property="abbr" size="16" maxlength="10"/></td>
                        </tr>
                        <tr>
                            <td><B><fmt:message key="beheer.serverURL"/>:</B></td>
                            <td><html:text property="url" size="75" /></td>
                        </tr>
                    </table>

                    <br />
                    <div class="knoppen">
                        <html:cancel accesskey="c" styleClass="knop" onclick="bCancel=true" onmouseover="this.className='knopover';" onmouseout="this.className='knop';">
                            <fmt:message key="button.cancel"/>
                        </html:cancel>
                        <c:choose>
                            <c:when test="${save || delete}">
                                <html:submit property="confirm" accesskey="o" styleClass="knop" onmouseover="this.className='knopover';" onmouseout="this.className='knop';">
                                    <fmt:message key="button.ok"/>
                                </html:submit>
                            </c:when>
                            <c:when test="${not empty mainid}">
                                <html:submit property="save" accesskey="s" styleClass="knop">
                                    <fmt:message key="button.update"/>
                                </html:submit>
                                <html:submit property="deleteConfirm" accesskey="d" styleClass="knop" onclick="bCancel=true">
                                    <fmt:message key="button.remove"/>
                                </html:submit>
                            </c:when>
                            <c:otherwise>
                                <html:submit property="save" accesskey="s" styleClass="knop">
                                    <fmt:message key="button.save"/>
                                </html:submit>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <html:hidden property="givenName"/>
                <div class="knoppen">
                    <html:submit property="create" accesskey="n" styleClass="knop" onclick="bCancel=true">
                        <fmt:message key="button.new"/>
                    </html:submit>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    <div id="groupDetails" style="clear: left; padding-top: 15px; height: 10px;" class="containerdiv">
        &nbsp;
    </div>
</html:form>