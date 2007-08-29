function XML() { }


/**
* This XML.Transformer class encapsulates an XSL stylesheet.
* If the stylesheet parameter is a URL, we load it.
* Otherwise, we assume it is an appropriate DOM Document.
*/
XML.Transformer = function(stylesheet) {
	// Load the stylesheet if necessary.
	this.stylesheet = stylesheet;
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

XML.Transformer.prototype.transformNodeSet = function(node) {
	if ("transformToFragment" in this.processor) {
		return this.processor.transformToFragment(node, document);
	}
	else if ("transform" in this.processor) {
		this.processor.input = node;
		this.processor.transform();		
		return this.processor.output;
	}
	else if ("transformNode" in node) {
		return node.transformNode(this.stylesheet);
	}
	else {
		throw "XSLT is not supported in this browser";
	}
}

XML.Transformer.prototype.transform = function(node, element) {
	// If element is specified by id, look it up.
	if (typeof element == "string") {
		element = document.getElementById(element);
	}
	if (element.appendChild) {
		element.innerHTML = "";
		element.appendChild(this.transformNodeSet(node));
	}
	else {
		element.innerHTML = this.transformNodeSet(node);
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


XML.transformNodeSet = function(xmldoc, stylesheet) {
	var transformer = new XML.Transformer(stylesheet);
	return transformer.transformNodeSet(xmldoc);
}

XML.transform = function(xmldoc, stylesheet, element) {
	var transformer = new XML.Transformer(stylesheet);
	transformer.transform(xmldoc, element);
}
