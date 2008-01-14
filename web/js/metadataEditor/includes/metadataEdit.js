init();

function init() {
	addLoadEvent(initWithXmlString);
}

function addLoadEvent(func) {
	var oldonload = window.onload;
	if (typeof window.onload != 'function') {
		window.onload = func;
	}
	else {
		window.onload = function() {
			if (oldonload) {
				oldonload();
			}
			func();
		}
	}
}

function initWithXmlString() {
	//debug("baseURL: " + baseURL);
	
	// if no metadata is present we start the editor with all elements empty
	if (metadataXML == "undefined" || metadataXML == null || trim(metadataXML) == "") {
		metadataXML = basicMetadataXML;
	}
		
	metadataXML = metadataXML.unescapeHTML();
	//debug(metadataXML);
	
	var rawXmlDoc = jsXML.createDOMDocument();
	rawXmlDoc.async = false;
	rawXmlDoc.loadXML(metadataXML);
	
	//debug("Raw:");
	//debug(rawXmlDoc.xml);

	//var freeThreadedIfPossible = true;
	var ppXslDoc = jsXML.createDOMDocument(true);
	ppXslDoc.async = false;
	ppXslDoc.load(preprocessorXslFullPath);
	//debug("preprocessorXslFullPath: " + preprocessorXslFullPath);
	
	//debug("Preprocessor:");
	//debug(ppXslDoc.xml);
	
	//var rawPreprocessedXML = XML.transformToString(rawXmlDoc, ppXslDoc);
	// Global var. Also used by create section.
	preprocessor = new XML.Transformer(ppXslDoc);
	var rawPreprocessedXML = preprocessor.transformToString(rawXmlDoc);
	
	// Global var. Backend xml-document.
	xmlDoc = jsXML.createDOMDocument();
	xmlDoc.async = false;
	xmlDoc.loadXML(rawPreprocessedXML);
	
	debug("xmlDoc:");
	//debug(xmlDoc.xml);
	debugXmlDoc(xmlDoc);

	//var freeThreadedIfPossible = true;
	var xslDoc = jsXML.createDOMDocument(true);
	xslDoc.async = false;
	xslDoc.load(mainXslFullPath);
	
	//debug("Xsl:");
	//debug(xslDoc.xml);
	
	// Global var. Also used by create section.
	xmlTransformer = new XML.Transformer(xslDoc);
	xmlTransformer.setParameter("basePath", baseFullPath);
	xmlTransformer.transformAndAppend(xmlDoc, "write-root");
}

// parameters: 
//				- path: is a XPath-path represented as a string; must start at the root
//				- newValue: text value to saved at the node in the path
function saveChangesInXMLDom(newValue, path) {
	//debug("saveChangesInXMLDom");
	//debug("root tag: " + xmlDoc.nodeName);
	
	targetNode = findNode(path);

	if (targetNode != null) {
		while (targetNode.hasChildNodes()) {
			targetNode.removeChild(targetNode.firstChild);
		}
		
		var textNode = xmlDoc.createTextNode(newValue);
		targetNode.appendChild(textNode);
		
		//debug("Saved changes in xml dom succesfully.");
	}
	else {
		alert("Save path in XML document not found. Changes will not be saved.");
	}
}

// parameters: 
//				- path: is a XPath-path represented as a string; must start at the root
function findNode(path) {
	var pathArray = path.split("/");
	
	var targetNode = null;
	for (var i = 0; i < pathArray.length; i++) {
		targetNode = findChildNode(targetNode, pathArray[i]);
	}
	
	return targetNode;
}

