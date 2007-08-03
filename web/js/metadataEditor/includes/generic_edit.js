// 3/8/07 Erik van de Pol

// Rewritten variant of "generic_dhtml.vbs" by Eric Compas (2/3/05).
// Rewritten in javascript and working in most W3C compliant browsers.


// ==============
// Global Options
// ==============

// text box size (larger than max will create TEXTAREA also
// sets with of TEXTAREA)
var MIN_TEXTINPUT_SIZE = 30;
var MAX_TEXTINPUT_SIZE = 100;

// self reference text ('this' for JavaScript, 'me' for VBScript)
var SELF_REF = "this";

// Tekst constanten vertaald naar het Nederlands
// default text for elements with no default value specified 
var GLOBAL_DEFAULT = "Klik hier om deze tekst te bewerken";

// add/delete elements/sections menu text 
var ADD_ELEMENT_ABOVE_TEXT = "Voeg element hierboven toe";
var ADD_ELEMENT_BELOW_TEXT = "Voeg element hieronder toe";
var DELETE_ELEMENT_TEXT = "Verwijder dit element";
var ADD_SECTION_ABOVE_TEXT = "Voeg sectie hierboven toe";
var ADD_SECTION_BELOW_TEXT = "Voeg sectie hieronder toe";
var DELETE_SECTION_TEXT = "Verwijder deze sectie";
var ADD_CHILD_TEXT = "Voeg nieuw kind toe";

// expand/collapse section && menu image paths 
//var PLUS_IMAGE = "images/xp_plus.gif";
//var MINUS_IMAGE = "images/xp_minus.gif";
//var MENU_IMAGE = "images/arrow.gif";
var MENU_TOOLTIP = "Klik hier om opties weer te geven";

// message titles
var ERROR_TITLE = "Fout";

// default element value (if user leaves blank)
var DEFAULT_VALUE = "Standaard waarde.";

// global variables

var strPreEditText; // text held by SPAN before editing

// ================
// Helper functions
// ================
function trim(value) {
	value = value.replace(/^\s+/,'');
	value = value.replace(/\s+$/,'');
	return value;
}



// =============
// Edit Routines
// =============

function mouseEvent() {
	//function mouseEvent(pElem){
	//var pActiveElem;
	//Set pActiveElem = document.activeElement;

	//window.event.cancelBubble=false;

	/*var xPos;
	var yPos;

	xPos = window.event.x;
	yPos = window.event.y;

	//stopEdit(pElem);

	//alert(xPos + "   "+ yPos);
	if (xPos < 50 || yPos < 50) {
		//stopEdit(Null);
		//alert(xPos + " -- " +yPos);
		//window.event.cancelBubble=false;
		//window.event.SendKeys "{ENTER}";
		//stopEdit(pElem);
	}*/

}


// Description:
//   Track state of editable page. When user edits
//   something, this topage change flag.
// 
// Arguments:
//   bChange = 'true' somethings been edited
//   bChange = 'false' changes have been saved (almost never called)
function changeFlag(bChange) {
	var root = document.getElementById("editDocRoot");
	if (bChange) {
		//the body's 'changed' attribute to 'true'
		root.changed = "true";
		//document.all.changeFlag.innerHTML = "<font color='red'>Unsaved changes</font>"
	}
	else {
		//the body's 'changed' attribute to 'false'
		root.changed = "false";
		//document.all.changeFlag.innerHTML = "No unsaved changes"
	}
}

// determine what the shell is from <body> attribute
// returns:
//    "arccatalog" if ArcCatalog is shell
//    "standalone" if Stand-alond app is shell (default if error)
/*function getShellType() {
	// get body attribute
	var root = document.getElementById("editDocRoot");
	var strShell = root.getAttribute("shell");
	if (strShell == "arccatalog")
		return "arccatalog";
	else
		return "standalone";
}*/

// Description:
//   Change element value into a text input || text area to be edited.
//   Which is determined by size of text to be edited. Assumes that
//   text to be edited is in particular format.
//
// Arguement:
//   objElem = the <span> element containing text to be edited.
//   selfRef = the token to use for self-reference for an object, use
//             'me'   if(language of HTML page is VBScript
//             'this' if(language of HTML page is JavaScript

