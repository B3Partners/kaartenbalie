function MapView(mapModel, border) {
	
	this.changed = true;
	this.mapModel = mapModel;
	this.htmlLayer = new Array();
	this.border = border;
	this.parentNode = null;
	this.parentDoc = null;
	
	this.paint = paint;
	this.repaint = repaint;
	this.getMapModel = getMapModel;
	this.setMapModel = setMapModel;
	this.removeHTMLLayers = removeHTMLLayers;
	this.getLayerType = getLayerType;
	this.isChanged = isChanged;
    this.getHTMLLayers = getHTMLLayers;
    this.offX = this.border
    this.offY = this.border
	this.showProgress = showProgress;
	this.hideProgress = hideProgress;
	this.aktiv = null;
	
	
	
	function getMapModel() {
		return this.mapModel;
	}

	function setMapModel(mapModel) {
		this.mapModel = mapModel;
		this.changed = true;
	}

	function removeHTMLLayers(parentDocument, parentNode) {
		// remove HTML layers
		for(var i = 0; i < this.htmlLayer.length; i++){
			if (  this.htmlLayer[i].getLayerGroup().isChanged() ) {
				var id = this.htmlLayer[i].getId();
				var node = parentDocument.getElementById( id );
				if ( node != null ) {
					parentNode.removeChild( node );
				} 
			}
		}
	}
    
    function getHTMLLayers() {
        return this.htmlLayer;
    }

	function getLayerType(layerGroup) {
		var type = '';
		var st = layerGroup.getServiceType();
		if ( st.indexOf('WMS') > 0 ) {
			type = 'WMSLAYER';
		} else if ( st.indexOf('WFS') > 0 ) {
			type = 'WFSLAYER';
		} else if ( st.indexOf('WCS') > 0 ) {
			type = 'WCSLAYER';
		}
	}

	function isChanged() {
		return this.changed;
	}

	function paint(parentDocument, parentNode) {
		
		this.parentDoc = parentDocument;
		this.parentNode = parentNode;
		
		this.showProgress();

		if( this.changed  || this.mapModel.isChanged() ) {
			this.removeHTMLLayers( parentDocument, parentNode );
		} 
		var layerlist = this.mapModel.getLayerList();
		var layerGroups = layerlist.getLayerGroups();
		var tmpArr = new Array();
		for(var i = 0; i < layerGroups.length; i++){
			if( this.changed || layerGroups[i].isChanged() ) {
				var type = this.getLayerType( layerGroups[i] );
				var id = 'HTMLMapLayer:' + layerGroups[i].id;
				var tmp = new HTMLMapLayer( id , layerGroups.length - i, type, this.offY, 
				                            this.offX, layerGroups[i], mapModel );
				tmp.paint( parentDocument, parentNode );
				tmpArr.push( tmp );
			} else {
				tmpArr.push( this.htmlLayer[i] );
			}
		}        
		this.htmlLayer = tmpArr;
		this.changed = false;
		mapModel.setChanged( false );
		
		this.aktiv = window.setInterval("controller.vMapView.hideProgress()", 100);
	}

	function repaint() {
		this.paint(this.parentDoc, this.parentNode);
	}
	
	function showProgress() {		
		var x1 = this.mapModel.getWidth() / 2 -16;
		var x2 = this.mapModel.getWidth() / 2 +16;
		var y1 = this.mapModel.getHeight() / 2 -16;
		var y2 = this.mapModel.getHeight() / 2 +16;
		var layer = createLayer( this.parentDoc, "progress", "progress", x1, y1, 
							     x2, y2, 
								 1000,  null, true );
		createImage( "images/progress.gif", this.parentDoc, layer, 32 , 32, "progressIMG" );
		this.parentNode.appendChild( layer );
	}
	
	function hideProgress() {		
		var finished = true;
		
		for (var i = 0; i < this.parentDoc.images.length; ++i) {
			if ( !this.parentDoc.images[i].complete && 
			this.parentDoc.images[i].id. indexOf("overview") < 0 ) {
				 finished = false;
			}
		}

		if ( finished ) {
			removeHTMLLayer( this.parentDoc, "progress" );
			window.clearInterval(this.aktiv);
		}
	}

}