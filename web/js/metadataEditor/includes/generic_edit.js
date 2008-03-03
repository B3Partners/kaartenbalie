// 3/8/07 Erik van de Pol

// Loosely based on "generic_dhtml.vbs" by Eric Compas (2/3/05).
// Rewritten in javascript and working in most W3C compliant browsers.


// ==============
// Global Options
// ==============

// text box size (larger than max will create TEXTAREA also
// sets width of TEXTAREA)
var MIN_TEXTINPUT_SIZE = 50;
var MAX_TEXTINPUT_SIZE = 75;

// Tekst constanten in het Nederlands
var GLOBAL_DEFAULT = "Klik hier om deze tekst te bewerken.";

// add/delete elements/sections menu text 
var ADD_ELEMENT_ABOVE_TEXT = "Voeg element hierboven toe";
var ADD_ELEMENT_BELOW_TEXT = "Voeg element hieronder toe";
var DELETE_ELEMENT_TEXT = "Verwijder dit element";

var ADD_SECTION_ABOVE_TEXT = "Voeg sectie hierboven toe";
var ADD_SECTION_BELOW_TEXT = "Voeg sectie hieronder toe";
var DELETE_SECTION_TEXT = "Verwijder deze sectie";

var CONFIRM_DELETE_ELEMENT_TEXT = "Weet u zeker dat u dit element wilt verwijderen?";
var NOT_ALLOWED_DELETE_ELEMENT_TEXT = "Het is niet toegestaan het laatste element van dit type te verwijderen.";

var CONFIRM_DELETE_SECTION_TEXT = "Weet u zeker dat u deze sectie wilt verwijderen?";
var NOT_ALLOWED_DELETE_SECTION_TEXT = "Het is niet toegestaan de laatste sectie van dit type te verwijderen.";

// expand/collapse section && menu image paths 
//var PLUS_IMAGE = "images/xp_plus.gif";
//var MINUS_IMAGE = "images/xp_minus.gif";
//var MENU_IMAGE = "images/arrow.gif";
var MENU_TOOLTIP = "Klik hier om opties weer te geven.";

// message titles
var ERROR_TITLE = "Fout";

// default element value (if user leaves blank)
var DEFAULT_VALUE = "Standaard waarde.";

// global variables
var preEditText; // text held by SPAN before editing


// a click anywhere in the document should close any open menu.
// propagation should be stopped from reaching this function for any behavior that needs the menu to be open.
function click() {
	//debug("click anywhere");
	hideMenu(openedMenuNode);
}


// ================
// Helper functions
// ================
function trim(value) {
	if (value != null && value != "") {
		value = value.replace(/^\s+/, "");
		value = value.replace(/\s+$/, "");
	}
	return value;
}


// =============
// Edit Routines
// =============

// Description:
//   Track state of editable page. When user edits
//   something, this topage change flag.
// 
// Arguments:
//   bChange = 'true' somethings been edited
//   bChange = 'false' changes have been saved (almost never called)
function changeFlag(changed) {
	var root = document.getElementById("edit-doc-root");
	if (changed) {
		document.getElementById("saveButton").disabled = false;
		if (document.title.substring(document.title.length - 1) != "*")
			document.title += "*";
	}
}

// Description:
//   Change element value into a text input || text area to be edited.
//   Which is determined by size of text to be edited. Assumes that
//   text to be edited is in particular format.
//
// Argument:
//   event = the <span> element containing text to be edited.

