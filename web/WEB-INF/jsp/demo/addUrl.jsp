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

<div class="steps">
    <div class="step"><html:link module="" page="/demo.do">Start</html:link></div>
    <div class="step"><html:link module="/demo" page="/registration.do">Registreren</html:link></div>
    <div class="stepactive">Kaarten</div>
    <div class="step">Pers. URL</div>   
    <div class="step">Viewer</div>   
</div>


<div id='democontent'>
    <div id="democontentheader">Kaarten via een OGC WMS server toevoegen</div>
    <div id="democontenttext">
        Kaartenbalie heeft om het gebruik te kunnen demonstreren een aantal verschillende kaarten al in haar bestand
        opgenomen. Om u te overtuigen van de mogelijkheden van Kaartenbalie, bieden wij u aan om ook een
        eigen mapserver toe te voegen aan het systeem zodat u duidelijk deze mogelijkheden kunt zien en testen.
        <b>U kunt deze stap ook overslaan.</b><br><br>
        <div class="serverDetailsClass">
            <html:form action="/voegurltoe" focus="givenName">
                <c:if test="${not empty message}">
                    <div id="error">
                        <h3><c:out value="${message}"/></h3>
                    </div>
                </c:if>
                <table>
                    <tr>
                        <td><fmt:message key="demo.serverName"/>:</td>
                        <td><html:text property="givenName" /></td>
                    </tr>
                    <tr>
                        <td><fmt:message key="beheer.serviceProviderAbbr"/>:</td>
                        <td><html:text property="abbr" size="5" maxlength="60"/></td>
                    </tr>
                    <tr>
                        <td><fmt:message key="demo.serverURL"/>:</td>
                        <td>
                            <html:text property="url" size="75" />
                        </td>
                    </tr>
                    <html:hidden property="id"></html:hidden>
                    <TR>
                        <TD>&nbsp;</TD>
                        <TD>&nbsp;</TD>
                    </TR>
                    <tr>
                        <td colspan="2">
                            <html:submit property="save" >
                                <fmt:message key="button.add"/>
                            </html:submit>
                            <button onclick="location.href='<html:rewrite page="/createPersonalURL.do" module="/demo"/>'; return false;">Verder</button>
                        </td>
                        <td align="left"></td>
                    </tr>
                </table>
            </html:form>
        </div>
    </div>
</div>
<div id="groupDetails" style="clear: left; padding-top: 1px; height: 1px;" class="containerdiv">
    &nbsp;
</div>