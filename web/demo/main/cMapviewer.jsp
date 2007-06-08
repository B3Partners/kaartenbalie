<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page language="java" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%--
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">



<%@ page isELIgnored="false"%>
<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/swfobject.js' module='' />"></script>
<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/simple_treeview.js' module='' />"></script>
<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/moveLayers.js' module='' />"></script>

<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/intellihelp.js' module='' />"></script>

<script language="JavaScript" type="text/javascript">
    
        var titleArray = new Array();
        titleArray[0] = "De Viewer";
        titleArray[1] = "De Layers";

        //TOOLTIP BODY TEXT
        var bodyArray = new Array();
        bodyArray[0] = "De viewer kent een aantal basis functies die hieronder uitgelegd zullen worden. Zo kan met de vier ingezoomd (+) en uitgezoomd (-) worden. Daarnaast kan er met de kaart gesleept worden (handje) en kan de afstand bepaald worden. Als laatst is het mogelijk om informatie bij de kaart op te vragen (i).";
        bodyArray[1] = "Hieronder vindt u een lijst met Layers. Als u zelf een WMS server toegevoegd heeft dan zullen er twee servers in de lijst staan die u allebei kunt gebruiken om de Kaartenbalie uit te proberen.<BR>Door de boom structuur op verschillende niveau's in en uit te klappen en een of meer kaartlagen te selecteren zullen deze kaartlagen onder in het beeld bij de volgorde verschijnen." + 
        " Hier kunt u door in deze box de kaarten te selecteren en met behulp van de twee buttons (pijl omhoog en pijl omlaag) de volgorde aan te passen, de volgorde waarop de kaartlagen over elkaar heen geprojecteerd moeten worden, aanpassen.<br>Door vervolgens op de button vernieuwen te klikken zal de viewer de geselecteerde kaarten ophalen en in de viewer tonen.";
        
        //TOOLTIP DISPLAY LINK
        var linkArray = new Array();
        linkArray[0] = "http://www.b3p.nl/";
        linkArray[1] = "http://www.b3p.nl/";

        //TOOLTIP URL
        var urlArray = new Array();
        urlArray[0] = "http://www.b3p.nl/";
        urlArray[1] = "http://www.b3p.nl/";
        
        //TOOLTIP OFFSET
        var xOffsetArray = new Array();
        xOffsetArray[0] = 10;
        xOffsetArray[1] = 10;

        var yOffsetArray = new Array();
        yOffsetArray[0] = 125;
        yOffsetArray[1] = 15;

        //TOOLTIP BOX DEFAULT WIDTH
        var toolTipDefaultWidth = 300;

        //TOOLTIP STYLING
        // 	Allows you to adjust the tooltip background color for the 
        //	roll-over and roll-off states, the font used for the tooltip,
        // 	the colors of the title and display URL links for the roll-over
        //	and roll-off states and whether or not the links should be
        // 	underlined at any point.
        //-------------- 
        var tooltipBkgColor = "#EEEEEE";
        var tooltipHighlightColor = "#FFFFE0";
        var tooltipFont = "Verdana, Arial, Helvetica";
        var tooltipTitleColorOff = "#0000DE";
        var tooltipTitleColorOn = "#0000DE";
        var tooltipURLColorOff = "#008000";
        var tooltipURLColorOn = "#008000";
        var tooltipTitleDecorationOff = "none";
        var tooltipTitleDecorationOn = "underline";
        var tooltipURLDecorationOff = "none";
        var tooltipURLDecorationOn = "underline";
        //............................................................
        // 								END EDITABLE TOOLTIP VARIABLES
       
</script>


<%-- flamingo viewer --%>
<div id="flashcontent">
    <font color="red"><strong>For some reason the Flamingo mapviewer can not be shown. Please contact the website administrator.</strong></font>
</div>
<script type="text/javascript">
    var so = new SWFObject('flamingo/flamingo.swf?config=../servlet/FlamingoConfigServlet/<c:out value="${checkedLayers}"/>extent=<c:out value="${extent}"/>', "flamingo", "500", "500", "8", "#FFFFFF");
    so.write("flashcontent");
</script>



<div id="treeContainer">
    <div class="treeHolder">
        <c:if test="${DemoActive == true}">
            <div id="tooltips" align="right">
                <div id="tooltipBox" onMouseOver="clearAdInterval();highlightAd('itxtTbl');" onMouseOut="hideAd();unHighlightAd('itxtTbl');" style="z-index:5000;position:absolute;cursor:pointer;"></div>
                <span id="link1" onMouseOver="displayAd(1);" onMouseOut="hideAd();" class="intellitextLink"><img src="<html:rewrite page='/images/siteImages/help.png' module='' />" width="20" height="20"></span>
            </div>
        </c:if>
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
                d.innerHTML=" Selecteer alles";
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
            var layers=layerstring.split(",");
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
