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


<%--
<c:if test="${not empty message}">
    <h3><c:out value="${message}"/></h3>
</c:if>

<c:if test="${not empty layersString}">--%>
    <div id="flashcontent">
        <font color="red"><strong>For some reason the Flamingo mapviewer can not be shown. Please contact the website administrator.</strong></font>
    </div>
    <script type="text/javascript">
   var so = new SWFObject('/flamingo/flamingo.swf?config=http://localhost:8084/kaartenbalie/viewer/flamingo/config.xml', "flamingo", "600", "600", "8", "#FFFFFF");
   so.write("flashcontent");
    </script>

<%--
<div style="float: right; clear: none; width: 190px; height: 600px;">
    <div>
        <html:form action="/index">
            <html:text property="zoeken" size="20" styleId="zoeken"/>
            <html:submit property="search" styleClass="knopZoek">
                <fmt:message key="button.zoeken"/>
            </html:submit>
            <html:submit property="clear" styleClass="knopZoek" onclick="javascript: document.getElementById('zoeken').value=''">
                <fmt:message key="button.reset"/>
            </html:submit>
        </html:form>
    </div>
    <div id="tree"></div>
</div>
--%>

<div id="tree"></div>
<input type="button" value="reload" onclick="javascript: reloadLayers()"/>
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
            if(item.type == "serviceprovider") {
                var DOMItemId = treeview_getDOMItemId(globalTreeOptions["tree"], item.id);        
                treeview_toggleItemChildren(DOMItemId);
            } else {}
            
        }

        function createLabel(container, item) {
            var div = document.createElement("div");
            var vink= document.createElement("input");
            vink.type="checkbox";
            vink.value="item.id";
            div.className = item.type == "serviceprovider" ? "serviceprovider" : "layer";
            container.appendChild(vink);
            if(item.type=="serviceprovider"){
                div.onclick = function() {
                     itemClick(item);
                };
            }
            else{
                 div.onclick= function(){
                    window.location.href="index.do?";
                 };
            }    

            div.appendChild(document.createTextNode(item.name));
            container.appendChild(div);
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
            window.location.ahref="/viewer/mapviewer.do?layers="+layersString
        }
    </c:if>
</script>
