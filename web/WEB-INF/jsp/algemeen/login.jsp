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

<div class="onderbalk" style="color: #FFF0C4;">LOGIN</div>
<form id="loginForm" action="j_security_check" method="POST">
    <div style="height: 430px">
        <div style="width: 430px; padding: 10px; border: 1px solid #dddddd;">
            <table>
                <tr><td>Gebruikersnaam:</td><td><input type="text" name="j_username" size="36"></td></tr>
                <tr><td>Wachtwoord:</td><td><input type="password" name="j_password" size="36"></td></tr>
                <tr><td><input type="Submit" value="Login"></td></tr>
            </table>
        </div>
    </div>    
</form>

<script language="JavaScript">
    <!--
    document.forms.loginForm.j_username.focus();
    // -->
</script>
