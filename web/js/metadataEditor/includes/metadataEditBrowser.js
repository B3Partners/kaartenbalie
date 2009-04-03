/*
B3P Metadata Editor is a ISO 19139 compliant metadata editor, 
that is preconfigured to use the Dutch profile for geography

Copyright 2006, 2007, 2008 B3Partners BV

This file is part of B3P Metadata Editor.

B3P Kaartenbalie is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

B3P Kaartenbalie is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
*/
//Auteur: Erik van de Pol. B3Partners.

//deze functie wordt in de onload attribuut van de body tag aangeroepen
//selecteert tab die bij starten zichtbaar is.
//als javascript disabled is, wordt alle inhoud van alle tabs onder elkaar weergegeven
function xmlDocInit() {
  var visibleTab = document.getElementById("overzicht-tab");
  setInitialTab(visibleTab);
}

var currentTab;
//selecteert de aangeklikte tab
function changeTab(eTD)  {
  var eTD = getWindowEvent(eTD);            
  var tabs = eTD.parentNode.childNodes;
  for (var i = 0; i < tabs.length; i++) {
    var oldTab = tabs[i];
    if (oldTab.nodeType == 1 && oldTab.className == "tab-selected") {
      break;
    }
  }
  oldTab.className = "tab-unselected";
  var oldContent = getAssociated(oldTab);
  if(oldContent != null) oldContent.style.display = "none";

  var newTab = eTD;
  currentTab = eTD.id;
  newTab.className = "tab-selected";
  var newContent = getAssociated(newTab);
  if(newContent != null) newContent.style.display = "block";

  stopPropagation(eTD);
}

function changeToCurrentTab() {
	var eRow = document.getElementById("main-tab-menu");
	var tabs = eRow.childNodes;
	for (var i = 0; i < tabs.length; i++) {
	    var oldTab = tabs[i];
	    if (oldTab.nodeType == 1) {
	      var tabContent = getAssociated(oldTab);
	      if(tabContent != null) tabContent.style.display = "none";
		  oldTab.className = "tab-unselected";
	    }
	}
	var currentTabEl = document.getElementById(currentTab);
	currentTabEl.className = "tab-selected";
	var newContent = getAssociated(currentTabEl);
	if(newContent != null) newContent.style.display = "block";
}

//selecteert tab die bij starten zichtbaar is.
function setInitialTab(tab)  {
  var eRow = document.getElementById("main-tab-menu");
  if (eRow == undefined || eRow == null) {
  	return;
  }
  var tabs = eRow.childNodes;
  // var tabs = tabs[0].childNodes;
  // var tabs = tabs[0].childNodes;  
  for (var i = 0; i < tabs.length; i++) {
    var oldTab = tabs[i];
    if (oldTab.nodeType == 1) {
      var tabContent = getAssociated(oldTab);
      if(tabContent != null) tabContent.style.display = "none";
    }
  }

  currentTab = tab.id;
  tab.className = "tab-selected";
  var newContent = getAssociated(tab);
  if(newContent != null) newContent.style.display = "block";
}

//returnt het element dat de inhoud van de tab bevat
function getAssociated(eTab) {
  var associated;
  switch (eTab.id) {
    case "overzicht-tab":
      associated = document.getElementById("overzicht");
      break;
    case "attributen-tab":
      associated = document.getElementById("attributen");
      break;
    case "specificaties-tab":
      associated = document.getElementById("specificaties");
      break;
    default:
      associated = null;
      break;
  }
  return associated;
}

//verandert css-klasse als over een tab-knop gehoverd wordt
function tabHover(obj) {
  if(obj.className != 'tab-selected') {
    obj.className = 'tab-hover';
    obj.onmouseout = function() { if(obj.className != 'tab-selected') obj.className = 'tab-unselected'; }
  }
}

//verandert css-klasse als over een section-title gehoverd wordt
function sectionTitleHover(obj) {
  if (obj.className != 'section-title-hover') {
    obj.className = 'section-title-hover';
    obj.onmouseout = function() { obj.className = 'section-title'; }
  }
}

//verandert css-klasse als over een menu image gehoverd wordt
function menuImgHover(obj) {
  if (obj.className == 'menu-img-element') {
    obj.className = 'menu-img-element-hover';
    obj.onmouseout = function() { obj.className = 'menu-img-element'; }
  }
  else if (obj.className == 'menu-img-section') {
    obj.className = 'menu-img-section-hover';
    obj.onmouseout = function() { obj.className = 'menu-img-section'; }
  }
}