function startEdit(pElem, event) {
	// return if already editing
	if (pElem.childNodes[0].nodeType == 1)
		return;
	
	// get current value (for checking if changed later)
	strPreEditText = getElementInnerText(pElem);
	var strCurEditText = strPreEditText;

	// get size from current text
	var iCol, iRow;
	if (strCurEditText.length > MIN_TEXTINPUT_SIZE) {
		if (strCurEditText.length < MAX_TEXTINPUT_SIZE) {
			iCol = strCurEditText.length;
			iRow = 1;
		}
		else {
			iCol = MAX_TEXTINPUT_SIZE;
			iRow = Math.floor(strCurEditText.length / MAX_TEXTINPUT_SIZE) + 1;
		}
	}
	else {
		iCol = MIN_TEXTINPUT_SIZE;
		iRow = 1;
	}
	//alert("col=" + iCol + " row=" + iRow + " len(PElem.innerText) = " + len(pElem.innerText));

	// has picklist?
	if (pElem.getAttribute("picklist") != "" && pElem.getAttribute("picklist") != null) {
		// get picklist
		var pPicklist = GetPicklist(pElem.getAttribute("picklist"));
		if (pPicklist == null) {
			alert("Error locating picklist '" + pElem.getAttribute("picklist") + "' in stylesheet.");
		}
		else {
			// add to code and display it
			//*************************************************
			pElem.insertAdjacentElement("afterEnd", pPicklist);

			//current value
			//pPicklist.childNodes[0].value = pElem.innerText;
			
			//for (i=0; i<pPicklist.childNodes.length; i++) {
			for (var childIndex in pPicklist.childNodes) {
				node = pPicklist.childNodes[childIndex];
				if (node.nodeType == 1) {
					node.value = getElementInnerText(pElem);
					break;
				}
			}
			/*var node;
			For Each node in pPicklist.childNodes;
				//alert("node.nodeType = " + node.nodeType);
				if(node.nodeType == 1){
				node.value = pElem.innerText;
				Exit For;
				}
			Next*/
		}
	}

	// alt-clicked? if so, force use of textarea
	var event = getWindowEvent(event);
	if (event.altKey) {
		iRow = 4;
		iCol = MAX_TEXTINPUT_SIZE;
		//debug("Alt-clicked.");
	}


	//setElementInnerText(pElem, checkText(getElementInnerText(pElem)));

	// which key event to use (depends on shell)
	//var strKeyEvent = "onkeypress";

	// change span into text input or textarea for editing
	var newInnerText = checkText(getElementInnerText(pElem));//getElementInnerText(pElem);
	var editElem;
	
	if (iRow > 1) {
		// use textarea
		editElem = document.createElement("textarea");
		editElem.setAttribute("cols", iCol);
		editElem.setAttribute("rows", iRow);
		
		var text = document.createTextNode(newInnerText);
		editElem.appendChild(text);
	}
	else {
		// use text input
		editElem = document.createElement("input");
		editElem.setAttribute("type", "text");
		editElem.setAttribute("class", "input");
		editElem.setAttribute("value", newInnerText);
		editElem.setAttribute("size", iCol);		
	}
	
	editElem.setAttribute("onkeypress", "return checkKey(this, event);");
	editElem.setAttribute("onclick", "return false;");
	editElem.setAttribute("onBlur", "stopEdit(this);");

	pElem.innerHTML = "";
	pElem.appendChild(editElem);
	
	//debug("pElem.innerHTML: " + pElem.innerHTML);

	//focus to text input
	editElem.focus();

	// if default value, select it (makes it easier to replace);
	if (pElem.className == "default_value") {
		editElem.select();
	}
	
}



// Description:
//   Change text input or area element value back into displayed text.
//   Assumes that text to be edited is in particular format.
//
// Argument:
//   objElem = the <span> element containing text to be edited.
function stopEdit(pElem) {
	//debug("stopEdit");
	
	// check if picklist has focus, if so don't stop editing
	/*var pActiveElem = document.activeElement;
	if (pActiveElem.tagName == "SELECT") {
		return;
	}*/

	stopPropagation(pElem);

	// change text input back to displayed text
	//alert("innerText=" + pElem.innerText + " outerHTML=" + pElem.outerHTML + " value=" + pElem.value);

	// get parent <span> element
	var pParentElem = getParentElement(pElem);
	//alert("pParentElem.innerHTML = " + pParentElem.innerHTML);

	// check for changed value (from original)
	if (strPreEditText != pElem.value) {
		// user changed value,attributes
		// change class of span to 'changed_value'
		pParentElem.setAttribute("className", "changed_value");

		//element changed attribute
		pParentElem.changed = "true";

		//page changed attribute;
		changeFlag(true);

		//alert("User changed text");
	}
	else {
		//alert("User didn't change text");
	}

	// is blank? user deleted value? to span default (default value)
	// gebruikt trim() helper-functie
	if (trim(pElem.value) == "") {
		//alert("Cannot leave value blank - replacing with default value. " + '\r\n' +  "Note: This value will not be saved unless changed or element is mandatory.");
		if (pParentElem.getAttribute("default") != "") {
			pElem.value = pParentElem.getAttribute("default");
		}
		else {
			pElem.value = GLOBAL_DEFAULT;//DEFAULT_VALUE;
		}

		//class of value to default (won't be saved unless 'mandatory')
		//pParentElem.setAttribute("className", "default_value");
		pParentElem.setAttribute("class", "default_value");		

		//element changed attribute
		pParentElem.changed = "false";
	}

	// has picklist? if so, delete it
	if (pParentElem.getAttribute("picklist") != "" && pParentElem.getAttribute("picklist") != null) {
		//alert("Has picklist=" + pElem.parentElement.getAttribute("picklist"));

		// get picklist
		var pPicklist = pParentElem.nextSibling;
		//alert("pPicklist.tagName=" + pPicklist.tagName);
		//*******************************************************
		if (pPicklist == null) {
			alert("Error locating picklist in stylesheet to remove.");
		}
		else if (pPicklist.tagName.toLowerCase() != "select") {
			alert("Error locating picklist in stylesheet to remove.");
		}
		else {
			// delete it
			pPicklist.removeNode(true);
		}
	}
	//alert("Has picklist? =" + pElem.parentElement.getAttribute("picklist"));

	// change span value to text alone
	//alert(pElem.outerHTML);
	//var elemValue = pElem.value;
	//pParentElem.innerHTML = "";
	pParentElem.innerHTML = checkText(pElem.value);
	//pElem.outerHTML = checkText(pElem.value);

	//*********************************************************
	// turn click event back on
	//window.event.cancelBubble = false;
	//stopPropagation(pElem);
}

