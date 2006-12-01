function MapOverview (src, minx, miny, maxx, maxy, fgColor, width, height) {
	// varaiable declaration
	this.src = src;	
	this.mapBBox = new Envelope(minx, miny, maxx, maxy); 
	this.width = width; 
	this.height = height; 
	this.fgColor = fgColor;
	this.jg = null;
	this.visibleBBox = this.mapBBox;
	this.targetDocument = null;
	this.parentElement = null;
	this.isNav = false;
        this.is5up = false;	
	this.transform = new GeoTransform( minx, miny, maxx, maxy, 0, 0, width-1, height-1 );

    // method declaration
	this.paint = paint;
	this.repaint = repaint;
	this.createImage = createImage;
	this.mouseUp = mouseUp;

	// method implementation
	function mouseUp(x,y) {
		x = this.transform.getSourceX( x );
		y = this.transform.getSourceY( y );
		parent.controller.vMapController.zoomByPoint( 0, x, y );
		parent.controller.repaint();
	}
	
	function paint(targetDocument, parentElement) {
		this.targetDocument = targetDocument;
		this.parentElement = parentElement;
		this.createImage(targetDocument, parentElement);
		this.repaint();
	}
	
	function repaint() {
		removeHTMLLayer(this.targetDocument, "id:cross");
		this.mapBBox = parent.controller.mapModel.getBoundingBox();
		var x1 = this.transform.getDestX( this.mapBBox.minx );
		var y1 = this.transform.getDestY( this.mapBBox.maxy );
		var x2 = this.transform.getDestX( this.mapBBox.maxx );
		var y2 = this.transform.getDestY( this.mapBBox.miny );
		if (y2-y1 > 15 && x2-x1 > 15) {
			var newLayer = createLayer(this.targetDocument, "left", "id:left", x1, y1, 
									   2, y2-y1, 3, this.fgColor, true );
			this.parentElement.appendChild(newLayer);		
			newLayer = createLayer(this.targetDocument, "top", "id:top", x1, y1, 
								   x2-x1+1, 2, 4, this.fgColor, true );
			this.parentElement.appendChild(newLayer);
			newLayer = createLayer(this.targetDocument, "right", "id:right", 
								   x2-1, y1, 2, y2-y1, 5, this.fgColor, true );
			this.parentElement.appendChild(newLayer);		
			newLayer = createLayer(this.targetDocument, "bottom", "id:bottom", x1, 
								   y2-1, x2-x1, 2, 6, this.fgColor, true );
			this.parentElement.appendChild(newLayer);
		} else {
			removeHTMLLayer(this.targetDocument, "id:left");
			removeHTMLLayer(this.targetDocument, "id:right");
			removeHTMLLayer(this.targetDocument, "id:top");
			removeHTMLLayer(this.targetDocument, "id:bottom");
			var newLayer = createLayer(this.targetDocument, "cross", "id:cross", 
									  x1+(x2-x1)/2-15, y1+(y2-y1)/2-15, 30, 30, 3, 
									  null, true );
			var newImage = this.targetDocument.createElement("img");
			newImage.setAttribute("id", "crossimage");
			newImage.setAttribute("src", "./images/cross.gif");
			newImage.setAttribute("border", 0);			
			newLayer.appendChild(newImage);
			this.parentElement.appendChild(newLayer);
		}
	}
	
	function createImage() {
		if ( this.targetDocument.getElementById("layerOverview") == null ) {
			var newLayer = createLayer(this.targetDocument, "layerOverview",
									   "id:layerOverview", 0, 0, this.width, this.height,
									   2, null, true );
			this.parentElement.appendChild(newLayer);
			var newImage = this.targetDocument.createElement("img");
			newImage.setAttribute("id", "overview");
			newImage.setAttribute("src", this.src);
			newImage.setAttribute("border", 0);			
			newImage.setAttribute("height", this.height);
			newImage.setAttribute("width", this.width);
			newLayer.appendChild(newImage);
		}
	}

}

