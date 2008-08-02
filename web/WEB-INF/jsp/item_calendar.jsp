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

<c:set var="calImage" scope="page"><html:rewrite page='/images/siteImages/calendar_image.gif' module='' /></c:set>

<c:set var="element"><%= request.getParameter("elementStyleId")%></c:set>

<img src="${calImage}" id="cal-button"
     style="cursor: pointer; border: 1px solid red; vertical-align:text-bottom;" 
     title="Date selector"
     alt="Date selector"
     onmouseover="this.style.background='red';" 
     onmouseout="this.style.background=''"
     onClick="cal.select(document.getElementById('${element}'),'cal-button','yyyy-MM-dd', document.getElementById('${element}').value); return false;"
     name="cal-button"
     />