function startEdit(event) {
	var element = getTarget(event);
	//debug("startEdit");
	
	// already editing?
	if (element.tagName.toLowerCase() == "input" || element.tagName.toLowerCase() == "textarea" || element.tagName.toLowerCase() == "select")
		return;
	
	// hides any menu if open
	hideMenu(openedMenuNode);
	
	// get current value (for checking if changed later)
	preEditText = trim(getElementInnerText(element));

	// has no picklist?
	if (element.getAttribute("picklist") == "" || element.getAttribute("picklist") == null) {
		// get size from current text
		var iCol, iRow;
		if (preEditText.length > MIN_TEXTINPUT_SIZE) {
			if (preEditText.length < MAX_TEXTINPUT_SIZE) {
				iCol = preEditText.length;
				iRow = 1;
			}
			else {
				iCol = MAX_TEXTINPUT_SIZE;
				iRow = Math.floor(preEditText.length / MAX_TEXTINPUT_SIZE) + 1;
			}
		}
		else {
			iCol = MIN_TEXTINPUT_SIZE;
			iRow = 1;
		}
		//alert("col=" + iCol + " row=" + iRow + " len(element.innerText) = " + len(element.innerText));
		
		// alt-clicked? if so, force use of textarea
		var event = getWindowEvent(event);
		if (event.altKey) {
			iRow = 4;
			iCol = MAX_TEXTINPUT_SIZE;
			//stopPropagation(event);
			//debug("Alt-clicked.");
		}

		// change span into text input or textarea for editing
		var newInnerText = getElementInnerText(element);		

		var inputElement;
		if (iRow > 1) {
			// use textarea
			inputElement = document.createElement("textarea");
			inputElement.setAttribute("cols", iCol);
			inputElement.setAttribute("rows", iRow);

			var text = document.createTextNode(newInnerText);
			inputElement.appendChild(text);
		}
		else {
			// use text input
			inputElement = document.createElement("input");
			inputElement.setAttribute("type", "text");
			inputElement.setAttribute("class", "input");
			inputElement.setAttribute("value", newInnerText);
			inputElement.setAttribute("size", iCol);		
		}

		inputElement.onkeypress = checkKey;
		if (inputElement.captureEvents) inputElement.captureEvents(Event.KEYPRESS);
		//inputElement.setAttribute("onkeypress", "debug(\"jaja\"); return checkKey(this, event);");
		//inputElement.setAttribute("onclick", "return false;");
		inputElement.onblur = stopEdit;
		//inputElement.setAttribute("onblur", "stopEdit(this);");

		element.innerHTML = "";
		element.appendChild(inputElement);

		inputElement.focus();

		// if default value, select it (makes it easier to replace);
		if (element.className == "default-value") {
			inputElement.select();
		}
	}
	else {
		// get picklist
		var picklist = getPicklist(element.getAttribute("picklist"));
		if (picklist == null) {
			alert("Error locating picklist '" + element.getAttribute("picklist") + "' in stylesheet.");
		}
		else {
			picklist.setAttribute("class", "picklist");
					
			// check if value exists in picklist and remember selected index
			var exists = false;
			var pick;
			for (var selectedIndex = 0; selectedIndex < picklist.options.length; selectedIndex++) {
				pick = picklist.options[selectedIndex];
				if (getElementInnerText(pick) == preEditText) {
					exists = true;
					break;
				}
			}
			
			if (exists) {
				picklist.selectedIndex = selectedIndex;
			}
			else if (preEditText != GLOBAL_DEFAULT) {
				// create new option
				var newOption = document.createElement('option');
				newOption.text = preEditText;
				newOption.value = preEditText;
				
				// insert value at the top of the picklist
				try {
					picklist.add(newOption, picklist.options[0]); // standards compliant; doesn't work in IE
					debug("standards compliant picklist add");
				}
				catch(e) {
					picklist.add(newOption, picklist.selectedIndex); // IE only
					debug("IE only picklist add");
				}
			}
			
			// add picklist to code and display it
			element.innerHTML = "";
			element.appendChild(picklist);
			picklist.focus();
		}
	}
	
	stopPropagation(event);
}


// Description:
//   Change text input or area element value back into displayed text.
//   Assumes that text to be edited is in particular format.
function stopEdit(event) {
	var element = getTarget(event);
	//debug("stopEdit element: " + element.tagName);
	
	var parentNode = element.parentNode;

	var newValue = trim(element.value);
	// check for changed value (from original)
	if (preEditText != newValue) {
		saveValueOnClientSide(parentNode, newValue);
	}

	// is blank? user deleted value? to span default (default value)
	if (newValue == "") {
		//alert("Cannot leave value blank - replacing with default value. " + '\r\n' +  "Note: This value will not be saved unless changed or element is mandatory.");
		if (parentNode.getAttribute("default") != "") {
			newValue = parentNode.getAttribute("default");
		}
		else {
			newValue = GLOBAL_DEFAULT;//DEFAULT_VALUE;
		}
		//class of value to default (won't be saved unless 'mandatory')
		parentNode.className = "default-value";
	}
	
	// change span value to text alone
	parentNode.innerHTML = "";
	parentNode.appendChild(document.createTextNode(newValue));
	
	stopPropagation(event);
    
    //debugXmlDoc(xmlDoc);
}

