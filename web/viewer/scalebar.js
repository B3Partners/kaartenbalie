/*
 * definieert een schaalstok
 * width = voorkeursbreedte van schaalstok
 * mapModel = gebruikte mapmodel
 */
function Scalebar(width, mapModel) {

        this.width = width;
	this.mapModel = mapModel;

        this.label = null;
        this.barPixWidth = null;

        this.getLabel = getLabel;
        this.getBarPixWidth = getBarPixWidth;
        this.calc = calc;

        function getLabel() {
            if (this.label == null)
                this.calc();
            if (this.label == null)
                this.label = "onbekend";
            return this.label;
        }

        function getBarPixWidth() {
            if (this.barPixWidth == null)
                this.calc();
            if (this.barPixWidth == null)
                this.barPixWidth = this.width;
            return this.barPixWidth;
        }

        function calc() {
            var mEnv = this.mapModel.getBoundingBox();
            var mnx = mEnv.minx;
            var mxx = mEnv.maxx;

            var scalePixWidth = this.width;
            var screenPixWidth = this.mapModel.getWidth(); //pixels van scherm
            var scaleRealWidth = (mxx-mnx)*scalePixWidth/screenPixWidth;
            //alert ("scaleRealWidth: " + scaleRealWidth);

            var tmpWidth = scaleRealWidth;
            var digitFactor = 1;
            while (tmpWidth>=10) {
            digitFactor *= 10;
            tmpWidth /=10;
            }
            var roundWidth = Math.round(tmpWidth) * digitFactor;
            //alert("roundWidth: " + roundWidth);
            var factor = roundWidth/scaleRealWidth;
            //alert("factor: " + factor);
                        
            this.barPixWidth = scalePixWidth*factor;

            var s = "";
            if (roundWidth>1000) {
                var s = "" + roundWidth/1000;
                s += " km";
            } else {
                s = roundWidth;
                s += " m";
            }
            this.label = s;
        }
}