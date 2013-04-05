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

<div class="infobalk">
    <div class="infobalk_description"><fmt:message key="algemeen.login.title"/></div>
    <div class="infobalk_actions"></div>
</div>
<div class="content_body">
    <h1>Inloggen</h1>
    <fmt:message key="algemeen.invalidLogin.loginfault" />
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
</div>
<script type="text/javascript" language="JavaScript">
    <!--
    var focusControl = document.forms[0].elements["j_username"];
    focusControl.focus();
    // -->
</script>
