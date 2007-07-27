//Auteur: Erik van de Pol

//Deze functies werken zowel in firefox als in ie6+

function getObjInnerText(obj) {
	if (obj.innerText)
		return obj.innerText;
	else if (obj.textContent)
		return obj.textContent;
	else
		alert("innerText and textContent not supported by your browser: Please use either IE or FireFox");
}

function setObjInnerText(obj, value) {
	if (obj.innerText)
		obj.innerText = value;
	else if (obj.textContent)
		obj.textContent = value;
	else
		alert("innerText and textContent not supported by your browser: Please use either IE or FireFox");
}

function getParentElement(obj) {
	if (obj.parentElement)
		return obj.parentElement;
	else if (obj.parentNode)
		return obj.parentNode;
	else
		alert("parentElement and parentNode not supported by your browser: Please use either IE or FireFox");
}


////////////////// Events /////////////////////////
function getWindowEvent(e) {
  if (!e)
    e = window.event;
  return e;
  //onderstaande misschien netter:
  //if (window.event)
	//return window.event;
  //else
	//return e;
}

function getKeyCode(e) {
	if (!e) var e = window.event
	if (e.keyCode) code = e.keyCode;
	else if (e.which) code = e.which;
	/*
	if (window.event) {
		//alert("window.event.keyCode");
		return window.event.keyCode;
	}
	else if (e.keyCode) {
		//alert("e.keyCode");
		return e.keyCode;
	}
	else if (e.which) {
		//alert("e.which");
		return e.which;
	}
	else if (e.charCode) {
		//alert("e.charCode");
		return e.charCode;
	}*/
	else {
		debug("debug: key niet te capturen van: " + e);
	}
}

function stopPropagation(e) {
  if (!e)
    var e = window.event;
  
  e.cancelBubble = true;
  e.returnValue = false;

  if (e.stopPropagation) {
	alert(e);
    e.stopPropagation();
	e.preventDefault();
  }
  //return false;
}

/**
 * This debug function displays plain-text debugging messages in a
 * special box at the end of a document. It is a useful alternative
 * to using alert(  ) to display debugging messages.
 **/
function debug(msg) {
    // If we haven't already created a box within which to display
    // our debugging messages, then do so now. Note that to avoid
    // using another global variable, we store the box node as
    // a proprty of this function.
    if (!debug.box) {
        // Create a new <div> element
        debug.box = document.createElement("div");
        // Specify what it looks like using CSS style attributes
        debug.box.setAttribute("style", 
                               "background-color: white; " +
                               "font-family: monospace; " +
                               "border: solid black 3px; " +
                               "padding: 10px;");
        
        // Append our new <div> element to the end of the document
        document.body.appendChild(debug.box);
 
        // Now add a title to our <div>. Note that the innerHTML property is
        // used to parse a fragment of HTML and insert it into the document.
        // innerHTML is not part of the W3C DOM standard, but it is supported
        // by Netscape 6 and Internet Explorer 4 and later. We can avoid 
        // the use of innerHTML by explicitly creating the <h1> element,
        // setting its style attribute, adding a Text node to it, and 
        // inserting it into the document, but this is a nice shortcut.
        debug.box.innerHTML = "<h1 style='text-align:center'>Debugging Output</h1>";
    }
 
    // When we get here, debug.box refers to a <div> element into which
    // we can insert our debugging message.
    // First create a <p> node to hold the message.
    var p = document.createElement("p");
    // Now create a text node containing the message, and add it to the <p>
    p.appendChild(document.createTextNode(msg));
    // And append the <p> node to the <div> that holds the debugging output
    debug.box.appendChild(p);
}
