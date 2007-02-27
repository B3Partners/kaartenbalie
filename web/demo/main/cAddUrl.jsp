<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>





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








<h1>Een WMS server toevoegen</h1>
Kaartenbalie heeft om het gebruik te kunnen demonstreren een aantal verschillende kaarten al in haar bestand
opgenomen. Echter met deze kaarten zal de kracht van kaartenbalie maar gedeeltelijk tot zijn recht komen en
om u er van te kunnen overtuigen dat kaartenbalie meer potentie heeft bieden wij u geheel gratis aan om ook een
eigen webserver toe te voegen aan het systeem zodat u duidelijk kunt zien dat kaartenbalie over de mogelijkheid
beschikt om kaarten van verschillende systemen die op verschillende locaties kunnen staan, kan samenvoegen tot
een geheel, zonder dat u hier als gebruiker veel moeite voor hoeft te doen of omslachtige handelingen uit dient
te voeren. Dat is precies de kracht van de kaartenbalie. <B>Eenvoudig edoch krachtig</B>.
<P>
    <%-- <html:javascript formName="voegurltoeForm" staticJavascript="false"/>--%>
    <html:form action="/voegurltoe"> <%-- onsubmit="return validateVoegurltoeForm(this)"> --%>
        <c:if test="${not empty message}">
            <div id="error">
                <h3><c:out value="${message}"/></h3>
            </div>
        </c:if>
        <table>
            <tr>
                <td><B>Naam server:</B></td>
                <td>
                    <html:text property="serviceProviderGivenName" />
                    <div id="tooltipBox" onMouseOver="clearAdInterval();highlightAd('itxtTbl');" onMouseOut="hideAd();unHighlightAd('itxtTbl');" style="z-index:5000;position:absolute;cursor:pointer;"></div>
                    <span id="link0" onMouseOver="displayAd(0);" onMouseOut="hideAd();" class="intellitextLink"><img src="<html:rewrite page='/images/siteImages/help.png' module='' />" width="15" height="15"></span>
                </td>
            </tr>
            <tr>
                <td><B>URL Server:</B></td>
                <td>
                    <html:text property="serviceProviderUrl" />
                    <div id="tooltipBox" onMouseOver="clearAdInterval();highlightAd('itxtTbl');" onMouseOut="hideAd();unHighlightAd('itxtTbl');" style="z-index:5000;position:absolute;cursor:pointer;"></div>
                    <span id="link1" onMouseOver="displayAd(1);" onMouseOut="hideAd();" class="intellitextLink"><img src="<html:rewrite page='/images/siteImages/help.png' module='' />" width="15" height="15"></span>
                </td>
            </tr>
            <html:hidden property="userid"></html:hidden>
            <tr>
                <td colspan="0">
                    <center>
                        <html:reset  value="Verwijder invoer" />
                        <html:submit value="Voeg WMS server toe" property="save"/>
                    </center>
                </td>
            </tr>
        </table>
    </html:form>
</P>
