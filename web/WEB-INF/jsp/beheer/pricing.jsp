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

<div class="containerDiv" style=";">
    <h1><fmt:message="beheer.pricing.title" /></h1>
    <a href="wfspricing.do"><fmt:message="beheer.pricing.wfspricing" /></a><p>
    
    <b><fmt:message="beheer.pricing.layerlist" />:</b>
    
    <div style="height: 600px; margin: 0px; border: 0px Solid Black; padding: 0px;">
        <div id="tree" style="border: 0px none White; float: left; width: 230px; height: 550px; overflow: auto; margin: 5px;">
        </div>
        <div style="border: 0px Solid Black; float: left; margin: 5px; width: 670px; height: 550px;">
            <iframe name="pricingframe" id="pricingframe" src="editpricing.do?edit=submit&id=" style="width: 100%; height: 100%;" frameborder="0"></iframe>
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
             div.className = item.type == "layer" ? "layerLabel" : "serviceproviderLabel";
             div.style.height = '18px';
             if(item.type == "layer") {
                 if (item.name != null && item.name != '')
                 {
                
                     var popupLink = document.createElement("a");
                     popupLink.innerHTML = item.name;
                     popupLink.href='editpricing.do?edit=submit&id=' + item.id;
                     popupLink.target='pricingframe';
                     div.appendChild(popupLink);
                 }  else {
                     div.innerHTML = item.name;
                 }
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
