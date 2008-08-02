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
<!-- Scripts and settings for the calendar function -->
<html:link href="calendar-brown" title="summer" />
<c:set var="calSrc" scope="page"><html:rewrite page='/js/calendar/CalendarPopup.js' module='' /></c:set>
<c:set var="calStyle" scope="page"><html:rewrite page='/styles/calendar/calendar-style.css' module='' /></c:set>
<div id="calDiv" style="position:absolute; visibility:hidden; background-color:white; layer-background-color:white;"></div>
<script language="JavaScript" type="text/javascript" src="${calSrc}"></script>
<link rel="stylesheet" type="text/css" media="all" href="${calStyle}" title="calendar-style" />
<script type="text/javascript">
    var cal = new CalendarPopup("calDiv");
    cal.setCssPrefix("calcss_");
</script>