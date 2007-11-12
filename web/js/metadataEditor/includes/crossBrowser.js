//Auteur: Erik van de Pol

// IE definieert onderstaande niet; Vandaar de toevoeging
if (!window['Node']) {
    window.Node = new Object();
    Node.ELEMENT_NODE = 1;
    Node.ATTRIBUTE_NODE = 2;
    Node.TEXT_NODE = 3;
    Node.CDATA_SECTION_NODE = 4;
    Node.ENTITY_REFERENCE_NODE = 5;
    Node.ENTITY_NODE = 6;
    Node.PROCESSING_INSTRUCTION_NODE = 7;
    Node.COMMENT_NODE = 8;
    Node.DOCUMENT_NODE = 9;
    Node.DOCUMENT_TYPE_NODE = 10;
    Node.DOCUMENT_FRAGMENT_NODE = 11;
    Node.NOTATION_NODE = 12;
}

/*document._importNode2 = function(node, allChildren) {
	switch (node.nodeType) {
	case Node.ELEMENT_NODE:
		var newNode = document.createElement(node.nodeName);
		
		if (node.attributes && node.attributes.length > 0)
			for (var i = 0; i < node.attributes.length; i++)
				newNode.setAttribute(node.attributes[i].nodeName, node.getAttribute(node.attributes[i].nodeName));
		
		if (allChildren && node.childNodes && node.childNodes.length > 0)
			for (var i = 0; i < node.childNodes.length; i++)
				newNode.appendChild(document._importNode2(node.childNodes[i], allChildren));
		
		return newNode;
	case Node.TEXT_NODE:
	case Node.CDATA_SECTION_NODE:
	case Node.COMMENT_NODE:
		return document.createTextNode(node.nodeValue);
	}
};*/

// IE hack for lack of importNode support
document._importNode = function(node, importChildren) {
	var newNode;
	
	switch (node.nodeType) {
	case Node.ELEMENT_NODE:

		newNode = document.createElement(node.nodeName);

		for (var i = 0; i < node.attributes.length; i++) {
			if (node.attributes[i].nodeValue != null && node.attributes[i].nodeValue != '') {
				var attrName = node.attributes[i].name;

				if (attrName == "class")
					newNode.setAttribute("className", node.attributes[i].value);
				else
					newNode.setAttribute(attrName, node.attributes[i].value);
			}
		}

		if (node.style != null && node.style.cssText != null)
			newNode.style.cssText = node.style.cssText;
		
		break;
		
	case Node.TEXT_NODE:
	case Node.CDATA_SECTION_NODE:
	case Node.COMMENT_NODE:
		newNode = document.createTextNode(node.nodeValue);
		break;
	}

	if (importChildren && node.hasChildNodes()) {
		for (var child = node.firstChild; child; child = child.nextSibling) {
			newNode.appendChild(document._importNode(child, true));
		}
	}

	return newNode;
}

//Deze functies werken zowel in firefox als in ie6+

function getElementInnerText(element) {
	//debug("getElInner: " + element);
	if (element.innerText)
		return element.innerText;
	else if (element.textContent)
		return element.textContent;
	else
		alert("innerText and textContent not supported by your browser: Please use either IE or FireFox");
}

function setElementInnerText(element, value) {
	if (element.innerText)
		element.innerText = value;
	else if (element.textContent)
		element.textContent = value;
	else
		alert("innerText and textContent not supported by your browser: Please use either IE or FireFox");
}

////////////////// Events /////////////////////////
function getWindowEvent(e) {
	if (!e) {
		e = window.event;
	}
	return e;
	//onderstaande misschien netter:
	//if (window.event)
	//return window.event;
	//else
	//return e;
}

function getTarget(e) {
	var target;
	if (!e) {
		var e = window.event;
		//debug("use winevent");
	}
	if (e.target) {
		target = e.target;
		//debug("use target");
	}
	else if (e.srcElement) {
		target = e.srcElement;
		//debug("use srcElem");
	}
	//if (target.nodeType == 3) // defeat Safari bug
		//target = target.parentNode;
	//debug("window.event: " + window.event);
	//debug("target: " + target);
	return target;
}

function getKeyCode(e) {
	if (!e) {
		e = window.event;
	}
	
	if (e.keyCode) {
		return e.keyCode;
	}
	else if (e.which) {
		return e.which;
	}
	else {
		debug("no key captured: " + e + "\n\"keyCode\" and \"which\" not supported by your browser: Please use either IE or FireFox");
	}
}

function stopPropagation(e) {
	if (!e) {
		e = window.event;
	}

	e.cancelBubble = true;
	e.returnValue = false;

	if (e.stopPropagation) {
		debug("stop propagation on element: " + e);
		e.stopPropagation();
		e.preventDefault();
	}
	//return false;
}

function debug(msg) {
	if (debugMode == true) {
		if (!debug.box) {
			debug.box = document.createElement("div");
			//debug.box.class = "debug-box"; // wat kan IE wel?!?
			debug.box.setAttribute("class", "debug-box");
			/*
								   "background-color: white; " +
								   "font-family: monospace; " +
								   "border: solid black 3px; " +
								   "padding: 10px; ");*/

			var h1 = document.createElement("h1");
			//h1.style = "text-align: center;";
			h1.setAttribute("style", "text-align: center; ");			
			
			h1.appendChild(document.createTextNode("Debugging Output"));
			debug.box.appendChild(h1);
			document.body.appendChild(debug.box);
        }

		var p = document.createElement("p");
		
		p.appendChild(document.createTextNode(msg));
		p.appendChild(document.createElement("br"));		
		p.appendChild(document.createElement("br"));				
		debug.box.appendChild(p);
	}

}
