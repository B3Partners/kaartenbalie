<%@include file="/templates/taglibs.jsp" %>

<c:set var="focus" value="newpassword" scope="request"/> 
<tiles:insert definition="common.setFocus"/>

<c:set var="form" value="${changeProfileForm}"/>
<c:set var="action" value="${form.map.action}"/>
<c:set var="mainid" value="${form.map.id}"/>

<c:set var="save" value="${action == 'save'}"/>
<c:set var="delete" value="${action == 'delete'}"/>

<html:javascript formName="changeProfileForm" staticJavascript="false"/>
<html:form action="/changeProfile" onsubmit="return validateChangeProfileForm(this)" focus="name">
    <html:hidden property="action"/>
    <html:hidden property="alt_action"/>
    <html:hidden property="id" />
    
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
                <td><c:out value="${user.firstName}"/>&nbsp;<c:out value="${user.lastName}"/></td>
            </tr>
            <tr>
                <td>Gebruikersnaam:</td>
                <td><c:out value="${user.username}"/></td>
            </tr>
            <tr>
                <td>Wachtwoord:</td>
                <td>*****</td>
            </tr>
            <tr>
                <td>Email adres:</td>
                <td><c:out value="${user.emailAddress}"/></td>
            </tr>
            <tr>
                <td>Role:</td>
                <td><c:out value="${user.role}"/></td>
            </tr>
        </table>
    </div>   
    <div id="groupDetails" style="clear: left; padding-top: 15px;">
        <c:if test="${action != 'list'}">
            <table>
                <tr>
                    <td><fmt:message key="viewer.persoonlijkeurl.nieuwpw"/>:</td>
                    <td><html:password property="newpassword" /></td>
                </tr>
                <tr>
                    <td><fmt:message key="viewer.persoonlijkeurl.retypepw"/>:</td>
                    <td><html:password property="newpasswordretyped" /></td>
                </tr>
                <tr>
                    <td><fmt:message key="viewer.profile.email"/>:</td>
                    <td><html:text property="emailaddress" /></td>
                </tr>
            </table>
        </c:if>
        
        <c:choose>
            <c:when test="${action != 'list'}">
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
                <div class="knoppen">
                    <html:submit property="edit" accesskey="n" styleClass="knop">
                        <fmt:message key="button.edit"/>
                    </html:submit>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    
</html:form>