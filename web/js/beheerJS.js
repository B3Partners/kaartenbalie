/*
 * B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
 * for authentication/authorization, pricing and usage reporting.
 *
 * Copyright 2006, 2007, 2008 B3Partners BV
 * 
 * This file is part of B3P Kaartenbalie.
 * 
 * B3P Kaartenbalie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * B3P Kaartenbalie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
 */

// BEGIN INFOLABEL CODE - voor het tonen van de informatielabel over de regel //

var timeout = null;
function showLabel(objId) {
    var lbl = document.getElementById('infoLabel' + objId);
    if (lbl) {
        lbl.style.left = xMousePos + 'px';
        lbl.style.top = yMousePos + 'px';
        timeout = setTimeout("makeLabelVisible('" + objId + "')", 200);
    }
}
function makeLabelVisible(objId) {
    if (document.getElementById('infoLabel' + objId))
        document.getElementById('infoLabel' + objId).style.display = 'block';
}
function hideLabel(objId) {
    window.clearTimeout(timeout)
    if (document.getElementById('infoLabel' + objId))
        document.getElementById('infoLabel' + objId).style.display = 'none';
}

xMousePos = 0;
yMousePos = 0;
if (document.layers) {
    document.captureEvents(Event.MOUSEMOVE);
    document.onmousemove = captureMousePosition;
} else if (document.all) {
    document.onmousemove = captureMousePosition;
} else if (document.getElementById) {
    document.onmousemove = captureMousePosition;
}
function captureMousePosition(e) {
    if (document.layers) {
        xMousePos = e.pageX;
        yMousePos = e.pageY;
    } else if (document.all) {
        xMousePos = window.event.x + document.body.scrollLeft;
        yMousePos = window.event.y + document.body.scrollTop;
    } else if (document.getElementById) {
        xMousePos = e.pageX;
        yMousePos = e.pageY;
    }
}

// EIND INFOLABEL CODE //

// BEGIN SORTTABLE CODE - voor het sorteren en om en om kleuren van de regels //

function sortTable(obj) {
    var childs = document.getElementById('topRij').childNodes;
    for (i = 0; i < childs.length; i++) {
        if (childs[i].tagName == 'TD' && childs[i] != obj) {
            childs[i].className = "serverRijTitel table-sortable";
        }
    }
    if (obj.className == "serverRijTitel table-sortable")
        obj.className = "serverRijTitel table-sorted-desc";
    else if (obj.className == "serverRijTitel table-sorted-asc")
        obj.className = "serverRijTitel table-sorted-desc";
    else if (obj.className == "serverRijTitel table-sorted-desc")
        obj.className = "serverRijTitel table-sorted-asc";
}

// EINDE SORTTABLE CODE //

// Used for sorting
var linkExtract = function(node) {
    var found = false;
    for (x in node.childNodes) {
        var tmpNode = node.childNodes[x];
        if (tmpNode.tagName && tmpNode.tagName.toLowerCase() == 'div') {
            if (tmpNode.hasChildNodes())
            {
                for (y in tmpNode.childNodes) {
                    var tmpNode2 = tmpNode.childNodes[y];
                    if (tmpNode2.tagName && tmpNode2.tagName.toLowerCase() == 'a') {
                        return tmpNode2.innerHTML;
                    }
                }
            }
        }
    }
    return node.innerHTML;
}

function displayGroupDiv() {
    if ($('#updateRights').is(":checked")) {
        $('#updateRightsDiv').show();
    } else {
        $('#updateRightsDiv').hide();
    }
}