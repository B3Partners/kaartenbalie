function DistanceLine (layWidth, layHeight, hspc, vspc, zoomBoxColor) {

	this.iWidth = layWidth;
	this.iHeight = layHeight;
	this.hspc = hspc;
	this.vspc = vspc;
	this.zoomBoxColor = zoomBoxColor;
	this.listener = null;
	this.jg = null;
	this.mouseX=0;
    this.mouseY=0;
	this.x = 0;
	this.y = 0;
	this.zoom = false;
	this.isNav = false;
    this.is5up = false;

	// method declaration
	this.init = init;
	this.kill = kill;
	this.clear = clear;
	this.setListener = setListener;
	this.getMouse = getMouse;
	this.chkMouseUp = chkMouseUp;
	this.mapTool = mapTool;
	this.getZoomBox = getZoomBox;
	this.createLayer = createLayer;
	this.detectBrowser = detectBrowser;
    this.createImage = createImage;

	// method implementation
	function init() {
		this.kill();
		this.detectBrowser();
		this.createLayer( "distanceLayer", hspc, vspc, 51, null, true);
		this.jg = new jsGraphics( "distanceLayer" );
		this.jg.setPrintable(true);
		this.jg.setColor(zoomBoxColor);
		this.jg.setStroke(2);
		this.createLayer( "top2Layer", hspc, vspc, 101, null,false);
    }
	
	function createLayer(name, offsetx, offsety, zIdx, bgColor, visible) {
		newLayer = document.createElement("div");
		newLayer.setAttribute("id", name);
		newLayer.setAttribute("name", name);
		newLayer.style.position = "absolute";
		newLayer.style.top = offsety + 'px';
		newLayer.style.left = offsetx + 'px';
		newLayer.style.zIndex = zIdx;
		newLayer.style.width = this.iWidth;
		newLayer.style.height = this.iHeight;
		newLayer.style.overflow ='visible';
		newLayer.style.visibility = visible ? 'visible' : 'hidden';
		if ( bgColor != null ) {
			newLayer.style.backgroundColor = bgColor;
		}
        if ( name == "top2Layer" && !this.isNav ) {
			this.createImage(newLayer);
		}
		document.getElementsByTagName('body')[0].appendChild(newLayer);
	}
    
    function createImage(newLayer) {
		var newImage = document.createElement("img");
		newImage.setAttribute("id", "overviewt2");
		newImage.setAttribute("src", "images/pixel.gif");
		newImage.setAttribute("border", 0);	
		newImage.setAttribute("height", this.iHeight);
		newImage.setAttribute("width", this.iWidth);
		newLayer.appendChild(newImage);
	}
	
	function clear() {
		this.jg.clear();
	}
	
	function kill() {
		var layer = document.getElementById("distanceLayer");
		if ( layer != null ) {
			layer.parentNode.removeChild( layer );
			layer = document.getElementById("top2Layer");
			layer.parentNode.removeChild( layer );
		}
	}
	
	function detectBrowser() {	
		this.isNav = (navigator.appName.indexOf("Netscape")>=0);
		if ( !this.isNav) {
			if (navigator.appVersion.indexOf("MSIE 5")>0 ||
				navigator.appVersion.indexOf("MSIE 6")>0 ) {
				this.is5up = true;
			}
		}
	}
	
	function setListener(listener) {
		this.listener = listener;
	}

    function getMouse(xx,yy) {		
		if ( (xx <= this.iWidth) && (yy <= this.iHeight) && 
			  xx > hspc && yy > vspc && this.mouseX >= 0 && 
             this.mouseY >= 0 && xx > 5 && yy > 5) {
			this.x = xx;
			this.y = yy;
			if ( this.zoom ) {		
				var rx = this.mouseX;
				var ry = this.mouseY;
				this.jg.clear();
				this.jg.drawLine( rx, ry, xx, yy);
				this.jg.paint();
			}
		}
	}

    function chkMouseUp(e) {
		this.zoom = false;
		if ( this.listener != null && this.x > 0 && 
		     this.x < (this.iWidth) && this.y > 0 &&
			 this.y < (this.iHeight )) {
			var event = new Event( this, "BOX", this.getZoomBox() );
			this.listener.actionPerformed( event );
		}
    }

    function mapTool(x,y) {
        if ( !this.zoom ) {
            this.mouseX= x;
            this.mouseY= y;
            this.x = this.mouseX;
            this.y = this.mouseY;
        
        }
        this.zoom = true;	
    }

    function getZoomBox() {
		if ( this.x < this.mouseX ) {
			var tmp = this.x;
			this.x = this.mouseX;
			this.mouseX = tmp;
		}
		if ( this.y < this.mouseY ) {
			var tmp = this.y;
			this.y = this.mouseY;
			this.mouseY = tmp;
		}
		return new Envelope( this.mouseX, this.mouseY, this.x, this.y );
    }

}
