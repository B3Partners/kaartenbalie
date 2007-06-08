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
        bodyArray[2] = "Een persoonlijke URL is een URL die u kunt gebruiken om via een viewer, anders dan de viewer van Kaartenbalie, verbinding te maken met de Kaartenbalie en zo uw kaartmateriaal op te vragen en te gebruiken.";
        
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
        <div id='democontent'>
            <div id="democontentheader">Welkom bij de demoversie van de Kaartenbalie</div>
            <div id="democontenttext">
            
            Welkom bij de demoversie van de Kaartenbalie. Door middel van deze Demo willen wij u laten zien
            wat de mogelijkheden van de Kaartenbalie zijn. Met behulp van een aantal pagina's zal worden uitgelegd
            hoe u gebruik kunt maken van de Kaartenbalie.
        
        
            <div id="tooltipBox" onMouseOver="clearAdInterval();highlightAd('itxtTbl');" 
                 onMouseOut="hideAd();unHighlightAd('itxtTbl');" 
                 style="z-index:5000;position:absolute;cursor:pointer;"></div>
            <div id="demoheader3">Wat is kaartenbalie</div>
            Kaartenbalie is een webapplicatie, aangeboden door B3Partners, waarmee u de mogelijkheid heeft 
            om kaartmateriaal dat op verschillende locaties (verschillende webservers) wordt aangeboden, op een
            eenvoudige en snelle manier te combineren <b>&eacute;n te beveiligen tegen ongewenst gebruik</b>. 
            Dit kaartmateriaal dient daartoe wel aangeboden te worden als WMS 1.1.1 service.
        
            <div id="demoheader3">Hoe gaat het in zijn werk?</div>
            Als u een nieuwe gebruiker bent van deze demo dan kunt u via de registratie knop links 
            in het menu op eenvoudige wijze uzelf registreren om zo gebruik
            te kunnen maken van Kaartenbalie.
            Standaard krijgt u een kaart te zien zoals die door B3Partners wordt aangeboden. Om de kracht van Kaartenbalie
            aan te tonen kunt u daarnaast ook eigen kaartmateriaal toevoegen
            <b>zonder</b> dat anderen van ditzelfde materiaal gebruik kunnen maken.
       
            <div id="demoheader3">Samenvatting</div>
            Als u nieuw bent kunt u met de volgende stappen snel en eenvoudig gebruik maken van deze demo.
            <ul>
                <li>Stap 1: U dient zich te registreren als gebruiker</li>
                <li>Stap 2: U kunt, indien gewenst eigen kaartmateriaal toevoegen aan Kaartenbalie</li>
                <li>Stap 3: U krijgt een samenvatting van uw gegevens</li>
                <li>Stap 4: U kunt werken met de demoviewer</li>
            </ul>
            <br>
      
        </div>
        </div>
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

