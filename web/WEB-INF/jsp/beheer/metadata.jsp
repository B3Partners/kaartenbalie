<%--
B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
for authentication/authorization, pricing and usage reporting.

Copyright 2006, 2007, 2008 B3Partners BV

This file is part of B3P Kaartenbalie.

B3P Kaartenbalie is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

B3P Kaartenbalie is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
--%>
<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<script type="text/javascript" src="<html:rewrite page='/js/simple_treeview.js' module='' />"></script>

<div class="containerdiv" style="height: 500px;">
    <h1><fmt:message key="beheer.metadata.title" /></h1>
    <div class="mdContainerDiv">
        <div id="tree" class="containerdivFloat"></div>
        
        <iframe frameborder="0" id="metadataIframe" name="metadataIframe" class="mdIframe" width="630" height="450"></iframe>
    </div>	
</div>

<div id="groupDetails" style="margin-top: 10px; height: 10px;" class="containerdiv">
    &nbsp;
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
            div.className = item.type == "layer" ? "layerLabel" : "serviceproviderLabel";
            div.style.height = '18px';
            if(item.type == "layer") {
                var downloadLink = document.createElement("a");
                downloadLink.innerHTML = "# ";
                downloadLink.href='metadata.do?get=submit&xsl=simple.xsl&id=' + item.id;
                downloadLink.target='download';
                div.appendChild(downloadLink);
                var popupLink = document.createElement("a");
                popupLink.innerHTML = item.name;
                popupLink.href='editmetadata.do?edit=submit&id=' + item.id;
                popupLink.target='metadataIframe';
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
                "collapsed": "<html:rewrite page='/images/treeview/plus.gif' module=''/>",
                "expanded": "<html:rewrite page='/images/treeview/minus.gif' module=''/>",
                "leaf": "<html:rewrite page='/images/treeview/leaft.gif' module=''/>"
            },
            "saveExpandedState": true,
            "saveScrollState": true,
            "expandAll": false
        });
    </c:if>
</script>    