function saveValueOnClientSide(parentNode, newValue) {
	parentNode.className = "changed-value";
	changeFlag(true);
	//debug("parentNode.attributes.getNamedItem(\"fullPath\").nodeValue: " + parentNode.attributes.getNamedItem("fullPath").nodeValue);		
	saveChangesInXMLDom(newValue, parentNode.attributes.getNamedItem("fullPath").nodeValue);
}

// Description:
//   Retrieve a copy of a picklist embedded in an editable stylesheet.
//   Picklist is a <select> tag with sName as id. Usually hidden in
//   a undisplayed <div> tag at the bottom of the document.
//;
// Arguments:
//   name = id of picklist <select> to get
//
// Return:
//   picklist = copy of <select> object containing picklist
//   -or-
//   null = if no picklist found
function getPicklist(name) {
	// copy picklist source
	var picklist = document.getElementById(name);

	if (picklist != null)
		return picklist.cloneNode(true);
	else
		return null;
}

// Description: code called by picklists when selection changed (onchange)
function selectPickListValue(event) {
	var element = getTarget(event);
	//debug("element.tagName=" + element.tagName);

	if (element == null) {
		alert("Error locating picklist in stylesheet to remove.");
	}
	else if (element.tagName.toLowerCase() != "select") {
		alert("Error locating picklist in stylesheet to remove.");
	}
	else {
		var newValue = element.value;
		//element.title? desc
		var parentNode = element.parentNode;
		parentNode.innerHTML = "";
		parentNode.appendChild(document.createTextNode(newValue));
		saveValueOnClientSide(parentNode, newValue);
	}
	
	stopPropagation(event);
}

function destroyPickList(event) {
	var element = getTarget(event);
	var parentNode = element.parentNode;
	parentNode.innerHTML = "";
	parentNode.appendChild(document.createTextNode(preEditText));
}

// Catch "tab" key press when picklist is open (will leave editing field open)
function pickListKeyPress(element) {
	var iKey = getKeyCode(element);
	//alert("Key pressed = " + iKey);

	// was 'tab' or 'escape' pressed?
	if (iKey == 9 || iKey == 27) {
		// cancel default IE tab handler
		//window.event.returnValue = false;
		stopPropagation(element);
		return false;
	}
}

// Description:
//   Capture a couple of key events in edit boxes.
//   For "tab" key, stop editing, but don't go to next link (IE default)
//   For "enter" key, stop editing if in INPUT edit box.
//   For "escape" key, return to original value (toss out edits)
function checkKey(event) {
	//debug("checkKey");
	var element = getTarget(event);
	var iKey = getKeyCode(event);
	
	//debug(element);

	// was enter pressed? (don't trigger in textarea)
	if (iKey == 13 && element.tagName.toLowerCase() == "input") {
		stopPropagation(element);
		
		// trigger onBlur event - stop editing
		element.blur();
		return false;
	}

	// was 'tab' or down-arrow pressed?
	// huh? can't use 40 for down-arrow, that's the '(' in ArcCatalog. Why?
	//if (iKey = 9 || iKey = 40) {
	if (iKey == 9) {
		// cancel default IE tab handler
		//window.event.returnValue = false;
		
		stopPropagation(element);
		// trigger blur event - stop editing
		element.blur();
		return false;

		// open editing for next field?
	}

	// was 'shift-tab' or up-arrow pressed?
	if (iKey == 38) {
		// cancel default IE tab handler
		//window.event.returnValue = false;

		stopPropagation(element);
		// trigger blur event - stop editing
		element.blur();
		return false;

		// open editing for previous field?
	}

	// was 'escape' pressed?
	if (iKey == 27) {
		//text to original value
		element.value = preEditText;

		stopPropagation(element);
		// trigger blur event - stop editing
		element.blur();
	}
}


