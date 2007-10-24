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
    
    <div class="containerdiv" style="float: left; clear: none;">
        <H1>Profiel wijzigen</H1>
        <P>
            Op deze pagina kunt uw profiel wijzigen. De velden zijn niet verplicht om in te vullen.
            Indien u velden leeg laat, zullen de oude waarden van dit veld automatisch opgenomen worden
            in de database.
        </P>        
        
        <H2>Huidige gegevens:</H2>
        <table>
            <tr>
                <td>Naam:</td>
                <td><c:out value="${form.map.firstname}"/>&nbsp;<c:out value="${form.map.surname}"/></td>
            </tr>
            <tr>
                <td>Gebruikersnaam:</td>
                <td><c:out value="${form.map.username}"/></td>
            </tr>
            <tr>
                <td>Email adres:</td>
                <td><c:out value="${form.map.emailAddress}"/></td>
            </tr>
        </table>
    </div>   
    <div id="groupDetails" style="clear: left; padding-top: 15px; height: 200px;">
        <c:choose>
            <c:when test="${action != 'list'}">
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
                <div class="knoppen">
                    <html:cancel accesskey="c" styleClass="knop" onclick="bCancel=true">
                        <fmt:message key="button.cancel"/>
                    </html:cancel>
                    <html:submit property="save" accesskey="s" styleClass="knop" onclick="bCancel=false">
                        <fmt:message key="button.update"/>
                    </html:submit>
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
    <div id="groupDetails" style="clear: left; padding-top: 15px; height: 10px;" class="containerdiv">
        &nbsp;
    </div>
</html:form>