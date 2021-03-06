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

<div class="content_body">
    <h1><fmt:message key="algemeen.index.title" /></h1>
    <table>
        <tr><td valign="top">
                <div id="inleiding" style="float: left;">
                    <p>
                        <fmt:message key="algemeen.index.inleiding.deel1" />
                    </p>
                    <p>
                        <fmt:message key="algemeen.index.inleiding.deel2" />
                    </p>
                    <p>
                        <fmt:message key="algemeen.index.inleiding.deel3" />
                    </p>
                </div>

            </td><td valign="top">
                &nbsp;&nbsp;
            </td><td valign="top">
                <dl>
                    <dt><b><fmt:message key="algemeen.index.beheer.title" /></b></dt>
                    <dd><fmt:message key="algemeen.index.beheer.body" /></dd>
                    <dt><b><fmt:message key="algemeen.index.extra.title" /></b></dt>
                    <dd><fmt:message key="algemeen.index.extra.body" /></dd>
                    <dt><b><fmt:message key="algemeen.index.persoonlijk.title" /></b></dt>
                    <dd><fmt:message key="algemeen.index.persoonlijk.body" /></dd>
                    <dt><b><fmt:message key="algemeen.index.metadataeditor.title" /></b></dt>
                    <dd><fmt:message key="algemeen.index.metadataeditor.body" /></dd>
                    <dt><b><fmt:message key="algemeen.index.viewer.title" /></b></dt>
                    <dd><fmt:message key="algemeen.index.viewer.body" /></dd>
                </dl>

        </td></tr>
    </table>
</div>
<div>
    <c:if test="${pageContext.request.remoteUser != null}">
        <fmt:message key="algemeen.index.ingelogdals" />: <c:out value="${pageContext.request.remoteUser}"/> | <html:link page="/logout.do" module=""><fmt:message key="algemeen.index.uitloggen" /></html:link>
    </c:if>    
</div>