// 1/27/2005 Eric Compas;
//
// Description:
//   Retrieve a copy of a picklist embedded in an editable stylesheet.
//   Picklist is a <select> tag with sName as id. Usually hidden in
//   a undisplayed <div> tag at the bottom of the document.
//;
// Arguments:
//   sName = id of picklist <select> to get
//
// Return:
//   picklist = copy of <select> object containing picklist
//   -or-
//   null = if no picklist found
function GetPicklist(sName) {
	// copy picklist source
	var pPicklist = document.getElementById(sName);
	/*if (pPicklist == null) {
	GetPicklist = null;
	Exit Function;
	}
	// return found picklist;
	return pPicklist.cloneNode(true);*/
	if (pPicklist != null)
		return pPicklist.cloneNode(true);
	else
		return null;
}

// 2/05 Eric Compas
//
// Description: code called by picklists when selection changed (onchange)
function pickList(pElem) {
	//alert("pElem.tagName=" + pElem.tagName);
	//htmlText.value = pElem.parentElement.outerHTML;

	// get adjacent text box;
	var pTextInput = pElem.previousSibling.childNodes[0];
	//Set pTextInput = pElem.parentElement.previousSibling.childNodes(0);
	
	if (pTextInput.tagName != "textarea" && pTextInput.tagName != "input") {
		alert("Error locating text input for picklist.");
		return;
	}
	//alert("pTextInput.tagName=" + pTextInput.tagName + " value=" + pTextInput.value +  '\r\n' + " pElem.value=" + pElem.value );

	// update value;
	pTextInput.value = pElem.value;

	//focus back to text input
	// (need to do this to make sure text input onBlur event is triggered correctly)
	pTextInput.focus();
}

// 2/05 Eric Compas;
// Catch "tab" key press when picklist is open (will leave editing field open)
//***********************************************
function pickListKeyPress(pElem) {
	var iKey = getKeyCode(pElem);
	//alert("Key pressed = " + iKey);

	// was 'tab' pressed?
	if (iKey == 9) {
		// cancel default IE tab handler
		//window.event.returnValue = false;
		return false;
	}
}

// Description:
//   Capture a couple of key events in edit boxes.
//   For "tab" key, stop editing, but don't go to next link (IE default)
//   For "enter" key, stop editing if(in INPUT edit box.
//   For "escape" key, return to original value (toss out edits)
//*******************************************************
function checkKey(objElem, keyEvent) {
	var iKey = getKeyCode(keyEvent);
	if (iKey != null)
		debug("Key pressed = " + iKey);
	
	debug(objElem);

	// was enter pressed? (don't trigger in textarea)
	if (iKey == 13 && objElem.tagName.toLowerCase() == "input") {
		stopPropagation(objElem);
		
		// trigger onBlur event - stop editing
		objElem.blur();
		return false;
	}

	// was 'tab' or down-arrow pressed?
	// huh? can't use 40 for down-arrow, that's the '(' in ArcCatalog. Why?
	//if(iKey = 9 || iKey = 40){
	if (iKey == 9) {
		// cancel default IE tab handler
		//window.event.returnValue = false;
		
		stopPropagation(objElem);
		// trigger blur event - stop editing
		objElem.blur();
		return false;

		// open editing for next field?
	}

	// was 'shift-tab' or up-arrow pressed?
	if (iKey == 38) {
		// cancel default IE tab handler
		//window.event.returnValue = false;

		stopPropagation(objElem);
		// trigger blur event - stop editing
		objElem.blur();
		return false;

		// open editing for previous field?
	}

	// was 'escape' pressed?
	if (iKey == 27) {
		//text to original value
		objElem.value = strPreEditText;

		stopPropagation(objElem);
		// trigger blur event - stop editing
		objElem.blur();
	}
}

