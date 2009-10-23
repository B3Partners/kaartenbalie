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

<script type="text/javascript" src="<html:rewrite page='/js/beheerJS.js' module='' />"></script>
<script type="text/javascript" src="<html:rewrite page='/js/simple_treeview.js' module='' />"></script>

<html:form action="/mapserver">

    Klik op een mapfile om de WMS en WMF servers te importeren.

    <div id="mapFiles" class="containerdiv" style="clear: left; padding-top: 15px; height: 400px;">
    </div>

    <input type="hidden" name="selectedMap" value=""/>

    <script type="text/javascript">
        setOnload(initTreeView);

        var options = {};

        // Copied from Utils.js from M.L.
        function setOnload(func) {
            /* Dit zou eventueel ook kunnen met window.addEventListener/attachEvent,
             * maar dan verschilt natuurlijk de volgorde van meerdere events tussen IE/Firefox
             */

            var oldOnload = window.onload;
            window.onload = function() {
                if(oldOnload) {
                    oldOnload();
                }
                func();
            }
        }
        // \

        function initTreeView() {
            var root = ${mapfiles};
            options.id = "mapFiles";
            options.root = root;
            options.itemLabelCreatorFunction = createLabel,
            options.rootChildrenAsRoots = true,
            options.toggleImages = {
                "collapsed": "<html:rewrite page="/images/treeview/plus.gif" module=""/>",
                "expanded": "<html:rewrite page="/images/treeview/minus.gif" module=""/>",
                "leaf": "<html:rewrite page="/images/treeview/kaart.gif" module=""/>"
            };

            if(options.root.children.length == 0){
                options.containerNode = document.getElementById(options.id);
                options.containerNode.innerHTML = "Niets gevonden";
            }else{
                treeview_create(options);
            }
        }



        function createLabel(container, item) {
            // Label text
            container.className = "node";

            var a = document.createElement("a");
            a.href = "#";
            a.onclick = function() {
                treeItemClick(item);
            };
            a.appendChild(document.createTextNode(item.title));

            container.appendChild(a);
        }

        function treeItemClick(item) {
            if(item.isChild){
                document.mapServerForm.selectedMap.value = item.id;
                alert(document.mapServerForm.selectedMap.value);
            }else{
                treeview_expandItemParents(options.id, item.id);
            }
        }
    </script>

</html:form>