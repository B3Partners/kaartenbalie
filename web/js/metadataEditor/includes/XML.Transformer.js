function XML() { }


/**
* This XML.Transformer class encapsulates an XSL stylesheet.
* If the stylesheet parameter is a URL, we load it.
* Otherwise, we assume it is an appropriate DOM Document.
*/
XML.Transformer = function(stylesheet) {
	// Load the stylesheet if necessary.
	//if (typeof stylesheet == "string") stylesheet = XML.load(stylesheet);
	this.stylesheet = stylesheet;
	// In Mozilla-based browsers, create an XSLTProcessor object and
	// tell it about the stylesheet.
	if (typeof XSLTProcessor != "undefined") {
		this.processor = new XSLTProcessor();
		this.processor.importStylesheet(this.stylesheet);
	}
	else if (typeof ActiveXObject != "undefined") { //!!window.ActiveX
		var xslt = new ActiveXObject("Msxml2.XSLTemplate.3.0");
		xslt.stylesheet = stylesheet;
		
		this.processor = xslt.createProcessor();
	}
	else {
		alert("No suitable XSLT Processor found for your browser. Please use either IE or Firefox.");
	}
};


/**
* This is the transform() method of the XML.Transformer class.
* It transforms the specified xml node using the encapsulated stylesheet.
* The results of the transformation are assumed to be HTML and are used to
* replace the content of the specified element.
*/
XML.Transformer.prototype.transform = function(node, element) {
	// If element is specified by id, look it up.
	if (typeof element == "string") {
		//debug("voor: " + element);
		element = document.getElementById(element);
		//debug("na: " + element);
	}
	//debug(element);
	if ("transformToFragment" in this.processor) {
		// If we've created an XSLTProcessor (i.e., we're in Mozilla) use it.
		// Transform the node into a DOM DocumentFragment.
		var fragment = this.processor.transformToFragment(node, document);
		//debug(fragment.xml);
		// Erase the existing content of element.
		element.innerHTML = "";
		// And insert the transformed nodes.
		element.appendChild(fragment);
	}
	else if ("transform" in this.processor) {
		debug("ie met parameters");
		this.processor.input = node;
		this.processor.transform();		
		element.innerHTML = node.transformNode(this.stylesheet);			
	}
	else if ("transformNode" in node) {
		debug("ie zonder parameters");		
		// If the node has a transformNode() function (in IE), use that.
		// Note that transformNode() returns a string.
		//debug(node);
		fragmentText = node.transformNode(this.stylesheet);
		//debug(fragmentText);
		element.innerHTML = fragmentText;
	}
	else {
		// Otherwise, we're out of luck.
		throw "XSLT is not supported in this browser";
	}
};

XML.Transformer.prototype.setParameter = function(key, value) {
	if ("setParameter" in this.processor) {
		this.processor.setParameter(null, key, value);
	}
	else if ("addParameter" in this.processor) {
		this.processor.addParameter(key, value);
	}
};


/**
* This is an XSLT utility function that is useful when a stylesheet is
* used only once.
*/
XML.transform = function(xmldoc, stylesheet, element) {
	var transformer = new XML.Transformer(stylesheet);
	transformer.transform(xmldoc, element);
}
