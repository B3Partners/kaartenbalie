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

<H1>Welkom in de beheer module</H1>
Deze module stelt u in staat om de kaartenbalie naar uw wensen te configureren. Zo is het via
deze module mogelijk nieuwe WMS servers toe te voegen aan het systeem of oude te verwijderen. 
Daarnaast kunnen er via deze module gebruikers en organisaties aan het systeem toegevoegd worden
en kunnen er voor deze gebruikers op organisatie niveau rechten ingesteld worden waarbij bepaald 
wordt welke layers ze wel en geen recht op heeft om te bekijken.
<p>
Indien u zich al geregistreerd heeft kunt u met behulp van de onderstaande button meteen naar de viewer.
<p>
<html:button  property="viewer" onclick="javascript:window.location.href='/gisviewer/viewer.do'">
    <fmt:message key="button.toviewer"/>
</html:button>
