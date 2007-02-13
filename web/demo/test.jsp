<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/intellihelp.js' module='' />"></script>

<body marginheight="0" marginwidth="0" leftmargin="0" topmargin="0" bgcolor="#FFFFFF">
    <script language="JavaScript" type="text/javascript">
        var titleArray = new Array();
        titleArray[0] = "Das ist ein \"Tooltip\"";
        titleArray[1] = "title goes here";
        titleArray[2] = "More Titles";
        titleArray[3] = "title goes here";

        //TOOLTIP BODY TEXT
        var bodyArray = new Array();
        bodyArray[0] = "nope";
        bodyArray[1] = "Hieronder staat de viewer met veel veel functies";
        bodyArray[2] = "Bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla";
        bodyArray[3] = "Bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla Bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla";
        
        //TOOLTIP DISPLAY LINK
        var linkArray = new Array();
        linkArray[0] = "www.vibrantmedia.com";
        linkArray[1] = "www.urlGoesHere.com";
        linkArray[2] = "www.vibrantmedia.com";
        linkArray[3] = "www.urlGoesHere.com";

        //TOOLTIP URL
        var urlArray = new Array();
        urlArray[0] = "http://www.landingPageURLGoesHere.com";
        urlArray[1] = "http://www.landingPageURLGoesHere.com";
        urlArray[2] = "http://www.landingPageURLGoesHere.com";
        urlArray[3] = "http://www.landingPageURLGoesHere.com";
        
        //TOOLTIP OFFSET
        var xOffsetArray = new Array();
        xOffsetArray[0] = 0;
        xOffsetArray[1] = 0;
        xOffsetArray[2] = 0;
        xOffsetArray[3] = 0;

        var yOffsetArray = new Array();
        yOffsetArray[0] = 0;
        yOffsetArray[1] = 0;
        yOffsetArray[2] = 0;
        yOffsetArray[3] = 0;

        //TOOLTIP BOX DEFAULT WIDTH
        var toolTipDefaultWidth = 250;

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
    
    <div id="tooltipBox" onMouseOver="clearAdInterval();highlightAd('itxtTbl');" onMouseOut="hideAd();unHighlightAd('itxtTbl');" style="z-index:5000;position:absolute;cursor:pointer;"></div>
    <span id="link0" onMouseOver="displayAd(0);" onMouseOut="hideAd();" class="intellitextLink">Tooltip</span>
    
    <div id="tooltipBox" onMouseOver="clearAdInterval();highlightAd('itxtTbl');" onMouseOut="hideAd();unHighlightAd('itxtTbl');" style="z-index:5000;position:absolute;cursor:pointer;"></div>
    <span id="link1" onMouseOver="displayAd(1);" onMouseOut="hideAd();" class="intellitextLink">Tooltip</span>
    
    <div id="tooltipBox" onMouseOver="clearAdInterval();highlightAd('itxtTbl');" onMouseOut="hideAd();unHighlightAd('itxtTbl');" style="z-index:5000;position:absolute;cursor:pointer;"></div>
    <span id="link2" onMouseOver="displayAd(2);" onMouseOut="hideAd();" class="intellitextLink">Tooltip</span>
    
    <div id="tooltipBox" onMouseOver="clearAdInterval();highlightAd('itxtTbl');" onMouseOut="hideAd();unHighlightAd('itxtTbl');" style="z-index:5000;position:absolute;cursor:pointer;"></div>
    <span id="link3" onMouseOver="displayAd(3);" onMouseOut="hideAd();" class="intellitextLink">Tooltip</span>
    
</body>