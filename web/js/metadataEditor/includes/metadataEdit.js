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
	debug(metadataXML);
	
	rawXmlDoc = jsXML.createDOMDocument();
	rawXmlDoc.async = false;
	rawXmlDoc.loadXML(metadataXML);
	
	debug("Raw:");
	debug(rawXmlDoc.xml);

	//var freeThreadedIfPossible = true;
	var ppXslDoc = jsXML.createDOMDocument(true);
	ppXslDoc.async = false;
	ppXslDoc.load(preprocessorFullPath);
	//debug("preprocessorFullPath: " + preprocessorFullPath);
	
	debug("Preprocessor:");
	debug(ppXslDoc.xml);
	
	var rawPreprocessedXML = XML.transformToString(rawXmlDoc, ppXslDoc);
	
	debug("Preprocessed:");
	debug(rawPreprocessedXML);
	
	xmlDoc = jsXML.createDOMDocument();
	xmlDoc.async = false;
	xmlDoc.loadXML(rawPreprocessedXML);
	
	//var freeThreadedIfPossible = true;
	var xslDoc = jsXML.createDOMDocument(true);
	xslDoc.async = false;
	xslDoc.load(xslFullPath);
	
	debug("Xsl:");
	debug(xslDoc.xml);
	
	var xmlTransformer = new XML.Transformer(xslDoc);
	xmlTransformer.setParameter("basePath", basePath);
	xmlTransformer.transformAndAppend(xmlDoc, "write-root");
	
	xmlDocInit();
}

function saveChangesInXMLDom(newValue, path) {
	//debug("saveChangesInXMLDom");

	var pathArray = path.split("/");
	var targetNode;
	for (var i = 0; i < pathArray.length; i++) {
		targetNode = findChildNode(xmlDoc, targetNode, pathArray[i]);
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

function findChildNode(root, searchParent, targetRawChildTag) {
	if (searchParent == null)
		searchParent = root;
	
	// split rawChildTag on ':', '[' and ']'. For example "prefix:tagName[3]"
	var targetChildAndIndex = targetRawChildTag.split(/[\[\]:]+/);
	for (var i = 0; i < targetChildAndIndex.length; i++) {
		debug(targetChildAndIndex[i]);
	}
	
	var targetChildTag, targetChildIndex;
	if (targetChildAndIndex.length == 3) {
		targetChildTag = targetChildAndIndex[1];
		targetChildIndex = targetChildAndIndex[2];
	}
	else if (targetChildAndIndex.length == 2) {
		targetChildTag = targetChildAndIndex[0];
		targetChildIndex = targetChildAndIndex[1];
	}
	else { // == 1
		targetChildTag = targetChildAndIndex[0];		
	}
	
	var searchChildren = searchParent.childNodes;
	if (searchChildren == null || searchChildren.length == 0) {
		debug("tree empty?");
		return null;
	}
	
	var searchChild;
	var correctChildCount = 0;
	for (var i = 0; i < searchChildren.length; i++) {
		searchChild = searchChildren[i];
		debug("searchChild: "+searchChild.tagName);
		if (searchChild.nodeType == 1 && searchChild.tagName.toLowerCase() == targetChildTag.toLowerCase()) {
			correctChildCount++;
			if (correctChildCount == targetChildIndex) {
				debug("child: " + searchChild.nodeName);
				return searchChild;
			}
		}
	}
	
	//debug("goede child niet gevonden bij parent: " + parent.nodeName);
	return null;
}

function checkForm(source) {
	var sourceName = source.getAttribute("name");
	if (sourceName != null && sourceName == "save") {
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