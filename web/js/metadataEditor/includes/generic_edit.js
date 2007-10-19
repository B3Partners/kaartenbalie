// 3/8/07 Erik van de Pol

// Rewritten variant of "generic_dhtml.vbs" by Eric Compas (2/3/05).
// Rewritten in javascript and working in most W3C compliant browsers.


// ==============
// Global Options
// ==============

// text box size (larger than max will create TEXTAREA also
// sets width of TEXTAREA)
var MIN_TEXTINPUT_SIZE = 50;
var MAX_TEXTINPUT_SIZE = 75;

// Tekst constanten vertaald naar het Nederlands
// default text for elements with no default value specified 
var GLOBAL_DEFAULT = "Klik hier om deze tekst te bewerken.";

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
var MENU_TOOLTIP = "Klik hier om opties weer te geven.";

// message titles
var ERROR_TITLE = "Fout";

// default element value (if user leaves blank)
var DEFAULT_VALUE = "Standaard waarde.";

// global variables
var preEditText; // text held by SPAN before editing

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
	
	// already editing?
	if (element.tagName.toLowerCase() == "input" || element.tagName.toLowerCase() == "textarea" || element.tagName.toLowerCase() == "select")
		return;
	
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
}


// Description:
//   Change text input or area element value back into displayed text.
//   Assumes that text to be edited is in particular format.
//
// Argument:
//   element = the <span> element containing text to be edited.
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
}

function saveValueOnClientSide(parentNode, newValue) {
	parentNode.className = "changed-value";
	changeFlag(true);
	//debug("parentNode.attributes.getNamedItem(\"fullPath\").nodeValue: " + parentNode.attributes.getNamedItem("fullPath").nodeValue);		
	saveChangesInXMLDom(newValue, parentNode.attributes.getNamedItem("fullPath").nodeValue);
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
function getPicklist(name) {
	// copy picklist source
	var picklist = document.getElementById(name);

	if (picklist != null)
		return picklist.cloneNode(true);
	else
		return null;
}

// 2/05 Eric Compas
//
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
}

function destroyPickList(event) {
	var element = getTarget(event);
	var parentNode = element.parentNode;
	parentNode.innerHTML = "";
	parentNode.appendChild(document.createTextNode(preEditText));
}

