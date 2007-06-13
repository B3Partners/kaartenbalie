<%@include file="/templates/taglibs.jsp" %>

<script type="text/javascript">
    function popUp(url) {
        window.open(url, 'Metadata Editor', 'width=700, height=600, resizable=yes, scrollbars=yes, location=yes');
        return false;
    }
</script>

<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/simple_treeview.js' module='' />"></script>
<div class="containerdiv" style="float: left; clear: none;">
<H1>Beheer Metadata</H1>

<b>Lijst met beschikbare layers:</b>

<div id="groupDetails" style="clear: left; padding-top: 15px;" class="containerdiv">
    <div id="treeContainerLarge">
        <div class="treeHolderLarge">
            <div id="tree"></div>
        </div>
    </div>
</div>

<script type="text/javascript">
    <c:if test = "${not empty layerList}">
        
        var root = ${layerList};
        function itemClick(item) {
            var DOMItemId = treeview_getDOMItemId(globalTreeOptions["tree"], item.id);        
            treeview_toggleItemChildren(DOMItemId);
        }

        function createLabel(container, item) {
            var div = document.createElement("div");
            div.className = item.type == "serviceprovider" ? "serviceproviderLabel" : "layerLabel";
            div.style.height = '18px';
            if(item.type != "serviceprovider") {
                var popupLink = document.createElement("a");
                popupLink.onclick = function() {
                    popUp('editmetadata.do?edit=submit&id=' + item.id);
                }
                popupLink.innerHTML = item.name;
                popupLink.href='#';
                div.appendChild(popupLink);
            } else {
                div.appendChild(document.createTextNode(item.name));
            }
            container.appendChild(div);
        }

        treeview_create({
            "id": "tree",
            "root": root,
            "rootChildrenAsRoots": true,
            "itemLabelCreatorFunction": createLabel,        
            "toggleImages": {
                "collapsed": "../images/treeview/plus.gif",
                "expanded": "../images/treeview/minus.gif",
                "leaf": "../images/treeview/leaft.gif"
            },
            "saveExpandedState": true,
            "saveScrollState": true,
            "expandAll": true
        });
        function reloadLayers(){
            var layersString="";
            var orderLayerBox= document.getElementById("orderLayerBox");
            var orderLayers=orderLayerBox.childNodes;
            for (var i=0; i < orderLayers.length; i++){
                if(layersString.length==0){
                    layersString+=orderLayers[i].name;
                }else{
                    layersString+=","+orderLayers[i].name;
                }
            }                
            //haal de extent op van de mainmap en plaats die in de request
            var e=flamingo.call("mainMap", "getCurrentExtent");
            window.location.href="mapviewer.do?layers="+layersString+"&extent="+e.minx + "," + e.miny + "," + e.maxx + "," + e.maxy;
        } 
        
        function setAllTrue(element){
            setAll(element,true);
            element.onclick= function(){setAllFalse(this);};
            element.innerHTML=" Deselecteer alles";
        }
        function setAllFalse(element){
            setAll(element,false);
            element.onclick= function(){setAllTrue(this);};
            element.innerHTML=" Selecteer alles";
        }
        
        function setAll(element,checked){
            var item=element.selecteditem;
            if(item && item.children){
                setAllChilds(item.children,checked);
            }
            
        }
        function setAllChilds(children,checked){
            for(var i=0; i < children.length; i++){
                var element=document.getElementById(children[i].id);
                if(element){
                    if (checked && element.checked){
                    }else{
                        element.checked=checked;
                        
                    }
                }
                if (children[i].children){
                    setAllChilds(children[i].children,checked);
                }
            }
        
        }
        //check the selected layers
        <c:if test="${not empty checkedLayers}">
            var layerstring="${checkedLayers}";
            var layers=layerstring.split(",");
            for (var i=0; i < layers.length; i++){
                var element=document.getElementById(layers[i]);
                if (element){
                    element.checked=true;
                    
                }
                
            }
        </c:if>
        function mainMap_onUpdateProgress(){
            setMapInfo();
        }
        function setMapInfo(){
            var e=flamingo.call("mainMap", "getCurrentExtent");
            var s=flamingo.call("mainMap", "getCurrentScale");
            
            var x=Math.round((e.minx+e.maxx)/2);
            var y=Math.round((e.miny+e.maxy)/2);
            document.getElementById("currentScale").innerHTML="Schaal= "+s;
            document.getElementById("currentCoordinates").innerHTML="X= "+x+ " Y= "+y;
        }
        
    </c:if>
</script>    
