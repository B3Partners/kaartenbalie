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

<html:javascript formName="userForm" staticJavascript="false"/>

<html:form action="/registration" onsubmit="return validateUserForm(this)" focus="firstname">
    <html:hidden property="action"/>
    <html:hidden property="alt_action"/>
    <html:hidden property="id" />
    <html:hidden property="personalURL" />
    <html:hidden property="registeredIP" />
    <html:hidden property="timeout" />
    
    <div class="steps">
        <div class="step"><html:link module="" page="/demo.do"><fmt:message key="demo.steps.start" /></html:link></div>
        <div class="stepactive"><fmt:message key="demo.steps.registeren" /></div>
        <div class="step"><fmt:message key="demo.steps.kaarten" /></div>
        <div class="step"><fmt:message key="demo.steps.persurl" /></div>   
        <div class="step"><fmt:message key="demo.steps.viewer" /></div>   
    </div>
    
    <div id='democontent'>
        <div id="democontentheader"><fmt:message key="demo.registratie.header" /></div>
        <div id="democontenttext">    
            <fmt:message key="demo.registratie.body" />
        </div>
    </div><br />
    
    <div class="serverDetailsClass">
        <table>
            <tr>
                <td colspan="2">
                    <div id="demoheader3" style="margin-top: 0px;"><fmt:message key="demo.registratie.gegevens" /></div>
                    <c:if test="${not empty message}">
                        <div id="error">
                            <div><c:out value="${message}"/></div>
                        </div>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td width="190"><fmt:message key="beheer.userFirstname"/>:</td>
                <td><html:text property="firstname" size="25" /></td>
            </tr>
            <tr>
                <td><fmt:message key="beheer.userSurname"/>:</td>
                <td><html:text property="surname" size="25"/></td>
            </tr>
            <tr>
                <td><fmt:message key="beheer.userEmail"/>:</td>
                <td><html:text property="emailAddress" size="25"/></td>
            </tr>
            <tr>
                <td><fmt:message key="beheer.userUsername"/>:</td>
                <td><html:text property="username" size="25"/></td>
            </tr>
            <tr>
                <td><fmt:message key="beheer.userPassword"/>:</td>
                <td><html:password property="password" size="25"/></td>
            </tr>
            <tr>
                <td><fmt:message key="beheer.repeatpassword"/>:</td>
                <td><html:password property="repeatpassword" size="25"/></td>
            </tr>
            <tr>
                <td><fmt:message key="beheer.name"/>:</td>
                <td><html:text property="organizationName" size="25"/></td>
            </tr>
            <tr>
                <td><fmt:message key="beheer.organizationTelephone"/>:</td>
                <td><html:text property="organizationTelephone" size="25"/></td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td colspan="2" align="left">
                    <html:submit property="save">
                        <fmt:message key="demo.registratie.registreren" />
                    </html:submit>                                        
                </td>
            </tr>
        </table>
        
    </div>
</html:form>
<div id="groupDetails" style="clear: left; padding-top: 1px; height: 1px;" class="containerdiv">
    &nbsp;
</div>
