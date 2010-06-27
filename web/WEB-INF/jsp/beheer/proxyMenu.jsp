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
    <p>
        <div id="nav-menu">

            <a href="<html:rewrite page='/index.do' module='' />"><fmt:message key="algemeen.home"/></a>
            <a href="<html:rewrite page='/beheer/mapserver.do' module='' />"><fmt:message key="beheer.mapserver"/></a>
            <a href="<html:rewrite page='/beheer/server.do' module='' />"><fmt:message key="beheer.server"/></a>
            <a href="<html:rewrite page='/beheer/wfsserver.do' module='' />"><fmt:message key="beheer.wfsserver"/></a>
            <a href="<html:rewrite page='/beheer/organization.do' module='' />"><fmt:message key="beheer.organization"/></a>
            <a href="<html:rewrite page='/beheer/rights.do' module='' />"><fmt:message key="beheer.rights"/></a>
            <a href="<html:rewrite page='/beheer/user.do' module='' />"><fmt:message key="beheer.users"/></a>
            <a href="<html:rewrite page='/beheer/role.do' module='' />"><fmt:message key="beheer.roles"/></a>
        </div>
    </p>
</div>