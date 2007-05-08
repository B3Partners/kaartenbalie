<%@include file="/templates/taglibs.jsp" %>

<c:set var="focus" value="timeout" scope="request"/> 
<tiles:insert definition="common.setFocus"/>

<c:set var="form" value="${createPersonalURLForm}"/>
<c:set var="action" value="${form.map.action}"/>
<c:set var="mainid" value="${form.map.id}"/>

<c:set var="save" value="${action == 'save'}"/>
<c:set var="delete" value="${action == 'delete'}"/>

<!-- Scripts and settings for the calendar function -->
<html:link href="calendar-brown" title="summer" />

<%--
<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/calendar.js' module='' />"/>
<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/calendar-en.js' module='' />"/>
<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/calendar-setup.js' module='' />"/>
<link rel="stylesheet" type="text/css" media="all" href="<html:rewrite page='/styles/calendar-brown.css' module='' />" title="calendar-brown" />
--%>
<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/calendar/CalendarPopup.js' module='' />"></script>
<link rel="stylesheet" type="text/css" media="all" href="<html:rewrite page='/styles/calendar/calendar-style.css' module='' />" title="calendar-style" />
<script type="text/javascript">
    var cal = new CalendarPopup("calDiv");
    cal.setCssPrefix("calcss_");
</script>
<html:javascript formName="createPersonalURLForm" staticJavascript="false"/>
<html:form action="/createPersonalURL" onsubmit="return validateCreatePersonalURLForm(this)">
    <html:hidden property="action"/>
    <html:hidden property="alt_action"/>
    <html:hidden property="id" />
    
    <div class="containerdiv" style="float: left; clear: none;">
        <H1>Persoonlijke URL</H1>
        <P>
            Op deze pagina kunt een reeds aangemaakt persoonlijke URL opvragen of wijzigen of u kunt een nieuwe
            URL maken indien u er nog geen een aangemaakt had. De URL kunt u vervolgens ook weer gebruiken om een
            GetMap URL aan te maken.
            De URL stelt u in staat om op eenvoudige wijze met andere viewers dan de intern meegeleverde viewer van
            deze applicatie gebruik te kunnen maken van de Kaartenblia. Hieronder vindt u de huidige gegevens zoals
            deze in de applicatie bekend zijn. Om een nieuwe URL aan te maken of de huidige URL te wijzigen kunt u
            de knop Wijzigen gebruiken.
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
            
            <c:choose>
                <c:when test="${user.registeredIP != null}">
                    <tr>
                        <td>IP adres:</td>
                        <td><c:out value="${user.registeredIP}"/></td>
                    </tr>
                    <tr>
                        <td>Pers. URL:</td>
                        <td><c:out value="${user.personalURL}"/></td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td>IP adres:</td>
                        <td>Nog geen IP adres geregistreerd.</td>
                    </tr>
                    <tr>
                        <td>Pers. URL:</td>
                        <td>Nog geen URL aangemaakt.</td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </table>
        
        <c:if test="${action != 'list'}">
            <div id="calDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
            <div id="serverDetails" class="containerdiv" style="clear: left; padding-top: 15px;">
                <table>
                    <tr>
                        <td><fmt:message key="viewer.persoonlijkeurl.timeout"/>:</td>
                        <td>
                            
                            <html:text property="timeout" styleId="cal_date"/> &nbsp;
                            <img src="<html:rewrite page='/images/siteImages/calendar_image.gif' module='' />" id="cal-button"
                                 style="cursor: pointer; border: 1px solid red;" 
                                 title="Date selector"
                                 alt="Date selector"
                                 onmouseover="this.style.background='red';" 
                                 onmouseout="this.style.background=''"
                                 onClick="cal.select(document.getElementById('cal_date'),'cal-button','yyyy/MM/dd', document.getElementById('cal_date').value); return false;"
                                 name="cal-button"
                            />
                        </td>
                    </tr>
                    <tr>
                        <td><fmt:message key="viewer.persoonlijkeurl.registeredip"/>:</td> 
                        <td><html:text property="registeredIP"/>
                        <html:submit property="getIpAddress" styleClass="knop">
                            <fmt:message key="button.getipaddress"/>
                        </html:submit>
                    </tr>
                    <tr>
                        <td><fmt:message key="viewer.persoonlijkeurl.createdurl"/>:</td>
                        <td><html:text property="personalURL" styleId="personalURL" styleClass="readOnly" readonly="true" size="100" /></td>
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