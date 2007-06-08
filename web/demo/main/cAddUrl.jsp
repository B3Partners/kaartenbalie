<%@include file="/templates/taglibs.jsp" %>

<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/intellihelp.js' module='' />"></script>

<script language="JavaScript" type="text/javascript">
        var titleArray = new Array();
        titleArray[0] = "Naamgeving";
        titleArray[1] = "URL notitie";
        
        //TOOLTIP BODY TEXT
        var bodyArray = new Array();
        bodyArray[0] = "Een zelf gekozen WMS server kan het beste een naam gegeven worden die het best deze WMS server omschrijft. Natuurlijk mag hier iedere willekeurige naam opgegeven worden die u prettig vindt.";
        bodyArray[1] = "Let op dat Kaartenbalie op dit moment alleen geschikt is om met WMS versie 1.1.1. te communiceren. In de nabije toekomst zal dit hoogstwaarschijnlijk uitgebreid worden, maar op dit moment is het in het belang van de demostratie wel belangrijk dat u een WMS server toevoegd die versie 1.1.1. ondersteunt.";
        
        //TOOLTIP DISPLAY LINK
        var linkArray = new Array();
        linkArray[0] = "http://www.b3p.nl/";
        linkArray[1] = "http://www.b3p.nl/";
        
        //TOOLTIP URL
        var urlArray = new Array();
        urlArray[0] = "http://www.b3p.nl/";
        urlArray[1] = "http://www.b3p.nl/";
        
        //TOOLTIP OFFSET
        var xOffsetArray = new Array();
        xOffsetArray[0] = 0;
        xOffsetArray[1] = 0;
        
        var yOffsetArray = new Array();
        yOffsetArray[0] = 0;
        yOffsetArray[1] = 0;
        
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
    <div id='democontent'>
    <div id="democontentheader">Een WMS server toevoegen</div>
    <div id="democontenttext">
    <br>
    Kaartenbalie heeft om het gebruik te kunnen demonstreren een aantal verschillende kaarten al in haar bestand
    opgenomen. Om u te overtuigen van de mogelijkheden van Kaartenbalie, bieden wij u aan om ook een
    eigen mapserver toe te voegen aan het systeem zodat u duidelijk deze mogelijkheden kunt zien en testen.<br><br>
    <html:form action="/voegurltoe">
        <c:if test="${not empty message}">
            <div id="error">
                <h3><c:out value="${message}"/></h3>
            </div>
        </c:if>
        <table>
            <tr>
                <td><B><fmt:message key="demo.serverName"/>:</B></td>
                <td>
                    
                    <html:text property="givenName" />
                    
                </td>
            </tr>
            <tr>
                <td><B><fmt:message key="demo.serverURL"/>:</B></td>
                <td>
                    <div id="tooltipBox" onMouseOver="clearAdInterval();highlightAd('itxtTbl');" onMouseOut="hideAd();unHighlightAd('itxtTbl');" style="z-index:5000;position:absolute;cursor:pointer;"></div>
                    <html:text property="url" size="75" />
                    <span id="link1" onMouseOver="displayAd(1);" onMouseOut="hideAd();" class="intellitextLink"><img src="<html:rewrite page='/images/siteImages/help.png' module='' />" width="15" height="15"></span>
                </td>
            </tr>
            <html:hidden property="userid"></html:hidden>
            <TR>
                <TD>&nbsp;</TD>
                <TD>&nbsp;</TD>
            </TR>
            <tr>
                <td colspan="0">
                    <html:reset>
                        <fmt:message key="button.reset"/>
                    </html:reset>
                    <html:submit property="save" >
                        <fmt:message key="button.ok"/>
                    </html:submit> 
                </td>
            </tr>
        </table>
    </html:form>
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
