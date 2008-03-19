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
        <div class="step"><html:link module="" page="/demo.do">Start</html:link></div>
        <div class="stepactive">Registreren</div>
        <div class="step">Kaarten</div>
        <div class="step">Pers. URL</div>   
        <div class="step">Viewer</div>   
    </div>
    
    <div id='democontent'>
        <div id="democontentheader">Registratie</div>
        <div id="democontenttext">    
            Voordat u gebruik kunt maken van de Kaartenbalie Demo
            dient u zich eerst te registreren.<br>
            Zodra u geregistreerd bent als gebruiker
            heeft u toegang tot enige testkaarten.
        </div>
    </div><br />
    
    <div class="serverDetailsClass">
        <table>
            <tr>
                <td colspan="2">
                    <div id="demoheader3" style="margin-top: 0px;">Vul hieronder uw gegevens in</div>
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
                        Registreren
                    </html:submit>                                        
                </td>
            </tr>
        </table>
        
    </div>
</html:form>
<div id="groupDetails" style="clear: left; padding-top: 1px; height: 1px;" class="containerdiv">
    &nbsp;
</div>
