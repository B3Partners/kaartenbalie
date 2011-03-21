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

<div class="steps">
    <div class="step"><html:link module="" page="/demo.do"><fmt:message key="demo.steps.start" /></html:link></div>
    <div class="step"><html:link module="/demo" page="/registration.do"><fmt:message key="demo.steps.registeren" /></html:link></div>
    <div class="stepactive"><fmt:message key="demo.steps.kaarten" /></div>
    <div class="step"><fmt:message key="demo.steps.persurl" /></div>   
    <div class="step"><fmt:message key="demo.steps.viewer" /></div>   
</div>


<div id='democontent'>
    <div id="democontentheader"><fmt:message key="demo.addUrl.title" /></div>
    <div id="democontenttext">
        <fmt:message key="demo.addUrl.text" />
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
                            <button onclick="location.href='<html:rewrite page="/createPersonalURL.do" module="/demo"/>'; return false;"><fmt:message key="demo.addUrl.verder" /></button>
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