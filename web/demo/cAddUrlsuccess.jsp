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


<h1>Uw WMS server is met succes toegevoegd</h1>
Kaartenbalie heeft met succes uw link toegevoegd aan het systeem en de verschillende data die op deze
WMS service aangeboden wordt op genomen in haar eigen database. Door middel van de interne (of externe)
viewer kunt u nu gebruik maken van de kaartenbalie om zo de gegegevens weer op te vragen.

<P>
    <input type="button" value="jiiij" onclick="javascript:window.location.href='<html:rewrite page='/viewer/mapviewer.do' module='' />'">
</P>