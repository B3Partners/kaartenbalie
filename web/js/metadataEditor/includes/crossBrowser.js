//Auteur: Erik van de Pol

alert("hier !!!!!!!!!!!!!!!!!!");

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


////////////////// Events/////////////////////////
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
		alert("debug: key niet te capturen van: " + e);
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

function test2() {
	//niets
}