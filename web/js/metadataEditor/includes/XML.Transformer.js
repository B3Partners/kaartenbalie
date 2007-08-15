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
	if ("transformToFragment" in this.processor) {
		var fragment = this.processor.transformToFragment(node, document);
		element.innerHTML = "";
		element.appendChild(fragment);
	}
	else if ("transform" in this.processor) {
		this.processor.input = node;
		this.processor.transform();		
		element.innerHTML = this.processor.output;
	}
	/*
	// oude IE methode: 
	else if ("transformNode" in node) {
		fragmentText = node.transformNode(this.stylesheet);
		element.innerHTML = fragmentText;
	}
	*/
	else {
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
