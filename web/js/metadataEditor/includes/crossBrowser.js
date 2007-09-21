//Auteur: Erik van de Pol

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
		debug.box.appendChild(p);
	}

}