// 2/05 Eric Compas;
// Catch "tab" key press when picklist is open (will leave editing field open)
function pickListKeyPress(element) {
	var iKey = getKeyCode(element);
	//alert("Key pressed = " + iKey);

	// was 'tab' or 'escape' pressed?
	if (iKey == 9 || iKey == 27) {
		// cancel default IE tab handler
		//window.event.returnValue = false;
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


// 12/30/2004 Eric Compas
//
// Description:
//   Add a new section (node tree) using existing one as template
//
// Arguments:
//  element = the calling element, the anchor tag in popup menu
//   addName = the name of the section to add, e.g. "theme"
//   bAbove = whether to add new element above (true) or below (false)
function addSection(element, addName, above) {
	// create new section
	var newContentNode = createSection(addName);
	if (newContentNode == null) {
		alert("Error creating new compound element.");
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

	// add new DIV to document (either before or after current element's 'folder' div)
	var tabNode = folderNode.parentNode;
	if (above)
		tabNode.insertBefore(newContentNode, folderNode);
	else // werkt ook als nextSibling null is (dan valt de DOM terug op appendChild)
		tabNode.insertBefore(newContentNode, folderNode.nextSibling);
}

// 12/30/2004 Eric Compas
//
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

	// check to make sure not last folder DIV of same type
	var section;
	var i = 0;
	for (var childIndex in folderNode.parentNode.childNodes) {
		section = folderNode.parentNode.childNodes[childIndex];
		if (section.nodeType == Node.ELEMENT_NODE)
			if (section.getAttribute("section-path") == sectionPath)
				i++;
	}
	
	//alert("i after: " + i);
	
	if (i < 2) {
		alert("Deleting the last section is not allowed.");
		return;
	}

	// confirm delete
	var returnKey = confirm("Are you sure you want to delete this section?");
	if (returnKey == 7) {
		return;
	}

	// delete section
	folderNode.parentNode.removeChild(folderNode);

	//page changed value
	changeFlag(true);
}

// Description:
//   Add a child section (compound element) below the current
//   parent. Similar to AddSection code except adding as
//   child instead of sibling.
function addChild(element, addName) {
	// create new section
	var newContentNode = createSection(addName);
	if (newContentNode == null) {
		alert("Error creating new compound element.");
		return;
	}

	// get calling menu and test for problems
	var menuNode = element.parentNode.parentNode.parentNode;
	if (menuNode.tagName.toLowerCase() != "span") {
		alert("Unexpected HTML object encountered. Expected SPAN, found " + menuNode.tagName);
		return;
	}

	// get section content div and check for problems
	var folderNode = menuNode.nextSibling;
	if (folderNode.tagName.toLowerCase() != "div") {
		alert("Unexpected HTML object encountered. Expected DIV, found " + folderNode.tagName);
		return;
	}
	
	folderNode.appendChild(newContentNode);
}

// 2/21/2005 Eric Compas
//
// Description:
//   Routine called by AddSection and AddChild code to create
//   a new section (compound element) using an XSL transformation
//   of a small chunk of XML code.
function createSection(strAddName) {
	debug("strAddName=" + strAddName);

	// create blank XML document
	var tempXmlDoc = jsXML.createDOMDocument();
	tempXmlDoc.async = false;
	tempXmlDoc.loadXML("<MD_Metadata xmlns='http://www.isotc211.org/2005/gmd'/>");

	// add blank parent elements (if they exist)
	// (needed if "full path" transformations used)
	var sParentNodes, sNode;
	if (strAddName.lastIndexOf("/") > -1) {
		debug("addSection: found parents");
		var sNodes, pNode, newNode;
		sParentNodes = strAddName.substring(0, strAddName.lastIndexOf("/"));
		if (sParentNodes.indexOf("/") == 0)
			sParentNodes = sParentNodes.substring(1);
		debug("addSection: parent nodes = " + sParentNodes);
		pNode = tempXmlDoc.documentElement;
		sNodes = sParentNodes;
		while (sNodes != "") {
			if (sNodes.indexOf("/") > -1) {
				// get next parent
				sNode = sNodes.substring(0, sNodes.indexOf("/"));
				sNodes = sNodes.substring(sNodes.indexOf("/") + 1);
			}
			else {
				// last parent
				sNode = sNodes;
				sNodes = "";
			}
			sNode = sNode.substring(sNode.lastIndexOf("gmd:") + 4);
			debug("addSection: sNode = " + sNode + ", sNodes = " + sNodes);
			if (sNode !== "MD_Metadata") {
				newNode = tempXmlDoc.createElement(sNode);
				pNode.appendChild(newNode);
				pNode = newNode;
			}
		}
		
		sNode = strAddName.substring(strAddName.lastIndexOf("/gmd:") + 5);
	}
	else {
		sParentNodes = "";
		sNode = strAddName;
		debug("addSection:parent not found " + strAddName);
	}

	debug("tempXmlDoc = " + tempXmlDoc.xml);
	debug("sParentNodes = " + sParentNodes + ", sNode = " + sNode);
	
	// create stylesheet to transform, or "preprocess" it;
	// this will add the appropriate blank XML elements;
	var strXSL = "<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform' xmlns:gmd='http://www.isotc211.org/2005/gmd' exclude-result-prefixes='gmd'>" +  "<xsl:output method='xml' indent='yes'/>" +  "<xsl:include href='" + baseURL + preprocessorTemplatesXslFullPath + "'/>" ;	
	strXSL += "<xsl:template match='" + sParentNodes + "'>";		
	strXSL += "<xsl:copy><xsl:call-template name='add-" + sNode + "' /></xsl:copy>" +  "</xsl:template><xsl:template match='@*|node()'><xsl:copy>" +  "<xsl:apply-templates select='@*|node()' />" +  "</xsl:copy></xsl:template></xsl:stylesheet>" ;

	//debug("strXSL "+ strXSL);

	preXSLDoc = jsXML.createDOMDocument(true);
	preXSLDoc.async = false;
	preXSLDoc.loadXML(strXSL);
	
	debug("preXSLDoc.xml = " + preXSLDoc.xml);
	// transform or "preprocess" it
	
	var strPreXML = XML.transformToString(tempXmlDoc, preXSLDoc);
	debug("strPreXML: " + strPreXML);
	
	var preXMLDoc = jsXML.createDOMDocument();
	preXMLDoc.async = false;
	preXMLDoc.loadXML(strPreXML);
	
	debug("preXMLDoc = "+ preXMLDoc.xml);

	var addElementsXslDoc = jsXML.createDOMDocument(true);
	addElementsXslDoc.async = false;
	addElementsXslDoc.load(addElementsXslFullPath);

	debug("addElementsXslDoc = " + addElementsXslDoc.xml);

	var addElementsXmlTransformer = new XML.Transformer(addElementsXslDoc);
	addElementsXmlTransformer.setParameter("basePath", baseFullPath);
	var strHTML = addElementsXmlTransformer.transformToString(preXMLDoc);
	
	debug("strHTML = " + strHTML);
	
	var resultAsXML = jsXML.createDOMDocument();
	resultAsXML.async = false;
	resultAsXML.loadXML(strHTML);
	
	//debug("resultAsXML = " + resultAsXML.xml);
	
	var section = findSectionDiv(resultAsXML);
	
	debug("section: " + section.xml);
	
	return section;
}

function findSectionDiv(parent) {
	debug("parent.nodeName: " + parent.nodeName);	
	for (var childIndex in parent.childNodes) {
		var child = parent.childNodes[childIndex];
		debug("child.nodeName: " + child.nodeName);
		if (child.nodeType == Node.ELEMENT_NODE && child.nodeName.toLowerCase() == "div") {
			var firstSectionFound = findSectionDivInAttributes(child);
			if (firstSectionFound) {
				return child;
			}
			else {
				var foundSectionDiv = findSectionDiv(child);
				if (foundSectionDiv != null)
					return foundSectionDiv;
			}
		}
	}
	return null;
}

function findSectionDivInAttributes(child) {
	for (var attributeIndex in child.attributes) {
		var attribute = child.attributes[attributeIndex];
		debug("attribute.nodeName: " + attribute.nodeName);			
		debug("attribute.nodeValue: " + attribute.nodeValue);						
		if (attribute.nodeName == "class" && attribute.nodeValue == "section") {
			return true;
		}
	}
	return false;
}