// Description:
//   Get next (bNext = true) || previous (bNext = false) editable
//   SPAN tag in document from current one (objElem). if none found,
//   returns null;
function getNextPreEditSpan(objElem, bNext) {
	// get all SPANs
	var pSPANElements = document.getElementsByTagName("span");

	// find current one in collection
	// ************************************
}

// 1/30/04 Eric Compas;
// 8/04 EDC - converted to VBScript from JScript
// 12/04 EDC - add in specified location (not just last element)
//
// Description:
//   Add new input box to input box list in the specified location.
//
// Arguments:
//   objElem = anchor element (in popup menu) that called this routine
//             (use to get reference to its list item)
//   iListItem = item number (1 based) to add new input after
//   strName = name of element, usually its full path, e.g. /metadata/idinfo/keywords/theme
//   strSize = size (max?) of the text element
//   strDefaultValue = the default value of the element to be assigned
//   strPicklist = name of picklist to use ('' if none)
//   bAbove = whether to add new element above (true) or below (false)
function addListItem(objElem, strName, strSize, strDefaultValue, strPicklist, bAbove) {
	// get calling menu and test for problems;
	var objMenuSpan = objElem.parentElement.parentElement.parentElement;
	if (objMenuSpan.tagName != "SPAN") {
		alert("Unexpected HTML object encountered. Expected SPAN, found " + objMenuSpan.tagName);
		return;
	}

	// get listitem and test for problems
	var objListItem = objMenuSpan.parentElement;
	if(objListItem.tagName != "LI"){
		alert("Unexpected HTML object encountered. Expected LI, found " + objListItem.tagName);
		return;
	}

	// get list and test for problems
	var objList = objListItem.parentElement;
	if(objList.tagName != "UL" && objList.tagName != "OL"){
		alert("Unexpected HTML object encountered. Expected UL or OL, found " + objList.tagName);
		return;
	}

	// create new span
	var strNewSpan = "<span class='changed_value' title='" + strDefaultValue +  "' name='" + strName + "' onclick='startEdit(this)' " +  "changed='true' picklist='" + strPicklist + "'>" +  strDefaultValue + "</span>";

	// add menu (copy HTML from calling menu)
	//var objMenuCopy;
	//Set objMenuCopy = objMenuSpan.cloneNode(true);
	//objMenuCopy.childNodes(1).style.display = "none"
	var strMenu = "<span onclick=\"ShowMenu(this)\"><img title=\"Click for options\" src=\"" + MENU_IMAGE + "\">" +  "<ul class=\"menu\" onmouseleave=\"HideMenu(this)\">" +  "<li class=\"menuaddabove\"><a href=\"javascript:void(0)\" onclick=\"addListItem(this,'" + strName + "',50,'" + strDefaultValue + "','" + strPicklist + "', true)\">" + ADD_ELEMENT_ABOVE_TEXT + "</a></li>" +  "<li class=\"menuaddbelow\"><a href=\"javascript:void(0)\" onclick=\"addListItem(this,'" + strName + "',50,'" + strDefaultValue + "','" + strPicklist + "', false)\">" + ADD_ELEMENT_BELOW_TEXT + "</a></li>" +  "<li class=\"menudelete\"><a href=\"javascript:void(0)\" onclick=\"deleteListItem(this)\">" + DELETE_ELEMENT_TEXT + "</a></li>" +  "</ul></span>";
	//alert("strMenu = " + strMenu);
	strNewSpan = strNewSpan + strMenu;
	//htmlText.value = strMenu;
	//alert("strNewSpan = " + strNewSpan);
	//strNewSpan = strNewSpan + objMenuCopy.outerHTML;

	// add line item
	var objNewLI = document.createElement("li");
	objNewLI.innerHTML = strNewSpan;

	// add to document (either before or after current element)
	//***************************************
	if (bAbove) {
		//alert("beforeBegin");
		//alert(objNewLI);
		objListItem.insertAdjacentElement("BeforeBegin", objNewLI);
	}
	else {
		objListItem.insertAdjacentElement("AfterEnd", objNewLI);
	}

	//page changed value
	changeFlag(true);
}


// 1/30/04 Eric Compas
// 8/04 EDC - converted to VBScrip
// 12/04 EDC - delete specified (not just last)
// Description:
//   Delete a list item from list with specified Id.
//
// Arguments:
//   strListId = Id of list to delete from. Needs to be unique in document
//   iListItem = number of list item to delete (1 based)
function deleteListItem(objElem) {
	// get calling menu and test for problems
	var objMenuSpan = objElem.parentElement.parentElement.parentElement;
	if (objMenuSpan.tagName != "SPAN") {
		alert("Unexpected HTML object encountered. Expected SPAN, found " + objMenuSpan.tagName);
		return;
	}

	// get listitem and test for problems
	var objListItem = objMenuSpan.parentElement;
	if (objListItem.tagName != "LI") {
		alert("Unexpected HTML object encountered. Expected LI, found " + objListItem.tagName);
		return;
	}

	//get list and check for problems;
	var objList = objListItem.parentElement;
	if (objList.tagName != "UL" && objList.tagName != "OL") {
		alert("Unexpected HTML object encountered. Expected UL or OL, found " + objList.tagName);
		return;
	}

	// last value? don't let them delete it
	if (objList.childNodes.length < 2) {
		alert("Deleting the last element is not allowed.");
		return;
	}

	// get span, contain value?
	var objSpan = objListItem.firstChild;
	if (objSpan.innerHTML != "") {
		var iReturn = confirm("Are you sure you want to delete the '" + objSpan.innerHTML + "' value?");

		// delete item;
		//*************************************
		if (iReturn == 7) {
			return;
		}
	}

	// delete item;
	objList.removeChild(objListItem);

	//page changed value;
	changeFlag(true);
}