function findChildNode(searchParent, targetRawChildTag) {
	if (targetRawChildTag == null || targetRawChildTag == "") {
		//debug("empty tagName");
		return null;
	}
	
	if (searchParent == null) {
		searchParent = xmlDoc;
	}
	
	//debug("searchParent: " + searchParent.nodeName);
	//debug("targetRawChildTag: " + targetRawChildTag);
	
	var splitQname = xpathQnameToArray(targetRawChildTag);
	if (splitQname == null)
		return null;
	
	//debug("prefix: " + splitQname[0]);
	//debug("name: " + splitQname[1]);
	//debug("index: " + splitQname[2]);	
	
	var searchChildren = searchParent.childNodes;
	if (searchChildren == null || searchChildren.length == 0) {
		debug("Childtree empty.");
		return null;
	}
	
	var searchChildNodeName, searchChildNode;
	var searchChildSplit;
	var correctChildCount = 0;
	for (var i = 0; i < searchChildren.length; i++) {
		searchChildNode = searchChildren[i];
		searchChildSplit = searchChildNode.nodeName.split(/[:]+/);
		if (searchChildSplit.length == 2) {
			searchChildNodeName = searchChildSplit[1];
		}
		else { // searchChildSplit.length == 1
			searchChildNodeName = searchChildSplit[0];
		}
		//debug("searchChildNodeName: " + searchChildNodeName);		
		//debug("searchChildSplit.length: " + searchChildSplit.length);
		//debug("searchChild.nodeType: " + searchChild.nodeType);
		//debug("searchChildNode.nodeName: " + searchChildNode.nodeName);
		//debug("targetChildTag: " + splitQname[1]);		
		if (searchChildNode.nodeType == 1 && searchChildNodeName == splitQname[1]) {
			// Xpath begint met tellen bij 1, dus eerst deze variable ophogen.
			correctChildCount++;
			if (correctChildCount == splitQname[2]) {
				//debug("correct child: " + searchChildNodeName);
				return searchChildNode;
			}
		}
	}
	
	debug("goede child niet gevonden bij parent: " + parent.nodeName);
	return null;
}

// E.g.: 
// prefix:tagName[3]
// to
// array:
// [0] == prefix or empty if not present
// [1] == local-name without prefix
// [2] == indexNr or 1 if not present
function xpathQnameToArray(qname) {
	var array = [];
	
	// defaults
	array[0] = "";
	array[1] = "";
	array[2] = 1;

	// split qname on '[' and ']'. For example "prefix:tagName[3]"
	var targetChildAndIndexUnfiltered = qname.split(/[\[\]]+/);
	var targetChildAndIndex = removeEmptyStringValuesFromArray(targetChildAndIndexUnfiltered);
	
	var targetRawPrefixAndChildTag;
	var targetChildIndex;
	
	if (targetChildAndIndex.length == 2) { // wel indexNr
		targetRawPrefixAndChildTag = targetChildAndIndex[0];
		array[2] = targetChildAndIndex[1];
	}
	else if (targetChildAndIndex.length == 1) { // geen indexNr. Standaard is 1
		targetRawPrefixAndChildTag = targetChildAndIndex[0];
        array[2] = 1;
	}
	else {
		debug("Incorrect tag name.");
		return null;
	}

	// split rawPrefixAndChildTag on ':'. For example "prefix:tagName"
	var targetPrefixAndChildTagUnfiltered = targetRawPrefixAndChildTag.split(/[:]+/);
	var targetPrefixAndChildTag = removeEmptyStringValuesFromArray(targetPrefixAndChildTagUnfiltered);

	var targetChildTag;
	var prefix;
	
	if (targetPrefixAndChildTag.length == 2) { // wel prefix
		array[0] = targetPrefixAndChildTag[0];
		array[1] = targetPrefixAndChildTag[1];
	}
	else if (targetPrefixAndChildTag.length == 1) { // geen prefix
		array[1] = targetPrefixAndChildTag[0];
	}
	else {
		debug("Incorrect tag name.");
		return null;
	}
	
	return array;
}

function removeEmptyStringValuesFromArray(array) {
	// filter out empty groups
	var newArray = [];
	var newIndex = 0;
	for (var i = 0; i < array.length; i++) {
		if (array[i] != "") {
			newArray[newIndex] = array[i];
			newIndex++;
		}
	}
	
	for (var i = 0; i < newArray.length; i++) {
		//debug(i + ": " + newArray[i]);
	}
	
	return newArray;
}

function checkForm(source) {
	var sourceName = source.getAttribute("name");
	if (sourceName != null && sourceName == "save") {
		addDateStampToXMLDom();
		//document.getElementById("xml").setAttribute("value", xmlDoc.xml);
		var xmlHiddenInput = document.createElement("input");
		xmlHiddenInput.setAttribute("type", "hidden");
		xmlHiddenInput.setAttribute("value", xmlDoc.xml.escapeHTML());	
		xmlHiddenInput.setAttribute("name", "metadata");
		var form = document.getElementById("metadataForm");
		form.appendChild(xmlHiddenInput);
		form.submit();
		//self.close();
	}
}

function addDateStampToXMLDom() {
	var currentTime = new Date();
	var month = currentTime.getMonth() + 1;
	var day = currentTime.getDate();
	var year = currentTime.getFullYear();
	
	var currentDate = day + "-" + month + "-" + year;
	var dateStampPath = "/MD_Metadata[1]/gmd:dateStamp[1]/gco:Date[1]";
	
	saveChangesInXMLDom(currentDate, dateStampPath);
}