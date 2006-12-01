<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>

<%@ page isELIgnored="false"%>
<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%--
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>
<c:set var="form" value="${mapviewerForm}"/>
<c:set var="minx" value="${form.map.minx}"/>
<c:set var="maxx" value="${form.map.maxx}"/>
<c:set var="miny" value="${form.map.miny}"/>
<c:set var="maxy" value="${form.map.maxy}"/>
<html:form action="/mapviewer">
    <html:hidden property="minx"/>   
    <html:hidden property="miny"/>
    <html:hidden property="maxx"/>
    <html:hidden property="maxy"/>
</html:form>
  
<SCRIPT TYPE="text/javascript" LANGUAGE="JavaScript1.2" src="box2.js"></SCRIPT>
<SCRIPT TYPE="text/javascript" LANGUAGE="JavaScript1.2" src="line.js"></SCRIPT>
<SCRIPT TYPE="text/javascript" LANGUAGE="JavaScript1.2" src="event.js"></SCRIPT>
<SCRIPT TYPE="text/javascript" LANGUAGE="JavaScript1.2" src="envelope.js"></SCRIPT>
<SCRIPT TYPE="text/javascript" LANGUAGE="JavaScript1.2" src="geotransform.js"></SCRIPT>
<SCRIPT TYPE="text/javascript" LANGUAGE="JavaScript1.2" src="layergroup.js"></SCRIPT>
<SCRIPT TYPE="text/javascript" LANGUAGE="JavaScript1.2" src="htmllayer.js"></SCRIPT>
<SCRIPT TYPE="text/javascript" LANGUAGE="JavaScript1.2" src="layerutils.js"></SCRIPT>
<SCRIPT TYPE="text/javascript" LANGUAGE="JavaScript1.2" src="mapview.js"></SCRIPT>
<SCRIPT TYPE="text/javascript" LANGUAGE="JavaScript1.2" src="mapcontroller.js"></SCRIPT>
<SCRIPT TYPE="text/javascript" LANGUAGE="JavaScript1.2" src="mapmodel.js"></SCRIPT>
<SCRIPT TYPE="text/javascript" LANGUAGE="JavaScript1.2" src="wmsrequestfactory.js"></SCRIPT>
<SCRIPT TYPE="text/javascript" LANGUAGE="JavaScript1.2" src="wmslayer.js"></SCRIPT>
<SCRIPT TYPE="text/javascript" LANGUAGE="JavaScript1.2" src="wz_jsgraphics.js"></SCRIPT>
<SCRIPT TYPE="text/javascript" LANGUAGE="JavaScript1.2" src="layerlist.js"></SCRIPT>
<SCRIPT TYPE="text/javascript" LANGUAGE="JavaScript1.2" src="mapoverview.js"></SCRIPT>
<SCRIPT TYPE="text/javascript" LANGUAGE="JavaScript1.2" src="scalebar.js"></SCRIPT>
<SCRIPT TYPE="text/javascript" LANGUAGE="JavaScript1.2">
var controller = new Controller();
controller.init();

var zoomFactor = 50;
var panFactor = 50;

//op 2 plaatsen staan deze waarden nog hardgecodeerd erin!
var screenWidth = 566;
var screenHeight = 506;

