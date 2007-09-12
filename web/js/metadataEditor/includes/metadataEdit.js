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
	
	if (targetChildAndIndex.length < 3)
		debug("length of rawChildTag < 3");
	var targetChildTag = targetChildAndIndex[1];
	
	if (targetChildAndIndex.length > 2)
		var targetChildIndex = targetChildAndIndex[2];
	else
		var targetChildIndex = 1;
		
	var searchChildren = searchParent.childNodes;
	if (searchChildren == null) {
		return null;
	}
	
	var searchChild;
	var correctChildCount = 0;
	for (var i = 0; i < searchChildren.length; i++) {
		searchChild = searchChildren[i];
		if (searchChild.nodeType == 1 && searchChild.tagName == targetChildTag) {
			correctChildCount++;
			if (correctChildCount == targetChildIndex) {
				//debug("child: " + child.nodeName);
				return child;
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