// 12/30/2004 Eric Compas
//
// Description:
//   Add a new section (node tree) using existing one as template
//
// Arguments:
//  objElem = the calling element, the anchor tag in popup menu
//   strAddName = the name of the section to add, e.g. "theme"
//   bAbove = whether to add new element above (true) or below (false)
function addSection(objElem, strAddName, bAbove) {
	//alert(objElem)
	//alert(strAddName)
	//alert(bAbove)

	// create new section
	var objContentDIV = createSection(strAddName);
	if (objContentDIV == null) {
		alert("Error creating new compound element.");
		return;
	}

	// insert new section into document

	// get calling menu and test for problems
	var objMenuSpan = objElem.parentElement.parentElement.parentElement;
	if (objMenuSpan.tagName != "SPAN") {
		alert("Unexpected HTML object encountered. Expected SPAN, found " + objMenuSpan.tagName);
		return;
	}
	//alert("objMenuSpan.outerHTML = " + objMenuSpan.outerHTML);

	// get parent folder div and check for problems
	var objFolderDiv = objMenuSpan.parentElement;
	if (objFolderDiv.tagName != "DIV") {
		alert("Unexpected HTML object encountered. Expected DIV, found " + objFolderDiv.tagName);
		return;
	}

	// add new DIV to document (either before or after current element's 'folder' div)
	//alert("objContentDIV");
	//alert(objContentDIV.innerHTML);
	//alert(objContentDIV.outerHTML);
	if (bAbove)
		objFolderDiv.insertAdjacentElement("beforeBegin", objContentDIV);
	else
		objFolderDiv.insertAdjacentElement("afterEnd", objContentDIV);

	//alert("Done add section");
}

// 12/30/2004 Eric Compas
//
// Description:
//   Delete section (node tree).
//
// Arguments:
//   objElem = the input button calling routine, used to get reference to
//             other objects to delete
function deleteSection(objElem, strSectPath) {
	// get calling menu and test for problems
	var objMenuSpan = objElem.parentElement.parentElement.parentElement;
	if (objMenuSpan.tagName != "SPAN") {
		alert("Unexpected HTML object encountered. Expected SPAN, found " + objMenuSpan.tagName);
		return;
	}
	//alert("objMenuSpan.outerHTML = " + objMenuSpan.outerHTML);

	// get parent folder div and check for problems;
	var objFolderDiv = objMenuSpan.parentElement;
	if (objFolderDiv.tagName != "DIV") {
		alert("Unexpected HTML object encountered. Expected DIV, found " + objFolderDiv.tagName);
		return;
	}

	// get FolderDiv's parent (may by "content" or "folder" DIV)
	var objParentDiv = objFolderDiv.parentElement;
	if (objParentDiv.tagName != "DIV") {
		alert("Unexpected HTML object encountered. Expected DIV, found " + objParentDiv.tagName);
		return;
	}

	// check to make sure not last folder DIV of same type
	var objDiv;
	var i = 0;
	//for (j=0; j<objParentDiv.childNodes.length; j++) {
		//objDiv = objParentDiv.childNodes[j];
	for (var childIndex in objParentDiv.childNodes) {
		objDiv = objParentDiv.childNodes[childIndex];
		// if nodeType is text, getAttribute() doesn't exists
		if (objDiv.getAttribute)
			if (objDiv.getAttribute("section_path") == strSectPath)
				i++;
	}
	
	//alert("i after: " + i);
	
	if (i < 2) {
		alert("Deleting the last section is not allowed.");
		return;
	}

	// confirm delete
	var iReturn = confirm("Are you sure you want to delete this section?");
	if (iReturn == 7) {
		return;
	}

	// delete section
	objFolderDiv.parentElement.removeChild(objFolderDiv);

	//page changed value
	changeFlag(true);
}