function Controller() {
	this.mode = null;
	this.init = init;
	
	this.mapModel = null;
	this.initMapModel = initMapModel;
	
	this.vMapView = null;
	this.initMapView = initMapView;
	
	this.vMapOverview = null;
	this.initMapOverview = initMapOverview;
	
	// object for transforming map coordinated to pixel coordinates
	// and vice versa
	this.gtrans = null;
	this.vMapController = null;
	this.layerList = null;
	this.actionPerformed = actionPerformed;
	this.performZoom = performZoom;
	this.performDragInfo = performDragInfo;
	this.performZoomByPoint = performZoomByPoint;
	this.performZoomByRect = performZoomByRect;
	this.performFeatureInfo = performFeatureInfo;
	this.performRecenter = performRecenter;
	this.performMove = performMove;
	this.performDistance = performDistance;
	this.performScale = performScale;
	this.print = print;
	
	this.repaint = repaint;
	this.addLayersToModel = addLayersToModel;
	
	this.setMapScreenCoords = setMapScreenCoords;

	/*
	* adds a list of layers served by one WMS to the map model. the passed layer
	* parameter is an associative array with one field for each layer. each layer
	* contains 'name', 'title', 'queryable'
	*/
	function addLayersToModel(wmsname, url, version, layers, format) {		
		var wmslayer  = new Array();
		var lay = null;
		
		for (var i = 0; i < layers.length; i++) {
			lay = new WMSLayer( wmsname, layers[i]['name'],  layers[i]['title'], layers[i]['abstract'], 'default', null, false, false, layers[i]['queryable'] );
			wmslayer.push( lay );
		}
		
		var lg = new LayerGroup(wmsname, 'OGC:WMS ' + version,  wmsname, url, wmslayer);
		lg.format=format;
		var ll = this.mapModel.getLayerList();
		ll.addLayerGroup( lg );
		this.mapModel.setChanged( true );
		this.repaint();
	}
	
	function actionPerformed(event) {
		
		this.initDragBox(screenWidth,screenHeight, 0,0,'#FF0000');
		this.initDistanceLine(screenWidth,screenHeight, 0,0,'#00FF00');
		
		if ( event.name == 'PAN' ) {
			this.vMapController.pan( event.value, panFactor);
			this.repaint();
		} else if ( event.name == 'BOX' &&  this.mode == 'draginfo' ) {
			this.performDragInfo(event);
		} else if ( event.name == 'BOX' &&  (this.mode == 'zoomin' || this.mode == 'zoomout') ) {
			this.performZoom(event);
		}  else if ( event.name == 'BOX' &&  (this.mode == 'featureinfo') ) {
			this.performFeatureInfo(event);
		} else if ( event.name == 'BOX' &&  (this.mode == 'recenter') ) {
			this.performRecenter(event);
			this.repaint();
		}  else if ( event.name == 'BOX' &&  (this.mode == 'distance')  ) {
			this.performDistance( event );
		}  else if ( event.name == 'BBOX' ) {
			this.mapModel.setBoundingBox( event.value );
			this.repaint();
		}  else if ( event.name == 'MOVE' ) {
			this.performMove( event );
			this.repaint();
		} else {
			alert('unknown event: ' + event.name); 
		};
	}
	
	function performDragInfo(event) {
            var env = event.value;
	    var mnx = this.gtrans.getSourceX(env.minx);
	    var mxy = this.gtrans.getSourceY(env.miny);
	    var mxx = this.gtrans.getSourceX(env.maxx);
	    var mny = this.gtrans.getSourceY(env.maxy);
	    JDragInfo.getSelectedElementIds(mxx,mxy,mnx,mny,{callback:getSelectedElementsIdsCallback});
	    
	}
	
	var selectedRowElements;
	
	function getSelectedElementsIdsCallback(str) {
		//if elements are selected by the map deselect them.
	    if (selectedRowElements && selectedRowElements.length > 0) {
	        for (var i=0; i < selectedRowElements.length; i++){
	            selectedRowElements[i].style.backgroundColor="";
	        }
	    }
	    
	    var newRowElements = new Array(str.length);
	    
	    for (var i = 0; i < str.length; i++) {
	        newRowElements[i]= document.getElementById("row" + str[i]);
	        if (newRowElements[i]) {
	           newRowElements[i].style.backgroundColor="#006633";
	        }
	    }
	    selectedRowElements = newRowElements;
	}
	
	function performZoom(event) {
		var env = event.value;
		
		if ( (env.maxx - env.minx < 5 && env.maxy - env.miny < 5) || this.mode == 'zoomout' ) {
			this.performZoomByPoint( env );
		} else {
			this.performZoomByRect( env );
		}
		
		this.clearDragBox();
		this.repaint();
	}
	
	function performZoomByPoint(envelope) {
		var level = zoomFactor; 
		
		if (this.mode == 'zoomin') {
			level = level * -1;
		}
		
		//level = level < 0 ? level/100.0 + 1.0 : 1.0 / (1.0 - (level/100.0)); 
		this.vMapController.zoomByPoint( level, this.gtrans.getSourceX(envelope.minx),
		this.gtrans.getSourceY(envelope.miny) );
	}
	
	function performZoomByRect(envelope) {
		var mnx = this.gtrans.getSourceX(envelope.minx);
		var mxy = this.gtrans.getSourceY(envelope.miny);
		var mxx = this.gtrans.getSourceX(envelope.maxx);
		var mny = this.gtrans.getSourceY(envelope.maxy);
		env = new Envelope( mnx, mny, mxx, mxy );
		this.mapModel.setBoundingBox( env );
		this.mapModel = this.vMapController.ensureAspectRatio();
	}
	
	function performFeatureInfo(event) {
		alert("Not implemented!");
		return;
		
		var fact = new WMSRequestFactory();
		var lgs = this.mapModel.getLayerList().getLayerGroups();
		var fi = false;
		var s = null;
		
		for ( var i = 0; i < lgs.length; i++) {
			s = fact.createGetFeatureInfoRequest( lgs[i], this.mapModel, "application/vnd.ogc.gml", event.value.minx, event.value.miny, null);
			
			if (s != null) {
				fi = true;
			}
		}
		
		if(!fi) {
			alert("U dient eerst een kaart te selecteren voordat u info bij een object kunt opvragen.");
			return;
		}
		
		var iframe;
		
		if(document.getElementById) {
			iframe = document.getElementById("FeatureDetails");
		} else {
			iframe = document.all["FeatureDetails"];
		}
		
		iframe.src = "../WMSFeatureInfoServlet?request=" + encodeURIComponent(s);
	}
	
	function performRecenter(event) {
		var env = event.value;
		var mEnv = this.mapModel.getBoundingBox();
		var level = 1;
		this.vMapController.zoomByPoint( level, this.gtrans.getSourceX(env.minx), this.gtrans.getSourceY(env.miny) );
	}
	
	function performMove(event) {
		var env = event.value;  
		var mEnv = this.mapModel.getBoundingBox();
		var xmin = this.gtrans.getSourceX( -1*env[0] ) ;
		var ymax = this.gtrans.getSourceY( -1*env[1] ) ;
		var bb = new Envelope( xmin, ymax-(mEnv.maxy-mEnv.miny), xmin+ (mEnv.maxx-mEnv.minx), ymax );
		this.mapModel.setBoundingBox( bb );
		this.repaint();
	}
	
	function performScale() {
		var sb = new Scalebar(200, this.mapModel);
		var elemI = document.getElementById( "SCALEBAR" );
	    elemI.setAttribute("width", sb.getBarPixWidth());
	    var elemA = document.getElementById( "IDSCALE" );
	    var n = elemA.firstChild.nodeValue.length;
	    elemA.firstChild.deleteData( 0, n );
	    elemA.firstChild.insertData( 0, sb.getLabel() ) ;
	}
	
	function performDistance( event ) {
		var env = event.value;
		var mnx = this.gtrans.getSourceX(env.minx);
		var mxy = this.gtrans.getSourceY(env.miny);
		var mxx = this.gtrans.getSourceX(env.maxx);
		var mny = this.gtrans.getSourceY(env.maxy);
		
		var distance = Math.sqrt((mxx-mnx)*(mxx-mnx) + (mxy-mny)*(mxy-mny));
		//alert("afstand: " + distance);
		//alert("mnx, mxx, mny, mxy: " + mnx + ", " + mxx + ", " + mny + ", " + mxy + ", ");
		
		var elemA = document.getElementById( "IDDISTANCE" );
		var n = elemA.firstChild.nodeValue.length;
		elemA.firstChild.deleteData( 0, n );
		
		var digits = 1;
		
		var s = "" + distance
		var p = s.indexOf( "." );
		s = s.substring( 0, p+digits+1 );
		elemA.firstChild.insertData( 0, s ) ;
		this.repaint();	
	}
	
	function setMapScreenCoords( x, y ) {
		x = this.gtrans.getSourceX( x );
		y = this.gtrans.getSourceY( y );
		var elemX = document.getElementById( "IDXCOORD" );
		var elemY = document.getElementById( "IDYCOORD" );
		
		if (elemX!=null && elemY!=null) {
			var n = elemX.firstChild.nodeValue.length;
			elemX.firstChild.deleteData( 0, n );
			
			n = elemY.firstChild.nodeValue.length;
			elemY.firstChild.deleteData( 0, n );
			
			var digits = 1;
			//alert("x, y, elemX, elemY: " + x +", "+y+", " +  elemX.firstChild.nodeValue+", " + elemY.firstChild.nodeValue);
			
			var s = "" + x
			var p = s.indexOf( "." );
			s = s.substring( 0, p+digits+1 );
			elemX.firstChild.insertData( 0, s ) ;
			
			s = "" + y
			p = s.indexOf( "." );
			s = s.substring( 0, p+digits+1 );
			elemY.firstChild.insertData( 0, s );
			
		}
	}
	
	function repaint() {
		// actualize with current map size
		var envelope = this.mapModel.getBoundingBox();
		this.gtrans = new GeoTransform(envelope.minx, envelope.miny, envelope.maxx,
		envelope.maxy, 0, 0, this.mapModel.getWidth()-1, 
		this.mapModel.getHeight()-1 );
		
		if ( this.vMapView != null ) {
			this.vMapView.repaint();
		}
		
		if ( this.vMapOverview != null ) {
			this.vMapOverview.repaint();
		}
		
		this.performScale();
		
		// form bbox updaten
		document.forms[0].minx.value=envelope.minx;
		document.forms[0].miny.value=envelope.miny; 
		document.forms[0].maxx.value=envelope.maxx; 
		document.forms[0].maxy.value=envelope.maxy;
	}
	
	function print() {
		var fact = new WMSRequestFactory();
		var lgs = this.mapModel.getLayerList().getLayerGroups();
		var s = null;
		
		for ( var i = 0; i < lgs.length; i++) {
			/* er is maar een layergroup */
			s = fact.createGetMapRequest(lgs[i], this.mapModel)
		}
		
		var framesrc = "../WMSPrintPageServlet?request=" + encodeURIComponent(s)
		var sb = new Scalebar(200, this.mapModel);
		framesrc += "&scalebarwidth=" + sb.getBarPixWidth();
		framesrc += "&scalebarlabel=" + sb.getLabel();
		
		if ( framesrc != null ) {
			var piw = window.open( framesrc ,"Print","width=600,height=500,left=0,top=0,scrollbars=yes");
			piw.focus();
		}
	}
	
	function init() {
		this.initMapModel();
		this.vMapController = new MapController(this.mapModel);
	}
	
	function initMapView(doc) {
		if ( this.vMapView  == null ) {
			this.vMapView = new MapView( this.mapModel,0 );
		}
		
		this.vMapView.paint( doc, doc.getElementsByTagName('body')[0] );
		this.performScale();
	}
	
	function initMapOverview(doc) {
		if ( this.vMapOverview  == null ) {
			this.vMapOverview = new MapOverview( './images/nl.jpg',11500,303500,283500,626500,'#ff0000',326,360 );
		}
		this.vMapOverview.paint( doc, doc.getElementsByTagName('body')[0] );
	}
	
	function initMapModel() {
		var layerGroups = new Array();
		var wmslayer  = new Array();
		var lay = null;
		var lg = null;
                <c:forEach var="nMaps" varStatus="status" items="${maps}">
                    lay = new WMSLayer( 'Kaartenbalie Kaart', '${nMaps}','${nMaps}', '', '', '', true, false, false, 0, 100000000, null, false );
                    wmslayer.push( lay );
                </c:forEach>
                
		//var lay = new WMSLayer( 'Milieukaart', '511_demo_basis', '', '', '', '', true, false, false, 0, 100000000, null, false );
		//wmslayer.push( lay );
		
		//'http://x3.b3p.nl/cgi-bin/mapserv?map=/home/mapserver/gstreet-demo.map'
		
		lg = new LayerGroup(20, 'OGC:WMS 1.1.1', 'Topografie', '/kaartenbalie/servlet/CallWMSServlet?', wmslayer);
		lg.setFormat('image/png');
		layerGroups.push( lg );
		wmslayer  = new Array();
		
		this.layerList = new LayerList( 'll1' );
		
		for (var k = 0; k < layerGroups.length; k++) {
			this.layerList.addLayerGroup(layerGroups[k]);
		}
		
		var envelope = new Envelope(<c:out value="${minx}"/>, <c:out value="${miny}"/>, <c:out value="${maxx}"/>, <c:out value="${maxy}"/>);
		//EPSG:28992
                //EPSG:4326
		this.mapModel = new MapModel(this.layerList, "EPSG:28992", envelope, 566, 506, "1");
		this.gtrans = new GeoTransform(envelope.minx, envelope.miny, envelope.maxx, envelope.maxy, 0, 0, this.mapModel.getWidth()-1, this.mapModel.getHeight()-1 );
	}
}

