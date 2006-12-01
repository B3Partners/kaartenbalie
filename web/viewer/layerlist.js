function LayerList(id)  {
	
	// variables declariation
	this.id = id;
	this.layerGroups = new Array();
	this.changed = true;

	// method declariation
	this.addLayerGroup = addLayerGroup;
	this.insertLayerGroupAt = insertLayerGroupAt;
	this.removeLayerGroupByIndex = removeLayerGroupByIndex;
	this.removeLayerGroupById = removeLayerGroupById;
	this.swapLayerGroupOrder = swapLayerGroupOrder;
	
	this.swapLayerOrder = swapLayerOrder;
	this.getLayerGroups = getLayerGroups;
	this.getLayerGroup = getLayerGroup;
	this.isChanged = isChanged;
	this.setChanged = setChanged;
	
	
	//implementation
	function getLayerGroups() {
		return this.layerGroups;
	}
	
	function getLayerGroup(index) {
		return this.layerGroups[index];
	}
	
	function addLayerGroup(layerGroup) {
		this.layerGroups.push(layerGroup);
		this.changed = true;
	}
	
	function insertLayerGroupAt(layerGroup, index) {
		this.layerGroups.push(layerGroup);
      for (var j = this.layerGroups.length-1; j > index; j--){
         this.layerGroups[j] = this.layerGroups[j-1];
      }
      this.layerGroups[index] = layerGroup;
	  this.changed = true;
	}
	
	function removeLayerGroupByIndex(layerGroupIndex) {
		for (var j = layerGroupIndex; j < this.layerGroups.length-1; j++){
        this.layerGroups[j] = this.layerGroups[j+1];
      }
      this.layerGroups.pop();
	  this.changed = true;
	}
	
	function removeLayerGroupById(id) {
		for (var i = 0; i < this.layerGroups.length; i++){
         if(this.layerGroups[i].id == id) {
               this.removeLayerGroupByIndex(i);
               break;
         }		 
		}
		this.changed = true;
	}
	
	function swapLayerGroupOrder(layerGroupIndex1, layerGroupIndex2) {
        if ( layerGroupIndex1 >= 0 && layerGroupIndex1 < this.layerGroups.length &&
             layerGroupIndex2 >= 0 && layerGroupIndex2 < this.layerGroups.length ) {
            var tmp  = this.layerGroups[layerGroupIndex1];
            this.layerGroups[layerGroupIndex1] = this.layerGroups[layerGroupIndex2];
            this.layerGroups[layerGroupIndex2] = tmp;
            this.changed = true;
        } else if ( layerGroupIndex1 < 0 || layerGroupIndex2 < 0) {
			alert( "you can't move layer group (WMS) up" );
		} else if ( layerGroupIndex1 >= this.layerGroups.length || 
					layerGroupIndex2 >= this.layerGroups.length) {
			alert( "you can't move layer group (WMS) down" );
		}
	}
	
	function swapLayerOrder(layerIndex1, layerIndex2) {
		//TODO
	}
	
	function isChanged() {
		var c = false;
		for (var i = 0; i < this.layerGroups.length; i++) {
			c = this.layerGroups[i].isChanged();
			if ( c ) {
				break;
			}
		}
		return this.changed || c;
	}
	
	function setChanged(changed) {
		this.changed = changed;
		for (var i = 0; i < this.layerGroups.length; i++) {
			this.layerGroups[i].setChanged(changed);
		}
	}		
	
}

