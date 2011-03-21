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

<div id="nav">
    <center><B>Menu Beheer</B></center><br>
    <p>
        <div id="nav-menu">

            <a href="<html:rewrite page='/index.do' module='' />"><fmt:message key="algemeen.home"/></a>
            <a href="<html:rewrite page='/beheer/reporting.do' module='' />"><fmt:message key="beheer.reporting"/></a>
            <a href="<html:rewrite page='/beheer/metadata.do' module='' />"><fmt:message key="beheer.metadata"/></a>
            <a href="<html:rewrite page='/beheer/pricing.do' module='' />"><fmt:message key="beheer.pricing"/></a>
            <a href="<html:rewrite page='/beheer/accounting.do' module='' />"><fmt:message key="beheer.accounting"/></a>
        </div>
    </p>
</div>