<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<html:javascript formName="userForm" staticJavascript="false"/>

<html:form action="/registration" onsubmit="return validateUserForm(this)" focus="firstname">
    <html:hidden property="action"/>
    <html:hidden property="alt_action"/>
    <html:hidden property="id" />
    <html:hidden property="personalURL" />
    <html:hidden property="registeredIP" />
    <html:hidden property="timeout" />
    
    <div id='democontent'>
        <div id="democontentheader">Registratie pagina</div>
        <div id="democontenttext">    
            Voordat u gebruik kunt maken van de Kaartenbalie Demo
            dient u zich eerst te registreren. Zodra u geregistreerd bent als gebruiker
            heeft u toegang tot enige testkaarten.
        </div>
    </div>
    
    <div id="demoheader3">Uw Gegevens</div>
    <c:if test="${not empty message}">
        <div id="error">
            <div><c:out value="${message}"/></div>
        </div>
    </c:if>
    <table>
        <tr>
            <td><B><fmt:message key="beheer.userFirstname"/>:</B></td>
            <td><html:text property="firstname"/></td>
            <td><B><fmt:message key="beheer.userSurname"/>:</B></td>
            <td><html:text property="surname"/></td>
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
            <td><B><fmt:message key="beheer.repeatpassword"/>:</B></td>
            <td><html:password property="repeatpassword"/></td>
        </tr>
        <tr>
            <td><B><fmt:message key="beheer.name"/>:</B></td>
            <td><html:text property="organizationName"/></td>
        </tr>
        <tr>
            <td><B><fmt:message key="beheer.organizationTelephone"/>:</B></td>
            <td><html:text property="organizationTelephone"/></td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td colspan="0"><center>
                    <html:reset>
                        <fmt:message key="button.reset"/>
                    </html:reset>
                    <html:submit property="save" >
                        <fmt:message key="button.ok"/>
                    </html:submit>                                        
            </center></td>
        </tr>
    </table>
</html:form>
<div id="groupDetails" style="clear: left; padding-top: 1px; height: 1px;" class="containerdiv">
    &nbsp;
</div>
