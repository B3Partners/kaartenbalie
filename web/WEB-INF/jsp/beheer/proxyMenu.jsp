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

<div id="nav">
    <center><B><fmt:message key="beheer.proxyMenu.title" /></B></center><br>
    
        <div id="nav-menu">

            <c:set var="requestURI" value="${fn:split(requestScope['javax.servlet.forward.request_uri'], '/')}" />
            <c:set var="requestJSP" value="${requestURI[fn:length(requestURI) - 1]}" />

            <c:set var="stijlklasse" value="menulink" />
            <a class="${stijlklasse}" href="<html:rewrite page='/index.do' module='' />"><fmt:message key="beheer.menu.home"/></a>

            <c:set var="stijlklasse" value="menulink" />
            <c:if test="${requestJSP eq 'mapserver.do'}">
                <c:set var="stijlklasse" value="activemenulink" />
            </c:if>
            <a class="${stijlklasse}" href="<html:rewrite page='/beheer/mapserver.do' module='' />"><fmt:message key="beheer.menu.mapserver"/></a>

            <c:set var="stijlklasse" value="menulink" />
            <c:if test="${requestJSP eq 'server.do'}">
                <c:set var="stijlklasse" value="activemenulink" />
            </c:if>
            <a class="${stijlklasse}" href="<html:rewrite page='/beheer/server.do' module='' />"><fmt:message key="beheer.menu.wmsservices"/></a>

            <c:set var="stijlklasse" value="menulink" />
            <c:if test="${requestJSP eq 'wfsserver.do'}">
                <c:set var="stijlklasse" value="activemenulink" />
            </c:if>
            <a class="${stijlklasse}" href="<html:rewrite page='/beheer/wfsserver.do' module='' />"><fmt:message key="beheer.menu.wfsservices"/></a>

            <c:set var="stijlklasse" value="menulink" />
            <c:if test="${requestJSP eq 'organization.do'}">
                <c:set var="stijlklasse" value="activemenulink" />
            </c:if>
            <a class="${stijlklasse}" href="<html:rewrite page='/beheer/organization.do' module='' />"><fmt:message key="beheer.menu.groepen"/></a>

            <c:set var="stijlklasse" value="menulink" />
            <c:if test="${requestJSP eq 'rights.do'}">
                <c:set var="stijlklasse" value="activemenulink" />
            </c:if>
            <a class="${stijlklasse}" href="<html:rewrite page='/beheer/rights2.do' module='' />"><fmt:message key="beheer.menu.rechten"/></a>

            <c:set var="stijlklasse" value="menulink" />
            <c:if test="${requestJSP eq 'user.do'}">
                <c:set var="stijlklasse" value="activemenulink" />
            </c:if>
            <a class="${stijlklasse}" href="<html:rewrite page='/beheer/user.do' module='' />"><fmt:message key="beheer.menu.gebruikers"/></a>

            <c:set var="stijlklasse" value="menulink" />
            <c:if test="${requestJSP eq 'role.do'}">
                <c:set var="stijlklasse" value="activemenulink" />
            </c:if>
            <a class="${stijlklasse}" href="<html:rewrite page='/beheer/role.do' module='' />"><fmt:message key="beheer.menu.rollen"/></a>
        </div>
</div>