function clickCheckBox(p) {
	var lg = controller.layerList.getLayerGroup( 0 );
	var i1 = p.id.lastIndexOf( 'r' );
	var i2 = p.id.length;
	var li = parseInt( p.id.substring(i1+1, i2) );
	var layer = lg.getLayer( li );
	lg.setChanged(true);
	layer.setVisible( p.checked );
	//controller.repaint();
}

function pan(dir) {
	var event = new Event( null, "PAN", dir );
	controller.actionPerformed( event );
}

function clickButton(buttonName) {
	controller.initDragBox(screenWidth, screenHeight, 0,0,'#FF0000');
    controller.clearDistanceLine();
    controller.initDistanceLine(screenWidth,screenHeight, 0,0,'#00FF00');

    var env = controller.mapModel.getBoundingBox();
    var centerX = controller.gtrans.getDestX(0.5*(env.maxx + env.minx));
    var centerY = controller.gtrans.getDestY(0.5*(env.miny + env.maxy));
    var centerPoint = new Envelope(centerX, centerY, centerX, centerY);
    var event = new Event( null, "BOX", centerPoint );

    if (buttonName == 'refresh') {
        controller.repaint();
    } else  if (buttonName == 'print') {
        controller.print();
    } else  if (buttonName == 'draginfo') {
        controller.mode = 'draginfo';            
    } else  if (buttonName == 'zoomin') {
        controller.mode = 'zoomin';
        //controller.actionPerformed( event );
    } else  if (buttonName == 'zoomout') {
        controller.mode = 'zoomout';
        //controller.actionPerformed( event );
    } else  if (buttonName == 'featureinfo') {
        controller.mode = 'featureinfo';
        //controller.actionPerformed( event );
    } else  if (buttonName == 'recenter') {
        controller.mode = 'recenter';
    } else  if (buttonName == 'move') {
        controller.mode = 'move';
    } else  if (buttonName == 'distance') {
        controller.mode = 'distance';
        controller.actionPerformed( event );
    }

    switchButtonImage(buttonName);
    return false;
}

