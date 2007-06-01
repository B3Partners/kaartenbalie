<%@include file="/templates/taglibs.jsp" %>

<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/intellihelp.js' module='' />"></script>

<script language="JavaScript" type="text/javascript">
    
        var titleArray = new Array();
        titleArray[0] = "Eisen";
        titleArray[1] = "Gegevens";
        titleArray[2] = "Persoonlijke URL";

        //TOOLTIP BODY TEXT
        var bodyArray = new Array();
        bodyArray[0] = "Een nieuw toe te voegen server dient alleen aan de volgende eisen te voldoen: <ul><li>Het is een WMS server en</li><li>Het is WMS versie 1.1.1</li></ul>";
        bodyArray[1] = "De gegevens die van u verlangt worden zijn: <ul><li>een naam</li><li>een gebruikersnaam</li><li>een wachtwoord</li><li>een email adres</li><li>een naam van de organisatie waar u werkt</li></ul>";
        bodyArray[2] = "Een persoonlijke URL is een URL die u kunt gebruiken om via een viewer, anders dan de viewer van Kaartenbalie, verbinding te maken met de Kaartenbalie en zo uw kaartmateriaal te gebruiken.";
        
        //TOOLTIP DISPLAY LINK
        var linkArray = new Array();
        linkArray[0] = "http://www.b3p.nl/";
        linkArray[1] = "http://www.b3p.nl/";
        linkArray[2] = "http://www.b3p.nl/";

        //TOOLTIP URL
        var urlArray = new Array();
        urlArray[0] = "http://www.b3p.nl/";
        urlArray[1] = "http://www.b3p.nl/";
        urlArray[2] = "http://www.b3p.nl/";
        
        //TOOLTIP OFFSET
        var xOffsetArray = new Array();
        xOffsetArray[0] = 10;
        xOffsetArray[1] = 10;
        xOffsetArray[2] = 10;

        var yOffsetArray = new Array();
        yOffsetArray[0] = 85;
        yOffsetArray[1] = 15;
        yOffsetArray[2] = 15;

        //TOOLTIP BOX DEFAULT WIDTH
        var toolTipDefaultWidth = 230;

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
        
        <H1>Welkom bij de demoversie van de Kaartenbalie</H1>
        
        <p align="justify">
            Welkom bij de demoversie van de Kaartenbalie. Door middel van deze Demo willen wij u laten zien
            wat de mogelijkheden van de Kaartenbalie zijn. Met behulp van een aantal pagina's zal worden uitgelegd
            hoe u gebruik kunt maken van de Kaartenbalie.
        </p>
        <p align="justify">
            <div id="tooltipBox" onMouseOver="clearAdInterval();highlightAd('itxtTbl');" 
                 onMouseOut="hideAd();unHighlightAd('itxtTbl');" 
                 style="z-index:5000;position:absolute;cursor:pointer;"></div>
            <h2>Wat is kaartenbalie</h2>
            Kaartenbalie is een webapplicatie, aangeboden door B3Partners, waarmee u de mogelijkheid heeft 
            om kaartmateriaal dat op verschillende locaties (verschillende webservers) wordt aangeboden, op een
            eenvoudige en snelle manier te combineren <b>&eacute;n te beveiligen tegen ongewenst gebruik</b>. 
            Dit kaartmateriaal dient aan een paar eisen
            <span id="link0" onMouseOver="displayAd(0);" onMouseOut="hideAd();" class="intellitextLink">
                <img src="<html:rewrite page='/images/siteImages/help.png' module='' />" width="12" height="12">
            </span>
            te voldoen en kaartenbalie is eenvoudig in gebruik 
            om dit materiaal toe te voegen.
        </p>
        <p align="justify">
            <h2>Hoe gaat het in zijn werk?</h2>
            Via het menu kunt u een keuze maken uit de verschillende mogelijkheden die er zijn om een beeld te krijgen
            van de kaartenbalie. Als u een nieuwe gebruiker bent van deze demo dan kunt u via de registratie knop links 
            in het menu op eenvoudige wijze en met een paar simpele stappen uzelf laten registreren.<br>
            Bij deze registratie zult u ook de mogelijkheid krijgen &eacute;&eacute;n <b>eigen</b> server toe te voegen
            aan Kaartenbalie om aan te tonen hoe eenvoudig nieuw materiaal aan de Kaartenbalie toegevoegd kan worden,
            <b>zonder</b> dat anderen van ditzelfde materiaal gebruik kunnen maken.
        </p>
        <p  align="justify">
            <h2>De registratie</h2>
            Om van de demo gebruik te kunnen maken en om het kaartmateriaal te beveiligen tegen ongewenst
            gebruik is een registratie vereist om het materiaal aan uw gegevens te kunnen koppelen. Daarom
            zal er tijdens de registratie wat informatie van u gevraagd worden. 
            <span id="link1" onMouseOver="displayAd(1);" onMouseOut="hideAd();" class="intellitextLink">
                <img src="<html:rewrite page='/images/siteImages/help.png' module='' />" width="12" height="12">
            </span>
        </p>
        <p  align="justify">
            <h2>Na de registratie</h2>
            Na de registratie kunt u gebruik maken van de verschillende mogelijkheden die Kaartenbalie biedt.
            In het menu hier links, kunt u via het item Mapviewer gebruik maken van een interne kaartviewer en
            met behulp van het item Profiel kunt u uw persoonlijke gegevens opgraven evenals een persoonlijke
            URL die tijdens de registratie voor u is aangemaakt.
            <span id="link2" onMouseOver="displayAd(2);" onMouseOut="hideAd();" class="intellitextLink">
                <img src="<html:rewrite page='/images/siteImages/help.png' module='' />" width="12" height="12">
            </span>
        </p>
        <p align="justify">
            <h2>Samenvatting</h2>
            Als u nieuw bent kunt u met de volgende stappen snel en eenvoudig gebruik maken van deze demo.
            <ul>
                <li>Stap 1: U dient zich te registreren als gebruiker</li>
                <li>Stap 2: U kunt, indien gewenst eigen kaartmateriaal toevoegen aan Kaartenbalie</li>
                <li>Stap 3: U krijgt een samenvatting van uw gegevens</li>
                <li>Stap 4: U kunt werken met de demoviewer</li>
            </ul>
        </p>
    </c:when>
    <c:otherwise>
        <h1>Pagina niet aanwezig</h1>
        De pagina die u heeft opgevraagd is niet (meer) toegankelijk in het systeem. Neemt u contact op met de beheerder
        indien u vragen heeft over deze pagina.
    </c:otherwise>
</c:choose>

