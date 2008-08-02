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
Wachtwoord en/of gebruikersnaam zijn onjuist.<p>
Probeer het nogmaals:
<form action="j_security_check" method='post' >
    <div class="item">
        <fmt:message key="algemeen.username"/>:
    </div>
    <div class="value">
        <input type="text" name="j_username">
    </div>
    <div class="item">
        <fmt:message key="algemeen.password"/>:
    </div>
    <div class="value">
        <input type="password" name="j_password">
    </div>
    <html:submit property="login" styleClass="knop">
        <fmt:message key="button.login"/>
    </html:submit>
</form>
<script type="text/javascript" language="JavaScript">
    <!--
    var focusControl = document.forms[0].elements["j_username"];
    focusControl.focus();
    // -->
</script>
