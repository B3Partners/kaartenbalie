//FIREFOX STYLE TWEAK
function MM_openBrWindow(theURL,winName,features) {
    window.open(theURL,winName,features);
}

function window_onblur() {
    intWindow = window.setInterval("closeme()", 1000)
}

function window_onfocus() {
    self.focus();
}

function closeme() {
    window.clearInterval(intWindow)
    window.close();
}

function MM_openBrWindow(theURL,winName,features) {
    window.open(theURL, winName, features);
}

if(navigator.appName == "Netscape") {
    document.write("<style>.intellitextLink{padding-bottom: 1px;}</style>");
}

function changeStyle(objectID, propertyName, propertyValue) {
    document.getElementById(objectID).style[propertyName] = propertyValue;
}

function changeProperty(objectID, propertyName, propertyValue) {
    document.getElementById(objectID)[propertyName] = propertyValue;
}

function getStyleValue(objectID, propertyName) {
    return document.getElementById(objectID).style[propertyName];
}

function getPropertyValue(objectID, propertyName) {
    return document.getElementById(objectID)[propertyName];
}

// PROCEDURAL FUNCTIONS
var hideID = 0;
var lastToolNum = 0;
var tooltipXOffset = 10;
var tooltipYOffset = 1;

function displayStatus(string) {
    window.status = string;
    return true;
}

function clearStatus() {
    window.status = '';
    return true;
}

function getRealPos(ele,dir) {
    (dir=="x") ? pos = ele.offsetLeft : pos = ele.offsetTop;
    tempEle = ele.offsetParent;
    while(tempEle != null) {
        pos += (dir=="x") ? tempEle.offsetLeft : tempEle.offsetTop;
        tempEle = tempEle.offsetParent;
    }
    return pos;
}

function getScrollY() {
    if(window.pageYOffset != null) {
        return window.pageYOffset;
    } else {
        return document.body.scrollTop;
    }
}

function getScrollX() {
    if(window.pageXOffset != null) {
        return window.pageXOffset;
    } else {
        return document.body.scrollLeft;
    }
}

function adDelay() {
    //close box
    changeStyle('tooltipBox', 'visibility', 'hidden');
    
    //clear ID
    clearInterval(hideID);
    
    //clear status message
    displayStatus(' ');
}

function clearAdInterval() {
    clearInterval(hideID);
}

function hideAd() {
    clearInterval(hideID);
    hideID = setInterval(adDelay, 1500);
    
    //THIN DOUBLE UNDERLINE
    linkRefString = "link" + lastToolNum;
    changeStyle(linkRefString, 'borderBottomWidth', '1px');
}

function linkClick(indexNum) {
    window.open(urlArray[indexNum]);
}

function highlightAd(idString) {
    document.getElementById(idString).style.filter = "alpha(opacity=100)";
    document.getElementById(idString).style.mozOpacity = "1";
    document.getElementById(idString).style.opacity = "1";
    for(var x = 1; x < 7; x++) {
        var tempID = "cZn" + x;
        document.getElementById(tempID).style.background = tooltipHighlightColor;
    }
}

function unHighlightAd(idString) {
    document.getElementById(idString).style.filter = "alpha(opacity=75)";
    document.getElementById(idString).style.mozOpacity = ".95";
    document.getElementById(idString).style.opacity = ".95";
    for(var x = 1; x < 7; x++) {
        var tempID = "cZn" + x;
        document.getElementById(tempID).style.background = tooltipBkgColor;
    }
}

