/*
 * represent a HTML layer containing a group of WMS layers assigned to a MapModel
 * id = unique ID of a HTML layer
 * zIndex = HTML layer z-Index -> vertical position of the layer
 * type = type of the layer; HIGHLIGHT, WMSLAYER, WFSLAYER ...
 */
function HTMLMapLayer(id, zIndex, type, top, left, layerGroup, mapModel) {

        this.top = top;
        this.left = left;
        
        this.id = id;
        this.layerGroup = layerGroup;
		this.mapModel = mapModel;
        this.zIndex = zIndex;
        this.type = type;
		this.targetDocument = null;

        this.getId = getId;
        this.getType = getType;
        this.getLayerGroup = getLayerGroup;
        this.setLayerGroup = setLayerGroup;
		this.getMapModel = getMapModel;
        this.setMapModel = setMapModel;
        this.paint = paint;
        this.createImage = createImage;
		this.repaint = repaint;
        
        function getId() {
                return this.id;
        }

        function getType() {
                return this.type;
        }
        
        function getLayerGroup() {
            return this.layerGroup;
        }

        function setLayerGroup(layerGroup) {
            this.layerGroup = layerGroup;
            this.layerGroup.setChanged( true )
        }
		
		function getMapModel() {
            return this.mapModel;
        }

        function setMapModel(mapModel) {
            this.mapModel = mapModel;
        }

        /*
         * 'paints' (adds) the HTML layer to the passed document/node
         */
        function paint(targetDocument, parentElement) {
				this.targetDocument = targetDocument;
                if ( this.layerGroup.isChanged() ) {
					  var fac = new WMSRequestFactory();
					  var src = fac.createGetMapRequest( layerGroup, mapModel );
					  if ( src != null ) {
						  this.createImage(src, targetDocument, parentElement);						  
					  } else {
						  var node = targetDocument.getElementById( this.id );
						  if ( node != null ) {					
							  parentElement.removeChild( node );
						  }
						  return false;
					  }
                }
				return true;
        }

        function createImage(src, targetDocument, parentElement){
              var newLayer = targetDocument.createElement("div");
              newLayer.setAttribute("id", this.id);
              newLayer.setAttribute("name", layerGroup);
              newLayer.style.position = "absolute";
              newLayer.style.top = this.top;
              newLayer.style.left = this.left;
              newLayer.style.border = "0";
              newLayer.style.zIndex = this.zIndex;
              parentElement.appendChild(newLayer);
              
              var newImage = targetDocument.createElement("img");
              newImage.setAttribute("id", "img:"+this.id);
              newImage.setAttribute("name", layerGroup.name);
              newImage.setAttribute("src", src);
              newImage.setAttribute("border", 0);
              newImage.setAttribute("width", mapModel.getWidth());
              newImage.setAttribute("height", mapModel.getHeight());
              newLayer.appendChild(newImage);
        }
		
		function repaint() {
			if ( this.layerGroup.isChanged() ) {
				var src = WMSRequestFactory.createGetMapRequest( layerGroup, mapModel );
				targetDocument.getElementById( "img:" + this.id ).src = src;
            }
		}

}