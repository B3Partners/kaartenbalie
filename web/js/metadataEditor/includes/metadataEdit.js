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
	ppXslDoc.load(preprocessorFullPath);
	//debug("preprocessorFullPath: " + preprocessorFullPath);
	
	//debug("Preprocessor:");
	//debug(ppXslDoc.xml);
	
	var rawPreprocessedXML = XML.transformToString(rawXmlDoc, ppXslDoc);
	
	//debug("Preprocessed:");
	//debug(rawPreprocessedXML);
	
	xmlDoc = jsXML.createDOMDocument();
	xmlDoc.async = false;
	xmlDoc.loadXML(rawPreprocessedXML);
	
	//var freeThreadedIfPossible = true;
	var xslDoc = jsXML.createDOMDocument(true);
	xslDoc.async = false;
	xslDoc.load(xslFullPath);
	
	//debug("Xsl:");
	//debug(xslDoc.xml);
	
	xmlTransformer = new XML.Transformer(xslDoc);
	xmlTransformer.setParameter("basePath", basePath);
	xmlTransformer.transformAndAppend(xmlDoc, "write-root");
	
	xmlDocInit();
}

function saveChangesInXMLDom(newValue, path) {
	//debug("saveChangesInXMLDom");
	//debug("root tag: " + xmlDoc.nodeName);
	
	var pathArray = path.split("/");
	
	var targetNode = null;
	for (var i = 0; i < pathArray.length; i++) {
		targetNode = findChildNode(targetNode, pathArray[i]);
	}

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

function findChildNode(searchParent, targetRawChildTag) {
	if (targetRawChildTag == null || targetRawChildTag == "") {
		//debug("empty tagName");
		return null;
	}
	
	if (searchParent == null) {
		searchParent = xmlDoc;
	}
	
	debug("searchParent: " + searchParent.nodeName);
	debug("targetRawChildTag: " + targetRawChildTag);
	
	// split rawChildTag on ':', '[' and ']'. For example "prefix:tagName[3]"
	var targetChildAndIndexUnfiltered = targetRawChildTag.split(/[:\[\]]+/);
	// filter out empty groups
	var targetChildAndIndex = [];
	var newIndex = 0;
	for (var i = 0; i < targetChildAndIndexUnfiltered.length; i++) {
		if (targetChildAndIndexUnfiltered[i] != "") {
			targetChildAndIndex[newIndex] = targetChildAndIndexUnfiltered[i];
			newIndex++;
		}
	}
	
	for (var i = 0; i < targetChildAndIndex.length; i++) {
		debug(i + ": " + targetChildAndIndex[i]);
	}
	
	//debug("targetChildAndIndex.length: " + targetChildAndIndex.length)
	// de xsl transformatie garandeert dat elke tag een volgnr heeft: dus
	// bijvoorbeeld "prefix:tagName[3]" of "tagName[1234]"
	var targetChildTag, targetChildIndex;
	if (targetChildAndIndex.length >= 3) { // met  prefix
		//targetChildTag = targetChildAndIndex[0] + ":" + targetChildAndIndex[1];
		targetChildTag = targetChildAndIndex[1];		
		targetChildIndex = targetChildAndIndex[2];
	}
	else if (targetChildAndIndex.length == 2) { // geen prefix
		targetChildTag = targetChildAndIndex[0];
		targetChildIndex = targetChildAndIndex[1];
	}
	else if (targetChildAndIndex.length == 1) { // eigenlijk overbodig:
		targetChildTag = targetChildAndIndex[0];
        targetChildIndex = 1;
	}
	else {
		debug("Incorrect tag name. Changes will not be saved correctly.");
		return null;
	}
	
	var searchChildren = searchParent.childNodes;
	if (searchChildren == null || searchChildren.length == 0) {
		debug("Childtree empty. Changes will not be saved correctly.");
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
		debug("searchChildNodeName: " + searchChildNodeName);		
		debug("searchChildSplit.length: " + searchChildSplit.length);
		//debug("searchChild.nodeType: " + searchChild.nodeType);
		//debug("searchChildNode.nodeName: " + searchChildNode.nodeName);
		debug("targetChildTag: " + targetChildTag);		
		if (searchChildNode.nodeType == 1 && searchChildNodeName == targetChildTag) {
			// Xpath begint met tellen bij 1, dus eerst deze variable ophogen.
			correctChildCount++;
			if (correctChildCount == targetChildIndex) {
				debug("correct child: " + searchChildNodeName);
				return searchChildNode;
			}
		}
	}
	
	debug("goede child niet gevonden bij parent: " + parent.nodeName);
	return null;
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