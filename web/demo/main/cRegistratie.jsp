<%@include file="/templates/taglibs.jsp" %>

<html:javascript formName="registrationForm" staticJavascript="false"/>

<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/intellihelp.js' module='' />"></script>

<script language="JavaScript" type="text/javascript">
        var titleArray = new Array();
        titleArray[0] = "Veiligheid";
        
        
        //TOOLTIP BODY TEXT
        var bodyArray = new Array();
        bodyArray[0] = "Kaartenbalie is opgezet met veiligheid in het achterhoofd. Het kaartmateriaal dat via kaartenablie aangeboden wordt en getoont wordt is in een aantal gevallen auteursrechtelijk beschermd. Om te voorkomen dat iedereen deze informatie al naar gelang op kan vragen is kaartenbalie afgeschermd en zijn er voor gebruikers rechten in te stellen die bepalen wat zij wel of niet mogen zien.";
        
        
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
                <div id="tooltipBox" 
                             onMouseOver="clearAdInterval();highlightAd('itxtTbl');" 
                             onMouseOut="hideAd();unHighlightAd('itxtTbl');" 
                             style="z-index:5000;position:absolute;cursor:pointer;">
                        </div>
                <h1>Registratie pagina</h1>
                Uw registratie is gelukt. Hieronder vindt u een overzicht van uw gegevens zoals u deze tijdens de
                registratiestap heeft opgegeven. Daarnaast vindt u hier gelijk uw persoonlijke URL.
                <span id="link0" 
                              onMouseOver="displayAd(0);" 
                              onMouseOut="hideAd();" 
                              class="intellitextLink">
                            <img src="<html:rewrite page='/images/siteImages/help.png' module='' />" width="12" height="12">
                        </span>
                <h2>Uw gegegevens</h2>
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
                <input type="button" onclick="javascript:window.location.href='voegurltoe.do?userid=${id}'" value="<fmt:message key="button.next"/>">
                </html:form>
            </c:when>
            <c:otherwise>
                <html:form action="/registration" onsubmit="return validateRegistrationForm(this)">
                    <h1>Registratie pagina</h1>
                    <p>
                        <div id="tooltipBox" 
                             onMouseOver="clearAdInterval();highlightAd('itxtTbl');" 
                             onMouseOut="hideAd();unHighlightAd('itxtTbl');" 
                             style="z-index:5000;position:absolute;cursor:pointer;">
                        </div>
                        <h2>Veiligheid</h2>
                        Voordat u gebruik kunt maken van de Kaartenbalie Demo (maar ook bij de officiele versie van Kaartenbalie)
                        geldt dat u zich eerst moet registreren of laten registreren. Zodra u geregistreerd bent als gebruiker
                        heeft u toegang tot het materiaal dat op kaartenbalie aangeboden wordt.<BR>
                        Het materiaal dat voor beschikbaar is, wordt door de beheerder vastgesteld. Dat wil zeggen dat kaartenbalie
                        u alleen toegang verschaft tot het kaartmateriaal dat voor u of meerdere mensen binnen uw bedrijf of organisatie
                        beschikbaar gesteld is. Op deze manier biedt kaartenbalie u en andere gebruikers de bescherming van het materiaal
                        dat aangeboden wordt.
                        <span id="link0" 
                              onMouseOver="displayAd(0);" 
                              onMouseOut="hideAd();" 
                              class="intellitextLink">
                            <img src="<html:rewrite page='/images/siteImages/help.png' module='' />" width="12" height="12">
                        </span>
                    </p>
                    
                    <p>
                        Op deze manier blijft het materiaal van u beschermd voor ongenode gasten. Tevens geldt dat u ook geen toegang
                        heeft tot kaartmateriaal van anderen. Met andere woorden: kaartenbalie garandeerd de veiligheid van de gegevens
                        door deze gegevens persoonlijk te maken en persoonlijk te houden.
                        Indien er kaarten zijn die wel voor een groter publiek toegankelijk moeten zijn, dan kan dit met behulp van 
                        rechten opgelost worden.
                    </p>
                    
                    <p>
                        <h2>Uw gegegevens</h2>
                        <c:if test="${not empty message}">
                            <div id="error">
                                <h3><c:out value="${message}"/></h3>
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
                    </p>
                </html:form>
            </c:otherwise>
        </c:choose>
    </c:when>
    <c:otherwise>
        <h1>Pagina niet aanwezig</h1>
        De pagina die u heeft opgevraagd is niet (meer) toegankelijk in het systeem. Neemt u contact op met de beheerder
        indien u vragen heeft over deze pagina.
    </c:otherwise>
</c:choose>