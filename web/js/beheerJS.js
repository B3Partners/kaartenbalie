// BEGIN INFOLABEL CODE - voor het tonen van de informatielabel over de regel //

var timeout = null;
function showLabel(objId) {
    var lbl = document.getElementById('infoLabel' + objId);
    lbl.style.left = xMousePos + 'px';
    lbl.style.top = yMousePos + 'px';
    timeout = setTimeout("makeLabelVisible('" + objId  +"')", 200);
}
function makeLabelVisible(objId) {
    document.getElementById('infoLabel' + objId).style.display = 'block';
}
function hideLabel(objId) {
    window.clearTimeout(timeout)
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
        xMousePos = window.event.x+document.body.scrollLeft;
        yMousePos = window.event.y+document.body.scrollTop;
    } else if (document.getElementById) {
        xMousePos = e.pageX;
        yMousePos = e.pageY;
    }
}

// EIND INFOLABEL CODE //

// BEGIN SORTTABLE CODE - voor het sorteren en om en om kleuren van de regels //

function sortTable(obj) {
    var childs = document.getElementById('topRij').childNodes;
    for(i = 0; i < childs.length; i++) {
        if(childs[i].tagName == 'TD' && childs[i] != obj) {
            childs[i].className = "serverRijTitel table-sortable";
        }
    }
    if(obj.className == "serverRijTitel table-sortable")
        obj.className = "serverRijTitel table-sorted-desc";
    else if(obj.className == "serverRijTitel table-sorted-asc")
        obj.className = "serverRijTitel table-sorted-desc";
    else if(obj.className == "serverRijTitel table-sorted-desc")
        obj.className = "serverRijTitel table-sorted-asc";
}

// EINDE SORTTABLE CODE //