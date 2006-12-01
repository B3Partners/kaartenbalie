function MapModel(layerlist, srs, boundingBox, width, height, reportnr) {
	
	this.layerlist = layerlist;
	this.srs = srs;
	this.initialBBox = boundingBox;
	this.boundingBox = boundingBox;
	this.width = width;
	this.height = height;
	this.reportnr = reportnr;
	this.changed = true;
	
	this.getLayerList = getLayerList;
	this.getSrs = getSrs;
	this.getBoundingBox = getBoundingBox;
	this.getInitialBoundingBox = getInitialBoundingBox;
	this.getWidth = getWidth;
	this.getHeight = getHeight;
	this.getReportnr = getReportnr;
	
	this.setLayerList = setLayerList;
	this.setSrs = setSrs;
	this.setBoundingBox = setBoundingBox;
	this.setWidth = setWidth;
	this.setHeight = setHeight;	
	this.setReportnr = setReportnr;
	this.isChanged = isChanged;
	this.setChanged = setChanged;
	
	function getLayerList(){
		return this.layerlist;
	}
	
	function setLayerList(layerlist){
		this.layerlist = layerlist;
		this.setChanged(true);
	}
	
	function getBoundingBox(){
		return this.boundingBox;
	}
	
	function getInitialBoundingBox(){
		return this.initialBBox;
	}
	
	function setBoundingBox(boundingBox){
		this.boundingBox = boundingBox;
		this.setChanged(true);
	}
	
	function getSrs(){
		return this.srs;
	}
	
	function setSrs(srs){
		this.srs = srs;
		this.setChanged(true);
	}
	
	function getWidth(){
		return this.width;
	}
	
	function setWidth(width){
		this.width = width;
		this.setChanged(true);
	}
	
	function getHeight() {
		return this.height;
	}
	
	function setHeight(height){
		this.height = height
		this.setChanged(true);
	}
	
	function getReportnr(){
		return this.reportnr;
	}
	
	function setReportnr(reportnr){
		this.reportnr = reportnr;
		this.setChanged(true);
	}
	
	function isChanged() {
		return this.changed || layerlist.isChanged();
	}
	
	function setChanged(changed) {
		this.changed = changed;
		layerlist.setChanged( changed );
	}
	
}