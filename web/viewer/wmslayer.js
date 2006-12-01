/*
 * represent one layer from a WMS
 */
function WMSLayer(wmsName, name, title, layerAbstract, styleName, SLDRef, visible, selected, queryable, minScale, maxScale, xmlInfo) {

   
	// name of the WMS the layer belongs to
	this.wmsName = wmsName;
	this.name = name;
	this.title = title;
	this.layerAbstract = layerAbstract;
	// styleName shall be is null if SLDRef isn't and vice versa
	this.styleName = styleName;
	this.sldRef = null;
	this.visible = visible;
	this.selected = selected;
	this.changed = true;
	this.queryable = queryable;
    this.minScale = minScale;
    this.maxScale = maxScale;
        
        // CvL toegevoegd 
        this.xmlInfo = xmlInfo;
	
	this.getWMSName = getWMSName;
	this.getName = getName;
	this.getTitle = getTitle;
	this.getAbstract = getAbstract;
	this.getStyleName = getStyleName;
	this.setStyleName = setStyleName;
	this.getSLDRef = getSLDRef;
	this.setSLDRef = setSLDRef;
	this.isVisible = isVisible;
	this.setVisible = setVisible;
	this.isSelected = isSelected;
	this.setSelected = setSelected;
	this.isQueryable = isQueryable;
	this.setQueryable = setQueryable;
	this.isChanged = isChanged;
    this.setChanged = setChanged;
    this.getMinScale = getMinScale; 
    this.getMaxScale = getMaxScale;
    this.getXmlInfo = getXmlInfo;
	
	function getName() {
		return this.name;
	}
	
	function getTitle() {
		return this.title;
	}
	
	function getAbstract() {
		return this.layerAbstract;
	}
	
	function getWMSName() {
		return this.wmsName;
	}
	
	function getStyleName() {
		return this.styleName;
	}

	function setStyleName(styleName) {
		this.sldRef = null;
		this.styleName = styleName;
		changed = true;
	}
	
	function getSLDRef() {
		return this.sldRef;
	}
	
	function setSLDRef(sldRef) {
		this.styleName = null;
		this.sldRef = sldRef;
		changed = true;
	}
	
	function isVisible() {
		return this.visible;
	}
	
	function setVisible(visible) {
		if ( this.visible != visible ) {
			changed = true;
		}
		this.visible = visible;		
	}
	
	function isQueryable() {
		return this.queryable;
	}
	
	function setQueryable(queryable) {
		this.queryable = queryable;	
	}
	
	function isSelected() {
		return this.selected;
	}
	
	function setSelected(selected) {
		if ( this.selected != selected ) {
			this.changed = true;
		}
		this.selected = selected;
	}
	
	function isChanged() {
		return this.changed;
	}

	function setChanged(changed) {	
		this.changed = changed;
	}
    
    function getMinScale() {
        return this.minScale;
    }
    
    function getMaxScale() {
        return this.maxScale;
    }

    function getXmlInfo() {
        return this.xmlInfo;
    }

}