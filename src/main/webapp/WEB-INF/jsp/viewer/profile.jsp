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

<c:set var="form" value="${userForm}"/>
<c:set var="action" value="${form.map.action}"/>
<c:set var="mainid" value="${form.map.id}"/>

<c:set var="save" value="${action == 'save'}"/>
<c:set var="delete" value="${action == 'delete'}"/>

<html:javascript formName="userForm" staticJavascript="false"/>
<html:form action="/changeProfile" onsubmit="return validateUserForm(this)" focus="password">
    <html:hidden property="action"/>
    <html:hidden property="alt_action"/>
    <html:hidden property="id" />
    <html:hidden property="firstname" />
    <html:hidden property="surname" />
    <html:hidden property="username" />
    <html:hidden property="personalURL" />
    <html:hidden property="registeredIP" />
    <html:hidden property="timeout" />
    <html:hidden property="organizationName" />
    <html:hidden property="organizationTelephone" />
    <html:hidden property="roleSelected" value="-1"/>
    
    <div class="containerdiv">
        <H1><fmt:message key="viewer.profile.title" /></H1>
        <P>
            <fmt:message key="viewer.profile.body" />
        </P>        
        
        <H2><fmt:message key="viewer.profile.gegevens" />:</H2>
        <table>
            <tr>
                <td><fmt:message key="viewer.profile.naam" />:</td>
                <td><c:out value="${form.map.firstname}"/>&nbsp;<c:out value="${form.map.surname}"/></td>
            </tr>
            <tr>
                <td><fmt:message key="viewer.profile.gebruikersnaam" />:</td>
                <td><c:out value="${form.map.username}"/></td>
            </tr>
            <tr>
                <td><fmt:message key="viewer.profile.emailadres" />:</td>
                <td><c:out value="${form.map.emailAddress}"/></td>
            </tr>
            <tr>
                <td><fmt:message key="viewer.profile.organisatie" />:</td>
                <td><c:out value="${form.map.organizationName}"/></td>
            </tr>
        </table>
    </div>   
    <div id="groupDetails" class="containerdiv" style="clear: left; padding-top: 15px;">
        <c:choose>
            <c:when test="${action != 'list'}">
                <div class="serverDetailsClass">
                    <table>
                        <tr>
                            <td><fmt:message key="viewer.persoonlijkeurl.nieuwpw"/>:</td>
                            <td><html:password property="password" /></td>
                        </tr>
                        <tr>
                            <td><fmt:message key="viewer.persoonlijkeurl.retypepw"/>:</td>
                            <td><html:password property="repeatpassword" /></td>
                        </tr>
                        <tr>
                            <td><fmt:message key="viewer.profile.email"/>:</td>
                            <td><html:text property="emailAddress" /></td>
                        </tr>
                    </table>
                    <br /><div class="knoppen">
                        <html:cancel accesskey="c" styleClass="knop" onclick="bCancel=true">
                            <fmt:message key="button.cancel"/>
                        </html:cancel>
                        <html:submit property="save" accesskey="s" styleClass="knop" onclick="bCancel=false">
                            <fmt:message key="button.update"/>
                        </html:submit>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <html:hidden property="password" />
                <div class="knoppen">
                    <html:submit property="edit" accesskey="n" styleClass="knop" onclick="bCancel=true">
                        <fmt:message key="button.edit"/>
                    </html:submit>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    <div id="groupDetails" style="clear: left; padding-top: 15px;" class="containerdiv">
        &nbsp;
    </div>
</html:form>