// 2/21/2005 Eric Compas
//
// Description:
//   Add a child section (compound element) below the current
//   parent. Similar to AddSection code except adding as
//   child instead of sibling.
function addChild(objElem, strAddName) {
	// create new section (compound element)
	var objNewDIV = createSection(strAddName);
	if (objNewDIV == null) {
		alert("Error creating new section (compound element)).");
		return;
	}

	// get objects to ensure proper placement of new child
	// (must appear within parent DIV's 'content' div just before hidden span
	//  closing out the parent XML element)

	// get calling menu and test for problems
	var objMenuSpan = objElem.parentElement.parentElement.parentElement;
	if(objMenuSpan.tagName != "SPAN"){
		alert("Unexpected HTML object encountered. Expected SPAN, found " + objMenuSpan.tagName);
		return;
	}

	// get parent folder div and check for problems
	var objFolderDiv = objMenuSpan.parentElement;
	if(objFolderDiv.tagName != "DIV"){
		alert("Unexpected HTML object encountered. Expected DIV, found " + objFolderDiv.tagName);
		return;
	}

	// get content div of folder div
	var objContentDiv = objFolderDiv.getElementsByTagName("DIV").item[0];
	if(objContentDiv.className != "content"){
		alert("Cannot locate 'content' DIV for parent 'folder' DIV.");
		return;
	}

	// get content div's last SPAN (the hidden closing XML element)
	var objLastSpan = objContentDiv.children.item(objContentDiv.children.length - 1);
	if(objLastSpan.tagName != "SPAN"){
		alert("Unexpected HTML object encountered. Expected SPAN, found " + objLastSpan.tagName);
		return;
	}

	// insert new DIV in document
	//objContentDiv.insertBefore objNewDIV, objLastSpan
	objLastSpan.insertAdjacentElement("beforeBegin", objNewDIV);
}

