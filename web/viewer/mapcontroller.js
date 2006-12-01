function MapController(mapModel) {

	// variable declaration
	this.mapModel = mapModel;

	// method declaration
	this.zoomByPoint = zoomByPoint;
	this.zoom = zoom;
	this.pan = pan;
	this.zoomToFullExtent = zoomToFullExtent;
	this.ensureAspectRatio = ensureAspectRatio;

	// method implementation	
	
	function zoomByPoint(level, x, y) {
		level = level/100.0 + 1.0;
		var bbox = this.mapModel.getBoundingBox();
		var width = bbox.maxx - bbox.minx;
		var height = bbox.maxy - bbox.miny;
		width = width * level;
		height = height * level;
		var minx = x - (width/2.0);
		var maxx = x + (width/2.0);
		var miny = y - (height/2.0);
		var maxy = y + (height/2.0);
		bbox = new Envelope( minx, miny, maxx, maxy );
		this.mapModel.setBoundingBox( bbox );		
		return this.mapModel;
	}

	function zoom(level) {
		var bbox = this.mapModel.getBoundingBox();
		var width = bbox.maxx - bbox.minx;
		var height = bbox.maxy - bbox.miny;
		var cx = bbox.maxx - (width/2.0);
		var cy = bbox.maxy - (height/2.0);
		return this.zoomByPoint(level, cx, cy);
	}

	function pan(direction, level) {
		level = level/100.0;
		var bbox = this.mapModel.getBoundingBox();
		var dx = (bbox.maxx - bbox.minx) * level;
		var dy = (bbox.maxy - bbox.miny) * level;
		var minx = bbox.minx;
		var miny = bbox.miny;
		var maxx = bbox.maxx;
		var maxy = bbox.maxy;

		if ( direction.indexOf('W') > -1 ) {
			minx = minx - dx;
			maxx = maxx - dx;
		} else if ( direction.indexOf('E') > -1 ) {
			minx = minx + dx;
			maxx = maxx + dx;
		} 
		if ( direction.indexOf('S') > -1 ) {
			miny = miny - dy;
			maxy = maxy - dy;
		} else if ( direction.indexOf('N') > -1 ) {
			miny = miny + dy;
			maxy = maxy + dy;
		} 
		bbox = new Envelope( minx, miny, maxx, maxy );
		this.mapModel.setBoundingBox( bbox );
		return this.mapModel;
	}

	function zoomToFullExtent() {
		var bbox = this.mapModel.getInitialBoundingBox();
		this.mapModel.setBoundingBox( bbox );
		return this.mapModel;
	}

	function ensureAspectRatio() {
		var bbox = this.mapModel.getBoundingBox();
        var xmin = bbox.minx;
        var ymin = bbox.miny;
        var xmax = bbox.maxx;
        var ymax = bbox.maxy;
		var dx = xmax - xmin;
		var dy = ymax - ymin;
		var RR = dy/dx; // CvL aangepast 30-10-2005
		
		var W = this.mapModel.getWidth()
		var H = this.mapModel.getHeight();
		var R = H/W;
		
		if ( RR >= R ){ // CvL aangepast 30-10-2005
			var normCoords = getNormalizedCoords(dx, R, ymin, ymax);
			ymin = normCoords[0];
			ymax = normCoords[1];
		}else{
			R = W/H;
			var normCoords = getNormalizedCoords(dy, R, xmin, xmax);
			xmin = normCoords[0];
			xmax = normCoords[1];
		}
		var env = new Envelope( xmin, ymin, xmax, ymax );
		this.mapModel.setBoundingBox(env)
		return this.mapModel;
	}
	
	function getNormalizedCoords(normLen, ratio, min, max){
		var mid = (max - min)/2 + min;
		min = mid - (normLen/2)*ratio;
		max = mid + (normLen/2)*ratio;
		var newCoords = new Array(min,max);
		return newCoords;	
		
	}		


}
