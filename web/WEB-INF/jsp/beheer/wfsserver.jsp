<%--
B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
for authentication/authorization, pricing and usage reporting.

Copyright 2007-2011 B3Partners BV.

This program is distributed under the terms
of the GNU General Public License.

You should have received a copy of the GNU General Public License
along with this software. If not, see http://www.gnu.org/licenses/gpl.html

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
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
                <div style="height: 325px;">
                    <table id="server_table" class="tablesorter">
                        <thead>
                            <tr>
                                <th style="width: 10%;">Teststatus</th>
                                <th style="width: 45%;"><fmt:message key="beheer.wfsserver.naam"/></th>
                                <th style="width: 30%;"><fmt:message key="beheer.wfsserver.afkorting"/></th>
                                <th style="width: 15%;"><fmt:message key="beheer.wfsserver.datumupdate"/></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="nServiceProvider" varStatus="status" items="${serviceproviderlist}">
                                <c:url var="link" value="/beheer/wfsserver.do?edit=submit&id=${nServiceProvider.id}" />
                                <c:set var="id_selected" value='' />
                                <c:if test="${nServiceProvider.id == mainid}"><c:set var="id_selected" value='selected' /></c:if>
                                <tr onmouseover="showLabel(${nServiceProvider.id})" onmouseout="hideLabel(${nServiceProvider.id});">

                                    <!-- status weergeven TODO: met link naar verzoek -->
                                    <c:if test="${nServiceProvider.status == 'GOED'}">
                                    <td>
                                        <span style="color: green;">
                                            <c:out value="${nServiceProvider.status}"/>
                                        </span>
                                    </td>
                                    </c:if>
                                    <c:if test="${nServiceProvider.status == 'FOUT'}">
                                    <td>
                                        <span style="color: red;">
                                            <c:out value="${nServiceProvider.status}"/>
                                        </span>
                                    </td>
                                    </c:if>

                                    <c:if test="${empty nServiceProvider.status}">
                                    <td>&nbsp;</td>
                                    </c:if>

                                    <td>
                                        <html:link page="/wfsserver.do?edit=submit&id=${nServiceProvider.id}">
                                            <c:out value="${nServiceProvider.givenName}"/>
                                        </html:link>
                                        <input type="hidden" name="link" value="${link}" /><input type="hidden" name="selected" value="${id_selected}" />
                                    </td>
                                    <td>
                                        <c:out value="${nServiceProvider.abbr}"/>
                                    </td>
                                    <td>
                                        <fmt:formatDate pattern="dd-MM-yyyy" value="${nServiceProvider.updatedDate}"/>
                                    </td>
                                </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <c:forEach var="nServiceProvider" varStatus="status" items="${serviceproviderlist}">
                <div id="infoLabel${nServiceProvider.id}" class="infoLabelClass">
                    <strong><fmt:message key="beheer.wfsserver.naam"/>:</strong> ${nServiceProvider.givenName}<br />
                    <strong><fmt:message key="beheer.wfsserver.afkorting"/>:</strong> ${nServiceProvider.abbr}<br />
                    <strong><fmt:message key="beheer.wfsserver.url"/>:</strong> ${nServiceProvider.url}<br />
                    <strong><fmt:message key="beheer.wfsserver.datumupdate"/>:</strong> <fmt:formatDate pattern="dd-MM-yyyy" value="${nServiceProvider.updatedDate}"/>
                </div>
                </c:forEach>
                            
                <script type="text/javascript">
                    tablepager(
                        'server_table',
                        '930',
                        '14',
                        false
                    );
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
                            <td><html:text property="abbr" size="16" maxlength="50"/></td>
                        </tr>
                        <tr>
                            <td><B><fmt:message key="beheer.serverURL"/>:</B></td>
                            <td><html:text property="url" size="75" /></td>
                        </tr>
                    </table>

                    <br />
                    <div class="knoppen">
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
                        <html:cancel accesskey="c" styleClass="knop" onclick="bCancel=true" onmouseover="this.className='knopover';" onmouseout="this.className='knop';">
                            <fmt:message key="button.cancel"/>
                        </html:cancel>
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

                <br/>
                <h2>Batch update</h2>
                <p>
                    Klik op Testen om de status van alle services bij te werken. Vul eventueel
                    het veld in om een stuk uit de huidige service url te vervangen. Gebruik dit als
                    je dezelfde services wilt gaan gebruiken in een andere omgeving. Bijvoorbeeld
                    van acceptatie naar productie. Let op: Tijdens het testen wordt de aangepaste
                    service url niet daadwerkelijk opgeslagen. Er wordt alleen gecontroleerd
                    of de service werkt met de vernieuwde url.
                </p>

                <table>
                    <tr>
                        <td>Expressie</td>
                        <td><html:text property="regexp" size="60" maxlength="255"/></td>
                    </tr>
                    <tr>
                        <td>Vervangen door:</td>
                        <td><html:text property="replacement" size="60" maxlength="80"/></td>
                    </tr>
                </table>

                <p>
                    <html:submit property="test" accesskey="t" styleClass="knop" onclick="bCancel=true">
                        Testen
                    </html:submit>

                    <html:submit property="batchUpdate" accesskey="b" styleClass="knop" onclick="bCancel=true">
                        Batch Update
                    </html:submit>
                </p>
            </c:otherwise>
        </c:choose>
    </div>
    <div id="groupDetails" style="clear: left; padding-top: 15px; height: 50px;" class="containerdiv">
        &nbsp;
    </div>
</html:form>