function addElement(element, addName, above) {
	// get calling menu and test for problems
	var menuNode = element.parentNode.parentNode.parentNode;
	if (menuNode.tagName.toLowerCase() != "span") {
		alert("Unexpected HTML object encountered. Expected SPAN, found " + menuNode.tagName);
		return;
	}
	
	// get parent folder div and check for problems
	var folderNode = menuNode.parentNode.parentNode;
	if (folderNode.tagName.toLowerCase() != "div") {
		alert("Unexpected HTML object encountered. Expected DIV, found " + folderNode.tagName);
		return;
	}
	
	// create new element
	addElementOrSection(addName, above);
	
	stopPropagation(element);
}

function addSection(element, addName, above) {
	// get calling menu and test for problems
	var menuNode = element.parentNode.parentNode.parentNode;
	if (menuNode.tagName.toLowerCase() != "span") {
		alert("Unexpected HTML object encountered. Expected SPAN, found " + menuNode.tagName);
		return;
	}

	// get parent folder div and check for problems
	var folderNode = menuNode.parentNode;
	if (folderNode.tagName.toLowerCase() != "div") {
		alert("Unexpected HTML object encountered. Expected DIV, found " + folderNode.tagName);
		return;
	}

	// create new section
	addElementOrSection(addName, above);
	
	stopPropagation(element);
}

// Description:
//   Routine called by AddSection and AddChild code to create
//   a new section (compound element) using an XSL transformation
//   of a small chunk of XML code.
function addElementOrSection(path, above) {
	debug("add");
	debug("path: " + path);
	
	var targetPath = getTargetPath(path);
	
	var toBeDuplicatedNode = findNode(targetPath);
	
	var newNode;
	if ("createNode" in xmlDoc) { // IE
		//debug("IE (createNode exists)");
		newNode = xmlDoc.createNode(Node.ELEMENT_NODE, toBeDuplicatedNode.nodeName, "http://www.isotc211.org/2005/gmd");
	}
	else { // W3C (Firefox, Opera, etc.)
		//debug("W3C (createNode doesn't exist)");
		newNode = xmlDoc.createElementNS("http://www.isotc211.org/2005/gmd", toBeDuplicatedNode.nodeName);
	}

	if (above)
		toBeDuplicatedNode.parentNode.insertBefore(newNode, toBeDuplicatedNode);
	else // werkt ook als nextSibling null is (dan valt de DOM terug op appendChild)
		toBeDuplicatedNode.parentNode.insertBefore(newNode, toBeDuplicatedNode.nextSibling);
	
	//debug("xmldoc na toevoeging nieuwe element: ");
	//debugXmlDoc(xmlDoc);
	
	// preprocess again to get all the ancestors to appear in the xmlDoc backend
	var xmlDocString = preprocessor.transformToString(xmlDoc);
	
	// put in backend var again. Compatible with both IE and FF
	xmlDoc.loadXML(xmlDocString);
	
	//debug("xmldoc nadat preprocessor zijn werk heeft gedaan: ");
	//debugXmlDoc(xmlDoc);
	
	// create entirely new xhtml representation of xmlDoc and add it to the current page
	xmlTransformer.transformAndAppend(xmlDoc, "write-root");
	
	// page changed value
	changeFlag(true);
}

function deleteElement(element, elementPath) {
	// get calling menu and test for problems
	var menuNode = element.parentNode.parentNode.parentNode;
	if (menuNode.tagName.toLowerCase() != "span") {
		alert("Unexpected HTML object encountered. Expected SPAN, found " + menuNode.tagName);
		return;
	}

	// get calling menu and test for problems
	var menuNode = element.parentNode.parentNode.parentNode;
	if (menuNode.tagName.toLowerCase() != "span") {
		alert("Unexpected HTML object encountered. Expected SPAN, found " + menuNode.tagName);
		return;
	}

	// get parent folder div and check for problems
	var folderNode = menuNode.parentNode.parentNode;
	if (folderNode.tagName.toLowerCase() != "p") {
		alert("Unexpected HTML object encountered. Expected P, found " + folderNode.tagName);
		return;
	}
	
	deleteElementOrSection(element, folderNode, elementPath, NOT_ALLOWED_DELETE_ELEMENT_TEXT, CONFIRM_DELETE_ELEMENT_TEXT);
	
	stopPropagation(element);
}