function switchButtonImage(buttonName) {
	var elemDI = document.getElementById("DRAGINFO_KNOP");
	
	if (elemDI!=null)
	elemDI.className="navunselected";
	var elemI = document.getElementById("ZOOMIN_KNOP");
	if (elemI!=null)
	elemI.className="navunselected";
	var elemO = document.getElementById("ZOOMOUT_KNOP");
	if (elemO!=null)
	elemO.className="navunselected";
	var elemF = document.getElementById("FEATUREINFO_KNOP");
	if (elemF!=null)
	elemF.className="navunselected";
	var elemR = document.getElementById("RECENTER_KNOP");
	if (elemR!=null)
	elemR.className="navunselected";
	var elemM = document.getElementById("MOVE_KNOP");
	if (elemM!=null)
	elemM.className="navunselected";
	var elemD = document.getElementById("DISTANCE_KNOP");
	if (elemD!=null)
	elemD.className="navunselected";
	
	if (buttonName == 'draginfo' && elemDI!=null) {
	    elemDI.className="navselected";
	} else  if (buttonName == 'zoomin' && elemI!=null) {
		elemI.className="navselected";
	} else  if (buttonName == 'zoomout' && elemO!=null) {
		elemO.className="navselected";
	} else  if (buttonName == 'featureinfo' && elemF!=null) {
		elemF.className="navselected";
	} else  if (buttonName == 'recenter' && elemR!=null) {
		elemR.className="navselected";
	} else  if (buttonName == 'move' && elemM!=null) {
		elemM.className="navselected";
	} else  if (buttonName == 'distance' && elemD!=null) {
		elemD.className="navselected";
	}
	return false;
}
	
