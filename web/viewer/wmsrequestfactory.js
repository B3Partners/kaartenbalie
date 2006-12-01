function WMSRequestFactory() {

	this.createGetMapRequest = createGetMapRequest;
	this.createGetFeatureInfoRequest = createGetFeatureInfoRequest;
	this.createGetLegendGraphicRequest = createGetLegendGraphicRequest;

	function createGetMapRequest(layerGroup, mapModel) {

		var wmsrequest;
		var serverurl;
		var request;
		var serviceType = layerGroup.getServiceType();
		var tmp2 = serviceType.split(" ");
		var ser = tmp2[0];
		var tmp = ser.split(":");
		var service = tmp[1];
		var version = tmp2[1];
		if (this.version <= "1.0.0") {
			request = "map";
		}else {
			request = "GetMap";
		}
		var tmp = mapModel.getBoundingBox();
		var bbox = tmp.minx + ',' + tmp.miny + ',' + tmp.maxx + ',' + tmp.maxy;

		serverurl = layerGroup.getServiceURL();
                if (serverurl.indexOf('?')==-1) {
                    serverurl += "?";
                } else {
                    serverurl += "&";
                }
		wmsrequest = serverurl +
					 "SERVICE=" + service + "&VERSION=" + version +
					 "&REQUEST=" + request + "&FORMAT=" + layerGroup.getFormat() +
					 "&BBOX=" + bbox + "&TRANSPARENT=" +
					 layerGroup.getTransparency() + "&WIDTH=" + mapModel.getWidth() +
					 "&HEIGHT=" + mapModel.getHeight() + 
                                         "&BGCOLOR=" + layerGroup.getBGColor() + 
                                         "&REPORTNR=" + escape(mapModel.getReportnr()) + 
                                        "&EXCEPTIONS=application/vnd.ogc.se_inimage";
		var serviceName = layerGroup.getServiceName();
		var layers = layerGroup.getLayers();
		var elevation, time;

		var sldRef = layers[0].getSLDRef();

		if (sldRef == null){
			var layerArray = new Array();
			var styleArray = new Array();
			var k = 0;
			for(var i = layers.length-1; i >= 0; i--){
				if ( layers[i].isVisible() ) {
					layerArray[k] = layers[i].getName();
					styleArray[k++] = layers[i].getStyleName();
				}
			}
			var layName = layerArray.join(",");
			if ( layName == null || layName == "" || layName == ","){
				return null;
			}
			var style = styleArray.join(",");
			if ( style == null || style == "" || style == "," ) {
				style = '';
			}
			wmsrequest = wmsrequest + "&STYLES=" + style + "&LAYERS=" + layName;
		} else {
			wmsrequest = wmsrequest + "&SLD=" + sldRef;
		}
		wmsrequest = wmsrequest + "&SRS=" + mapModel.getSrs();
		/*
		var elevation = layerGroup.getElevation();
		if (elevation != null && elevation.length > 0 ){
			var s = '';
			for (var el = 0; el < elevation.length; el++){
				s = s + elevation[i] + "/";
			}
			s = s.substring(0, s.length-1);
			wmsrequest = wmsrequest + "&ELEVATION=" + s;
		}

		var time = layerGroup.getTime();
		if ( ( time != null ) && ( time.length > 0 ) ) {
			wmsrequest = wmsrequest + "&TIME=" + time;
		}
		alert("Maprequest: " + wmsrequest);
		*/
		return wmsrequest;
	}

	function createGetFeatureInfoRequest(layerGroup, mapModel, format, x, y) {

		var fiRequest = "";
		var request = "GetFeatureInfo";
		var wmsrequest = createGetMapRequest(layerGroup, mapModel);
        if ( wmsrequest == null ) return null;
		if (wmsrequest.search(/REQUEST=GetMap/) != -1 ){
			wmsrequest = wmsrequest.replace(/REQUEST=GetMap/, "REQUEST=GetFeatureInfo");
		}else {
			wmsrequest = wmsrequest.replace(/REQUEST=map/, "REQUEST=FeatureInfo");
		}
		var layers = layerGroup.getLayers();
		var selLayer = new Array ();
		var j = 0;
		for (var i = 0; i < layers.length; i++){
			if ( layers[i].isSelected() ) {
				selLayer[j++]= layers[i].getName();
			}
		}
		if ( selLayer.length == 0 ) return null;
		var actLayList = selLayer.join(",");
		fiRequest = wmsrequest + "&QUERY_LAYERS=" + selLayer + "&INFO_FORMAT=" + format
						+ "&X=" + x + "&Y=" + y
						+ "&FEATURE_COUNT=30";
		return fiRequest;
	}

	function createGetLegendGraphicRequest(layergroup, layer, width, height) {
		var serviceType = layergroup.getServiceType();
		var tmp2 = serviceType.split(" ");
		var ser = tmp2[0];
		var tmp = ser.split(":");
		var service = tmp[1];
		var version = tmp2[1];

		var serverurl = layergroup.getServiceURL();
        if (serverurl.indexOf('?')==-1) {
            serverurl += "?";
        } else {
            serverurl += "&";
        }
		var request = serverurl;
		if ( service == 'WMS' ) {
			request = request + 'service=' + service + '&version=' + version + '&request=GetLegendGraphic';
			request = request + '&format=image/png' + '&layer=' + layer.getName();
			request = request + '&style='+ layer.getStyleName() + '&width=' + width;
			request = request + '&height=' + height;
			if ( layer.getSLDRef() != null ) {
				request = request + '&SLD=' + layer.getSLDRef();
			}
		}
		return request;
	}

}