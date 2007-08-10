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
	metadataXML = metadataXML.unescapeHTML();

	xmlDoc = jsXML.createDOMDocument();
	xmlDoc.async = false;
	xmlDoc.loadXML(metadataXML);

	var freeThreadedIfPossible = true;
	var xslDoc = jsXML.createDOMDocument(freeThreadedIfPossible);
	xslDoc.async = false;
	xslDoc.load(xslFullPath);	
	
	var xmlTransformer = new XML.Transformer(xslDoc);
	xmlTransformer.setParameter("basePath", basePath);
	xmlTransformer.transform(xmlDoc, "writeroot");
	//XML.transform(xmlDoc, xslDoc, "writeroot");
	
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
		
		debug("Saved changes in xml dom succesfully.");
	}
	else {
		alert("Save path in XML document not found. Changes are not saved.");
	}
}

function getChildNode(root, parent, childTag) {
	if (parent == null)
		parent = root;
		
	var children = parent.childNodes;
	if (children == null) {
		return null;
	}
	
	var child;
	for (var i = 0; i < children.length; i++) {
		child = children[i];
		if (child.nodeType == 1 && child.tagName == childTag) {
			//debug("child: " + child.nodeName);
			return child;
		}
	}
	
	//debug("goede child niet gevonden bij parent: " + parent.nodeName);
	return null;
}

function checkForm(source) {
	var sourceName = source.getAttribute("name");
	if (sourceName != "" && sourceName == "save") {
		//document.getElementById("xml").setAttribute("value", xmlDoc.xml);
		var xmlHiddenInput = document.createElement("input");
		xmlHiddenInput.setAttribute("type", "hidden");
		xmlHiddenInput.setAttribute("name", "xml");
		xmlHiddenInput.setAttribute("value", xmlDoc.xml);	
		var form = document.getElementById("metadataForm");
		form.appendChild(xmlHiddenInput);
		form.submit();
	}
}