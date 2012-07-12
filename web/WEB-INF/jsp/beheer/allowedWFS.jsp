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

<script type="text/javascript" src="<html:rewrite page='/js/beheerJS.js' module='' />"></script>
<html:javascript formName="serverForm" staticJavascript="false"/>

<div class="containerdiv" style="float: left; clear: none;">
    <H1><fmt:message key="beheer.allowedWFS.title" /></H1>

    <c:choose>
        <c:when test="${!empty currentServiceslist}">
            <div style="height: 325px;">
                <table id="server_table" class="tablesorter">
                    <thead>
                        <tr>
                            <th style="width: 45%;"><fmt:message key="beheer.server.table.naam" /></th>
                            <th style="width: 30%;"><fmt:message key="beheer.server.table.afkorting" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="nServiceProvider" varStatus="status" items="${currentServiceslist}">
                            <tr onmouseover="showLabel(${nServiceProvider.id})" onmouseout="hideLabel(${nServiceProvider.id});">
                                <td>
                                    <c:out value="${nServiceProvider.givenName}"/>
                                </td>
                                <td>
                                    <c:out value="${nServiceProvider.abbr}"/>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

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
            <fmt:message key="beheer.allowedWFS.noneAdded" />
        </c:otherwise>
    </c:choose>
</div>
<div id="serverDetails" class="containerdiv" style="clear: left; padding-top: 15px; min-height: 150px;">
    <html:form action="/allowedWFS">
        <input type="hidden" name="alt_action" value="list">
        
        <div class="knoppen" style="margin-bottom:40px">
            <html:hidden property="action" value="add"/>
            <p>
                <fmt:message key="beheer.allowedWMS.addService"/>
            </p>
            <html:select property="providerId">
                <c:forEach var="sp" items="${otherServiceslist}">
                    <html:option value="${sp.abbr}">
                        <c:out value="${sp.givenName}-${sp.abbr}"/>
                    </html:option>
                </c:forEach>
            </html:select>

            <html:submit property="save" accesskey="n" styleClass="knop">
                <fmt:message key="button.add"/>
            </html:submit>
        </div>
    </html:form>

    <html:form action="/allowedWFS">
        <input type="hidden" name="alt_action" value="list">
        
        <div class="knoppen">
            <html:hidden property="action" value="delete"/>
            <p>
                <fmt:message key="beheer.allowedWMS.deleteService"/>
            </p>
            <html:select property="providerId">
                <c:forEach var="sp" items="${currentServiceslist}">
                    <html:option value="${sp.abbr}">
                        <c:out value="${sp.givenName}-${sp.abbr}"/>
                    </html:option>
                </c:forEach>
            </html:select>

            <html:submit property="save" accesskey="n" styleClass="knop">
                <fmt:message key="button.delete"/>
            </html:submit>
        </div>
    </html:form>
</div>