function showXmlInfo(xmlInfoRequest) {
	//alert(xmlInfoRequest);
	if ( xmlInfoRequest != null ) {
		var fiw = window.open( xmlInfoRequest ,"XMLInfo","width=750,height=400,left=0,top=0,scrollbars=yes");
		fiw.focus();
	}
}

var newwindow;

function poptastic(url) {
	newwindow=window.open(url,'name','height=600,width=800,scrollbars=yes,toolbar=no');
	if (window.focus) {
		newwindow.focus()
	}
}

function initOnload() {
	clickButton('<c:out value="${mode}"/>');
}

if (window.addEventListener) {
	window.addEventListener("load", initOnload, false);
} else if (window.attachEvent) {
	window.attachEvent("onload", initOnload);
} else {
	window.onload = initOnload;
}



image1     = new Image(42, 42);
image1.src = "images/controls/zoom.gif";
image2     = new Image(42, 42);
image2.src = "images/controls/zoom2.gif";

image3     = new Image(42, 42);
image3.src = "images/controls/zoom.gif";
image4     = new Image(42, 42);
image4.src = "images/controls/zoom2.gif";

image5     = new Image(42, 42);
image5.src = "images/controls/zoom.gif";
image6     = new Image(42, 42);
image6.src = "images/controls/zoom2.gif";

