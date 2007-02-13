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


<script language="JavaScript" type="text/javascript" src="<html:rewrite page='/js/intellihelp.js' module='' />"></script>

<script language="JavaScript" type="text/javascript">
        var titleArray = new Array();
        titleArray[0] = "Das ist ein \"Tooltip\"";
        titleArray[1] = "title goes here";
        titleArray[2] = "More Titles";
        titleArray[3] = "title goes here";

        //TOOLTIP BODY TEXT
        var bodyArray = new Array();
        bodyArray[0] = "nope";
        bodyArray[1] = "Hieronder staat de viewer met veel veel functies";
        bodyArray[2] = "Bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla";
        bodyArray[3] = "Bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla Bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla";
        
        //TOOLTIP DISPLAY LINK
        var linkArray = new Array();
        linkArray[0] = "www.vibrantmedia.com";
        linkArray[1] = "www.urlGoesHere.com";
        linkArray[2] = "www.vibrantmedia.com";
        linkArray[3] = "www.urlGoesHere.com";

        //TOOLTIP URL
        var urlArray = new Array();
        urlArray[0] = "http://www.landingPageURLGoesHere.com";
        urlArray[1] = "http://www.landingPageURLGoesHere.com";
        urlArray[2] = "http://www.landingPageURLGoesHere.com";
        urlArray[3] = "http://www.landingPageURLGoesHere.com";
        
        //TOOLTIP OFFSET
        var xOffsetArray = new Array();
        xOffsetArray[0] = 0;
        xOffsetArray[1] = 0;
        xOffsetArray[2] = 0;
        xOffsetArray[3] = 0;

        var yOffsetArray = new Array();
        yOffsetArray[0] = 0;
        yOffsetArray[1] = 0;
        yOffsetArray[2] = 0;
        yOffsetArray[3] = 0;

        //TOOLTIP BOX DEFAULT WIDTH
        var toolTipDefaultWidth = 500;

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


<div id="tooltipBox" onMouseOver="clearAdInterval();highlightAd('itxtTbl');" onMouseOut="hideAd();unHighlightAd('itxtTbl');" style="z-index:5000;position:absolute;cursor:pointer;"></div>
<span id="link0" onMouseOver="displayAd(0);" onMouseOut="hideAd();" class="intellitextLink">Tooltip</span>

<div id="tooltipBox" onMouseOver="clearAdInterval();highlightAd('itxtTbl');" onMouseOut="hideAd();unHighlightAd('itxtTbl');" style="z-index:5000;position:absolute;cursor:pointer;"></div>
<span id="link1" onMouseOver="displayAd(1);" onMouseOut="hideAd();" class="intellitextLink">Tooltip</span>

<div id="tooltipBox" onMouseOver="clearAdInterval();highlightAd('itxtTbl');" onMouseOut="hideAd();unHighlightAd('itxtTbl');" style="z-index:5000;position:absolute;cursor:pointer;"></div>
<span id="link2" onMouseOver="displayAd(2);" onMouseOut="hideAd();" class="intellitextLink">Tooltip</span>

<div id="tooltipBox" onMouseOver="clearAdInterval();highlightAd('itxtTbl');" onMouseOut="hideAd();unHighlightAd('itxtTbl');" style="z-index:5000;position:absolute;cursor:pointer;"></div>
<span id="link3" onMouseOver="displayAd(3);" onMouseOut="hideAd();" class="intellitextLink">Tooltip</span>


<%-- flamingo viewer --%>
<div id="flashcontent">
    <font color="red"><strong>For some reason the Flamingo mapviewer can not be shown. Please contact the website administrator.</strong></font>
</div>
<script type="text/javascript">
var so = new SWFObject('flamingo/flamingo.swf?config=../servlet/FlamingoConfigServlet/<c:out value="${checkedLayers}"/>/', "flamingo", "500", "500", "8", "#FFFFFF");
so.write("flashcontent");
</script>

<div class="treeHolder">
    <div id="tree"></div>
</div>
<input type="button" value="Vernieuw" onclick="reloadLayers()"/>
<%--
<c:if test="${not empty kaartId}">
    <div>
        <a class="link" href='download.do?kaartId=<c:out value="${kaartId}"/>'>Download kaart </a>
    </div>
</c:if>
--%>
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
                vink.value=item.id;
                vink.id=item.id;
                vink.layerType=item.type;
                vink.className="layerVink";
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
                d.onclick= function(){selectAll(this);};
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
                "collapsed": "/kaartenbalie/images/treeview/plus.gif",
                "expanded": "/kaartenbalie/images/treeview/minus.gif",
                "leaf": "/kaartenbalie/images/treeview/leaft.gif"
            },
            "saveExpandedState": true,
            "saveScrollState": true,
            "expandAll": false
        });
        function reloadLayers(){
            var layersString="";
            var treeElement=document.getElementById("tree");
            var checkboxes=treeElement.getElementsByTagName("input");
            for (var i=0; i < checkboxes.length; i++){
                if (checkboxes[i].type=="checkbox"){
                    if(checkboxes[i].checked){
                        if(layersString.length==0){
                            layersString+=checkboxes[i].value;
                        }else{
                            layersString+=","+checkboxes[i].value;
                        }
                    }
                }
            }
            window.location.href="mapviewer.do?layers="+layersString;
        }
        
        function selectAll(element){
            var item=element.selecteditem;
            if(item && item.children){
                selectAllChilds(item.children);
            }
            
        }
        function selectAllChilds(children){
            for(var i=0; i < children.length; i++){
                var element=document.getElementById(children[i].id);
                if(element){
                    element.checked="true";                    
                }
                if (children[i].children){
                    selectAllChilds(children[i].children);
                }
            }
        
        }
        //check the selected layers
        <c:if test="${not empty checkedLayers}">
            var layerstring="${checkedLayers}";
            var layers=layerstring.split(",");
            for (var i=0; i < layers.length; i++){
                var element=document.getElementById(layers[i]);
                //if found
                if (element){
                    element.checked="true";
                }
            }
        </c:if>
    </c:if>
</script>
