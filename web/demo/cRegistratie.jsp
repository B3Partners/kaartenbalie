<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
    <c:when test="${not empty id}">
        
        <h1>Registratie pagina</h1>
        Voordat u gebruik kunt maken van de Kaartenbalie Demo (maar ook bij de officiele versie van Kaartenbalie)
        geldt dat u zich eerst moet registreren of laten registreren. De reden dat u dit dient te doen is omdat u
        op die manier een inlognaam en wachtwoord aangeleverd krijgt waarmee alleen u toegang heeft tot het materiaal
        dat u op kaartenbalie aangeboden heeft.
        Op deze manier blijft het materiaal van u beschermd voor ongenode gasten. Tevens geldt dat u ook geen toegang
        heeft tot kaartmateriaal van anderen. Met andere woorden: kaartenbalie garandeerd de veiligheid van de gegevens
        door deze gegevens persoonlijk te maken en persoonlijk te houden.
        Indien er kaarten zijn die wel voor een groter publiek toegankelijk moeten zijn, dan kan dit met behulp van 
        rechten opgelost worden.
        
        <h2>Uw gegegevens</h2>
        
        <table>
            <tr>
                <td><B>Naam:</B>&nbsp;<c:out value="${firstname}"/>&nbsp;<c:out value="${lastname}"/></td>
            </tr>
            <tr>
                <td><B>Email:</B>&nbsp;<c:out value="${emailAddress}"/></td>
            </tr>
            <tr>
                <td><B>Opgegeven gebruikersnaam:</B>&nbsp;<c:out value="${username}"/></td>
            </tr>
            <tr>
                <td><B>Persoonlijke URL:</B>&nbsp;<c:out value="${personalURL}"/></td>
            </tr>
            
            
            <tr>
                <td><B>Naam organisatie:</B>&nbsp;<c:out value="${name}"/></td>
            </tr>
            <tr>
                <td><B>Straat:</B>&nbsp;<c:out value="${organizationStreet}"/>&nbsp;
                <c:out value="${organizationNumber}"/>&nbsp;<c:out value="${organizationAddition}"/></td>
            </tr>
            
            <tr>
                <td><B>Postcode:</B>&nbsp;<c:out value="${organizationPostalcode}"/>
                <B>Plaats:</B>&nbsp;<c:out value="${organizationProvince}"/></td>
            </tr>
            
            
            <tr>
                <td><B>Telefoon nr.:</B>&nbsp;<c:out value="${organizationTelephone}"/></td>
            </tr>    
            
            
        </table>
        <input type="button" value="Verder" onclick="javascript:window.location.href='<html:rewrite page='/demo/voegurltoe.do?userid=${id}' module='' />'">
<%--        <html:form action="/voegurltoe">
            <html:hidden property="userid" value="${id}"></html:hidden>
            <html:submit value="Volgende"/>
        </html:form> --%>       
    </c:when>
    <c:otherwise>
        <html:form action="/registration" onsubmit="return validateRegistrationForm(this)">
            <h1>Registratie pagina</h1>
            Voordat u gebruik kunt maken van de Kaartenbalie Demo (maar ook bij de officiele versie van Kaartenbalie)
            geldt dat u zich eerst moet registreren of laten registreren. De reden dat u dit dient te doen is omdat u
            op die manier een inlognaam en wachtwoord heeft waarmee alleen u toegang heeft tot het materiaal
            dat u op kaartenbalie aangeboden heeft.
            <div id="tooltipBox" onMouseOver="clearAdInterval();highlightAd('itxtTbl');" onMouseOut="hideAd();unHighlightAd('itxtTbl');" style="z-index:5000;position:absolute;cursor:pointer;"></div>
            <span id="link0" onMouseOver="displayAd(0);" onMouseOut="hideAd();" class="intellitextLink"><img src="<html:rewrite page='/images/siteImages/help.png' module='' />" width="10" height="10"></span>
            <P>
            Op deze manier blijft het materiaal van u beschermd voor ongenode gasten. Tevens geldt dat u ook geen toegang
            heeft tot kaartmateriaal van anderen. Met andere woorden: kaartenbalie garandeerd de veiligheid van de gegevens
            door deze gegevens persoonlijk te maken en persoonlijk te houden.
            Indien er kaarten zijn die wel voor een groter publiek toegankelijk moeten zijn, dan kan dit met behulp van 
            rechten opgelost worden.
            </P>
            
            <P>
            <h2>Uw gegegevens</h2>
            <table>
                <tr>
                    <td><B>Voornaam:</B></td>
                    <td><html:text property="firstname"/></td>
                    <td><B>Achternaam:</B></td>
                    <td><html:text property="lastname"/></td>
                </tr>
                <tr>
                    <td><B>Email:</B></td>
                    <td><html:text property="emailAddress"/></td>
                </tr>
                <tr>
                    <td><B>Gewenste gebruikersnaam:</B></td>
                    <td><html:text property="username"/></td>
                </tr>
                <tr>
                    <td><B>Gewenst wachtwoord:</B></td>
                    <td><html:text property="password"/></td>
                </tr>
                
                <tr>
                    <td><B>Naam organisatie:</B></td>
                    <td><html:text property="name"/></td>
                </tr>
                <tr>
                    <td><B>Straat:</B></td>
                    <td><html:text property="organizationStreet"/></td>
                    <td><B>Nummer:</B></td>
                    <td><html:text property="organizationNumber"/></td>
                    <td><B>Toevoeging:</B></td>
                    <td><html:text property="organizationAddition"/></td>
                </tr>
                
                <tr>
                    <td><B>Postcode:</B></td>
                    <td><html:text property="organizationPostalcode"/></td>
                    <td><B>Plaats:</B></td>
                    <td><html:text property="organizationProvince"/></td>
                    <td><B>Land:</B></td>
                    <td><html:text property="organizationCountry"/></td>
                </tr>
                
                <tr>
                    <td><B>Postbus:</B></td>
                    <td><html:text property="organizationPostbox"/></td>
                </tr>
                
                <tr>
                    <td><B>Telefoon nr.:</B></td>
                    <td><html:text property="organizationTelephone"/></td>
                </tr>
                <tr>
                    <td><B>Fax nr.:</B></td>
                    <td><html:text property="organizationFax"/></td>
                </tr>     
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td colspan="0"><center>
                        <html:submit value="Registreer" property="save"/>    
                        <html:reset  value="Verwijder invoer" />
                    </center></td>
                </tr>
            </table>
            </P>
        </html:form>
    </c:otherwise>
</c:choose>




