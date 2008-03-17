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
        <br>
        Kaartenbalie heeft om het gebruik te kunnen demonstreren een aantal verschillende kaarten al in haar bestand
        opgenomen. Om u te overtuigen van de mogelijkheden van Kaartenbalie, bieden wij u aan om ook een
        eigen mapserver toe te voegen aan het systeem zodat u duidelijk deze mogelijkheden kunt zien en testen.
        <b>U kunt deze stap ook overslaan.</b><br><br>
        <html:form action="/voegurltoe" focus="givenName">
            <c:if test="${not empty message}">
                <div id="error">
                    <h3><c:out value="${message}"/></h3>
                </div>
            </c:if>
            <table>
                <tr>
                    <td><B><fmt:message key="demo.serverName"/>:</B></td>
                    <td><html:text property="givenName" /></td>
                </tr>
                <tr>
                    <td><B><fmt:message key="beheer.serviceProviderAbbr"/>:</B></td>
                    <td><html:text property="abbr" size="5" maxlength="60"/></td>
                </tr>
                <tr>
                    <td><B><fmt:message key="demo.serverURL"/>:</B></td>
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
                        <button onclick="location.href='<html:rewrite page="/createPersonalURL.do" module="/demo"/>'">Verder</button>
                    </td>
                    <td align="left"></td>
                </tr>
            </table>
        </html:form>
    </div>
</div>
<div id="groupDetails" style="clear: left; padding-top: 1px; height: 1px;" class="containerdiv">
    &nbsp;
</div>