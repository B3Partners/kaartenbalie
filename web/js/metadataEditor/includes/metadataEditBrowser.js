//Auteur: Erik van de Pol. B3Partners.

//deze functie wordt in de onload attribuut van de body tag aangeroepen
//selecteert tab die bij starten zichtbaar is.
//als javascript disabled is, wordt alle inhoud van alle tabs onder elkaar weergegeven
function xmlDocInit() {
  var visibleTab = document.getElementById("overzicht-tab");
  setInitialTab(visibleTab);
}

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
  oldContent.style.display = "none";

  var newTab = eTD;
  newTab.className = "tab-selected";
  var newContent = getAssociated(newTab);
  newContent.style.display = "block";

  stopPropagation(eTD);
}

//selecteert tab die bij starten zichtbaar is.
function setInitialTab(tab)  {
  var eRow = document.getElementById("main-menu");
  var tabs = eRow.childNodes;
  var tabs = tabs[0].childNodes;
  var tabs = tabs[0].childNodes;  
  for (var i = 0; i < tabs.length; i++) {
    var oldTab = tabs[i];
    if (oldTab.nodeType == 1) {
      var tabContent = getAssociated(oldTab);
      tabContent.style.display = "none";
    }
  }

  tab.className = "tab-selected";
  var newContent = getAssociated(tab);
  newContent.style.display = "block";
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