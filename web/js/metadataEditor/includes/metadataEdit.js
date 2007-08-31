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
		targetNode = getChildNode(xmlDoc, targetNode, pathArray[i]);
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

function getChildNode(root, parent, rawChildTag) {
	if (parent == null)
		parent = root;
	
	var childAndIndex = rawChildTag.split(/[\[\]]+/);
	var childTag = childAndIndex[0];
	if (childAndIndex.length > 1)
		var childIndex = childAndIndex[1];
	else
		var childIndex = 1;
		
	var children = parent.childNodes;
	if (children == null) {
		return null;
	}
	
	var child;
	var correctChildCount = 0;
	for (var i = 0; i < children.length; i++) {
		child = children[i];
		if (child.nodeType == 1 && child.tagName == childTag) {
			correctChildCount++;
			if (correctChildCount == childIndex) {
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