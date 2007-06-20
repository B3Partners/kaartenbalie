<%@include file="/templates/taglibs.jsp" %>

<%@ page isELIgnored="false"%>
<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/swfobject.js' module='' />"></script>
<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/simple_treeview.js' module='' />"></script>
<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/moveLayers.js' module='' />"></script>

<%-- flamingo viewer --%>
<div id="flashcontent">
    <font color="red"><strong>For some reason the Flamingo mapviewer can not be shown. Please contact the website administrator.</strong></font>
</div>
<script type="text/javascript">
    var so = new SWFObject('../flamingo/flamingo.swf?config=servlet/FlamingoConfigServlet/<c:out value="${checkedLayers}"/>extent=<c:out value="${extent}"/>', "flamingo", "500", "500", "8", "#FFFFFF");
    so.write("flashcontent");
</script>

<div id="treeContainer">
    <div class="treeHolder">
        <div id="tree"></div>
    </div>
    <input type="button" value="Vernieuw" onclick="reloadLayers()"/>
</div>

<div class="mapInfo">
    <div id="currentCoordinates"></div>
    <div id="currentScale"></div>
</div>

<div>
    <br /><b>Volgorde</b><br>
    Bepaal hieronder de volgorde van de kaarten.<br />Selecteer een kaart en verplaats het met de knoppen.
    <input type="button" value="/\" onclick="javascript: moveSelectedUp()"/>
    <input type="button" value="\/" onclick="javascript: moveSelectedDown()"/>
    <div id="orderLayerBox" class="orderLayerBox"></div>
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
            var vink= document.createElement("input");
            //als het item kinderen heeft dan geen vink toevoegen.
            if (!item.children){
                vink.type="checkbox";
                vink.value=item.name;
                vink.name=item.id;
                vink.id=item.id;
                vink.layerType=item.type;
                vink.className="layerVink";
                vink.onclick=function(){checkboxClick(this);};
                container.appendChild(vink);
            }
            div.className = item.type == "serviceprovider" ? "serviceproviderLabel" : "layerLabel";
            div.onclick = function() {
                itemClick(item);
            };
            div.appendChild(document.createTextNode(item.name));
            container.appendChild(div);
            if (item.children){
                var d=document.createElement("a");
                d.href="#";
                d.onclick= function(){setAllTrue(this);};
                d.selecteditem=item;
                d.innerHTML=" <img src='../images/siteImages/deselectall.jpg' title='Selecteer alles' alt='Selecteer alles' height='16' width='22' border=null>";
                d.style.marginLeft="5px";
                container.appendChild(d);
            }
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
            "expandAll": false
        });
        function reloadLayers(){
            var layersString="";
            var orderLayerBox= document.getElementById("orderLayerBox");
            var orderLayers=orderLayerBox.childNodes;
            for (var i=0; i < orderLayers.length; i++){
                if(layersString.length==0){
                    layersString+=orderLayers[i].name;
                }else{
                    layersString+="*"+orderLayers[i].name;
                }
            }                
            //haal de extent op van de mainmap en plaats die in de request
            var e=flamingo.call("mainMap", "getCurrentExtent");
            window.location.href="mapviewer.do?layers="+layersString+"&extent="+e.minx + "*" + e.miny + "*" + e.maxx + "*" + e.maxy;
        } 
        
        function setAllTrue(element){
            setAll(element,true);
            element.onclick= function(){setAllFalse(this);};
            element.innerHTML=" <img src='../images/siteImages/selectall.jpg' title='Deselecteer alles' alt='Selecteer alles' height='16' width='22' border=null>";
        }
        function setAllFalse(element){
            setAll(element,false);
            element.onclick= function(){setAllTrue(this);};
            element.innerHTML=" <img src='../images/siteImages/deselectall.jpg' title='Selecteer alles' alt='Selecteer alles' height='16' width='22' border=null>";
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
                        checkboxClick(element);
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
            var layers=layerstring.split("*");
            for (var i=0; i < layers.length; i++){
                var element=document.getElementById(layers[i]);
                if (element){
                    element.checked=true;
                    checkboxClick(element);
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
<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/enableJsFlamingo.js' module='' />">