function displayAd(indexNum) {
    var linkRefString = "link" + indexNum;
    var linkRef =  document.getElementById(linkRefString);
    
    //clear id
    clearInterval(hideID);
    
    //update global link number variable
    lastToolNum = indexNum;
    
    //truncate text
    //if(bodyArray[lastToolNum].length > 300) bodyArray[lastToolNum] = bodyArray[lastToolNum].substring(0,100) + "...";
    //if(titleArray[lastToolNum].length > 60) titleArray[lastToolNum] = titleArray[lastToolNum].substring(0,60) + "...";
    //if(linkArray[lastToolNum].length > 35) linkArray[lastToolNum] = linkArray[lastToolNum].substring(0,35) + "...";
    
    //DISPLAY TIP TITLE IN STATUS BAR --updated to better accomidate escaped chars
    statusVar = titleArray[indexNum];
    displayStatus(statusVar);
    
    //COMPOSE TIP
    var displayString = '';
    displayString += '<table id="itxtTbl" name="itxtTbl" cellspacing="0" cellpadding="0" border="0" width="100%" style="width:100%;filter:alpha(opacity=95);moz-opacity:.95;opacity:.95;background:transparent;">';
    displayString +=    '<tr class="intellitxtTooltip">'
    displayString +=        '<td class="intellitxtTooltip">'
    displayString +=            '<b style="font-size:1px;display:block;height:1px;background:#000000; border-left:1px solid #000000; border-right:1px solid #000000;overflow: hidden;margin:0 6px;"></b>'
    displayString += 		'<b style="font-size:1px;display:block;height:1px;background:#E6E6E6; border-left:1px solid #000000; border-right:1px solid #000000;overflow: hidden;margin:0 4px; border-width:0 2px;"></b>'
    displayString += 		'<b style="font-size:1px;display:block;height:1px;background:#F4F4F4; border-left:1px solid #000000; border-right:1px solid #000000;overflow: hidden;margin:0 2px; border-width:0 2px;"></b>'
    displayString += 		'<b style="font-size:1px;display:block;height:1px;background:#E9E9E9; border-left:1px solid #000000; border-right:1px solid #000000;overflow: hidden;margin:0 2px;"></b>'
    displayString += 		'<b style="font-size:1px;display:block;height:1px;background:#E0E0E0; border-left:1px solid #000000; border-right:1px solid #000000;overflow: hidden;margin:0 1px;"></b>'
    displayString += 		'<b style="font-size:1px;display:block;height:1px;background:#DADADA; border-left:1px solid #000000; border-right:1px solid #000000;overflow: hidden;margin:0 1px;"></b>'
    displayString += 		'<b style="font-size:1px;display:block;height:1px;background:#D7D7D7; border-left:1px solid #000000; border-right:1px solid #000000;overflow: hidden;margin: 0 0px;"></b>'
    displayString += 		'<b style="font-size:1px;display:block;height:1px;background:#D4D4D4; border-left:1px solid #000000; border-right:1px solid #000000;overflow: hidden;margin: 0 0px;"></b>'
    displayString += 		'<b style="font-size:1px;display:block;height:1px;background:#D4D4D4; border-left:1px solid #000000; border-right:1px solid #000000;overflow: hidden;margin: 0 0px;"></b>'
    displayString += 		'<b style="font-size:1px;display:block;height:1px;background:#D5D5D5; border-left:1px solid #000000; border-right:1px solid #000000;overflow: hidden;margin: 0 0px;"></b>'
    displayString += 		'<b style="font-size:1px;display:block;height:1px;background:#D0D0D0; border-left:1px solid #000000; border-right:1px solid #000000;overflow: hidden;margin: 0 0px;"></b>'
    displayString += 		'<b style="font-size:1px;display:block;height:1px;background:#C5C5C5; border-left:1px solid #000000; border-right:1px solid #000000;overflow: hidden;margin: 0 0px;"></b>'
    displayString += 		'<b style="font-size:1px;display:block;height:1px;background:#C6C6C6; border-left:1px solid #000000; border-right:1px solid #000000;overflow: hidden;margin: 0 0px;"></b>'
    displayString += 		'<b style="font-size:1px;display:block;height:1px;background:#C7C7C6; border-left:1px solid #000000; border-right:1px solid #000000;overflow: hidden;margin: 0 0px;"></b>'
    displayString += 		'<b style="font-size:1px;display:block;height:1px;background:#CACAC6; border-left:1px solid #000000; border-right:1px solid #000000;overflow: hidden;margin: 0 0px;"></b>'
    displayString += 		'<b style="font-size:1px;display:block;height:1px;background:#CDCDC7; border-left:1px solid #000000; border-right:1px solid #000000;overflow: hidden;margin: 0 0px;"></b>'
    displayString += 		'<b style="font-size:1px;display:block;height:1px;background:#D0D0C7; border-left:1px solid #000000; border-right:1px solid #000000;overflow: hidden;margin: 0 0px;"></b>'
    displayString += 		'<b style="font-size:1px;display:block;height:1px;background:#D4D4C7; border-left:1px solid #000000; border-right:1px solid #000000;overflow: hidden;margin: 0 0px;"></b>'
    displayString += 		'<b style="font-size:1px;display:block;height:1px;background:#DBDBCB; border-left:1px solid #000000; border-right:1px solid #000000;overflow: hidden;margin: 0 0px;"></b>'
    displayString += 		'<b style="font-size:1px;display:block;height:1px;background:#CECEDB; border-left:1px solid #000000; border-right:1px solid #000000;overflow: hidden;margin: 0 0px;"></b>'
    displayString += 		'<b style="font-size:1px;display:block;height:1px;background:#ADADA8; border-left:1px solid #000000; border-right:1px solid #000000;overflow: hidden;margin: 0 0px;"></b>'
    displayString +=        '</td>'
    displayString += 	'</tr>'
    displayString += 	'<tr class="intellitxtTooltip">'
    displayString +=        '<td id="cZn1" name="cZn1" class="intellitxtTooltip" style="display:block; background:' + tooltipBkgColor + '; border-left:1px solid #000000; border-right:1px solid #000000;padding-left:0px;padding-right:0px;padding-top:0px;padding-bottom:0px;">'
    displayString +=            '<table cellpadding="0" cellspacing="0" border="0" style="position:relative;width:100%;">'
    displayString +=                '<tr>'
    displayString +=                    '<td class="intellitxtTooltip" style="padding-left:7px;padding-right:7px;padding-top:7px;padding-bottom:2px;vertical-align:top;text-align:left;">'
    displayString += 			'<a href="#" onMouseOver="this.style.textDecoration=\'' + tooltipTitleDecorationOn + '\';this.style.color=\'' + tooltipTitleColorOn + '\';window.status=statusVar;return true;" onMouseOut="this.style.textDecoration=\'' + tooltipTitleDecorationOff + '\';this.style.color=\'' + tooltipTitleColorOff + '\';" onclick="return false;" style="font-size:11px;display:block;font-family:' + tooltipFont + ';color:' + tooltipTitleColorOff + ';text-decoration:' + tooltipTitleDecorationOff + ';font-weight:bold;">' + titleArray[lastToolNum] + '</a>'
    displayString += 			'<span style="color:#000000;display:block;padding-top:7px;padding-bottom:7px;font-size:11px;display:block;font-weight:normal;font-family:' + tooltipFont + ';">' + bodyArray[lastToolNum] + '</span>'
    displayString += 			'<a href="#" onMouseOver="this.style.textDecoration=\'' + tooltipURLDecorationOn + '\';this.style.color=\'' + tooltipURLColorOn + '\';window.status=statusVar;return true;" onMouseOut="this.style.textDecoration=\'' + tooltipURLDecorationOff + '\';this.style.color=\'' + tooltipURLColorOff + '\';" onclick="return false;" onMouseOut="this.style.textDecoration=\'' + tooltipURLDecorationOff + '\';" style="font-size:10px;font-weight:normal;font-family:' + tooltipFont + ';color:' + tooltipURLColorOff + ';text-decoration:' + tooltipURLDecorationOff + ';">' + linkArray[lastToolNum] + '</a>'
    displayString += 			'</td>'
    displayString +=                '</tr>'
    displayString += 		'</table>'
    displayString +=        '</td>'
    displayString += 	'</tr>'
    displayString += 	'<tr class="intellitxtTooltip">'
    displayString +=        '<td class="intellitxtTooltip">'
    displayString +=            '<b id="cZn2" name="cZn2" style="font-size:1px;display:block;height:1px;background:' + tooltipBkgColor + '; border-left:1px solid #000000; border-right:1px solid #000000;overflow: hidden;margin:0 1px;"></b>'
    displayString += 		'<b id="cZn3" name="cZn3" style="font-size:1px;display:block;height:1px;background:' + tooltipBkgColor + '; border-left:1px solid #000000; border-right:1px solid #000000;overflow: hidden;margin:0 1px;"></b>'
    displayString += 		'<b id="cZn4" name="cZn4" style="font-size:1px;display:block;height:1px;background:' + tooltipBkgColor + '; border-left:1px solid #000000; border-right:1px solid #000000;overflow: hidden;margin:0 2px;"></b>'
    displayString += 		'<b id="cZn5" name="cZn5" style="font-size:1px;display:block;height:1px;background:' + tooltipBkgColor + '; border-left:1px solid #000000; border-right:1px solid #000000;overflow: hidden;margin:0 2px; border-width:0 2px;"></b>'
    displayString += 		'<b id="cZn6" name="cZn6" style="font-size:1px;display:block;height:1px;background:' + tooltipBkgColor + '; border-left:1px solid #000000; border-right:1px solid #000000;overflow: hidden;margin:0 4px; border-width:0 2px;"></b>'
    displayString += 		'<b class="c1" style="font-size:1px;display:block;height:1px;background:#000000; border-left:1px solid #000000; border-right:1px solid #000000;overflow: hidden;margin:0 6px;"></b>'
    displayString +=        '</td>'
    displayString += 	'</tr>'
    displayString += '</table>'
    displayString += '<div style="position:absolute;left:8px;top:5px;color:#666666;font-size:10px;font-family:' + tooltipFont + ';"><nobr>IntelliHelp</nobr></div>'
    displayString += '<div style="position:absolute;z-index:5002;right:8px;top:5px;"><img style="background:inherit;border:none;margin:0px;padding:0px;display:inline;vertical-align:top;" src="http://images.intellitxt.com/images/itxt_xButton.gif" height="13" onclick="adDelay();" border="0" alt="close"></div>';
    
    //RENDER TIP
    changeProperty('tooltipBox', 'innerHTML', displayString);
    
    //RESIZE TOOLTIP BOX
    var tempWidth = toolTipDefaultWidth + "px";
    changeStyle('tooltipBox', 'width', tempWidth);
    
    //POSITION TOOL TIP
    var toolTipBoxWidth = getPropertyValue('tooltipBox', 'offsetWidth')
    var toolTipBoxHeight = getPropertyValue('tooltipBox', 'offsetHeight');
    var linkPosX = getRealPos(linkRef,'x') + tooltipXOffset;
    var linkPosY = getRealPos(linkRef,'y') - toolTipBoxHeight + tooltipYOffset;
    
    //Account for page scrolling. Reposition tooltip as neccesary
    if((getScrollX() + document.body.clientWidth) < (linkPosX + toolTipBoxWidth)) {
        var tempOffset = (linkPosX + toolTipBoxWidth) - (getScrollX() + document.body.clientWidth);
        linkPosX -= tempOffset + 6;
    }
    
    if(getScrollY() > linkPosY) {
        var tempName = document.getElementById("link" + lastToolNum);
        var tempOffset = tempName.offsetHeight;
        linkPosY += toolTipBoxHeight - (2*tooltipYOffset) + tempOffset + 4;
    }
    
    //allow for manual positioning override
    linkPosX += xOffsetArray[lastToolNum];
    linkPosY += yOffsetArray[lastToolNum];
    
    //Make it happen
    var linkPosXString = linkPosX + "px";
    var linkPosYString = linkPosY + "px";
    changeStyle('tooltipBox', 'left', linkPosXString);
    changeStyle('tooltipBox', 'top', linkPosYString);
    
    //THICK DOUBLE UNDERLINE
    changeStyle(linkRefString, 'borderBottomWidth', '3px');
    
    //REVEAL TIP
    changeStyle('tooltipBox', 'visibility', 'visible');
}