image7     = new Image(42, 42);
image7.src = "images/controls/zoom.gif";
image8     = new Image(42, 42);
image8.src = "images/controls/zoom2.gif";

image9     = new Image(42, 42);
image9.src = "images/controls/zoom.gif";
image10     = new Image(42, 42);
image10.src = "images/controls/zoom2.gif";

image11     = new Image(42, 42);
image11.src = "images/controls/zoom.gif";
image12     = new Image(42, 42);
image12.src = "images/controls/zoom2.gif";

image13     = new Image(42, 42);
image13.src = "images/controls/zoom.gif";
image14     = new Image(42, 42);
image14.src = "images/controls/zoom2.gif";

image15     = new Image(42, 42);
image15.src = "images/controls/zoom.gif";
image16     = new Image(42, 42);
image16.src = "images/controls/zoom2.gif";




function removeClassName (elem, className) {
	elem.className = elem.className.replace(className, "").trim();
}

function addCSSClass (elem, className) {
	removeClassName (elem, className);
	elem.className = (elem.className + " " + className).trim();
}

String.prototype.trim = function() {
	return this.replace( /^\s+|\s+$/, "" );
}

function stripedTable() {
	if (document.getElementById && document.getElementsByTagName) {  
		var allTables = document.getElementsByTagName('table');
		if (!allTables) { return; }

		for (var i = 0; i < allTables.length; i++) {
			if (allTables[i].className.match(/[\w\s ]*scrollTable[\w\s ]*/)) {
				var trs = allTables[i].getElementsByTagName("tr");
				for (var j = 0; j < trs.length; j++) {
					removeClassName(trs[j], 'alternateRow');
					addCSSClass(trs[j], 'normalRow');
				}
				for (var k = 0; k < trs.length; k += 2) {
					removeClassName(trs[k], 'normalRow');
					addCSSClass(trs[k], 'alternateRow');
				}
			}
		}
	}
}

window.onload = function() { stripedTable(); }
</script>


