<%@include file="/templates/taglibs.jsp" %>

<html:javascript formName="registrationForm" staticJavascript="false"/>

<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/intellihelp.js' module='' />"></script>

<script language="JavaScript" type="text/javascript">
        var titleArray = new Array();
        titleArray[0] = "Veiligheid";
        
        
        //TOOLTIP BODY TEXT
        var bodyArray = new Array();
        bodyArray[0] = "Kaartenbalie is opgezet met veiligheid in het achterhoofd. Het kaartmateriaal dat via Kaartenablie aangeboden wordt en getoond wordt is in een aantal gevallen auteursrechtelijk beschermd. Om te voorkomen dat iedereen deze informatie al naar gelang op kan vragen is Kaartenbalie afgeschermd en zijn er voor gebruikers rechten in te stellen die bepalen wat zij wel of niet mogen zien.";
        
        
        //TOOLTIP DISPLAY LINK
        var linkArray = new Array();
        linkArray[0] = "http://www.b3p.nl/";
        
        
        //TOOLTIP URL
        var urlArray = new Array();
        urlArray[0] = "http://www.b3p.nl/";
        
        
        //TOOLTIP OFFSET
        var xOffsetArray = new Array();
        xOffsetArray[0] = 0;
        
        
        var yOffsetArray = new Array();
        yOffsetArray[0] = 0;
        
        
        //TOOLTIP BOX DEFAULT WIDTH
        var toolTipDefaultWidth = 220;

        //TOOLTIP STYLING
        // 	Allows you to adjust the tooltip background color for the 
        //	roll-over and roll-off states, the font used for the tooltip,
        // 	the colors of the title and display URL links for the roll-over
        //	and roll-off states and whether or not the links should be
        // 	underlined at any point.
        //-------------- 
        var tooltipBkgColor = "#EEEEEE";
        var tooltipHighlightColor = "#FFFFE0";
        var tooltipFont = "Verdana, Arial, Helvetica";
        var tooltipTitleColorOff = "#0000DE";
        var tooltipTitleColorOn = "#0000DE";
        var tooltipURLColorOff = "#008000";
        var tooltipURLColorOn = "#008000";
        var tooltipTitleDecorationOff = "none";
        var tooltipTitleDecorationOn = "underline";
        var tooltipURLDecorationOff = "none";
        var tooltipURLDecorationOn = "underline";
        //............................................................
        // 								END EDITABLE TOOLTIP VARIABLES
</script>

<c:choose>
    <c:when test="${DemoActive == true}">
        <c:choose>
            <c:when test="${not empty id}">
                <html:form action="/voegurltoe">
                    <div id='democontent'>
                    <div id="democontentheader">Registratie pagina</div>
                    <div id="democontenttext">
                    
                    <div id="tooltipBox" 
                         onMouseOver="clearAdInterval();highlightAd('itxtTbl');" 
                         onMouseOut="hideAd();unHighlightAd('itxtTbl');" 
                         style="z-index:5000;position:absolute;cursor:pointer;">
                    </div>
                    
                    Uw registratie is gelukt. Hieronder vindt u een overzicht van uw gegevens zoals u deze tijdens de
                    registratiestap heeft opgegeven. Daarnaast vindt u hier meteen uw persoonlijke URL. Hiermee kunt
                    u het kaartmateriaal ook via uw eigen GIS viewer opvragen.
                    
                    <div id="demoheader3">Uw gegegevens</div>
                    <table>
                        <tr>
                            <td><B><fmt:message key="beheer.userFirstname"/>:</B>&nbsp;<c:out value="${firstname}"/>&nbsp;<c:out value="${lastname}"/></td>
                        </tr>
                        <tr>
                            <td><B><fmt:message key="beheer.userEmail"/>:</B>&nbsp;<c:out value="${emailAddress}"/></td>
                        </tr>
                        <tr>
                            <td><B><fmt:message key="beheer.userUsername"/>:</B>&nbsp;<c:out value="${username}"/></td>
                        </tr>
                        <tr>
                            <td><B><fmt:message key="viewer.persoonlijkeurl"/>:</B>&nbsp;<c:out value="${personalURL}"/></td>
                        </tr>
                        <tr>
                            <td><B><fmt:message key="beheer.name"/>:</B>&nbsp;<c:out value="${name}"/></td>
                        </tr>
                        <tr>
                            <td><B><fmt:message key="beheer.organizationTelephone"/>:</B>&nbsp;<c:out value="${organizationTelephone}"/></td>
                        </tr>
                    </table>
                    
                    <div id="demoheader3">De volgende stap</div>
                    Kaartenbalie biedt u de mogelijheid om naast het kaartmateriaal dat standaard aangeboden wordt, zelf ook
                    kaartmateriaal toe te voegen aan de Kaartenbalie, om zo een beter idee te krijgen van de werking van de 
                    Kaartenbalie. Indien u geen kaart toe wilt voegen kunt u ook direct naar de viewer gaan door op de button
                    <b>Naar de viewer</b> te klikken. Anders kunt u met de button <b>Voeg kaart toe</b> zelf een WMS server toevoegen
                    met eigen kaartmateriaal.<br><br>

                    <input type="button" onclick="javascript:window.location.href='voegurltoe.do?userid=${id}'" value="<fmt:message key="button.addmap"/>">
                    <input type="button" onclick="javascript:window.location.href='<html:rewrite page='/demo/mapviewer.do' module='' />'" value="<fmt:message key="button.toviewer"/>" >
                    <br>
                </html:form>
            </c:when>
            <c:otherwise>
                <html:form action="/registration" onsubmit="return validateRegistrationForm(this)">
                    <div id='democontent'>
                    <div id="democontentheader">Registratie pagina</div>
                    <div id="democontenttext">
                    
                    
                    <div id="tooltipBox" 
                         onMouseOver="clearAdInterval();highlightAd('itxtTbl');" 
                         onMouseOut="hideAd();unHighlightAd('itxtTbl');" 
                         style="z-index:5000;position:absolute;cursor:pointer;">
                    </div>
                                        
                    Voordat u gebruik kunt maken van de Kaartenbalie Demo
                    geldt dat u zich eerst moet registreren. Zodra u geregistreerd bent als gebruiker
                    heeft u toegang tot het materiaal dat op kaartenbalie aangeboden wordt.
                    
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
                            <td><B><fmt:message key="beheer.name"/>:</B></td>
                            <td><html:text property="name"/></td>
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
            </c:otherwise>
        </c:choose>
    </c:when>
    <c:otherwise>
        <div id='democontent'>
            <div id="democontentheader">Pagina niet aanwezig</div>
            <div id="democontenttext">
                De pagina die u heeft opgevraagd is niet (meer) toegankelijk in het systeem. Neemt u contact op 
                met de beheerder indien u vragen heeft over deze pagina.
            </div>
        </div>
    </c:otherwise>
</c:choose>
<br>