// 2/21/2005 Eric Compas
//
// Description:
//   Routine called by AddSection and AddChild code to create
//   a new section (compound element) using an XSL transformation
//   of a small chunk of XML code.
function createSection(strAddName) {
	//alert("strAddName=" + strAddName);

	// get edit stylesheet file location from document
	var pXSLSpan = document.getElementById("editStylesheetFile");
	if (pXSLSpan == null) {
		alert("Error locating editable stylesheet file. Please " +  "correct its path within this stylesheet.");
		return null;
	}
	var strEditXSLFile = pXSLSpan.innerText;

	// get add templates stylesheet file location from document
	pXSLSpan = document.getElementById("addStylesheetFile");
	if (pXSLSpan == null) {
		alert("Error locating add templates stylesheet file. Please " +  "correct its path within this stylesheet.");
		return null;
	}
	var strAddXSLFile = pXSLSpan.innerText;


	//alert("strAddXSLFile=" + strAddXSLFile + ", strEditXSLFile=" + strEditXSLFile);

	// create blank XML document
	//*****************************************
	//IE only. Nog FF doen
	var xmlDoc = new ActiveXObject("MSXML2.DOMDocument");
	xmlDoc.async = false;
	//xmlDoc.loadXML "<metadata/>";
	xmlDoc.loadXML("<root/>");

	// add blank parent elements (if they exist)
	// (needed if "full path" transformations used)
	var sParentNodes, sNode;
	//if (InStrRev(strAddName,"/") > 0) {
	if (strAddName.lastIndexOf("/") > -1) {
		//alert("AddSection: found parents");
		var sNodes, pNode, newNode;
		//sParentNodes = Left(strAddName, InStrRev(strAddName,"/")-1);
		sParentNodes = strAddName.substring(0, strAddName.lastIndexOf("/"));
		//alert("AddSection: parent nodes = " + sParentNodes);
		pNode = xmlDoc.documentElement;
		sNodes = sParentNodes;
		while (sNodes != "") {
			//if (InStr(sNodes,"/") > 0) {
			if (sNodes.indexOf("/") > -1) {
				// get next parent
				//sNode = Left(sNodes, InStr(sNodes,"/") - 1);
				//sNodes = Right(sNodes, Len(sNodes) - InStr(sNodes,"/"));
				sNode = sNodes.substring(0, sNodes.indexOf("/"));
				sNodes = sNodes.substring(sNodes.indexOf("/") + 1);
			}
			else {
				// last parent
				sNode = sNodes;
				sNodes = "";
			}
			//alert("AddSection: sNode = " + sNode + ", sNodes = " + sNodes);
			newNode = xmlDoc.createNode(1, sNode, "");
			pNode.appendChild(newNode);
			pNode = newNode;
		}
		
		//sNode = Right(strAddName, Len(strAddName) - InStrRev(strAddName,"/"));
		sNode = strAddName.substring(strAddName.lastIndexOf("/") + 1);
	}
	else {
		sParentNodes = "";
		sNode = strAddName;
		//alert("AddSection:parent ! found " + strAddName);
	}
	//alert("xmlDoc = " + xmlDoc.xml);
	//alert("sParentNodes = " + sParentNodes + ", sNode = " + sNode);

	// create stylesheet to transform, or "preprocess" it;
	// this will add the appropriate blank XML elements;
	var preXSLDoc = new ActiveXObject("MSXML2.DOMDocument");
	var strXSL = "<?xml version='1.0' encoding='ISO-8859-1'?>" +  "<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>" +  "<xsl:output method='xml' indent='yes'/>" +  "<!-- inlude Add Templates library for stylesheet -->" +  "<xsl:include href='" + strAddXSLFile + "'/>" ;

	//start of creating the XSL transform;
	if (sParentNodes != "") {
		strXSL += "<xsl:template match='" + sParentNodes + "'>";
	}
	else {
		strXSL += "<xsl:template match='/'>";
		strXSL += "<xsl:template match='" + strAddName + "'>";
	}

	//alert("strXSL : "+ strXSL);
	//alert("sNode =  "+sNode);


	strXSL += "<xsl:copy><xsl:call-template name='add_" + sNode + "' /></xsl:copy>" +  "</xsl:template><xsl:template match='@*|node()'><xsl:copy>" +  "<xsl:apply-templates select='@*|node()' />" +  "</xsl:copy></xsl:template></xsl:stylesheet>" ;

	alert("strXSL "+ strXSL);

	preXSLDoc.async = false;
	preXSLDoc.loadXML(strXSL);
	if (preXSLDoc.parseError.errorCode != 0) {
		alert("Error parsing the preprocesser XSL file. Code=" +  preXSLDoc.parseError.errorCode + ", Desc=" +  preXSLDoc.parseError.reason);
		return null;
	}
	alert("preXSLDoc.xml = " + preXSLDoc.xml);

	// transform or "preprocess" it
	var preXMLDoc = new ActiveXObject("MSXML2.DOMDocument");
	//On Error Goto EH:
	alert("hierna loopt ie vast");
	var strPreXML = xmlDoc.transformNode(preXSLDoc);
	alert("hier");
	preXMLDoc.loadXML(strPreXML);
	if (preXMLDoc.parseError.errorCode != 0) {
		alert("Error parsing the preprocessed XML file. Code=" +  preXMLDoc.parseError.errorCode + ", Desc=" +  preXMLDoc.parseError.reason);
		return null;
	}

	alert("preXMLDoc "+ preXMLDoc.xml);


	// transform the resulting xml with editable stylesheet
	var xslDoc = new ActiveXObject("MSXML2.DOMDocument");
	xslDoc.load(strEditXSLFile);
	//alert("xslDoc.xml = " + Left(xslDoc.xml, 50) + "...");

	//alert("preXMLDoc.xml = " + preXMLDoc.xml);
	alert("xslDoc = "+ xslDoc.xml);

	var strHTML = preXMLDoc.transformNode(xslDoc);
	//alert("strHTML  " + strHTML);
	//htmlText.value = strHTML;
	//prompt("transformed HTML", strHTML)

	//oFile.Write strHTML;

	//get rid of <?xml?> tag, if(present;
	if (strHTML.indexOf("<?xml") > -1) {
		//strHTML = Right(strHTML, strHTML.length - strHTML.indexOf(">"));
		strHTML = strHTML.substring(strHTML.indexOf(">") + 1);
	}

	//alert("strHTML (edited1) = " + strHTML);

	//get rid of <!DOCTYPE> tag, if(present;
	if (strHTML.indexOf("<!DOCTYPE") > -1) {
		//strHTML = Right(strHTML, strHTML.length - strHTML.indexOf(">"));
		strHTML = strHTML.substring(strHTML.indexOf(">") + 1);
	}

	//alert("strHTML (edited2) = " + strHTML);

	//get rid of the rest (shouldn't be needed);
	//strHTML = Right(strHTML, Len(strHTML) - InStr(strHTML,"<div class=""folder""") + 1);
	strHTML = strHTML.substring(strHTML.indexOf("<div class=\"folder\""));

	//alert("strHTML (edited2) = " + strHTML);

	// create div to place html within;
	var objDIV = document.createElement("div");
	//objDIV.setAttribute "class", "section";

	// insert HTML in div;
	objDIV.innerHTML = strHTML;
	//alert("objDIV.innerHTML" + objDIV.innerHTML);
	//var fso, fsw;

	//Set fso = new ActiveXObject("Scripting.FileSystemObject");
	//set fsw = fso.CreateTextFile( "D:\metadata_strHTML.xml",True);
	//fsw.write strHTML;

	// remove more stuff - get the correct content DIV;
	var objContentDIV;
	if(sParentNodes == ""){
		//Set objContentDIV = document.createElement("div");
		//objDIV.setAttribute "class", "section";
		//alert("objDIV.childNodes(0).outerHTML = " + objDIV.childNodes(0).outerHTML);
		//alert("objDIV.childNodes(0).childNodes(1).outerHTML = " + objDIV.childNodes(0).childNodes(1).outerHTML);
		//alert("objDIV.childNodes(0).childNodes(0).childNodes(1).outerHTML = " + objDIV.childNodes(0).childNodes(0).childNodes(1).outerHTML);
		//Set objContentDIV = objDIV.childNodes(0).childNodes(1);
		//Set objContentDIV = objDIV.childNodes(0);
		//alert("Child Nodes " + objDIV.childNodes(0));
		objContentDIV = objDIV;
		//alert("NO Parent Insert");
	}
	else {
		var fsx, fsy;

		//Set fso = new ActiveXObject("Scripting.FileSystemObject");
		//set fsx = fso.CreateTextFile( "D:\metadata_logs.xml",True);
		//set fsy = fso.CreateTextFile( "D:\metadata_logs_childnodes.xml",True);
		//fsw.write("This is a test");
		//fsw.close;

		// search for DIV with correct section_path;
		//alert("Loop");
		var objTemp = objDIV.childNodes[0];
		//alert("strAddName " + strAddName);
		//alert("objDIV.innerHTML " + objDIV.innerHTML);
		//fsx.writeline(strAddName);
		//fsx.writeline("");
		//fsx.writeline("");
		//fsx.writeline("");
		//fsx.writeline("");

		var varAddName, totalCount;
		totalCount = 1;
		while (objTemp != null) {
			//fsx.writeline("TOTAL COUNT = "+totalCount);
			//fsx.writeline (objTemp.innerHTML);
			//fsx.writeline (objTemp.outerHTML);
			//fsx.writeline("");
			//fsx.writeline("");

			//alert("objTemp.outerHTML = " + objTemp.outerHTML);
			//alert("objTemp.innerHTML = " + objTemp.innerHTML);
			//if(objTemp.getAttribute("section_path") is ! Null)){
			//alert("section_path " +objTemp.getAttribute("section_path"));
			//}

			varAddName = objTemp.getAttribute("section_path");
			//
			if (varAddName == strAddName) {
				//fsx.writeline("");
				//fsx.writeline("MATCH");
				//fsx.writeline("")   'if(! isNull(varAddName)){
				objContentDIV = objTemp;
				break;
			}
			
			
			//onderstaande doet niets (alles was al uitgecommentarieerd)
			/*
			var oNodeList = objTemp.childNodes;

			if (oNodeList != null) {
				// alert("null");
				//}else{
				var obj, i;
				if (oNodeList.length > 0) {
					//fsy.writeline("####################### NEW############################");

					//i = 0;

					For Each obj in oNodeList    ;

					//if(! isNull(obj.getAttribute("section_path"))){
					//alert("section_path: "+ obj.getAttribute("section_path"));
					//}else{
					//alert("section_path: null");
					//}

					//fsy.writeline ("********************   " + i+ "  **************************");
					//fsy.writeline (obj.outerHTML);
					//fsy.writeline("");
					//fsy.writeline("");


					//i= i+1;
					Next;
				}
			}*/

			var listNodes, listNode, divNode;

			listNodes = objTemp.childNodes;
			divNode = "DIV";

			//for (i=0; i<listNodes; i++) {
			//for each listNode in listNodes;
				//fsx.writeline("");
				//fsx.writeline(listNode);
				//fsx.writeline("");
				//fsx.writeline(listNode.outerHTML);
				//fsx.writeline("");
				//''WORKING ON THE FOR each loop;
			for (var childIndex in listNodes) {
				listNode = listNodes[childIndex];
				if (listNode.nodeType == 1) {
					if (listNode.outerHTML.indexOf("DIV") > -1) {
						objTemp = listNode;
						break;
					}
				}
			}

			//if(! isSet){
			//'Set objTemp = objTemp.childNodes(1);
			//}

			totalCount++;
		}


	}

	//fsx.writeline(objContentDIV.innerHTML);

	//fsw.close;
	//fsx.close;
	//fsy.close;


	//return value;
	return objContentDIV;
}

// Description:
//   Check that edited text contains only valid XML characters.
//   Checks for the following characters && makes replacement:
//  <   &lt;
//  >   &gt;
//  +   &amp;
//  "   &quot;
//  '   &apos;   ' ! required? won't display correctly in IE
//
// Arguments:
//   strText = text string to check && reformat
function checkText(strText){
	// do replacements

	//strText = replace(strText,"+","&#38");
	//strText = replace(strText,"'","&#39");
	//strText = replace(strText,"""","&#34");
	//strText = replace(strText,"<","&#60");
	//strText = replace(strText,">","&#62");

	strText = strText.replace("+", "&amp;");
	strText = strText.replace("<", "&lt;");
	strText = strText.replace(">", "&gt;");
	strText = strText.replace("\"", "&quot;");
	//strText = strText.replace("'", "&apos;"); // not required?
	strText = strText.replace("'", "&#39");

	// more complex search for ampersand (since it's in all of the replacements)
	// TODO: write this example &nbsp;

	//debug
	//alert("return " + strText);

	//return value
	return strText;
}

function showHTML(textAreaID){
	// show page's html in debug window
	var strData = document.documentElement.innerHTML;
	textAreaID.value = strData;
}