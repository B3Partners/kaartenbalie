function createLayer(parentDoc, name, id, offsetx, offsety, width, hght, zIdx, 
					 bgColor, visible) {	
	removeHTMLLayer(parentDoc, id);
	var newLayer = parentDoc.createElement("div");	
	newLayer.setAttribute("id", id);
	newLayer.setAttribute("name", name);
	newLayer.style.position = "absolute";
	newLayer.style.top = offsety + 'px';
	newLayer.style.left = offsetx + 'px';
	newLayer.style.zIndex = zIdx;
	newLayer.style.width = width + 'px';
	newLayer.style.height = hght + 'px';
	newLayer.style.overflow ='hidden';
	newLayer.style.visibility = visible ? 'visible' : 'hidden';
	if ( bgColor != null ) {
		newLayer.style.backgroundColor = bgColor;
	}

	return newLayer;
}

function createImage(src, targetDocument, layer, width, height, name ){
	var newImage = targetDocument.createElement("img");
	newImage.setAttribute("id", "imgID:"+name);
	newImage.setAttribute("name", name);
	newImage.setAttribute("src", src);
	newImage.setAttribute("border", 0);
	newImage.setAttribute("width", width );
	newImage.setAttribute("height", height );
	layer.appendChild(newImage);
	return "imgID:"+name;
}

function removeHTMLLayer(targetDocument, id) {
	removeNodeById( targetDocument, id );
}

function removeNodeById(targetDocument, id) {
	var node = targetDocument.getElementById( id );	
	if ( node != null ) {
	  node.parentNode.removeChild( node );
	}
}

function pausecomp(millis) {
	date = new Date();
	var curDate = null;
	
	do { var curDate = new Date(); }
	while(curDate-date < millis);
} 

		
