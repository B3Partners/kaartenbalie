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

<H1>Welkom op de Persoonlijke Pagina</H1>
<p>Hier kunt u een persoonlijke URL samenstellen zodat u via een externe viewer 
    het kaartmateriaal van kaartenbalie kunt bekijken. Deze samengestelde url heeft 
    een beveiligingssysteem dat als vervanging dient voor het inloggen. Hiermee
    kunt u dus iedere viewer gebruiken die de open WMS standaard ondersteunt.
    Natuurlijk kunt u ook onze viewer gebruiken.
</p>
<p>
    Onder Profiel kunt u uw wachtwoord en emailadres aanpassen. Tenslotte kunt u
    een GetMap URL gegenereren voor viewers die het GetCapabilities commando niet
    ondersteunen.
</p>

