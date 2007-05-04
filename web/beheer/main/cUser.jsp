<%@include file="/templates/taglibs.jsp" %>

<c:set var="form" value="${userForm}"/>
<c:set var="action" value="${form.map.action}"/>
<c:set var="mainid" value="${form.map.id}"/>

<c:set var="save" value="${action == 'save'}"/>
<c:set var="delete" value="${action == 'delete'}"/>

<html:javascript formName="userForm" staticJavascript="false"/>
<html:form action="/user" onsubmit="return validateUserForm(this)" focus="firstname">
    <html:hidden property="action"/>
    <html:hidden property="alt_action"/>
    <html:hidden property="id" />
        
    <div class="containerdiv" style="float: left; clear: none;">
        <H1>Beheer Gebruikers</H1>
        
        <b>Gebruikers tabel:</b>
        <div class="serverRijTitel">
            <div style="width: 160px;">Voornaam</div>
            <div style="width: 160px;">Achternaam</div>
            <div style="width: 160px;">Gebruikersnaam</div>
            <div style="width: 160px;">E-mailadres</div>
        </div>
        <DIV class="tableContainer" id="tableContainer">          
            <c:forEach var="nUser" varStatus="status" items="${userlist}">
                <div class="serverRij">    
                    <div style="width: 160px;" class="vakSpeciaal" title="<c:out value="${nUser.firstName}"/>">
                        <c:out value="${nUser.firstName}"/>
                    </div>
                    <div style="width: 160px;" title="<c:out value="${nUser.lastName}"/>">
                        <c:out value="${nUser.lastName}"/>
                    </div>
                    <div style="width: 160px;" class="vakSpeciaal" title="<c:out value="${nUser.username}"/>">
                        <html:link page="/user.do?edit=submit&id=${nUser.id}">
                            <c:out value="${nUser.username}"/>
                        </html:link>
                    </div>
                    <div style="width: 160px;" title="<c:out value="${nUser.emailAddress}"/>">
                        <c:out value="${nUser.emailAddress}"/>
                    </div>
                </div>    
            </c:forEach>
        </DIV>
    </div>
    
    <c:if test="${action != 'list'}">
        <div id="groupDetails" style="clear: left; padding-top: 15px;" class="containerdiv">
            <table>
                <tr>
                    <td><B><fmt:message key="beheer.userFirstname"/>:</B></td>
                    <td><html:text property="firstname"/></td>
                </tr>
                <tr>
                    <td><B><fmt:message key="beheer.userSurname"/>:</B></td>
                    <td><html:text property="lastname"/></td>
                </tr>
                <tr>
                    <td><B><fmt:message key="beheer.userEmail"/>:</B></td>
                    <td><html:text property="emailAddress"/></td>
                </tr>
                <tr>
                    <td><B><fmt:message key="beheer.userUsername"/>:</B></td>
                    <td><html:text property="username"/></td>
                </tr>
                <tr>
                    <td><B><fmt:message key="beheer.userPassword"/>:</B></td>
                    <td><html:password property="password"/></td>
                </tr>
                <tr>
                    <td><B><fmt:message key="beheer.userOrganization"/>:<B></td>
                    <td>
                        <html:select property="selectedOrganization">
                            <c:forEach var="nOrganization" varStatus="status" items="${organizationlist}">
                                <html:option value="${nOrganization.id}">
                                    ${nOrganization.name}
                                </html:option>
                            </c:forEach>
                        </html:select>     
                    </td>
                </tr>
                <tr>
                    <td><B><fmt:message key="beheer.userRole"/>:</B></td>
                    <td>
                        <html:select property="selectedRole">
                            <html:option value="beheerder">beheerder</html:option>
                            <html:option value="gebruiker">gebruiker</html:option>
                            <html:option value="demogebruiker">demogebruiker</html:option>
                        </html:select>     
                    </td>
                </tr>
            </table>
        </div>
    </c:if>
    
    <c:choose>
        <c:when test="${action != 'list'}">
            <div class="knoppen">
                <html:cancel accesskey="c" styleClass="knop" onclick="bCancel=true">
                    <fmt:message key="button.cancel"/>
                </html:cancel>
                <c:if test="${empty mainid}">
                    <html:submit property="save" accesskey="s" styleClass="knop">
                        <fmt:message key="button.save"/>
                    </html:submit>
                </c:if>
                <c:if test="${not empty mainid}">
                    <html:submit property="save" accesskey="s" styleClass="knop">
                        <fmt:message key="button.update"/>
                    </html:submit>
                    <html:submit property="delete" accesskey="d" styleClass="knop" onclick="bCancel=true">
                        <fmt:message key="button.remove"/>
                    </html:submit>
                </c:if>
            </div>
        </c:when>
        <c:otherwise>
            <div class="knoppen">
                <html:submit property="create" accesskey="n" styleClass="knop" onclick="bCancel=true">
                    <fmt:message key="button.new"/>
                </html:submit>
            </div>
        </c:otherwise>
    </c:choose>
</html:form>