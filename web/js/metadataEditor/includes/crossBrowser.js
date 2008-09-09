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

// IE hack for lack of importNode support
document._importNode = function(node, importChildren) {
	if (document.importNode) // non-IE (Firefox)
		return document.importNode(node, importChildren);

	// else IE:
	
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

	//debug("Voor IE: cancelBubble");
	e.cancelBubble = true;
	e.returnValue = false;

	if (e.stopPropagation) {
		//debug("stop propagation on element: " + e);
		e.stopPropagation();
		e.preventDefault();
	}
	//return false;
}

function debug(msg) {
	if (debugMode == true) {
		if (!debug.box) {
			debug.box = document.createElement("div");

			// IE
			debug.box.setAttribute("className", "debug-box");
			// non-IE
			debug.box.setAttribute("class", "debug-box");

			var h1 = document.createElement("h1");
			
			// IE
			if (h1.style != null && h1.style.cssText != null)
				h1.style.cssText = "text-align: center; ";
			
			// non-IE
			h1.setAttribute("style", "text-align: center; ");			
			
			h1.appendChild(document.createTextNode("Debugging Output"));
			debug.box.appendChild(h1);
			document.body.appendChild(debug.box);
        }

		if (msg !== undefined) {
			if (msg.nodeType !== undefined && msg.nodeType == Node.ELEMENT_NODE) {
				debug.box.appendChild(msg);
			}
			else {
				var p = document.createElement("p");
				p.appendChild(document.createTextNode(msg));
				p.appendChild(document.createElement("br"));		
				p.appendChild(document.createElement("br"));	
				debug.box.appendChild(p);
			}
		}
	}

}

// LET OP!: Deze functie geeft niet alle informatie uit een xml document weer!!!
// (geen attributes en geen mixed elements (text en elementen door elkaar))
// Wel handig voor bepaalde debugging purposes
function debugXmlDoc(xmlDocToBeDebugged) {
	var p = document.createElement("p");
	var depth = 0;
	var stringBuffer = new StringBuffer();
	_debugXmlDoc(xmlDocToBeDebugged, stringBuffer, depth);
	p.innerHTML = stringBuffer.toString();
	debug(p);
}

function _debugXmlDoc(node, stringBuffer, depth) {
	for (var i = 0; i < node.childNodes.length; i++) {
		var childNode = node.childNodes[i];
		if (childNode.nodeType == Node.ELEMENT_NODE) {
			addSpaces(stringBuffer, depth);
			stringBuffer.append("&lt;" + childNode.nodeName + "&gt;");
			var hasChildElementNodes = false;
			for (var j = 0; j < childNode.childNodes.length; j++) {
				if (childNode.childNodes[j].nodeType == Node.ELEMENT_NODE) {
					//stringBuffer.append("*elementnode gevonden*: ");
                    hasChildElementNodes = true;
                    break;
                }
            }
			if (!hasChildElementNodes) {
                for (var k = 0; k < childNode.childNodes.length; k++) {
                    var possibleTextNode = childNode.childNodes[k];
                    if (possibleTextNode.nodeType == Node.TEXT_NODE) {
                        //stringBuffer.append("*TEXT*: ");
                        stringBuffer.append(possibleTextNode.nodeValue);
                    }
                }
			}
			else {
				stringBuffer.append("<br />");
				_debugXmlDoc(childNode, stringBuffer, depth + 1);
				addSpaces(stringBuffer, depth);
			}
			stringBuffer.append("&lt;/" + childNode.nodeName + "&gt;");
			stringBuffer.append("<br />");
		}
	}
}

// aantal spaties weergegeven bij inspringen xml doc debug.
var SPACE_DEPTH = 4;

function addSpaces(stringBuffer, depth) {
	for (var i = 0; i < depth * SPACE_DEPTH; i++)
		stringBuffer.append("&nbsp;");
}