// Description:
//   Delete section (node tree).
//
// Arguments:
//   element = the input button calling routine, used to get reference to
//             other objects to delete
function deleteSection(element, sectionPath) {
	// get calling menu and test for problems
	var menuNode = element.parentNode.parentNode.parentNode;
	if (menuNode.tagName.toLowerCase() != "span") {
		alert("Unexpected HTML object encountered. Expected SPAN, found " + menuNode.tagName);
		return;
	}

	// get calling menu and test for problems
	var menuNode = element.parentNode.parentNode.parentNode;
	if (menuNode.tagName.toLowerCase() != "span") {
		alert("Unexpected HTML object encountered. Expected SPAN, found " + menuNode.tagName);
		return;
	}

	// get parent folder div and check for problems
	var folderNode = menuNode.parentNode;
	if (folderNode.tagName.toLowerCase() != "div") {
		alert("Unexpected HTML object encountered. Expected DIV, found " + folderNode.tagName);
		return;
	}

	deleteElementOrSection(element, folderNode, sectionPath, NOT_ALLOWED_DELETE_SECTION_TEXT, CONFIRM_DELETE_SECTION_TEXT);
	
	stopPropagation(element);
}

function deleteElementOrSection(element, folderNode, path, notAllowedDeleteText, confirmDeleteText) {
	debug("delete");
	debug("path: " + path);
	
	var targetPath = getTargetPath(path);
	
	// find section in backend
	var toBeDeletedNode = findNode(targetPath);
	
	debug("toBeDeletedNode.xml: " + toBeDeletedNode.xml);
	
	// get nr of same nodes in backend
	var nrOfSameNodes = 0;
	for (var i = 0; i < toBeDeletedNode.parentNode.childNodes.length; i++) {
		var sibling = toBeDeletedNode.parentNode.childNodes[i];
		if (sibling.nodeType == Node.ELEMENT_NODE && sibling.nodeName == toBeDeletedNode.nodeName)
			nrOfSameNodes++;
	}
	
	//debug("nrOfSameSections: " + nrOfSameSections);
	
	if (nrOfSameNodes < 2) {
		alert(notAllowedDeleteText);
		return;
	}
	
	// confirm delete
	var returnKey = confirm(confirmDeleteText);
	if (returnKey == 7) {
		return;
	}

	// delete section from xml backend
	toBeDeletedNode.parentNode.removeChild(toBeDeletedNode);
	
	// create entirely new xhtml representation of xmlDoc and add it to the current page (must be done to get sequence of duplicated nodes right)
	xmlTransformer.transformAndAppend(xmlDoc, "write-root");

	//page changed value
	changeFlag(true);	
}

// Returns the node that must be deleted from the full path. This path is an XPath-like path. See comment below.
// Or the node where a sibling will be added above or below.
function getTargetPath(path) {
	// path MUST have only one nodeName with a indexNr attached to it (e.g. /nodeNameA/nodeNameB[99]/nodeNameC)
	// if not, the parent of the last node will be used for duplication
	var targetPath;
	//debug("strAddName.lastIndexOf(\"]\"): " + strAddName.lastIndexOf("]"));
	//debug("strAddName.lastIndexOf(\"]\", strAddName.lastIndexOf(\"]\") - 1): " + strAddName.lastIndexOf("]", strAddName.lastIndexOf("]") - 1) );
	if (path.lastIndexOf("]", path.lastIndexOf("]") - 1) < 0)
		targetPath = path.substring(0, path.lastIndexOf("]") + 1);
	else
		targetPath = path.substring(0, path.lastIndexOf("/"));
	
	debug("targetPath: " + targetPath);
	
	return targetPath;
}