<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAu0EUX6a9ZKgI6hddUjrnRhTFRfqDGOwfXAlOK-54sJyR4NNS5RSI3rpYek2BAFESjvkfbnaXY3_RUQ"
      type="text/javascript"></script>    
        <table>
            <tr>
                <td>
                    <div id="mapField">
                        <table cellpadding="0" cellspacing="0" border="0" height="440" width="500">
                            <tr>
                                <td><a href="javascript:pan('NW')"><img src="images/northWestArrow_lm.gif" border="0"/></a></td>
                                <td align="center"><a href="javascript:pan('N')"><img src="images/northArrow_lm.gif" width="476" height="17" border="0"/></a></td>
                                <td><a href="javascript:pan('NE')"><img src="images/northEastArrow_lm.gif" border="0"/></a></td>
                            </tr>
                            <tr>
                                <td><a href="javascript:pan('W')"><img src="images/westArrow_lm.gif" height="416" width="17" border="0"/></a></td>
                                <td align="center">
                                    
                                    <iframe  marginwidth="0" marginheight="0"  height="416" src="imapview.html" width="476" frameborder="0" scrolling="no"></iframe>
                                </td>
                                <td><a href="javascript:pan('E')"><img src="images/eastArrow_lm.gif" height="416" width="17" border="0"/></a></td>
                            </tr>
                            <tr>
                                <td><a href="javascript:pan('SW')"><img src="images/southWestArrow_lm.gif" border="0"/></a></td>
                                <td align="center"><a href="javascript:pan('S')"><img src="images/southArrow_lm.gif" width="476" height="17" border="0"/></a></td>
                                <td><a href="javascript:pan('SE')"><img src="images/southEastArrow_lm.gif" border="0"/></a></td>
                            </tr>
                        </table>
                    </div>
                    <div id="mapButtonBar">            
                        <a 
                            id="ZOOMIN_KNOP"
                            onMouseOver="document.images['zoomin'].src = image2.src" 
                            onMouseOut ="document.images['zoomin'].src = image1.src"
                            href="#"><img src="images/controls/zoom.gif" name="zoomin" title="zoom in door muisklik of slepen" width="29" height="29">
                        </a>
                        <a 
                            id="ZOOMOUT_KNOP"
                            onMouseOver="document.images['zoomout'].src = image4.src" 
                            onMouseOut ="document.images['zoomout'].src = image3.src"
                            href="#"><img src="images/controls/zoom.gif" name="zoomout" title="zoom uit door muisklik" width="29" height="29">
                        </a>
                        <a 
                            id="RECENTER_KNOP"
                            onMouseOver="document.images['recenter'].src = image6.src" 
                            onMouseOut ="document.images['recenter'].src = image5.src"
                            href="#"><img src="images/controls/zoom.gif" name="recenter" title="centreer kaart op muisklik" width="29" height="29">
                        </a>
                        <a 
                            id="MOVE_KNOP"
                            onMouseOver="document.images['move'].src = image8.src" 
                            onMouseOut ="document.images['move'].src = image7.src"
                            href="#"><img src="images/controls/zoom.gif" name="move" title="versleep kaart met muis" width="29" height="29">
                        </a>
                        <a 
                            id="DISTANCE_KNOP"
                            onMouseOver="document.images['distance'].src = image10.src" 
                            onMouseOut ="document.images['distance'].src = image9.src"
                            href="#"><img src="images/controls/zoom.gif" name="distance" title="bepaal afstand in kaart" width="29" height="29">
                        </a>
                        <a 
                            id="FEATUREINFO_KNOP"
                            onMouseOver="document.images['info'].src = image12.src" 
                            onMouseOut ="document.images['info'].src = image11.src"
                            href="#"><img src="images/controls/zoom.gif" name="info" title="haal info op bij object" width="29" height="29">
                        </a>
                        <a 
                            onMouseOver="document.images['refresh'].src = image14.src" 
                            onMouseOut ="document.images['refresh'].src = image13.src"
                            onclick="clickButton('refresh')"
                            href="#"><img src="images/controls/zoom.gif" name="refresh" title="ververs de kaart" width="29" height="29">
                        </a>
                        <a 
                            id="PRINT_KNOP"
                            onMouseOver="document.images['print'].src = image16.src" 
                            onMouseOut ="document.images['print'].src = image15.src"
                            href="#"><img src="images/controls/zoom.gif" name="print" title="print de kaart" width="29" height="29">
                        </a>
                        <div class="scalebar">
                            <img id="SCALEBAR" src="images/scalebar.gif" height="6" width="200">
                            <span id="IDSCALE">m</span>
                        </div>
                    </div>
                </td>
                <td rowspan="0" align="right">
                    <html:form action="/mapviewer">
                    <div class=smallTableContainer id=smallTableContainer>            
                        <TABLE class=scrollTable cellSpacing=0 cellPadding=0 width="50%" border=0>
                            <THEAD class=fixedHeader>
                                <TR>
                                    <TH><B>Beschikbare layers</B></TH>
                                </TR>
                            </THEAD>
                            <TBODY class=scrollContent>
                                <c:forEach var="nLayer" varStatus="status" items="${layerList}">
                                    <TR>
                                        <TD>
                                            <html:multibox value="${nLayer.id}" property="selectedLayers" />
                                            <c:out value="${nLayer.name}"/>
                                        </TD>
                                    </TR>
                                </c:forEach>
                                
                                <%--
                        <td>html:multibox value="${nServiceProvider.id}" property="wmsProviderSelected" /</td>
                        <td><a href="mapviewer.do?id=${nServiceProvider.id}"><c:out value="${nServiceProvider.name}"/></a></td>
                    --%>                
                            </TBODY>
                        </TABLE>
                    </div>
                    <html:submit value="Use selected map(s)" property="delete"/>
                    </html:form>
                </td>
            </tr>
            <tr>
                <td>
                    <div class=tableContainer id=tableContainer2 style="top: auto">
                        <table class=scrollTable cellSpacing=0 cellPadding=0 width="50%" border=0>
                            <thead class=fixedHeader>
                                <tr> 
                                    <th><b>Administratieve data</b></th>
                                </tr>
                            </thead>
                            <tbody class=scrollContent>
                                <tr><td>FoutmeldingentdTD></tr>
                                <tr><td>Error code 21: fout in bestand.... bla bla bla</td></tr>
                                <tr><td>Gevonden locaties in de buurt:</td></tr>
                                <tr><td>Locatie 1: buurtsuper</td></tr>
                                <tr><td>Locatie 2: bouwput</td></tr>
                                <tr><td>Gebruiker 'nando' bestaat al</td></tr>
                                <tr><td>Ongeldig postcode/adres gegeven</td></tr>
                                <tr><td>Gebruiker 'nando' opgeslagen in het system</td></tr>
                                <tr><td>Nando</td></tr>
                                <tr><td>Nando</td></tr>
                                <tr><td>Nando</td></tr>
                                <tr><td>Nando</td></tr>
                                <tr><td>Nando</td></tr>
                                <tr><td>Nando</td></tr>
                            </tbody>
                        </table>
                    </div>
                </td>
            </tr>    
        </table>
        
        <%--<c:forEach var="l" items="${layers}">
        <c:out value="${l.title}"/>
    </c:forEach>--%>
        <%-- The map--%>
    
        <%-- map control buttons --%>
    
        <!--
        <div class="mapbutton">
            
            
                
            
            <a href="#" id="ZOOMIN_KNOP" onClick="clickButton('zoomin')" onMouseOver="movr(1);return true;" onMouseOut="mout(1);return true;">
                <img name=img1 width=29 height=29 border=0 alt="zoom in door muisklik of slepen" src="images/controls/zoom_in.gif">
            </a>
            
            <a href="#" id="ZOOMIN_KNOP" onclick="clickButton('zoomin')" title="zoom in door muisklik of slepen" >zoom in</a>
        </div>
        <div class="mapbutton">
            <a href="#" id="ZOOMOUT_KNOP" onclick="clickButton('zoomout')"title="zoom uit door muisklik" />zoom uit</a>
        </div>
        <div class="mapbutton">
            <a href="#" id="RECENTER_KNOP" onclick="clickButton('recenter')" title="centreer kaart op muisklik">centreer</a>
        </div>
        <div class="mapbutton">
            <a href="#" id="MOVE_KNOP" onclick="clickButton('move')" title="versleep kaart met muis">slepen</a>
        </div>
        <div class="mapbutton">
            <a href="#" id="DRAGINFO_KNOP" onclick="clickButton('draginfo')" title="Sleep een vierkant om de data te selecteren">Info</a>
        </div>
        <div class="mapbutton">
        <a href="#" id="FEATUREINFO_KNOP" onclick="clickButton('featureinfo')" title="haal info op bij object" >info</a>
        </div>
        <div class="mapbutton">
        <a href="#" id="DISTANCE_KNOP" onclick="clickButton('distance')" title="bepaal afstand in kaart">afstand</a>
        </div>
        <div class="mapbutton">
        <a href="#" onclick="clickButton('refresh')" title="ververs kaart">ververs</a>
        </div>
        <div class="scalebar">
            <img id="SCALEBAR" src="images/scalebar.gif" height="6" width="200">
            <span id="IDSCALE">m</span>
        </div>
        -->
    </body>